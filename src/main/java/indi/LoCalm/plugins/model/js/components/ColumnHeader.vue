<template>
  <div>
    <!--  prefix 查询前缀 没写默认没有  -->
    <!--  suffix 查询后缀 没写默认没有 -->
    <!--  column.prop.replace(/UserName$/g, 'User')     以UserName结尾会改为User   createUserName会改为createUser  or updateUserName会改为updateUser    xxxxName会改为xxxx-->
    <el-input v-if="['input', 'textarea'].includes(column.type)" v-model="query[`${column.prefix ? column.prefix : ''}` + column.prop.replace(/UserName$/g, 'User') + `${column.suffix ? column.suffix : ''}`]" placeholder='请输入' :size='size' @keyup.enter.native="$emit('onLoad')" clearable/>
    <el-input-number v-if="column.type === 'number'" v-model="query[`${column.prefix ? column.prefix : ''}` + column.prop+`${column.suffix ? column.suffix : '_equal'}`]" placeholder='请输入' :size='size' controls-position='right' @keyup.enter.native="$emit('onLoad')"/>
    <el-select v-if="['select', 'switch', 'radio'].includes(column.type)" v-model="query[`${column.prefix ? column.prefix : ''}` + column.prop+`${column.suffix ? column.suffix : '_equal'}`]" placeholder='请选择' :size='size' @change="$emit('onLoad')" filterable clearable>
      <el-option v-for='subItem in column.dicData' :key='subItem.value' :label='subItem.label' :value='subItem.value'/>
    </el-select>
    <el-date-picker v-if="['datetime','date'].includes(column.type)" v-model="query[`${column.prefix ? column.prefix : ''}` +column.prop]" :size='size' type='datetimerange' start-placeholder='开始时间' range-separator='至' end-placeholder='结束时间' value-format='timestamp' @change="$emit('onLoad')" :clearable='false'/>
    <avue-input-tree v-if="column.type === 'tree'" v-model="query[`${column.prefix ? column.prefix : ''}` +column.prop+`${column.suffix ? column.suffix : '_equal'}`]" :props='column.props' :dic='column.dicData' :size='size' placeholder='请选择' clearable/>
  </div>
</template>

<script>

export default {
  name: 'crudColumnHeader',
  props: {
    query: Object,
    column: Object,
    size: {
      type: String,
      default: 'small'
    }
  }
}
</script>
<style>
.el-picker-panel__footer .el-picker-panel__link-btn.el-button--text {
  display: none
}
</style>
