import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/app',
  },
  {
    path: '/',
    name: 'home',
    component: () => import('../views/home.vue'),
    children: [
      {
        path: '/engineList/:appId',
        name: 'engineList',
        meta: { title: '引擎列表' },
        component: () => import('../views/engineList.vue')
      },
      {
        path: '/engine/:engineId',
        name: 'engine',
        meta: { },
        component: () => import('../views/engine/index.vue')
      },
      {
        path: '/app',
        name: 'app',
        meta: { title: 'APP列表' },
        component: () => import('../views/appList.vue')
      },
    ]
  },


]

const router = new VueRouter({
  routes
})

export default router
