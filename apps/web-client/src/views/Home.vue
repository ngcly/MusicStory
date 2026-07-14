<template>
  <div class="container">
    <div class="index">
      <el-row class="notice-row">
        <el-col :span="24">
          <div class="notice-bar-container">
            <div class="notice-prefix">
              <i class="fa fa-bullhorn notice-icon"></i>
              <span class="notice-label">公告：</span>
            </div>
            <div class="notice-body">
              <Acquaint :delay="0.5" :speed="70" :content="notice">
                <span v-for="(item, index) in notice" :key="index" class="notice-text-item">
                  <i class="fa fa-star-o star-icon"></i> {{ item }} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </span>
              </Acquaint>
            </div>
          </div>
        </el-col>
      </el-row>

      <div class="row">
        <div class="col-xs-16 main">
          <div class="block">
            <el-carousel height="280px" type="card" interval="5000">
              <el-carousel-item v-for="item in carousel" :key="item.id" class="carousel-card-item">
                <a target="_blank" :href="item.forwardUrl" class="carousel-link">
                  <img :src="item.imageUrl" class="imgCarousel" />
                </a>
              </el-carousel-item>
            </el-carousel>
          </div>

          <div class="split-line"></div>

          <div class="list-container">
            <ul class="list">
              <li v-for="(essay, indx) in essays" :key="indx">
                <ul class="note-list" infinite-scroll-url="/">
                  <li v-for="(item, index) in essay" :key="index">
                    <div class="content">
                      <router-link :to="{path: '/essayDetail/'+item.id}" v-text="item.title" class="title"></router-link>
                      <p class="abstract" v-html="item.synopsis"></p>
                      <div class="meta">
                        <router-link to="#"><i class="fa fa-user" aria-hidden="true"> {{ item.username }}</i></router-link>
                        <span><i class="fa fa-eye" aria-hidden="true"> {{ item.read_num }}</i></span>
                        <span><i class="fa fa-comment" aria-hidden="true"> {{ item.updated_time }}</i></span>
                      </div>
                    </div>
                  </li>
                </ul>
              </li>
            </ul>
            <el-button type="info" round class="load-more" @click="load()" v-if="!noMore" :loading="loading">阅读更多</el-button>
            <p v-if="noMore">没有更多了</p>
          </div>
        </div>
        <div class="col-xs-7 col-xs-offset-1 aside">
          <!-- 网站简介卡片 -->
          <div class="aside-card intro-card">
            <h4 class="card-title">
              <i class="fa fa-info-circle icon-color"></i>
              关于音书
            </h4>
            <p class="card-desc">
              音书是一个结合了音乐播放与文字创作的文艺社区。在这里，你可以听着喜欢的歌，看着温暖的文字，记录每一个触动内心的故事。
            </p>
          </div>

          <!-- 热门专题卡片 -->
          <div class="aside-card topics-card">
            <h4 class="card-title">
              <i class="fa fa-tags icon-color"></i>
              热门专题
            </h4>
            <div class="topic-list">
              <span class="topic-badge">民谣与诗</span>
              <span class="topic-badge">午后随笔</span>
              <span class="topic-badge">深夜读书</span>
              <span class="topic-badge">独立音乐</span>
              <span class="topic-badge">治愈系故事</span>
            </div>
          </div>

          <!-- 推荐作者 -->
          <div class="aside-card author-card">
            <h4 class="card-title">
              <i class="fa fa-users icon-color"></i>
              推荐作者
            </h4>
            <ul class="recommend-authors">
              <li class="author-item">
                <el-avatar :size="32" :src="defaultAvatar" />
                <div class="author-info">
                  <span class="name">晴空万里</span>
                  <span class="desc">写温暖的字，听安静的歌</span>
                </div>
                <el-button type="success" size="small" plain round class="follow-btn">关注</el-button>
              </li>
              <li class="author-item">
                <el-avatar :size="32" :src="defaultAvatar" />
                <div class="author-info">
                  <span class="name">民谣故事集</span>
                  <span class="desc">每个故事都有一首歌</span>
                </div>
                <el-button type="success" size="small" plain round class="follow-btn">关注</el-button>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Acquaint from '@/components/Acquaint.vue'
import api from '@/api/api'
import defaultAvatar from '@/assets/img/avatar.png'

const carousel = ref([{}])
const notice = ref([])
const essays = ref([])
const page = ref(1)
const noMore = ref(false)
const loading = ref(false)

function load() {
  loading.value = true
  api.essays('', '/10/' + page.value).then(response => {
    if (response.data && response.data.length > 0) {
      essays.value.push(response.data)
      if (response.data.length < 10) noMore.value = true
      page.value++
      loading.value = false
    } else {
      noMore.value = true
    }
  })
}

