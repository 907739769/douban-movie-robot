package cn.jackding.doubanmovierobot.telegram.factory;

import cn.jackding.doubanmovierobot.DoubanMovieRobotApplication;

/**
 * @Author Jack
 * @Date 2022/9/2 21:31
 * @Version 1.0.0
 */
@Deprecated
public class DoSearchDouban implements TelegramDo{
    @Override
    public String doIt() {
        new DoubanMovieRobotApplication().run();
        return "重新执行豆瓣搜索完成";
    }
}
