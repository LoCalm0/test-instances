import request from '@/router/axios'

const API = '/api/im-tristone-develop/modelTable'

export const getPage = params => request.get(`${API}/list`, {params})
export const getDetail = id => request.get(`${API}/detail?id=${id}`)
export const submit = data => request.post(`${API}/submit`, data)
export const remove = ids => request.post(`${API}/remove?ids=${ids}`)
