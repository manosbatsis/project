<template>
  <JFX-Dialog
    title="确认审核"
    :width="'450px'"
    :top="'20vh'"
    :showClose="true"
    :visible.sync="visible"
    @comfirm="comfirm"
    class="edit-bx"
  >
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-form-item label="审核结果：" prop="auditType">
        <el-radio-group v-model="ruleForm.auditType">
          <el-radio v-if="type === 'purchase'" label="0">通过</el-radio>
          <el-radio v-if="type === 'purchase'" label="1">驳回</el-radio>
          <el-radio v-if="type !== 'purchase'" label="1">通过</el-radio>
          <el-radio v-if="type !== 'purchase'" label="2">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
    </JFX-Form>
    <div class="mr-t-20"></div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { auditSMPrice, salesAuditSMPrice } from '@a/customerManage/index'
  export default {
    mixins: [commomMix],
    props: {
      visible: {
        type: Object,
        default: function () {
          return {
            show: false,
            ids: ''
          }
        }
      },
      type: {
        type: String,
        default: 'purchase'
      }
    },
    data() {
      return {
        ruleForm: {
          auditType: '0'
        },
        rules: {}
      }
    },
    mounted() {
      if (this.type !== 'purchase') {
        this.ruleForm.auditType = '1'
      }
    },
    methods: {
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          const param = {
            ids: this.visible.ids
          }
          param[this.type === 'purchase' ? 'auditType' : 'type'] =
            this.ruleForm.auditType
          this.type === 'purchase'
            ? await auditSMPrice(param)
            : await salesAuditSMPrice(param)
          this.$successMsg('操作成功')
          this.$emit('comfirm')
          this.visible.show = false
        })
      }
    }
  }
</script>
<style scoped lang="scss">
  ::v-deep.edit-bx {
    .el-form-item__label {
      width: 100px;
    }
    .el-form-item__error {
      white-space: nowrap;
    }
  }
</style>
