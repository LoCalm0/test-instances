<template>
  <span>
    <el-tooltip enterable='false' content='清空筛选' placement='bottom' style='margin-right: 10px'>
      <el-button type='danger' icon='el-icon-refresh-right' :size=size @click="$emit('onLoad')" circle plain/>
    </el-tooltip>
<!--    <el-button icon='el-icon-setting' :size=size  @click="$emit('openCrudEdit')" circle/>-->
  </span>
</template>

<script>
export default {
  name: 'crudMenuRight',
  props: {
    size: {
      type: String,
      default: 'small'
    }
  }
}
</script>
