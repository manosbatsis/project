<template>
  <!-- 打印请款单组件 -->
  <div>
    <JFX-Dialog
      :visible.sync="printPaymentVisible"
      :showClose="true"
      :width="'830px'"
      :title="'打印请款单'"
      :btnAlign="'center'"
      :loading="viewLoading"
      :confirmBtnLoading="confirmBtnLoading"
      top="10vh"
    >
      <div class="content" id="printCons">
        <div v-for="(detail, index) in detailList" :key="index">
          <p class="clr-II base-title">
            <span>请款单</span>
          </p>
          <div
            class="print-title row-item clr-II detail-row"
            style="font-size: 16px"
          >
            <div class="col-item">申请部门：{{ detail.buName || '' }}</div>
            <div class="col-item" style="padding-left: 110px">
              申请人：{{ detail.creater || '' }}
            </div>
            <div class="col-item">请款日期：{{ detail.createDate || '' }}</div>
            <div class="col-item" style="padding-left: 110px">
              单据编号：{{ detail.code || '' }}
            </div>
            <div class="col-item">
              预计款日期：{{
                detail.expectedPaymentDate
                  ? detail.expectedPaymentDate.slice(0, 10)
                  : ''
              }}
            </div>
          </div>
          <table class="base-table-container mr-t-20">
            <tr>
              <td width="149">财务组织</td>
              <td width="240">{{ detail.merchantName || '' }}</td>
              <td width="150">合同编号</td>
              <td width="240">{{ detail.poNos || '' }}</td>
            </tr>
            <tr>
              <td>收款单位</td>
              <td>{{ detail.supplierName || '' }}</td>
              <td>币种</td>
              <td>{{ detail.currency || '' }}</td>
            </tr>
            <tr>
              <td>金额</td>
              <td>
                {{
                  detail.payableAmount ? numberFormat(detail.payableAmount) : ''
                }}
              </td>
              <td>大写金额</td>
              <td>{{ detail.payableAmountStr || '' }}</td>
            </tr>
            <tr>
              <td>收款银行账号</td>
              <td>{{ detail.bankAccount || '' }}</td>
              <td>收款银行开户行</td>
              <td>{{ detail.depositBank || '' }}</td>
            </tr>
            <tr>
              <td>请款原因</td>
              <td colspan="3" style="text-align: left">
                {{ detail.paymentReason || '' }}
              </td>
            </tr>
            <tr>
              <td>财务审批</td>
              <td colspan="3" style="text-align: left">
                <template v-for="(item, index) in detail.printAuditList">
                  {{ item }} &nbsp;&nbsp;&nbsp;&nbsp;
                  <br v-if="index % 3 === 2" :key="`${item}-${index}`" />
                </template>
                <div>出纳：</div>
              </td>
            </tr>
          </table>

          <div
            class="print-title row-item clr-II detail-row mr-t-15"
            style="font-size: 16px"
          >
            <div class="col-item">打印人：{{ detail.printer || '' }}</div>
            <div class="col-item" style="padding-left: 110px">
              打印时间：{{ $formatDate(Date.now(), 'yyyy-MM-dd HH:mm:ss') }}
            </div>
          </div>
          <div style="page-break-after: always"></div>
        </div>
      </div>
      <template slot="dialog-footer">
        <el-button type="primary" @click="printList">打印</el-button>
        <el-button type="primary" @click="exportList">导出</el-button>
        <el-button @click="$emit('comfirm', true)">取消</el-button>
      </template>
    </JFX-Dialog>
  </div>