api.carousel().then(response => { carousel.value = response.data })
api.notice().then(response => {
  response.data.forEach(element => { notice.value.push(element.content) })
})
load()
</script>

<style scoped>
.container {
  background: #f8fafc;
  min-height: calc(100vh - 56px);
  padding-bottom: 40px;
}
.carousel-card-item {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}
.carousel-link {
  display: block;
  width: 100%;
  height: 100%;
}
.imgCarousel {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}
.imgCarousel:hover {
  transform: scale(1.04);
}
.index { width: 960px; margin-right: auto; margin-left: auto; padding: 30px 15px 0 15px; }
*, :after, :before { box-sizing: border-box; }
.row { margin-left: -15px; margin-right: -15px; }
.index .main { padding-top: 20px; padding-right: 0; }
.col-xs-16 { width: 66.66667%; }
.index .main .split-line { margin: 0 0 20px; border-bottom: 1px solid #f0f0f0; }

/* --- 公告栏美化 --- */
.notice-row {
  margin-top: 15px;
  margin-bottom: 15px;
}
.notice-bar-container {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 24px;
  padding: 6px 20px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.02);
}
.notice-prefix {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  margin-right: 12px;
  font-size: 13px;
  font-weight: 600;
  color: #2c3e50;
}
.notice-icon {
  margin-right: 6px;
  color: #e67e22;
  font-size: 14px;
  animation: notice-shake 2.5s infinite;
}
.notice-label {
  letter-spacing: 1px;
}
.notice-body {
  flex-grow: 1;
  overflow: hidden;
  height: 24px;
  display: flex;
  align-items: center;
}
.notice-text-item {
  font-size: 13px;
  color: #555;
}
.star-icon {
  color: #f1c40f;
  margin-right: 6px;
}
@keyframes notice-shake {
  0%, 100% { transform: rotate(0); }
  10%, 30%, 50%, 70%, 90% { transform: rotate(-10deg); }
  20%, 40%, 60%, 80% { transform: rotate(10deg); }
}

/* --- 文章卡片美化 --- */
.note-list { margin: 0; padding: 0; list-style: none; }
.note-list li {
  position: relative;
  width: 100%;
  margin: 0 0 20px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.01);
  word-wrap: break-word;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}
.note-list li:hover {
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  transform: translateY(-4px);
  border-color: rgba(44, 62, 80, 0.15);
  background: rgba(255, 255, 255, 0.95);
}
.note-list .title {
  margin: 0 0 10px;
  display: inherit;
  font-size: 19px;
  font-weight: 700;
  color: #2c3e50;
  text-decoration: none;
  line-height: 1.5;
  transition: color 0.2s;
}
.note-list .title:hover {
  color: #1abc9c;
}
.note-list .abstract {
  margin: 0 0 12px;
  font-size: 13.5px;
  line-height: 1.6;
  color: #666;
}
.note-list .meta {
  padding-right: 0 !important;
  font-size: 12px;
  font-weight: 400;
  line-height: 20px;
  display: flex;
  gap: 16px;
}
.note-list .meta a {
  color: #777;
  text-decoration: none;
}
.note-list .meta a:hover {
  color: #1abc9c;
}
.note-list .meta span {
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
}
.note-list .meta i {
  margin-right: 0;
  color: #b4b4b4;
}
.index .aside { padding: 20px 0 0; }
.col-xs-offset-1 { margin-left: 4.16667%; }
.col-xs-7 { width: 29.16667%; }
.col-xs-7, .col-xs-16 { float: left; }
.load-more { width: 100%; height: 40px; margin: 30px auto 60px; padding: 10px 15px; text-align: center; font-size: 15px; }
.aside-card {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.02);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}
.aside-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  transform: translateY(-4px);
}
.card-title {
  margin-top: 0;
  margin-bottom: 12px;
  font-size: 15px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
}
.icon-color {
  color: #2c3e50;
  margin-right: 8px;
  font-size: 16px;
}
.card-desc {
  margin: 0;
  font-size: 13px;
  color: #666;
  line-height: 1.6;
}
.topic-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.topic-badge {
  background: #f7f7f7;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 4px 12px;
  font-size: 12px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}
.topic-badge:hover {
  background: #2c3e50;
  color: #fff;
  border-color: #2c3e50;
}
.recommend-authors {
  margin: 0;
  padding: 0;
  list-style: none;
}
.author-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
.author-item:last-child {
  margin-bottom: 0;
}
.author-info {
  margin-left: 10px;
  flex-grow: 1;
}
.author-info .name {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #333;
}
.author-info .desc {
  display: block;
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}
.follow-btn {
  padding: 4px 10px !important;
  font-size: 11px !important;
  height: 24px !important;
}
</style>
