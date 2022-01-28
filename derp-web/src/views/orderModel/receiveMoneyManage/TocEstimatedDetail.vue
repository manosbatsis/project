<template>
  <!-- To-C暂估应收表详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        平台：{{ detail.storePlatformName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        应收状态：{{ detail.settlementStatusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        店铺名称：{{ detail.shopName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        应收月份：{{ detail.month || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        暂估币种：{{ detail.currency || 'CNY' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData="tableData"
      :tableColumn="tableColumn"
      v-if="$route.query.type === 'income'"
      @change="listToCTempReceiveItem"
    >
      <template slot="settlementOriAmount" slot-scope="{ row }">
        {{ row.originalCurrency || '' }} {{ row.settlementOriAmount || '' }}
      </template>
    </JFX-table>
    <JFX-table
      :tableData="feeTableData"
      :tableColumn="feeColumn"
      v-if="$route.query.type === 'fee'"
      @change="listToCTempReceiveCostItem"
    >
      <template slot="settlementOriCost" slot-scope="{ row }">
        {{ row.originalCurrency || '' }} {{ row.settlementOriCost || '' }}
      </template>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    tocTempReceiveDetail,
    listToCTempReceiveCostItem,
    listToCTempReceiveItem,
    tocTempCostReceiveDetail
  } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        // 当前tabs页指向
        activeName: '1',
        // 暂估应收核销明细表格结构
        tableColumn: [
          {
            label: '外部订单号',
            prop: 'externalCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '系统订单号',
            prop: 'orderCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '单据类型',
            prop: 'orderTypeLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '暂估应收金额 \n（RMB）',
            prop: 'temporaryRmbAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '平台结算货款 \n（RMB）',
            prop: 'settlementRmbAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '平台结算货款 \n（原币）',
            prop: 'settlementOriAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '冲销金额 \n（RMB）',
            prop: 'writeOffAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '未核金额',
            prop: 'nonVerifyAmount',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算标识',
            prop: 'settlementMarkLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算日期',
            prop: 'settlementDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '结算单号',
            prop: 'settlementCode',
            width: '140',
            align: 'center',
            hide: true
          }
        ],
        // 暂估费用核销明细表格数据
        feeTableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        // 暂估费用核销明细表格结构
        feeColumn: [
          {
            label: '外部订单号',
            prop: 'externalCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '系统订单号',
            prop: 'orderCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '单据类型',
            prop: 'orderTypeLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '系统费项名称',
            prop: 'projectName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '平台费项名称',
            prop: 'platformProjectName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '暂估费用金额 \n（RMB）',
            prop: 'temporaryRmbCost',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '平台结算费用 \n（RMB）',
            prop: 'settlementRmbCost',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '平台结算费用 \n（原币）',
            prop: 'settlementOriCost',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '冲销金额 \n（RMB）',
            prop: 'writeOffAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '差异调整金额',
            prop: 'adjustmentRmbAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '未核销金额',
            prop: 'nonVerifyAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '核销状态',
            prop: 'settlementMarkLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算日期',
            prop: 'settlementDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '结算单号',
            prop: 'settlementCode',
            width: '140',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.init()
      const { type } = this.$route.query
      if (type === 'income') {
        this.listToCTempReceiveItem()
      } else {
        this.listToCTempReceiveCostItem()
      }
    },
    methods: {
      async init() {
        const { id: billId, type } = this.$route.query
        try {
          // 获取详情
          if (type === 'income') {
            const { data } = await tocTempReceiveDetail({ billId })
            this.detail = data || {}
          } else {
            const { data } = await tocTempCostReceiveDetail({ billId })
            this.detail = data || {}
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 暂估应收核销明细
      async listToCTempReceiveItem(pageNum) {
        const { id } = this.$route.query
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listToCTempReceiveItem({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            billId: id
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 暂估费用核销明细
      async listToCTempReceiveCostItem(pageNum) {
        const { id } = this.$route.query
        try {
          this.feeTableData.pageNum = pageNum || this.feeTableData.pageNum
          this.feeTableData.loading = true
          const { data } = await listToCTempReceiveCostItem({
            begin: (this.feeTableData.pageNum - 1) * this.feeTableData.pageSize,
            pageSize: this.feeTableData.pageSize || 10,
            billId: id
          })
          this.feeTableData.list = data.list
          this.feeTableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.feeTableData.loading = false
        }
      }
    }
  }
</script>
