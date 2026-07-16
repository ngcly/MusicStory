<template>
  <div 
    ref="widgetRef"
    class="companion-widget"
    :class="[girlThemeClass, { 'is-music-playing': isMusicPlaying }]"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
  >
    <!-- 毛玻璃对话气泡 -->
    <transition name="bubble-fade">
      <div v-if="showMessage" class="dialogue-bubble">
        <p class="bubble-text">{{ currentMessage }}</p>
        <div class="bubble-arrow"></div>
      </div>
    </transition>

    <!-- 伴读小红心/惊喜提示 -->
    <transition name="heart-pop">
      <div v-if="showHeart" class="reading-heart-pop">
        <svg viewBox="0 0 24 24" class="w-6 h-6 text-red-500 fill-red-500">
          <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
        </svg>
      </div>
    </transition>
    <transition name="heart-pop">
      <div v-if="showSurprise" class="reading-heart-pop text-yellow-500 font-bold text-lg select-none">
        ❗
      </div>
    </transition>

    <!-- 播放音乐时的浮动音符 -->
    <div v-if="isMusicPlaying" class="music-notes-container">
      <span class="music-note note-1">🎵</span>
      <span class="music-note note-2">🎶</span>
    </div>

    <!-- 伴读少女主体容器 (支持听歌点头) -->
    <div 
      class="girl-body-container" 
      :class="{ 
        'is-hovered': isHovered, 
        'is-clicking': isClicking,
        'is-music-nodding': isMusicPlaying
      }"
      @click="handleBodyClick"
    >
      <svg viewBox="0 0 150 150" class="girl-svg">
        <defs>
          <!-- 头发渐变 (白天浅棕，夜间深蓝紫) -->
          <linearGradient id="hairGrad" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="var(--hair-grad-start)" />
            <stop offset="100%" stop-color="var(--hair-grad-end)" />
          </linearGradient>

          <!-- 皮肤柔和渐变 -->
          <linearGradient id="skinGrad" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#fff1f2" />
            <stop offset="100%" stop-color="#ffe4e6" />
          </linearGradient>

          <!-- 毛衣毛呢渐变 -->
          <linearGradient id="sweaterGrad" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="var(--sweater-grad-start)" />
            <stop offset="100%" stop-color="var(--sweater-grad-end)" />
          </linearGradient>

          <!-- 书桌木纹质感渐变 -->
          <linearGradient id="deskGrad" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#7c2d12" />
            <stop offset="100%" stop-color="#451a03" />
          </linearGradient>

          <!-- 台灯暖色背景光晕 -->
          <radialGradient id="lampLight" cx="35%" cy="35%" r="65%">
            <stop offset="0%" stop-color="rgba(253, 224, 71, 0.4)" />
            <stop offset="70%" stop-color="rgba(249, 115, 22, 0.1)" />
            <stop offset="100%" stop-color="rgba(249, 115, 22, 0)" />
          </radialGradient>
        </defs>

        <!-- 1. 背景温暖台灯光晕 (深夜 18点到 6点显示，营造温暖文艺氛围) -->
        <circle cx="20" cy="50" r="50" fill="url(#lampLight)" v-if="currentHour >= 18 || currentHour < 6" />

        <!-- 2. 后发层 (脑后长发，带微风摇摆) -->
        <path 
          d="M 42 65 C 28 85, 26 125, 40 135 C 50 140, 52 110, 52 90 Z" 
          fill="url(#hairGrad)" 
          class="hair-back-left"
        />
        <path 
          d="M 108 65 C 122 85, 124 125, 110 135 C 100 140, 98 110, 98 90 Z" 
          fill="url(#hairGrad)" 
          class="hair-back-right"
        />

        <!-- 3. 身体与衣服 (舒适的高领针织衫) -->
        <g class="girl-torso">
          <!-- 衣服肩膀与躯干 -->
          <path 
            d="M 25 125 C 10 135, 2 150, 2 150 L 148 150 C 148 150, 140 135, 125 125 C 110 115, 40 115, 25 125 Z" 
            fill="url(#sweaterGrad)" 
            stroke="var(--line-color)" 
            stroke-width="1.2"
          />
          <!-- 针织高领 -->
          <path 
            d="M 64 103 Q 75 107 86 103 L 88 117 Q 75 122 62 117 Z" 
            fill="url(#sweaterGrad)" 
            stroke="var(--line-color)" 
            stroke-width="1"
          />
        </g>

        <!-- 4. 头部与脸庞 (支持 2.5D 视差倾斜) -->
        <g class="girl-head-group">
          <!-- 脸部轮廓 (左侧精致下巴) -->
          <path 
            d="M 52 64 Q 50 88 74 96 Q 94 96 98 75 C 99 70, 95 65, 93 64 Z" 
            fill="url(#skinGrad)" 
            stroke="var(--line-color)" 
            stroke-width="1.2"
          />

          <!-- 左耳 -->
          <path d="M 51 68 Q 47 70 49 76 Z" fill="url(#skinGrad)" stroke="var(--line-color)" stroke-width="1" />

          <!-- 5. 2.5D 脸部五官视差层 (眼/鼻/嘴/红晕，0.8x 位移幅度，塑造转头立体感) -->
          <g :style="faceParallaxStyle" @click.stop="clickFace" class="cursor-pointer">
            <!-- 害羞红晕 -->
            <ellipse cx="62" cy="74" rx="4" ry="1.8" fill="#f43f5e" opacity="0.4" />
            <ellipse cx="86" cy="74" rx="4" ry="1.8" fill="#f43f5e" opacity="0.4" />

            <!-- 双眼 (带灵动瞳孔、睫毛与眼神追踪) -->
            <g class="girl-eyes">
              <template v-if="!isHovered && !isMusicPlaying && !isReadingComplete && !isNoseSneezed">
                <!-- 闭眼阅读/沉思 -->
                <path d="M 56 68 Q 63 72 68 67" fill="none" stroke="var(--line-color)" stroke-width="2" stroke-linecap="round" />
                <path d="M 80 67 Q 85 72 92 68" fill="none" stroke="var(--line-color)" stroke-width="2" stroke-linecap="round" />
              </template>
              <template v-else-if="isNoseSneezed">
                <!-- 打喷嚏时闭眼 -->
                <path d="M 56 71 L 66 67" stroke="var(--line-color)" stroke-width="2" stroke-linecap="round" />
                <path d="M 56 67 L 66 71" stroke="var(--line-color)" stroke-width="2" stroke-linecap="round" />
                <path d="M 82 71 L 92 67" stroke="var(--line-color)" stroke-width="2" stroke-linecap="round" />
                <path d="M 82 67 L 92 71" stroke="var(--line-color)" stroke-width="2" stroke-linecap="round" />
              </template>
              <template v-else>
                <!-- 睁开的高亮双眼 -->
                <!-- 左眼 -->
                <ellipse cx="62" cy="69" rx="5" ry="7" fill="var(--eyes-color)" />
                <ellipse cx="62" cy="69" rx="3.5" ry="5.5" fill="#0d9488" v-if="girlThemeClass === 'theme-daylight'" />
                <ellipse cx="62" cy="69" rx="3.5" ry="5.5" fill="#4338ca" v-else />
                <circle cx="60.5" cy="66.5" r="1.5" fill="#ffffff" />
                <circle cx="63.5" cy="71.5" r="0.8" fill="#ffffff" opacity="0.6" />
                <path d="M 55 64 Q 62 60 69 64" fill="none" stroke="var(--line-color)" stroke-width="2" />

                <!-- 右眼 -->
                <ellipse cx="86" cy="69" rx="5" ry="7" fill="var(--eyes-color)" />
                <ellipse cx="86" cy="69" rx="3.5" ry="5.5" fill="#0d9488" v-if="girlThemeClass === 'theme-daylight'" />
                <ellipse cx="86" cy="69" rx="3.5" ry="5.5" fill="#4338ca" v-else />
                <circle cx="84.5" cy="66.5" r="1.5" fill="#ffffff" />
                <circle cx="87.5" cy="71.5" r="0.8" fill="#ffffff" opacity="0.6" />
                <path d="M 79 64 Q 86 60 93 64" fill="none" stroke="var(--line-color)" stroke-width="2" />
              </template>
            </g>

            <!-- 小巧粉鼻 (点击打喷嚏) -->
            <g @click.stop="clickNose">
              <path d="M 73 75 L 75 77 L 73 78" fill="none" stroke="#f43f5e" stroke-width="1.2" stroke-linecap="round" />
              <circle cx="74" cy="76" r="6" fill="transparent" />
            </g>

            <!-- 温暖微笑小嘴 -->
            <path d="M 70 83 Q 74 86 78 83" fill="none" stroke="var(--line-color)" stroke-width="1.8" stroke-linecap="round" />
          </g>

          <!-- 6. 睡帽 (深夜 23点至6点闭眼睡觉戴) -->
          <g v-if="isLateNight" class="girl-sleep-cap-container">
            <path d="M 48 40 Q 75 12 90 22 Q 68 28 48 40 Z" fill="#4f46e5" />
            <path d="M 90 22 Q 95 24 94 30 Q 88 28 90 22 Z" fill="#818cf8" />
            <circle cx="94" cy="30" r="3.5" fill="#e0e7ff" />
          </g>

          <!-- 7. 头戴式耳机 (听歌状态，带灯光呼吸闪烁) -->
          <g v-if="isMusicPlaying" class="girl-headphones-container cursor-pointer" @click.stop="clickHeadphones">
            <path d="M 49 46 Q 74 20 99 46" fill="none" stroke="#2c3e50" stroke-width="4.5" stroke-linecap="round" />
            <ellipse cx="48" cy="53" rx="4.5" ry="8" fill="#14b8a6" class="headphone-cup" />
            <ellipse cx="100" cy="53" rx="4.5" ry="8" fill="#14b8a6" class="headphone-cup" />
          </g>

          <!-- 8. 前发/刘海 (支持 0.4x 极微幅视差微移，浮动感拉满) -->
          <g :style="bangsParallaxStyle" class="hair-fringe-container cursor-pointer" @click.stop="clickHair">
            <!-- 额头刘海 -->
            <path d="M 48 40 Q 56 60 59 62 Q 63 50 68 62 Q 74 48 78 62 Q 86 48 94 56 Q 96 40 48 40 Z" fill="url(#hairGrad)" />
            <!-- 左侧鬓角发缕 -->
            <path d="M 50 48 Q 42 68 45 92 Q 52 90 52 75 Z" fill="url(#hairGrad)" class="hair-strand-left" />
            <!-- 右侧鬓角发缕 -->
            <path d="M 96 48 Q 104 68 101 92 Q 94 90 95 75 Z" fill="url(#hairGrad)" class="hair-strand-right" />
          </g>
        </g>

        <!-- 9. 右手撑头骨骼 (撑在桌面上托住右脸颊) -->
        <g class="girl-arm-right">
          <!-- 撑住脸部的右手小臂与手掌 -->
          <path 
            d="M 120 150 L 102 108 Q 97 90 90 92 Q 90 98 103 108 Z" 
            fill="url(#skinGrad)" 
            stroke="var(--line-color)" 
            stroke-width="1.2"
          />
          <!-- 衣服袖子 (右臂) -->
          <path 
            d="M 125 150 L 108 112 Q 102 110 100 114 L 115 150 Z" 
            fill="url(#sweaterGrad)" 
            stroke="var(--line-color)" 
            stroke-width="1"
          />
        </g>

        <!-- 10. 左手在桌上 (点击左手转笔) -->
        <g 
          class="girl-arm-left cursor-pointer" 
          :class="{ 'is-waving': isLeftHandWaved }"
          @click.stop="clickHand"
        >
          <!-- 衣服左袖 -->
          <path d="M 28 150 Q 38 128 46 130 L 52 150 Z" fill="url(#sweaterGrad)" stroke="var(--line-color)" stroke-width="1" />
          <!-- 露出的左手背 (搭在书旁) -->
          <ellipse cx="48" cy="144" rx="5" ry="3.5" fill="url(#skinGrad)" stroke="var(--line-color)" stroke-width="0.8" />
          <!-- 极简书写钢笔 -->
          <line x1="48" y1="144" x2="38" y2="132" stroke="#475569" stroke-width="2" stroke-linecap="round" />
          <path d="M 38 132 L 36 134 L 38 132 Z" fill="#ef4444" />
        </g>

        <!-- 11. 木纹书桌与书本 (搭在最前侧) -->
        <g class="girl-desk">
          <!-- 木纹桌面 (横跨屏幕最底边) -->
          <rect x="0" y="140" width="150" height="10" fill="url(#deskGrad)" />
          <!-- 桌面上翻开的一页毛玻璃书本 (带判定，点击翻页) -->
          <g class="cursor-pointer" @click.stop="clickBook">
            <!-- 书本纸张 -->
            <path 
              d="M 52 140 Q 64 135 78 139 Q 92 135 104 139 L 102 147 Q 90 143 78 147 Q 62 143 52 147 Z" 
              fill="#f8fafc" 
              stroke="#cbd5e1" 
              stroke-width="0.8" 
            />
            <!-- 书本横线模拟文字 -->
            <line x1="58" y1="141" x2="70" y2="141" stroke="#94a3b8" stroke-width="0.6" />
            <line x1="58" y1="143" x2="66" y2="143" stroke="#94a3b8" stroke-width="0.6" />
            <line x1="84" y1="141" x2="96" y2="141" stroke="#94a3b8" stroke-width="0.6" />
            <line x1="84" y1="143" x2="94" y2="143" stroke="#94a3b8" stroke-width="0.6" />
          </g>
        </g>
      </svg>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import { useRoute } from '#app'
