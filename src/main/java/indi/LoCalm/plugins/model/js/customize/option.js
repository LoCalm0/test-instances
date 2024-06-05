export default {
  height: 'auto',
  align: 'center',
  viewTitle: '详细信息',
  menuWidth: 160,
  labelWidth: 120,
  tip: false,
  delBtn: false,
  editBtn: false,
  viewBtn: false,
  columnBtn: false,
  border: true,
  selection: true,
  dialogClickModal: true,
  column: [
    {
      label: '表名',
      prop: 'tableName',
      children: [{
        label: '表名',
        prop: 'tableName',
        type: 'input',
        rules: [{required: true, message: '表名不能为空', trigger: 'blur'}]
      }]
    },
    {
      label: '表备注',
      prop: 'tableRemark',
      children: [{
        label: '表备注',
        prop: 'tableRemark',
        type: 'input',
        rules: [{required: true, message: '表备注不能为空', trigger: 'blur'}]
      }]
    },
    {
      label: '创建人',
      prop: 'createUserName',
      children: [{
        label: '创建人',
        prop: 'createUserName',
        width: 120,
        addDisplay: false,
        editDisplay: false,
        hide: true
      }]
    },
    {
      label: '是否启用',
      prop: 'status',
      children: [{
        label: '是否启用',
        prop: 'status',
        type: 'switch',
        dicData: [],
        dict: {label: 'dictValue', value: 'dictKey', errorMessage: '获取是否启用失败'},
        dataType: 'number',
        value: 1,
        display: false,
        hide: true
      }]
    },
    {
      label: '创建时间',
      prop: 'createTime',
      children: [{
        label: '创建时间',
        prop: 'createTime',
        width: 340,
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        addDisplay: false,
        editDisplay: false,
        hide: true
      }]
    }, {
      label: '修改人',
      prop: 'updateUserName',
      children: [{
        label: '修改人',
        prop: 'updateUserName',
        width: 120,
        addDisplay: false,
        editDisplay: false,
        hide: true
      }]
    }, {
      label: '修改时间',
      prop: 'updateTime',
      children: [{
        label: '修改时间',
        prop: 'updateTime',
        width: 340,
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        addDisplay: false,
        editDisplay: false,
        hide: true
      }]
    }, {
      label: '创建部门',
      prop: 'createDeptName',
      children: [{
        label: '创建部门',
        prop: 'createDeptName',
        width: 120,
        type: 'input',
        addDisplay: false,
        editDisplay: false,
        hide: true
      }]
    }
  ]
}
