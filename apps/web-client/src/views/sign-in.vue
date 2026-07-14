<template>
  <div class="sign">
    <div class="logo">
      <router-link to="/"><img src="../assets/img/logo.png"></router-link>
    </div>
    <div class="main">
      <div class="title">
        <h4>
          <router-link class="active" to="/signin">登录</router-link>
          <b>·</b>
          <router-link to="/signup">注册</router-link>
        </h4>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" @submit.native.prevent>
        <el-form-item prop="username" class="input-prepend">
          <el-input class="top-radius" placeholder="用户名或邮箱" v-model="loginForm.username"></el-input>
          <i class="fa fa-user"></i>
        </el-form-item>
        <el-form-item prop="password" class="input-prepend">
          <el-input class="bottom-radius" type="password" placeholder="密码" v-model="loginForm.password"></el-input>
          <i class="fa fa-lock"></i>
        </el-form-item>
        <div class="remember">
          <input type="checkbox">&nbsp;<span>记住我</span>
        </div>
        <div class="help">
          <router-link to="/help">登录遇到问题?</router-link>
        </div>
        <el-form-item>
          <el-button class="btn btn-primary" type="primary" round @click="login" native-type="submit">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="more-sign">
        <p>社交账号登录</p>
        <ul>
          <li class="weibo"><a href="#"><i class="fa fa-weibo"></i></a></li>
          <li class="weixin"><a href="#"><i class="fa fa-weixin"></i></a></li>
          <li class="qq"><a href="#"><i class="fa fa-qq"></i></a></li>
          <li class="other"><a href="#">其他</a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from '../store'
import md5 from 'js-md5'

const router = useRouter()
const route = useRoute()
const store = useStore()
const loginFormRef = ref(null)

const loginForm = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const loading = ref(false)

function login() {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      const userInfo = JSON.parse(JSON.stringify(loginForm))
      userInfo.password = md5(userInfo.password)
      store.login(userInfo).then(() => {
        loading.value = false
        router.push(route.query.redirect || '/')
      }).catch(() => { loading.value = false })
    }
  })
}
</script>

<style scoped>
@import '../assets/css/sign.css';
</style>
