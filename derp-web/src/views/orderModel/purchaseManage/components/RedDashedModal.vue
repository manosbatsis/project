<template>
  <JFX-Dialog
    :title="type === 'audit' ? '审核红冲' : '申请红冲'"
    :width="'500px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    btnAlign="center"
    closeBtnText="取 消"
    @comfirm="comfirm"
  >
    <div v-loading="confirmBtnLoading" class="red-dashed-modal">
      <JFX-Form :model="ruleForm" :rules="rules" ref="form">
        <el-row :gutter="10">
          <el-col :span="24" v-if="type === 'apply'">
            <h3 style="text-align: center" class="mr-b-15">
              请确认是否申请红冲该单据？
            </h3>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="红冲单据："
              prop="code"
              v-if="type === 'audit'"
            >
              <el-input
                v-model.trim="data.code"
                clearable
                placeholder="请输入"
                :disabled="type === 'audit'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="红冲原因：" prop="reason" class="textarea-con">
              <el-input
                type="textarea"
                v-model="ruleForm.reason"
                placeholder="请输入红冲原因"
                maxlength="100"
                show-word-limit
                :rows="6"
                :disabled="type === 'audit'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              v-if="type === 'audit'"
              label="审核结果："
              prop="auditResult"
            >
              <el-radio-group v-model="ruleForm.auditResult">
                <el-radio label="1">通过</el-radio>
                <el-radio label="0">驳回</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              v-if="type === 'audit'"
              label="归属日期："
              prop="attributiveDate"
            >
              <el-date-picker
                v-model="ruleForm.attributiveDate"
                type="date"
                value-format="yyyy-MM-dd"
                style="width: 100%"
                placeholder="请选择归属日期"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { saveApplyWriteOff, saveAuditWriteOff } from '@a/purchaseManage'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      /* 操作类型 apply：申请红冲，audit：审核红冲 */
      type: {
        type: String,
        default: ''
      },
      data: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          reason: '',
          auditResult: '',
          attributiveDate: ''
        },
        /* 表单规则 */
        rules: {
          reason: {
            required: true,
            message: '红冲原因不能为空',
            trigger: 'blur'
          },
          auditResult: {
            required: true,
            message: '审核结果不能为空',
            trigger: 'change'
          },
          attributiveDate: {
            required: true,
            message: '归属日期不能为空',
            trigger: 'change'
          }
        },
        /* 按钮loading */
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { reason } = this.data
        this.ruleForm.reason = reason
      },
      async comfirm() {
        this.$refs.form.validate((valid) => {
          if (!valid) return false
          this.type === 'apply' ? this.onApply() : this.onAudit()
        })
      },
      /* 申请红冲 */
      async onApply() {
        const { id } = this.data
        const { reason } = this.ruleForm
        try {
          this.confirmBtnLoading = true
          await saveApplyWriteOff({ id, reason })
          this.$successMsg('申请红冲成功')
          this.$emit('update:isVisible', { show: false })
          this.$emit('comfirm')
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.confirmBtnLoading = false
        }
      },
      /* 审核红冲 */
      async onAudit() {
        const { id, itemDTOS } = this.data
        const { auditResult, attributiveDate } = this.ruleForm
        try {
          this.confirmBtnLoading = true
          /* 校验库存量 */
          if (this.ruleForm.auditResult === '1' && itemDTOS?.length) {
            const itemList = itemDTOS.map((item) => ({
              goodsId: item.goodsId,
              goodsNo: item.goodsNo,
              okNum: item.type === '0' ? item.num : 0,
              badNum: item.type === '1' ? item.num : 0,
              tallyingUnit: item.tallyingUnit || '',
              batchNo: item.batchNo || '',
              depotId: item.depotId || '',
              productionDate: item.productionDate || '',
              overdueDate: item.overdueDate || '',
              inventoryType: 1
            }))
            const { isInventoryValidate } = await this.$checkInventoryNum({
              itemList
            })
            if (!isInventoryValidate) return
          }
          await saveAuditWriteOff({ id, auditResult, attributiveDate })
          this.$successMsg('审核红冲成功')
          this.$emit('update:isVisible', { show: false })
          this.$emit('comfirm')
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.confirmBtnLoading = false
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .red-dashed-modal {
    margin-bottom: 20px;

    ::v-deep .el-form-item {
      display: flex;
      align-items: center;
      margin-bottom: 5px;
    }

    ::v-deep .el-form-item__label {
      padding: 0 12px 0 0;
      width: 110px;
      line-height: 40px;
      font-size: 14px;
      text-align: right;
      vertical-align: middle;
      box-sizing: border-box;
      color: #606266;
    }

    ::v-deep .el-form-item__content {
      flex: 1;
      margin-right: 40px;
    }

    ::v-deep.textarea-con {
      display: flex;
      align-items: flex-start;

      .el-form-item__label {
        width: 110px;
      }

      .el-form-item__content {
        flex: 1;
        margin-right: 40px;
      }
    }
  }
</style>