import { useStore } from '@/store'
import { storeToRefs } from 'pinia'

const store = useStore()
const route = useRoute()
const { isMusicPlaying } = storeToRefs(store)

const widgetRef = ref(null)
const isHovered = ref(false)
const isClicking = ref(false)
const showMessage = ref(false)
const currentMessage = ref('')
const showHeart = ref(false)
const showSurprise = ref(false)
const isReadingComplete = ref(false)

// 各部位交互动画状态
const isLeftEarTwitched = ref(false)
const isRightEarTwitched = ref(false)
const isNoseSneezed = ref(false)
const isLeftHandWaved = ref(false)
const isTailFlicked = ref(false) // 针对发丝抖动

// 鼠标位置追随眼珠偏移量
const eyeOffset = reactive({ x: 0, y: 0 })

let bubbleTimer = null
let hasReadCurrentPage = false

// 1. 伴读女孩通用随机话术
const bodyQuotes = [
  "今天，你打算在音书记录下怎样的心境呢？✍️",
  "慢下来，听一首轻歌，享受文字流淌的温度。",
  "阅读是一场心灵的远行，而我会一直在这里陪着你。",
  "今天的温度，很适合静下心来读一首温暖的短诗。"
]

const currentHour = ref(12)
onMounted(() => {
  currentHour.value = new Date().getHours()
})

