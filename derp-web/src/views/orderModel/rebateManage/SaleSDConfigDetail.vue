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
        公司：{{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        生效时间：{{ detail.effectiveDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        失效时间：{{ detail.expirationDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        平台备注：{{ detail.remark || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        创建人：{{ detail.createName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        创建时间：{{ detail.createDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        审核人：{{ detail.auditName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        审核时间：{{ detail.auditDate || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 单比例配置 -->
    <JFX-title title="单比例配置" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    ></JFX-table>
    <!-- 单比例配置 end -->
    <!-- 多比例配置 -->
    <JFX-title title="多比例配置" />
    <JFX-table
      :tableData.sync="tableData2"
      :tableColumn="tableColumn2"
      :showPagination="false"
    ></JFX-table>
    <!-- 多比例配置 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { sdSaleConfigDetail } from '@a/rebateManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        // 表格列数据
        tableColumn: [
          {
            label: 'SD类型',
            prop: 'sdTypeName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '简称',
            prop: 'sdSimpleName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '映射费项',
            prop: 'projectName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '比例',
            prop: 'proportion',
            minWidth: '100',
            align: 'center',
            hide: true
          }
        ],
        tableColumn2: [
          {
            label: 'SD类型',
            prop: 'sdTypeName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '简称',
            prop: 'sdSimpleName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '映射费项',
            prop: 'projectName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '标准品牌',
            prop: 'brandParent',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '标准条码',
            prop: 'commbarcode',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '比例',
            prop: 'proportion',
            minWidth: '100',
            align: 'center',
            hide: true
          }
        ],
        tableData2: {
          list: []
        }
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        if (!id) return false
        const { data } = await sdSaleConfigDetail({ id })
        this.detail = data
        this.tableData.list = data.singleList || []
        this.tableData2.list = data.multipleList || []
      }
    }
  }
</script>
