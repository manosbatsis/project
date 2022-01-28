<template>
  <JFX-Dialog
    :visible.sync="addVisible"
    :showClose="true"
    :width="'500px'"
    :title="'新增品牌'"
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
        <el-form-item label="品牌名称：" prop="name">
          <el-input
            v-model.trim="addRuleForm.name"
            clearable
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </el-row>
      <el-row class="fs-14 clr-II mr-b-10">
        <el-form-item label="标准品牌：" prop="brandParentId">
          <el-select
            v-model="addRuleForm.brandParentId"
            placeholder="请选择"
            clearable
            filterable
            :data-list="getBrandParent('brandList', { type: 1 })"
          >
            <el-option
              v-for="item in selectOpt.brandList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-row>
      <el-row class="fs-14 clr-II mr-b-10">
        <el-form-item label="中文品牌名称：" prop="chinaBrandName">
          <el-input
            v-model.trim="addRuleForm.chinaBrandName"
            clearable
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </el-row>
      <el-row class="fs-14 clr-II mr-b-10">
        <el-form-item label="英文品牌名称：" prop="englishBrandName">
          <el-input
            v-model.trim="addRuleForm.englishBrandName"
            clearable
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </el-row>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { brandSave } from '@a/brandManage/index'
  export default {
    mixins: [commomMix],
    props: {
      addVisible: {
        default() {
          return { show: false }
        }
      }
    },
    data() {
      return {
        addRuleForm: {
          name: '',
          brandParentId: '',
          chinaBrandName: '',
          englishBrandName: ''
        },
        addRules: {
          name: {
            required: true,
            message: '请输入名称',
            trigger: 'blur'
          },
          brandParentId: {
            required: true,
            message: '请选择品牌',
            trigger: 'change'
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
            await brandSave({ ...this.addRuleForm })
            this.$successMsg('操作成功')
            this.$emit('close')
            this.visible.show = false
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
      width: 120px;
    }
    .el-form-item__error {
      white-space: nowrap;
    }
  }
</style>
