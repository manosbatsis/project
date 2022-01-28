<template>
  <!-- 上架列表页面 -->
  <div class="page-bx bgc-w root-page">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->

    <el-tabs v-model="currentTabsIndex" type="card" class="mr-t-20">
      <el-tab-pane
        label="业务进销存汇总"
        name="0"
        v-if="btnList.includes('business_summary_tab')"
      >
        <BusinessSummaryTab />
      </el-tab-pane>
      <el-tab-pane
        label="库位进销存汇总"
        name="1"
        v-if="btnList.includes('inventory_summary_tab')"
      >
        <InventorySummaryTab />
      </el-tab-pane>
    </el-tabs>
    <div
      class="empty-text"
      v-if="
        !btnList.includes('business_summary_tab') &&
        !btnList.includes('inventory_summary_tab')
      "
    >
      暂无数据
    </div>
  </div>
</template>

<script>
  export default {
    components: {
      /* 业务进销存汇总 */
      BusinessSummaryTab: () => import('./BusinessSummaryTab'),
      /* 库位进销存汇总 */
      InventorySummaryTab: () => import('./InventorySummaryTab')
    },
    data() {
      return {
        currentTabsIndex: '0',
        btnList: []
      }
    },
    mounted() {
      this.btnList = sessionStorage.getItem('btnList')
        ? JSON.parse(sessionStorage.getItem('btnList'))
        : []
      this.currentTabsIndex = this.btnList.includes('business_summary_tab')
        ? '0'
        : '1'
    }
  }
</script>

<style lang="scss" scoped>
  .root-page {
    position: relative;
  }
  .empty-text {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    color: #909399;
  }
</style>
