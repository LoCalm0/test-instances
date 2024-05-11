<!--<template>-->
<!--  <el-drawer title='自定义列设置' size='1000px' :visible.sync='visible' @close="$emit('update:visible', false)" destroy-on-close append-to-body>-->
<!--    <el-table :data='column' style='width: 100%' border>-->
<!--      <el-table-column label='字段名' prop='label' align='center' show-overflow-tooltip />-->
<!--      <el-table-column label='列宽度' prop='width' align='center' width='160'>-->
<!--        <template v-slot='{row}'>-->
<!--          <avue-input-number v-model='row.width' :size='size' />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label='是否隐藏' prop='hide' align='center' width='120'>-->
<!--        <template v-slot='{row}'>-->
<!--          <el-switch v-model='row.hide' :inactive-value='false' inactive-text='否' inactive-color='#ff4949' :active-value='true' active-text='是' active-color='#13ce66' />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label='是否冻结' prop='fixed' align='center' width='120'>-->
<!--        <template v-slot='{row}'>-->
<!--          <el-switch v-model='row.fixed' :inactive-value='false' inactive-text='否' inactive-color='#ff4949' :active-value='true' active-text='是' active-color='#13ce66' />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label='是否过滤' prop='filters' align='center' width='120'>-->
<!--        <template v-slot='{row}'>-->
<!--          <el-switch v-model='row.filters' :inactive-value='false' inactive-text='否' inactive-color='#ff4949' :active-value='true' active-text='是' active-color='#13ce66' />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label='是否排序' prop='sortable' align='center' width='120'>-->
<!--        <template v-slot='{row}'>-->
<!--          <el-switch v-model='row.sortable' :inactive-value='false' inactive-text='否' inactive-color='#ff4949' :active-value='true' active-text='是' active-color='#13ce66' />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label='位置' align='center' width='90'>-->
<!--        <template v-slot='{row,$index}'>-->
<!--          <el-button type='text' :size='size' @click="moveColumn($index,'top')">上移</el-button>-->
<!--          <el-button type='text' :size='size' @click="moveColumn($index,'bottom')">下移</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->

<!--    <div style='display: flex;justify-content: center;margin: 10px'>-->
<!--      <el-button icon='el-icon-close' @click='visible=false'>取 消</el-button>-->
<!--      <el-button icon='el-icon-check' type='primary' @click='submit()' :loading='loading'>{{ loading ? '提交中 ...' : '提 交' }}</el-button>-->
<!--    </div>-->
<!--  </el-drawer>-->
<!--</template>-->

<!--<script>-->
<!--import {columnMerge, columnOne, columnSubmit} from '@/api/crudEdit'-->

<!--export default {-->
<!--  name: 'crudEdit',-->
<!--  props: {-->
<!--    column: Array,-->
<!--    urlInfo: String,-->
<!--    versions: String,-->
<!--    visible: Boolean,-->
<!--  },-->
<!--  data() {-->
<!--    return {-->
<!--      size: 'small',-->
<!--      loading: false,-->
<!--    }-->
<!--  },-->
<!--  async mounted() {-->
<!--    const {data: {data, code}} = await columnOne({urlInfo: this.urlInfo, versions: this.versions})-->
<!--    if (code === 200) {-->
<!--      if (data.verifyInfo === 'VERIFY_SUCCESS')      this.$emit('crudChange', JSON.parse(data.customColumn.columnOption))-->
<!--      else if (data.verifyInfo === 'VERIFY_FAIL')    this.columnMergeSubmit()-->
<!--      else if (data.verifyInfo === 'VERIFY_DEFAULT') this.$emit('crudChange')-->
<!--    }-->
<!--  },-->
<!--  methods: {-->
<!--    moveColumn(index, type) {-->
<!--      if (type === 'top') {-->
<!--        if (index) this.column[index - 1] = this.column.splice(index, 1, this.column[index - 1])[0]-->
<!--      } else {-->
<!--        if (index !== this.column.length - 1) this.column[index] = this.column.splice(index + 1, 1, this.column[index])[0]-->
<!--      }-->
<!--    },-->
<!--    columnMergeSubmit() {-->
<!--      columnMerge({urlInfo: this.urlInfo, versions: this.versions, columnOption: JSON.stringify(this.column)}).then(res => {-->
<!--        if(res.data.data) this.$emit('crudChange',JSON.parse(res.data.data.columnOption))-->
<!--      })-->
<!--    },-->
<!--    async submit() {-->
<!--      this.loading = true-->
<!--      let reducedInfo = this.column.map((item, index) => {-->
<!--        return {-->
<!--          serialno: index,-->
<!--          prop: item.prop,-->
<!--          label: item.label,-->
<!--          width: item.width,-->
<!--          hide: item.hide,-->
<!--          fixed: item.fixed,-->
<!--          filters: item.filters,-->
<!--          sortable: item.sortable-->
<!--        }-->
<!--      })-->
<!--      const {data: {code}} = await columnSubmit({allInfo: {columnOption: JSON.stringify(this.column), urlInfo: this.urlInfo, versions: this.versions}, reducedInfo})-->
<!--      if (code === 200) {-->
<!--        this.$emit('crudChange', this.column)-->
<!--        this.visible = false-->
<!--      }-->
<!--      this.loading = false-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->
