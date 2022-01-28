<template>
  <!-- 编辑平台费用映射件 -->
  <JFX-Dialog
    :title="title"
    :width="'800px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    closeBtnText="取 消"
    @comfirm="comfirm"
  >
    <div v-loading="pageLoading">
      <JFX-Form :model="ruleForm" :rules="rules" ref="ruleForm" class="edit-bx">
        <el-row :gutter="10" class="page-view">
          <el-col :span="12">
            <el-form-item label="平台名称：" prop="storePlatformCode">
              <el-select
                v-model="ruleForm.storePlatformCode"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('storePlatformList')"
              >
                <el-option
                  v-for="item in selectOpt.storePlatformList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="平台费项名称：" prop="name">
              <el-input
                v-model="ruleForm.name"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="本级费项名称：" prop="settlementConfigId">
              <el-select
                v-model="ruleForm.settlementConfigId"
                placeholder="请选择"
                filterable
                clearable
                @change="settlementConfigIdChange"
              >
                <el-option
                  v-for="item in settlementConfigList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="NC收支费项：" prop="ncPaymentId">
              <el-select
                v-model="ruleForm.ncPaymentId"
                placeholder="请选择"
                clearable
                filterable
                disabled
              >
                <el-option
                  v-for="item in receivePaymentSubjectList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态：" prop="status">
              <el-radio v-model="ruleForm.status" label="1">启用</el-radio>
              <el-radio v-model="ruleForm.status" label="0">禁用</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import {
    saveSettlementConfig,
    modifySettlementConfig,
    platformSettlementConfigDetail,
    platformSettlementSelectList,
    getSettlementConfigDetail
  } from '@a/paymentSubjectManage'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      /* 类型 add：新增 edit：编辑 */
      type: {
        type: String,
        default: 'add'
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
          storePlatformCode: '',
          name: '',
          settlementConfigId: '',
          ncPaymentId: '',
          status: '1'
        },
        /* 表单校验 */
        rules: {
          storePlatformCode: {
            required: true,
            message: '平台名称不能为空',
            trigger: 'change'
          },
          name: {
            required: true,
            message: '平台费项名称不能为空',
            trigger: 'blur'
          },
          settlementConfigId: {
            required: true,
            message: '本级费项名称不能为空',
            trigger: 'change'
          },
          status: {
            required: true,
            message: '状态不能为空',
            trigger: 'change'
          }
        },
        /* 标题 */
        title: '',
        /* 本级费项名称列表 */
        settlementConfigList: [],
        /* NC收支费项列表 */
        receivePaymentSubjectList: [],
        /* 确认按钮loading */
        confirmBtnLoading: false,
        /* 页面loading */
        pageLoading: false,
        /* 保存旧的平台名称 */
        oldName: ''
      }
    },
    mounted() {
      this.mapSelectListToPage()
      this.init()
    },
    methods: {
      async init() {
        const {
          type,
          data: { id }
        } = this
        this.title = type === 'add' ? '新增平台费用映射' : '编辑平台费用映射'
        if (!id) {
          this.$nextTick(() => {
            this.$refs.ruleForm.resetFields()
          })
          return false
        }
        try {
          this.pageLoading = true
          const { data } = await platformSettlementConfigDetail({ id })
          for (const key in this.ruleForm) {
            this.ruleForm[key] = ![null, undefined].includes(data[key])
              ? data[key] + ''
              : ''
          }
          /* 保存旧的平台名称 */
          this.oldName = this.ruleForm.name || ''
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.pageLoading = false
        }
      },
      /* 获取下拉列表 */
      async mapSelectListToPage() {
        try {
          const {
            data: { settlementConfigList, receivePaymentSubjectList }
          } = await platformSettlementSelectList()
          if (receivePaymentSubjectList && receivePaymentSubjectList.length) {
            this.receivePaymentSubjectList = receivePaymentSubjectList.map(
              ({ id, name }) => ({
                label: name,
                value: id + ''
              })
            )
          }
          if (settlementConfigList && settlementConfigList.length) {
            this.settlementConfigList = settlementConfigList.map(
              ({ id, projectName }) => ({
                label: projectName,
                value: id + ''
              })
            )
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 提交表单
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请先正确完成表单信息')
            return false
          }
          const {
            data: { id }
          } = this
          const selectListName = this.getSelectListName()
          const params = {
            ...this.ruleForm,
            ...selectListName,
            id
          }
          try {
            this.confirmBtnLoading = true
            const {
              data: { code, message }
            } = id
              ? await modifySettlementConfig(params)
              : await saveSettlementConfig(params)
            if (code !== '00') {
              this.$errorMsg(message || '系统异常')
            } else {
              this.$successMsg(id ? '编辑成功' : '保存成功')
              this.$emit('update:isVisible', { show: false })
              this.$emit('close')
            }
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.confirmBtnLoading = false
          }
        })
      },
      getSelectListName() {
        const { oldName } = this
        const storePlatformName = (
          this.selectOpt.storePlatformList.find(
            (item) => item.key === this.ruleForm.storePlatformCode
          ) || {}
        ).value
        const settlementConfigName = (
          this.settlementConfigList.find(
            (item) => item.value === this.ruleForm.settlementConfigId
          ) || {}
        ).label
        const ncPaymentName = (
          this.receivePaymentSubjectList.find(
            (item) => item.value === this.ruleForm.ncPaymentId
          ) || {}
        ).label
        return {
          oldName,
          storePlatformName,
          settlementConfigName,
          ncPaymentName
        }
      },
      async settlementConfigIdChange(id) {
        try {
          const {
            data: { paymentSubjectId }
          } = await getSettlementConfigDetail({ id })
          this.ruleForm.ncPaymentId = paymentSubjectId
            ? paymentSubjectId + ''
            : ''
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
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
    width: 130px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
