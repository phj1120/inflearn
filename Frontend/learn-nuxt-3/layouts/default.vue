<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated class="bg-dark text-white">
      <q-toolbar>
        <q-toolbar-title> Learn Nuxt3 </q-toolbar-title>
        <nuxt-link v-slot="{ navigate }" custom to="/">
          <q-btn stretch flat :label="$t('home')" no-caps @click="navigate" />
        </nuxt-link>
        <q-separator dark vertical />
        <nuxt-link v-slot="{ navigate }" custom to="/about">
          <q-btn stretch flat :label="$t('about')" no-caps @click="navigate" />
        </nuxt-link>
        <q-separator dark vertical />
        <q-btn stretch flat :label="$t('youtube')" no-caps @click="moveYouTube()" />
        <q-separator dark vertical />
        <NuxtLink v-slot="{ navigate }" custom to="/admin">
          <q-btn stretch flat :label="$t('admin')" no-caps @click="navigate()" />
        </NuxtLink>
        <q-separator dark vertical />
        <q-btn-dropdown stretch flat no-caps :label="selectedLanguageName">
          <q-list padding dense>
            <q-item
              v-for="{ code, name } in languages"
              :key="code"
              v-close-popup
              clickable
              :active="code === $i18n.locale"
              @click="$i18n.locale = code"
            >
              <q-item-section>
                <q-item-label>{{ name }}</q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </q-btn-dropdown>
      </q-toolbar>
    </q-header>
    <q-page-container :style="pageContainerStyle">
      <slot></slot>
    </q-page-container>
  </q-layout>
</template>
<script setup lang="ts">
import i18n from "~/plugins/i18n";

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

interface Language{
  name: string,
  code: 'ko' | 'en'
}

const languages = ref<Language[]>([
  {name: 'Englesh', code:'en'},
  {name: '한국어', code:'ko'},
])

const {locale} = useI18n();
const selectedLanguageName = computed(()=>
 languages.value.find((lang)=> lang.code === locale.value)?.name 
)
</script>
