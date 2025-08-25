<script setup lang="tsx">
import { onMounted, reactive, ref, watch } from 'vue'
import { Message, type FormInstance, type FieldRule, Switch } from '@arco-design/web-vue'
import {
  getAllBatteries,
  updateBatteryStatus,
  createBattery,
  updateBattery,
  type BatteryResponse,
  type ParamsGetBattery,
  type CreateBatteryRequest,
} from '@/api/batteryApi'

// ====== State ======
const search = ref('')
const selectedKeys = ref<(string | number)[]>([])

const rowSelection = reactive({
  type: 'checkbox',
  showCheckedAll: true,
  onlyCurrent: false,
})

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
})

const data = ref<BatteryResponse[]>([])

// ====== Form & Modal ======
const modalVisible = ref(false)
const isEdit = ref(false)
const selectedBattery = ref<BatteryResponse | null>(null)

const formRef = ref<FormInstance>()
const formState = reactive<CreateBatteryRequest>({
  name: '',
  brand: '',
  type: 'LI_ION',
  capacity: 4000,
  voltage: 0,
  chargingCycles: 0,
  technolyCharging: 'STANDARD',
  description: '',
})

const rules: Record<string, FieldRule[]> = {
  name: [{ required: true, message: 'Vui lòng nhập tên pin', trigger: ['blur', 'submit'] }],
  brand: [{ required: true, message: 'Vui lòng nhập hãng', trigger: ['blur', 'submit'] }],
  type: [{ required: true, message: 'Vui lòng chọn loại pin', trigger: ['blur', 'submit'] }],
  technolyCharging: [{ required: true, message: 'Vui lòng chọn công nghệ sạc', trigger: ['blur', 'submit'] }],
  capacity: [{ required: true, message: 'Vui lòng nhập dung lượng', trigger: ['blur', 'submit'] }],
}

// ====== Methods ======
async function fetchBatteries() {
  const params: ParamsGetBattery = {
    page: pagination.current,
    size: pagination.pageSize,
    name: search.value || undefined,
  }
  try {
    const res = await getAllBatteries(params)
    data.value = res.items.map((item, index) => ({
      ...item,
      stt: (pagination.current - 1) * pagination.pageSize + index + 1,
    }))
    pagination.total = res.totalItems
  } catch {
    Message.error('Lấy danh sách pin thất bại')
  }
}

function openCreateModal() {
  isEdit.value = false
  selectedBattery.value = null
  Object.assign(formState, {
    name: '',
    brand: '',
    type: 'LI_ION',
    capacity: 4000,
    voltage: 0,
    chargingCycles: 0,
    technolyCharging: 'STANDARD',
    description: '',
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

function openEditModal(battery: BatteryResponse) {
  isEdit.value = true
  selectedBattery.value = battery
  Object.assign(formState, {
    name: battery.name,
    brand: battery.brand,
    type: battery.type,
    capacity: battery.capacity,
    voltage: battery.voltage,
    chargingCycles: battery.chargingCycles,
    technolyCharging: battery.technolyCharging,
    description: battery.description,
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

const handleOk = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value && selectedBattery.value) {
      await updateBattery(selectedBattery.value.id, formState)
      Message.success('Cập nhật pin thành công')
    } else {
      await createBattery(formState)
      Message.success('Thêm mới pin thành công')
    }
    modalVisible.value = false
    fetchBatteries()
  } catch (err: any) {
    Message.error(err?.message || 'Đã có lỗi xảy ra')
  }
}

const handleCancel = () => {
  modalVisible.value = false
}

async function toggleStatus(record: BatteryResponse, checked: boolean) {
  try {
    await updateBatteryStatus(record.id, checked ? 'ACTIVE' : 'INACTIVE')
    record.status = checked ? 'ACTIVE' : 'INACTIVE'
    Message.success('Cập nhật trạng thái thành công')
  } catch {
    Message.error('Cập nhật trạng thái thất bại')
  }
}

// ====== Columns ======
const columns = [
  { title: 'STT', dataIndex: 'stt' },
  { title: 'Tên', dataIndex: 'name' },
  { title: 'Hãng', dataIndex: 'brand' },
  { title: 'Công nghệ sạc', dataIndex: 'technolyCharging' },
  { title: 'Dung lượng (mAh)', dataIndex: 'capacity' },
  { title: 'Loại pin', dataIndex: 'type' },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    render: ({ record }: { record: BatteryResponse }) => (
      <Switch modelValue={record.status === 'ACTIVE'} onChange={(val: boolean) => toggleStatus(record, val)} />
    ),
  },
  {
    title: 'Hành động',
    dataIndex: 'action',
    render: ({ record }: { record: BatteryResponse }) => (
      <a-button type='text' onClick={() => openEditModal(record)}>
        Chỉnh sửa
      </a-button>
    ),
  },
]

// ====== Search debounce ======
let debounceTimer: number | null = null
watch(search, () => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    pagination.current = 1
    fetchBatteries()
  }, 500) as unknown as number
})

