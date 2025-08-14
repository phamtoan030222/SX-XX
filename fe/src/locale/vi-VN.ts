import localeMessageBox from '@/components/message-box/locale/vi-VN'
import localeLogin from '@/views/login/locale/vi-VN'

import localeWorkplace from '@/views/admin/dashboard/workplace/locale/vi-VN'
/** simple */

import localeCardList from '@/views/admin/orders/card/locale/vi-VN'
import localeSearchTable from '@/views/admin/orders/search-table/locale/vi-VN'

import localeGroupForm from '@/views/admin/products/product/locale/vi-VN'
import localeImel from '@/views/admin/products/imel/locale/vi-VN'
import localeColor from '@/views/admin/products/color/locale/vi-VN'
import localeRam from '@/views/admin/products/ram/locale/vi-VN'
import localeStorage from '@/views/admin/products/storage/locale/vi-VN'
import localeMaterial from '@/views/admin/products/material/locale/vi-VN'
import localeGPU from '@/views/admin/products/gpu/locale/vi-VN'
import localeCPU from '@/views/admin/products/cpu/locale/vi-VN'
import localeScreen from '@/views/admin/products/screen/locale/vi-VN'
import localeBrand from '@/views/admin/products/brand/locale/vi-VN'
import localeBattery from '@/views/admin/products/battery/locale/vi-VN'
import localeOperating from '@/views/admin/products/operating/locale/vi-VN'

import localePromotion from '@/views/admin/discount/promotion/locale/vi-VN'
import localeCoupon from '@/views/admin/discount/coupon/locale/vi-VN'

import localeDataAnalysis from '@/views/admin/visualization/data-analysis/locale/vi-VN'

import localeClient from '@/views/admin/users/client/locale/vi-VN'
import localeStaff from '@/views/admin/users/staff/locale/vi-VN'

import locale403 from '@/views/exception/403/locale/vi-VN'
import locale404 from '@/views/exception/404/locale/vi-VN'
import locale500 from '@/views/exception/500/locale/vi-VN'

import localeUserInfo from '@/views/user/info/locale/vi-VN'
import localeUserSetting from '@/views/user/setting/locale/vi-VN'
/** simple end */
import localeSettings from './vi-VN/settings'

export default {
  'menu.dashboard': 'Bảng điều khiển',
  'menu.server.dashboard': 'Bảng điều khiển - Máy chủ',
  'menu.server.workplace': 'Bàn làm việc - Máy chủ',
  'menu.server.monitor': 'Giám sát thời gian thực - Máy chủ',
  'menu.list': 'Đơn hàng',
  'menu.users': 'Người dùng',
  'menu.exception': 'Hỗ trợ khách hàng',
  'menu.form': 'Sản phẩm',
  'menu.discount': 'Giảm giá',
  'menu.visualization': 'Thống kê',
  'menu.user': 'Trung tâm cá nhân',
  'menu.arcoWebsite': 'Arco Design',
  'menu.faq': 'Câu hỏi thường gặp',
  'navbar.docs': 'Trung tâm tài liệu',
  'navbar.action.locale': 'Chuyển sang Tiếng Việt',
  ...localeSettings,
  ...localeMessageBox,
  ...localeLogin,
  ...localeWorkplace,
  /** simple */
  ...localeSearchTable,
  ...localeCardList,
  ...localeImel,
  ...localeColor,
  ...localeRam,
  ...localeStorage,
  ...localeMaterial,
  ...localeGPU,
  ...localeCPU,
  ...localeScreen,
  ...localeBrand,
  ...localeBattery,
  ...localeOperating,
  ...localeGroupForm,
  ...localePromotion,
  ...localeCoupon,
  ...localeDataAnalysis,
  ...localeClient,
  ...localeStaff,
  ...locale403,
  ...locale404,
  ...locale500,
  ...localeUserInfo,
  ...localeUserSetting,
  /** simple end */
}
