<template>
  <a-tabs type="card-gutter" :editable="true" @add="handleAdd" @delete="handleDelete" show-add-button auto-switch>
    <a-tab-pane v-for="item in data" :key="item.key" :title="item.title">
      <TableOrder />

      <InfoClient />
      <InfoOrder />
    </a-tab-pane>
  </a-tabs>
</template>

<script setup>
import { ref } from 'vue'
import { IconPlus, IconUser, IconPhone, IconSearch } from '@arco-design/web-vue/es/icon'
import TableOrder from './table-order.vue'
import InfoClient from './info-client.vue'
import InfoOrder from './info-order.vue'

// Dữ liệu các tab
const data = ref([])

const handleAdd = () => {
  const number = Math.floor(Math.random() * 1000)
  data.value.push({
    key: `${number}`,
    title: `HD-${number}`,
    content: `Content of New Tab Panel ${number}`,
  })
}

const handleDelete = (key) => {
  data.value = data.value.filter((item) => item.key !== key)
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
