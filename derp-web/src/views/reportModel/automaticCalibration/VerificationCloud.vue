<template>
  <!-- 云集账单校验页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="账单月份："
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                type="month"
                value-format="yyyy-MM"
                placeholder="请选择年月份"
                :clearable="false"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="结算单号："
              prop="settleId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.settleId"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="云集商品编码："
              prop="skuNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.skuNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="只看差异："
              prop="checkDifference"
              class="search-panel-item fs-14 clr-II"
            >
              <el-checkbox v-model="checkDifference" />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'yunji_auto_veri_export'"
          v-loading="exportBtnLoading"
          @click="exportList"
        >
          导出
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showIndex
      @change="getList"
    >
      <template slot="deliveryDifferent" slot-scope="{ row }">
        <div>出库：{{ row.deliveryDifferent || 0 }}</div>
        <div>入库：{{ row.returnDifferent || 0 }}</div>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listYunjiAutoVerification,
    exportYunjiAutoVerificationNum,
    exportYunjiAutoVerification
  } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: '',
          settleId: '',
          checkDifference: '',
          skuNo: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '账单月份',
            prop: 'month',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '结算单号',
            prop: 'settleId',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算日期',
            prop: 'settleDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '云集商品编码',
            prop: 'skuNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '平台发货量',
            prop: 'platformDeliveryAccount',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '平台退货量',
            prop: 'platformReturnAccount',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '系统出库量',
            prop: 'systemDeliveryAccount',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '系统入库量',
            prop: 'systemReturnAccount',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '差异',
            slotTo: 'deliveryDifferent',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '原因',
            prop: 'result',
            width: '110',
            align: 'center',
            hide: true
          }
        ],
        checkDifference: false,
        exportBtnLoading: false // 导出按钮loading
      }
    },
    activated() {
      this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
      this.getList()
    },
    mounted() {
      this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        this.searchProps.checkDifference = this.checkDifference ? '1' : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listYunjiAutoVerification({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.searchProps.checkDifference = this.checkDifference ? '1' : ''
        try {
          this.exportBtnLoading = true
          const { data } = await exportYunjiAutoVerificationNum({
            ...this.searchProps
          })
          if (data > 10000) {
            this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
            return false
          }
          this.$exportFile(exportYunjiAutoVerification, { ...this.searchProps })
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.exportBtnLoading = false
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.checkDifference = false
          this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
          this.getList(1)
        })
      }
    }
  }
</script>
