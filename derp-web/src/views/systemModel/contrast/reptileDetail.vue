<template>
  <!-- 供应商详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        使用平台：{{ detail.platformName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        用户名：{{ detail.loginName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        爬取账单时点(天)： {{ detail.timePoint || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        公司：{{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        店铺名称：{{ detail.shopName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        出仓库：{{ detail.outDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        入仓库：{{ detail.inDepotName || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { toDetailsPage } from '@a/contrast'
  export default {
    mixins: [commomMix],
    components: {},
    data() {
      return {
        id: '',
        detail: {}
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        this.id = id
        const { data } = await toDetailsPage({ id })
        this.detail = data
      }
    }
  }
</script>
<style lang="scss" scoped>
  .table-container {
    border: 1px solid #ebeef5;
    border-collapse: collapse;
    width: 100%;
    text-align: center;
    color: #666;
    font-size: 14px;
    td {
      border-left: 1px solid #ebeef5;
      border-bottom: 1px solid #ebeef5;
      height: 34px;
    }
  }
</style>
