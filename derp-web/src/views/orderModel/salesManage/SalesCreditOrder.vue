<template>
  <!-- 销售赊销单页面 -->
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
              label="销售赊销单号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.code"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="销售订单号："
              prop="saleOrderCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.saleOrderCode"
                placeholder="请输入"
                clearable
              />
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
                :data-list="getCustomerSelectBean('customer_list')"
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
              label="PO号："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.poNo"
                placeholder="请输入"
                clearable
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          @click="deleteTableItem"
          v-permission="'sales_credit_del'"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          @click="exportList"
          v-permission="'sales_credit_export'"
        >
          导出结算单
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20 mr-b-10">
      <el-radio-group
        v-model="searchProps.status"
        :data-list="getSelectList('saleCredit_statusList')"
        @change="getList(1)"
      >
        <el-radio-button
          v-for="item in selectOpt.saleCredit_statusList"
          :key="item.key"
          :label="item.key"
        >
          {{ item.value }}({{ tabData[item.key] || 0 }})
        </el-radio-button>
        <el-radio-button label="">全部({{ this.tabTotal }})</el-radio-button>
      </el-radio-group>
    </div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="code" slot-scope="{ row }">
        <span>{{ row.code }}</span>
        <br />
        <span>{{ row.statusLabel }}</span>
        <br />
        <span v-if="row.stayDays && +row.stayDays < 0">
          剩 {{ (row.stayDays + '').slice(1) }} 天到期
        </span>
        <span v-if="row.stayDays && +row.stayDays === 0" style="color: #0000ff">
          今天到期
        </span>
        <span v-if="row.stayDays && +row.stayDays > 0" style="color: #ff0000">
          超期 {{ row.stayDays }} 天
        </span>
      </template>
      <template slot="buName" slot-scope="{ row }">
        {{ row.buName }}
        <br />
        {{ row.merchantName }}
      </template>
      <template slot="saleOrderCode" slot-scope="{ row }">
        销售单:{{ row.saleOrderCode }}
        <br />
        PO号:{{ row.poNo }}
      </template>
      <template slot="num" slot-scope="{ row }">
        {{ row.totalNum }}
        <br />
        {{ row.currency }} {{ row.creditAmount }}
      </template>
      <template slot="payableMarginAmount" slot-scope="{ row }">
        应收：{{
          row.payableMarginAmount !== 0 && !row.payableMarginAmount
            ? '-'
            : row.currency + ' ' + row.payableMarginAmount
        }}
        <br />
        实收：{{
          row.actualMarginAmount !== 0 && !row.actualMarginAmount
            ? '-'
            : row.currency + ' ' + row.actualMarginAmount
        }}
        <br />
        日期：{{
          row.receiveMarginDate ? row.receiveMarginDate.slice(0, 10) : '-'
        }}
      </template>
      <template slot="createName" slot-scope="{ row }">
        {{ row.createName }}
        <br />
        {{ row.createDate }}
      </template>
      <template slot="loanDate" slot-scope="{ row }">
        放款日期：{{ row.loanDate ? row.loanDate.slice(0, 10) : '-' }}
        <br />
        起息日期：{{ row.valueDate ? row.valueDate.slice(0, 10) : '-' }}
        <br />
        到期日期：{{ row.expireDate ? row.expireDate.slice(0, 10) : '-' }}
      </template>
      <template slot="principal" slot-scope="{ row }">
        本金：{{
          row.receivePrincipalAmount !== 0 && !row.receivePrincipalAmount
            ? '-'
            : row.currency + ' ' + row.receivePrincipalAmount
        }}
        <br />
        利息：{{
          row.receiveInterestAmount !== 0 && !row.receiveInterestAmount
            ? '-'
            : row.currency + ' ' + row.receiveInterestAmount
        }}
        <br />
        日期：{{ row.receiveDate ? row.receiveDate.slice(0, 10) : '-' }}
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          style="padding-left: 5px"
          v-permission="'sales_credit_detail'"
          @click="linkTo(`/sales/SalesCreditDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-permission="'sales_credit_sumbit'"
          v-if="row.status === '001'"
          @click="handleSubmit(row.id)"
        >
          提交
        </el-button>
        <el-button
          type="text"
          v-permission="'sales_credit_depositReceived'"
          v-if="row.status === '002'"
          @click="showModal('depositReceived', row)"
        >
          收到保证金
        </el-button>
        <el-button
          type="text"
          v-if="row.status === '003'"
          v-permission="'sales_credit_confirmTheLoan'"
          @click="showModal('confirmTheLoan', row)"
        >
          确认放款
        </el-button>
        <el-button
          type="text"
          v-if="row.status === '004'"
          v-permission="'sales_credit_creditSalesCollection'"
          @click="showModal('creditSalesCollection', row)"
        >
          申请收款
        </el-button>
        <el-button
          type="text"
          v-if="row.status === '005'"
          v-permission="'sales_credit_repaymentReceived'"
          @click="showModal('repaymentReceived', row)"
        >
          收到还款
        </el-button>
        <el-button
          type="text"
          v-permission="'sales_credit_logList'"
          @click="showModal('logList', row.code)"
        >
          日志
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 日志 -->
    <LogList
      v-if="logList.visible.show"
      :visible.sync="logList.visible"
      :filterData="logList.filterData"
    ></LogList>
    <!-- 日志 end -->
    <!-- 收到保证金 -->
    <DepositReceived
      v-if="depositReceived.visible.show"
      :isVisible="depositReceived.visible"
      :filterData="depositReceived.filterData"
      @comfirm="closeModal('depositReceived')"
      @cancel="depositReceived.visible.show = false"
    ></DepositReceived>
    <!-- 收到保证金 end -->
    <!-- 收到保证金 -->
    <ConfirmTheLoan
      v-if="confirmTheLoan.visible.show"
      :isVisible="confirmTheLoan.visible"
      :filterData="confirmTheLoan.filterData"
      @comfirm="closeModal('confirmTheLoan')"
      @cancel="confirmTheLoan.visible.show = false"
    ></ConfirmTheLoan>
    <!-- 收到保证金 end -->
    <!-- 收到还款 -->
    <RepaymentReceived
      v-if="repaymentReceived.visible.show"
      :isVisible="repaymentReceived.visible"
      :filterData="repaymentReceived.filterData"
      @comfirm="closeModal('repaymentReceived')"
      @cancel="repaymentReceived.visible.show = false"
    ></RepaymentReceived>
    <!-- 收到还款 end -->
    <!-- 赊销收款 -->
    <CreditSalesCollection
      v-if="creditSalesCollection.visible.show"
      :isVisible="creditSalesCollection.visible"
      :filterData="creditSalesCollection.filterData"
      @comfirm="closeModal('creditSalesCollection')"
      @cancel="creditSalesCollection.visible.show = false"
    ></CreditSalesCollection>
    <!-- 赊销收款 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getCreditRepayList,
    getCreditTypeNum,
    deleteCreditOrder,
    exportCreditOrderUrl,
    submitCreditCreditOrder
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      // 日志
      LogList: () => import('@c/logList/index.vue'),
      // 收到保证金
      DepositReceived: () => import('./components/DepositReceived'),
      // 确认放款
      ConfirmTheLoan: () => import('./components/ConfirmTheLoan'),
      // 收到还款
      RepaymentReceived: () => import('./components/RepaymentReceived'),
      // 赊销收款
      CreditSalesCollection: () => import('./components/CreditSalesCollection')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          code: '',
          saleOrderCode: '',
          customerId: '',
          poNo: '',
          buId: '',
          status: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '赊销单号',
            slotTo: 'code',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            slotTo: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '关联单据',
            slotTo: 'saleOrderCode',
            minWidth: '150',
            align: 'left',
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
            label: '数量/金额',
            slotTo: 'num',
            minWidth: '140',
            align: 'left',
            hide: true
          },
          {
            label: '保证金',
            slotTo: 'payableMarginAmount',
            minWidth: '140',
            align: 'left',
            hide: true
          },
          {
            label: '创建人',
            slotTo: 'createName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '起息日/到期日',
            slotTo: 'loanDate',
            minWidth: '140',
            align: 'left',
            hide: true
          },
          {
            label: '收款信息',
            slotTo: 'principal',
            minWidth: '140',
            align: 'left',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            minWidth: '100',
            align: 'left',
            fixed: 'right'
          }
        ],
        // 日志组件
        logList: {
          filterData: {},
          visible: { show: false }
        },
        // 收到保证金
        depositReceived: {
          filterData: {},
          visible: { show: false }
        },
        // 确认放款
        confirmTheLoan: {
          filterData: {},
          visible: { show: false }
        },
        // 赊销收款
        creditSalesCollection: {
          filterData: {},
          visible: { show: false }
        },
        // 收到还款
        repaymentReceived: {
          filterData: {},
          visible: { show: false }
        },
        // 标签页数据
        tabData: {},
        // 标签页数量
        tabTotal: 0
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
        // 订单日期
        this.searchProps.createDateStartDate =
          this.date && this.date.length ? this.date[0] : ''
        this.searchProps.createDateEndDate =
          this.date && this.date.length ? this.date[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await getCreditRepayList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
          this.getDeclareTypeNum()
        } catch (err) {}
        this.tableData.loading = false
      },
      // 显示日志弹窗
      showModal(type, payload) {
        switch (type) {
          case 'logList':
            this.logList.filterData = { relCode: payload, module: 10 }
            this.logList.visible.show = true
            break
          case 'depositReceived':
            this.depositReceived.filterData = payload
            this.depositReceived.visible.show = true
            break
          case 'confirmTheLoan':
            this.confirmTheLoan.filterData = payload
            this.confirmTheLoan.visible.show = true
            break
          case 'creditSalesCollection':
            this.creditSalesCollection.filterData = payload
            this.creditSalesCollection.visible.show = true
            break
          case 'repaymentReceived':
            this.repaymentReceived.filterData = payload
            this.repaymentReceived.visible.show = true
            break
        }
      },
      // 关闭弹窗
      closeModal(type) {
        switch (type) {
          case 'depositReceived':
            this.depositReceived.filterData = {}
            this.depositReceived.visible.show = false
            break
          case 'confirmTheLoan':
            this.confirmTheLoan.filterData = {}
            this.confirmTheLoan.visible.show = false
            break
          case 'creditSalesCollection':
            this.creditSalesCollection.filterData = {}
            this.creditSalesCollection.visible.show = false
            break
          case 'repaymentReceived':
            this.repaymentReceived.filterData = {}
            this.repaymentReceived.visible.show = false
            break
        }
        this.getList()
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        if (!this.tableChoseList.length) {
          this.$errorMsg('至少勾选一条数据！')
          return false
        }
        if (this.tableChoseList.length > 1) {
          this.$errorMsg('只能选择一条数据！')
          return false
        }
        try {
          if (this.tableChoseList.length) {
            this.$showToast('确定导出勾选数据？', async () => {
              const id = this.tableChoseList[0].id
              // const ids = this.tableChoseList.map(item => item.id).toString()
              this.$exportFile(exportCreditOrderUrl, { id })
            })
          } else {
            this.$exportFile(exportCreditOrderUrl, { ...this.searchProps })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 删除表格项
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await deleteCreditOrder({ ids })
            this.$successMsg('删除成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 提交
      handleSubmit(id) {
        try {
          this.$showToast('确定提交数据？', async () => {
            await submitCreditCreditOrder({ id })
            this.$successMsg('提交成功')
            this.getList()
          })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 获取标签页数量
      async getDeclareTypeNum() {
        const { data } = await getCreditTypeNum({
          ...this.searchProps,
          status: ''
        })
        const helper = {}
        if (data && data.length) {
          data.forEach(({ status, num, total }) => {
            if (status) helper[status] = num
            if (total !== null && total !== undefined) this.tabTotal = total
          })
        }
        this.tabData = helper
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.date = []
          this.getList(1)
        })
      }
    }
  }
</script>