const isLateNight = computed(() => {
  return currentHour.value >= 23 || currentHour.value < 6
})

const girlThemeClass = computed(() => {
  if (currentHour.value >= 6 && currentHour.value < 18) {
    return 'theme-daylight' // 白天清新针织少女
  }
  return 'theme-midnight' // 夜晚静谧治愈系少女
})

const getGreeting = () => {
  if (isMusicPlaying.value) return "你正在听歌呢，这首歌的旋律我也很喜欢，感觉心静下来了..."
  const hour = new Date().getHours()
  if (hour >= 6 && hour < 11) return "早上好呀！新的一天，先读一篇温暖的文章吧~"
  if (hour >= 11 && hour < 17) return "午后时光，听着轻柔的音乐，阅读感觉加倍哦~"
  if (hour >= 17 && hour < 23) return "傍晚好，辛苦工作了一天，来音书放松一下心灵吧。"
  return "夜已深了，我也有些犯困了。听首老歌，早点休息吧..."
}

function showBubble(text) {
  if (bubbleTimer) clearTimeout(bubbleTimer)
  currentMessage.value = text
  showMessage.value = true
  bubbleTimer = setTimeout(() => {
    showMessage.value = false
  }, 6000)
}

function handleMouseEnter() {
  isHovered.value = true
  showBubble(getGreeting())
}

