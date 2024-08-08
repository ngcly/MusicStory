# MusicStory
一个用来听歌看书的网站   
- 基本框架：springboot3.0全家桶 ，gradle构建  
- 没弄微服务分布式，麻烦，用不上
- 后台页面采用 thymeleaf 模板   
- 以layui框架为页面布局主题   
- 数据库：mysql
- 数据连接池：阿里druid   
- 缓存：Redis  
- 消息队列：RabbitMQ   
- 消息推送：WebSocket  
- 全文搜索：ElasticSearch

## 模块概述

| 模块名称 | 功能 |
|:--|:--|
| backstage | 后台管理端 |
| commons | 公共代码部分 |
| interface | 前端API接口 |
| repository | 数据DAO层 |
| service | 业务service层 |

前端代码链接：https://github.com/ngcly/music-story  

为了方便下载项目代码后直接运行启动，建议先安装docker：   
项目目前已经添加了spring-boot-docker-compose支持
项目启动时会根据docker-compose.yml的配置自动创建docker容器  
容器创建完并启动后 部分组件需要安装插件

 1.安装RabbitMQ延时队列插件： 
   1. 下载rabbitmq_delayed_message_exchange插件 下载地址：https://www.rabbitmq.com/community-plugins.html
   2. 将下载的插件拷贝到容器中
      ```
      docker cp rabbitmq_delayed_message_exchange-3.10.2.ez  rabbitmq:/plugins
      ```  
   3. 启动插件并重启容器
      ```
      rabbitmq-plugins enable rabbitmq_delayed_message_exchange
      ```
2.若有使用fluentd则需要安装插件(自行配置可选)：
  ```
  docker exec -it --user root efk-fluentd /bin/sh
  fluent-gem install fluent-plugin-elasticsearch
  ```

### SpringBoot3.0 已知存在以下问题
1. ~~Druid组件监控页面无法使用~~
2. ~~Jasypt配置文件加解密无效~~
3. ~~Hutool还未适配，SpringUtil无法正常使用~~

特别鸣谢：  
感谢JetBrains免费提供Idea开发工具对本项目的支持  
[![JetBrains](jetbrains.svg "jetbrains")](https://www.jetbrains.com/?from=MusicStory)