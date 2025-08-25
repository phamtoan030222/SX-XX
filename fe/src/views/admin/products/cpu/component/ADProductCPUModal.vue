<template>
  <a-modal
    :width="700"
    :visible="props.isOpen"
    :title="!props.id ? 'Thêm CPU' : props.isDetail ? 'Chi tiết CPU' : 'Cập nhật CPU'"
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
          <a-input v-model="detailCPU.code" placeholder="Nhập mã" />
        </a-form-item>
        <a-form-item label="Tên">
          <a-input v-model="detailCPU.name" placeholder="Nhập tên" />
        </a-form-item>
        <a-form-item label="Thế hệ">
          <a-input v-model="detailCPU.generation" placeholder="Nhập thế hệ" />
        </a-form-item>
        <a-form-item label="Dòng CPU">
          <a-input v-model="detailCPU.series" placeholder="Nhập dòng CPU" />
        </a-form-item>
        <a-form-item label="Hãng">
          <a-select v-model="detailCPU.brand" placeholder="Chọn hãng">
            <a-option>Intel</a-option>
            <a-option>AMD</a-option>
            <a-option>Apple</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Năm phát hành">
          <a-year-picker v-model="detailCPU.releaseYear" placeholder="Chọn năm" />
        </a-form-item>
        <a-form-item label="Mô tả">
          <a-textarea
            v-model="detailCPU.description"
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
import { modifyCPU, ADProductCPUResponse, getCPUById } from '@/api/admin/product/cpu.api'
import { Notification } from '@arco-design/web-vue'

const props = defineProps<{
  isOpen: boolean
  id: string | undefined
  isDetail: boolean
}>()

const emit = defineEmits(['success', 'close'])

const detailCPU: Ref<ADProductCPUResponse> = ref({
  code: '',
  name: '',
  description: '',
  generation: '',
  series: '',
  brand: '',
  releaseYear: 0,
})

const fetchDetailCPU = async () => {
  const res = await getCPUById(props.id as string)

  detailCPU.value = res.data
}

const resetField = () => {
  detailCPU.value = {
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
    if (newId) fetchDetailCPU()
    else resetField()
  }
)

const handleClickCancel = () => {
  emit('close')
}

const handleClickOK = async () => {
  const res = await modifyCPU(detailCPU.value)
  console.log(res.success)
  if (res.success) Notification.success(props.id ? 'Cập nhật CPU thành công' : 'Thêm CPU thành công')
  else Notification.error(props.id ? 'Thêm CPU thành công' : 'Thêm CPU thất bại')
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
