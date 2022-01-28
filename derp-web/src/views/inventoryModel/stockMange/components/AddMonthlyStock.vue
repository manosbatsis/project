<template>
  <!--  新增月结账单 组件  -->
  <div class="edit-bx">
    <JFX-Dialog
      title="新增月结账单"
      closeBtnText="取 消"
      :width="'750px'"
      :top="'80px'"
      :showClose="true"
      :visible.sync="isVisible"
      :confirmBtnLoading="confirmBtnLoading"
      @comfirm="comfirm"
    >
      <JFX-Form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        style="margin-bottom: 10px"
      >
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="仓库：" prop="depotId">
              <el-select
                v-model="ruleForm.depotId"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectBeanByMerchantRel('warehouseList')"
              >
                <el-option
                  v-for="item in selectOpt.warehouseList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结转月份：" prop="month">
              <el-date-picker
                v-model="ruleForm.month"
                value-format="yyyy-MM"
                placeholder="请选择日期"
                type="month"
                clearable
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-Dialog>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { saveMonthlyAccount } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: { show: false }
    },
    data() {
      return {
        ruleForm: {
          depotId: '',
          month: ''
        },
        rules: {
          depotId: { required: true, message: '请选择仓库', trigger: 'change' },
          month: {
            required: true,
            message: '请选择结转月份',
            trigger: 'change'
          }
        },
        confirmBtnLoading: false
      }
    },
    methods: {
      // 提交表单
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请填写必填信息')
            return false
          }
          try {
            this.confirmBtnLoading = true
            const { depotId: depotIdAdd, month: settlementMonthAdd } =
              this.ruleForm
            await saveMonthlyAccount({ depotIdAdd, settlementMonthAdd })
            this.$successMsg('操作成功')
            this.$emit('update:isVisible', { show: false })
            this.$emit('comfirm')
          } catch (error) {
            this.$errorMsg(error.message)
          } finally {
            this.confirmBtnLoading = false
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.edit-bx {
    .el-form-item__label {
      width: 100px;
    }
  }
</style>
