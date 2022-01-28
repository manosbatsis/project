<template>
  <div class="toSale">
    <JFX-Dialog
      top="6vh"
      :visible.sync="isVisible"
      :showClose="true"
      :width="'70vw'"
      :title="'转销售订单'"
      :btnAlign="'center'"
      @comfirm="comfirm"
    >
      <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.customerId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getCustomerSelectBean('customerList')"
                @change="customerChange"
              >
                <el-option
                  v-for="item in selectOpt.customerList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="加价比例："
              prop="rate"
              class="search-panel-item fs-14 clr-II"
            >
              <span slot="label">
                <el-tooltip
                  effect="dark"
                  content="销售单价=采购单价 * (1 + 加价比例)"
                  placement="top-start"
                >
                  <i class="el-icon-warning-outline fs-16 c-p clr-warn"></i>
                </el-tooltip>
                <span class="need">加价比例：</span>
              </span>
              <JFX-Input
                v-model.trim="ruleForm.rate"
                :disabled="priceManage"
                :precision="5"
                :min="0"
                :max="9.99999"
                placeholder="请输入，最多保留5位小数"
                @change="changeRate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="销售PO号："
              prop="poNo"
              ref="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <div style="display: flex">
                <el-input
                  v-model="ruleForm.poNo"
                  placeholder="请输入"
                  clearable
                />
                <div
                  style="margin-left: 10px; color: blue; cursor: pointer"
                  @click="getOrderPoNo"
                >
                  取采购订单PO号
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="出库仓库："
              prop="outDepotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.outDepotId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getSelectBeanByMerchantBuRel('out_warehousesList', {
                    buId: data.buId
                  })
                "
              >
                <el-option
                  v-for="item in selectOpt.out_warehousesList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
      <el-row class="company-page">
        <el-col :span="24" class="mr-t-20">
          <JFX-table
            :tableData.sync="tableData"
            :tableColumn="tableColumn"
            :showPagination="false"
            :showSummary="true"
            :summaryMethod="summaryMethod"
          >
            <!-- 销售数量 -->
            <template slot="num" slot-scope="{ row, $index }">
              <el-input-number
                v-model.trim="row.num"
                :precision="0"
                :min="0"
                :controls="false"
                style="width: 100%"
                @change="calcAmount($index)"
              />
            </template>

            <!-- 销售单价 -->
            <template slot="price" slot-scope="{ row, $index }">
              <el-select
                v-if="priceManage"
                v-model="row.price"
                placeholder="请选择"
                clearable
                filterable
                @change="calcAmount($index)"
              >
                <el-option
                  v-for="item in row.priceManageList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.value"
                />
              </el-select>
              <el-input-number
                v-else
                v-model.trim="row.price"
                v-max-spot="8"
                :precision="8"
                :disabled="priceManage"
                :controls="false"
                :min="0"
                style="width: 100%"
                @change="calcAmount($index)"
              />
            </template>

            <!-- 销售金额 -->
            <template slot="amount" slot-scope="{ row, $index }">
              <el-input-number
                v-model.trim="row.amount"
                v-max-spot="2"
                :precision="2"
                :controls="false"
                :min="0"
                :disabled="priceManage"
                style="width: 100%"
                @change="calcSum($index)"
              />
            </template>

            <!-- 税率 -->
            <template slot="taxRate" slot-scope="{ row, $index }">
              <el-select
                v-model="row.taxRate"
                placeholder="请选择"
                style="width: 100%"
                filterable
                @change="calcTax($index)"
              >
                <el-option
                  v-for="item in selectOpt.taxList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.value"
                />
              </el-select>
            </template>
          </JFX-table>
          <!-- 表格 -->
        </el-col>
      </el-row>
    </JFX-Dialog>
  </div>
