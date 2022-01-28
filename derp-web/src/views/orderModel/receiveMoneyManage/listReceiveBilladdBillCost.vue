<template>
  <!-- 应收账单新建页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" :backPathUrl="backPathUrl" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
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
      <el-table-column prop="projectName" align="center" width="100">
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
      <el-table-column prop="billType" align="center" width="100">
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
      <el-table-column prop="poNo" align="center" width="100" label="PO号">
        <template slot-scope="{ row }">
          <el-input v-model="row.poNo"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="invoiceDescription"
        align="center"
        width="100"
        label="发票描述"
      >
        <template slot-scope="{ row }">
          <el-input v-model="row.invoiceDescription"></el-input>
        </template>
      </el-table-column>
      <el-table-column align="center" width="100" label="商品货号">
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
        width="100"
        label="商品名称"
      ></el-table-column>
      <el-table-column align="center" width="100">
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
      <el-table-column align="center" width="100" label="数量">
        <template slot-scope="{ row }">
          <el-input-number
            style="width: 100%"
            :controls="false"
            v-model="row.num"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" width="100">
        <template slot="header">
          <span class="need">税率</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            @change="calc(row)"
            style="width: 100%"
            :controls="false"
            :precision="2"
            v-model="row.taxRate"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" width="120">
        <template slot="header">
          <span class="need">费用金额（不含税）</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            disabled
            style="width: 100%"
            :controls="false"
            :precision="2"
            v-model="row.price"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" width="120">
        <template slot="header">
          <span class="need">税额</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            disabled
            style="width: 100%"
            :controls="false"
            :precision="2"
            v-model="row.tax"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" width="120">
        <template slot="header">
          <span class="need">费用金额（含税）</span>
        </template>
        <template slot-scope="{ row }">
          <el-input-number
            style="width: 100%"
            @change="calc(row)"
            :controls="false"
            :precision="2"
            v-model="row.taxAmount"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" width="110">
        <template slot="header">
          <span id="storePlatformListHeader">核销平台</span>
        </template>
        <template slot-scope="{ row }">
          <el-select
            v-model="row.storePlatformCode"
            placeholder="请选择"
            clearable
            filterable
            :disabled="sortType !== '2'"
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
      <el-table-column prop="remark" align="center" width="130" label="备注">
        <template slot-scope="{ row }">
          <el-input v-model="row.remark"></el-input>
        </template>
      </el-table-column>
    </JFX-table>
    <JFX-title title="附件列表" className="mr-t-10" />
    <JFX-upload
      v-if="action"
      @success="successUpload"
      :url="action"
      :limit="10000"
      :multiple="true"
    >
      <el-button id="sale-upload-btn" type="primary">上传文件</el-button>
    </JFX-upload>
    <EnclosureList
      v-if="bill.code"
      :code="bill.code"
      ref="enclosure"
      class="mr-t-15"
    />
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="comfirm">保 存</el-button>
      <el-button
        @click="closeTagAndJump(`/receiveBill/detail?isEdit=1&id=${billId}`)"
        >取 消</el-button
      >
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
    getDetail,
    saveReceiveBillCost,
    exportReceiveCostItemsUrl
  } from '@a/receiveMoneyManage/index'
  import { exportFilePost } from '@u/common'
  import { getBaseUrl } from '@u/tool'
  import { attachmentUploadFiles } from '@a/base/index'
  export default {
    mixins: [commomMix],
    components: {
      // 选择费项
      ChooseSettlement: () => import('./components/ChooseSettlement'),
      // 选择商品
      ChoseProducts: () => import('@c/choseProducts/index'),
      // 导入组件
      ImportFile: () => import('@/components/importfile/index'),
      // 附件列表
      EnclosureList: () => import('@c/enclosureList/index')
    },
    data() {
      return {
        customerId: '',
        billId: '',
        importAddReceiveItemsUrl,
        action: '', // 上传地址
        bill: {}, // 账单详情
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
    computed: {
      backPathUrl() {
        return `/receiveBill/detail?isEdit=1&id=${this.billId}`
      }
    },
    async mounted() {
      const { id, customerId, sortType } = this.$route.query
      this.billId = id
      this.customerId = customerId
      this.sortType = sortType
      // 核销项目必填回显
      this.$nextTick(() => {
        if (this.sortType === '2') {
          document
            .querySelector('#storePlatformListHeader')
            .classList.add('need')
        } else {
          document
            .querySelector('#storePlatformListHeader')
            .classList.remove('need')
        }
      })
      // 下拉列表
      this.getBrandList('brandList')
      this.getSelectList('storePlatformList')
      // 账单详情
      const { data } = await getDetail({ id })
      this.bill = data.bill
      // 列表恢复
      this.tableData.list = data.costItemDTOS.map((item) => {
        item.superiorParentBrandId = item.parentBrandId
          ? String(item.parentBrandId)
          : ''
        item.isEdit = Math.random()
        item.storePlatformCode = item.verifyPlatformCode
        return item
      })
      // 上传附件地址rul
      this.action =
        getBaseUrl(attachmentUploadFiles) +
        attachmentUploadFiles +
        '?token=' +
        sessionStorage.getItem('token') +
        '&code=' +
        data.bill.code
    },
    methods: {
      // 选择费项
      selectProject(row, index) {
        this.settlement.visible.show = true
        this.settlement.customerId = this.customerId
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
          num: undefined, // 添加为空，不为0
          taxRate: '',
          price: '',
          tax: '',
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
        this.importItems.visible.show = true
        this.importItems.filterData = { customerId: this.customerId }
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
          item.taxRate =
            goods.taxRate === 0 ? '0.00' : String(goods.taxRate || '')
          item.num = goods.num ? String(goods.num) : ''
          item.price = goods.price ? String(goods.price) : ''
          item.remark = goods.remark || ''
          item.brandParent = goods.superiorParentBrandId || ''
          item.tax = goods.tax ? String(goods.tax) : ''
          item.taxAmount = goods.taxAmount ? String(goods.taxAmount) : ''
          item.platformGoodsCode = goods.platformGoodsCode
            ? String(goods.platformGoodsCode)
            : ''
          return item
        })
        console.log(itemList)
        exportFilePost(exportReceiveCostItemsUrl, {
          json: JSON.stringify({ itemList })
        })
      },
      // 附件上传成功
      successUpload(res) {
        if (res.code + '' === '10000') {
          setTimeout(() => {
            this.$refs.enclosure.getEnclosureList(1)
          }, 1000)
        } else {
          this.$errorMsg(res.message)
        }
      },
      // 获取参数
      getParam() {
        var json = {}
        json.billId = this.billId
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
          if (!item.superiorParentBrandId) {
            this.$errorMsg(`第${index}行，母品牌不能为空`)
            return false
          }
          if (['', null, undefined].includes(item.tax)) {
            this.$errorMsg(`第${index}行，税额不能为空`)
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
          if (this.sortType === '2' && !item.storePlatformCode) {
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
          goods.price = item.price
          goods.invoiceDescription = item.invoiceDescription
          goods.storePlatformCode = item.storePlatformCode
          goods.storePlatformName = item.storePlatformCode
            ? selectstorePlatformList.find(
                (sitem) => sitem.key === item.storePlatformCode
              ).value
            : ''
          goods.remark = item.remark
          goods.platformGoodsCode = item.platformGoodsCode
          goods.taxAmount = item.taxAmount
          return goods
        })
        json.costItemList = listData
        return json
      },
      // 税额	*费用金额（含税）联动计算
      calc(row) {
        // const { taxRate, price } = row
        // let taxAmount = 0
        // let tax = 0
        // taxAmount = parseFloat(price) * (1 + parseFloat(taxRate))
        // taxAmount = parseFloat(taxAmount).toFixed(2)
        // tax = parseFloat(taxAmount) - parseFloat(price)
        // tax = parseFloat(tax).toFixed(2)
        // row.tax = tax
        // row.taxAmount = taxAmount
        const { taxRate, taxAmount } = row
        let price = 0
        let tax = 0
        price = parseFloat(taxAmount) / (1 + parseFloat(taxRate))
        price = parseFloat(price).toFixed(2)
        tax = parseFloat(taxAmount) - parseFloat(price)
        tax = parseFloat(tax).toFixed(2)
        row.tax = tax
        row.price = price
      },
      // 确认
      async comfirm() {
        const param = this.getParam()
        if (!param) return
        await saveReceiveBillCost({ ...param })
        this.$successMsg('操作成功')
        this.closeTagAndJump(`/receiveBill/detail?isEdit=1&id=${this.billId}`)
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
