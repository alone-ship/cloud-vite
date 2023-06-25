<template>
  <div class="songlist_container">
    <div class="position_tags_box">
      <div class="collapse_box">
        <span class="tags_box_collapse"></span>
        <div ref="TagBoxInstance" class="tags_box">
          <el-scrollbar max-height="810px">
            <div class="tags_index_box">
              <div v-for="(item, index) in bang.menu" :key="index" class="tags_container">
                <div class="tag_index_title">{{ item.name }}</div>
                <div class="tags">
                  <span
                    v-for="(tag, i) in item.list"
                    :key="i"
                    class="tag"
                    @click="changeBang(tag.sourceid)"
                  >
                    {{ tag.name }}
                  </span>
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>
      </div>
    </div>
    <div class="lists">
      <el-table :data="filterTableData" height="840" style="width: 100%">
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
          :hide-on-single-page="true"
          background
          layout="prev, pager, next"
          :page-size="bang.rn"
          :current-page="bang.pn"
          :total="bang.total"
          @current-change="prevAnext"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import pinia from '@renderer/plugins/pinia'
import { authorize } from '@renderer/plugins/axios'

//参数区
const that = getCurrentInstance()
const router = that?.appContext.config.globalProperties.$router
const bang = pinia.bang()
const play = pinia.play()
const tk = pinia.token()
const search = ref('')
let loading
//监听区
watch(
  () => play.getNewlist,
  (newval) => {
    if (newval == 'backward') {
      BangInfo(play.pn, play.reqRn, (list) => {
        play.list = list
        play.nowplay = play.list[play.index]
        play.getMusicInfo()
      })
    }
    if (newval == 'forward') {
      BangInfo(play.pn, play.reqRn, (list) => {
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
  bang.info.filter(
    (data: { name: string }) =>
      !search.value || data.name.toLowerCase().includes(search.value.toLowerCase())
  )
)
/**
 * 播放音乐
 * @param index
 */
const playMusic = (index: number): void => {
  play.list = bang.info
  play.index = index
  play.total = Math.ceil(bang.total / bang.rn)
  play.pn = 1
  play.rn = bang.info.length
  play.reqRn = bang.rn
  play.getMusicInfo()
}
/**
 * 切换榜单
 */
const changeBang = (key): void => {
  if (bang.nowshowbang != key) {
    bang.nowshowbang = key
    bang.pn = 1
    BangInfo()
  }
}
/**
 * 当页数发生变化时
 */
const prevAnext = (val): void => {
  bang.pn = val
  BangInfo()
}
/**
 * 获取榜单详情
 */
const BangInfo = (pn?: number, rn?: number, callback?: FunctionStringCallback): void => {
  loadingService()
  const params = new URLSearchParams()
  params.append('bangId', bang.nowshowbang.toString())
  params.append('pn', (pn ? pn : bang.pn).toString())
  params.append('rn', (rn ? rn : bang.rn).toString())
  authorize.post('/BangInfo', params).then((res) => {
    const result = res.data
    if (result.status == 200) {
      const data = JSON.parse(result.data)
      if (data.code == 200) {
        if (callback) {
          callback(data.data.musicList)
        } else {
          bang.info = data.data.musicList
          bang.total = Number(data.data.num)
        }
        loading.close()
      } else BangInfo(pn, rn, callback)
    } else BangInfo(pn, rn, callback)
  })
}
/**
 * 获取榜单菜单
 */
const BangMenu = (): void => {
  authorize.get('/BangMenu').then((res) => {
    //console.log(`Menu${res}`)
    const result = res.data
    if (result.status == 200) {
      const data = JSON.parse(result.data)
      if (data.code == 200) {
        bang.menu = data.data
        BangInfo()
      } else BangMenu()
    } else BangMenu()
  })
}
/**
 * 第一次加载页面时
 */
const initPage = (): void => {
  if (bang.menu == '') BangMenu()
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
  if (tk.access_token != '') {
    initPage()
  } else {
    router?.replace('/')
  }
})
</script>
<style scoped lang="less">
.songlist_container {
  display: flex;
  height: 100%;
  position: relative;

  .position_tags_box {
    height: 100%;
    z-index: 1;
    background-color: #2c2c2c;
    transition: all 0.5s linear;
    width: 300px;

    .collapse_box {
      height: 100%;
      position: relative;

      .tags_box_collapse {
        position: absolute;
        right: 0px;
        top: 50%;
        transform: translate(100%, -50%);
        background-color: #363636;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        width: 20px;
        cursor: pointer;
      }

      .tags_box {
        height: 100%;
        display: flex;
        flex-direction: column;
        position: relative;
        overflow: hidden;

        .tags_index_box {
          overflow: hidden;
          padding: 10px;
        }

        .tags_container {
          font-size: 12px;
          padding: 5px 0;

          .tag_index_title {
            margin-bottom: 15px;
          }

          .tags {
            display: flex;
            flex-wrap: wrap;

            .tag {
              padding: 5px 10px;
              border: 1px solid var(--el-border-color);
              margin: 5px;
              margin-left: 0;
              border-radius: 3px;
              cursor: pointer;
              transition: all 0.2s linear;
            }

            .tag:hover {
              color: var(--el-menu-active-color);
              border: 1px solid var(--el-menu-active-color);
            }
          }
        }
      }
    }
  }

  .lists {
    margin-left: 20px;
    flex: 1;

    .page_controller {
      padding: 15px;
      display: flex;
      justify-content: center;
    }
  }
}
</style>
