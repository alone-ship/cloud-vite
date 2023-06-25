<template>
  <div class="lrc_container">
    <audio
      id="Audio"
      :src="play.playurl"
      hidden
      @canplaythrough="doFronting"
      @timeupdate="HandleDuration"
      @ended="forward"
    ></audio>
    <div class="mode_controller">
      <el-button-group>
        <el-button @click="albumShow = true">专辑</el-button>
        <el-button @click="albumShow = false">歌词</el-button>
      </el-button-group>
    </div>
    <div class="albumAndlrc">
      <div v-show="albumShow" class="noOnlyAlbum">
        <div class="Album">
          <img :src="play.nowplay?.pic" alt="album" />
        </div>
        <div class="lrc">
          <p class="lrc_content" :style="`color:${lrcColor == 1 ? 'skyblue' : 'white'}`">
            {{ play.lrclist == '' ? '' : play.lrclist[play.lrcindex]?.lineLyric }}
          </p>
          <p class="lrc_content" :style="`color:${lrcColor == 2 ? 'skyblue' : 'white'}`">
            {{ play.lrclist == '' ? '' : play.lrclist[play.lrcindex + 1]?.lineLyric }}
          </p>
        </div>
      </div>
      <el-scrollbar v-show="!albumShow" ref="scrollbarRef" height="474px">
        <div v-show="!albumShow" class="onlyLrc">
          <div class="lrc">
            <div v-for="(item, index) in play.lrclist" :key="index" class="lrcBox">
              <p class="content" :style="`color:${play.lrcindex == index ? 'skyblue' : ''}`">
                {{ item.lineLyric }}
              </p>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <div class="play_controller">
      <div class="slide">
        <Slide ref="slider" :wd="409" :pct="PCT" @uppct="uppct" />
      </div>
      <div class="controller">
        <i class="iconfont icon-shangyiji" @click="backward"></i>
        <span class="playAndpause" @click="playORstop">
          <i v-show="play.playState" class="iconfont icon-bofang1"></i>
          <i v-show="!play.playState" class="iconfont icon-zanting"></i>
        </span>
        <i class="iconfont icon-xiayiji" @click="forward"></i>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import pinia from '@renderer/plugins/pinia'
import { ElScrollbar } from 'element-plus'

//参数区
const albumShow = ref(true)
const play = pinia.play()
const token = pinia.token()
const slider = ref()
const current = getCurrentInstance()
const router = current?.appContext.config.globalProperties.$router
const Audio = ref<HTMLAudioElement | null>(null)
const PCT = ref(0)
const lrcColor = ref(1)
const scrollbarRef = ref<InstanceType<typeof ElScrollbar>>()
//监听区
watch(
  () => play.playState,
  (val) => {
    if (Audio.value != null && play.playurl != '') {
      const audio = Audio.value
      if (val) {
        audio.play()
      } else {
        audio.pause()
      }
    }
  }
)
watch(
  () => play.lrcindex,
  (newval) => {
    if (!albumShow.value) {
      if (newval > 9) {
        scrollbarRef.value?.setScrollTop((newval - 10) * 28)
      } else {
        scrollbarRef.value?.setScrollTop(0)
      }
    }
  }
)
//函数区
/**
 * 播放前
 */
const doFronting = (): void => {
  const audio = Audio.value
  if (audio != null && play.playState) {
    audio.play()
  }
}
/**
 * 播放时间更新
 */
const HandleDuration = (): void => {
  if (play.lrcindex < play.lrclist.length) {
    uplrc(Number(Audio.value?.currentTime.toFixed(2)))
  }
}
/**
 * 更新歌词
 */
const uplrc = (pt: number): void => {
  PCT.value = Number((pt / play.playduration).toFixed(2))
  const long = Math.ceil(pt * 100)
  for (let i = 0; i < play.lrclist.length; i++) {
    const item = play.lrclist[i]
    const lrcTime = Math.ceil(Number(item.time) * 100)
    if (albumShow.value) {
      if (long > lrcTime - 30) {
        if (i - play.lrcindex == 2) {
          play.lrcindex = i - 1 < 0 ? 0 : i
          break
        }
        if (i - play.lrcindex >= 1) {
          lrcColor.value = 2
        } else {
          lrcColor.value = 1
        }
      }
    } else {
      if (long < lrcTime - 30) {
        play.lrcindex = i - 1 < 0 ? 0 : i - 1
        break
      }
    }
  }
}
/**
 * 更新播放进度
 * @param val 进度
 */
const uppct = (pct): void => {
  if (Audio.value) {
    const progress = Number((play.playduration * pct).toFixed(2))
    Audio.value.currentTime = progress
  }
  slider.value.startWatch()
}
/**
 * 播放和停止
 */
const playORstop = (): void => {
  play.playState = !play.playState
}
/**
 * 上一首
 */
const backward = (): void => {
  if (play.pn == 1 && play.index == 0) {
    ElMessage.success('已经是第一首了!')
  } else {
    if (play.index - 1 < 0) {
      play.index = play.rn - 1
      play.pn -= 1
      play.getNewlist = 'backward'
    } else {
      play.index -= 1
      play.nowplay = play.list[play.index]
      play.getMusicInfo()
    }
  }
}
/**
 * 下一首
 */
const forward = (): void => {
  if (play.index == play.rn - 1 && play.pn == play.total) {
    ElMessage.success('已经是最后一首了!')
  } else {
    if (play.index + 1 == play.rn) {
      play.index = 0
      play.pn += 1
      play.getNewlist = 'forward'
    } else {
      play.index += 1
      play.nowplay = play.list[play.index]
      play.getMusicInfo()
    }
  }
}
//初始化区
onMounted(() => {
  if (token.access_token == '') router?.replace('/')
  else {
    Audio.value = document.getElementById('Audio') as HTMLAudioElement | null
  }
})
</script>
<style scoped lang="less">
.lrc_container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  * {
    max-width: 100%;
  }
  .mode_controller {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
  }
  .albumAndlrc {
    height: 494px;
    .noOnlyAlbum {
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      .Album {
        width: 350px;
        margin-bottom: 20px;
        .img {
          width: 100%;
          object-fit: none;
        }
      }
      .lrc {
        width: 100%;
        height: 60px;
        display: flex;
        flex-direction: column;
        margin: 20px 0;
        .lrc_content {
          flex: 1;
          max-width: 300px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          font-family: youyuan;
          font-weight: bold;
        }
        .lrc_content:nth-child(1) {
          margin-right: auto;
        }
        .lrc_content:nth-child(2) {
          margin-left: auto;
        }
      }
    }
    .onlyLrc .lrc .lrcBox {
      .content {
        font-family: youyuan;
        padding: 5px;
        text-align: center;
      }
    }
  }
  .play_controller {
    .slide {
      height: 30px;
      widows: 460px;
      margin-bottom: 20px;
    }
    .controller {
      height: 100px;
      display: flex;
      justify-content: center;
      align-items: center;
      .playAndpause {
        margin: 10px;
        i {
          font-size: 70px;
        }
      }
      i {
        font-size: 60px;
        margin: 10px;
        cursor: pointer;
        color: #b1aeae;
      }
      i:hover {
        color: #565656;
      }
    }
  }
}
</style>
