import axios from 'axios'
import pinia from '../pinia'
import { useStore } from '../store'
import Cookies from 'js-cookie'
import { ElMessage } from 'element-plus'

const api_url = import.meta.env.VITE_BASE_API
const service = axios.create({
  baseURL: api_url,
  timeout: 5000,
})

// http request interceptor
service.interceptors.request.use(
  config => {
    const store = useStore(pinia)
    if (store.token) {
      const tokenInfo = Cookies.getJSON('token')
      config.headers.Authorization = tokenInfo.token_type + ' ' + tokenInfo.access_token
    }
    return config
  },
  err => {
    ElMessage.error({
      center: true,
      message: '服务器请求无响应！',
    })
    return Promise.reject(err)
  })

// http response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    switch (res.code) {
      case 0:
        return res
      case 401: {
        const store = useStore(pinia)
        store.token = null
        ElMessage.error({
          message: res.msg,
          onClose: () => {
            window.location.href = '/signin'
          },
        })
        break
      }
      case 409: {
        const store = useStore(pinia)
        if (store.token) {
          return doRequest(response)
        } else {
          ElMessage.error({ message: res.msg })
        }
        break
      }
      default:
        ElMessage.error({ message: res.msg })
        break
    }
    return Promise.reject()
  },
  error => {
    ElMessage.error({
      message: '服务器异常！',
      center: true,
    })
    return Promise.reject(error)
  })

async function doRequest(response) {
  const store = useStore(pinia)
  await store.relogin({
    client_id: 'music_story',
    client_secret: 'secret',
    refresh_token: Cookies.getJSON('token').refresh_token,
  })
  const config = response.config
  config.headers.Authorization = store.token.token_type + ' ' + store.token.access_token
  const res = await axios.request(config)
  return res.data
}

export default service