function handleMouseLeave() {
  isHovered.value = false
  eyeOffset.x = 0
  eyeOffset.y = 0
}

function handleBodyClick() {
  isClicking.value = true
  setTimeout(() => {
    isClicking.value = false
  }, 300)

  const randIdx = Math.floor(Math.random() * bodyQuotes.length)
  showBubble(bodyQuotes[randIdx])
}

// 2. 局部部件互动逻辑

// 👓 摸摸头/刘海
function clickHair() {
  showBubble("唔，头发乱了吗？有些不好意思呢... (*/ω＼*)")
}

// 🎧 点耳麦
function clickHeadphones() {
  showBubble("这首歌的律动真美，感觉思绪都随着音符飞舞了。🎶")
}

// 👃 戳鼻子打喷嚏
function clickNose() {
  if (isNoseSneezed.value) return
  isNoseSneezed.value = true
  showBubble("阿啾！—— 唔，是冷气开太大了，还是你在偷偷想我呢？🤧")
  setTimeout(() => {
    isNoseSneezed.value = false
  }, 1000)
}

// 📖 翻桌上的书
function clickBook() {
  showBubble("我也在看一本很有意思的小说呢，文字真是有魔力。📖")
}

// ✍️ 戳左手转钢笔
function clickHand() {
  if (isLeftHandWaved.value) return
  isLeftHandWaved.value = true
  showBubble("击个掌吧！今天也要写出很棒的文字哦！✋")
  setTimeout(() => {
    isLeftHandWaved.value = false
  }, 600)
}

