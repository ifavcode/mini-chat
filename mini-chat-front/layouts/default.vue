<script setup lang='ts'>


</script>
 
<template>
  <div class="total">
    <ClientOnly>
      <div class="left">
        <ChatLeft />
      </div>
      <div class="right">
        <ChatHeader />
        <slot />
        <ChatFooter />
      </div>
    </ClientOnly>
  </div>
</template>
 
<style lang='scss' scoped>
.total {
  width: 70%;
  margin: 3rem auto 0;
  display: flex;
  border-radius: 20px;
  overflow: hidden;

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