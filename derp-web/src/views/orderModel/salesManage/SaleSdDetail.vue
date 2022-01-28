<template>
  <!-- 客户额度预警页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        销售SD单号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        业务单号：{{ detail.businessCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        来源单号：{{ detail.orderCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        PO号：{{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        SD简称：{{ detail.sdTypeName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        销售币种：{{ detail.currency || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        归属日期：{{ detail.ownDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        修改人：{{ detail.modifiyName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        修改时间：{{ detail.modifyDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        修改时间：{{ detail.isWriteOffLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        是否红冲单：{{ detail.isWriteOffLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        原销售SD单号：{{ detail.originalSaleSdOrderCode || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 商品SD明细 -->
    <JFX-title title="商品SD明细" class="mr-t-15" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    ></JFX-table>
    <!-- 商品SD明细 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { detailPreSaleOrder } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        // 表格列结构
        tableColumn: [
          {
            label: '商品货号',
            prop: 'goodsNo',
            align: 'center',
            hide: true,
            minWidth: '140'
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            align: 'center',
            minWidth: '120',
            hide: true
          },
          {
            label: '上架商品数量',
            prop: 'num',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '销售单价',
            prop: 'price',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '上架金额',
            prop: 'amount',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: 'SD比例',
            prop: 'sdRatio',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: 'SD金额',
            prop: 'sdAmount',
            align: 'center',
            minWidth: '100',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.getDetail()
    },
    methods: {
      async getDetail() {
        const { id } = this.$route.query
        if (!id) return false
        try {
          const { data } = await detailPreSaleOrder({ id })
          this.detail = data || {}
          const { itemList } = this.detail
          this.tableData.list = itemList || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      }
    }
  }
</script>
