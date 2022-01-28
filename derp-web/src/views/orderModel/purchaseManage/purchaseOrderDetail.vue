<template>
  <div class="page-bx bgc-w">
    <JFX-Breadcrumb :showGoback="true" />
    <JFX-Description title="基本信息" :data="detail" :schema="baseSchema" />
    <JFX-Description title="发票信息" :data="detail" :schema="invoiceSchema" />
    <el-tabs v-model="tabActiveName" class="mr-t-15">
      <el-tab-pane label="商品信息" name="goods">
        <JFX-table
          :tableData.sync="tableData"
          :tableColumn="tableColumn"
          :showPagination="false"
          :summaryMap="{
            num: 0,
            amount: 2,
            taxAmount: 2,
            tax: 2
          }"
          showIndex
        />
      </el-tab-pane>
      <el-tab-pane label="附件信息" name="enclosure">
        <JFX-upload
          :url="uploadUrl"
          :limit="10000"
          :multiple="true"
          @success="successUpload"
        >
          <el-button type="primary">上传文件</el-button>
        </JFX-upload>
        <EnclosureList
          class="mr-t-15"
          ref="enclosure"
          :showAction="false"
          :code="detail.code"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  import { attachmentUploadFiles } from '@a/base'
  import { getPurchaseOrderDetails } from '@a/purchaseManage'
  export default {
    mixins: [commomMix],
    components: {
      /* 附件列表 */
      EnclosureList: () => import('@c/enclosureList/index')
    },
    data() {
      return {
        detail: {},
        tabActiveName: 'goods',
        baseSchema: [
          { label: '采购订单编号', field: 'code' },
          { label: '订单状态', field: 'statusLabel' },
          { label: '业务模式', field: 'businessModelLabel' },
          { label: '入仓仓库', field: 'depotName' },
          { label: '供应商', field: 'supplierName' },
          { label: '采购币种', field: 'currencyLabel' },
          { label: '归属日期', field: 'attributionDate' },
          { label: '事业部', field: 'buName' },
          { label: 'PO号', field: 'poNo' },
          { label: '汇率', field: 'rate' },
          { label: '贸易条款', field: 'tradeTermsLabel' },
          { label: '海外仓理货单位', field: 'tallyingUnitLabel' },
          { label: 'LBX单号', field: 'lbxNo' },
          { label: '付款条款', field: 'paymentProvisionLabel' },
          { label: '运输方式', field: 'transportLabel' },
          { label: '融资订单号', field: 'financingNo' },
          { label: '融资备注', field: 'financingRemark' },
          { label: '框架合同号', field: 'frameContractNo' },
          { label: '试单申请编号', field: 'tryApplyCode' },
          { label: '采购方式', field: 'purchaseMethodLabel' },
          { label: '审批单号', field: 'approvalNo' },
          { label: '预计付款日期', field: 'paymentDate' },
          { label: '预计发货日期', field: 'estimateDeliverDate' },
          { label: '红冲状态', field: 'writeOffStatusLabel' },
          { label: '红冲日期', field: 'writeOffDate' },
          { label: '审批方式', field: 'auditMethodLabel' },
          { label: '库位类型', field: 'stockLocationTypeName' }
        ],
        invoiceSchema: [
          { label: '开票人', field: 'invoiceName' },
          { label: '付款人', field: 'payName' },
          { label: '收到发票日期', field: 'invoiceDate' },
          { label: '付款日期', field: 'payDate' },
          { label: '开发票日期', field: 'drawInvoiceDate' },
          { label: '单据状态', field: 'billStatusLabel' },
          { label: '发送邮件状态', field: 'mailStatusLabel' }
        ],
        tableColumn: [
          {
            label: '工厂编码',
            prop: 'factoryNo',
            minWidth: '120'
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '140'
          },
          {
            label: '商品条码',
            prop: 'barcode',
            minWidth: '140'
          },
          {
            label: '采购数量',
            prop: 'num',
            width: '100'
          },
          {
            label: '采购单价 \n (不含税)',
            prop: 'price',
            width: '100'
          },
          {
            label: '采购总金额 \n (不含税)',
            prop: 'amount',
            width: '100'
          },
          {
            label: '采购总金额 \n (含税)',
            prop: 'taxAmount',
            width: '100',
            formatter(row) {
              return row.taxRate && row.taxRate > 0 ? row.taxAmount : row.amount
            }
          },
          {
            label: '税率',
            prop: 'taxRate',
            width: '100',
            formatter(row) {
              return row.taxRate && row.taxRate > 0 ? row.taxRate : '0.00'
            }
          },
          {
            label: '税额',
            prop: 'tax',
            width: '100',
            formatter(row) {
              return row.taxRate && row.taxRate > 0 ? row.tax : 0
            }
          },
          {
            label: '剩余校期',
            prop: 'effectiveIntervalLabel',
            minWidth: '120'
          }
        ],
        /* 上传附件的url */
        uploadUrl: ''
      }
    },
    activated() {
      this.init()
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        if (!id) return false
        try {
          const { data } = await getPurchaseOrderDetails({ id })
          for (const key in data) {
            if (key.match(/date/gi)) {
              this.$set(this.detail, key, data[key]?.slice(0, 10))
              continue
            }
            this.$set(this.detail, key, data[key])
          }
          this.tableData.list = data?.itemList || []
          /* 上传附件的url */
          const ip = getBaseUrl(attachmentUploadFiles) + attachmentUploadFiles
          const params = `?token=${sessionStorage.getItem('token')}&code=${
            this.detail.code
          }`
          this.uploadUrl = ip + params
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 上传附件 */
      successUpload({ code, message }) {
        if (code?.toString() !== '10000') {
          this.$errorMsg(message)
          return false
        }
        this.$refs.enclosure.getEnclosureList()
      }
    }
  }
</script>
