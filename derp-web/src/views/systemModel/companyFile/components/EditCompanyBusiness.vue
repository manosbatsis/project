<template>
  <!-- 公司事业部新增、编辑弹窗 -->
  <JFX-Dialog
    :title="id ? '编辑' : '新增'"
    :width="'500px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    btnAlign="center"
    closeBtnText="取 消"
    @comfirm="comfirm"
  >
    <div v-loading="confirmBtnLoading" class="edit-company-business">
      <JFX-Form :model="ruleForm" :rules="rules" ref="form">
        <el-row :gutter="10" class="page-view">
          <el-col :span="24">
            <el-form-item label="公司名称：" prop="merchantId">
              <el-select
                v-model="ruleForm.merchantId"
                placeholder="请选择"
                clearable
                filterable
                style="width: 100%"
                :disabled="!!id"
                :data-list="getMerchantSelectBean('merchantList')"
              >
                <el-option
                  v-for="item in selectOpt.merchantList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="事业部：" prop="buId">
              <el-select
                v-model="ruleForm.buId"
                placeholder="请选择"
                clearable
                filterable
                style="width: 100%"
                :disabled="!!id"
                :data-list="getAllSelectBean('businessList')"
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
          <el-col :span="24">
            <el-form-item label="状态：" prop="status">
              <el-select
                v-model="ruleForm.status"
                placeholder="请选择"
                filterable
                clearable
                style="width: 100%"
                :data-list="getSelectList('merchant_bu_rel_statuslist')"
              >
                <el-option
                  v-for="item in selectOpt.merchant_bu_rel_statuslist"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="采购价格管理：" prop="purchasePriceManage">
              <el-select
                v-model="ruleForm.purchasePriceManage"
                placeholder="请选择"
                filterable
                clearable
                style="width: 100%"
                :data-list="
                  getSelectList('merchant_bu_rel_purchase_price_manageList')
                "
              >
                <el-option
                  v-for="item in selectOpt.merchant_bu_rel_purchase_price_manageList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="销售价格管理：" prop="salePriceManage">
              <el-select
                v-model="ruleForm.salePriceManage"
                placeholder="请选择"
                filterable
                clearable
                style="width: 100%"
                :data-list="
                  getSelectList('merchant_bu_rel_sale_price_manageList')
                "
              >
                <el-option
                  v-for="item in selectOpt.merchant_bu_rel_sale_price_manageList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="采购审批方式：" prop="purchaseAuditMethod">
              <el-select
                v-model="ruleForm.purchaseAuditMethod"
                placeholder="请选择"
                filterable
                clearable
                style="width: 100%"
                :data-list="
                  getSelectList('merchant_bu_rel_purchase_audit_methodList')
                "
              >
                <el-option
                  v-for="item in selectOpt.merchant_bu_rel_purchase_audit_methodList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="库位管理：" prop="stockLocationManage">
              <el-select
                v-model="ruleForm.stockLocationManage"
                placeholder="请选择"
                filterable
                clearable
                style="width: 100%"
                :data-list="
                  getSelectList('merchant_bu_rel_stock_location_managelist')
                "
              >
                <el-option
                  v-for="item in selectOpt.merchant_bu_rel_stock_location_managelist"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import {
    modifyMerchantBuRel,
    saveMerchantBuRel,
    detailMerchantBuRel
  } from '@a/companyFile'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      id: {
        type: String | Number,
        default: ''
      }
    },
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          merchantId: '',
          buId: '',
          status: '',
          purchasePriceManage: '',
          salePriceManage: '',
          purchaseAuditMethod: '',
          stockLocationManage: ''
        },
        /* 表单规则 */
        rules: {
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          buId: {
            required: true,
            message: '请选择事业部',
            trigger: 'change'
          },
          status: {
            required: true,
            message: '请选择状态',
            trigger: 'change'
          },
          purchasePriceManage: {
            required: true,
            message: '请选择采购价格管理',
            trigger: 'change'
          },
          salePriceManage: {
            required: true,
            message: '请选择销售价格管理',
            trigger: 'change'
          },
          purchaseAuditMethod: {
            required: true,
            message: '请选择销采购审批方式',
            trigger: 'change'
          },
          stockLocationManage: {
            required: true,
            message: '请选择库位管理'
          }
        },
        /* 按钮loading */
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this
        if (!id) return false
        try {
          this.confirmBtnLoading = true
          const { data } = await detailMerchantBuRel({ id })
          Object.keys(this.ruleForm).forEach((key) => {
            this.ruleForm[key] = ![undefined, null].includes(data[key])
              ? data[key] + ''
              : ''
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.confirmBtnLoading = false
        }
      },
      // 提交表单
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请填写表单必填项')
            return false
          }
          const { id } = this
          try {
            this.confirmBtnLoading = true
            id
              ? await modifyMerchantBuRel({ ...this.ruleForm, id })
              : await saveMerchantBuRel({ ...this.ruleForm })
            this.$emit('update:isVisible', { show: false })
            this.$emit('comfirm')
            this.$successMsg(id ? '编辑成功' : '新增成功')
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.confirmBtnLoading = false
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep .edit-company-business {
    margin-bottom: 20px;

    .el-form-item {
      display: flex;
      align-items: center;
      margin-bottom: 5px;
    }

    .el-form-item__label {
      padding: 0 12px 0 0;
      width: 140px;
      line-height: 40px;
      font-size: 14px;
      text-align: right;
      vertical-align: middle;
      box-sizing: border-box;
      color: #606266;
    }

    .el-form-item__content {
      flex: 1;
      margin-right: 40px;
    }
  }
</style>
