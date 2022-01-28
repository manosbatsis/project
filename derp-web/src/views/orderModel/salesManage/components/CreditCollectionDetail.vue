<template>
  <div>
    <JFX-Dialog
      :visible.sync="visible"
      :showClose="true"
      @comfirm="visible.show = false"
      :showConfirmBtn="false"
      :width="'1200px'"
      :title="'赊销收款详情'"
      :btnAlign="'center'"
      top="6vh"
    >
      <el-row class="company-page">
        <el-row>
          <el-col :span="8">
            滞纳金减免金额：{{ filterData.discountDelayAmount }}
          </el-col>
          <el-col :span="8"> 减免原因：{{ filterData.discountReason }} </el-col>
        </el-row>
        <el-col :span="24" class="mr-t-20">
          <!-- 表格 -->
          <JFX-table
            :tableData.sync="tableData"
            :showPagination="false"
            :tableHeight="'460px'"
          >
            <el-table-column
              prop="goodsNo"
              label="商品货号"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="goodsName"
              label="商品名称"
              align="center"
              min-width="120"
            ></el-table-column>
            <el-table-column
              prop="num"
              label="赎回数量"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="marginAmount"
              label="分摊保证金"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="principalAmount"
              label="分摊本金"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="occupationAmount"
              label="资金占用费"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="agencyAmount"
              label="代理费"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="delayAmount"
              label="滞纳金"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="discountDelayAmount"
              label="滞纳金减免金额"
              align="center"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="receivableAmount"
              label="应收金额"
              align="center"
              width="120"
            ></el-table-column>
          </JFX-table>
          <!-- 表格 end-->
        </el-col>
      </el-row>
    </JFX-Dialog>
  </div>
</template>
<script>
  import { searchBillItemDetail } from '@a/salesManage/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      visible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      filterData: {
        type: Object,
        default: function () {
          return {}
        }
      }
    },
    data() {
      return {}
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.filterData
        const { data } = await searchBillItemDetail({ id })
        if (data && data.length) {
          this.tableData.list = data
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  .company-page {
    width: 100%;
    margin-bottom: 10px;
    min-height: 200px;
  }
  .fen-pei-bx {
    margin-top: -30px;
    border-top: solid 1px #eaeaea;
    border-bottom: solid 1px #eaeaea;
    height: 80px;
  }
  .dialog-footer {
    height: 30px;
    margin-top: -30px;
  }
</style>
