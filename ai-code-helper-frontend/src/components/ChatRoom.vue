<template>
  <div class="chat-room">
    <div class="chat-messages" ref="messagesContainer">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message', msg.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="message-avatar">
          <span v-if="msg.role === 'user'">👤</span>
          <span v-else>🤖</span>
        </div>
        <div class="message-content">
          <div class="message-role">{{ msg.role === 'user' ? '用户' : 'AI 助手' }}</div>
          <div class="message-text" v-html="formatMessage(msg.content)"></div>
        </div>
      </div>
      <div v-if="isLoading" class="message ai-message">
        <div class="message-avatar">🤖</div>
        <div class="message-content">
          <div class="message-role">AI 助手</div>
          <div class="message-text typing">
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="chat-input">
      <div class="input-container">
        <textarea
          v-model="inputMessage"
          @keydown.enter.exact.prevent="sendMessage"
          placeholder="请输入您的编程问题..."
          rows="1"
          ref="inputRef"
        ></textarea>
        <button 
          @click="sendMessage" 
          :disabled="!inputMessage.trim() || isLoading"
          class="send-btn"
        >
          <span v-if="!isLoading">发送</span>
          <span v-else>...</span>
        </button>
      </div>
      <div class="session-info">
        会话 ID: {{ memoryId }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { generateMemoryId, chatStream } from '../api/chat.js'

const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const memoryId = ref(null)
const messagesContainer = ref(null)
const inputRef = ref(null)
let currentEventSource = null

onMounted(() => {
  memoryId.value = generateMemoryId()
  messages.value.push({
    role: 'assistant',
    content: '您好！我是 AI 编程小助手，可以帮助您解答编程学习和求职面试相关的问题。请问有什么可以帮助您的吗？'
  })
})

onUnmounted(() => {
  if (currentEventSource) {
    currentEventSource.close()
  }
})

const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const formatMessage = (content) => {
  if (!content) return ''
  return content
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/```(\w*)\n?([\s\S]*?)```/g, '<pre><code class="language-$1">$2</code></pre>')
}

const sendMessage = async () => {
  const message = inputMessage.value.trim()
  if (!message || isLoading.value) return
  
  messages.value.push({
    role: 'user',
    content: message
  })
  
  inputMessage.value = ''
  isLoading.value = true
  await scrollToBottom()
  
  let aiResponse = ''
  messages.value.push({
    role: 'assistant',
    content: ''
  })
  
  currentEventSource = chatStream(
    memoryId.value,
    message,
    (data) => {
      aiResponse += data
      messages.value[messages.value.length - 1].content = aiResponse
      scrollToBottom()
    },
    (error) => {
      console.error('SSE Error:', error)
      if (!aiResponse) {
        messages.value[messages.value.length - 1].content = '抱歉，发生了错误，请稍后重试。'
      }
    },
    () => {
      isLoading.value = false
      currentEventSource = null
    }
  )
}
</script>

<style scoped>
.chat-room {
  flex: 1;
  display: flex;
  flex-direction: column;
  max-width: 900px;
  width: 100%;
  margin: 0 auto;
  background: #fff;
  border-radius: 20px 20px 0 0;
  box-shadow: 0 -5px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f8f9fa;
}

.message {
  display: flex;
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-message {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.user-message .message-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin-left: 12px;
}

.ai-message .message-avatar {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  margin-right: 12px;
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 16px;
  position: relative;
}

.user-message .message-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.ai-message .message-content {
  background: white;
  color: #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.message-role {
  font-size: 12px;
  margin-bottom: 4px;
  opacity: 0.7;
}

.message-text {
  line-height: 1.6;
  word-break: break-word;
}

.message-text :deep(code) {
  background: rgba(0, 0, 0, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Consolas', monospace;
  font-size: 0.9em;
}

.message-text :deep(pre) {
  background: #2d2d2d;
  color: #f8f8f2;
  padding: 12px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 8px 0;
}

.message-text :deep(pre code) {
  background: none;
  padding: 0;
}

.typing {
  display: flex;
  gap: 4px;
  padding: 8px 0;
}

.typing-dot {
  width: 8px;
  height: 8px;
  background: #667eea;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-dot:nth-child(1) {
  animation-delay: 0s;
}

.typing-dot:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

.chat-input {
  padding: 16px 20px;
  background: white;
  border-top: 1px solid #eee;
}

.input-container {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.input-container textarea {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 24px;
  font-size: 15px;
  resize: none;
  outline: none;
  font-family: inherit;
  line-height: 1.5;
  max-height: 120px;
  transition: border-color 0.3s;
}

.input-container textarea:focus {
  border-color: #667eea;
}

.send-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 24px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.send-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.session-info {
  text-align: center;
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}
</style>
