package cn.jackding.doubanmovierobot.telegram;

import cn.jackding.doubanmovierobot.config.Config;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
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
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        telegramBotsApi.registerBot(new MovieTelegramBot(botOptions));
    }

}
