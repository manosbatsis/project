<template>
  <!-- 事业部库位类型新增、编辑弹窗 -->
  <JFX-Dialog
    :title="'新增'"
    :width="'500px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    btnAlign="center"
    closeBtnText="取 消"
    @comfirm="comfirm"
  >
    <div v-loading="confirmBtnLoading" class="edit-company-business">
      <JFX-Form :model="ruleForm" :rules="rules" ref="form">
        <el-row :gutter="10" class="page-view">
          <el-col :span="24">
            <el-form-item label="事业部：" prop="buId">
              <el-select
                v-model="ruleForm.buId"
                placeholder="请选择"
                clearable
                filterable
                style="width: 100%"
                :data-list="getBUSelectBean('businessList')"
              >
                <el-option
                  v-for="item in selectOpt.businessList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="库位类型：" prop="name">
              <el-input
                v-model="ruleForm.name"
                clearable
                style="width: 100%"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { addStockLocationType } from '@a/stockLocation'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          buId: '',
          name: ''
        },
        /* 表单规则 */
        rules: {
          buId: {
            required: true,
            message: '请选择事业部',
            trigger: 'change'
          },
          name: {
            required: true,
            message: '请输入库位类型',
            trigger: 'change'
          }
        },
        /* 按钮loading */
        confirmBtnLoading: false
      }
    },
    methods: {
      // 提交表单
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请填写表单必填项')
            return false
          }
          try {
            this.confirmBtnLoading = true
            await addStockLocationType({ ...this.ruleForm })
            this.$emit('update:isVisible', { show: false })
            this.$emit('comfirm')
            this.$successMsg('新增成功')
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.confirmBtnLoading = false
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep .edit-company-business {
    margin-bottom: 20px;

    .el-form-item {
      display: flex;
      align-items: center;
      margin-bottom: 5px;
    }

    .el-form-item__label {
      padding: 0 12px 0 0;
      width: 140px;
      line-height: 40px;
      font-size: 14px;
      text-align: right;
      vertical-align: middle;
      box-sizing: border-box;
      color: #606266;
    }

    .el-form-item__content {
      flex: 1;
      margin-right: 40px;
    }
  }
</style>
