package cn.jackding.doubanmovierobot.telegram.factory;

/**
 * @Author Jack
 * @Date 2022/9/2 21:28
 * @Version 1.0.0
 */
public enum TelegramFactory {

    DoSearchDouban("/DoSearchDouban", new DoSearchDouban())


    ;
    private final String doName;
    private final TelegramDo telegramDo;

    TelegramFactory(String doName, TelegramDo telegramDo) {
        this.doName = doName;
        this.telegramDo = telegramDo;
    }

    public String getDoName() {
        return doName;
    }

    public TelegramDo getTelegramDo() {
        return telegramDo;
    }

    public static String getIt(String message) {
        for (TelegramFactory factory : TelegramFactory.values()) {
            if (factory.getDoName().equals(message)) {
                return factory.getTelegramDo().doIt();
            }
        }
        return message;
    }

}
