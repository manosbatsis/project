<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" :breadcrumb="breadcrumb" />
    <!-- 面包屑 end -->
    <el-tabs v-model="activeName">
      <el-tab-pane label="应收汇总" name="first">
        <receive-summary-tab v-if="id" :detail="detail"></receive-summary-tab>
      </el-tab-pane>
      <el-tab-pane label="应收详情" name="second">
        <receive-detail-tab v-if="id" :detail="detail"></receive-detail-tab>
      </el-tab-pane>
      <el-tab-pane label="附件审批记录" name="third">
        <audit-record-tab v-if="id" :detail="detail"></audit-record-tab>
      </el-tab-pane>
      <el-tab-pane label="核销记录" name="fourth">
        <writeoff-record-tab v-if="id" :detail="detail"></writeoff-record-tab>
      </el-tab-pane>
    </el-tabs>
    <JFX-Form
      :model="ruleForm"
      ref="ruleForm"
      :rules="rules"
      v-if="type === 'audit'"
      class="audtiForm"
    >
      <el-row :gutter="10" class="fs-14 clr-II mr-t-20">
        <el-col :span="24">
          <el-form-item label="审核结果：" prop="auditResult">
            <el-radio-group
              v-model="ruleForm.auditResult"
              @change="auditResultChange"
            >
              <el-radio label="00">审核通过</el-radio>
              <el-radio label="01">驳回</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="入账月份："
            prop="billInDate"
            v-if="
              detail.tocSettlementReceiveBillDTO &&
              detail.tocSettlementReceiveBillDTO.billStatus !== '05'
            "
          >
            <el-date-picker
              v-model="ruleForm.billInDate"
              :clearable="false"
              :format="'yyyy-MM-dd'"
              :value-format="'yyyy-MM-dd'"
              type="date"
              placeholder="选择日期时间"
              :pickerOptions="{
                disabledDate(time) {
                  return time.getTime() > Date.now()
                }
              }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审核备注：" prop="auditRemark">
            <el-input
              v-model="ruleForm.auditRemark"
              type="textarea"
              :rows="4"
              style="width: 500px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <div class="mr-t-20 flex-c-c" v-if="type === 'edit'">
      <el-button
        type="primary"
        @click="
          linkTo(
            '/receivemoney/repairDeduction?id=' +
              id +
              '&code=' +
              code +
              '&customerId=' +
              customerId
          )
        "
      >
        添加补扣款
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
      <el-button type="primary" @click="save" :loading="saveLoading">
        提交
      </el-button>
    </div>
    <div class="mr-t-20 flex-c-c" v-if="type === 'audit'">
      <el-button type="primary" :loading="saveLoading" @click="audit">
        审核
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import {
    toCReceiveBillDetail,
    submitReceiveBill,
    auditItem
  } from '@a/receiveMoneyManage/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    components: {
      receiveSummaryTab: () => import('./components/receiveSummaryTab'),
      receiveDetailTab: () => import('./components/receiveDetailTab'),
      writeoffRecordTab: () => import('./components/writeoffRecordTab'),
      auditRecordTab: () => import('./components/auditRecordTab')
    },
    data() {
      return {
        activeName: 'first',
        detail: {},
        id: '',
        type: 'detail',
        code: '',
        customerId: '',
        breadcrumb: [
          {
            path: '',
            meta: { title: '收款管理' }
          },
          {
            path: '',
            meta: { title: '应收详情' }
          }
        ],
        saveLoading: false,
        ruleForm: {
          auditResult: '',
          auditRemark: '',
          billInDate: ''
        },
        rules: {
          auditResult: {
            required: true,
            message: '审核结果不能为空',
            trigger: 'change'
          },
          billInDate: {
            required: true,
            message: '请选择入账月份',
            trigger: 'change'
          },
          auditRemark: {
            required: true,
            message: '审核备注不能为空',
            trigger: 'change'
          }
        }
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { query } = this.$route
        if (!query.id) return false
        if (query.type) {
          this.type = query.type
          this.breadcrumb.splice(1, 1, {
            path: '',
            meta: { title: query.type === 'edit' ? '应收编辑' : '应收审核' }
          })
        }
        try {
          const res = await toCReceiveBillDetail({ id: query.id })
          this.detail = res.data
          this.id = query.id
          this.code = res.data.tocSettlementReceiveBillDTO.code || ''
          this.customerId =
            res.data.tocSettlementReceiveBillDTO.customerId || ''
        } catch (err) {}
      },
      // 审核结果变化，入账日期变化
      auditResultChange() {
        this.rules.billInDate.required = this.ruleForm.auditResult === '00'
      },
      // 提交
      async save() {
        this.saveLoading = true
        try {
          await submitReceiveBill({ id: this.id })
          this.$successMsg('操作成功')
          this.closeTag()
        } catch (error) {
          console.log(error)
        }
        this.saveLoading = false
      },
      // 审核
      async audit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          this.saveLoading = true
          try {
            await auditItem({ billId: this.id, ...this.ruleForm })
            this.$successMsg('操作成功')
            this.closeTag()
          } catch (error) {
            console.log(error)
          }
          this.saveLoading = false
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .audtiForm ::v-deep.el-form-item__label {
    width: 130px;
  }
</style>
