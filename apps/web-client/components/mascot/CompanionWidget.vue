<template>
  <div 
    class="companion-widget"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
    @click="handleClick"
  >
    <!-- 毛玻璃对话气泡 -->
    <transition name="bubble-fade">
      <div v-if="showMessage" class="dialogue-bubble">
        <p class="bubble-text">{{ currentMessage }}</p>
        <div class="bubble-arrow"></div>
      </div>
    </transition>

    <!-- 极简矢量猫咪 SVG -->
    <div class="cat-body-container" :class="{ 'is-hovered': isHovered, 'is-clicking': isClicking }">
      <svg viewBox="0 0 100 100" class="cat-svg">
        <!-- 尾巴 (独立动画) -->
        <path 
          d="M 65 75 Q 85 70 82 50 Q 80 35 90 32" 
          fill="none" 
          stroke="currentColor" 
          stroke-width="7" 
          stroke-linecap="round" 
          class="cat-tail text-slate-700" 
        />
        
        <!-- 身体 (带有呼吸缩放) -->
        <ellipse cx="50" cy="72" rx="25" ry="18" fill="currentColor" class="cat-body text-slate-800" />
        
        <!-- 头部 -->
        <circle cx="50" cy="46" r="16" fill="currentColor" class="cat-head text-slate-800" />
        
        <!-- 左耳 -->
        <polygon points="36,40 24,20 42,32" fill="currentColor" class="cat-ear-left text-slate-800" />
        
        <!-- 右耳 -->
        <polygon points="64,40 76,20 58,32" fill="currentColor" class="cat-ear-right text-slate-800" />
        
        <!-- 闭眼/睁眼 (Hover 状态切换) -->
        <g class="cat-eyes text-slate-200">
          <template v-if="!isHovered">
            <!-- 闭眼弧线 (打盹) -->
            <path d="M 39 46 Q 44 49 45 46" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
            <path d="M 61 46 Q 56 49 55 46" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
          </template>
          <template v-else>
            <!-- 睁眼椭圆 -->
            <ellipse cx="41" cy="45" rx="2" ry="3" fill="currentColor" />
            <ellipse cx="59" cy="45" rx="2" ry="3" fill="currentColor" />
            <!-- 微笑小红晕 -->
            <ellipse cx="36" cy="49" rx="2.5" ry="1" fill="#f87171" opacity="0.6" />
            <ellipse cx="64" cy="49" rx="2.5" ry="1" fill="#f87171" opacity="0.6" />
          </template>
        </g>
        
        <!-- 胡须 -->
        <g class="cat-whiskers text-slate-500" opacity="0.7">
          <line x1="30" y1="48" x2="16" y2="46" stroke="currentColor" stroke-width="1.5" />
          <line x1="30" y1="51" x2="14" y2="52" stroke="currentColor" stroke-width="1.5" />
          <line x1="70" y1="48" x2="84" y2="46" stroke="currentColor" stroke-width="1.5" />
          <line x1="70" y1="51" x2="86" y2="52" stroke="currentColor" stroke-width="1.5" />
        </g>
        
        <!-- 粉色小鼻子 -->
        <polygon points="48,49 52,49 50,51" fill="#fda4af" />
      </svg>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const isHovered = ref(false)
const isClicking = ref(false)
const showMessage = ref(false)
const currentMessage = ref('')
let timer = null

// 伴侣对话短语
const quotes = [
  "嗨，我是音书的陪伴猫咪。今天读了什么好文章呢？",
  "慢下来，听一首轻歌，记录文字流淌的乐章。",
  "音书里有很多温暖的诗词，它们都在静静等待你发现呢。",
  "阅读就像一场旅行，而我会在终点一直陪伴你。",
  "点击我可以换一句话哦，我也非常乐意听听你的想法~",
  "今天的天气，非常适合窝起来静静看一本书。"
]

const getGreeting = () => {
  const hour = new Date().getHours()
  if (hour >= 6 && hour < 11) return "早上好呀！新的一天，先读一篇温暖的文章吧~"
  if (hour >= 11 && hour < 17) return "午后时光，听着轻柔的音乐，阅读感觉加倍哦~"
  if (hour >= 17 && hour < 23) return "傍晚好，辛苦工作了一天，来音书放松一下心灵吧。"
  return "夜已深了，小猫也要打盹了。听首老歌，早点休息吧..."
}

