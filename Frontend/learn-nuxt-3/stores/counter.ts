export const useCounterStore = defineStore('counter', () =>{
  const count = ref(0)
  const name = ref('HJ')
  const doubleCount = computed(()=>count.value * 2)
  function increment(){
    count.value ++
  }

  return {
    count,
    name,
    doubleCount,
    increment
  }
})