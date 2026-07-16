<template>
  <div class="index-gallery">
    <!-- 极慢微移壁画背景 -->
    <div class="mural-bg"></div>

    <!-- 左上角返回首页 -->
    <div class="home-nav">
      <NuxtLink to="/">
        <HomeIcon class="w-4 h-4" /> 首页
      </NuxtLink>
    </div>

    <!-- 右上角极简音乐控制器 -->
    <div class="minimal-player" @mouseenter="showVolume = true" @mouseleave="showVolume = false">
      <audio ref="audioRef" :src="playlist[currentTrackIndex]" loop></audio>

      <!-- 音量控制 -->
      <transition name="slide-volume">
        <div v-show="showVolume" class="volume-control">
          <Volume2Icon class="w-4 h-4 text-white/70" />
          <input
            type="range"
            min="0"
            max="1"
            step="0.05"
            v-model="volume"
            @input="adjustVolume"
            class="volume-slider"
          />
        </div>
      </transition>

      <!-- 下一首按钮 -->
      <button @click="nextTrack" class="control-btn" title="下一首">
        <SkipForwardIcon class="w-4 h-4" />
      </button>

      <!-- 黑胶播放按钮 -->
      <button @click="togglePlay" class="vinyl-btn" :class="{ 'is-playing': isPlaying }" title="播放/暂停">
        <DiscIcon class="w-4 h-4" />
      </button>
    </div>

    <!-- 精选文章主体卡片 -->
    <div class="main-container">
      <div class="featured-card">
        <div class="card-header">
          <span class="header-tag"><StarIcon class="w-3.5 h-3.5" /> 优选推介</span>
          <span class="header-subtitle">情景交融 · 倾听文字</span>
        </div>

        <div v-if="loading" class="card-loading">
          <Loader2Icon class="w-5 h-5 animate-spin mx-auto mb-2" /> 正在加载精选内容...
        </div>

        <div v-else-if="featuredEssays.length > 0" class="essays-carousel">
          <div v-for="(essay, index) in featuredEssays" :key="essay.id" class="essay-item animate__animated animate__fadeIn">
            <span class="essay-tag">其{{ index === 0 ? '一' : '二' }}</span>
            <h2 class="essay-title">{{ essay.title }}</h2>
            <p class="essay-author">— {{ essay.username || '佚名' }}</p>
            <p class="essay-synopsis" v-html="essay.synopsis"></p>
            <div class="essay-action">
              <NuxtLink :to="'/essayDetail/' + essay.id" class="read-btn">
                阅全文 <ChevronRightIcon class="w-4 h-4" />
              </NuxtLink>
            </div>
          </div>
        </div>

        <div v-else class="card-empty">
          <p>暂无精选文章，快去首页看看吧。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useStore } from '@/store'
import api from '@/api/api'
import {
  Home as HomeIcon,
  Volume2 as Volume2Icon,
  SkipForward as SkipForwardIcon,
  Disc as DiscIcon,
  Star as StarIcon,
  Loader2 as Loader2Icon,
  ChevronRight as ChevronRightIcon
} from 'lucide-vue-next'

// 禁用 Nuxt 默认 Layout，此页面为全屏沉浸画廊
definePageMeta({
  layout: false
})

// 播放列表（原有两条好歌）
const playlist = [
  'http://music.163.com/song/media/outer/url?id=453843751.mp3',
  'http://music.163.com/song/media/outer/url?id=103035.mp3'
]

const currentTrackIndex = ref(0)
const audioRef = ref(null)
const isPlaying = ref(false)
const showVolume = ref(false)
const volume = ref(0.5)

const store = useStore()
const featuredEssays = ref([])
const loading = ref(true)

