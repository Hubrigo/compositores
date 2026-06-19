<template>
  <transition name="alert-slide">
    <div v-if="visible && message" class="alert" :class="`alert-${type}`" role="alert">
      <span class="alert-icon">{{ icons[type] }}</span>
      <span class="alert-message">{{ message }}</span>
      <button class="alert-dismiss" @click="dismiss" aria-label="Cerrar">&times;</button>
    </div>
  </transition>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: 'success',
    validator: (v) => ['success', 'error', 'warning'].includes(v)
  },
  message: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['dismissed'])

const visible = ref(false)
let timer = null

const icons = {
  success: '✔',
  error: '✖',
  warning: '⚠'
}

watch(() => props.message, (val) => {
  if (val) {
    visible.value = true
    clearTimeout(timer)
    timer = setTimeout(() => {
      visible.value = false
      emit('dismissed')
    }, 3000)
  } else {
    visible.value = false
  }
}, { immediate: true })

function dismiss() {
  clearTimeout(timer)
  visible.value = false
  emit('dismissed')
}
</script>

<style scoped>
.alert {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.85rem 1.1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  font-weight: 500;
  border-left: 4px solid transparent;
}

.alert-success {
  background: #d5f5e3;
  color: #1e8449;
  border-left-color: #27ae60;
}

.alert-error {
  background: #fadbd8;
  color: #922b21;
  border-left-color: #e74c3c;
}

.alert-warning {
  background: #fef9e7;
  color: #9a7d0a;
  border-left-color: #f39c12;
}

.alert-icon {
  font-size: 1rem;
  flex-shrink: 0;
}

.alert-message {
  flex: 1;
}

.alert-dismiss {
  background: none;
  border: none;
  font-size: 1.2rem;
  line-height: 1;
  cursor: pointer;
  color: inherit;
  opacity: 0.6;
  padding: 0;
  flex-shrink: 0;
}

.alert-dismiss:hover {
  opacity: 1;
}

/* Transition */
.alert-slide-enter-active,
.alert-slide-leave-active {
  transition: all 0.25s ease;
}

.alert-slide-enter-from,
.alert-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
