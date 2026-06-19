<template>
  <div class="page">
    <div class="container">
      <div class="page-header">
        <h1>🎼 Compositores</h1>
        <button class="btn btn-primary" @click="openCreate">+ Nuevo Compositor</button>
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
        </template>
      </DataTable>
    </div>

    <ModalForm :show="showModal" :title="editingItem ? 'Editar Compositor' : 'Nuevo Compositor'" @close="closeModal">
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
import { getCompositores, createCompositor, updateCompositor, deleteCompositor } from '../api/compositores.js'

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
    const res = await getCompositores()
    const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? res.data?.data ?? [])
    items.value = data.filter(c => c.activo !== false)
  } catch {
    alert.type = 'error'
    alert.message = 'Error al cargar los compositores.'
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
      await updateCompositor(editingItem.value.id, { ...form })
      alert.type = 'success'
      alert.message = 'Compositor actualizado correctamente.'
    } else {
      await createCompositor({ ...form })
      alert.type = 'success'
      alert.message = 'Compositor creado correctamente.'
    }
    closeModal()
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al guardar el compositor.'
  } finally {
    submitting.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`¿Eliminar a ${item.nombre} ${item.apellido}? Esta acción no se puede deshacer.`)) return
  try {
    await deleteCompositor(item.id)
    alert.type = 'success'
    alert.message = 'Compositor eliminado correctamente.'
    await loadData()
  } catch (err) {
    alert.type = 'error'
    alert.message = err.response?.data?.message || 'Error al eliminar el compositor.'
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
