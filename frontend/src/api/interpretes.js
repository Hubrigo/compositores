import api from './axios'

export const getInterpretes = () => api.get('/api/interpretes')
export const getInterprete = (id) => api.get(`/api/interpretes/${id}`)
export const createInterprete = (data) => api.post('/api/interpretes', data)
export const updateInterprete = (id, data) => api.put(`/api/interpretes/${id}`, data)
export const deleteInterprete = (id) => api.delete(`/api/interpretes/${id}`)
