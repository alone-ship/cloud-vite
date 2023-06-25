/* eslint-disable @typescript-eslint/no-explicit-any */
import { defineStore } from 'pinia'

const search = defineStore('SEARCH', () => {
  const keyword = ref('')
  const list = ref<any>([])
  const total = ref(0)
  const pn = ref(1)
  const rn = ref(50)
  return {
    keyword,
    total,
    list,
    pn,
    rn
  }
})

export default search
