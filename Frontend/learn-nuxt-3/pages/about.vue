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

        <div class="q-gutter-y-sm q-mt-md">
          <div class="text-subtitle1 text-weight-bold">useState('counter')</div>
          <div>
            counter: {{ counter }}
            <q-btn
              label="counter"
              color="primary"
              dense
              no-caps
              @click="counter++"
            />
          </div>
          <div>
            sameCounter: {{ sameCounter }}
            <q-btn
              label="sameCounter"
              color="primary"
              dense
              no-caps
              @click="sameCounter++"
            />
          </div>
          <div>
            <q-btn
              label="clear"
              color="primary"
              dense
              no-caps
              @click="clearCounter()"
            />
          </div>
        </div>

        <div class="q-gutter-y-sm q-mt-md">
          <div class="text-subtitle1 text-weight-bold">useCounterStore()</div>
          <div>
            <div>
              count: {{ count }}
            </div>
            <div>
              doubleCount: {{ doubleCount }}
            </div>
            <div>
              <q-btn
              label="increment"
              color="primary"
              dense
              no-caps
              @click="counterStore.increment"
            />
            </div>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
const counter = useState<number>('counter', () => 0)
const sameCounter = useState<number>('counter')
const clearCounter = ()=>{
  clearNuxtState('counter')
  useState<number>('counter', () => 0)
}

const counterStore = useCounterStore()
const { count, doubleCount,  } = storeToRefs(counterStore)

</script>
