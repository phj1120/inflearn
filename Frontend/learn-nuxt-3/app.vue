<template>
  <div>
    <NuxtLayout>
      <!-- <RouterView /> -->
      <!-- <NuxtPage :page-key="$route.fullPath" /> -->
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>
<script setup lang="ts">
const nuxtApp = useNuxtApp()
console.log(nuxtApp);

// vue application instance
// nuxtApp.vueApp.component()
// nuxtApp.vueApp.use()

nuxtApp.provide('hello1', (msg: string) => `Hello1 ${msg}`)
</script>
