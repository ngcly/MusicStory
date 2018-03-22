# MusicStory
一个用来听歌看书的网站   
- 基本框架：springboot2.0全家桶 ，gradle构建  
- 暂时没弄分布式，感觉用不上（其实我不会）
- 后台页面使用thymeleaf   
- 以layui框架为页面布局主题
- 数据连接池：阿里druid  mysql数据库      
目前存在两个问题，直接地址栏访问页面会没有iframe外层，另一个则是security怎么自定义授权和自定义授权失败信息

前台页面准备采用react加typescript（感觉好难）