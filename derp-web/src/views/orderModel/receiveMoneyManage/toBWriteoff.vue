<template>
  <!-- 上架列表页面 -->
  <div class="page-bx bgc-w root-page">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <el-tabs v-model="currentTabsIndex" type="card" class="mr-t-20">
      <el-tab-pane
        label="暂估收入"
        name="0"
        v-if="btnList.includes('toBWriteoff_income')"
      >
        <ToBWriteoffIncome v-if="currentTabsIndex === '0'" />
      </el-tab-pane>
      <el-tab-pane
        label="暂估费用"
        name="1"
        v-if="btnList.includes('toBWriteoff_fee')"
      >
        <ToBWriteoffFee v-if="currentTabsIndex === '1'" />
      </el-tab-pane>
      <el-tab-pane
        label="收入月结快照"
        name="2"
        v-if="btnList.includes('toBTempRevenueMonthlySnapshot')"
      >
        <listToBTempRevenueMonthlySnapshot v-if="currentTabsIndex === '2'" />
      </el-tab-pane>
      <el-tab-pane
        label="费用月结快照"
        name="3"
        v-if="btnList.includes('toBTempCostMonthlySnapshot')"
      >
        <listToBTempCostMonthlySnapshot v-if="currentTabsIndex === '3'" />
      </el-tab-pane>
    </el-tabs>
    <div
      class="empty-text"
      v-if="
        !btnList.includes('toBWriteoff_income') &&
        !btnList.includes('toBWriteoff_fee') &&
        !btnList.includes('toBTempRevenueMonthlySnapshot') &&
        !btnList.includes('toBTempCostMonthlySnapshot')
      "
    >
      暂无数据
    </div>
  </div>
</template>

<script>
  export default {
    components: {
      // 暂估收入
      ToBWriteoffIncome: () => import('./toBWriteoffIncome'),
      // 暂估费用
      ToBWriteoffFee: () => import('./toBWriteoffFee'),
      // 收入月结
      listToBTempRevenueMonthlySnapshot: () =>
        import('./listToBTempRevenueMonthlySnapshot.vue'),
      // 费用月结
      listToBTempCostMonthlySnapshot: () =>
        import('./listToBTempCostMonthlySnapshot.vue')
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
      this.currentTabsIndex = this.btnList.includes('toBWriteoff_income')
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
