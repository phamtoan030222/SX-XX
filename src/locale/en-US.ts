import localeMessageBox from '@/components/message-box/locale/en-US'
import localeLogin from '@/views/login/locale/en-US'

import localeWorkplace from '@/views/admin/dashboard/workplace/locale/en-US'
/** simple */

import localeCardList from '@/views/admin/orders/card/locale/en-US'
import localeSearchTable from '@/views/admin/orders/search-table/locale/en-US'

import localeGroupForm from '@/views/admin/products/product/locale/en-US'
import localeImel from '@/views/admin/products/imel/locale/en-US'
import localeColor from '@/views/admin/products/color/locale/en-US'
import localeRam from '@/views/admin/products/ram/locale/en-US'
import localeStorage from '@/views/admin/products/storage/locale/en-US'
import localeMaterial from '@/views/admin/products/material/locale/en-US'
import localeGPU from '@/views/admin/products/gpu/locale/en-US'
import localeCPU from '@/views/admin/products/cpu/locale/en-US'
import localeScreen from '@/views/admin/products/screen/locale/en-US'
import localeBrand from '@/views/admin/products/brand/locale/en-US'
import localeBattery from '@/views/admin/products/battery/locale/en-US'
import localeOperating from '@/views/admin/products/operating/locale/en-US'

import localePromotion from '@/views/admin/discount/promotion/locale/en-US'
import localeCoupon from '@/views/admin/discount/coupon/locale/en-US'

import localeDataAnalysis from '@/views/admin/visualization/data-analysis/locale/en-US'

import localeClient from '@/views/admin/users/client/locale/en-US'
import localeStaff from '@/views/admin/users/staff/locale/en-US'

import locale403 from '@/views/exception/403/locale/en-US'
import locale404 from '@/views/exception/404/locale/en-US'
import locale500 from '@/views/exception/500/locale/en-US'

import localeUserInfo from '@/views/user/info/locale/en-US'
import localeUserSetting from '@/views/user/setting/locale/en-US'
/** simple end */
import localeSettings from './en-US/settings'

export default {
  'menu.dashboard': 'Dashboard',
  'menu.server.dashboard': 'Dashboard-Server',
  'menu.server.workplace': 'Workplace-Server',
  'menu.server.monitor': 'Monitor-Server',
  'menu.list': 'List',
  'menu.result': 'Result',
  'menu.exception': 'Exception',
  'menu.form': 'Form',
  'menu.profile': 'Profile',
  'menu.visualization': 'Data Visualization',
  'menu.user': 'User Center',
  'menu.arcoWebsite': 'Arco Design',
  'menu.faq': 'FAQ',
  'navbar.docs': 'Docs',
  'navbar.action.locale': 'Switch to English',
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
