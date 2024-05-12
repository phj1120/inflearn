<template>
  <q-page padding>
    <div class="q-my-xl tex-center">
      <div class="text-h4">Courses</div>
      <p class="q-mt-sm text-grey-8">학습 해보세요!</p>
    </div>
    <div class="row q-col-gutter-lg">
      <div
        v-for="{ courseSlug, title, subtitle, thumbnail, path } in courses"
        :key="courseSlug"
        class="col-12 col-md-4 col-sm-6"
      >
        <NuxtLink v-slot="{ navigate }" custom :to="path">
          <CourseCard
            :title="title"
            :subtitle="subtitle"
            :thumbnail="thumbnail"
            @click="navigate"
          />
        </NuxtLink>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
const { courses } = useCourses();
</script>

<style scoped></style>
