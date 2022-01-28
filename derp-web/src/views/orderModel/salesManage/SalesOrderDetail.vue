<template>
  <!-- 销售订单详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-15" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        销售订单编号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        出仓仓库：{{ detail.outDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        订单状态：{{ detail.stateLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        入仓仓库：{{ detail.inDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        参照订单：{{ detail.referToOrder || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        LBX单号：{{ detail.lbxNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        PO单号：{{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        销售类型：{{ detail.businessModelLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        PO单时间：{{ detail.poDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        销售币种：{{ detail.currencyLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        归属月份：{{ detail.ownMonth || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        运输方式：{{ detail.transportLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        海外仓理货单位：{{ detail.tallyingUnitLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        单据标识：{{ detail.orderTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        原销出仓仓库：{{ detail.originalOutDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        贸易条款：{{ detail.tradeTermsLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        付款条款：{{ detail.paymentTermsLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        审批单号：{{ detail.approvalNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        备注：{{ detail.remark || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        红冲状态：{{ detail.writeOffStatusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        库位类型：{{ detail.stockLocationTypeName || '- -' }}
      </el-col>
      <el-col
        class="mr-b-10"
        :xs="24"
        :sm="12"
        :md="12"
        :lg="8"
        :xl="6"
        v-if="detail.writeOffStatusLabel === '已红冲'"
      >
        红冲日期：{{ detail.writeOffDate || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 操作信息 -->
    <JFX-title title="操作信息" className="mr-t-15" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        创建人：{{ detail.createName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        创建时间：{{ detail.createDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        提交人：{{ detail.submiterName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        提交时间：{{ detail.submitDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        审核人：{{ detail.auditName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        审核时间：{{ detail.auditDate || '- -' }}
      </el-col>
    </el-row>
    <!-- 操作信息 end -->
    <el-tabs v-model="activeName" class="mr-t-15" type="card">
      <el-tab-pane label="商品信息" name="1">
        <JFX-table
          :tableData.sync="tableData"
          :showPagination="false"
          :summary-method="summaryMethod"
          showSummary
        >
          <el-table-column
            prop="seq"
            align="center"
            width="80"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="goodsCode"
            align="center"
            label="商品编号"
            width="130"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="goodsName"
            align="center"
            label="商品名称"
            min-width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="goodsNo"
            align="center"
            label="商品货号"
            width="130"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="barcode"
            align="center"
            label="条码"
            width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="num"
            align="center"
            label="销售数量"
            width="80"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="price"
            align="center"
            label="销售单价"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="declarePrice"
            v-if="false"
            align="center"
            label="申报单价"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column align="center" width="100">
            <template slot="header">
              <span>
                销售总金额
                <br />
                (不含税)
              </span>
            </template>
            <template slot-scope="{ row }">
              {{ row.amount }}
            </template>
          </el-table-column>
          <el-table-column align="center" width="100">
            <template slot="header">
              <span>
                销售总金额
                <br />
                (含税)
              </span>
            </template>
            <template slot-scope="{ row }">
              {{ row.taxAmount }}
            </template>
          </el-table-column>
          <el-table-column
            prop="taxRate"
            align="center"
            label="税率"
            width="80"
          ></el-table-column>
          <el-table-column
            prop="tax"
            align="center"
            label="税额"
            width="80"
          ></el-table-column>
        </JFX-table>
      </el-tab-pane>
      <el-tab-pane label="商品上架明细" name="2">
        <JFX-table
          :tableData.sync="saleShelfData"
          :summary-method="saleShelfSummary"
          showSummary
        >
          :showPagination="false">
          <el-table-column
            prop="goodsNo"
            align="center"
            label="商品货号"
            width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="goodsName"
            align="center"
            label="商品名称"
            min-width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="barcode"
            align="center"
            label="条码"
            width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="tallyingUnitLabel"
            align="center"
            label="海外仓理货单位"
            width="110"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="price"
            align="center"
            label="销售单价"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="num"
            align="center"
            label="销售数量"
            width="80"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="shelfNum"
            align="center"
            label="本次上架数量"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="damagedNum"
            align="center"
            label="本次残损数量"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="lackNum"
            align="center"
            label="本次少货数量"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="shelfAmount"
            align="center"
            label="上架总金额"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="shelfDate"
            align="center"
            label="本次上架时间"
            width="150"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="poNo"
            align="center"
            label="po单号"
            min-width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="remark"
            align="center"
            label="备注"
            min-width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="operator"
            align="center"
            label="操作人"
            width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="createDate"
            align="center"
            label="操作时间"
            width="150"
            show-overflow-tooltip
          ></el-table-column>
        </JFX-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listSaleDetail } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // tab当前点击name
        activeName: '1',
        // 详情数据
        detail: {},
        // 融资信息
        saleFinancingOrderDTO: {},
        // 上架明细列表
        saleShelfData: {
          list: []
        },
        // 融资信息列表
        saleFinancing: {
          list: []
        }
      }
    },
    computed: {
      total() {
        return (prop, precision) =>
          this.tableData.list
            .reduce((pre, item) => {
              pre += item[prop] ? item[prop] : 0
              return pre
            }, 0)
            .toFixed(precision)
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        if (!id) return false
        const {
          data: {
            saleOrderDTO = {},
            saleShelfDTO = {},
            saleFinancingOrderDTO = {}
          }
        } = await listSaleDetail({ id })
        this.detail = saleOrderDTO || {}
        this.tableData.list = this.detail.itemList || []
        if (this.tableData.list && this.tableData.list.length) {
          this.tableData.list = this.tableData.list.map((item) => {
            if (!item.taxRate) {
              return {
                ...item,
                taxRate: 0,
                taxAmount: item.amount,
                tax: 0
              }
            } else {
              return { ...item }
            }
          })
        }
        this.saleShelfData.list = saleShelfDTO
          ? saleShelfDTO.saleShelfDTOList
          : []
        this.saleFinancingOrderDTO = saleFinancingOrderDTO || {}
        this.saleFinancing.list = saleFinancingOrderDTO
          ? saleFinancingOrderDTO.itemList
          : []
      },
      // 合计方法
      summaryMethod({ data }) {
        const sums = []
        let nums = 0
        let amounts = 0
        let taxAmounts = 0
        let taxs = 0
        data.forEach((item) => {
          nums += item.num ? +item.num : 0
          amounts += item.amount ? +item.amount : 0
          taxAmounts += item.taxAmount ? +item.taxAmount : 0
          taxs += item.tax ? +item.tax : 0
        })
        sums[0] = '合计：'
        sums[5] = nums
        sums[7] = (+amounts).toFixed(2)
        sums[8] = (+taxAmounts).toFixed(2)
        sums[10] = (+taxs).toFixed(2)
        return sums
      },
      // 合计方法
      saleShelfSummary({ data }) {
        const sums = []
        // let nums = 0
        let shelfNums = 0
        let damagedNums = 0
        let lackNums = 0
        let shelfAmounts = 0
        data.forEach((item) => {
          // nums += item.num ? +item.num : 0
          shelfNums += item.shelfNum ? +item.shelfNum : 0
          damagedNums += item.damagedNum ? +item.damagedNum : 0
          lackNums += item.lackNum ? +item.lackNum : 0
          shelfAmounts += item.shelfAmount ? +item.shelfAmount : 0
        })
        sums[0] = '合计：'
        // sums[5] = nums
        sums[6] = shelfNums
        sums[7] = damagedNums
        sums[8] = lackNums
        sums[9] = (+shelfAmounts).toFixed(2)
        return sums
      }
    }
  }
</script>
