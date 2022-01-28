<template>
  <!-- 收到还款 -->
  <JFX-Dialog
    title="收到还款"
    closeBtnText="取 消"
    :width="'800px'"
    :top="'150px'"
    :showClose="true"
    :btnAlign="'center'"
    :visible="isVisible"
    :confirmBtnLoading="confirmBtnLoading"
    :beforeClose="beforeClose"
    confirmBtnText="收到还款"
    @comfirm="comfirm"
  >
    <JFX-Form
      :model="ruleForm"
      ref="ruleForm"
      :rules="rules"
      class="form__container"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item
            label="收款单号："
            prop="id"
            class="search-panel-item fs-14 clr-II"
          >
            <el-select
              v-model="ruleForm.id"
              placeholder="请选择"
              filterable
              clearable
              @change="handleCodeChange"
            >
              <el-option
                v-for="item in codeSelectList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 表格 -->
        <el-col :span="24">
          <JFX-table
            :tableData.sync="tableData"
            :tableColumn="tableColumn"
            :showPagination="false"
          >
            <template slot="receiveDate" slot-scope="{ row }">
              {{ row.receiveDate ? row.receiveDate.slice(0, 10) : '' }}
            </template>
          </JFX-table>
        </el-col>
        <el-col :span="24">
          <el-form-item label="到账日期：" prop="accountDate">
            <el-date-picker
              v-model="ruleForm.accountDate"
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
  import {
    searchBillOrder,
    confirmCreditCreditBill
  } from '@a/salesManage/index'
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
          id: '',
          accountDate: '',
          remark: ''
        },
        rules: {
          id: { required: true, message: '请选择收款单号', trigger: 'change' },
          accountDate: {
            required: true,
            message: '请选择到账日期',
            trigger: 'change'
          }
        },
        // 表格列数据
        tableColumn: [
          {
            label: '收款单号',
            prop: 'code',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '创建人',
            prop: 'createName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '还款日期',
            slotTo: 'receiveDate',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本金',
            prop: 'principalAmount',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '资金占用费',
            prop: 'occupationAmount',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '代理费',
            prop: 'agencyAmount',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '滞纳金',
            prop: 'delayAmount',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '应还款金额',
            prop: 'receivableAmount',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        // 详情
        detail: {},
        // 附件列表
        attachmenList: [],
        // 附件上传的url
        action: '',
        confirmBtnLoading: false,
        codeSelectList: [],
        codeList: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        this.detail = this.filterData || {}
        // 获取附件上传的url
        this.action =
          getBaseUrl(attachmentUploadFiles) +
          attachmentUploadFiles +
          '?token=' +
          sessionStorage.getItem('token') +
          '&code=' +
          this.detail.code
        const { data } = await searchBillOrder({ id: this.detail.id })
        if (data && data.length) {
          this.codeSelectList = data.map((item) => ({
            key: item.id,
            value: item.code
          }))
          this.codeList = data
        }
      },
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            this.confirmBtnLoading = true
            try {
              await confirmCreditCreditBill({ ...this.ruleForm })
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
      // 收款单号改变
      handleCodeChange(id) {
        this.tableData.list = []
        if (!id) return false
        const target = this.codeList.find((item) => item.id === id)
        this.tableData.list.push(target)
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
