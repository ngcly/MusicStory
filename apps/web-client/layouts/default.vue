<template>
  <div class="min-h-screen flex flex-col bg-slate-50/50">
    <!-- 顶部导航栏 -->
    <nav class="sticky top-0 z-50 w-full h-14 bg-white/80 backdrop-blur-md border-b border-slate-200/80">
      <div class="max-w-[1140px] h-full mx-auto px-4 flex items-center justify-between">
        <!-- Logo -->
        <div class="flex items-center gap-8">
          <NuxtLink to="/" class="flex items-center">
            <img src="~/assets/img/nav-logo.png" alt="Logo" class="h-9 object-contain" />
          </NuxtLink>

          <!-- 导航链接 -->
          <ul class="hidden md:flex items-center gap-1 text-slate-600 font-medium text-sm">
            <li>
              <NuxtLink to="/" class="px-3 py-2 rounded-lg hover:bg-slate-100 hover:text-slate-900 transition flex items-center gap-2">
                <CompassIcon class="w-4 h-4" /> <span>首页</span>
              </NuxtLink>
            </li>
            <li>
              <NuxtLink to="/selected" class="px-3 py-2 rounded-lg hover:bg-slate-100 hover:text-slate-900 transition flex items-center gap-2">
                <BookOpenIcon class="w-4 h-4" /> <span>精选</span>
              </NuxtLink>
            </li>
            <li v-if="isLogin" class="relative group">
              <NuxtLink to="/notify/comments" class="px-3 py-2 rounded-lg hover:bg-slate-100 hover:text-slate-900 transition flex items-center gap-2">
                <BellIcon class="w-4 h-4" /> <span>消息</span>
              </NuxtLink>
              <!-- 消息下拉子菜单 -->
              <div class="absolute left-0 top-full mt-1 hidden group-hover:block w-40 bg-white border border-slate-200 rounded-lg shadow-lg py-1 animate-in fade-in slide-in-from-top-1 duration-150">
                <NuxtLink to="/notify/comments" class="flex items-center gap-2 px-3 py-2 text-xs text-slate-700 hover:bg-slate-100">
                  <MessageSquareIcon class="w-3.5 h-3.5" /> 评论
                </NuxtLink>
                <NuxtLink to="/notify/chats" class="flex items-center gap-2 px-3 py-2 text-xs text-slate-700 hover:bg-slate-100">
                  <MailIcon class="w-3.5 h-3.5" /> 音信
                </NuxtLink>
                <NuxtLink to="/notify/requests" class="flex items-center gap-2 px-3 py-2 text-xs text-slate-700 hover:bg-slate-100">
                  <UploadIcon class="w-3.5 h-3.5" /> 投稿请求
                </NuxtLink>
                <NuxtLink to="/notify/likes" class="flex items-center gap-2 px-3 py-2 text-xs text-slate-700 hover:bg-slate-100">
                  <HeartIcon class="w-3.5 h-3.5" /> 喜欢和赞
                </NuxtLink>
                <NuxtLink to="/notify/follows" class="flex items-center gap-2 px-3 py-2 text-xs text-slate-700 hover:bg-slate-100">
                  <UserIcon class="w-3.5 h-3.5" /> 关注
                </NuxtLink>
              </div>
            </li>
          </ul>
        </div>

        <!-- 右侧区域 (搜索 + 用户状态) -->
        <div class="flex items-center gap-4">
          <!-- 搜索框 -->
          <div class="relative hidden sm:block">
            <input
              type="text"
              v-model="sf"
              placeholder="搜索"
              @keyup.enter="searchFor"
              class="w-40 focus:w-56 h-8 pl-8 pr-3 text-xs bg-slate-100 border border-transparent rounded-full focus:bg-white focus:border-slate-300 focus:outline-none transition-all duration-300"
            />
            <SearchIcon class="absolute left-2.5 top-2.5 w-3.5 h-3.5 text-slate-400" />
          </div>

          <!-- 写文章 -->
          <NuxtLink to="/create" class="h-8 px-4 inline-flex items-center gap-1 bg-slate-900 hover:bg-slate-800 text-white font-medium text-xs rounded-full transition shadow-sm">
            <EditIcon class="w-3.5 h-3.5" /> 写文章
          </NuxtLink>

          <!-- 登录/注册 -->
          <div v-if="!isLogin" class="flex items-center gap-2">
            <NuxtLink to="/signin" class="text-xs font-semibold text-slate-500 hover:text-slate-900 px-3 py-1.5 transition">
              登录
            </NuxtLink>
            <NuxtLink to="/signup" class="text-xs font-semibold text-slate-800 border border-slate-300 hover:bg-slate-50 px-3.5 py-1.5 rounded-full transition">
              注册
            </NuxtLink>
          </div>

          <!-- 用户头像下拉单 -->
          <div v-else class="relative">
            <DropdownMenu>
              <DropdownMenuTrigger as-child>
                <button class="flex items-center focus:outline-none rounded-full border border-slate-200 hover:shadow-sm transition">
                  <img :src="userInfo.avatar || defaultAvatar" alt="Avatar" class="w-8 h-8 rounded-full object-cover" />
                </button>
              </DropdownMenuTrigger>
              <DropdownMenuContent class="w-44" align="end">
                <DropdownMenuLabel class="text-xs text-slate-500 font-normal">
                  账号：{{ userInfo.username }}
                </DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem>
                  <NuxtLink to="/u/123" class="w-full flex items-center text-xs">
                    <UserIcon class="w-3.5 h-3.5 mr-2 text-slate-400" /> 我的主页
                  </NuxtLink>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <NuxtLink to="/bookmarks" class="w-full flex items-center text-xs">
                    <BookmarkIcon class="w-3.5 h-3.5 mr-2 text-slate-400" /> 收藏文章
                  </NuxtLink>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <NuxtLink to="/users/123/like" class="w-full flex items-center text-xs">
                    <HeartIcon class="w-3.5 h-3.5 mr-2 text-slate-400" /> 喜欢文章
                  </NuxtLink>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <NuxtLink to="/settings/basic" class="w-full flex items-center text-xs">
                    <SettingsIcon class="w-3.5 h-3.5 mr-2 text-slate-400" /> 设置
                  </NuxtLink>
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem @click="logout" class="text-red-600 focus:bg-red-50 focus:text-red-700">
                  <span class="w-full flex items-center text-xs cursor-pointer">
                    <LogOutIcon class="w-3.5 h-3.5 mr-2" /> 退出登录
                  </span>
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主页面流 -->
    <main class="flex-grow">
      <slot />
    </main>

    <!-- 返回顶部悬浮按钮 -->
    <transition name="fade">
      <button
        v-show="showBacktop"
        @click="scrollToTop"
        class="fixed bottom-28 right-8 z-40 w-10 h-10 flex items-center justify-center bg-white hover:bg-slate-50 text-slate-600 rounded-full border border-slate-200/80 shadow-lg hover:shadow-xl transition-all duration-300"
        title="返回顶部"
      >
        <ArrowUpIcon class="w-5 h-5" />
      </button>
    </transition>

    <!-- 极简文艺猫咪挂件 (看板娘) -->
    <MascotCompanionWidget />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from '#app'
