<template>
  <div class="page">
    <div class="container">
      <div class="page-header">
        <h1>📜 Obras</h1>
        <button class="btn btn-primary" @click="openCreate">+ Nueva Obra</button>
      </div>

      <AlertMessage :type="alert.type" :message="alert.message" @dismissed="alert.message = ''" />

      <DataTable :columns="columns" :items="items" :loading="loading">
        <template #cell-compositores="{ item }">
          <span class="composers-list">
            {{ formatCompositores(item.compositores) }}
          </span>
        </template>
        <template #cell-activo="{ value }">
          <span :class="value ? 'badge badge-success' : 'badge badge-danger'">
            {{ value ? 'Sí' : 'No' }}
          </span>
        </template>
        <template #actions="{ item }">
          <button class="btn btn-primary btn-sm" @click="openEdit(item)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="handleDelete(item)">Eliminar</button>
          <router-link to="/interpretaciones" class="btn btn-secondary btn-sm">
            Interpretaciones
          </router-link>
        </template>
      </DataTable>
    </div>

    <ModalForm
      :show="showModal"
      :title="editingItem ? 'Editar Obra' : 'Nueva Obra'"
      @close="closeModal"
    >
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>Título *</label>
          <input v-model="form.titulo" type="text" required placeholder="Título de la obra" />
        </div>
        <div class="form-group">
          <label>Descripción</label>
          <textarea v-model="form.descripcion" placeholder="Descripción de la obra"></textarea>
        </div>
        <div class="form-group">
          <label>Género *</label>
          <input v-model="form.genero" type="text" required placeholder="Ej: Sinfonía, Ópera, Concierto..." />
        </div>
        <div class="form-group">
          <label>Fecha de Creación *</label>
          <input v-model="form.fechaCreacion" type="date" required />
        </div>
        <div class="form-group">
          <label>Compositores</label>
          <div class="checkbox-list" v-if="compositoresDisponibles.length">
            <label
              v-for="comp in compositoresDisponibles"
              :key="comp.id"
              class="checkbox-item"
            >
              <input
                type="checkbox"
                :value="comp.id"
                v-model="form.compositoresIds"
              />
              {{ comp.nombre }} {{ comp.apellido }}
            </label>
          </div>
          <p v-else class="text-muted">Cargando compositores...</p>
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
import { ref, reactive, onMounted } from 'vue'
import DataTable from '../components/DataTable.vue'
import ModalForm from '../components/ModalForm.vue'
import AlertMessage from '../components/AlertMessage.vue'
import { getObras, createObra, updateObra, deleteObra } from '../api/obras.js'
import { getCompositores } from '../api/compositores.js'

const columns = [
  { key: 'titulo', label: 'Título' },
  { key: 'genero', label: 'Género' },
  { key: 'fechaCreacion', label: 'Fecha Creación' },
  { key: 'compositores', label: 'Compositores' },
  { key: 'activo', label: 'Activo' }
]

const items = ref([])
const loading = ref(false)
const showModal = ref(false)
const editingItem = ref(null)
const submitting = ref(false)
const compositoresDisponibles = ref([])

const alert = reactive({ type: 'success', message: '' })

const form = reactive({
  titulo: '',
  descripcion: '',
  genero: '',
  fechaCreacion: '',
  compositoresIds: []
})

function resetForm() {
  form.titulo = ''
  form.descripcion = ''
  form.genero = ''
  form.fechaCreacion = ''
  form.compositoresIds = []
}

function formatCompositores(compositores) {
  if (!compositores || !Array.isArray(compositores) || compositores.length === 0) return '—'
  return compositores.map(c => `${c.nombre} ${c.apellido}`).join(', ')
}

async function loadData() {
  loading.value = true
  try {
    const res = await getObras()
    const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? res.data?.data ?? [])
    items.value = data.filter(o => o.activo !== false)
  } catch {
    alert.type = 'error'
    alert.message = 'Error al cargar las obras.'
  } finally {
    loading.value = false
  }
}

async function loadCompositores() {
  try {
    const res = await getCompositores()
    const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? res.data?.data ?? [])
    compositoresDisponibles.value = data.filter(c => c.activo !== false)
  } catch {
    compositoresDisponibles.value = []
  }
}

function openCreate() {
  editingItem.value = null
  resetForm()
  showModal.value = true
}

function openEdit(item) {
  editingItem.value = item
  form.titulo = item.titulo || ''
  form.descripcion = item.descripcion || ''
  form.genero = item.genero || ''
  form.fechaCreacion = item.fechaCreacion ? item.fechaCreacion.split('T')[0] : ''
  form.compositoresIds = item.compositores
    ? item.compositores.map(c => c.id).filter(Boolean)
    : []
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
      titulo: form.titulo,
      descripcion: form.descripcion,
      genero: form.genero,
      fechaCreacion: form.fechaCreacion,
      compositoresIds: form.compositoresIds
    }

    if (editingItem.value) {
      await updateObra(editingItem.value.id, payload)
      alert.type = 'success'
      alert.message = 'Obra actualizada correctamente.'
    } else {
      await createObra(payload)
      alert.type = 'success'
      alert.message = 'Obra creada correctamente.'
    }
    closeModal()
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al guardar la obra.'
  } finally {
    submitting.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`¿Eliminar la obra "${item.titulo}"? Esta acción no se puede deshacer.`)) return
  try {
    await deleteObra(item.id)
    alert.type = 'success'
    alert.message = 'Obra eliminada correctamente.'
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al eliminar la obra.'
  }
}

onMounted(() => {
  loadData()
  loadCompositores()
})
</script>

<style scoped>
.page {
  padding: 2rem 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.8rem;
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border);
}

.composers-list {
  font-size: 0.85rem;
  color: var(--text-light);
}

.checkbox-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  max-height: 180px;
  overflow-y: auto;
  padding: 0.6rem;
  border: 1px solid var(--border);
  border-radius: 6px;
  background: var(--white);
}

.checkbox-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  cursor: pointer;
  padding: 0.2rem 0;
  color: var(--text);
}

.checkbox-item input[type="checkbox"] {
  width: 16px;
  height: 16px;
  cursor: pointer;
  flex-shrink: 0;
}

.text-muted {
  color: var(--text-light);
  font-size: 0.875rem;
}
</style>
