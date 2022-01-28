<template>
  <!-- 客户采购单详情组件 -->
  <JFX-Dialog
    title="客户采购单详情"
    closeBtnText="取 消"
    :width="'1200px'"
    :top="'80px'"
    :showClose="true"
    :btnAlign="'center'"
    :loading="pageLoading"
    :visible="isVisible"
    @comfirm="comfirm"
  >
    <el-row :gutter="10" class="fs-12 clr-II detail-row mr-b-20">
      <el-col class="mr-b-10" :span="8">
        PO号：{{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        下单时间：{{ detail.orderTime || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        平台状态：{{ detail.platformStatusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        采购币种：{{ detail.currencyLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        客户仓库：{{ detail.platformDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        入库时间：{{ detail.deliverDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        单据状态：{{ detail.statusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        提交人：{{ detail.submitName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        转销人：{{ detail.resaleName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        转销时间：{{ detail.resaleDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        销售订单号：{{ detail.saleCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        平台账号：{{ detail.userCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :span="8">
        PR号：{{ detail.prNo || '- -' }}
      </el-col>
    </el-row>

    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :summary-method="getSummaries"
      :showPagination="false"
      show-summary
    />
    <!-- 表格 end-->
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { detailPlatformPurchaseOrder } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      data: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        /* 详情数据 */
        detail: {},
        /* 页面loading */
        pageLoading: false,
        /* 表格列数据 */
        tableColumn: [
          {
            label: '平台货号',
            prop: 'platformGoodsNo',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '条形码',
            prop: 'platformBarcode',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'platformGoodsName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '采购数量',
            prop: 'num',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '单价',
            prop: 'price',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '采购金额',
            prop: 'amount',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '实收好品数',
            prop: 'receiptOkNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '实收坏品数',
            prop: 'receiptBadNum',
            width: '90',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.data
        try {
          this.pageLoading = true
          const { data } = await detailPlatformPurchaseOrder({ id })
          this.detail = data || {}
          this.tableData.list = this.detail.itemList || []
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.pageLoading = false
        }
      },
      /* 确认 */
      comfirm() {
        this.$emit('update:isVisible', { show: false })
      },
      /* 合计 */
      getSummaries({ data }) {
        const sum = []
        let nums = 0
        let amounts = 0
        let receiptOkNums = 0
        let receiptBadNums = 0
        data.forEach((item) => {
          nums += item.num ? +item.num : 0
          amounts += item.amount ? +item.amount : 0
          receiptOkNums += item.receiptOkNum ? +item.receiptOkNum : 0
          receiptBadNums += item.receiptBadNum ? +item.receiptBadNum : 0
        })
        sum[0] = '合计'
        sum[3] = nums
        sum[5] = `${this.detail.currency} ${(+amounts).toFixed(2)}`
        sum[6] = receiptOkNums
        sum[7] = receiptBadNums
        return sum
      }
    }
  }
</script>
