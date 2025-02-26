<template>
  <div class="chat-container">
    <!-- 聊天消息显示区域 -->
    <div class="chat-messages" ref="chatMessages">
      <div class="chat-messages-header">
        <div class="chat-messages-title">____________</div>
        <!-- 添加显示对方用户的账户 -->
        <div class="chat-user-account">对方用户: {{ otherUser }}</div>
      </div>
      <div class="chat-separator"></div>
      <ChatMessage
          v-for="(message, index) in messages"
          :key="index"
          :message="message"
          :isSelf="message.isSelf"
      />
    </div>
    <!-- 添加分隔线 -->
    <div class="chat-separator"></div>
    <div class="chat-input">
      <!-- 上传文件按钮 -->
      <input type="file" ref="fileInput" @change="handleFileUpload" accept="image/*, .pdf, .doc, .docx" style="display: none;" />
      <el-button icon="el-icon-folder-add" @click="openFileInput"></el-button>
      <!-- 输入消息框 -->
      <el-input
          v-model="newMessage"
          placeholder="输入消息..."
          type="textarea"
          :autosize="{ minRows: 3, maxRows: 6 }"
          @keyup.enter="sendTextMessage"
      ></el-input>
      <!-- 发送按钮 -->
      <el-button type="primary" @click="sendTextMessage">发送</el-button>
    </div>
  </div>
</template>

<script>
import ChatMessage from './ChatMessage.vue';
import axios from 'axios';

export default {
  components: {
    ChatMessage
  },
  data() {
    return {
      messages: [],
      newMessage: '',
      file: null,
      otherUser: '用户1', // 初始化对方用户
      websocket: null,
      uid: localStorage.getItem('uid'),
      acid: '',
      clientId: localStorage.getItem('ms_username'),
      qq : localStorage.getItem('qq'),
      sendjson: {
        'user': this.clientId,
        'acid': this.otherUser,
        'content': '',
        'type': '',
        'time': ''
      },
    };
  },
  created() {
    this.getData();


    if ('WebSocket' in window) {
      // 连接WebSocket节点，添加 token 参数
      const token = localStorage.getItem('token');
      this.websocket = new WebSocket(this.WSApi + "ws/" + localStorage.getItem('token')+ "/" + localStorage.getItem('ms_username'));

      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = () => {
        this.websocket.close();
      };

      // 连接发生错误的回调方法
      this.websocket.onerror = () => {
        this.setMessageInnerHTML("error");
      };

      // 连接成功建立的回调方法
      this.websocket.onopen = () => {
        this.setMessageInnerHTML("连接成功");
      };

      // 接收到消息的回调方法
      this.websocket.onmessage = (event) => {
        let data = JSON.parse(event.data);
        if(data.username == this.otherUser){
          this.messages.push(data);
          this.scrollToBottom();
        }

        //this.setMessageInnerHTML(event.data);
      };

      // 连接关闭的回调方法
      this.websocket.onclose = () => {
        this.setMessageInnerHTML("close");
      };
    } else {
      alert('Not support websocket');
    }
  },
  methods: {
    // 发送文本消息
    sendTextMessage() {
      if (this.newMessage.trim() !== '') {
        this.sendjson.content = this.newMessage;
        this.sendjson.type = 'text';
        this.sendjson.time = new Date().getTime();
        this.sendjson.user = this.clientId;
        this.sendjson.touser = this.otherUser;
        this.websocket.send(JSON.stringify(this.sendjson));
        this.messages.push({
          username: '我',
          avatar: this.qq,
          content: this.newMessage,
          type: 'text',
          isSelf: true
        });
        this.newMessage = '';
        this.scrollToBottom();
      }
    },
    // 处理文件上传
    handleFileUpload(event) {
      this.file = event.target.files[0];
      if (this.file) {
        this.uploadFile();
      }
    },
    // 打开文件选择框
    openFileInput() {
      this.$refs.fileInput.click();
    },
    // 上传文件
    async uploadFile() {
      const formData = new FormData();
      formData.append('file', this.file);

      try {
        const response = await axios.post(this.Api+'User/upload' , formData, {
          headers: {
            'token': localStorage.getItem('token')
          }
        });

        const fileUrl = response.data.data;
        const messageType = this.file.type.startsWith('image') ? 'image' : 'file';
        this.sendjson.content = fileUrl;
        this.sendjson.type = messageType;
        this.sendjson.time = new Date().getTime();
        this.sendjson.user = this.clientId;
        this.sendjson.touser = this.otherUser;
        this.websocket.send(JSON.stringify(this.sendjson));

        this.messages.push({
          username: '我',
          avatar: this.qq,
          content: fileUrl,
          type: messageType,
          isSelf: true
        });
        this.scrollToBottom();
      } catch (error) {
        console.error('文件上传失败', error);
      }
    },
    // 将消息显示在网页上
    setMessageInnerHTML(innerHTML) {
      this.messages.push({
        username: "系统",
        avatar: 666666,
        content: innerHTML,
        type: 'text',
        isSelf: false
      });
      this.scrollToBottom();
    },
    // 获取数据
    getData() {
      const hash = window.location.hash;

      // 去掉开头的 '#'
      const hashWithoutHash = hash.substring(1);

      // 创建 URL 对象
      const url = new URL(hashWithoutHash, this.Api);

      // 使用 URLSearchParams 解析查询参数
      const searchParams = new URLSearchParams(url.search);

      // 获取 id 参数
      this.acid = searchParams.get('id');

      this.$axios.post(this.Api + 'User/talklist', {
        'id': this.acid
      }, {
        headers: {
          'token': localStorage.getItem('token')
        }
      }).then(res => {
        this.messages = res.data.data;
        this.otherUser = res.data.msg;
        this.$nextTick(() => {
          this.scrollToBottom();
        });
        this.scrollToBottom();
      }, error => {
        console.log(error);
        this.$router.push('/login');
        this.$message.error('登录信息已过期，请重新登录');
      });
    },
    // 滚动到聊天框底部
    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessages = this.$refs.chatMessages;
        chatMessages.scrollTop = chatMessages.scrollHeight;
      });
    }
  }
};
</script>

<style scoped>
/* 聊天容器样式 */
.chat-container {
  display: flex;
  flex-direction: column;
  height: 80vh;
  padding: 20px;
  background-color: #f0f3f5;
}

/* 聊天消息显示区域样式 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #fff;
  border-radius: 10px;
  margin-bottom: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 聊天输入区域样式 */
.chat-input {
  display: flex;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 输入消息框样式 */
.chat-input .el-input {
  flex: 1;
  margin-right: 10px;
}

/* 文件上传按钮样式 */
.chat-input .el-button {
  margin-right: 10px;
}

/* 添加对方用户账户样式 */
.chat-messages-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.chat-user-account {
  font-size: 14px;
  color: #666;
}
</style>