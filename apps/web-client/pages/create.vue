<template>
  <div class="create-container max-w-[1140px] mx-auto py-8 px-4">
    <!-- 头部操作条 -->
    <div class="flex items-center justify-between mb-8">
      <NuxtLink to="/" class="h-9 px-4 inline-flex items-center gap-1.5 border border-slate-300 hover:bg-slate-50 text-slate-700 text-sm font-semibold rounded-full transition">
        <ArrowLeftIcon class="w-4 h-4" /> 返回首页
      </NuxtLink>
      <div class="flex items-center gap-3">
        <button
          @click="saveDraft"
          :disabled="loading"
          class="h-9 px-5 inline-flex items-center justify-center bg-slate-100 hover:bg-slate-200 text-slate-700 text-sm font-semibold rounded-full transition"
        >
          保存草稿
        </button>
        <button
          @click="publish"
          :disabled="loading"
          class="h-9 px-5 inline-flex items-center justify-center bg-slate-900 hover:bg-slate-800 text-white text-sm font-semibold rounded-full transition"
        >
          发布文章
        </button>
      </div>
    </div>

    <!-- 提示通知 -->
    <div v-if="statusMsg" class="mb-6 p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs rounded-xl flex items-center gap-2">
      <CheckCircleIcon class="w-4 h-4" />
      <span>{{ statusMsg }}</span>
    </div>

    <!-- 文章表单 -->
    <div class="bg-white/70 backdrop-blur-md border border-slate-200/50 rounded-2xl p-6 md:p-8 shadow-sm mb-6">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <!-- 分类选择 -->
        <div class="flex flex-col gap-2">
          <label class="text-xs font-bold text-slate-500">分类列表</label>
          <select
            v-model="formData.classifyId"
            class="h-10 px-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-teal-500 focus:border-teal-500 text-sm bg-white"
          >
            <option value="" disabled>请选择分类</option>
            <option
              v-for="item in classify"
              :key="item.id"
              :value="item.id"
            >
              {{ item.name }}
            </option>
          </select>
        </div>

        <!-- 标题输入 -->
        <div class="md:col-span-2 flex flex-col gap-2">
          <label class="text-xs font-bold text-slate-500">文章标题</label>
          <input
            type="text"
            v-model="formData.title"
            placeholder="请输入文章标题 (不超过 50 字)"
            class="h-10 px-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-teal-500 focus:border-teal-500 text-sm"
          />
        </div>
      </div>

      <!-- 添加音乐弹窗控制 -->
      <div class="mb-8">
        <Dialog v-model:open="dialogVisible">
          <DialogTrigger as-child>
            <button class="h-9 px-4 inline-flex items-center gap-1.5 border border-slate-300 hover:bg-slate-50 text-slate-700 text-xs font-semibold rounded-lg transition">
              <MusicIcon class="w-3.5 h-3.5" /> 
              {{ hasMusicAttached ? '已添加音乐信息 (点击修改)' : '为文章添加配乐' }}
            </button>
          </DialogTrigger>
          <DialogContent class="sm:max-w-md">
            <DialogHeader>
              <DialogTitle class="text-slate-800 font-serif">添加配乐信息</DialogTitle>
            </DialogHeader>
            <div class="grid gap-4 py-4 text-sm">
              <div class="grid grid-cols-4 items-center gap-4">
                <label class="text-right text-xs font-semibold text-slate-500">歌名</label>
                <input v-model="formData.musicList[0].name" placeholder="请输入歌名" class="col-span-3 h-9 px-3 border rounded-lg text-xs focus:outline-none focus:ring-teal-500" />
              </div>
              <div class="grid grid-cols-4 items-center gap-4">
                <label class="text-right text-xs font-semibold text-slate-500">歌手</label>
                <input v-model="formData.musicList[0].artist" placeholder="请输入歌手" class="col-span-3 h-9 px-3 border rounded-lg text-xs focus:outline-none focus:ring-teal-500" />
              </div>
              <div class="grid grid-cols-4 items-center gap-4">
                <label class="text-right text-xs font-semibold text-slate-500">歌曲链接</label>
                <input v-model="formData.musicList[0].url" placeholder="MP3 链接地址" class="col-span-3 h-9 px-3 border rounded-lg text-xs focus:outline-none focus:ring-teal-500" />
              </div>
              <div class="grid grid-cols-4 items-center gap-4">
                <label class="text-right text-xs font-semibold text-slate-500">歌词</label>
                <input v-model="formData.musicList[0].lrc" placeholder="LRC 歌词链接 (可选)" class="col-span-3 h-9 px-3 border rounded-lg text-xs focus:outline-none focus:ring-teal-500" />
              </div>
              <div class="grid grid-cols-4 items-center gap-4">
                <label class="text-right text-xs font-semibold text-slate-500">专辑</label>
                <input v-model="formData.musicList[0].album" placeholder="请输入专辑 (可选)" class="col-span-3 h-9 px-3 border rounded-lg text-xs focus:outline-none focus:ring-teal-500" />
              </div>
              <div class="grid grid-cols-4 items-center gap-4">
                <label class="text-right text-xs font-semibold text-slate-500">封面图片</label>
                <input v-model="formData.musicList[0].cover" placeholder="图片 URL 地址" class="col-span-3 h-9 px-3 border rounded-lg text-xs focus:outline-none focus:ring-teal-500" />
              </div>
            </div>
            <DialogFooter>
              <button @click="dialogVisible = false" class="px-4 py-2 bg-slate-900 text-white text-xs font-semibold rounded-lg hover:bg-slate-800 transition">
                确定
              </button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      <!-- Tiptap 编辑器容器 -->
      <div class="border rounded-2xl overflow-hidden border-slate-200">
        <ClientOnly>
          <TiptapEditor
            v-model="mdContent"
            @upload-image="onUploadImg"
          />
          <template #fallback>
            <div class="h-96 flex items-center justify-center text-slate-400 text-sm">
              <Loader2Icon class="w-6 h-6 animate-spin mr-2" /> 正在装载宣纸画布...
            </div>
          </template>
        </ClientOnly>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from '#app'
