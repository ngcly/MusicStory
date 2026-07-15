<template>
  <div class="min-h-screen flex items-center justify-center bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white/80 backdrop-blur-md p-8 border border-slate-200/50 rounded-2xl shadow-lg">
      <div>
        <NuxtLink to="/" class="flex justify-center">
          <img src="~/assets/img/logo.png" alt="Logo" class="h-12 w-auto object-contain" />
        </NuxtLink>
        <h2 class="mt-6 text-center text-2xl font-bold text-slate-800 font-serif">
          注 册 音 书
        </h2>
        <p class="mt-2 text-center text-xs text-slate-400">
          已有账户？
          <NuxtLink to="/signin" class="font-medium text-teal-600 hover:text-teal-500 transition">
            直接登录
          </NuxtLink>
        </p>
      </div>

      <!-- 提示信息框 -->
      <div v-if="successMsg" class="p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs rounded-xl flex items-center gap-2">
        <CheckCircleIcon class="w-4 h-4 flex-shrink-0" />
        <span>{{ successMsg }}</span>
      </div>

      <div v-if="errorMsg" class="p-3 bg-red-50 border border-red-200 text-red-600 text-xs rounded-xl flex items-center gap-2">
        <AlertCircleIcon class="w-4 h-4 flex-shrink-0" />
        <span>{{ errorMsg }}</span>
      </div>

      <form class="mt-8 space-y-6" @submit.prevent="handleSignup">
        <div class="rounded-md shadow-sm -space-y-px">
          <div class="relative">
            <input
              v-model="signupForm.username"
              type="text"
              required
              placeholder="用户名 (3 到 15 个字符)"
              class="appearance-none rounded-none relative block w-full px-3 py-3 pl-10 border border-slate-300 placeholder-slate-400 text-slate-900 rounded-t-xl focus:outline-none focus:ring-teal-500 focus:border-teal-500 focus:z-10 text-sm"
            />
            <UserIcon class="absolute left-3.5 top-3.5 w-4 h-4 text-slate-400" />
          </div>
          <div class="relative">
            <input
              v-model="signupForm.email"
              type="email"
              required
              placeholder="邮箱"
              class="appearance-none rounded-none relative block w-full px-3 py-3 pl-10 border border-slate-300 placeholder-slate-400 text-slate-900 focus:outline-none focus:ring-teal-500 focus:border-teal-500 focus:z-10 text-sm"
            />
            <MailIcon class="absolute left-3.5 top-3.5 w-4 h-4 text-slate-400" />
          </div>
          <div class="relative">
            <input
              v-model="signupForm.password"
              type="password"
              required
              placeholder="设置密码 (6 到 20 个字符)"
              class="appearance-none rounded-none relative block w-full px-3 py-3 pl-10 border border-slate-300 placeholder-slate-400 text-slate-900 rounded-b-xl focus:outline-none focus:ring-teal-500 focus:border-teal-500 focus:z-10 text-sm"
            />
            <LockIcon class="absolute left-3.5 top-3.5 w-4 h-4 text-slate-400" />
          </div>
        </div>

        <div>
          <button
            type="submit"
            :disabled="loading"
            class="group relative w-full flex justify-center py-2.5 px-4 border border-transparent text-sm font-semibold rounded-xl text-white bg-slate-900 hover:bg-slate-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-900 transition disabled:bg-slate-500"
          >
            <Loader2Icon v-if="loading" class="w-4 h-4 animate-spin mr-2" />
            注册
          </button>
        </div>
        <p class="text-center text-[10px] text-slate-400 leading-normal">
          点击 "注册" 即表示您同意并愿意遵守音书<br />用户协议和隐私政策
        </p>
      </form>

      <!-- 社交账号注册 -->
      <div class="mt-6 border-t border-slate-200 pt-6">
        <p class="text-center text-xs text-slate-400 mb-4">社交账号直接注册</p>
        <div class="flex justify-center gap-4">
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
import { useRouter } from '#app'
import md5 from 'js-md5'
import api from '@/api/api'
import {
  User as UserIcon,
  Mail as MailIcon,
  Lock as LockIcon,
  Loader2 as Loader2Icon,
  CheckCircle as CheckCircleIcon,
  AlertCircle as AlertCircleIcon
} from 'lucide-vue-next'

definePageMeta({
  layout: false
})

const router = useRouter()

const signupForm = reactive({
  username: '',
  password: '',
  email: '',
  phone: '',
})

const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')

function handleSignup() {
  // 简易校验
  if (signupForm.username.length < 3 || signupForm.username.length > 15) {
    errorMsg.value = '用户名长度需在 3 到 15 个字符之间'
    return
  }
  if (signupForm.password.length < 6 || signupForm.password.length > 20) {
    errorMsg.value = '密码长度需在 6 到 20 个字符之间'
    return
  }

  loading.value = true
  errorMsg.value = ''
  successMsg.value = ''

  const userInfo = {
    username: signupForm.username,
    email: signupForm.email,
    password: md5(signupForm.password)
  }

  api.signup(userInfo).then(response => {
    loading.value = false
    successMsg.value = (response.data || '注册成功') + '！请前往您的邮箱进行激活。'
    setTimeout(() => {
      router.push('/signin')
    }, 3000)
  }).catch(err => {
    loading.value = false
    errorMsg.value = err?.msg || '注册失败，请检查填写信息或换一个用户名'
  })
}
</script>
