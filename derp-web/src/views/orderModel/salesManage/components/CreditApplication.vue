<template>
  <!-- 赊销申请组件 -->
  <JFX-Dialog
    title="赊销申请"
    closeBtnText="取 消"
    :width="'1200px'"
    :top="'80px'"
    :showClose="true"
    :btnAlign="'center'"
    :visible="financingApplicationVisible"
    @comfirm="comfirm"
  >
    <div v-loading="viewLoding">
      <JFX-Form
        :model="ruleForm"
        ref="ruleForm"
        :rules="rules"
        class="purchase-bx"
      >
        <el-row :gutter="10" class="page-view">
          <el-col :span="8">
            <el-form-item label="客户：" style="margin-bottom: 0">
              {{ ruleForm.customerName || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="PO号：" style="margin-bottom: 0">
              {{ ruleForm.poNo || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="币种：" style="margin-bottom: 0">
              {{ ruleForm.currency || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="事业部：" style="margin-bottom: 0">
              {{ ruleForm.buName || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建人：" style="margin-bottom: 0">
              {{ ruleForm.createName || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间：" style="margin-bottom: 0">
              {{ ruleForm.createDate || '- -' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单金额：" prop="creditAmount">
              <JFX-Input
                v-model.trim="ruleForm.creditAmount"
                :precision="2"
                :min="0"
                style="width: 100%"
                :disabled="type === 'redemption'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="应收保证金：" prop="payableMarginAmount">
              <JFX-Input
                v-model.trim="ruleForm.payableMarginAmount"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="权责月份：" prop="ownMonth">
              <el-date-picker
                v-model="ruleForm.ownMonth"
                type="month"
                value-format="yyyy-MM"
                placeholder="选择月"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
      <JFX-table
        :tableData.sync="tableData"
        :showPagination="false"
        :summaryMethod="summaryMethod"
        showSummary
      >
        <el-table-column
          prop="goodsNo"
          align="center"
          label="商品货号"
          width="140"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="goodsName"
          align="center"
          label="商品名称"
          min-width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="num"
          align="center"
          label="销售数量"
          width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="单价"
          prop="price"
          width="150"
        ></el-table-column>
        <el-table-column align="center" prop="'amount'" width="150">
          <template slot="header">
            <span class="need">金额</span>
          </template>
          <template slot-scope="{ row, $index }">
            <JFX-Input
              v-model.trim="row.amount"
              style="width: 100%"
              :precision="2"
              :min="0"
              @change="calc('amount', $index)"
            ></JFX-Input>
          </template>
        </el-table-column>
      </JFX-table>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { getFinanceDetail, applyCreditOrder } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    props: {
      financingApplicationVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      data: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        ruleForm: {
          customerName: '',
          poNo: '',
          currency: '',
          buName: '',
          createName: '',
          createDate: '',
          creditAmount: '',
          payableMarginAmount: '',
          ownMonth: ''
        },
        rules: {
          creditAmount: {
            required: true,
            message: '请输入订单金额',
            trigger: 'change'
          },
          payableMarginAmount: {
            required: true,
            message: '请输入应收保证金',
            trigger: 'change'
          },
          ownMonth: {
            required: true,
            message: '请选择权责月份'
          }
        },
        salePoNo: '',
        viewLoding: false,
        calAmountLoading: false,
        applyLoading: false,
        cancelLoading: false,
        title: '',
        // 是否已经申请赎回
        isApply: false,
        oldTime: ''
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id: orderId } = this.data
        try {
          this.viewLoding = true
          const { data } = await getFinanceDetail({ orderId })
          for (const key in this.ruleForm) {
            this.ruleForm[key] = data[key] ? data[key] + '' : ''
          }
          this.ruleForm.creditAmount = data.saleAmount || ''
          const { itemList } = data
          if (itemList && itemList.length) {
            this.tableData.list = itemList.map((item) => ({
              goodsId: item.goodsId + '',
              goodsName: item.goodsName || '',
              goodsNo: item.goodsNo || '',
              num: item.num || 0,
              amount: item.amount || '0',
              price: item.price || '0'
            }))
          }
        } finally {
          this.viewLoding = false
        }
      },
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            try {
              // 校验商品列表
              const flag = this.checkGoods()
              if (!flag) return false
              const itemList = this.tableData.list.map((item) => ({
                goodsId: item.goodsId || '',
                goodsName: item.goodsName || '',
                goodsNo: item.goodsNo || '',
                num: item.num ? item.num.toString() : '0',
                amount: item.amount ? item.amount.toString() : '0',
                price: item.price ? item.price.toString() : '0'
              }))
              const { id: saleOrderId } = this.data
              await applyCreditOrder({
                ...this.ruleForm,
                saleOrderId,
                itemList
              })
              this.$successMsg('操作成功')
              this.$emit('comfirm')
            } catch (error) {
              this.$errorMsg(error.message)
            }
          }
        })
      },
      // 校验商品
      checkGoods() {
        let flag = true
        let totalAmount = 0
        let { creditAmount } = this.ruleForm
        creditAmount = creditAmount ? +creditAmount : 0
        if (!this.tableData.list.length) {
          this.$errorMsg('该订单没有商品！')
          flag = false
          return flag
        }
        for (let i = 0; i < this.tableData.list.length; i++) {
          let { price, amount } = this.tableData.list[i]
          price = price ? +price : 0
          amount = amount ? +amount : 0
          if (!amount) {
            this.$errorMsg(`商品表格第${i + 1}行，金额必须大于0`)
            flag = false
            break
          }
          if (!price) {
            this.$errorMsg(`商品表格第${i + 1}行，商品单价必须大于0`)
            flag = false
            break
          }
          totalAmount += amount
        }
        if ((+totalAmount).toFixed(2) !== (+creditAmount).toFixed(2)) {
          this.$errorMsg('合计的商品金额与订单金额不一致!')
          flag = false
        }
        return flag
      },
      // 计算表格数据
      calc(type, index) {
        const item = this.tableData.list[index]
        let { price, num, amount } = item
        price = price ? +price : 0
        num = num ? +num : 0
        amount = amount ? +amount : 0
        if (type === 'amount') {
          price = amount / num
          price = price.toFixed(4) + ''
        }
        this.tableData.list.splice(index, 1, { ...item, price, num, amount })
      },
      // 合计方法
      summaryMethod({ columns, data }) {
        const sums = []
        let nums = 0
        let amounts = 0
        columns.forEach((item, index) => {
          if (index === 0) {
            item.colSpan = 2
          }
        })
        data.forEach(({ num, amount }) => {
          nums += num ? +num : 0
          amounts += amount ? +amount : 0
        })
        sums[0] = '合计'
        sums[1] = nums
        sums[3] = (+amounts).toFixed(2)
        return sums
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
