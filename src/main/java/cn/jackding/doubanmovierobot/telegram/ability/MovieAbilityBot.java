package cn.jackding.doubanmovierobot.telegram.ability;

import cn.jackding.doubanmovierobot.DoubanMovieRobotApplication;
import cn.jackding.doubanmovierobot.config.Config;
import cn.jackding.doubanmovierobot.config.Constant;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.CREATOR;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

/**
 * @Author Jack
 * @Date 2022/9/2 23:44
 * @Version 1.0.0
 */
public class MovieAbilityBot extends AbilityBot {

    private final ResponseHandler responseHandler;

    public MovieAbilityBot(String botToken, String botUsername, DefaultBotOptions botOptions) {
        super(botToken, botUsername, botOptions);
        responseHandler = new ResponseHandler(sender, db);
    }

    @Override
    public long creatorId() {
        return Config.telegramUserId;
    }

    public Ability douban() {
        return Ability.builder()
                .name(Command.DOUBAN.getCmd())
                .info(Command.DOUBAN.getDes())
                .privacy(CREATOR)
                .locality(USER)
                .input(0)
                .action(ctx -> {
                    silent.send("开始执行豆瓣搜索", ctx.chatId());
                    new DoubanMovieRobotApplication().run();
                    silent.send("执行豆瓣搜索完成", ctx.chatId());
                })
                .build();
    }

    /**
     * 根据用户的回复搜索电影
     *
     * @return
     */
    public Ability movie() {
        return Ability.builder()
                .name(Command.MOVIE.getCmd())
                .info(Command.MOVIE.getDes())
                .privacy(CREATOR)
                .locality(USER)
                .input(0)
                .action(ctx -> silent.forceReply(Constant.MOVIE_ANSWER_MOVIE, ctx.chatId()))
                .reply((bot, upd) -> responseHandler.replyToSearchMovie(getChatId(upd), upd.getMessage().getText(),upd.getMessage().getMessageId()), Flag.REPLY//回复
                        , upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername()),//回复的是机器人
                        upd -> upd.getMessage().getReplyToMessage().hasText(), upd -> upd.getMessage().getReplyToMessage().getText().equals(Constant.MOVIE_ANSWER_MOVIE)//回复的是上面的问题
                )
                .build();
    }

    /**
     * 根据用户的回复搜索电视剧
     *
     * @return
     */
    public Ability series() {
        return Ability.builder()
                .name(Command.SERIES.getCmd())
                .info(Command.SERIES.getDes())
                .privacy(CREATOR)
                .locality(USER)
                .input(0)
                .action(ctx -> silent.forceReply(Constant.SERIES_ANSWER_SERIES, ctx.chatId()))
                .reply((bot, upd) -> responseHandler.replyToSearchSeries(getChatId(upd), upd.getMessage().getText(),upd.getMessage().getMessageId()), Flag.REPLY//回复
                        , upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername()),//回复的是机器人
                        upd -> upd.getMessage().getReplyToMessage().hasText(), upd -> upd.getMessage().getReplyToMessage().getText().equals(Constant.SERIES_ANSWER_SERIES)//回复的是上面的问题
                )
                .build();
    }

    /**
     * 根据用户的下载添加电影
     *
     * @return
     */
    public Reply replyToMovieButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (bot, upd) -> responseHandler.replyToMovieButtons(getChatId(upd), upd.getCallbackQuery().getData());
        return Reply.of(action, Flag.CALLBACK_QUERY, upd -> upd.getCallbackQuery().getMessage().hasText(), upd -> upd.getCallbackQuery().getMessage().getText().equals(Constant.CHOOSE_ONE_MOVIE));
    }

    /**
     * 根据用户的下载添加电视剧
     *
     * @return
     */
    public Reply replyToSeriesButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (bot, upd) -> responseHandler.replyToSeriesButtons(getChatId(upd), upd.getCallbackQuery().getData());
        return Reply.of(action, Flag.CALLBACK_QUERY, upd -> upd.getCallbackQuery().getMessage().hasText(), upd -> upd.getCallbackQuery().getMessage().getText().equals(Constant.CHOOSE_ONE_SERIES));
    }


}
