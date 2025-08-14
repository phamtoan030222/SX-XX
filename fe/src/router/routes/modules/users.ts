import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const RESULT: AppRouteRecordRaw = {
  path: '/users',
  name: 'users',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.users',
    icon: 'icon-check-circle',
    requiresAuth: true,
    order: 5,
  },
  children: [
    {
      path: 'client',
      name: 'Client',
      component: () => import('@/views/admin/users/client/index.vue'),
      meta: {
        locale: 'menu.users.client',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'staff',
      name: 'Staff',
      component: () => import('@/views/admin/users/staff/index.vue'),
      meta: {
        locale: 'menu.users.staff',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
  ],
}

export default RESULT
