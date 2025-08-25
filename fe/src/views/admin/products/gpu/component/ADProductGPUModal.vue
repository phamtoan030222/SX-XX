<template>
  <a-modal
    :width="700"
    :visible="props.isOpen"
    :title="!props.id ? 'Thêm GPU' : props.isDetail ? 'Chi tiết GPU' : 'Cập nhật GPU'"
    @cancel="handleClickCancel"
  >
    <template #footer>
      <a-button @click="handleClickCancel">Hủy</a-button>
      <a-popconfirm content="Bạn chắc chắn muốn thao tác" @ok="handleClickOK">
        <a-button type="primary" :disabled="props.isDetail">Xác nhận</a-button>
      </a-popconfirm>
    </template>

    <div class="container">
      <a-form>
        <a-form-item label="Mã">
          <a-input v-model="detailGPU.code" placeholder="Nhập mã" />
        </a-form-item>
        <a-form-item label="Tên">
          <a-input v-model="detailGPU.name" placeholder="Nhập tên" />
        </a-form-item>
        <a-form-item label="Thế hệ">
          <a-input v-model="detailGPU.generation" placeholder="Nhập thế hệ" />
        </a-form-item>
        <a-form-item label="Dòng GPU">
          <a-input v-model="detailGPU.series" placeholder="Nhập dòng GPU" />
        </a-form-item>
        <a-form-item label="Hãng">
          <a-select v-model="detailGPU.brand" placeholder="Chọn hãng">
            <a-option>NVIDIA</a-option>
            <a-option>AMD</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Năm phát hành">
          <a-year-picker v-model="detailGPU.releaseYear" placeholder="Chọn năm" />
        </a-form-item>
        <a-form-item label="Mô tả">
          <a-textarea
            v-model="detailGPU.description"
            :auto-size="{
              minRows: 2,
              maxRows: 5,
            }"
            placeholder="Mô tả"
            allow-clear
          />
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { Ref, ref, watch } from 'vue'
import { modifyGPU, ADProductGPUResponse, getGPUById } from '@/api/admin/product/gpu.api'
import { Notification } from '@arco-design/web-vue'

const props = defineProps<{
  isOpen: boolean
  id: string | undefined
  isDetail: boolean
}>()

const emit = defineEmits(['success', 'close'])

const detailGPU: Ref<ADProductGPUResponse> = ref({
  code: '',
  name: '',
  description: '',
  generation: '',
  series: '',
  brand: '',
  releaseYear: 0,
})

const fetchDetailGPU = async () => {
  const res = await getGPUById(props.id as string)

  detailGPU.value = res.data
}

const resetField = () => {
  detailGPU.value = {
    code: '',
    name: '',
    description: '',
    generation: '',
    series: '',
    brand: '',
    releaseYear: 0,
  }
}

watch(
  () => props.id,
  (newId) => {
    if (newId) fetchDetailGPU()
    else resetField()
  }
)

const handleClickCancel = () => {
  emit('close')
}

const handleClickOK = async () => {
  const res = await modifyGPU(detailGPU.value)
  console.log(res.success)
  if (res.success) Notification.success(props.id ? 'Cập nhật GPU thành công' : 'Thêm GPU thành công')
  else Notification.error(props.id ? 'Thêm GPU thành công' : 'Thêm GPU thất bại')
  resetField()
  emit('success')
}
</script>

<style scoped>
.container {
  max-height: 400px;
  overflow-y: auto;
}
</style>
