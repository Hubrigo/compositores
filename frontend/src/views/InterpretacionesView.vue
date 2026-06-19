<template>
  <div class="page">
    <div class="container">
      <div class="page-header">
        <h1>🎶 Interpretaciones</h1>
        <button class="btn btn-primary" @click="openCreate">+ Nueva Interpretación</button>
      </div>

      <AlertMessage :type="alert.type" :message="alert.message" @dismissed="alert.message = ''" />

      <div class="filter-bar">
        <span class="filter-label">Filtrar por:</span>
        <div class="filter-selects">
          <select v-model="filtroInterpreteId" @change="applyFilter">
            <option value="">Todos los intérpretes</option>
            <option v-for="i in interpretes" :key="i.id" :value="i.id">
              {{ i.nombre }} {{ i.apellido }}
            </option>
          </select>
          <select v-model="filtroDirectorId" @change="applyFilter">
            <option value="">Todos los directores</option>
            <option v-for="d in directores" :key="d.id" :value="d.id">
              {{ d.nombre }} {{ d.apellido }}
            </option>
          </select>
          <button v-if="filtroInterpreteId || filtroDirectorId" class="btn btn-secondary btn-sm" @click="clearFilter">
            Limpiar filtros
          </button>
        </div>
      </div>

      <DataTable :columns="columns" :items="filteredItems" :loading="loading">
        <template #cell-obra="{ item }">
          {{ item.obra?.titulo || item.obraTitulo || '—' }}
        </template>
        <template #cell-interprete="{ item }">
          {{ formatPersona(item.interprete) }}
        </template>
        <template #cell-director="{ item }">
          {{ formatPersona(item.director) }}
        </template>
        <template #cell-fechaInterpretacion="{ value }">
          {{ value ? formatDate(value) : '—' }}
        </template>
        <template #cell-activo="{ value }">
          <span :class="value ? 'badge badge-success' : 'badge badge-danger'">
            {{ value ? 'Sí' : 'No' }}
          </span>
        </template>
        <template #actions="{ item }">
          <button class="btn btn-primary btn-sm" @click="openEdit(item)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="handleDelete(item)">Eliminar</button>
        </template>
      </DataTable>
    </div>

    <ModalForm
      :show="showModal"
      :title="editingItem ? 'Editar Interpretación' : 'Nueva Interpretación'"
      @close="closeModal"
    >
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>Obra *</label>
          <select v-model="form.obraId" required>
            <option value="">Seleccione una obra</option>
            <option v-for="o in obras" :key="o.id" :value="o.id">{{ o.titulo }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Intérprete *</label>
          <select v-model="form.interpreteId" required>
            <option value="">Seleccione un intérprete</option>
            <option v-for="i in interpretes" :key="i.id" :value="i.id">
              {{ i.nombre }} {{ i.apellido }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>Director *</label>
          <select v-model="form.directorId" required>
            <option value="">Seleccione un director</option>
            <option v-for="d in directores" :key="d.id" :value="d.id">
              {{ d.nombre }} {{ d.apellido }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>Fecha de Interpretación *</label>
          <input v-model="form.fechaInterpretacion" type="date" required />
        </div>
        <div class="form-group">
          <label>Lugar *</label>
          <input v-model="form.lugar" type="text" required placeholder="Lugar de la interpretación" />
        </div>
        <div class="form-group">
          <label>Observaciones</label>
          <textarea v-model="form.observaciones" placeholder="Observaciones adicionales"></textarea>
        </div>
        <div class="form-actions">
          <button type="button" class="btn btn-secondary" @click="closeModal">Cancelar</button>
          <button type="submit" class="btn btn-primary" :disabled="submitting">
            {{ submitting ? 'Guardando...' : (editingItem ? 'Actualizar' : 'Crear') }}
          </button>
        </div>
      </form>
    </ModalForm>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import DataTable from '../components/DataTable.vue'
import ModalForm from '../components/ModalForm.vue'
import AlertMessage from '../components/AlertMessage.vue'
import {
  getInterpretaciones,
  createInterpretacion,
  updateInterpretacion,
  deleteInterpretacion,
  getInterpretacionesPorInterprete,
  getInterpretacionesPorDirector
} from '../api/interpretaciones.js'
import { getObras } from '../api/obras.js'
import { getInterpretes } from '../api/interpretes.js'
import { getDirectores } from '../api/directores.js'

const columns = [
  { key: 'obra', label: 'Obra' },
  { key: 'interprete', label: 'Intérprete' },
  { key: 'director', label: 'Director' },
  { key: 'fechaInterpretacion', label: 'Fecha' },
  { key: 'lugar', label: 'Lugar' },
  { key: 'activo', label: 'Activo' }
]

const items = ref([])
const loading = ref(false)
const showModal = ref(false)
const editingItem = ref(null)
const submitting = ref(false)
const obras = ref([])
const interpretes = ref([])
const directores = ref([])
const filtroInterpreteId = ref('')
const filtroDirectorId = ref('')

const alert = reactive({ type: 'success', message: '' })

const form = reactive({
  obraId: '',
  interpreteId: '',
  directorId: '',
  fechaInterpretacion: '',
  lugar: '',
  observaciones: ''
})

function resetForm() {
  form.obraId = ''
  form.interpreteId = ''
  form.directorId = ''
  form.fechaInterpretacion = ''
  form.lugar = ''
  form.observaciones = ''
}

function formatPersona(persona) {
  if (!persona) return '—'
  return `${persona.nombre || ''} ${persona.apellido || ''}`.trim() || '—'
}

function formatDate(dateStr) {
  if (!dateStr) return '—'
  const d = new Date(dateStr)
  if (isNaN(d.getTime())) return dateStr
  return d.toLocaleDateString('es-ES')
}

const filteredItems = computed(() => {
  let result = items.value
  if (filtroInterpreteId.value) {
    result = result.filter(i => {
      const id = i.interprete?.id || i.interpreteId
      return String(id) === String(filtroInterpreteId.value)
    })
  }
  if (filtroDirectorId.value) {
    result = result.filter(i => {
      const id = i.director?.id || i.directorId
      return String(id) === String(filtroDirectorId.value)
    })
  }
  return result
})

async function loadData() {
  loading.value = true
  try {
    const res = await getInterpretaciones()
    const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? res.data?.data ?? [])
    items.value = data.filter(i => i.activo !== false)
  } catch {
    alert.type = 'error'
    alert.message = 'Error al cargar las interpretaciones.'
  } finally {
    loading.value = false
  }
}

async function loadRelaciones() {
  try {
    const [obrasRes, interpRes, dirRes] = await Promise.all([
      getObras(),
      getInterpretes(),
      getDirectores()
    ])
    obras.value = (Array.isArray(obrasRes.data) ? obrasRes.data : (obrasRes.data?.content ?? [])).filter(o => o.activo !== false)
    interpretes.value = (Array.isArray(interpRes.data) ? interpRes.data : (interpRes.data?.content ?? [])).filter(i => i.activo !== false)
    directores.value = (Array.isArray(dirRes.data) ? dirRes.data : (dirRes.data?.content ?? [])).filter(d => d.activo !== false)
  } catch {
    // silently fail
  }
}

async function applyFilter() {
  if (!filtroInterpreteId.value && !filtroDirectorId.value) {
    await loadData()
    return
  }
  loading.value = true
  try {
    if (filtroInterpreteId.value) {
      const res = await getInterpretacionesPorInterprete(filtroInterpreteId.value)
      const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? [])
      items.value = data
    } else if (filtroDirectorId.value) {
      const res = await getInterpretacionesPorDirector(filtroDirectorId.value)
      const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? [])
      items.value = data
    }
  } catch {
    alert.type = 'error'
    alert.message = 'Error al filtrar las interpretaciones.'
  } finally {
    loading.value = false
  }
}