</template>
<script>
  import print from 'print-js'
  import commomMix from '@m/index'
  import { numberFormat } from '@u/tool'
  import {
    paymentBillPrinting,
    exportPayablePDF,
    updatePrintingInfo
  } from '@a/paymentManage'
  export default {
    mixins: [commomMix],
    props: {
      printPaymentVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      ids: {
        type: String | Number,
        default: ''
      }
    },
    data() {
      return {
        // 详情
        detailList: [],
        // 提交按钮loading
        confirmBtnLoading: false,
        viewLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        let { ids } = this
        ids = ids.split(',')
        try {
          this.viewLoading = true
          const detailListPromise = []
          ids.map((id) => {
            detailListPromise.push(paymentBillPrinting({ id }))
          })
          const detailList = await Promise.all(detailListPromise)
          this.detailList = detailList.map(({ data }) => {
            if (data) {
              data.operateList = data.operateList || []
              return data
            } else {
              return { operateList: [] }
            }
          })
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.viewLoading = false
        }
      },
      // 打印
      async printList() {
        // landscape portrait
        const style = '@page { size:A4 landscape;margin: 1cm 1.5cm 0cm 4cm; } '
        print({
          printable: 'printCons',
          type: 'html',
          style,
          targetStyles: ['*'],
          ignoreitemements: ['no-print', 'bc', 'gb']
        })
        let { ids } = this
        try {
          ids = ids.split(',')
          for (let i = 0; i < ids.length; i++) {
            updatePrintingInfo({ id: ids[i] })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 导出
      exportList() {
        const { ids } = this
        this.$exportFile(exportPayablePDF, { id: ids })
      },
      numberFormat
    }
  }
</script>

<style lang="scss" scoped>
  .examine-table-header,
  .examine-table-item,
  .examine-title {
    page-break-inside: avoid !important;
  }
  .dialog-footer {
    height: 30px;
    margin-top: -30px;
  }
  .content {
    font-size: 12px;
  }
  .row-item {
    display: flex;
    margin-bottom: -10px;
    justify-content: space-between;
    align-content: center;
    flex-wrap: wrap;
    .col-item {
      width: 49%;
      margin-bottom: 10px;
      box-sizing: border-box;
    }
  }

  // 滚动条样式
  .content::-webkit-scrollbar-track-piece {
    background-color: #f8f8f8;
  }
  .content::-webkit-scrollbar {
    width: 9px;
    height: 9px;
  }
  .content::-webkit-scrollbar-thumb {
    background-color: #ddd;
    border-radius: 6px;
    background-clip: padding-box;
    min-height: 28px;
  }
  .content::-webkit-scrollbar-thumb:hover {
    background-color: #bbb;
  }

  // 表格样式
  .base-table-container {
    border: 1px solid #cbcbcb;
    width: 100%;
    text-align: center;
    border-collapse: collapse;
    color: #666;
    font-size: 16px;
    tr:nth-of-type(5) {
      > td {
        height: 80px;
      }
    }
    td {
      border-left: 1px solid #cbcbcb;
      border-bottom: 1px solid #cbcbcb;
      height: 36px;
      box-sizing: border-box;
      transform: scale(0.95, 0.9);
    }
  }

  // 审核列表
  .brand-table-header,
  .brand-table-item {
    display: table-row;
    > div {
      box-sizing: border-box;
      vertical-align: middle;
      border-right: 1px solid #cbcbcb;
      border-top: 1px solid #cbcbcb;
      border-bottom: 1px solid #cbcbcb;
      padding: 0px;
      font-size: 16px;
      display: table-cell;
      .scale {
        display: inline-block;
        transform: scale(0.95, 0.9);
        width: 100%;
      }
    }
    > div:nth-of-type(1) {
      border-left: 1px solid #cbcbcb;
      text-align: center;
      width: 104px;
    }
    > div:nth-of-type(2) {
      text-align: center;
      width: 240px;
    }
    > div:nth-of-type(3) {
      text-align: center;
      width: 224px;
    }
    > div:nth-of-type(4) {
      width: 240px;
    }
  }
  .examine-table-header {
    > div {
      text-align: center;
    }
  }

  // 审核列表
  .examine-table-header,
  .examine-table-item {
    display: table-row;
    > div {
      box-sizing: border-box;
      vertical-align: middle;
      border-right: 1px solid #cbcbcb;
      border-top: 1px solid #cbcbcb;
      border-bottom: 1px solid #cbcbcb;
      padding: 0px;
      font-size: 16px;
      display: table-cell;
      .scale {
        display: inline-block;
        transform: scale(0.95, 0.9);
        width: 100%;
      }
    }
    > div:nth-of-type(1) {
      border-left: 1px solid #cbcbcb;
      text-align: center;
      width: 150px;
    }
    > div:nth-of-type(2) {
      text-align: center;
      width: 120px;
    }
    > div:nth-of-type(3) {
      text-align: center;
      width: 100px;
    }
    > div:nth-of-type(4) {
      width: 330px;
    }
    > div:nth-of-type(5) {
      text-align: center;
      width: 180px;
    }
  }
  .examine-table-header {
    > div {
      text-align: center;
    }
  }
  .base-title {
    text-align: center;
    margin: 20px 0 40px 0;
    > span {
      font-size: 16px;
      margin-top: 16px;
      width: 100px;
      text-align: center;
      display: inline-block;
      transform: scale(1.5);
    }
  }
</style>
