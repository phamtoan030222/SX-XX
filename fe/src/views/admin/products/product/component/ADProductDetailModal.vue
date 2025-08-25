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
        <a-form-item v-show="id" label="Mã">
          <a-input v-model="detailProductDetail.code" disabled/>
        </a-form-item>
        <a-form-item label="Giá">
          <a-input-number v-model.number="detailProductDetail.price" placeholder="Nhập giá" :formatter="formatter" :parser="parser" />
        </a-form-item>
        <a-form-item label="Màu">
          <a-select v-model="detailProductDetail.idColor" placeholder="Chọn màu" multiple allow-clear>
            <a-option v-for="color in colors" :key="color.value" :value="color.value">
              {{ color.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Bộ nhớ">
          <a-select v-model="detailProductDetail.idRAM" placeholder="Chọn Bộ nhớ">
            <a-option v-for="ram in rams" :key="ram.value" :value="ram.value">
              {{ ram.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="CPU">
          <a-select v-model="detailProductDetail.idCPU" placeholder="Chọn CPU">
            <a-option v-for="cpu in cpus" :value="cpu.value" :key="cpu.value">{{ cpu.label }}</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="GPU">
          <a-select v-model="detailProductDetail.idGPU" placeholder="Chọn màn hình">
            <a-option v-for="gpu in gpus" :key="gpu.value" :value="gpu.value">
              {{ gpu.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Chất liệu">
          <a-select v-model="detailProductDetail.idMaterial" placeholder="Chọn chất liệu">
            <a-option v-for="material in materials" :key="material.value" :value="material.value">
              {{ material.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Ổ cứng">
          <a-select v-model="detailProductDetail.idHardDrive" placeholder="Chọn ổ cứng">
            <a-option v-for="hardDrive in hardDrives" :key="hardDrive.value" :value="hardDrive.value">
              {{ hardDrive.label }}
            </a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { Ref, ref, watch } from 'vue'
import { Notification } from '@arco-design/web-vue'
import {
  ADProductDetailDetailResponse,
  ADPRPropertiesComboboxResponse,
  getProductDetailById,
  modifyProductDetail,
} from '@/api/admin/product/productDetail.api'

const props = defineProps<{
  isOpen: boolean
  id: string | undefined
  isDetail: boolean
  idProduct: string
  cpus: ADPRPropertiesComboboxResponse[]
  gpus: ADPRPropertiesComboboxResponse[]
  colors: ADPRPropertiesComboboxResponse[]
  rams: ADPRPropertiesComboboxResponse[]
  hardDrives: ADPRPropertiesComboboxResponse[]
  materials: ADPRPropertiesComboboxResponse[]
}>()

const emit = defineEmits(['success', 'close'])

const detailProductDetail: Ref<ADProductDetailDetailResponse> = ref({
  id: '',
  code: '',
  name: '',
  idCPU: '',
  idGPU: '',
  idColor: [],
  idRAM: '',
  idHardDrive: '',
  idMaterial: '',
  price: 0,
  description: '',
})

const fetchDetailProductDetail = async () => {
  const res = await getProductDetailById(props.id as string)

  detailProductDetail.value = res.data
  // detailProductDetail.value.idColor = [detailProductDetail.value.idColor as unknown]
}

const resetField = () => {
  detailProductDetail.value = {
    id: '',
    code: '',
    name: '',
    idCPU: '',
    idGPU: '',
    idColor: [],
    idRAM: '',
    idHardDrive: '',
    idMaterial: '',
    price: 0,
    description: '',
  }
}

watch(
  () => props.id,
  (newId) => {
    if (newId) fetchDetailProductDetail()
    else resetField()
  }
)

const handleClickCancel = () => {
  emit('close')
}

const handleClickOK = async () => {
  const res = await modifyProductDetail({
    idProduct: props.idProduct,
    ...detailProductDetail.value
  })
  console.log(res.success)
  if (res.success) Notification.success(props.id ? 'Cập nhật sản phẩm chi tiết thành công' : 'Thêm sản phẩm chi tiết thành công')
  else Notification.error(props.id ? 'Thêm sản phẩm chi tiết thành công' : 'Thêm sản phẩm chi tiết thất bại')
  if (!props.id) resetField()
  emit('success')
}

const formatter = (value: string) => {
  const values = value.split('.')
  values[0] = values[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',')

  return values.join('.')
}

const parser = (value: string) => {
  return value.replace(/,/g, '')
}
</script>

<style scoped>
.container {
  max-height: 400px;
  overflow-y: auto;
}
</style>
