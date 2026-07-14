# MusicStory (音书)
一个结合了高品质音乐播放与优雅文字创作的文艺社区网站。

---

## 🛠️ 技术选型与基础架构

*   **后端核心**：Spring Boot 3.x / 4.0 全家桶，基于 **Java 21** 运行环境。
*   **构建工具**：Gradle 构建，使用模块化、层次化架构设计。
*   **数据存储**：MySQL / H2 嵌入式数据库。
*   **连接池与监控**：阿里 Druid 连接池（集成 Jakarta EE / Spring Boot 3 Starter）。
*   **数据缓存**：Redis 缓存体系。
*   **异步消息**：RabbitMQ 消息队列（处理延时通知与异步任务）。
*   **即时推送**：WebSocket (SockJS + STOMP) 实时消息。
*   **全文检索**：Elasticsearch 搜索引擎。
*   **对象存储**：抽象的标准 S3 协议存储组件（兼容 Aliyun OSS, MinIO 与 AWS S3）。
*   **并发模型**：全面对齐 **JVM 虚拟线程 (Virtual Threads)**，彻底摒弃传统重度平台线程池。
*   **前端客户端**：Vite 6 + Vue 3 (Composition API) + Pinia + Vue Router + Element Plus 的单页应用（SPA）。

---

## 📂 项目模块结构

项目采用现代微核与依赖分层设计，模块划分清晰对齐：

```text
MusicStory (根目录)
├── apps (应用层入口)
│   ├── admin-app (后台管理系统 API)
│   ├── web-app (前台用户端 API)
│   └── web-client (前端 Vue 3 独立客户端工程)
├── modules (业务领域模块)
│   ├── shared (基础公共定义、存储接口声明)
│   ├── user (用户领域模型与业务逻辑)
│   ├── content (文章、公告、轮播图内容领域)
│   ├── relation (社交关系与关注粉丝模块)
│   ├── notification (通知发送、WebSocket 与 RabbitMQ 消费)
│   ├── security (Spring Security 认证与安全配置)
│   └── search (基于 Elasticsearch 的全文搜索模块)
└── infrastructure (基础设施层依赖配置)
    ├── persistence (持久化配置，MyBatis-Plus 及 Druid 监控)
    ├── cache (Redis 缓存模板与配置)
    ├── messaging (RabbitMQ 连接与参数定义)
    ├── oss (基于 aws-java-sdk-s3 的抽象 S3 存储服务实现)
    └── observability (系统可观测性与健康度量指标，待扩充)
```

---

## 🚀 启动与部署准备

项目已添加 `spring-boot-docker-compose` 原生支持。启动主类时，Docker Compose 会自动拉取并启动 Redis、RabbitMQ、Elasticsearch 等底层容器。

### 1. RabbitMQ 延时队列插件安装
容器启动后，需要启用 RabbitMQ 的延时队列插件：
1. 下载 `rabbitmq_delayed_message_exchange` 插件 (版本需与 RabbitMQ 匹配)，下载地址：[RabbitMQ Community Plugins](https://www.rabbitmq.com/community-plugins.html)。
2. 将下载的插件拷贝进容器：
   ```bash
   docker cp rabbitmq_delayed_message_exchange-3.10.2.ez rabbitmq:/plugins
   ```
3. 进入容器启用插件并重启：
   ```bash
   docker exec -it rabbitmq rabbitmq-plugins enable rabbitmq_delayed_message_exchange
   ```

### 2. 数据库初始化
项目包含 `data.sql` 文件，启动 MySQL 容器后导入该 SQL 文件即可完成初始数据的填充。

---

## 🔧 已修复的重大技术问题与重构

在最新版本中，我们对以下历史遗留问题和痛点进行了彻底修复与重构：

*   **Jasypt 配置文件解密修复 (Step 1)**：升级到 `4.0.4` 原生适配 Spring Boot 3.5+ / 4.x，解决启动加解密失效报错。
*   **Druid 监控页与连接池恢复 (Step 2)**：引入 `druid-spring-boot-3-starter`，移除老旧包引用并补充 Servlet Filter 过滤器，完美激活 `/druid/index.html` 控制台。
*   **标准 S3 存储协议抽象重构 (Step 3)**：剔除强绑定阿里云 OSS 的底层代码，通过标准 `AmazonS3` 客户端与 [StorageService](file:///Users/chenning/personalProjects/backend/MusicStory/modules/shared/src/main/java/com/cn/service/StorageService.java) 接口进行完全解耦。控制器层全面转为构造器依赖注入。
*   **虚拟线程对齐一致性 (Step 4)**：废除 `AsyncPoolConfig.java` 中硬件资源开销巨大的物理线程池，全量路由至 `SimpleAsyncTaskExecutor` 虚拟线程执行器。
*   **前端视觉深度打磨**：
    *   **首页美化**：轮播图升级为 3D 卡片层叠滑块；公告栏采用半透明毛玻璃材质并加入小喇叭振动微动效；文章列表卡片化并带有平滑 hover 位移。
    *   **精选页重构**：采用情景交融的全屏微动壁画背景（Ken Burns Effect）搭配高虚化磨玻璃段落。配乐控制器重构为极简黑胶唱片旋钮，Hover 时滑动显示音量拉杆，且包大小精简 95% 以上。
    *   **沉浸阅读 (Zen Mode)**：在详情页提供全屏禅意阅读遮罩，隐藏旁杂元素；并在顶部加装滚动比率进度条，全面加载 **Noto Serif SC (思源宋体)** 艺术长文排版。

---

## 🌟 特别鸣谢
感谢 JetBrains 免费提供 IntelliJ IDEA 开发工具对本项目的支持。
[![JetBrains](jetbrains.svg "jetbrains")](https://www.jetbrains.com/?from=MusicStory)