<script setup lang='ts'>
import { PlusOutlined, CheckOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import emitter from '~/utils/emitter'

definePageMeta({
  layout: 'group-list'
})
const userInfo = useUser()
const groupList = ref<any>([])
const getGroupList = async () => {
  const { data }: { data: any } = await useFetch('/api/group/list', {
    params: {
      consumerId: userInfo.value.userId
    }
  })
  groupList.value = data.value || []
}
getGroupList()

const enterGroup = async (item: any) => {
  const { data }: { data: any } = await useFetch('/api/chat/init_group', {
    method: 'POST',
    body: {
      userId: userInfo.value.userId,
      conveyGroupId: item.groupId
    }
  })
  if (data.value) {
    message.success('加入成功')
    getGroupList()
    emitter.emit('refreshGroup')

  } else {
    message.error('加入失败,请稍后再试')
  }
}

emitter.on('refreshGroupList',() => {
  getGroupList()
})
</script>
 
<template>
  <div class="box">
    <p class="title">所有群聊：</p>
    <div class="group_details_wrap">
      <a-row type="flex" :gutter="[12, 12]">
        <a-col :sm="24" :md="12" :lg="8" :xl="6" :xxl="4" v-for="item in groupList">
          <a-card hoverable>
            <template #cover>
              <img style="width:100%;object-fit: cover;height: 250px;" alt="example" :src="item.pic" />
            </template>
            <template #actions>
              <PlusOutlined key="setting" v-if="!item.isJoined" @click="enterGroup(item)" />
              <div style="text-align: center;" v-if="item.isJoined">
                <span>已加入&nbsp;</span>
                <CheckOutlined />
              </div>
            </template>
            <a-card-meta :title="item.groupName" :description="item.groupDesc">
              <template #avatar>
                <a-avatar :src="item.pic" />
              </template>
            </a-card-meta>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
 
<style lang='scss' scoped>
.box {
  border-left: solid 1px #F0F0F0;
  width: 100%;
  height: 800px;

  .title {
    font-size: 20px;
    background: #F0F0F066;
    height: 50px;
    line-height: 50px;
    padding-left: 20px;
    user-select: none;
    margin: 0;
  }

  .group_details_wrap {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    overflow-y: auto;
    overflow-x: hidden;
    height: 750px;
    padding-bottom: 40px;

    .group_item {
      width: calc((100% - 40px) / 3);
      max-width: 250px;
    }
  }
}
</style>