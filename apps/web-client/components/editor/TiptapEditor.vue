<template>
  <div class="tiptap-editor-container">
    <!-- 悬浮气泡菜单 Bubble Menu -->
    <bubble-menu
      v-if="editor"
      :editor="editor"
      :tippy-options="{ duration: 100 }"
      class="bubble-menu"
    >
      <button
        @click="editor.chain().focus().toggleBold().run()"
        :class="{ 'is-active': editor.isActive('bold') }"
        class="bubble-btn"
        title="加粗"
      >
        <BoldIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().toggleItalic().run()"
        :class="{ 'is-active': editor.isActive('italic') }"
        class="bubble-btn"
        title="斜体"
      >
        <ItalicIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().toggleStrike().run()"
        :class="{ 'is-active': editor.isActive('strike') }"
        class="bubble-btn"
        title="删除线"
      >
        <StrikethroughIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().toggleBlockquote().run()"
        :class="{ 'is-active': editor.isActive('blockquote') }"
        class="bubble-btn"
        title="引用"
      >
        <QuoteIcon class="w-4 h-4" />
      </button>
    </bubble-menu>

    <!-- 固定工具栏 Toolbar (位于顶部，毛玻璃悬浮) -->
    <div class="editor-toolbar" v-if="editor">
      <button
        @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 2 }) }"
        class="toolbar-btn text-xs font-bold"
        title="二级标题"
      >
        H2
      </button>
      <button
        @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 3 }) }"
        class="toolbar-btn text-xs font-bold"
        title="三级标题"
      >
        H3
      </button>
      <div class="toolbar-divider"></div>
      <button
        @click="editor.chain().focus().toggleBold().run()"
        :class="{ 'is-active': editor.isActive('bold') }"
        class="toolbar-btn"
        title="加粗"
      >
        <BoldIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().toggleItalic().run()"
        :class="{ 'is-active': editor.isActive('italic') }"
        class="toolbar-btn"
        title="斜体"
      >
        <ItalicIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().toggleBulletList().run()"
        :class="{ 'is-active': editor.isActive('bulletList') }"
        class="toolbar-btn"
        title="无序列表"
      >
        <ListIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().toggleOrderedList().run()"
        :class="{ 'is-active': editor.isActive('orderedList') }"
        class="toolbar-btn"
        title="有序列表"
      >
        <ListOrderedIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().toggleBlockquote().run()"
        :class="{ 'is-active': editor.isActive('blockquote') }"
        class="toolbar-btn"
        title="引用"
      >
        <QuoteIcon class="w-4 h-4" />
      </button>
      <button
        @click="editor.chain().focus().setHorizontalRule().run()"
        class="toolbar-btn"
        title="分割线"
      >
        <MinusIcon class="w-4 h-4" />
      </button>
      <div class="toolbar-divider"></div>
      <button @click="triggerImageUpload" class="toolbar-btn" title="插入图片">
        <ImageIcon class="w-4 h-4" />
      </button>
      <input
        type="file"
        ref="fileInputRef"
        class="hidden"
        accept="image/*"
        @change="handleImageUpload"
      />
    </div>

    <!-- Tiptap 编辑画布 -->
    <editor-content :editor="editor" class="tiptap-editor" />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { Editor, EditorContent, BubbleMenu } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Placeholder from '@tiptap/extension-placeholder'
import Image from '@tiptap/extension-image'
import {
  Bold as BoldIcon,
  Italic as ItalicIcon,
  Strikethrough as StrikethroughIcon,
  Quote as QuoteIcon,
  List as ListIcon,
  ListOrdered as ListOrderedIcon,
  Minus as MinusIcon,
  Image as ImageIcon
} from 'lucide-vue-next'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: '在这页白纸上，留下您的思绪与故事...'
  }
})

const emit = defineEmits(['update:modelValue', 'upload-image'])

const editor = ref(null)
const fileInputRef = ref(null)

onMounted(() => {
  editor.value = new Editor({
    content: props.modelValue,
    extensions: [
      StarterKit,
      Image,
      Placeholder.configure({
        placeholder: props.placeholder
      })
    ],
    onUpdate: () => {
      emit('update:modelValue', editor.value.getHTML())
    }
  })
})

watch(() => props.modelValue, (val) => {
  if (editor.value && editor.value.getHTML() !== val) {
    editor.value.commands.setContent(val, false)
  }
})

function triggerImageUpload() {
  if (fileInputRef.value) {
    fileInputRef.value.click()
  }
}

function handleImageUpload(event) {
  const file = event.target.files[0]
  if (!file) return
  emit('upload-image', file, (url) => {
    if (editor.value && url) {
      editor.value.chain().focus().setImage({ src: url }).run()
    }
  })
  event.target.value = ''
}

onBeforeUnmount(() => {
  if (editor.value) {
    editor.value.destroy()
  }
})
</script>

<style scoped>
.tiptap-editor-container {
  display: flex;
  flex-direction: column;
  background: #fbf9f6;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.02);
}

/* --- 顶部毛玻璃悬浮工具栏 --- */
.editor-toolbar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}
.toolbar-btn {
  padding: 6px;
  border-radius: 6px;
  color: #475569;
  font-size: 13px;
  font-weight: 500;
  transition: all 0.2s;
  background: transparent;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}
.toolbar-btn:hover {
  background: rgba(0, 0, 0, 0.04);
  color: #0f172a;
}
.toolbar-btn.is-active {
  background: #2c3e50;
  color: #fff;
}
.toolbar-divider {
  width: 1px;
  height: 18px;
  background: rgba(0, 0, 0, 0.08);
  margin: 0 4px;
}

/* --- Tiptap 核心编辑画布 --- */
.tiptap-editor {
  padding: 30px;
  min-height: 500px;
  outline: none;
  font-family: 'Lora', 'Noto Serif SC', serif;
  font-size: 16px;
  line-height: 1.8;
  color: #1e293b;
}

/* 覆盖 tiptap editor 容器本身的默认 focus 样式 */
:deep(.ProseMirror) {
  outline: none;
  min-height: 500px;
}
:deep(.ProseMirror p.is-editor-empty:first-child::before) {
  content: attr(data-placeholder);
  float: left;
  color: #94a3b8;
  pointer-events: none;
  height: 0;
}
:deep(.ProseMirror blockquote) {
  border-left: 4px solid #cbd5e1;
  padding-left: 16px;
  color: #475569;
  font-style: italic;
  margin: 16px 0;
}
:deep(.ProseMirror img) {
  max-width: 100%;
  border-radius: 8px;
  margin: 20px auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}
:deep(.ProseMirror hr) {
  border: none;
  border-top: 1px dashed rgba(0, 0, 0, 0.15);
  margin: 24px 0;
}

/* --- 选中文字悬浮菜单 --- */
.bubble-menu {
  display: flex;
  background: #0f172a;
  border-radius: 6px;
  padding: 4px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}
.bubble-btn {
  padding: 6px 8px;
  border: none;
  background: transparent;
  color: #94a3b8;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}
.bubble-btn:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.1);
}
.bubble-btn.is-active {
  color: #1abc9c;
  background: rgba(255, 255, 255, 0.15);
}
</style>
