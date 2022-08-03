package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.pojo.HttpClientResult;
import cn.jackding.doubanmovierobot.radarr.RadarrUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DoubanMovieRobotApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DoubanMovieRobotApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        HttpClientResult result= RadarrUtils.searchMovie("光年正传");

        JSONArray jsonArray=JSONArray.parseArray(result.getContent());
        String movieInfo="";
        for (Object json :jsonArray) {
            JSONObject json1=JSON.parseObject(json.toString());
            String imdbid=json1.getString("imdbId");
            if(imdbid.equals("tt10298810")){
                movieInfo=json1.toJSONString();
                break;
            }
        }
        RadarrUtils.addMovie(movieInfo);
    }
}
