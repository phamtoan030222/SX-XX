<template>
  <a-table
    :columns="columns"
    :data="data"
    :loading="loading"
    :pagination="paginationProps"
    :scroll="{ x: '100%' }"
    @page-change="handlePageChange"
    @page-size-change="handlePageSizeChange"
  >
    <!-- Discount Name -->
    <template #discountName="{ record }">
      <a-typography-text copyable>
        {{ record.discountName }}
      </a-typography-text>
    </template>

    <!-- Discount Code -->
    <template #discountCode="{ record }">
      <a-tag color="blue">
        {{ record.discountCode }}
      </a-tag>
    </template>

    <!-- Percentage -->
    <template #percentage="{ record }">
      <a-tag color="green">
        <template #icon>
          <IconGift />
        </template>
        {{ record.percentage }}%
      </a-tag>
    </template>

    <!-- Status -->
    <template #status="{ record }">
      <a-tag :color="getStatusColor(record)">
        {{ getStatusText(record) }}
      </a-tag>
    </template>

    <!-- Time Range -->
    <template #timeRange="{ record }">
      <a-space direction="vertical" size="mini">
        <a-typography-text type="secondary" style="font-size: 12px">
          <IconClockCircle />
          Bắt đầu: {{ formatDate(record.startTime) }}
        </a-typography-text>
        <a-typography-text type="secondary" style="font-size: 12px">
          <IconClockCircle />
          Kết thúc: {{ formatDate(record.endTime) }}
        </a-typography-text>
      </a-space>
    </template>

    <!-- Created Date -->
    <template #createdDate="{ record }">
      <a-typography-text type="secondary">
        {{ formatDate(record.createdDate) }}
      </a-typography-text>
    </template>

    <!-- Description -->
    <template #description="{ record }">
      <a-tooltip :content="record.description || 'Không có mô tả'" position="top">
        <a-typography-text style="max-width: 150px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; display: inline-block">
          {{ record.description || '-' }}
        </a-typography-text>
      </a-tooltip>
    </template>

    <!-- Actions -->
    <template #actions="{ record }">
      <a-space>
        <a-tooltip content="Chỉnh sửa" position="top">
          <a-button type="text" size="small" @click="handleEdit(record)" :disabled="!canEdit(record)">
            <template #icon>
              <IconEdit />
            </template>
          </a-button>
        </a-tooltip>

        <a-tooltip content="Kết thúc sớm" position="top">
          <a-button type="text" size="small" status="warning" @click="handleDeactivate(record)" :disabled="!canDeactivate(record)">
            <template #icon>
              <IconPauseCircle />
            </template>
          </a-button>
        </a-tooltip>

        <a-tooltip content="Xóa" position="top">
          <a-button type="text" size="small" status="danger" @click="handleDelete(record)" :disabled="!canDelete(record)">
            <template #icon>
              <IconDelete />
            </template>
          </a-button>
        </a-tooltip>
      </a-space>
    </template>
  </a-table>
</template>

<script lang="ts" setup>
import { DiscountResponse } from '@/api/discount/discountApi'
import { TableColumnData } from '@arco-design/web-vue'
import { IconClockCircle, IconDelete, IconEdit, IconGift, IconPauseCircle } from '@arco-design/web-vue/es/icon'
import { computed } from 'vue'

interface Props {
  data: DiscountResponse[]
  loading: boolean
  pagination: {
    current: number
    pageSize: number
    total: number
    showTotal: boolean
    showPageSize: boolean
  }
}

interface Emits {
  (e: 'edit', record: DiscountResponse): void
  (e: 'delete', record: DiscountResponse): void
  (e: 'deactivate', record: DiscountResponse): void
  (e: 'page-change', page: number, pageSize: number): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// Table columns configuration
const columns = computed<TableColumnData[]>(() => [
  {
    title: 'Tên đợt giảm giá',
    dataIndex: 'discountName',
    slotName: 'discountName',
    width: 200,
    ellipsis: true,
    tooltip: true,
  },
  {
    title: 'Mã giảm giá',
    dataIndex: 'discountCode',
    slotName: 'discountCode',
    width: 120,
    align: 'center',
  },
  {
    title: 'Phần trăm giảm',
    dataIndex: 'percentage',
    slotName: 'percentage',
    width: 100,
    align: 'center',
  },
  {
    title: 'Trạng thái',
    slotName: 'status',
    width: 120,
    align: 'center',
  },
  {
    title: 'Thời gian áp dụng',
    slotName: 'timeRange',
    width: 220,
  },
  {
    title: 'Mô tả',
    dataIndex: 'description',
    slotName: 'description',
    width: 200,
    ellipsis: true,
  },
  {
    title: 'Ngày tạo',
    dataIndex: 'createdDate',
    slotName: 'createdDate',
    width: 150,
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
  },
  {
    title: 'Thao tác',
    slotName: 'actions',
    width: 150,
    align: 'center',
    fixed: 'right',
  },
])

// Pagination props
const paginationProps = computed(() => ({
  ...props.pagination,
  showJumper: true,
  showPageSize: true,
  pageSizeOptions: [10, 20, 50, 100],
}))

// Utility functions
const formatDate = (timestamp: number): string => {
  // Fix: Handle invalid timestamps
  if (!timestamp || timestamp <= 0) {
    return 'N/A'
  }

  try {
    return new Date(timestamp).toLocaleString('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    })
  } catch (error) {
    console.error('Error formatting date:', error)
    return 'N/A'
  }
}

const getPromotionStatus = (record: DiscountResponse) => {
  const now = Date.now()

  // Fix: Handle invalid timestamps
  if (!record.startTime || !record.endTime) {
    return 'unknown'
  }

  if (now < record.startTime) return 'upcoming'
  if (now > record.endTime) return 'expired'
  return 'active'
}

const getStatusColor = (record: DiscountResponse): string => {
  const status = getPromotionStatus(record)
  switch (status) {
    case 'upcoming':
      return 'blue'
    case 'active':
      return 'green'
    case 'expired':
      return 'red'
    default:
      return 'gray'
  }
}

const getStatusText = (record: DiscountResponse): string => {
  const status = getPromotionStatus(record)
  switch (status) {
    case 'upcoming':
      return 'Sắp diễn ra'
    case 'active':
      return 'Đang diễn ra'
    case 'expired':
      return 'Đã hết hạn'
    default:
      return 'Không xác định'
  }
}

// Permission checks
const canEdit = (record: DiscountResponse): boolean => {
  const now = Date.now()
  // Chỉ sửa được khi chưa bắt đầu hoặc đang diễn ra
  return !!(record.startTime && record.endTime && now < record.endTime)
}

const canDelete = (record: DiscountResponse): boolean => {
  const now = Date.now()
  // Chỉ xóa được khi chưa bắt đầu
  return !!(record.startTime && now < record.startTime)
}

const canDeactivate = (record: DiscountResponse): boolean => {
  const now = Date.now()
  // Chỉ kết thúc sớm khi đang diễn ra
  return !!(record.startTime && record.endTime && now >= record.startTime && now <= record.endTime)
}

// Event handlers
const handleEdit = (record: DiscountResponse) => {
  emit('edit', record)
}

const handleDelete = (record: DiscountResponse) => {
  emit('delete', record)
}

const handleDeactivate = (record: DiscountResponse) => {
  emit('deactivate', record)
}

const handlePageChange = (page: number) => {
  emit('page-change', page, props.pagination.pageSize)
}

const handlePageSizeChange = (pageSize: number) => {
  emit('page-change', 1, pageSize)
}
</script>
