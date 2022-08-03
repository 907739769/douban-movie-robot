package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.pojo.HttpClientResult;
import cn.jackding.doubanmovierobot.radarr.RadarrUtils;
import cn.jackding.doubanmovierobot.sonarr.SonarrUtils;
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
        new DoubanCore().run("193538704");

    }
}
