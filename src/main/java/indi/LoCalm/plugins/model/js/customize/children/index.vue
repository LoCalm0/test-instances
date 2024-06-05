<template>
  <el-dialog title='test' :visible.sync='visible' @close="$emit('update:visible', false)" fullscreen append-to-body>
    <template slot='title'>
      <div>
        <span style='margin-right: 5px'>test</span>
        <el-button :size='size' @click='addColumn'       :disabled='loading'>add field test</el-button>
        <el-button :size='size' @click='editColumn'      :disabled='selection.length !== 1'>edit field test</el-button>
        <el-button :size='size' @click='delColumn'       :disabled='!selection.length'>del field test</el-button>
        <el-button :size='size' @click='showFormEffect'  :disabled='loading'>show form effect</el-button>
        <el-button :size='size' @click="refresh" icon='el-icon-refresh' style='float: right;margin-right: 40px' circle plain />
        <el-button :size='size' @click='multipleChoices' :disabled='loading'>multiple choices test</el-button>
        <el-button :size='size' @click='textareaDefaultValueClick' :disabled='loading'>textarea default value test</el-button>

        <el-popover placement="left-end" width="600" trigger="click">
          <el-button :size='size' @click='showJson' slot="reference" style='float: right' >show json test</el-button>
          <el-input type="textarea" :rows="40" :size="size" :value="jsonData" style="height: 800px"/>
        </el-popover>
        <el-popover placement="left-end" width="600" trigger="click">
          <el-button :size='size' :disabled='!selection.length' @click='chooseShowJson' slot="reference" style='float: right;margin-right: 10px'>choose show json test</el-button>
          <el-input type="textarea" :rows="40" :size="size" :value="chooseJsonData" style="height: 800px" />
        </el-popover>


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
                  <el-input        v-if="     item.type === 'input'"  v-model.trim.number='row[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" clearable/>
                  <el-input-number v-else-if="item.type === 'number'" v-model='row[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :min='item.min' :max='item.max' :precision='item.precision' controls-position='right' @blur='item.blur($event, form.data[$index])' />
                  <el-switch       v-else-if="item.type === 'switch'" v-model='row[item.prop]' :size='size' :active-text='dicData[0].label' :active-value='dicData[0].value' :active-color='status.activeColor' :inactive-text='dicData[1].label' :inactive-value='dicData[1].value' :inactive-color='status.inactiveColor' />
                  <el-select       v-else-if="item.type === 'select'" v-model='row[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" @change='item.change ? item.change($event, form.data[$index],textareaDefaultValue) : Object' filterable clearable>
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
                <template #label v-if="item.prop === 'rules'">
                  <span>{{ item.label }}</span>
                  <br>
                  <el-button :size='size' @click="handleButtonClick" type="primary">生成默认规则</el-button>
                </template>
                <el-input        v-if="item.type      === 'input'"    v-model.trim='dialogForm[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :disabled='item.disabled' clearable/>
                <el-input        v-else-if="item.type === 'textarea'" v-model.trim='dialogForm[item.prop]' :size='size' :placeholder="item.placeholder ? item.placeholder : ''" :disabled='item.disabled' rows="4" type='textarea' />
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
import {getDetail, submit} from './api'
import {deleteEmptyElements, getDicData, isEmpty, isNotEmpty, isObjectEqual} from '../.././util'

