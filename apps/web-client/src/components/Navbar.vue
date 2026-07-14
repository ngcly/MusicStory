<template>
  <div>
    <nav class="navbar navbar-default nav-fixed-top">
      <div class="width-limit">
        <router-link class="nav-logo" to="/">
          <img src="../assets/img/nav-logo.png">
        </router-link>
        <router-link class="btn write-btn" to="/create">
          <i class="fa fa-pied-piper"/>
          写文章
        </router-link>
        <router-link v-if="!isLogin" to="/signup" class="btn sign-up">注册</router-link>
        <router-link v-if="!isLogin" to="/signin" class="btn sign-in">登录</router-link>
        <div v-if="isLogin" class="user" @mouseover="personShow=true" @mouseout="personShow=false">
          <div class="drop-down">
            <router-link class="avatar" to="/u/123">
              <el-avatar :size="40" :src="userInfo.avatar || defaultAvatar" />
            </router-link>
          </div>
          <div v-show="personShow" class="drop-menu">
            <ul>
              <li><router-link to="/u/123"><i class="fa fa-user"/> 我的主页</router-link></li>
              <li><router-link to="/bookmarks"><i class="fa fa-bookmark"/> 收藏的文章</router-link></li>
              <li><router-link to="/users/123/like"><i class="fa fa-heart"/> 喜欢的文章</router-link></li>
              <li><router-link to="/settings/basic"><i class="fa fa-cog"/> 设置</router-link></li>
              <li><a @click="logout"><i class="fa fa-sign-out"/> 退出</a></li>
            </ul>
          </div>
        </div>
        <div class="my-container">
          <ul class="nav-list">
            <li><router-link to="/"><i class="fa fa-compass"/> <span>首页</span></router-link></li>
            <li><router-link to="/index"><i class="fa fa-book"/> <span>精选</span></router-link></li>
            <li v-if="isLogin" class="notify" @mouseover="notifyShow=true" @mouseleave="notifyShow=false">
              <router-link to="/notify/comments">
                <i class="fa fa-bell-o"/> <span>消息</span>
              </router-link>
              <div v-show="notifyShow" class="drop-menu">
                <ul>
                  <li><router-link to="/notify/comments"><i class="fa fa-comment-o"/> 评论</router-link></li>
                  <li><router-link to="/notify/chats"><i class="fa fa-envelope-open-o"/> 音信</router-link></li>
                  <li><router-link to="/notify/requests"><i class="fa fa-upload"/> 投稿请求</router-link></li>
                  <li><router-link to="/notify/likes"><i class="fa fa-heart-o"/> 喜欢和赞</router-link></li>
                  <li><router-link to="/notify/follows"><i class="fa fa-user-o"/> 关注</router-link></li>
                </ul>
              </div>
            </li>
            <li class="search">
              <el-form @submit.native.prevent="searchFor">
                <input
                  ref="inputRef"
                  class="input"
                  placeholder="搜索"
                  type="text"
                  v-model="sf"
                  @focus="bgShow=true"
                  @blur="bgShow=false">
                <a :class="{active:bgShow}" class="search-btn" @click="searchFor">
                  <i class="fa fa-search"/>
                </a>
              </el-form>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from '../store'
import { storeToRefs } from 'pinia'
import { initWebSocket } from '../utils/websocket'
import defaultAvatar from '../assets/img/avatar.png'

const store = useStore()
const router = useRouter()
const route = useRoute()
const { token, user } = storeToRefs(store)

const isLogin = computed(() => token.value)
const userInfo = computed(() => user.value)

const sf = ref('')
const personShow = ref(false)
const notifyShow = ref(false)
const bgShow = ref(false)
let stompClient = ''

onMounted(() => {
  if (store.token) {
    store.getUserInfo()
  }
  if (stompClient === '') {
    stompClient = initWebSocket()
    stompClient.connect({}, () => {
      stompClient.subscribe('/topic/notify', (msg) => {
        window.console.log(msg)
      })
      stompClient.subscribe('/user/' + userInfo.value.username + '/queue/notify', (msg) => {
        window.console.log(msg)
      })
    }, (err) => {
      window.console.log(err)
    })
  }
})

onBeforeUnmount(() => {
  if (stompClient) {
    stompClient.disconnect()
  }
})

function logout() {
  store.logout('/' + store.token.access_token).then(() => {
    location.reload()
  })
}

function searchFor() {
  if (sf.value && route.path !== '/search/' + sf.value) {
    router.push({ path: '/search/' + sf.value })
  }
}
</script>

