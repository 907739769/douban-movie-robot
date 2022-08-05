package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.config.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@SpringBootApplication
@EnableScheduling
public class DoubanMovieRobotApplication implements CommandLineRunner, SchedulingConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DoubanMovieRobotApplication.class, args);
    }

    //启动时候就会执行一次
    @Override
    public void run(String... args) throws Exception {
        run();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(this::run, Config.scheduledTaskCron);
    }

    private void run() {
        for (String id : Config.doubanUserIds.split(",")) {
            new DoubanCore().run(id);
        }
    }


}
