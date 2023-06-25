/* eslint-disable @typescript-eslint/no-explicit-any */
import { defineStore } from 'pinia'
const playlistinfo = defineStore('PLAYLISTINFO', () => {
  const infoData = ref<any>('')
  const key = ref('')
  const pn = ref(1)
  const rn = ref(50)
  const total = ref(0)
  const playpn = ref(1)
  return {
    infoData,
    playpn,
    total,
    key,
    pn,
    rn
  }
})
export default playlistinfo
