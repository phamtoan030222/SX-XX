<template>
  <div class="container">
    <Breadcrumb :items="['Sản phẩm', 'GPU']" />
    <a-spin :loading="loading" style="width: 100%">
      <a-card class="general-card">
        <a-row class="mb-20px pt-20px" :gutter="12">
          <a-col :span="6">
            <a-input v-model="state.search.q" placeholder="Tìm kiếm"></a-input>
          </a-col>

          <a-col :span="6">
            <a-input v-model="state.search.generation" placeholder="Tìm kiếm thế hệ"></a-input>
          </a-col>

          <a-col :span="4">
            <a-select v-model="state.search.brand" placeholder="Chọn hãng">
              <a-option>NVIDIA</a-option>
              <a-option>AMD</a-option>
            </a-select>
          </a-col>

          <a-col :span="4">
            <a-input v-model="state.search.series" placeholder="Tìm kiếm dòng GPU"></a-input>
          </a-col>

          <a-col :span="3">
            <a-year-picker v-model="state.search.releaseYear" placeholder="Chọn năm" />
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
            <a-tooltip content="Thêm GPU" background-color="#3491FA">
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
          :data="state.data.GPUs"
          :columns="columns"
        >
          <template #orderNumber="{ rowIndex }">
            {{ rowIndex + 1 }}
          </template>
          <template #action="{ record, rowIndex }">
            <a-space>
              <a-tooltip content="Chi tiết GPU" background-color="#3491FA">
                <a-button type="text" shape="circle" @click="clickOpenModal(record.id, true)"><icon-eye /></a-button>
              </a-tooltip>
              <a-tooltip content="Cập nhật GPU" background-color="#3491FA">
                <a-button type="text" shape="circle" @click="clickOpenModal(record.id)"><icon-edit /></a-button>
              </a-tooltip>
            </a-space>
          </template>
        </a-table>
      </a-card>
    </a-spin>

    <ADProductGPUModal
      @success="handleSuccessModifyModal"
      :isDetail="isDetailModal"
      :isOpen="isOpenModal"
      :id="GPUIDSelected"
      @close="closeModal"
    />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, Ref, ref, watch } from 'vue'
import { ADProductGPUResponse, getGPUs } from '@/api/admin/product/gpu.api'
import { debounce } from 'lodash'
import useLoading from '@/hooks/loading'
import ADProductGPUModal from './component/ADProductGPUModal.vue'

const { loading, setLoading } = useLoading(false)

const state = reactive({
  search: {
    q: '',
    brand: '',
    generation: '',
    series: '',
    releaseYear: undefined as number | undefined,
  },
  data: {
    GPUs: [] as ADProductGPUResponse[],
  },
  pagination: {
    page: 1,
    size: 10,
    totalPages: undefined as number | undefined,
  },
})

const fetchGPUs = async () => {
  const res = await getGPUs({
    page: state.pagination.page,
    size: state.pagination.size,
    q: state.search.q,
    brand: state.search.brand,
    releaseYear: state.search.releaseYear,
    series: state.search.series,
    generation: state.search.generation,
  })

  console.table(res.data.data)

  state.data.GPUs = res.data.data
  state.pagination.totalPages = res.data.totalPages
}

const refreshFilter = () => {
  state.search.q = ''
  state.search.brand = ''
  state.search.generation = ''
  state.search.series = ''
  state.search.releaseYear = undefined
}

const columns = [
  { title: '#', name: 'orderNumber', dataIndex: 'orderNumber', width: 50, fixed: 'left', slotName: 'orderNumber' },
  { title: 'Mã', name: 'code', dataIndex: 'code', width: 100, fixed: 'left', slotName: 'code' },
  { title: 'Tên GPU', name: 'name', dataIndex: 'name', width: 150, fixed: 'left', slotName: 'name' },
  { title: 'Thế hệ', name: 'generation', dataIndex: 'generation', width: 150, align: 'center', slotName: 'generation' },
  { title: 'Hãng', name: 'brand', dataIndex: 'brand', width: 150, align: 'center', slotName: 'brand' },
  { title: 'Năm phát hàng', name: 'releaseYear', dataIndex: 'releaseYear', width: 150, align: 'center', slotName: 'releaseYear' },
  { title: 'Dòng GPU', name: 'series', dataIndex: 'series', width: 150, align: 'center', slotName: 'series' },
  { title: 'Mô tả', name: 'description', dataIndex: 'description', width: 200, align: 'left', slotName: 'description' },
  { title: '', name: 'action', dataIndex: 'action', width: 100, fixed: 'right', slotName: 'action' },
]

onMounted(() => {
  fetchGPUs()
})

const isOpenModal = ref<boolean>(false)

const isDetailModal: Ref<boolean> = ref(true)

const GPUIDSelected = ref<string>()

const clickOpenModal = (id?: string, isDetail?: boolean) => {
  console.log(id)
  GPUIDSelected.value = id
  isOpenModal.value = true
  isDetailModal.value = isDetail ?? false
}

const closeModal = () => {
  isOpenModal.value = false
}

const handleSuccessModifyModal = () => {
  fetchGPUs()
  closeModal()
}

const debounceFetchGPUs = debounce(fetchGPUs, 300)

watch(
  () => [state.search.q, state.search.brand, state.search.releaseYear, state.search.generation, state.search.releaseYear],
  () => {
    console.log('here')
    debounceFetchGPUs()
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
