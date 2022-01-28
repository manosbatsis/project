<template>
  <!-- 修改PO组件 -->
  <JFX-Dialog
    title="修改PO"
    class="change-po"
    :width="'450px'"
    :top="'20vh'"
    :showClose="true"
    :visible.sync="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    @comfirm="comfirm"
  >
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-form-item label="PO号：" prop="poNo">
        <el-input
          v-model.trim="ruleForm.poNo"
          clearable
          style="width: 120%"
          placeholder="请输入PO号"
        />
      </el-form-item>
      <el-form-item label="原因：" prop="remark">
        <el-input
          v-model.trim="ruleForm.remark"
          clearable
          style="width: 120%"
          placeholder="请输入原因"
        />
      </el-form-item>
    </JFX-Form>
    <div class="fs-12 clr-r mr-t-20 mr-b-20" style="text-align: center">
      （备注：输入时仅限中文、大小写字母、数字和“-”这4种字符。）
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { modifyPoNo, checkExistByPo } from '@a/salesManage/index'
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
        /* 表单数据 */
        ruleForm: {
          poNo: '',
          remark: ''
        },
        /* 表单校验规则 */
        rules: {
          poNo: [
            {
              required: true,
              message: 'PO号不能为空',
              trigger: 'blur'
            },
            {
              pattern: /^[A-Za-z0-9\u4e00-\u9fa5-]+$/,
              message: '只能输入中文、字母、数字和“-”',
              trigger: ['change', 'blur']
            }
          ]
        },
        /* 保存按钮loading */
        confirmBtnLoading: false
      }
    },
    mounted() {
      console.log(this.isVisible)
      const { poNo } = this.data
      this.ruleForm.poNo = poNo || ''
    },
    methods: {
      /* 保存 */
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          const { orderId } = this.data
          try {
            this.confirmBtnLoading = true
            // 校验po号
            const { data } = await checkExistByPo({
              orderId,
              poNo: this.ruleForm.poNo
            })
            if (data?.length) {
              return this.$errorMsg(`PO号：${data[0]}已有存在销售订单信息`)
            }
            await modifyPoNo({
              ...this.ruleForm,
              orderId
            })
            this.$successMsg('操作成功')
            this.$emit('comfirm')
            this.$emit('update:isVisible', { show: false })
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

<style scoped lang="scss">
  ::v-deep.change-po {
    .el-form-item__label {
      width: 100px;
    }
    .el-form-item__error {
      white-space: nowrap;
    }
  }
</style>
