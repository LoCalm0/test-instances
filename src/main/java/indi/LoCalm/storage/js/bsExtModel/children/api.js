import request from '@/router/axios'

const API = '/api/im-tristone-develop/bsExtModelPro'

export const getColumnJson = id => request.get(`${API}/getColumnJson?id=${id}`)
export const submit = (data,proIds,ids) => request.post(`${API}/submit?proIds=${proIds}&ids=${ids}`, data)
