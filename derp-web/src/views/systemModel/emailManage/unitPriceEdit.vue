<template>
  <div class="page-bx bgc-w edit-box">
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 面包屑 -->
      <JFX-Breadcrumb :showGoback="true" />
      <!-- 面包屑 end -->
      <!-- 单价预警配置 -->
      <JFX-title title="单价预警配置" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司：" prop="merchantId">
            <el-select
              v-model="ruleForm.merchantId"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectMerchantBean('merchantList')"
            >
              <el-option
                v-for="item in selectOpt.merchantList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="事业部：" prop="buId" ref="buId">
            <el-select
              v-model="ruleForm.buId"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getBUSelectBean('bu_list')"
            >
              <el-option
                v-for="item in selectOpt.bu_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="波动范围：" prop="waveRange">
            <JFX-Input
              v-model.trim="ruleForm.waveRange"
              :precision="3"
              :min="0"
              placeholder="请输入"
              style="width: 100%"
              clearable
            >
              <template slot="append">%</template>
            </JFX-Input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 单价预警配置 end -->
      <!-- 邮件提醒设置 -->
      <JFX-title title="邮件提醒设置" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :span="24">
          <el-form-item label="邮件主题：" prop="emailTitle">
            <el-input
              v-model="ruleForm.emailTitle"
              clearable
              placeholder="请输入"
              style="width: 700px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10" class="fs-14 clr-II email-container">
        <el-col class="mr-b-0" :span="24">
          <el-form-item label="收件人邮箱：">
            <el-form-item>
              <el-button
                type="primary"
                @click="selectUsers.visible.show = true"
              >
                选择用户
              </el-button>
            </el-form-item>
            <el-form-item prop="receiverEmail">
              <el-input
                type="textarea"
                :rows="8"
                v-model="ruleForm.receiverEmail"
                clearable
                style="width: 700px"
              />
            </el-form-item>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 邮件提醒设置 end -->
      <!-- 选择用户组件 -->
      <SelectUsers
        v-if="selectUsers.visible.show"
        :isVisible.sync="selectUsers.visible"
        @comfirm="comfirmSeclectUsers"
      />
      <!-- 选择用户组件 end -->
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="onSave">保 存</el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    modifySettlementPriceWarnconfig,
    saveSettlementPriceWarnconfig,
    settlementPriceWarnconfigDetail
  } from '@a/emailManage'
  import SelectUsers from './components/SelectUsers.vue'
  export default {
    mixins: [commomMix],
    components: {
      SelectUsers
    },
    data() {
      return {
        ruleForm: {
          merchantId: '',
          buId: '',
          waveRange: '',
          emailTitle: '',
          receiverEmail: '',
          receiverId: '',
          receiverName: ''
        },
        rules: {
          merchantId: {
            required: true,
            message: '公司不能为空',
            trigger: 'change'
          },
          buId: {
            required: true,
            message: '事业部不能为空',
            trigger: 'change'
          },
          waveRange: {
            required: true,
            message: '波动范围不能为空',
            trigger: 'blur'
          },
          emailTitle: {
            required: true,
            message: '邮件主题不能为空',
            trigger: 'blur'
          },
          receiverEmail: {
            required: true,
            message: '收件人邮箱不能为空',
            trigger: 'blur'
          }
        },
        selectUsers: {
          visible: {
            show: false
          }
        }
      }
    },
    mounted() {
      const { id } = this.$route.query
      if (id) this.init(id)
    },
    methods: {
      async init(id) {
        try {
          const {
            data: { detail }
          } = await settlementPriceWarnconfigDetail({ id })
          for (const key in this.ruleForm) {
            this.ruleForm[key] = ![null, undefined].includes(detail[key])
              ? detail[key] + ''
              : ''
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 确认选择用户
      comfirmSeclectUsers(payload) {
        if (payload && payload.length) {
          this.ruleForm.receiverEmail = payload
            .map((item) => item.email)
            .join(';')
          this.ruleForm.receiverName = payload
            .map((item) => item.name)
            .join(';')
          this.ruleForm.receiverId = payload
            .map((item) => item.userId)
            .join(';')
        }
      },
      // 保存
      onSave() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请正确填写表单信息')
            return false
          }
          const { id } = this.$route.query
          const params = { ...this.ruleForm, id }
          try {
            id
              ? await modifySettlementPriceWarnconfig(params)
              : await saveSettlementPriceWarnconfig(params)
            this.$successMsg(id ? '修改成功' : '保存成功')
            this.closeTag()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.edit-box .el-form-item__label {
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
  ::v-deep.edit-box .el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  ::v-deep.email-container .el-form-item {
    align-items: flex-start;
  }
</style>
