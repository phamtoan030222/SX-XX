import Mock from 'mockjs'
import setupMock, { successResponseWrap } from '@/utils/setup-mock'

const haveReadIds: number[] = []
const getMessageList = () => {
  return [
    {
      id: 1,
      type: 'message',
      title: 'Thông báo',
      subTitle: 'Bạn có một thông báo mới',
      avatar: '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/8361eeb82904210b4f55fab888fe8416.png~tplv-uwbnlip3yd-webp.webp',
      content: 'Hệ thống đã cập nhật thành công, vui lòng kiểm tra các thay đổi.',
      time: ' 12:30:01',
    },
  ].map((item) => ({
    ...item,
    status: haveReadIds.indexOf(item.id) === -1 ? 0 : 1,
  }))
}

setupMock({
  setup: () => {
    Mock.mock(new RegExp('/api/message/list'), () => {
      return successResponseWrap(getMessageList())
    })

    Mock.mock(new RegExp('/api/message/read'), (params: { body: string }) => {
      const { ids } = JSON.parse(params.body)
      haveReadIds.push(...(ids || []))
      return successResponseWrap(true)
    })
  },
})
