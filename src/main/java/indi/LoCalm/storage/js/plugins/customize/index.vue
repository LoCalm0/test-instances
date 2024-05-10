<template>
  <basic-container>
    <avue-crud
      :option='option'
      :data='data'
      :table-loading='loading'
      :page.sync='page'
      :before-open='beforeOpen'
      v-model='form'
      ref='crud'
      @on-load='onLoad()'
      @row-update='handleSubmit'
      @row-save='handleSubmit'
      @row-del='handleDelete'
      @row-click='rowClick'
      @selection-change='selectionChange'
      @refresh-change='onLoad()'>
      <template slot='menuLeft'>
        <el-button type='danger' icon='el-icon-delete' :size='size' :disabled='multiple' @click='handleDelete' plain>删 除</el-button>
      </template>
      <template #menuRight='{ size }'>
        <crudMenuRight :size='size' @onLoad='query = {};onLoad()' />
     </template>

      <template v-for='item in option.column' #[`${item.prop}Header`]='{ column }'>
        <crudColumnHeader :column='column' :query='query' @onLoad='onLoad' />
      </template>
      <template #menu='{ row, index }'>
        <crudMenu :permissionList='permissionList' :crud='$refs.crud' :row='row' :index='index' />
      </template>

      <template #status='{ row }'>
        <el-switch v-if="status.dicData && status.dicData.length && (status.dicData[0].value === row.status || status.dicData[1].value === row.status)" v-model='row.status' :size='size' @change='handleSubmit(row,Object,Object,Object)' :active-text='status.dicData[1].label' :active-value='status.dicData[1].value' :active-color='status.activeColor' :inactive-text='status.dicData[0].label' :inactive-value='status.dicData[0].value' :inactive-color='status.inactiveColor' />
      </template>
    </avue-crud>

    <bs-ext-model-children ref="children" :visible.sync='visible' :status='status' :parentData='form' @onLoad='onLoad' />
  </basic-container>
</template>

<script>
import {getDetail, getList, remove} from './api'
import {getDicData, setQueryArray} from '.././util'
import {PAGE_SIZE, PAGE_SIZES} from '@/util/constants'
import BsExtModelChildren from './children'
import CrudColumnHeader from '.././components/ColumnHeader'
import crudMenuRight from '.././components/MenuRight'
import CrudMenu from '.././components/Menu'
import {mapGetters} from 'vuex'
import option from './option'

export default {
  components: {BsExtModelChildren, CrudMenu, crudMenuRight, CrudColumnHeader},
  data() {
    return {
      size: 'small',
      visible: false,
      loading: true,
      multiple: true,
      form: {},
      query: {},
      status: {},
      permissionList: {
        viewBtn: true,
        editBtn: true,
        delBtn: true
      },
      page: {
        total: 0,
        currentPage: 1,
        pageSize: PAGE_SIZE,
        pageSizes: PAGE_SIZES
      },
      ids: [],
      data: [],
      option,
    }
  },
  methods: {
    handleSubmit(row, index, done, loading) {
      row.id ? loading() : done()
      // if (!row.id) this.form.basicTableName = 'im_bs_factory_info'
      this.visible = true
      this.$refs.children.init()
    },
    async onLoad() {
      this.loading = true
      if (this.oldCurrent===this.page.currentPage) this.page.currentPage=1
      setQueryArray(this.query)
      const {data:{data,code}} = await getList({current:this.page.currentPage,size:this.page.pageSize,...this.query})
      if(code === 200) {
        this.data = data.records
        this.page.total = data.total
        this.loading = false
        this.oldCurrent=data.current
      }
    },
    async handleDelete({id}) {
      await this.$confirm('确定将选择数据删除?', {type: 'warning'})
      const ids = id || this.ids
      const {data:{code}}=await remove(ids)
      if(code === 200) {
        this.onLoad()
        this.$message({type: 'success', message: '删除成功'})
      }else {
        this.$message({type: 'error', message: '删除失败'})
      }
    },
    beforeOpen(done, type) {
      if ('edit'===type) getDetail(this.form.id).then(res => this.form = res.data.data)
      done()
    },

    rowClick(row,column){
      if (column.label === '操作' || column.columnKey === 'status') return
      this.$refs.crud.toggleRowSelection(row)
    },
    selectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    }
  },
  computed: {
    ...mapGetters(["permission"]),
  },
  async mounted() {
    this.status = await getDicData('/api/im-tristone-system/dict/dictionary?code=yes_no', this.findObject(this.option.column,'status'))
    // getDicData('/api/im-tristone-system/menu/enhanceTheTableMenu',this.findObject(this.option.column,'basicTableName'))
    // getDicData('/api/im-tristone-system/dict-biz/dictionary?code=ext_business_code', this.findObject(this.option.column,'businessCode'))
  }
}
</script>
