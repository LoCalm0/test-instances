<template>
  <div>
    <el-button v-if='permissionList.viewBtn' type='primary' icon='el-icon-view'   size='mini' @click='crud.rowView(row, index)' circle plain/>
    <el-button v-if='permissionList.editBtn' type='success' icon='el-icon-edit'   size='mini' @click='crud.rowEdit(row, index)' circle plain/>
    <el-button v-if='permissionList.delBtn'  type='danger'  icon='el-icon-delete' size='mini' @click='crud.rowDel(row, index)'  circle plain/>
  </div>
</template>

<script>
export default {
  name: 'crudMenu',
  props: {
    permissionList: Object,
    crud: Object,
    row: Object,
    index: Number,
    size: {
      type: String,
      default: 'small'
    }
  }
}
</script>
