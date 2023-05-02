// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  app: {
    layoutTransition: { name: 'layout', mode: 'out-in' }
  },
  nitro: {
    devProxy: {
      '/api': {
        target: 'http://192.168.0.103:8080',
        changeOrigin: true,
        prependPath: true,
      },
      '/image':{
        target:'http://192.168.0.103:8086',
        changeOrigin: true,
        prependPath: true,
      }
    }
  }
})
