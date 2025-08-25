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
          <a-input v-model="detailScreen.code" placeholder="Nhập mã" />
        </a-form-item>
        <a-form-item label="Tên">
          <a-input v-model="detailScreen.name" placeholder="Nhập tên" />
        </a-form-item>
        <a-form-item label="Dạng tấm nền">
          <a-select v-model="detailScreen.panelType" placeholder="Chọn tấm nền">
            <a-option>LCD</a-option>
            <a-option>OLED</a-option>
            <a-option>LED_BACKLIT_LCD</a-option>
            <a-option>AMOLED</a-option>
            <a-option>MINI_LED</a-option>
            <a-option>MICRO_LED</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Độ phân giải">
          <a-select v-model="detailScreen.resolution" placeholder="Chọn độ phân giải ">
            <a-option value="1920x1080">1920 x 1080 pixels</a-option>
            <a-option value="1366x768">1366 x 768 pixels</a-option>
            <a-option value="2880x1800">2880 x 1800 pixels</a-option>
            <a-option value="3840x2160">3840 x 2160 pixels</a-option>
            <a-option value="2560x1440">2560 x 1440 pixels</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Kích thước màn hình">
          <a-select v-model="detailScreen.physicalSize" placeholder="Chọn kích thước màn hình">
            <a-option value="11.6">11.6 inch</a-option>
            <a-option value="13.3">13.3 inch</a-option>
            <a-option value="14">14 inch</a-option>
            <a-option value="15.6">15.6 inch</a-option>
            <a-option value="16">16 inch</a-option>
            <a-option value="17.3">17.3 inch</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Công nghệ">
          <a-textarea
            v-model="detailScreen.technology"
            placeholder="Mô tả công nghệ"
            :auto-size="{
              minRows: 3,
              maxRows: 6,
            }"
          ></a-textarea>
        </a-form-item>
      </a-form>
    </div>
    {{ detailScreen }}
  </a-modal>
</template>

<script setup lang="ts">
import { Ref, ref, watch } from 'vue'
import { modifyScreen, getScreenById, ADProductScreenDetailResponse } from '@/api/admin/product/screen.api'
import { Notification } from '@arco-design/web-vue'
import TypeScreenResolution from '@/constants/TypeScreenResolution'

const props = defineProps<{
  isOpen: boolean
  id: string | undefined
  isDetail: boolean
}>()

const emit = defineEmits(['success', 'close'])

const detailScreen: Ref<ADProductScreenDetailResponse> = ref({
  id: '',
  code: '',
  name: '',
  physicalSize: 0,
  resolution: '',
  panelType: '',
  technology: '',
})

const fetchDetailScreen = async () => {
  const res = await getScreenById(props.id as string)

  detailScreen.value = res.data
  detailScreen.value.resolution = TypeScreenResolution[res.data.resolution as keyof typeof TypeScreenResolution]
}

const resetField = () => {
  detailScreen.value = {
    id: '',
    code: '',
    name: '',
    physicalSize: 0,
    resolution: '',
    panelType: '',
    technology: '',
  }
}

watch(
  () => props.id,
  (newId) => {
    if (newId) fetchDetailScreen()
    else resetField()
  }
)

const handleClickCancel = () => {
  emit('close')
}

const handleClickOK = async () => {
  try {
    const res = await modifyScreen(detailScreen.value)

    if (res.success) Notification.success(props.id ? 'Cập nhật Screen thành công' : 'Thêm Screen thành công')
    else Notification.error(props.id ? 'Thêm Screen thành công' : 'Thêm Screen thất bại')
    resetField()
    emit('success')
  } catch (error) {
    console.log(error)
  }
}
</script>

<style scoped>
.container {
  max-height: 400px;
  overflow-y: auto;
}
</style>
