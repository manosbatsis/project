<template>
  <div class="container bgc-w mr-t-20">
    <div class="title">
      <div style="display: flex; align-items: center">
        <div style="margin-right: 20px">待办事项</div>
        <div
          v-for="(item, index) in todoList"
          class="tabs"
          :key="`tabs-${index}`"
          :class="{ 'tabs--active': todoIdx === index }"
          @click="tabsClick(index)"
        >
          {{ item }}
        </div>
      </div>
    </div>
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="todoColumn[todoIdx]"
      :border="false"
      tableHeight="240"
    >
      <template slot="purchasePrice" slot-scope="{ row }">
        {{ priceFormat(row.purchasePrice, row.purchaseCurrency) }}
      </template>
      <template slot="afterPrice" slot-scope="{ row }">
        {{ priceFormat(row.afterPrice, row.afterCurrency) }}
      </template>
      <template slot="settlementPrice" slot-scope="{ row }">
        {{ priceFormat(row.settlementPrice, row.settlementCurrency) }}
      </template>
      <template slot="waveRate" slot-scope="{ row }">
        {{ row.waveRate ? row.waveRate + '%' : '' }}
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getPendingRecordOrders,
    getPendingShelfOrders,
    getPendingConfirmOrders,
    getPendingCheckOrders,
    getPendingCarryForward,
    getSkuPriceWarns
  } from '@a/home'
  export default {
    mixins: [commomMix],
    data() {
      return {
        /* 待办事项list */
        todoList: [
          '待录入时间',
          '待上架',
          '待确认',
          '待审核',
          '待结转',
          'SKU预警'
        ],
        /* 待办事项当前点击的index */
        todoIdx: 0,
        /* 表格列数据 */
        todoColumn: [
          [
            { label: '订单编号', prop: 'code' },
            { label: '入仓仓库', prop: 'depotName' },
            { label: 'PO单号', prop: 'poNo' },
            { label: '事业部', prop: 'buName' }
          ],
          [
            { label: '订单编号', prop: 'code' },
            { label: '出仓仓库', prop: 'depotName' },
            { label: '事业部', prop: 'buName' },
            { label: '客户', prop: 'customerName' },
            { label: '销售类型', prop: 'saleTypeLabel' }
          ],
          [
            { label: '理货单号', prop: 'code' },
            { label: '仓库', prop: 'depotName' },
            { label: '事业部', prop: 'buName' },
            { label: '理货时间', prop: 'tallyingDate' },
            { label: '订单类型', prop: 'orderTypeLabel' }
          ],
          [
            { label: '订单编号', prop: 'code' },
            { label: '仓库', prop: 'depotName' },
            { label: '事业部', prop: 'buName' },
            { label: '创建时间', prop: 'createDate' },
            { label: '订单类型', prop: 'orderTypeLabel' }
          ],
          [
            { label: '仓库名称', prop: 'depotName' },
            { label: '结转月份', prop: 'settlementMonth' },
            { label: '期初时间', prop: 'firstDate' },
            { label: '期末时间', prop: 'endDate' }
          ],
          [
            { label: '事业部', prop: 'buName' },
            { label: '商品条码', prop: 'sku' },
            { label: '采购单价', slotTo: 'purchasePrice' },
            { label: '折算汇率价', slotTo: 'afterPrice' },
            { label: '本位币价', slotTo: 'settlementPrice' },
            { label: '浮动率', slotTo: 'waveRate' }
          ]
        ]
      }
    },
    mounted() {
      this.init()
    },
    computed: {
      /* 表格列表api */
      apis() {
        return [
          getPendingRecordOrders,
          getPendingShelfOrders,
          getPendingConfirmOrders,
          getPendingCheckOrders,
          getPendingCarryForward,
          getSkuPriceWarns
        ]
      },
      priceFormat() {
        return (price, currency) => (price ? `${currency} ${price}` : '')
      }
    },
    methods: {
      async init() {
        /* 生成表格列数据 */
        this.todoColumn = this.todoColumn.reduce(
          (pre, cur) => [
            ...pre,
            cur.map((item) => ({
              ...item,
              minWidth: '100',
              align: 'center',
              hide: true
            }))
          ],
          []
        )
        this.tabsClick(this.todoIdx)
      },
      /* tabs切换 */
      async tabsClick(index) {
        this.todoIdx = index
        try {
          this.tableData.loading = true
          const { data } = await this.apis[index]()
          this.tableData.list = data || []
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    border-left: 6px solid #daa3ff;
    height: 308px;
    .title {
      padding-left: 20px;
      height: 50px;
      border-bottom: 1px solid #ebeef5;
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #333;
    }
    .tabs {
      height: 30px;
      line-height: 30px;
      padding: 8px 24px;
      font-size: 14px;
      color: #7e8b96;
      border-bottom: 4px solid transparent;
      cursor: pointer;
      user-select: none;
      &--active {
        background-color: #f2f3f9;
        border-bottom: 4px solid #3488ca;
        color: #3488ca;
      }
    }
    .label {
      padding-right: 10px;
      color: #666;
    }
    .empty {
      width: 100%;
      height: 490px;
      line-height: 490px;
      text-align: center;
      color: #666;
    }
  }
</style>
