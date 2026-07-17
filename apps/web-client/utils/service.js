import axios from 'axios'
import { useStore } from '../store'
import Cookies from 'js-cookie'

const api_url = import.meta.env.VITE_BASE_API
const service = axios.create({
  baseURL: api_url,
  timeout: 10000,
})

// http request interceptor
service.interceptors.request.use(
  config => {
    if (process.client) {
      const store = useStore()
      if (store.token) {
        let tokenInfo = store.token
        if (typeof tokenInfo === 'string') {
          try {
            tokenInfo = JSON.parse(tokenInfo)
          } catch (e) {}
        }
        if (tokenInfo && tokenInfo.access_token) {
          config.headers.Authorization = tokenInfo.token_type + ' ' + tokenInfo.access_token
        }
      }
    }
    return config
  },
  err => {
    return Promise.reject(err)
  })

// http response interceptor (RFC 7807 & Standard RESTful)
service.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    const errRes = error.response
    if (errRes) {
      if (errRes.status === 401) {
        if (process.client) {
          const store = useStore()
          store.token = null
          Cookies.remove('token')
          window.location.href = '/signin'
        }
      }
      const errorMsg = errRes.data?.detail || errRes.data?.title || error.message || '请求失败'
      return Promise.reject(new Error(errorMsg))
    }
    return Promise.reject(error)
  })

export default service
