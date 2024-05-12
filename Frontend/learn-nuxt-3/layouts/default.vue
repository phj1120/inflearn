<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated class="bg-dark text-white">
      <q-toolbar>
        <q-toolbar-title> Learn Nuxt3 </q-toolbar-title>
        <nuxt-link v-slot="{ navigate }" custom to="/">
          <q-btn stretch flat label="Home" no-caps @click="navigate" />
        </nuxt-link>
        <q-separator dark vertical />
        <nuxt-link v-slot="{ navigate }" custom to="/about">
          <q-btn stretch flat label="About" no-caps @click="navigate" />
        </nuxt-link>
        <q-separator dark vertical />
        <q-btn stretch flat label="YouTube" no-caps @click="moveYouTube()" />
        <q-separator dark vertical />
        <NuxtLink v-slot="{ navigate }" custom to="/admin">
          <q-btn stretch flat label="Admin" no-caps @click="navigate()" />
        </NuxtLink>
      </q-toolbar>
    </q-header>
    <q-page-container :style="pageContainerStyle">
      <slot></slot>
    </q-page-container>
  </q-layout>
</template>
<script setup lang="ts">
const pageContainerStyle = computed(() => ({
  maxWidth: '1080px',
  margin: '0 auto',
}));

const moveYouTube = async () => {
  await navigateTo('https://youtube.com/', {
    external: true,
    open: {
      target: '_blank',
      windowFeatures: {
        popup: true,
      },
    },
  });
};
</script>
