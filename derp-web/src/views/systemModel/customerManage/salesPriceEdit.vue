<template>
  <!-- 客户新增/编辑页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <JFX-Form
      :model="baseForm"
      ref="baseForm"
      :rules="rules"
      :disabled="!!$route.query.isUpload"
    >
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司：" prop="merchantName">
            <el-input
              disabled
              v-model="baseForm.merchantName"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="事业部：" prop="buId">
            <el-select
              v-model="baseForm.buId"
              placeholder="请选择"
              clearable
              filterable
              :disabled="isEdit"
              :data-list="getBUSelectBean('buList')"
            >
              <el-option
                v-for="item in selectOpt.buList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="!isEdit">
          <el-form-item label="客户：" prop="customerId">
            <el-select
              v-model="baseForm.customerId"
              placeholder="请选择"
              :data-list="
                getCusOrSurpSelectBean('supplier_list', { typeFlag: '5' })
              "
              filterable
              clearable
            >
              <el-option
                v-for="item in selectOpt.supplier_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="!isEdit">
          <el-form-item label="条形码：" prop="barcode">
            <el-input v-model="baseForm.barcode" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="isEdit">
          <el-form-item label="客户编码：" prop="customerCode">
            <el-input v-model="baseForm.customerCode" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="isEdit">
          <el-form-item label="标准条码：" prop="commbarcode">
            <el-input v-model="baseForm.commbarcode" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="isEdit">
          <el-form-item label="客户名称：" prop="customerName">
            <el-input v-model="baseForm.customerName" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="isEdit">
          <el-form-item label="商品名称：" prop="goodsName">
            <el-input
              v-model="baseForm.goodsName"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="isEdit">
          <el-form-item label="品牌：" prop="brandName">
            <el-input
              v-model="baseForm.brandName"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-if="isEdit">
          <el-form-item label="规格型号：" prop="spec">
            <el-input v-model="baseForm.spec" clearable disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="币种" prop="currency">
            <el-select
              v-model="baseForm.currency"
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
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="销售价：" prop="salePrice">
            <JFX-Input
              v-model="baseForm.salePrice"
              :min="0"
              :precision="8"
              placeholder="请输入销售价"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="报价生效时间：" prop="effectiveDate">
            <el-date-picker
              type="date"
              style="width: 94%"
              clearable
              value-format="yyyy-MM-dd 00:00:00"
              v-model="baseForm.effectiveDate"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="报价失效时间：" prop="expiryDate">
            <el-date-picker
              type="date"
              style="width: 94%"
              clearable
              value-format="yyyy-MM-dd 00:00:00"
              v-model="baseForm.expiryDate"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="备注：" prop="remark">
            <el-input clearable v-model="baseForm.remark"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <JFX-title title="附件信息" className="mr-t-10" />
    <JFX-upload
      v-if="action"
      @success="successUpload"
      :url="action"
      :limit="10000"
      :multiple="true"
    >
      <el-button id="sale-upload-btn" type="primary">上传文件</el-button>
    </JFX-upload>
    <EnclosureList
      :showAction="true"
      :code="uploadCode"
      ref="enclosure"
      class="mr-t-15"
    />
    <div class="mr-t-30 flex-c-c" v-if="!$route.query.isUpload">
      <el-button @click="handleSubmit" type="primary" :loading="comfirmLoading"
        >保 存</el-button
      >
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  import { attachmentUploadFiles } from '@a/base/index'
  import {
    salesPriceGetCode,
    getSalePriceDetails,
    addCustomerPrice,
    editSMPrice
  } from '@a/customerManage/index'
  export default {
    mixins: [commomMix],
    components: {
      EnclosureList: () => import('@c/enclosureList/index')
    },
    data() {
      return {
        // 基本信息
        baseForm: {
          merchantId: '',
          merchantName: '',
          buId: '',
          customerId: '',
          barcode: '',
          currency: '',
          salePrice: '',
          effectiveDate: '',
          expiryDate: '',
          remark: '',
          // 编辑字段:
          customerCode: '',
          commbarcode: '',
          customerName: '',
          goodsName: '',
          brandName: '',
          spec: ''
        },
        // 表单规则
        rules: {
          merchantName: { required: true },
          buId: { required: true, message: '请选择事业部' },
          customerId: { required: true, message: '请选择客户' },
          barcode: { required: true, message: '请输入条形码' },
          currency: { required: true, message: '请选择币种' },
          salePrice: { required: true, message: '请输入销售价' },
          effectiveDate: { required: true, message: '请选择报价生效时间' },
          expiryDate: { required: true, message: '请选择报价失效时间' },
          customerCode: { required: true, message: '请完善数据' },
          commbarcode: { required: true, message: '请完善数据' },
          customerName: { required: true, message: '请完善数据' },
          goodsName: { required: true, message: '请完善数据' },
          spec: { required: true, message: '请完善数据' }
        },
        // 上传
        uploadCode: '',
        action: '',
        // 编辑
        isEdit: false,
        // 复制
        isCopy: false,
        copyId: '',
        // 保存按钮
        comfirmLoading: false
      }
    },
    async mounted() {
      const { id, isCopy, copyId } = this.$route.query
      const { merchantName, merchantId } = this.$store.getters.userInfo
      this.baseForm.merchantName = merchantName
      this.baseForm.merchantId = merchantId
      // 复制情况
      if (isCopy) {
        this.isCopy = true
        this.copyId = copyId
        this.copyInit()
        return
      }
      // 新增编辑情况
      id ? this.editInit() : this.addInit()
    },
    methods: {
      // 新增初始化
      async addInit() {
        // 新增获取code，附件上传参数，保存的时候关联附件
        const { data: code } = await salesPriceGetCode()
        this.uploadInit(code)
      },
      // 编辑初始化
      async editInit() {
        this.isEdit = true
        const { id } = this.$route.query
        const { data } = await getSalePriceDetails({ id })
        let uploadCode = data.code
        // 判断有没有code,用于附件上传
        if (!uploadCode) {
          const { data: code } = await salesPriceGetCode({ id })
          uploadCode = code
        }
        this.uploadInit(uploadCode)
        for (const key in this.baseForm) {
          this.baseForm[key] = data[key] ? String(data[key]) : ''
        }
        this.baseForm.salePrice = data.salePriceLabel
      },
      // 复制初始化
      async copyInit() {
        // 填充数据
        const { copyId: id } = this.$route.query
        const { data } = await getSalePriceDetails({ id })
        for (const key in {
          buId: '',
          customerId: '',
          barcode: '',
          currency: '',
          salePrice: '',
          effectiveDate: '',
          expiryDate: '',
          remark: ''
        }) {
          this.baseForm[key] = data[key] ? String(data[key]) : ''
        }
        this.baseForm.salePrice = data.salePriceLabel
        // 附件
        const { data: code } = await salesPriceGetCode()
        this.uploadInit(code)
      },
      // 上传附件初始化
      async uploadInit(code) {
        this.uploadCode = code
        this.action =
          getBaseUrl(attachmentUploadFiles) +
          attachmentUploadFiles +
          '?token=' +
          sessionStorage.getItem('token') +
          '&code=' +
          code
      },
      // 上传回调
      successUpload(res) {
        if (res.code + '' === '10000') {
          this.$refs.enclosure.getEnclosureList(1)
        } else {
          this.$errorMsg(res.message)
        }
      },
      // 获取参数
      getParams() {
        const params = {
          ...this.baseForm,
          effectiveDateStr: this.baseForm.effectiveDate,
          expiryDateStr: this.baseForm.expiryDate
        }
        // 附件关联
        params.code = this.uploadCode
        // 编辑
        if (this.isEdit) params.id = this.$route.query.id
        return params
      },
      handleSubmit() {
        this.$refs.baseForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请先完善信息')
            return false
          }
          this.comfirmLoading = true
          try {
            const params = this.getParams()
            console.log(params)
            if (this.isEdit) {
              // 编辑
              await editSMPrice(params)
            } else {
              // 新增 复制
              await addCustomerPrice(params)
            }
            this.$successMsg('操作成功')
            this.closeTag()
          } catch (err) {
            console.log(err)
          } finally {
            this.comfirmLoading = false
          }
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.edit-bx .el-form-item__label {
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
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }

  ::v-deep.textarea-container {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 130px;
    }
    .el-form-item__content {
      width: 700px;
    }
  }
</style>
