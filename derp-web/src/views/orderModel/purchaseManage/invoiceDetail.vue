<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- title -->
    <JFX-title title="录入发票时间" className="mr-t-10" />
    <!-- title end -->
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        采购订单编号：{{ detail.purchaseOrderCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        PO号：{{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        供应商：{{ detail.supplierName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        发票号码：{{ detail.invoiceNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        发票日期：{{
          detail.drawInvoiceDate
            ? detail.drawInvoiceDate.substring(0, 10)
            : '- -'
        }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        收到发票日期：{{
          detail.invoiceDate ? detail.invoiceDate.substring(0, 10) : '- -'
        }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        开票人：{{ detail.payName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        预付款日期：{{
          detail.paymentDate ? detail.paymentDate.substring(0, 10) : '- -'
        }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        采购币种：{{ detail.currencyLabel }}
      </el-col>
    </el-row>
    <!-- title -->
    <JFX-title title="商品信息" className="mr-t-15" />
    <JFX-table
      :tableData.sync="tableData"
      :showPagination="false"
      :showSummary="true"
      :summaryMethod="null"
    >
      <el-table-column
        type="index"
        label="序号"
        align="center"
        width="50"
      ></el-table-column>
      <el-table-column
        label="工厂编码"
        align="center"
        width="160"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.factoryNo }}
        </template>
      </el-table-column>
      <el-table-column
        label="商品条形码"
        align="center"
        width="160"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.barcode }}
        </template>
      </el-table-column>
      <el-table-column
        prop="goodsName"
        label="商品名称"
        align="center"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="purchaseNum"
        label="采购数量"
        align="center"
        width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="采购单价" align="center" width="140">
        <template slot-scope="scope">
          {{ scope.row.purchasePrice || 0 }}
        </template>
      </el-table-column>
      <el-table-column
        prop="num"
        label="本次开票数量"
        align="center"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">{{ scope.row.num || 0 }}</template>
      </el-table-column>
      <el-table-column
        prop="amount"
        label="发票总金额(不含税)"
        align="center"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">{{ scope.row.amount || 0 }}</template>
      </el-table-column>
      <el-table-column
        prop="taxAmount"
        label="发票总金额(含税)"
        align="center"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">{{ scope.row.taxAmount || 0 }}</template>
      </el-table-column>
      <el-table-column
        prop="tax"
        label="税额"
        align="center"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">{{ scope.row.tax || 0 }}</template>
      </el-table-column>
    </JFX-table>
    <!-- title end -->
    <JFX-title title="附件列表" className="mr-t-15" />
    <div class="mr-t-15 fs-14 mr-b-20 clr-II">
      <JFX-upload
        @success="successUpload"
        :url="action"
        :limit="10000"
        :multiple="true"
      >
        <el-button type="primary">上传附件</el-button>
        <span class="clr-II fs-10">
          (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
        </span>
      </JFX-upload>
    </div>
    <enclosure-list
      :code="detail.code"
      v-if="detail.code"
      ref="enclosure"
      showAction
    />
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { purchaseInvoiceDetail } from '@a/purchaseManage'
  import { attachmentUploadFiles } from '@a/base/index'
  import { getBaseUrl } from '@u/tool'
  export default {
    mixins: [commomMix],
    components: {
      /* 附件列表 */
      enclosureList: () => import('@c/enclosureList/index')
    },
    data() {
      return {
        detail: {},
        /* 附件上传url */
        action: ''
      }
    },
    mounted() {
      const { id } = this.$route.query
      if (!id) return false
      this.init(id)
    },
    methods: {
      async init(id) {
        try {
          const { data } = await purchaseInvoiceDetail({ id })
          this.detail = data
          this.tableData.list = this.detail.itemList || []
          /* 附件上传url */
          this.action =
            getBaseUrl(attachmentUploadFiles) +
            attachmentUploadFiles +
            '?token=' +
            sessionStorage.getItem('token') +
            '&code=' +
            this.detail.code
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 上传成功 */
      successUpload({ code, data }) {
        if (code + '' === '10000') {
          this.$refs.enclosure.getEnclosureList(1)
        } else {
          this.$errorMsg(data)
        }
      }
    }
  }
</script>
