<script setup lang="tsx">
import { getAllColors, updateColorStatus, createColor, updateColor, type ColorResponse, type ParamsGetColor } from '@/api/colorApi'
import { Message, type FieldRule, type FormInstance, Switch } from '@arco-design/web-vue'
import { onMounted, reactive, ref, watch } from 'vue'

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
  pageSize: 5,
  total: 0,
})

const data = ref<ColorResponse[]>([])

// ====== Methods ======
async function fetchColors() {
  const params: ParamsGetColor = {
    page: pagination.current,
    size: pagination.pageSize,
    q: search.value,
  }
  try {
    const res = await getAllColors(params)
    data.value = res.items.map((item, index) => ({
      ...item,
      stt: (pagination.current - 1) * pagination.pageSize + index + 1,
    }))
    pagination.total = res.totalItems
  } catch {
    Message.error('Lấy danh sách màu thất bại')
  }
}

async function toggleStatus(record: ColorResponse, checked: boolean) {
  try {
    await updateColorStatus(record.id, checked ? 'ACTIVE' : 'INACTIVE')
    record.colorStatus = checked ? 'ACTIVE' : 'INACTIVE'
    Message.success('Cập nhật trạng thái thành công')
  } catch {
    Message.error('Cập nhật trạng thái thất bại')
  }
}

// ====== Modal & Form State ======
const modalVisible = ref(false)
const isEdit = ref(false)
const selectedColor = ref<ColorResponse | null>(null)

const formRef = ref<FormInstance>()
const formState = reactive({
  colorName: '',
  colorCode: '#000000',
})
const rules: Record<string, FieldRule[]> = {
  colorName: [
    {
      required: true,
      validator: (_, value) => {
        if (!String(value || '').trim()) {
          return Promise.reject(new Error('Tên màu không được để trống'))
        }
        return Promise.resolve()
      },
      trigger: ['blur', 'input', 'submit'],
    },
  ],
  colorCode: [{ required: true, message: 'Vui lòng chọn mã màu', trigger: ['change', 'submit'] }],
}

const loading = ref(false)

function openCreateModal() {
  isEdit.value = false
  selectedColor.value = null
  Object.assign(formState, { colorName: '', colorCode: '#000000' })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

function openEditModal(record: ColorResponse) {
  isEdit.value = true
  selectedColor.value = record
  Object.assign(formState, { colorName: record.colorName, colorCode: record.colorCode })
  formRef.value?.clearValidate()
  modalVisible.value = true
}

const handleOk = async () => {
  loading.value = true
  try {
    await formRef.value?.validate()
    const payload = {
      colorName: String(formState.colorName || '').trim(),
      colorCode: formState.colorCode,
    }

    if (isEdit.value && selectedColor.value) {
      await updateColor(selectedColor.value.id, payload)
      Message.success('Cập nhật màu sắc thành công')
    } else {
      await createColor(payload)
      Message.success('Thêm mới màu sắc thành công')
    }

    modalVisible.value = false
    fetchColors()
  } catch (err: any) {
    if (err?.message) {
      Message.error(err.message)
    } else {
      Message.error('Đã có lỗi xảy ra, vui lòng thử lại')
    }
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  modalVisible.value = false
}

// ====== Columns ======
const columns = [
  { title: 'STT', dataIndex: 'stt' },
  { title: 'Tên màu', dataIndex: 'colorName' },
  {
    title: 'Mã màu',
    dataIndex: 'colorCode',
    render: ({ record }: { record: ColorResponse }) => (
      <div
        style={{
          backgroundColor: record.colorCode,
          width: '30px',
          height: '30px',
          borderRadius: '4px',
          border: '1px solid #ddd',
        }}
      />
    ),
  },
  {
    title: 'Trạng thái',
    dataIndex: 'colorStatus',
    render: ({ record }: { record: ColorResponse }) => (
      <Switch modelValue={record.colorStatus === 'ACTIVE'} onChange={(checked: boolean) => toggleStatus(record, checked)} />
    ),
  },

  {
    title: 'Hành động',
    dataIndex: 'action',
    render: ({ record }: { record: ColorResponse }) => (
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
    fetchColors()
  }, 500) as unknown as number
})

// ====== Mounted ======
onMounted(() => {
  fetchColors()
})
</script>

<template>
  <div class="container">
    <Breadcrumb :items="['menu.products', 'menu.products.color']" />

    <a-card class="general-card" :title="$t('menu.products.color')">
      <a-row>
        <a-col :flex="1">
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
        </a-col>
      </a-row>

      <a-divider style="margin-top: 10px" />

      <a-col :span="24">
        <a-space direction="vertical" size="large" fill>
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
              // showJumper: true,
            }"
            @page-change="
              (page) => {
                pagination.current = page
                fetchColors()
              }
            "
          />
        </a-space>
      </a-col>
    </a-card>

    <!-- Modal thêm/sửa màu -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? 'Chỉnh sửa màu sắc' : 'Thêm mới màu sắc'"
      :ok-loading="loading"
      @ok="handleOk"
      @cancel="handleCancel"
      unmount-on-close
    >
      <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
        <a-form-item field="colorName" label="Tên màu">
          <input v-model="formState.colorName" placeholder="Nhập tên màu" />
        </a-form-item>
        <a-form-item field="colorCode" label="Mã màu">
          <input v-model="formState.colorCode" type="color" style="width: 60px; padding: 0; border: none" />
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
