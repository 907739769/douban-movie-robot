package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.config.Config;
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
        for (String id : Config.doubanUserIds.split(",")) {
            new DoubanCore().run(id);
        }
//        SonarrUtils.searchSeries("石子和羽男");
    }
}
