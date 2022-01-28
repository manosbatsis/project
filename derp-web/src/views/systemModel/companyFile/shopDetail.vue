<template>
  <!-- 客户详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        店铺类型：{{ detail.storeTypeName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        店铺统一ID：{{ detail.shopUnifyId || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        店铺名称：{{ detail.shopName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        店铺编码：{{ detail.shopCode || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        开店公司：{{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户名称：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        卓志编码：{{ detail.topidealCode || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        状态：{{ detail.statusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        仓库名称：{{ detail.depotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        电商平台：{{ detail.storePlatformName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        运营类型：{{ detail.shopTypeName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        数据来源：{{ detail.dataSourceName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        开店事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        专营母品牌：{{ detail.superiorParentBrandName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        结算币种：{{ detail.currencyLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        库位类型：{{ detail.stockLocationTypeName || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <JFX-title title="店铺货主信息" className="mr-t-10" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    />
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { shopDetail } from '@a/companyFile/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        // tableColumn
        tableColumn: [
          {
            label: '货主公司',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '货主事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '库位类型',
            prop: 'stockLocationTypeName',
            minWidth: '120',
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
        const { id } = this.$route.query
        if (!id) return false
        try {
          const {
            data: { detail, shipperList }
          } = await shopDetail({ id })
          this.detail = detail || {}
          this.tableData.list = shipperList
        } catch (error) {
          this.$errorMsg(error.message)
        }
      }
    }
  }
</script>
