import request from '@/router/axios'

const API = '/api/im-tristone-develop/modelTableFields'

export const getDetail = parentId => request.get(`${API}/detail?parentId=${parentId}`)
export const submit = (data,ids) => request.post(`${API}/submit?ids=${ids}`, data)
