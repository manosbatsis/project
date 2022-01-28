<template>
  <!-- 销售订单出库编辑页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20 detail-row">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        预申报单号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        出库仓库：{{ detail.outDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        入库仓库：{{ detail.inDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 出库信息 -->
    <JFX-title title="本次出库时间" className="mr-t-10" />
    <JFX-Form :model="ruleForm" :rules="rules" ref="ruleForm" class="mr-b-10">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="本次出库时间：" prop="deliverDate">
            <el-date-picker
              v-model="ruleForm.deliverDate"
              value-format="yyyy-MM-dd"
              type="date"
              style="width: 250px"
              placeholder="选择日期时间"
              clearable
              :picker-options="pickerOptions"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" @click="allUp">整单出库</el-button>
        </el-col>
      </el-row>
    </JFX-Form>
    <JFX-table
      :tableData.sync="tableData"
      :showPagination="false"
      :summary-method="getSummaries"
      showSummary
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
        prop="saleOrderCode"
        align="center"
        width="140"
        label="销售订单号"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.saleOrderCode : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="poNo"
        align="center"
        width="120"
        label="PO单号"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.poNo : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="goodsNo"
        align="center"
        label="商品货号"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.goodsNo : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="goodsName"
        align="center"
        label="商品名称"
        min-width="120"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.goodsName : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="barcode"
        align="center"
        label="条形码"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.barcode : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="num"
        align="center"
        width="100"
        label="申报数量"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.num : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="stayOutNum"
        align="center"
        width="100"
        label="待出库数量"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.stayOutNum : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="salePrice"
        align="center"
        width="100"
        label="销售单价"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column align="center" width="150">
        <template slot="header">
          <span class="need">好品数量</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.transferNum"
            :precision="0"
            :controls="false"
            :min="0"
            label="必填"
            style="width: 100%"
            :disabled="row.stayOutNum === 0"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" width="150">
        <template slot="header">
          <span class="need">坏品数量</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.wornNum"
            :precision="0"
            :controls="false"
            :min="0"
            label="必填"
            style="width: 100%"
            :disabled="row.stayOutNum === 0"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="批次号" width="150">
        <template slot-scope="{ row }">
          <el-input v-model.trim="row.batchNo" clearable />
        </template>
      </el-table-column>
      <el-table-column align="center" label="生产日期" width="150">
        <template slot-scope="{ row }">
          <el-date-picker
            v-model="row.productionDate"
            value-format="yyyy-MM-dd"
            type="date"
            style="width: 350px"
            placeholder="选择日期时间"
            clearable
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="失效日期" width="150">
        <template slot-scope="{ row }">
          <el-date-picker
            v-model="row.overdueDate"
            value-format="yyyy-MM-dd"
            type="date"
            style="width: 350px"
            placeholder="选择日期时间"
            clearable
          />
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 出库信息 end -->
    <div class="flex-c-c">
      <el-button
        @click="handleSubmit"
        type="primary"
        :loading="confirmBtnLoading"
        >保存</el-button
      >
      <el-button @click="closeTag">取销</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    searchDetail,
    saveSaleDeclareOut,
    getDepotDetails
  } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        confirmBtnLoading: false,
        ruleForm: {
          deliverDate: ''
        },
        rules: {
          deliverDate: {
            required: true,
            message: '请选择出库时间',
            trigger: 'change'
          }
        },
        // 详情数据
        detail: {},
        // 日期控件配置
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now()
          }
        }
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
          const { data } = await searchDetail({ id })
          this.detail = data || {}
          const { itemList } = this.detail
          if (itemList && itemList.length) {
            this.tableData.list = itemList.map((item) => ({
              saleOrderId: item.saleOrderId || '',
              saleOrderCode: item.saleOrderCode || '',
              poNo: item.poNo || '',
              goodsId: item.goodsId || '',
              goodsCode: item.goodsCode || '',
              goodsNo: item.goodsNo || '',
              goodsName: item.goodsName || '',
              barcode: item.barcode || '',
              num: item.num || 0,
              transferNum: item.transferNum || 0,
              wornNum: item.wornNum || 0,
              batchNo: item.batchNo || '',
              productionDate: item.productionDate || '',
              overdueDate: item.overdueDate || '',
              stayOutNum: item.num,
              saleItemId: item.saleItemId,
              salePrice: item.salePrice,
              original: true
            }))
          } else {
            this.tableData.list = []
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      /* 增加一行商品 */
      addOneLineProduction(index) {
        const data = this.tableData.list[index]
        this.tableData.list.splice(index + 1, 0, {
          ...data,
          original: false,
          transferNum: 0,
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
      // 整单上架
      allUp() {
        this.tableData.list.forEach((item) => {
          item.transferNum = item.original ? item.stayOutNum : 0
          item.wornNum = 0
        })
      },
      /* 提交表单 */
      handleSubmit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请选择本次出库时间!')
            return false
          }
          const { id } = this.$route.query
          // 获取仓库类型
          const { data: depotData } = await getDepotDetails({
            id: this.detail.outDepotId
          })
          const { type } = depotData
          // 只校验中转仓库存
          if (type === 'd') {
            const { isInventoryValidate } = await this.$checkInventoryNum({
              itemList: this.tableData.list.map((item) => {
                return {
                  goodsId: item.goodsId,
                  goodsNo: item.goodsNo,
                  okNum: item.transferNum || 0,
                  badNum: item.wornNum || 0,
                  batchNo: item.batchNo || '',
                  depotId: this.detail.outDepotId || '',
                  inventoryType: 1
                }
              })
            })
            if (!isInventoryValidate) return false
          }
          const itemList = this.tableData.list.map((item) => ({
            ...item,
            goodsId: item.goodsId + '',
            stayOutNum: item.stayOutNum ? item.stayOutNum.toString() : '0',
            num: item.num ? item.num.toString() : '0',
            transferNum: item.transferNum ? item.transferNum.toString() : '0',
            wornNum: item.wornNum ? item.wornNum.toString() : '0',
            saleItemId: item.saleItemId || ''
          }))
          this.confirmBtnLoading = true
          try {
            await saveSaleDeclareOut({ ...this.ruleForm, id, itemList })
            this.$successMsg('操作成功')
            this.closeTagAndJump('/sales/PreDeclarationList')
          } catch (error) {
            this.$errorMsg(error.message)
          } finally {
            this.confirmBtnLoading = false
          }
        })
      },
      // 计算总和
      getSummaries({ data }) {
        const sums = []
        let num = 0
        let transferNum = 0
        let wornNum = 0
        let stayOutNum = 0
        data.forEach((item) => {
          num += item.num && item.original ? +item.num : 0
          transferNum += item.transferNum ? +item.transferNum : 0
          wornNum += item.wornNum ? +item.wornNum : 0
          stayOutNum += item.stayOutNum && item.original ? +item.stayOutNum : 0
        })
        sums[0] = '合计'
        sums[1] = `${this.tableData.list.length}个SKU`
        sums[6] = num
        sums[7] = stayOutNum
        sums[9] = transferNum
        sums[10] = wornNum
        return sums
      }
    }
  }
</script>

<style lang="scss" scoped>
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
