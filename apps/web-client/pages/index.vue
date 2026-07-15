<template>
  <div class="container py-8 px-4">
    <div class="max-w-[1140px] mx-auto">
      <!-- 轮播图与公告行 -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <!-- 3D Card 轮播图 (左侧 2/3) -->
        <div class="md:col-span-2 relative h-[280px] overflow-hidden rounded-2xl bg-slate-900/5 shadow-inner">
          <div v-if="carousel.length === 0" class="w-full h-full flex items-center justify-center text-slate-400 text-sm">
            暂无轮播图资源
          </div>
          <div v-else class="relative w-full h-full flex items-center justify-center">
            <!-- 幻灯片渲染 -->
            <div
              v-for="(item, index) in carousel"
              :key="item.id"
              class="absolute w-[70%] h-[90%] rounded-xl overflow-hidden transition-all duration-500 ease-out shadow-lg"
              :class="getCarouselClass(index)"
            >
              <a :href="item.forwardUrl" target="_blank" class="block w-full h-full">
                <img :src="item.imageUrl" alt="Carousel image" class="w-full h-full object-cover" />
              </a>
            </div>

            <!-- 控制按钮 -->
            <button @click="prevSlide" class="absolute left-4 z-20 w-8 h-8 flex items-center justify-center bg-white/70 hover:bg-white text-slate-700 rounded-full shadow transition">
              <ChevronLeftIcon class="w-5 h-5" />
            </button>
            <button @click="nextSlide" class="absolute right-4 z-20 w-8 h-8 flex items-center justify-center bg-white/70 hover:bg-white text-slate-700 rounded-full shadow transition">
              <ChevronRightIcon class="w-5 h-5" />
            </button>
          </div>
        </div>

        <!-- 公告栏 (右侧 1/3) -->
        <div class="flex flex-col justify-center bg-white/70 backdrop-blur-md border border-white/40 rounded-2xl p-6 shadow-sm">
          <div class="flex items-center gap-2 mb-4">
            <span class="p-2 bg-amber-50 rounded-xl">
              <MegaphoneIcon class="w-5 h-5 text-amber-500 animate-bounce" />
            </span>
            <div>
              <h3 class="font-bold text-slate-800 text-sm">最新公告</h3>
              <p class="text-xs text-slate-400">Marquee notice banner</p>
            </div>
          </div>
          <div class="h-28 overflow-hidden relative">
            <div v-if="notice.length === 0" class="text-slate-400 text-xs py-4 text-center">
              暂无最新公告发布。
            </div>
            <div v-else class="flex flex-col gap-2">
              <Acquaint :content="notice.join(' ')" :speed="60">
                <span v-for="(txt, idx) in notice" :key="idx" class="inline-flex items-center gap-2 mr-16 text-slate-600 text-sm">
                  <StarIcon class="w-3.5 h-3.5 text-amber-400 fill-amber-400" />
                  {{ txt }}
                </span>
              </Acquaint>
            </div>
          </div>
        </div>
      </div>

      <!-- 分割线 -->
      <hr class="border-slate-200 my-8" />

      <!-- 下方双栏 -->
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- 左侧文章流 (70% 宽) -->
        <div class="w-full lg:w-[68%]">
          <ul class="flex flex-col gap-6">
            <li v-for="(essay, indx) in essays" :key="indx">
              <ul class="flex flex-col gap-6">
                <li
                  v-for="(item, index) in essay"
                  :key="index"
                  class="group relative bg-white/60 backdrop-blur-sm border border-slate-200/50 hover:border-slate-300 hover:bg-white/95 rounded-xl p-5 hover:-translate-y-1 shadow-sm hover:shadow-md transition-all duration-300 border-l-4 border-l-teal-500/50 hover:border-l-teal-500"
                  :class="{ 'border-l': item.cover }"
                >
                  <div class="flex gap-5 items-center">
                    <!-- 左侧文字 -->
                    <div class="flex-grow">
                      <NuxtLink :to="'/essayDetail/' + item.id" class="block font-bold text-slate-800 hover:text-teal-600 text-lg mb-2 transition">
                        {{ item.title }}
                      </NuxtLink>
                      <p class="text-sm text-slate-500 line-clamp-3 mb-4 leading-relaxed" v-html="item.synopsis"></p>

                      <!-- Meta 数据 -->
                      <div class="flex items-center gap-4 text-xs text-slate-400">
                        <span class="flex items-center gap-1 hover:text-teal-500 cursor-pointer transition">
                          <UserIcon class="w-3.5 h-3.5" /> {{ item.username }}
                        </span>
                        <span class="flex items-center gap-1">
                          <EyeIcon class="w-3.5 h-3.5" /> {{ item.read_num }}
                        </span>
                        <span class="flex items-center gap-1">
                          <ClockIcon class="w-3.5 h-3.5" /> {{ item.updated_time || item.updated_at || '刚刚' }}
                        </span>
                      </div>
                    </div>

                    <!-- 右侧封面图 -->
                    <div v-if="item.cover" class="flex-shrink-0 w-28 h-20 rounded-lg overflow-hidden border border-slate-100 shadow-sm">
                      <img :src="item.cover" alt="Cover" class="w-full h-full object-cover group-hover:scale-105 transition duration-300" />
                    </div>
                  </div>
                </li>
              </ul>
            </li>
          </ul>

          <!-- 加载更多 -->
          <div class="mt-8 text-center">
            <button
              v-if="!noMore"
              @click="loadMore"
              :disabled="loading"
              class="w-full h-10 inline-flex items-center justify-center px-6 bg-slate-200 hover:bg-slate-300 disabled:bg-slate-100 disabled:text-slate-400 text-slate-700 font-semibold text-sm rounded-full transition cursor-pointer"
            >
              <span v-if="loading" class="flex items-center gap-2">
                <Loader2Icon class="w-4 h-4 animate-spin" /> 加载中...
              </span>
              <span v-else>阅读更多</span>
            </button>
            <p v-else class="text-xs text-slate-400 py-4">没有更多了</p>
          </div>
        </div>

        <!-- 右侧侧边栏 (30% 宽) -->
        <div class="w-full lg:w-[28%] flex flex-col gap-6">
          <!-- Widget 1: 关于音书 -->
          <div class="bg-white/70 backdrop-blur-md border border-slate-200/50 rounded-xl p-5 shadow-sm hover:shadow-md transition">
            <h3 class="flex items-center gap-2 font-bold text-slate-800 text-sm mb-3">
              <CompassIcon class="w-4 h-4 text-slate-600" /> 关于音书
            </h3>
            <p class="text-xs text-slate-500 leading-relaxed">
              音书是一个将音乐的旋律与文字的温度交织融合的文艺深阅读社区。在这里，聆听旋律背后的故事，记录文字流淌出的乐章。
            </p>
          </div>

          <!-- Widget 2: 热门专题 -->
          <div class="bg-white/70 backdrop-blur-md border border-slate-200/50 rounded-xl p-5 shadow-sm hover:shadow-md transition">
            <h3 class="flex items-center gap-2 font-bold text-slate-800 text-sm mb-3">
              <HashIcon class="w-4 h-4 text-slate-600" /> 热门专题
            </h3>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="tag in hotTopics"
                :key="tag"
                class="px-2.5 py-1 text-xs bg-slate-100 hover:bg-slate-900 hover:text-white border border-slate-200 rounded-full cursor-pointer transition"
              >
                {{ tag }}
              </span>
            </div>
          </div>

          <!-- Widget 3: 推荐作者 -->
          <div class="bg-white/70 backdrop-blur-md border border-slate-200/50 rounded-xl p-5 shadow-sm hover:shadow-md transition">
            <h3 class="flex items-center gap-2 font-bold text-slate-800 text-sm mb-3">
              <SparklesIcon class="w-4 h-4 text-slate-600" /> 推荐作者
            </h3>
            <ul class="flex flex-col gap-3">
              <li v-for="author in recommendedAuthors" :key="author.name" class="flex items-center justify-between">
                <div class="flex items-center gap-3">
                  <img :src="author.avatar" :alt="author.name" class="w-8 h-8 rounded-full border object-cover" />
                  <div>
                    <span class="block text-xs font-semibold text-slate-800">{{ author.name }}</span>
                    <span class="block text-[10px] text-slate-400">{{ author.desc }}</span>
                  </div>
                </div>
                <button class="h-6 px-2.5 text-[10px] font-semibold bg-slate-100 hover:bg-slate-200 border rounded-full transition">
                  关注
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import {
  ChevronLeft as ChevronLeftIcon,
  ChevronRight as ChevronRightIcon,
  Megaphone as MegaphoneIcon,
  Star as StarIcon,
  User as UserIcon,
  Eye as EyeIcon,
  Clock as ClockIcon,
  Loader2 as Loader2Icon,
  Compass as CompassIcon,
  Hash as HashIcon,
  Sparkles as SparklesIcon
} from 'lucide-vue-next'

