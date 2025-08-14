<template>
  <a-table :columns="columns" :data="data" />
</template>

<script setup>
import { ref, h } from 'vue'
import { Link, Tag } from '@arco-design/web-vue'
import { IconEdit, IconEye, IconDelete, IconCheck, IconClose } from '@arco-design/web-vue/es/icon'

const handleCancel = (id) => {
  //   console.log('Hủy đơn hàng:', id)
}

const handleApprove = (id) => {
  //   console.log('Duyệt đơn hàng:', id)
}

const data = ref([
  {
    id: 1,
    order: 1,
    orderCode: 'HD202308001',
    customerName: 'Nguyễn Văn A',
    phoneNumber: '0912345678',
    deliveryAddress: '123 Đường ABC, Quận 1, TP.HCM',
    orderType: 'Giao hàng',
    status: 'Chờ xác nhận',
  },
  {
    id: 2,
    order: 2,
    orderCode: 'HD202308002',
    customerName: 'Trần Thị B',
    phoneNumber: '0987654321',
    deliveryAddress: '456 Đường XYZ, Quận 2, TP.HCM',
    orderType: 'Tại quầy',
    status: 'Chờ giao hàng',
  },
  {
    id: 3,
    order: 3,
    orderCode: 'HD202308003',
    customerName: 'Lê Văn C',
    phoneNumber: '0901122334',
    deliveryAddress: '789 Đường DEF, Quận 3, TP.HCM',
    orderType: 'Giao hàng',
    status: 'Đang giao hàng',
  },
  {
    id: 4,
    order: 4,
    orderCode: 'HD202308004',
    customerName: 'Phạm Thị D',
    phoneNumber: '0977123456',
    deliveryAddress: '321 Đường GHI, Quận 4, TP.HCM',
    orderType: 'Tại quầy',
    status: 'Hoàn thành',
  },
  {
    id: 5,
    order: 5,
    orderCode: 'HD202308005',
    customerName: 'Hoàng Văn E',
    phoneNumber: '0911222333',
    deliveryAddress: '654 Đường KLM, Quận 5, TP.HCM',
    orderType: 'Giao hàng',
    status: 'Đã hủy',
  },
])

const getStatusColor = (status) => {
  switch (status) {
    case 'Chờ xác nhận':
      return 'orange'
    case 'Chờ giao hàng':
      return 'arcoblue'
    case 'Đang giao hàng':
      return 'purple'
    case 'Hoàn thành':
      return 'green'
    case 'Đã hủy':
      return 'red'
    default:
      return 'gray'
  }
}

const columns = [
  {
    title: 'STT',
    dataIndex: 'order',
    width: 80,
    align: 'center',
  },
  {
    title: 'Mã đơn hàng',
    dataIndex: 'orderCode',
    width: 150,
  },
  {
    title: 'Nhân viên',
    dataIndex: 'customerName',
    width: 150,
  },
  {
    title: 'Khách hàng',
    dataIndex: 'phoneNumber',
    width: 130,
  },
  {
    title: 'Loại đơn',
    dataIndex: 'orderType',
    render: ({ record }) =>
      h(
        Tag,
        {
          color: record.orderType === 'Giao hàng' ? 'arcoblue' : 'green',
        },
        { default: () => record.orderType }
      ),
    width: 120,
  },
  {
    title: 'Tổng tiền',
    dataIndex: 'total',
  },

  {
    title: 'Ngày tạo',
    dataIndex: 'total',
  },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    render: ({ record }) =>
      h(
        Tag,
        {
          color: getStatusColor(record.status),
        },
        { default: () => record.status }
      ),
    width: 150,
  },
  {
    title: 'Thao tác',
    dataIndex: 'action',
    render: ({ record }) =>
      h(
        'div',
        { class: 'actions' },
        [
          h(
            Link,
            {
              class: 'action-btn',
              hoverable: false,
              href: `/edit/${record.id}`,
            },
            () => h(IconEye, { class: 'icon-view' })
          ),
        ].filter(Boolean)
      ),
    width: 90,
    fixed: 'right',
  },
]
</script>

<style scoped>
.actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: 4px;
  transition: all 0.2s;
}

.icon-edit {
  color: var(--arcoblue-6);
  font-size: 16px;
}

.icon-view {
  color: var(--green-6);
  font-size: 16px;
}

.icon-cancel {
  color: var(--red-6);
  font-size: 16px;
}

.icon-approve {
  color: var(--green-6);
  font-size: 16px;
}

.action-btn:hover {
  background-color: var(--color-fill-2);
}

.action-btn:hover .icon-edit {
  color: var(--arcoblue-5);
}

.action-btn:hover .icon-view {
  color: var(--green-5);
}

.action-btn:hover .icon-cancel {
  color: var(--red-5);
}

.action-btn:hover .icon-approve {
  color: var(--green-5);
}
</style>