// 3. 2.5D 立体转头视差偏移计算
// 眼珠平移 (100% 偏移量)
const eyeStyle = computed(() => {
  if (!isHovered.value && !isMusicPlaying.value && !isReadingComplete.value && !isNoseSneezed.value) return {}
  return {
    transform: `translate(${eyeOffset.x * 1.2}px, ${eyeOffset.y * 1.2}px)`,
    transition: 'transform 0.12s ease-out'
  }
})

// 五官平移视差 (80% 偏移量，模拟脸部转动透视)
const faceParallaxStyle = computed(() => {
  if (!isHovered.value && !isMusicPlaying.value) return {}
  return {
    transform: `translate(${eyeOffset.x * 0.8}px, ${eyeOffset.y * 0.8}px)`,
    transition: 'transform 0.12s ease-out'
  }
})

// 刘海发缕视差 (40% 极微偏移量，产生前后空间阻尼感)
const bangsParallaxStyle = computed(() => {
  if (!isHovered.value && !isMusicPlaying.value) return {}
  return {
    transform: `translate(${eyeOffset.x * 0.4}px, ${eyeOffset.y * 0.4}px)`,
    transition: 'transform 0.12s ease-out'
  }
})

function handleMouseMove(e) {
  if (!isHovered.value && !isMusicPlaying.value) return
  if (!widgetRef.value) return

  const rect = widgetRef.value.getBoundingClientRect()
  const centerX = rect.left + rect.width / 2
  const centerY = rect.top + rect.height / 2

  const dx = e.clientX - centerX
  const dy = e.clientY - centerY
  const distance = Math.sqrt(dx * dx + dy * dy)

  if (distance < 350 && distance > 10) {
    const maxOffset = 2.2
    eyeOffset.x = (dx / distance) * maxOffset
    eyeOffset.y = (dy / distance) * maxOffset
  } else {
    eyeOffset.x = 0
    eyeOffset.y = 0
  }
}

