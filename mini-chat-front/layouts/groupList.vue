<script setup lang='ts'>

</script>
 
<template>
  <div class="group_list">
    <ClientOnly>
      <div class="left">
        <ChatLeft />
      </div>
      <div class="right">
        <NuxtPage />
      </div>
    </ClientOnly>
  </div>
</template>
 
<style lang='scss' scoped>
.group_list {
  width: 70%;
  margin: 3rem auto 0;
  display: flex;
  border-radius: 20px;
  overflow: hidden;
  background: #BAFB90;

  .left {
    width: 150px;
    height: 800px;
    background: #E9E7E6;
    padding-bottom: 100px;
  }

  .right {
    width: calc(100% - 150px);
    height: 100%;
  }
}
</style>