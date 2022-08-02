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

    public static String radarrHost;

    public static String radarrPort;

    public static String radarrApikey;

    public static String radarrRootFolderPath;

    public static String radarrQualityProfileId;

    @Value("${radarr.host:127.0.0.1}")
    public void setRadarrHost(String radarrHost) {
        Config.radarrHost = radarrHost;
    }

    @Value("${radarr.port:7878}")
    public void setRadarrPort(String radarrPort) {
        Config.radarrPort = radarrPort;
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


}