<style scoped>
ul,ol,dl { margin: 0; }
nav { width: 100%; height: 56px; }
.navbar {
  padding: 0;
  border-bottom: 1px solid #e7e7e7;
  margin-bottom: 20px;
  background-color: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(12px) saturate(180%);
  -webkit-backdrop-filter: blur(12px) saturate(180%);
  position: relative;
  display: -ms-flexbox;
  display: flex;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
  -ms-flex-align: center;
  align-items: center;
  -ms-flex-pack: justify;
  justify-content: space-between;
}
.nav-fixed-top { position: fixed; top: 0; left: 0; z-index: 1000; }
nav .width-limit { min-width: 768px; width: 1440px; margin: 0 auto; }
nav .nav-logo { float: left; height: 56px; padding: 0; }
nav a.write-btn {
  float: right; margin: 9px 0 0 0; line-height: 24px; font-size: 16px;
  background-color: #2c3e50; color: #fff !important; border-radius: 20px; margin: 8px 15px 0;
}
nav a.write-btn:hover { background: #084c91; }
nav a.sign-in {
  float: right; height: 35px; margin: 10px 6px 0 10px; padding: 6px 12px;
  line-height: 24px; font-size: 15px; color: #969696 !important; box-shadow: none;
}
nav a.sign-up {
  float: right; width: 80px; height: 38px; margin: 10px 16px 0 10px; padding: 6px 12px;
  line-height: 24px; font-size: 15px; border: 1px solid #2c3e50; border-radius: 20px;
  color: #2c3e50 !important; box-shadow: none;
}
nav a.sign-up:hover { background-color: rgb(192, 193, 245); }
nav .user { float: right; position: relative; }
nav .user .avatar {
  width: 40px; height: 40px; display: block; position: relative; margin: 8px 28px 8px 20px;
}
nav .user .avatar img { width: 100%; height: 100%; border: 1px solid #ccc; border-radius: 50%; }
nav .user .avatar:before {
  content: ''; position: absolute; top: 18px; right: -14px;
  border-left: 5px solid transparent; border-right: 5px solid transparent; border-top: 6px solid #999;
}
nav .user:hover { background: #f5f5f5; }
nav .user .drop-menu {
  position: absolute; left: 0; z-index: 999; min-width: 160px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1); font-size: 15px; background: #fff;
}
nav .user .drop-menu ul { border: 1px solid #ccc; padding: 10px 0; margin: 0; }
nav .user .drop-menu ul li a { padding: 10px 20px; display: block; }
nav .user .drop-menu ul li a:hover { background: #f5f5f5; }
nav .user .drop-menu ul li i { margin-right: 15px; color: #2e1e5c; font-size: 18px; }
nav .nav-list { margin: 0 -15px; padding: 0; }
nav .nav-list:after { content: ''; height: 0; visibility: hidden; display: block; clear: both; }
nav .nav-list > li { float: left; margin-right: 5px; }
nav .nav-list > li:hover { background-color: #f5f5f5; }
nav .nav-list > li > a { display: block; height: 55px; padding: 15px; margin-right: 10px; font-size: 17px; }
nav .nav-list > li > a > i { margin-right: 7px; margin-top: 5px; font-size: 20px; }
nav .nav-list .notify { position: relative; }
nav .notify .drop-menu {
  position: absolute; left: 0; z-index: 999; min-width: 160px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1); font-size: 15px; background: #fff;
}
nav .notify .drop-menu ul { border: 1px solid #ccc; padding: 10px 0; margin: 0; }
nav .notify .drop-menu ul li a { padding: 10px 20px; display: block; }
nav .notify .drop-menu ul li a:hover { background: #f5f5f5; }
nav .notify .drop-menu ul li i { margin-right: 15px; color: #2e1e5c; font-size: 18px; }
nav .nav-list .search:hover { background: #fff; }
nav .nav-list .search { padding-left: 15px; }
nav .nav-list .search form { margin-top: 9px; position: relative; }
nav .nav-list .search form input {
  width: 240px; height: 38px; border: none; background: rgb(238, 238, 238);
  border-radius: 20px; padding: 0 40px 0 20px; font-size: 15px; transition: width ease-in 0.3s;
}
nav .nav-list .search form input:focus { width: 320px; }
nav .nav-list .search form a.search-btn {
  width: 30px; height: 30px; position: absolute; right: 5px; top: 4px;
  text-align: center; line-height: 30px; color: #969696 !important;
}
nav .nav-list .search form a.active {
  background: #969696; border-radius: 50%; color: #fff !important; font-size: 18px;
}
@media (max-width: 768px) { nav .nav-list { display: none; } }
@media (max-width: 1440px) {
  nav .nav-list > li > a > i { display: none; }
  nav .nav-list .search form input { width: 160px; }
  nav .nav-list .search form input:focus { width: 240px; }
}
@media (max-width: 1080px) {
  nav .nav-list > li > a > i { display: block; }
  nav .nav-list > li > a > span { display: none; }
  nav .nav-list .search form input { width: 150px; }
  nav .nav-list .search form input:focus { width: 150px; }
}
.btn { display: inline-block; text-align: center; white-space: nowrap; vertical-align: middle; border: 1px solid transparent; padding: 0.375rem 0.75rem; }
a:hover { cursor: pointer; }
</style>
