<template>
  <JFX-Dialog
    title="申请红冲"
    :width="'500px'"
    :top="'80px'"
    :visible="applyRedPunchVisible"
    :confirmBtnLoading="confirmBtnLoading"
    @comfirm="comfirm"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      style="padding-bottom: 30px"
    >
      <el-form-item label="红冲原因：" prop="reason">
        <el-input type="textarea" v-model="formData.reason"></el-input>
      </el-form-item>
    </el-form>
  </JFX-Dialog>
</template>

<script>
  import { saveWriteOff } from '@a/salesManage/index'
  export default {
    props: {
      applyRedPunchVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      data: {
        type: Object,
        default: () => {
          return {}
        }
      }
    },
    data() {
      return {
        // 表单
        formData: {
          reason: ''
        },
        // 规则
        rules: {
          reason: [
            { required: true, message: '请输入红冲原因' },
            { max: 150, message: '不能超过150个字', trigger: 'blur' }
          ]
        },
        // 确认loading
        confirmBtnLoading: false
      }
    },
    methods: {
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) return
          try {
            this.confirmBtnLoading = true
            const params = {
              id: this.data.id,
              reason: this.formData.reason
            }
            await saveWriteOff(params)
            this.$successMsg('操作成功')
            this.$emit('update:applyRedPunchVisible', { show: false })
            this.$emit('comfirm')
          } catch (err) {
            console.log(err)
          } finally {
            this.confirmBtnLoading = false
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep .el-form-item__content textarea {
    width: 300px;
    min-height: 80px !important;
  }
</style>
