<template>
  <div class="app-container">
    <header class="app-header">
      <h1>AI 编程小助手</h1>
      <p class="subtitle">帮助您解答编程学习和求职面试相关的问题</p>
    </header>
    <ChatRoom />
  </div>
</template>

<script setup>
import ChatRoom from './components/ChatRoom.vue'
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.app-header {
  text-align: center;
  padding: 20px;
  color: white;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.app-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
}

.subtitle {
  margin: 8px 0 0;
  font-size: 14px;
  opacity: 0.9;
}
</style>
