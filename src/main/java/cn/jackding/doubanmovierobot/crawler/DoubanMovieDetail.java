package cn.jackding.doubanmovierobot.crawler;

import cn.jackding.doubanmovierobot.config.Constant;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ResultItemsCollectorPipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.RegexSelector;

import java.util.List;

/**
 * @Author Jack
 * @Date 2022/8/2 13:37
 * @Version 1.0.0
 */
public class DoubanMovieDetail implements PageProcessor {

    private final Site site = Site.me().setDomain("movie.douban.com").setCycleRetryTimes(3);

    @Override
    public void process(Page page) {
        String imdb = page.getHtml().selectDocument(new RegexSelector("<span class=\"pl\">IMDb:</span>([^<]+)<br>"));
        String isJs = page.getHtml().selectDocument(new RegexSelector("<span class=\"pl\">集数:</span>(.*?)<br>"));
        String isPc = page.getHtml().selectDocument(new RegexSelector("<span class=\"pl\">单集片长:</span>(.*?)<br>"));
        String otherName = page.getHtml().selectDocument(new RegexSelector("<span class=\"pl\">又名:</span>(.*?)<br>"));
        page.putField(Constant.IMDB, StringUtils.isBlank(imdb) ? "" : imdb.trim());
        String videoType = Constant.MOVIE;
        if (StringUtils.isNotBlank(isJs) || StringUtils.isNotBlank(isPc)) {
            videoType = Constant.SERIES;
        }
        page.putField(Constant.VIDEO_TYPE, videoType);
        page.putField(Constant.OTHER_NAME, otherName);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "https://movie.douban.com/subject/35426353/";
        ResultItemsCollectorPipeline pipeline = new ResultItemsCollectorPipeline();
        Spider.create(new DoubanMovieDetail()).addUrl(url)
                .addPipeline(pipeline).run();
        List<ResultItems> resultItems = pipeline.getCollected();
        System.out.println(resultItems);
    }

}
