<template>
  <!-- 销售退货订单新增/编辑页面 -->
  <div class="page-bx bgc-w edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" class="bg" />
    <JFX-Form :model="baseForm" ref="baseForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司：" prop="merchantId">
            <el-select
              v-model="baseForm.merchantId"
              placeholder="请选择"
              disabled
              :data-list="getSelectMerchantBean('merchantList')"
            >
              <el-option
                v-for="item in selectOpt.merchantList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="退货类型：" prop="returnType">
            <el-select
              v-model="baseForm.returnType"
              :disabled="baseForm.returnType === '3'"
              placeholder="请选择"
              clearable
              filterable
              @change="getInDepot"
            >
              <el-option key="1" label="消费者退货" value="1" />
              <el-option key="2" label="代销退货" value="2" disabled />
              <el-option key="3" label="购销退货" value="3" disabled />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="客户：" prop="customerId">
            <el-select
              v-model="baseForm.customerId"
              placeholder="请选择"
              clearable
              filterable
              @change="changeCustomer"
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
          <el-form-item label="事业部：" prop="buId">
            <el-select
              v-model="baseForm.buId"
              placeholder="请选择"
              clearable
              filterable
              disabled
              :data-list="getBUSelectBean('bu_list')"
              @change="buIdChange"
            >
              <el-option
                v-for="item in selectOpt.bu_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="退入仓库：" prop="inDepotId">
            <BeforeChangeSelect
              v-model="baseForm.inDepotId"
              :select-list="inDepotList"
              :before-change="beforechangeInDepot"
              clearable
              filterable
              @change="changeInDepot"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="退出仓库：" prop="outDepotId">
            <el-select
              v-model="baseForm.outDepotId"
              placeholder="请选择"
              clearable
              filterable
              disabled
              :data-list="getSelectBeanByMerchantRel('warehousesList')"
            >
              <el-option
                v-for="item in selectOpt.warehousesList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="退货方式：" prop="returnMode">
            <el-select
              v-model="baseForm.returnMode"
              placeholder="请选择"
              clearable
              disabled
              filterable
            >
              <el-option label="退货退款" value="1" />
              <el-option label="仅退货" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="关联销售单：" prop="saleOrderRelCode">
            <el-input
              v-model="baseForm.saleOrderRelCode"
              clearable
              placeholder="请输入"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="PO号：" prop="poNo">
            <el-input
              v-model="baseForm.poNo"
              placeholder="请输入"
              clearable
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="销售类型：" prop="businessModel">
            <el-select
              v-model="baseForm.businessModel"
              placeholder="请选择"
              clearable
              filterable
              disabled
            >
              <el-option
                v-for="item in [
                  { value: '1', label: '购销-整批结算' },
                  { value: '2', label: '代销' },
                  { value: '3', label: '采购执行' },
                  { value: '4', label: '购销-实销实结' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="LBX单号：" prop="lbxNo">
            <el-input v-model="baseForm.lbxNo" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="平台售后单号：" prop="platformReturnCode">
            <el-input
              v-model="baseForm.platformReturnCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <!-- 入库非海外仓则不显示理货单位 -->
        <el-col
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
          v-if="baseForm.inDepotType === 'c'"
        >
          <el-form-item label="理货单位：" prop="tallyingUnit">
            <el-select
              v-model="baseForm.tallyingUnit"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('order_tallyingUnitList')"
            >
              <el-option
                v-for="item in selectOpt.order_tallyingUnitList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="退货币种："
            prop="currency"
            :rules="rules.currency"
            ref="currency"
          >
            <el-select
              v-model="baseForm.currency"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getCurrencySelectBean('currencyList')"
            >
              <el-option
                v-for="item in selectOpt.currencyList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="退货原因：" prop="remark">
            <el-input
              v-model="baseForm.remark"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="库位类型："
            prop="stockLocationTypeId"
            ref="stockLocationType"
          >
            <el-select
              v-model="baseForm.stockLocationTypeId"
              placeholder="请选择"
              clearable
              :disabled="!rules.stockLocationTypeId.required"
            >
              <el-option
                v-for="item in stockLocationTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <!-- 基本信息 end -->
    <!-- 销售退货商品明细 -->
    <JFX-title title="销售退货商品明细" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      showIndex
    >
      <!-- 新增、删除 -->
      <template slot="action" slot-scope="{ $index }">
        <el-button
          type="text"
          icon="el-icon-plus"
          @click="tableData.list.push({})"
        />
        <el-button
          type="text"
          icon="el-icon-minus"
          @click="removeTableItem($index)"
        />
      </template>

      <!-- po号 -->
      <template slot="poNo" slot-scope="{ row }">
        <el-input v-model.trim="row.poNo" />
      </template>

      <!-- po时间 -->
      <template slot="poDateStr" slot-scope="{ row }">
        <el-date-picker
          v-model="row.poDateStr"
          type="date"
          value-format="yyyy-MM-dd"
          style="width: 100%"
          clearable
        />
      </template>

      <!-- 入库商品 -->
      <template slot="inGoodsNo" slot-scope="{ row, $index }">
        <div v-if="row.inGoodsNo">
          {{ row.inGoodsNo }}
        </div>
        <div v-else style="color: red">未关联仓库</div>
        <el-button
          type="primary"
          @click="
            row.barcode
              ? showEditInGoodsNo($index)
              : showChooseInGoodsModal($index)
          "
        >
          选择商品
        </el-button>
      </template>

      <!-- 出库商品 -->
      <template slot="outGoodsNo" slot-scope="{ row }">
        <div v-if="row.outGoodsNo">
          {{ row.outGoodsNo }}
        </div>
        <div v-else style="color: red">未关联仓库</div>
      </template>

      <!-- 退货数量 -->
      <template slot="returnNum" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.returnNum"
          :precision="0"
          :controls="false"
          :min="0"
          style="width: 100%"
          @blur="count('returnNum', $index)"
        />
      </template>

      <!-- 退货单价 -->
      <template slot="price" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.price"
          :precision="8"
          v-max-spot="8"
          :controls="false"
          :min="0"
          :disabled="baseForm.returnMode === '2'"
          style="width: 100%"
          @blur="count('price', $index)"
        />
      </template>

      <!-- 退货总金额（不含税） -->
      <template slot="amount" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.amount"
          :precision="2"
          v-max-spot="2"
          :controls="false"
          :min="0"
          :disabled="baseForm.returnMode === '2'"
          style="width: 100%"
          @blur="count('amount', $index)"
        />
      </template>

      <!-- 退货总金额（含税） -->
      <template slot="taxAmount" slot-scope="{ row, $index }">
        <el-input-number
          v-model.trim="row.taxAmount"
          :precision="2"
          v-max-spot="2"
          :controls="false"
          :min="0"
          :disabled="baseForm.returnMode === '2'"
          style="width: 100%"
          @blur="changeTaxAmount($index)"
        />
      </template>

      <!-- 税率 -->
      <template slot="taxRate" slot-scope="{ row, $index }">
        <el-select
          v-model="row.taxRate"
          placeholder="请选择"
          filterable
          :disabled="baseForm.returnMode === '2'"
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

    <!-- 底部按钮 -->
    <div class="mr-t-30 flex-c-c">
      <el-button @click="handleSubmit" type="primary" :loading="saveBtnLoading">
        保 存
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
    <!-- 底部按钮 end -->

    <!-- 修改货号 -->
    <ChooseProduct
      v-if="chooseProduct.visible.show"
      :isVisible="chooseProduct.visible"
      :data="chooseProduct.data"
      :isRadio="chooseProduct.isRadio"
      @comfirm="comfirmEditGoodsNo"
    />
    <!-- 修改货号 end -->

    <!-- 选择商品 -->
    <SaleReturnChooseGoods
      v-if="saleReturnChooseGoods.visible.show"
      :isVisible="saleReturnChooseGoods.visible"
      :data="saleReturnChooseGoods.data"
      @comfirm="comfirmChooseProduct"
    />
    <!-- 选择商品 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getDepotById,
    getSelectBeanByMerchantBuRel,
    getCurrencySelectBean,
    getOrderUpdateMerchandiseInfo,
    getLocationManage,
    getBuStockLocationTypeConfigSelect
  } from '@a/base/index'
  import {
    toEditPage,
    toAddPage,
    modifySaleReturnOrder,
    saveSaleReturnOrder,
    getRateByCustomerAndMerchant
  } from '@a/salesReturnManage/index'
  export default {
    mixins: [commomMix],
    components: {
      /* 带校验的el-select */
      BeforeChangeSelect: () => import('@c/beforeChangeSelect/index'),
      /* 修改货号 */
      ChooseProduct: () => import('@c/choseGoods/index'),
      /* 选择商品 */
      SaleReturnChooseGoods: () => import('./components/SaleReturnChooseGoods')
    },
    data() {
      return {
        /* 表单信息 */
        baseForm: {
          merchantId: '',
          merchantName: '',
          returnType: '',
          customerId: '',
          inDepotId: '',
          buId: '',
          outDepotId: '',
          returnMode: '',
          saleOrderRelCode: '',
          platformReturnCode: '',
          poNo: '',
          businessModel: '',
          lbxNo: '',
          tallyingUnit: '',
          currency: '',
          remark: '',
          inDepotType: '',
          outDepotType: '',
          stockLocationTypeId: ''
        },
        /* 表单校验规则 */
        rules: {
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          returnType: {
            required: true,
            message: '请选择退货类型',
            trigger: 'change'
          },
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          inDepotId: {
            required: true,
            message: '请选择退入仓库',
            trigger: 'change'
          },
          buId: { required: true, message: '请选择事业部', trigger: 'change' },
          outDepotId: {
            required: false,
            message: '请选择退出仓库',
            trigger: 'change'
          },
          businessModel: {
            required: true,
            message: '请选择销售类型',
            trigger: 'change'
          },
          currency: {
            required: true,
            message: '请选择退货币种',
            trigger: 'change'
          },
          returnMode: {
            required: true,
            message: '请选择退货方式',
            trigger: 'change'
          },
          tallyingUnit: {
            required: false,
            message: '请选择理货单位',
            trigger: 'change'
          },
          platformReturnCode: {
            max: 30,
            message: '不能超过30个字符',
            trigger: 'blur'
          },
          stockLocationTypeId: {
            required: true,
            message: '请选择库位类型',
            trigger: 'change'
          }
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '操作',
            slotTo: 'action',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: 'PO单号',
            prop: 'poNo',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: 'PO时间',
            slotTo: 'poDateStr',
            width: '160',
            align: 'center',
            hide: true
          },
          {
            label: '退出商品货号',
            slotTo: 'outGoodsNo',
            minWidth: '160',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '退入商品货号',
            slotTo: 'inGoodsNo',
            minWidth: '160',
            align: 'center',
            hide: true,
            need: true
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
            width: '150',
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
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '退货总金额 \n（不含税）',
            slotTo: 'amount',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '退货总金额 \n（含税）',
            slotTo: 'taxAmount',
            width: '150',
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
        /* 退入仓库列表 */
        inDepotList: [],
        /* 保存按钮loading状态 */
        saveBtnLoading: false,
        /* 修改货号 */
        chooseProduct: {
          visible: { show: false },
          data: {},
          isRadio: true
        },
        /* 选择商品组件状态 */
        saleReturnChooseGoods: {
          visible: { show: false },
          data: {}
        },
        /* 选择退出或退入商品 */
        chooseGoodsType: '',
        /* 记录当前操作的行号 */
        currentEditIndex: 0,
        // 库位类型下拉列表数据
        stockLocationTypeList: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id, type } = this.$route.query
        if (id) {
          this.editInit(id) // 编辑初始化
        } else if (type === 'purchaseAndSale') {
          this.purchaseAndSaleInit() // 购销退货
        } else {
          this.addInit() // 消费者退货
        }
      },
      /* 编辑初始化 */
      async editInit(id) {
        try {
          const { data } = await toEditPage({ id })
          /* 回显页面数据 */
          this.mapDataToPage(data || {})
          /* 获取下拉数据 */
          this.getSelectData()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 事业部改变
      buIdChange() {
        // 获取退入仓库列表
        this.getInDepot()
        //  清空库位类型
        this.$refs.stockLocationType.resetField()
        // 库位类型
        this.reloadStockLocationType()
        //
      },
      /* 购销退货新增 */
      async purchaseAndSaleInit() {
        /*
          sessionStorage 里面获取销售退的弹窗勾选的列表
          TODO：根据路由作为key值不同tab获取对应的数据
        */
        const json = JSON.parse(sessionStorage.getItem('saleReturnToAddData'))
        try {
          const { data } = await toAddPage(json)
          /* 回显页面数据 */
          this.mapDataToPage(data || {})
          /* 出入库仓互换 */
          ;[this.baseForm.outDepotId, this.baseForm.inDepotId] = [
            this.baseForm.inDepotId,
            this.baseForm.outDepotId
          ]
          /* 获取下拉数据 */
          this.getSelectData()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 回显页面数据 */
      mapDataToPage(data) {
        for (const key in this.baseForm) {
          this.baseForm[key] = ![undefined, null, ''].includes(data[key])
            ? data[key] + ''
            : ''
        }
        const { itemList } = data
        if (itemList && itemList.length) {
          this.tableData.list = itemList.map((item) => ({
            outGoodsId: item.outGoodsId || '',
            inGoodsId: item.inGoodsId || '',
            poNo: item.poNo || '',
            poDateStr: item.poDate ? item.poDate.slice(0, 10) : '',
            inGoodsNo: item.inGoodsNo || '',
            outGoodsNo: item.outGoodsNo || '',
            inGoodsName: item.inGoodsName || '',
            barcode: item.barcode || '',
            returnNum: (item.returnNum || 0) + (item.badGoodsNum || 0),
            price: item.price || 0,
            amount: item.amount || 0,
            taxAmount: item.taxAmount || 0,
            taxRate: item.taxRate || '0.00',
            tax: item.tax || 0
          }))
        }
        /* 获取公司信息 */
        const { merchantId, merchantName } = this.getUserInfo()
        this.baseForm.merchantId = merchantId + '' || ''
        this.baseForm.merchantName = merchantName || ''
        this.rules.outDepotId.required = this.baseForm.businessModel === '4'
      },
      /* 获取下拉数据 */
      getSelectData() {
        const { inDepotId, returnType, outDepotId, buId } = this.baseForm
        if (returnType && buId) {
          this.getInDepot()
        }
        inDepotId && this.getDepotInfo(inDepotId)
        outDepotId && this.changeOutDepot(outDepotId)
        this.reloadStockLocationType(this.baseForm.stockLocationTypeId)
      },
      // 提交表单
      handleSubmit() {
        this.$refs.baseForm.validate(async (valid) => {
          if (valid) {
            const { id = '' } = this.$route.query
            const { returnType, inDepotType, outDepotType, outDepotId } =
              this.baseForm
            // 退货类型为消费者退货情况
            if (returnType === '1') {
              if (inDepotType !== 'f') {
                this.$errorMsg('退货类型为消费者退货时，退入仓仅为销毁区')
                return false
              }
            } else {
              // 退货类型不为消费者退货情况
              if (!['f', 'a', 'd', 'c'].includes(inDepotType)) {
                this.$errorMsg('退入仓库仅能选中转仓、保税仓、海外仓、销毁区')
                return false
              }
            }
            // 退出仓限制
            if (outDepotId && !['b', 'e'].includes(outDepotType)) {
              this.$errorMsg('退出仓库仅能为备查库、暂存仓')
              return false
            }
            // 校验商品数据
            if (!this.checkSumbitData()) return false
            const { list: itemList } = this.tableData
            const json = { ...this.baseForm, itemList, id }
            try {
              this.saveBtnLoading = true
              id
                ? await modifySaleReturnOrder(json)
                : await saveSaleReturnOrder(json)
              this.$successMsg('操作成功')
              this.closeTag()
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.saveBtnLoading = false
            }
          } else {
            this.$errorMsg('请先填写必填信息！')
          }
        })
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
      },
      // 校验参数
      checkSumbitData() {
        let flag = true
        for (let i = 0; i < this.tableData.list.length; i++) {
          const {
            inGoodsId,
            outGoodsId,
            price,
            returnNum,
            amount,
            taxRate,
            taxAmount
          } = this.tableData.list[i]
          const rowTips = '表格第' + (i + 1) + '行,'

          if (!outGoodsId) {
            this.$errorMsg(rowTips + '请选择退出商品!')
            flag = false
            break
          }

          if (!inGoodsId) {
            this.$errorMsg(rowTips + '请选择退入商品!')
            flag = false
            break
          }

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
      // 退入仓库状态改变
      async changeInDepot(depotId) {
        if (!depotId) return false
        /* 获取仓库信息 */
        this.getDepotInfo(depotId)
        /* 切换仓库修改商品信息 */
        this.changeGoodsInfoByDepot(depotId)
      },
      // 切换退入出库仓前校验
      async beforechangeInDepot(depotId) {
        /* 没有选择商品不需要校验 */
        if (!this.tableData.list.length) return true
        const itemList = this.tableData.list.map((item, index) => ({
          barcode: item.barcode,
          index
        }))
        try {
          const { data } = await getOrderUpdateMerchandiseInfo({
            depotId,
            orderType: 2,
            itemList
          })
          if (data && data.length) {
            const barcodeList = []
            data.forEach((item, index) => {
              const { goodsNo } = item.merchandiseInfoModel
              /* 货号没有关联仓库 将条码保存起来做提示 */
              if (!goodsNo) {
                const { barcode } = this.tableData.list[index]
                barcodeList.push(barcode)
              }
            })
            /* 改仓库关联了仓库 */
            if (!barcodeList.length) {
              return true
            }
            /* 自定义提示信息 */
            const h = this.$createElement
            const message = h('div', null, [
              h('span', null, '条码为'),
              h('b', { style: 'color: teal' }, barcodeList.toString()),
              h('span', null, '的商品货号未关联出仓仓库确定切换出仓仓库吗？')
            ])
            /* 提示信息 */
            return this.$confirm(message, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
              .then(async () => true)
              .catch(async () => false)
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取仓库信息 */
      async getDepotInfo(id) {
        if (!id) return false
        try {
          const {
            data: { type }
          } = await getDepotById({ id })
          this.baseForm.inDepotType = type || ''
          // 退入仓为海外仓的时候理货单位必填
          this.rules.tallyingUnit.required = type === 'c'
          this.baseForm.tallyingUnit =
            type === 'c' ? this.baseForm.tallyingUnit : ''
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 退出仓库状态改变
      async changeOutDepot(id, type) {
        if (!id) return false
        // 选择退出仓库 清空退出商品信息
        if (type === 'change') {
          const list = this.tableData.list.map((item) => ({
            ...item,
            outGoodsId: '',
            outGoodsNo: ''
          }))
          this.tableData.list = list
        }
        try {
          // 获取退出仓库信息
          const {
            data: { type }
          } = await getDepotById({ id })
          this.baseForm.outDepotType = type || ''
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 是否同关区状态改变
      changeIsSameArea(val) {
        // 同关区 退出仓必填
        if (val === '1') {
          this.rules.outDepotId.required = true
          return false
        }
        // 购销-实销实结时跨关区 退出仓必填
        if (
          this.baseForm.returnType === '3' &&
          this.isSameArea === '0' &&
          this.businessModel === '4'
        ) {
          this.rules.outDepotId.required = true
          return false
        }
        // 当类型为代销退货且为跨关区时 退出仓必填
        if (
          this.baseForm.returnType === '2' &&
          this.baseForm.isSameArea === '0'
        ) {
          this.rules.outDepotId.required = true
          return false
        }
        // 清空规则
        this.rules.outDepotId.required = false
      },
      // 删除表格项
      removeTableItem(index) {
        if (this.tableData.list.length <= 1) {
          this.$errorMsg('至少要有一条商品数据')
          return false
        }
        this.tableData.list.splice(index, 1)
      },
      // 获取退入仓库列表
      async getInDepot() {
        const { returnType, buId } = this.baseForm
        if (!buId || !returnType) {
          this.inDepotList = []
          return false
        }
        const type = returnType === '3' ? 'a,c,d,f' : 'f' // 消费者退货只能选择销毁仓库
        try {
          const { data } = await getSelectBeanByMerchantBuRel({ type, buId })
          if (data && data.length) {
            this.inDepotList = data.map((item) => ({
              key: item.value,
              value: item.label
            }))
          } else {
            this.inDepotList = []
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 选择客户
      async changeCustomer(customerId, type) {
        if (!customerId) return false
        try {
          // 根据客户带出币种
          const { data: currency } = await getCurrencySelectBean({ customerId })
          currency
            ? (this.baseForm.currency = currency)
            : this.$refs.currency.resetField()
          // 带出税率
          const { data } = await getRateByCustomerAndMerchant({
            customerId: customerId,
            merchantId: this.baseForm.merchantId
          })
          // 选择商品
          if (type === 'choose') {
            return data.rate
          }
          this.tableData.list.forEach((item, index) => {
            if (item.seq) {
              this.tableData.list.splice(index, 1, {
                ...item,
                taxRate: data.rate || '0'
              })
              this.count('taxRate', index)
            }
          })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      /* 选择退入商品 */
      showChooseInGoodsModal(index) {
        const { inDepotId, returnType, buId } = this.baseForm
        if (!returnType || !inDepotId || !buId) {
          this.$errorMsg('请先选择退货类型、退入仓库、事业部!')
          this.$refs.baseForm.validateField(['buId', 'inDepotId', 'returnType'])
          return false
        }
        /* 选择过的商品不展示在选择商品弹窗上 */
        const unNeedGoodsIds = this.tableData.list
          .reduce((pre, cur) => {
            if (cur.barcode && cur.poNo) {
              /* 过滤条件参数格式：po号_入库商品id */
              pre.push(`${cur.poNo}__${cur.barcode}`)
            }
            return pre
          }, [])
          .toString()
        /* 销售退单关联的销售单号 */
        const { saleOrderRelCode: saleOrderCode } = this.baseForm
        this.saleReturnChooseGoods.data = {
          saleOrderCode,
          unNeedGoodsIds,
          inDepotId: this.baseForm.inDepotId
        }
        /* 记录当前操作的行号 */
        this.currentEditIndex = index
        /* 记录当前选择的是退入商品 */
        this.chooseGoodsType = 'inDepot'
        this.saleReturnChooseGoods.visible.show = true
      },
      /* 显示修改退入货号弹窗 */
      showEditInGoodsNo(index) {
        const { inDepotId } = this.baseForm
        const { barcode } = this.tableData.list[index]
        if (!inDepotId) {
          this.$errorMsg('退入仓库不能为空')
          this.$refs.baseForm.validateField(['inDepotId'])
          return false
        }
        /* 选择过的商品不展示在选择商品弹窗上 */
        const unNeedIds = this.tableData.list
          .reduce((pre, cur) => {
            if (cur.inGoodsId) {
              pre.push(cur.inGoodsId)
            }
            return pre
          }, [])
          .toString()
        this.chooseProduct.data = {
          depotId: inDepotId,
          unNeedIds,
          popupType: 2,
          barcode
        }
        /* 记录当前操作的行号 */
        this.currentEditIndex = index
        /* 记录当前选择的是退入商品 */
        this.chooseGoodsType = 'inDepot'
        this.chooseProduct.visible.show = true
      },
      /* 确认选择商品 */
      async comfirmChooseProduct(payload) {
        if (payload && payload.length) {
          /* 选择退入商品 */
          if (this.chooseGoodsType === 'inDepot') {
            const data = payload.map((item) => {
              return {
                ...item,
                returnNum: item.returnNum || 0,
                price: item.price || 0,
                amount: item.amount || 0,
                taxAmount: item.taxAmount || 0,
                tax: item.tax || 0,
                taxRate: item.taxRate ? (+item.taxRate).toFixed(2) : '0.00'
              }
            })
            this.tableData.list.splice(this.currentEditIndex, 1, ...data)
            this.saleReturnChooseGoods.visible.show = false
          } else {
            /* 选择退出商品 */
            const { id, goodsNo } = payload.shift()
            this.tableData.list[this.currentEditIndex].outGoodsId = id
            this.tableData.list[this.currentEditIndex].outGoodsNo = goodsNo
            this.chooseProduct.visible.show = false
          }
        }
      },
      /* 确认修改货号 */
      comfirmEditGoodsNo(payload) {
        if (payload && payload.length) {
          const item = payload[0]
          const current = this.tableData.list[this.currentEditIndex]
          if (this.chooseGoodsType === 'inDepot') {
            current.inGoodsId = item.id || ''
            current.inGoodsName = item.name || ''
            current.inGoodsNo = item.goodsNo || ''
          } else {
            current.outGoodsNo = item.goodsNo || ''
          }
        }
        this.chooseProduct.visible.show = false
      },
      /* 切换仓库修改商品信息 */
      async changeGoodsInfoByDepot(depotId) {
        if (!this.tableData.list.length) return false
        const itemList = this.tableData.list.map((item, index) => ({
          barcode: item.barcode,
          index
        }))
        try {
          const { data } = await getOrderUpdateMerchandiseInfo({
            depotId,
            orderType: 2,
            itemList
          })
          /* 覆盖商品信息 */
          this.tableData.list.forEach((item, index) => {
            const { id, goodsNo, name } = data[index].merchandiseInfoModel
            item.inGoodsId = id || ''
            item.inGoodsNo = goodsNo || ''
            item.inGoodsName = name || ''
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 库位类型，事业部选择后调整
      async reloadStockLocationType(selectId) {
        const { buId, merchantId } = this.baseForm
        if (!buId) {
          return
        }
        const param = { merchantId, buId }
        // 是否开启库位类型
        const { data: isOpenLocation } = await getLocationManage(param)
        this.rules.stockLocationTypeId.required = isOpenLocation
        // 库位下拉
        const { data: list } = await getBuStockLocationTypeConfigSelect(param)
        this.stockLocationTypeList = list.map((item) => {
          return {
            key: String(item.value),
            value: String(item.label)
          }
        })
        this.baseForm.stockLocationTypeId = ''
        // 选中
        selectId && (this.baseForm.stockLocationTypeId = selectId)
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
</style>
