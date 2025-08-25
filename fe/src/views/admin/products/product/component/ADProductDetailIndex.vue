<template>
  <div class="container">
    <Breadcrumb :items="['Sản phẩm']" />
    <a-spin :loading="loading" style="width: 100%">
      <a-card class="general-card">
        <a-row class="mb-20px pt-20px" :gutter="12">
          <a-col :span="5">
            <a-input v-model="state.search.q" placeholder="Tìm kiếm"></a-input>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.material" placeholder="Chọn vật liệu">
              <a-option v-for="brand in state.data.materials" :key="brand.value" :value="brand.value">
                {{ brand.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.color" placeholder="Chọn màu">
              <a-option v-for="battery in state.data.colors" :key="battery.value" :value="battery.value">
                {{ battery.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.gpu" placeholder="Chọn cpu">
              <a-option v-for="screen in state.data.gpus" :key="screen.value" :value="screen.value">
                {{ screen.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.hardDrive" placeholder="Chọn ổ cứng">
              <a-option v-for="operatingSystem in state.data.hardDrives" :key="operatingSystem.value" :value="operatingSystem.value">
                {{ operatingSystem.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.cpu" placeholder="Chọn cpu">
              <a-option v-for="operatingSystem in state.data.cpus" :key="operatingSystem.value" :value="operatingSystem.value">
                {{ operatingSystem.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.ram" placeholder="Chọn ram">
              <a-option v-for="operatingSystem in state.data.rams" :key="operatingSystem.value" :value="operatingSystem.value">
                {{ operatingSystem.label }}
              </a-option>
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
            <a-tooltip content="Thêm sản phẩm" background-color="#3491FA">
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
          :data="state.data.productDetails"
          :columns="columns"
        >
          <template #orderNumber="{ rowIndex }">
            {{ rowIndex + 1 }}
          </template>
          <template #action="{ record, rowIndex }">
            <a-space>
              <a-tooltip content="Chi tiết sản phẩm" background-color="#3491FA">
                <a-button type="text" shape="circle" @click="clickOpenModal(record.id, true)"><icon-eye /></a-button>
              </a-tooltip>
              <a-tooltip content="Cập nhật sản phẩm" background-color="#3491FA">
                <a-button type="text" shape="circle" @click="clickOpenModal(record.id)"><icon-edit /></a-button>
              </a-tooltip>
            </a-space>
          </template>
        </a-table>
      </a-card>
    </a-spin>

    <ADProductDetailModal
      @success="handleSuccessModifyModal"
      :isDetail="isDetailModal"
      :isOpen="isOpenModal"
      :id="productDetailIdSelected"
      :idProduct="idProduct"
      @close="closeModal"
      :cpus="state.data.cpus"
      :gpus="state.data.gpus"
      :materials="state.data.materials"
      :colors="state.data.colors"
      :hardDrives="state.data.hardDrives"
      :rams="state.data.rams"
    />
  </div>
</template>

<script lang="ts" setup>
import useLoading from '@/hooks/loading'
import { debounce } from 'lodash'
import { onMounted, reactive, Ref, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import {
  ADProductDetailResponse,
  ADPRPropertiesComboboxResponse,
  getColors,
  getCPUs,
  getGPUs,
  getHardDrives,
  getMaterials,
  getProductDetails,
  getRAMs,
} from '@/api/admin/product/productDetail.api'
import ADProductDetailModal from './ADProductDetailModal.vue'

const { loading, setLoading } = useLoading(false)
const route = useRoute()
const idProduct: Ref<string> = ref(route.params.id as string)

const state = reactive({
  search: {
    q: '',
    material: '',
    ram: '',
    cpu: '',
    gpu: '',
    color: '',
    hardDrive: '',
  },
  data: {
    productDetails: [] as ADProductDetailResponse[],
    materials: [] as ADPRPropertiesComboboxResponse[],
    rams: [] as ADPRPropertiesComboboxResponse[],
    cpus: [] as ADPRPropertiesComboboxResponse[],
    gpus: [] as ADPRPropertiesComboboxResponse[],
    colors: [] as ADPRPropertiesComboboxResponse[],
    hardDrives: [] as ADPRPropertiesComboboxResponse[],
  },
  pagination: {
    page: 1,
    size: 10,
    totalPages: undefined as number | undefined,
  },
})

const fetchProducts = async () => {
  const res = await getProductDetails({
    page: state.pagination.page,
    size: state.pagination.size,
    q: state.search.q,
    idMaterial: state.search.material,
    idRAM: state.search.ram,
    idCPU: state.search.cpu,
    idGPU: state.search.gpu,
    idColor: state.search.color,
    idHardDrive: state.search.hardDrive,
    idProduct: idProduct.value,
  })

  state.data.productDetails = res.data.data
  state.pagination.totalPages = res.data.totalPages
}

const fetchComboboxProperties = async () => {
  try {
    const [colorProperties, materialProperties, cpuProperties, gpuProperties, hardDriveProperties, ramProperties] = await Promise.all([
      getColors(),
      getMaterials(),
      getCPUs(),
      getGPUs(),
      getHardDrives(),
      getRAMs(),
    ])

    state.data.colors = colorProperties.data
    state.data.materials = materialProperties.data
    state.data.cpus = cpuProperties.data
    state.data.gpus = gpuProperties.data
    state.data.hardDrives = hardDriveProperties.data
    state.data.rams = ramProperties.data
  } catch (error) {
    console.log(error)
  }
}

const refreshFilter = () => {
  state.search.q = ''
  state.search.material = ''
  state.search.color = ''
  state.search.material = ''
  state.search.cpu = ''
  state.search.gpu = ''
  state.search.ram = ''
}

const columns = [
  { title: '#', name: 'orderNumber', dataIndex: 'orderNumber', width: 50, fixed: 'left', slotName: 'orderNumber' },
  { title: 'Mã', name: 'code', dataIndex: 'code', width: 120, fixed: 'left', slotName: 'code' },
  { title: 'Tên ', name: 'name', dataIndex: 'name', width: 150, fixed: 'left', slotName: 'name' },
  { title: 'Giá bán ', name: 'price', dataIndex: 'price', width: 150, fixed: 'left', slotName: 'price' },
  { title: 'Màu', name: 'color', dataIndex: 'color', width: 150, align: 'center', slotName: 'brand' },
  { title: 'RAM', name: 'ram', dataIndex: 'ram', width: 150, align: 'center', slotName: 'battery' },
  { title: 'CPU', name: 'cpu', dataIndex: 'cpu', width: 150, align: 'center', slotName: 'cpu' },
  { title: 'GPU', name: 'gpu', dataIndex: 'gpu', width: 150, align: 'center', slotName: 'gpu' },
  { title: 'Ổ cứng', name: 'hardDrive', dataIndex: 'hardDrive', width: 150, align: 'center', slotName: 'hardDrive' },
  { title: 'Chất liệu', name: 'material', dataIndex: 'material', width: 150, align: 'center', slotName: 'material' },
  { title: '', name: 'action', dataIndex: 'action', width: 100, fixed: 'right', slotName: 'action' },
]

onMounted(() => {
  fetchProducts()
  fetchComboboxProperties()
})

const isOpenModal = ref<boolean>(false)

const isDetailModal: Ref<boolean> = ref(true)

const productDetailIdSelected = ref<string>()

const clickOpenModal = (id?: string, isDetail?: boolean) => {
  productDetailIdSelected.value = id
  isOpenModal.value = true
  isDetailModal.value = isDetail ?? false
}

const closeModal = () => {
  isOpenModal.value = false
}

const handleSuccessModifyModal = () => {
  fetchProducts()
  closeModal()
}

const debounceFetchProducts = debounce(fetchProducts, 300)

watch(
  () => [
    state.search.q,
    state.search.color,
    state.search.cpu,
    state.search.gpu,
    state.search.ram,
    state.search.hardDrive,
    state.search.material,
  ],
  () => {
    debounceFetchProducts()
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
