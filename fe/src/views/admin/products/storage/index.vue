<script setup lang="tsx">
import { onMounted, reactive, ref, watch } from 'vue'
import { Message, type FormInstance, type FieldRule, Switch } from '@arco-design/web-vue'
import {
  getAllHardDrives,
  updateHardDriveStatus,
  createHardDrive,
  updateHardDrive,
  type HardDriveResponse,
  type ParamsGetHardDrive,
  type CreateHardDriveRequest,
} from '@/api/hardDriveApi'

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

const data = ref<HardDriveResponse[]>([])

// ====== Form & Modal ======
const modalVisible = ref(false)
const isEdit = ref(false)
const selectedDrive = ref<HardDriveResponse | null>(null)

const formRef = ref<FormInstance>()
const formState = reactive<CreateHardDriveRequest>({
  name: '',
  brand: '',
  type: '',
  typeConnect: '',
  capacity: 256,
  readSpeed: 500,
  writeSpeed: 450,
  cacheMemory: 32,
  physicalSize: 2.5,
  description: '',
})

const rules: Record<string, FieldRule[]> = {
  name: [{ required: true, message: 'Vui lòng nhập tên ổ cứng', trigger: ['blur', 'submit'] }],
  brand: [{ required: true, message: 'Vui lòng nhập hãng', trigger: ['blur', 'submit'] }],
  type: [{ required: true, message: 'Vui lòng nhập loại ổ cứng', trigger: ['blur', 'submit'] }],
  typeConnect: [{ required: true, message: 'Vui lòng nhập loại kết nối', trigger: ['blur', 'submit'] }],
}

// ====== Methods ======
async function fetchHardDrives() {
  const params: ParamsGetHardDrive = {
    page: pagination.current,
    size: pagination.pageSize,
    name: search.value || undefined,
  }
  try {
    const res = await getAllHardDrives(params)
    data.value = res.items.map((item, index) => ({
      ...item,
      stt: (pagination.current - 1) * pagination.pageSize + index + 1,
    }))
    pagination.total = res.totalItems
  } catch {
    Message.error('Lấy danh sách ổ cứng thất bại')
  }
}

function openCreateModal() {
  isEdit.value = false
  selectedDrive.value = null
  Object.assign(formState, {
    name: '',
    brand: '',
    type: '',
    typeConnect: '',
    capacity: 256,
    readSpeed: 500,
    writeSpeed: 450,
    cacheMemory: 32,
    physicalSize: 2.5,
    description: '',
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

function openEditModal(drive: HardDriveResponse) {
  isEdit.value = true
  selectedDrive.value = drive
  Object.assign(formState, {
    name: drive.name,
    brand: drive.brand,
    type: drive.type,
    typeConnect: drive.typeConnect,
    capacity: drive.capacity,
    readSpeed: drive.readSpeed,
    writeSpeed: drive.writeSpeed,
    cacheMemory: drive.cacheMemory,
    physicalSize: drive.physicalSize,
    description: drive.description,
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

const handleOk = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value && selectedDrive.value) {
      await updateHardDrive(selectedDrive.value.id, formState)
      Message.success('Cập nhật ổ cứng thành công')
    } else {
      await createHardDrive(formState)
      Message.success('Thêm mới ổ cứng thành công')
    }
    modalVisible.value = false
    fetchHardDrives()
  } catch (err: any) {
    Message.error(err?.message || 'Đã có lỗi xảy ra')
  }
}

const handleCancel = () => {
  modalVisible.value = false
}

async function toggleStatus(record: HardDriveResponse, checked: boolean) {
  try {
    await updateHardDriveStatus(record.id, checked ? 'ACTIVE' : 'INACTIVE')
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
  { title: 'Loại', dataIndex: 'type' },
  { title: 'Loại kết nối', dataIndex: 'typeConnect' },
  { title: 'Dung lượng (GB)', dataIndex: 'capacity' },
  { title: 'Tốc độ đọc (MB/s)', dataIndex: 'readSpeed' },
  { title: 'Tốc độ ghi (MB/s)', dataIndex: 'writeSpeed' },
  { title: 'Cache (MB)', dataIndex: 'cacheMemory' },
  { title: 'Kích thước (inch)', dataIndex: 'physicalSize' },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    render: ({ record }: { record: HardDriveResponse }) => (
      <Switch modelValue={record.status === 'ACTIVE'} onChange={(val: boolean) => toggleStatus(record, val)} />
    ),
  },
  {
    title: 'Hành động',
    dataIndex: 'action',
    render: ({ record }: { record: HardDriveResponse }) => (
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
    fetchHardDrives()
  }, 500) as unknown as number
})

// ====== Mounted ======
onMounted(() => {
  fetchHardDrives()
})
</script>

<template>
  <div class="container">
    <Breadcrumb :items="['menu.products', 'menu.products.hardDrive']" />

    <a-card class="general-card" :title="$t('menu.products.hardDrive')">
      <a-row :gutter="16" align="middle" style="margin-bottom: 16px">
        <!-- Input tìm kiếm -->
        <a-col :span="8">
          <a-form-item label="Tên ổ" labelAlign="left" labelCol="{ span: 24 }">
            <a-input v-model="search" :placeholder="$t('search.placeholder')" allow-clear />
          </a-form-item>
        </a-col>

        <!-- Hai nút về bên phải -->
        <a-col :span="16" style="text-align: right">
          <a-button @click="() => Message.info('Export demo')" style="margin-right: 8px">
            <template #icon>
              <icon-download />
            </template>
            {{ $t('export.excel') }}
          </a-button>
          <a-button @click="openCreateModal" class="active">
            <template #icon>
              <icon-plus />
            </template>
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
            fetchHardDrives()
          }
        "
        @page-size-change="
          (size) => {
            pagination.pageSize = size
            pagination.current = 1
            fetchHardDrives()
          }
        "
      />
    </a-card>

    <!-- Modal Thêm / Sửa ổ cứng -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? 'Chỉnh sửa ổ cứng' : 'Thêm mới ổ cứng'"
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

          <!-- Loại & Kết nối -->
          <a-col :span="12">
            <a-form-item field="type" label="Loại">
              <a-input v-model="formState.type" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="typeConnect" label="Loại kết nối">
              <a-input v-model="formState.typeConnect" />
            </a-form-item>
          </a-col>

          <!-- Dung lượng & Tốc độ -->
          <a-col :span="8">
            <a-form-item field="capacity" label="Dung lượng (GB)">
              <a-input-number v-model="formState.capacity" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item field="readSpeed" label="Đọc (MB/s)">
              <a-input-number v-model="formState.readSpeed" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item field="writeSpeed" label="Ghi (MB/s)">
              <a-input-number v-model="formState.writeSpeed" style="width: 100%" />
            </a-form-item>
          </a-col>

          <!-- Cache & Size -->
          <a-col :span="12">
            <a-form-item field="cacheMemory" label="Cache (MB)">
              <a-input-number v-model="formState.cacheMemory" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="physicalSize" label="Kích thước (inch)">
              <a-input-number v-model="formState.physicalSize" style="width: 100%" />
            </a-form-item>
          </a-col>

          <!-- Mô tả -->
          <a-col :span="24">
            <a-form-item field="description" label="Mô tả">
              <a-input v-model="formState.description" type="textarea" rows="3" />
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
.action-icon {
  margin-left: 12px;
  cursor: pointer;
}
.active {
  color: #0960bd;
  background-color: #e3f4fc;
}
</style>
