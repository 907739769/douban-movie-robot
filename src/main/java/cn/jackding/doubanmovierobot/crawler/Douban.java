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
 * @Date 2022/8/2 9:27
 * @Version 1.0.0
 */
public class Douban implements PageProcessor {

    private final Site site = Site.me().setDomain("movie.douban.com");

    @Override
    public void process(Page page) {
        List<String> nextPageList = page.getHtml().xpath("//div[@class=\"paginator\"]/span[@class=\"next\"]/a").links().all();
        List<String> movieUrlList = page.getHtml().xpath("//li[@class=\"title\"]/a/@href").all();
        List<String> addDateList = page.getHtml().xpath("//li/span[@class=\"date\"]/text()").all();
        List<String> movieNameList = page.getHtml().xpath("//div[@class=\"item\"]/div[@class=\"info\"]/ul/li[@class=\"title\"]/a/em/text()").all();


        page.putField(Constant.NEXT_PAGE_LIST, nextPageList);
        page.putField(Constant.MOVIE_URL_LIST, movieUrlList);
        page.putField(Constant.ADD_DATE_LIST, addDateList);
        page.putField(Constant.MOVIE_NAME_LIST, movieNameList);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "https://movie.douban.com/people/ILWTFT/wish?start=0&sort=time&rating=all&filter=all&mode=grid";
        ResultItemsCollectorPipeline pipeline = new ResultItemsCollectorPipeline();
        Spider.create(new Douban()).addUrl(url)
                .addPipeline(pipeline).run();
        List<ResultItems> resultItems = pipeline.getCollected();
        System.out.println(resultItems);
    }

}
