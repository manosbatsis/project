<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->

    <!-- 合同基本信息 -->
    <JFX-title title="合同基本信息" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row mr-t-10">
      <el-col
        v-for="item in Object.keys(contractInfoMap)"
        :key="item"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        :xl="6"
        class="mr-b-10"
      >
        {{ contractInfoMap[item] }}：{{ resultData[item] || '- -' }}
      </el-col>
    </el-row>
    <!-- 合同基本信息 end -->

    <!-- 品牌/产品信息 -->
    <JFX-title title="品牌/产品信息" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row mr-t-10">
      <el-col
        v-for="item in Object.keys(productInfoMap)"
        :key="item"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        :xl="6"
        class="mr-b-10"
      >
        {{ productInfoMap[item] }}：{{ resultData[item] || '- -' }}
      </el-col>
    </el-row>
    <!-- 品牌/产品信息 end -->
  </div>
</template>
<script>
  import { purchaseTryApplyOrderDetail } from '@a/purchaseManage'
  export default {
    data() {
      return {
        /* 合同基本信息 */
        contractInfoMap: {
          oaBillCode: '试单流水编号',
          createrName: '申请人',
          businessManagerName: '业务员',
          businessDept: '业务部门',
          businessModeLabel: '业务类型',
          effectiveCode: '归档编辑号',
          projectName: '项目名称',
          annualPurPlanAmount: '预计年度采购金额（万元人民币）',
          proAccLimit: '立项额度（万元人民币）',
          deliveryTypeLabel: '交货方式',
          otherDelType: '其他交货方式',
          merchantName: '我司签约主体',
          supplierName: '供应商名称',
          supplierTypeLabel: '供应商类型',
          otherSupplier: '其他供应商类型'
        },
        /* 品牌/产品信息 */
        productInfoMap: {
          brandName: '品牌名称',
          comtyTypeLabel: '商品类型',
          otherComty: '其他商品类型',
          otherMonAppoint: '市场营销费用、补贴、奖励等约定',
          returnGoodsApp: '退货条款',
          purchaseTypeLabel: '采购类型',
          counterpartDesc: '供应商描述',
          supProdDesc: '供应商商品描述'
        },
        /* 后端返回数据 */
        resultData: {}
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        if (!id) return false
        try {
          const { data } = await purchaseTryApplyOrderDetail({ id })
          this.resultData = data
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      }
    }
  }
</script>
