<template>
  <div class="container" :class="{ 'zen-active': isZen }">
    <!-- 阅读进度条 -->
    <div class="read-progress" :style="{ width: scrollProgress + '%' }"></div>

    <!-- 浮动退出沉浸按钮 -->
    <button v-if="isZen" class="exit-zen-btn" @click="toggleZen">
      <i class="fa fa-times"></i> 退出沉浸
    </button>
    <div class="row">
      <div class="main">
        <section class="bgw art">
          <h1 class="title" v-text="essayDetail.title"></h1>
          <div class="essay-us">
            <div class="us-box">
              <router-link class="avatar" to="/u/123">
                <el-avatar :size="48" :src="essayDetail.user.avatar || defaultAvatar" />
              </router-link>
              <div style="margin-left: 8px;">
                <div class="us-info">
                  <span class="us-name">
                    <router-link to="#">{{ essayDetail.user.username }}</router-link>
                  </span>
                  <el-button class="btn btn-info" round size="mini">关注</el-button>
                  <el-button class="btn btn-warning zen-btn" round size="mini" @click="toggleZen" style="margin-left: 8px;">
                    <i class="fa fa-eye"></i> 沉浸阅读
                  </el-button>
                </div>
                <div style="display: flex;">
                  <span class="ed">发表时间：{{ essayDetail.createdTime }}</span>
                  <span class="ed">阅读：{{ essayDetail.readNum }}</span>
                </div>
              </div>
            </div>
          </div>
          <article v-html="essayDetail.content"></article>
        </section>
        <div id="page-comment">
          <section class="pg-comment">
            <div style="display:flex;">
              <el-avatar :size="40" :src="defaultAvatar" />
              <div class="comment-form">
                <el-form>
                  <el-form-item>
                    <el-input type="textarea" placeholder="写下你的评论..." />
                  </el-form-item>
                  <el-form-item>
                    <div class="cm-s">
                      <div><span>Enter 发表</span></div>
                      <div>
                        <el-button class="btn btn-info" round size="mini">发布</el-button>
                        <el-button class="btn btn-info" round size="mini">取消</el-button>
                      </div>
                    </div>
                  </el-form-item>
                </el-form>
              </div>
            </div>
            <h3 class="cmt">
              <div class="cmt-info">
                <span class="cmt-all">全部评论</span>
                <span class="cmt-num">{{ comments.content.length }}</span>
              </div>
            </h3>
            <div class="comt">
              <div style="display: flex;" v-for="(item, index) in comments.content" :key="index">
                <router-link class="avatar" to="/#">
                  <el-avatar :size="40" :src="item.avatar" />
                </router-link>
                <div class="comt-content">
                  <div class="comt-uname">
                    <router-link to="#">{{ item.username }}</router-link>
                  </div>
                  <div class="comt-flr">
                    <span>{{ index+1 }} 楼</span>
                    <span v-text="item.createdTime"></span>
                  </div>
                  <div class="comt-info">{{ item.content }}</div>
                  <div class="comt-other">
                    <span style="cursor: pointer;color: #b0b0b0;">
                      <i class="fa fa-comment" aria-hidden="true"> 回复</i>
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
      <aside style="width: 260px;">
        <!-- 作者名片 -->
        <section class="bgw extral author-profile-card">
          <div class="author-avatar-box">
            <el-avatar :size="64" :src="essayDetail.user.avatar || defaultAvatar" />
          </div>
          <h4 class="author-name">{{ essayDetail.user.username }}</h4>
          <p class="author-signature">“写温暖的字，听安静的歌。”</p>
          <div class="author-meta">
            <div class="meta-item">
              <span class="value">12</span>
              <span class="label">文章</span>
            </div>
            <div class="meta-item">
              <span class="value">{{ essayDetail.readNum || 0 }}</span>
              <span class="label">阅读</span>
            </div>
          </div>
          <el-button type="success" round class="profile-follow-btn">关注作者</el-button>
        </section>

        <!-- 听歌指南 -->
        <section class="bgw extral music-tips-card">
          <h4 class="sidebar-title"><i class="fa fa-music"></i> 听歌指南</h4>
          <p class="tips-content">
            本文配有精选音乐，可以点击左下角播放器开启伴奏阅读。
          </p>
        </section>
      </aside>
    </div>
    <APlayer
      v-if="essayDetail.musicList.length > 0"
      :audio="essayDetail.musicList"
      :lrcType="3"
      :autoplay="true"
      fixed
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import api from '@/api/api'
import APlayer from '@/components/APlayer.vue'
import defaultAvatar from '@/assets/img/avatar.png'

const props = defineProps({ id: String })

const essayDetail = reactive({
  title: '',
  content: '',
  readNum: '',
  createTime: '',
  updateTime: '',
  user: { username: '', avatar: '' },
  musicList: [],
})

const comments = reactive({ content: [] })

api.essays('', '/' + props.id).then(response => {
  Object.assign(essayDetail, response.data)
})
api.comments('', '/' + props.id + '/1').then(response => {
  Object.assign(comments, response.data)
})

const scrollProgress = ref(0)
const isZen = ref(false)

function updateProgress() {
  const total = document.documentElement.scrollHeight - window.innerHeight
  if (total > 0) {
    scrollProgress.value = (window.scrollY / total) * 100
  }
}

