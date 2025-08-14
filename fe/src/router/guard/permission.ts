import NProgress from 'nprogress' // progress bar
import type { Router, RouteRecordNormalized } from 'vue-router'

import usePermission from '@/hooks/permission'
import { useAppStore, useUserStore } from '@/store'
import { NOT_FOUND, WHITE_LIST } from '../constants'
import { appRoutes } from '../routes'

export default function setupPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const appStore = useAppStore()
    const userStore = useUserStore()
    const Permission = usePermission()
    const permissionsAllow = Permission.accessRouter(to)
    if (appStore.menuFromServer) {
      if (!appStore.appAsyncMenus.length && !WHITE_LIST.find((el) => el.name === to.name)) {
        await appStore.fetchServerMenuConfig()
      }
      const serverMenuConfig = [...appStore.appAsyncMenus, ...WHITE_LIST]

      let exist = false
      while (serverMenuConfig.length && !exist) {
        const element = serverMenuConfig.shift()
        if (element?.name === to.name) exist = true

        if (element?.children) {
          serverMenuConfig.push(...(element.children as unknown as RouteRecordNormalized[]))
        }
      }
      if (exist && permissionsAllow) {
        next()
      } else next(NOT_FOUND)
    } else {
      // eslint-disable-next-line no-lonely-if
      if (permissionsAllow) next()
      else {
        const destination = Permission.findFirstPermissionRoute(appRoutes, userStore.role) || NOT_FOUND
        next(destination)
      }
    }
    NProgress.done()
  })
}
