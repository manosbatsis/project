<template>
  <!-- 库位映射新增、编辑弹窗 -->
  <JFX-Dialog
    :title="data.title"
    :width="'700px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    closeBtnText="取 消"
    @comfirm="comfirm"
  >
    <JFX-Form :model="ruleForm" :rules="rules" ref="form" class="edit-bx">
      <el-row :gutter="10" class="page-view">
        <el-col :span="12">
          <el-form-item label="公司：" prop="merchantId">
            <el-select
              v-model="ruleForm.merchantId"
              placeholder="请选择"
              clearable
              filterable
              style="width: 100%"
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
        <el-col :span="12">
          <el-form-item label="原货号：" prop="originalGoodsNo">
            <el-input
              v-model.trim="ruleForm.originalGoodsNo"
              placeholder="请输入"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="库位货号：" prop="goodsNo">
            <el-input
              v-model.trim="ruleForm.goodsNo"
              placeholder="请输入"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="库位类型：" prop="type">
            <el-select
              v-model="ruleForm.type"
              placeholder="请选择"
              filterable
              clearable
              style="width: 100%"
              :data-list="getSelectList('invenlocaitonMapping_TypeList')"
            >
              <el-option
                v-for="item in selectOpt.invenlocaitonMapping_TypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import {
    saveOrModifyInventoryLocationMapping,
    detailInventoryLocationMapping
  } from '@a/contrast'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: function () {
          return { visible: false }
        }
      },
      data: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        // 表单数据
        ruleForm: {
          merchantId: '',
          originalGoodsNo: '',
          goodsNo: '',
          type: ''
        },
        // 表单规则
        rules: {
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          originalGoodsNo: {
            required: true,
            message: '请输入原货号',
            trigger: 'blur'
          },
          goodsNo: {
            required: true,
            message: '请输入库位货号',
            trigger: 'blur'
          },
          type: { required: true, message: '请选择库位类型', trigger: 'change' }
        },
        // 按钮loading
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const {
          data: { id }
        } = this
        if (!id) return false
        try {
          this.confirmBtnLoading = true
          const { data = {} } = await detailInventoryLocationMapping({ id })
          for (const key in this.ruleForm) {
            this.ruleForm[key] = ![undefined, null].includes(data[key])
              ? data[key] + ''
              : ''
          }
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.confirmBtnLoading = false
        }
      },
      // 提交表单
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (valid) {
            try {
              this.confirmBtnLoading = true
              const {
                data: { id }
              } = this
              await saveOrModifyInventoryLocationMapping({
                ...this.ruleForm,
                id
              })
              this.$emit('comfirm')
              this.$successMsg('操作成功')
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.confirmBtnLoading = false
            }
          }
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .page-view {
    width: 100%;
    margin-bottom: 10px;
  }
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
    width: 120px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  ::v-deep.edit-bx .el-form-item__content {
    flex: 1;
    margin-right: 20px;
  }
</style>
