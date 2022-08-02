package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.pojo.HttpClientResult;
import cn.jackding.doubanmovierobot.pojo.Movieinfo;
import cn.jackding.doubanmovierobot.radarr.RadarrUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
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
        HttpClientResult result= RadarrUtils.searchMovie("唯爱永存");
        System.out.println(result);
        JSONArray jsonArray=JSONArray.parseArray(result.getContent());
        System.out.println("jsonArray  "+jsonArray);

        Movieinfo movieinfo=JSON.parseObject(jsonArray.get(0).toString(),Movieinfo.class);
        System.out.println(movieinfo);
//        JSONObject jsonObject= JSON.parseObject(result.getContent());
//        System.out.println("221  "+jsonObject);
        //        RadarrUtils.addMovie(result.getContent());
    }
}
