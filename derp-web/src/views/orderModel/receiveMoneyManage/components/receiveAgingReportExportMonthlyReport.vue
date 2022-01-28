<template>
  <JFX-Dialog
    title="月结报告导出"
    :width="'450px'"
    :top="'20vh'"
    :showClose="true"
    :visible.sync="reportVisible"
    @comfirm="comfirm"
    :confirmBtnLoading="confirmBtnLoading"
    class="edit-bx"
  >
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-form-item
        label="事业部："
        prop="buId"
        class="search-panel-item fs-14 clr-II"
      >
        <el-select
          v-model="ruleForm.buId"
          placeholder="请选择"
          clearable
          filterable
          :data-list="getBUSelectBean('buList')"
        >
          <el-option
            v-for="item in selectOpt.buList"
            :key="item.key"
            :label="item.value"
            :value="item.key"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="月结月份："
        prop="month"
        class="search-panel-item fs-14 clr-II"
      >
        <el-date-picker
          v-model="ruleForm.month"
          :clearable="false"
          :format="'yyyy-MM'"
          :value-format="'yyyy-MM'"
          type="month"
          placeholder="选择日期时间"
        ></el-date-picker>
      </el-form-item>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { receiveAgingExportMonthlyReport } from '@a/receiveMoneyManage/index'
  export default {
    mixins: [commomMix],
    props: {
      reportVisible: {
        type: Object,
        default: function () {
          return {
            show: false
          }
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
          buId: '',
          month: ''
        },
        rules: {
          month: {
            required: true,
            message: '请选择月结月份',
            trigger: 'blur'
          }
        },
        confirmBtnLoading: false
      }
    },
    mounted() {},
    methods: {
      comfirm() {
        this.confirmBtnLoading = true
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          const {
            data: { code }
          } = await receiveAgingExportMonthlyReport({
            ...this.ruleForm
          })
          this.confirmBtnLoading = false
          code === '00' &&
            this.$alertSuccess('请在任务列表查看进度', () => {
              this.$emit('close')
            })
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
