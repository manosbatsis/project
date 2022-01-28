<template>
  <section>
    <JFX-title title="NC信息" className="mr-t-20" />
    <el-row :gutter="10" class="fs-12 clr-II mr-t-20 target-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        NC单据号：{{ target.code || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        NC单据状态：{{ target.ncStatusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        凭证编号：{{ target.voucherCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        凭证状态：{{ target.voucherStatusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        凭证名称：{{ target.voucherName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        凭证序号：{{ target.voucherIndex || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        会计期间：{{ target.period || '- -' }}
      </el-col>
    </el-row>
    <JFX-title title="核销记录" className="mr-t-20" />
    <JFX-table :tableData.sync="tableData" :showPagination="false">
      <el-table-column
        prop="price"
        label="核销金额"
        align="center"
        min-width="100"
      ></el-table-column>

      <el-table-column
        prop="currency"
        label="结算币种"
        align="center"
        min-width="120"
      ></el-table-column>

      <el-table-column
        prop="receiveDate"
        label="收款日期"
        align="center"
        min-width="140"
      ></el-table-column>

      <el-table-column
        prop="receiceNo"
        label="收款流水单号"
        align="center"
        min-width="110"
      ></el-table-column>
      <el-table-column
        prop="verifyDate"
        label="核销时间"
        align="center"
        min-width="140"
      ></el-table-column>
      <el-table-column
        prop="verifier"
        label="核销人"
        align="center"
        min-width="100"
      ></el-table-column>
    </JFX-table>
  </section>
</template>
<script>
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      detail: {
        type: Object,
        default: function () {
          return {}
        }
      }
    },
    data() {
      return {
        target: {}
      }
    },
    mounted() {
      this.tableData.pageSize = 10000
      const { tocSettlementReceiveBillDTO = {}, verifyItemModels } = this.detail
      this.target = {
        ...tocSettlementReceiveBillDTO
      }
      this.tableData.list = verifyItemModels || []
    }
  }
</script>
