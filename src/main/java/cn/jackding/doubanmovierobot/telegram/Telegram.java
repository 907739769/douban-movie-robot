package cn.jackding.doubanmovierobot.telegram;

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

    public void test() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        // Set up Http proxy
        DefaultBotOptions botOptions = new DefaultBotOptions();

        botOptions.setProxyHost("127.0.0.1");
        botOptions.setProxyPort(10809);
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        telegramBotsApi.registerBot(new MovieTelegramBot(botOptions));
    }

    public static void main(String[] args) throws TelegramApiException {
        new Telegram().test();
    }

}
