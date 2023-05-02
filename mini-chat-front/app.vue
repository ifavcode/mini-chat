<script setup lang='ts'>


</script>
 
<template>
  <div>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>
 
<style lang='scss'>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  overflow: hidden;
}

.layout-enter-active,
.layout-leave-active {
  transition: all 1s;
}

.layout-enter-from,
.layout-leave-to {
  opacity: 1
}
</style>