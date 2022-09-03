package cn.jackding.doubanmovierobot.telegram.ability;

/**
 * @Author Jack
 * @Date 2022/9/3 10:40
 * @Version 1.0.0
 */
public enum Command {

    DOUBAN("douban","重新执行豆瓣搜索服务"),
    MOVIE("movie","搜索电影"),
    SERIES("series","搜索电视剧"),

    ;
    private final String cmd;
    private final String des;

    Command(String cmd, String des) {
        this.cmd = cmd;
        this.des = des;
    }

    public String getCmd() {
        return cmd;
    }

    public String getDes() {
        return des;
    }
}
