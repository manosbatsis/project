<template>
  <!-- 选择单据组件 -->
  <JFX-Dialog
    title="选择销售退货PO号页面"
    closeBtnText="取 消"
    :width="'1100px'"
    :top="'80px'"
    :showClose="true"
    :visible="isVisible"
    @comfirm="comfirm"
  >
    <JFX-Form :model="ruleForm" ref="ruleForm" class="search-bx" :rules="rules">
      <el-row :gutter="10" class="page-view">
        <el-col :span="12">
          <el-form-item label="PO单号：" prop="poNos">
            <el-select
              v-model="ruleForm.poNos"
              placeholder="请选择"
              clearable
              filterable
              multiple
              style="width: 250px"
              @change="poNoChange"
            >
              <el-option
                v-for="item in poNoList"
                :key="item.key"
                :label="item.value"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="销售单号：" prop="saleOrderCode">
            <el-input
              v-model="ruleForm.saleOrderCode"
              clearable
              disabled
              style="width: 250px"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="退货方式：" prop="returnMode">
            <el-select
              v-model="ruleForm.returnMode"
              placeholder="请选择"
              clearable
              filterable
              style="width: 250px"
              @change="returnModeChange"
            >
              <el-option label="退货退款" value="1" />
              <el-option label="仅退货" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      showSelection
      ref="rootTable"
      @selection-change="selectionChange"
    >
      <template slot="returnNum" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.returnNum"
          :precision="0"
          :controls="false"
          :min="0"
          style="width: 100%"
          @blur="count('returnNum', $index)"
        ></el-input-number>
      </template>
      <template slot="price" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.price"
          :precision="8"
          v-max-spot="8"
          :controls="false"
          :min="0"
          :disabled="ruleForm.returnMode === '2'"
          style="width: 100%"
          @blur="count('price', $index)"
        ></el-input-number>
      </template>
      <template slot="amount" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.amount"
          :precision="2"
          v-max-spot="2"
          :controls="false"
          :min="0"
          :disabled="ruleForm.returnMode === '2'"
          style="width: 100%"
          @blur="count('amount', $index)"
        ></el-input-number>
      </template>
      <template slot="taxAmount" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.taxAmount"
          :precision="2"
          v-max-spot="2"
          :controls="false"
          :min="0"
          :disabled="ruleForm.returnMode === '2'"
          style="width: 100%"
          @blur="changeTaxAmount($index)"
        ></el-input-number>
      </template>
      <template slot="taxRate" slot-scope="{ row, $index }">
        <el-select
          v-model="row.taxRate"
          placeholder="请选择"
          filterable
          :disabled="ruleForm.returnMode === '2'"
          :data-list="getRaxList('rateList')"
          @change="count('taxRate', $index)"
        >
          <el-option
            v-for="item in selectOpt.rateList"
            :key="item.key"
            :label="item.value"
            :value="item.value"
          />
        </el-select>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { getReturnItemListAndPOList } from '@a/salesReturnManage/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
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
        // 查询数据
        ruleForm: {
          poNos: [],
          saleOrderCode: '',
          saleOrderId: '',
          returnMode: '1'
        },
        rules: {
          saleOrderCode: {
            required: true,
            message: '销售单号不能为空',
            trigger: 'blur'
          },
          returnMode: {
            required: true,
            message: '请选择退货方式',
            trigger: 'change'
          }
        },
        poNoList: [],
        // 表格列数据
        tableColumn: [
          {
            label: 'PO单号',
            prop: 'poNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品名称',
            prop: 'inGoodsName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品条形码',
            prop: 'barcode',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '合计上架量',
            prop: 'shelfNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '退货商品数量',
            slotTo: 'returnNum',
            width: '100',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '退货商品单价',
            slotTo: 'price',
            width: '160',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '退货总金额 \n（不含税）',
            slotTo: 'amount',
            width: '140',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '退货总金额 \n（含税）',
            slotTo: 'taxAmount',
            width: '140',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '税率',
            slotTo: 'taxRate',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '税额',
            prop: 'tax',
            width: '120',
            align: 'center',
            hide: true
          }
        ],
        originList: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id: saleOrderId, code } = this.data
        try {
          const {
            data: { poNoList, returnItemList, saleOrderCode }
          } = await getReturnItemListAndPOList({
            saleOrderId,
            saleOrderCode: code
          })
          this.ruleForm.saleOrderCode = saleOrderCode || ''
          this.ruleForm.saleOrderId = saleOrderId || ''
          if (returnItemList && returnItemList.length) {
            this.tableData.list = returnItemList.map((item) => ({
              ...item,
              shelfNum: item.returnNum || 0,
              taxRate: item.taxRate || '0.00'
            }))
            this.originList = this.tableData.list
          }
          if (poNoList && poNoList.length) {
            this.poNoList = poNoList.map((item) => ({
              key: item,
              value: item
            }))
          }
          this.toggle(this.tableData.list)
        } catch (err) {
          this.$errorMsg(err.message)
        }
      },
      // 提交表单
      async comfirm() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('请至少选择一单！')
          return false
        }
        try {
          this.$refs.ruleForm.validate(async (valid) => {
            if (valid) {
              const isPass = this.checkSumbitData()
              if (!isPass) return false
              const poNo = this.poNoList.length
                ? this.poNoList.map((item) => item.key).join('&')
                : ''
              const data = JSON.stringify({
                ...this.ruleForm,
                poNo,
                itemList: this.tableChoseList
              })
              sessionStorage.setItem('saleReturnToAddData', data)
              this.linkTo('/sales/salesreturnadd?type=purchaseAndSale')
              this.$emit('comfirm')
            } else {
              this.$errorMsg('请填写表单信息')
            }
          })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 退货方式改变
      returnModeChange(returnMode) {
        if (returnMode === '2') {
          this.tableData.list = this.tableData.list.map((item) => ({
            ...item,
            price: 0,
            amount: 0,
            taxAmount: 0,
            taxRate: '0.00',
            tax: 0
          }))
        } else {
          this.tableData.list.forEach((item) => {
            this.originList.forEach((subItem) => {
              if (item.inGoodsId === subItem.inGoodsId) {
                item.price = subItem.price
                item.amount = subItem.amount
                item.taxAmount = subItem.taxAmount
                item.taxRate = subItem.taxRate
                item.tax = subItem.tax
              }
            })
          })
          this.tableData.list.forEach((_, index) => {
            this.count('returnNum', index)
          })
        }
      },
      // 校验参数
      checkSumbitData() {
        let flag = true
        for (let i = 0; i < this.tableChoseList.length; i++) {
          const { returnNum, price, amount, taxAmount, taxRate } =
            this.tableChoseList[i]
          const rowTips = '选中表格的第' + (i + 1) + '行,'

          if (
            price === null ||
            price === undefined ||
            price === '' ||
            price < 0
          ) {
            this.$errorMsg(rowTips + '请填写大于0的退货商品单价!')
            flag = false
            break
          }

          if (returnNum === null || returnNum === undefined || returnNum <= 0) {
            this.$errorMsg(rowTips + '销售商品数量必须是大于0的数!')
            flag = false
            break
          }

          if (taxRate === null || taxRate === undefined) {
            this.$errorMsg(rowTips + '税率不能为空')
            flag = false
            break
          }

          if (
            amount === null ||
            amount === undefined ||
            amount === '' ||
            amount < 0
          ) {
            this.$errorMsg(rowTips + '请填写大于0的退货总金额（不含税）')
            flag = false
            break
          }

          if (
            taxAmount === null ||
            taxAmount === undefined ||
            taxAmount === '' ||
            taxAmount < 0
          ) {
            this.$errorMsg(rowTips + '请填写大于0的退货总金额（含税）')
            flag = false
            break
          }
        }
        return flag
      },
      // 计算
      count(type, index) {
        const data = this.tableData.list[index]
        let { price, amount, taxRate, tax, taxAmount, returnNum, badGoodsNum } =
          data
        price = price || 0
        amount = amount || 0
        taxRate = taxRate ? +taxRate : 0
        taxAmount = taxAmount || 0
        tax = tax || 0
        returnNum = returnNum || 0
        badGoodsNum = badGoodsNum || 0
        // 好品坏品改变 计算总数量
        if (type === 'badGoodsNum' || type === 'returnNum') {
          // 旧数据存在坏品需要累加
          returnNum = returnNum + badGoodsNum
          // 计算总金额
          amount = (returnNum * price).toFixed(2)
        }
        // 单价(不含税)改变
        if (type === 'price') {
          amount = (price * returnNum).toFixed(2)
        }
        // 退货总金额(不含税) 改变时 重新计算退货单价(不含税)
        if (type === 'amount' && returnNum) {
          price = (amount / returnNum).toFixed(8)
        }
        // 计算总金额(含税)
        taxAmount = (amount * (1 + taxRate)).toFixed(2)
        // 计算税额
        tax = (taxAmount - amount).toFixed(2)
        this.tableData.list.splice(index, 1, {
          ...data,
          amount,
          taxAmount,
          price,
          tax,
          returnNum
        })
        this.toggle(this.tableData.list)
      },
      // 总金额（含税输入）
      changeTaxAmount(index) {
        const data = this.tableData.list[index]
        let {
          returnNum = 0,
          price = 0,
          amount,
          taxRate = 0,
          tax = 0,
          taxAmount
        } = data
        price = price || 0
        amount = amount || 0
        taxRate = taxRate ? +taxRate : 0 // 转为数字类型
        taxAmount = taxAmount || 0
        // 重新计算退货总金额(不含税)
        amount = (taxAmount / (1 + taxRate)).toFixed(2)
        // 重新计算税额
        tax = (taxAmount - amount).toFixed(2)
        if (returnNum) {
          // 重新计算单价
          price = (amount / returnNum).toFixed(8)
        }
        this.tableData.list.splice(index, 1, {
          ...data,
          amount,
          tax,
          taxAmount,
          price
        })
        this.toggle(this.tableData.list)
      },
      // po号改变
      async poNoChange() {
        const { id: saleOrderId, code: saleOrderCode } = this.data
        const poNos = this.ruleForm.poNos.length
          ? this.ruleForm.poNos.toString()
          : ''
        try {
          const {
            data: { returnItemList }
          } = await getReturnItemListAndPOList({
            saleOrderId,
            saleOrderCode,
            poNos
          })
          if (returnItemList && returnItemList.length) {
            if (this.ruleForm.returnMode === '2') {
              this.tableData.list = returnItemList.map((item) => ({
                ...item,
                shelfNum: item.returnNum || 0,
                price: 0,
                amount: 0,
                taxAmount: 0,
                taxRate: '0.00',
                tax: 0
              }))
            } else {
              this.tableData.list = returnItemList.map((item) => ({
                ...item,
                shelfNum: item.returnNum || 0,
                taxRate: item.taxRate || '0.00'
              }))
            }
          } else {
            this.tableData.list = []
          }
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.toggle(this.tableData.list)
        }
      },
      toggle(data) {
        if (data.length) {
          this.$nextTick(() => {
            data.forEach((item) => {
              this.$refs.rootTable.$refs['el-table'].toggleRowSelection(
                item,
                true
              )
            })
          })
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
  ::v-deep.search-bx .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 120px;
  }
  ::v-deep.search-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
