<template>
  <!-- 打印请款单组件 -->
  <div>
    <JFX-Dialog
      :visible.sync="printPaymentVisible"
      :showClose="true"
      :width="'830px'"
      :title="'打印请款单'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      top="15vh"
    >
      <div class="content" v-loading="viewLoading">
        <section id="printCons">
          <p class="clr-II base-title"><span>请款单</span></p>
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
          </div>
          <table class="base-table-container mr-t-20">
            <tr>
              <td width="99">财务组织</td>
              <td width="265">{{ detail.merchantName || '' }}</td>
              <td width="150">合同编号</td>
              <td width="265">{{ detail.poNos || '' }}</td>
            </tr>
            <tr>
              <td>收款单位</td>
              <td>{{ detail.supplierName || '' }}</td>
              <td>币种</td>
              <td>{{ detail.currency || '' }}</td>
            </tr>
            <tr>
              <td>金额</td>
              <td>{{ detail.payableAmount || '' }}</td>
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
                <div style="display: flex; flex-wrap: wrap">
                  <span
                    style="width: 33.33%"
                    v-for="item in operateList"
                    :key="item.id"
                  >
                    {{ item.operaterDepot || '' }}：{{ item.operater || '' }}
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td>财务审批</td>
              <td colspan="3" style="text-align: left">出纳：</td>
            </tr>
          </table>
          <!-- <div class="brand-table-header">
            <div><span class="scale">序号</span></div>
            <div><span class="scale">摘要</span></div>
            <div><span class="scale">母品牌</span></div>
            <div><span class="scale">收支项目</span></div>
          </div>
          <div class="brand-table-item" v-for="item in printSummaryList" :key="item.id">
            <div><span class="scale">{{item.index || ''}}</span></div>
            <div><span class="scale">{{item.abstractName || ''}}</span></div>
            <div><span class="scale">{{item.superiorParentBrandName || ''}}</span></div>
            <div><span class="scale">{{item.ncName || ''}}</span></div>
          </div>
          <div class="mr-b-10 mr-t-10 fs-14 clr-II examine-title">审批记录</div>
          <div class="examine-table-header">
            <div><span class="scale">审批节点</span></div>
            <div><span class="scale">审批人</span></div>
            <div><span class="scale">审批结果</span></div>
            <div><span class="scale">审批意见</span></div>
            <div><span class="scale">审批时间</span></div>
          </div>
          <div class="examine-table-item" v-for="item in operateList" :key="item.id">
            <div><span class="scale">{{item.operaterDepot || ''}}</span></div>
            <div><span class="scale">{{item.operater || ''}}</span></div>
            <div><span class="scale">{{item.operateResult || ''}}</span></div>
            <div><span class="scale">{{item.operateRemark || ''}}</span></div>
            <div><span class="scale">{{item.operateDate || ''}}</span></div>
          </div> -->
        </section>
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
  import { paymentBillPrinting, exportPayablePDF } from '@a/paymentManage'
  export default {
    mixins: [commomMix],
    props: {
      printPaymentVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      id: {
        type: String | Number,
        default: ''
      }
    },
    data() {
      return {
        // 详情
        detail: {},
        // 提交按钮loading
        confirmBtnLoading: false,
        viewLoading: false,
        operateList: [],
        printSummaryList: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this
        try {
          this.viewLoading = true
          const { data } = await paymentBillPrinting({ id })
          this.detail = data || {}
          this.operateList = this.detail.operateList || []
          this.printSummaryList = this.detail.printSummaryList || []
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.viewLoading = false
        }
      },
      // 打印
      printList() {
        const style = '@page { size:A4 portrait;margin: 1cm 1.5cm 0cm 1cm; } '
        // const style = '@page { size:A4 portrait;margin:0 10mm; };'
        print({
          printable: 'printCons',
          type: 'html',
          style,
          targetStyles: ['*'],
          ignoreitemements: ['no-print', 'bc', 'gb']
        })
      },
      // 导出
      exportList() {
        const { id } = this
        this.$exportFile(exportPayablePDF, { id })
      }
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
    width: 790px;
    height: 600px;
    overflow: auto;
    padding-right: 10px;
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
