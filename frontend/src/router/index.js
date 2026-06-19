import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: () => {
      return localStorage.getItem('token') ? '/dashboard' : '/login'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/DashboardView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/compositores',
    name: 'Compositores',
    component: () => import('../views/CompositoresView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/directores',
    name: 'Directores',
    component: () => import('../views/DirectoresView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/interpretes',
    name: 'Interpretes',
    component: () => import('../views/InterpretesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/obras',
    name: 'Obras',
    component: () => import('../views/ObrasView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/interpretaciones',
    name: 'Interpretaciones',
    component: () => import('../views/InterpretacionesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/interpretaciones/interprete/:id',
    name: 'InterpretacionesPorInterprete',
    component: () => import('../views/InterpretacionesPorInterpreteView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/interpretaciones/director/:id',
    name: 'InterpretacionesPorDirector',
    component: () => import('../views/InterpretacionesPorDirectorView.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
