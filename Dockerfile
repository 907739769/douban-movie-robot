FROM amazoncorretto:8u342-alpine3.13-jre
LABEL title="豆瓣影视剧机器人"
LABEL description="可以自动从豆瓣用户的想看列表中自动获取电影，并通过Radarr、Sonarr管理数据"
LABEL authors="JackDing"
COPY ./target/application.jar /application.jar
VOLUME /config
ENV TZ=Asia/Shanghai
ENTRYPOINT ["java", "-jar", "/application.jar"]
