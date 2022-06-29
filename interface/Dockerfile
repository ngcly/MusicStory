# FROM 表示使用 Jdk8 环境 为基础镜像，如果镜像不是本地的会从 DockerHub 进行下载
FROM eclipse-temurin:17.0.3_7-jre
# VOLUME 指向了一个/tmp的目录，由于 Spring Boot 使用内置的Tomcat容器，Tomcat 默认使用/tmp作为工作目录。这个命令的效果是：在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
VOLUME /tmp

ARG PROJECT_NAME=interface
ARG JAR_FILE=build/libs/${PROJECT_NAME}-*.jar
ENV APP_JAR=mustic-story-${PROJECT_NAME}.jar
COPY ${JAR_FILE} ${APP_JAR}
ENTRYPOINT java -jar ${APP_JAR}