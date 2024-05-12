<template>
  <div>
    <p>TEST PAGE</p>
    <button @click="showModal = true">Show Modal</button>
    <my-modal v-if="showModal" @close="showModal = false">
      <template #header>
        <h3>custom header</h3>
      </template>
    </my-modal>
    <button @click="showComponet = !showComponet">Show Component</button>
    <my-component v-if="showComponet" />
    <div>
      <h5>selectedComponet</h5>
      <div>selected: {{ selected }}</div>
      <input v-model="selected" type="radio" value="Component1" />
      <label for="Component1">DynamicComponent1</label>
      <input v-model="selected" type="radio" value="Component2" />
      <label for="Component2">DynamicComponent2</label>
    </div>
    <div>
      <h5>is: {{ `dynamic-${selected}` }}</h5>
      <component :is="`dynamic-${selected}`" v-if="selected" />
      <h5>is: {{ `dynamic${selected}` }}</h5>
      <component :is="`dynamic${selected}`" v-if="selected" />
      <h5>is: {{ selectedComponentName }}</h5>
      <component :is="selectedComponentName" v-if="selected" />
      <h5>is: {{ selected }}</h5>
      <component :is="selected" v-if="selected" />
    </div>
  </div>
</template>

<script setup lang="ts">
const showModal = ref(false);
const showComponet = ref(false);
const selected = ref('');
const selectedComponentName = computed(() => {
  return `dynamic-${selected.value}`.toLocaleLowerCase();
});
</script>

<style scoped>
body {
  margin: 0;
}
div {
  box-sizing: border-box;
}
.black-bg {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}
.white-bg {
  width: 100%;
  background: white;
  border-radius: 8px;
  padding: 20px;
}
</style>
