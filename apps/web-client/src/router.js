import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('./components/Layout.vue'),
      children: [{
        path: '',
        name: 'home',
        component: () => import('./views/Home.vue'),
      }, {
        path: 'essayDetail/:id',
        name: 'essayDetail',
        component: () => import('./views/EssayDetail.vue'),
        props: true,
      }, {
        path: 'search/:sf',
        name: 'search',
        component: () => import('./views/SearchResult.vue'),
        props: true,
      }],
    },
    {
      path: '/index',
      name: 'index',
      component: () => import('./views/Index.vue'),
    },
    {
      path: '/signin',
      name: 'signin',
      component: () => import('./views/sign-in.vue'),
    },
    {
      path: '/signup',
      name: 'signup',
      component: () => import('./views/sign-up.vue'),
    },
    {
      path: '/create',
      name: 'create',
      meta: { requireAuth: true },
      component: () => import('./views/Create.vue'),
    },
    {
      path: '/404',
      name: '404',
      component: () => import('./views/404.vue'),
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/404',
    },
  ],
})

export default router
