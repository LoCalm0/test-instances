export default {
  column: [
    {
      label: "输入框占位文本",
      prop: "placeholder",
      type: 'input',
    },
    {
      label: '是否可清空',
      prop: 'clearable',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '最大输入长度',
      prop: 'maxlength',
      type: 'number',
      precision: 0,
    },
    {
      label: '最小输入长度',
      prop: 'minlength',
      type: 'number',
      precision: 0,
    },
    {
      label: '输入框行最大行数',
      prop: 'maxRows',
      type: 'number',
      precision: 0,
    },
    {
      label: '输入框行最小行数',
      prop: 'minRows',
      type: 'number',
      precision: 0,
    },
    {
      label: '输入框行数',
      prop: 'rows',
      type: 'number',
      precision: 0,
    },
    {
      label: '最大值',
      prop: 'max',
      type: 'number',
    },
    {
      label: '最小值',
      prop: 'min',
      type: 'number',
    },
    {
      label: '步长',
      prop: 'step',
      type: 'number',
    },
    // {
    //   label: '显示值',
    //   prop: 'dictLabel',
    //   type: 'input',
    //   placeholder: '多个用逗号分开 ,',
    // },
    // {
    //   label: '选中值',
    //   prop: 'dictValue',
    //   type: 'input',
    //   placeholder: '多个用逗号分开 ,',
    // },
    {
      label: '系统字典',
      prop: 'systemDict',
      type: 'select',
      disabled: false,
      dicData: [],
      dict: {url: '/api/im-tristone-system/dict/select', label: 'dictValue', value: 'code',errorMessage: '获取系统字典失败'},
    },
    {
      label: '业务字典',
      prop: 'busineDict',
      type: 'select',
      disabled: false,
      dicData: [],
      dict: {url: '/api/im-tristone-system/dict-biz/select', label: 'dictValue', value: 'code',errorMessage: '获取业务字典失败'},
    },
    {
      label: '数据来源',
      prop: 'dict',
      type: 'compound',
      span: 24,
      children: [
        {placeholder: 'url (接口)', prop: 'url',width: '40%'},
        {placeholder: '显示值', prop: 'labelDict',width: '20%'},
        {placeholder: '选中值', prop: 'valueDict',width: '20%'},
        {placeholder: '请求错误提示', prop: 'errorMessage',width: '20%'},
      ]
    },
    {
      label: '级联的子节点',
      prop: 'cascader',
      type: 'input',
    },
    {
      label: '级联的默认选项序号',
      prop: 'cascaderIndex',
      type: 'input',
    },
    {
      label: '数据类型',
      prop: 'dataType',
      type: 'select',
      dicData: [],
    },
    {
      label: '是否可搜索',
      prop: 'filterable',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '是否为远程搜索',
      prop: 'remote',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '是否多选',
      prop: 'multiple',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '数据字典值',
      prop: 'dicData',
      type: 'textarea',
      blur: () => {
        console.log("test")
        console.log("test")
      },
      span: 24
    },
    {
      label: '真实值的时间格式',
      prop: 'valueFormat',
      type: 'select',
    },
    {
      label: '显示值时间格式',
      prop: 'format',
      type: 'select',
    },
    {
      label: '多选时是否将选中值按文字的形式展示',
      prop: 'tags',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '在显示复选框的情况下,是否严格的遵循父子不互相关联的做法',
      prop: 'checkStrictly',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '是否可以选择父类',
      prop: 'parent',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '是否每次只打开一个同级树节点展开',
      prop: 'accordion',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: '子类全选时勾选值不包含父类',
      prop: 'leafOnly',
      type: 'switch',
      dicData: [],
      dataType: 'number',
    },
    {
      label: "表单验证规则",
      prop: "rules",
      span: 24,
      order: -1,
      type: "textarea",
    },
  ]
}
