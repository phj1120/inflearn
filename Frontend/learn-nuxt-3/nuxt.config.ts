// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },

  typescript: {
    shim: false,
    typeCheck: true,
  },
  modules: ['nuxt-quasar-ui', '@pinia/nuxt'],
  quasar: {
    /* */
  },
  imports:{
    presets:[
      {
        from: 'vue-i18n',
        imports: ['useI18n']
      }
    ]
  },
  components: {
    global: true,
    dirs: ['~/components'],
  },
  ssr: true
});
