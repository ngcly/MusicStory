<template>
  <div class="create">
    <div class="back">
      <router-link class="back-home" to="/">回到首页</router-link>
      <el-button type="info" round @click="publish">发布文章</el-button>
    </div>
    <el-form :inline="true" :model="formData" :rules="rules" ref="formRef" class="form-inline">
      <el-form-item label="分类列表" prop="classifyId">
        <el-select v-model="formData.classifyId" placeholder="请选择分类">
          <el-option
            v-for="(item, index) in classify"
            :key="index"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="formData.title"></el-input>
      </el-form-item>
      <el-button type="info" @click="dialogVisible = true">添加音乐</el-button>
      <el-dialog title="音乐信息" v-model="dialogVisible">
        <el-form-item label="歌名" :label-width="labelWidth">
          <el-input v-model="formData.musicList.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="歌手" :label-width="labelWidth">
          <el-input v-model="formData.musicList.artist" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="歌曲" :label-width="labelWidth">
          <el-input v-model="formData.musicList.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="歌词" :label-width="labelWidth">
          <el-input v-model="formData.musicList.lrc" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专辑" :label-width="labelWidth">
          <el-input v-model="formData.musicList.album" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="封面" :label-width="labelWidth">
          <el-input v-model="formData.musicList.cover" autocomplete="off"></el-input>
        </el-form-item>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
          </span>
        </template>
      </el-dialog>
    </el-form>
    <div>
      <md-editor
        v-model="mdContent"
        @onUploadImg="onUploadImg"
        @onSave="onSave"
        style="min-height: 600px"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import api from '@/api/api'

const router = useRouter()
const formRef = ref(null)

const dialogVisible = ref(false)
const labelWidth = '80px'
const mdContent = ref('')

const classify = ref([])
const formData = reactive({
  id: '',
  title: '',
  content: '',
  synopsis: '',
  classifyId: '',
  musicList: [{}],
})

const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { max: 50, message: '长度请勿超过50个字符', trigger: 'blur' },
  ],
  content: [
    { required: true, message: '请输入文章内容', trigger: 'blur' },
  ],
  classifyId: [
    { required: true, message: '请选择分类', trigger: 'change' },
  ],
}

function onUploadImg(files, callback) {
  const formdata = new FormData()
  formdata.append('file', files[0])
  api.upload(formdata, '/img').then(response => {
    callback([response.data])
  })
}

function onSave(text, html) {
  formRef.value.validate(valid => {
    if (valid) {
      if (text.length > 200) {
        formData.synopsis = text.substring(0, 200) + '...'
      } else {
        formData.synopsis = text
      }
      formData.content = html
      api.create(formData).then(response => {
        formData.id = response.data
        ElMessage({ type: 'success', message: '保存成功' })
      })
    }
  })
}

function publish() {
  formRef.value.validate(valid => {
    if (valid) {
      if (mdContent.value.length > 200) {
        formData.synopsis = mdContent.value.substring(0, 200) + '...'
      } else {
        formData.synopsis = mdContent.value
      }
      api.altessay(formData).then(() => {
        ElMessage({
          type: 'success',
          message: '发布成功',
          onClose: () => router.push('/'),
        })
      })
    }
  })
}

api.classify().then(response => {
  classify.value = response.data
})
</script>

<style scoped>
.back {
  display: flex; justify-content: space-between; padding: 30px 10% 5px 18px; text-align: center;
}
.back-home {
  display: block; font-size: 15px; padding: 9px 15px; border: 1px solid rgba(36, 114, 89, 0.8); border-radius: 20px;
}
.form-inline { margin: 15px; }
</style>
