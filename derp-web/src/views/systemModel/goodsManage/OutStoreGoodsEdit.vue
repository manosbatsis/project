<template>
  <!-- 领料单新增/编辑页面 -->
  <div class="page-bx bgc-w edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 表单部分 -->
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 基本信息 -->
      <JFX-title title="基本信息" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="平台备案关区：" prop="customsAreaName">
            <el-input
              v-model.trim="ruleForm.customsAreaName"
              disabled
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="平台商品货号：" prop="goodsNo">
            <el-input
              v-model.trim="ruleForm.goodsNo"
              disabled
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品条形码：" prop="barcode">
            <el-input
              v-model.trim="ruleForm.barcode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品名称：" prop="goodsName">
            <el-input
              v-model.trim="ruleForm.goodsName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品英文名称：" prop="englishGoodsName">
            <el-input
              v-model.trim="ruleForm.englishGoodsName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品品牌：" prop="brandName">
            <el-input
              v-model.trim="ruleForm.brandName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="HS编码：" prop="hsCode">
            <el-input
              v-model.trim="ruleForm.hsCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="工厂编码：" prop="factoryNo">
            <el-input
              v-model.trim="ruleForm.factoryNo"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="一级品类：" prop="productTypeId0">
            <el-select
              v-model="ruleForm.productTypeId0"
              placeholder="请选择"
              clearable
              filterable
              @change="changeProductTypeId0"
            >
              <el-option
                v-for="item in oneCatBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="二级品类：" prop="productTypeId">
            <el-select
              v-model="ruleForm.productTypeId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in twoCatBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="经分销货号：" prop="derpGoodsNo">
            <el-input
              v-model.trim="ruleForm.derpGoodsNo"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 基本信息 end -->
      <!-- 备案信息 -->
      <JFX-title title="备案信息" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品净重(kg)：" prop="netWeight">
            <JFX-Input
              v-model.trim="ruleForm.netWeight"
              :precision="5"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品毛重(kg)：" prop="grossWeight">
            <JFX-Input
              v-model.trim="ruleForm.grossWeight"
              :precision="5"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="规格型号：" prop="spec">
            <el-input
              v-model.trim="ruleForm.spec"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="保质天数：" prop="shelfLifeDays">
            <JFX-Input
              v-model.trim="ruleForm.shelfLifeDays"
              :precision="0"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="备案单价：" prop="filingPrice">
            <JFX-Input
              v-model.trim="ruleForm.filingPrice"
              :precision="5"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="计量单位：" prop="unit">
            <el-input
              v-model.trim="ruleForm.unit"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="原产国：" prop="countyName">
            <el-input
              v-model.trim="ruleForm.countyName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="原产地区：" prop="countyArea">
            <el-input
              v-model.trim="ruleForm.countyArea"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="生产企业名称：" prop="manufacturer">
            <el-input
              v-model.trim="ruleForm.manufacturer"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="生产企业地址：" prop="manufacturerAddr">
            <el-input
              v-model.trim="ruleForm.manufacturerAddr"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="海关商品备案号：" prop="customsGoodsRecordNo">
            <el-input
              v-model.trim="ruleForm.customsGoodsRecordNo"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="金二项号：" prop="jinerxiang">
            <el-input
              v-model.trim="ruleForm.jinerxiang"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="第一法定单位：" prop="unitNameOne">
            <el-input
              v-model.trim="ruleForm.unitNameOne"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="第二法定单位：" prop="unitNameTwo">
            <el-input
              v-model.trim="ruleForm.unitNameTwo"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="SKU编码：" prop="skuCode">
            <el-input
              v-model.trim="ruleForm.skuCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="品牌类型：" prop="brandType">
            <el-input
              v-model.trim="ruleForm.brandType"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="账册备案料号：" prop="materialAccount">
            <el-input
              v-model.trim="ruleForm.materialAccount"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="售卖商品名称（中文）：" prop="saleGoodNames">
            <el-input
              v-model.trim="ruleForm.saleGoodNames"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="EMG编码：" prop="emsCode">
            <el-input
              v-model.trim="ruleForm.emsCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品描述：" prop="describe" class="textarea-con">
            <el-input
              v-model.trim="ruleForm.describe"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="申报要素：" prop="declareFactor">
            <el-input
              v-model.trim="ruleForm.declareFactor"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="商品成分："
            prop="component"
            class="textarea-con"
          >
            <el-input
              v-model="ruleForm.component"
              type="textarea"
              clearable
              placeholder="请输入"
              :autosize="{ minRows: 1, maxRows: 5 }"
            ></el-input>
            <!-- <el-input v-model.trim="ruleForm.component" placeholder="请输入" clearable /> -->
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 备案信息 end -->
      <!-- 箱规设置 -->
      <JFX-title title="箱规设置" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="箱规：" prop="boxToUnit">
            <el-input
              v-model.trim="ruleForm.boxToUnit"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="托规：" prop="torrToUnit">
            <el-input
              v-model.trim="ruleForm.torrToUnit"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 箱规设置 end -->
      <!-- 规格描述 -->
      <JFX-title title="规格描述" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="长(cm)：" prop="length">
            <JFX-Input
              v-model.trim="ruleForm.length"
              :precision="2"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="宽(cm)：" prop="width">
            <JFX-Input
              v-model.trim="ruleForm.width"
              :precision="2"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="高(cm)：" prop="height">
            <JFX-Input
              v-model.trim="ruleForm.height"
              :precision="2"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="体积(cm³)：" prop="volume">
            <JFX-Input
              v-model.trim="ruleForm.volume"
              :precision="2"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="包装方式：" prop="packType">
            <el-input
              v-model.trim="ruleForm.packType"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 规格描述 end -->
    </JFX-Form>
    <!-- 表单部分 end -->
    <!-- 底部按钮 -->
    <div class="flex-c-c mr-t-30">
      <el-button
        type="primary"
        :loading="actionBtnLoading"
        @click="handleSumbit"
      >
        保存
      </el-button>
      <el-button @click="closeTag">取消</el-button>
    </div>
    <!-- 底部按钮 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getTwoLevel,
    editMerchandiseExternalWarehousePage,
    modifyMerchandiseExternalWarehouse
  } from '@a/goodsManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        ruleForm: {
          // 基本信息
          customsAreaName: '',
          goodsNo: '',
          barcode: '',
          goodsName: '',
          englishGoodsName: '',
          brandName: '',
          hsCode: '',
          factoryNo: '',
          productTypeId0: '',
          productTypeId: '',
          derpGoodsNo: '',
          // 备案信息
          netWeight: '',
          grossWeight: '',
          spec: '',
          shelfLifeDays: '',
          filingPrice: '',
          unit: '',
          countyName: '',
          countyArea: '',
          manufacturer: '',
          manufacturerAddr: '',
          customsGoodsRecordNo: '',
          jinerxiang: '',
          unitNameOne: '',
          unitNameTwo: '',
          skuCode: '',
          brandType: '',
          materialAccount: '',
          saleGoodNames: '',
          emsCode: '',
          describe: '',
          declareFactor: '',
          component: '',
          // 箱规设置
          boxToUnit: '',
          torrToUnit: '',
          // 规格描述
          length: '',
          width: '',
          height: '',
          volume: '',
          packType: ''
        },
        rules: {
          customsAreaName: {
            required: true,
            message: '请输入平台备案关区',
            trigger: 'change'
          },
          goodsNo: {
            required: true,
            message: '请输入平台商品货号',
            trigger: 'blur'
          },
          barcode: {
            required: true,
            message: '请输入商品条形码',
            trigger: 'blur'
          },
          goodsName: {
            required: true,
            message: '请输入商品名称',
            trigger: 'blur'
          }
        },
        oneCatBean: [],
        twoCatBean: [],
        actionBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        try {
          const {
            data: { detail, oneCatBean, twoCatBean }
          } = await editMerchandiseExternalWarehousePage({ id })
          for (const key in this.ruleForm) {
            this.ruleForm[key] = ![null, undefined].includes(detail[key])
              ? detail[key] + ''
              : ''
          }
          this.oneCatBean = oneCatBean || []
          this.twoCatBean = twoCatBean || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 保存
      handleSumbit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const { id } = this.$route.query
            try {
              this.actionBtnLoading = true
              await modifyMerchandiseExternalWarehouse({ ...this.ruleForm, id })
              this.$successMsg('操作成功')
              this.closeTag()
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.actionBtnLoading = false
            }
          } else {
            this.$errorMsg('请正确填写表单必填项')
          }
        })
      },
      // 选择一级类目
      async changeProductTypeId0() {
        const { productTypeId0 } = this.ruleForm
        this.ruleForm.productTypeId = ''
        if (productTypeId0) {
          try {
            const { data } = await getTwoLevel({ id: productTypeId0 })
            this.twoCatBean = data || []
          } catch (error) {
            this.$errorMsg(error.message)
          }
        }
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
    padding: 0 4px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 166px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  ::v-deep.textarea-con {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 166px;
    }
    .el-form-item__content {
      width: 220px;
    }
  }
</style>
