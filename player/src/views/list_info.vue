<template>
  <div class="info_container">
    <div class="detail">
      <img class="list_header" :src="listinfo.infoData.img" alt="list_header" />
      <div class="list_name">{{ listinfo.infoData.name }}</div>
      <div class="play_controller">
        <el-button-group>
          <el-button><b>All Play</b></el-button>
          <el-button @click="GoBack"><b>Go Back</b></el-button>
        </el-button-group>
      </div>
      <div class="Description">
        <p class="title"><b>Description</b></p>
        <el-scrollbar max-height="429px" style="width: 100%">
          <p class="content">{{ listinfo.infoData?.info }}</p>
        </el-scrollbar>
      </div>
    </div>
    <div class="my_table">
      <el-table :data="filterTableData" height="840">
        <el-table-column label="Name" prop="name" />
        <el-table-column label="Artist" prop="artist" />
        <el-table-column label="Time" prop="songTimeMinutes" />
        <el-table-column align="center">
          <template #header>
            <el-input v-model="search" placeholder="Name to search on table" />
          </template>
          <template #default="scope">
            <el-button size="small">
              <i class="iconfont icon-download"></i>
            </el-button>
            <el-button size="small" @click="playMusic(scope.$index)">
              <i class="iconfont icon-bofang1"></i>
            </el-button>
            <el-button size="small">
              <i class="iconfont icon-chuangjiantianjiapiliangtianjia"></i>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page_controller">
        <el-pagination
          background
          layout="prev, pager, next"
          :page-size="listinfo.rn"
          :current-page="listinfo.pn"
          :total="listinfo.total"
          @current-change="Do"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import pinia from '@renderer/plugins/pinia'
import { authorize } from '@renderer/plugins/axios'
//参数区
const current = getCurrentInstance()
const router = current?.appContext.config.globalProperties.$router
const playlistinfo = pinia.playlistinfo()
const play = pinia.play()
const listinfo = pinia.playlistinfo()
const tk = pinia.token()
const search = ref('')
let loading
//监听区
watch(
  () => play.getNewlist,
  (newval) => {
    if (newval == 'backward') {
      getlistinfo(play.pn, play.reqRn, (list) => {
        play.list = list
        play.nowplay = play.list[play.index]
        play.getMusicInfo()
      })
    }
    if (newval == 'forward') {
      getlistinfo(play.pn, play.reqRn, (list) => {
        play.list = list
        play.nowplay = play.list[play.index]
        play.getMusicInfo()
      })
    }
    play.getNewlist = ''
  }
)
//函数区
/**
 * 表格数据过滤
 */
const filterTableData = computed(() =>
  listinfo.infoData.musicList?.filter(
    (data: { name: string }) =>
      !search.value || data.name.toLowerCase().includes(search.value.toLowerCase())
  )
)
/**
 * 播放音乐
 * @param index
 * @param playState
 */
const playMusic = (index: number): void => {
  play.list = playlistinfo.infoData.musicList
  play.index = index
  play.total = Math.ceil(playlistinfo.total / playlistinfo.rn)
  play.pn = 1
  play.playState = true
  play.rn = playlistinfo.infoData.musicList.length
  play.reqRn = playlistinfo.rn
  play.getMusicInfo()
}
/**
 * 返回
 */
const GoBack = (): void => {
  playlistinfo.infoData = ''
  router?.back()
}
/**
 * 页数变化,尺寸变化
 */
const Do = (val: number): void => {
  listinfo.pn = val
  getlistinfo()
}
/**
 * 获取歌单详情
 */
const getlistinfo = (pn?: number, rn?: number, callback?: FunctionStringCallback): void => {
  loadingService()
  const params = new URLSearchParams()
  params.append('pid', playlistinfo.key)
  params.append('pn', pn ? pn.toString() : playlistinfo.pn.toString())
  params.append('rn', rn ? rn.toString() : playlistinfo.rn.toString())
  authorize.post('/ListInfo', params).then((res) => {
    const reStart = (): void => {
      getlistinfo(pn, rn, reStart)
    }
    if (res.status == 200) {
      const result = res.data
      if (result.status == 200) {
        const data = JSON.parse(result.data)
        if (data.code == 200) {
          if (callback) {
            callback(data.data.musicList)
          } else {
            playlistinfo.infoData = data.data
            playlistinfo.total = Number(data.data.total)
          }
          loading.close()
        } else reStart()
      } else reStart()
    } else ElMessage.warning('lists requst not send')
  })
}
/**
 * 加载层
 */
const loadingService = (): void => {
  loading = ElLoading.service({
    lock: true,
    text: 'Loading',
    background: 'rgba(0, 0, 0, 0.7)'
  })
}
//初始化区
onMounted(() => {
  if (playlistinfo.key == '' || tk.access_token == '') GoBack()
  else {
    loading = ElLoading.service({
      lock: true,
      text: 'Loading',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    getlistinfo()
  }
})
</script>
<style scoped lang="less">
.info_container {
  height: 100%;
  display: flex;

  .detail {
    min-width: 300px;
    max-width: 300px;
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-right: 1px solid var(--el-border-color);
    background-color: #2c2c2c;
    font-family: youyuan;
    font-size: 15px;

    .list_header {
      width: 180px;
      margin-bottom: 20px;
    }

    .list_name {
      text-align: center;
      line-height: 23px;
      margin-bottom: 20px;
    }

    .play_controller {
      margin-bottom: 20px;
    }

    .Description {
      display: flex;
      flex-direction: column;
      flex: 1;

      .title {
        margin-bottom: 20px;
        text-align: center;
      }

      .content {
        flex: 1;
        line-height: 23px;
      }
    }
  }

  .my_table {
    flex: 1;

    .page_controller {
      padding-top: 15px;
      display: flex;
      justify-content: center;
    }
  }
}
</style>
