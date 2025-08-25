<template>
  <div class="container">
    <a-col>
      <a-card>
        <!-- Header với tiêu đề bên trái, nút bên phải -->
        <template #title>
          <div class="card-header">
            <span>Giỏ hàng</span>
            <a-space :size="12" align="center">
              <a-button type="primary" @click="scanQR">
                <template #icon><icon-scan /></template>
                Quét QR
              </a-button>
              <a-button type="outline" @click="addProduct">
                <template #icon><icon-plus /></template>
                Thêm sản phẩm
              </a-button>
            </a-space>
          </div>
        </template>

        <a-row justify="space-between">
          <a-table :columns="columns" :data="tableData" :summary="summary" row-key="id" bordered>
            <!-- Cột thao tác -->
            <template #operation="{ record }">
              <a-tooltip v-if="record.id" content="Xóa sản phẩm">
                <a-button type="text" status="danger" @click="removeItem(record.id)">
                  <template #icon><icon-delete /></template>
                </a-button>
              </a-tooltip>
            </template>
          </a-table>
        </a-row>
      </a-card>
    </a-col>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { IconDelete, IconScan, IconPlus } from '@arco-design/web-vue/es/icon'

const tableData = reactive([])

// Hàm định dạng giá tiền
const formatPrice = (price) => {
  return `${price.toLocaleString('vi-VN')}`
}

const columns = [
  { title: 'STT', dataIndex: 'stt', width: 60, align: 'center' },
  { title: 'Sản phẩm', dataIndex: 'product', width: 500, align: 'center' },
  { title: 'IMEl', dataIndex: 'imel', width: 200, align: 'center' },
  {
    title: 'Đơn giá',
    dataIndex: 'price',
    width: 200,
    render: ({ record }) => formatPrice(record.price),
    align: 'center',
  },
  {
    title: 'Thao tác',
    dataIndex: 'operation',
    slotName: 'operation',
    width: 100,
    align: 'center',
  },
]

// Hàng tổng cộng (không có id để tránh hiện nút xóa)
const summary = ({ data }) => {
  const total = data.reduce((sum, item) => sum + item.price, 0)
  return [
    {
      stt: '',
      product: '',
      imei: '',
      price: `Tổng cộng: ${formatPrice(total)} đ`,
    },
  ]
}

// Xóa sản phẩm
const removeItem = (id) => {
  const index = tableData.findIndex((item) => item.id === id)
  if (index !== -1) {
    tableData.splice(index, 1)
  }
}

// Quét QR
const scanQR = () => {
  console.log('Quét QR code')
}

// Thêm sản phẩm
const addProduct = () => {
  console.log('Thêm sản phẩm mới')
}
</script>

<style scoped>
.container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Style cho hàng tổng */
:deep(.arco-table-summary) td {
  font-weight: bold;
  background-color: var(--color-fill-2);
}

/* Canh chỉnh header của card */
:deep(.arco-card-header) {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
