<script setup lang='ts'>
import { PlusOutlined, TeamOutlined, UploadOutlined, ReconciliationOutlined, MenuOutlined } from '@ant-design/icons-vue';
import { FormInstance, Rule } from 'ant-design-vue/lib/form';
import { RouteLocationNormalizedLoaded } from 'vue-router';
import { Router } from 'vue-router';
import emitter from '~/utils/emitter'
import BScroll from '@better-scroll/core'
import MouseWheel from '@better-scroll/mouse-wheel'
BScroll.use(MouseWheel)

const defaultSelected = ref(1)
const router: Router = useRouter()
const route: RouteLocationNormalizedLoaded = useRoute()
defaultSelected.value = parseInt(route.params.groupId as string)
const visible = ref(false)
const userInfo: any = useUser()
//群组
const groupList = ref<any>([])
let scroll: any = null
const leftEle = ref<HTMLElement>()

const formRef = ref<FormInstance>();
const formState = reactive<any>({
  groupName: '',
  groupDesc: '',
  pic: [],
});

const getGroupAll = async () => {
  groupList.value = []
  const { data }: { data: any } = await useFetch('/api/group/all', {
    params: {
      consumerId: userInfo.value.userId
    }
  })
  groupList.value = data.value.data
  if (data.value.data.length != 0) {
    toggleChat(groupList.value.find((v: any) => v.groupId == route.params.groupId))
  }
  nextTick(() => {
    initScroll()
  })
}

const initScroll = () => {
  scroll = new BScroll(leftEle.value as HTMLElement, {
    pullUpLoad: true,
    scrollbar: true,
    pullDownRefresh: true,
    mouseWheel: {
      speed: 40,
      invert: false,
      easeTime: 300
    }
  })
}

getGroupAll()

emitter.on('refreshGroup', () => {
  getGroupAll()
})

let groupNameValidatePass = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('Please input the content');
  } else {
    return Promise.resolve();
  }
};

const rules: Record<string, Rule[]> = {
  groupName: [{ required: true, validator: groupNameValidatePass, trigger: 'change' }],
  groupDesc: [{ validator: groupNameValidatePass, trigger: 'change' }],
};

const handleOk = async () => {
  try {
    const values: any = await formRef.value?.validateFields();
    const { data }: { data: any } = await useFetch('/api/chat/get_group_id', {
      method: 'POST',
      body: {
        ...values,
        pic: values.pic[0].response.data.url,
      }
    })
    await useFetch('/api/chat/init_group', {
      method: 'POST',
      body: {
        userId: userInfo.value.userId,
        conveyGroupId: data.value.groupId
      }
    })
    getGroupAll()
    emitter.emit('refreshGroupList')
    visible.value = false
    formRef.value?.resetFields()
  } catch (errorInfo) {
    console.log('Failed:', errorInfo);
  }
}

const toggleChat = (item: { [key: string]: any }) => {
  if (item == null || item == undefined) return
  defaultSelected.value = item.groupId
  router.push('/' + item.groupId)
  userInfo.value.curGroup = item
  emitter.emit('refreshGroupNumber')
}
</script>
 
<template>
  <div ref="leftEle" style="height: 100%;">
    <div>
      <a-list class="group_list" :loading="false" item-layout="horizontal" :data-source="groupList">
        <template #renderItem="{ item }">
          <Transition name="fade" appear>
            <a-list-item class="group_item" :class="item.groupId == defaultSelected ? 'group_item_active' : ''"
              @click="toggleChat(item)">
              <a-list-item-meta :description="item.groupDesc">
                <template #title>
                  <a href="#">{{ item.groupName }}</a>
                </template>
                <template #avatar>
                  <a-avatar :src="item.pic" />
                </template>
              </a-list-item-meta>
            </a-list-item>
          </Transition>
        </template>
      </a-list>
      <div class="add_btn">
        <a-button type="link" @click="visible = true">
          <template #icon>
            <plus-outlined />
          </template>
        </a-button>
        <a-button type="link" @click="router.push('/group_details')">
          <template #icon>
            <menu-outlined />
          </template>
        </a-button>
      </div>
      <a-modal v-model:visible="visible" title="新建群聊" @ok="handleOk" cancelText="取消" okText="新建">
        <a-form :model="formState" name="normal_login" class="login-form" ref="formRef" :rules="rules">
          <a-form-item label="群聊名称" name="groupName" :rules="[{ required: true, message: '键入新群聊名称~' }]">
            <a-input v-model:value="formState.groupName">
              <template #prefix>
                <TeamOutlined class="site-form-item-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item label="群聊描述" name="groupDesc" :rules="[{ required: true, message: '键入描述内容~' }]">
            <a-input v-model:value="formState.groupDesc">
              <template #prefix>
                <ReconciliationOutlined class="site-form-item-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item name="pic" label="头像">
            <a-upload :multiple="false" :max-count="1" v-model:fileList="formState.pic" name="file"
              action="/image/oss/upload" list-type="picture">
              <a-button>
                <template #icon>
                  <UploadOutlined />
                </template>
                Click to upload
              </a-button>
            </a-upload>
          </a-form-item>
        </a-form>
      </a-modal>
    </div>
  </div>
</template>
 
<style lang='scss' scoped>
.group_list {
  .group_item {
    cursor: pointer;
    padding: 10px;
  }

  .group_item_active {
    background: #C4C4C4;
  }
}

.add_btn {
  text-align: center;
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
    transform: translateX(-10px);
  }

  100% {
    opacity: 1;
    transform: translateX(0px);
  }
}
</style>