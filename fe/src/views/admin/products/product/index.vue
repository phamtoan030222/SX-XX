<template>
  <div class="container">

    <Breadcrumb :items="['Sản phẩm']" />
    <a-spin :loading="loading" style="width: 100%">
      <a-card class="general-card">
        <a-row class="mb-20px pt-20px" :gutter="12">
          <a-col :span="8">
            <a-input v-model="state.search.q" placeholder="Tìm kiếm"></a-input>
          </a-col>

          <a-col :span="4">
            <a-select v-model="state.search.brand" placeholder="Chọn hãng">
              <a-option v-for="brand in state.data.brands" :key="brand.value" :value="brand.value">
                {{ brand.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="4">
            <a-select v-model="state.search.battery" placeholder="Chọn loại pin">
              <a-option v-for="battery in state.data.batteries" :key="battery.value" :value="battery.value">
                {{ battery.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="4">
            <a-select v-model="state.search.screen" placeholder="Chọn màn hình">
              <a-option v-for="screen in state.data.screens" :key="screen.value" :value="screen.value">
                {{ screen.label }}
              </a-option>
            </a-select>
          </a-col>

          <a-col :span="3">
            <a-select v-model="state.search.operatingSystem" placeholder="Chọn hệ điều hành">
              <a-option v-for="operatingSystem in state.data.operatingSystems" :key="operatingSystem.value" :value="operatingSystem.value">
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
          :data="state.data.products"
          :columns="columns"
        >
          <template #orderNumber="{ rowIndex }">
            {{ rowIndex + 1 }}
          </template>
          <template #action="{ record, rowIndex }">
            <a-space>
              <a-tooltip content="Sản phẩm chi tiết" background-color="#3491FA">
                <a-button type="text" shape="circle" @click="handleClickDetailProduct(record.id)"><icon-info-circle /></a-button>
              </a-tooltip>
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

    <ADProductModal
      @success="handleSuccessModifyModal"
      :isDetail="isDetailModal"
      :isOpen="isOpenModal"
      :id="productIdSelected"
      :screens="state.data.screens"
      :brands="state.data.brands"
      :batteries="state.data.batteries"
      :operatingSystems="state.data.operatingSystems"
      @close="closeModal"
    />
  </div>
</template>

<script lang="ts" setup>
import {
  ADProductResponse,
  ADPRPropertiesComboboxResponse,
  getBatteries,
  getBrands,
  getOperatingSystems,
  getProducts,
  getScreens,
} from '@/api/admin/product/product.api'
import useLoading from '@/hooks/loading'
import { debounce } from 'lodash'
import { onMounted, reactive, Ref, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import ADProductModal from './component/ADProductModal.vue'

const { loading, setLoading } = useLoading(false)
const router = useRouter()
const state = reactive({
  search: {
    q: '',
    brand: '',
    screen: '',
    battery: '',
    operatingSystem: '',
  },
  data: {
    products: [] as ADProductResponse[],
    screens: [] as ADPRPropertiesComboboxResponse[],
    brands: [] as ADPRPropertiesComboboxResponse[],
    batteries: [] as ADPRPropertiesComboboxResponse[],
    operatingSystems: [] as ADPRPropertiesComboboxResponse[],
  },
  pagination: {
    page: 1,
    size: 10,
    totalPages: undefined as number | undefined,
  },
})

const fetchProducts = async () => {
  const res = await getProducts({
    page: state.pagination.page,
    size: state.pagination.size,
    q: state.search.q,
    idBrand: state.search.brand,
    idOperatingSystem: state.search.operatingSystem,
    idBattery: state.search.battery,
    idScreen: state.search.screen,
  })

  state.data.products = res.data.data
  state.pagination.totalPages = res.data.totalPages
}

const fetchComboboxProperties = async () => {
  try {
    const [screenProperties, brandProperties, batteryProperties, operatingSystemProperties] = await Promise.all([
      getScreens(),
      getBrands(),
      getBatteries(),
      getOperatingSystems(),
    ])

    state.data.screens = screenProperties.data
    state.data.batteries = batteryProperties.data
    state.data.brands = brandProperties.data
    state.data.operatingSystems = operatingSystemProperties.data
  } catch (error) {
    console.log(error)
  }
}

const refreshFilter = () => {
  state.search.q = ''
  state.search.battery = ''
  state.search.brand = ''
  state.search.screen = ''
  state.search.operatingSystem = ''
}

const columns = [
  { title: '#', name: 'orderNumber', dataIndex: 'orderNumber', width: 50, fixed: 'left', slotName: 'orderNumber' },
  { title: 'Mã', name: 'code', dataIndex: 'code', width: 100, fixed: 'left', slotName: 'code' },
  { title: 'Tên ', name: 'name', dataIndex: 'name', width: 150, fixed: 'left', slotName: 'name' },
  { title: 'Hãng', name: 'brand', dataIndex: 'brand', width: 150, align: 'center', slotName: 'brand' },
  { title: 'Pin', name: 'battery', dataIndex: 'battery', width: 150, align: 'center', slotName: 'battery' },
  { title: 'Màn hình', name: 'screen', dataIndex: 'screen', width: 150, align: 'center', slotName: 'screen' },
  {
    title: 'Hệ điều hành',
    name: 'operatingSystem',
    dataIndex: 'operatingSystem',
    width: 150,
    align: 'operatingSystem',
    slotName: 'operatingSystem',
  },
  { title: '', name: 'action', dataIndex: 'action', width: 100, fixed: 'right', slotName: 'action' },
]

onMounted(() => {
  fetchProducts()
  fetchComboboxProperties()
})

const isOpenModal = ref<boolean>(false)

const isDetailModal: Ref<boolean> = ref(true)

const productIdSelected = ref<string>()

const clickOpenModal = (id?: string, isDetail?: boolean) => {
  productIdSelected.value = id
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
  () => [state.search.q, state.search.operatingSystem, state.search.battery, state.search.brand, state.search.screen],
  () => {
    debounceFetchProducts()
  }
)

const handleClickDetailProduct = (idProduct: string) => {
  router.push({
    name: 'ProductDetail',
    params: {
      id: idProduct,
    },
  })
  console.log(router.getRoutes());
}
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
