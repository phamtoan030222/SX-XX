<template>
  <a-modal
    :width="700"
    :visible="props.isOpen"
    :title="!props.id ? 'Thêm màn hình' : props.isDetail ? 'Chi tiết màn hình' : 'Cập nhật màn hình'"
    @cancel="handleClickCancel"
  >
    <template #footer>
      <a-button @click="handleClickCancel">Hủy</a-button>
      <a-popconfirm content="Bạn chắc chắn muốn thao tác" @ok="handleClickOK">
        <a-button type="primary" :disabled="props.isDetail">Xác nhận</a-button>
      </a-popconfirm>
    </template>

    <div class="container">
      <a-form auto-label-width>
        <a-form-item label="Mã">
          <a-input v-model="detailProduct.code" placeholder="Nhập mã" />
        </a-form-item>
        <a-form-item label="Tên">
          <a-input v-model="detailProduct.name" placeholder="Nhập tên" />
        </a-form-item>
        <a-form-item label="Hãng">
          <a-select v-model="detailProduct.idBrand" placeholder="Chọn hãng">
            <a-option v-for="brand in brands" :value="brand.value" :key="brand.value">{{ brand.label }}</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Màn hình">
          <a-select v-model="detailProduct.idScreen" placeholder="Chọn màn hình">
            <a-option v-for="screen in screens" :key="screen.value" :value="screen.value">
              {{ screen.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Pin">
          <a-select v-model="detailProduct.idBattery" placeholder="Chọn Pin">
            <a-option v-for="battery in batteries" :key="battery.value" :value="battery.value">
              {{ battery.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Chọn hệ điều hành">
          <a-select v-model="detailProduct.idOperatingSystem" placeholder="Chọn hệ điều hành">
            <a-option v-for="operatingSystem in operatingSystems" :key="operatingSystem.value" :value="operatingSystem.value">{{ operatingSystem.label }}</a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { Ref, ref, watch } from 'vue'
import { Notification } from '@arco-design/web-vue'
import { ADProductDetailResponse, ADPRPropertiesComboboxResponse, getProductById, modifyProduct } from '@/api/admin/product/product.api'

const props = defineProps<{
  isOpen: boolean
  id: string | undefined
  isDetail: boolean
  screens: ADPRPropertiesComboboxResponse[]
  batteries: ADPRPropertiesComboboxResponse[]
  brands: ADPRPropertiesComboboxResponse[]
  operatingSystems: ADPRPropertiesComboboxResponse[]
}>()

const emit = defineEmits(['success', 'close'])

const detailProduct: Ref<ADProductDetailResponse> = ref({
  id: '',
  code: '',
  name: '',
  idBattery: '',
  idOperatingSystem: '',
  idBrand: '',
  idScreen: '',
})

const fetchDetailProduct = async () => {
  const res = await getProductById(props.id as string)

  detailProduct.value = res.data
}

const resetField = () => {
  detailProduct.value = {
    id: '',
    code: '',
    name: '',
    idBattery: '',
    idOperatingSystem: '',
    idBrand: '',
    idScreen: '',
  }
}

watch(
  () => props.id,
  (newId) => {
    if (newId) fetchDetailProduct()
    else resetField()
  }
)

const handleClickCancel = () => {
  emit('close')
}

const handleClickOK = async () => {
  const res = await modifyProduct(detailProduct.value)
  console.log(res.success)
  if (res.success) Notification.success(props.id ? 'Cập nhật sản phẩm thành công' : 'Thêm sản phẩm thành công')
  else Notification.error(props.id ? 'Thêm sản phẩm thành công' : 'Thêm sản phẩm thất bại')
  if(!props.id) resetField()
  emit('success')
}
</script>

<style scoped>
.container {
  max-height: 400px;
  overflow-y: auto;
}
</style>
