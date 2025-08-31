const toggleAdvancedFilter = () => { showAdvancedFilter.value = !showAdvancedFilter.value }

<template>
  <div class="container">
    <Breadcrumb :items="['Đợt giảm giá', 'Quản lý đợt giảm giá']" />
    <a-space direction="vertical" :size="16" fill>
      <a-row :gutter="16">
        <a-col :span="6">
          <a-card class="statistic-card active-card">
            <a-statistic title="Đang diễn ra" :value="statistics.active">
              <template #prefix>
                <div class="statistic-icon active-icon">
                  <IconPlayCircle />
                </div>
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card class="statistic-card upcoming-card">
            <a-statistic title="Sắp diễn ra" :value="statistics.upcoming">
              <template #prefix>
                <div class="statistic-icon upcoming-icon">
                  <IconClockCircle />
                </div>
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card class="statistic-card expired-card">
            <a-statistic title="Đã hết hạn" :value="statistics.expired">
              <template #prefix>
                <div class="statistic-icon expired-icon">
                  <IconPauseCircle />
                </div>
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card class="statistic-card total-card">
            <a-statistic title="Tổng số" :value="statistics.total">
              <template #prefix>
                <div class="statistic-icon total-icon">
                  <IconGift />
                </div>
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>


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

        <div class="filter-container">
          <div class="status-filter-section">
            <a-radio-group v-model="quickFilter" @change="handleQuickFilterChange" type="button" class="status-radio-group">
              <a-radio :value="'all'" class="status-radio">
                <span class="status-text">Tất cả</span>
                <a-badge :count="statistics.total" :max-count="99" class="status-badge" />
              </a-radio>
              <a-radio :value="'active'" class="status-radio">
                <span class="status-text">Đang diễn ra</span>
                <a-badge :count="statistics.active" :max-count="99" status="processing" class="status-badge" />
              </a-radio>
              <a-radio :value="'upcoming'" class="status-radio">
                <span class="status-text">Sắp diễn ra</span>
                <a-badge :count="statistics.upcoming" :max-count="99" status="default" class="status-badge" />
              </a-radio>
              <a-radio :value="'expired'" class="status-radio">
                <span class="status-text">Đã hết hạn</span>
                <a-badge :count="statistics.expired" :max-count="99" status="error" class="status-badge" />
              </a-radio>
            </a-radio-group>
          </div>

          <div class="filter-toggle">
            <a-button type="text" @click="toggleAdvancedFilter" class="toggle-btn">
              <template #icon><IconFilter /></template>
              {{ showAdvancedFilter ? 'Ẩn bộ lọc' : 'Hiển thị bộ lọc' }}
            </a-button>
          </div>

          <a-collapse-transition>
            <div v-show="showAdvancedFilter" class="advanced-filters">
              <div class="filter-title">Bộ lọc</div>

              <a-row :gutter="[16, 16]">
                <a-col :span="6">
                  <div class="filter-item">
                    <label class="filter-label">Tên đợt giảm giá</label>
                    <a-input
                      v-model="searchForm.q"
                      placeholder="Nhập tên đợt giảm giá"
                      allow-clear
                      @press-enter="handleSearch"
                      class="filter-input"
                    />
                  </div>
                </a-col>

                <a-col :span="6">
                  <div class="filter-item">
                    <label class="filter-label">Mã giảm giá</label>
                    <a-input
                      v-model="searchForm.discountCode"
                      placeholder="Nhập mã giảm giá"
                      allow-clear
                      @press-enter="handleSearch"
                      class="filter-input"
                    />
                  </div>
                </a-col>

                <a-col :span="6">
                  <div class="filter-item">
                    <label class="filter-label">Trạng thái</label>
                    <a-select
                      v-model="searchForm.discountStatus"
                      placeholder="Chọn trạng thái"
                      allow-clear
                      @change="handleSearch"
                      class="filter-input"
                    >
                      <a-option :value="0">Đang diễn ra</a-option>
                      <a-option :value="1">Sắp diễn ra</a-option>
                      <a-option :value="3">Đã hết hạn</a-option>
                    </a-select>
                  </div>
                </a-col>

                <a-col :span="6">
                  <div class="filter-item">
                    <label class="filter-label">Phần trăm giảm giá (%)</label>
                    <a-input-number-group class="filter-input">
                      <a-input-number
                        v-model="searchForm.percentageRange[0]"
                        placeholder="0"
                        :min="0"
                        :max="100"
                        @change="handleSearch"
                        style="width: 50%"
                      />
                      <a-input-number
                        v-model="searchForm.percentageRange[1]"
                        placeholder="100"
                        :min="0"
                        :max="100"
                        @change="handleSearch"
                        style="width: 50%"
                      />
                    </a-input-number-group>
                  </div>
                </a-col>
              </a-row>

              <a-row :gutter="[16, 16]">
                <a-col :span="12">
                  <div class="filter-item">
                    <label class="filter-label">Ngày bắt đầu</label>
                    <a-date-picker
                      v-model="searchForm.startDate"
                      placeholder="dd/mm/yyyy"
                      @change="handleSearch"
                      class="filter-input"
                      style="width: 100%"
                    />
                  </div>
                </a-col>

                <a-col :span="12">
                  <div class="filter-item">
                    <label class="filter-label">Ngày kết thúc</label>
                    <a-date-picker
                      v-model="searchForm.endDate"
                      placeholder="dd/mm/yyyy"
                      @change="handleSearch"
                      class="filter-input"
                      style="width: 100%"
                    />
                  </div>
                </a-col>
              </a-row>

              <div class="filter-actions">
                <a-space>
                  <a-button type="primary" @click="handleSearch" class="action-btn primary-btn">
                    <template #icon><IconSearch /></template>
                    Tìm kiếm
                  </a-button>
                  <a-button @click="handleReset" class="action-btn secondary-btn">Làm mới bộ lọc</a-button>
                </a-space>
              </div>
            </div>
          </a-collapse-transition>

          <div v-if="activeFilters.length > 0" class="active-filters-section">
            <div class="active-filters-header">
              <span class="active-filters-label">Bộ lọc đang áp dụng:</span>
              <a-button type="text" size="mini" @click="clearAllFilters" class="clear-all-btn">Xóa tất cả</a-button>
            </div>
            <div class="active-filters-tags">
              <a-tag
                v-for="filter in activeFilters"
                :key="filter.key"
                color="blue"
                closable
                @close="removeFilter(filter.key)"
                class="filter-tag"
              >
                {{ filter.label }}
              </a-tag>
            </div>
          </div>
        </div>
      </a-card>

      <!-- Phần filter và table -->
      <a-card class="general-card">
        <template #title>
          <a-space>
            <span>Danh sách đợt giảm giá</span>
            <a-tag color="blue">{{ pagination.total }} kết quả</a-tag>
          </a-space>
        </template>
        <template #extra>
          <a-button @click="handleRefresh">
            <template #icon><IconRefresh /></template>
            Làm mới
          </a-button>
        </template>

        <PromotionTable
          :loading="loading"
          :data="discountData"
          :pagination="pagination"
          @edit="handleEdit"
          @delete="handleDelete"
          @deactivate="handleDeactivate"
          @start="handleStart"
          @page-change="handlePageChange"
        />
      </a-card>
    </a-space>

    <PromotionModal v-model:visible="modalVisible" :form-data="currentFormData" :is-edit="isEdit" @success="handleModalSuccess" />
  </div>
