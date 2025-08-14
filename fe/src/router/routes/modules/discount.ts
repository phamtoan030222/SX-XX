import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const PROFILE: AppRouteRecordRaw = {
  path: '/discount',
  name: 'Discount',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.discount',
    requiresAuth: true,
    icon: 'icon-file',
    order: 4,
  },
  children: [
    {
      path: 'promotion',
      name: 'Promotion',
      component: () => import('@/views/admin/discount/promotion/index.vue'),
      meta: {
        locale: 'menu.discount.promotion',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'coupon',
      name: 'Coupon',
      component: () => import('@/views/admin/discount/coupon/index.vue'),
      meta: {
        locale: 'menu.discount.coupon',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
  ],
}

export default PROFILE
