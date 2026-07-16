<template>
  <div
    class="cat-body-container"
    :class="{
      'is-hovered': isHovered,
      'is-clicking': isClicking,
      'is-sleeping': isSleeping
    }"
  >
    <svg viewBox="0 0 100 100" class="cat-svg" aria-hidden="true">
      <!-- 尾巴 -->
      <path
        d="M 65 75 Q 85 70 82 50 Q 80 35 90 32"
        fill="none"
        stroke="currentColor"
        stroke-width="7"
        stroke-linecap="round"
        class="cat-tail"
      />

      <!-- 身体 -->
      <ellipse cx="50" cy="72" rx="25" ry="18" fill="currentColor" class="cat-body" />

      <!-- 肚皮 (浅色区域) -->
      <ellipse cx="50" cy="74" rx="15" ry="10" class="cat-belly" />

      <!-- 头部组 (打盹时整体前倾) -->
      <g class="cat-head-group">
        <!-- 头部 -->
        <circle cx="50" cy="46" r="16" fill="currentColor" class="cat-head" />

        <!-- 左耳 + 内耳 -->
        <polygon points="36,40 24,20 42,32" fill="currentColor" class="cat-ear-left" />
        <polygon points="35,37 28,23 40,32" class="cat-inner-ear-left" />

        <!-- 右耳 + 内耳 -->
        <polygon points="64,40 76,20 58,32" fill="currentColor" class="cat-ear-right" />
        <polygon points="65,37 72,23 60,32" class="cat-inner-ear-right" />

        <!-- 眼睛区 (含随机眨眼动画) -->
        <g class="cat-eyes-group">
          <template v-if="!isHovered">
            <!-- 闭眼弧线 (打盹 / 休眠) -->
            <path d="M 39 46 Q 44 49 45 46" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" class="cat-eye-left" />
            <path d="M 61 46 Q 56 49 55 46" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" class="cat-eye-right" />
          </template>
          <template v-else>
            <!-- 睁眼椭圆 (带随机眨眼) -->
            <ellipse cx="41" cy="45" rx="2" ry="3" fill="currentColor" class="cat-eye-left" />
            <ellipse cx="59" cy="45" rx="2" ry="3" fill="currentColor" class="cat-eye-right" />
            <!-- 微笑小红晕 -->
            <ellipse cx="36" cy="49" rx="2.5" ry="1" class="cat-blush" />
            <ellipse cx="64" cy="49" rx="2.5" ry="1" class="cat-blush" />
          </template>
        </g>

        <!-- 胡须 -->
        <g class="cat-whiskers" opacity="0.7">
          <line x1="30" y1="48" x2="16" y2="46" stroke="currentColor" stroke-width="1.5" />
          <line x1="30" y1="51" x2="14" y2="52" stroke="currentColor" stroke-width="1.5" />
          <line x1="70" y1="48" x2="84" y2="46" stroke="currentColor" stroke-width="1.5" />
          <line x1="70" y1="51" x2="86" y2="52" stroke="currentColor" stroke-width="1.5" />
        </g>

        <!-- 鼻子 -->
        <polygon points="48,49 52,49 50,51" class="cat-nose" />

        <!-- 嘴巴 (说话时开合) -->
        <g class="cat-mouth-group" :class="{ 'is-speaking': isSpeaking }">
          <path d="M 47 53 Q 50 54 53 53" fill="none" stroke="#94a3b8" stroke-width="1" stroke-linecap="round" />
        </g>

        <!-- 休眠 Zzz -->
        <g v-if="isSleeping" class="cat-zzz">
          <text x="68" y="30" class="zzz-text zzz-1">z</text>
          <text x="76" y="22" class="zzz-text zzz-2">z</text>
          <text x="84" y="14" class="zzz-text zzz-3">Z</text>
        </g>
      </g>

      <!-- 前爪 (点击时踩奶) -->
      <g class="cat-paws" :class="{ 'is-active': isClicking }">
        <ellipse cx="34" cy="82" rx="6" ry="4" fill="currentColor" class="cat-paw-left" />
        <ellipse cx="66" cy="82" rx="6" ry="4" fill="currentColor" class="cat-paw-right" />
      </g>
    </svg>
  </div>
</template>

<script setup>
defineProps({
  isHovered: { type: Boolean, default: false },
  isClicking: { type: Boolean, default: false },
  isSpeaking: { type: Boolean, default: false },
  isSleeping: { type: Boolean, default: false },
})
</script>

<style scoped>
/* —— 容器 —— */
.cat-body-container {
  width: 100%;
  height: 100%;
  transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.cat-body-container:hover {
  transform: translateY(-4px) scale(1.05);
}

.cat-body-container.is-clicking {
  transform: scale(0.9) translateY(4px);
}

/* 休眠时整只猫微微下沉 */
.cat-body-container.is-sleeping {
  transform: translateY(4px);
}

.cat-svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 4px 10px rgba(0, 0, 0, 0.1));
  transition: filter 0.6s;
}

.is-sleeping .cat-svg {
  filter: drop-shadow(0 2px 6px rgba(0, 0, 0, 0.06));
}

