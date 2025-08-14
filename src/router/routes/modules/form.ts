import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const FORM: AppRouteRecordRaw = {
  path: '/form',
  name: 'Form',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.form',
    icon: 'icon-settings',
    requiresAuth: true,
    order: 3,
  },
  children: [
    {
      path: 'product',
      name: 'Product',
      component: () => import('@/views/admin/products/product/index.vue'),
      meta: {
        locale: 'menu.form.product',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'color',
      name: 'Color',
      component: () => import('@/views/admin/products/color/index.vue'),
      meta: {
        locale: 'menu.form.color',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'ram',
      name: 'Ram',
      component: () => import('@/views/admin/products/ram/index.vue'),
      meta: {
        locale: 'menu.form.ram',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'storage',
      name: 'Storage',
      component: () => import('@/views/admin/products/storage/index.vue'),
      meta: {
        locale: 'menu.form.storage',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'material',
      name: 'Material',
      component: () => import('@/views/admin/products/material/index.vue'),
      meta: {
        locale: 'menu.form.material',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'gpu',
      name: 'GPU',
      component: () => import('@/views/admin/products/gpu/index.vue'),
      meta: {
        locale: 'menu.form.gpu',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'cpu',
      name: 'CPU',
      component: () => import('@/views/admin/products/cpu/index.vue'),
      meta: {
        locale: 'menu.form.cpu',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'screen',
      name: 'Screen',
      component: () => import('@/views/admin/products/screen/index.vue'),
      meta: {
        locale: 'menu.form.screen',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'brand',
      name: 'Brand',
      component: () => import('@/views/admin/products/brand/index.vue'),
      meta: {
        locale: 'menu.form.brand',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'operating',
      name: 'OperatingSystem',
      component: () => import('@/views/admin/products/operating/index.vue'),
      meta: {
        locale: 'menu.form.operating',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
  ],
}

export default FORM
