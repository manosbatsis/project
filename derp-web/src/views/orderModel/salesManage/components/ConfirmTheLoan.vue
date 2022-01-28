<template>
  <!-- 确认放款 -->
  <JFX-Dialog
    title="确认放款"
    closeBtnText="取 消"
    :width="'600px'"
    :top="'150px'"
    :showClose="true"
    :btnAlign="'center'"
    :visible="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    confirmBtnText="确认放款"
    @comfirm="comfirm"
    :beforeClose="beforeClose"
  >
    <JFX-Form
      :model="ruleForm"
      ref="ruleForm"
      :rules="rules"
      class="form__container"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item label="赊销金额：">
            {{ detail.currency }} {{ detail.creditAmount || 0 }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="实收保证金：">
            {{ detail.currency }} {{ detail.actualMarginAmount || 0 }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="垫付金额：">
            {{ detail.currency }}
            {{
              (
                (+detail.creditAmount || 0) - (+detail.actualMarginAmount || 0)
              ).toFixed(2) || 0
            }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="放款日期：" prop="loanDate">
            <el-date-picker
              v-model="ruleForm.loanDate"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择放款日期"
              clearable
              @change="loanDateChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="起息日期：" prop="valueDate">
            <el-date-picker
              v-model="ruleForm.valueDate"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择起息日期"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="卓普信放款日期：" prop="sapienceLoanDate">
            <el-date-picker
              v-model="ruleForm.sapienceLoanDate"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择卓普信放款日期"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注：" prop="remark">
            <el-input
              v-model.trim="ruleForm.remark"
              placeholder="请输入"
              clearable
              style="width: 600px';"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="附件：" prop="name">
            <JFX-upload
              @success="successUpload"
              :url="action"
              :limit="10000"
              :multiple="false"
            >
              <el-button id="sale-upload-btn" type="primary">
                上传文件
              </el-button>
            </JFX-upload>
          </el-form-item>
          <div
            v-for="item in attachmenList"
            :key="item.id"
            style="color: blue; padding: 0 0 20px 130px"
            class="mr-t-10"
          >
            <span
              @click="
                downloadAttachmen(item.attachmentUrl, item.attachmentName)
              "
              style="padding-right: 10px; cursor: pointer"
            >
              {{ item.attachmentName }}
            </span>
            <span
              @click="delAttachmenItem(item.attachmentCode)"
              style="cursor: pointer"
            >
              删除
            </span>
          </div>
        </el-col>
      </el-row>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import {
    listAttachment,
    downloadFileUrl,
    delAttachment,
    attachmentUploadFiles
  } from '@a/base/index'
  import { confirmCreditOrder } from '@a/salesManage/index'
  import { getBaseUrl } from '@u/tool'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      filterData: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        ruleForm: {
          loanDate: '',
          valueDate: '',
          remark: '',
          sapienceLoanDate: ''
        },
        rules: {
          loanDate: {
            required: true,
            message: '请输入收款日期',
            trigger: 'change'
          },
          valueDate: {
            required: true,
            message: '请输入起息日期',
            trigger: 'change'
          },
          sapienceLoanDate: {
            required: true,
            message: '请输入卓普信放款日期',
            trigger: 'change'
          }
        },
        // 详情
        detail: {},
        // 附件列表
        attachmenList: [],
        // 附件上传的url
        action: '',
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        this.detail = this.filterData || {}
        this.ruleForm.sapienceLoanDate = this.detail.sapienceLoanDate || ''
        // 获取附件上传的url
        this.action =
          getBaseUrl(attachmentUploadFiles) +
          attachmentUploadFiles +
          '?token=' +
          sessionStorage.getItem('token') +
          '&code=' +
          this.detail.code
      },
      // 放款日期改变
      loanDateChange() {
        const { loanDate } = this.ruleForm
        this.ruleForm.valueDate = loanDate
        this.ruleForm.sapienceLoanDate = loanDate
      },
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            this.confirmBtnLoading = true
            try {
              await confirmCreditOrder({
                ...this.ruleForm,
                id: this.detail.id
              })
              this.$emit('comfirm')
              this.$successMsg('操作成功')
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.confirmBtnLoading = false
            }
          }
        })
      },
      // 获取附件的列表
      async getListAttachment() {
        const {
          data: { list }
        } = await listAttachment({
          code: this.detail.code,
          begin: 0,
          pageSize: 99
        })
        this.attachmenList.push(list.shift())
      },
      // 上传成功
      successUpload(res) {
        if (res.code + '' === '10000') {
          this.$successMsg('上传成功')
          this.getListAttachment()
        }
      },
      // 下载附件
      downloadAttachmen(url, name) {
        this.$exportFile(downloadFileUrl, {
          url: encodeURI(url),
          fileName: encodeURI(name)
        })
      },
      // 删除附件
      delAttachmenItem(attachmentCodes) {
        this.$showToast('确定要删除吗？', async () => {
          try {
            await delAttachment({ attachmentCodes })
            const codes = attachmentCodes.split(',')
            codes.forEach((code) => {
              const target = this.attachmenList.find(
                (item) => item.attachmentCode === code
              )
              if (target) {
                this.attachmenList.splice(
                  this.attachmenList.indexOf(target) >>> 0,
                  1
                )
              }
            })
            this.$successMsg('操作成功')
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      async beforeClose() {
        try {
          if (this.attachmenList.length) {
            const attachmentCodes = this.attachmenList
              .map((item) => item.attachmentCode)
              .toString()
            await delAttachment({ attachmentCodes })
          }
          this.$emit('cancel')
        } catch (error) {
          this.$errorMsg(error.message)
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.form__container {
    padding: 0 20px;
    .el-form-item {
      padding-right: 30px;
      margin-bottom: 20px;
      display: flex;
      .el-form-item__label {
        width: 150px;
      }
      .el-form-item__content {
        width: 300px;
      }
    }
  }
</style>
