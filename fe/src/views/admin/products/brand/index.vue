<script setup lang="tsx">
import { onMounted, reactive, ref, watch } from 'vue'
import { Message, type FormInstance, type FieldRule, Switch } from '@arco-design/web-vue'
import {
  getAllBrands,
  updateBrandStatus,
  createBrand,
  updateBrand,
  type BrandResponse,
  type ParamsGetBrand,
  type CreateBrandRequest,
} from '@/api/brandApi'

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

const data = ref<BrandResponse[]>([])

// ====== Form & Modal ======
const modalVisible = ref(false)
const isEdit = ref(false)
const selectedBrand = ref<BrandResponse | null>(null)

const formRef = ref<FormInstance>()
const formState = reactive<CreateBrandRequest>({
  name: '',
  code: '',
})

const rules: Record<string, FieldRule[]> = {
  name: [{ required: true, message: 'Vui lòng nhập tên brand', trigger: ['blur', 'submit'] }],
  code: [{ required: true, message: 'Vui lòng nhập mã code', trigger: ['blur', 'submit'] }],
}

// ====== Methods ======
async function fetchBrands() {
  const params: ParamsGetBrand = {
    page: pagination.current,
    size: pagination.pageSize,
    name: search.value || undefined,
  }
  try {
    const res = await getAllBrands(params)
    data.value = res.items.map((item, index) => ({
      ...item,
      stt: (pagination.current - 1) * pagination.pageSize + index + 1,
    }))
    pagination.total = res.totalItems
  } catch {
    Message.error('Lấy danh sách brand thất bại')
  }
}

function openCreateModal() {
  isEdit.value = false
  selectedBrand.value = null
  Object.assign(formState, {
    name: '',
    code: '',
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

function openEditModal(brand: BrandResponse) {
  isEdit.value = true
  selectedBrand.value = brand
  Object.assign(formState, {
    name: brand.name,
    code: brand.code,
  })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

const handleOk = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value && selectedBrand.value) {
      await updateBrand(selectedBrand.value.id, formState)
      Message.success('Cập nhật brand thành công')
    } else {
      await createBrand(formState)
      Message.success('Thêm brand thành công')
    }
    modalVisible.value = false
    fetchBrands()
  } catch (err: any) {
    Message.error(err?.message || 'Đã có lỗi xảy ra')
  }
}

const handleCancel = () => {
  modalVisible.value = false
}

async function toggleStatus(record: BrandResponse, checked: boolean) {
  try {
    await updateBrandStatus(record.id)
    record.status = checked ? 'ACTIVE' : 'INACTIVE'
    Message.success('Cập nhật trạng thái thành công')
  } catch {
    Message.error('Cập nhật trạng thái thất bại')
  }
}

// ====== Columns ======
const columns = [
  { title: 'STT', dataIndex: 'stt' },
  { title: 'Tên Brand', dataIndex: 'name' },
  { title: 'Mã Code', dataIndex: 'code' },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    render: ({ record }: { record: BrandResponse }) => (
      <Switch modelValue={record.status === 'ACTIVE'} onChange={(val: boolean) => toggleStatus(record, val)} />
    ),
  },
  {
    title: 'Hành động',
    dataIndex: 'action',
    render: ({ record }: { record: BrandResponse }) => (
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
    fetchBrands()
  }, 500) as unknown as number
})

// ====== Mounted ======
onMounted(() => {
  fetchBrands()
})
</script>

<template>
  <div class="container">
    <Breadcrumb :items="['menu.products', 'menu.products.brand']" />

    <a-card class="general-card" :title="$t('menu.products.brand')">
      <a-row :gutter="16" align="middle" style="margin-bottom: 16px">
        <!-- Input tìm kiếm -->
        <a-col :span="8">
          <a-form-item label="Tên brand" labelAlign="left" labelCol="{ span: 24 }">
            <a-input v-model="search" :placeholder="$t('search.placeholder')" allow-clear />
          </a-form-item>
        </a-col>

        <!-- Nút bên phải -->
        <a-col :span="16" style="text-align: right">
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
            fetchBrands()
          }
        "
        @page-size-change="
          (size) => {
            pagination.pageSize = size
            pagination.current = 1
            fetchBrands()
          }
        "
      />
    </a-card>

    <!-- Modal Thêm / Sửa Brand -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? 'Chỉnh sửa Brand' : 'Thêm mới Brand'"
      @ok="handleOk"
      @cancel="handleCancel"
      width="500px"
      unmount-on-close
    >
      <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
        <a-form-item field="name" label="Tên Brand">
          <a-input v-model="formState.name" />
        </a-form-item>
        <a-form-item field="code" label="Mã Code">
          <a-input v-model="formState.code" />
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
</style>
