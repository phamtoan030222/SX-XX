<template>
  <a-card :style="{ margin: '20px' }">
    <template #title>
      <div class="card-header">
        <span>Thông tin đơn hàng</span>
        <a-space :size="12" align="center">
          <a-switch />
          <span style="color: gray; font-size: small">Bán giao hàng</span>
        </a-space>
      </div>
    </template>
  </a-card>
</template>

<script setup>
import { ref } from 'vue'
import { IconPlus, IconUser, IconPhone, IconSearch } from '@arco-design/web-vue/es/icon'

// Form khách hàng
const customerForm = ref({
  name: '',
  phone: '',
  isRetail: true,
})
const addProduct = () => {
  console.log('Thêm khách hàng mới', customerForm.value)
  // Thêm logic xử lý ở đây
}

const handleSearchCustomer = (value) => {
  console.log('Tìm kiếm khách hàng:', value)
  // Thêm logic tìm kiếm ở đây
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

:deep(.arco-card-header) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
}

:deep(.arco-form) {
  max-width: 500px;
  margin: 0 auto;
}
</style>
