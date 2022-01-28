<template>
  <!-- 供应商新增/编辑页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <JFX-Form :model="baseForm" ref="baseForm" :rules="rules">
      <!-- 供应商详情 -->
      <JFX-title title="供应商详情" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          供应商编号：{{ baseForm.customerCode || '- -' }}
        </el-col>
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          供应商名称：{{ baseForm.customerName || '- -' }}
        </el-col>
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          供应商简称：{{ baseForm.customerName || '- -' }}
        </el-col>
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          结算方式：{{
            { 1: '预付', 2: '到付', 3: '月结' }[baseForm.settlementMode] ||
            '- -'
          }}
        </el-col>
      </el-row>
      <!-- 询价池 -->
      <JFX-title title="询价池" className="mr-t-10" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="品类：" prop="merchandiseCatId">
            <el-select
              v-model="baseForm.merchandiseCatId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in productTypeBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="品牌：" prop="brandId">
            <el-select
              v-model="baseForm.brandId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in brandBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="原产国：" prop="countryId">
            <el-select
              v-model="baseForm.countryId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in countryBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="计量单位：" prop="unitId">
            <el-select
              v-model="baseForm.unitId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in unitBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品名称：" prop="goodsName">
            <el-input
              v-model="baseForm.goodsName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="最小起订量：" prop="minimum">
            <JFX-Input
              v-model.trim="baseForm.minimum"
              :precision="0"
              :min="0"
              clearable
              placeholder="请输入"
            ></JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="最大供货量：" prop="maximum">
            <JFX-Input
              v-model.trim="baseForm.maximum"
              :precision="0"
              :min="0"
              clearable
              placeholder="请输入"
            ></JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="供货价：" prop="supplyPrice">
            <JFX-Input
              v-model.trim="baseForm.supplyPrice"
              :precision="3"
              :min="0"
              clearable
              placeholder="请输入"
            ></JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="规格型号：" prop="spec">
            <el-input v-model="baseForm.spec" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button @click="handleSubmit" type="primary" :loading="btnLoading">
        保 存
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { SIPoolEditPage, modifySIPool } from '@a/customerManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        baseForm: {
          customerCode: '',
          customerName: '',
          settlementMode: '',
          merchandiseCatId: '',
          brandId: '',
          countryId: '',
          unitId: '',
          goodsName: '',
          minimum: '',
          maximum: '',
          supplyPrice: '',
          spec: ''
        },
        rules: {
          merchandiseCatId: {
            required: true,
            message: '请选择品类',
            trigger: 'change'
          },
          brandId: { required: true, message: '请选择品牌', trigger: 'change' },
          countryId: {
            required: true,
            message: '请选择原产国',
            trigger: 'change'
          },
          unitId: {
            required: true,
            message: '请选择计量单位',
            trigger: 'change'
          }
        },
        btnLoading: false,
        brandBean: [],
        catBean: [],
        countryBean: [],
        productTypeBean: [],
        unitBean: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        try {
          this.btnLoading = true
          const {
            data: { brandBean, countryBean, detail, productTypeBean, unitBean }
          } = await SIPoolEditPage({ id })
          for (const key in this.baseForm) {
            this.baseForm[key] = ![null, undefined].includes(detail[key])
              ? detail[key] + ''
              : ''
            this.brandBean = brandBean || []
            this.countryBean = countryBean || []
            this.productTypeBean = productTypeBean || []
            this.unitBean = unitBean || []
          }
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.btnLoading = false
        }
      },
      handleSubmit() {
        this.$refs.baseForm.validate(async (valid) => {
          if (valid) {
            const { id } = this.$route.query
            try {
              this.btnLoading = true
              await modifySIPool({ ...this.baseForm, id })
              this.$successMsg('操作成功')
              this.closeTag()
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.btnLoading = false
            }
          } else {
            this.$errorMsg('请正确填写表单必填项')
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
</style>
