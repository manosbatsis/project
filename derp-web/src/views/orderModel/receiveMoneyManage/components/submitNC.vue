<template>
  <JFX-Dialog
    :visible.sync="submitNCVisible"
    :showClose="true"
    @comfirm="comfirm"
    :width="'1000px'"
    :title="'同步确认'"
    :btnAlign="'center'"
    :confirmBtnText="'同步'"
    :confirmBtnLoading="confirmBtnLoading"
    top="15vh"
  >
    <el-col :span="24" class="mr-b-10">确认将本次开票应收信息同步至NC：</el-col>
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showSummary="true"
      :summaryMethod="getSummaries"
    ></JFX-table>
    <template slot="footer_expand_btn">
      <el-button type="primary" @click="exportList">导出</el-button>
    </template>
  </JFX-Dialog>
</template>

<script>
  import commomMix from '@m/index'
  import {
    ToBLstReceiveBillsToNC,
    ToBsynNC,
    exportPdfUrl
  } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      submitNCVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      data: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    data() {
      return {
        tableColumn: [
          {
            label: '账单编号',
            prop: 'billCode',
            align: 'center'
          },
          {
            label: '事业部',
            prop: 'buName',
            align: 'center'
          },
          {
            label: '结算类型',
            prop: 'settlementTypeLabel',
            align: 'center'
          },
          {
            label: '销售模式',
            prop: 'saleModelLabel',
            align: 'center'
          },
          {
            label: '渠道',
            prop: 'ncChannelName',
            align: 'center'
          },
          {
            label: '品牌',
            prop: 'parentBrandName',
            align: 'center'
          },
          {
            label: '收支项目名称',
            prop: 'paymentSubjectName',
            align: 'center'
          },
          {
            label: '应收金额',
            prop: 'price',
            align: 'center'
          }
        ],
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      async getList() {
        try {
          this.tableData.loading = true
          const { data } = await ToBLstReceiveBillsToNC({
            id: this.data.id,
            dataSource: 1
          })
          this.tableData.list = data
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      async comfirm() {
        this.confirmBtnLoading = true
        try {
          await ToBsynNC({
            id: this.data.id,
            dataSource: 1
          })
          this.$emit('comfirm')
          this.$successMsg('操作成功')
        } catch (err) {
          console.log(err)
        } finally {
          this.confirmBtnLoading = false
        }
      },
      exportList() {
        this.$exportFile(exportPdfUrl, { ids: this.data.id })
      },
      getSummaries() {
        const sums = []
        sums[0] = '合计'
        sums[7] = this.tableData.list.reduce((pre, cur) => {
          return pre + cur.price
        }, 0)
        return sums
      }
    }
  }
</script>

<style></style>
