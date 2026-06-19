<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <router-link to="/dashboard" class="navbar-brand">
        <span class="brand-icon">🎵</span>
        <span class="brand-name">Compo</span>
      </router-link>

      <button class="navbar-toggle" @click="menuOpen = !menuOpen" aria-label="Toggle menu">
        <span></span>
        <span></span>
        <span></span>
      </button>

      <div class="navbar-menu" :class="{ open: menuOpen }">
        <router-link to="/dashboard" class="nav-link" @click="menuOpen = false">
          Dashboard
        </router-link>
        <router-link to="/compositores" class="nav-link" @click="menuOpen = false">
          Compositores
        </router-link>
        <router-link to="/directores" class="nav-link" @click="menuOpen = false">
          Directores
        </router-link>
        <router-link to="/interpretes" class="nav-link" @click="menuOpen = false">
          Intérpretes
        </router-link>
        <router-link to="/obras" class="nav-link" @click="menuOpen = false">
          Obras
        </router-link>
        <router-link to="/interpretaciones" class="nav-link" @click="menuOpen = false">
          Interpretaciones
        </router-link>
      </div>

      <div class="navbar-user">
        <span class="user-name" v-if="userName">👤 {{ userName }}</span>
        <button class="btn btn-logout" @click="logout">Cerrar Sesión</button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const menuOpen = ref(false)

const userName = computed(() => localStorage.getItem('userName') || '')

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('userName')
  localStorage.removeItem('userRole')
  router.push('/login')
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: #1a1a2e;
  height: var(--nav-height);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.navbar-inner {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 1.5rem;
  gap: 1rem;
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #fff;
  font-weight: 700;
  font-size: 1.3rem;
  flex-shrink: 0;
}

.brand-icon {
  font-size: 1.4rem;
}

.brand-name {
  color: #3498db;
  letter-spacing: 1px;
}

.navbar-menu {
  display: flex;
  align-items: center;
  gap: 0.2rem;
  flex: 1;
}

.nav-link {
  color: #bdc3c7;
  text-decoration: none;
  padding: 0.5rem 0.8rem;
  border-radius: 6px;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.nav-link:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.1);
}

.nav-link.router-link-active {
  color: #3498db;
  background: rgba(52, 152, 219, 0.15);
}

.navbar-user {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  flex-shrink: 0;
}

.user-name {
  color: #bdc3c7;
  font-size: 0.85rem;
  white-space: nowrap;
}

.btn-logout {
  padding: 0.4rem 0.9rem;
  background: transparent;
  border: 1px solid #e74c3c;
  color: #e74c3c;
  border-radius: 6px;
  font-size: 0.8rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.btn-logout:hover {
  background: #e74c3c;
  color: #fff;
}

.navbar-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  margin-left: auto;
}

.navbar-toggle span {
  display: block;
  width: 24px;
  height: 2px;
  background: #fff;
  border-radius: 2px;
  transition: all 0.2s ease;
}

@media (max-width: 900px) {
  .navbar-inner {
    flex-wrap: wrap;
    height: auto;
    padding: 0.75rem 1rem;
    position: relative;
  }

  .navbar {
    height: auto;
  }

  main.with-nav {
    padding-top: 56px;
  }

  .navbar-toggle {
    display: flex;
  }

  .navbar-menu {
    display: none;
    flex-direction: column;
    align-items: flex-start;
    width: 100%;
    padding: 0.5rem 0;
    gap: 0.2rem;
  }

  .navbar-menu.open {
    display: flex;
  }

  .navbar-user {
    display: none;
  }

  .navbar-menu.open + .navbar-user {
    display: flex;
    width: 100%;
    padding: 0.5rem 0;
    border-top: 1px solid rgba(255,255,255,0.1);
  }

  .nav-link {
    width: 100%;
    padding: 0.6rem 0.8rem;
  }
}
</style>
