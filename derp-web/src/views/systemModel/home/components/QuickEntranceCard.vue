<template>
  <!-- 快捷入口卡片组件 -->
  <div
    class="card"
    :class="[
      { 'bg-blue': type === 'purchase' },
      { 'bg-red': type === 'sale' },
      { 'bg-purple': type === 'transfer' }
    ]"
    @click="linkTo(jumpPath[type])"
  >
    <div class="mr-b-10">
      <span class="circle">
        <i :class="iconPath[type] || ''" style="color: #fff"></i>
      </span>
      <span>入口</span>
    </div>
    <div>{{ text[type] || '' }}</div>
    <div class="number">
      {{ orderNum[type] }}
    </div>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      /**
       * @param type 类型
       * @example 'sale': 销售  'purchase': 采购 'transfer': 调拨
       */
      type: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        text: {
          purchase: '新增采购订单',
          sale: '新增销售订单',
          transfer: '新增调拨订单'
        },
        iconPath: {
          sale: 'el-icon-tickets',
          purchase: 'el-icon-shopping-cart-2',
          transfer: 'el-icon-takeaway-box'
        },
        orderNum: {
          purchase: '01',
          sale: '02',
          transfer: '03'
        },
        jumpPath: {
          sale: '/sales/salesorderadd',
          purchase: '/purchase/purchaseOrderedit/add',
          transfer: '/transfer/orderEdit/add'
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .card {
    width: 220px;
    height: 110px;
    border-radius: 9px;
    padding: 18px;
    color: #ffffff;
    position: relative;
    box-sizing: border-box;
    cursor: pointer;
    transition: all 0.2s linear;
    box-shadow: 0 6px 10px 0 rgba(95, 101, 105, 0.4);
    user-select: none;
  }
  .card:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 20px 0 rgba(95, 101, 105, 0.4);
  }
  .bg-blue {
    background: linear-gradient(to left, #46e0d6, #109ad1);
    background: -webkit-linear-gradient(to left, #46e0d6, #109ad1);
  }
  .bg-red {
    background: linear-gradient(to left, #f8cf8b, #f35969);
    background: -webkit-linear-gradient(to left, #f8cf8b, #f35969);
  }
  .bg-purple {
    background: linear-gradient(to left, #9270ff, #fd5d9f);
    background: -webkit-linear-gradient(to left, #9270ff, #fd5d9f);
  }
  .circle {
    border-radius: 50%;
    margin-right: 12px;
    width: 34px;
    height: 34px;
    display: inline-block;
    text-align: center;
    line-height: 34px;
    color: #000;
    background: rgba(0, 0, 0, 0.3);
  }
  .number {
    margin-right: 12px;
    position: absolute;
    right: 0;
    top: 50%;
    font-size: 48px;
    font-weight: bold;
    transform: translateY(-50%);
    opacity: 0.2;
  }
</style>
