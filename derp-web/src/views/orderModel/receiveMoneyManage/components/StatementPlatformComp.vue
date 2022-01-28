<template>
  <!-- 平台结算单页面 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetSearchForm"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="账单月份："
            prop="month"
            class="search-panel-item fs-14 clr-II"
          >
            <el-date-picker
              v-model="searchProps.month"
              type="month"
              placeholder="请选择"
              style="width: 203px"
              value-format="yyyy-MM"
            />
          </el-form-item>
        </el-col>
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="平台结算单号："
              prop="billCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.billCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="客户："
              prop="customerType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerType"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('platformStatement_customerTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.platformStatement_customerTypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="是否已开票："
              prop="isInvoice"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.isInvoice"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('platformStatement_isInvoiceList')"
              >
                <el-option
                  v-for="item in selectOpt.platformStatement_isInvoiceList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="应收结算单号："
              prop="receiveCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.receiveCode"
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
          v-permission="'platformStatement_invoice'"
          @click="showOpenTemplate"
          >开票</el-button
        >
        <el-button
          type="primary"
          v-permission="'platformStatement_export'"
          @click="exportList"
          >导出</el-button
        >
        <el-button
          type="primary"
          v-permission="'platformStatement_flash'"
          @click="flash"
          >刷新</el-button
        >
        <el-button
          type="primary"
          v-permission="'platformStatement_generateBill'"
          @click="generateToc"
          >生成应收单</el-button
        >
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      show-selection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="invoiceNo" slot-scope="{ row }">
        <el-button
          @click="viewInvoice(row.invoiceNo)"
          type="text"
          v-if="row.invoiceNo"
        >
          {{ row.invoiceNo }}
        </el-button>
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          @click="linkTo(`/receivemoney/platformdetail/${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          @click="
            linkTo(
              `/common/enclosureManage?code=${row.billCode}${
                row.customerType != 3 ? '' : '_' + row.currency
              }`
            )
          "
        >
          附件管理
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
    <!-- 选择发票模板组件 -->
    <InvoiceTemplate
      :isVisible="visible"
      v-if="visible.show"
      :data="invoiceTemplateData"
      @comfirm="openTemplateCb"
    />
    <!-- 选择发票模板组件 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listPlatformStatementOrder,
    exportOrdersUrl,
    flashPlatformStatementOrders,
    saveToCReceiveBill,
    viewInvoiceUrl
  } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    components: {
      InvoiceTemplate: () => import('./InvoiceTemplate')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          billCode: '',
          customerType: '',
          isInvoice: '',
          month: '',
          receiveCode: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '平台结算单号',
            prop: 'billCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '账单月份',
            prop: 'month',
            minWidth: '100',
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
            label: '账单金额',
            prop: 'billAmount',
            minWidth: '120',
            align: 'center',
            hide: true,
            formatter: (row) =>
              (row.currency || '') + ' ' + (row.billAmount || '0')
          },
          {
            label: '是否已开票',
            prop: 'isInvoiceLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '发票号码',
            slotTo: 'invoiceNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '开票人',
            prop: 'invoiceDrawer',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '开票日期',
            prop: 'invoiceDate',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '是否创建应收',
            prop: 'isCreateReceiveLabel',
            width: '70',
            align: 'center',
            hide: true
          },
          {
            label: '应收结算单号',
            prop: 'receiveCode',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '120',
            align: 'left',
            fixed: 'right'
          }
        ],
        invoiceTemplateData: []
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
          const { data } = await listPlatformStatementOrder({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (err) {
          console.log(err)
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
      // 显示开票
      showOpenTemplate() {
        const list = this.tableChoseList
        if (list.length !== 1) {
          return this.$warningMsg(
            list.length > 1 ? '只能选择一条记录' : '至少选择一条记录'
          )
        }
        if (list[0].isInvoice === '1') {
          return this.$warningMsg('仅对标识为“未开票”的平台结算单进行开票！')
        }
        this.invoiceTemplateData = list
        this.visible.show = true
      },
      // 开票回调
      openTemplateCb() {
        this.visible.show = false
        this.invoiceTemplateData = []
      },
      //  导出
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        let opt = {
          ...this.searchProps
        }
        if (this.tableChoseList.length) {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          opt = { ids }
        } else if (!this.searchProps.month) {
          return this.$errorMsg('请选择账单月份')
        }
        await exportOrdersUrl(opt)
        this.$alertSuccess('请在任务列表查看进度')
      },
      // 刷新
      flash() {
        const { billCode, month, customerType } = this.searchProps
        if (!customerType) {
          return this.$errorMsg('请选择客户')
        }
        if (!month) {
          return this.$errorMsg('请选择账单月份')
        }
        this.$showToast('确定刷新该月份？', async () => {
          try {
            await flashPlatformStatementOrders({
              billCode,
              month,
              customerType
            })
            this.$successMsg('正在刷新，稍后点击【查询】，查询结果')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 创建应收
      async generateToc() {
        const list = this.tableChoseList
        if (list.length === 0) {
          return this.$errorMsg('至少选择一条记录')
        }
        if (list.every((item) => String(item.isCreateReceive) !== '0')) {
          return this.$errorMsg('勾选的记录必须为“未创建应收”！')
        }
        const isRepeat = function (attr) {
          const attrs = list.map((item) => item[attr])
          return [...new Set(attrs)].length > 1
        }
        if (isRepeat('customerType')) {
          return this.$errorMsg('勾选的记录必须为相同的平台！')
        }
        if (isRepeat('shopCode')) {
          return this.$errorMsg('勾选的记录必须为相同的店铺！')
        }
        if (isRepeat('customerId')) {
          return this.$errorMsg('勾选的记录必须为相同的客户！')
        }
        if (isRepeat('buId')) {
          return this.$errorMsg('勾选的记录必须为相同的事业部！')
        }
        if (isRepeat('currency')) {
          return this.$errorMsg('勾选的记录必须为相同的币种！')
        }
        if (isRepeat('month')) {
          return this.$errorMsg('勾选的记录必须为相同的月份！')
        }
        await saveToCReceiveBill({
          ids: list.map((item) => item.id).toString()
        })
        this.$successMsg('正在生成中')
        this.getList()
      },
      // 查看发票信息
      viewInvoice(invoiceNo) {
        this.$exportFile(viewInvoiceUrl + invoiceNo)
      }
    }
  }
</script>
