# MusicStory Web Client (音书前端客户端)

音书的前端独立单页应用 (SPA)，基于 Vue 3 核心生态构建。项目旨在融合“高颜值音乐播放”与“沉浸式文章阅读”，为用户打造极具文艺美感与流畅手感的浏览体验。

---

## 🛠️ 技术选型与依赖包

*   **构建与开发工具**：**Vite 6.x** (超快的热重载与极小体积生产打包)
*   **前端核心框架**：**Vue 3 (Composition API)**，全部采用现代化 `<script setup>` 风格编写。
*   **状态管理**：**Pinia** (轻量响应式状态中心)
*   **路由管理**：**Vue Router 4** (支持路由级组件动态懒加载)
*   **UI 组件库**：**Element Plus** & **@element-plus/icons-vue**
*   **富文本编辑**：**MdEditorV3**
*   **动画与样式**：**Animate.css** + **FontAwesome 4/6** + **Normalize.css**
*   **音频内核**：HTML5 Native Audio / APlayer

---

## 🌟 页面设计与交互亮点

前端应用在最新迭代中进行了深度的视觉重构与质感升级，具体表现为：

### 1. 首页 (Home.vue) —— 精细化信息流
*   **3D 卡片式轮播**：轮播图重构为 Element Plus 的卡片叠放形式（`type="card"`），图片辅以圆角和慢变焦动效，如同 CD 柜切换般生动。
*   **公告栏玻璃容器**：用磨砂玻璃外框和微抖动小喇叭动效（`notice-shake`）重新装潢滚动公告栏，显著消除素版界面的单调。
*   **微悬浮文章卡片**：文章列表项全部设计为玻璃透光卡片，Hover 时使用缓动贝塞尔曲线实现向上微浮动（`translateY(-4px)`）与背景加白高亮，极具物理实体感。

### 2. 精选页 (Index.vue) —— 意境壁画与极简配乐
*   **壁画微移动效 (Ken Burns)**：应用全屏壁画背景，搭配极慢速的缩放与位移（`35s` 动效循环），做到画境随文字流转，情景交融。
*   **极简黑胶控制器**：彻底废除繁杂的多媒体面板，设计了挂载于右上角的黑胶自旋按钮（33转/分）。点击播放/暂停，Hover 时向左平滑推出音量调节杆（Volume slider），将空间完全留给精选文字。
*   **动态拉取精选**：接入后端 API 动态装载前两条优选文章，以大字号、衬线宋体在磨砂宣纸（`blur(20px)`）面板上文艺排版。

### 3. 文章详情页 (EssayDetail.vue) —— 禅意沉浸模式
*   **滚动微进度条**：在视口最顶端增加了阅读进度指示条，随用户的页面滚动实时高亮变化。
*   **“禅” 沉浸式阅读 (Zen Mode)**：在作者卡片旁新增“沉浸阅读”开关。点击后启用全屏绝对遮罩，隐藏页头、侧边栏和评论等任何干扰信息，将底色设为宣纸米黄色（`#fbf9f6`），并缩收正文在屏幕中央，呈现极简阅读形态。
*   **艺术排版**：正文字体引入 **Noto Serif SC (思源宋体)** 与 **Lora**，行高放宽至 `1.9`，让文字张弛有度。

---

## ⚡ 打包拆包优化 (Rollup ManualChunks)

针对 Vite 打包阶段大型组件库（如 Element Plus 和 MD 编辑器）导致主包超大警告的问题，在 `vite.config.js` 中新增了配置分卷代理：
```javascript
build: {
  rollupOptions: {
    output: {
      manualChunks(id) {
        if (id.includes('node_modules')) {
          if (id.includes('element-plus')) return 'element-plus';
          if (id.includes('md-editor-v3')) return 'md-editor';
          return 'vendor';
        }
      }
    }
  }
}
```
*   **优化结果**：各页面路由脚本（如 `Create.js`）体积由原 **`883 kB`** 骤缩至 **`4.23 kB`**。首屏文件体积极小，强力减少首包白屏时间。

---

## 💻 本地运行与构建

进入 `apps/web-client` 目录后：

### 安装依赖
```bash
npm install
```

### 本地开发运行 (运行在 9091 端口，且已配置 API 反向代理)
```bash
npm run dev
```

### 生产环境打包编译 (输出至 dist 目录)
```bash
npm run build
```