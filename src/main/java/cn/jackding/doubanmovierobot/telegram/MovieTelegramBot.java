package cn.jackding.doubanmovierobot.telegram;

import cn.jackding.doubanmovierobot.config.Config;
import cn.jackding.doubanmovierobot.telegram.factory.TelegramFactory;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @Author Jack
 * @Date 2022/9/2 13:59
 * @Version 1.0.0
 */
@Slf4j
@Deprecated
public class MovieTelegramBot extends TelegramLongPollingBot {

    protected MovieTelegramBot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public String getBotUsername() {
        return Config.telegramBotName;
    }

    @Override
    public String getBotToken() {
        return Config.telegramBotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(TelegramFactory.getIt(update.getMessage().getText()));
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                log.error("", e);
            }
        }
    }
}
