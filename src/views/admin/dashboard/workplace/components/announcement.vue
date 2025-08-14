<template>
  <a-card
    class="general-card"
    :title="$t('workplace.announcement')"
    :header-style="{ paddingBottom: '0' }"
    :body-style="{ padding: '15px 20px 13px 20px' }"
  >
    <template #extra>
      <a-link>{{ $t('workplace.viewMore') }}</a-link>
    </template>
    <div>
      <div v-for="(item, idx) in list" :key="idx" class="item">
        <a-tag :color="item.type" size="small">{{ item.label }}</a-tag>
        <span class="item-content">
          {{ item.content }}
        </span>
      </div>
    </div>
  </a-card>
</template>

<script lang="ts" setup>
const list = [
  {
    type: 'orangered',
    label: 'Hoạt động',
    content: 'Nội dung ưu đãi mới nhất',
  },
  {
    type: 'cyan',
    label: 'Tin nhắn',
    content: 'Nội dung mới thêm chưa được duyệt, vui lòng nhấp để xem chi tiết.',
  },
  {
    type: 'blue',
    label: 'Thông báo',
    content: 'Thời gian dùng thử sản phẩm hiện tại sắp kết thúc, nếu cần gia hạn vui lòng nhấp để xem.',
  },
  {
    type: 'blue',
    label: 'Thông báo',
    content: 'Thông báo kế hoạch nâng cấp hệ thống tháng 1',
  },
  {
    type: 'cyan',
    label: 'Tin nhắn',
    content: 'Nội dung mới thêm đã được duyệt, vui lòng nhấp để xem chi tiết.',
  },
]
</script>

<style scoped lang="less">
.item {
  display: flex;
  align-items: center;
  width: 100%;
  height: 24px;
  margin-bottom: 4px;
  .item-content {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-left: 4px;
    color: var(--color-text-2);
    text-decoration: none;
    font-size: 13px;
    cursor: pointer;
  }
}
</style>