</template>

<script lang="ts" setup>
import {
  deactivateDiscount,
  deleteDiscount,
  getAllDiscounts,
  startDiscount,
  type DiscountResponse,
  type ParamsGetDiscount,
} from '@/api/discount/discountApi'
import useLoading from '@/hooks/loading'
import { Message, Modal } from '@arco-design/web-vue'
import {
  IconClockCircle,
  IconFilter,
  IconGift,
  IconPauseCircle,
  IconPlayCircle,
  IconPlus,
  IconRefresh,
  IconSearch,
} from '@arco-design/web-vue/es/icon'
import { computed, onMounted, reactive, ref, watch } from 'vue'
import PromotionModal from './components/PromotionModal.vue'
import PromotionTable from './components/PromotionTable.vue'

const { loading, setLoading } = useLoading(false)

const quickFilter = ref('all')
const showAdvancedFilter = ref(false)

const searchForm = reactive({
  q: '',
  discountCode: '',
  discountStatus: undefined as number | undefined,
  percentageRange: [undefined, undefined] as [number | undefined, number | undefined],
  applyDateRange: undefined as [Date, Date] | undefined,
  startDate: undefined as Date | undefined,
  endDate: undefined as Date | undefined,
  sortBy: 'createdDate_desc' as string,
})

const statistics = reactive({
  active: 0,
  upcoming: 0,
  expired: 0,
  total: 0,
})

