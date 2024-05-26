export default defineNuxtPlugin(()=>{
  return {
    provide: {
      hello2: (msg: string) => `Hello2 ${msg}`
    }
  }
})