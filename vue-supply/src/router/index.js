import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import system from '@/router/modules/system'
import job from '@/router/modules/job'
import message from '@/router/modules/message'
import bpm from '@/router/modules/bpm'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/home-page',
    children: [
      {
        path: '/home-page',
        name: 'homePage',
        component: () => import('@/views/home-page'),
        meta: { title: '首页' }
      },
      {
        path: '/table',
        name: 'table',
        component: () => import('@/views/example/table'),
        meta: { title: 'Table' }
      },
      {
        path: '/form',
        name: 'Form',
        component: () => import('@/views/example/form'),
        meta: { title: 'Form' }
      },
      ...system,
      ...job,
      ...message,
      ...bpm
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