// ====== Mounted ======
onMounted(() => {
  fetchBatteries()
})

// ====== Options cho combobox ======
const typeOptions = [
  { label: 'Li-ion', value: 'LI_ION' },
  { label: 'Li-po', value: 'LI_PO' },
]

const technolyChargingOptions = [
  { label: 'STANDARD', value: 'STANDARD' },
  { label: 'FAST', value: 'FAST' },
  { label: 'WIRELESS', value: 'WIRELESS' },
  { label: 'REVERSE', value: 'REVERSE' },
  { label: 'SMART', value: 'SMART' },
]
</script>

<template>
  <div class="container">
    <Breadcrumb :items="['menu.products', 'menu.products.battery']" />

    <a-card class="general-card" :title="$t('menu.products.battery')">
      <a-row :gutter="16" align="middle" style="margin-bottom: 16px">
        <!-- Input tìm kiếm -->
        <a-col :span="8">
          <a-form-item label="Tên pin" labelAlign="left" labelCol="{ span: 24 }">
            <a-input v-model="search" :placeholder="$t('search.placeholder')" allow-clear />
          </a-form-item>
        </a-col>

        <!-- Nút bên phải -->
        <a-col :span="16" style="text-align: right">
          <a-button @click="() => Message.info('Export demo')" style="margin-right: 8px">
            <template #icon><icon-download /></template>
            {{ $t('export.excel') }}
          </a-button>
          <a-button @click="openCreateModal" class="active">
            <template #icon><icon-plus /></template>
            {{ $t('button.addNew') }}
          </a-button>
        </a-col>
      </a-row>

      <a-divider style="margin-top: 10px" />

      <a-table
        row-key="id"
        :columns="columns"
        :data="data"
        :row-selection="rowSelection"
        v-model:selectedKeys="selectedKeys"
        :pagination="{
          current: pagination.current,
          pageSize: pagination.pageSize,
          total: pagination.total,
          showTotal: true,
          showJumper: true,
          showPageSize: true,
          pageSizeOptions: [5, 10, 20, 40],
        }"
        @page-change="
          (page) => {
            pagination.current = page
            fetchBatteries()
          }
        "
        @page-size-change="
          (size) => {
            pagination.pageSize = size
            pagination.current = 1
            fetchBatteries()
          }
        "
      />
    </a-card>

    <!-- Modal Thêm / Sửa pin -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? 'Chỉnh sửa pin' : 'Thêm mới pin'"
      @ok="handleOk"
      @cancel="handleCancel"
      width="700px"
      unmount-on-close
    >
      <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <!-- Tên & Hãng -->
          <a-col :span="12">
            <a-form-item field="name" label="Tên">
              <a-input v-model="formState.name" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="brand" label="Hãng">
              <a-input v-model="formState.brand" />
            </a-form-item>
          </a-col>

          <!-- Loại pin & Công nghệ sạc -->
          <a-col :span="12">
            <a-form-item field="type" label="Loại pin">
              <a-select v-model="formState.type" :options="typeOptions" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="technolyCharging" label="Công nghệ sạc">
              <a-select v-model="formState.technolyCharging" :options="technolyChargingOptions" />
            </a-form-item>
          </a-col>

          <!-- Dung lượng & Mô tả -->
          <a-col :span="12">
            <a-form-item field="capacity" label="Dung lượng (mAh)">
              <a-input-number v-model="formState.capacity" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
:deep(.arco-table-th) {
  &:last-child {
    .arco-table-th-item-title {
      margin-left: 16px;
    }
  }
}
.active {
  color: #0960bd;
  background-color: #e3f4fc;
}
</style>
