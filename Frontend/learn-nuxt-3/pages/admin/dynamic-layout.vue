<template>
  <div>
    <div class="q-pa-md">
      <div class="q-my-xl text-center q-gutter-md">
        <q-btn
          label="default layout"
          color="dark"
          @click="changePageLayout('default')"
        />
        <q-btn
          label="admin layout"
          color="dark"
          @click="changePageLayout('admin')"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 동적으로 레이아웃 변경
import type { LayoutKey } from '#build/types/layouts';

const changePageLayout = (layout: LayoutKey) => {
  setPageLayout(layout);
};
</script>

<style scoped></style>
