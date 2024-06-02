export const useDefaultLocale = (fallback = 'en')=>{
  const locale = ref(fallback)
  if(process.server){
    // server 에서 request 에 접근할 수 있게 해주는 API
    const requestLocale = useRequestHeaders()['accept-language'].split(';')[0].split(',')[1]
    if(requestLocale){
      locale.value = requestLocale
    }
  } else if(process.client){
    // navigator 는 web spec 이라, 서버에서 사용 불가능.
    locale.value = navigator.language.split('-')[0]
  }

  return locale
}