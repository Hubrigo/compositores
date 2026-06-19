<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-logo">
        <span class="logo-icon">🎵</span>
        <h1 class="logo-title">Compo</h1>
        <p class="logo-sub">Gestión Musical</p>
      </div>

      <AlertMessage :type="alert.type" :message="alert.message" @dismissed="alert.message = ''" />

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="cedula">Cédula</label>
          <input
            id="cedula"
            v-model="form.cedula"
            type="text"
            placeholder="Ingrese su cédula"
            autocomplete="username"
            required
          />
        </div>

        <div class="form-group">
          <label for="password">Contraseña</label>
          <div class="password-wrapper">
            <input
              id="password"
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="Ingrese su contraseña"
              autocomplete="current-password"
              required
            />
            <button type="button" class="toggle-password" @click="showPassword = !showPassword">
              {{ showPassword ? '🙈' : '👁' }}
            </button>
          </div>
        </div>

        <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
          <span v-if="loading" class="btn-spinner"></span>
          {{ loading ? 'Iniciando sesión...' : 'Iniciar Sesión' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/auth.js'
import AlertMessage from '../components/AlertMessage.vue'

const router = useRouter()
const loading = ref(false)
const showPassword = ref(false)

const form = reactive({
  cedula: '',
  password: ''
})

const alert = reactive({
  type: 'error',
  message: ''
})

async function handleLogin() {
  loading.value = true
  alert.message = ''

  try {
    const response = await login({
      cedula: form.cedula,
      password: form.password
    })

    const data = response.data
    const token = data.token || data.accessToken || data.jwt || data.access_token || ''
    const userName = data.nombre || data.name || data.username || data.cedula || form.cedula
    const userRole = data.rol || data.role || ''

    localStorage.setItem('token', token)
    localStorage.setItem('userName', userName)
    localStorage.setItem('userRole', userRole)

    router.push('/dashboard')
  } catch (err) {
    const msg = err.response?.data?.message
      || err.response?.data?.error
      || err.response?.data
      || 'Credenciales incorrectas. Intente de nuevo.'
    alert.type = 'error'
    alert.message = typeof msg === 'string' ? msg : 'Credenciales incorrectas. Intente de nuevo.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 40%, #0f3460 100%);
  padding: 1rem;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: rgba(255, 255, 255, 0.97);
  border-radius: 16px;
  padding: 2.5rem;
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.4);
}

.login-logo {
  text-align: center;
  margin-bottom: 2rem;
}

.logo-icon {
  font-size: 3rem;
  display: block;
  margin-bottom: 0.5rem;
}

.logo-title {
  font-size: 2.2rem;
  font-weight: 800;
  color: #2c3e50;
  letter-spacing: 2px;
  margin-bottom: 0.3rem;
}

.logo-sub {
  color: #7f8c8d;
  font-size: 0.9rem;
  letter-spacing: 1px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.password-wrapper {
  position: relative;
}

.password-wrapper input {
  width: 100%;
  padding-right: 3rem;
}

.toggle-password {
  position: absolute;
  right: 0.8rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  padding: 0;
  line-height: 1;
}

.login-btn {
  width: 100%;
  padding: 0.85rem;
  font-size: 1rem;
  font-weight: 600;
  margin-top: 0.5rem;
  justify-content: center;
  gap: 0.6rem;
  background: linear-gradient(135deg, #3498db, #2980b9);
}

.login-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #2980b9, #1f6aa5);
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.4);
}

.btn-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255,255,255,0.4);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
