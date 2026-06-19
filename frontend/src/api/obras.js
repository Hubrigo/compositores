import api from './axios'

export const getObras = () => api.get('/api/obras')
export const getObra = (id) => api.get(`/api/obras/${id}`)
export const createObra = (data) => api.post('/api/obras', data)
export const updateObra = (id, data) => api.put(`/api/obras/${id}`, data)
export const deleteObra = (id) => api.delete(`/api/obras/${id}`)
