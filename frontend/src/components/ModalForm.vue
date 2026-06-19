<template>
  <teleport to="body">
    <transition name="modal-fade">
      <div v-if="show" class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-box" role="dialog" aria-modal="true">
          <div class="modal-header">
            <h2 class="modal-title">{{ title }}</h2>
            <button class="modal-close" @click="$emit('close')" aria-label="Cerrar">&times;</button>
          </div>
          <div class="modal-body">
            <slot />
          </div>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup>
defineProps({
  show: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    default: ''
  }
})

defineEmits(['close'])
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.55);
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}

.modal-box {
  background: var(--white);
  border-radius: 12px;
  width: 100%;
  max-width: 540px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.2rem 1.5rem;
  border-bottom: 1px solid var(--border);
  position: sticky;
  top: 0;
  background: var(--white);
  border-radius: 12px 12px 0 0;
  z-index: 1;
}

.modal-title {
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--primary);
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.6rem;
  line-height: 1;
  color: var(--text-light);
  cursor: pointer;
  padding: 0 0.3rem;
  transition: color 0.2s;
}

.modal-close:hover {
  color: var(--danger);
}

.modal-body {
  padding: 1.5rem;
}

/* Transition */
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.2s ease;
}

.modal-fade-enter-active .modal-box,
.modal-fade-leave-active .modal-box {
  transition: transform 0.2s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-fade-enter-from .modal-box {
  transform: scale(0.95);
}
</style>
