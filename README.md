# MusicStory
一个用来听歌看书的网站   
- 基本框架：springboot2.0全家桶 ，gradle构建  
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
  
特别鸣谢：  
感谢JetBrains免费提供Idea开发工具对本项目的支持  
[![JetBrains](jetbrains.svg "jetbrains")](https://www.jetbrains.com/?from=MusicStory)