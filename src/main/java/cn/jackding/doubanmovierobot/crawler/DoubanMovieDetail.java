package cn.jackding.doubanmovierobot.crawler;

import cn.jackding.doubanmovierobot.config.Constant;
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

    private final Site site = Site.me().setDomain("movie.douban.com");

    @Override
    public void process(Page page) {
        String imdb = page.getHtml().selectDocument(new RegexSelector("<span class=\"pl\">IMDb:</span>([^<]+)<br>"));
        page.putField(Constant.IMDB, imdb.trim());

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "https://movie.douban.com/subject/35404023/";
        ResultItemsCollectorPipeline pipeline = new ResultItemsCollectorPipeline();
        Spider.create(new DoubanMovieDetail()).addUrl(url)
                .addPipeline(pipeline).run();
        List<ResultItems> resultItems = pipeline.getCollected();
        System.out.println(resultItems);
    }

}
