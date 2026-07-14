<template>
  <div class="container">
    <div class="row">
      <div class="search-content">
        <span v-show="essays.length <= 0">抱歉，暂未搜到相关内容！</span>
        <div class="sort-type">
          <a href="#">时间排序</a>
        </div>
        <div class="result">{{ total }} 个结果</div>
        <ul class="list">
          <li v-for="(essay, indx) in essays" :key="indx">
            <div class="content">
              <div class="author">
                <router-link class="avatar" to="#">
                  <el-avatar :size="24" :src="defaultAvatar" />
                </router-link>
                <div class="info">
                  <router-link to="#">{{ essay.content.author }}</router-link>
                  <span>1年前</span>
                </div>
              </div>
              <router-link
                :to="{path: '/essayDetail/'+essay.id}"
                v-html="essay.highlightFields.title ? essay.highlightFields.title[0] : essay.content.title"
                class="title"
              ></router-link>
              <p class="abstract" v-html="essay.highlightFields.content ? essay.highlightFields.content[0] : essay.content.content"></p>
              <div class="meta">
                <span><i class="fa fa-eye" aria-hidden="true">1</i></span>
                <span><i class="fa fa-comment" aria-hidden="true">1</i></span>
              </div>
            </div>
          </li>
        </ul>
        <el-pagination
          hide-on-single-page
          background
          @current-change="load"
          v-model:current-page="page"
          :page-size="10"
          :total="total"
          prev-text="上一页"
          next-text="下一页"
          layout="prev, pager, next"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import api from '@/api/api'
import defaultAvatar from '@/assets/img/avatar.png'

const props = defineProps({ sf: String })

const page = ref(1)
const total = ref(0)
const keyWord = ref('')
const essays = ref([])
const loading = ref(false)

function load() {
  loading.value = true
  api.essaySearch('', '/10/' + page.value + '/' + keyWord.value).then(response => {
    essays.value = response.data
    total.value = response.data.length
    loading.value = false
  })
}

keyWord.value = props.sf
load()

watch(() => props.sf, (newVal) => {
  keyWord.value = newVal
  load()
})
</script>

<style scoped>
.row { width: 960px; margin-right: auto; margin-left: auto; padding: 30px 15px 0 15px; }
.search-content { position: relative; }
.sort-type { padding-bottom: 20px; font-size: 13px; }
.result { position: absolute; top: 0; right: 15px; font-size: 13px; color: #b4b4b4; }
ul li { position: relative; width: 100%; margin: 0 0 15px; padding: 15px 2px 20px 0; border-bottom: 1px solid #f0f0f0; word-wrap: break-word; }
.title { margin: -7px 0 4px; display: inherit; font-size: 18px; font-weight: 700; line-height: 1.5; }
.abstract { margin: 0 0 8px; font-size: 14px; line-height: 24px; color: #999; }
.meta { padding-right: 0 !important; font-size: 13px; font-weight: 400; line-height: 20px; }
.meta i { margin-right: 10px; color: #b4b4b4; }
.list .author { margin-bottom: 14px; font-size: 13px; }
.list .author .avatar, .list .author .info { display: inline-block; vertical-align: middle; }
.list .author .avatar { margin: 0 5px 0 0; }
.list .author .info span { display: inline-block; padding-left: 3px; color: #969696; vertical-align: middle; }
</style>
