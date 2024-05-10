<template>
  <el-dialog title='test' :visible.sync='visible' @close="$emit('update:visible', false)" fullscreen append-to-body>
    <template slot='title'>
      <div>
        <span style='margin-right: 5px'>test</span>
        <el-button :size='size' @click='addColumn'       :disabled='loading'>add field test</el-button>
        <el-button :size='size' @click='editColumn'      :disabled='selection.length !== 1'>edit field test</el-button>
        <el-button :size='size' @click='delColumn'       :disabled='!selection.length'>del field test</el-button>
        <el-button :size='size' @click='showFormEffect'  :disabled='loading'>show form effect</el-button>
        <el-button :size='size' @click="oldValue=null;init()" icon='el-icon-refresh' style='float: right;margin-right: 40px' circle plain />
        <el-button :size='size' @click='multipleChoices' :disabled='loading'>multiple choices test</el-button>
      </div>
    </template>
    <!--form-table-->
    <el-form :model='form' ref='formTable'>
      <el-table ref='table' v-loading='loading' :data='form.data' :row-class-name='rowClassName' @row-click='rowClick' @selection-change='selectionChange' border>
        <el-table-column type='selection' width='50' align='center' />
        <template v-for='item in optionTable.column'>
          <el-table-column :label='item.label' :prop='item.prop' :width='item.width' align='center'>
            <template v-slot='{row,$index}'>
                <el-form-item style='margin-bottom: 0' :prop='`data.${$index}.${item.prop}`' :rules='item.rules'>
                <el-tooltip :disabled='!map.has(`data.${$index}.${item.prop}`)' :content='map.get(`data.${$index}.${item.prop}`)' placement='bottom'>
                  <el-input        v-if="     item.type === 'input'"  v-model.trim.number='row[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :disabled="item.prop === 'prop' && !Boolean(form.data[$index].isExtPro)" clearable/>
                  <el-input-number v-else-if="item.type === 'number'" v-model='row[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :min='item.min' :max='item.max' :precision='item.precision' controls-position='right' @blur='item.blur($event, form.data[$index])' />
                  <el-switch       v-else-if="item.type === 'switch'" v-model='row[item.prop]' :size='size' :active-text='dicData[0].label' :active-value='dicData[0].value' :active-color='status.activeColor' :inactive-text='dicData[1].label' :inactive-value='dicData[1].value' :inactive-color='status.inactiveColor' />
                  <el-select       v-else-if="item.type === 'select'" v-model='row[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :disabled="item.prop === 'propertyType' && !Boolean(form.data[$index].isExtPro)" filterable clearable>
                    <el-option     v-for='dic in item.dicData' :key='item.value' :label='dic.label' :value='dic.value' />
                  </el-select>
                </el-tooltip>
              </el-form-item>
            </template>
          </el-table-column>
        </template>
      </el-table>
    </el-form>
    <!--form-->
    <el-dialog title='edit test' width='60%' :visible.sync='dialogFormVisible' :before-close="formVerify" append-to-body>
      <el-form :model='dialogForm' ref="form">
        <el-row>
          <template v-for='item in optionForm.column'>
            <el-col :span='item.span ? item.span : 12' v-if='item.editDisplay'>
              <el-form-item :label='item.label' :prop='item.prop' :rules='item.rules' :label-width="item.labelWidth ? item.labelWidth : '145px'">
                <el-input        v-if="item.type      === 'input'"    v-model.trim='dialogForm[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :disabled='item.disabled' clearable/>
                <el-input        v-else-if="item.type === 'textarea'" v-model.trim='dialogForm[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :disabled='item.disabled' rows="4" type='textarea' @blur='item.blur($event, dialogForm)' />
                <el-input-number v-else-if="item.type === 'number'"   v-model='dialogForm[item.prop]'      :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :min='item.min' :max='item.max' :precision='item.precision' controls-position='right' style='width: 100%' />
                <el-switch       v-else-if="item.type === 'switch'"   v-model='dialogForm[item.prop]'      :size='size' :active-text='dicData[0].label' :active-value='dicData[0].value' :active-color='status.activeColor' :inactive-text='dicData[1].label' :inactive-value='dicData[1].value' :inactive-color='status.inactiveColor'/>
                <el-select       v-else-if="item.type === 'select'"   v-model='dialogForm[item.prop]'      :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :disabled='item.disabled' style="width: 100%" filterable clearable>
                  <el-option     v-for='item in item.dicData' :key='item.value' :label='item.label' :value='item.value' />
                </el-select>
                <div v-if="item.type === 'compound'">
                  <template v-for="dict in item.children">
                    <el-input v-model.trim='dialogForm[dict.prop]' :size='size' :placeholder='dict.placeholder' :disabled='dict.disabled' :style='{width:dict.width}' />
                  </template>
                </div>
              </el-form-item>
            </el-col>
          </template>
        </el-row>
      </el-form>
      <div slot='footer' class='dialog-footer'>
        <el-button @click='formVerify'>确 定</el-button>
      </div>
    </el-dialog>

    <span slot='footer' class='dialog-footer'>
      <el-button type='danger'  @click='visible = false'>关 闭</el-button>
      <el-button type='primary' @click='handleSubmit()' :loading='loading'>提 交</el-button>
    </span>

    <avue-crud v-show='false' :option='optionShowFormEffect' ref='crud' />
  </el-dialog>
