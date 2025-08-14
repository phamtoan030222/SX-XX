import Mock from 'mockjs'
import setupMock, { successResponseWrap } from '@/utils/setup-mock'
import { ServiceRecord } from '@/api/list'

const qualityInspectionList: ServiceRecord[] = []
const theServiceList: ServiceRecord[] = []
const rulesPresetList: ServiceRecord[] = []

setupMock({
  setup() {
    // Quality Inspection
    Mock.mock(new RegExp('/api/list/quality-inspection'), () => {
      return successResponseWrap(
        qualityInspectionList.map((_, index) => ({
          ...qualityInspectionList[index % qualityInspectionList.length],
          id: Mock.Random.guid(),
        }))
      )
    })

    // the service
    Mock.mock(new RegExp('/api/list/the-service'), () => {
      return successResponseWrap(
        theServiceList.map((_, index) => ({
          ...theServiceList[index % theServiceList.length],
          id: Mock.Random.guid(),
        }))
      )
    })

    // rules preset
    Mock.mock(new RegExp('/api/list/rules-preset'), () => {
      return successResponseWrap(
        rulesPresetList.map((_, index) => ({
          ...rulesPresetList[index % rulesPresetList.length],
          id: Mock.Random.guid(),
        }))
      )
    })
  },
})
