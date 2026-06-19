<template>
  <div id="app-root">
    <Navbar v-if="isAuthenticated" />
    <main :class="{ 'with-nav': isAuthenticated }">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import Navbar from './components/Navbar.vue'

const route = useRoute()

const isAuthenticated = ref(!!localStorage.getItem('token'))

// localStorage no es reactivo: actualizamos el ref en cada cambio de ruta
watch(() => route.path, () => {
  isAuthenticated.value = !!localStorage.getItem('token')
})
</script>

<style>
*, *::before, *::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

:root {
  --primary: #2c3e50;
  --accent: #3498db;
  --success: #27ae60;
  --danger: #e74c3c;
  --warning: #f39c12;
  --light: #ecf0f1;
  --dark: #1a1a2e;
  --text: #333333;
  --text-light: #666666;
  --border: #dde1e7;
  --bg: #f5f6fa;
  --white: #ffffff;
  --nav-height: 64px;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
  color: var(--text);
  background: var(--bg);
  min-height: 100vh;
}

#app-root {
  min-height: 100vh;
}

main {
  min-height: 100vh;
}

main.with-nav {
  padding-top: var(--nav-height);
  min-height: calc(100vh - var(--nav-height));
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

/* Buttons */
.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.5rem 1.1rem;
  border: none;
  border-radius: 6px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  text-decoration: none;
  white-space: nowrap;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: var(--accent);
  color: var(--white);
}
.btn-primary:hover:not(:disabled) {
  background: #2980b9;
}

.btn-danger {
  background: var(--danger);
  color: var(--white);
}
.btn-danger:hover:not(:disabled) {
  background: #c0392b;
}

.btn-secondary {
  background: #95a5a6;
  color: var(--white);
}
.btn-secondary:hover:not(:disabled) {
  background: #7f8c8d;
}

.btn-success {
  background: var(--success);
  color: var(--white);
}
.btn-success:hover:not(:disabled) {
  background: #219a52;
}

.btn-sm {
  padding: 0.3rem 0.7rem;
  font-size: 0.8rem;
}

/* Forms */
.form-group {
  margin-bottom: 1.2rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.4rem;
  font-weight: 500;
  font-size: 0.9rem;
  color: var(--text);
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 0.6rem 0.9rem;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 0.95rem;
  color: var(--text);
  background: var(--white);
  transition: border-color 0.2s ease;
  outline: none;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: var(--accent);
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.15);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-group select[multiple] {
  min-height: 120px;
}

/* Cards */
.card {
  background: var(--white);
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 1.5rem;
}

/* Page header */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.page-header h1 {
  font-size: 1.6rem;
  color: var(--primary);
  font-weight: 700;
}

/* Spinner */
.spinner {
  display: inline-block;
  width: 36px;
  height: 36px;
  border: 3px solid var(--border);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem;
}

/* Badges */
.badge {
  display: inline-block;
  padding: 0.2rem 0.6rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
}

.badge-success {
  background: #d5f5e3;
  color: #1e8449;
}

.badge-danger {
  background: #fadbd8;
  color: #c0392b;
}

/* Responsive */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