function showBubble(text) {
  if (timer) clearTimeout(timer)
  currentMessage.value = text
  showMessage.value = true
  
  // 6秒后自动淡出气泡
  timer = setTimeout(() => {
    showMessage.value = false
  }, 6000)
}

function handleMouseEnter() {
  isHovered.value = true
  showBubble(getGreeting())
}

function handleMouseLeave() {
  isHovered.value = false
}

function handleClick() {
  isClicking.value = true
  setTimeout(() => {
    isClicking.value = false
  }, 300)

  // 随机切换对话短语
  const randIdx = Math.floor(Math.random() * quotes.length)
  showBubble(quotes[randIdx])
}

onMounted(() => {
  // 页面加载3秒后自动打招呼
  setTimeout(() => {
    showBubble("欢迎来到音书，聆听旋律与墨香的交响...")
  }, 3000)
})

onBeforeUnmount(() => {
  if (timer) clearTimeout(timer)
})
</script>

<style scoped>
.companion-widget {
  position: fixed;
  bottom: 16px;
  right: 16px;
  width: 75px;
  height: 75px;
  z-index: 50;
  cursor: pointer;
  user-select: none;
}

/* --- 极简矢量猫咪容器 --- */
.cat-body-container {
  width: 100%;
  height: 100%;
  transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.cat-body-container:hover {
  transform: translateY(-4px) scale(1.05);
}

.cat-body-container.is-clicking {
  transform: scale(0.9) translateY(4px);
}

.cat-svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 4px 10px rgba(0, 0, 0, 0.1));
}

/* --- CSS 动画部分 --- */

/* 1. 身体上下微幅呼吸动效 */
.cat-body, .cat-head, .cat-eyes, .cat-whiskers {
  transform-origin: 50% 80%;
  animation: breathing 4s ease-in-out infinite;
}

@keyframes breathing {
  0%, 100% {
    transform: scaleY(1);
  }
  50% {
    transform: scaleY(1.04);
  }
}

/* 2. 尾巴左右温柔摆动 */
.cat-tail {
  transform-origin: 65px 75px;
  animation: tailWag 6s ease-in-out infinite;
  transition: animation 0.3s;
}

.is-hovered .cat-tail {
  animation: tailWagActive 1.8s ease-in-out infinite;
}

@keyframes tailWag {
  0%, 100% {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(8deg) translate(-1px, 1px);
  }
}

@keyframes tailWagActive {
  0%, 100% {
    transform: rotate(0deg);
  }
  33% {
    transform: rotate(12deg) translate(-2px, 1px);
  }
  66% {
    transform: rotate(-6deg) translate(1px, -1px);
  }
}

/* 3. 耳朵间歇性微微抖动 */
.cat-ear-left {
  transform-origin: 36px 40px;
  animation: earTwitchLeft 8s ease-in-out infinite;
}

.cat-ear-right {
  transform-origin: 64px 40px;
  animation: earTwitchRight 8s ease-in-out infinite 0.5s;
}

@keyframes earTwitchLeft {
  0%, 94%, 100% {
    transform: rotate(0deg);
  }
  96%, 98% {
    transform: rotate(-8deg);
  }
}

@keyframes earTwitchRight {
  0%, 94%, 100% {
    transform: rotate(0deg);
  }
  96%, 98% {
    transform: rotate(8deg);
  }
}

/* --- 毛玻璃对话气泡 --- */
.dialogue-bubble {
  position: absolute;
  bottom: 84px;
  right: -8px;
  width: 200px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08), 
              0 1px 3px rgba(0, 0, 0, 0.02);
  border-radius: 16px;
  padding: 10px 14px;
  z-index: 60;
  transform-origin: bottom right;
}

.bubble-text {
  margin: 0;
  font-size: 11.5px;
  line-height: 1.6;
  color: #334155;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
  text-align: justify;
}

.bubble-arrow {
  position: absolute;
  bottom: -6px;
  right: 36px;
  width: 12px;
  height: 12px;
  background: rgba(255, 255, 255, 0.85);
  border-right: 1px solid rgba(255, 255, 255, 0.5);
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
  transform: rotate(45deg);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

/* --- 气泡淡入淡出动画 --- */
.bubble-fade-enter-active,
.bubble-fade-leave-active {
  transition: opacity 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275), 
              transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.bubble-fade-enter-from,
.bubble-fade-leave-to {
  opacity: 0;
  transform: scale(0.8) translateY(10px);
}
</style>
