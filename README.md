# AI Code Helper - AI 编程小助手

一个基于 Spring Boot 3 + Vue 3 的 AI 编程问答助手，帮助用户解答编程学习和求职面试相关的问题。

## 📋 项目简介

AI Code Helper 是一个专注于编程学习和求职面试的 AI 助手，提供以下核心功能：

1. **规划清晰的编程学习路线**
2. **提供项目学习建议**
3. **给出程序员求职全流程指南**（简历优化、投递技巧）
4. **分享高频面试题和面试技巧**

## 🛠️ 技术栈

### 后端
- **框架**: Spring Boot 3.5.12
- **语言**: Java 17
- **AI 框架**: LangChain4j 1.1.0-beta7
- **大模型**: 阿里云通义千问 (Qwen-max)
- **嵌入模型**: text-embedding-v4
- **工具**: Lombok, Maven
- **特性支持**: 
  - MCP (Model Context Protocol)
  - RAG (Retrieval-Augmented Generation)
  - 流式响应 (SSE)
  - 输入安全护栏 (Guardrail)

### 前端
- **框架**: Vue 3.4.21
- **构建工具**: Vite 5.2.0
- **HTTP 客户端**: Axios 1.6.8
- **UI**: 自定义现代化聊天界面

## 📁 项目结构

```
ai-code-helper/
├── src/main/java/com/lzx/aicodehelper/
│   ├── ai/                          # AI 核心模块
│   │   ├── guardrail/               # 输入安全护栏
│   │   ├── listener/                # 监听器配置
│   │   ├── mcp/                     # MCP 配置
│   │   ├── model/                   # 模型配置
│   │   ├── rag/                     # RAG 配置
│   │   ├── AiCodeHelper.java        # 手动实现 AI 服务
│   │   ├── AiCodeHelperService.java # 声明式 AI 服务接口
│   │   └── AiCodeHelperServiceFactory.java
│   ├── config/                      # 配置类
│   │   └── CorsConfig.java          # 跨域配置
│   ├── controller/                  # 控制器
│   │   └── AiController.java        # AI 聊天接口
│   └── AiCodeHelperApplication.java # 启动类
├── src/main/resources/
│   ├── docs/                        # 知识库文档
│   │   ├── Java 学习路线.md
│   │   ├── Java 热门面试题 200 道.md
│   │   └── 程序员求职指南.md
│   ├── application.yml              # 主配置文件
│   ├── application-local.yml        # 本地环境配置
│   └── system-prompt.txt            # 系统提示词
├── ai-code-helper-frontend/         # 前端项目
│   ├── src/
│   │   ├── api/
│   │   │   └── chat.js              # 聊天 API
│   │   ├── components/
│   │   │   └── ChatRoom.vue         # 聊天室组件
│   │   ├── App.vue                  # 主应用
│   │   └── main.js                  # 入口文件
│   ├── index.html
│   └── vite.config.js
└── pom.xml
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- Maven 3.6+
- 阿里云 DashScope API Key

### 1. 克隆项目
```bash
git clone <repository-url>
cd ai-code-helper
```

### 2. 配置 API Key

编辑 `src/main/resources/application-local.yml`：
```yaml
langchain4j:
  community:
    dashscope:
      chat-model:
        model-name: qwen-max
        api-key: 你的 API_KEY
      embedding-model:
        model-name: text-embedding-v4
        api-key: 你的 API_KEY
      streaming-chat-model:
        model-name: qwen-max
        api-key: 你的 API_KEY
bigmodel:
  api-key: 你的 API_KEY
```

### 3. 启动后端

```bash
# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或使用本地 Maven
mvn spring-boot:run
```

后端服务将在 `http://localhost:8081/api` 启动

### 4. 启动前端

```bash
cd ai-code-helper-frontend
npm install
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

## 📖 API 接口

### 聊天接口
```
GET /api/ai/chat?memoryId={会话 ID}&userMessage={用户消息}
```

**响应格式**: Server-Sent Events (SSE) 流式输出

**示例**:
```javascript
const eventSource = new EventSource(
  '/api/ai/chat?memoryId=123&userMessage=你好'
);

eventSource.onmessage = (event) => {
  console.log('AI 响应:', event.data);
};
```

## 💡 核心功能

### 1. 流式聊天
支持实时流式输出，用户体验更流畅

### 2. 记忆管理
通过 `memoryId` 维护多轮对话上下文

### 3. 安全护栏
内置输入安全检查，防止恶意请求

### 4. 结构化输出
支持返回结构化数据（Report 模式）

### 5. RAG 增强
基于知识库文档提供精准答案

### 6. MCP 集成
支持 Model Context Protocol 扩展能力

## 🎨 界面特点

- 🌈 渐变紫色主题设计
- 💬 实时打字动画效果
- 📝 Markdown 代码高亮支持
- 📱 响应式布局
- ✨ 流畅的消息动画

## 📝 开发笔记

项目包含详细的开发笔记，记录了：
- YAML 配置最佳实践
- LangChain4j 自动配置原理
- @Resource 与 @Autowired 的区别
- AiCodeHelper 与 AiCodeHelperService 对比
- Java Record 语法使用

详见：[笔记.md](笔记.md)

## 🔧 配置说明

### application.yml
```yaml
spring:
  application:
    name: ai-code-helper
  profiles:
    active: local
server:
  port: 8081
  servlet:
    context-path: /api
```

### System Prompt
系统提示词定义在 `system-prompt.txt`，用于设定 AI 助手的角色和职责。

## 🧪 测试

运行测试用例：
```bash
mvn test
```

测试类：
- `AiCodeHelperServiceTest.java` - AI 服务测试
- `AiCodeHelperApplicationTests.java` - 应用启动测试

## 📄 License

MIT License

## 👨‍💻 作者

lzx

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot)
- [LangChain4j](https://github.com/langchain4j/langchain4j)
- [Vue.js](https://vuejs.org/)
- [阿里云通义千问](https://www.aliyun.com/product/dashscope)