// 获取精选文章（获取前2条）
onMounted(() => {
  api.essays('', '/2/1').then(response => {
    if (response.data && response.data.length > 0) {
      featuredEssays.value = response.data
    }
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
})

function togglePlay() {
  if (!audioRef.value) return
  if (isPlaying.value) {
    audioRef.value.pause()
    store.isMusicPlaying = false
  } else {
    audioRef.value.play().then(() => {
      store.isMusicPlaying = true
    }).catch(err => console.log('Autoplay blocked:', err))
  }
  isPlaying.value = !isPlaying.value
}

function nextTrack() {
  currentTrackIndex.value = (currentTrackIndex.value + 1) % playlist.length
  isPlaying.value = false
  store.isMusicPlaying = false
  setTimeout(() => {
    if (audioRef.value) {
      audioRef.value.load()
      audioRef.value.play().then(() => {
        isPlaying.value = true
        store.isMusicPlaying = true
      }).catch(err => console.log('Playback error:', err))
    }
  }, 100)
}

function adjustVolume() {
  if (audioRef.value) {
    audioRef.value.volume = volume.value
  }
}

onBeforeUnmount(() => {
  if (audioRef.value) {
    audioRef.value.pause()
  }
  store.isMusicPlaying = false
})
</script>

<style scoped>
.index-gallery {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* --- 极慢微移壁画背景 --- */
.mural-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('~/assets/img/bg-min.jpg');
  background-size: cover;
  background-position: center;
  filter: brightness(0.7) contrast(1.05); /* 稍微降低亮度，衬托文字 */
  animation: kenburns 35s ease-in-out infinite alternate;
  z-index: 1;
}

@keyframes kenburns {
  0% { transform: scale(1) translate(0, 0); }
  100% { transform: scale(1.08) translate(-1%, -1%); }
}

/* --- 左上角返回首页 --- */
.home-nav {
  position: absolute;
  top: 32px;
  left: 32px;
  z-index: 10;
}
.home-nav a {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 15px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  padding: 8px 16px;
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s;
}
.home-nav a:hover {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  transform: translateX(-2px);
}

/* --- 右上角极简播放器 --- */
.minimal-player {
  position: absolute;
  top: 32px;
  right: 32px;
  z-index: 10;
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(0, 0, 0, 0.45);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  padding: 6px 14px;
  border-radius: 24px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.25);
}
.control-btn {
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: color 0.2s;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.control-btn:hover {
  color: #fff;
}
.vinyl-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: 2px dashed rgba(255, 255, 255, 0.5);
  background: #18181c;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  padding: 0;
}
.vinyl-btn.is-playing {
  animation: rotate-disc 5s linear infinite;
  border-style: solid;
  border-color: #1abc9c;
  color: #1abc9c;
}
@keyframes rotate-disc {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* --- 音量调节滑动条 --- */
.volume-control {
  display: flex;
  align-items: center;
  gap: 8px;
  padding-right: 4px;
}
.volume-slider {
  width: 70px;
  height: 3px;
  background: rgba(255, 255, 255, 0.3);
  outline: none;
  border-radius: 2px;
  cursor: pointer;
}
.slide-volume-enter-active,
.slide-volume-leave-active {
  transition: all 0.3s ease;
}
.slide-volume-enter-from,
.slide-volume-leave-to {
  opacity: 0;
  transform: translateX(10px);
}

/* --- 精选文章主体卡片 --- */
.main-container {
  position: relative;
  z-index: 5;
  width: 90%;
  max-width: 680px;
}
.featured-card {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 32px;
  color: #fff;
  box-shadow: 0 15px 45px rgba(0, 0, 0, 0.3);
}
.card-header {
  display: flex;
  flex-direction: column;
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
  padding-bottom: 16px;
  margin-bottom: 24px;
}
.header-tag {
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 2px;
  color: #1abc9c;
  font-weight: 600;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.header-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 1px;
}
.card-loading, .card-empty {
  text-align: center;
  padding: 40px 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 15px;
}
.essays-carousel {
  display: flex;
  flex-direction: column;
  gap: 32px;
}
.essay-item {
  position: relative;
  animation-duration: 0.8s;
}
.essay-tag {
  position: absolute;
  left: 0;
  top: -16px;
  font-size: 10px;
  background: rgba(26, 188, 156, 0.2);
  border: 1px solid rgba(26, 188, 156, 0.4);
  color: #1abc9c;
  padding: 2px 8px;
  border-radius: 10px;
}
.essay-title {
  margin-top: 12px;
  margin-bottom: 6px;
  font-family: 'Noto Serif SC', serif;
  font-size: 24px;
  font-weight: 700;
  color: #fff;
  line-height: 1.4;
}
.essay-author {
  margin-top: 0;
  margin-bottom: 12px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
  font-style: italic;
}
.essay-synopsis {
  font-size: 14.5px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-align: justify;
}
.essay-action {
  display: flex;
  justify-content: flex-end;
}
.read-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13.5px;
  color: #1abc9c;
  text-decoration: none;
  font-weight: 600;
  transition: transform 0.2s, color 0.2s;
}
.read-btn:hover {
  color: #2ed573;
  transform: translateX(4px);
}
</style>
