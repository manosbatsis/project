<template>
  <!-- 预申报单列表页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetForm" @search="getList(1)">
      <JFX-Form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="平台名称："
              prop="storePlatformCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.storePlatformCode"
                placeholder="请选择"
                filterable
                clearable
                @change="getShopInfoList()"
                :data-list="getSelectList('storePlatformList')"
              >
                <el-option
                  v-for="item in selectOpt.storePlatformList"
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
                filterable
                clearable
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
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.buId"
                placeholder="请选择"
                filterable
                clearable
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
              label="平台结算单号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model.trim="ruleForm.code" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="账单月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="ruleForm.month"
                type="month"
                value-format="yyyy-MM"
                placeholder="请选择"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="账单状态："
              prop="billStatusList"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.billStatusList"
                placeholder="请选择"
                clearable
                multiple
                :data-list="getSelectList('tocReceiveBill_billStatusList')"
              >
                <el-option
                  v-for="item in selectOpt.tocReceiveBill_billStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="发票编号："
              prop="invoiceNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model.trim="ruleForm.invoiceNo" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="发票状态："
              prop="invoiceStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.invoiceStatus"
                placeholder="请选择"
                clearable
                :data-list="getSelectList('receiveBill_tocInvoiceStatusList')"
              >
                <el-option
                  v-for="item in selectOpt.receiveBill_tocInvoiceStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="外部结算单号："
              prop="externalCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="ruleForm.externalCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="会计期间："
              prop="period"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="ruleForm.period"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="店铺名称："
              prop="shopCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.shopCode"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in shopInfoList"
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
                v-model="ruleForm.ncStatus"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('tocReceiveBill_nvSynList')"
              >
                <el-option
                  v-for="item in selectOpt.tocReceiveBill_nvSynList"
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
          :size="'small'"
          @click="exportExcel"
          v-permission="'toc_receiveBill_export'"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="dele"
          v-permission="'toc_receiveBill_del'"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="linkTo('/receivemoney/toCSettlementEdit')"
          v-permission="'toc_receiveBill_add'"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="invalid"
          v-permission="'toc_receiveBill_invalidApply'"
        >
          作废
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="open"
          v-permission="'toc_receiveBill_open'"
        >
          开票
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="exportPDF"
          v-permission="'toc_receiveBill_exportBill'"
        >
          导出PDF
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="exportSummaryBill"
          v-permission="'toc_receiveBill_exportSummaryBill'"
        >
          导出结算单汇总
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column
        type="selection"
        align="center"
        width="55"
      ></el-table-column>
      <el-table-column label="平台结算单号" align="center" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.code }}</span>
          <br />
          <span>{{ scope.row.buName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="运营类型" align="center" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.shopTypeName }}</span>
          <br />
          <span>{{ scope.row.customerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="平台" align="center" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.storePlatformName }}</span>
          <br />
          <span>{{ scope.row.shopName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应收金额" align="center" min-width="90">
        <template slot-scope="scope">
          {{ scope.row.settlementCurrency }}
          {{ scope.row.receivableAmount || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="原币结算金额" align="center" min-width="100">
        <template slot-scope="scope">
          {{ scope.row.oriCurrency }}
          {{ scope.row.oriReceivableAmount || 0 }}
        </template>
      </el-table-column>
      <el-table-column
        prop="settlementDate"
        label="账单状态"
        align="center"
        min-width="90"
      >
        <template slot-scope="scope">
          {{
            scope.row.settlementDate
              ? scope.row.settlementDate.substring(0, 7)
              : ''
          }}
          <br />
          {{ scope.row.billStatusLabel }}
        </template>
      </el-table-column>
      <el-table-column
        prop="billInDate"
        label="入账月份"
        align="center"
        min-width="90"
      >
        <template slot-scope="scope">
          {{ scope.row.billInDate ? scope.row.billInDate.substring(0, 7) : '' }}
        </template>
      </el-table-column>
      <el-table-column label="发票信息" align="center" min-width="100">
        <template slot-scope="scope">
          <el-button
            type="text"
            v-if="scope.row.invoiceNo"
            v-permission="'toc_receiveBill_view'"
            @click="reView(scope.row)"
          >
            {{ scope.row.invoiceNo }}
          </el-button>
          <br v-if="scope.row.invoiceNo" />
          <span>{{ scope.row.invoiceStatusLabel }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        min-width="100"
        label="外部结算单号"
        prop="externalCode"
      >
      </el-table-column>
      <el-table-column label="创建人" align="center" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.creater }}</span>
          <br />
          <span>{{ scope.row.createDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="NC单据" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.ncCode }}</span>
          <br />
          <span>{{ scope.row.ncStatusLabel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="凭证信息" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.voucherName }}</span>
          <br />
          <span>{{ scope.row.voucherStatusLabel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="left" fixed="right" width="130">
        <template slot-scope="scope">
          <!-- 待提交 —— 编辑页面 -->
          <el-button
            type="text"
            v-if="['00'].includes(scope.row.billStatus)"
            v-permission="'toc_receiveBill_edit'"
            @click="
              linkTo(
                '/receivemoney/toCSettlemenDetail?type=edit&id=' + scope.row.id,
                '应收编辑'
              )
            "
          >
            编辑
          </el-button>
          <!-- 待审核/作废待审 —— 审核-->
          <el-button
            type="text"
            v-if="['01', '05'].includes(scope.row.billStatus)"
            v-permission="'toc_receiveBill_audit'"
            @click="
              linkTo(
                '/receivemoney/toCSettlemenDetail?type=audit&id=' +
                  scope.row.id,
                '应收审核'
              )
            "
          >
            审核
          </el-button>
          <!-- 账单状态为待核销或部分核销、已核销的账单状态，且NC同步状态为未同步 —— 同步NC应收 -->
          <el-button
            type="text"
            v-if="
              ['02', '03', '04'].includes(scope.row.billStatus) &&
              scope.row.ncStatus === '10'
            "
            v-permission="'toc_receiveBill_submitNC'"
            @click="openNc(scope.row)"
          >
            同步NC应收
          </el-button>
          <!-- 生成失败 —— 导出错误信息 -->
          <el-button
            type="text"
            v-if="['08'].includes(scope.row.billStatus)"
            v-permission="'toc_receiveBill_error'"
            @click="downLoadError(scope.row)"
          >
            失败原因
          </el-button>
          <el-button
            type="text"
            v-if="['08'].includes(scope.row.billStatus)"
            v-permission="'toc_receiveBill_add'"
            @click="
              linkTo('/receivemoney/toCSettlementEdit?id=' + scope.row.id)
            "
          >
            重新上传
          </el-button>
          <!-- 除了生成中/生成失败 —— 详情 -->
          <el-button
            type="text"
            v-if="!['08', '08'].includes(scope.row.billStatus)"
            v-permission="'toc_receiveBill_detail'"
            @click="
              linkTo('/receivemoney/toCSettlemenDetail?id=' + scope.row.id)
            "
          >
            详情
          </el-button>
          <el-button
            type="text"
            v-if="['02', '03'].includes(scope.row.billStatus)"
            v-permission="'toc_receiveBill_verify'"
            @click="openVerify(scope.row)"
          >
            核销
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 作废 -->
    <void-settlement
      v-if="visible.show"
      :visible="visible"
      :targetData="targetData"
      @close="getList"
    ></void-settlement>
    <!-- 作废 end-->
    <!-- 同步NC-->
    <sync-nC
      v-if="NcVisible.show"
      :visible="NcVisible"
      :ids="ids"
      @close="getList()"
    ></sync-nC>
    <!-- 同步NC end-->
    <!-- 选择模板 start -->
    <ChooseTemplate
      v-if="openTemplate.visible.show"
      :openTemplateVisible="openTemplate.visible"
      :data="openTemplate.data"
      @comfirm="selectTemplateCb"
      :diyComfirm="true"
    ></ChooseTemplate>
    <!-- 选择模板 end -->
    <!-- 核销 start -->
    <verifyToc
      v-if="verify.visible.show"
      :verifyVisible="verify.visible"
      :data="verify.data"
      @comfirm="closeVerify"
    ></verifyToc>
    <!-- 核销 end -->
  </div>
</template>
<script>
  import {
    listTocReceiveBill,
    delToCReceiveBill,
    exportBillUrl,
    exportPDFUlr,
    downLoadErrorUrl,
    exportSummaryBillUrl,
    previewUrl,
    validateInfo,
    getShopInfo
  } from '@a/receiveMoneyManage/index'
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  export default {
    mixins: [commomMix],
    components: {
      voidSettlement: () => import('./components/voidSettlement'),
      syncNC: () => import('./components/toCsyncNC'),
      // 开票
      ChooseTemplate: () => import('./components/ChooseTemplate'),
      // 核销
      verifyToc: () => import('./components/verifyToc.vue')
    },
    data() {
      return {
        shopInfoList: [],
        ruleForm: {
          storePlatformCode: '',
          customerId: '',
          buId: '',
          code: '',
          month: '',
          billStatusList: [],
          invoiceNo: '',
          invoiceStatus: '',
          externalCode: '',
          period: '',
          shopCode: '',
          ncStatus: ''
        },
        visible: { show: false },
        NcVisible: { show: false },
        targetData: {},
        ids: '',
        openTemplate: { visible: { show: false }, data: null },
        // 核销
        verify: {
          visible: { show: false },
          data: null
        }
      }
    },
    activated() {
      this.getShopInfoList()
      this.getList()
    },
    mounted() {
      // 先获取应收结算状态再请求数据
      this.getSelectList('tobTempReceiveBill_statusList', null, null, () => {
        this.getList(1)
        this.getShopInfoList()
      })
    },
    methods: {
      // 获取店铺列表
      async getShopInfoList() {
        try {
          const { data = [] } = await getShopInfo({
            storePlatformCode: this.ruleForm.storePlatformCode
          })
          const tempList = data.map((item) => ({
            key: item.shopCode,
            value: item.shopName
          }))
          await this.$nextTick()
          this.shopInfoList = tempList
          this.ruleForm.shopCode = ''
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      statusLabel(val) {
        const data =
          this.selectOpt.tobTempReceiveBill_statusList.find(
            (item) => item.key === val
          ) || {}
        return data.value || ''
      },
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
          const res = await listTocReceiveBill(opt)
          this.tableData.list = res.data.list
          this.tableData.total = res.data.total
        } catch (err) {
          console.log(err)
        }
        this.tableData.loading = false
      },
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      },
      // 导出
      async exportExcel() {
        if (!this.tableChoseList.length) {
          return this.$errorMsg('请选择一条记录')
        }
        const {
          data: { code }
        } = await exportBillUrl({
          ids: this.tableChoseList.map((item) => item.id).toString()
        })
        code === '00' && this.$alertSuccess('请在任务列表查看进度')
      },
      // 删除
      dele() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条数据！')
          return false
        }
        // 只能删除账单状态为待提交或者生成失败的应收账单
        const target = this.tableChoseList.every((item) =>
          ['00', '08'].includes(item.billStatus)
        )
        if (!target) {
          return this.$errorMsg(
            '只能删除账单状态为待提交或者生成失败的应收账单'
          )
        }
        this.$confirm('确认删除数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          await delToCReceiveBill({ ids })
          this.$successMsg('删除成功')
          this.getList()
        })
      },
      // 作废
      invalid() {
        if (this.tableChoseList.length !== 1) {
          this.$errorMsg('请选择一条记录')
          return false
        }
        if (this.tableChoseList[0].billStatus !== '02') {
          this.$errorMsg('仅对账单状态为”待核销“的结算单可操作作废申请！')
          return false
        }
        this.targetData = this.tableChoseList[0]
        this.visible.show = true
      },
      // 导出pdf
      exportPDF() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('请至少选择一条记录')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.$exportFile(exportPDFUlr, { ids })
      },
      // 导出结算单汇总
      async exportSummaryBill() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('请至少选择一条记录')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        const {
          data: { code }
        } = await exportSummaryBillUrl({ ids })
        code === '00' && this.$alertSuccess('请在任务列表查看进度')
      },
      // 点击同步NC
      openNc(row) {
        this.ids = row.id
        this.NcVisible.show = true
      },
      // 开票
      async open() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('请至少选择一条记录')
          return false
        }
        // 校验
        await validateInfo({
          ids: this.tableChoseList.map((item) => item.id).toString()
        })
        this.openTemplate.visible.show = true
        this.openTemplate.data = this.tableChoseList
      },
      // 开票回填
      selectTemplateCb(data) {
        this.openTemplate.visible.show = false
        this.openTemplate.data = []
        const param = {
          tempId: data.template.id,
          ids: this.tableChoseList.map((item) => item.id).toString(),
          codes: this.tableChoseList.map((item) => item.code).toString(),
          invoiceStatus: 1
        }
        let url = '/receivemoney/toInvoicePageToc?'
        for (const key in param) {
          url += key
          url += '='
          url += param[key]
          url += '&'
        }
        this.linkTo(url)
      },
      downLoadError(row) {
        this.$exportFile(downLoadErrorUrl, { id: row.id })
      },
      reView(row) {
        const url =
          getBaseUrl(previewUrl) +
          previewUrl +
          row.invoiceNo +
          '?&token=' +
          sessionStorage.getItem('token')
        window.open(url)
      },
      // 核销
      openVerify(row) {
        this.verify.visible.show = true
        this.verify.data = row
      },
      closeVerify() {
        this.verify.visible.show = false
        this.verify.data = null
        this.getList()
      }
    }
  }
</script>
<style lang="scss" scoped></style>
