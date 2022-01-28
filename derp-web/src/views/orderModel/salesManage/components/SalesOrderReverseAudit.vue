<template>
  <JFX-Dialog
    :visible.sync="visible"
    :showClose="true"
    :width="'450px'"
    :title="'反审'"
    :btnAlign="'center'"
    top="30vh"
    @comfirm="confirm"
  >
    <h1 style="text-align: center">确定反审核吗？</h1>
    <div class="mr-t-20"></div>
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-form-item label="反审原因：" prop="remark">
        <el-input
          v-model.trim="ruleForm.remark"
          clearable
          style="width: 120%"
          placeholder="请输入反审原因"
        ></el-input>
      </el-form-item>
    </JFX-Form>
    <div class="mr-t-30"></div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { reverseAudit } from '@a/salesManage/index'
  export default {
    name: 'SalesOrderReverseAudit',
    mixins: [commomMix],
    props: {
      visible: {
        default() {
          return { show: false }
        }
      },
      data: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    data() {
      return {
        ruleForm: {
          remark: ''
        },
        rules: {
          remark: {
            required: true,
            message: '请输入反审原因',
            trigger: 'blur'
          }
        }
      }
    },
    methods: {
      confirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          await reverseAudit({
            orderId: this.data.id,
            remark: this.ruleForm.remark
          })
          this.$successMsg('操作成功')
          this.$emit('close')
          this.visible.show = false
        })
      }
    }
  }
</script>