async function clearFilter() {
  filtroInterpreteId.value = ''
  filtroDirectorId.value = ''
  await loadData()
}

function openCreate() {
  editingItem.value = null
  resetForm()
  showModal.value = true
}

function openEdit(item) {
  editingItem.value = item
  form.obraId = item.obra?.id || item.obraId || ''
  form.interpreteId = item.interprete?.id || item.interpreteId || ''
  form.directorId = item.director?.id || item.directorId || ''
  form.fechaInterpretacion = item.fechaInterpretacion ? item.fechaInterpretacion.split('T')[0] : ''
  form.lugar = item.lugar || ''
  form.observaciones = item.observaciones || ''
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  editingItem.value = null
  resetForm()
}

async function handleSubmit() {
  submitting.value = true
  try {
    const payload = {
      obraId: form.obraId,
      interpreteId: form.interpreteId,
      directorId: form.directorId,
      fechaInterpretacion: form.fechaInterpretacion,
      lugar: form.lugar,
      observaciones: form.observaciones
    }

    if (editingItem.value) {
      await updateInterpretacion(editingItem.value.id, payload)
      alert.type = 'success'
      alert.message = 'Interpretación actualizada correctamente.'
    } else {
      await createInterpretacion(payload)
      alert.type = 'success'
      alert.message = 'Interpretación creada correctamente.'
    }
    closeModal()
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al guardar la interpretación.'
  } finally {
    submitting.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm('¿Eliminar esta interpretación? Esta acción no se puede deshacer.')) return
  try {
    await deleteInterpretacion(item.id)
    alert.type = 'success'
    alert.message = 'Interpretación eliminada correctamente.'
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al eliminar la interpretación.'
  }
}

onMounted(() => {
  loadData()
  loadRelaciones()
})
</script>

<style scoped>
.page {
  padding: 2rem 0;
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.2rem;
  flex-wrap: wrap;
  background: var(--white);
  padding: 1rem 1.2rem;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.filter-label {
  font-weight: 600;
  color: var(--primary);
  font-size: 0.9rem;
  white-space: nowrap;
}

.filter-selects {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  flex-wrap: wrap;
  flex: 1;
}

.filter-selects select {
  padding: 0.45rem 0.8rem;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 0.875rem;
  color: var(--text);
  background: var(--white);
  outline: none;
  min-width: 200px;
}

.filter-selects select:focus {
  border-color: var(--accent);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.8rem;
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border);
}

@media (max-width: 600px) {
  .filter-bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-selects {
    width: 100%;
    flex-direction: column;
  }

  .filter-selects select {
    width: 100%;
  }
}
</style>
