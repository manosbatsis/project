<template>
  <!-- 唯品账单校验页面 -->
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
              label="年月份："
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
              label="PO号："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.poNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="账单号："
              prop="crawlerNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.crawlerNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="校验结果："
              prop="verificationResult"
              class="search-panel-item fs-14 clr-II"
              filterable
            >
              <el-select
                v-model="searchProps.verificationResult"
                placeholder="请选择"
                clearable
                :data-list="getSelectList('vipAutoVeri_verificationResultList')"
              >
                <el-option
                  v-for="item in selectOpt.vipAutoVeri_verificationResultList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
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
          v-permission="'vip_auto_veri_export'"
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
    />
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listVipAutoVerification,
    exportVipAutoVerification
  } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: '',
          poNo: '',
          crawlerNo: '',
          verificationResult: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '年月份',
            prop: 'month',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '账单号',
            prop: 'crawlerNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'PO号',
            prop: 'poNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '账单销售总量',
            prop: 'billSalesAccount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '账单红冲总量',
            prop: 'billHcAccount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '账单其他总量（调增）',
            prop: 'billAdjustmentIncreaseAccount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '账单其他总量（调减）',
            prop: 'billAdjustmentDecreaseAccount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '系统销售出库总量',
            prop: 'systemSalesOutAccount',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '系统红冲总量',
            prop: 'systemHcAccount',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '系统库存调整（调增）',
            prop: 'systemAdjustmentIncreaseAccount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '系统库存调整（调减）',
            prop: 'systemAdjustmentDecreaseAccount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '校验结果',
            prop: 'verificationResultLabel',
            width: '100',
            align: 'center',
            hide: true
          }
        ],
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
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listVipAutoVerification({
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
        const { poNo, crawlerNo } = this.searchProps
        if (!poNo && !crawlerNo) {
          this.$errorMsg('请先填写PO号或者账单号')
          return false
        }
        this.exportBtnLoading = true
        this.$exportFile(exportVipAutoVerification, { ...this.searchProps })
        this.exportBtnLoading = false
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
          this.getList(1)
        })
      }
    }
  }
</script>
