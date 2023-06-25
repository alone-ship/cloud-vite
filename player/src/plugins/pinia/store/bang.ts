/* eslint-disable @typescript-eslint/no-explicit-any */
import { defineStore } from 'pinia'

const bang = defineStore('BANG', () => {
  const nowshowbang = ref<string | number>(16)
  const bangname = ref('')
  const menu = ref<any>('')
  const info = ref<any>([])
  const pn = ref<any>(1)
  const rn = ref<any>(30)
  const total = ref<number>(0)
  return {
    nowshowbang,
    bangname,
    menu,
    info,
    pn,
    rn,
    total
  }
})

export default bang
