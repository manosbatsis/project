<template>
  <div class="page-bx bgc-w edit-bx">
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 面包屑 -->
      <JFX-Breadcrumb :showGoback="true" />
      <!-- 面包屑 end -->
      <!-- title -->
      <JFX-title title="基本信息" className="mr-t-10" />
      <el-row :gutter="10" class="fs-12 clr-II detail-row">
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          采购订单编号：{{ detail.code || '- -' }}
        </el-col>
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          入库仓库：{{ detail.depotName || '- -' }}
        </el-col>
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          PO号：{{ detail.poNo || '- -' }}
        </el-col>
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          事业部：{{ detail.buName || '- -' }}
        </el-col>
        <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          供应商：{{ detail.supplierName || '- -' }}
        </el-col>
      </el-row>
      <JFX-title title="入库信息" className="mr-t-10" />
      <el-row :gutter="16" class="fs-14 clr-II">
        <el-col :span="8">
          <el-form-item label="入仓仓库：" prop="depotId">
            <el-select
              v-model="ruleForm.depotId"
              placeholder="请选择"
              filterable
              clearable
              @change="changeGoodsInfoByDepot"
              :data-list="getSelectBeanByMerchantRel('warehouseList')"
            >
              <el-option
                v-for="item in selectOpt.warehouseList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="本次入库时间：" prop="inboundDate">
            <el-date-picker
              v-model="ruleForm.inboundDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item>
            <el-button type="primary" @click="allInputDepot">
              整单入库
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 表格 -->
      <JFX-table
        class="mr-t-20"
        :tableData.sync="tableData"
        :showPagination="false"
        :showSummary="true"
        :summaryMethod="getSummaries"
        @selection-change="selectionChange"
      >
        <el-table-column width="80" label="操作" align="center" fixed="left">
          <template slot-scope="{ row, $index }">
            <div
              class="action-icon"
              :class="
                row.original ? 'action-icon--not-allow' : 'action-icon--allow'
              "
              @click="removeOneLineProduction(row.original, $index)"
            >
              <i class="el-icon-minus"></i>
            </div>
            <div
              class="action-icon action-icon--allow mr-b-10"
              @click="addOneLineProduction($index)"
            >
              <i class="el-icon-plus"></i>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          type="index"
          width="50"
          label="序号"
          align="center"
        ></el-table-column>
        <el-table-column
          label="商品货号"
          align="center"
          min-width="150"
          show-overflow-tooltip
        >
          <template slot-scope="{ row, $index }">
            <template v-if="row.goodsNo">
              <div>{{ row.original ? row.goodsNo : '' }}</div>
              <el-button
                v-if="row.original"
                type="primary"
                @click="showChooseProductModal(row.barcode, $index)"
              >
                修改货号
              </el-button>
            </template>
            <span v-else style="color: red">未关联仓库</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="goodsName"
          label="商品名称"
          min-width="120"
          align="center"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.goodsName : '' }}
          </template>
        </el-table-column>
        <el-table-column
          label="条形码"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.barcode : '' }}
          </template>
        </el-table-column>
        <el-table-column
          label="采购单价"
          align="center"
          width="90"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.price : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="num" label="采购数量" align="center" width="90">
          <template slot-scope="{ row }">
            {{ row.original ? row.num : '' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="unInwarehouseNum"
          label="待入库数量"
          align="center"
          width="90"
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.unInwarehouseNum : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="normalNum" align="center" width="130">
          <template slot="header">
            <span class="need">好品数量</span>
          </template>
          <template slot-scope="scope">
            <el-input-number
              v-model.trim="scope.row.normalNum"
              :min="0"
              clearable
              :controls="false"
              style="width: 100%"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column
          prop="wornNum"
          label="损货数量"
          align="center"
          width="130"
        >
          <template slot="header">
            <span class="need">坏品数量</span>
          </template>
          <template slot-scope="scope">
            <el-input-number
              v-model.trim="scope.row.wornNum"
              :min="0"
              clearable
              :controls="false"
              style="width: 100%"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="批次号" align="center" width="160">
          <template slot-scope="scope">
            <el-input
              v-model.trim="scope.row.batchNo"
              clearable
              style="width: 100%"
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column label="生产日期" align="center" width="160">
          <template slot-scope="scope">
            <el-date-picker
              v-model="scope.row.productionDate"
              type="date"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </template>
        </el-table-column>
        <el-table-column label="失效日期" align="center" width="160">
          <template slot-scope="scope">
            <el-date-picker
              v-model="scope.row.overdueDate"
              type="date"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </template>
        </el-table-column>
      </JFX-table>
      <!-- 表格 end-->
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="onSumbit" :loading="saveLoading">
        保 存
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>

    <!-- 选择商品 -->
    <ChooseProduct
      v-if="chooseProduct.visible.show"
      :isVisible="chooseProduct.visible"
      :data="chooseProduct.data"
      :isRadio="chooseProduct.isRadio"
      @comfirm="comfirmEditGoodsNo"
    />
    <!-- 选择商品 end -->
  </div>
</template>
<script>
  import {
    getPurchaseOrderDetails,
    purchaseDelivery
  } from '@a/purchaseManage/index'
  import { getOrderUpdateMerchandiseInfo } from '@a/base'
  import commomMix from '@m/index'
  export default {
    components: {
      /* 选择商品组件 */
      ChooseProduct: () => import('@c/choseGoods/index')
    },
    mixins: [commomMix],
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          inboundDate: '',
          depotId: ''
        },
        /* 表单校验 */
        rules: {
          inboundDate: {
            required: true,
            message: '入库时间不能为空',
            trigger: 'change'
          },
          depotId: {
            required: true,
            message: '请入仓仓库不能为空',
            trigger: 'change'
          }
        },
        /* 回响的数据 */
        detail: {},
        /* 保持按钮的loading */
        saveLoading: false,
        /* 选择商品组件状态 */
        chooseProduct: {
          visible: { show: false },
          data: {},
          isRadio: true
        },
        /* 点击修改货号的索引 */
        editGoodsNoIndex: 0
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        if (!id) return false
        try {
          const { data } = await getPurchaseOrderDetails({ id })
          this.detail = data
          if (this.detail.itemList && this.detail.itemList.length) {
            this.tableData.list = this.detail.itemList.map((item) => {
              const { unInwarehouseNum } = item
              return {
                ...item,
                purchaseItemId: item.id,
                wornNum: 0,
                normalNum: unInwarehouseNum || 0,
                goodsNo: '' /* 清空采购单的货号 */,
                original: true /* 表示是初始的数据，非新增 */
              }
            })
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 增加一行商品 */
      addOneLineProduction(index) {
        const data = this.tableData.list[index]
        this.tableData.list.splice(index + 1, 0, {
          ...data,
          original: false,
          normalNum: 0,
          wornNum: 0,
          batchNo: '',
          productionDate: '',
          overdueDate: ''
        })
      },
      /* 删除一行商品 */
      removeOneLineProduction(isOriginal, index) {
        /* 如果是初始数据不能删除 */
        if (isOriginal) return false
        this.tableData.list.splice(index, 1)
      },
      /* 保存前校验 */
      beforeSumbit() {
        let isPassed = true
        /* 原始的商品列表 */
        const originalList = this.tableData.list.filter((item) => item.original)
        /* 添加的商品列表 */
        const addedList = this.tableData.list.filter((item) => !item.original)
        /* 添加的商品列表map 用于累加数量 */
        let addedListMap = {}
        if (addedList.length) {
          addedListMap = addedList.reduce((pre, cur) => {
            if (pre[cur.purchaseItemId]) {
              pre[cur.purchaseItemId].push(cur)
            } else {
              pre[cur.purchaseItemId] = [cur]
            }
            return pre
          }, {})
        }
        /* 循环校验 */
        for (let i = 0; i < originalList.length; i++) {
          const item = originalList[i]
          let normalNum = 0
          let wornNum = 0
          /* 如果存在添加的商品 将好品和坏品数量累加 */
          if (addedListMap[item.purchaseItemId]) {
            addedListMap[item.purchaseItemId].forEach((subItem) => {
              normalNum += subItem.normalNum || 0
              wornNum += subItem.wornNum || 0
            })
          }
          normalNum += item.normalNum || 0
          wornNum += item.wornNum || 0
          if (normalNum + wornNum > item.unInwarehouseNum) {
            this.$errorMsg(
              `商品货号：${item.goodsNo}，单价：${item.price} 入库量大于待入库数量，待入库量：${item.unInwarehouseNum}，不能进行打托入库!`
            )
            isPassed = false
            break
          }
        }
        return isPassed
      },
      /* 切换仓库修改商品信息 */
      async changeGoodsInfoByDepot(depotId) {
        if (!depotId) return
        const itemList = this.tableData.list.map((item, index) => ({
          barcode: item.barcode,
          index
        }))
        try {
          const { data } = await getOrderUpdateMerchandiseInfo({
            depotId,
            orderType: 1,
            itemList
          })
          this.tableData.list.forEach((item, index) => {
            const { id, name, goodsNo } = data[index].merchandiseInfoModel || {}
            item.goodsId = id || ''
            item.goodsName = name || ''
            item.goodsNo = goodsNo || ''
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 显示选择商品弹窗 */
      showChooseProductModal(barcode, index) {
        const { depotId } = this.ruleForm
        if (!depotId) {
          this.$errorMsg('请先选择入仓仓库!')
          this.$refs.ruleForm.validateField(['depotId'])
          return false
        }
        this.chooseProduct.data = {
          depotId,
          barcode,
          popupType: 1
        }
        this.editGoodsNoIndex = index
        this.chooseProduct.visible.show = true
      },
      /* 确认修改货号 */
      comfirmEditGoodsNo(payload) {
        if (payload && payload.length) {
          const item = payload[0]
          const id = this.tableData.list[this.editGoodsNoIndex].id
          const list = this.tableData.list.filter((item) => item.id === id)
          list.forEach((subItem) => {
            subItem.goodsId = item.id || ''
            subItem.goodsName = item.name || ''
            subItem.goodsNo = item.goodsNo || ''
          })
        }
        this.chooseProduct.visible.show = false
      },
      /* 整托出库 */
      allInputDepot() {
        this.tableData.list.forEach((item) => {
          item.normalNum = item.original ? item.unInwarehouseNum : 0
          item.wornNum = 0
        })
      },
      /* 保存 */
      async onSumbit() {
        const { inboundDate, depotId } = this.ruleForm
        if (!inboundDate || !depotId) {
          this.$errorMsg('请正确填写表单信息')
          this.$refs.ruleForm.validateField(['inboundDate', 'depotId'])
          return false
        }
        /* 校验商品 */
        const isPassed = this.beforeSumbit()
        if (!isPassed) return false
        const itemList = this.tableData.list.map((item) => ({
          ...item,
          expireNum: 0,
          wornNum: item.wornNum || 0,
          normalNum: item.normalNum || 0
        }))
        try {
          this.saveLoading = true
          await purchaseDelivery({
            purchaseId: this.detail.id,
            ...this.ruleForm,
            itemList
          })
          this.$successMsg('保存成功')
          this.closeTag()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.saveLoading = false
        }
      },
      /* 合计 */
      getSummaries({ data }) {
        const sums = []
        const len = this.tableData.list.length
        let num = 0
        let unInwarehouseNum = 0
        let normalNum = 0
        let wornNum = 0
        data.forEach((item) => {
          num += item.num && item.original ? +item.num : 0
          unInwarehouseNum +=
            item.unInwarehouseNum && item.original ? +item.unInwarehouseNum : 0
          normalNum += item.normalNum ? +item.normalNum : 0
          wornNum += item.wornNum ? +item.wornNum : 0
        })
        sums[2] = `合计：${len}个SKU`
        sums[6] = num
        sums[7] = unInwarehouseNum
        sums[8] = normalNum
        sums[9] = wornNum
        return sums
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.edit-bx .el-form-item__label {
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
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  /* 增加减少商品icon */
  .action-icon {
    font-size: 26px;
    font-weight: bolder;
    &--allow {
      color: #6ea9f3;
      cursor: pointer;
    }
    &--not-allow {
      color: #666;
      cursor: not-allowed;
    }
  }
</style>
