<template>
  <!-- 事业部库位类型新增、编辑弹窗 -->
  <JFX-Dialog
    :title="data.id ? '编辑' : '新增'"
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
            <el-form-item label="资金占用费率：" prop="occupationRate">
              <JFX-Input
                v-model="ruleForm.occupationRate"
                clearable
                style="width: 100%"
                :precision="2"
              >
                <template slot="append">%</template>
              </JFX-Input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="生效时间：" prop="effectiveDate">
              <el-date-picker
                style="width: 100%"
                :format="'yyyy-MM-dd HH:mm:ss'"
                :value-format="'yyyy-MM-dd HH:mm:ss'"
                v-model="ruleForm.effectiveDate"
                type="datetime"
                placeholder="选择生效时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="失效时间：" prop="expirationDate">
              <el-date-picker
                style="width: 100%"
                :format="'yyyy-MM-dd HH:mm:ss'"
                :value-format="'yyyy-MM-dd HH:mm:ss'"
                v-model="ruleForm.expirationDate"
                type="datetime"
                placeholder="选择失效时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { saveOccuptionRateCongfig } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      data: {
        type: Object,
        default: () => {
          return { id: '' }
        }
      }
    },
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          buId: '',
          occupationRate: '',
          effectiveDate: '',
          expirationDate: ''
        },
        /* 表单规则 */
        rules: {
          buId: {
            required: true,
            message: '请选择事业部',
            trigger: 'change'
          },
          occupationRate: {
            required: true,
            message: '请完善数据',
            trigger: 'change'
          },
          effectiveDate: {
            required: true,
            message: '请完善数据',
            trigger: 'change'
          },
          expirationDate: {
            required: true,
            message: '请完善数据',
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
      init() {
        if (!this.data.id) return
        Object.keys(this.ruleForm).forEach((key) => {
          const value = this.data[key]
          this.ruleForm[key] = String(value || '')
        })
      },
      // 提交表单
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请完善数据')
            return false
          }
          const { id } = this.data
          try {
            this.confirmBtnLoading = true
            const param = { ...this.ruleForm }
            id && (param.id = id)
            await saveOccuptionRateCongfig({ ...this.ruleForm, id })
            this.$emit('update:isVisible', { show: false })
            this.$emit('comfirm')
            this.$successMsg(id ? '编辑成功' : '新增成功')
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
