<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
    <!-- title -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <!-- title end -->
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        预申报单号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        贸易条款：{{ detail.tradeTermsLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        退出仓库：{{ detail.outDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        退入仓库：{{ detail.inDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        退货币种：{{ detail.currency || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        PO号：{{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        LBX单号： {{ detail.lbxNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        关联销售退单号： {{ detail.saleReturnOrderCodes || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        海外仓理货单位： {{ detail.tallyingUnitLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        退货原因： {{ detail.returnReason || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        创建人： {{ detail.createName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        创建时间： {{ detail.createDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        审核人： {{ detail.auditUsername || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        审核时间： {{ detail.auditerDate || '- -' }}
      </el-col>
    </el-row>
    <JFX-title title="物流信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        装货港: {{ detail.loadPort || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        卸货港： {{ detail.portDestLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        收货地址： {{ detail.deliveryAddr || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        境外发货人： {{ detail.shipper || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        发票号：{{ detail.invoiceNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        报关合同号：{{ detail.contractNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        提单毛重KG： {{ detail.billWeight || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        箱数：{{ detail.boxNum || '0' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        托数：{{ detail.torrNum || '0' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        托盘材质：{{ detail.torrPackTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        包装方式: {{ detail.pack || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        运输方式：{{ detail.transportLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        出库关区：{{ detail.outCustomsName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        入库关区：{{ detail.inCustomsName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        头程提单号： {{ detail.ladingBill || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        唛头： {{ detail.mark || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        备注：{{ detail.remark || '- -' }}
      </el-col>
    </el-row>
    <!-- title -->
    <JFX-title title="商品信息" className="mr-t-15" />
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    ></JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getSaleDeclarReturnDetail } from '@a/salesReturnManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 面包屑
        breadcrumb: [
          { path: '', meta: { title: '销售退货管理' } },
          {
            path: '/saleReturn/PreDeclarationList',
            meta: { title: '销售退货预申报单' }
          },
          {
            path: `/saleReturn/PreDeclarationDetail?id=${this.$route.query.id}`,
            meta: { title: '销售退货预申报单详情' }
          }
        ],
        // 详情数据
        detail: {},
        // 表格列数据
        tableColumn: [
          {
            label: '序号',
            prop: 'seq',
            width: '55',
            align: 'center',
            hide: true
          },
          {
            label: '销售退货单号',
            prop: 'saleReturnOrderCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: 'PO单号',
            prop: 'poNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退出商品货号',
            prop: 'outGoodsNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品货号',
            prop: 'inGoodsNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品名称',
            prop: 'inGoodsName',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品条形码',
            prop: 'inBarcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退货商品数量',
            prop: 'num',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '退货商品单价',
            prop: 'price',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '退货总金额 \n（不含税）',
            prop: 'amount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '退货总金额 \n（含税）',
            prop: 'taxAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '税率',
            prop: 'taxRate',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '税额',
            prop: 'tax',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '批次号',
            prop: 'batchNo',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '品牌类型',
            prop: 'brandName',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '毛重',
            prop: 'grossWeightSum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '净重',
            prop: 'netWeightSum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '箱号',
            prop: 'boxNo',
            width: '100',
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
        const { id: declareOrderId } = this.$route.query
        try {
          const { data } = await getSaleDeclarReturnDetail({ declareOrderId })
          this.detail = data || {}
          this.tableData.list = this.detail.itemList || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      }
    }
  }
</script>
