<template>
  <!-- 销售订单列表页面 -->
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
              label="权责月份："
              prop="ownMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.ownMonth"
                type="month"
                placeholder="请选择"
                style="width: 203px"
                value-format="yyyy-MM"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.buId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getBUSelectBean('businessList')"
              >
                <el-option
                  v-for="item in selectOpt.businessList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                :data-list="getCustomerSelectBean('customer_list')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.customer_list"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO单号："
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
              label="销售赊销单号："
              prop="creditOrderCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.creditOrderCode"
                placeholder="请输入"
                clearable
              />
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
          v-permission="'sale_fund_occupation_export'"
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
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="buName" slot-scope="{ row }">
        <div>{{ row.buName }}</div>
        <div>{{ row.merchantName }}</div>
      </template>
      <template slot="creditAmount" slot-scope="{ row }">
        <div style="text-align: left">
          <div>金额：{{ row.creditAmount || 0 }}</div>
          <div>
            起息日：{{ row.valueDate ? row.valueDate.slice(0, 10) : '-' }}
          </div>
        </div>
      </template>
      <template slot="actualMarginAmount" slot-scope="{ row }">
        <div style="text-align: left">
          <div>金额：{{ row.actualMarginAmount || 0 }}</div>
          <div>
            日期：{{
              row.receiveMarginDate ? row.receiveMarginDate.slice(0, 10) : '-'
            }}
          </div>
        </div>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getOccupationCapitalStatisticsCount,
    getOccupationCapitalStatisticsList,
    occupationCapitalStatisticsExport
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        searchProps: {
          ownMonth: '',
          buId: '',
          customerId: '',
          poNo: '',
          creditOrderCode: ''
        },
        tableColumn: [
          {
            label: '权责月份',
            prop: 'ownMonth',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            slotTo: 'buName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '销售赊销单号',
            prop: 'creditOrderCode',
            minWidth: '140',
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
            label: '客户',
            prop: 'customerName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '币种',
            prop: 'currency',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '支付金额',
            slotTo: 'creditAmount',
            width: '160',
            align: 'center',
            hide: true
          },
          {
            label: '卓普信放款日期',
            prop: 'sapienceLoanDate',
            width: '150',
            align: 'center',
            hide: true,
            formatter: (row) =>
              row.sapienceLoanDate ? row.sapienceLoanDate.slice(0, 10) : '-'
          },
          {
            label: '已收保证金',
            slotTo: 'actualMarginAmount',
            width: '160',
            align: 'center',
            hide: true
          },
          {
            label: '到期日期',
            prop: 'expireDate',
            width: '150',
            align: 'center',
            hide: true,
            formatter: (row) =>
              row.expireDate ? row.expireDate.slice(0, 10) : '-'
          },
          {
            label: '垫付金额',
            prop: 'advancedAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '回款本金',
            prop: 'principalAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '回款日期',
            prop: 'receiveDate',
            width: '150',
            align: 'center',
            hide: true,
            formatter: (row) =>
              row.receiveDate ? row.receiveDate.slice(0, 10) : ''
          },
          {
            label: '垫付余款',
            prop: 'advancedRestAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '利息',
            prop: 'interest',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '逾期天数',
            prop: 'overdueDays',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '逾期费用',
            prop: 'overdueAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '资金占用天数',
            prop: 'occupationDays',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '资金占用费率',
            prop: 'occupationRate',
            width: '100',
            align: 'center',
            hide: true,
            formatter: (row) =>
              row.occupationRate ? row.occupationRate + '%' : '-'
          },
          {
            label: '资金占用费',
            prop: 'occupationAmount',
            width: '100',
            align: 'center',
            hide: true,
            formatter: (row) => row.occupationAmount || '0'
          },
          {
            label: '毛利',
            prop: 'grossProfit',
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
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await getOccupationCapitalStatisticsList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        try {
          const { data: count } = await getOccupationCapitalStatisticsCount({
            ...this.searchProps
          })
          if (count > 10000) {
            this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
            return false
          }
          this.$exportFile(occupationCapitalStatisticsExport, {
            ...this.searchProps
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