const discountData = ref<DiscountResponse[]>([])
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: true,
  showPageSize: true,
})

const modalVisible = ref(false)
const isEdit = ref(false)
const currentFormData = ref<Partial<DiscountResponse>>({})

const activeFilters = computed(() => {
  const filters: Array<{ key: string; label: string }> = []

  if (searchForm.q) {
    filters.push({ key: 'q', label: `Tên: "${searchForm.q}"` })
  }

  if (searchForm.discountCode) {
    filters.push({ key: 'discountCode', label: `Mã: "${searchForm.discountCode}"` })
  }

  if (searchForm.percentageRange && searchForm.percentageRange[0] !== undefined && searchForm.percentageRange[1] !== undefined) {
    filters.push({
      key: 'percentageRange',
      label: `Phần trăm: ${searchForm.percentageRange[0]}% - ${searchForm.percentageRange[1]}%`,
    })
  }

  if (searchForm.startDate) {
    filters.push({
      key: 'startDate',
      label: `Từ ngày: ${searchForm.startDate.toLocaleDateString('vi-VN')}`,
    })
  }

  if (searchForm.endDate) {
    filters.push({
      key: 'endDate',
      label: `Đến ngày: ${searchForm.endDate.toLocaleDateString('vi-VN')}`,
    })
  }

  if (searchForm.discountStatus !== undefined) {
    const statusMap: { [key: number]: string } = {
      0: 'Đang diễn ra',
      1: 'Sắp diễn ra',
      3: 'Đã hết hạn',
    }
    filters.push({ key: 'discountStatus', label: `Trạng thái: ${statusMap[searchForm.discountStatus]}` })
  }

  return filters
})

const calculateStatistics = (data: DiscountResponse[]) => {
  const now = Date.now()

  statistics.total = data.length
  statistics.active = data.filter((item) => item.startTime && item.endTime && now >= item.startTime && now <= item.endTime).length
  statistics.upcoming = data.filter((item) => item.startTime && now < item.startTime).length
  statistics.expired = data.filter((item) => item.endTime && now > item.endTime).length
}

const toggleAdvancedFilter = () => {
  showAdvancedFilter.value = !showAdvancedFilter.value
}

const fetchDiscountData = async () => {
  setLoading(true)
  try {
    const params: ParamsGetDiscount = {
      page: pagination.current - 1,
      size: pagination.pageSize,
      q: searchForm.q || '',
      discountStatus: searchForm.discountStatus,
      ...(searchForm.percentageRange &&
        searchForm.percentageRange[0] !== undefined &&
        searchForm.percentageRange[1] !== undefined && {
          minPercentage: searchForm.percentageRange[0],
          maxPercentage: searchForm.percentageRange[1],
        }),
      ...(searchForm.startDate && { startDate: searchForm.startDate.getTime() }),
      ...(searchForm.endDate && { endDate: searchForm.endDate.getTime() }),
      ...(searchForm.sortBy && { sortBy: searchForm.sortBy }),
    }

    const response = await getAllDiscounts(params)
    discountData.value = response.items || []
    pagination.total = response.totalItems || 0

    calculateStatistics(discountData.value)
  } catch (error) {
    Message.error('Có lỗi xảy ra khi tải danh sách đợt giảm giá')
    discountData.value = []
    pagination.total = 0
    statistics.active = 0
    statistics.upcoming = 0
    statistics.expired = 0
    statistics.total = 0
  } finally {
    setLoading(false)
  }
}

const getDiscountStatusFromQuickFilter = (): number | undefined => {
  switch (quickFilter.value) {
    case 'active':
      return 0
    case 'upcoming':
      return 1
    case 'expired':
      return 3
    default:
      return undefined
  }
}

const handleQuickFilterChange = () => {
  searchForm.discountStatus = getDiscountStatusFromQuickFilter()
  pagination.current = 1
  fetchDiscountData()
}

const handleSearch = () => {
  pagination.current = 1
  fetchDiscountData()
}

const handleReset = () => {
  searchForm.q = ''
  searchForm.discountCode = ''
  searchForm.discountStatus = undefined
  searchForm.percentageRange = [undefined, undefined]
  searchForm.applyDateRange = undefined
  searchForm.startDate = undefined
  searchForm.endDate = undefined
  searchForm.sortBy = 'createdDate_desc'

  quickFilter.value = 'all'

  pagination.current = 1
  fetchDiscountData()
}

