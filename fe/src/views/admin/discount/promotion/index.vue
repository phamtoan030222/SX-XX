<template>
  <div class="container">
    <Breadcrumb :items="['Quản trị', 'Quản lý đợt giảm giá']" />
    <a-space direction="vertical" :size="16" fill>
      <a-card class="general-card" title="Quản lý đợt giảm giá">
        <template #extra>
          <a-space>
            <a-button type="primary" @click="handleCreate">
              <template #icon>
                <IconPlus />
              </template>
              Tạo đợt giảm giá
            </a-button>
          </a-space>
        </template>

        <!-- Search and Filter Section -->
        <div class="search-section">
          <a-row :gutter="16">
            <a-col :span="6">
              <a-input v-model="searchForm.q" placeholder="Tìm kiếm theo tên" allow-clear @press-enter="handleSearch">
                <template #prefix>
                  <IconSearch />
                </template>
              </a-input>
            </a-col>
            <a-col :span="6">
              <a-select v-model="searchForm.discountStatus" placeholder="Trạng thái" allow-clear @change="handleSearch">
                <a-option :value="1">Đang hoạt động</a-option>
                <a-option :value="0">Không hoạt động</a-option>
              </a-select>
            </a-col>
            <a-col :span="6">
              <a-space>
                <a-button type="primary" @click="handleSearch">Tìm kiếm</a-button>
                <a-button @click="handleReset">Đặt lại</a-button>
              </a-space>
            </a-col>
          </a-row>
        </div>
      </a-card>

      <!-- Discount Table -->
      <a-card class="general-card">
        <PromotionTable
          :loading="loading"
          :data="discountData"
          :pagination="pagination"
          @edit="handleEdit"
          @delete="handleDelete"
          @deactivate="handleDeactivate"
          @page-change="handlePageChange"
        />
      </a-card>
    </a-space>

    <!-- Create/Edit Modal -->
    <PromotionModal v-model:visible="modalVisible" :form-data="currentFormData" :is-edit="isEdit" @success="handleModalSuccess" />
  </div>
</template>

<script lang="ts" setup>
import {
  deactivateDiscount,
  deleteDiscount,
  getAllDiscounts,
  type DiscountResponse,
  type ParamsGetDiscount,
} from '@/api/discount/discountApi'
import useLoading from '@/hooks/loading'
import { Message, Modal } from '@arco-design/web-vue'
import { IconPlus, IconSearch } from '@arco-design/web-vue/es/icon'
import { onMounted, reactive, ref } from 'vue'
import PromotionModal from './components/PromotionModal.vue'
import PromotionTable from './components/PromotionTable.vue'

const { loading, setLoading } = useLoading(false)

// Search form - Fix: Use 'q' instead of 'discountName' to match API
const searchForm = reactive({
  q: '',
  discountStatus: undefined as number | undefined,
})

// Table data
const discountData = ref<DiscountResponse[]>([])
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: true,
  showPageSize: true,
})

// Modal states
const modalVisible = ref(false)
const isEdit = ref(false)
const currentFormData = ref<Partial<DiscountResponse>>({})

// Fetch discount data
const fetchDiscountData = async () => {
  setLoading(true)
  try {
    const params: ParamsGetDiscount = {
      page: pagination.current - 1, // Backend uses 0-based pagination
      size: pagination.pageSize,
      q: searchForm.q || '', // Fix: Make sure q is always a string
      discountStatus: searchForm.discountStatus,
    }

    // Debug logs - can be removed in production
    // console.log('Fetching with params:', params)

    const response = await getAllDiscounts(params)

    // console.log('API Response:', response)

    discountData.value = response.items || []
    pagination.total = response.totalItems || 0

    // console.log('Discount data:', discountData.value)
  } catch (error) {
    console.error('Error fetching discount data:', error)
    Message.error('Có lỗi xảy ra khi tải danh sách đợt giảm giá')
    discountData.value = []
    pagination.total = 0
  } finally {
    setLoading(false)
  }
}

// Search handlers
const handleSearch = () => {
  pagination.current = 1
  fetchDiscountData()
}

const handleReset = () => {
  searchForm.q = ''
  searchForm.discountStatus = undefined
  pagination.current = 1
  fetchDiscountData()
}

// CRUD handlers
const handleCreate = () => {
  isEdit.value = false
  currentFormData.value = {}
  modalVisible.value = true
}

const handleEdit = (record: DiscountResponse) => {
  isEdit.value = true
  // Fix: Pass the complete record with correct field names
  currentFormData.value = {
    ...record,
    // Make sure all required fields are present
    id: record.id,
    discountName: record.discountName,
    discountCode: record.discountCode,
    percentage: record.percentage,
    startTime: record.startTime, // Keep original field names
    endTime: record.endTime, // Keep original field names
    description: record.description,
    createdDate: record.createdDate,
  }
  modalVisible.value = true
}

const handleDelete = (record: DiscountResponse) => {
  Modal.confirm({
    title: 'Xác nhận xóa',
    content: `Bạn có chắc chắn muốn xóa đợt giảm giá "${record.discountName}"? Hành động này không thể hoàn tác.`,
    onOk: async () => {
      try {
        await deleteDiscount(record.id)
        Message.success('Xóa đợt giảm giá thành công')
        fetchDiscountData()
      } catch (error) {
        // console.error('Error deleting discount:', error)
        Message.error('Có lỗi xảy ra khi xóa đợt giảm giá')
      }
    },
  })
}

const handleDeactivate = (record: DiscountResponse) => {
  Modal.confirm({
    title: 'Xác nhận kết thúc sớm',
    content: `Bạn có chắc chắn muốn kết thúc sớm đợt giảm giá "${record.discountName}"?`,
    onOk: async () => {
      try {
        await deactivateDiscount(record.id)
        Message.success('Kết thúc sớm đợt giảm giá thành công')
        fetchDiscountData()
      } catch (error) {
        // console.error('Error deactivating discount:', error)
        Message.error('Có lỗi xảy ra khi kết thúc sớm đợt giảm giá')
      }
    },
  })
}

// Pagination handler
const handlePageChange = (page: number, pageSize: number) => {
  pagination.current = page
  pagination.pageSize = pageSize
  fetchDiscountData()
}

// Modal success handler
const handleModalSuccess = () => {
  modalVisible.value = false
  fetchDiscountData() // Reload data after success
}

// Initialize
onMounted(() => {
  fetchDiscountData()
})
</script>

<script lang="ts">
export default {
  name: 'DiscountManagement',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.search-section {
  margin-bottom: 16px;
  padding: 16px;
  background-color: var(--color-fill-2);
  border-radius: 4px;
}
</style>
