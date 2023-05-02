<script setup lang='ts'>
import { RouteLocationNormalizedLoaded } from 'vue-router';
const route: RouteLocationNormalizedLoaded = useRoute()
const userInfo: any = useUser()

if (userInfo.value.eventSourceListener != null) {
  userInfo.value.eventSourceListener.close()
  userInfo.value.eventSourceListener = null
}

userInfo.value.eventSourceListener = new EventSource('/api/chat/get?groupId=' + route.params.groupId)
const messageList = ref<Array<any>>([])
const chatContentEle = ref<HTMLElement>()
userInfo.value.eventSourceListener.onmessage = (e: any) => {
  let res = JSON.parse(e.data)
  messageList.value.push({
    ...res,
    message: JSON.parse(res.message)
  });
  nextTick(() => {
    (chatContentEle.value as HTMLElement).scrollTo({
      top: (chatContentEle.value as HTMLElement).scrollHeight,
      behavior: 'smooth'
    })
  })
}
</script>
 
<template>
  <Transition name="fade" appear>
    <div class="chat_content" ref="chatContentEle">
      <div class="chat_item" v-for="item in messageList">
        <div class="choose_box" v-if="item.userId == userInfo.userId"
          :class="item.userId == userInfo.userId ? 'my_chat' : ''">
          <div class="right" v-show="item.userId">
            {{ item.message }}
          </div>
          <div class="left" v-show="item.userId">
            <a-avatar style="background-color: #f56a00">{{ item.userId || '匿名' }}</a-avatar>
          </div>
          <a-divider v-show="!item.userId">{{ item.message }}</a-divider>
        </div>
        <div class="choose_box" v-else :class="item.userId == userInfo.userId ? 'my_chat' : ''">
          <div class="left" v-show="item.userId">
            <a-avatar style="background-color: #f56a00">{{ item.userId || '匿名' }}</a-avatar>
          </div>
          <div class="right" v-show="item.userId">
            {{ item.message }}
          </div>
          <a-divider v-show="!item.userId">{{ item.message }}</a-divider>
        </div>
      </div>
    </div>
  </Transition>
</template>
 
<style lang='scss' scoped>
.chat_content {
  width: 100%;
  height: 600px;
  background: #F5F5F5;
  padding: 20px;
  overflow-y: auto;

  .chat_item {

    .choose_box {
      display: flex;
      align-items: center;

      &:nth-child(n+1) {
        margin-top: 20px;
      }
    }
  }

  .left {
    margin-right: 10px;
  }

  .right {
    max-width: 50%;
    padding: 5px 10px;
    background: #ccc;
    border-radius: 5px;
    transition: all .5s;

    &:hover {
      background: #bcbcbc;
    }
  }
}

.my_chat {
  justify-content: flex-end;

  .right {
    max-width: 50%;
    background: #9aed86;
  }

  .left {
    margin-right: 0;
    margin-left: 10px;
  }

}

.fade-enter-active {
  animation: fadeAnimation .4s ease-in forwards;
}

.fade-leave-active {
  animation: fadeAnimation .4s ease-in forwards reverse;
}

@keyframes fadeAnimation {
  0% {
    opacity: 0;
    transform: translateY(-10px);
  }

  100% {
    opacity: 1;
    transform: translateY(0px);
  }
}
</style>