import { defineStore } from 'pinia'

const token = defineStore('TOKEN', () => {
  const access_token = ref('')
  return {
    access_token
  }
})

export default token
