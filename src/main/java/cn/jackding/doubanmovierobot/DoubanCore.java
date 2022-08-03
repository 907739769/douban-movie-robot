package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.config.Constant;
import cn.jackding.doubanmovierobot.crawler.Douban;
import cn.jackding.doubanmovierobot.crawler.DoubanMovieDetail;
import cn.jackding.doubanmovierobot.pojo.HttpClientResult;
import cn.jackding.doubanmovierobot.radarr.RadarrUtils;
import cn.jackding.doubanmovierobot.sonarr.SonarrUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ResultItemsCollectorPipeline;

import java.util.List;

/**
 * @Author Jack
 * @Date 2022/8/3 22:23
 * @Version 1.0.0
 */
@Slf4j
public class DoubanCore {

    public void run(String doubanUserID) {
        String url = "https://movie.douban.com/people/" + doubanUserID + "/wish?start=0&sort=time&rating=all&filter=all&mode=grid";
        while (StringUtils.isNotBlank(url)) {
            ResultItemsCollectorPipeline pipeline = new ResultItemsCollectorPipeline();
            Spider.create(new Douban()).addUrl(url)
                    .addPipeline(pipeline).run();
            List<ResultItems> resultItemsList = pipeline.getCollected();
            ResultItems resultItem = null;
            if (!CollectionUtils.isEmpty(resultItemsList)) {
                resultItem = resultItemsList.get(0);
            } else {
                log.warn(url + "未查询到任何数据");
                return;
            }

            List<String> movieUrlList = resultItem.get(Constant.MOVIE_URL_LIST);
            List<String> addDateList = resultItem.get(Constant.ADD_DATE_LIST);
            List<String> movieNameList = resultItem.get(Constant.MOVIE_NAME_LIST);
            for (int i = 0; i < movieUrlList.size(); i++) {
                String movieUrl = movieUrlList.get(i);
                String movieName = movieNameList.get(i);
                String addDate = addDateList.get(i);

                ResultItemsCollectorPipeline pipeline1 = new ResultItemsCollectorPipeline();
                Spider.create(new DoubanMovieDetail()).addUrl(movieUrl)
                        .addPipeline(pipeline1).run();
                List<ResultItems> resultItems = pipeline1.getCollected();
                System.out.println("111"+resultItems);
                if (!CollectionUtils.isEmpty(resultItems)) {
                    resultItem = resultItems.get(0);
                } else {
                    log.warn(movieUrl + "未查询到任何数据1");
                    continue;
                }
                String imdb = resultItem.get(Constant.IMDB);
                String videoType = resultItem.get(Constant.VIDEO_TYPE);
                if (videoType.equals(Constant.MOVIE)) {
                    HttpClientResult allMovieResult = RadarrUtils.getAllMovie();
                    JSONArray allMovieJsonArray = JSONArray.parseArray(allMovieResult.getContent());
                    log.info("正在添加电影" + movieName);
                    if (exitMovie(imdb, allMovieJsonArray)) {
                        log.info(movieName + "已存在");
                        continue;
                    }
                    addMovie(imdb, movieName);
                } else if (videoType.equals(Constant.SERIES)) {
                    HttpClientResult allSeries = SonarrUtils.getAllSeries();
                    JSONArray allSeriesJSONArray = JSONArray.parseArray(allSeries.getContent());

                    if (exitSeries(imdb, movieName, allSeriesJSONArray)) {
                        continue;
                    }
                    addSeries(imdb, movieName);
                }
            }

            List<String> nextPageList = resultItem.get(Constant.NEXT_PAGE_LIST);
            if (CollectionUtils.isEmpty(nextPageList)) {
                url = "";
                log.info("用户" + doubanUserID + "豆瓣数据已查询完成");
            } else {
                url = nextPageList.get(0);
            }
        }

    }

    public boolean exitMovie(String imdb, JSONArray allMovie) {
        for (Object json : allMovie) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String imdbid = json1.getString("imdbId");
            return imdbid.equals(imdb);
        }
        return false;
    }

    public void addMovie(String imdb, String movieName) {
        HttpClientResult movie = RadarrUtils.searchMovie(movieName.split(" / ")[0]);
        JSONArray movieJsonArray = JSONArray.parseArray(movie.getContent());

        String movieInfo = "";
        for (Object json : movieJsonArray) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String imdbid = json1.getString("imdbId");
            if (imdbid.equals(imdb)) {
                movieInfo = json1.toJSONString();
                break;
            }
        }
        if (null == RadarrUtils.addMovie(movieInfo)) {
            log.error(movieName + "电影添加失败");
        } else {
            log.info(movieName + "电影添加成功");
        }
    }

    public boolean exitSeries(String imdb, String movieName, JSONArray allSeries) {
        for (Object json : allSeries) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String imdbid = json1.getString("imdbId");
            String cleantitle = json1.getString("cleanTitle");
            return imdbid.equals(imdb) || movieName.split(" / ")[1].split("Season")[0].replaceAll("[^a-zA-Z0-9]", "").equals(cleantitle);
        }
        return false;
    }

    public void addSeries(String imdb, String movieName) {
        HttpClientResult series = SonarrUtils.searchSeries(movieName.split(" / ")[0]);
        JSONArray seriesJsonArray = JSONArray.parseArray(series.getContent());

        String seriesInfo = "";
        for (Object json : seriesJsonArray) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String imdbid = json1.getString("imdbId");
            String cleantitle = json1.getString("cleanTitle");
            if (imdbid.equals(imdb) || movieName.split(" / ")[1].split("Season")[0].replaceAll("[^a-zA-Z0-9]", "").equals(cleantitle)) {
                seriesInfo = json1.toJSONString();
                break;
            }
        }
        if (null == SonarrUtils.addSeries(seriesInfo)) {
            log.error(movieName + "电视剧添加失败");
        } else {
            log.info(movieName + "电视剧添加成功");
        }
    }

}