import { useStore } from '@/store'
import { storeToRefs } from 'pinia'
import websocket from '@/utils/websocket'
import defaultAvatar from '@/assets/img/avatar.png'
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuLabel
} from '@/components/ui/dropdown-menu'
import {
  Compass as CompassIcon,
  BookOpen as BookOpenIcon,
  Bell as BellIcon,
  Search as SearchIcon,
  Edit as EditIcon,
  User as UserIcon,
  Bookmark as BookmarkIcon,
  Heart as HeartIcon,
  Settings as SettingsIcon,
  LogOut as LogOutIcon,
  MessageSquare as MessageSquareIcon,
  Mail as MailIcon,
  Upload as UploadIcon,
  ArrowUp as ArrowUpIcon
} from 'lucide-vue-next'

const store = useStore()
const router = useRouter()
const route = useRoute()
const { token, user } = storeToRefs(store)

const isLogin = computed(() => !!token.value)
const userInfo = computed(() => user.value)

const sf = ref('')
const showBacktop = ref(false)
let stompClient = null

onMounted(() => {
  // 获取用户信息
  if (store.token) {
    store.getUserInfo()
  }

  // 监听滚动条，显示/隐藏返回顶部按钮
  window.addEventListener('scroll', handleScroll)

  // 客户端连接 Websocket
  if (process.client && isLogin.value && !stompClient) {
    try {
      stompClient = websocket.connection(store)
      if (stompClient) {
        stompClient.connect({}, () => {
          stompClient.subscribe('/topic/notify', (msg) => {
            console.log('WS topic message:', msg)
          })
          stompClient.subscribe('/user/' + userInfo.value.username + '/queue/notify', (msg) => {
            console.log('WS user message:', msg)
          })
        }, (err) => {
          console.log('WS connection err:', err)
        })
      }
    } catch (e) {
      console.error('WS initialization failed:', e)
    }
  }
})

onBeforeUnmount(() => {
  if (process.client) {
    window.removeEventListener('scroll', handleScroll)
  }
  if (stompClient) {
    try {
      stompClient.disconnect()
    } catch (e) {}
  }
})

function handleScroll() {
  showBacktop.value = window.scrollY > 400
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function logout() {
  store.logout('/' + store.token.access_token).then(() => {
    if (process.client) {
      window.location.reload()
    }
  })
}

function searchFor() {
  if (sf.value && route.path !== '/search/' + sf.value) {
    router.push({ path: '/search/' + sf.value })
  }
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>
