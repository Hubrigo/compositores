import api from './axios'

export const getDirectores = () => api.get('/api/directores')
export const getDirector = (id) => api.get(`/api/directores/${id}`)
export const createDirector = (data) => api.post('/api/directores', data)
export const updateDirector = (id, data) => api.put(`/api/directores/${id}`, data)
export const deleteDirector = (id) => api.delete(`/api/directores/${id}`)
