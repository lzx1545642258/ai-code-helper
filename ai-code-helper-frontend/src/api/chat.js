const API_BASE_URL = '/api'

export function generateMemoryId() {
  return Math.floor(Math.random() * 1000000000)
}

export function chatStream(memoryId, message, onMessage, onError, onComplete) {
  const url = `${API_BASE_URL}/ai/chat?memoryId=${encodeURIComponent(memoryId)}&userMessage=${encodeURIComponent(message)}`
  
  const eventSource = new EventSource(url)
  
  eventSource.onmessage = (event) => {
    if (event.data) {
      onMessage(event.data)
    }
  }
  
  eventSource.onerror = (error) => {
    eventSource.close()
    if (onError) {
      onError(error)
    }
    if (onComplete) {
      onComplete()
    }
  }
  
  eventSource.addEventListener('complete', () => {
    eventSource.close()
    if (onComplete) {
      onComplete()
    }
  })
  
  return eventSource
}
