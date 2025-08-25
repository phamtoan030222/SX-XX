<template>
  <a-modal
    v-model:visible="modalVisible"
    :title="isEdit ? 'Chỉnh sửa đợt giảm giá' : 'Tạo đợt giảm giá'"
    :width="800"
    :mask-closable="false"
    @cancel="handleCancel"
    @before-ok="handleSubmit"
    :confirm-loading="submitLoading"
  >
    <a-form ref="formRef" :model="formModel" :rules="formRules" layout="vertical">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item field="discountName" label="Tên đợt giảm giá" :required="true">
            <a-input v-model="formModel.discountName" placeholder="Nhập tên đợt giảm giá" :max-length="100" show-word-limit />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item field="discountCode" label="Mã giảm giá" :required="true">
            <a-input v-model="formModel.discountCode" placeholder="Nhập mã giảm giá" :max-length="50" show-word-limit>
              <template #suffix>
                <a-button type="text" size="mini" @click="generateCode">
                  <template #icon>
                    <IconRefresh />
                  </template>
                </a-button>
              </template>
            </a-input>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item field="percentage" label="Phần trăm giảm giá" :required="true">
            <a-input-number
              v-model="formModel.percentage"
              placeholder="Nhập % giảm giá"
              :min="1"
              :max="100"
              :precision="0"
              style="width: 100%"
            >
              <template #suffix>%</template>
            </a-input-number>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item field="timeRange" label="Thời gian áp dụng" :required="true">
            <a-range-picker
              v-model="formModel.timeRange"
              show-time
              :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
              format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
              :disabled-date="disabledDate"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item field="description" label="Mô tả">
        <a-textarea v-model="formModel.description" placeholder="Nhập mô tả cho đợt giảm giá" :max-length="500" :rows="4" show-word-limit />
      </a-form-item>

      <a-divider>Xem trước</a-divider>
      <div class="preview-section">
        <a-descriptions :column="2" size="small" bordered>
          <a-descriptions-item label="Tên đợt giảm giá">
            {{ formModel.discountName || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="Mã giảm giá">
            <a-tag v-if="formModel.discountCode" color="blue">
              {{ formModel.discountCode }}
            </a-tag>
            <span v-else>-</span>
          </a-descriptions-item>
          <a-descriptions-item label="Phần trăm giảm">
            <a-tag v-if="formModel.percentage" color="green">{{ formModel.percentage }}%</a-tag>
            <span v-else>-</span>
          </a-descriptions-item>
          <a-descriptions-item label="Thời gian áp dụng">
            <div v-if="formModel.timeRange && formModel.timeRange.length === 2">
              <div>
                <strong>Từ:</strong>
                {{ formatPreviewDate(formModel.timeRange[0]) }}
              </div>
              <div>
                <strong>Đến:</strong>
                {{ formatPreviewDate(formModel.timeRange[1]) }}
              </div>
            </div>
            <span v-else>-</span>
          </a-descriptions-item>
          <a-descriptions-item label="Mô tả" :span="2">
            {{ formModel.description || '-' }}
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
import {
  createDiscount,
  updateDiscount,
  type CreateDiscountRequest,
  type DiscountResponse,
  type UpdateDiscountRequest,
} from '@/api/discount/discountApi'
import { FormInstance, Message } from '@arco-design/web-vue'
import { IconRefresh } from '@arco-design/web-vue/es/icon'
import { computed, nextTick, reactive, ref, watch } from 'vue'

interface FormData {
  id?: string
  discountName: string
  discountCode: string
  percentage: number
  startDate: number // Dùng cho API request
  endDate: number // Dùng cho API request
  description?: string
  timeRange?: [Date, Date] // Chỉ dùng cho UI
}

interface Props {
  visible: boolean
  formData: Partial<DiscountResponse>
  isEdit: boolean
}

interface Emits {
  (e: 'update:visible', visible: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()
const submitLoading = ref(false)

const modalVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value),
})

const formModel = reactive<FormData>({
  discountName: '',
  discountCode: '',
  percentage: 1,
  startDate: 0,
  endDate: 0,
  description: '',
  timeRange: undefined,
})

const formRules = {
  discountName: [
    { required: true, message: 'Vui lòng nhập tên đợt giảm giá' },
    { max: 100, message: 'Tên đợt giảm giá không được vượt quá 100 ký tự' },
  ],
  discountCode: [
    { required: true, message: 'Vui lòng nhập mã giảm giá' },
    { max: 50, message: 'Mã giảm giá không được vượt quá 50 ký tự' },
    {
      validator: (value: string, callback: (error?: string) => void) => {
        if (value && !/^[A-Z0-9]+$/.test(value)) {
          callback('Mã giảm giá chỉ được chứa chữ hoa và số')
        } else {
          callback()
        }
      },
    },
  ],
  percentage: [
    { required: true, message: 'Vui lòng nhập phần trăm giảm giá' },
    { type: 'number', min: 1, max: 100, message: 'Phần trăm giảm giá phải từ 1 đến 100' },
  ],
  timeRange: [
    {
      required: true,
      validator: (value: [Date, Date] | undefined, callback: (error?: string) => void) => {
        if (!value || !Array.isArray(value) || value.length !== 2) {
          callback('Vui lòng chọn thời gian áp dụng')
          return
        }

        // Chuyển sang timestamp để chắc chắn so sánh chính xác
        const start = new Date(value[0]).getTime()
        const end = new Date(value[1]).getTime()

        if (Number.isNaN(start) || Number.isNaN(end)) {
          callback('Thời gian không hợp lệ')
          return
        }

        if (start >= end) {
          callback('Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc')
          return
        }

        callback()
      },
    },
  ],
}

const generateCode = () => {
  const timestamp = Date.now().toString().slice(-6)
  const random = Math.random().toString(36).substring(2, 6).toUpperCase()
  formModel.discountCode = `PROMO${timestamp}${random}`
}

const resetForm = () => {
  formModel.discountName = ''
  formModel.discountCode = ''
  formModel.percentage = 1
  formModel.startDate = 0
  formModel.endDate = 0
  formModel.description = ''
  formModel.timeRange = undefined

  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

// Watch modal visibility để xử lý khởi tạo giá trị
watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      if (!props.isEdit) {
        resetForm()
        generateCode()
        // Khởi tạo timeRange mặc định khi mở modal tạo mới
        const now = new Date()
        const tomorrow = new Date(now.getTime() + 24 * 60 * 60 * 1000)
        formModel.timeRange = [now, tomorrow]
        // Cập nhật startDate và endDate
        formModel.startDate = now.getTime()
        formModel.endDate = tomorrow.getTime()
      } else {
        // Gán lại giá trị cho formModel khi edit
        Object.assign(formModel, props.formData)
        // Chuyển đổi startDate và endDate thành timeRange để hiển thị trong UI
        if (props.formData.startTime && props.formData.endTime) {
          formModel.timeRange = [new Date(props.formData.startTime), new Date(props.formData.endTime)]
        }
        nextTick(() => {
          formRef.value?.clearValidate()
        })
      }
    }
  }
)