function toggleZen() {
  isZen.value = !isZen.value
  if (isZen.value) {
    document.body.style.overflow = 'hidden' // 锁定外部滚动
  } else {
    document.body.style.overflow = ''
  }
}

onMounted(() => {
  window.addEventListener('scroll', updateProgress)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', updateProgress)
  document.body.style.overflow = ''
  api.readEssay('', '/' + props.id)
})
</script>

<style scoped>
.container { background-color: #f9f9f9; }
.row {
  box-sizing: initial; width: 1000px; padding-left: 16px; padding-right: 16px;
  margin-left: auto; margin-right: auto; display: flex; justify-content: center;
  align-items: flex-start; min-height: calc(100vh - 66px); padding-top: 10px; font-size: 16px;
}
*, :after, :before { box-sizing: border-box; }
.main { flex-shrink: 0; width: 730px; margin-bottom: 24px; margin-right: 10px; }
.bgw { background-color: #fff; }
.art { border-radius: 4px; margin-bottom: 10px; padding: 24px; }
.title { font-size: 30px; font-weight: 700; word-break: break-word; margin-bottom: 0.5em; }
.essay-us { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px; font-size: 13px; }
.us-box { display: flex; align-items: center; }
.us-info { display: flex; align-items: center; margin-bottom: 6px; }
.us-name { font-size: 16px; font-weight: 500; margin-right: 8px; }
.ed { margin-right: 10px; color: #969696; font-size: 13px; }
.pg-comment { padding: 24px; background-color: #fff; border-radius: 4px; margin-bottom: 10px; }
.comment-form { margin-left: 10px; margin-bottom: 48px; flex-grow: 1; }
.cmt {
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;
  padding-left: 12px; border-left: 4px solid #ec7259; font-size: 18px; font-weight: 500; height: 20px; line-height: 20px;
}
.cmt-info { display: flex; align-items: center; }
.cmt-all { font-size: 18px; font-weight: 500; line-height: 20px; }
.cmt-num { margin-left: 6px; font-size: 14px; font-weight: normal; }
.cm-s { display: flex; justify-content: space-between; font-size: 14px; color: #969696; align-items: center; }
.comt { margin-top: 30px; margin-bottom: 30px; line-height: 1.5; }
.comt-content { flex-grow: 1; margin-left: 10px; margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid #eee; }
.comt-uname { display: flex; align-items: center; font-size: 15px; font-weight: 500; }
.comt-flr { margin-top: 2px; font-size: 12px; color: #969696; }
.comt-info { margin-top: 10px; font-size: 16px; word-break: break-word; }
.comt-other {
  position: relative; display: flex; justify-content: space-between; align-items: center;
  margin-top: 12px; font-size: 15px; user-select: none;
}
.extral { padding: 16px; border-radius: 4px; margin-bottom: 10px; }
.author-profile-card {
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 10px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
}
.author-avatar-box {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}
.author-name {
  margin: 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
.author-signature {
  margin: 0 0 16px;
  font-size: 12px;
  color: #999;
  font-style: italic;
}
.author-meta {
  display: flex;
  justify-content: space-around;
  margin-bottom: 16px;
  border-top: 1px dashed #eee;
  border-bottom: 1px dashed #eee;
  padding: 8px 0;
}
.meta-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.meta-item .value {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}
.meta-item .label {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}
.profile-follow-btn {
  width: 100%;
}
.music-tips-card {
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
}
.sidebar-title {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}
.sidebar-title i {
  color: #2c3e50;
  margin-right: 6px;
}
.tips-content {
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.6;
}

/* --- 升级排版与艺术字体 --- */
article {
  font-family: 'Noto Serif SC', 'Lora', 'Georgia', 'Source Han Serif SC', serif;
  font-size: 17.5px;
  line-height: 1.9;
  letter-spacing: 0.6px;
  color: #2c3e50;
}
article p {
  margin-bottom: 1.5em;
  text-align: justify;
}

/* --- 阅读进度条 --- */
.read-progress {
  position: fixed;
  top: 0;
  left: 0;
  height: 3px;
  background: linear-gradient(90deg, #1abc9c, #3498db);
  z-index: 2100;
  transition: width 0.1s ease-out;
}

/* --- 沉浸式禅意模式 (Zen Mode) --- */
.exit-zen-btn {
  position: fixed;
  top: 24px;
  right: 32px;
  z-index: 2500;
  background: rgba(44, 62, 80, 0.95);
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 8px 20px;
  font-size: 14px;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}
.exit-zen-btn:hover {
  background: #1abc9c;
  transform: translateY(-2px);
}
.container.zen-active {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  overflow-y: auto;
  background-color: #fbf9f6 !important; /* 宣纸质感的柔和底色 */
  z-index: 2000;
  padding-top: 80px;
  padding-bottom: 80px;
}
.container.zen-active .row {
  width: 700px;
  margin: 0 auto;
  padding: 0;
  display: block;
  min-height: auto;
}
.container.zen-active .main {
  width: 100%;
  margin-right: 0;
}
.container.zen-active .art {
  box-shadow: none;
  background: transparent;
  padding: 0;
}
.container.zen-active aside {
  display: none !important;
}
.container.zen-active #page-comment {
  display: none !important;
}
.container.zen-active .zen-btn {
  display: none !important;
}
</style>
