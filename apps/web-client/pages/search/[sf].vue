<template>
  <div class="container max-w-[1140px] mx-auto py-8 px-4">
    <div class="flex flex-col gap-6">
      <!-- 搜索头部状态 -->
      <div class="relative border-b border-slate-200 pb-4 mb-4">
        <div class="text-sm font-semibold text-slate-800 flex items-center gap-2">
          <span>关于“<span class="text-teal-600 font-bold">{{ keyWord }}</span>”的检索结果</span>
        </div>
        <div class="absolute right-0 top-0 text-xs text-slate-400">
          共找到 {{ total }} 个匹配项
        </div>
      </div>

      <!-- 空结果提示 -->
      <div v-show="essays.length <= 0 && !loading" class="py-16 text-center text-slate-400 text-sm">
        抱歉，暂未搜到相关内容！
      </div>

      <div v-show="loading" class="py-16 text-center text-slate-400 text-sm flex flex-col items-center gap-2">
        <Loader2Icon class="w-6 h-6 animate-spin text-teal-600" />
        <span>正在从书架中检索...</span>
      </div>

      <!-- 搜索结果列表 -->
      <ul v-show="!loading && essays.length > 0" class="flex flex-col gap-6">
        <li
          v-for="(essay, indx) in essays"
          :key="indx"
          class="bg-white/60 backdrop-blur-sm border border-slate-200/50 rounded-xl p-5 hover:bg-white transition-all duration-200 hover:shadow-sm"
        >
          <div class="content">
            <!-- 作者栏 -->
            <div class="flex items-center gap-2 mb-3 text-xs text-slate-500">
              <img :src="defaultAvatar" alt="Avatar" class="w-6 h-8 rounded-full object-cover border" />
              <span class="font-semibold text-slate-700">{{ essay.content.author }}</span>
              <span class="text-slate-300">|</span>
              <span>1年前发布</span>
            </div>

            <!-- 标题 (支持 Highlight 高亮) -->
            <NuxtLink
              :to="'/essayDetail/' + essay.id"
              v-html="essay.highlightFields?.title ? essay.highlightFields.title[0] : essay.content.title"
              class="block font-bold text-slate-800 hover:text-teal-600 text-lg mb-2 transition"
            ></NuxtLink>

            <!-- 摘要 (支持 Highlight 高亮) -->
            <p
              class="text-sm text-slate-500 line-clamp-3 mb-4 leading-relaxed"
              v-html="essay.highlightFields?.content ? essay.highlightFields.content[0] : essay.content.content"
            ></p>

            <!-- 元信息 -->
            <div class="flex items-center gap-3 text-xs text-slate-400">
              <span class="flex items-center gap-1"><EyeIcon class="w-3.5 h-3.5" /> 1 阅读</span>
              <span class="flex items-center gap-1"><MessageSquareIcon class="w-3.5 h-3.5" /> 1 评论</span>
            </div>
          </div>
        </li>
      </ul>

      <!-- 极简分页 -->
      <div v-show="total > 10" class="flex items-center justify-center gap-2 mt-8">
        <button
          @click="prevPage"
          :disabled="page === 1"
          class="h-8 px-3 inline-flex items-center justify-center bg-slate-100 hover:bg-slate-200 disabled:opacity-50 text-xs font-semibold rounded-lg transition"
        >
          上一页
        </button>
        <span class="text-xs text-slate-500">第 {{ page }} 页</span>
        <button
          @click="nextPage"
          :disabled="essays.length < 10"
          class="h-8 px-3 inline-flex items-center justify-center bg-slate-100 hover:bg-slate-200 disabled:opacity-50 text-xs font-semibold rounded-lg transition"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute } from '#app'
import api from '@/api/api'
import defaultAvatar from '@/assets/img/avatar.png'
import {
  Loader2 as Loader2Icon,
  Eye as EyeIcon,
  MessageSquare as MessageSquareIcon
} from 'lucide-vue-next'

definePageMeta({
  layout: 'default'
})

const route = useRoute()
const page = ref(1)
const total = ref(0)
const keyWord = ref('')
const essays = ref([])
const loading = ref(false)

function load() {
  if (!keyWord.value) return
  loading.value = true
  api.essaySearch('', '/10/' + page.value + '/' + keyWord.value).then(response => {
    essays.value = response.data || []
    total.value = essays.value.length
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

onMounted(() => {
  keyWord.value = route.params.sf
  load()
})

watch(() => route.params.sf, (newVal) => {
  if (newVal) {
    keyWord.value = newVal
    page.value = 1
    load()
  }
})

function prevPage() {
  if (page.value > 1) {
    page.value -= 1
    load()
  }
}

function nextPage() {
  if (essays.value.length === 10) {
    page.value += 1
    load()
  }
}
</script>

<style scoped>
.container {
  min-height: calc(100vh - 56px);
}
</style>