import api from '@/api/api'
import TiptapEditor from '@/components/editor/TiptapEditor.vue'
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter
} from '@/components/ui/dialog'
import {
  ArrowLeft as ArrowLeftIcon,
  Music as MusicIcon,
  Loader2 as Loader2Icon,
  CheckCircle as CheckCircleIcon
} from 'lucide-vue-next'

definePageMeta({
  layout: 'default'
})

const router = useRouter()

const dialogVisible = ref(false)
const mdContent = ref('')
const classify = ref([])
const loading = ref(false)
const statusMsg = ref('')

const formData = reactive({
  id: '',
  title: '',
  content: '',
  synopsis: '',
  classifyId: '',
  musicList: [{
    name: '',
    artist: '',
    url: '',
    lrc: '',
    album: '',
    cover: ''
  }]
})

const hasMusicAttached = computed(() => {
  const m = formData.musicList[0]
  return !!(m.name && m.url)
})

onMounted(() => {
  // 加载分类列表
  api.classify().then(response => {
    classify.value = response.data || []
  }).catch(err => console.error(err))
})

// 处理图片上传
function onUploadImg(file, insertImageCallback) {
  const payload = new FormData()
  payload.append('file', file)
  api.upload(payload, '/img').then(response => {
    if (response.data) {
      insertImageCallback(response.data)
    }
  }).catch(err => console.error('Image upload failed:', err))
}

// 抽取纯文本作为简介
function getSynopsis(htmlContent) {
  const text = htmlContent.replace(/<[^>]*>/g, '') // 简单剥离 HTML tags
  if (text.length > 200) {
    return text.substring(0, 200) + '...'
  }
  return text
}

// 保存草稿
function saveDraft() {
  if (!formData.title || !formData.classifyId) {
    alert('请填写标题并选择分类！')
    return
  }
  loading.value = true
  statusMsg.value = ''

  formData.content = mdContent.value
  formData.synopsis = getSynopsis(mdContent.value)

  // 如果音乐列表中的项目都为空，清空列表避免提交空对象报错
  const m = formData.musicList[0]
  const cleanData = JSON.parse(JSON.stringify(formData))
  if (!m.name && !m.url) {
    cleanData.musicList = []
  }

  api.create(cleanData).then(response => {
    formData.id = response.data
    loading.value = false
    statusMsg.value = '草稿保存成功！'
    setTimeout(() => {
      statusMsg.value = ''
    }, 3000)
  }).catch(err => {
    loading.value = false
    alert(err?.msg || '保存草稿失败')
  })
}

// 发布文章
function publish() {
  if (!formData.title || !formData.classifyId) {
    alert('请填写标题并选择分类！')
    return
  }
  loading.value = true
  statusMsg.value = ''

  formData.content = mdContent.value
  formData.synopsis = getSynopsis(mdContent.value)

  const m = formData.musicList[0]
  const cleanData = JSON.parse(JSON.stringify(formData))
  if (!m.name && !m.url) {
    cleanData.musicList = []
  }

  // 后端发布流程：如果已经保存过（具有id），则更新并审核，否则直接创建并审核
  if (formData.id) {
    api.altessay(cleanData).then(() => {
      loading.value = false
      statusMsg.value = '文章发布成功，即将返回首页。'
      setTimeout(() => {
        router.push('/')
      }, 2000)
    }).catch(err => {
      loading.value = false
      alert(err?.msg || '发布文章失败')
    })
  } else {
    // 先保存草稿获取 id，再提审发布
    api.create(cleanData).then(response => {
      cleanData.id = response.data
      api.altessay(cleanData).then(() => {
        loading.value = false
        statusMsg.value = '文章发布成功，即将返回首页。'
        setTimeout(() => {
          router.push('/')
        }, 2000)
      })
    }).catch(err => {
      loading.value = false
      alert(err?.msg || '发布文章失败')
    })
  }
}
</script>

<style scoped>
.create-container {
  min-height: calc(100vh - 56px);
}
</style>
