<template>
  <!-- 应收账单新建页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <JFX-Form :model="baseForm" ref="baseForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="分类：" prop="sortType">
            <el-select
              v-model="baseForm.sortType"
              placeholder="请选择"
              clearable
              filterable
              @change="sortTypeChange"
              :data-list="getSelectList('receiveBill_sortTypeList')"
            >
              <el-option
                v-for="item in selectOpt.receiveBill_sortTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="客户："
            prop="customerId"
            class="search-panel-item fs-14 clr-II"
          >
            <el-select
              v-model="baseForm.customerId"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getCustomerSelectBean('customerList')"
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
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="入账月份："
            prop="billMonth"
            class="search-panel-item fs-14 clr-II"
          >
            <el-date-picker
              v-model="baseForm.billMonth"
              :clearable="false"
              :format="'yyyy-MM'"
              :value-format="'yyyy-MM'"
              type="month"
              placeholder="选择日期时间"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="事业部："
            prop="buId"
            class="search-panel-item fs-14 clr-II"
          >
            <el-select
              v-model="baseForm.buId"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getBUSelectBean('buIdList')"
            >
              <el-option
                v-for="item in selectOpt.buIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="结算币种：" prop="currency">
            <el-select
              v-model="baseForm.currency"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getCurrencySelectBean('currency_list')"
            >
              <el-option
                v-for="item in selectOpt.currency_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button type="primary" @click="add">添加补扣款</el-button>
        <el-button type="primary" @click="importData">导入</el-button>
        <el-button type="primary" @click="del">删除</el-button>
      </el-col>
    </el-row>
    <JFX-table :tableData.sync="tableData" @selection-change="selectionChange">
      <el-table-column
        type="selection"
        align="center"
        width="55"
      ></el-table-column>
      <el-table-column prop="projectName" align="center" min-width="100">
        <template slot="header">
          <span class="need">费用项目</span>
        </template>
        <template slot-scope="{ row, $index }">
          <el-input
            readonly
            @click.native="selectProject(row, $index)"
            v-model="row.projectName"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="billType" align="center" min-width="100">
        <template slot="header">
          <span class="need">类型</span>
        </template>
        <template slot-scope="{ row }">
          <el-select v-model="row.billType" clearable>
            <el-option
              v-for="item in billTypeList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="poNo" align="center" min-width="100" label="PO号">
        <template slot-scope="{ row }">
          <el-input v-model="row.poNo"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="invoiceDescription"
        align="center"
        min-width="100"
        label="发票描述"
      >
        <template slot-scope="{ row }">
          <el-input v-model="row.invoiceDescription"></el-input>
        </template>
      </el-table-column>
      <el-table-column align="center" min-width="100" label="商品货号">
        <template slot-scope="{ row, $index }">
          {{ row.goodsNo }}
          <el-button type="primary" @click="selectGood(row, $index)">
            选择商品
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="goodsName"
        align="center"
        min-width="100"
        label="商品名称"
      ></el-table-column>
      <el-table-column align="center" min-width="100">
        <template slot="header">
          <span class="need">母品牌</span>
        </template>
        <template slot-scope="{ row }">
          <el-select
            v-model="row.superiorParentBrandId"
            placeholder="请选择"
            clearable
            filterable
          >
            <el-option
              v-for="item in selectOpt.brandList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column align="center" min-width="100" label="数量">
        <template slot-scope="{ row }">
          <el-input-number
            style="width: 100%"
            :controls="false"
            v-model="row.num"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" min-width="100">
        <template slot="header">
          <span class="need">税率</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            style="width: 100%"
            :controls="false"
            :precision="2"
            v-model="row.taxRate"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" min-width="100">
        <template slot="header">
          <span class="need">费用金额（含税）</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            style="width: 100%"
            :controls="false"
            :precision="2"
            v-model="row.taxAmount"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" min-width="110">
        <template slot="header">
          <span id="storePlatformListHeader">核销平台</span>
        </template>
        <template slot-scope="{ row }">
          <el-select
            v-model="row.storePlatformCode"
            placeholder="请选择"
            clearable
            filterable
            :disabled="baseForm.sortType !== '2'"
          >
            <el-option
              v-for="item in selectOpt.storePlatformList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="remark"
        align="center"
        min-width="100"
        label="备注"
      >
        <template slot-scope="{ row }">
          <el-input v-model="row.remark"></el-input>
        </template>
      </el-table-column>
    </JFX-table>
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="comfirm">保 存</el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
    <!-- 选择费项 start -->
    <ChooseSettlement
      v-if="settlement.visible.show"
      :settlementVisible="settlement.visible"
      :customerId="settlement.customerId"
      @comfirm="selectProjectAfter"
    ></ChooseSettlement>
    <!-- 选择费项 end -->
    <ChoseProducts
      v-if="products.visible.show"
      :visible="products.visible"
      @comfirm="selectGoodAfter"
      :filterData="products.filterData"
    ></ChoseProducts>
    <!-- 导入费用明细 -->
    <JFX-Dialog
      :visible.sync="importItems.visible"
      :showClose="true"
      :width="'860px'"
      :top="'3vh'"
      title="费用明细导入"
      :showFooter="false"
      @comfirm="importItems.visible.show = false"
    >
      <!-- 费用明细导入 -->
      <ImportFile
        :url="importAddReceiveItemsUrl"
        :selfDown="true"
        :filterData="importItems.filterData"
        :downText="'明细导出'"
        @downup="downItemsTemplate"
        @successUpload="importItemsSuccess"
      ></ImportFile>
    </JFX-Dialog>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    importAddReceiveItemsUrl,
    getBrandParent,
    saveAddReceiveBill,
    exportReceiveCostItemsUrl
  } from '@a/receiveMoneyManage/index'
  import { exportFilePost } from '@u/common'
  export default {
    mixins: [commomMix],
    components: {
      // 选择费项
      ChooseSettlement: () => import('./components/ChooseSettlement'),
      // 选择商品
      ChoseProducts: () => import('@c/choseProducts/index'),
      // 导入组件
      ImportFile: () => import('@/components/importfile/index')
    },
    data() {
      return {
        importAddReceiveItemsUrl,
        baseForm: {
          sortType: '',
          customerId: '',
          billMonth: '',
          buId: '',
          currency: ''
        },
        rules: {
          sortType: {
            required: true,
            message: '请选择分类',
            trigger: 'change'
          },
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          billMonth: {
            required: true,
            message: '请选择账单月份',
            trigger: 'change'
          },
          buId: { required: true, message: '请选择事业部', trigger: 'change' },
          currency: {
            required: true,
            message: '请选择结算币种',
            trigger: 'change'
          }
        },
        billTypeList: [
          { key: '0', value: '补款' },
          { key: '1', value: '扣款' }
        ],
        settlement: {
          visible: { show: false },
          customerId: ''
        },
        products: {
          visible: { show: false },
          filterData: {}
        },
        importItems: {
          visible: { show: false },
          filterData: {}
        }
      }
    },
    mounted() {
      this.getBrandList('brandList')
      this.getSelectList('storePlatformList')
    },
    methods: {
      // 分类改变
      sortTypeChange() {
        // 清空所有核销平台
        this.tableData.list.forEach((item) => {
          item.storePlatformList = ''
        })
        this.$nextTick(() => {
          if (this.baseForm.sortType === '2') {
            document
              .querySelector('#storePlatformListHeader')
              .classList.add('need')
          } else {
            document
              .querySelector('#storePlatformListHeader')
              .classList.remove('need')
          }
        })
      },
      // 选择费项
      selectProject(row, index) {
        if (!this.baseForm.customerId) {
          return this.$alertWarning('请先选择客户')
        }
        this.settlement.visible.show = true
        this.settlement.customerId = this.baseForm.customerId
        this.settlement.row = row
        this.settlement.index = index
      },
      // 选择费项回调
      selectProjectAfter(selectProject) {
        this.settlement.visible.show = false
        const data = this.tableData.list[this.settlement.index]
        data.projectName = selectProject.projectName
        data.projectId = selectProject.id
      },
      // 选择商品
      selectGood(row, index) {
        this.products.visible.show = true
        this.products.row = row
        this.products.index = index
      },
      // 选择商品回调
      async selectGoodAfter(selectGood) {
        if (selectGood.length !== 1) {
          return this.$alertWarning(
            selectGood.length === 0 ? '请选择一条商品' : '只能选择一个商品'
          )
        }
        this.products.visible.show = false
        const setData = selectGood[0]
        const data = this.tableData.list[this.products.index]
        data.goodsId = setData.id
        data.goodsNo = setData.goodsNo
        data.goodsName = setData.name
        const { data: brandRes } = await getBrandParent({
          commbarcode: setData.commbarcode
        })
        data.superiorParentBrandId = brandRes.superiorParentBrandId
          ? String(brandRes.superiorParentBrandId)
          : ''
      },
      // 导入费项成功
      importItemsSuccess(res) {
        if (res.data.failure + '' !== '0') {
          return false
        }
        this.importItems.visible.show = false
        const importList = res.data.costItemList
        // 先清空
        this.tableData.list = []
        importList.forEach((item) => {
          item.isEdit = Math.random()
          item.num = [null, ''].includes(item.num) ? undefined : item.num // 导入数量可以为空
          item.superiorParentBrandId = item.parentBrandId
            ? String(item.parentBrandId)
            : ''
          this.tableData.list.push(item)
        })
      },
      // 新增记录
      add() {
        this.tableData.list.push({
          projectId: '',
          projectName: '',
          billType: '',
          poNo: '',
          invoiceDescription: '',
          goodsId: '',
          goodsNo: '',
          goodsName: '',
          superiorParentBrandId: '',
          num: undefined, // 默认为空，不为0
          taxRate: '',
          taxAmount: '',
          storePlatformCode: '',
          platformGoodsCode: '',
          remark: '',
          isEdit: Math.random()
        })
      },
      // 删除
      del() {
        const choseList = this.tableChoseList.map((item) => item.isEdit)
        if (!choseList.length) {
          return this.$alertWarning('请选择一条数据')
        }
        this.tableData.list = this.tableData.list.filter((item) => {
          return !choseList.includes(item.isEdit)
        })
      },
      // 导入
      importData() {
        if (!this.baseForm.customerId) {
          return this.$alertWarning('请先选择客户')
        }
        this.importItems.visible.show = true
        this.importItems.filterData = { customerId: this.baseForm.customerId }
      },
      // 下载导出明细
      downItemsTemplate() {
        const itemList = this.tableData.list.map((goods) => {
          var item = {}
          item.projectId = goods.projectId || ''
          item.billType = goods.billType || ''
          item.poNo = goods.poNo || ''
          item.invoiceDescription = goods.invoiceDescription || ''
          item.goodsNo = goods.goodsNo || ''
          item.taxRate = goods.taxRate ? String(goods.taxRate) : ''
          item.num = goods.num ? String(goods.num) : ''
          item.taxAmount = goods.taxAmount ? String(goods.taxAmount) : ''
          item.remark = goods.remark || ''
          item.brandParent = goods.superiorParentBrandId || ''
          item.platformGoodsCode = goods.platformGoodsCode || ''
          return item
        })
        console.log(itemList)
        exportFilePost(exportReceiveCostItemsUrl, {
          json: JSON.stringify({ itemList })
        })
      },
      // 获取参数
      getParam() {
        var json = {}
        json.sortType = this.baseForm.sortType
        json.customerId = this.baseForm.customerId
        json.buId = this.baseForm.buId
        json.currency = this.baseForm.currency
        json.billMonth = this.baseForm.billMonth
        const flag = this.tableData.list.every((item, i) => {
          const index = i + 1
          if (!item.projectName) {
            this.$errorMsg(`第${index}行，费用项目不能为空`)
            return false
          }
          if (!item.billType) {
            this.$errorMsg(`第${index}行，类型不能为空`)
            return false
          }
          if (['', null, undefined].includes(item.taxRate)) {
            this.$errorMsg(`第${index}行，税率不能为空`)
            return false
          }
          if (['', null, undefined].includes(item.taxAmount)) {
            this.$errorMsg(`第${index}行，费用金额（含税）不能为空`)
            return false
          }
          if (this.baseForm.sortType === '2' && !item.storePlatformCode) {
            this.$errorMsg(`第${index}行，核销平台不能为空`)
            return false
          }
          return true
        })
        if (!flag) return
        const selectBrandList = this.selectOpt.brandList || []
        const selectstorePlatformList = this.selectOpt.storePlatformList || []
        const listData = this.tableData.list.map((item) => {
          var goods = {}
          goods.projectId = item.projectId
          goods.projectName = item.projectName
          goods.billType = item.billType
          goods.poNo = item.poNo
          goods.goodsId = item.goodsId
          goods.goodsNo = item.goodsNo
          goods.goodsName = item.goodsName
          goods.brandParent = item.superiorParentBrandId
          goods.brandParentName = selectBrandList.find(
            (sitem) => sitem.key === item.superiorParentBrandId
          ).value
          goods.num = [undefined, null].includes(item.num) ? '' : item.num
          goods.taxRate = item.taxRate
          goods.taxAmount = item.taxAmount
          goods.invoiceDescription = item.invoiceDescription
          goods.storePlatformCode = item.storePlatformCode
          goods.storePlatformName = item.storePlatformCode
            ? selectstorePlatformList.find(
                (sitem) => sitem.key === item.storePlatformCode
              ).value
            : ''
          goods.remark = item.remark
          goods.currency = json.currency
          goods.platformGoodsCode = item.platformGoodsCode || ''
          return goods
        })
        json.costItemList = listData
        return json
      },
      // 确认
      comfirm() {
        this.$refs.baseForm.validate(async (valid) => {
          if (!valid) {
            return this.$errorMsg('请先完善信息')
          }
          const param = this.getParam()
          if (!param) return
          await saveAddReceiveBill({ ...param })
          this.$successMsg('创建成功')
          this.closeTag()
          this.linkTo('/receiveBill/list')
        })
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

  ::v-deep.textarea-container {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 130px;
    }
    .el-form-item__content {
      width: 700px;
    }
  }
</style>
