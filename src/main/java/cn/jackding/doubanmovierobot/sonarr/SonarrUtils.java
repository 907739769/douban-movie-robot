package cn.jackding.doubanmovierobot.sonarr;


import cn.jackding.doubanmovierobot.config.Config;
import cn.jackding.doubanmovierobot.pojo.HttpClientResult;
import cn.jackding.doubanmovierobot.util.HttpClientUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Jack
 * @Date 2022/8/3 18:18
 * @Version 1.0.0
 */
@Slf4j
public class SonarrUtils {

    private static final Map<String, String> headers = new HashMap<String, String>(1) {{
        put("X-Api-Key", Config.sonarrApikey);
    }};

    private static final String url = Config.sonarrUrl;

    public static HttpClientResult getAllSeries() {
        try {
            HttpClientResult result = HttpClientUtils.doGet(url + "/api/v3/series", headers, null);
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpClientResult searchSeries(String seriesName) {
        Map<String, String> params = new HashMap<String, String>(1) {{
            put("term", seriesName);
        }};
        try {
            HttpClientResult result = HttpClientUtils.doGet(url + "/api/v3/series/lookup", headers, params);
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpClientResult searchSeriesByTvdbId(String tvdbId) {
        Map<String, String> params = new HashMap<String, String>(1) {{
            put("term", "tvdb:"+tvdbId);
        }};
        try {
            HttpClientResult result = HttpClientUtils.doGet(url + "/api/v3/series/lookup", headers, params);
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpClientResult addSeries(String seriesInfo) {
        JSONObject json = JSON.parseObject(seriesInfo);
        json.put("qualityProfileId", Config.sonarrQualityProfileId);
        json.put("rootFolderPath", Config.sonarrRootFolderPath);
        json.put("addOptions", JSON.parse("{searchForSeries: true}"));
        json.put("monitored", true);
        json.put("seriesType", "Standard");
        json.put("seasonFolder", true);
        json.put("languageProfileId", Config.sonarrLanguageProfileId);
        try {
            HttpClientResult result = HttpClientUtils.doPost(url + "/api/v3/series", headers, json.toJSONString());
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }


}
