<template>
  <div class="home">
    <el-row :gutter="24">
      <el-col :span="16">
        <QuickEntrance />
        <TodoList />
      </el-col>
      <el-col :span="8">
        <InventoryWarning />
      </el-col>
    </el-row>
    <BrandSaleTop10 />
  </div>
</template>

<script>
  export default {
    components: {
      /* 快捷入口 / 事业部库存 */
      QuickEntrance: () => import('./components/QuickEntrance'),
      /* 库存预警 */
      InventoryWarning: () => import('./components/InventoryWarning'),
      /* 品牌销售 Top 10 */
      BrandSaleTop10: () => import('./components/BrandSaleTop10'),
      /* 待办事项 */
      TodoList: () => import('./components/TodoList')
    },
    activated() {
      /* 手动触发一下window的resize事件 */
      const event = new Event('resize')
      window.dispatchEvent(event)
    }
  }
</script>

<style lang="scss" scoped>
  .home {
    font-family: Source Han Sans CN;
  }
</style>
