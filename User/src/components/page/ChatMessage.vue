<template>
  <div class="chat-message" :class="{'self': isSelf}">
    <div class="avatar">
      <img :src="`http://q1.qlogo.cn/g?b=qq&nk=${message.avatar}&s=100`" alt="avatar">
    </div>
    <div class="content">
      <div class="username">{{ message.username }}</div>
      <div class="text" v-if="message.type === 'text'">{{ message.content }}</div>
      <img v-else-if="message.type === 'image'" :src="message.content" alt="image" class="chat-image">
      <a v-else-if="message.type === 'file'" :href="message.content" download class="chat-file"><i class="el-icon-lx-file"> </i>下载文件 <i class="el-icon-download"> </i></a>
    </div>
  </div>
</template>

<script>
import Icon from "./Icon.vue";

export default {
  components: {Icon},
  props: {
    message: {
      type: Object,
      required: true
    },
    isSelf: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>
.chat-message {
  display: flex;
  margin-bottom: 15px;
}

.chat-message.self {
  flex-direction: row-reverse;
}

.avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.chat-message.self .avatar img {
  margin-right: 0;
  margin-left: 10px;
}

.content {
  max-width: 60%;
  padding: 10px;
  border-radius: 10px;
  background-color: #f1f1f1;
}

.chat-message.self .content {
  background-color: #dcf8c6;
}

.username {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
}

.text {
  font-size: 14px;
  color: #333;
}

.chat-image {
  max-width: 100%;
  border-radius: 5px;
}

.chat-file {
  color: #007bff;
  text-decoration: none;
}
</style>