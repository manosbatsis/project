<template>
  <!-- 编辑弹窗 -->
  <JFX-Dialog
    :title="title"
    closeBtnText="取 消"
    :width="'600px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="showDialog"
    :confirmBtnLoading="confirmBtnLoading"
    @comfirm="comfirm"
  >
    <JFX-Form :model="ruleForm" :rules="rules" ref="form" class="edit-bx">
      <el-row :gutter="10" class="page-view">
        <el-col :span="24">
          <el-form-item label="事业部编码：" prop="code">
            <el-input
              v-model.trim="ruleForm.code"
              placeholder="请输入"
              clearable
              :disabled="!!id"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="事业部名称：" prop="name">
            <el-input
              v-model.trim="ruleForm.name"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="部门名称：" prop="departmentId">
            <el-select
              v-model="ruleForm.departmentId"
              placeholder="请选择"
              clearable
              style="width: 100%"
            >
              <el-option
                v-for="item in departmentNameList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import {
    saveBusinessUnit,
    detailBusinessUnits,
    getDepartSelectBean
  } from '@a/companyFile'
  export default {
    mixins: [commomMix],
    props: {
      showDialog: {
        type: Object,
        default: () => {}
      },
      title: {
        type: String,
        default: ''
      },
      id: {
        type: String | Number,
        default: ''
      }
    },
    data() {
      return {
        ruleForm: {
          code: '',
          name: '',
          departmentId: ''
        },
        rules: {
          code: {
            required: true,
            message: '请输入事业部编码',
            trigger: 'blur'
          },
          name: {
            required: true,
            message: '请输入事业部名称',
            trigger: 'blur'
          },
          departmentId: {
            required: true,
            message: '请选择部门',
            trigger: 'change'
          }
        },
        confirmBtnLoading: false,
        departmentNameList: []
      }
    },
    mounted() {
      this.getDepartSelectBean()
      this.init()
    },
    methods: {
      async init() {
        const { id, ruleForm } = this
        if (!id) return false
        const { data } = await detailBusinessUnits({ id })
        for (const key in ruleForm) {
          ruleForm[key] = data[key] ? data[key].toString() : data[key]
        }
      },
      async getDepartSelectBean() {
        const { data } = await getDepartSelectBean()
        this.departmentNameList = data || []
      },
      // 提交表单
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (valid) {
            try {
              this.confirmBtnLoading = true
              const { id } = this
              const target = this.departmentNameList.find(
                (item) => item.value === this.ruleForm.departmentId
              )
              const departmentName = target ? target.label : ''
              await saveBusinessUnit({ ...this.ruleForm, departmentName, id })
              this.$emit('comfirm')
              this.$successMsg('操作成功')
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.confirmBtnLoading = false
            }
          }
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .page-view {
    width: 100%;
    margin-bottom: 10px;
  }
  ::v-deep.edit-bx .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 120px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  ::v-deep.edit-bx .el-form-item__content {
    flex: 1;
    margin-right: 20px;
  }
</style>
