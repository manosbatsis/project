<template>
  <!--  -->
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
              label="应收账单号："
              prop="receiveCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.receiveCode" clearable />
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
                :data-list="
                  getSelectList('receiveBillVerification_billTypeList')
                "
              >
                <el-option
                  v-for="item in selectOpt.receiveBillVerification_billTypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="入账月份："
              prop="creditMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.creditMonth"
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
              label="核销状态："
              prop="billStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.billStatus"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getSelectList('receiveBillVerification_billStatusList')
                "
              >
                <el-option
                  v-for="item in selectOpt.receiveBillVerification_billStatusList"
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
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'listBillMonthlySnapshot_flash'"
          @click="refresh"
        >
          刷新
        </el-button>
        <el-button
          type="primary"
          v-permission="'listBillMonthlySnapshot_export'"
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
      <template slot="action" slot-scope="{ row }">
        <el-button type="text" @click="remark(row)">备注</el-button>
        <el-button type="text" @click="detail(row)">详情</el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 备注 -->
    <remarkBillVerification
      v-if="remarkBillVerification.visible.show"
      :remarkBillVerificationVisible="remarkBillVerification.visible"
      :data="remarkBillVerification.data"
      @comfirm="remarkAfter"
    ></remarkBillVerification>
    <!-- 备注 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listReceiveBillVerification,
    receiveBillVerificationFlash,
    receiveBillVerificationExportUrl
  } from '@a/receiveMoneyManage/index'
  export default {
    mixins: [commomMix],
    components: {
      // 选择单据
      remarkBillVerification: () =>
        import('./components/remarkBillVerification')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          customerId: '',
          buId: '',
          billType: '',
          receiveCode: '',
          creditMonth: '',
          billStatus: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '应收账单号',
            prop: 'receiveCode',
            align: 'center',
            hide: true,
            width: 150
          },
          {
            label: '账单类型',
            prop: 'billTypeLabel',
            align: 'center',
            hide: true,
            width: 80
          },
          {
            label: '事业部',
            prop: 'buName',
            align: 'center',
            width: 200
          },
          {
            label: '发票编号',
            prop: 'invoiceNo',
            width: 200,
            align: 'center',
            hide: true
          },
          {
            label: '是否开发票',
            prop: 'invoiceStatusLabel',
            width: '100',
            align: 'center'
          },
          {
            label: '账单月份',
            prop: 'billDate',
            width: '100',
            align: 'center',
            formatter(row, column, cellValue, index) {
              return cellValue ? cellValue.slice(0, 10) : ''
            },
            hide: true
          },
          {
            label: '客户',
            prop: 'customerName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '应收金额',
            prop: 'receivePrice',
            width: '100',
            align: 'center'
          },
          {
            label: '未核金额',
            prop: 'uncollectedPrice',
            align: 'center',
            width: '100',
            hide: true
          },
          {
            label: '结算币种',
            prop: 'currencyLabel',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '核销状态',
            prop: 'billStatusLabel',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '账单日期',
            prop: 'billDate',
            width: 110,
            align: 'center',
            formatter(row, column, cellValue, index) {
              return cellValue ? cellValue.slice(0, 10) : ''
            },
            hide: true
          },
          {
            label: '开票日期',
            prop: 'invoiceDate',
            width: 110,
            align: 'center',
            formatter(row, column, cellValue, index) {
              return cellValue ? cellValue.slice(0, 10) : ''
            },
            hide: true
          },
          {
            label: '客户账期',
            prop: 'accountPeriodLabel',
            width: 110,
            align: 'center',
            hide: true
          },
          {
            label: '账期逾期天数',
            prop: 'accountOverdueDays',
            width: 110,
            align: 'center',
            hide: true
          },
          {
            label: '创建时间',
            prop: 'createDate',
            width: 200,
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: 110,
            align: 'center',
            hide: true,
            fixed: 'right'
          }
        ],
        //   备注
        remarkBillVerification: {
          visible: { show: false },
          data: null
        }
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
        const { code } = this.$route.params
        if (code) {
          this.searchProps.receiveCode = code
          delete this.$route.params.code
        }
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listReceiveBillVerification({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list.map((item) => {
            return item
          })
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
        const chooseLst = this.tableChoseList
        if (chooseLst.length === 0) {
          return this.$alertWarning('至少选择一条记录！')
        }
        const receiveCodes = chooseLst
          .map((item) => item.receiveCode)
          .toString()
        await receiveBillVerificationFlash({ receiveCodes })
        this.$successMsg('刷新成功')
        this.getList()
      },
      // 导出excel
      exportExcel() {
        const exportJson = { ...this.searchProps }
        if (!exportJson.billStatus) {
          return this.$errorMsg('请选择核销状态')
        }
        this.$exportFile(receiveBillVerificationExportUrl, exportJson)
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
