<template>
  <q-page padding>
    <div class="text-center q-py-xl">
      <div class="text-h4">[[slug]]/index.vue</div>
      <p class="text-subtitle1 q-mt-md">slug: {{ $route.params.slug }}</p>
      <p class="text-subtitle1 q-mt-md">파라미터를 필수로 받고 싶을 때: []</p>
      <p class="text-subtitle1 q-mt-md">
        파라미터를 선택적으로 받고 싶을 때: [[]]
      </p>
    </div>
  </q-page>
</template>
