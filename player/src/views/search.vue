<template>
  <div>
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
        :page-size="sh.rn"
        :current-page="sh.pn"
        :total="sh.total"
        @current-change="prevAnext"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
import { authorize } from "@renderer/plugins/axios";
import pinia from "@renderer/plugins/pinia";

const sh = pinia.search();
const play = pinia.play();
const tk = pinia.token();
const that = getCurrentInstance();
const router = that?.appContext.config.globalProperties.$router;
const search = ref("");
//监听区
watch(
  () => sh.keyword,
  (newval) => {
    if (newval != "" && newval.replace(/\s/g, "") != "") {
      MusicByKeyWord();
    }
  }
);
watch(
  () => play.getNewlist,
  (newval) => {
    if (newval == "backward") {
      MusicByKeyWord(play.pn, play.reqRn, (list) => {
        play.list = list;
        play.nowplay = play.list[play.index];
        play.getMusicInfo();
      });
    }
    if (newval == "forward") {
      MusicByKeyWord(play.pn, play.reqRn, (list) => {
        play.list = list;
        play.nowplay = play.list[play.index];
        play.getMusicInfo();
      });
    }
    play.getNewlist = "";
  }
);
//函数区
/**
 * 播放音乐
 * @param index
 */
const playMusic = (index: number): void => {
  play.list = sh.list;
  play.index = index;
  play.total = Math.ceil(sh.total / sh.rn);
  play.pn = 1;
  play.rn = sh.list.length;
  play.reqRn = sh.rn;
  play.getMusicInfo();
};
/**
 * 表格数据过滤
 */
const filterTableData = computed(() =>
  sh.list.filter(
    (data: { name: string }) =>
      !search.value || data.name.toLowerCase().includes(search.value.toLowerCase())
  )
);
/**
 * 当页数发生变化时
 */
const prevAnext = (val): void => {
  sh.pn = val;
  MusicByKeyWord();
};
/**
 * 关键词搜索音乐
 */
const MusicByKeyWord = (
  pn?: number,
  rn?: number,
  callback?: FunctionStringCallback
): void => {
  const params = new URLSearchParams();
  params.append("key", encodeURI(sh.keyword));
  params.append("pn", (pn ? pn : sh.pn).toString());
  params.append("rn", (rn ? rn : sh.rn).toString());
  authorize.post("/MusicBykeyWord", params).then((res) => {
    const result = res.data;
    if (result.status == 200) {
      const data = JSON.parse(result.data);
      if (data.code == 200) {
        if (callback) callback(data.data.list);
        else {
          sh.list = data.data.list;
          sh.total = Number(data.data.total);
        }
      } else ElMessage.warning("Search request faild");
    } else ElMessage.warning("Search request faild");
  });
};
//初始化区
onMounted(() => {
  if (tk.access_token != "") {
    if (sh.keyword != "") MusicByKeyWord();
  } else {
    router?.go(-1);
  }
});
</script>
<style scoped lang="less">
.page_controller {
  padding: 15px;
  display: flex;
  justify-content: center;
}
</style>
