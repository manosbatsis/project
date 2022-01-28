<template>
  <!-- 销售订单出库编辑页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        销售订单号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        出库仓库：{{ detail.outDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        入库仓库：{{ detail.inDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        销售类型 {{ detail.businessModelLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        PO号：{{ detail.poNo || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 出库信息 -->
    <JFX-title title="出库信息" className="mr-t-10" />
    <JFX-Form :model="ruleForm" :rules="rules" ref="ruleForm" class="mr-b-10">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="出库时间：" prop="returnOutDate">
            <el-date-picker
              v-model="ruleForm.returnOutDate"
              value-format="yyyy-MM-dd"
              type="date"
              style="width: 220px"
              placeholder="选择日期时间"
              clearable
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
        prop="goodsNo"
        align="center"
        label="商品货号"
        width="140"
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
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.barcode : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="num"
        align="center"
        width="120"
        label="销售数量"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.num : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="stayOutNum"
        align="center"
        width="120"
        label="待出库数量"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.original ? row.stayOutNum : '' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="price"
        align="center"
        width="110"
        label="销售单价"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column align="center" width="130">
        <template slot="header">
          <span class="need">好品出库量</span>
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
      <el-table-column align="center" width="130">
        <template slot="header">
          <span class="need">坏品出库量</span>
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
          <el-input v-model.trim="row.transferBatchNo" clearable />
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
      <el-button @click="handleSubmit" type="primary">保存</el-button>
      <el-button @click="closeTag">取销</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { toSaleOutPage, saveSaleOut, getDepotDetails } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        ruleForm: {
          returnOutDate: ''
        },
        rules: {
          returnOutDate: {
            required: true,
            message: '请选择出库时间',
            trigger: 'change'
          }
        },
        // 详情数据
        detail: {},
        depotDetail: {}
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
          const { data } = await toSaleOutPage({ id })
          this.detail = data || {}
          const { itemList } = this.detail
          if (itemList && itemList.length) {
            this.tableData.list = itemList.map((item) => ({
              goodsId: item.goodsId || '',
              goodsCode: item.goodsCode || '',
              goodsNo: item.goodsNo || '',
              goodsName: item.goodsName || '',
              barcode: item.barcode || '',
              num: item.num || 0,
              transferNum: item.transferNum || 0,
              wornNum: item.wornNum || 0,
              transferBatchNo: item.transferBatchNo || '',
              productionDate: item.productionDate || '',
              overdueDate: item.overdueDate || '',
              stayOutNum: item.stayOutNum,
              price: item.price || '',
              id: item.id,
              original: true /* 表示是初始的数据，非新增 */
            }))
            /* 查询仓库信息 */
            const { data: depotDetail } = await getDepotDetails({
              id: this.detail.outDepotId || ''
            })
            this.depotDetail = depotDetail
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
          transferBatchNo: '',
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
      /* 校验出库列表中  同一商品id (item.id) 下  好品出库量 + 坏品出库量 < 待出库数量 */
      beforeSubmit() {
        let isPass = true
        /** 按照item.id 分组 */
        const groupMap = this.tableData.list.reduce((group, item) => {
          const key = item.id
          if (group[key]) {
            group[key].push(item)
          } else {
            group[key] = [item]
          }
          return group
        }, {})
        /** 每一组进行 好品坏品累计  后 和待出库数量校验 */
        for (const id in groupMap) {
          const curGroup = groupMap[id]
          const { stayOutNum, goodsNo, price, transferBatchNo } = curGroup[0]
          //  坏品
          let wornNum = 0
          //  好品
          let transferNum = 0
          //  累加
          curGroup.forEach((item) => {
            wornNum += item.wornNum || 0
            transferNum += item.transferNum || 0
          })
          // 校验
          if (transferNum + wornNum > stayOutNum) {
            this.$errorMsg(
              `货号：${goodsNo}，单价：${price}，批次号：${transferBatchNo}当前出库量大于待出库量，待出库量：${stayOutNum}，不能进行出库打托!`
            )
            isPass = false
            break
          }
        }
        return isPass
      },
      // 提交表单
      handleSubmit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请填写出库时间！')
            return false
          }
          // 校验商品
          const isPassed = this.beforeSubmit()
          if (!isPassed) return false
          // 中转仓校验库存
          if (this.depotDetail.type === 'd') {
            const { isInventoryValidate } = await this.$checkInventoryNum({
              itemList: this.tableData.list.map((item) => {
                return {
                  goodsId: item.goodsId,
                  depotId: this.depotDetail.id,
                  goodsNo: item.goodsNo,
                  okNum: item.transferNum,
                  badNum: item.wornNum,
                  batchNo: item.batchNo || '',
                  inventoryType: 1
                }
              })
            })
            // 库存校验不通过
            if (!isInventoryValidate) {
              return false
            }
          }
          const itemList = this.tableData.list.map((item) => ({
            ...item,
            productionDate: '',
            productionDateStr: item.productionDate || '',
            overdueDate: '',
            overdueDateStr: item.overdueDate || '',
            stayOutNum: item.stayOutNum ? +item.stayOutNum : 0,
            num: item.num ? +item.num : 0,
            transferNum: item.transferNum ? +item.transferNum : 0,
            wornNum: item.wornNum ? +item.wornNum : 0,
            saleItemId: item.id || ''
          }))
          await saveSaleOut({
            ...this.ruleForm,
            orderId: this.$route.query.id,
            itemList
          })
          this.$successMsg('操作成功')
          this.closeTag()
          this.linkTo('/sales/salesorder')
        })
      },
      /* 合计 */
      getSummaries({ data }) {
        const sums = []
        let num = 0
        let transferNum = 0
        let wornNum = 0
        let stayOutNum = 0
        data.forEach((item) => {
          num += item.num && item.original ? +item.num : 0
          stayOutNum += item.stayOutNum && item.original ? +item.stayOutNum : 0
          transferNum += item.transferNum ? +item.transferNum : 0
          wornNum += item.wornNum ? +item.wornNum : 0
        })
        sums[0] = '合计：'
        sums[1] = `${this.tableData.list.length}个SKU`
        sums[4] = num
        sums[5] = stayOutNum
        sums[7] = transferNum
        sums[8] = wornNum
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
