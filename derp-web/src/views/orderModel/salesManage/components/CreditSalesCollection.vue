<template>
  <!-- 赊销收款 -->
  <JFX-Dialog
    title="赊销收款"
    closeBtnText="取 消"
    :width="'1200px'"
    :top="'80px'"
    :showClose="true"
    :btnAlign="'center'"
    :visible="isVisible"
    @comfirm="comfirm"
  >
    <div v-loading="viewLoding">
      <JFX-Form
        :model="ruleForm"
        ref="ruleForm"
        :rules="rules"
        class="purchase-bx"
        style="max-height: 500px; overflow: auto"
      >
        <el-row :gutter="10" class="page-view">
          <el-col :span="8">
            <el-form-item label="客户：" style="margin-bottom: 0">
              {{ detail.customerName || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="PO号：" style="margin-bottom: 0">
              {{ detail.poNo || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="币种：" style="margin-bottom: 0">
              {{ detail.currency || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="事业部：" style="margin-bottom: 0">
              {{ detail.buName || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建人：" style="margin-bottom: 0">
              {{ detail.createName || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间：" style="margin-bottom: 0">
              {{ detail.createDate || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单金额：" prop="creditAmount">
              <JFX-Input
                v-model.trim="ruleForm.creditAmount"
                :precision="2"
                :min="0"
                style="width: 100%"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="起息日期：" prop="valueDate">
              <el-date-picker
                v-model="ruleForm.valueDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期时间"
                clearable
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="还款日期：" prop="receiveDate">
              <el-date-picker
                v-model="ruleForm.receiveDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期时间"
                clearable
                @change="editField"
              />
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="权责月份：" prop="ownMonth">
              <el-date-picker
                v-model="ruleForm.ownMonth"
                type="month"
                value-format="yyyy-MM"
                placeholder="选择权责月份"
                @change="editField"
                clearable
                :disabled="ownMonthDisabled"
              />
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="滞纳金减免金额：" prop="discountDelayAmount">
              <JFX-Input
                v-model.trim="ruleForm.discountDelayAmount"
                :precision="2"
                :min="0"
                @change="discountDelayAmountChange"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>

          <el-col :span="16">
            <el-form-item label="减免原因：" prop="discountReason">
              <el-input
                style="width: 100%"
                v-model="ruleForm.discountReason"
                type="textarea"
                placeholder="150字以内"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item>
              <el-checkbox v-model="checkAll" @change="handleCheckAll">
                全部赎回
              </el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>

        <JFX-table
          :tableData.sync="tableData"
          :tableColumn="tableColumn"
          :showPagination="false"
          :summaryMethod="summaryMethod"
          showSummary
          ref="rootTable"
        >
          <template slot="num" slot-scope="{ row }">
            订单：{{ row.num }}
            <br />
            可赎：{{ row.stayRedempNum }}
          </template>
          <template slot="amount" slot-scope="{ row }">
            订单：{{ row.amount }}
            <br />
            可赎：{{ row.stayRedempAmount }}
          </template>
          <template slot="redempNum" slot-scope="{ row }">
            <JFX-Input
              v-model.trim="row.redempNum"
              :precision="2"
              :min="0"
              style="width: 100%"
              @change="editField"
            ></JFX-Input>
          </template>
        </JFX-table>
        <!-- 统计数据 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="保证金：" style="margin-bottom: 0">
              {{ formatNumber(ruleForm.marginAmount) }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="还款本金：" style="margin-bottom: 0">
              {{ formatNumber(ruleForm.principalAmount) }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="资金占用费：" style="margin-bottom: 0">
              {{ formatNumber(ruleForm.occupationAmount) }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="代理费：" style="margin-bottom: 0">
              {{ formatNumber(ruleForm.agencyAmount) }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="滞纳金：" style="margin-bottom: 0">
              {{ formatNumber(ruleForm.delayAmount) }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="应还款金额：" style="margin-bottom: 0">
              {{ formatNumber(ruleForm.receivableAmount) }}
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 附件 -->
        <el-form-item label="附件：" prop="name">
          <JFX-upload
            @success="successUpload"
            :url="action"
            :limit="10000"
            :multiple="false"
          >
            <el-button id="sale-upload-btn" type="primary">上传文件</el-button>
          </JFX-upload>
        </el-form-item>
        <div
          v-for="item in attachmenList"
          :key="item.id"
          style="color: blue; padding: 0 0 20px 130px"
          class="mr-t-10"
        >
          <span
            @click="downloadAttachmen(item.attachmentUrl, item.attachmentName)"
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
      </JFX-Form>
    </div>
    <template slot="dialog-footer">
      <el-button type="primary" @click="calAmount" :loading="calAmountLoading">
        <span>还款试算</span>
        <el-tooltip
          class="item"
          content="还款试算功能不计算商品分摊金额"
          effect="dark"
          placement="top-start"
        >
          <i class="el-icon-info" />
        </el-tooltip>
      </el-button>
      <el-button
        type="primary"
        @click="apply"
        :loading="applyLoading"
        v-if="isApply"
      >
        申请还款
      </el-button>
      <el-button
        type="primary"
        @click="comfirm"
        :loading="comfirmLoading"
        v-else
      >
        提交还款
      </el-button>
      <el-button type="primary" @click="exportList" :loading="exportLoading">
        导出试算结果
      </el-button>
      <el-button @click="beforeClose">取消</el-button>
    </template>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  import {
    listAttachment,
    downloadFileUrl,
    delAttachment,
    attachmentUploadFiles
  } from '@a/base/index'
  import {
    getCreditRepayDetail,
    calCreditRepayAmount,
    applyCreditRefund,
    saveCreditBill,
    exportCalResultUrl
  } from '@a/salesManage'
  import { exportFilePost } from '@/utils/common'
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
          creditAmount: '',
          valueDate: '',
          receiveDate: '',
          discountDelayAmount: '',
          discountReason: '',
          ownMonth: ''
        },
        rules: {
          creditAmount: {
            required: true,
            message: '订单金额不能为空',
            trigger: 'change'
          },
          valueDate: {
            required: true,
            message: '起息日期不能为空',
            trigger: 'change'
          },
          receiveDate: {
            required: true,
            message: '还款日期不能为空',
            trigger: 'change'
          },
          ownMonth: {
            required: true,
            message: '权责月份不能为空',
            trigger: 'change'
          },
          discountReason: [
            {
              required: false,
              message: '减免原因不能为空',
              trigger: 'change'
            },
            { max: 150, message: '长度不能超过150个字符', trigger: 'blur' }
          ]
        },
        // 表格列数据
        tableColumn: [
          {
            label: '商品货号',
            prop: 'goodsNo',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '数量',
            slotTo: 'num',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '单价',
            prop: 'price',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '金额',
            slotTo: 'amount',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '本次赎回数量',
            slotTo: 'redempNum',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '分摊保证金',
            prop: 'marginAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '分摊本金',
            prop: 'principalAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '资金占用费',
            prop: 'occupationAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '代理费',
            prop: 'agencyAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '滞纳金',
            prop: 'delayAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '滞纳金减免金额',
            prop: 'discountDelayAmount',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '应还金额',
            prop: 'receivableAmount',
            width: '100',
            align: 'center',
            hide: true
          }
        ],
        // 权责月份禁用
        ownMonthDisabled: false,
        // 详情数据
        detail: {},
        // 全部赎回
        checkAll: '',
        // 申请还款
        isApply: true,
        // 附件列表
        attachmenList: [],
        // 附件上传的url
        action: '',
        // loading
        viewLoding: false,
        // 还款试算loading
        calAmountLoading: false,
        // 申请还款loading
        applyLoading: false,
        // 提交还款loading
        comfirmLoading: false,
        // 导出loading
        exportLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.filterData
        try {
          this.viewLoding = true
          const {
            data: { itemList, code, ownMonth, isExistBillOrder },
            data
          } = await getCreditRepayDetail({ id })
          this.detail = data
          this.ruleForm.ownMonth = ownMonth || ''
          // 存在收款单 权责月份禁止修改
          if (isExistBillOrder === '1') {
            this.ownMonthDisabled = true
          }
          // 回填数据
          for (const key in this.ruleForm) {
            this.ruleForm[key] = String(data[key] || '')
          }
          if (itemList?.length) {
            this.tableData.list = itemList.map((item) => ({
              goodsId: item.goodsId + '',
              goodsNo: item.goodsNo || '',
              goodsName: item.goodsName || '',
              num: item.num || 0,
              stayRedempNum: item.stayRedempNum || 0,
              price: item.price || 0,
              amount: item.amount || 0,
              stayRedempAmount: item.stayRedempAmount || 0,
              redempNum: 0,
              marginAmount: '',
              principalAmount: '',
              occupationAmount: '',
              agencyAmount: '',
              delayAmount: '',
              receivableAmount: '',
              discountDelayAmount: ''
            }))
          }
          // 获取附件上传的url
          this.action =
            getBaseUrl(attachmentUploadFiles) +
            attachmentUploadFiles +
            '?token=' +
            sessionStorage.getItem('token') +
            '&code=' +
            code
        } finally {
          this.viewLoding = false
        }
      },
      // 滞纳金减免金额改变
      discountDelayAmountChange() {
        const { discountDelayAmount } = this.ruleForm
        this.rules.discountReason[0].required = ![
          0,
          '0',
          undefined,
          null,
          '0.00'
        ].includes(discountDelayAmount)
        if (!isNaN(discountDelayAmount)) {
          this.ruleForm.discountDelayAmount =
            Number(discountDelayAmount).toFixed(2)
        }
        this.editField()
      },
      // 修改可编辑字段 提交还款 转 申请还款
      editField() {
        this.isApply = true
      },
      // 格式化数值 两位小数 空设置为0
      formatNumber(number) {
        return number ? (+number).toFixed(2) : 0
      },
      // 设置统计数据
      setAmount(data) {
        const {
          marginAmount,
          principalAmount,
          occupationAmount,
          agencyAmount,
          delayAmount,
          receivableAmount
        } = data
        this.ruleForm.marginAmount = marginAmount || ''
        this.ruleForm.principalAmount = principalAmount || ''
        this.ruleForm.occupationAmount = occupationAmount || ''
        this.ruleForm.agencyAmount = agencyAmount || ''
        this.ruleForm.delayAmount = delayAmount || ''
        this.ruleForm.receivableAmount = receivableAmount || ''
      },
      // 还款试算
      calAmount() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const { id } = this.filterData
            try {
              this.calAmountLoading = true
              let redempNums = 0
              this.tableData.list.forEach((item) => {
                redempNums += item.redempNum ? +item.redempNum : 0
              })
              if (!redempNums) {
                this.$errorMsg('商品列表至少有一项本次赎回数量大于0！')
                return false
              }
              const itemList = this.tableData.list
              const { data } = await calCreditRepayAmount({
                ...this.ruleForm,
                id,
                itemList
              })
              this.setAmount(data)
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.calAmountLoading = false
            }
          }
        })
      },
      // 导出试算结果
      async exportList() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            try {
              this.exportLoading = true
              const json = JSON.stringify({
                ...this.ruleForm,
                id: this.detail.id,
                itemList: this.tableData.list.map((item) => ({
                  ...item,
                  redempNum: item.redempNum ? +item.redempNum : 0,
                  marginAmount: item.marginAmount ? +item.marginAmount : 0,
                  principalAmount: item.principalAmount
                    ? +item.principalAmount
                    : 0,
                  occupationAmount: item.occupationAmount
                    ? +item.occupationAmount
                    : 0,
                  agencyAmount: item.agencyAmount ? +item.agencyAmount : 0,
                  delayAmount: item.delayAmount ? +item.delayAmount : 0,
                  receivableAmount: item.receivableAmount
                    ? +item.receivableAmount
                    : 0
                }))
              })
              let redempNums = 0
              this.tableData.list.forEach((item) => {
                redempNums += item.redempNum ? +item.redempNum : 0
              })
              const flag = this.tableData.list.find((item) => {
                return item.redempNum > item.stayRedempNum
              })
              if (!redempNums) {
                this.$errorMsg('商品列表至少有一项本次赎回数量大于0！')
                return false
              }
              if (flag) {
                this.$errorMsg('商品列表存在本次赎回数量大于可赎回数量！')
                return false
              }
              exportFilePost(exportCalResultUrl, { json })
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.exportLoading = false
            }
          } else {
            this.$errorMsg('请先填写必填信息！')
          }
        })
      },
      // 申请还款
      apply() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const { id } = this.filterData
            try {
              this.applyLoading = true
              let redempNums = 0
              this.tableData.list.forEach((item) => {
                redempNums += item.redempNum ? +item.redempNum : 0
              })
              if (!redempNums) {
                this.$errorMsg('商品列表至少有一项本次赎回数量大于0！')
                return false
              }
              const itemList = this.tableData.list
              const {
                data: { itemList: list },
                data
              } = await applyCreditRefund({ ...this.ruleForm, id, itemList })
              if (list?.length) {
                // 表格赋值
                list.forEach((item) => {
                  const target = this.tableData.list.find(
                    (subItem) => +subItem.goodsId === +item.goodsId
                  )
                  if (target) {
                    target.marginAmount = item.marginAmount
                    target.principalAmount = item.principalAmount
                    target.occupationAmount = item.occupationAmount
                    target.agencyAmount = item.agencyAmount
                    target.delayAmount = item.delayAmount
                    target.receivableAmount = item.receivableAmount
                    target.discountDelayAmount = item.discountDelayAmount
                  }
                })
              }
              this.setAmount(data)
              this.isApply = false
              this.ownMonthDisabled = true
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.applyLoading = false
            }
          }
        })
      },
      // 提交还款
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const { id } = this.filterData
            try {
              this.comfirmLoading = true
              let redempNums = 0
              this.tableData.list.forEach((item) => {
                redempNums += item.redempNum ? +item.redempNum : 0
              })
              if (!redempNums) {
                this.$errorMsg('商品列表至少有一项本次赎回数量大于0！')
                return false
              }
              const itemList = this.tableData.list
              await saveCreditBill({ ...this.ruleForm, id, itemList })
              this.$successMsg('还款成功！')
              this.$emit('comfirm')
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.comfirmLoading = false
            }
          }
        })
      },
      // 全部赎回
      handleCheckAll(val) {
        if (val) {
          this.tableData.list = this.tableData.list.map((item) => ({
            ...item,
            redempNum: item.stayRedempNum
          }))
        } else {
          this.tableData.list = this.tableData.list.map((item) => ({
            ...item,
            redempNum: 0
          }))
        }
      },
      // 合计方法
      summaryMethod({ columns, data }) {
        const sums = []
        let nums = 0
        let stayRedempNums = 0
        let amounts = 0
        let stayRedempAmounts = 0
        let redempNums = 0
        let marginAmounts = 0
        let principalAmounts = 0
        let occupationAmounts = 0
        let agencyAmounts = 0
        let delayAmounts = 0
        let receivableAmounts = 0
        columns.forEach((item, index) => {
          if (index === 0) {
            item.colSpan = 2
          }
        })
        data.forEach((item) => {
          nums += item.num ? +item.num : 0
          stayRedempNums += item.stayRedempNum ? +item.stayRedempNum : 0
          amounts += item.amount ? +item.amount : 0
          stayRedempAmounts += item.stayRedempAmount
            ? +item.stayRedempAmount
            : 0
          redempNums += item.redempNum ? +item.redempNum : 0
          marginAmounts += item.marginAmount ? +item.marginAmount : 0
          principalAmounts += item.principalAmount ? +item.principalAmount : 0
          occupationAmounts += item.occupationAmount
            ? +item.occupationAmount
            : 0
          agencyAmounts += item.agencyAmount ? +item.agencyAmount : 0
          delayAmounts += item.delayAmount ? +item.delayAmount : 0
          receivableAmounts += item.receivableAmount
            ? +item.receivableAmount
            : 0
        })
        sums[0] = '合计'
        sums[1] = `订单：${nums} \n 可赎：${stayRedempNums}`
        sums[3] = `订单：${amounts} \n 可赎：${stayRedempAmounts}`
        sums[4] = (+redempNums).toFixed(2)
        sums[5] = (+marginAmounts).toFixed(2)
        sums[6] = (+principalAmounts).toFixed(2)
        sums[7] = (+occupationAmounts).toFixed(2)
        sums[8] = (+agencyAmounts).toFixed(2)
        sums[9] = (+delayAmounts).toFixed(2)
        sums[11] = (+receivableAmounts).toFixed(2)
        return sums
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
  .page-view {
    width: 100%;
    margin-bottom: 10px;
  }
  ::v-deep.purchase-bx .el-form-item__label {
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
  ::v-deep.purchase-bx .el-form-item__content {
    flex: 1;
  }
  ::v-deep.purchase-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  .append-text {
    cursor: pointer;
    display: flex;
    margin: 10px 0 0 10px;
    color: #17a9eb;
  }
</style>
