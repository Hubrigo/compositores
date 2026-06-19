import api from './axios'

export const getInterpretaciones = () => api.get('/api/interpretaciones')
export const getInterpretacion = (id) => api.get(`/api/interpretaciones/${id}`)
export const createInterpretacion = (data) => api.post('/api/interpretaciones', data)
export const updateInterpretacion = (id, data) => api.put(`/api/interpretaciones/${id}`, data)
export const deleteInterpretacion = (id) => api.delete(`/api/interpretaciones/${id}`)
export const getInterpretacionesPorInterprete = (interpreteid) => api.get(`/api/interpretaciones/interprete/${interpreteid}`)
export const getInterpretacionesPorDirector = (directorId) => api.get(`/api/interpretaciones/director/${directorId}`)