// 伴读滚动条事件
function handleScroll() {
  if (!route.path.startsWith('/essayDetail/')) return
  if (hasReadCurrentPage) return

  const scrollHeight = document.documentElement.scrollHeight
  const clientHeight = document.documentElement.clientHeight
  const scrollTop = window.scrollY || document.documentElement.scrollTop

  if (scrollHeight - clientHeight <= 0) return

  const progress = (scrollTop / (scrollHeight - clientHeight)) * 100
  if (progress > 93) {
    hasReadCurrentPage = true
    isReadingComplete.value = true
    showHeart.value = true
    showBubble("你已经读完了整篇文章，真是一位专注的人。伴读少女送你一颗心 ❤️")
    
    setTimeout(() => {
      showHeart.value = false
      isReadingComplete.value = false
    }, 4500)
  }
}

watch(() => route.path, () => {
  hasReadCurrentPage = false
  isReadingComplete.value = false
})

onMounted(() => {
  if (process.client) {
    window.addEventListener('mousemove', handleMouseMove)
    window.addEventListener('scroll', handleScroll)
  }
  setTimeout(() => {
    showBubble("欢迎来到音书，我在这里陪着你一起阅读与听歌...")
  }, 3000)
})

onBeforeUnmount(() => {
  if (process.client) {
    window.removeEventListener('mousemove', handleMouseMove)
    window.removeEventListener('scroll', handleScroll)
  }
  if (bubbleTimer) clearTimeout(bubbleTimer)
})
</script>

<style scoped>
.companion-widget {
  position: fixed;
  bottom: 0px;
  right: 12px;
  width: 145px;
  height: 145px;
  z-index: 50;
  cursor: pointer;
  user-select: none;
}

/* --- 主体微调过渡 --- */
.girl-body-container {
  width: 100%;
  height: 100%;
  transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  transform-origin: bottom center;
}
.girl-body-container:hover {
  transform: translateY(-2px) scale(1.02);
}
.girl-body-container.is-clicking {
  transform: scale(0.96) translateY(2px);
}

/* 👃 戳鼻子打喷嚏，整个人发生快速颤抖 */
.girl-body-container.is-sneezing {
  animation: sneezeShake 0.6s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
}

@keyframes sneezeShake {
  0%, 100% { transform: scale(1) translateY(0); }
  10%, 90% { transform: scale(1.02) translate3d(-1px, -3px, 0); }
  20%, 80% { transform: scale(0.98) translate3d(1.5px, -1px, 0); }
  30%, 50%, 70% { transform: scale(1.03) translate3d(-2px, -4px, 0); }
  40%, 60% { transform: scale(0.96) translate3d(2px, -0.5px, 0); }
}

.girl-svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 8px 16px rgba(0, 0, 0, 0.06));
}

/* --- 昼夜自适应颜色变量 --- */
.companion-widget.theme-daylight {
  /* 燕麦奶白毛衣 + 金褐发丝 */
  --hair-grad-start: #d97706; /* 金褐色 */
  --hair-grad-end: #78350f;
  --sweater-grad-start: #fafaf9; /* 暖白色 */
  --sweater-grad-end: #e7e5e4;
  --eyes-color: #0f766e;
  --line-color: #451a03;
}
.companion-widget.theme-midnight {
  /* 极简蓝黑毛衣 + 幽邃紫发丝 */
  --hair-grad-start: #6366f1; /* 靛紫蓝 */
  --hair-grad-end: #1e1b4b;
  --sweater-grad-start: #312e81; /* 黛青蓝 */
  --sweater-grad-end: #1e1b4b;
  --eyes-color: #3730a3;
  --line-color: #1e1b4b;
}

.girl-head-group circle, .girl-head-group path {
  transition: color 0.5s ease;
}

/* --- 核心动画部分 --- */

/* 1. 慢呼吸起伏 (听歌时减速) */
.girl-torso, .girl-head-group, .girl-arm-right, .girl-arm-left {
  transform-origin: 75px 145px;
  animation: breathing 5s ease-in-out infinite;
}
.is-music-playing .girl-torso,
.is-music-playing .girl-head-group,
.is-music-playing .girl-arm-right {
  animation-duration: 7s;
}
@keyframes breathing {
  0%, 100% { transform: scaleY(1); }
  50% { transform: scaleY(1.02); }
}

