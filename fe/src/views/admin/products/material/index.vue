<script setup lang="tsx">
import { onMounted, reactive, ref, watch } from 'vue'
import { Message, type FormInstance, type FieldRule, Switch } from '@arco-design/web-vue'
import {
  getAllMaterials,
  updateMaterialStatus,
  createMaterial,
  updateMaterial,
  type MaterialResponse,
  type ParamsGetMaterial,
  type CreateMaterialRequest,
} from '@/api/materialApi'

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

const data = ref<MaterialResponse[]>([])

// ====== Form & Modal ======
const modalVisible = ref(false)
const isEdit = ref(false)
const selectedMaterial = ref<MaterialResponse | null>(null)

const formRef = ref<FormInstance>()
const formState = reactive<CreateMaterialRequest>({
  topCaseMaterial: '',
  bottomCaseMaterial: '',
  keyboardMaterial: '',
})

const rules: Record<string, FieldRule[]> = {
  topCaseMaterial: [{ required: true, message: 'Vui lòng nhập chất liệu mặt trên', trigger: ['blur', 'submit'] }],
  bottomCaseMaterial: [{ required: true, message: 'Vui lòng nhập chất liệu mặt dưới', trigger: ['blur', 'submit'] }],
  keyboardMaterial: [{ required: true, message: 'Vui lòng nhập chất liệu bàn phím', trigger: ['blur', 'submit'] }],
}

// ====== Methods ======
async function fetchMaterials() {
  const params: ParamsGetMaterial = {
    page: pagination.current,
    size: pagination.pageSize,
    name: search.value || undefined,
  }
  try {
    const res = await getAllMaterials(params)
    data.value = res.items.map((item, index) => ({
      ...item,
      stt: (pagination.current - 1) * pagination.pageSize + index + 1,
    }))
    pagination.total = res.totalItems
  } catch {
    Message.error('Lấy danh sách chất liệu thất bại')
  }
}

function openCreateModal() {
  isEdit.value = false
  selectedMaterial.value = null
  Object.assign(formState, {
    topCaseMaterial: '',
    bottomCaseMaterial: '',
    keyboardMaterial: '',
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

function openEditModal(material: MaterialResponse) {
  isEdit.value = true
  selectedMaterial.value = material
  Object.assign(formState, {
    topCaseMaterial: material.topCaseMaterial,
    bottomCaseMaterial: material.bottomCaseMaterial,
    keyboardMaterial: material.keyboardMaterial,
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

const handleOk = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value && selectedMaterial.value) {
      await updateMaterial(selectedMaterial.value.id, formState)
      Message.success('Cập nhật chất liệu thành công')
    } else {
      await createMaterial(formState)
      Message.success('Thêm mới chất liệu thành công')
    }
    modalVisible.value = false
    fetchMaterials()
  } catch (err: any) {
    Message.error(err?.message || 'Đã có lỗi xảy ra')
  }
}

const handleCancel = () => {
  modalVisible.value = false
}

async function toggleStatus(record: MaterialResponse, checked: boolean) {
  try {
    await updateMaterialStatus(record.id, checked ? 'ACTIVE' : 'INACTIVE')
    record.status = checked ? 'ACTIVE' : 'INACTIVE'
    Message.success('Cập nhật trạng thái thành công')
  } catch {
    Message.error('Cập nhật trạng thái thất bại')
  }
}

// ====== Columns ======
const columns = [
  { title: 'STT', dataIndex: 'stt' },
  { title: 'Mặt trên', dataIndex: 'topCaseMaterial' },
  { title: 'Mặt dưới', dataIndex: 'bottomCaseMaterial' },
  { title: 'Bàn phím', dataIndex: 'keyboardMaterial' },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    render: ({ record }: { record: MaterialResponse }) => (
      <Switch modelValue={record.status === 'ACTIVE'} onChange={(val: boolean) => toggleStatus(record, val)} />
    ),
  },
  {
    title: 'Hành động',
    dataIndex: 'action',
    render: ({ record }: { record: MaterialResponse }) => (
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
    fetchMaterials()
  }, 500) as unknown as number
})

// ====== Mounted ======
onMounted(() => {
  fetchMaterials()
})
</script>

<template>
  <div class="container">
    <Breadcrumb :items="['menu.products', 'menu.products.material']" />

    <a-card class="general-card" :title="$t('menu.products.material')">
      <a-row :gutter="16" justify="end">
        <a-col :span="8">
          <a-input v-model="search" placeholder="Nhập từ tên" allow-clear />
        </a-col>
        <a-col :span="8">
          <a-button @click="() => Message.info('Export demo')" style="float: right">
            <template #icon><icon-download /></template>
            {{ $t('export.excel') }}
          </a-button>
        </a-col>
        <a-col :span="8">
          <a-button @click="openCreateModal" class="active" style="float: right">
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
            fetchMaterials()
          }
        "
        @page-size-change="
          (size) => {
            pagination.pageSize = size
            pagination.current = 1
            fetchMaterials()
          }
        "
      />
    </a-card>

    <!-- Modal Thêm / Sửa Material -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? 'Chỉnh sửa chất liệu' : 'Thêm mới chất liệu'"
      @ok="handleOk"
      @cancel="handleCancel"
      unmount-on-close
      width="500px"
    >
      <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical" label-align="left">
        <a-form-item field="topCaseMaterial" label="Chất liệu mặt trên">
          <a-input v-model="formState.topCaseMaterial" placeholder="Nhập chất liệu mặt trên" />
        </a-form-item>
        <a-form-item field="bottomCaseMaterial" label="Chất liệu mặt dưới">
          <a-input v-model="formState.bottomCaseMaterial" placeholder="Nhập chất liệu mặt dưới" />
        </a-form-item>
        <a-form-item field="keyboardMaterial" label="Chất liệu bàn phím">
          <a-input v-model="formState.keyboardMaterial" placeholder="Nhập chất liệu bàn phím" />
        </a-form-item>
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
a-form-item {
  margin-bottom: 16px;
}
</style>
