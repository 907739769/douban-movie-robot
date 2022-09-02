package cn.jackding.doubanmovierobot;

import cn.jackding.doubanmovierobot.config.Config;
import cn.jackding.doubanmovierobot.telegram.Telegram;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class DoubanMovieRobotApplication implements CommandLineRunner, SchedulingConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DoubanMovieRobotApplication.class, args);
    }

    //启动时候就会执行一次
    @Override
    public void run(String... args) throws Exception {
        //启动服务首次运行
        run();
        //启用电报机器人
        if (Config.telegramBotEnable) {
            Telegram.registerBot();
        } else {
            log.info("未启用电报机器人");
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(this::run, Config.scheduledTaskCron);
    }

    public void run() {
        log.info("任务执行开始");
        for (String id : Config.doubanUserIds.split(",")) {
            new DoubanCore().run(id);
        }
        log.info("任务执行完成 ");
    }


}
