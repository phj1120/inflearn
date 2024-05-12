<template>
  <q-page padding>
    <div class="text-center q-py-xl">
      <div class="text-h4">
        {{ $route.path }}
      </div>
    </div>
  </q-page>
</template>

<script lang="ts">
console.log('prefetching-1');
</script>

<script setup lang="ts">
// 컴파일 시 vue component 의 setup hook 안에 들어감
console.log('prefetching-1 setup');
</script>

<style scoped></style>
