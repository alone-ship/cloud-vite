<template>
  <div class="background_box">
    <div class="login_container" :style="`background-color:${bg_color}`">
      <div class="right_login_card" :style="`background-color:${bg_color}`">
        <div
          class="check_background"
          @mousedown="bg_color = 'transparent'"
          @mouseup="bg_color = '#000000c2'"
        >
          <el-icon size="20"><IEpSearch /></el-icon>
        </div>
        <div class="login_card_header">
          <span class="text_row"><b class="card_header_title">WenJing</b>@aloneship</span>
          <span class="text_row"><h2>The app was created on June 13, 2023</h2></span>
          <span class="text_row"><h2>author is AloneShip</h2></span>
          <span class="text_row"><h3>Welcome Star</h3></span>
        </div>
        <el-button type="warning" style="width: 70%" @click="location_jump">
          <b>Authorize Login</b>
        </el-button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import pinia from '@renderer/plugins/pinia'
import { proxy, authorize } from '@renderer/plugins/axios'

const bg_color = ref('#000000c2')
const that = getCurrentInstance()
const route = "appContext" in that ? that.appContext.config.globalProperties.$route : null
const router = "appContext" in that ? that.appContext.config.globalProperties.$router : null
const code = route.query.code
const tk_s = pinia.token()
const location_jump = (): void => {
  window.open(
    'http://authorization-server:8801/oauth2/authorize?client_id=alone-ship&response_type=code&scope=music&redirect_uri=http://localhost:5173/',
    '_self'
  )
}
const AccessToken = (code): void => {
  const params = new URLSearchParams()
  params.append('code',code)
  params.append('grant_type','authorization_code')
  params.append('redirect_uri','http://localhost:5173/')
  proxy.post('/api/oauth2/token', params).then((res) => {
    const result = res.data
    if (res.status == 200) {
      tk_s.access_token = result.access_token
      authorize.defaults.headers.Authorization = `Bearer ${tk_s.access_token}`
      router.replace('/home')
    } else ElMessage.warning(result.data.message)
  })
}
onMounted(() => {
  if (code != undefined && code != '' && tk_s.access_token == '') AccessToken(code)
})
</script>
<style scoped lang="less">
.background_box {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background: url('../assets/pictrue/defaultBG.png') center no-repeat;
  background-size: cover;
  .login_container {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    .right_login_card {
      display: flex;
      align-self: flex-end;
      width: 400px;
      height: 100%;
      border-left: 1px solid #3e3e3e;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      position: relative;
      .check_background {
        position: absolute;
        top: 5px;
        right: 5px;
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
      }
      .login_card_header {
        font-size: 12px;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 20px;
        span.text_row {
          display: block;
          margin-bottom: 10px;
        }
        .card_header_title {
          font-size: 22px;
        }
      }
    }
  }
}
</style>
