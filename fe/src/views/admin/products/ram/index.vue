<script setup lang="tsx">
import { onMounted, reactive, ref, watch } from 'vue'
import { Message, type FormInstance, type FieldRule, Switch } from '@arco-design/web-vue'
import { getAllRams, updateRamStatus, createRam, updateRam, type RamResponse, type ParamsGetRam, type CreateRamRequest } from '@/api/ramApi'

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

const data = ref<RamResponse[]>([])

// ====== Form & Modal ======
const modalVisible = ref(false)
const isEdit = ref(false)
const selectedRam = ref<RamResponse | null>(null)

const formRef = ref<FormInstance>()
const formState = reactive<CreateRamRequest>({
  name: '',
  brand: '',
  type: '',
  capacity: 8,
  busSpeed: 2666,
  slotConFig: 1,
  maxSupported: 16,
  description: '',
})

const rules: Record<string, FieldRule[]> = {
  name: [{ required: true, message: 'Vui lòng nhập tên RAM', trigger: ['blur', 'submit'] }],
  brand: [{ required: true, message: 'Vui lòng nhập hãng', trigger: ['blur', 'submit'] }],
  type: [{ required: true, message: 'Vui lòng nhập loại RAM', trigger: ['blur', 'submit'] }],
  capacity: [{ required: true, message: 'Vui lòng nhập dung lượng', trigger: ['blur', 'submit'] }],
}

// ====== Methods ======
async function fetchRams() {
  const params: ParamsGetRam = {
    page: pagination.current,
    size: pagination.pageSize,
    name: search.value || undefined,
  }
  try {
    const res = await getAllRams(params)
    data.value = res.items.map((item, index) => ({
      ...item,
      stt: (pagination.current - 1) * pagination.pageSize + index + 1,
    }))
    pagination.total = res.totalItems
  } catch {
    Message.error('Lấy danh sách RAM thất bại')
  }
}

function openCreateModal() {
  isEdit.value = false
  selectedRam.value = null
  Object.assign(formState, {
    name: '',
    brand: '',
    type: '',
    capacity: 8,
    busSpeed: 2666,
    slotConFig: 1,
    maxSupported: 16,
    description: '',
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

function openEditModal(ram: RamResponse) {
  isEdit.value = true
  selectedRam.value = ram
  Object.assign(formState, {
    name: ram.name,
    brand: ram.brand,
    type: ram.type,
    capacity: ram.capacity,
    busSpeed: ram.busSpeed,
    slotConFig: ram.slotConFig,
    maxSupported: ram.maxSupported,
    description: ram.description,
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

const handleOk = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value && selectedRam.value) {
      await updateRam(selectedRam.value.id, formState)
      Message.success('Cập nhật RAM thành công')
    } else {
      await createRam(formState)
      Message.success('Thêm mới RAM thành công')
    }
    modalVisible.value = false
    fetchRams()
  } catch (err: any) {
    Message.error(err?.message || 'Đã có lỗi xảy ra')
  }
}

const handleCancel = () => {
  modalVisible.value = false
}

async function toggleStatus(record: RamResponse, checked: boolean) {
  try {
    await updateRamStatus(record.id, checked ? 'ACTIVE' : 'INACTIVE')
    record.status = checked ? 'ACTIVE' : 'INACTIVE'
    Message.success('Cập nhật trạng thái thành công')
  } catch {
    Message.error('Cập nhật trạng thái thất bại')
  }
}

// ====== Columns ======
const columns = [
  { title: 'STT', dataIndex: 'stt' },
  { title: 'Tên RAM', dataIndex: 'name' },
  { title: 'Hãng', dataIndex: 'brand' },
  { title: 'Loại', dataIndex: 'type' },
  { title: 'Dung lượng (GB)', dataIndex: 'capacity' },
  { title: 'Bus Speed (MHz)', dataIndex: 'busSpeed' },
  { title: 'Slot', dataIndex: 'slotConFig' },
  { title: 'Max hỗ trợ (GB)', dataIndex: 'maxSupported' },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    render: ({ record }: { record: RamResponse }) => (
      <Switch modelValue={record.status === 'ACTIVE'} onChange={(val: boolean) => toggleStatus(record, val)} />
    ),
  },
  {
    title: 'Hành động',
    dataIndex: 'action',
    render: ({ record }: { record: RamResponse }) => (
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
    fetchRams()
  }, 500) as unknown as number
})

// ====== Mounted ======
onMounted(() => {
  fetchRams()
})
</script>

<template>
  <div class="container">
    <Breadcrumb :items="['menu.products', 'menu.products.ram']" />

    <a-card class="general-card" :title="$t('menu.products.ram')">
      <a-row :gutter="16">
        <a-col :span="8">
          <a-input v-model="search" :placeholder="$t('search.placeholder')" allow-clear />
        </a-col>
        <a-col :span="8">
          <a-button @click="() => Message.info('Export demo')">
            <template #icon>
              <icon-download />
            </template>
            {{ $t('export.excel') }}
          </a-button>
        </a-col>
        <a-col :span="8">
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
            fetchRams()
          }
        "
        @page-size-change="
          (size) => {
            pagination.pageSize = size
            pagination.current = 1
            fetchRams()
          }
        "
      />
    </a-card>

    <!-- Modal Thêm / Sửa RAM -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? 'Chỉnh sửa RAM' : 'Thêm mới RAM'"
      @ok="handleOk"
      @cancel="handleCancel"
      unmount-on-close
      width="700px"
    >
      <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical" label-align="left">
        <a-row :gutter="16">
          <!-- Cột trái -->
          <a-col :span="12">
            <a-form-item field="name" label="Tên RAM">
              <a-input v-model="formState.name" />
            </a-form-item>
            <a-form-item field="type" label="Loại RAM">
              <a-input v-model="formState.type" />
            </a-form-item>
            <a-form-item field="capacity" label="Dung lượng (GB)">
              <a-input-number v-model="formState.capacity" style="width: 100%" />
            </a-form-item>
            <a-form-item field="slotConFig" label="Slot">
              <a-input-number v-model="formState.slotConFig" style="width: 100%" />
            </a-form-item>
          </a-col>

          <!-- Cột phải -->
          <a-col :span="12">
            <a-form-item field="brand" label="Hãng">
              <a-input v-model="formState.brand" />
            </a-form-item>
            <a-form-item field="busSpeed" label="Bus Speed (MHz)">
              <a-input-number v-model="formState.busSpeed" style="width: 100%" />
            </a-form-item>
            <a-form-item field="maxSupported" label="Max hỗ trợ (GB)">
              <a-input-number v-model="formState.maxSupported" style="width: 100%" />
            </a-form-item>
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
.setting {
  display: flex;
  align-items: center;
  width: 200px;
  .title {
    margin-left: 12px;
    cursor: pointer;
  }
}
</style>
