package cn.jackding.doubanmovierobot.telegram.ability;

import cn.jackding.doubanmovierobot.config.Constant;
import cn.jackding.doubanmovierobot.pojo.HttpClientResult;
import cn.jackding.doubanmovierobot.radarr.RadarrUtils;
import cn.jackding.doubanmovierobot.sonarr.SonarrUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
public class ResponseHandler {

    private final MessageSender sender;

    public ResponseHandler(MessageSender sender, DBContext db) {
        this.sender = sender;
    }

    /**
     * 根据用户的选择添加电影
     *
     * @param chatId
     * @param buttonId
     */
    public void replyToMovieButtons(long chatId, String buttonId) {
        try {
            //通过imdb id搜索电影
            HttpClientResult movie = RadarrUtils.searchMovieByImdbId(buttonId);
            if (null == movie || StringUtils.isEmpty(movie.getContent())) {
                log.info("添加电影{}失败", buttonId);
                sender.execute(SendMessage.builder().chatId(chatId).text("查询电影失败").build());
                return;
            }

            JSONObject json = JSONObject.parseObject(movie.getContent());
            if (null == RadarrUtils.addMovie(json.toJSONString())) {
                log.info("添加电影{}失败", buttonId);
                sender.execute(SendMessage.builder().chatId(chatId).text("添加电影《" + json.getString("title") + "》失败").build());
            } else {
                log.info("添加电影{}完成", buttonId);
                sender.execute(SendMessage.builder().chatId(chatId).text("添加电影《" + json.getString("title") + "》完成").build());
            }

        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 根据用户的选择添加电视剧
     *
     * @param chatId
     * @param buttonId
     */
    public void replyToSeriesButtons(long chatId, String buttonId) {
        try {
            //通过tvdb id搜索
            HttpClientResult series = SonarrUtils.searchSeriesByTvdbId(buttonId);
            if (null == series || StringUtils.isEmpty(series.getContent()) || CollectionUtils.isEmpty(JSONArray.parseArray(series.getContent()))) {
                log.info("添加电视剧{}失败", buttonId);
                sender.execute(SendMessage.builder().chatId(chatId).text("查询电视剧失败").build());
                return;
            }

            JSONArray movieJsonArray = JSONArray.parseArray(series.getContent());
            if (null == SonarrUtils.addSeries(JSON.parseObject(movieJsonArray.get(0).toString()).toJSONString())) {
                log.info("添加电视剧{}失败", buttonId);
                sender.execute(SendMessage.builder().chatId(chatId).text("添加电视剧《" + JSON.parseObject(movieJsonArray.get(0).toString()).getString("title") + "》失败").build());
            } else {
                log.info("添加电视剧{}完成", buttonId);
                sender.execute(SendMessage.builder().chatId(chatId).text("添加电视剧《" + JSON.parseObject(movieJsonArray.get(0).toString()).getString("title") + "》完成").build());
            }

        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 根据用户的关键字搜索电影
     *
     * @param chatId
     * @param movieName
     */
    public void replyToSearchMovie(long chatId, String movieName, Integer messageId) {
        try {
            HttpClientResult movie = RadarrUtils.searchMovie(movieName);
            if (null == movie || StringUtils.isEmpty(movie.getContent()) || CollectionUtils.isEmpty(JSONArray.parseArray(movie.getContent()))) {
                sender.execute(SendMessage.builder().chatId(chatId).replyToMessageId(messageId).text("没有查询到任何电影!").build());
                return;
            }

            JSONArray movieJsonArray = JSONArray.parseArray(movie.getContent());

            sender.execute(SendMessage.builder()
                    .replyToMessageId(messageId)
                    .text(Constant.CHOOSE_ONE_MOVIE)
                    .chatId(chatId)
                    .replyMarkup(KeyboardFactory.movieButtons(movieJsonArray)).build());

        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 根据用户的关键字搜索电视剧
     *
     * @param chatId
     * @param seriesName
     */
    public void replyToSearchSeries(long chatId, String seriesName, Integer messageId) {
        try {
            HttpClientResult series = SonarrUtils.searchSeries(seriesName);
            if (null == series || StringUtils.isEmpty(series.getContent()) || CollectionUtils.isEmpty(JSONArray.parseArray(series.getContent()))) {
                sender.execute(SendMessage.builder().chatId(chatId).replyToMessageId(messageId).text("没有查询到任何电视剧!").build());
                return;
            }

            JSONArray seriesJsonArray = JSONArray.parseArray(series.getContent());

            sender.execute(SendMessage.builder()
                    .replyToMessageId(messageId)
                    .text(Constant.CHOOSE_ONE_SERIES)
                    .chatId(chatId)
                    .replyMarkup(KeyboardFactory.seriesButtons(seriesJsonArray)).build());

        } catch (Exception e) {
            log.error("", e);
        }
    }


}
