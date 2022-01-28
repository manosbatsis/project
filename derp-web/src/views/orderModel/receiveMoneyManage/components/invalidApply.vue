<template>
  <div>
    <JFX-Dialog
      :visible.sync="invalidApplyVisible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'500px'"
      :title="'应收作废申请'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      top="15vh"
    >
      <JFX-Form ref="form" :model="form" :rules="rules" class="invalidAllyEdit">
        <el-form-item label="应收单号：">
          {{ data.code }}
        </el-form-item>
        <el-form-item label="事业部：">
          {{ data.buName }}
        </el-form-item>
        <el-form-item label="应收金额：">
          {{ data.receivablePrice }}
        </el-form-item>
        <el-form-item label="作废理由：" prop="invalidRemark">
          <el-input type="textarea" v-model="form.invalidRemark"></el-input>
        </el-form-item>
      </JFX-Form>
    </JFX-Dialog>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { receiveBillSaveInvalidBill } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      invalidApplyVisible: {
        type: Object,
        default: function () {
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
        form: {
          invalidRemark: ''
        },
        confirmBtnLoading: false,
        rules: {
          invalidRemark: {
            required: true,
            message: '请输入作废理由',
            trigger: 'change'
          }
        }
      }
    },
    methods: {
      // 提交
      async comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) {
            return this.$errorMsg('请补充必填信息')
          }
          const submitJson = {
            ...this.form,
            ids: this.data.id
          }
          await receiveBillSaveInvalidBill(submitJson)
          this.$successMsg('操作成功')
          this.$emit('comfirm')
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.invalidAllyEdit .el-form-item__label {
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
</style>
