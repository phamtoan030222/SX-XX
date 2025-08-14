import Mock from 'mockjs'
import qs from 'query-string'
import dayjs from 'dayjs'
import { GetParams } from '@/types/global'
import setupMock, { successResponseWrap } from '@/utils/setup-mock'

const textList = [
  {
    key: 1,
    clickNumber: '346.3w+', // 346.3 vạn+
    title: 'Kinh tế Nhật báo: Chính sách tài chính cần nâng cao chính xác...',
    increases: 35, // tăng 35
  },
  {
    key: 2,
    clickNumber: '324.2w+',
    title: 'Ngày 12/12 ảm đạm, người tiêu dùng đã chán các sàn thương mại điện tử...',
    increases: 22,
  },
  {
    key: 3,
    clickNumber: '318.9w+',
    title: 'Tri ân các cộng tác viên tuyến đầu chống dịch ở cộng đồng...',
    increases: 9,
  },
  {
    key: 4,
    clickNumber: '257.9w+',
    title: 'Trung học phổ thông hay trung học nghề? Phụ huynh rơi vào sự lựa chọn...',
    increases: 17,
  },
  {
    key: 5,
    clickNumber: '124.2w+',
    title: 'Bình luận nhân dân: Không ngờ “đôi mắt to và mày rậm” lại...',
    increases: 37,
  },
]

const imageList = [
  {
    key: 1,
    clickNumber: '15.3w+',
    title: 'Dương Đào thay Lục Kháng đảm nhiệm Bộ Ngoại giao Mỹ - Đại sự...',
    increases: 15,
  },
  {
    key: 2,
    clickNumber: '12.2w+',
    title: 'Ảnh: Lốc xoáy tấn công nhiều bang Mỹ phá hủy nhà cửa...',
    increases: 26,
  },
  {
    key: 3,
    clickNumber: '18.9w+',
    title: 'Chị gái 52 tuổi bỏ tiền chăm sóc trẻ tự kỷ 8 năm...',
    increases: 9,
  },
  {
    key: 4,
    clickNumber: '7.9w+',
    title: 'Gia đình ba người ở Hàng Châu bị ngộ độc khi cắm trại trong công viên để giữ ấm',
    increases: 0,
  },
  {
    key: 5,
    clickNumber: '5.2w+',
    title: 'Phó trưởng đồn cảnh sát đe dọa dân? Cảnh sát điều tra...',
    increases: 4,
  },
]

const videoList = [
  {
    key: 1,
    clickNumber: '367.6w+',
    title: 'Đây là lúc 10 giờ sáng nay tại Nam Kinh',
    increases: 5,
  },
  {
    key: 2,
    clickNumber: '352.2w+',
    title: 'Lithuania liên tục khiêu khích làm kinh tế bị tổn thương, người dân...',
    increases: 17,
  },
  {
    key: 3,
    clickNumber: '348.9w+',
    title: 'Nghệ sĩ Hàn Quốc Yoo Jae-suk xác nhận dương tính với Covid',
    increases: 30,
  },
  {
    key: 4,
    clickNumber: '346.3w+',
    title: 'Về Thế vận hội mùa đông Bắc Kinh, Moon Jae-in đã phát biểu',
    increases: 12,
  },
  {
    key: 5,
    clickNumber: '271.2w+',
    title: 'Lính hiện dịch thế hệ 95 được tặng huân chương hạng nhất',
    increases: 2,
  },
]

setupMock({
  setup() {
    Mock.mock(new RegExp('/api/content-data'), () => {
      const presetData = [58, 81, 53, 90, 64, 88, 49, 79]
      const getLineData = () => {
        const count = 8
        return new Array(count).fill(0).map((el, idx) => ({
          x: dayjs()
            .day(idx - 2)
            .format('YYYY-MM-DD'),
          y: presetData[idx],
        }))
      }
      return successResponseWrap([...getLineData()])
    })
    Mock.mock(new RegExp('/api/popular/list'), (params: GetParams) => {
      const { type = 'text' } = qs.parseUrl(params.url).query
      if (type === 'image') {
        return successResponseWrap([...videoList])
      }
      if (type === 'video') {
        return successResponseWrap([...imageList])
      }
      return successResponseWrap([...textList])
    })
  },
})
