# douban-movie-robot
 豆瓣影视剧机器人
## docker打包脚本
 docker build -t jacksaoding/douban_movie_robot . -f Dockerfile --platform linux/amd64
## docker部署 

配置文件`application.properties`放到`/volume1/docker/douban-movie-robot/config/`目录下
```
# 可以设置多用户，逗号分隔，如: 5465465465,4445545
# 豆瓣用户ID获取方式：网页登录之后网址https://www.douban.com/people/193538704/，193538704就是用户ID
douban.userIds=193538704
#radarr的访问路径
radarr.url=http://127.0.0.1:7878
#radarr的密钥
radarr.apikey=9c6f79a4b11f4ba79d6e50b5b808e28b
#保存视频的目录
radarr.rootFolderPath=/movies
#视频质量配置
radarr.qualityProfileId=1
#sonarr的访问路径
sonarr.url=http://127.0.0.1:8989
#sonarr的密钥
sonarr.apikey=409c7d9926f84f93bf1a7e7dd9a28bf8
#保存视频的目录
sonarr.rootFolderPath=/tv
#视频质量配置
sonarr.qualityProfileId=1
#语言配置
sonarr.languageProfileId=3
#定时任务cron   秒 分 时 天 月 星期
scheduled.task.cron=0 0 2,10,12,14,16,19,21,23 * * ?
```
docker运行命令
```
docker run -d \
--name=douban_movie_robot \
-e TZ=Asia/Shanghai \
-v /volume1/docker/douban-movie-robot/config:/config \
--restart unless-stopped \
jacksaoding/douban_movie_robot:latest
```