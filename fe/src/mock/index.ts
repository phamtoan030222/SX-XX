import Mock from 'mockjs'

import './user'
import './message-box'

import '@/views/admin/dashboard/workplace/mock'
/** simple */

import '@/views/admin/orders/card/mock'
import '@/views/admin/orders/search-table/mock'

import '@/views/admin/products/product/mock'

import '@/views/admin/discount/coupon/mock'

import '@/views/admin/visualization/data-analysis/mock'

import '@/views/user/info/mock'
import '@/views/user/setting/mock'
/** simple end */

Mock.setup({
  timeout: '600-1000',
})