</template>


<script>
import optionForm from './optionForm'
import optionTable from './optionTable'
import optionShowFormEffect from './optionShowFormEffect'
import {getColumnJson, submit} from './api'
import {getDicData, isEmpty, isNotEmpty, isObjectEqual} from '../.././util'

export default {
  name: 'bsExtModelChildren',
  props: {
    visible: Boolean,
    status: Object,
    parentData: Object,
  },
  data() {
    //自定义校验规则
    const dict = (rule, value, callback) => {
      value ? this.disabledStatus(this.preventDuplication, rule.field,true) : this.disabledStatus(!this.preventDuplication, rule.field, false); callback()
    }
    const dictIsNotEmpty = (rule, value, callback) => {
      const arr = Array.from(new Set([Boolean(this.dialogForm.url), Boolean(this.dialogForm.labelDict), Boolean(this.dialogForm.valueDict), Boolean(this.dialogForm.errorMessage)]))
      if (arr.length === 2) {
        this.disabledStatus(this.preventDuplication, rule.field, true)
        callback(new Error('数据来源不能为空'))
      } else {
        if (arr[0] === false) this.disabledStatus(!this.preventDuplication, rule.field, false)
        callback()
      }
    }
    // const fieldLength = (rule, value, callback) => {
    //   if (Boolean(this.dialogForm.dictLabel) === Boolean(this.dialogForm.dictValue)) {
    //     if (Boolean(this.dialogForm.dictLabel) === false) {
    //       this.disabledStatus(!this.preventDuplication,this.finalDisabled.filter(item => !['dictLabel', 'dictValue'].includes(item)), null, false)
    //       this.$refs.form.clearValidate(['dictLabel','dictValue'])
    //       return callback()
    //     }
    //     const dictLabel=this.dialogForm.dictLabel.split(',').filter(Boolean)
    //     const dictValue=this.dialogForm.dictValue.split(',').filter(Boolean)
    //     if (new Set(dictLabel).size !== dictLabel.length) return  callback(new Error('显示值有重复数据'))
    //     if (new Set(dictValue).size !== dictValue.length) return  callback(new Error('选中值有重复数据'))
    //     if (dictLabel.length === dictValue.length) this.$refs.form.clearValidate(['dictLabel','dictValue']);callback()
    //
    //   }
    //   this.disabledStatus(this.preventDuplication,this.finalDisabled.filter(item => !['dictLabel', 'dictValue'].includes(item)), null, true)
    //   callback(new Error('显示值,选中值 长度不相等'))
    // }
    const fieldRepeat = (rule, value, callback) => {
      const Index = Number(rule.field.split('.')[1])
      let arr = []
      this.form.data.forEach((item, index) => {
        if (index !== Index) arr.push(item.prop)
      })
      if (arr.includes(value)) callback(new Error('属性名重复'))
      callback()
    }
    return {
      size: 'small',//使用mini编辑样式会出问题
      oldValue: null,
      loading: true,
      choices: false,
      dialogFormVisible: false,
      preventDuplication: false,
      dialogForm: {},
      form: {
        data: [],
      },
      arr: [],
      ids: [],
      clone: [],
      index: [],
      proIds: [],
      map: new Map(),
      selection: [],
      dicData: [
        {label: '是', value: true},
        {label: '否', value: false}
      ],
      rulesDict: [{validator: dict, trigger: 'change'}],
      // rulesDictLength: [{validator: fieldLength, trigger: 'blur'}],
      rulesDictIsNotEmpty: [{validator: dictIsNotEmpty, trigger: 'blur'}],
      rulesProp: [
        {pattern: /^[A-Za-z]+$/, required: true, message: '只能输入字母', trigger: 'blur'},
        {validator: fieldRepeat, trigger: 'blur'}
      ],
      optionForm,
      optionTable,
      optionShowFormEffect
    }
  },
  methods: {
    multipleChoices() {
      this.choices = !this.choices
      this.$message({type: 'success', message: this.choices ? '行选启用' : '行选关闭'})
    },
    showFormEffect() {
      optionShowFormEffect.column = this.form.data.map(cloneItem => {
        cloneItem.columnJson = {label: cloneItem.label, prop: cloneItem.prop, children: [{}]}
        this.handleFrom(cloneItem.type, cloneItem)
        this.optionTable.column.forEach(tableItem => {
          if (tableItem.columnJson !== false) this.arr.push(tableItem.prop) //获取formTable数据
        })
        this.arr.forEach(field => {
          if (cloneItem[field]) cloneItem.columnJson.children[0][field] = cloneItem[field]
        })
        if (cloneItem.columnJson.children[0].type === 'switch') {
          const {activeColor, inactiveColor,dicData} = this.status
          cloneItem.columnJson.children[0].value = 1
          cloneItem.columnJson.children[0].dataType = 'number'
          cloneItem.columnJson.children[0].dicData = dicData
          cloneItem.columnJson.children[0].activeColor = activeColor
          cloneItem.columnJson.children[0].inactiveColor = inactiveColor
        }

        if (isNotEmpty(cloneItem.columnJson.children[0].rules)) cloneItem.columnJson.children[0].rules = eval(cloneItem.columnJson.children[0].rules)
        if (isNotEmpty(cloneItem.columnJson.children[0].order)) cloneItem.columnJson.children[0].order = cloneItem.columnJson.children[0].order > 0 ? -Math.abs(cloneItem.columnJson.children[0].order) : cloneItem.columnJson.children[0].order < 0 ? Math.abs(cloneItem.columnJson.children[0].order) : cloneItem.columnJson.children[0].order

        console.log(cloneItem.columnJson)
        return cloneItem.columnJson
      })
      this.arr = []
      this.$refs.crud.rowAdd()
    },
    async formVerify() {
      await this.$refs.form.validate()
      this.dialogFormVisible = false
    },
    disabledStatus(run, field, flag) {
      if (run) return
      const finalDisabled = ['systemDict', 'busineDict', 'dict', 'dicData']
      for (let item of finalDisabled) {
        if (item === field) continue;
        const column = this.findObject(this.optionForm.column, item)
        column.children ? column.children.forEach(dict => dict.disabled = flag) : column.disabled = flag
      }
      this.preventDuplication = flag
    },
    addColumn() {
      this.form.data.push({
        span: 12,
        isExtPro: 1,
        isPrint: true,
        isExport: true,
        addDisplay: true,
        editDisplay: true,
        viewDisplay: true,
        modelId: this.parentData.id
      })
    },
    editColumn() {
      this.arr.forEach(item => this.findObject(this.optionForm.column, item).editDisplay = false)
      this.dialogForm = this.form.data[this.index[0]]
      if (this.dialogForm.type === 'switch') return this.$message({type: 'error', message: '不支持'})
      this.handleFrom(this.dialogForm.type)
      this.dialogFormVisible = true
    },
    delColumn() {
      this.index.sort((a, b) => a - b)
      for (const index of this.index) {
        if (!this.form.data[index].isExtPro) {
          return this.$message({type: 'error', message: '基础字段不能删除'})
        }
        const { id, proId } = this.form.data[index];
        this.ids.push(id);this.proIds.push(proId)
      }
      this.form.data = this.form.data.filter(item => !this.index.includes(item.index))
    },
    async handleSubmit() {
      await this.$refs.formTable.validate()
      const Index = []
      const arr = this.form.data.map(item => item.prop)
      for (let cloneElement of this.clone) {  //去除没变更数据
        let index = arr.indexOf(cloneElement.prop)
        if (index !== -1) {
          if (isObjectEqual(this.form.data[index], cloneElement, 'index')) Index.push(index)
        }
      }
      const cloneFormData = this.form.data.filter(item => !Index.includes(item.index))
      if (isEmpty(cloneFormData) && isEmpty(this.ids)) return this.visible = false
      this.loading = true
      const {businessCode, extModelName, basicTableName} = this.parentData
      cloneFormData.forEach(cloneItem => {
        cloneItem.extModelName = extModelName
        cloneItem.businessCode = businessCode
        cloneItem.basicTableName = basicTableName
        cloneItem.isExport = cloneItem.isExport ? 1 : 0
        cloneItem.isPrint = cloneItem.isPrint ? 1 : 0
        cloneItem.propertyName = cloneItem.prop
        cloneItem.propertyLable = cloneItem.label
        cloneItem.columnJson = {label: cloneItem.label, prop: cloneItem.prop, children: [{}]}
        this.handleFrom(cloneItem.type, cloneItem)
        this.optionTable.column.forEach(tableItem => {
          if (tableItem.columnJson !== false) this.arr.push(tableItem.prop) //获取formTable数据
        })
        this.arr.forEach(field => {
          if (cloneItem[field]) cloneItem.columnJson.children[0][field] = cloneItem[field]
        })
        cloneItem.columnJson = JSON.stringify(cloneItem.columnJson)
      })
      try {
        await submit(cloneFormData, this.proIds, this.ids)
        this.oldValue = null //重置
        this.visible = false
        this.$nextTick(() => {
          this.$parent.$parent.$parent.$refs.crud.closeDialog() //关闭父级对话框
          this.$emit('onLoad')
        })
        this.$message({type: 'success', message: '操作成功'})
      } catch (e) {
        this.loading = false
        this.$message({type: 'error', message: '操作失败'})
      }
    },
    handleFrom(type, item) {
      let arr = ['rules', 'placeholder', 'clearable']
      switch (type) {
        case 'input':
          this.arr = arr.concat(['maxlength', 'minlength'])
          break
        case 'textarea':
          this.arr = arr.concat(['maxlength', 'minlength', 'rows', 'maxRows', 'minRows'])
          break
        case 'number':
          this.arr = arr.concat(['min', 'max', 'step'])
          break
        case 'select':
          this.arr = arr.concat(['multiple', 'cascader', 'cascaderIndex', 'filterable', 'remote', 'systemDict', 'busineDict', 'dataType', 'dict','dicData'])
          // if (item) this.assembleDicData(item)
          break
        case 'tree':
          this.arr = arr.concat(['multiple', 'tags', 'checkStrictly', 'parent', 'accordion', 'leafOnly'])
          break
        case 'datetime':
          this.arr = arr.concat(['valueFormat', 'format'])
          break
        case 'switch': //暂时没用
          this.arr = ['dataType', 'systemDict', 'busineDict', 'dict']
          // if (item) this.assembleDicData(item)
          break
        default:
          return item ? this.$message({type: 'error', message: '表单类型不能为空'}) : this.$message({type: 'info', message: '请选择表单类型'})
      }
      if (isEmpty(item)) this.arr.forEach(item => this.findObject(this.optionForm.column, item).editDisplay = true)
    },
    // assembleDicData(item) {
    //   this.arr = this.arr.splice(0, this.arr.indexOf('dict'))  //只要在dict数组前的数据
    //   if (item.url) {
    //     item.columnJson.children[0].dict = {url: item.url, label: item.labelDict, value: item.valueDict, errorMessage: item.errorMessage}
    //   } else if (item.dictLabel) {
    //     const label = item.dictLabel.includes(',') ? item.dictLabel.split(',').filter(Boolean) : Array(item.dictLabel)
    //     const value = item.dictValue.includes(',') ? item.dictValue.split(',').filter(Boolean) : Array(item.dictValue)
    //     item.columnJson.children[0].dicData = label.map((label, index) => {
    //       return {label, value: value[index]}
    //     })
    //   }
    // },
    rowClassName({row, rowIndex}) {
      row.index = rowIndex
    },
    rowClick(row, event, column) {
      if (this.choices) {
        if (column.label === '操作' || column.columnKey === 'status') return
        this.$refs.table.toggleRowSelection(row)
      }
    },
    selectionChange(selection) {
      this.selection = selection
      this.index = selection.map(item => item.index)
    },
    async init() {
      this.loading = true
      //modelId=t0 proId=t1 id=t2
      if (this.parentData.id) {
        if (this.oldValue === this.parentData.id) return this.loading = false
        this.oldValue = this.parentData.id
        const {data: {data, code}} = await getColumnJson(this.oldValue)
        if (code === 200) {
          this.form.data = data.map(item => {
            console.log(item)
            console.log(JSON.parse(item.columnJson))
            const columnJson = JSON.parse(item.columnJson).children[0]
            columnJson.id = item.id
            columnJson.proId = item.proId
            columnJson.modelId = item.modelId
            columnJson.propertyType = item.propertyType
            columnJson.isExtPro = item.isExtPro
            columnJson.isPrint = item.isPrint
            this.status.dicData.forEach(dic => {
              if (dic.value === item.isPrint) columnJson.isPrint = dic.label === '是' ? true : dic.label === '否' ? false : ''
              if (dic.value === item.isExport) columnJson.isExport = dic.label === '是' ? true : dic.label === '否' ? false : ''
            })
            return columnJson
          })
          this.clone = structuredClone(this.form.data)
        }
      } else {
        return this.loading = false
        if (this.oldValue === this.parentData.basicTableName) return this.loading = false
        this.oldValue = this.parentData.basicTableName
        const {data: {data, code}} = await columnOne({urlInfo: this.oldValue, versions: '1.0.0'})
        if (code === 200) {
          const {column} = JSON.parse(data.customColumn.columnOption)
          this.form.data = column.map(item => {
            if (!item.type) item.type = 'input'
            if (['input', 'textarea'].includes(item.type)) item.propertyType = 'java.lang.String'
            else if (['datetime', 'date', 'tree', 'select'].includes(item.type)) item.propertyType = 'java.lang.Long'
            else if (['switch', 'number', 'radio'].includes(item.type)) item.propertyType = 'java.lang.Integer'
            else item.propertyType = 'java.lang.String'
            item.span = item.span ? item.span : 12
            item.rules = JSON.stringify(item.rules)
            item.order = item.order > 0 ? -Math.abs(item.order) : item.order < 0 ? Math.abs(item.order) : item.order
            item.addDisplay = item.addDisplay !== false
            item.editDisplay = item.editDisplay !== false
            item.viewDisplay = item.viewDisplay !== false
            item.isExtPro = 0
            item.isExport = true
            item.isPrint = true
            return item
          })
        }
      }
      this.ids = []
      this.arr = []
      this.proIds = []
      this.loading = false
    }
  },
  async mounted() {
    this.findObject(this.optionTable.column, 'prop').rules = this.rulesProp
    this.findObject(this.optionForm.column, 'systemDict').rules = this.rulesDict
    this.findObject(this.optionForm.column, 'busineDict').rules = this.rulesDict
    // this.findObject(this.optionForm.column, 'dictLabel').rules = this.rulesDictLength
    // this.findObject(this.optionForm.column, 'dictValue').rules = this.rulesDictLength
    this.findObject(this.optionForm.column, 'dict').rules = this.rulesDictIsNotEmpty
    for (const item of this.optionTable.column) {
      if (item.type === 'switch') {
        const {activeColor, inactiveColor} = this.status
        item.dicData = this.dicData
        item.activeColor = activeColor
        item.inactiveColor = inactiveColor
      }
    }
    for (const item of this.optionForm.column) {
      if (item.dict) await getDicData(item.dict.url, item)
    }
  },
  watch: {
    'form.data': {
      handler() {
        this.map.clear()
        this.$refs.formTable.validate()
        this.$refs.formTable.fields.forEach(item => {
          if (item.validateState === 'error') {
            this.map.set(item._props.prop, item.validateMessage ? item.validateMessage : item._props.rules[0].message)
          }
        })
      },
      deep: true
    }
  }
}
</script>
<style scoped>
::v-deep .el-dialog__body {
  padding: 0 20px;
}

::v-deep .el-table-column--selection .cell {
  text-align: right;
  padding: 0 10px
}
</style>
