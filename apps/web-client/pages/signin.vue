<template>
  <div class="min-h-screen flex items-center justify-center bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white/80 backdrop-blur-md p-8 border border-slate-200/50 rounded-2xl shadow-lg">
      <div>
        <NuxtLink to="/" class="flex justify-center">
          <img src="~/assets/img/logo.png" alt="Logo" class="h-12 w-auto object-contain" />
        </NuxtLink>
        <h2 class="mt-6 text-center text-2xl font-bold text-slate-800 font-serif">
          登 录 音 书
        </h2>
        <p class="mt-2 text-center text-xs text-slate-400">
          或者
          <NuxtLink to="/signup" class="font-medium text-teal-600 hover:text-teal-500 transition">
            注册新账户
          </NuxtLink>
        </p>
      </div>

      <!-- 错误提示框 -->
      <div v-if="errorMsg" class="p-3 bg-red-50 border border-red-200 text-red-600 text-xs rounded-xl flex items-center gap-2">
        <AlertCircleIcon class="w-4 h-4 flex-shrink-0" />
        <span>{{ errorMsg }}</span>
      </div>

      <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
        <div class="rounded-md shadow-sm -space-y-px">
          <div class="relative">
            <input
              v-model="loginForm.username"
              type="text"
              required
              placeholder="用户名或邮箱"
              class="appearance-none rounded-none relative block w-full px-3 py-3 pl-10 border border-slate-300 placeholder-slate-400 text-slate-900 rounded-t-xl focus:outline-none focus:ring-teal-500 focus:border-teal-500 focus:z-10 text-sm"
            />
            <UserIcon class="absolute left-3.5 top-3.5 w-4 h-4 text-slate-400" />
          </div>
          <div class="relative">
            <input
              v-model="loginForm.password"
              type="password"
              required
              placeholder="密码"
              class="appearance-none rounded-none relative block w-full px-3 py-3 pl-10 border border-slate-300 placeholder-slate-400 text-slate-900 rounded-b-xl focus:outline-none focus:ring-teal-500 focus:border-teal-500 focus:z-10 text-sm"
            />
            <LockIcon class="absolute left-3.5 top-3.5 w-4 h-4 text-slate-400" />
          </div>
        </div>

        <div class="flex items-center justify-between text-xs">
          <label class="flex items-center text-slate-500 cursor-pointer">
            <input type="checkbox" class="h-4 w-4 text-teal-600 border-slate-300 rounded focus:ring-teal-500 mr-2" />
            记住我
          </label>
          <NuxtLink to="/help" class="font-medium text-teal-600 hover:text-teal-500">
            登录遇到问题?
          </NuxtLink>
        </div>

        <div>
          <button
            type="submit"
            :disabled="loading"
            class="group relative w-full flex justify-center py-2.5 px-4 border border-transparent text-sm font-semibold rounded-xl text-white bg-slate-900 hover:bg-slate-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-900 transition disabled:bg-slate-500"
          >
            <Loader2Icon v-if="loading" class="w-4 h-4 animate-spin mr-2" />
            登录
          </button>
        </div>
      </form>

      <!-- 社交账号登录 -->
      <div class="mt-6 border-t border-slate-200 pt-6">
        <p class="text-center text-xs text-slate-400 mb-4">社交账号登录</p>
        <div class="flex justify-center gap-4">
          <a href="#" class="w-8 h-8 flex items-center justify-center bg-slate-100 hover:bg-red-50 text-slate-500 hover:text-red-500 rounded-full transition" title="微博">
            <i class="fa fa-weibo text-sm"></i>
          </a>
          <a href="#" class="w-8 h-8 flex items-center justify-center bg-slate-100 hover:bg-emerald-50 text-slate-500 hover:text-emerald-500 rounded-full transition" title="微信">
            <i class="fa fa-weixin text-sm"></i>
          </a>
          <a href="#" class="w-8 h-8 flex items-center justify-center bg-slate-100 hover:bg-sky-50 text-slate-500 hover:text-sky-500 rounded-full transition" title="QQ">
            <i class="fa fa-qq text-sm"></i>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRoute, useRouter } from '#app'
import { useStore } from '@/store'
import md5 from 'js-md5'
import {
  User as UserIcon,
  Lock as LockIcon,
  Loader2 as Loader2Icon,
  AlertCircle as AlertCircleIcon
} from 'lucide-vue-next'

definePageMeta({
  layout: false
})

const router = useRouter()
const route = useRoute()
const store = useStore()

const loginForm = reactive({
  username: '',
  password: '',
})

const loading = ref(false)
const errorMsg = ref('')

function handleLogin() {
  if (!loginForm.username || !loginForm.password) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  errorMsg.value = ''

  const userInfo = {
    username: loginForm.username,
    password: md5(loginForm.password)
  }

  store.login(userInfo).then(() => {
    loading.value = false
    router.push(route.query.redirect || '/')
  }).catch((err) => {
    loading.value = false
    errorMsg.value = err?.msg || '登录失败，请检查用户名或密码'
  })
}
</script>
