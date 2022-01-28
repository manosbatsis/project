<template>
  <!-- 客户列表页面 -->
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
              label="入账月份："
              prop="billMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.billMonth"
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
              label="开票状态："
              prop="invoiceStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.invoiceStatus"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('receiveBill_invoiceStatusList')"
              >
                <el-option
                  v-for="item in selectOpt.receiveBill_invoiceStatusList"
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
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.code" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="来源业务单："
              prop="relCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.relCode" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="发票号码："
              prop="invoiceNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.invoiceNo" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="账单状态："
              prop="billStates"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.billStates"
                placeholder="请选择"
                clearable
                filterable
                multiple
                :data-list="getSelectList('receiveBill_billStatusList')"
              >
                <el-option
                  v-for="item in selectOpt.receiveBill_billStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="NC应收状态："
              prop="ncStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.ncStatus"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('receiveBill_nvSynList')"
              >
                <el-option
                  v-for="item in selectOpt.receiveBill_nvSynList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="发票月份："
              prop="invoiceMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.invoiceMonth"
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
              label="创建人："
              prop="creater"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.creater" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="会计期间："
              prop="period"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.period"
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
              label="po："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.poNo" clearable />
            </el-form-item>
          </el-col>
          <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="币种：" prop="currency">
              <el-select
                v-model="searchProps.currency"
                style="width: 100%"
                placeholder="请选择"
                filterable
                clearable
                :list-data="getCurrencySelectBean('currencyList')"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'receiveBill_refresh'"
          @click="refresh"
        >
          刷新
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBill_add'"
          @click="showModal('add')"
        >
          新增
        </el-button>
        <el-button type="primary" v-permission="'receiveBill_del'" @click="del">
          删除
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBill_open'"
          @click="open"
        >
          开票
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBill_invalidApply'"
          @click="showModal('invalidApply')"
        >
          申请作废
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBill_exportBill'"
          @click="exportPDF"
        >
          导出PDF
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBill_exportExcelBill'"
          @click="exportExcel"
        >
          导出Excel
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBill_updateVoucher'"
          @click="update"
        >
          更新凭证信息
        </el-button>
        <el-button
          type="primary"
          v-permission="'receiveBill_addBill'"
          @click="linkTo('/receiveBill/add')"
        >
          创建应收
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
      <template slot="code" slot-scope="{ row }">
        {{ row.code }}
        <br />
        {{ row.buName }}
      </template>
      <template slot="relCode" slot-scope="{ row }">
        <JFX-more-btn
          v-if="row.relCode"
          :isShowBtn="row.relCode && row.relCode.split('&').length > 1"
        >
          <template slot="more">
            关联单号：{{ row.relCode.split('&').slice(0, 1).join('&') }}
          </template>
          <template slot="hide">关联单号：{{ row.relCode }}</template>
        </JFX-more-btn>
        <JFX-more-btn
          v-if="row.poNo"
          :isShowBtn="row.poNo && row.poNo.split('&').length > 1"
        >
          <template slot="more">
            PO：{{ row.poNo.split('&').slice(0, 1).join('&') }}
          </template>
          <template slot="hide">PO：{{ row.poNo }}</template>
        </JFX-more-btn>
      </template>
      <template slot="receivablePrice" slot-scope="{ row }">
        {{ row.currency }} &nbsp; {{ row.receivablePrice }}
      </template>
      <template slot="invoiceStatusLabel" slot-scope="{ row }">
        <el-button
          @click="viewInvoice(row.invoiceNo)"
          type="text"
          v-if="row.invoiceNo"
          v-permission="'receiveBill_view'"
        >
          {{ row.invoiceNo }}
        </el-button>
        <br v-if="row.invoiceNo" />
        {{ row.invoiceDate ? row.invoiceDate.slice(0, 10) : '' }}
        <br v-if="row.invoiceDate" />
        {{ row.invoiceStatusLabel }}
      </template>
      <template slot="ncStatusLabel" slot-scope="{ row }">
        {{ row.ncCode || '' }}
        <br />
        {{ row.ncStatusLabel }}
      </template>
      <template slot="voucherStatusLabel" slot-scope="{ row }">
        {{ row.voucherName || '' }}
        <br />
        {{ row.voucherStatusLabel }}
        <br />
        {{ row.period || '' }}
      </template>
      <template slot="creater" slot-scope="{ row }">
        {{ row.creater || '' }}
        <br />
        {{ row.createDate.slice(0, 10) }}
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'receiveBill_edit'"
          v-if="row.billStatus === '00'"
          @click="linkTo(`/receiveBill/detail?isEdit=1&id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'receiveBill_audit'"
          v-if="row.billStatus === '01' || row.billStatus === '05'"
          @click="linkTo(`/receiveBill/detail?isAudit=1&id=${row.id}`)"
        >
          审核
        </el-button>
        <el-button
          type="text"
          v-permission="'receiveBill_verify'"
          v-if="row.billStatus === '02' || row.billStatus === '03'"
          @click="showModal('verify', row)"
        >
          核销
        </el-button>
        <el-button
          type="text"
          v-permission="'receiveBillInvoice_submitNC'"
          v-if="
            ['02', '03', '04'].includes(row.billStatus) && row.ncStatus === '10'
          "
          @click="showModal('submitNC', row)"
        >
          同步NC应收
        </el-button>
        <el-button
          type="text"
          v-permission="'receiveBill_detail'"
          @click="linkTo(`/receiveBill/detail?id=${row.id}`)"
        >
          详情
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 新增 选择单据 -->
    <ChooseDocument
      v-if="selectDocument.visible.show"
      :selectDocumentVisible="selectDocument.visible"
      @comfirm="(data) => closeModal(data.type, data.billId)"
    ></ChooseDocument>
    <!-- 新增 选择单据 end -->
    <!-- 新增 选择单据 -->
    <InvalidApply
      v-if="invalidApply.visible.show"
      :invalidApplyVisible="invalidApply.visible"
      :data="invalidApply.data"
      @comfirm="closeModal('invalidApply')"
    ></InvalidApply>
    <!-- 新增 选择单据 end -->
    <!-- 选择模板 start -->
    <ChooseTemplate
      v-if="openTemplate.visible.show"
      :openTemplateVisible="openTemplate.visible"
      :data="openTemplate.data"
      @comfirm="closeModal('openTemplate')"
    ></ChooseTemplate>
    <!-- 选择模板 end -->
    <!-- 核销 start -->
    <Verify
      v-if="verify.visible.show"
      :verifyVisible="verify.visible"
      :data="verify.data"
      @comfirm="closeModal('verify')"
    ></Verify>
    <!-- 核销 end -->
    <!-- 同步NC start -->
    <SubmitNC
      v-if="submitNC.visible.show"
      :submitNCVisible="submitNC.visible"
      :data="submitNC.data"
      @comfirm="closeModal('submitNC')"
    ></SubmitNC>
    <!-- 同步NC end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listReceiveBill,
    refreshReceiveBill,
    delReceiveBill,
    receiveBillvalidateInfo as validateInfo,
    exportPdfUrl,
    exportExcelUrl,
    receiveBillUpdateVoucher,
    viewInvoiceUrl
  } from '@a/receiveMoneyManage/index'
  export default {
    mixins: [commomMix],
    components: {
      // 选择单据
      ChooseDocument: () => import('./components/ChooseDocumentReceiveBill'),
      // 应收作废
      InvalidApply: () => import('./components/invalidApply'),
      // 开票
      ChooseTemplate: () => import('./components/ChooseTemplate'),
      // 核销
      Verify: () => import('./components/verify'),
      // 同步NC
      SubmitNC: () => import('./components/submitNC')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          customerId: '',
          billMonth: '',
          buId: '',
          invoiceStatus: '',
          code: '',
          relCode: '',
          invoiceNo: '',
          billStates: [],
          ncStatus: '',
          invoiceMonth: '',
          creater: '',
          period: '',
          currency: '',
          poNo: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '应收账单号',
            slotTo: 'code',
            align: 'center',
            hide: true,
            width: 150
          },
          {
            label: '客户',
            prop: 'customerName',
            align: 'center',
            width: 200
          },
          {
            label: '关联单号',
            slotTo: 'relCode',
            width: 200,
            align: 'center'
          },
          {
            label: '应收金额',
            slotTo: 'receivablePrice',
            minWidth: '110',
            align: 'center'
          },
          {
            label: '账单状态',
            prop: 'billStatusLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '入账月份',
            prop: 'creditDate',
            width: '120',
            align: 'center',
            formatter(row, column, cellValue, index) {
              return cellValue ? cellValue.slice(0, 10) : ''
            },
            hide: true
          },
          {
            label: '发票信息',
            slotTo: 'invoiceStatusLabel',
            width: '150',
            align: 'center'
          },
          {
            label: 'NC单据',
            slotTo: 'ncStatusLabel',
            align: 'center',
            width: '150',
            hide: true
          },
          {
            label: '凭证信息',
            slotTo: 'voucherStatusLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建人',
            slotTo: 'creater',
            width: 110,
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
        // 选择单据弹框
        selectDocument: {
          visible: { show: false }
        },
        // 申请作废
        invalidApply: {
          visible: { show: false },
          data: null
        },
        // 开票
        openTemplate: {
          visible: { show: false },
          data: null
        },
        // 核销
        verify: {
          visible: { show: false },
          data: null
        },
        // 同步NC
        submitNC: {
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
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const billStates = this.searchProps.billStates.join(',')
          const {
            data: { list, total }
          } = await listReceiveBill({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps,
            billStates
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
      // 打开模态框
      showModal(type, data) {
        const chooseList = this.tableChoseList
        const chooseListLength = chooseList.length
        if (type === 'add') {
          // 新增
          this.selectDocument.visible.show = true
        } else if (type === 'invalidApply') {
          // 申请作废
          if (chooseListLength !== 1) {
            return this.$errorMsg(
              chooseListLength === 0 ? '至少选择一条记录' : '只能选择一条记录'
            )
          }
          if (chooseList.some((item) => item.billStatus !== '02')) {
            return this.$errorMsg('仅对账单状态为“待核销”可操作作废！')
          }
          this.invalidApply.visible.show = true
          this.invalidApply.data = this.tableChoseList[0]
        } else if (type === 'openTemplate') {
          // 发票
          this.openTemplate.visible.show = true
          this.openTemplate.data = data
        } else if (type === 'verify') {
          // 核销
          this.verify.visible.show = true
          this.verify.data = data
        } else if (type === 'submitNC') {
          // 同步NC
          this.submitNC.visible.show = true
          this.submitNC.data = data
        }
      },
      closeModal(type, data) {
        console.log(type, data)
        if (type === 'add') {
          this.selectDocument.visible.show = false
          this.linkTo(`/receiveBill/detail?isEdit=1&id=${data}`)
        }
        if (type === 'invalidApply') {
          this.invalidApply.visible.show = false
          this.invalidApply.data = null
          this.getList()
        }
        if (type === 'openTemplate') {
          this.openTemplate.visible.show = false
          this.openTemplate.data = null
        }
        if (type === 'verify') {
          this.verify.visible.show = false
          this.verify.data = null
          this.getList()
        }
        if (type === 'submitNC') {
          this.submitNC.visible.show = false
          this.submitNC.data = null
          this.getList()
        }
      },
      // 刷新
      async refresh() {
        const chooseLst = this.tableChoseList
        if (chooseLst.length !== 1) {
          this.$alertWarning(
            chooseLst.length === 0 ? '至少选择一条记录！' : '只能选择一条记录'
          )
          return false
        }
        const { orderType, billStatus, id } = chooseLst[0]
        if (!orderType) {
          this.$alertWarning('手工创建的应收账单不能刷新！')
          return false
        }
        if (billStatus !== '00') {
          this.$alertWarning('只能刷新账单状态为待提交的应收账单！')
          return false
        }
        await refreshReceiveBill({ id })
        this.$successMsg('正在刷新，稍后点击【查询】，查询结果')
        this.getList()
      },
      // 删除
      del() {
        const chooseList = this.tableChoseList
        if (!chooseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        if (chooseList.some((item) => item.billStatus !== '00')) {
          return this.$alertWarning('只能删除账单状态为待提交的应收账单！')
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = chooseList.map((item) => item.id).toString()
          try {
            await delReceiveBill({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 开票
      async open() {
        const chooseList = this.tableChoseList
        const chooseListLength = chooseList.length
        if (!chooseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        const flag = chooseList.every((item) => {
          if (['05', '06'].includes(item.billStatus)) {
            this.$alertWarning('开票的应收账单状态不能为“作废待审”、“已作废”！')
            return false
          }
          if (
            chooseListLength > 1 &&
            !['02', '03', '04'].includes(item.billStatus)
          ) {
            this.$alertWarning(
              '合并开票时账单状态需为“待核销”、“部分核销”、“已核销”！'
            )
            return false
          }
          if (!['00', '02'].includes(item.invoiceStatus)) {
            this.$alertWarning(
              '开票的应收账单开票状态只能为“待开票”、“已作废”！'
            )
            return false
          }
          return true
        })
        if (!flag) return
        // 相同客户 相同币种校验
        if (
          [...new Set(chooseList.map((item) => item.customerId))].length !== 1
        ) {
          this.$alertWarning('仅能对同客户的应收账单进行同时合并开票！')
          return false
        }
        if (
          [...new Set(chooseList.map((item) => item.currency))].length !== 1
        ) {
          this.$alertWarning('仅能对同结算币种的应收账单进行同时合并开票！')
          return false
        }
        await validateInfo({
          ids: chooseList.map((item) => item.id).toString()
        })
        this.showModal('openTemplate', chooseList)
      },
      // 导出pdf
      exportPDF() {
        const chooseList = this.tableChoseList
        if (!chooseList.length) {
          return this.$alertWarning('请选择导出的应收账单！')
        }
        this.$exportFile(exportPdfUrl, {
          ids: chooseList.map((item) => item.id).toString()
        })
      },
      // 导出excel
      exportExcel() {
        const chooseList = this.tableChoseList
        let exportJson = {}
        if (chooseList.length) {
          exportJson = { ids: chooseList.map((item) => item.id).toString() }
        } else {
          const billStates = this.searchProps.billStates.join(',')
          exportJson = { ...this.searchProps, billStates }
        }
        this.$exportFile(exportExcelUrl, exportJson)
      },
      // 更新凭证信息
      async update() {
        const chooseList = this.tableChoseList
        if (!chooseList.length) {
          return this.$alertWarning('请选择更新的数据')
        }
        await receiveBillUpdateVoucher({
          ids: chooseList.map((item) => item.id).toString()
        })
        this.$successMsg('操作成功')
        this.getList()
      },
      // 查看发票信息
      viewInvoice(invoiceNo) {
        this.$exportFile(viewInvoiceUrl + invoiceNo)
      }
    }
  }
</script>
