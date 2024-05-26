declare module '#app' {
  interface NuxtApp {
    $hello1 (msg: string): string
  }
}

declare module 'vue' {
  interface ComponentCustomProperties {
    $hello1 (msg: string): string
  }
}

export {}