const removeFilter = (filterKey: string) => {
  switch (filterKey) {
    case 'q':
      searchForm.q = ''
      break
    case 'discountCode':
      searchForm.discountCode = ''
      break
    case 'discountStatus':
      searchForm.discountStatus = undefined
      break
    case 'percentageRange':
      searchForm.percentageRange = [undefined, undefined]
      break
    case 'startDate':
      searchForm.startDate = undefined
      break
    case 'endDate':
      searchForm.endDate = undefined
      break
    case 'quickFilter':
      quickFilter.value = 'all'
      searchForm.discountStatus = undefined
      break
    default:
      break
  }
  handleSearch()
}

const clearAllFilters = () => {
  handleReset()
}

const handleRefresh = () => {
  fetchDiscountData()
  Message.success('Đã làm mới dữ liệu')
}

const handleCreate = () => {
  isEdit.value = false
  currentFormData.value = {}
  modalVisible.value = true
}

const handleStart = (record: DiscountResponse) => {
  Modal.confirm({
    title: 'Xác nhận bắt đầu sớm',
    content: `Bạn có chắc chắn muốn bắt đầu sớm đợt giảm giá "${record.discountName}"? Đợt giảm giá sẽ được kích hoạt ngay lập tức.`,
    okText: 'Bắt đầu sớm',
    cancelText: 'Hủy',
    onOk: async () => {
      try {
        setLoading(true)
        await startDiscount(record.id)
        Message.success('Bắt đầu sớm đợt giảm giá thành công')
        fetchDiscountData() 
      } catch (error: any) {
        console.error('Error starting discount:', error)
        const errorMessage = error?.response?.data?.message || 'Có lỗi xảy ra khi bắt đầu sớm đợt giảm giá'
        Message.error(errorMessage)
      } finally {
        setLoading(false)
      }
    },
  })
}

const handleEdit = (record: DiscountResponse) => {
  isEdit.value = true
  currentFormData.value = {
    ...record,
    id: record.id,
    discountName: record.discountName,
    discountCode: record.discountCode,
    percentage: record.percentage,
    startTime: record.startTime,
    endTime: record.endTime,
    description: record.description,
    createdDate: record.createdDate,
  }
  modalVisible.value = true
}

const handleDelete = (record: DiscountResponse) => {
  Modal.confirm({
    title: 'Xác nhận xóa',
    content: `Bạn có chắc chắn muốn xóa đợt giảm giá "${record.discountName}"? Hành động này không thể hoàn tác.`,
    okText: 'Xác nhận xoá',
    cancelText: 'Hủy',
    onOk: async () => {
      try {
        await deleteDiscount(record.id)
        Message.success('Xóa đợt giảm giá thành công')
        fetchDiscountData()
      } catch (error) {
        Message.error('Có lỗi xảy ra khi xóa đợt giảm giá')
      }
    },
  })
}

const handleDeactivate = (record: DiscountResponse) => {
  Modal.confirm({
    title: 'Xác nhận kết thúc sớm',
    content: `Bạn có chắc chắn muốn kết thúc sớm đợt giảm giá "${record.discountName}"?`,
    okText: 'Kết thúc sớm',
    cancelText: 'Hủy',
    onOk: async () => {
      try {
        await deactivateDiscount(record.id)
        Message.success('Kết thúc sớm đợt giảm giá thành công')
        fetchDiscountData()
      } catch (error) {
        Message.error('Có lỗi xảy ra khi kết thúc sớm đợt giảm giá')
      }
    },
  })
}

const handlePageChange = (page: number, pageSize: number) => {
  pagination.current = page
  pagination.pageSize = pageSize
  fetchDiscountData()
}

const handleModalSuccess = () => {
  modalVisible.value = false
  fetchDiscountData()
}

let searchTimeout: ReturnType<typeof setTimeout>
watch([() => searchForm.q, () => searchForm.discountCode], () => {
  if (searchTimeout) clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    if (
      (searchForm.q && searchForm.q.length >= 2) ||
      (searchForm.discountCode && searchForm.discountCode.length >= 2) ||
      searchForm.q === '' ||
      searchForm.discountCode === ''
    ) {
      handleSearch()
    }
  }, 500)
})

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

