<template>
  <div class="songlist_container">
    <div class="position_tags_box" :style="`width:${tags_box_hidden ? 0 : 300}px`">
      <div class="collapse_box">
        <span class="tags_box_collapse" @click="tags_box_hidden = !tags_box_hidden"> lll </span>
        <div ref="TagBoxInstance" class="tags_box" :style="`padding:${tags_box_hidden ? 0 : 10}px`">
          <el-scrollbar max-height="810px">
            <div
              class="tags_index_box"
              :style="`padding:${tags_box_hidden ? 0 : 10}px;width:${tags_box_hidden ? 0 : 260}px;`"
            >
              <div class="tags_container">
                <div class="tag_index_title">精选</div>
                <div class="tags">
                  <span class="tag" @click="getplaylist('hot')">热门</span>
                  <span class="tag" @click="getplaylist('new')">最新</span>
                </div>
              </div>
              <div v-for="(item, index) in playlist.tags" :key="index" class="tags_container">
                <div class="tag_index_title">{{ item.name }}</div>
                <div class="tags">
                  <span
                    v-for="(tag, i) in item.data"
                    :key="i"
                    class="tag"
                    @click="getplaylist(Number(tag.id))"
                  >
                    {{ tag.name }}
                    <b :style="`color:${tag.extend == '|HOT' ? 'red' : 'green'}`">{{
                      tag.extend.replace(/\|/g, '')
                    }}</b>
                  </span>
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>
      </div>
    </div>
    <div class="lists">
      <el-scrollbar ref="scrollbar" max-height="820px" style="width: 100%">
        <div class="lists_box">
          <div
            v-for="(item, index) in playlist.playlists"
            :key="index"
            class="list"
            @click.stop="GoInfo(item.id)"
          >
            <img :src="item.img" alt="" />
            <div class="on_play_card">
              <!-- @click.stop="PlayListMusic(item.id)" -->
              <span class="play">▶</span>
            </div>
            <div class="detail">
              <p>{{ item.name }}</p>
              <p>
                author : <b>{{ item.uname }}</b>
              </p>
              <p>
                listencnt : <b>{{ item.listencnt }}</b>
              </p>
              <p>
                total : <b>{{ item.total }}</b>
              </p>
            </div>
          </div>
        </div>
      </el-scrollbar>
      <div class="page_controller">
        <el-pagination
            :hide-on-single-page="true"
            background
            layout="prev, pager, next"
            :page-size="playlist.rn"
            :current-page="playlist.pn"
            :total="playlist.total"
            @current-change="Do"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ElScrollbar } from 'element-plus'
import pinia from '@renderer/plugins/pinia'
import { authorize } from '@renderer/plugins/axios'

//参数区
const tags_box_hidden = ref(false)
const that = getCurrentInstance()
const router = that?.appContext.config.globalProperties.$router
const playlist = pinia.playlist()
const playlistinfo = pinia.playlistinfo()
const tk = pinia.token()
const scrollbar = ref<InstanceType<typeof ElScrollbar>>()
let loading
//函数区
/**
 * 跳转到详情
 */
const GoInfo = (id): void => {
  playlistinfo.pn = 1
  playlistinfo.key = id
  router?.push('/listinfo')
}
/**
 * 页数变化时触发
 */
const Do = (val): void => {
  playlist.pn = val
  scrollbar.value?.setScrollTop(0)
  getplaylist()
}
/**
 * 按标签获取歌单
 * @param tag
 * @param name
 */
const getplaylist = (tag?: string | number): void => {
  loadingService()
  if (tag) playlist.nowShowType = tag
  const params = new URLSearchParams()
  let path = ''
  if (typeof playlist.nowShowType == 'string') {
    params.append('order', playlist.nowShowType)
    path = '/OrderList'
  } else {
    params.append('id', playlist.nowShowType.toString())
    path = '/TagList'
  }
  params.append('pn', playlist.pn.toString())
  params.append('rn', playlist.rn.toString())
  authorize.post(path, params).then((res): void => {
    if (res.status == 200) {
      const result = res.data
      if (result.status == 200) {
        const data = JSON.parse(result.data)
        if (data.code == 200) {
          playlist.playlists = data.data.data
          playlist.total = data.data.total
          loading.close()
        } else getplaylist(tag)
      } else getplaylist(tag)
    } else ElMessage.warning('lists requst not send')
  })
}
/**
 * 获取歌单类型
 */
const getListTypes = (): void => {
  authorize.get('/ListTag').then((res): void => {
    if (res.status == 200) {
      const result = res.data
      if (result.status == 200) {
        const data = JSON.parse(result.data)
        if (data.code == 200) {
          playlist.tags = data.data
          if (playlist.playlists == '') getplaylist('new')
        } else initPage()
      } else initPage()
    } else ElMessage.warning('tags requst not send')
  })
}
/**
 * 第一次加载页面时
 */
const initPage = (): void => {
  if (playlist.tags == '') getListTypes()
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
//初始化
onMounted(() => {
  if (tk.access_token != '') {
    initPage()
  } else router?.go(-1)
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
    margin: 10px 30px;
    max-width: 100%;
    flex: 1;
    display: flex;
    flex-direction: column;

    .lists_box {
      display: flex;
      flex-wrap: wrap;

      .list {
        position: relative;
        margin: 10px;
        overflow: hidden;
        font-size: 12px;
        display: flex;
        width: 280px;
        overflow: hidden;
        border: 1px solid var(--el-border-color);
        background-color: var(--el-fill-color-light);

        img {
          width: 150px;
          height: 150px;
        }

        .on_play_card {
          position: absolute;
          width: 150px;
          height: 150px;
          display: none;
          align-items: center;
          justify-content: center;
          background-color: #26272775;
          font-size: 35px;

          .play {
            padding: 10px;
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px solid #b3b3b3;
            color: #ddd;
            border-radius: 50%;
            padding-left: 17px;
            cursor: pointer;
          }
        }

        .detail {
          padding: 10px;
          display: flex;
          flex-direction: column;
          justify-content: space-around;

          p {
            max-height: 32px;
            overflow: hidden;
          }
        }
      }

      .list:hover {
        .on_play_card {
          display: flex;
        }
      }
    }

    .page_controller {
      padding: 10px;
      display: flex;
      justify-content: center;
    }
  }
}
</style>
