<script setup lang='ts'>
import emitter from '~/utils/emitter'
const userInfo: any = useUser()
const groupNumber = ref('...')
const comsumerIds = ref([])
const getGroupNumber = async () => {
  groupNumber.value = '...'

  const res: any = await useFetch('/api/group/consumers', {
    params: {
      groupId: userInfo.value.curGroup.groupId
    }
  })
  comsumerIds.value = res.data.value.data
  groupNumber.value = comsumerIds.value.length.toString()
}

const userIdChange = (e: any) => {
  emitter.emit('refreshGroup')
}

emitter.on('refreshGroupNumber', () => {
  getGroupNumber()
})
</script>
 
<template>
  <div class="chat_header">
    <div class="left">
      <a-popover title="群聊成员" placement="rightTop">
        <template #content>
          <p v-for="userId in comsumerIds">{{ userId }}</p>
        </template>
        <p>{{ userInfo.curGroup.groupName }}({{ groupNumber }})</p>
      </a-popover>
    </div>
    <div class="right">
      <a-input v-model:value="userInfo.userId" addon-before="用户ID" @change="userIdChange" />
    </div>
  </div>
</template>
 
<style lang='scss' scoped>
.chat_header {
  width: 100%;
  height: 100px;
  display: flex;
  align-items: center;
  background: #F5F5F5;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;

  .left {
    font-size: 20px;
    cursor: pointer;
  }

  .right {}
}
</style>