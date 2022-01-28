<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->

    <!-- 合同基本信息 -->
    <JFX-title title="合同基本信息" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row mr-t-10">
      <el-col
        v-for="item in Object.keys(contractBaseInfoMap)"
        :key="item"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        :xl="6"
        class="mr-b-10"
      >
        {{ contractBaseInfoMap[item] }}：{{ resultData[item] || '- -' }}
      </el-col>
    </el-row>
    <!-- 合同基本信息 end -->

    <!-- 合同信息 -->
    <JFX-title title="合同信息" className="mr-t-10 " />
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
    <!-- 合同信息 end -->

    <!-- 合同结算信息 -->
    <JFX-title title="合同结算信息" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row mr-t-10">
      <el-col
        v-for="item in Object.keys(contractSettleInfoMap)"
        :key="item"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        :xl="6"
        class="mr-b-10"
      >
        {{ contractSettleInfoMap[item] }}：{{ resultData[item] || '- -' }}
      </el-col>
    </el-row>
    <!-- 合同结算信息 end -->

    <!-- 合同其他信息 -->
    <JFX-title title="合同其他信息" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row mr-t-10">
      <el-col
        v-for="item in Object.keys(contractOtherInfoMap)"
        :key="item"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        :xl="6"
        class="mr-b-10"
      >
        {{ contractOtherInfoMap[item] }}：{{ resultData[item] || '- -' }}
      </el-col>
    </el-row>
    <!-- 合同其他信息 end -->

    <!-- 用印基本信息 -->
    <JFX-title title="用印基本信息" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row mr-t-10">
      <el-col
        v-for="item in Object.keys(printInfoMap)"
        :key="item"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        :xl="6"
        class="mr-b-10"
      >
        {{ printInfoMap[item] }}：{{ resultData[item] || '- -' }}
      </el-col>
    </el-row>
    <!-- 用印基本信息 end -->
  </div>
</template>
<script>
  import { purchaseFrameContractDetail } from '@a/purchaseManage'
  export default {
    data() {
      return {
        /* 合同基本信息 */
        contractBaseInfoMap: {
          contractNo: '合同协议号',
          contractOldNo: '原协议编号',
          lxsdNo: '立项/试单编号',
          contractName: '合同名称',
          contractVersionLabel: '合同模板',
          createrName: '申请人',
          applicationDate: '申请日期',
          businessManagerName: '业务员',
          businessDept: '业务部门',
          contractTypeLabel: '合同类型',
          effectiveCode: '归档编辑号',
          merchantName: '我司签约主体',
          supplierName: '供应商名称',
          supplierTypeLabel: '供应商类型',
          otherSupplier: '其他供应商',
          contractStartTime: '协议开始日期',
          contractEndTime: '协议结束日期',
          purchaseTypeLabel: '采购类型',
          capitalTypeLabel: '资金来源',
          financeManagerLabel: '业务对应的财务经理',
          comtyTypeLabel: '商品类型',
          otherComty: '其他商品类型'
        },
        /* 合同信息 */
        contractInfoMap: {
          comtySourceLabel: '商品来源',
          contractStateLabel: '合同状态',
          annualPurPlanAmount: '预计年度采购金额（万元人民币）',
          proAccLimit: '立项额度（万元人民币）',
          empJudgeLabel: '是否已取得授权',
          orderNumReq: '起订量要求',
          orderType: '下采购订单的方式',
          saleRange: '分销范围',
          deliveryTypeLabel: '交货方式',
          priceReq: '供应商对产品销售定价要求（如有）',
          saleAimReq: '供应商对销售目标的要求（如有）',
          otherMonAppoint: '市场营销费用、补贴、奖励等约定',
          operationNeeds: '运营需求（如有）',
          storageTranReq: '仓储运输要求',
          acceptanceReq: '收货及验收要求',
          damagedAppoint: '短缺残损与滞销约定',
          returnGoodsApp: '退货条款',
          argueSolvePlace: '法律管辖仲裁地',
          endAppoint: '终止约定（如有）',
          specialAppoint: '特殊约定（如违约金、违约责任）（如有）'
        },
        /* 合同结算信息 */
        contractSettleInfoMap: {
          settleMentLabel: '结算方式',
          settleCury: '结算币种',
          otherSettleCury: '其他结算币种',
          settleAppointLabel: '结算条款',
          settleAccPeriod: '结算账期（自然日）',
          advanceChargeJudgeLabel: '预付款',
          bankAccChaAppoint: '约定银行账号变更条款',
          advanceChargeCury: '预付款币种',
          otherAdvanceChargeCury: '其他预付款币种',
          advanceCharge: '预付款金额（万元人民币）'
        },
        /* 合同其他信息 */
        contractOtherInfoMap: {
          counterpartDesc: '供应商描述',
          supProdDesc: '供应商商品描述'
        },
        /* 用印基本信息 */
        printInfoMap: {
          sealOrderLabel: '用印顺序',
          sealTypeLabel: '用印类型'
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
          const { data } = await purchaseFrameContractDetail({ id })
          this.resultData = data
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      }
    }
  }
</script>