</template>
<script>
  import {
    genSaleOrderByPurchaseIds,
    saveSaleOrder,
    getCustomerBuIdPrice
  } from '@a/purchaseManage/index'
  import { getSalePriceManage } from '@a/salesManage/index'
  import commomMix from '@m/index'
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
        /* 表单数据 */
        ruleForm: {
          customerId: '',
          rate: '',
          poNo: '',
          outDepotId: ''
        },
        /* 表单校验 */
        rules: {
          customerId: {
            required: true,
            message: '客户不能为空',
            trigger: 'change'
          },
          poNo: {
            required: true,
            message: 'po号不能为空',
            trigger: 'blur'
          },
          outDepotId: {
            required: true,
            message: '出库仓库不能为空',
            trigger: 'change'
          }
        },
        /* 表格结构数据 */
        tableColumn: [
          {
            label: '商品条码',
            prop: 'goodsNo',
            minWidth: '160',
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
            label: '销售数量',
            slotTo: 'num',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '销售单价',
            slotTo: 'price',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '销售金额',
            slotTo: 'amount',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '税率',
            slotTo: 'taxRate',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '税额',
            prop: 'tax',
            width: '100',
            align: 'center',
            hide: true
          }
        ],
        canSave: true,
        /* 是否开启价格管理 */
        priceManage: false
      }
    },
    mounted() {
      this.getRaxList('taxList')
      this.init()
    },
    methods: {
      async init() {
        const { ids } = this.data
        try {
          this.tableData.loading = true
          const {
            data: { itemList }
          } = await genSaleOrderByPurchaseIds({ ids })
          if (itemList && itemList.length) {
            this.tableData.list = itemList.map((item) => ({
              ...item,
              baseAmount: item.amount || 0,
              taxRate: item.taxRate || '0.00',
              tax: Number(item.tax).toFixed(2) || 0.0,
              /* 开启价格管理时用来存放单价列表 */
              priceManageList: []
            }))
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            let flag = true
            const itemList = []
            for (let i = 0; i < this.tableData.list.length; i++) {
              const tips = `表格第${i + 1}行，`
              const { amount, price, id, num, goodsNo, taxRate, tax } =
                this.tableData.list[i]
              if ([null, undefined].includes(num)) {
                this.$errorMsg(tips + '请补全销售数量')
                flag = false
                break
              }
              if ([null, undefined].includes(price)) {
                this.$errorMsg(tips + '请补全销售单价')
                flag = false
                break
              }
              if ([null, undefined].includes(amount)) {
                this.$errorMsg(tips + '请补全销售金额')
                flag = false
                break
              }
              itemList.push({
                amount: amount + '',
                price: price + '',
                id: id + '',
                taxRate: taxRate + '',
                tax: tax + '',
                num: num.toString(),
                goodsNo: goodsNo.toString()
              })
            }
            if (!flag) return false
            this.tableData.loading = true
            if (!this.canSave) return false // 幂等
            this.canSave = false
            // 提交前校验是否存在
            try {
              const opt = {
                ids: this.data.ids,
                customerId: this.ruleForm.customerId,
                poNo: this.ruleForm.poNo,
                outDepotId: this.ruleForm.outDepotId,
                items: JSON.stringify(itemList)
              }
              const res1 = await saveSaleOrder(opt)
              this.$successMsg('操作成功')
              this.togoSaleEdit(res1.data)
            } catch (error) {
              console.log(error)
            }
            this.canSave = true
            this.tableData.loading = false
          }
        })
      },
      // 跳转销售编辑页
      togoSaleEdit(saleId) {
        if (!saleId) {
          this.$emit('update:isVisible', { show: false })
          return false
        }
        this.$confirm('转销售订单成功，是否进入销售订单编辑页面?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(async () => {
            this.$emit('update:isVisible', { show: false })
            this.linkTo('/sales/salesorderedit?id=' + saleId)
          })
          .catch(() => {
            this.$emit('update:isVisible', { show: false })
          })
      },
      /* 改变销售数量 或者改变 销售单价 得到销售总价，同时触发税率计算 */
      calcAmount(index) {
        const { num, price } = this.tableData.list[index]
        this.tableData.list[index].amount = (num * price).toFixed(2)
        this.calcTax(index)
      },
      /* 改变销售总价 */
      calcSum(index) {
        const { num, amount } = this.tableData.list[index]
        this.tableData.list[index].price = (amount / num).toFixed(8)
      },
      /* 改变税率 */
      calcTax(index) {
        const { taxRate, amount } = this.tableData.list[index]
        if ([null, undefined, 'NaN'].includes(amount)) {
          return
        }
        this.tableData.list[index].tax = (amount * taxRate).toFixed(2)
      },
      /* 改变加成比列 */
      changeRate() {
        let { rate } = this.ruleForm
        rate = rate || 0
        this.tableData.list.forEach((item) => {
          const { baseAmount, num, taxRate } = item
          // 加成在基础总价上
          item.amount = ((1 + Number(rate)) * baseAmount).toFixed(2)
          // 销售单价
          item.price = (item.amount / num).toFixed(8)
          // 改变税额
          item.tax = (Number(taxRate) * item.amount).toFixed(2)
        })
      },
      /* 取采购订单PO号 */
      getOrderPoNo() {
        const { poNo } = this.data
        if (poNo) {
          this.ruleForm.poNo = poNo
          this.$refs.poNo.clearValidate()
        } else {
          this.$errorMsg('当前采购单不存在po号')
        }
      },
      // 改变公司
      async customerChange(customerId) {
        const { ids: id, buId } = this.data
        // 清空加价比例
        this.ruleForm.rate = ''
        try {
          let {
            data: { itemList }
          } = await getCustomerBuIdPrice({ id, customerId })
          // 开启价格管理，禁用加价比例
          const { data: priceManage } = await getSalePriceManage({
            buId,
            customerId
          })
          this.priceManage = priceManage
          if (this.priceManage) {
            if (itemList && itemList.length) {
              itemList = itemList.reduce((pre, cur) => {
                pre[cur.goodsNo] = cur.priceJOSN || []
                return pre
              }, {})
              this.tableData.list.forEach((item) => {
                if (itemList[item.goodsNo] && itemList[item.goodsNo].length) {
                  item.priceManageList = itemList[item.goodsNo].map(
                    (subItem) => ({ value: subItem + '', label: subItem })
                  )
                } else {
                  item.priceManageList = []
                }
                item.price = undefined
                item.amount = undefined
                item.tax = undefined
                if (item.priceManageList.length === 1) {
                  item.price = item.priceManageList[0].value
                  item.amount = (item.price * item.num).toFixed(2)
                  item.tax = (item.taxRate * item.amount).toFixed(2)
                }
              })
            } else {
              this.tableData.list.forEach((item) => {
                item.price = undefined
                item.amount = undefined
                item.tax = undefined
              })
            }
          } else {
            this.init()
          }
        } catch (error) {
          this.ruleForm.customerId = ''
          this.$errorMsg(error.message)
        }
      },
      /* 合计方法 */
      summaryMethod({ data }) {
        const sums = []
        let num = 0
        let amount = 0
        let tax = 0
        data.forEach((item) => {
          num += item.num ? +item.num : 0
          amount += item.amount ? +item.amount : 0
          tax += item.tax ? +item.tax : 0
        })
        sums[0] = '合计: '
        sums[2] = num
        sums[4] = (+amount).toFixed(2)
        sums[6] = (+tax).toFixed(2)
        return sums
      }
    }
  }
</script>
<style lang="scss" scoped>
  .company-page {
    width: 100%;
    margin-bottom: 0px;
    min-height: 200px;
  }
  .dialog-footer {
    height: 30px;
    margin-top: -30px;
  }
  .list-bx {
    width: 100%;
  }

  ::v-deep.toSale .el-form-item__label {
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
</style>
