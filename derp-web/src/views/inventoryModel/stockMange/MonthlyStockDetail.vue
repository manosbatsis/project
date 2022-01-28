<template>
  <!-- 库存月结详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        结转状态：{{ detail.stateLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        结转月份：{{ detail.settlementMonth || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        结算时间：{{ detail.settlementDate || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        公司：{{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        期初时间：{{ detail.firstDate || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        结算人：{{ detail.planName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        期末时间：{{ detail.endDate || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        创建时间：{{ detail.createDate || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 商品信息 -->
    <JFX-title title="商品信息" className="mr-t-10" />
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      :summary-method="summaryMethod"
      showIndex
      showSummary
    ></JFX-table>
    <!-- 表格 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { toMonthlyDetailPage } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        // 表格列结构
        tableColumn: [
          {
            label: '仓库名称',
            prop: 'depotName',
            align: 'center',
            hide: true,
            minWidth: '140'
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            align: 'center',
            minWidth: '120',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '商品条码',
            prop: 'barcode',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '批次',
            prop: 'batchNo',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '库存余量',
            prop: 'surplusNum',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '单位',
            prop: 'unitLabel',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '库存类型',
            prop: 'typeLabel',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '期末结转库存',
            prop: 'settlementNum',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '是否过期',
            prop: 'isExpireLabel',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '标准条码',
            prop: 'commbarcode',
            align: 'center',
            minWidth: '100',
            hide: true
          }
        ],
        userType: ''
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
            data: { monthlyAccountModel, monthlyAccountItemModelList, userType }
          } = await toMonthlyDetailPage({ id })
          this.detail = monthlyAccountModel || {}
          this.tableData.list = monthlyAccountItemModelList || []
          this.userType = userType || ''
          /* usertype等于1和2 才添加仓库现存量表格列 */
          if (['2', '1'].includes(userType)) {
            const targetIdx = this.tableColumn.findIndex(
              (item) => item.label === '批次'
            )
            this.tableColumn.splice(targetIdx + 1, 0, {
              label: '仓库现存量',
              prop: 'availableNum',
              align: 'center',
              minWidth: '100',
              hide: true
            })
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 合计 */
      summaryMethod({ data }) {
        const sums = []
        let surplusNum = 0
        let availableNum = 0
        data.forEach((item) => {
          surplusNum += item.surplusNum ? +item.surplusNum : 0
          availableNum += item.availableNum ? +item.availableNum : 0
        })
        sums[0] = '合计'
        if (['2', '1'].includes(this.userType)) {
          sums[6] = +availableNum
          sums[7] = +surplusNum
        } else {
          sums[6] = +surplusNum
        }
        return sums
      }
    }
  }
</script>
