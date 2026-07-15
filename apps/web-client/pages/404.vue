<template>
  <div class="min-h-screen flex items-center justify-center bg-slate-50 px-4">
    <div class="max-w-md w-full text-center space-y-6 bg-white/70 backdrop-blur-md border border-slate-200/50 p-8 rounded-2xl shadow-lg">
      <div class="flex justify-center">
        <CompassIcon class="w-16 h-16 text-teal-500 animate-spin-slow" />
      </div>
      <h1 class="text-6xl font-extrabold text-slate-800 tracking-widest font-serif">404</h1>
      <div class="text-sm font-semibold text-slate-600">
        迷失了音符，翻过了白纸
      </div>
      <p class="text-xs text-slate-400 leading-relaxed">
        抱歉，您所寻找的乐章不存在或已被移除。回溯脚步，重新启程。
      </p>
      <div>
        <NuxtLink to="/" class="h-9 px-6 inline-flex items-center justify-center bg-slate-900 hover:bg-slate-800 text-white text-xs font-semibold rounded-full transition shadow-sm">
          回到音书首页
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Compass as CompassIcon } from 'lucide-vue-next'

definePageMeta({
  layout: false
})
</script>

<style scoped>
.animate-spin-slow {
  animation: spin 8s linear infinite;
}
@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
