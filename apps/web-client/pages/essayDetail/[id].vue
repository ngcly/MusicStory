<template>
  <div class="container min-h-screen py-8 px-4 relative" :class="{ 'zen-active': isZen }">
    <!-- 顶部固定滚动进度条 -->
    <div
      v-if="!isZen"
      class="fixed top-14 left-0 h-[3px] bg-gradient-to-r from-teal-400 to-emerald-500 z-50 transition-all duration-100"
      :style="{ width: progress + '%' }"
    ></div>

    <div class="max-w-[1140px] mx-auto">
      <!-- 详情页双栏 -->
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- 左侧文章主体 (70% 宽) -->
        <div class="w-full lg:w-[68%]" :class="{ 'zen-main-width': isZen }">
          <div class="bg-white/60 backdrop-blur-sm border border-slate-200/50 rounded-2xl p-6 md:p-8 shadow-sm transition" :class="{ 'zen-paper': isZen }">
            <!-- 退出沉浸按钮 -->
            <button
              v-if="isZen"
              @click="toggleZen"
              class="fixed top-8 right-8 z-50 flex items-center gap-2 px-4 py-2 bg-slate-900/90 text-white rounded-full shadow-lg hover:bg-slate-800 transition"
            >
              <Minimize2Icon class="w-4 h-4" /> 退出沉浸
            </button>

            <!-- 文章头部 -->
            <div class="mb-6">
              <h1 class="font-bold text-slate-800 text-2xl md:text-3.5xl mb-4 font-serif leading-snug">
                {{ essayDetail.title }}
              </h1>
              <div class="flex items-center gap-4 text-xs text-slate-400 pb-6 border-b border-slate-100">
                <span class="flex items-center gap-1"><UserIcon class="w-3.5 h-3.5" /> {{ essayDetail.user?.username || '佚名' }}</span>
                <span class="flex items-center gap-1"><EyeIcon class="w-3.5 h-3.5" /> {{ essayDetail.readNum || 0 }} 阅读</span>
                <span class="flex items-center gap-1"><ClockIcon class="w-3.5 h-3.5" /> {{ essayDetail.createdAt ? new Date(essayDetail.createdAt).toLocaleDateString() : '刚刚' }}</span>
              </div>
            </div>

            <!-- 正文区域 (Markdown 渲染) -->
            <div class="essay-body leading-[1.9] text-slate-800 font-serif text-[16px] antialiased" v-html="essayDetail.content"></div>
          </div>

          <!-- 评论区容器 (沉浸模式下隐藏) -->
          <div v-if="!isZen" class="mt-8 bg-white/60 backdrop-blur-sm border border-slate-200/50 rounded-2xl p-6 shadow-sm">
            <h3 class="font-bold text-slate-800 text-sm mb-4">全部评论</h3>
            <!-- 评论列表 (简单 mock 占位) -->
            <div class="py-6 text-center text-slate-400 text-xs">
              这里是读者的乐章与心声...（评论区正在升级中）
            </div>
          </div>
        </div>

        <!-- 右侧侧边栏 (30% 宽，沉浸模式下隐藏) -->
        <div v-if="!isZen" class="w-full lg:w-[28%] flex flex-col gap-6">
          <!-- 开启沉浸阅读悬浮按钮 -->
          <button
            @click="toggleZen"
            class="w-full h-11 inline-flex items-center justify-center gap-2 bg-teal-600 hover:bg-teal-500 text-white font-semibold rounded-xl shadow-md transition"
          >
            <Maximize2Icon class="w-4 h-4" /> 开启禅意沉浸阅读
          </button>

          <!-- 挂件 1: 作者信息卡片 -->
          <div class="bg-white/70 backdrop-blur-md border border-slate-200/50 rounded-xl p-5 shadow-sm">
            <div class="flex items-center gap-3 mb-4">
              <img
                :src="'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=80&q=80'"
                alt="Author Avatar"
                class="w-10 h-10 rounded-full border object-cover"
              />
              <div>
                <span class="block text-xs font-semibold text-slate-800">{{ essayDetail.user?.username || '佚名' }}</span>
                <span class="block text-[10px] text-slate-400">音书签约写作者</span>
              </div>
            </div>
            <p class="text-xs text-slate-500 leading-relaxed mb-4 italic">
              "笔尖流淌出音乐的音符，琴键上记录着文字的故事。"
            </p>
            <button class="w-full h-8 flex items-center justify-center text-xs font-semibold bg-slate-900 hover:bg-slate-800 text-white rounded-lg transition">
              关注作者
            </button>
          </div>

          <!-- 挂件 2: 聆听指南/配乐 -->
          <div v-if="essayDetail.musicList && essayDetail.musicList.length > 0" class="bg-white/70 backdrop-blur-md border border-slate-200/50 rounded-xl p-5 shadow-sm">
            <h3 class="flex items-center gap-2 font-bold text-slate-800 text-sm mb-3">
              <DiscIcon class="w-4 h-4 text-slate-600" /> 本文伴奏
            </h3>
            <div v-for="music in essayDetail.musicList" :key="music.id" class="flex items-center gap-3 p-2 bg-slate-50 rounded-lg">
              <img :src="music.cover" alt="Album Art" class="w-10 h-10 rounded object-cover border" />
              <div class="flex-grow min-w-0">
                <span class="block text-xs font-bold text-slate-800 truncate">{{ music.name }}</span>
                <span class="block text-[10px] text-slate-400 truncate">{{ music.artist }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { useRoute } from '#app'
import api from '@/api/api'
import {
  User as UserIcon,
  Eye as EyeIcon,
  Clock as ClockIcon,
  Maximize2 as Maximize2Icon,
  Minimize2 as Minimize2Icon,
  Disc as DiscIcon
} from 'lucide-vue-next'

definePageMeta({
  layout: 'default'
})

const route = useRoute()
const essayDetail = reactive({
  title: '',
  content: '',
  user: null,
  readNum: 0,
  createdAt: '',
  musicList: []
})

const progress = ref(0)
const isZen = ref(false)

onMounted(() => {
  const essayId = route.params.id
  if (essayId) {
    // 拉取文章详情
    api.essays('', '/' + essayId).then(response => {
      Object.assign(essayDetail, response.data)
      // 增加一次阅读量
      api.readEssay(essayId)
    }).catch(err => console.error(err))
  }

  // 监听网页滚动计算进度
  if (process.client) {
    window.addEventListener('scroll', handleProgress)
  }
})

onBeforeUnmount(() => {
  if (process.client) {
    window.removeEventListener('scroll', handleProgress)
  }
})

function handleProgress() {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight
  const winHeight = window.innerHeight
  const scrollPercent = (scrollTop / (docHeight - winHeight)) * 100
  progress.value = Math.min(Math.max(scrollPercent, 0), 100)
}

function toggleZen() {
  isZen.value = !isZen.value
  if (process.client) {
    if (isZen.value) {
      document.body.style.overflow = 'hidden'
    } else {
      document.body.style.overflow = ''
    }
  }
}
</script>

<style scoped>
.container {
  transition: all 0.3s ease;
}

/* --- 沉浸式 Zen 模式样式层 --- */
.zen-active {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 10000;
  background-color: #fbf9f6 !important; /* 宣纸白 */
  overflow-y: auto !important;
  padding-top: 40px;
  padding-bottom: 40px;
}
.zen-main-width {
  width: 100% !important;
  max-width: 740px !important;
  margin: 0 auto;
}
.zen-paper {
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
  padding: 0 !important;
}

/* --- 长文段落细节修饰 --- */
.essay-body :deep(p) {
  margin-bottom: 24px;
  text-align: justify;
  text-justify: inter-word;
}
.essay-body :deep(h2) {
  font-size: 20px;
  font-weight: 700;
  margin: 36px 0 16px;
  color: #1e293b;
}
.essay-body :deep(h3) {
  font-size: 18px;
  font-weight: 700;
  margin: 28px 0 12px;
}
.essay-body :deep(blockquote) {
  border-left: 4px solid #1abc9c;
  padding-left: 20px;
  margin: 24px 0;
  color: #475569;
  font-style: italic;
}
.essay-body :deep(img) {
  max-width: 100%;
  border-radius: 12px;
  margin: 30px auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
}
</style>
