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
/* eslint-disable */
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
  startDate: number 
  endDate: number 
  description?: string
  timeRange?: [Date, Date] 
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

watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      if (!props.isEdit) {
        resetForm()
        generateCode()
        const now = new Date()
        const tomorrow = new Date(now.getTime() + 24 * 60 * 60 * 1000)
        formModel.timeRange = [now, tomorrow]
        formModel.startDate = now.getTime()
        formModel.endDate = tomorrow.getTime()
      } else {
        Object.assign(formModel, props.formData)
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


watch(
  () => formModel.timeRange,
  (newTimeRange) => {
    console.log('Watch timeRange:', newTimeRange, typeof newTimeRange?.[0])
    
    if (newTimeRange && Array.isArray(newTimeRange) && newTimeRange.length === 2) {
      const [start, end] = newTimeRange
      
      let startDate: Date
      let endDate: Date
      
      if (typeof start === 'string') {
        startDate = new Date(start)
      } else {
        startDate = start
      }
      
      if (typeof end === 'string') {
        endDate = new Date(end)
      } else {
        endDate = end
      }
      
      if (startDate instanceof Date && !isNaN(startDate.getTime()) && 
          endDate instanceof Date && !isNaN(endDate.getTime())) {
        formModel.startDate = startDate.getTime()
        formModel.endDate = endDate.getTime()
        console.log('Updated timestamps:', formModel.startDate, formModel.endDate)
      } else {
        console.log('Invalid dates')
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

const formatPreviewDate = (date: Date | string): string => {
  console.log('formatPreviewDate input:', date, typeof date)
  
  let dateObj: Date
  
  if (typeof date === 'string') {
    dateObj = new Date(date)
  } else if (date instanceof Date) {
    dateObj = date
  } else {
    return '-'
  }
  
  if (isNaN(dateObj.getTime())) {
    console.log('Invalid date:', date)
    return '-'
  }
  
  const result = dateObj.toLocaleString('vi-VN', {
    year: 'numeric',
    month: '2-digit', 
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  })
  
  console.log('formatPreviewDate result:', result)
  return result
}

const disabledDate = (current: Date) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return current < today
}

const handleSubmit = async () => {
  try {
    submitLoading.value = true
    
    const validationErrors = await formRef.value?.validate()
    console.log('Form validation result:', validationErrors)
    
    if (validationErrors !== undefined) {
      console.log('❌ Form validation FAILED with errors:', validationErrors)
      return false
    }

    console.log('✅ Form validation PASSED')

    if (!formModel.timeRange || formModel.startDate === 0 || formModel.endDate === 0) {
      Message.error('Vui lòng chọn thời gian áp dụng hợp lệ')
      return false
    }
    
    const requestData = {
      discountName: formModel.discountName,
      discountCode: formModel.discountCode,
      percentage: formModel.percentage,
      startDate: formModel.startDate,  
      endDate: formModel.endDate,      
      description: formModel.description || '',
    }
    
    console.log('Request data:', requestData)
    
    if (props.isEdit) {
      const updateData: Omit<UpdateDiscountRequest, 'id'> = {
        discountName: formModel.discountName,
        discountCode: formModel.discountCode,
        percentage: formModel.percentage,
        startDate: formModel.startDate,
        endDate: formModel.endDate,
        description: formModel.description || '',
      }
      
      await updateDiscount(formModel.id!, updateData as UpdateDiscountRequest)
      Message.success('Cập nhật đợt giảm giá thành công')
    } else {
      const createData: CreateDiscountRequest = {
        discountName: formModel.discountName,
        discountCode: formModel.discountCode,
        percentage: formModel.percentage,
        startDate: formModel.startDate,  
        endDate: formModel.endDate,  
        description: formModel.description || '',
      }
      await createDiscount(createData)
      Message.success('Tạo đợt giảm giá thành công')
    }
    
    emit('success')
    emit('update:visible', false)
    return true
    
  } catch (error: any) {
    console.error('Submit error:', error)
    Message.error(error?.message || 'Có lỗi xảy ra, vui lòng thử lại')
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
