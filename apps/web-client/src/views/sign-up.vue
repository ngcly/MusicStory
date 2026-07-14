<template>
  <div class="sign">
    <div class="logo">
      <router-link to="/"><img src="../assets/img/logo.png"></router-link>
    </div>
    <div class="main">
      <div class="title">
        <h4>
          <router-link to="/signin">登录</router-link>
          <b>·</b>
          <router-link class="active" to="/signup">注册</router-link>
        </h4>
      </div>
      <el-form :model="signupForm" :rules="rules" ref="signupFormRef">
        <el-form-item class="input-prepend" prop="username">
          <el-input class="top-radius" placeholder="用户名" v-model="signupForm.username"></el-input>
          <i class="fa fa-user"></i>
        </el-form-item>
        <el-form-item class="input-prepend" prop="email">
          <el-input class="top-radius" placeholder="邮箱" v-model="signupForm.email"></el-input>
          <i class="fa fa-envelope" aria-hidden="true"></i>
        </el-form-item>
        <el-form-item class="input-prepend" prop="password">
          <el-input class="bottom-radius" type="password" placeholder="设置密码" v-model="signupForm.password"></el-input>
          <i class="fa fa-lock"></i>
        </el-form-item>
        <el-form-item>
          <el-button class="btn btn-info" type="info" round @click="signup">注册</el-button>
        </el-form-item>
        <p class="sign-up-msg">点击 "注册" 即表示您同意并愿意遵守音书<br>用户协议和隐私政策</p>
      </el-form>
      <div class="more-sign">
        <p>社交账号直接注册</p>
        <ul>
          <li class="weixin"><a href="#"><i class="fa fa-weixin"></i></a></li>
          <li class="qq"><a href="#"><i class="fa fa-qq"></i></a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import md5 from 'js-md5'
import api from '@/api/api'

const router = useRouter()
const signupFormRef = ref(null)

const signupForm = reactive({
  username: '',
  password: '',
  email: '',
  phone: '',
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
}

const loading = ref(false)

function signup() {
  signupFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      const userInfo = JSON.parse(JSON.stringify(signupForm))
      userInfo.password = md5(userInfo.password)
      api.signup(userInfo).then(response => {
        loading.value = false
        ElMessage.success({ message: response.data + ': 注册成功！请前往邮箱进行激活' })
        router.push('/signin')
      })
    }
  })
}
</script>

<style scoped>
@import '../assets/css/sign.css';
</style>
