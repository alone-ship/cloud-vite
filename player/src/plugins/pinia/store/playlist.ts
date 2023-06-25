/* eslint-disable @typescript-eslint/no-explicit-any */
import { defineStore } from 'pinia'

const playlist = defineStore('PLAYLIST', () => {
  const tags = ref<any>('')
  const nowShowType = ref<string | number>('new')
  const pn = ref(1)
  const rn = ref(40)
  const total = ref(0)
  const playlists = ref<any>('')
  return {
    tags,
    nowShowType,
    pn,
    playlists,
    rn,
    total
  }
})

export default playlist
