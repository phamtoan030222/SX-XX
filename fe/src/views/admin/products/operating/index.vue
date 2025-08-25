<script setup lang="tsx">
import { onMounted, reactive, ref, watch } from 'vue'
import { Message, type FormInstance, type FieldRule, Switch } from '@arco-design/web-vue'
import {
  getAllOperatingSystems,
  updateOperatingSystemStatus,
  createOperatingSystem,
  updateOperatingSystem,
  type OperatingSystemResponse,
  type ParamsGetOperatingSystem,
  type CreateOperatingSystemRequest,
} from '@/api/operatingApi'

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

const data = ref<OperatingSystemResponse[]>([])

// ====== Form & Modal ======
const modalVisible = ref(false)
const isEdit = ref(false)
const selectedOS = ref<OperatingSystemResponse | null>(null)

const formRef = ref<FormInstance>()
const formState = reactive<CreateOperatingSystemRequest>({
  name: '',
  code: '',
  version: '',
  description: '',
})

const rules: Record<string, FieldRule[]> = {
  name: [{ required: true, message: 'Vui lòng nhập tên hệ điều hành', trigger: ['blur', 'submit'] }],
  code: [{ required: true, message: 'Vui lòng nhập mã hệ điều hành', trigger: ['blur', 'submit'] }],
  version: [{ required: true, message: 'Vui lòng nhập phiên bản', trigger: ['blur', 'submit'] }],
}

// ====== Methods ======
async function fetchOperatingSystems() {
  const params: ParamsGetOperatingSystem = {
    page: pagination.current,
    size: pagination.pageSize,
    name: search.value || undefined,
  }
  try {
    const res = await getAllOperatingSystems(params)
    data.value = res.items.map((item, index) => ({
      ...item,
      stt: (pagination.current - 1) * pagination.pageSize + index + 1,
    }))
    pagination.total = res.totalItems
  } catch {
    Message.error('Lấy danh sách hệ điều hành thất bại')
  }
}

function openCreateModal() {
  isEdit.value = false
  selectedOS.value = null
  Object.assign(formState, {
    name: '',
    code: '',
    version: '',
    description: '',
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

function openEditModal(os: OperatingSystemResponse) {
  isEdit.value = true
  selectedOS.value = os
  Object.assign(formState, {
    name: os.name,
    code: os.code,
    version: os.version,
    description: os.description,
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

const handleOk = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value && selectedOS.value) {
      await updateOperatingSystem(selectedOS.value.id, formState)
      Message.success('Cập nhật hệ điều hành thành công')
    } else {
      await createOperatingSystem(formState)
      Message.success('Thêm mới hệ điều hành thành công')
    }
    modalVisible.value = false
    fetchOperatingSystems()
  } catch (err: any) {
    Message.error(err?.message || 'Đã có lỗi xảy ra')
  }
}

const handleCancel = () => {
  modalVisible.value = false
}

async function toggleStatus(record: OperatingSystemResponse, checked: boolean) {
  try {
    await updateOperatingSystemStatus(record.id, checked ? 'ACTIVE' : 'INACTIVE')
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
  { title: 'Mã', dataIndex: 'code' },
  { title: 'Phiên bản', dataIndex: 'version' },
  { title: 'Mô tả', dataIndex: 'description' },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    render: ({ record }: { record: OperatingSystemResponse }) => (
      <Switch modelValue={record.status === 'ACTIVE'} onChange={(val: boolean) => toggleStatus(record, val)} />
    ),
  },
  {
    title: 'Hành động',
    dataIndex: 'action',
    render: ({ record }: { record: OperatingSystemResponse }) => (
      <a-button type="text" onClick={() => openEditModal(record)}>
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
    fetchOperatingSystems()
  }, 500) as unknown as number
})

// ====== Mounted ======
onMounted(() => {
  fetchOperatingSystems()
})
</script>

<template>
  <div class="container">
    <Breadcrumb :items="['menu.products', 'menu.products.operating']" />

    <a-card class="general-card" :title="$t('menu.products.operating')">
      <a-row :gutter="16" align="middle" style="margin-bottom: 16px">
        <!-- Input tìm kiếm -->
        <a-col :span="8">
          <a-form-item label="Tên hệ điều hành" labelAlign="left" labelCol="{ span: 24 }">
            <a-input v-model="search" :placeholder="$t('search.placeholder')" allow-clear />
          </a-form-item>
        </a-col>

        <!-- Nút bên phải -->
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
            fetchOperatingSystems()
          }
        "
        @page-size-change="
          (size) => {
            pagination.pageSize = size
            pagination.current = 1
            fetchOperatingSystems()
          }
        "
      />
    </a-card>

    <!-- Modal Thêm / Sửa hệ điều hành -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? 'Chỉnh sửa hệ điều hành' : 'Thêm mới hệ điều hành'"
      @ok="handleOk"
      @cancel="handleCancel"
      width="700px"
      unmount-on-close
    >
      <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <!-- Tên & Mã -->
          <a-col :span="12">
            <a-form-item field="name" label="Tên">
              <a-input v-model="formState.name" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="code" label="Mã">
              <a-input v-model="formState.code" />
            </a-form-item>
          </a-col>

          <!-- Phiên bản -->
          <a-col :span="24">
            <a-form-item field="version" label="Phiên bản">
              <a-input v-model="formState.version" />
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
