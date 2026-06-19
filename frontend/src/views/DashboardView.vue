<template>
  <div class="dashboard-page">
    <div class="container">
      <div class="dashboard-header">
        <div>
          <h1 class="welcome-title">Bienvenido, {{ userName }} 👋</h1>
          <p class="welcome-sub">Panel principal de Compo - Gestión Musical</p>
        </div>
      </div>

      <div class="stats-grid">
        <div v-for="stat in stats" :key="stat.key" class="stat-card">
          <div class="stat-icon">{{ stat.icon }}</div>
          <div class="stat-info">
            <div class="stat-count">
              <span v-if="stat.loading" class="stat-spinner"></span>
              <span v-else>{{ stat.count }}</span>
            </div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
          <router-link :to="stat.route" class="stat-link btn btn-primary btn-sm">
            Ver todos
          </router-link>
        </div>
      </div>

      <div class="quick-actions">
        <h2 class="section-title">Acceso Rápido</h2>
        <div class="actions-grid">
          <router-link
            v-for="action in quickActions"
            :key="action.route"
            :to="action.route"
            class="action-card"
          >
            <span class="action-icon">{{ action.icon }}</span>
            <span class="action-label">{{ action.label }}</span>
            <span class="action-arrow">→</span>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { getCompositores } from '../api/compositores.js'
import { getDirectores } from '../api/directores.js'
import { getInterpretes } from '../api/interpretes.js'
import { getObras } from '../api/obras.js'
import { getInterpretaciones } from '../api/interpretaciones.js'

const userName = computed(() => localStorage.getItem('userName') || 'Usuario')

const stats = reactive([
  { key: 'compositores', label: 'Compositores', icon: '🎼', count: 0, loading: true, route: '/compositores' },
  { key: 'directores', label: 'Directores', icon: '🎻', count: 0, loading: true, route: '/directores' },
  { key: 'interpretes', label: 'Intérpretes', icon: '🎤', count: 0, loading: true, route: '/interpretes' },
  { key: 'obras', label: 'Obras', icon: '📜', count: 0, loading: true, route: '/obras' },
  { key: 'interpretaciones', label: 'Interpretaciones', icon: '🎶', count: 0, loading: true, route: '/interpretaciones' }
])

const quickActions = [
  { label: 'Gestionar Compositores', icon: '🎼', route: '/compositores' },
  { label: 'Gestionar Directores', icon: '🎻', route: '/directores' },
  { label: 'Gestionar Intérpretes', icon: '🎤', route: '/interpretes' },
  { label: 'Gestionar Obras', icon: '📜', route: '/obras' },
  { label: 'Gestionar Interpretaciones', icon: '🎶', route: '/interpretaciones' }
]

async function loadStats() {
  const loaders = [
    { key: 'compositores', fn: getCompositores },
    { key: 'directores', fn: getDirectores },
    { key: 'interpretes', fn: getInterpretes },
    { key: 'obras', fn: getObras },
    { key: 'interpretaciones', fn: getInterpretaciones }
  ]

  for (const loader of loaders) {
    const stat = stats.find(s => s.key === loader.key)
    try {
      const res = await loader.fn()
      const data = res.data
      stat.count = Array.isArray(data) ? data.length : (data?.total ?? data?.count ?? 0)
    } catch {
      stat.count = '—'
    } finally {
      stat.loading = false
    }
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard-page {
  padding: 2rem 0;
  min-height: 100%;
}

.dashboard-header {
  margin-bottom: 2rem;
}

.welcome-title {
  font-size: 1.8rem;
  color: var(--primary);
  font-weight: 800;
  margin-bottom: 0.3rem;
}

.welcome-sub {
  color: var(--text-light);
  font-size: 0.95rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1.2rem;
  margin-bottom: 2.5rem;
}

.stat-card {
  background: var(--white);
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 0.6rem;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
}

.stat-icon {
  font-size: 2.5rem;
}

.stat-info {
  flex: 1;
}

.stat-count {
  font-size: 2rem;
  font-weight: 800;
  color: var(--primary);
  line-height: 1;
  margin-bottom: 0.3rem;
  min-height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-label {
  font-size: 0.85rem;
  color: var(--text-light);
  font-weight: 500;
}

.stat-link {
  width: 100%;
  justify-content: center;
}

.stat-spinner {
  display: inline-block;
  width: 24px;
  height: 24px;
  border: 3px solid var(--border);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.section-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--primary);
  margin-bottom: 1rem;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 1rem;
}

.action-card {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  background: var(--white);
  border-radius: 10px;
  padding: 1rem 1.2rem;
  text-decoration: none;
  color: var(--text);
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  border: 2px solid transparent;
  transition: all 0.2s ease;
  font-weight: 500;
}

.action-card:hover {
  border-color: var(--accent);
  color: var(--accent);
  transform: translateX(4px);
}

.action-icon {
  font-size: 1.4rem;
  flex-shrink: 0;
}

.action-label {
  flex: 1;
  font-size: 0.9rem;
}

.action-arrow {
  color: var(--accent);
  font-size: 1.1rem;
}

@media (max-width: 600px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>