/* 2. 听歌状态：轻柔点头动作 */
.is-music-nodding {
  animation: nodToMusic 3.5s ease-in-out infinite;
}
@keyframes nodToMusic {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(1.5px) rotate(1.2deg); }
}

/* 3. 听歌状态：耳麦闪烁呼吸灯 */
.headphone-cup {
  animation: pulseCup 2s ease-in-out infinite;
}
@keyframes pulseCup {
  0%, 100% { filter: drop-shadow(0 0 1px rgba(20,184,166,0.3)); opacity: 0.85; }
  50% { filter: drop-shadow(0 0 6px rgba(20,184,166,0.9)); opacity: 1; }
}

/* 4. 长发发梢：微风自然晃动 */
.hair-back-left {
  transform-origin: 42px 65px;
  animation: hairWindLeft 8s ease-in-out infinite;
}
.hair-back-right {
  transform-origin: 108px 65px;
  animation: hairWindRight 8s ease-in-out infinite 0.7s;
}
@keyframes hairWindLeft {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(-3.5deg) skewX(-2deg); }
}
@keyframes hairWindRight {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(3.5deg) skewX(2deg); }
}

/* 5. 捏左耳/右耳 */
.cat-ear-left-group {
  transform-origin: 37px 42px;
  transition: transform 0.3s ease;
}
.cat-ear-left-group.is-twitched {
  transform: rotate(-15deg) scaleY(0.8);
}
.cat-ear-right-group {
  transform-origin: 75px 42px;
  transition: transform 0.3s ease;
}
.cat-ear-right-group.is-twitched {
  transform: rotate(15deg) scaleY(0.8);
}

/* 6. 左手写字转笔 */
.girl-arm-left {
  transform-origin: 28px 150px;
  transition: transform 0.25s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.girl-arm-left.is-waving {
  transform: translateY(-6px) scale(1.1) rotate(-8deg);
}

/* --- 音符与心形上升 --- */
.music-notes-container {
  position: absolute;
  top: 10px;
  left: 30px;
  width: 50px;
  height: 30px;
  pointer-events: none;
}
.music-note {
  position: absolute;
  font-size: 11px;
  opacity: 0;
}
.note-1 {
  left: 0;
  animation: floatNote1 3.5s linear infinite;
}
.note-2 {
  left: 20px;
  animation: floatNote2 4s linear infinite 1.5s;
}

@keyframes floatNote1 {
  0% { transform: translateY(10px) rotate(0deg); opacity: 0; }
  20% { opacity: 0.8; }
  100% { transform: translateY(-40px) translateX(-20px) rotate(-20deg); opacity: 0; }
}
@keyframes floatNote2 {
  0% { transform: translateY(10px) rotate(0deg); opacity: 0; }
  20% { opacity: 0.8; }
  100% { transform: translateY(-45px) translateX(15px) rotate(25deg); opacity: 0; }
}

.reading-heart-pop {
  position: absolute;
  top: 0px;
  left: 60px;
  pointer-events: none;
  z-index: 55;
}
.heart-pop-enter-active {
  animation: heartPopAnim 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.heart-pop-leave-active {
  transition: opacity 0.5s ease;
}
.heart-pop-leave-to {
  opacity: 0;
}

@keyframes heartPopAnim {
  0% { transform: scale(0) translateY(10px); }
  50% { transform: scale(1.3) translateY(-10px); }
  100% { transform: scale(1) translateY(-5px); }
}

/* --- 毛玻璃对话气泡 --- */
.dialogue-bubble {
  position: absolute;
  bottom: 145px;
  right: -8px;
  width: 200px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06), 0 1px 3px rgba(0, 0, 0, 0.02);
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
  text-align: justify;
}

.bubble-arrow {
  position: absolute;
  bottom: -6px;
  right: 56px;
  width: 12px;
  height: 12px;
  background: rgba(255, 255, 255, 0.85);
  border-right: 1px solid rgba(255, 255, 255, 0.5);
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
  transform: rotate(45deg);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

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