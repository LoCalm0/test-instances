import request from '@/router/axios'

const API = '/api/im-tristone-develop/bsExtModel'

export const getList = params => request.get(`${API}/list`, {params})
export const getDetail = id => request.get(`${API}/detail?id=${id}`)
export const remove = ids => request.post(`${API}/remove?ids=${ids}`)
