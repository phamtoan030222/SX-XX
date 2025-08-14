<template>
  <a-tabs>
    <template #extra>
      <a-button>Tạo hóa đơn</a-button>
    </template>
    <a-tab-pane key="0" title="Tất cả"><TableOrder /></a-tab-pane>
    <a-tab-pane key="1" title="Chờ xác nhận"><TableOrder /></a-tab-pane>
    <a-tab-pane key="2" title="Chờ giao hàng">Content of Tab Panel 2</a-tab-pane>
    <a-tab-pane key="3" title="Đang giao hàng">Content of Tab Panel 3</a-tab-pane>
    <a-tab-pane key="4" title="Hoàn thành">Content of Tab Panel 3</a-tab-pane>
    <a-tab-pane key="5" title="Đã hủy">Content of Tab Panel 3</a-tab-pane>
  </a-tabs>
</template>

<script lang="ts" setup>
import TableOrder from './table-order.vue'
</script>
