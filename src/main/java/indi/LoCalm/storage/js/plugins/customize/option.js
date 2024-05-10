export default {
  height: 'auto',
  align: 'center',
  viewTitle: '详细信息',
  saveBtnText: '下一步',
  updateBtnText: '下一步',
  menuWidth: 130,
  labelWidth: 120,
  tip: false,
  delBtn: false,
  editBtn: false,
  viewBtn: false,
  columnBtn: false,
  border: true,
  excelBtn: true,
  selection: true,
  dialogClickModal: true,
  column: [
    // {
    //   label: '功能菜单',
    //   prop: 'basicTableName',
    //   children: [{
    //     label: '功能菜单',
    //     prop: 'basicTableName',
    //     type: 'select',
    //     dicData: [],
    //     dict: {label: 'name', value: 'basicTableName', errorMessage: '获取基础表名失败'},
    //     rules: [{required: true, message: '功能菜单为空', trigger: 'change'}],
    //   }]
    // }, {
    //   label: '业务名称',
    //   prop: 'businessCode',
    //   children: [{
    //     label: '业务名称',
    //     prop: 'businessCode',
    //     type: 'select',
    //     dicData: [],
    //     dict: {label: 'dictValue', value: 'dictKey', errorMessage: '获取业务名称失败'},
    //     rules: [{required: true, message: '业务名称为空', trigger: 'change'}],
    //   }]
    // },
    {
      label: '模型名称',
      prop: 'extModelName',
      children: [{
        label: '模型名称',
        prop: 'extModelName',
        type: 'input',
        rules: [{required: true, message: '模型名称为空', trigger: 'blur'}]
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
        value: 1
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
    }, {
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
