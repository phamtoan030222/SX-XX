<template>
  <div class="container">
    <Breadcrumb :items="['menu.form', 'menu.form.group']" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import useLoading from '@/hooks/loading'

const formData = ref({})
const formRef = ref<FormInstance>()
const { loading, setLoading } = useLoading()
const onSubmitClick = async () => {
  const res = await formRef.value?.validate()
  if (!res) {
    setLoading(true)
  }
  setTimeout(() => {
    setLoading(false)
  }, 1000)
}
</script>

<script lang="ts">
export default {
  name: 'Group',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 40px 20px;
  overflow: hidden;
}

.actions {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 60px;
  padding: 14px 20px 14px 0;
  background: var(--color-bg-2);
  text-align: right;
}
</style>
