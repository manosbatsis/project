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
          预申报单号：{{ detail.code || '- -' }}
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
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="本次入库时间：" prop="inboundDate">
            <el-date-picker
              v-model="ruleForm.inboundDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期时间"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="mr-t-20 mr-b-15">
        <el-button type="primary" @click="allInDepot">整托入库</el-button>
      </div>
      <!-- 表格 -->
      <JFX-table
        :tableData.sync="tableData"
        @selection-change="selectionChange"
        :showPagination="false"
        :showSummary="true"
        :summaryMethod="getSummaries"
      >
        <el-table-column
          type="index"
          width="80"
          label="操作"
          align="center"
          fixed="left"
        >
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
          label="采购订单"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.purchase : '' }}
          </template>
        </el-table-column>
        <el-table-column
          label="PO号"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.poNo : '' }}
          </template>
        </el-table-column>
        <el-table-column
          label="商品货号"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.goodsNo : '' }}
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
          prop="price"
          label="申报单价"
          align="center"
          min-width="80"
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.price : '' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="num"
          label="申报数量"
          align="center"
          width="120"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.original ? row.num : '' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="normalNum"
          label="好品数量"
          align="center"
          width="120"
        >
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
          width="120"
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
        <el-table-column label="批次号" align="center" min-width="130">
          <template slot-scope="scope">
            <el-input
              v-model.trim="scope.row.batchNo"
              clearable
              style="width: 100%"
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column label="生产日期" align="center" min-width="150">
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
        <el-table-column label="失效日期" align="center" min-width="150">
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
  </div>
</template>
<script>
  import { declareDelivery, getDeclareDetail } from '@a/purchaseManage/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        ruleForm: {
          inboundDate: ''
        },
        rules: {
          inboundDate: {
            required: true,
            message: '请选择入库时间',
            trigger: 'change'
          }
        },
        detail: {},
        saveLoading: false
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
          const { data } = await getDeclareDetail({ id })
          this.detail = data
          if (data.itemList && data.itemList.length) {
            this.tableData.list = data.itemList.map((item) => ({
              ...item,
              wornNum: 0,
              normalNum: 0,
              original: true /* 表示是初始的数据，非新增 */
            }))
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
      /* 整单上架 */
      allUp() {
        this.tableData.list.forEach((item) => {
          item.transferNum = item.original ? item.stayOutNum : 0
          item.wornNum = 0
        })
      },
      /* 保存 */
      async onSumbit() {
        if (!this.ruleForm.inboundDate) {
          this.$errorMsg('入库时间不能为空')
          return false
        }
        const itemList = this.tableData.list.map((item) => ({
          ...item,
          expireNum: 0
        }))
        try {
          this.saveLoading = true
          await declareDelivery({
            declareOrderId: this.detail.id,
            inboundDate: this.ruleForm.inboundDate,
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
      // 整托入库
      allInDepot() {
        this.tableData.list.forEach((item) => {
          item.normalNum = item.original ? item.num : 0
          item.wornNum = 0
        })
      },
      /* 合计 */
      getSummaries({ data }) {
        const sums = []
        const len = this.tableData.list.length
        let num = 0
        let normalNum = 0
        let wornNum = 0
        data.forEach((item) => {
          num += item.num && item.original ? +item.num : 0
          normalNum += item.normalNum ? +item.normalNum : 0
          wornNum += item.wornNum ? +item.wornNum : 0
        })
        sums[1] = `合计：${len}个SKU`
        sums[7] = num
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
  .image-preview {
    display: inline-block;
  }
  .title-bx {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .last-bx {
    position: relative;
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