export default {
  name: 'bsExtModelChildren',
  props: {
    status: Object,

    parentData: Object,
  },

  data() {
    //自定义校验规则
    const dict = (rule, value, callback) => {
      value ? this.disabledStatus(this.preventDuplication, rule.field, true) : this.disabledStatus(!this.preventDuplication, rule.field, false);
      callback()
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
      oldParentId: null,
      loading: true,
      textareaDefaultValue: true,
      choices: false,
      visible: false,
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
      jsonData: [],
      chooseJsonData: [],
      map: new Map(),
      selection: [],
      dicData: [
        {label: '是', value: true},
        {label: '否', value: false}
      ],
      rulesDict: [{validator: dict, trigger: 'change'}],
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
    fromSubmitData(data,isNotSubmitData) {
      return data.map(item => {
        const cloneItem = structuredClone(item)
        this.deleteElements(cloneItem)
        const fieldJson = item.isChildren ? {label: cloneItem.label, prop: cloneItem.prop, children: [cloneItem]} : cloneItem
        return isNotSubmitData ? fieldJson : {
          id: item.id,
          modelTableId: this.oldParentId,
          fieldName: item.prop,
          fieldRemark: item.label,
          fieldType: item.fieldType,
          fieldJson: JSON.stringify(fieldJson)
        }
      })
    },
    handleButtonClick() {
      const message = this.dialogForm.type === 'select' ? '请选择' : '请输入'.concat(this.dialogForm.label)
      const trigger = this.dialogForm.type === 'select' ? 'change' : 'blur'
      this.$set(this.dialogForm, 'rules', `[{required: true, message: '${message}', trigger: '${trigger}'}]`)
    },
    textareaDefaultValueClick() {
      this.textareaDefaultValue = !this.textareaDefaultValue
      this.$message({duration: 1000,type: 'success', message: this.textareaDefaultValue ? '多行文本默认值启用' : '多行文本默认值关闭'})
    },
    deleteElements(item) {
      const deleteFields = ['fieldType', 'index', 'id', 'modelTableId','isChildren']
      ;['maxRows','minRows'].forEach(field => {
        if (item[field] === 0) deleteFields.push(field)
      })
      // ;['addDisplay','editDisplay','viewDisplay'].forEach(field => {
      //   if (item[field] === true) deleteFields.push(field)
      // })
      deleteFields.forEach(field => {
        delete item[field]
      })
      deleteEmptyElements(item)
    },
    showJson() {
      this.jsonData = JSON.stringify(this.fromSubmitData(structuredClone(this.form.data),true), null, 4)
    },
    chooseShowJson() {
      this.chooseJsonData = JSON.stringify(this.fromSubmitData(structuredClone(this.selection),true), null, 4)
    },
    refresh() {
      const temp = this.oldParentId;
      this.oldParentId = null
      this.init(temp)
    },
    multipleChoices() {
      this.choices = !this.choices
      this.$message({duration: 1000,type: 'success', message: this.choices ? '行选启用' : '行选关闭'})
    },
    showFormEffect() {
      optionShowFormEffect.column = structuredClone(this.form.data).map(cloneItem => {
        if (cloneItem.type === 'switch') {
          const {activeColor, inactiveColor, dicData} = this.status
          cloneItem.value = 1
          cloneItem.dataType = 'number'
          cloneItem.dicData = dicData
          cloneItem.activeColor = activeColor
          cloneItem.inactiveColor = inactiveColor
        }
        if (isNotEmpty(cloneItem.rules)) cloneItem.rules = eval(cloneItem.rules)
        this.deleteElements(cloneItem)
        return cloneItem
      })
      console.log(optionShowFormEffect.column)
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
        addDisplay: true,
        editDisplay: true,
        viewDisplay: true,
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
        const {id} = this.form.data[index];
        this.ids.push(id);
      }
      this.form.data = this.form.data.filter(item => !this.index.includes(item.index))
    },
    async handleSubmit() {
      await this.$refs.formTable.validate()
      const indexArr = []
      const propsArr = this.form.data.map(item => item.prop)
      /** 去除没变更数据 */
      for (let cloneElement of this.clone) {
        let index = propsArr.indexOf(cloneElement.prop)
        if (index !== -1) {
          if (isObjectEqual(this.form.data[index], cloneElement, 'index')) indexArr.push(index)
        }
      }
      const cloneFormData = this.form.data.filter(item => !indexArr.includes(item.index))

      if (isEmpty(cloneFormData) && isEmpty(this.ids)) return this.visible = false
      this.loading = true
      try {
        await submit(this.fromSubmitData(cloneFormData,false), this.ids)
        this.oldParentId = null //重置
        this.visible = false
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
          this.arr = arr.concat(['multiple', 'cascader', 'cascaderIndex', 'filterable', 'remote', 'systemDict', 'busineDict', 'dataType', 'dict', 'dicData'])
          break
        case 'tree':
          this.arr = arr.concat(['multiple', 'tags', 'checkStrictly', 'parent', 'accordion', 'leafOnly'])
          break
        case 'datetime':
          this.arr = arr.concat(['valueFormat', 'format'])
          break
        case 'switch': //暂时没用
          this.arr = ['dataType', 'systemDict', 'busineDict', 'dict']
          break
        default:
          return this.$message({type: item ? 'error' : 'info', message: item ? '表单类型不能为空' : '请选择表单类型'})
      }
      if (isEmpty(item)) this.arr.forEach(item => this.findObject(this.optionForm.column, item).editDisplay = true)
    },
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
    async init(parentId) {
      this.visible = true;

      if (isEmpty(parentId)) return this.$message({type: 'error', message: '缺少parentId'});
      if (this.oldParentId === parentId) return;

      try {
        this.loading = true;
        this.oldParentId = parentId;
        const result = await getDetail(parentId);
        this.form.data = result.data.data.map(item => {
          const {id, modelTableId, fieldType} = item;
          const fieldJson = JSON.parse(item.fieldJson);
          const isChildren = isNotEmpty(fieldJson.children)
          const child = isChildren ? fieldJson.children[0] : fieldJson;
          Object.assign(child, {id, modelTableId, fieldType,isChildren});
          return child;
        });
        this.clone = structuredClone(this.form.data);
        this.ids = [];
        this.arr = [];
        this.loading = false;
      } catch (e) {
        this.loading = false;
        this.$message({type: 'error', message: '获取数据失败'});
      }
    }
  },
  async mounted() {
    this.findObject(this.optionTable.column, 'prop').rules = this.rulesProp
    // this.findObject(this.optionForm.column, 'systemDict').rules = this.rulesDict
    // this.findObject(this.optionForm.column, 'busineDict').rules = this.rulesDict
    // this.findObject(this.optionForm.column, 'dict').rules = this.rulesDictIsNotEmpty
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
