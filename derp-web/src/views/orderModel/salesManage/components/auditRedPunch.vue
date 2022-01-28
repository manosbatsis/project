<template>
  <JFX-Dialog
    title="审核红冲"
    :width="'500px'"
    :top="'80px'"
    :visible="auditRedPunchVisible"
    :confirmBtnLoading="confirmBtnLoading"
    @comfirm="comfirm"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      style="padding-bottom: 30px"
    >
      <el-form-item label="红冲单据：" prop="code">
        <el-input v-model="formData.code" clearable disabled></el-input>
      </el-form-item>
      <el-form-item label="红冲原因：" prop="reason">
        <el-input type="textarea" v-model="formData.reason" disabled></el-input>
      </el-form-item>
      <el-form-item label="审核结果：" prop="auditResult">
        <el-radio v-model="formData.auditResult" label="1">通过</el-radio>
        <el-radio v-model="formData.auditResult" label="0">驳回</el-radio>
      </el-form-item>
      <el-form-item label="归属日期：" prop="writeOffDate">
        <el-date-picker
          v-model="formData.writeOffDate"
          type="date"
          value-format="yyyy-MM-dd"
        >
        </el-date-picker>
      </el-form-item>
    </el-form>
  </JFX-Dialog>
</template>

<script>
  import {
    showAuditWriteOff,
    auditWriteOff,
    validateAuditWriteOff
  } from '@a/salesManage/index'
  export default {
    props: {
      auditRedPunchVisible: {
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
          code: '',
          auditResult: '',
          reason: '',
          writeOffDate: ''
        },
        // 规则
        rules: {
          auditResult: { required: true, message: '请选择审核结果' },
          writeOffDate: { required: true, message: '请选择日期' }
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
              code: this.formData.code,
              reason: this.formData.reason,
              writeOffDate: this.formData.writeOffDate,
              auditResult: this.formData.auditResult
            }
            // 是否通过状态
            const isPass = this.formData.auditResult === '1'
            // 审核前校验
            const {
              data: { isRelSaleReturn, shelfIdepotItemList }
            } = await validateAuditWriteOff(params)
            console.log(isRelSaleReturn, shelfIdepotItemList)
            // 选择审核通过  和  此销售订单存在关联的销售退货订单，确定红冲吗？
            if (isRelSaleReturn && isPass) {
              const result = await this.$showToast(
                '此销售订单存在关联的销售退货订单，确定红冲吗？'
              )
              // 点击取消
              if (!result) {
                throw new Error('取消红冲')
              }
            }
            // 需要校验库存量
            if (shelfIdepotItemList?.length && isPass) {
              const { isInventoryValidate } = await this.$checkInventoryNum({
                itemList: shelfIdepotItemList.map((item) => {
                  return {
                    goodsId: item.goodsId,
                    depotId: this.data.inDepotId,
                    goodsNo: item.goodsNo,
                    okNum: item.normalNum,
                    badNum: item.damagedNum,
                    tallyingUnit: item.unit || '',
                    batchNo: item.batchNo || '',
                    productionDate: item.productionDate || '',
                    overdueDate: item.overdueDate || '',
                    inventoryType: 1
                  }
                })
              })
              // 库存校验不通过
              if (!isInventoryValidate) {
                return
              }
            }
            // 审核操作
            await auditWriteOff(params)
            this.$successMsg('操作成功')
            this.$emit('update:auditRedPunchVisible', { show: false })
            this.$emit('comfirm')
          } catch (err) {
            console.log(err)
          } finally {
            this.confirmBtnLoading = false
          }
        })
      }
    },
    async mounted() {
      const { id, code } = this.data
      const { data } = await showAuditWriteOff({ id })
      this.formData.code = code
      this.formData.reason = data
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep .el-form-item__content textarea {
    width: 300px;
    min-height: 80px !important;
  }
  ::v-deep .el-form-item__content .el-date-editor,
  ::v-deep .el-form-item__content input {
    width: 300px;
  }
</style>
