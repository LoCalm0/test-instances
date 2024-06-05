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
        <el-button type='danger' icon='el-icon-delete' :size='size' :disabled='multiple' @click='handleDelete' plain>
          删 除
        </el-button>
      </template>
      <template #menuRight='{ size }'>
        <crudMenuRight :size='size' @onLoad='query = {};onLoad()'/>
      </template>

      <template v-for='item in option.column' #[`${item.prop}Header`]='{ column }'>
        <crudColumnHeader :column='column' :query='query' @onLoad='onLoad'/>
      </template>
      <template #menu='{ row, index }'>
        <crudMenu :permissionList='permissionList' :crud='$refs.crud' :row='row' :index='index'>
          <el-button :size='size' icon="el-icon-setting" @click.stop=$refs.children.init(row.id) circle plain/>
        </crudMenu>
      </template>
    </avue-crud>

    <bs-ext-model-children ref="children" :status='status' :parentId="form.id" :parentData='form' @onLoad='onLoad'/>
  </basic-container>
</template>

<script>
import {getDetail, getPage, remove, submit} from './api'
import {getDicData, isEmpty, setQueryArray} from '.././util'
import {PAGE_SIZE, PAGE_SIZES} from '@/util/constants'
import BsExtModelChildren from './children'
import CrudColumnHeader from '.././components/ColumnHeader'
import crudMenuRight from '.././components/MenuRight'
import CrudMenu from '.././components/Menu'
import option from './option'

export default {
  components: {BsExtModelChildren, CrudMenu, crudMenuRight, CrudColumnHeader},
  data() {
    return {
      size: 'small',
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
    async handleSubmit(row, index, done, loading) {
      try {
        const result = await submit(row)
        row.id ? done(row) : index(row)
        this.$message({type: 'success', message: row.id ? '更新成功' : '新增成功'})
        await this.onLoad()
        if (isEmpty(row.id)) {
          this.$confirm('是否进行模型表字段配置?', {type: 'warning'}).then(() => {
            this.$refs.children.init(result.data.data)
          })
        }
      } catch (e) {
        row.id ? loading() : done()
        this.$message({type: 'error', message: row.id ? '更新失败' : '新增失败'})
      }
    },
    async onLoad() {
      this.loading = true
      if (this.oldCurrent === this.page.currentPage) this.page.currentPage = 1
      setQueryArray(this.query)
      const {currentPage: current, pageSize: size} = this.page;
      try {
        const {data: {data: {records, total}}} = await getPage({current, size, ...this.query})
        this.data = records
        this.page.total = total
        this.loading = false
        this.oldCurrent = current
      } catch (e) {
        this.loading = false
        this.$message({type: 'error', message: '获取数据失败'})
      }
    },
    async handleDelete({id}) {
      await this.$confirm('确定将选择数据删除?', {type: 'warning'})
      const ids = id || this.ids
      try {
        await remove(ids)
        await this.onLoad()
        this.$message({type: 'success', message: '删除成功'})
      } catch (e) {
        this.$message({type: 'error', message: '删除失败'})
      }
    },
    beforeOpen(done, type) {
      if ('edit' === type) getDetail(this.form.id).then(res => this.form = res.data.data)
      done()
    },

    rowClick(row, column) {
      if (column.label === '操作') return
      this.$refs.crud.toggleRowSelection(row)
    },
    selectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    }
  },
  async mounted() {
    this.status = await getDicData('/api/im-tristone-system/dict/dictionary?code=yes_no', this.findObject(this.option.column, 'status'))
  }
}
</script>
