package cn.jackding.doubanmovierobot.telegram;

import cn.jackding.doubanmovierobot.config.Config;
import cn.jackding.doubanmovierobot.telegram.ability.MovieAbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * 机器人注册中心
 *
 * @Author Jack
 * @Date 2022/9/2 13:54
 * @Version 1.0.0
 */
public class Telegram {

    public static void registerBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        // Set up Http proxy
        DefaultBotOptions botOptions = new DefaultBotOptions();

        botOptions.setProxyHost(Config.telegramBotProxyHost);
        botOptions.setProxyPort(Config.telegramBotProxyPort);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        //使用AbilityBot创建的事件响应机器人
        telegramBotsApi.registerBot(new MovieAbilityBot(Config.telegramBotToken, Config.telegramBotName, botOptions));
    }

}