/* —— 颜色（CSS 自定义属性继承，带回退） —— */
.cat-body,
.cat-head,
.cat-ear-left,
.cat-ear-right,
.cat-paw-left,
.cat-paw-right {
  color: var(--cat-primary, #1e293b);
}

.cat-tail {
  color: var(--cat-secondary, #334155);
}

.cat-whiskers {
  color: var(--cat-whisker, #64748b);
}

.cat-eyes-group {
  color: var(--cat-eyes, #e2e8f0);
}

/* 肚皮和内耳用更暖更浅的色调 */
.cat-belly {
  fill: var(--cat-belly, #cbd5e1);
  opacity: 0.45;
}

.cat-inner-ear-left,
.cat-inner-ear-right {
  fill: var(--cat-inner-ear, #f1b5c0);
  opacity: 0.6;
}

/* 鼻子 */
.cat-nose {
  fill: #fda4af;
}

/* 腮红 */
.cat-blush {
  fill: #f87171;
  opacity: 0.6;
}

/* —— 呼吸动画 —— */
.cat-body,
.cat-head-group {
  transform-origin: 50% 80%;
  animation: breathing 4s ease-in-out infinite;
}

@keyframes breathing {
  0%, 100% { transform: scaleY(1); }
  50%      { transform: scaleY(1.04); }
}

/* 休眠时呼吸变慢变深 */
.is-sleeping .cat-body,
.is-sleeping .cat-head-group {
  animation: breathingSleep 6s ease-in-out infinite;
}

@keyframes breathingSleep {
  0%, 100% { transform: scaleY(1); }
  50%      { transform: scaleY(1.06); }
}

/* 休眠时头部前倾 */
.is-sleeping .cat-head-group {
  transform-origin: 50px 62px;
  animation: headDroop 6s ease-in-out infinite;
}

@keyframes headDroop {
  0%, 100% { transform: rotate(0deg) translateY(0); }
  50%      { transform: rotate(6deg) translateY(2px); }
}

/* —— 尾巴 —— */
.cat-tail {
  transform-origin: 65px 75px;
  animation: tailWag 6s ease-in-out infinite;
  transition: animation 0.3s;
}

.is-hovered .cat-tail {
  animation: tailWagActive 1.8s ease-in-out infinite;
}

.is-sleeping .cat-tail {
  animation: tailWagSleep 8s ease-in-out infinite;
}

@keyframes tailWag {
  0%, 100% { transform: rotate(0deg); }
  50%      { transform: rotate(8deg) translate(-1px, 1px); }
}

@keyframes tailWagActive {
  0%, 100% { transform: rotate(0deg); }
  33%      { transform: rotate(12deg) translate(-2px, 1px); }
  66%      { transform: rotate(-6deg) translate(1px, -1px); }
}

@keyframes tailWagSleep {
  0%, 100% { transform: rotate(-4deg); }
  50%      { transform: rotate(2deg) translate(-0.5px, 1px); }
}

/* —— 耳朵抖动 —— */
.cat-ear-left {
  transform-origin: 36px 40px;
  animation: earTwitchLeft 8s ease-in-out infinite;
}

.cat-ear-right {
  transform-origin: 64px 40px;
  animation: earTwitchRight 8s ease-in-out infinite 0.5s;
}

@keyframes earTwitchLeft {
  0%, 94%, 100% { transform: rotate(0deg); }
  96%, 98%      { transform: rotate(-8deg); }
}

@keyframes earTwitchRight {
  0%, 94%, 100% { transform: rotate(0deg); }
  96%, 98%      { transform: rotate(8deg); }
}

/* —— 随机眨眼动画 (每个眼睛独立延迟) —— */
.cat-eye-left {
  transform-origin: 41px 45px;
  animation: blinkLeft 5s ease-in-out infinite;
}

.cat-eye-right {
  transform-origin: 59px 45px;
  animation: blinkRight 5s ease-in-out infinite 1.2s;
}

@keyframes blinkLeft {
  0%, 42%, 48%, 100% { transform: scaleY(1); }
  45%                 { transform: scaleY(0.1); }
}

@keyframes blinkRight {
  0%, 42%, 48%, 100% { transform: scaleY(1); }
  45%                 { transform: scaleY(0.1); }
}

/* 休眠时眼睛保持闭合，不再独立眨眼 */
.is-sleeping .cat-eye-left,
.is-sleeping .cat-eye-right {
  animation: none;
}

/* —— 嘴巴说话动画 —— */
.cat-mouth-group.is-speaking .cat-mouth-group path,
.is-speaking path {
  animation: speak 0.4s ease-in-out infinite alternate;
}

@keyframes speak {
  from { d: path("M 47 53 Q 50 54 53 53"); }
  to   { d: path("M 46 53 Q 50 56 54 53"); }
}

/* —— 爪子踩奶 —— */
.cat-paws {
  opacity: 0;
  transform-origin: 50% 80%;
  transition: opacity 0.15s, transform 0.15s;
}

.cat-paws.is-active {
  opacity: 0.85;
  animation: pawKnead 0.3s ease-in-out;
}

@keyframes pawKnead {
  0%   { transform: scaleY(0.5); }
  50%  { transform: scaleY(1.3); }
  100% { transform: scaleY(1); }
}

/* —— Zzz 飘散 —— */
.cat-zzz {
  font-family: "Comic Sans MS", cursive, sans-serif;
  pointer-events: none;
}

.zzz-text {
  fill: var(--cat-whisker, #94a3b8);
  opacity: 0;
}

.is-sleeping .zzz-1 {
  animation: zzzFloat 2s ease-out infinite 0s;
}

.is-sleeping .zzz-2 {
  animation: zzzFloat 2s ease-out infinite 0.7s;
}

.is-sleeping .zzz-3 {
  animation: zzzFloat 2s ease-out infinite 1.4s;
}

@keyframes zzzFloat {
  0%   { opacity: 0; transform: translate(0, 0) scale(0.5); }
  30%  { opacity: 0.7; }
  100% { opacity: 0; transform: translate(-6px, -12px) scale(1.2); }
}
</style>