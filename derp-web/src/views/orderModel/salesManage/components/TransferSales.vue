<template>
  <!-- 转销售订单组件 -->
  <div class="edit-bx">
    <JFX-Dialog
      closeBtnText="取 消"
      title="转销售订单"
      :width="'1300px'"
      :top="'80px'"
      :showClose="true"
      :loading="viewLoading"
      :btnAlign="'center'"
      :visible.sync="isVisible"
      @comfirm="comfirm"
    >
      <!-- 表单部分 -->
      <JFX-Form ref="ruleForm" :model="ruleForm" :rules="rules">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="事业部：" prop="buId">
              <el-select
                v-model="ruleForm.buId"
                placeholder="请选择"
                clearable
                @change="buIdChange"
                :data-list="getBUSelectBean('bu_list')"
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
            <el-form-item label="客户：" prop="customerId">
              <el-select
                disabled
                v-model="ruleForm.customerId"
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
            <el-form-item label="公司：" prop="merchantId">
              <el-select
                v-model="ruleForm.merchantId"
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
            <el-form-item label="出库仓库：" prop="outDepotId" ref="outDepotId">
              <el-select
                v-model="ruleForm.outDepotId"
                placeholder="请选择"
                clearable
                :data-list="
                  getSelectBeanByMerchantBuRel('out_warehousesList', {
                    type: 'a,c,d'
                  })
                "
                @change="outDepotChange"
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
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="销售类型："
              prop="businessModel"
              ref="businessModel"
            >
              <el-select
                v-model="ruleForm.businessModel"
                placeholder="请选择"
                filterable
                clearable
                @change="businessModelChange"
              >
                <el-option value="1" label="购销-整批结算" />
                <el-option value="4" label="购销-实销实结" />
                <el-option
                  value="3"
                  label="采销执行"
                  :disabled="outDepotType !== 'd'"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="是否同关区："
              prop="isSameArea"
              ref="isSameArea"
            >
              <el-select
                v-model="ruleForm.isSameArea"
                :disabled="isSameAreaDisabled"
                placeholder="请选择"
                clearable
                filterable
                @change="isSameAreaChange"
              >
                <el-option value="1" label="同关区"></el-option>
                <el-option value="0" label="跨关区"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="入库仓库：" prop="inDepotId">
              <el-select
                v-model="ruleForm.inDepotId"
                placeholder="请选择"
                clearable
                filterable
                @change="inDepotChange"
                :data-list="
                  getSelectBeanByMerchantRel('in_warehousesList', { type: 'b' })
                "
              >
                <el-option
                  v-for="item in selectOpt.in_warehousesList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="销售币种：" prop="currency" ref="currency">
              <el-select
                v-model="ruleForm.currency"
                placeholder="请选择"
                clearable
                filterable
                @change="reloadPrice"
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
            <el-form-item label="LBX单号：" prop="lbxNo">
              <el-input
                v-model="ruleForm.lbxNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="审批单号：" prop="approvalNo">
              <el-input
                v-model="ruleForm.approvalNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="库位类型：" prop="stockLocationTypeId">
              <el-select
                v-model="ruleForm.stockLocationTypeId"
                placeholder="请选择"
                clearable
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
          <el-col class="mr-b-0" :span="12">
            <el-form-item label="贸易条款：" prop="tradeTerms">
              <el-radio-group
                v-model="ruleForm.tradeTerms"
                :data-list="getSelectList('saleOrder_tradeTermsList')"
              >
                <el-radio
                  v-for="item in selectOpt.saleOrder_tradeTermsList"
                  :key="item.key"
                  :label="item.key"
                >
                  {{ item.value }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col class="mr-b-0" :span="12">
            <el-form-item label="付款条款：" prop="paymentTerms">
              <el-radio-group
                v-model="ruleForm.paymentTerms"
                :data-list="getSelectList('saleOrder_paymentTermsList')"
              >
                <el-radio
                  v-for="item in selectOpt.saleOrder_paymentTermsList"
                  :key="item.key"
                  :label="item.key"
                >
                  {{ item.value }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col class="mr-b-0" :span="12">
            <el-form-item label="运输方式：" prop="transport">
              <el-radio-group
                v-model="ruleForm.transport"
                :data-list="getSelectList('transportList')"
              >
                <el-radio
                  v-for="item in selectOpt.transportList"
                  :key="item.key"
                  :label="item.key"
                >
                  {{ item.value }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col class="mr-b-0" :span="12" v-if="outDepotType === 'c'">
            <el-form-item label="海外仓理货单位：" prop="tallyingUnit">
              <el-radio-group
                v-model="ruleForm.tallyingUnit"
                :data-list="getSelectList('order_tallyingUnitList')"
                @change="reloadPrice"
              >
                <el-radio
                  v-for="item in selectOpt.order_tallyingUnitList"
                  :key="item.key"
                  :label="item.key"
                >
                  {{ item.value }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
      <!-- 表单部分 end -->

      <!-- 表格 -->
      <JFX-table
        class="mr-t-10"
        :tableData.sync="tableData"
        :tableColumn="tableColumn"
        :summary-method="getSummaries"
        :showPagination="false"
        show-summary
      >
        <!-- 平台商品 -->
        <template slot="platformGoodsNo" slot-scope="{ row }">
          货号：{{ row.platformGoodsNo || '' }} <br />
          条形码：{{ row.platformBarcode || '' }}
        </template>

        <!-- 平台数量/价格 -->
        <template slot="num" slot-scope="{ row }">
          数量：{{ row.platFormNum || 0 }} <br />
          单价：{{ platFormCurrency || '' }} {{ row.platFormPrice || 0 }}
        </template>

        <!-- 转销货号 -->
        <template slot="saleGoodsNo" slot-scope="{ row }">
          <el-select
            v-if="row.saleGoodsNoList.length"
            v-model="row.saleGoodsNo"
            placeholder="请选择"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="item in row.saleGoodsNoList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
          <span v-else style="color: red">未关联仓库</span>
        </template>

        <!-- 转销售数量 -->
        <template slot="saleNum" slot-scope="{ row, $index }">
          <el-input-number
            v-model.trim="row.saleNum"
            :precision="0"
            :controls="false"
            :min="0"
            label="必填"
            style="width: 100%"
            @change="calc('price', $index)"
          />
        </template>
        <!-- S 销售单价 $index -->
        <template slot="price" slot-scope="{ row, $index }">
          <el-select
            v-model="row.price"
            v-if="hasPriceManage"
            placeholder="请选择"
            clearable
            filterable
            @change="calc('price', $index)"
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
            :precision="8"
            v-max-spot="8"
            :controls="false"
            :min="0"
            @blur="calc('price', $index)"
            style="width: 100%"
          />
        </template>
        <!-- E 销售单价  -->
        <!-- S 销售总金额(不含税)   -->
        <template slot="amount" slot-scope="{ row, $index }">
          <el-input-number
            :disabled="hasPriceManage"
            v-model.trim="row.amount"
            :precision="2"
            v-max-spot="2"
            :controls="false"
            :min="0"
            style="width: 100%"
            @blur="calc('amount', $index)"
          />
        </template>
        <!-- E 销售总金额(不含税)  -->
        <!-- S 销售总金额(含税)  -->
        <template slot="taxAmount" slot-scope="{ row, $index }">
          <el-input-number
            :disabled="hasPriceManage"
            v-model.trim="row.taxAmount"
            :precision="2"
            v-max-spot="2"
            :controls="false"
            :min="0"
            style="width: 100%"
            @blur="changeTaxAmount($index)"
          />
        </template>
        <!-- E 销售总金额(含税)  -->
        <!-- S 税率 -->
        <template slot="taxRate" slot-scope="{ row, $index }">
          <el-select
            v-model="row.taxRate"
            placeholder="请选择"
            filterable
            @change="calc('taxRate', $index)"
            :data-list="getRaxList('rateList')"
          >
            <el-option
              v-for="item in selectOpt.rateList"
              :key="item.key"
              :label="item.value"
              :value="item.value"
            />
          </el-select>
        </template>
        <!-- E 税率 -->
        <!-- S 税额 -->
        <template slot="tax" slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.tax"
            :precision="2"
            v-max-spot="2"
            :controls="false"
            :min="0"
            style="width: 100%"
            disabled
          />
          <!-- E 税额  -->
        </template>
      </JFX-table>
      <!-- 表格 end-->
    </JFX-Dialog>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getPlatformPurchaseOrderItems,
    getSaleItems,
    getDepotDetails,
    getSalePriceManage,
    getSalePrice,
    savePlatFormPurchaseToSales
  } from '@a/salesManage/index'
  import {
    getListByIds,
    getLocationManage,
    getBuStockLocationTypeConfigSelect
  } from '@a/base'
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
          buId: '',
          customerId: '',
          merchantId: '',
          merchantName: '',
          outDepotId: '',
          businessModel: '',
          isSameArea: '',
          inDepotId: '',
          currency: '',
          lbxNo: '',
          tradeTerms: '',
          transport: '',
          paymentTerms: '',
          tallyingUnit: '',
          approvalNo: '',
          remark: '',
          stockLocationTypeId: ''
        },
        /* 表单校验 */
        rules: {
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          businessModel: {
            required: true,
            message: '请选择销售类型',
            trigger: 'change'
          },
          outDepotId: {
            required: true,
            message: '请选择出库仓库',
            trigger: 'change'
          },
          buId: {
            required: true,
            message: '请选择事业部',
            trigger: 'change'
          },
          isSameArea: {
            required: false,
            message: '请选择是否同关区',
            trigger: 'change'
          },
          inDepotId: {
            required: false,
            message: '请选择入库仓库',
            trigger: 'change'
          },
          tallyingUnit: {
            required: false,
            message: '请选择理货单位',
            trigger: 'change'
          },
          currency: {
            required: true,
            message: '请选择销售币种',
            trigger: 'change'
          },
          tradeTerms: {
            required: true,
            message: '请选择贸易条款',
            trigger: 'change'
          },
          paymentTerms: {
            required: true,
            message: '请选择付款条款',
            trigger: 'change'
          },
          stockLocationTypeId: {
            required: true,
            message: '请选择库位类型',
            trigger: 'change'
          }
        },
        /* 页面loading */
        viewLoading: false,
        /* 表格列数据 */
        tableColumn: [
          {
            label: 'PO号',
            prop: 'poNo',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '平台商品',
            slotTo: 'platformGoodsNo',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'platformGoodsName',
            minWidth: '200',
            align: 'center',
            hide: true
          },
          {
            label: '平台数量/价格',
            slotTo: 'num',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '转销货号',
            slotTo: 'saleGoodsNo',
            width: '180',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '转销售数量',
            slotTo: 'saleNum',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '销售单价(不含税) ',
            slotTo: 'price',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '销售总金额(不含税) ',
            slotTo: 'amount',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '销售总金额(含税)',
            slotTo: 'taxAmount',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '税率',
            slotTo: 'taxRate',
            width: '100',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '税额',
            slotTo: 'tax',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          }
        ],
        // 是否同关区是否禁用
        isSameAreaDisabled: false,
        /* 出库仓库类型 */
        outDepotType: '',
        /* 出库仓编码 */
        outDepotCode: '',
        // 入库仓库类型
        inDepotType: '',
        // 是否开启价格管理
        hasPriceManage: false,
        // 库位类型下拉列表数据
        stockLocationTypeList: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { ids, selectData } = this.data
        const { currency, customerId } = selectData[0]
        // 币种
        this.ruleForm.currency = currency || ''
        this.platFormCurrency = currency
        // 客户
        this.ruleForm.customerId = customerId + ''
        // 公司
        const { merchantId, merchantName } = this.getUserInfo()
        this.ruleForm.merchantId = merchantId + '' || ''
        this.ruleForm.merchantName = merchantName || ''

        try {
          this.viewLoading = true
          const {
            data: { itemDTOList }
          } = await getPlatformPurchaseOrderItems({ ids })
          if (itemDTOList && itemDTOList.length) {
            this.getTableParams(itemDTOList)
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.viewLoading = false
        }
      },
      // 回显表格数据
      async getTableParams(list) {
        this.tableData.list = list.map((item) => ({
          ...item,
          platFormNum: item.num,
          platFormPrice: item.price,
          saleNum: item.num,
          /* 存放货号列表 */
          saleGoodsNoList: [],
          price: undefined,
          amount: undefined,
          taxAmount: undefined,
          taxRate: '',
          tax: undefined,
          /* 开启价格管理时用于存放价格 */
          priceManageList: []
        }))
      },
      // 事业部改变
      async buIdChange() {
        // 清空出库仓库
        this.$refs.outDepotId.resetField()
        // 清空销售类型
        this.$refs.businessModel.resetField()
        // 库位类型
        this.reloadStockLocationType()
        // 表体数据清空
        this.outDepotChange()
        const { customerId, buId } = this.ruleForm
        try {
          // 是否开启价格管理
          if (customerId && buId) {
            const hasPriceManage = await getSalePriceManage({
              customerId,
              buId
            })
            this.hasPriceManage = hasPriceManage.data
          }
          // 根据事业部限制出库仓库
          delete this.selectOpt.out_warehousesList
          this.getSelectBeanByMerchantBuRel('out_warehousesList', {
            buId: buId || '',
            type: 'a,c,d'
          })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      /* 切换出库仓库 */
      async outDepotChange(depotId) {
        /* 清空销售类型 */
        this.$refs.businessModel.resetField()
        /* 切换仓库修改商品信息 */
        await this.updateGoodsList(depotId)
        /* 获取出仓库类型 */
        this.getDepotType('outDepot', depotId)
        /* 重新获取销售单价 */
        this.reloadPrice()
      },
      // 销售类型改变
      businessModelChange() {
        // 校验出库仓
        this.validOutDepot()
      },
      // 是否通关区改变
      isSameAreaChange(value) {
        if (value === '1') {
          // 保税仓、同关区--出库仓必填
          if (this.outDepotType === 'a') {
            this.rules.inDepotId.required = true
          }
        } else {
          // 保税仓、跨关区--出库仓非必填
          if (this.outDepotType === 'a') {
            this.rules.inDepotId.required = false
          }
        }
        this.validOutDepot()
      },
      // 入库仓改变
      inDepotChange(depotId) {
        // 获取入仓库类型
        this.getDepotType('inDepot', depotId)
      },
      // 获取出入仓库类型
      async getDepotType(depotType, id) {
        if (depotType === 'outDepot' && !id) {
          this.outDepotType = ''
          return false
        }
        if (depotType === 'inDepot' && !id) {
          this.inDepotType = ''
          return false
        }
        const {
          data: { type, code }
        } = await getDepotDetails({ id })
        if (depotType === 'outDepot') {
          this.outDepotType = type
          this.outDepotCode = code
          switch (this.outDepotType) {
            // 保税仓
            case 'a':
              // 出库仓为保税仓时是否同关区必填
              this.rules.isSameArea.required = true
              this.isSameAreaDisabled = false
              break
            // 海外仓
            case 'c':
              // 出库仓为海外仓时是否同关区禁用
              this.$refs.isSameArea.resetField()
              this.isSameAreaDisabled = true
              this.rules.isSameArea.required = false
              // 海外仓、实销实结--入库仓必填
              if (this.ruleForm.businessModel === '4') {
                this.rules.inDepotId.required = true
              } else {
                this.rules.inDepotId.required = false
              }
              break
            // 中转仓
            case 'd':
              // 出库仓为中转仓时是否同关区非必填
              this.rules.isSameArea.required = false
              this.isSameAreaDisabled = false
              break
            default:
              this.rules.isSameArea.required = false
              this.isSameAreaDisabled = false
              break
          }
          // 海外仓
          if (this.outDepotType === 'c') {
            this.rules.tallyingUnit.required = true
          } else {
            this.rules.tallyingUnit.required = false
            this.ruleForm.tallyingUnit = ''
          }
          // 校验出库仓
          this.validOutDepot()
        } else {
          this.inDepotType = type
        }
      },
      // 校验出库仓
      validOutDepot() {
        const { isSameArea, businessModel } = this.ruleForm
        const outDepotType = this.outDepotType
        if (outDepotType) {
          switch (outDepotType) {
            // 保税仓
            case 'a':
              if (isSameArea === '1') {
                // 当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库
                this.rules.inDepotId.required = true
                delete this.selectOpt.in_warehousesList
                this.getSelectBeanByMerchantRel('in_warehousesList', {
                  type: 'b'
                })
              } else {
                if (businessModel === '1') {
                  // 当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库禁用；
                  this.rules.inDepotId.required = false
                  delete this.selectOpt.in_warehousesList
                  this.getSelectBeanByMerchantRel('in_warehousesList', {
                    type: 'b'
                  })
                } else if (businessModel === '4') {
                  // 当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库
                  this.rules.inDepotId.required = true
                  delete this.selectOpt.in_warehousesList
                  this.getSelectBeanByMerchantRel('in_warehousesList', {
                    type: 'b'
                  })
                }
              }
              break
            // 海外仓
            case 'c':
              if (businessModel === '4') {
                // 当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库
                this.rules.inDepotId.required = true
                delete this.selectOpt.in_warehousesList
                this.getSelectBeanByMerchantRel('in_warehousesList', {
                  type: 'b'
                })
              } else {
                this.rules.inDepotId.required = false
              }
              break
            // 中转仓
            case 'd':
              if (businessModel === '4') {
                // 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
                this.rules.inDepotId.required = true
                delete this.selectOpt.in_warehousesList
                this.getSelectBeanByMerchantRel('in_warehousesList', {
                  type: 'b'
                })
              } else {
                this.rules.inDepotId.required = false
                delete this.selectOpt.in_warehousesList
                this.getSelectBeanByMerchantRel('in_warehousesList')
              }
              break
            default:
              this.rules.inDepotId.required = false
              delete this.selectOpt.in_warehousesList
              this.getSelectBeanByMerchantRel('in_warehousesList', {
                type: 'b'
              })
              break
          }
        }
      },
      /* 切换仓库修改商品信息 */
      async updateGoodsList(depotId) {
        // 清空转销货号下拉数据
        this.tableData.list.forEach((item) => {
          item.saleGoodsNoList = []
          item.saleGoodsNo = ''
          item.saleGoodsId = ''
          item.saleGoodsCommbarcode = ''
        })
        // 商品或仓库无数据，不继续执行
        if (!this.tableData.list.length || !depotId) {
          return
        }

        try {
          // 获取货号参数
          const getSaleItemsParam = this.tableData.list.map(
            ({ id, platformGoodsNo, platformBarcode }) => ({
              id,
              platformGoodsNo,
              platformBarcode
            })
          )
          //  获取数据
          const { data: saleItemsData } = await getSaleItems({
            outDepotId: depotId,
            itemList: getSaleItemsParam
          })

          // 回填数据
          Object.keys(saleItemsData).forEach((changeId) => {
            // 表格数据
            const tableItem = this.tableData.list.find(
              ({ id }) => Number(id) === Number(changeId)
            )
            // 修改数据
            const changeItemList = saleItemsData[changeId]
            // 存在数据,覆盖数据
            if (changeItemList?.length) {
              const saleGoodsNoList = []
              changeItemList.forEach((subItem) => {
                if (subItem.goodsNo) {
                  saleGoodsNoList.push({
                    key: subItem.goodsNo,
                    value: subItem.goodsNo,
                    saleGoodsId: subItem.merchandiseId,
                    saleGoodsCommbarcode: subItem.commbarcode
                  })
                }
              })
              // 转销货号下拉
              tableItem.saleGoodsNoList = saleGoodsNoList
              // 默认选择第一项
              if (saleGoodsNoList.length) {
                const { value, saleGoodsId, saleGoodsCommbarcode } =
                  saleGoodsNoList[0]
                tableItem.saleGoodsNo = value
                tableItem.saleGoodsId = saleGoodsId
                tableItem.saleGoodsCommbarcode = saleGoodsCommbarcode
              }
            }
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 改变事业部 币种，理货单位 价格重新请求加载
      async reloadPrice() {
        // 客户、事业部 有值的情况下才能继续
        if (!this.ruleForm.customerId || !this.ruleForm.buId) return false
        // 存在商品的情况下继续
        if (!this.tableData.list.filter((item) => item.saleGoodsId).length) { return false }
        // 开启了价格管理、但没有选择币种清空商品价格
        if (this.hasPriceManage && !this.ruleForm.currency) {
          this.tableData.list.forEach((item) => {
            item.priceManageList = []
            item.price = undefined
            item.amount = undefined
            item.taxAmount = undefined
            item.tax = undefined
          })
          return false
        }
        let basePriceMap = {} // 存放原始单价
        if (!this.hasPriceManage) {
          const ids = this.tableData.list
            .map((item) => item.saleGoodsId)
            .filter((item) => !!item)
            .toString()
          const { data } = await getListByIds({ ids })
          if (data && data.length) {
            basePriceMap = data.reduce((pre, cur) => {
              pre[cur.id] = cur.filingPrice * (cur.priceDeclareRatio || 1) || 0
              return pre
            }, {})
          }
        }
        // 循环设置商品价格
        this.tableData.list.forEach(async (item, index) => {
          // 当前记录已选择货号
          if (item.saleGoodsId) {
            // 开启价格管理，价格处理
            if (this.hasPriceManage) {
              const json = JSON.stringify({
                customerId: this.ruleForm.customerId,
                currency: this.ruleForm.currency,
                unitId: this.ruleForm.tallyingUnit,
                buId: this.ruleForm.buId,
                commbarcode: item.saleGoodsCommbarcode,
                goodId: item.saleGoodsId
              })
              try {
                // 获取价格管理单价
                const { data } = await getSalePrice({ json })
                // 返回多个价格下拉列表展示
                item.priceManageList =
                  data && data.length
                    ? data.map((subItem) => ({
                        key: +(+subItem).toFixed(8),
                        value: +(+subItem).toFixed(8)
                      }))
                    : []
                // 返回价格只有一个则直接选中
                item.price =
                  item.priceManageList.length === 1
                    ? item.priceManageList[0].value
                    : undefined
              } catch (error) {
                // 报错清空单价和价格列表
                item.priceManageList = []
                item.price = undefined
                this.$errorMsg(error.message)
              }
            } else {
              // 没有开启价格管理，回填价格本身
              item.price = basePriceMap[item.saleGoodsId] || ''
            }
            if (item.price === undefined) {
              item.amount = undefined
              item.taxAmount = undefined
              item.tax = undefined
            } else {
              this.calc('price', index)
            }
          }
        })
      },
      // 计算表格行数据
      calc(type, index) {
        const data = this.tableData.list[index]
        let {
          price = 0,
          saleNum: num = 0,
          amount = 0,
          taxAmount = 0,
          tax = 0,
          taxRate = 0
        } = data
        // 价格没有维护下，改变不触发改变
        if ([null, undefined, ''].includes(data.price)) {
          return
        }
        num = num || 0
        price = price || 0
        amount = amount || 0
        taxRate = taxRate ? +taxRate : 0
        // 单价或数量改变
        if (type === 'price' || type === 'num') {
          amount = (num * price).toFixed(2)
        }
        // 总价（不含税）改变
        if (type === 'amount' && num) {
          price = (amount / num).toFixed(8)
        }
        // 计算含税总价
        taxAmount = (amount * (1 + taxRate)).toFixed(2)
        // 计算税额
        tax = taxAmount - amount
        this.tableData.list.splice(index, 1, {
          ...data,
          price,
          num,
          amount,
          taxAmount,
          tax
        })
      },
      // 总金额（含税输入）
      changeTaxAmount(index) {
        const data = this.tableData.list[index]
        let {
          saleNum: num = 0,
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
        if (num) {
          // 重新计算单价
          price = (amount / num).toFixed(8)
        }
        this.tableData.list.splice(index, 1, {
          ...data,
          amount,
          tax,
          taxAmount,
          price
        })
      },
      /* 校验商品列表 */
      checkGoodsList() {
        const isPassed = this.tableData.list.every(
          (
            { saleNum, saleGoodsNo, price, amount, taxAmount, taxRate, tax },
            index
          ) => {
            // 转销数量不能为空
            if ([null, undefined, ''].includes(saleNum)) {
              this.$errorMsg(
                `表格第${index + 1}行，${
                  saleGoodsNo ? '已选择转销货号，' : ''
                }转销数量不能为空`
              )
              return false
            }
            // 转销数量为0，全部不校验
            if ([0, '0'].includes(saleNum)) {
              return true
            }
            // 转销货号不能为空
            if (!saleGoodsNo) {
              this.$errorMsg(`表格第${index + 1}行，转销货号不能为空`)
              return false
            }
            if ([null, undefined, ''].includes(price) || price < 0) {
              this.$errorMsg(`表格第${index + 1}行，销售单价不能为空或者小于0`)
              return false
            }
            if ([null, undefined, ''].includes(amount) || amount < 0) {
              this.$errorMsg(
                `表格第${index + 1}行，总金额（不含税）不能为空或者小于0`
              )
              return false
            }
            if ([null, undefined, ''].includes(taxAmount) || taxAmount < 0) {
              this.$errorMsg(
                `表格第${index + 1}行，总金额（含税）不能为空或者小于0`
              )
              return false
            }
            if ([null, undefined, ''].includes(taxRate)) {
              this.$errorMsg(`表格第${index + 1}行，税率不能为空`)
              return false
            }
            if ([null, undefined, ''].includes(tax) || tax < 0) {
              this.$errorMsg(`表格第${index + 1}行，税额不能为空`)
              return false
            }
            return true
          }
        )
        return isPassed
      },
      /* 保存前校验 */
      comfirm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请正确填写表单必填项')
            return false
          }
          /* 校验商品列表 */
          if (!this.checkGoodsList()) return false
          /* 执行保存 */
          this.handleSumbit()
        })
      },
      /* 保存 */
      async handleSumbit() {
        try {
          this.comfirmLoading = true
          await savePlatFormPurchaseToSales({
            ...this.ruleForm,
            itemList: this.tableData.list.map((item) => {
              return {
                amount: item.amount,
                goodsId: item.saleGoodsId,
                goodsNo: item.saleGoodsNo,
                num: item.saleNum,
                price: item.price,
                tax: item.tax,
                taxAmount: item.taxAmount,
                taxPrice: item.taxPrice,
                taxRate: item.taxRate,
                poNo: item.poNo,
                id: item.id
              }
            })
          })
          this.$emit('update:isVisible', { show: false })
          this.$successMsg('操作成功')
          this.$emit('comfirm')
        } catch (err) {
          console.log(err)
        } finally {
          this.comfirmLoading = false
        }
      },
      /* 合计 */
      getSummaries({ data }) {
        const sum = []
        const poNos = []
        let num = 0
        let saleNum = 0
        let amounts = 0
        let taxAmounts = 0
        let taxs = 0
        data.forEach((item) => {
          if (item.poNo) {
            poNos.push(item.poNo)
          }
          if (item.num) {
            num += item.num
            saleNum += item.saleNum + 0 ? item.saleNum : 0
            amounts += item.amount ? +item.amount : 0
            taxAmounts += item.taxAmount ? +item.taxAmount : 0
            taxs += item.tax ? +item.tax : 0
          }
        })
        sum[0] = `合计：${[...new Set(poNos)].length}个PO号`
        sum[3] = `数量：${num}`
        sum[5] = saleNum
        sum[7] = (+amounts).toFixed(2)
        sum[8] = (+taxAmounts).toFixed(2)
        sum[10] = (+taxs).toFixed(2)
        return sum
      },
      // 库位类型，事业部选择后调整
      async reloadStockLocationType() {
        const { buId, merchantId } = this.ruleForm
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
        this.ruleForm.stockLocationTypeId = ''
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.edit-bx {
    .el-form-item__label {
      text-align: right;
      vertical-align: middle;
      float: left;
      font-size: 14px;
      color: #606266;
      line-height: 40px;
      padding: 0 12px 0 0;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      width: 140px;
    }
    .el-form-item--mini.el-form-item {
      display: flex;
      align-items: center;
      margin-bottom: 5px;
    }
  }
</style>
