<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetForm" @search="getList(1)">
      <JFX-Form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.buId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getBUSelectBean('buList', {
                    merchantId: getUserInfo().merchantId
                  })
                "
              >
                <el-option
                  v-for="item in selectOpt.buList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
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
                v-model="ruleForm.customerId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getCustomerByMerchantId(
                    'kuhuList',
                    {
                      cusType: 1,
                      merchantId: getUserInfo().merchantId,
                      begin: 0,
                      pageSize: 10000
                    },
                    { key: 'id', value: 'name' }
                  )
                "
              >
                <el-option
                  v-for="item in selectOpt.kuhuList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="销售币种："
              prop="currency"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.currency"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getCurrencySelectBean('currencyList')"
              >
                <el-option
                  v-for="item in selectOpt.currencyList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="渠道类型："
              prop="channelType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.channelType"
                placeholder="请选择"
                clearable
                :data-list="getSelectList('receiveAging_channelTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.receiveAging_channelTypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
              <el-form-item label="报表月份：" prop="effectiveMonth">
                <el-date-picker
                  v-model="ruleForm.effectiveMonth"
                  value-format="yyyy-MM"
                  type="month"
                  placeholder="选择月份"
                  clearable
                />
              </el-form-item>
            </el-col> -->
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          @click="exportExcel"
          v-permission="'receivableAccountList_export'"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          @click="refresh"
          v-permission="'receivableAccountList_refresh'"
        >
          刷新
        </el-button>
        <el-button
          type="primary"
          @click="exportMonthlyReport"
          v-permission="'receivableAccountList_exportMonthlyReport'"
        >
          月结报告导出
        </el-button>
        <span class="clr-r fs-14" style="margin-left: 10px">
          报表日期：{{ reshDate }}
        </span>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="receiveCostOriginalAmount" slot-scope="{ row }">
        {{ row.rmbCurrency + ' ' + (row.receiveCostOriginalAmount || 0) }}
      </template>
      <template slot="effectiveDate" slot-scope="{ row }">
        {{ row.effectiveDate ? row.effectiveDate.substring(0, 10) : '' }}
      </template>
      <template slot="rmbAmount" slot-scope="{ row }">
        {{ row.rmbCurrency + ' ' + (row.rmbAmount || 0) }}
      </template>
      <template slot="costRmbAmount" slot-scope="{ row }">
        {{ row.rmbCurrency + ' ' + (row.costRmbAmount || 0) }}
      </template>
      <template slot="receiveIncomeOriginalAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.receiveIncomeOriginalAmount || 0) }}
      </template>
      <template slot="originalAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.originalAmount || 0) }}
      </template>
      <template slot="costOriginalAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.costOriginalAmount || 0) }}
      </template>
      <template slot="thirtyAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.thirtyAmount || 0) }}
      </template>
      <template slot="sixtyAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.sixtyAmount || 0) }}
      </template>
      <template slot="ninetyAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.ninetyAmount || 0) }}
      </template>
      <template slot="onetwentyAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.onetwentyAmount || 0) }}
      </template>
      <template slot="twentyAboveAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.twentyAboveAmount || 0) }}
      </template>
      <template slot="accountDay" slot-scope="{ row }">
        {{ row.accountDay + '天' }}
      </template>
      <template slot="accountAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.accountAmount || 0) }}
      </template>
      <template slot="overdueAmount" slot-scope="{ row }">
        {{ row.currency + ' ' + (row.overdueAmount || 0) }}
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'receivableAccountList_detail'"
          @click="linkTo(`/receivemoney/receivableAccountDetail?id=${row.id}`)"
        >
          详情
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 月结报告导出start -->
    <receiveAgingReportExportMonthlyReport
      v-if="reportVisible.show"
      :reportVisible="reportVisible"
      @close="reportVisible.show = false"
    />
    <!-- 月结报告导出end -->
  </div>
</template>
<script>
  import {
    listReceiveAgingReport,
    refreshReceiveAgingReport,
    exportReceiveAgingReportUrl,
    getMaxRefreshDate
  } from '@a/receiveMoneyManage/index'
  import commomMix from '@m/index'
  import receiveAgingReportExportMonthlyReport from './components/receiveAgingReportExportMonthlyReport'
  export default {
    mixins: [commomMix],
    components: {
      receiveAgingReportExportMonthlyReport
    },
    data() {
      return {
        ruleForm: {
          buId: '',
          customerId: '',
          currency: '',
          channelType: ''
        },
        visible: { show: false },
        reshDate: '',
        // 表格列数据
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '渠道类型',
            prop: 'channelTypeLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '客户名称',
            prop: 'customerName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '客户简称',
            prop: 'simpleName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '应收账面总余额 \n（本币）',
            slotTo: 'receiveCostOriginalAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '应收收入 \n（本币）',
            slotTo: 'rmbAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '应收费用 \n（本币）',
            slotTo: 'costRmbAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '折算汇率',
            prop: 'rate',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '汇率日期',
            slotTo: 'effectiveDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '应收账面总余额 \n（原币）',
            slotTo: 'receiveIncomeOriginalAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '应收收入 \n（原币）',
            slotTo: 'originalAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '应收费用 \n（原币）',
            slotTo: 'costOriginalAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '0~30天',
            slotTo: 'thirtyAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '30~60天',
            slotTo: 'sixtyAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '60~90天',
            slotTo: 'ninetyAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '90~120天',
            slotTo: 'onetwentyAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '120天以上',
            slotTo: 'twentyAboveAmount',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '正常账期天数',
            slotTo: 'accountDay',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '账期内金额',
            slotTo: 'accountAmount',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '逾期金额',
            slotTo: 'overdueAmount',
            width: '140',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '100', align: 'center' }
        ],
        reportVisible: {
          show: false
        }
      }
    },
    mounted() {
      this.getList(1)
    },
    activated() {
      this.getList()
    },
    methods: {
      async getList(pageNum) {
        try {
          this.tableData.loading = true
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          // 同步方法
          const opt = {
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm
          }
          const res = await listReceiveAgingReport(opt)
          this.tableData.list = res.data.list
          this.tableData.total = res.data.total
          this.getMaxRefreshDate()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 获取报表日期
      async getMaxRefreshDate() {
        try {
          const { data, message } = await getMaxRefreshDate()
          this.reshDate = data || message || ''
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      async exportExcel() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        let opt = {
          begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
          pageSize: this.tableData.pageSize || 10,
          ...this.ruleForm
        }
        if (this.tableChoseList.length > 0) {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          opt = { ids }
        }
        this.$exportFile(exportReceiveAgingReportUrl, opt)
      },
      // 刷新
      async refresh() {
        this.tableData.loading = true
        try {
          await refreshReceiveAgingReport({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm
          })
          this.$successMsg('数据正在更新中,请稍后刷新列表', () => {
            this.getList()
          })
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 月结报告导出
      exportMonthlyReport() {
        this.reportVisible.show = true
      },
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
