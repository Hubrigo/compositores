<template>
  <div class="page">
    <div class="container">
      <div class="page-header">
        <h1>🎻 Directores</h1>
        <button class="btn btn-primary" @click="openCreate">+ Nuevo Director</button>
      </div>

      <AlertMessage :type="alert.type" :message="alert.message" @dismissed="alert.message = ''" />

      <DataTable :columns="columns" :items="items" :loading="loading">
        <template #cell-activo="{ value }">
          <span :class="value ? 'badge badge-success' : 'badge badge-danger'">
            {{ value ? 'Sí' : 'No' }}
          </span>
        </template>
        <template #actions="{ item }">
          <button class="btn btn-primary btn-sm" @click="openEdit(item)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="handleDelete(item)">Eliminar</button>
          <router-link :to="`/interpretaciones/director/${item.id}`" class="btn btn-secondary btn-sm">
            Interpretaciones
          </router-link>
        </template>
      </DataTable>
    </div>

    <ModalForm :show="showModal" :title="editingItem ? 'Editar Director' : 'Nuevo Director'" @close="closeModal">
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>Nombre *</label>
          <input v-model="form.nombre" type="text" required placeholder="Nombre" />
        </div>
        <div class="form-group">
          <label>Apellido *</label>
          <input v-model="form.apellido" type="text" required placeholder="Apellido" />
        </div>
        <div class="form-group">
          <label>Nacionalidad *</label>
          <input v-model="form.nacionalidad" type="text" required placeholder="Nacionalidad" />
        </div>
        <div class="form-group">
          <label>Fecha de Nacimiento *</label>
          <input v-model="form.fechaNacimiento" type="date" required />
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
import { getDirectores, createDirector, updateDirector, deleteDirector } from '../api/directores.js'

const columns = [
  { key: 'nombre', label: 'Nombre' },
  { key: 'apellido', label: 'Apellido' },
  { key: 'nacionalidad', label: 'Nacionalidad' },
  { key: 'fechaNacimiento', label: 'Fecha Nacimiento' },
  { key: 'activo', label: 'Activo' }
]

const items = ref([])
const loading = ref(false)
const showModal = ref(false)
const editingItem = ref(null)
const submitting = ref(false)

const alert = reactive({ type: 'success', message: '' })

const form = reactive({
  nombre: '',
  apellido: '',
  nacionalidad: '',
  fechaNacimiento: ''
})

function resetForm() {
  form.nombre = ''
  form.apellido = ''
  form.nacionalidad = ''
  form.fechaNacimiento = ''
}

async function loadData() {
  loading.value = true
  try {
    const res = await getDirectores()
    const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? res.data?.data ?? [])
    items.value = data.filter(d => d.activo !== false)
  } catch {
    alert.type = 'error'
    alert.message = 'Error al cargar los directores.'
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingItem.value = null
  resetForm()
  showModal.value = true
}

function openEdit(item) {
  editingItem.value = item
  form.nombre = item.nombre || ''
  form.apellido = item.apellido || ''
  form.nacionalidad = item.nacionalidad || ''
  form.fechaNacimiento = item.fechaNacimiento ? item.fechaNacimiento.split('T')[0] : ''
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
    if (editingItem.value) {
      await updateDirector(editingItem.value.id, { ...form })
      alert.type = 'success'
      alert.message = 'Director actualizado correctamente.'
    } else {
      await createDirector({ ...form })
      alert.type = 'success'
      alert.message = 'Director creado correctamente.'
    }
    closeModal()
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al guardar el director.'
  } finally {
    submitting.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`¿Eliminar a ${item.nombre} ${item.apellido}? Esta acción no se puede deshacer.`)) return
  try {
    await deleteDirector(item.id)
    alert.type = 'success'
    alert.message = 'Director eliminado correctamente.'
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al eliminar el director.'
  }
}

onMounted(loadData)
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
</style>
