import { Component } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: (): Component => import('@renderer/views/login.vue')
  },
  {
    path: '/home',
    component: (): Component => import('@renderer/views/home.vue'),
    children: [
      {
        path: '',
        redirect: '/Songlist'
      },
      {
        path: '/Songlist',
        component: (): Component => import('@renderer/views/song_list.vue')
      },
      {
        path: '/listInfo',
        component: (): Component => import('@renderer/views/list_info.vue')
      },
      {
        path: '/bang',
        component: (): Component => import('@renderer/views/bang.vue')
      },
      {
        path: '/search',
        component: (): Component => import('@renderer/views/search.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
