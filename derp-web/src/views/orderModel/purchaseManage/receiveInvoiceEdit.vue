<template>
  <div class="page-bx bgc-w order-edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <JFX-title title="录入发票时间" className="mr-t-20" />
    <JFX-Form class="mr-t-20" :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="采购订单编号：" prop="purchaseOrderCode">
            <el-input
              v-model.trim="ruleForm.purchaseOrderCode"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="PO号：" prop="poNo">
            <el-input v-model.trim="ruleForm.poNo" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="事业部：" prop="buId">
            <el-select
              v-model="ruleForm.buId"
              placeholder="请选择"
              filterable
              disabled
              clearable
              :data-list="getBUSelectBean('buList')"
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
        <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="供应商：" prop="supplierId">
            <el-select
              v-model="ruleForm.supplierId"
              placeholder="请选择"
              filterable
              disabled
              clearable
              :data-list="getSupplierList('suppList')"
            >
              <el-option
                v-for="item in selectOpt.suppList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="采购币种：">
            <el-select
              v-model="ruleForm.currency"
              disabled
              placeholder="请选择"
              filterable
              clearable
              :list-data="
                getCurrencySelectBean('currencyList', { customerId: '' })
              "
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
        <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="发票号码：" prop="invoiceNo">
            <el-input
              v-model.trim="ruleForm.invoiceNo"
              clearable
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="发票日期：" prop="drawInvoiceDate">
            <el-date-picker
              v-model="ruleForm.drawInvoiceDate"
              type="date"
              value-format="yyyy-MM-dd 00:00:00"
              style="width: 94%"
              clearable
              :picker-options="pickerOptions"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="收到发票日期：" prop="invoiceDate">
            <el-date-picker
              v-model="ruleForm.invoiceDate"
              type="date"
              value-format="yyyy-MM-dd 00:00:00"
              style="width: 94%"
              clearable
              :picker-options="pickerOptions"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="开票人：" prop="payName">
            <el-input
              v-model.trim="ruleForm.payName"
              clearable
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <el-form-item label="预计付款日期：" prop="payDate">
            <el-date-picker
              v-model="ruleForm.payDate"
              type="date"
              value-format="yyyy-MM-dd 00:00:00"
              style="width: 94%"
              clearable
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <JFX-title title="商品信息" className="mr-t-20 mr-b-20" />
    <!-- 表格 -->
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
        min-width="120"
        align="center"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="purchaseNum"
        label="采购数量"
        align="center"
        width="90"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="采购单价" align="center" width="140">
        <template slot-scope="scope">
          {{ scope.row.purchasePrice || 0 }}
        </template>
      </el-table-column>
      <el-table-column
        prop="oldNum"
        label="可开票数量"
        align="center"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">{{ scope.row.oldNum || 0 }}</template>
      </el-table-column>
      <el-table-column
        prop="num"
        label="本次开票数量"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-input-number
            v-model.trim="scope.row.num"
            v-max-spot="0"
            clearable
            :min="0"
            :precision="0"
            :controls="false"
            @change="clac('num', scope.$index)"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column
        prop="amount"
        label="发票总金额(不含税)"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-input-number
            v-model.trim="scope.row.amount"
            v-max-spot="2"
            clearable
            :min="0"
            :precision="2"
            :controls="false"
            @change="clac('amount', scope.$index)"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column prop="tax" label="税额" align="center" width="160">
        <template slot-scope="scope">
          <el-input-number
            v-model.trim="scope.row.tax"
            v-max-spot="2"
            clearable
            :min="0"
            :precision="2"
            :controls="false"
            @change="clac('tax', scope.$index)"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column label="税率" align="center" width="100">
        <template slot-scope="scope">{{ scope.row.taxRate || 0 }}</template>
      </el-table-column>
      <el-table-column
        prop="taxAmount"
        label="发票总金额(含税)"
        align="center"
        width="160"
      >
        <template slot-scope="scope">{{ scope.row.taxAmount || 0 }}</template>
      </el-table-column>
    </JFX-table>
    <JFX-title title="附件列表" className="mr-t-20" />
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
    ></enclosure-list>
    <!-- 表格 end-->
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="submitForm" :loading="saveLoading">
        确 定
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getInvocieByPurchaseId, saveInvoice } from '@a/purchaseManage/index'
  import { attachmentUploadFiles } from '@a/base/index'
  import { getBaseUrl } from '@u/tool'
  export default {
    mixins: [commomMix],
    components: {
      enclosureList: () => import('@c/enclosureList/index')
    },
    data() {
      return {
        ruleForm: {
          // id: '',
          purchaseOrderCode: '',
          poNo: '',
          buId: '',
          buName: '',
          supplierId: '',
          supplierName: '',
          invoiceDate: '',
          drawInvoiceDate: '',
          payDate: '',
          payName: '',
          invoiceNo: '',
          code: '',
          currency: ''
        },
        rules: {
          drawInvoiceDate: {
            required: true,
            message: '发票日期不能为空',
            trigger: 'change'
          }
        },
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10000,
          total: 10001
        },
        detail: {}, // 详情
        depotType: '',
        isInOutInstruction: false,
        saveLoading: false,
        choseIndex: '',
        ids: '',
        action: '',
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now()
          }
        }
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        this.detail = {}
        this.tableData.list = []
        this.reset('ruleForm')
        const { query } = this.$route
        if (query.id) {
          // 编辑
          this.editInit(query)
        }
      },
      // 保存
      submitForm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            if (this.$refs.enclosure.tableData.list.length < 1) {
              this.$errorMsg('请上传附件')
              return false
            }
            let flage = true
            for (let i = 0; i < this.tableData.list.length; i++) {
              const { num, oldNum } = this.tableData.list[i]
              const tips = `表格第${i + 1}行, `
              if (!this.$transformZeroBl(num)) {
                this.$errorMsg(tips + '请输入本次开票数量')
                flage = false
                return false
              }
              if (num > oldNum) {
                this.$errorMsg(tips + '本次开票数量不能大于可开票数量')
                flage = false
                return false
              }
            }
            if (!flage) return false
            try {
              const opt = {
                ...this.ruleForm,
                itemList: this.tableData.list,
                // purchaseOrderId: this.ruleForm.id
                purchaseOrderId: this.$route.query.id || ''
              }
              await saveInvoice(opt)
              this.$successMsg('操作成功')
              this.closeTagAndJump('/purchase/invoiceList')
            } catch (error) {
              console.log(error)
            }
          } else {
            this.$errorMsg('请先完善信息')
          }
        })
      },
      // 编辑时初始
      async editInit(query) {
        try {
          const res = await getInvocieByPurchaseId({ purchaseId: query.id })
          this.detail = res.data
          this.depotType = res.data.depotType || ''
          for (const key in this.ruleForm) {
            this.ruleForm[key] = this.detail[key]
              ? this.detail[key].toString()
              : ''
          }
          //  this.ruleForm.id = query.id
          res.data.itemList = res.data.itemList || []
          if (this.detail.itemList) {
            this.tableData.list = this.detail.itemList.map((item) => {
              const { num } = item
              return {
                ...item,
                oldNum: num
              }
            })
          }
          for (let i = 0; i < this.tableData.list.length; i++) {
            this.clac('num', i)
          }
          this.action =
            getBaseUrl(attachmentUploadFiles) +
            attachmentUploadFiles +
            '?token=' +
            sessionStorage.getItem('token') +
            '&code=' +
            this.detail.code
        } catch (err) {
          console.log(err)
        }
      },
      // 上传成功
      successUpload(res) {
        if (res.code + '' === '10000') {
          this.$refs.enclosure.getEnclosureList(1)
        } else {
          this.$errorMsg(res.data)
        }
      },
      // 修改数量
      clac(type, index) {
        let { num, purchasePrice, taxRate, tax, taxAmount, amount } =
          this.tableData.list[index]
        num = num || 0
        purchasePrice = purchasePrice || 0
        taxRate = taxRate || 0
        tax = tax || 0
        taxAmount = taxAmount || 0
        amount = amount || 0
        // 数量改变
        if (type === 'num') {
          amount = (purchasePrice * num).toFixed(2)
        }
        // 发票总金额(不含税)改变
        if (type === 'amount') {
          purchasePrice =
            num === '0' || num === 0 ? '0.00' : (amount / num).toFixed(2)
        }
        taxRate = tax === '0' || tax === 0 ? '0.00' : (tax / amount).toFixed(2)
        taxAmount = (+amount + +tax).toFixed(2)
        this.tableData.list.splice(index, 1, {
          ...this.tableData.list[index],
          num,
          purchasePrice,
          taxRate,
          tax,
          taxAmount,
          amount
        })
      }
    }
  }
</script>
<style>
  .order-edit-bx .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 130px;
  }
  .edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
<style lang="scss" scoped>
  .image-preview {
    display: inline-block;
  }
</style>
