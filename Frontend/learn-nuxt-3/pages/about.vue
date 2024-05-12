<template>
  <q-page padding>
    <div class="q-my-xl text-center">
      <div class="text-h4">About</div>
      <p class="q-mt-sm text-grey-8">
        Vue & Nuxt Mastery Class 웹은 Nuxt3로 만들어졌습니다.
      </p>
      <div class="column">
        <p>RouterLink</p>
        <router-link to="/">RouterLink Home</router-link>
        <router-link to="https://youtube.com/">RouterLink Youtube</router-link>
        <p>NuxtLink</p>
        <nuxt-link to="/">RouterLink Home</nuxt-link>
        <nuxt-link to="https://youtube.com/">RouterLink Youtube</nuxt-link>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts"></script>
