<template>
  <!-- 编辑费项配置组件 -->
  <JFX-Dialog
    :title="dialogTitle"
    closeBtnText="取 消"
    :width="'800px'"
    :top="'80px'"
    :showClose="true"
    :loading="dialogLoading"
    :visible.sync="showDialog"
    @comfirm="comfirm"
    :confirmBtnLoading="confirmBtnLoading"
  >
    <JFX-Form :model="ruleForm" :rules="rules" ref="form" class="edit-bx">
      <el-row :gutter="10" class="page-view">
        <el-col :span="12">
          <el-form-item label="所属层级：" prop="level">
            <el-select
              v-model="ruleForm.level"
              placeholder="请选择"
              clearable
              @change="levelChange"
              :data-list="getSelectList('settlementConfig_levelList')"
            >
              <el-option
                v-for="item in selectOpt.settlementConfig_levelList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上级费项：" prop="parentProjectId">
            <el-select
              v-model="ruleForm.parentProjectId"
              placeholder="请选择"
              clearable
              @change="parentProjectNameChange"
              :disabled="ruleForm.level === '1'"
            >
              <el-option
                v-for="item in parentProjectNameSelectList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="本级费项名称：" prop="projectName">
            <el-input
              v-model="ruleForm.projectName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="NC收支费项：" prop="receivePaymentSubject">
            <el-select
              v-model="ruleForm.receivePaymentSubject"
              placeholder="请选择"
              clearable
            >
              <el-option
                v-for="item in ncSelectList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主数据编码：" prop="inExpCode">
            <el-input
              v-model="ruleForm.inExpCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="适用模块：" prop="moduleType">
            <el-select
              v-model="ruleForm.moduleType"
              placeholder="请选择"
              clearable
              multiple
              collapse-tags
              :data-list="getSelectList('settlementConfig_moduleTypeList')"
            >
              <el-option
                v-for="item in selectOpt.settlementConfig_moduleTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="适用类型：" prop="type">
            <el-select
              v-model="ruleForm.type"
              placeholder="请选择"
              clearable
              multiple
              collapse-tags
              :data-list="getSelectList('settlementConfig_typeList')"
            >
              <el-option
                v-for="item in selectOpt.settlementConfig_typeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="适用客户：" prop="customerId">
            <el-radio
              @change="suitableCustomerChange"
              v-model="ruleForm.suitableCustomer"
              label="1"
              >通用</el-radio
            >
            <el-radio
              @change="suitableCustomerChange"
              v-model="ruleForm.suitableCustomer"
              label="2"
            >
              指定客户
            </el-radio>
            <el-select
              v-model="ruleForm.customerId"
              placeholder="请选择"
              clearable
              multiple
              collapse-tags
              :data-list="getCustomerSelectBean('customerList')"
            >
              <el-option
                v-for="item in selectOpt.customerList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
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
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { getNCSelectBean } from '@a/rebateManage/index.js'
  import {
    parentProjectNameList,
    tab1SaveSettlementConfig,
    toDetail,
    tab1ModifySettlementConfig
  } from '@a/paymentSubjectManage'
  export default {
    mixins: [commomMix],
    props: {
      showDialog: { show: false },
      dialogTitle: {
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
        parentProjectNameSelectList: [], // 上级费项列表
        ncSelectList: [], // nc
        confirmBtnLoading: false, // 确认按钮loading
        dialogLoading: false, // 弹框loading
        ruleForm: {
          level: '',
          parentProjectId: '',
          projectName: '',
          receivePaymentSubject: '',
          inExpCode: '',
          type: [],
          suitableCustomer: '1',
          customerId: [],
          status: '1',
          moduleType: []
        },
        rules: {
          level: [{ required: true, message: '请选择', trigger: 'blur' }],
          parentProjectId: [
            { required: true, message: '请选择', trigger: 'blur' }
          ],
          projectName: [{ required: true, message: '请输入', trigger: 'blur' }],
          receivePaymentSubject: [
            { required: true, message: '请选择', trigger: 'blur' }
          ],
          type: [{ required: true, message: '请选择', trigger: 'blur' }],
          customerId: [{ required: false, message: '请选择', trigger: 'blur' }],
          status: [{ required: true, message: '请选择', trigger: 'blur' }],
          moduleType: [{ required: true, message: '请选择', trigger: 'blur' }]
        }
      }
    },
    methods: {
      // 所属层级改变
      levelChange() {
        this.rules.parentProjectId[0].required = this.ruleForm.level !== '1'
      },
      // 上级费项改变, 默认带出对应的nc收支费项
      async parentProjectNameChange() {
        // 存在nc收支费项,不执行
        if (this.ruleForm.receivePaymentSubject) {
          return
        }
        const { data: ncData } = await getNCSelectBean({
          id: this.ruleForm.parentProjectId
        })
        const selectId = ncData[0]?.value
        // 上级费用项目有对应的nc收支费项目,赋值
        if (selectId) {
          this.ruleForm.receivePaymentSubject = selectId
        }
      },
      // 适用客户改变
      suitableCustomerChange() {
        this.rules.customerId[0].required =
          this.ruleForm.suitableCustomer === '2'
      },
      // 获取详情
      getDetail(data) {
        console.log(data)
        function S(value) {
          return value ? String(value) : ''
        }
        this.ruleForm.level = S(data.level)
        this.ruleForm.parentProjectId = S(data.parentId)
        this.ruleForm.projectName = S(data.projectName)
        this.ruleForm.receivePaymentSubject = S(data.paymentSubjectId)
        this.ruleForm.inExpCode = data.inExpCode
        this.ruleForm.type = data.type
          ? data.type.split(',').filter((item) => !!item)
          : []
        this.ruleForm.suitableCustomer = data.suitableCustomer
        this.ruleForm.customerId = data.customerIdsStr
          ? data.customerIdsStr.split(',').filter((item) => !!item)
          : []
        this.ruleForm.status = S(data.status)
        this.ruleForm.moduleType = data.moduleType
          ? data.moduleType.split(',').filter((item) => !!item)
          : []
      },
      // 提交表单
      comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) {
            return
          }
          try {
            this.confirmBtnLoading = true
            var json = []
            json.push({
              inExpCode: this.ruleForm.inExpCode,
              level: this.ruleForm.level,
              parentProjectId: this.ruleForm.parentProjectId,
              parentProjectNameText: this.ruleForm.parentProjectId
                ? this.parentProjectNameSelectList.find(
                    (item) => +item.key === +this.ruleForm.parentProjectId
                  ).value
                : '',
              projectName: this.ruleForm.projectName,
              receivePaymentSubjectId: this.ruleForm.receivePaymentSubject,
              receivePaymentSubjectText: this.ncSelectList.find(
                (item) => +item.value === +this.ruleForm.receivePaymentSubject
              ).label,
              suitableCustomer: this.ruleForm.suitableCustomer,
              status: this.ruleForm.status,
              customerIdStr: this.ruleForm.customerId,
              type: this.ruleForm.type.toString(),
              module: this.ruleForm.moduleType.toString()
            })
            console.log(json)
            if (this.id) {
              json[0].id = this.id
              json[0].oldProjectName = this.detail.projectName
              await tab1ModifySettlementConfig({
                json: JSON.stringify(json),
                settlementConfigId: this.id
              })
            } else {
              await tab1SaveSettlementConfig({
                json: JSON.stringify(json)
              })
            }
            this.$successMsg('操作成功')
            this.$emit('close')
          } catch (err) {
            console.log(err)
          } finally {
            this.confirmBtnLoading = false
          }
        })
      }
    },
    async mounted() {
      this.dialogLoading = true
      // 上级费项下拉
      const { data } = await parentProjectNameList()
      this.parentProjectNameSelectList = data.map((item) => {
        return {
          key: String(item.id),
          value: String(item.projectName)
        }
      })
      // nc收支费项下拉
      const { data: ncData } = await getNCSelectBean()
      this.ncSelectList = ncData.map(({ value, label }) => {
        return {
          value: String(value),
          label: String(label)
        }
      })
      // 详情
      if (this.id) {
        const { data } = await toDetail({ id: this.id })
        this.getDetail(data)
        this.detail = data
      }
      // 联动
      this.suitableCustomerChange()
      this.levelChange()
      this.dialogLoading = false
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
