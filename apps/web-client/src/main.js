import { createApp } from 'vue'
import pinia from './pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import { ElMessage } from 'element-plus'

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'

import App from './App.vue'
import router from './router'
import { useStore } from './store'
import 'animate.css'
import 'normalize.css'
import 'font-awesome/css/font-awesome.min.css'
import './assets/css/main.css'

library.add(faUserSecret)

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale: zhCn })

app.component('font-awesome-icon', FontAwesomeIcon)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    const store = useStore(pinia)
    if (store.token) {
      next()
    } else {
      ElMessage.error({
        customClass: 'my-message',
        message: '该页面需登录，即将自动跳转登录页。',
        onClose: () => next({ path: '/signin', query: { redirect: to.fullPath } }),
      })
    }
  } else {
    next()
  }
})

app.mount('#app')
