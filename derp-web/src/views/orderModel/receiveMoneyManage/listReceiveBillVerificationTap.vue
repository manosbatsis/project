<template>
  <!-- 平台结算单页面 -->
  <div class="page-bx bgc-w root-page">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <el-tabs v-model="activeName" type="card" class="mr-t-20">
      <el-tab-pane label="收款跟踪" name="1">
        <listReceiveBillVerification />
      </el-tab-pane>
      <el-tab-pane
        label="月结快照"
        name="2"
        v-if="btnList.includes('listBillMonthlySnapshot')"
      >
        <listBillMonthlySnapshot />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  export default {
    components: {
      // 收款核销跟踪 src\views\orderModel\receiveMoneyManage\listReceiveBillVerification.vue
      listReceiveBillVerification: () =>
        import('./listReceiveBillVerification'),
      // 月结快照
      listBillMonthlySnapshot: () => import('./listBillMonthlySnapshot')
    },
    data() {
      return {
        activeName: '1',
        btnList: []
      }
    },
    mounted() {
      this.btnList = sessionStorage.getItem('btnList')
        ? JSON.parse(sessionStorage.getItem('btnList'))
        : []
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
