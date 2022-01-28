<template>
  <!-- 销售退货订单详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        公司： {{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        退货类型： {{ detail.returnTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        客户： {{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        退入仓库： {{ detail.inDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        退出仓库： {{ detail.outDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        PO号： {{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        报关合同号： {{ detail.contractNo || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        报关合同号： {{ detail.contractNo || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        销售类型： {{ detail.businessModelLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        关联销售单： {{ detail.saleOrderRelCode || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        付款条约：{{ detail.payRules || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        退货方式：{{ detail.returnModeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        海外理货单位：{{ detail.tallyingUnitLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        LBX单号：{{ detail.lbxNo || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        退货币种：{{ detail.currencyLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        退货原因：{{ detail.remark || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        库位类型：{{ detail.stockLocationTypeName || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 销售退货商品明细 -->
    <JFX-title title="销售退货商品明细" className="mr-t-15" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      showIndex
    ></JFX-table>
    <!-- 销售退货商品明细 end -->
  </div>
</template>
<script>
  import { getSaleReturnDetail } from '@a/salesReturnManage/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情的数据
        detail: {},
        // 面包屑数据
        breadcrumb: [
          { path: '', meta: { title: '销售退货管理' } },
          { path: '/sales/salesreturnorder', meta: { title: '销售退货订单' } },
          {
            path: `/sales/salesreturndetail/${this.$route.query.id}`,
            meta: { title: '销售退货订单详情' }
          }
        ],
        // 表格列数据
        tableColumn: [
          {
            label: 'PO单号',
            prop: 'poNo',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: 'PO时间',
            prop: 'poDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '退出商品货号',
            prop: 'inGoodsNo',
            minWidth: '160',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品货号',
            prop: 'outGoodsNo',
            minWidth: '160',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品名称',
            prop: 'inGoodsName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品条形码',
            prop: 'barcode',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '退货商品数量',
            prop: 'returnNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '退货商品单价',
            prop: 'price',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退货总金额 \n（不含税）',
            prop: 'amount',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退货总金额 \n（含税）',
            prop: 'taxAmount',
            width: '120',
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
          }
        ]
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.getList()
    },
    methods: {
      async getList() {
        try {
          this.tableData.loading = true
          const { data } = await getSaleReturnDetail({
            id: this.$route.query.id
          })
          this.detail = data
          this.tableData = {
            list: data.itemList,
            loading: false
          }
        } catch (err) {
          this.tableData.loading = false
        }
      }
    }
  }
</script>
