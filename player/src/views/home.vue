<template>
  <div class="home_container">
    <div class="controller_box">
      <el-menu mode="horizontal" default-active="/Songlist" router>
        <el-menu-item disabled style="cursor: default"><b>AloneShip</b></el-menu-item>
        <el-menu-item index="/Songlist"><b>List</b></el-menu-item>
        <el-menu-item index="/bang"><b>Rank</b></el-menu-item>
        <el-menu-item @click="OpenDrawer"><b>Playing</b></el-menu-item>
      </el-menu>
      <div class="window_controller">
        <div class="search_box">
          <el-input
            v-model="SearchValue"
            placeholder="Please input"
            size="large"
            @keyup.enter="GoSearch"
            style="width: 400px"
          >
            <template #append>
              <el-button size="large" @click="GoSearch">
                <div class="icon_box">
                  <el-icon size="20">
                    <IEpSearch />
                  </el-icon>
                </div>
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
    </div>
    <div style="flex: 1">
      <router-view></router-view>
    </div>
    <!-- 抽屉  :style="`left:${DrawerVisible ? '0px' : '100%'}`"-->
    <div v-show="DrawerVisible" class="CustomDrawer">
      <div style="flex: 1" @click="handleDrawerClose"></div>
      <div class="Playing" :style="`left:${playerVisible ? '0px' : '100%'}`">
        <div class="Title">
          <b class="Content">{{ pl_s.nowplay?.name + '  ' + pl_s.Tip }}</b>
          <b class="CustomDrawerClose" @click="handleDrawerClose">
            <el-icon size="18">
              <IEpClose />
            </el-icon>
          </b>
        </div>
        <div class="Content">
          <PlayMusic />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import pinia from '@renderer/plugins/pinia/index'
import PlayMusic from './play_music.vue'
const tk_s = pinia.token()
const pl_s = pinia.play()
const search = pinia.search()
const SearchValue = ref('')
const that = getCurrentInstance()
const router = that?.appContext.config.globalProperties.$router
const DrawerVisible = ref(false)
const playerVisible = ref(false)
const OpenDrawer = (): void => {
  DrawerVisible.value = true
  setTimeout(() => {
    playerVisible.value = true
  }, 1)
}
const handleDrawerClose = (): void => {
  playerVisible.value = false
  setTimeout(() => {
    DrawerVisible.value = false
  }, 500)
}
const GoSearch = (): void => {
  if (SearchValue.value != '') {
    search.keyword = SearchValue.value
    router?.push('/search')
  } else ElMessage.warning('Please Input')
}
onMounted(() => {
  if (tk_s.access_token == '') router?.replace('/')
})
</script>
<style scoped lang="less">
.home_container {
  max-width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;

  .controller_box {
    position: relative;

    .window_controller {
      position: absolute;
      top: 50%;
      right: 5px;
      transform: translateY(-50%);
      display: flex;
      align-items: center;

      .search_box {
        margin-right: 30px;

        .icon_box {
          display: flex;
          align-items: center;
          height: 16px;
        }
      }

      .control {
        padding: 10px;
        width: 40px;
        height: 40px;
        overflow: hidden;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid var(--el-border-color);
        background-color: var(--el-fill-color-light);
        border-radius: 10px;
        cursor: pointer;
        margin: 0 5px;
      }
    }
  }

  .CustomDrawer {
    display: flex;
    position: absolute;
    right: 0;
    top: 59px;
    background: #000000b8;
    bottom: 0;
    width: 100%;
    justify-content: flex-end;
    overflow: hidden;
    z-index: 3;

    .Playing {
      width: 450px;
      background: #202020;
      position: relative;
      left: 500px;
      transition: left 0.5s linear;
      z-index: 4;

      .Title {
        padding: 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .Content {
          max-width: 200px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .CustomDrawerClose {
          cursor: pointer;
        }
      }

      .Content {
        padding: 0 20px;
      }
    }
  }
}
</style>
