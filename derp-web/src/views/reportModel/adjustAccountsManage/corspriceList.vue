<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <div class="mr-t-15"></div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="单价管理" name="0">
        <cors-prive-list v-if="activeName === '0'"></cors-prive-list>
      </el-tab-pane>
      <el-tab-pane label="待审核单价" name="1">
        <wait-examine-price-list
          v-if="activeName === '1'"
        ></wait-examine-price-list>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  export default {
    components: {
      // 单价管理
      corsPriveList: () => import('./components/corsPriveList'),
      // 待审核单价
      waitExaminePriceList: () => import('./components/waitExaminePriceList')
    },
    data() {
      return {
        activeName: '0'
      }
    }
  }
</script>
<style lang="scss" scope></style>
