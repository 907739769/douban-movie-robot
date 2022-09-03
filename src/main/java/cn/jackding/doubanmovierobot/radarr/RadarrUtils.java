package cn.jackding.doubanmovierobot.radarr;

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
 * @Date 2022/8/2 21:03
 * @Version 1.0.0
 */
@Slf4j
public class RadarrUtils {

    private static final Map<String, String> headers = new HashMap<String, String>(1) {{
        put("X-Api-Key", Config.radarrApikey);
    }};

    private static final String url = Config.radarrUrl;

    public static HttpClientResult getAllMovie() {
        try {
            HttpClientResult result = HttpClientUtils.doGet(url + "/api/v3/movie", headers, null);
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpClientResult searchMovie(String movieName) {
        Map<String, String> params = new HashMap<String, String>(1) {{
            put("term", movieName);
        }};
        try {
            HttpClientResult result = HttpClientUtils.doGet(url + "/api/v3/movie/lookup", headers, params);
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpClientResult searchMovieByImdbId(String imdbId) {
        Map<String, String> params = new HashMap<String, String>(1) {{
            put("imdbId", imdbId);
        }};
        try {
            HttpClientResult result = HttpClientUtils.doGet(url + "/api/v3/movie/lookup/imdb", headers, params);
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpClientResult addMovie(String movieInfo) {
        JSONObject json = JSON.parseObject(movieInfo);
        json.put("qualityProfileId", Config.radarrQualityProfileId);
        json.put("rootFolderPath", Config.radarrRootFolderPath);
        json.put("addOptions", JSON.parse("{searchForMovie: true}"));
        json.put("monitored", true);
        json.put("minimumAvailability", "announced");
        try {
            HttpClientResult result = HttpClientUtils.doPost(url + "/api/v3/movie", headers, json.toJSONString());
            return result;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }


}
