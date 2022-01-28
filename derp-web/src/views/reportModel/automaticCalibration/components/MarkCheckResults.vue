<template>
  <!-- 标识核对结果 -->
  <JFX-Dialog
    title="标识核对结果"
    closeBtnText="取 消"
    :width="'700px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    @comfirm="comfirm"
  >
    <JFX-Form :model="ruleForm" :rules="rules" ref="form" class="edit-bx">
      <el-row :gutter="10" class="page-view">
        <el-col :span="24">
          <el-form-item label="核对结果：" prop="checkResult">
            <el-radio v-model="ruleForm.checkResult" label="0">未对平</el-radio>
            <el-radio v-model="ruleForm.checkResult" label="1">已对平</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="核对备注：" prop="remark">
            <el-input
              type="textarea"
              :rows="2"
              style="width: 500px"
              v-model="ruleForm.remark"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import { modifyTaskCheckResult } from '@a/reportManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      data: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        ruleForm: {
          checkResult: '',
          remark: ''
        },
        rules: {
          checkResult: {
            required: true,
            message: '请选择核对结果',
            trigger: 'change'
          }
        },
        ids: '',
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        const { ids } = this.data
        this.ids = ids
      },
      // 提交表单
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请正确填写表单信息')
            return false
          }
          try {
            await modifyTaskCheckResult({ ids: this.ids, ...this.ruleForm })
            this.$successMsg('操作成功')
            this.$emit('update:isVisible', { show: false })
            this.$emit('comfirm')
          } catch (error) {
            this.$errorMsg(error.message)
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
</style>
