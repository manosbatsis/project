<template>
  <!-- 月结快报 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetSearchForm"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="月结月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                :clearable="false"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                type="month"
                placeholder="选择日期时间"
              ></el-date-picker>
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
                clearable
                filterable
                :data-list="getCustomerSelectBean('customerList')"
              >
                <el-option
                  v-for="item in selectOpt.customerList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
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
                :data-list="getBUSelectBean('buIdList')"
              >
                <el-option
                  v-for="item in selectOpt.buIdList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="账单类型："
              prop="billType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.billType"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('billMonthlySnapshot_billTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.billMonthlySnapshot_billTypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="应收账单号："
              prop="receiveCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.receiveCode" clearable />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'receiveBillVerification_flash'"
          @click="refresh"
        >
          刷新
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBillVerification_export'"
          @click="exportExcel"
        >
          导出Excel
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listBillMonthlySnapshot,
    refreshMonthlySnapshot,
    exportMonthlySnapshotUrl
  } from '@a/receiveMoneyManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          customerId: '',
          buId: '',
          billType: '',
          receiveCode: '',
          month: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '月结月份',
            prop: 'month',
            align: 'center',
            hide: true,
            minWidth: 110
          },
          {
            label: '应收账单号',
            prop: 'receiveCode',
            align: 'center',
            hide: true,
            minWidth: 150
          },
          {
            label: '账单类型',
            prop: 'billTypeLabel',
            align: 'center',
            hide: true,
            minWidth: 80
          },
          {
            label: '事业部',
            prop: 'buName',
            align: 'center',
            minWidth: 200
          },
          {
            label: '入账月份',
            prop: 'creditMonth',
            minWidth: 100,
            align: 'center',
            hide: true,
            formatter(row, column, cellValue, index) {
              return cellValue ? cellValue.slice(0, 7) : ''
            }
          },
          {
            label: '客户',
            prop: 'customerName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '平台',
            prop: 'storePlatformName',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '应收金额',
            prop: 'receivableAmount',
            minWidth: '100',
            align: 'center'
          },
          {
            label: '未核金额',
            prop: 'nonverifyAmount',
            align: 'center',
            minWidth: '100',
            hide: true
          },
          {
            label: '结算币种',
            prop: 'currencyLabel',
            minWidth: '110',
            align: 'center',
            hide: true
          },
          {
            label: '开票日期',
            prop: 'invoiceDate',
            minWidth: 110,
            align: 'center',
            formatter(row, column, cellValue, index) {
              return cellValue ? cellValue.slice(0, 10) : ''
            },
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
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listBillMonthlySnapshot({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list
          this.tableData.total = total
          return Promise.resolve()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      },
      // 刷新
      async refresh() {
        if (!this.searchProps.month) {
          return this.$warningMsg('请先选择月结月份')
        }
        await refreshMonthlySnapshot({ month: this.searchProps.month })
        this.$successMsg('刷新成功')
        this.getList()
      },
      // 导出excel
      exportExcel() {
        if (!this.searchProps.month) {
          return this.$warningMsg('请先选择月结月份')
        }
        const exportJson = { ...this.searchProps }
        this.$exportFile(exportMonthlySnapshotUrl, exportJson)
      },
      // 备注
      remark(row) {
        this.remarkBillVerification.visible.show = true
        this.remarkBillVerification.data = row
      },
      remarkAfter() {
        this.remarkBillVerification.visible.show = false
        this.remarkBillVerification.data = null
      },
      // 详情
      detail(row) {
        this.linkTo(`/receiveBill/detail?id=${row.receiveId}`)
      }
    }
  }
</script>
