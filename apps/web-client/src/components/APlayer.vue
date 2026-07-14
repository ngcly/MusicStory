<template>
  <div ref="playerRef"></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import APlayer from 'aplayer'
import 'aplayer/dist/APlayer.min.css'

const props = defineProps({
  audio: { type: Array, default: () => [] },
  lrcType: { type: Number, default: 3 },
  autoplay: { type: Boolean, default: false },
  fixed: { type: Boolean, default: false },
})

const playerRef = ref(null)
let player = null

onMounted(() => {
  player = new APlayer({
    container: playerRef.value,
    audio: props.audio,
    lrcType: props.lrcType,
    autoplay: props.autoplay,
    fixed: props.fixed,
  })
})

watch(() => props.audio, (newAudio) => {
  if (player && newAudio.length > 0) {
    player.list.clear()
    newAudio.forEach(song => player.list.add(song))
  }
}, { deep: true })

onBeforeUnmount(() => {
  if (player) {
    player.destroy()
  }
})
</script>
