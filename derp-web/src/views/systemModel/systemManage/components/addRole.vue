<template>
  <JFX-Dialog
    :visible.sync="addRoleVisible"
    :showClose="true"
    :width="'400px'"
    :title="copyId ? '复制角色' : '新增角色'"
    :btnAlign="'center'"
    :confirmBtnLoading="confirmBtnLoadingAdd"
    top="30vh"
    @comfirm="comfirm"
  >
    <JFX-Form
      class="addRoleForm"
      :model="addRuleForm"
      ref="addRuleForm"
      :rules="addRules"
      style="overflow: hidden"
      :label-position="'right'"
    >
      <el-row class="fs-14 clr-II mr-b-10">
        <el-form-item label="角色名称：" prop="name">
          <el-input
            v-model.trim="addRuleForm.name"
            clearable
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="备注：" prop="remark">
          <el-input
            v-model.trim="addRuleForm.remark"
            type="textarea"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </el-row>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { add } from '@a/systemManage/role/index'
  export default {
    mixins: [commomMix],
    props: {
      addRoleVisible: {
        default() {
          return { show: false }
        }
      },
      copyId: {
        default: ''
      }
    },
    data() {
      return {
        addRuleForm: {
          name: '',
          remark: ''
        },
        addRules: {
          name: {
            required: true,
            message: '请输入角色名称',
            trigger: 'blur'
          }
        },
        confirmBtnLoadingAdd: false
      }
    },
    methods: {
      // 点击确认
      comfirm() {
        this.$refs.addRuleForm.validate(async (valid) => {
          if (!valid) return false
          this.confirmBtnLoadingAdd = true
          try {
            const { data } = await add({ ...this.addRuleForm })
            console.log(data)
            this.$successMsg('操作成功')
            this.$emit('close', data)
          } catch (err) {
            console.log(err)
          }
          this.confirmBtnLoadingAdd = false
        })
      }
    }
  }
</script>
<style scoped lang="scss">
  ::v-deep.addRoleForm {
    .el-form-item__label {
      width: 100px;
    }
    .el-form-item__error {
      white-space: nowrap;
    }
  }
</style>
