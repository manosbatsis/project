<template>
  <!-- 平台费用单详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        费用单号： {{ detail.code }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部： {{ detail.buName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        账单号： {{ detail.billCode }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户： {{ detail.customerName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        费用项目： {{ detail.itemProjectName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        费用类型： {{ detail.costTypeLabel }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        结算币种： {{ detail.currency }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        单据状态： {{ detail.statusLabel }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        单据来源： {{ detail.sourceLabel }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        创建人： {{ detail.confirmName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        创建时间： {{ detail.createDate }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        确认人： {{ detail.confirmName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        确认时间： {{ detail.confirmDate }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        转账单人：{{ detail.transferSlipName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        转账单时间：{{ detail.transferSlipDate }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 费用明细 -->
    <JFX-title title="费用明细" className="mr-t-10" />
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column prop="skuNo" align="center" label="平台SKU条码" />
      <el-table-column prop="goodsName" align="center" label="商品名称" />
      <el-table-column prop="poNo" align="center" label="PO号" />
      <el-table-column prop="num" align="center" label="数量" />
      <el-table-column prop="amount" align="center" label="结算金额" />
    </JFX-table>
    <!-- 费用明细 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    platformCostGetDetailsById,
    listPlatformCostOrderItem
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
            path: `/receivemoney/feelistdetail/${this.$route.params.id}`,
            meta: { title: '平台费用单详情' }
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
        const { data } = await platformCostGetDetailsById({ id })
        this.detail = data
      },
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listPlatformCostOrderItem({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            platformCostOrderId: this.$route.params.id
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
