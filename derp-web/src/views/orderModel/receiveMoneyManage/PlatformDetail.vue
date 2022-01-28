<template>
  <!-- 平台结算单详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        平台结算单号： {{ detail.billCode }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        账单月份：{{ detail.month }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户： {{ detail.customerName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        账单金额： {{ detail.billAmount }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        结算币种：{{ detail.currency }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        是否已开票： {{ detail.isInvoiceLabel }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        开票人： {{ detail.invoiceDrawer }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        开票日期：{{ detail.invoiceDate }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        账单日期：{{ detail.billDate }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 账单信息 -->
    <JFX-title title="账单信息" className="mr-t-10" />
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column prop="typeLabel" align="center" label="类型" />
      <el-table-column prop="poNo" align="center" label="PO号" />
      <el-table-column prop="barcode" align="center" label="商品条码" />
      <el-table-column prop="goodsName" align="center" label="商品名称" />
      <el-table-column prop="brandParent" align="center" label="标准品牌" />
      <el-table-column prop="settlementNum" align="center" label="结算数量" />
      <el-table-column
        prop="settlementAmount"
        align="center"
        label="结算金额（原币）"
      />
      <el-table-column
        prop="settlementAmountRmb"
        align="center"
        label="结算金额（RMB）"
      />
    </JFX-table>
    <!-- 账单信息 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    platformStatementGetDetailsById,
    listPlatformStatementItem
  } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 面包屑数据
        breadcrumb: [
          { path: '', meta: { title: '账单管理' } },
          {
            path: '/receivemoney/statementplatform',
            meta: { title: '平台结算单' }
          },
          {
            path: `/receivemoney/platformdetail/${this.$route.params.id}`,
            meta: { title: '平台结算单详情' }
          }
        ],
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 11
        },
        detail: {}
      }
    },
    mounted() {
      this.getDetail()
      this.getList()
    },
    methods: {
      async getDetail() {
        const { id } = this.$route.params
        const { data } = await platformStatementGetDetailsById({ id })
        this.detail = data
      },
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listPlatformStatementItem({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            platformStatementOrderId: this.$route.params.id
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (err) {
          console.log(err)
        } finally {
          this.tableData.loading = false
        }
      }
    }
  }
</script>
