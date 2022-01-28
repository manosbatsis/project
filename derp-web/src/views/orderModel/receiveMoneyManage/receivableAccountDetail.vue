<template>
  <!-- 应收账龄报告详情 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        渠道类型：{{ detail.channelTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户名称：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户简称：{{ detail.simpleName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        应收收入（原币）：{{ detail.currency + ' ' + detail.originalAmount }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        应收收入（RMB）：{{ detail.rmbCurrency + ' ' + detail.rmbAmount }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 应收余额明细 -->
    <JFX-title title="应收余额明细" className="mr-t-10" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :summary-method="summaryMethod"
      :showPagination="false"
      showSummary
    >
      <template slot="writtenOffAmount" slot-scope="{ row }">
        {{
          row.writtenOffAmount
            ? detail.currency + ' ' + row.writtenOffAmount
            : ''
        }}
      </template>
    </JFX-table>
    <!-- 应收余额明细 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getReceiveAgingReportDetail } from '@a/receiveMoneyManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        // 操作日志表格列结构
        tableColumn: [
          {
            label: '单据类型',
            prop: 'orderTypeLabel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'PO号',
            prop: 'poNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '单据号',
            prop: 'code',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '待核销金额',
            slotTo: 'writtenOffAmount',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '应收月份',
            prop: 'month',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        try {
          const { data } = await getReceiveAgingReportDetail({ id })
          this.detail = data || {}
          const { itemList } = this.detail
          this.tableData.list = itemList || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 合计方法
      summaryMethod({ data }) {
        const sums = []
        let writtenOffAmounts = 0
        data.forEach((item) => {
          writtenOffAmounts += item.writtenOffAmount
            ? +item.writtenOffAmount
            : 0
        })
        sums[0] = '合计'
        sums[3] = `${this.detail.currency} ${writtenOffAmounts.toFixed(2)}`
        return sums
      }
    }
  }
</script>

<style lang="scss" scoped>
  .title-bx {
    display: flex;
    justify-content: space-between;
    padding: 0 20vw 0 15px;
    color: #0000ff;
    > span {
      display: inline-block;
    }
  }
  ::v-deep.textarea-con {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 120px;
    }
    .el-form-item__content {
      width: 630px;
    }
  }
</style>
