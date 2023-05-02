export default defineNuxtRouteMiddleware((to, from) => {
  if (from.fullPath == '/') {
    return navigateTo('/1')
  }
})
