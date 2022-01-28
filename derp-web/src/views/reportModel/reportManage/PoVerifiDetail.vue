<template>
  <!-- 唯品PO账单核销报表详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
    <!-- 面包屑 end -->
    <!-- 账单出库单详情 -->
    <JFX-title title="基本信息" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        公司： {{ detail.merchantName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户：{{ detail.customerName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        PO单号： {{ detail.po }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        PO时间：{{ detail.poDate }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        标准条码： {{ detail.commbarcode }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部： {{ detail.buName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        标准品牌： {{ detail.brandParent }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        母品牌： {{ detail.superiorParentBrand }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        商品名称： {{ detail.goodsName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        销售币种：{{ detail.currency }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 商品上架明细 -->
    <JFX-title title="商品上架明细" />
    <JFX-table :tableData.sync="tableDataObj.shelfTableData">
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column prop="orderNo" align="center" label="销售订单号" />
      <el-table-column prop="num" align="center" label="销售数量" />
      <el-table-column prop="salePrice" align="center" label="销售单价" />
      <el-table-column prop="shelfNum" align="center" label="上架数量" />
      <el-table-column prop="damagedNum" align="center" label="残损数量" />
      <el-table-column prop="lackNum" align="center" label="少货数量" />
      <el-table-column prop="shelfDate" align="center" label="上架时间" />
    </JFX-table>
    <!-- 商品上架明细 end -->
    <!-- 销售出库明细 -->
    <JFX-title title="销售出库明细" />
    <JFX-table :tableData.sync="tableDataObj.saleOutTableData">
      <el-table-column
        prop="saleOutOrder"
        align="center"
        label="销售出库单号"
      />
      <el-table-column prop="vipBillCode" align="center" label="唯品账单号" />
      <el-table-column prop="billType" align="center" label="账单类型" />
      <el-table-column prop="saleOrder" align="center" label="销售订单号" />
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column prop="outDepotNum" align="center" label="出库数量" />
      <el-table-column prop="outDepotDate" align="center" label="出库时间" />
    </JFX-table>
    <!-- 销售出库明细 end -->
    <!-- 销售退明细 -->
    <JFX-title title="销售退明细" />
    <JFX-table :tableData.sync="tableDataObj.returnOdepotTableData">
      <el-table-column
        prop="saleReturnOrder"
        align="center"
        label="销售退订单号"
      />
      <el-table-column
        prop="saleReturnOdepotOrder"
        align="center"
        label="销售退出库单号"
      />
      <el-table-column
        prop="saleReturnOdepotType"
        align="center"
        label="退货类型"
      />
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column
        prop="saleReturnOdepotNum"
        align="center"
        label="退货数量"
      />
      <el-table-column
        prop="saleReturnOdepotDate"
        align="center"
        label="退货时间"
      />
    </JFX-table>
    <!-- 销售退明细 end -->
    <!-- 国检抽样明细 -->
    <JFX-title title="国检抽样明细" />
    <JFX-table :tableData.sync="tableDataObj.nationalInspectionTableData">
      <el-table-column
        prop="adjustmentInventoryOrder"
        align="center"
        label="库存调整单号"
      />
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column
        prop="adjustmentInventoryType"
        align="center"
        label="调整类型"
      />
      <el-table-column
        prop="adjustmentInventoryNum"
        align="center"
        label="调整数量"
      />
      <el-table-column
        prop="adjustmentInventoryMonths"
        align="center"
        label="归属月份"
      />
      <el-table-column
        prop="adjustmentInventoryDate"
        align="center"
        label="调整时间"
      />
    </JFX-table>
    <!-- 国检抽样明细 end -->
    <!-- 唯品红冲明细 -->
    <JFX-title title="唯品红冲明细" />
    <JFX-table :tableData.sync="tableDataObj.hcTableData">
      <el-table-column
        prop="adjustmentInventoryOrder"
        align="center"
        label="库存调整单号"
      />
      <el-table-column prop="sourceCode" align="center" label="唯品账单号" />
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column
        prop="adjustmentInventoryType"
        align="center"
        label="调整类型"
      />
      <el-table-column
        prop="adjustmentInventoryNum"
        align="center"
        label="调整数量"
      />
      <el-table-column
        prop="adjustmentInventoryMonths"
        align="center"
        label="归属月份"
      />
      <el-table-column
        prop="adjustmentInventoryDate"
        align="center"
        label="调整时间"
      />
    </JFX-table>
    <!-- 唯品红冲明细 end -->
    <!-- 唯品报废明细 -->
    <JFX-title title="唯品报废明细" />
    <JFX-table :tableData.sync="tableDataObj.scrapTableData">
      <el-table-column
        prop="adjustmentInventoryOrder"
        align="center"
        label="库存调整单号"
      />
      <el-table-column prop="sourceCode" align="center" label="唯品账单号" />
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column
        prop="adjustmentInventoryType"
        align="center"
        label="调整类型"
      />
      <el-table-column
        prop="adjustmentInventoryNum"
        align="center"
        label="调整数量"
      />
      <el-table-column
        prop="adjustmentInventoryMonths"
        align="center"
        label="归属月份"
      />
      <el-table-column
        prop="adjustmentInventoryDate"
        align="center"
        label="调整时间"
      />
    </JFX-table>
    <!-- 唯品报废明细 end -->
    <!-- 唯品盘点明细 -->
    <JFX-title title="唯品盘点明细" />
    <JFX-table :tableData.sync="tableDataObj.takeStockTableData">
      <el-table-column
        prop="takesStockResultOrder"
        align="center"
        label="盘点单号"
      />
      <el-table-column prop="sourceCode" align="center" label="唯品账单号" />
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column
        prop="takesStockResultType"
        align="center"
        label="调整类型"
      />
      <el-table-column
        prop="takesStockResultNum"
        align="center"
        label="调整数量"
      />
      <el-table-column prop="receiveTime" align="center" label="接收时间" />
    </JFX-table>
    <!-- 唯品盘点明细 end -->
    <!-- 唯品调拨明细 -->
    <JFX-title title="唯品调拨明细" />
    <JFX-table :tableData.sync="tableDataObj.transferTableData">
      <el-table-column prop="transferOrder" align="center" label="调拨单据号" />
      <el-table-column
        prop="transferDepotOrder"
        align="center"
        label="调拨出/入库单号"
      />
      <el-table-column prop="transferType" align="center" label="调拨类型" />
      <el-table-column prop="goodsNo" align="center" label="商品货号" />
      <el-table-column prop="transferNum" align="center" label="调拨数量" />
      <el-table-column prop="transferTime" align="center" label="调拨时间" />
    </JFX-table>
    <!-- 唯品调拨明细 end -->
    <!-- 账单明细 -->
    <!-- <JFX-title title="账单明细" />
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column prop="barcode" align="center" label="唯品账单号" />
      <el-table-column prop="barcode" align="center" label="账单类型" />
      <el-table-column prop="barcode" align="center" label="条形码" />
      <el-table-column prop="barcode" align="center" label="账单数量" />
      <el-table-column prop="barcode" align="center" label="销售价格" />
      <el-table-column prop="barcode" align="center" label="账单金额" />
      <el-table-column prop="barcode" align="center" label="账单时间" />
    </JFX-table> -->
    <!-- 账单明细 end -->
  </div>
</template>

<script>
  import {
    vipPoBillVerificationToDetail,
    getDetailsByType
  } from '@a/reportManage/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        breadcrumb: [
          {
            path: '',
            meta: { title: '报表管理' }
          },
          {
            path: '/report/pobillreport',
            meta: { title: '唯品PO账单核销报表' }
          },
          {
            path: '',
            meta: { title: '唯品PO账单核销报表详情' }
          }
        ],
        tableDataObj: {},
        // 表格数据
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        detail: {}
      }
    },
    async mounted() {
      const { poNo, commbarcode, depotId } = this.$route.query
      const {
        data: { detail }
      } = await vipPoBillVerificationToDetail({
        po: poNo,
        commbarcode,
        depotId
      })
      this.detail = detail
      this.getList()
    },
    methods: {
      // 拉取表格数据
      getList() {
        const that = this
        const { poNo, commbarcode } = this.$route.query
        // 加载表格数据
        async function loadTable(type) {
          const { data } = await getDetailsByType({
            type,
            po: poNo,
            commbarcode
          })
          that.$set(that.tableDataObj, type + 'TableData', {
            list: data,
            loading: false,
            pageNum: 1,
            pageSize: 10,
            total: 0
          })
        }
        loadTable('shelf')
        loadTable('saleOut')
        loadTable('returnOdepot')
        loadTable('nationalInspection')
        loadTable('hc')
        loadTable('scrap')
        loadTable('takeStock')
        loadTable('transfer')
      }
    }
  }
</script>