// Watch formModel.timeRange để cập nhật startDate và endDate
watch(
  () => formModel.timeRange,
  (newTimeRange) => {
    if (newTimeRange && Array.isArray(newTimeRange) && newTimeRange.length === 2) {
      const [startDate, endDate] = newTimeRange
      if (startDate instanceof Date && endDate instanceof Date) {
        formModel.startDate = startDate.getTime()
        formModel.endDate = endDate.getTime()
      } else {
        formModel.startDate = 0
        formModel.endDate = 0
      }
    } else {
      formModel.startDate = 0
      formModel.endDate = 0
    }
  },
  { deep: true }
)

const formatPreviewDate = (date: Date): string => {
  if (!date || !(date instanceof Date)) return '-'
  return date.toLocaleString('vi-VN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  })
}

const disabledDate = (current: Date) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return current < today
}

const handleSubmit = async () => {
  try {
    const valid = await formRef.value?.validate()
    if (!valid) return false

    submitLoading.value = true

    if (!formModel.timeRange || formModel.timeRange.length !== 2) {
      Message.error('Vui lòng chọn thời gian áp dụng')
      return false
    }

    // Chắc chắn lấy timestamp chính xác
    formModel.startDate = formModel.timeRange[0]?.valueOf()
    formModel.endDate = formModel.timeRange[1]?.valueOf()

    if (props.isEdit && formModel.id) {
      const updateData: UpdateDiscountRequest = {
        id: formModel.id,
        discountName: formModel.discountName,
        discountCode: formModel.discountCode,
        percentage: formModel.percentage,
        startDate: formModel.startDate,
        endDate: formModel.endDate,
        description: formModel.description || '',
      }

      const response = await updateDiscount(updateData)

      if (response) {
        Message.success('Cập nhật đợt giảm giá thành công')
        emit('success') // 🔹 quan trọng: emit để table reload
        emit('update:visible', false) // 🔹 đóng modal
      }
    } else {
      const createData: CreateDiscountRequest = {
        discountName: formModel.discountName,
        discountCode: formModel.discountCode,
        percentage: formModel.percentage,
        startDate: formModel.startDate,
        endDate: formModel.endDate,
        description: formModel.description || '',
      }

      const response = await createDiscount(createData)

      if (response) {
        Message.success('Tạo đợt giảm giá thành công')
        emit('success') // 🔹 emit để table reload
        emit('update:visible', false) // 🔹 đóng modal
        resetForm() // 🔹 reset form
      }
    }

    return true
  } catch (error: any) {
    let errorMessage = 'Có lỗi xảy ra khi xử lý yêu cầu'
    const responseData = error?.response?.data

    if (responseData?.message) {
      errorMessage = responseData.message
    } else if (Array.isArray(responseData?.errors) && responseData.errors.length > 0) {
      const [firstError] = responseData.errors
      errorMessage = firstError
    } else if (error?.message) {
      errorMessage = error.message
    }

    Message.error(errorMessage)
    return false
  } finally {
    submitLoading.value = false
  }
}

const handleCancel = () => {
  formRef.value?.clearValidate()
  emit('update:visible', false)
}
</script>

<style scoped lang="less">
.preview-section {
  background-color: var(--color-fill-1);
  padding: 16px;
  border-radius: 4px;
  margin-top: 16px;
}

:deep(.arco-descriptions-item-label) {
  font-weight: 600;
}

:deep(.arco-input-number) {
  width: 100%;
}

:deep(.arco-range-picker) {
  width: 100%;
}
</style>
