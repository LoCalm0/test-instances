import request from '@/router/axios'
import {Message, MessageBox} from 'element-ui'
import Vue from 'vue'
import {exportBlob} from '@/api/common'
import {downloadXls} from '@/util/util'

/**
 * 简化dicData获取数据
 * option.column  里需要的参数
 * dicData: [],
 * dict: {label:  'label', value: 'value', errorMessage: '请求错误信息'},   type=tree 不需要 (dict)
 * @author LoCalm
 * @param method 接口函数 或 url ('/api/im-tristone-system/dict/dictionary?code=yes_no')
 * @param column 列数据
 * @return column
 */
export const getDicData = async (method, column) => {
  if (!(method instanceof Function || method instanceof Promise || '[object String]' === getType(method))) return console.error('getDicData方法 第一个参数错误')
  const run = '[object String]' === getType(method) ? () => request.get(method) : method
  try {
    const {config: {url}, data: {data}} = run instanceof Function ? await run() : await run
    if (column.children && column.children.length)        setDicData(column.children[0], data)
    else if (column instanceof Array) {
      if ('/api/im-tristone-system/dict-biz/dict?code' === url.split('=')[0]) {
        for (const [index, item] of url.split('=')[1].split(',').entries()) column[index].children[0].dicData = assembleDict(data[item], column[index].children[0])
      } else {
        column.forEach(item => {
          if (item.dicData)                               setDicData(item, data)
          else if (item.children && item.children.length) setDicData(item.children[0], data)
        })
      }
    } else                                                setDicData(column, data)
    return column.children ? column.children[0] : column instanceof Array ? column.map(item => item.children[0]) : column
  } catch (e) {
    if (column.dict && column.dict.errorMessage) return Message({type: 'error', message: column.dict.errorMessage})
    else if (column.children[0].dict.errorMessage) return Message({type: 'error', message: column.children[0].dict.errorMessage})
  }
}
/**
 * 设置dicData数据
 * @author LoCalm
 * @param column 列
 * @param data   数据
 */
const setDicData = (column, data) => {
  if (column.type === 'switch') column.inactiveColor='#ff4949';column.activeColor='#13ce66'
  if (data instanceof Array) {
    if (column.type === 'tree') column.dicData = data
    else column.dicData = assembleDict(data,column)
  } else column.dicData = assembleDict(data.records,column)
}
/**
 * 组装字典
 * @author LoCalm
 * @param data   数据
 * @param column 列
 * @return 字典
 */
const assembleDict = (data, column) => {
  return data.map(item => {
    // let label = '' //支持拼接label   label:'test,demo',separator:' - '      test - demo
    // const separator = column.dict.separator ? column.dict.separator.split(',') : []
    // column.dict.label.split(',').forEach((item, index) => {
    //   label=label.concat(item + (separator[index] ? separator[index] : ''))
    // })
    // return {label: item[label], value: item[column.dict.value]}
    return {label: item[column.dict.label], value: item[column.dict.value]}
  })
}
/**
 * 获取类型
 * @author LoCalm
 * @param type 传入值
 * @returns {string}  返回类型
 */
const getType = type => {
  return Object.prototype.toString.call(type)
}
/**
 * 去除第二行表头显示(固定、排序、过滤)
 * @author LoCalm
 * @param crud     this.$refs.crud
 */
export const hideHeaderColumn = crud => {
  Vue.nextTick(() => {
    crud.$refs.table.$refs.tableHeader.columns.forEach(item => {
      item.fixed = false
      item.sortable = false
      item.filterable = false
    })
  })
}
/**
 * 修改query对象下的数组
 * 假设createTime数组长度为2 会改为
 * createTime_ge=createTime[0]
 * createTime_le=createTime[1]
 * (原数组会删除)
 * @author LoCalm
 * @param query    this.query
 */
export const setQueryArray = query => {
  for (let key in query) {
    if (query[key] instanceof Array && query[key].length === 2) {
      query[`${key}_ge`] = query[key][0]
      query[`${key}_le`] = query[key][1]
      delete query[key]
    }
  }
}
/**
 * 导出数据
 * @author LoCalm
 * @param ExportUrl  接口地址
 * @param filename   文件名称
 * @param params     查询参数
 */
export const exportUtil = async (ExportUrl, filename, params) => {
  await MessageBox.confirm('是否导出数据?', '提示', {type: 'warning'})
  try {
    const {data} = await exportBlob(ExportUrl, params)
    downloadXls(data, filename)
    Message.success('导出成功')
  } catch (error) {
    Message.error('导出失败')
  }
}
/**
 * 判断对象是否相等 空值不判断
 * @author LoCalm
 * @param obj1 对象1
 * @param obj2 对象2
 * @param exclude 对象1排除值
 * @returns {boolean}
 */
export const isObjectEqual = (obj1,obj2,exclude) => {
  obj1=structuredClone(obj1)
  obj2=structuredClone(obj2)
  if (exclude) {
    for (let key of exclude.split(',')) delete obj1[key]
  }
  deleteEmptyElements(obj1)
  deleteEmptyElements(obj2)
  const obj1Keys = Object.keys(obj1)
  const obj2Keys = Object.keys(obj2)
  if (obj1Keys.length !== obj2Keys.length) return false
  for (let key of obj1Keys) {
    if (obj1[key] !== obj2[key]) return false
  }
  return true
}
/**
 * 判断是否为空
 * @author LoCalm
 * @param obj 判断值
 * @returns {boolean}
 */
export const isEmpty = obj => {
  switch (getType(obj)) {
    case '[object Null]':
      return true
    case '[object Undefined]':
      return true
    case '[object Number]':
      return false
    case '[object Boolean]':
      return false
    case '[object String]':
      return Boolean(obj.trim()) === false
    case '[object Array]':
      if (obj.length === 0) return true
      for (let value of obj.values()) {
        if (isNotEmpty(value)) return false
      }
      return true
    case '[object Object]':
      if (Object.keys(obj).length === 0) return true
      for (let value of Object.values(obj)) {
        if (isNotEmpty(value)) return false
      }
      return true
    default:
      return false
  }
}
/**
 * 判断是否为非空
 * @author LoCalm
 * @param obj 判断值
 * @returns {boolean}
 */
export const isNotEmpty = obj => {
  return false === isEmpty(obj);
}
/**
 * 删除空元素
 * @author LoCalm
 * @param obj     对象
 */
export const deleteEmptyElements = obj => {
  for (let key of Object.keys(obj)) {
    if (isEmpty(obj[key])) delete obj[key]
  }
}
