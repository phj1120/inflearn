<template>
  <q-page padding>
    <div class="text-center q-py-xl">
      <div class="text-h4">[[slug]]/index.vue</div>
      <p class="text-subtitle1 q-mt-md">
        파라미터를 필수로 받고 싶을 때: [] / {{ $route.params.requiredSlug }}
        <q-btn stretch flat label="예시" to="/slug/requiredSlug" />
      </p>
      <p class="text-subtitle1 q-mt-md">
        파라미터를 선택적으로 받고 싶을 때: [[]] /
        {{ $route.params.optionalSlug }}
        <q-btn stretch flat label="예시" to="/slug/optionalSlug/requiredSlug" />
      </p>
    </div>
  </q-page>
</template>
