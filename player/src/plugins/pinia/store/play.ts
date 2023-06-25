/* eslint-disable @typescript-eslint/no-explicit-any */
import { defineStore } from 'pinia'
import { authorize } from '@renderer/plugins/axios'

const play = defineStore('PLAY', () => {
  const list = ref<any>('')
  const index = ref(0)
  const nowplay = ref<any>('')
  const lrclist = ref<any>('')
  const lrcindex = ref(0)
  const playurl = ref('')
  const playduration = ref(0)
  const Tip = ref('暂无播放')
  const pn = ref(0)
  const rn = ref(0)
  const reqRn = ref(0)
  const PCT = ref(0)
  const total = ref(0)
  const getNewlist = ref('')
  const playState = ref(true)
  const volume = ref(1)
  const getMusicInfo = (): void => {
    playurl.value = ''
    lrclist.value = ''
    lrcindex.value = 0
    Tip.value = '正在获取资源'
    nowplay.value = list.value[index.value]
    getSongInfo()
    getMusicUrl()
  }
  /**
   * 获取音乐播放详情
   */
  const getSongInfo = (): void => {
    const params = new URLSearchParams()
    params.append('musicId', nowplay.value.rid)
    authorize.post('/SongInfo', params).then((res: any) => {
      if (res.status == 200) {
        const result = res.data
        if (result.status == 200) {
          const info = JSON.parse(result.data)
          if (info.status == 200) {
            if (info.data.lrclist instanceof Array) {
              lrclist.value = info.data.lrclist
              Tip.value = ''
            } else {
              Tip.value = '资源获取失败'
            }
          } else {
            Tip.value = '资源获取失败'
            ElMessage.warning('music info requst faild')
          }
        } else {
          Tip.value = '资源获取失败'
          ElMessage.warning(result.message)
        }
      } else {
        Tip.value = '资源获取失败'
        ElMessage.warning('music info requst not send')
      }
    })
  }
  /**
   * 获取音乐播放路径
   */
  const getMusicUrl = (): void => {
    const params = new URLSearchParams()
    params.append('mid', nowplay.value.rid)
    params.append('type', 'flac')
    authorize.post('/MusicUrl', params).then((res: any) => {
      if (res.status == 200) {
        const result = res.data
        if (result.status == 200) {
          const url = JSON.parse(result.data)
          if (url.code == 200) {
            playurl.value = url.data.url
            playduration.value = nowplay.value.duration
          } else ElMessage.warning('music url requst faild')
        } else ElMessage.warning(result.message)
      } else ElMessage.warning('music url requst not send')
    })
  }
  return {
    rn,
    pn,
    total,
    Tip,
    list,
    reqRn,
    index,
    nowplay,
    getMusicInfo,
    lrclist,
    lrcindex,
    playurl,
    getNewlist,
    playduration,
    PCT,
    playState,
    volume
  }
})

export default play