definePageMeta({
  layout: 'default'
})

const carousel = ref([])
const notice = ref([])
const essays = ref([])
const page = ref(1)
const loading = ref(false)
const noMore = ref(false)

// 3D Card Deck 状态
const activeCarouselIndex = ref(0)

// 推荐作者 & 热门专题 mock
const hotTopics = ['民谣故事', '古典沉思', '流行前线', '摇滚余温', '治愈纯音', '古风雅韵']
const recommendedAuthors = [
  { name: '云深不知处', desc: '写了 24k 字 · 230 个喜欢', avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=80&q=80' },
  { name: '晚风歌谣', desc: '写了 12k 字 · 140 个喜欢', avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=80&q=80' },
  { name: '琴瑟和鸣', desc: '写了 45k 字 · 560 个喜欢', avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=80&q=80' }
]

onMounted(() => {
  api.carousel().then(response => {
    carousel.value = response.data || []
  }).catch(err => console.error(err))

  api.notice().then(response => {
    if (response.data) {
      notice.value = response.data.map(item => item.content)
    }
  }).catch(err => console.error(err))

  loadInitial()
})

function loadInitial() {
  loading.value = true
  api.essays('', '/10/' + page.value).then(response => {
    if (response.data && response.data.length > 0) {
      essays.value.push(response.data)
      if (response.data.length < 10) {
        noMore.value = true
      }
    } else {
      noMore.value = true
    }
    loading.value = false
  }).catch(err => {
    console.error(err)
    loading.value = false
  })
}

function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true
  page.value += 1
  api.essays('', '/10/' + page.value).then(response => {
    if (response.data && response.data.length > 0) {
      essays.value.push(response.data)
      if (response.data.length < 10) {
        noMore.value = true
      }
    } else {
      noMore.value = true
    }
    loading.value = false
  }).catch(err => {
    console.error(err)
    loading.value = false
  })
}

// 3D Card Carousel 逻辑
function nextSlide() {
  if (carousel.value.length === 0) return
  activeCarouselIndex.value = (activeCarouselIndex.value + 1) % carousel.value.length
}

function prevSlide() {
  if (carousel.value.length === 0) return
  activeCarouselIndex.value = (activeCarouselIndex.value - 1 + carousel.value.length) % carousel.value.length
}

function getCarouselClass(index) {
  const len = carousel.value.length
  if (len === 0) return ''
  const active = activeCarouselIndex.value

  if (index === active) {
    return 'z-10 scale-100 translate-x-0 opacity-100 cursor-pointer pointer-events-auto'
  }
  if (index === (active - 1 + len) % len) {
    return 'z-0 scale-90 -translate-x-[25%] opacity-60 pointer-events-none'
  }
  if (index === (active + 1) % len) {
    return 'z-0 scale-90 translate-x-[25%] opacity-60 pointer-events-none'
  }
  return 'scale-75 opacity-0 z-[-1] pointer-events-none'
}
</script>

<style scoped>
.container {
  min-height: calc(100vh - 56px);
}
</style>
