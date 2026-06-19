<template>
  <div class="page">
    <div class="container">
      <div class="page-header">
        <div>
          <button class="btn btn-secondary btn-sm back-btn" @click="$router.back()">← Volver</button>
          <h1>
            <span v-if="loadingHeader">Cargando...</span>
            <span v-else>Interpretaciones de {{ interpreteNombre }}</span>
          </h1>
        </div>
      </div>

      <AlertMessage :type="alert.type" :message="alert.message" @dismissed="alert.message = ''" />

      <DataTable :columns="columns" :items="items" :loading="loading">
        <template #cell-obra="{ item }">
          {{ item.obra?.titulo || item.obraTitulo || '—' }}
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
      </DataTable>

      <div v-if="!loading && items.length === 0" class="empty-message">
        <p>No se encontraron interpretaciones para este intérprete.</p>
        <router-link to="/interpretaciones" class="btn btn-primary" style="margin-top: 1rem;">
          Ver todas las interpretaciones
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import DataTable from '../components/DataTable.vue'
import AlertMessage from '../components/AlertMessage.vue'
import { getInterpretacionesPorInterprete } from '../api/interpretaciones.js'
import { getInterprete } from '../api/interpretes.js'

const route = useRoute()
const id = computed(() => route.params.id)

const items = ref([])
const loading = ref(false)
const loadingHeader = ref(true)
const interpreteNombre = ref('')

const alert = reactive({ type: 'error', message: '' })

const columns = [
  { key: 'obra', label: 'Obra' },
  { key: 'director', label: 'Director' },
  { key: 'fechaInterpretacion', label: 'Fecha' },
  { key: 'lugar', label: 'Lugar' },
  { key: 'observaciones', label: 'Observaciones' },
  { key: 'activo', label: 'Activo' }
]

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

async function loadInterprete() {
  loadingHeader.value = true
  try {
    const res = await getInterprete(id.value)
    const data = res.data
    interpreteNombre.value = `${data.nombre || ''} ${data.apellido || ''}`.trim()
  } catch {
    interpreteNombre.value = `Intérprete #${id.value}`
  } finally {
    loadingHeader.value = false
  }
}

async function loadInterpretaciones() {
  loading.value = true
  try {
    const res = await getInterpretacionesPorInterprete(id.value)
    const data = Array.isArray(res.data) ? res.data : (res.data?.content ?? res.data?.data ?? [])
    items.value = data
  } catch {
    alert.type = 'error'
    alert.message = 'Error al cargar las interpretaciones del intérprete.'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadInterprete()
  loadInterpretaciones()
})
</script>

<style scoped>
.page {
  padding: 2rem 0;
}

.back-btn {
  margin-bottom: 0.8rem;
  display: inline-flex;
}

.empty-message {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
