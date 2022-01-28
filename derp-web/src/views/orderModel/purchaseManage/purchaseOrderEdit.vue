<template>
  <!-- 采购订单新增编辑 -->
  <div class="page-bx bgc-w purchase-order-edit">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->

    <JFX-Form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      :disabled="$route.query.audit === 'true'"
    >
      <JFX-title title="基本信息" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="事业部：" prop="buId">
            <el-select
              v-model="ruleForm.buId"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getBUSelectBean('buList')"
              @change="changeBuId"
            >
              <el-option
                v-for="item in selectOpt.buList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 审批方式为oa显示 0：以销定采；1：备货（已立项）；2：备货（集团自主）；3：试单 -->
        <el-col
          v-if="ruleForm.auditMethod === '1'"
          class="mr-b-0"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="采购方式：" prop="purchaseMethod">
            <el-select
              v-model="ruleForm.purchaseMethod"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              @change="purchaseMethodChange"
              :data-list="getSelectList('purchaseOrder_purchaseMethodList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseOrder_purchaseMethodList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="供应商：" prop="supplierId">
            <el-select
              v-model="ruleForm.supplierId"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              @change="changeSupplierId"
              :data-list="getSupplierList('goongyingList')"
            >
              <el-option
                v-for="item in selectOpt.goongyingList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 根据采购审批方式，如果为OA，显示必填，如果为经分销，则隐藏不必填 -->
        <el-col
          v-if="ruleForm.auditMethod === '1'"
          class="mr-b-0"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="框架合同号：" prop="frameContractNo">
            <el-select
              v-model="ruleForm.frameContractNo"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in frameContractNoList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 并且采购方式=试单，字段显示，必填，其他情况不显示，不必填 -->
        <el-col
          v-if="ruleForm.auditMethod === '1' && ruleForm.purchaseMethod === '3'"
          class="mr-b-0"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="采购试单申请编号：" prop="tryApplyCode">
            <el-select
              v-model="ruleForm.tryApplyCode"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in tryApplyCodeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="业务模式：" prop="businessModel">
            <el-select
              v-model="ruleForm.businessModel"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getSelectList('purchaseOrder_businessModelList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseOrder_businessModelList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购币种：" prop="currency">
            <el-select
              @change="loadGoodListPrice"
              v-model="ruleForm.currency"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :list-data="
                getCurrencySelectBean('currencyList', { customerId: '' })
              "
            >
              <el-option
                v-for="item in selectOpt.currencyList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="PO号：" prop="poNo">
            <el-input
              v-model.trim="ruleForm.poNo"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="归属日期：" prop="attributionDateStr">
            <el-date-picker
              v-model="ruleForm.attributionDateStr"
              type="date"
              value-format="yyyy-MM-dd 00:00:00"
              style="width: 100%"
              clearable
              placeholder="选择日期"
              :picker-options="{
                disabledDate: (time) => time.getTime() > Date.now()
              }"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="预计付款日期：" prop="paymentDateStr">
            <el-date-picker
              v-model="ruleForm.paymentDateStr"
              type="date"
              value-format="yyyy-MM-dd 00:00:00"
              style="width: 100%"
              clearable
              :picker-options="{
                disabledDate: (time) =>
                  time.getTime() < new Date(new Date().toLocaleDateString())
              }"
              placeholder="选择日期"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            ref="stockLocationTypeId"
            label="库位类型："
            prop="stockLocationTypeId"
          >
            <el-select
              v-model="ruleForm.stockLocationTypeId"
              placeholder="请选择"
              clearable
              filterable
              style="width: 100%"
              :disabled="!rules.stockLocationTypeId.required"
              @change="changeStockLocationType"
            >
              <el-option
                v-for="item in typeSelectList"
                :key="item.key"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="预计发货日期：" prop="estimateDeliverDateStr">
            <el-date-picker
              v-model="ruleForm.estimateDeliverDateStr"
              type="date"
              value-format="yyyy-MM-dd 00:00:00"
              style="width: 100%"
              clearable
              :picker-options="{
                disabledDate: (time) =>
                  time.getTime() < new Date(new Date().toLocaleDateString())
              }"
              placeholder="选择日期"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="审批单号：" prop="approvalNo">
            <el-input
              v-model.trim="ruleForm.approvalNo"
              clearable
              placeholder="请输入"
            />
          </el-form-item>
        </el-col>
        <!-- 1-OA审批 2-经分销审批 -->
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="审批方式：" prop="auditMethod">
            <el-radio-group
              v-model="ruleForm.auditMethod"
              :data-list="
                getSelectList('merchant_bu_rel_purchase_audit_methodList')
              "
              @change="changeAuditMethod"
            >
              <el-radio
                v-for="item in selectOpt.merchant_bu_rel_purchase_audit_methodList"
                :key="item.key"
                :label="item.key"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col
          class="mr-b-0"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
          v-if="depotType === 'c'"
        >
          <el-form-item
            label="海外理货单位："
            :prop="depotType === 'c' ? 'tallyingUnit' : ''"
          >
            <el-radio-group
              v-model="ruleForm.tallyingUnit"
              :data-list="getSelectList('order_tallyingUnitList')"
              @change="loadGoodListPrice"
            >
              <el-radio
                v-for="item in selectOpt.order_tallyingUnitList"
                :key="item.key"
                :label="item.key"
                :disabled="depotType !== 'c'"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0 depot" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="入仓仓库：" prop="depotId">
            <el-select
              v-model="ruleForm.depotId"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              @change="changeDepotId"
            >
              <el-option
                v-for="item in selectOpt.depotIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
            <div style="color: red">注：如需创建交易链路请选择入库仓库</div>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="24">
          <el-form-item label="结算条款：" prop="paymentProvision">
            <el-radio-group
              v-model="ruleForm.paymentProvision"
              :data-list="getSelectList('purchaseOrder_paymentProvisionList')"
            >
              <el-radio
                v-for="item in selectOpt.purchaseOrder_paymentProvisionList"
                :key="item.key"
                :label="item.key"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="24">
          <el-form-item label="交货方式：" prop="tradeTerms">
            <el-radio-group
              v-model="ruleForm.tradeTerms"
              :data-list="getSelectList('purchaseOrder_tradeTermsList')"
            >
              <el-radio
                v-for="item in selectOpt.purchaseOrder_tradeTermsList"
                :key="item.key"
                :label="item.key"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 商品信息 -->
      <JFX-title title="商品信息" className="mr-t-10 mr-b-20 title">
        <div>
          <el-button type="primary" @click="choseGoods"> 选择商品 </el-button>
          <el-button type="primary" @click="importGoods"> 导入商品 </el-button>
          <el-button type="primary" @click="removeTableItem"> 删除 </el-button>
        </div>
      </JFX-title>
      <JFX-table
        :tableData.sync="tableData"
        :showPagination="false"
        :showSummary="true"
        :summaryMethod="null"
        @selection-change="selectionChange"
      >
        <el-table-column
          type="selection"
          width="50"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="goodsName"
          label="商品名称"
          align="center"
          min-width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="工厂编码" align="center" min-width="130">
          <template slot-scope="{ row }">
            {{ row.factoryNo }}
          </template>
        </el-table-column>
        <el-table-column
          label="条形码"
          align="center"
          min-width="130"
          show-overflow-tooltip
        >
          <template slot-scope="scope">{{ scope.row.barcode }}</template>
        </el-table-column>
        <el-table-column prop="num" align="center" min-width="110">
          <template slot="header">
            <span class="need">采购数量</span>
          </template>
          <template slot-scope="scope">
            <el-input-number
              v-model.trim="scope.row.num"
              :precision="0"
              :controls="false"
              :min="0"
              style="width: 100%"
              @change="changeNumOrPrice(scope.$index)"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="130">
          <template slot="header">
            <span class="need">采购单价(不含税)</span>
          </template>
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.price"
              v-if="purchasePriceManage"
              placeholder="请选择"
              clearable
              filterable
              @change="changeNumOrPrice(scope.$index)"
            >
              <el-option
                v-for="item in scope.row.priceManageList"
                :key="item.key"
                :label="item.value"
                :value="item.value"
              />
            </el-select>
            <el-input-number
              v-else
              :disabled="purchasePriceManage"
              v-model.trim="scope.row.price"
              :precision="8"
              v-max-spot="8"
              :min="0"
              clearable
              :controls="false"
              style="width: 100%"
              @change="changeNumOrPrice(scope.$index)"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column prop="amount" align="center" min-width="140">
          <template slot="header">
            <span class="need">采购总金额(不含税)</span>
          </template>
          <template slot-scope="scope">
            <el-input-number
              :disabled="purchasePriceManage"
              v-model.trim="scope.row.amount"
              :precision="2"
              v-max-spot="2"
              :min="0"
              clearable
              :controls="false"
              style="width: 100%"
              @change="changeAmount(scope.$index)"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column
          prop="taxAmount"
          label="采购总金额(含税)"
          align="center"
          min-width="140"
        >
          <template slot-scope="scope">
            <el-input-number
              :disabled="purchasePriceManage"
              v-model.trim="scope.row.taxAmount"
              :precision="2"
              v-max-spot="2"
              :min="0"
              clearable
              :controls="false"
              style="width: 100%"
              @change="changeTaxAmount(scope.$index)"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="税率" align="center" min-width="100">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.taxRate"
              style="width: 100%"
              placeholder="请选择"
              filterable
              :clearable="false"
              @change="changeTaxRate(scope.$index)"
            >
              <el-option
                v-for="item in selectOpt.raxList"
                :key="item.key"
                :label="item.value"
                :value="item.value"
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="tax" label="税额" align="center" min-width="120">
          <template slot-scope="scope">
            <el-input-number
              v-model.trim="scope.row.tax"
              :precision="2"
              v-max-spot="2"
              :min="0"
              clearable
              :controls="false"
              style="width: 100%"
              disabled
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="剩余校期" align="center" min-width="120">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.effectiveInterval"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('purchaseOrder_effectiveIntervalList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseOrder_effectiveIntervalList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </template>
        </el-table-column>
      </JFX-table>
      <!-- 商品信息 end -->
    </JFX-Form>

    <!-- 底部按钮 -->
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="save" :loading="saveLoading">
        保 存
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
      <el-button type="primary" @click="nextStep" :loading="nextStepBtnLoading">
        下一步
      </el-button>
    </div>
    <!-- 底部按钮 end -->

    <!-- 选择商品 -->
    <ChooseProduct
      v-if="chooseProductState.visible.show"
      :isVisible="chooseProductState.visible"
      :data="chooseProductState.data"
      @comfirm="comfirmChooseProduct"
    />
    <!-- 选择商品 end -->

    <!-- 导入商品 -->
    <PurchaseOrderImport
      :isVisible.sync="importGoodsVisible"
      :url="importPurchaseGoodsUrl"
      :data="importGoodsData"
      :templateId="'168'"
      @successUpload="importGoodsSuccess"
    />
    <!-- 导入商品 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getPurchasePriceManage,
    saveOrUpdateTempOrder,
    savePurchaseOrder,
    modifyPurchaseOrder,
    getPurchaseOrderDetails,
    getPurchasePriceByGoodsId,
    importPurchaseGoodsUrl,
    listOaBillCodeSelect,
    listFrameContracSelect,
    getPurchaseMethod
  } from '@a/purchaseManage'
  import {
    getCurrencySelectBean,
    getListByIds,
    getBuStockLocationTypeConfigSelect,
    getLocationManage
  } from '@a/base'
  import { getDepotDetails, getRateByCustomerAndMerchant } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    components: {
      /* 选择商品组件 */
      ChooseProduct: () => import('@c/choseGoods/index'),
      /* 导入商品 */
      PurchaseOrderImport: () => import('./components/PurchaseOrderImport.vue')
    },
    data() {
      return {
        importPurchaseGoodsUrl,
        ruleForm: {
          merchantId: '',
          merchantName: '',
          buId: '',
          paySubject: '',
          businessModel: '',
          depotId: '',
          supplierId: '',
          poNo: '',
          currency: '',
          attributionDateStr: '',
          tallyingUnit: '',
          tradeTerms: '',
          paymentProvision: '',
          tryApplyCode: '',
          frameContractNo: '',
          approvalNo: '',
          paymentDateStr: '',
          estimateDeliverDateStr: '',
          auditMethod: '',
          purchaseMethod: '',
          stockLocationTypeId: ''
        },
        rules: {
          supplierId: {
            required: true,
            message: '请选择供应商',
            trigger: 'change'
          },
          purchaseMethod: {
            required: true,
            message: '请选择采购方式',
            trigger: 'change'
          },
          businessModel: {
            required: true,
            message: '请选择业务模式',
            trigger: 'change'
          },
          buId: {
            required: true,
            message: '请选择事业部',
            trigger: 'change'
          },
          poNo: [
            {
              required: true,
              message: 'PO号不能为空',
              trigger: 'blur'
            },
            {
              pattern: /^[A-Za-z0-9\u4e00-\u9fa5-]+$/,
              message: '只能输入中文、字母、数字和“-”',
              trigger: ['change', 'blur']
            }
          ],
          currency: {
            required: true,
            message: '采购币种不能为空',
            trigger: 'change'
          },
          tryApplyCode: {
            required: true,
            message: '采购试单申请编号不能为空',
            trigger: 'change'
          },
          attributionDateStr: {
            required: true,
            message: '归属日期不能为空',
            trigger: 'change'
          },
          tallyingUnit: {
            required: true,
            message: '理货单位不能为空',
            trigger: 'change'
          },
          tradeTerms: {
            required: true,
            message: '贸易条款不能为空',
            trigger: 'change'
          },
          paymentProvision: {
            required: true,
            message: '付款条款不能为空',
            trigger: 'change'
          },
          auditMethod: {
            required: true,
            message: '审批方式不能为空',
            trigger: 'change'
          },
          frameContractNo: {
            required: true,
            message: '框架合同号不能为空',
            trigger: 'change'
          },
          stockLocationTypeId: {
            required: false,
            message: '库位类型不能为空',
            trigger: 'change'
          }
        },
        /* 选择商品组件状态 */
        chooseProductState: {
          visible: { show: false },
          data: {}
        },
        /* 用于删除商品 */
        goodsDelId: 0,
        // 导入商品组件状态
        importGoodsVisible: { show: false },
        // 导入商品组件请求参数
        importGoodsData: {},
        /* 采购试单下拉列表 */
        tryApplyCodeList: [],
        /* 框架合同号下拉列表 */
        frameContractNoList: [],
        /* 库位类型下拉列表 */
        typeSelectList: [],
        /* 当前事业部和供应商是否开启价格管理 */
        purchasePriceManage: false,
        /* 入仓库类型 */
        depotType: '',
        saveLoading: false,
        nextStepBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { audit, id, copyId } = this.$route.query
        /* 获取税率下拉 */
        this.getRaxList('raxList')
        this.audit = !!audit
        if (id) {
          this.editInit(id) /* 编辑 */
        } else if (copyId) {
          this.editInit(copyId) /* 复制 */
        } else {
          this.getMerchant() /* 新增的情况获取公司信息 */
        }
      },
      /* 编辑回显 */
      async editInit(id) {
        try {
          const { data } = await getPurchaseOrderDetails({ id })
          /* 赋值表单 */
          this.mapStateToForm(data)
          /* 赋值表格 */
          this.mapStateToTable(data?.itemList || [])
          /* 根据商家和事业部查询审批方式 */
          // await this.getPurchaseMethod()
          /* 根据事业部和供应商查询是否开启价格管理 */
          await this.getPurchasePriceManage()
          /* 开启价格管理后表格价格回显 */
          this.initTablePrice()
          /* 获取仓库类型 */
          this.getDepotType()
          /* 获取入库关区 */
          this.getCustomsAreaList()
          /* 根据事业部获取入库仓库下拉列表 */
          this.getDepotListByBu()
          /* 获取采购试单申请编号列表 */
          this.getOaBillCodeSelect()
          /* 获取采购框架合同编号列表 */
          this.getFrameContracSelect()
          /* 获取库位类型下拉列表 */
          this.getTypeSeclectList()
          /* 查询是否启用库位管理 */
          this.getLocationManage()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 赋值表单 */
      mapStateToForm(data) {
        if (!data) return
        for (const key in this.ruleForm) {
          this.ruleForm[key] = ![null, undefined].includes(data[key])
            ? data[key].toString()
            : ''
          /* 时间的字段需要去除Str后缀 */
          if (key.includes('Str')) {
            const replaceKey = key.replace(/Str/gi, '')
            this.ruleForm[key] = ![null, undefined].includes(data[replaceKey])
              ? data[replaceKey].toString()
              : ''
          }
        }
      },
      /* 赋值表格 */
      mapStateToTable(itemList) {
        if (!itemList?.length) return
        const list = itemList.map((item) => ({
          goodsId: item.goodsId || '',
          id: item.id || '',
          goodsCode: item.goodsCode || '',
          goodsName: item.goodsName || '',
          barcode: item.barcode || '',
          goodsNo: item.goodsNo || '',
          num: item.num || '',
          price: [null, undefined, ''].includes(item.price)
            ? undefined
            : item.price,
          amount: [null, undefined, ''].includes(item.amount)
            ? undefined
            : item.amount,
          taxAmount: [null, undefined, ''].includes(item.taxAmount)
            ? undefined
            : item.taxRate && Number(item.taxRate) > 0
            ? item.taxAmount
            : item.amount,
          taxPrice: item.taxPrice,
          tax: [null, undefined, ''].includes(item.tax) ? undefined : item.tax,
          taxRate: item.taxRate || '0.00',
          priceManageList: [],
          effectiveInterval: item.effectiveInterval,
          factoryNo: item.factoryNo,
          goodsDelId: this.goodsDelId++ // 用于删除商品
        }))
        this.tableData.list = list
      },
      /* 开启价格管理后表格价格回显 */
      async initTablePrice() {
        try {
          if (
            this.purchasePriceManage &&
            this.ruleForm.currency &&
            this.tableData.list.length
          ) {
            const priceMap = await this.getPurchasePriceMap(
              this.tableData.list.map((item) => item.goodsId).toString()
            )
            this.tableData.list.forEach((item) => {
              if (
                priceMap[item.goodsId] &&
                Array.isArray(priceMap[item.goodsId]) &&
                priceMap[item.goodsId].length
              ) {
                item.priceManageList = priceMap[item.goodsId].map(
                  (subItem) => ({
                    value: +(+subItem).toFixed(8),
                    label: +(+subItem).toFixed(8)
                  })
                )
                const condition = item.priceManageList.find(
                  (e) => e.value === +(+item.price).toFixed(8)
                )
                item.price = condition ? +(+item.price).toFixed(8) : undefined
              } else {
                item.priceManageList = []
                item.price = undefined
              }
            })
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取公司信息 */
      async getMerchant() {
        const { merchantName, merchantId } = this.getUserInfo()
        this.ruleForm.merchantName = merchantName || ''
        this.ruleForm.merchantId = merchantId || ''
      },
      /* 根据事业部+供应商+币种+商品 查询采购单价 */
      async getPurchasePriceMap(goodIds) {
        if (!this.purchasePriceManage) return {}
        const {
          supplierId,
          currency,
          buId,
          depotId,
          stockLocationTypeId,
          tallyingUnit: torrToUnit
        } = this.ruleForm
        try {
          const { data } = await getPurchasePriceByGoodsId({
            supplierId,
            currency,
            buId,
            depotId,
            torrToUnit,
            goodIds,
            stockLocationTypeId
          })
          return data.reduce((pre, cur) => {
            pre[cur.id] = [null].includes(cur.price) ? undefined : cur.price
            return pre
          }, {})
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
          return {}
        }
      },
      /* 显示选择商品弹窗 */
      async choseGoods() {
        const { buId, supplierId, currency, tallyingUnit, depotId } =
          this.ruleForm
        const { validateField } = this.$refs.ruleForm
        if (!buId) {
          this.$errorMsg('请选择事业部!')
          validateField(['buId'])
          return
        }
        /* 开启价格管理的情况 */
        if (this.purchasePriceManage && (!supplierId || !currency)) {
          this.$errorMsg(
            '该供应商+事业部已开启销售价格管理，请先选择【供应商】和【采购币种】'
          )
          validateField(['currency', 'customerId'])
          return
        }
        /* 仓库为海外仓情况 */
        if (this.depotType === 'c' && !tallyingUnit) {
          this.$errorMsg('仓库为海外仓时，请先选择【理货单位】')
          validateField('tallyingUnit')
          return false
        }
        this.chooseProductState.data = { depotId }
        /* 选择弹窗状态 */
        this.chooseProductState.visible.show = true
      },
      /* 根据事业部和供应商查询是否开启价格管理 */
      async getPurchasePriceManage() {
        const { buId, supplierId } = this.ruleForm
        if (!buId || !supplierId) return
        try {
          const { data = false } = await getPurchasePriceManage({
            buId,
            supplierId
          })
          this.purchasePriceManage = data
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 确认选择商品 */
      async comfirmChooseProduct(data) {
        if (!data) return
        const tableList = []

        /* 根据供应商带出对应税率 */
        const taxRate = await this.getRate()
        /* 根据事业部+供应商+币种+商品 查询采购单价, 开启价格管理之后会返回 */
        const priceMap = await this.getPurchasePriceMap(
          data.map((item) => item.id).toString()
        )

        /* 处理价格 */
        for (let i = 0; i < data.length; i++) {
          const item = data[i]
          let price = item.supplyMinPrice || 0 /* 单价 */
          let priceManageList = [] /* 开启价格管理之后的价格下拉列表 */
          /* 开启价格管理情况 */
          if (this.purchasePriceManage) {
            priceManageList =
              priceMap[item.id] && priceMap[item.id].length
                ? priceMap[item.id].map((subItem) => ({
                    value: subItem + '',
                    label: subItem
                  }))
                : []
            /* 单价列表只有一项时直接选中该价格 */
            price =
              priceManageList.length === 1
                ? priceManageList[0].value
                : undefined
          }
          /* 总金额（数量*单价）选择商品的时候默认数量给1 */
          const amount = 1 * Number(price)
          /* 含税总金额（总金额 *（1 + 税率）） */
          const taxAmount = amount * (1 + Number(taxRate))
          /* 税额（含税总金额 - 总金额） */
          const tax = taxAmount - amount
          tableList.push({
            goodsId: item.id,
            id: item.id,
            goodsName: item.name,
            barcode: item.barcode || '',
            goodsNo: item.goodsNo,
            goodsCode: item.goodsCode || '',
            factoryNo: item.factoryNo || '',
            num: 1,
            price: [undefined].includes(price) ? undefined : price,
            amount: [undefined].includes(price) ? undefined : amount.toFixed(2),
            taxAmount: [undefined].includes(price)
              ? undefined
              : taxAmount.toFixed(2),
            tax: [undefined].includes(price) ? undefined : tax.toFixed(2),
            taxRate,
            priceManageList,
            goodsDelId: this.goodsDelId++ /* 用于删除商品 */
          })
        }
        this.tableData.list = [...this.tableData.list, ...tableList]
        this.chooseProductState.visible.show = false
      },
      /* 根据供应商带出对应税率 */
      async getRate() {
        const { supplierId } = this.ruleForm
        if (!supplierId) return ''
        try {
          const {
            data: { rateId }
          } = await getRateByCustomerAndMerchant({
            customerId: supplierId
          })
          return (
            this.selectOpt.raxList.find((item) => +item.key === +rateId)
              ?.value || '0.00'
          )
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 保存 */
      async save() {
        if (!this.ruleForm.buId) {
          this.$errorMsg('事业部不能为空！')
          this.$refs.ruleForm.validateField(['buId'])
          return false
        }
        /* 校验商品列表 */
        const isPassed = this.checkGoods('save')
        if (!isPassed) return false
        try {
          this.saveLoading = true
          const params = this.getParams()
          if (!params.tallyingUnit) delete params.tallyingUnit
          await saveOrUpdateTempOrder(params)
          this.$successMsg('编辑成功')
          this.closeTag()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.saveLoading = false
        }
      },
      /* 显示导入弹窗 */
      async importGoods() {
        const { depotId, supplierId, currency, buId, tallyingUnit } =
          this.ruleForm
        if (!buId || !supplierId || !currency) {
          this.$errorMsg('请先选择【事业部】【供应商】【采购币种】!')
          this.$refs.ruleForm.validateField(['buId', 'supplierId', 'currency'])
          return false
        }
        this.importGoodsData = {
          depotId,
          supplierId,
          currency,
          buId,
          tallyingUnit,
          purchasePriceManage: this.purchasePriceManage,
          stockLocationTypeId: this.ruleForm.stockLocationTypeId
        }
        this.importGoodsVisible.show = true
      },
      /* 商品导入 */
      async importGoodsSuccess(importList) {
        if (!importList?.length) return
        this.$showToast('确认覆盖已存在的所有商品信息？', () => {
          this.coverImportGoods(importList)
        })
      },
      /* 将导入数据覆盖原有数据 */
      coverImportGoods(importList) {
        this.tableData.list = []
        importList.forEach((importItem, index) => {
          let price
          let priceManageList = []
          /* 开启价格管理的情况下 */
          if (this.purchasePriceManage && importItem?.priceList?.length) {
            priceManageList = importItem.priceList.map((item) => ({
              value: item + '',
              label: item
            }))
            /* 价格列表只有一个的情况下选择改项金额 */
            price =
              priceManageList.length === 1
                ? priceManageList[0].value
                : undefined
          }
          const amount = 1 * Number(price)
          const taxAmount = amount * (1 + Number(importItem.taxRate))
          const tax = taxAmount - amount
          this.tableData.list[index] = {
            goodsId: importItem.goodsId || '',
            goodsCode: importItem.goodsCode || '',
            goodsName: importItem.goodsName || '',
            barcode: importItem.barcode || '',
            goodsNo: importItem.goodsNo || '',
            num: importItem.num || '',
            factoryNo: importItem.factoryNo || '',
            price,
            amount: [undefined].includes(price) ? undefined : amount.toFixed(2),
            taxAmount: [undefined].includes(price)
              ? undefined
              : taxAmount.toFixed(2),
            tax: [undefined].includes(price) ? undefined : tax.toFixed(2),
            priceManageList,
            taxRate: importItem.taxRate || '0.00',
            effectiveInterval: '',
            goodsDelId: this.goodsDelId++
          }
        })
      },
      /* 采购方式改变 */
      purchaseMethodChange(purchaseMethod) {
        if (purchaseMethod !== '3') {
          this.ruleForm.tryApplyCode = ''
        }
      },
      /* 加载商品列表单价 */
      async loadGoodListPrice() {
        const { supplierId, currency, buId } = this.ruleForm
        if (!supplierId || !buId) return false
        const goodsList = this.tableData.list
        if (!goodsList.length) return false
        /* 根据事业部和供应商查询是否开启价格管理 */
        await this.getPurchasePriceManage()
        /* 根据供应商带出对应税率 */
        const taxRate = await this.getRate()
        /* 根据事业部+供应商+币种+商品 查询采购单价, 开启价格管理之后会返回 */
        const priceMap = await this.getPurchasePriceMap(
          goodsList.map((item) => item.goodsId).toString()
        )
        /* 没有开启价格管理时，用基础价格 */
        let basePriceMap = {}
        if (!this.purchasePriceManage) {
          const basePriceListRes = await getListByIds({
            ids: goodsList.map((item) => item.goodsId).toString()
          })
          basePriceMap = basePriceListRes.data.reduce((pre, cur) => {
            pre[cur.id] = cur.supplyMinPrice || 0
            return pre
          }, {})
        }
        /* 开启了价格管理但没有选择币种 */
        if (this.purchasePriceManage && !currency) {
          this.tableData.list.forEach((item) => {
            item.priceManageList = []
            item.price = undefined
            item.amount = undefined
            item.taxAmount = undefined
            item.tax = undefined
          })
          return false
        }
        for (let i = 0; i < goodsList.length; i++) {
          const item = goodsList[i]
          item.taxRate = taxRate
          let price = item.price || 0
          /* 开启了价格管理的情况 */
          if (this.purchasePriceManage) {
            item.priceManageList =
              priceMap[item.goodsId] && priceMap[item.goodsId].length
                ? priceMap[item.goodsId].map((subItem) => ({
                    value: subItem + '',
                    label: subItem
                  }))
                : []
            /* 价格列表只存在一个价格，则直接选中 */
            price =
              item.priceManageList.length === 1
                ? item.priceManageList[0].value
                : undefined
          } else {
            price = basePriceMap[item.goodsId]
          }
          item.price = price
          if (item.price === undefined) {
            item.amount = undefined
            item.taxAmount = undefined
            item.tax = undefined
          } else {
            this.changeNumOrPrice(i)
          }
        }
      },

      /* 删除 */
      removeTableItem() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        this.$showToast('确定要删除勾选的商品？', () => {
          const ids = this.tableChoseList.map((item) => item.goodsDelId)
          this.tableData.list = this.tableData.list.filter(
            (item) => !ids.includes(item.goodsDelId)
          )
        })
      },
      /* 点击下一步 */
      nextStep() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请先完善信息')
            return false
          }
          const { depotId, tallyingUnit } = this.ruleForm
          /* 如果仓库为海外仓，海外仓理货单位必填 */
          if (depotId && this.depotType === 'c' && !tallyingUnit) {
            this.$errorMsg('仓库为海外仓时，理货单位必填！')
            return false
          }
          if (this.tableData.list.length < 1) {
            this.$errorMsg('请选择商品')
            return false
          }
          try {
            this.nextStepBtnLoading = true
            /* 校验商品列表 */
            const isPassed = this.checkGoods()
            if (!isPassed) return false
            this.$showToast('确定提交该采购订单？', () => {
              this.savePurchaseOneStep()
            })
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.nextStepBtnLoading = false
          }
        })
      },
      /* 校验商品列表 */
      checkGoods(type) {
        let isPassed = true
        for (let i = 0; i < this.tableData.list.length; i++) {
          const { num, price } = this.tableData.list[i]
          const tips = `表格第${i + 1}行,`
          if (!num || num < 0) {
            this.$errorMsg(tips + '数量要大于0')
            isPassed = false
            break
          }
          if (
            type !== 'save' &&
            ([null, undefined, ''].includes(price) || Number(price) < 0)
          ) {
            this.$errorMsg(tips + '采购单价(不含税)不能为空且不能小于0')
            isPassed = false
            break
          }
        }
        return isPassed
      },
      /* 下一步提交数据 */
      async savePurchaseOneStep() {
        const params = this.getParams()
        if (!params.tallyingUnit) delete params.tallyingUnit
        const { id, audit } = this.$route.query
        try {
          this.nextStepBtnLoading = true
          const { data } = id
            ? await modifyPurchaseOrder(params)
            : await savePurchaseOrder(params)
          this.$successMsg('编辑成功，请编辑合同')
          /* 跳转到采购合同页面 */
          let url = `/purchase/contract?id=${data}&customerId=${this.ruleForm.supplierId}`
          if (audit) {
            url += '&audit=true'
          }
          this.closeTagAndJump(url)
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.nextStepBtnLoading = false
        }
      },
      /* 选中供应商 */
      async changeSupplierId() {
        /* 根据商家和事业部查询审批方式 */
        this.getPurchaseMethod()
        /* 根据供应商查询币种 */
        this.getCurrency()
        /* 获取采购试单申请编号列表 */
        this.getOaBillCodeSelect()
        /* 获取采购试单申请编号列表 */
        this.getFrameContracSelect()
        /* 根据事业部和供应商查询是否开启价格管理 */
        await this.getPurchasePriceManage()
        /* 加载价格 */
        this.loadGoodListPrice()
      },
      /* 事业部改变 */
      async changeBuId() {
        await this.$nextTick()
        this.$refs.stockLocationTypeId.resetField()
        this.ruleForm.depotId = ''
        /* 根据事业部获取入库仓库下拉列表 */
        this.getDepotListByBu()
        /* 根据商家和事业部查询审批方式 */
        this.getPurchaseMethod()
        /* 获取库位类型下拉列表 */
        this.getTypeSeclectList()
        /* 查询是否启用库位管理 */
        this.getLocationManage()
        /* 根据事业部和供应商查询是否开启价格管理 */
        await this.getPurchasePriceManage()
        /* 加载价格 */
        this.loadGoodListPrice()
      },
      /* 根据事业部获取入库仓库下拉列表 */
      getDepotListByBu() {
        delete this.selectOpt.depotIdList
        const { buId } = this.ruleForm
        if (!buId) return
        /* 仓库类别 a-保税仓，b-备查库，c-海外仓，d-中转仓,e-暂存区，f-销毁区 g-内贸仓 */
        this.getSelectBeanByMerchantBuRel('depotIdList', {
          buId,
          type: 'a,c,d'
        })
      },
      /* 根据供应商查询币种 */
      async getCurrency() {
        const { supplierId } = this.ruleForm
        if (!supplierId) return
        try {
          const { data: currency } = await getCurrencySelectBean({
            customerId: supplierId
          })
          this.ruleForm.currency = currency || ''
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 根据商家和事业部查询审批方式 */
      async getPurchaseMethod() {
        const { buId, supplierId } = this.ruleForm
        if (!buId || !supplierId) return
        try {
          const { data: purchaseAuditMethod } = await getPurchaseMethod({
            buId,
            supplierId
          })
          this.ruleForm.auditMethod = purchaseAuditMethod
          /* 采购方式根据采购审批方式，如果为OA，显示必填，如果为经分销，则隐藏不必填， */
          this.rules.purchaseMethod.required = purchaseAuditMethod === '1'
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 修改审批方式 */
      changeAuditMethod() {
        /* 清空审批方式关联的字段 */
        this.ruleForm.purchaseMethod = ''
        this.ruleForm.frameContractNo = ''
        this.ruleForm.tryApplyCode = ''
      },
      /* 获取采购试单申请编号列表 */
      async getOaBillCodeSelect() {
        const { supplierId } = this.ruleForm
        if (!supplierId) return false
        try {
          const { data } = await listOaBillCodeSelect({
            supplierId
          })
          if (!data?.length) return false
          this.tryApplyCodeList = data.map(({ oaBillCode }) => ({
            label: oaBillCode,
            key: oaBillCode
          }))
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取采购框架合同编号列表 */
      async getFrameContracSelect() {
        const { supplierId } = this.ruleForm
        if (!supplierId) return false
        try {
          const { data } = await listFrameContracSelect({
            supplierId
          })
          if (!data?.length) return false
          this.frameContractNoList = data.map(({ contractNo }) => ({
            label: contractNo,
            key: contractNo
          }))
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取库位类型下拉列表 */
      async getTypeSeclectList() {
        const { buId } = this.ruleForm
        if (!buId) return
        try {
          const { data } = await getBuStockLocationTypeConfigSelect({
            buId
          })
          this.typeSelectList = data || []
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 查询是否启用库位管理 */
      async getLocationManage() {
        const { buId, merchantId } = this.ruleForm
        if (!buId || !merchantId) return
        try {
          const { data: locationManage } = await getLocationManage({
            buId,
            merchantId
          })
          /* 开启库位管理则获取库位类型下拉列表且库位类型必填 */
          this.rules.stockLocationTypeId.required = locationManage
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 入库仓库改变 */
      async changeDepotId() {
        this.depotType = ''
        this.ruleForm.tallyingUnit = ''
        /* 获取仓库类型 */
        this.getDepotType()
        /* 获取入库关区 */
        this.getCustomsAreaList()
      },
      /* 获取入库关区 */
      async getCustomsAreaList() {
        delete this.selectOpt.goongyingList
        const { depotId } = this.ruleForm
        if (!depotId) return
        this.getSelectCustomsArea('customsAreaList', { depotId })
      },
      /* 获取仓库类型 */
      async getDepotType() {
        const { depotId } = this.ruleForm
        if (!depotId) return
        try {
          const { data } = await getDepotDetails({ id: depotId })
          this.depotType = data?.type || ''
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 库位类型改变 */
      changeStockLocationType() {
        /* 加载价格 */
        this.loadGoodListPrice()
      },
      /* 统一处理提交参数 */
      getParams() {
        const { id } = this.$route.query
        const list = this.tableData.list.map((item) => ({
          goodsId: item.goodsId || '',
          num: item.num || '0',
          barcode: item.barcode || '',
          effectiveInterval: item.effectiveInterval || '',
          taxRate: item.taxRate || '0',
          amount: [null, undefined, ''].includes(item.amount)
            ? null
            : item.amount || '0',
          /* 如果输入为空时，为价格未维护，传null */
          price: [null, undefined, ''].includes(item.price)
            ? null
            : item.price
            ? (+item.price).toFixed(8)
            : '0',
          /* 如果税率为0, 含税金额与不含税金额保持一致 */
          taxAmount: [null, undefined, ''].includes(item.taxAmount)
            ? null
            : item.taxRate > 0
            ? item.taxAmount
            : item.amount,
          taxPrice:
            item.taxAmount && item.num
              ? (+item.taxAmount / +item.num).toFixed(2)
              : '0',

          tax: [null, undefined, ''].includes(item.tax) ? null : item.tax || '0'
        }))
        const params = {
          ...this.ruleForm,
          items: JSON.stringify(list),
          id
        }
        console.log(params)
        return params
      },
      // 输入采购数量/单价 TODO：代码优化
      changeNumOrPrice(index) {
        let { price, num, taxRate } = this.tableData.list[index]
        num = +num || 0
        price = +price || 0
        taxRate = +taxRate || 0
        const amount = price * num
        const taxAmount = amount * (1 + taxRate)
        const tax = taxAmount - amount
        this.tableData.list[index].amount = amount.toFixed(2)
        this.tableData.list[index].taxAmount = taxAmount.toFixed(2) || ''
        this.tableData.list[index].tax = tax.toFixed(2) || ''
      },
      // 改变采购总金额(不含税) TODO：代码优化
      changeAmount(index) {
        let { amount, num, taxRate } = this.tableData.list[index]
        taxRate = taxRate ? Number(taxRate) : 0
        amount = amount ? Number(amount) : 0
        const price = num > 0 ? amount / num : ''
        const taxAmount = amount * (1 + taxRate)
        const tax = taxAmount - amount
        this.tableData.list[index].price = price.toFixed(8)
        this.tableData.list[index].taxAmount = taxAmount.toFixed(2) || ''
        this.tableData.list[index].tax = tax.toFixed(2) || ''
      },
      // 改变采购总金额(含税) TODO：代码优化
      changeTaxAmount(index) {
        let { taxAmount, num, taxRate } = this.tableData.list[index]
        taxAmount = +taxAmount || 0
        taxRate = +taxRate || 0
        taxRate = +taxRate || 0
        const amount = taxAmount / (1 + taxRate)
        const tax = taxAmount - amount
        this.tableData.list[index].amount = amount.toFixed(2)
        this.tableData.list[index].tax = tax.toFixed(2)
        if (num && num > 0) {
          const price = amount / num
          this.tableData.list[index].price = price.toFixed(8)
        }
      },
      // 修改税率 TODO：代码优化
      changeTaxRate(index) {
        let { taxRate, amount } = this.tableData.list[index]
        if (!taxRate) return false
        if ([null, undefined, ''].includes(amount)) {
          return false
        }
        amount = +amount || 0
        taxRate = +taxRate
        const taxAmount = amount * (1 + taxRate)
        const tax = taxAmount - amount
        this.tableData.list[index].taxAmount = taxAmount.toFixed(2)
        this.tableData.list[index].tax = tax.toFixed(2)
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.purchase-order-edit {
    .el-form-item__label {
      text-align: right;
      vertical-align: middle;
      font-size: 14px;
      color: #606266;
      line-height: 40px;
      padding: 0 4px 0 0;
      box-sizing: border-box;
      width: 150px;
    }
    .el-form-item {
      display: flex;
      align-items: center;
      margin-bottom: 14px;
    }
    .el-form-item__content {
      width: calc(100% - 150px);
      box-sizing: border-box;
      padding-right: 20px;
    }
    .el-input-group__append {
      padding: 0 4px;
    }
  }
  .depot {
    .el-form-item {
      display: flex;
      align-items: flex-start;
    }
  }
  .title {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
</style>
