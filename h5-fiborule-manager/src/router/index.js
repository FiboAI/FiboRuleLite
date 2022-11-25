import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/engineList',
  },
  {
    path: '/',
    name: 'home',
    component: () => import('../views/home.vue'),
    children: [
      {
        path: '/engineList',
        name: 'engineList',
        component: () => import('../views/engineList.vue')
      },
      {
        path: '/engine',
        name: 'engine',
        component: () => import('../views/engine/index.vue')
      },
    ]
  },


]

const router = new VueRouter({
  routes
})

export default router
