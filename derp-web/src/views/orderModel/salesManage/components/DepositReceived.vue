<template>
  <!-- 收到保证金 -->
  <JFX-Dialog
    title="收到保证金"
    closeBtnText="取 消"
    :width="'600px'"
    :top="'150px'"
    :showClose="true"
    :btnAlign="'center'"
    :visible="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    confirmBtnText="收到保证金"
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
          <el-form-item label="赊销金额：" prop="actualMarginAmount">
            {{ detail.currency }} {{ detail.creditAmount || 0 }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="应收保证金：" prop="receiveMarginDate">
            {{ detail.currency }} {{ detail.payableMarginAmount || 0 }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="实收保证金：" prop="actualMarginAmount">
            <JFX-Input
              v-model="ruleForm.actualMarginAmount"
              placeholder="请输入"
              style="width: 94%"
              clearable
              :min="0"
              :precision="2"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="收款日期：" prop="receiveMarginDate">
            <el-date-picker
              v-model="ruleForm.receiveMarginDate"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期时间"
            ></el-date-picker>
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
  import { getBaseUrl } from '@u/tool'
  import {
    listAttachment,
    downloadFileUrl,
    delAttachment,
    attachmentUploadFiles
  } from '@a/base/index'
  import { updateCreditMarginOrder } from '@a/salesManage/index'
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
          actualMarginAmount: '',
          receiveMarginDate: '',
          remark: ''
        },
        rules: {
          actualMarginAmount: {
            required: true,
            message: '请输入实收保证金',
            trigger: 'blur'
          },
          receiveMarginDate: {
            required: true,
            message: '请选择收款日期',
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
        /* 默认实收保证金=应收保证金 */
        this.ruleForm.actualMarginAmount = this.detail.payableMarginAmount
        /* 获取附件上传的url */
        this.action =
          getBaseUrl(attachmentUploadFiles) +
          attachmentUploadFiles +
          '?token=' +
          sessionStorage.getItem('token') +
          '&code=' +
          this.detail.code
      },
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            this.confirmBtnLoading = true
            try {
              await updateCreditMarginOrder({
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
        width: 130px;
      }
      .el-form-item__content {
        width: 300px;
      }
    }
  }
</style>
