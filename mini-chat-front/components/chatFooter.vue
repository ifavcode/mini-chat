<script setup lang='ts'>
import { RouteLocationNormalizedLoaded } from 'vue-router';

const inputValue = ref('')
const userInfo: any = useUser()
const route: RouteLocationNormalizedLoaded = useRoute()

const sendMessage = async () => {
  await useLazyFetch('/api/chat/send', {
    method: 'POST',
    body: {
      message: inputValue.value,
      conveyGroupId: route.params.groupId,
      userId: userInfo.value.userId
    }
  })
  inputValue.value = ''
}
</script>
 
<template>
  <div class="chat_footer">
    <a-textarea v-model:value="inputValue" placeholder="输入你要发送的信息" :rows="4" class="input_area"
      @pressEnter="sendMessage" />
    <div class="send_btn">
      <a-button @click="sendMessage" :disabled="inputValue == ''">
        发送
      </a-button>
    </div>
  </div>
</template>
 
<style lang='scss' scoped>
.chat_footer {
  width: 100%;
  height: 100px;
  background: #F5F5F5;
  position: relative;

  .input_area {
    background: transparent;
  }

  .send_btn {
    position: absolute;
    bottom: 20px;
    right: 20px;
  }
}
</style>