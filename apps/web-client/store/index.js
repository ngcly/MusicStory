import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'
import api from '@/api/api'
import Cookies from 'js-cookie'

export const useStore = defineStore('main', () => {
  const token = ref(null)
  const isMusicPlaying = ref(false)

  // SSR 安全的 token 初始化
  if (process.client) {
    const rawToken = Cookies.get('token')
    if (rawToken) {
      try {
        token.value = JSON.parse(rawToken)
      } catch (e) {
        token.value = rawToken
      }
    }
  }

  const user = reactive({
    username: '',
    nickname: '',
    avatar: '',
    signature: '',
    roleList: [],
  })

  function setToken(val) {
    token.value = val
  }

  function setUser(val) {
    Object.assign(user, val)
  }

  function login(userInfo) {
    return api.login(userInfo).then(response => {
      const data = response.data
      Cookies.set('token', JSON.stringify(data), { expires: 7 })
      setToken(data)
    })
  }

  function relogin(freshToken) {
    return api.relogin(freshToken).then(response => {
      const data = response.data
      Cookies.set('token', JSON.stringify(data), { expires: 7 })
      setToken(data)
    })
  }

  function logout(accessToken) {
    return api.logout('', accessToken).then(() => {
      setToken(null)
      Cookies.remove('token')
    })
  }

  function getUserInfo() {
    return api.userInfo().then(response => {
      setUser(response.data)
    })
  }

  function updateUser() {
    return api.updateUser().then(response => {
      setUser(response.data)
    })
  }

  return { token, user, isMusicPlaying, login, relogin, logout, getUserInfo, updateUser }
})
