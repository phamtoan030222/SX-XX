<template>
  <div class="container">
    <Breadcrumb :items="['Sản phẩm', 'Màn hình']" />
    <a-spin :loading="loading" style="width: 100%">
      <a-card class="general-card">
        <a-row class="mb-20px pt-20px" :gutter="12">
          <a-col :span="6">
            <a-input v-model="state.search.q" placeholder="Tìm kiếm"></a-input>
          </a-col>

          <a-col :span="6">
            <a-input v-model="state.search.technology" placeholder="Tìm kiếm công nghệ"></a-input>
          </a-col>

          <a-col :span="4">
            <a-select v-model="state.search.panelType" placeholder="Chọn tấm nền">
              <a-option>LCD</a-option>
              <a-option>OLED</a-option>
              <a-option>LED_BACKLIT_LCD</a-option>
              <a-option>AMOLED</a-option>
              <a-option>MINI_LED</a-option>
              <a-option>MICRO_LED</a-option>
            </a-select>
          </a-col>

          <a-col :span="4">
            <a-select v-model="state.search.screenResolution" placeholder="Chọn độ phân giải">
              <template v-for="screenResolution in screenResolutions" :key="screenResolution.id">
                <a-option :value="screenResolution.id">{{ screenResolution.name }}</a-option>
              </template>
            </a-select>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.physicalSize" placeholder="Chọn kích thước màn hình">
              <a-option value="11.6">11.6 inch</a-option>
              <a-option value="13.3">13.3 inch</a-option>
              <a-option value="14">14 inch</a-option>
              <a-option value="15.6">15.6 inch</a-option>
              <a-option value="16">16 inch</a-option>
              <a-option value="17.3">17.3 inch</a-option>
            </a-select>
          </a-col>

          <a-col :span="1">
            <a-tooltip content="Làm sạch bộ lọc" background-color="#3491FA">
              <a-button type="primary" shape="circle" @click="refreshFilter"><icon-refresh /></a-button>
            </a-tooltip>
          </a-col>
        </a-row>
        <div class="line"></div>
        <a-row justify="end" class="p-20px">
          <a-space>
            <a-tooltip content="Thêm Screen" background-color="#3491FA">
              <a-button type="primary" shape="circle" @click="clickOpenModal()"><icon-plus /></a-button>
            </a-tooltip>
          </a-space>
        </a-row>
        <a-table
          :pagination="{
            current: state.pagination.page,
            pageSize: state.pagination.size,
            total: state.pagination.totalPages,
            showTotal: true,
            showJumper: true,
          }"
          :scroll="{ x: 200, y: 500 }"
          :data="state.data.Screens"
          :columns="columns"
        >
          <template #orderNumber="{ rowIndex }">
            {{ rowIndex + 1 }}
          </template>
          <template #action="{ record, rowIndex }">
            <a-space>
              <a-tooltip content="Chi tiết Screen" background-color="#3491FA">
                <a-button type="text" shape="circle" @click="clickOpenModal(record.id, true)"><icon-eye /></a-button>
              </a-tooltip>
              <a-tooltip content="Cập nhật Screen" background-color="#3491FA">
                <a-button type="text" shape="circle" @click="clickOpenModal(record.id)"><icon-edit /></a-button>
              </a-tooltip>
            </a-space>
          </template>
        </a-table>
      </a-card>
    </a-spin>

    <ADProductScreenModal
      @success="handleSuccessModifyModal"
      :isDetail="isDetailModal"
      :isOpen="isOpenModal"
      :screenResolutions="screenResolutions"
      :id="ScreenIDSelected"
      @close="closeModal"
    />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, Ref, ref, watch } from 'vue'
import {
  ADProductScreenResolutionResponse,
  ADProductScreenResponse,
  getScreenResolutions,
  getScreens,
} from '@/api/admin/product/screen.api'
import { debounce } from 'lodash'
import useLoading from '@/hooks/loading'
import ADProductScreenModal from './component/ADProductScreenModal.vue'

const { loading, setLoading } = useLoading(false)

const state = reactive({
  search: {
    q: '',
    screenResolution: '' as string | undefined,
    technology: '',
    panelType: '',
    physicalSize: undefined as number | undefined,
  },
  data: {
    Screens: [] as ADProductScreenResponse[],
  },
  pagination: {
    page: 1,
    size: 10,
    totalPages: undefined as number | undefined,
  },
})

const fetchScreens = async () => {
  const res = await getScreens({
    page: state.pagination.page,
    size: state.pagination.size,
    q: state.search.q,
    idScreenResolution: state.search.screenResolution as string,
    physicalSize: state.search.physicalSize as number,
    panelType: state.search.panelType,
    technology: state.search.technology,
  })

  state.data.Screens = res.data.data
  state.pagination.totalPages = res.data.totalPages
}

const screenResolutions: Ref<ADProductScreenResolutionResponse[]> = ref([])

const fetchScreenResolution = async () => {
  const res = await getScreenResolutions()

  screenResolutions.value = res.data
}

const refreshFilter = () => {
  state.search.q = ''
  state.search.screenResolution = undefined
  state.search.technology = ''
  state.search.panelType = ''
  state.search.physicalSize = undefined
}

const columns = [
  { title: '#', name: 'orderNumber', dataIndex: 'orderNumber', width: 50, fixed: 'left', slotName: 'orderNumber' },
  { title: 'Mã', name: 'code', dataIndex: 'code', width: 100, fixed: 'left', slotName: 'code' },
  { title: 'Tên ', name: 'name', dataIndex: 'name', width: 150, fixed: 'left', slotName: 'name' },
  { title: 'Kích thước', name: 'physicalSize', dataIndex: 'physicalSize', width: 150, align: 'center', slotName: 'physicalSize' },
  {
    title: 'Độ phân giải',
    name: 'screenResolution',
    dataIndex: 'screenResolution',
    width: 150,
    align: 'center',
    slotName: 'screenResolution',
  },
  { title: 'Chất liệu tấm nền', name: 'panelType', dataIndex: 'panelType', width: 150, align: 'center', slotName: 'panelType' },
  { title: 'Công nghệ', name: 'technology', dataIndex: 'technology', width: 150, align: 'center', slotName: 'technology' },
  { title: '', name: 'action', dataIndex: 'action', width: 100, fixed: 'right', slotName: 'action' },
]

onMounted(() => {
  fetchScreens()
  fetchScreenResolution()
})

const isOpenModal = ref<boolean>(false)

const isDetailModal: Ref<boolean> = ref(true)

const ScreenIDSelected = ref<string>()

const clickOpenModal = (id?: string, isDetail?: boolean) => {
  ScreenIDSelected.value = id
  isOpenModal.value = true
  isDetailModal.value = isDetail ?? false
}

const closeModal = () => {
  isOpenModal.value = false
}

const handleSuccessModifyModal = () => {
  fetchScreens()
  closeModal()
}

const debounceFetchScreens = debounce(fetchScreens, 300)

watch(
  () => [state.search.q, state.search.screenResolution, state.search.physicalSize, state.search.technology, state.search.panelType],
  () => {
    debounceFetchScreens()
  }
)
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.mt-20px {
  margin-top: 20px;
}

.mb-20px {
  margin-bottom: 20px;
}

.pt-20px {
  padding-top: 20px;
}

.p-20px {
  padding: 20px;
}

.line {
  border-top: 1px solid #a4a5a8;
}

.d-inline {
  display: inline;
}
</style>
