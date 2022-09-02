package cn.jackding.doubanmovierobot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author Jack
 * @Date 2022/8/2 21:04
 * @Version 1.0.0
 */
@Component
public class Config {

    public static String doubanUserIds;

    public static String radarrUrl;

    public static String radarrApikey;

    public static String radarrRootFolderPath;

    public static String radarrQualityProfileId;

    public static String sonarrUrl;

    public static String sonarrApikey;

    public static String sonarrRootFolderPath;

    public static String sonarrQualityProfileId;

    public static String sonarrLanguageProfileId;

    public static String scheduledTaskCron;

    public static boolean telegramBotEnable;
    public static String telegramBotName;
    public static String telegramBotToken;
    public static String telegramBotProxyHost;
    public static int telegramBotProxyPort;

    @Value("${douban.userIds}")
    public void setDoubanUserIds(String doubanUserIds) {
        Config.doubanUserIds = doubanUserIds;
    }

    @Value("${radarr.url:127.0.0.1:7878}")
    public void setRadarrUrl(String radarrHost) {
        Config.radarrUrl = radarrHost;
    }

    @Value("${radarr.apikey:9c6f79a4b11f4ba79d6e50b5b808e28b}")
    public void setRadarrApikey(String radarrApikey) {
        Config.radarrApikey = radarrApikey;
    }

    @Value("${radarr.rootFolderPath:/movies}")
    public void setRadarrRootFolderPath(String radarrRootFolderPath) {
        Config.radarrRootFolderPath = radarrRootFolderPath;
    }

    @Value("${radarr.qualityProfileId:1}")
    public void setRadarrQualityProfileId(String radarrQualityProfileId) {
        Config.radarrQualityProfileId = radarrQualityProfileId;
    }

    @Value("${sonarr.url:127.0.0.1:8989}")
    public void setSonarrUrl(String sonarrHost) {
        Config.sonarrUrl = sonarrHost;
    }

    @Value("${sonarr.apikey:409c7d9926f84f93bf1a7e7dd9a28bf8}")
    public void setSonarrApikey(String sonarrApikey) {
        Config.sonarrApikey = sonarrApikey;
    }

    @Value("${sonarr.rootFolderPath:/tv}")
    public void setSonarrRootFolderPath(String sonarrRootFolderPath) {
        Config.sonarrRootFolderPath = sonarrRootFolderPath;
    }

    @Value("${sonarr.qualityProfileId:1}")
    public void setSonarrQualityProfileId(String sonarrQualityProfileId) {
        Config.sonarrQualityProfileId = sonarrQualityProfileId;
    }

    @Value("${sonarr.languageProfileId:3}")
    public void setSonarrLanguageProfileId(String sonarrLanguageProfileId) {
        Config.sonarrLanguageProfileId = sonarrLanguageProfileId;
    }

    @Value("${scheduled.task.cron}")
    public void setScheduledTaskCron(String scheduledTaskCron) {
        Config.scheduledTaskCron = scheduledTaskCron;
    }

    @Value("${telegram.bot.name}")
    public void setTelegramBotName(String telegramBotName) {
        Config.telegramBotName = telegramBotName;
    }

    @Value("${telegram.bot.token}")
    public void setTelegramBotToken(String telegramBotToken) {
        Config.telegramBotToken = telegramBotToken;
    }

    @Value("${telegram.bot.proxy.host}")
    public void setTelegramBotProxyHost(String telegramBotProxyHost) {
        Config.telegramBotProxyHost = telegramBotProxyHost;
    }

    @Value("${telegram.bot.proxy.port}")
    public void setTelegramBotProxyPort(int telegramBotProxyPort) {
        Config.telegramBotProxyPort = telegramBotProxyPort;
    }

    @Value("${telegram.bot.enable:false}")
    public void setTelegramBotEnable(boolean telegramBotEnable) {
        Config.telegramBotEnable = telegramBotEnable;
    }
}
