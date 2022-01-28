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
        仓库自编码：{{ detail.depotCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        OP仓库编号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        仓库名称：{{ detail.name || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        仓库类别：{{ detail.typeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        仓库类型：{{ detail.depotTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        申报海关编号：{{ detail.customsNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        申报国检编号：{{ detail.inspectNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        仓管员：{{ detail.adminName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        联系电话：{{ detail.tel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        仓库所在国家：{{ detail.country || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        ISV仓库类型：{{ detail.isvdepotTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        是否代销仓：{{ detail.isTopBooks === '1' ? '是' : '否' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        是否启用：{{ detail.status === '1' ? '是' : '否' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        仓库详细地址：{{ detail.address || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 流程设置 -->
    <JFX-title title="流程设置" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        入仓下推指令appkey：{{
          { '1': '公司key', '2': '关联公司key' }[detail.isMerchant] || '- -'
        }}
      </el-col>
      <el-col
        class="mr-b-10"
        :xs="24"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="6"
        v-if="detail.isMerchant === '2'"
      >
        关联公司：{{ relDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        是否批次效期强校验：{{ detail.batchValidationLabel || '- -' }}
      </el-col>
    </el-row>
    <!-- 流程设置 end -->
    <!-- 变更记录 -->
    <JFX-title title="变更记录" class="mr-t-15" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    ></JFX-table>
    <!-- 变更记录 end -->
    <!-- 变更记录 -->
    <JFX-title title="仓库关区信息" class="mr-t-15" />
    <JFX-table
      :tableData.sync="depotTableData"
      :tableColumn="depotTableColumn"
      :showPagination="false"
    ></JFX-table>
    <!-- 变更记录 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getDepotDetail } from '@a/warehouseFile'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        tableColumn: [
          {
            label: '仓库自编码',
            prop: 'depotCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '仓库名称',
            prop: 'depotName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '是否批次效期强校验',
            prop: 'batchValidation',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建/修改时间',
            prop: 'createDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '月结生效时间',
            prop: 'effectiveTime',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        depotTableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        depotTableColumn: [
          {
            label: '平台关区',
            prop: 'customsAreaName',
            minWidth: '140',
            align: 'center',
            hide: true
          },

          {
            label: '关区代码',
            prop: 'customsAreaCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },

          {
            label: '电商账册号',
            prop: 'onlineRegisterNo',
            minWidth: '140',
            align: 'center',
            hide: true
          },

          {
            label: '收货人公司名称（中文）',
            prop: 'recCompanyName',
            minWidth: '140',
            align: 'center',
            hide: true
          },

          {
            label: '收货人公司名称（英文）',
            prop: 'recCompanyEnname',
            minWidth: '140',
            align: 'center',
            hide: true
          },

          {
            label: '仓库地址',
            prop: 'address',
            minWidth: '140',
            align: 'center',
            hide: true
          }
        ],
        relDepotName: ''
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
          const {
            data: { detail, batch, merchantBean, depotCustomsRelList }
          } = await getDepotDetail({ id })
          this.detail = detail || {}
          this.tableData.list = batch || []
          this.depotTableData.list = depotCustomsRelList || []
          if (merchantBean && merchantBean.length) {
            const target = merchantBean.find(
              (item) => item.id === detail.merchantId
            )
            this.relDepotName = target ? target.name : ''
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      }
    }
  }
</script>
