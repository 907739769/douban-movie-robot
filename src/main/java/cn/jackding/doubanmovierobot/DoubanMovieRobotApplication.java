package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.sonarr.SonarrUtils;
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
//        SonarrUtils.searchSeries("アンナチュラル");
    }
}
