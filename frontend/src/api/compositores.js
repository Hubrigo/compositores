import api from './axios'

export const getCompositores = () => api.get('/api/compositores')
export const getCompositor = (id) => api.get(`/api/compositores/${id}`)
export const createCompositor = (data) => api.post('/api/compositores', data)
export const updateCompositor = (id, data) => api.put(`/api/compositores/${id}`, data)
export const deleteCompositor = (id) => api.delete(`/api/compositores/${id}`)
