<template>
  <JFX-Dialog
    title="申请作废"
    :width="'600px'"
    :top="'120px'"
    :showClose="true"
    :visible.sync="isVisible"
    @comfirm="comfirm"
    class="edit-bx"
  >
    <h3 style="text-align: center">确认申请作废选中数据？</h3>
    <el-row class="mr-b-10">
      <el-col :span="24" class="mr-t-10">
        <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
          <el-form-item label="作废备注：" prop="remark" class="textarea-con">
            <el-input type="textarea" v-model="ruleForm.remark" :rows="5" />
          </el-form-item>
        </JFX-Form>
      </el-col>
    </el-row>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import {
    submitPurchaseInvalid,
    submitSaleInvalid
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
          remark: ''
        },
        rules: {
          remark: { required: true, message: '请输入反审原因', trigger: 'blur' }
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
              ? await submitPurchaseInvalid(param)
              : await submitSaleInvalid(param)
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
