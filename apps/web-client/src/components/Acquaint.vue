<template>
  <div ref="wrap" class="wrap">
    <div ref="content" class="content" :class="animationClass" :style="contentStyle" @animationend="onAnimationEnd" @webkitAnimationEnd="onAnimationEnd">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'

const props = defineProps({
  content: { default: '' },
  delay: { type: Number, default: 0.5 },
  speed: { type: Number, default: 100 },
})

const wrap = ref(null)
const content = ref(null)
const wrapWidth = ref(0)
const firstRound = ref(true)
const duration = ref(0)
const offsetWidth = ref(0)
const animationClass = ref('')

const contentStyle = computed(() => ({
  paddingLeft: (firstRound.value ? 0 : wrapWidth.value) + 'px',
  animationDelay: (firstRound.value ? props.delay : 0) + 's',
  animationDuration: duration.value + 's',
}))

watch(() => props.content, () => {
  nextTick(() => {
    const wrapEl = wrap.value
    const contentEl = content.value
    if (!wrapEl || !contentEl) return
    const ww = wrapEl.getBoundingClientRect().width
    const ow = contentEl.getBoundingClientRect().width
    wrapWidth.value = ww
    offsetWidth.value = ow
    duration.value = ow / props.speed
    animationClass.value = 'animate'
  })
})

function onAnimationEnd() {
  firstRound.value = false
  duration.value = (offsetWidth.value + wrapWidth.value) / props.speed
  animationClass.value = 'animate-infinite'
}
</script>

<style scoped>
.wrap {
  width: 100%; height: 24px; overflow: hidden; position: relative;
  background: rgb(253, 252, 251); padding: 0;
}
.wrap .content { position: absolute; white-space: nowrap; }
.animate { animation: paomadeng linear; }
.animate-infinite { animation: paomadeng-infinite linear infinite; }
@keyframes paomadeng { to { transform: translate3d(-100%, 0, 0); } }
@keyframes paomadeng-infinite { to { transform: translate3d(-100%, 0, 0); } }
</style>
