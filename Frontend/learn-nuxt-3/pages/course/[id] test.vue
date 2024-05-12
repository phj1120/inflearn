<template>
  <q-page padding>
    <h2>Course</h2>
    <AppCard>
      <!-- <template v-slot:header> -->
      <template #header>
        <div>header</div>
      </template>
      <template #footer>
        <div>footer</div>
      </template>
      <div>
        <div>body</div>
        <div class="text-sub">params: {{ $route.params }}</div>
      </div>
    </AppCard>
  </q-page>
</template>

<script setup lang="ts">
const route = useRoute();
console.log(route.params.id);
</script>

<style scoped></style>
