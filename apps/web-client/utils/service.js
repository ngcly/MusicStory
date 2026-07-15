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

// http response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    switch (res.code) {
      case 0:
        return res
      case 401: {
        if (process.client) {
          const store = useStore()
          store.token = null
          Cookies.remove('token')
          window.location.href = '/signin'
        }
        break
      }
      case 409: {
        if (process.client) {
          const store = useStore()
          if (store.token) {
            return doRequest(response)
          }
        }
        break
      }
      default:
        break
    }
    return Promise.reject(res)
  },
  error => {
    return Promise.reject(error)
  })

async function doRequest(response) {
  if (process.client) {
    const store = useStore()
    let tokenInfo = store.token
    if (typeof tokenInfo === 'string') {
      try {
        tokenInfo = JSON.parse(tokenInfo)
      } catch (e) {}
    }
    await store.relogin({
      client_id: 'music_story',
      client_secret: 'secret',
      refresh_token: tokenInfo.refresh_token,
    })
    const config = response.config
    const newToken = store.token
    config.headers.Authorization = newToken.token_type + ' ' + newToken.access_token
    const res = await axios.request(config)
    return res.data
  }
}

export default service
