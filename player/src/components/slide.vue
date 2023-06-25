<template>
  <div
    class="progressBox"
    @mouseleave="endState"
    @mousedown="startState"
    @mouseup="endState"
    @mousemove="setSlider"
  >
    <div class="slider">
      <div ref="Bar" class="progressBar">
        <div class="progress"></div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
//参数区
// eslint-disable-next-line vue/require-prop-types
//const props = defineProps(['wd', 'pct'])
const props = defineProps<{
  wd: number
  pct: number
}>()
const emits = defineEmits(['uppct'])
const canMove = ref(false)
const newpct = ref(0)
const Bar = ref()
const dowatch = ref(true)
//监听区
watch(
  () => props.pct,
  () => {
    if (dowatch.value) {
      Bar.value.style.width = `${props.pct * props.wd}px`
    }
  }
)
//函数区
const startState = (e: MouseEvent): void => {
  canMove.value = true
  dowatch.value = false
  newpct.value = e.offsetX / props.wd
  Bar.value.style.width = e.offsetX + 'px'
}
const endState = (): void => {
  if (canMove.value) {
    emits('uppct', newpct.value)
  }
  canMove.value = false
}
const startWatch = (): void => {
  dowatch.value = true
}
const setSlider = (e: MouseEvent): void => {
  if (canMove.value) {
    if (e.offsetX >= 0 && e.offsetX <= props.wd) {
      newpct.value = e.offsetX / props.wd
      Bar.value.style.width = e.offsetX + 'px'
    }
  }
}
onMounted(() => {
  Bar.value.style.width = `${props.pct * props.wd}px`
})
defineExpose({ startWatch })
</script>
<style lang="less" scoped>
.progressBox {
  flex: 1;
  height: 25px;
  display: flex;
  align-items: center;
  cursor: pointer;
  padding-right: 1px;

  .slider {
    flex: 1;
    height: 5px;
    background-color: var(--el-border-color);
    border-radius: 3px;

    .progressBar {
      height: 100%;
      background: rgb(107, 179, 238);
      border-radius: 3px;
      display: flex;
      align-items: center;
      overflow: hidden;

      .progress {
        flex: 1;
      }
    }
  }
}
</style>
