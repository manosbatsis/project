<template>
  <!-- To C暂估应收表 -->
  <div class="page-bx bgc-w root-page">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <el-tabs v-model="currentTabsIndex" type="card" class="mr-t-20">
      <el-tab-pane
        label="暂估收入"
        name="0"
        v-if="btnList.includes('tocestimated_income')"
      >
        <TocEstimatedIncome v-if="currentTabsIndex === '0'" />
      </el-tab-pane>
      <el-tab-pane
        label="暂估费用"
        name="1"
        v-if="btnList.includes('tocestimated_fee')"
      >
        <TocEstimatedFee v-if="currentTabsIndex === '1'" />
      </el-tab-pane>
      <el-tab-pane
        label="收入月结快照"
        name="2"
        v-if="btnList.includes('listReceiveByPage')"
      >
        <listReceiveByPage v-if="currentTabsIndex === '2'" />
      </el-tab-pane>
      <el-tab-pane
        label="费用月结快照"
        name="3"
        v-if="btnList.includes('listCostByPage')"
      >
        <listCostByPage v-if="currentTabsIndex === '3'" />
      </el-tab-pane>
    </el-tabs>
    <div
      class="empty-text"
      v-if="
        !btnList.includes('tocestimated_income') &&
        !btnList.includes('tocestimated_fee')
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
      TocEstimatedIncome: () => import('./TocEstimatedIncome'),
      // 暂估费用
      TocEstimatedFee: () => import('./TocEstimatedFee'),
      // 收入月结快照
      listReceiveByPage: () => import('./listReceiveByPage'),
      // 收入费用快照
      listCostByPage: () => import('./listCostByPage.vue')
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
      this.currentTabsIndex = this.btnList.includes('tocestimated_income')
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
