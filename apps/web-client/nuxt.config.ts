// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2026-07-15',
  ssr: true, // 启用服务端渲染以利于 SEO
  modules: [
    '@nuxtjs/tailwindcss',
    '@pinia/nuxt'
  ],
  app: {
    head: {
      title: '音书 - 高颜值文艺深阅读与听歌社区',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: '结合了高品质音乐播放与优雅文字创作的文艺社区网站。' }
      ],
      script: [
        {
          children: `if (typeof global === 'undefined') { window.global = window; }`,
          type: 'text/javascript'
        }
      ],
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
        { rel: 'preconnect', href: 'https://fonts.googleapis.com' },
        { rel: 'preconnect', href: 'https://fonts.gstatic.com', crossorigin: '' },
        { rel: 'stylesheet', href: 'https://fonts.googleapis.com/css2?family=Lora:ital,wght@0,400..700;1,400..700&family=Noto+Serif+SC:wght@200..900&display=swap' }
      ]
    }
  },
  // 生产构建配置，启用 Vite 压缩
  vite: {
    server: {
      port: 9091
    }
  }
})
