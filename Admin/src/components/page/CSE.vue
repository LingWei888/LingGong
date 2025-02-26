<template>
  <div class="chat-container">
    <!-- 用户列表区域 -->
    <div class="user-list">
      <div
          v-for="user in userList"
          :key="user.id"
          class="user-item"
          :class="{ 'active': user.toid === selectedUserId }"
          @click="selectUser(user.toid, user.toName)"
      >
        <img :src="getQQAvatar(user.qq)" alt="User Avatar" class="user-avatar" />
        <div class="user-info">
          <div class="user-name">{{ user.toName }}</div>
          <div class="user-num">{{ user.num }}</div>
        </div>
        <div v-if="user.unread > 0" class="unread-badge">{{ user.unread }}</div>
      </div>
    </div>

    <!-- 聊天消息显示区域 -->
    <div class="chat-main">
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

      <!-- 消息发送框 -->
      <div class="chat-input-area">
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
      otherUser: '系统', // 初始化对方用户
      websocket: null,
      uid: localStorage.getItem('uid'),
      acid: '',
      clientId: localStorage.getItem('ms_username'),
      qq: localStorage.getItem('qq'),
      sendjson: {
        'user': this.clientId,
        'acid': this.otherUser,
        'content': '',
        'type': '',
        'time': ''
      },
      userList: [],
      selectedUserId: null,
      selectedUserName: null,
    };
  },
  created() {
    this.getUserList();
    this.scrollToBottom();

    if ('WebSocket' in window) {
      // 连接WebSocket节点，添加 token 参数
      const token = localStorage.getItem('token');
      this.websocket = new WebSocket(this.WSApi + "ws/" + localStorage.getItem('token')+ "/官方客服" );

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
        this.messages.push(data);
        this.scrollToBottom();
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
    // 获取用户列表
    getUserList() {
      this.$axios.post(this.Api + 'Admin/CSE', {}, {
        headers: {
          'token': localStorage.getItem('token')
        }
      }).then(res => {
        this.userList = res.data.data.records;
      }, error => {
        console.log(error);
        this.$router.push('/login');
        this.$message.error('登录信息已过期，请重新登录');
      });
    },
    // 选择用户
    selectUser(userId,userName) {
      this.selectedUserId = userId;
      this.selectedUserName = userName;
      this.getChatHistory(userId);
    },
    // 获取聊天记录
    getChatHistory(userId) {
      this.$axios.post(this.Api + 'Admin/CSETalk', {
        'id': userId
      }, {
        headers: {
          'token': localStorage.getItem('token')
        }
      }).then(res => {
        this.messages = res.data.data;
        this.otherUser = res.data.msg;
        this.selectedUserName = res.data.msg;
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
    // 发送文本消息
    sendTextMessage() {
      if (this.newMessage.trim() !== '') {
        this.sendjson.content = this.newMessage;
        this.sendjson.type = 'text';
        this.sendjson.time = new Date().getTime();
        this.sendjson.user = "官方客服";
        this.sendjson.touser = this.selectedUserName;
        this.websocket.send(JSON.stringify(this.sendjson));
        this.messages.push({
          username: '我',
          avatar: "666666",
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
        const response = await axios.post(this.Api+'Admin/upload' , formData, {
          headers: {
            'token': localStorage.getItem('token')
          }
        });

        const fileUrl = response.data.data;
        const messageType = this.file.type.startsWith('image') ? 'image' : 'file';
        this.sendjson.content = fileUrl;
        this.sendjson.type = messageType;
        this.sendjson.time = new Date().getTime();
        this.sendjson.user = "官方客服";
        this.sendjson.touser = this.selectedUserName;
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
    // 滚动到聊天框底部
    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessages = this.$refs.chatMessages;
        chatMessages.scrollTop = chatMessages.scrollHeight;
      });
    },
    getQQAvatar(qq) {
      return `https://q1.qlogo.cn/g?b=qq&nk=${qq}&s=100`;
    },
  }
};
</script>

<style scoped>
/* 聊天容器样式 */
.chat-container {
  display: flex;
  flex-direction: row;
  height: 80vh;
  padding: 20px;
  background-color: #f0f3f5;
}

/* 用户列表样式 */
.user-list {
  width: 200px;
  padding: 10px;
  background-color: #fff;
  border-radius: 10px;
  margin-right: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-item {
  padding: 10px;
  cursor: pointer;
  border-radius: 5px;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  position: relative;
}

.user-item:hover {
  background-color: #f0f3f5;
}

.user-item.active {
  background-color: #e0e0e0;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 16px;
  margin-bottom: 5px;
}

.user-num {
  font-size: 14px;
  color: #999;
}

.unread-badge {
  position: absolute;
  right: 10px;
  top: 10px;
  background-color: red;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}

/* 聊天主区域样式 */
.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 聊天消息显示区域样式 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #fff;
  border-radius: 10px;
}

/* 聊天输入区域样式 */
.chat-input-area {
  padding: 10px;
  background-color: #f0f3f5;
  border-top: 1px solid #ddd;
  border-radius: 0 0 10px 10px;
}

.chat-input {
  display: flex;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 10px;
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