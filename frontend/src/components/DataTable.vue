<template>
  <div class="table-wrapper">
    <div v-if="loading" class="loading-wrapper">
      <div class="spinner"></div>
    </div>

    <div v-else-if="items.length === 0" class="empty-state">
      <p>No hay datos disponibles.</p>
    </div>

    <div v-else class="table-scroll">
      <table class="data-table">
        <thead>
          <tr>
            <th v-for="col in columns" :key="col.key">{{ col.label }}</th>
            <th v-if="$slots.actions" class="actions-col">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in items" :key="item.id || index">
            <td v-for="col in columns" :key="col.key">
              <slot :name="'cell-' + col.key" :item="item" :value="item[col.key]">
                <template v-if="typeof item[col.key] === 'boolean'">
                  <span :class="item[col.key] ? 'badge badge-success' : 'badge badge-danger'">
                    {{ item[col.key] ? 'Sí' : 'No' }}
                  </span>
                </template>
                <template v-else>
                  {{ item[col.key] ?? '—' }}
                </template>
              </slot>
            </td>
            <td v-if="$slots.actions" class="actions-cell">
              <slot name="actions" :item="item" :index="index" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
defineProps({
  columns: {
    type: Array,
    required: true
  },
  items: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})
</script>

<style scoped>
.table-wrapper {
  width: 100%;
  background: var(--white);
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.table-scroll {
  overflow-x: auto;
  width: 100%;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.data-table thead {
  background: var(--primary);
  color: var(--white);
}

.data-table thead th {
  padding: 0.9rem 1rem;
  text-align: left;
  font-weight: 600;
  font-size: 0.85rem;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

.data-table tbody tr {
  border-bottom: 1px solid var(--border);
  transition: background 0.15s ease;
}

.data-table tbody tr:last-child {
  border-bottom: none;
}

.data-table tbody tr:hover {
  background: #f0f4f8;
}

.data-table tbody td {
  padding: 0.75rem 1rem;
  vertical-align: middle;
  color: var(--text);
}

.actions-col {
  width: 1px;
  text-align: center !important;
}

.actions-cell {
  white-space: nowrap;
  display: flex;
  gap: 0.4rem;
  align-items: center;
  flex-wrap: wrap;
}

.empty-state {
  padding: 3rem;
  text-align: center;
  color: var(--text-light);
  font-size: 0.95rem;
}
</style>