.statistic-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }

  .statistic-icon {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    color: white;
  }

  &.active-card {
    border-left: 4px solid #00b42a;
    .active-icon {
      background: linear-gradient(135deg, #00b42a, #00d932);
    }
  }

  &.upcoming-card {
    border-left: 4px solid #165dff;
    .upcoming-icon {
      background: linear-gradient(135deg, #165dff, #3c7eff);
    }
  }

  &.expired-card {
    border-left: 4px solid #f53f3f;
    .expired-icon {
      background: linear-gradient(135deg, #f53f3f, #f76560);
    }
  }

  &.total-card {
    border-left: 4px solid #722ed1;
    .total-icon {
      background: linear-gradient(135deg, #722ed1, #9254de);
    }
  }
}

.filter-container {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  margin-bottom: 16px;
  overflow: hidden;
}

.status-filter-section {
  padding: 16px 20px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;

  .status-radio-group {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;

    .status-radio {
      border-radius: 6px;
      transition: all 0.3s ease;

      .status-text {
        margin-right: 8px;
      }

      .status-badge {
        :deep(.arco-badge-number) {
          font-size: 10px;
          min-width: 16px;
          height: 16px;
          line-height: 14px;
          border-radius: 8px;
        }
      }
    }
  }
}

.filter-toggle {
  padding: 12px 20px;
  border-bottom: 1px solid #f0f0f0;
  background: #fff;

  .toggle-btn {
    color: #1890ff;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 13px;

    &:hover {
      background-color: #f0f8ff;
    }
  }
}

.advanced-filters {
  padding: 20px;
  background: #fff;

  .filter-title {
    font-size: 14px;
    font-weight: 600;
    color: #262626;
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 1px solid #f0f0f0;
  }

  .filter-item {
    .filter-label {
      display: block;
      font-size: 13px;
      color: #595959;
      margin-bottom: 6px;
      font-weight: 500;
    }

    .filter-input {
      width: 100%;
      height: 36px;
      border-radius: 4px;
      border: 1px solid #d9d9d9;
      font-size: 13px;

      &:hover {
        border-color: #40a9ff;
      }

      &:focus {
        border-color: #1890ff;
        box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
      }

      :deep(.arco-select-selector),
      :deep(.arco-picker-input),
      :deep(.arco-input) {
        border: none;
        height: 34px;
        font-size: 13px;
      }
    }
  }

  .filter-actions {
    margin-top: 20px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
    text-align: left;

    .action-btn {
      height: 36px;
      padding: 0 16px;
      border-radius: 4px;
      font-size: 13px;
      font-weight: 500;
      transition: all 0.3s ease;

      &.primary-btn {
        background: #1890ff;
        border-color: #1890ff;

        &:hover {
          background: #40a9ff;
          border-color: #40a9ff;
        }
      }

      &.secondary-btn {
        background: #fff;
        border: 1px solid #d9d9d9;
        color: #595959;

        &:hover {
          border-color: #40a9ff;
          color: #1890ff;
        }
      }
    }
  }
}

.active-filters-section {
  margin: 16px 20px 0;
  padding: 16px 0 0;
  border-top: 1px solid #f0f0f0;

  .active-filters-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;

    .active-filters-label {
      font-weight: 500;
      color: #595959;
      font-size: 13px;
    }

    .clear-all-btn {
      color: #ff4d4f;
      font-size: 12px;
      padding: 2px 8px;

      &:hover {
        background-color: #fff2f0;
        color: #cf1322;
      }
    }
  }

  .active-filters-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;

    .filter-tag {
      border-radius: 4px;
      font-size: 12px;
      padding: 2px 8px;
      border: 1px solid #1890ff;
      background: #e6f7ff;
      color: #1890ff;

      :deep(.arco-tag-close-btn) {
        color: #1890ff;
        margin-left: 4px;
        font-size: 10px;

        &:hover {
          background-color: rgba(24, 144, 255, 0.1);
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .status-filter-section {
    .status-radio-group {
      justify-content: center;
    }
  }

  .advanced-filters {
    .filter-actions {
      text-align: center;

      .action-btn {
        width: 100%;
        margin-bottom: 8px;

        &:last-child {
          margin-bottom: 0;
        }
      }
    }
  }
}

:deep(.arco-statistic-title) {
  font-weight: 500;
  color: var(--color-text-2);
}

:deep(.arco-statistic-value) {
  font-weight: 600;
}
</style>
