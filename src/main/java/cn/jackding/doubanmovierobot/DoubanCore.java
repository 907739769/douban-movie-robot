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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Jack
 * @Date 2022/8/3 22:23
 * @Version 1.0.0
 */
@Slf4j
public class DoubanCore {

    public void run(String doubanUserID) {
        String url = "https://movie.douban.com/people/" + doubanUserID + "/wish?start=0&sort=time&rating=all&filter=all&mode=grid";
        HttpClientResult allMovieResult = RadarrUtils.getAllMovie();
        HttpClientResult allSeries = SonarrUtils.getAllSeries();
        while (StringUtils.isNotBlank(url)) {
            ResultItemsCollectorPipeline pipeline = new ResultItemsCollectorPipeline();
            Spider.create(new Douban()).addUrl(url)
                    .addPipeline(pipeline).run();
            List<ResultItems> resultItemsList = pipeline.getCollected();
            log.info("url:[{}],resultItemsList:[{}]", url, resultItemsList);
            ResultItems resultItem = null;
            if (!CollectionUtils.isEmpty(resultItemsList)) {
                resultItem = resultItemsList.get(0);
            } else {
                log.warn(url + "未查询到任何数据");
                return;
            }

            List<String> movieUrlList = resultItem.get(Constant.MOVIE_URL_LIST);
            List<String> addDateList = resultItem.get(Constant.ADD_DATE_LIST);
            List<String> videoNameList = resultItem.get(Constant.VIDEO_NAME_LIST);
            for (int i = 0; i < movieUrlList.size(); i++) {
                String movieUrl = movieUrlList.get(i);
                String videoName = videoNameList.get(i);
                String addDate = addDateList.get(i);

                //休眠0.5秒
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    log.error("", e);
                }

                ResultItemsCollectorPipeline pipeline1 = new ResultItemsCollectorPipeline();
                Spider.create(new DoubanMovieDetail()).addUrl(movieUrl)
                        .addPipeline(pipeline1).run();
                List<ResultItems> resultItems = pipeline1.getCollected();
                log.info("url:[{}],resultItemsList:[{}]", movieUrl, resultItems);
                if (!CollectionUtils.isEmpty(resultItems)) {
                    resultItem = resultItems.get(0);
                } else {
                    log.warn(movieUrl + "未查询到任何数据1");
                    continue;
                }
                String imdb = resultItem.get(Constant.IMDB);
                String videoType = resultItem.get(Constant.VIDEO_TYPE);
                String otherName = resultItem.get(Constant.OTHER_NAME);
                if (Constant.MOVIE.equals(videoType)) {

                    if (null == allMovieResult || StringUtils.isBlank(allMovieResult.getContent())) {
                        return;
                    }

                    JSONArray allMovieJsonArray = JSONArray.parseArray(allMovieResult.getContent());
                    log.info("正在添加电影:[{}],IMDBID:[{}]", videoName, imdb);
                    if (exitMovie(imdb, allMovieJsonArray)) {
                        log.info(videoName + "已存在");
                        continue;
                    }
                    addMovie(imdb, videoName);
                } else if (Constant.SERIES.equals(videoType)) {

                    if (null == allSeries || StringUtils.isBlank(allSeries.getContent())) {
                        return;
                    }

                    JSONArray allSeriesJSONArray = JSONArray.parseArray(allSeries.getContent());

                    if (exitSeries(imdb, videoName, otherName, allSeriesJSONArray)) {
                        continue;
                    }
                    addSeries(imdb, videoName, otherName);
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
            if (StringUtils.isNotBlank(imdbid) && imdbid.equals(imdb)) {
                return true;
            }
        }
        return false;
    }

    public void addMovie(String imdb, String movieName) {
        HttpClientResult movie = RadarrUtils.searchMovie(movieName.split(" / ")[0]);

        if (null == movie || StringUtils.isEmpty(movie.getContent())) {
            log.warn("查询电影[{}]没有结果", movieName);
            return;
        }

        JSONArray movieJsonArray = JSONArray.parseArray(movie.getContent());

        String movieInfo = "";
        for (Object json : movieJsonArray) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String imdbid = json1.getString("imdbId");
            if (StringUtils.isNotBlank(imdbid) && imdbid.equals(imdb)) {
                movieInfo = json1.toJSONString();
                break;
            }
        }

        if (StringUtils.isBlank(movieInfo)) {
            log.warn("电影[{}]没有匹配到,添加失败", movieName);
            return;
        }

        if (null == RadarrUtils.addMovie(movieInfo)) {
            log.error(movieName + "电影添加失败");
        } else {
            log.info(movieName + "电影添加成功");
        }
    }

    public boolean exitSeries(String imdb, String seriesName, String otherName, JSONArray allSeries) {
        String[] seriesNames = {};
        if (StringUtils.isNotBlank(seriesName)) {
            seriesNames = seriesName.split(" / ");
        }
        String[] otherNames = {};
        if (StringUtils.isNotBlank(otherName)) {
            otherNames = otherName.split(" / ");
        }
        List<String> allNames = new ArrayList<>(Arrays.asList(seriesNames));
        allNames.addAll(Arrays.asList(otherNames));
        log.info("allNames:[{}]", allNames);
        for (Object json : allSeries) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String imdbid = json1.getString("imdbId");
            String cleantitle = json1.getString("cleanTitle");
            if ((StringUtils.isNotBlank(imdbid) && imdbid.equals(imdb)) || (StringUtils.isNotBlank(cleantitle) && matchName(cleantitle, allNames))) {
                return true;
            }
        }
        return false;
    }

    public void addSeries(String imdb, String seriesName, String otherName) {
        String[] seriesNames = {};
        if (StringUtils.isNotBlank(seriesName)) {
            seriesNames = seriesName.split(" / ");
        }
        String[] otherNames = {};
        if (StringUtils.isNotBlank(otherName)) {
            otherNames = otherName.split(" / ");
        }
        List<String> allNames = new ArrayList<>(Arrays.asList(seriesNames));
        allNames.addAll(Arrays.asList(otherNames));
        for (String a : allNames) {
            HttpClientResult series = SonarrUtils.searchSeries(formatName(a));
            if (null == series || StringUtils.isEmpty(series.getContent())) {
                log.warn("查询电视剧[{}]没有结果", seriesName);
                return;
            }
            JSONArray seriesJsonArray = JSONArray.parseArray(series.getContent());

            String seriesInfo = "";
            for (Object json : seriesJsonArray) {
                JSONObject json1 = JSON.parseObject(json.toString());
                String imdbid = json1.getString("imdbId");
                String cleantitle = json1.getString("cleanTitle");
                if ((StringUtils.isNotBlank(imdb) && imdb.equals(imdbid)) || (StringUtils.isNotBlank(cleantitle) && matchName(cleantitle, allNames))) {
                    seriesInfo = json1.toJSONString();
                    break;
                }
            }

            if (StringUtils.isBlank(seriesInfo)) {
                log.warn("电视剧[{}]没有匹配到,添加失败", seriesName);
                continue;
            }

            if (null == SonarrUtils.addSeries(seriesInfo)) {
                log.error(seriesName + "电视剧添加失败");
            } else {
                log.info(seriesName + "电视剧添加成功");
                break;
            }
        }

    }

    private String formatName(String name) {
        if (isContainChinese(name)) {
            name = name.split(" ")[0].trim();
        } else {
            name = name.split("Season")[0].trim();
        }
        return name;
    }

    private boolean matchName(String name, List<String> names) {
        for (String a : names) {
            if (name.equals(a.split("Season")[0].replaceAll("[^a-zA-Z0-9]", "").toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

}
