<template>
  <JFX-Dialog
    title="作废审核"
    :width="'400px'"
    :top="'120px'"
    :showClose="true"
    :visible.sync="isVisible"
    @comfirm="comfirm"
    class="edit-bx"
  >
    <el-row class="mr-b-10">
      <el-col :span="24" class="mr-t-10 flex-c-c">
        <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
          <el-form-item label="审核结果：" prop="auditResult">
            <el-radio-group v-model="ruleForm.auditResult">
              <el-radio label="1">通过</el-radio>
              <el-radio label="2">驳回</el-radio>
            </el-radio-group>
          </el-form-item>
        </JFX-Form>
      </el-col>
    </el-row>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import {
    invalidPurchaseAudit,
    auditSaleInvalid
  } from '@a/customerManage/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      type: String,
      ids: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        ruleForm: {
          auditResult: '1'
        },
        rules: {
          auditResult: {
            required: true,
            message: '请选择审核结果',
            trigger: 'change'
          }
        }
      }
    },
    methods: {
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          const param = {
            ids: this.ids,
            ...this.ruleForm
          }
          try {
            this.type === 'purchase'
              ? await invalidPurchaseAudit(param)
              : await auditSaleInvalid(param)
            this.$successMsg('操作成功')
            this.$emit('comfirm')
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      }
    }
  }
</script>
<style scoped lang="scss">
  ::v-deep.textarea-con {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 90px;
      padding-right: 4px;
    }
    .el-form-item__content {
      flex: 1;
      margin-right: 10px;
    }
  }
</style>
