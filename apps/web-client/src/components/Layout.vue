<template>
  <div id="body">
    <Navbar />
    <router-view v-slot="{ Component }">
      <transition>
        <component :is="Component" />
      </transition>
    </router-view>
    <el-backtop target="#body" :right="300" />
  </div>
</template>

<script setup>
import Navbar from './Navbar.vue'
</script>

<style scoped>
#body {
  overflow-x: auto;
  height: 100%;
  padding-top: 56px;
}
</style>
