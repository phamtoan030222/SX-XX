import setupMock, { failResponseWrap, successResponseWrap } from '@/utils/setup-mock'
import Mock from 'mockjs'

import { MockParams } from '@/types/mock'
import { isLogin } from '@/utils/auth'

setupMock({
  setup() {
    // Thông tin người dùng
    Mock.mock(new RegExp('/api/user/info'), () => {
      if (isLogin()) {
        const role = window.localStorage.getItem('userRole') || 'admin'
        return successResponseWrap({
          name: 'Phạm toàn',
          avatar: 'https://i.gtimg.cn/club/item/face/img/2/15922_100.gif',
          email: 'wangliqun@email.com',
          job: 'frontend',
          jobName: 'Nghệ sĩ Frontend',
          organization: 'Frontend',
          organizationName: 'Frontend',
          location: 'beijing',
          locationName: 'Bắc Kinh',
          introduction: 'Người phong cách, tính cách nhẹ nhàng',
          personalWebsite: 'https://www.arco.design',
          phone: '150****0000',
          registrationDate: '2013-05-10 12:10:00',
          accountId: '15012312300',
          certification: 1,
          role,
        })
      }
      return failResponseWrap(null, 'Chưa đăng nhập', 50008)
    })

    // Đăng nhập
    Mock.mock(new RegExp('/api/user/login'), (params: MockParams) => {
      const { username, password } = JSON.parse(params.body)
      if (!username) {
        return failResponseWrap(null, 'Tên đăng nhập không được để trống', 50000)
      }
      if (!password) {
        return failResponseWrap(null, 'Mật khẩu không được để trống', 50000)
      }
      if (username === 'admin' && password === 'admin') {
        window.localStorage.setItem('userRole', 'admin')
        return successResponseWrap({
          token: '12345',
        })
      }
      if (username === 'user' && password === 'user') {
        window.localStorage.setItem('userRole', 'user')
        return successResponseWrap({
          token: '54321',
        })
      }
      return failResponseWrap(null, 'Tài khoản hoặc mật khẩu sai', 50000)
    })

    // Đăng xuất
    Mock.mock(new RegExp('/api/user/logout'), () => {
      return successResponseWrap(null)
    })

    // Menu phía server của người dùng
    Mock.mock(new RegExp('/api/user/menu'), () => {
      const menuList = [
        {
          path: '/dashboard',
          name: 'dashboard',
          meta: {
            locale: 'menu.server.dashboard', // Bảng điều khiển - Máy chủ
            requiresAuth: true,
            icon: 'icon-dashboard',
            order: 1,
          },
          children: [
            {
              path: 'workplace',
              name: 'Workplace',
              meta: {
                locale: 'menu.server.workplace', // Bàn làm việc - Máy chủ
                requiresAuth: true,
              },
            },
            {
              path: 'https://arco.design',
              name: 'arcoWebsite',
              meta: {
                locale: 'menu.arcoWebsite', // Arco Design
                requiresAuth: true,
              },
            },
          ],
        },
      ]
      return successResponseWrap(menuList)
    })
  },
})
