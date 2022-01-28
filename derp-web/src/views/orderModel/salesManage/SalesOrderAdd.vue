<template>
  <!-- 新增销售订单页面 -->
  <div class="page-bx bgc-w sales-order-add">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <div v-loading="loading">
      <JFX-Form
        :model="ruleForm"
        ref="ruleForm"
        :rules="rules"
        :disabled="$route.query.examine === '1'"
      >
        <!-- 基本信息 -->
        <JFX-title title="基本信息" className="bor-n mr-t-15" />
        <el-row :gutter="10" class="edit-row-bor">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="事业部：" prop="buId" ref="buId">
              <el-select
                v-model="ruleForm.buId"
                placeholder="请选择"
                clearable
                filterable
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
            <el-form-item label="出库仓库：" prop="outDepotId" ref="outDepotId">
              <BeforeChangeSelect
                v-model="ruleForm.outDepotId"
                :data-list="getSelectBeanByMerchantBuRel('out_warehousesList')"
                :select-list="selectOpt.out_warehousesList"
                :before-change="outDepotBeforeChange"
                clearable
                filterable
                @change="outDepotChange"
              />
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
            <el-form-item label="客户：" prop="customerId">
              <el-select
                v-model="ruleForm.customerId"
                placeholder="请选择"
                clearable
                filterable
                @change="customerIdChange"
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
              <!-- 当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库禁用 -->
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
            <el-form-item label="PO单号：" prop="poNo">
              <el-input
                v-model="ruleForm.poNo"
                placeholder="请输入"
                clearable
              />
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
            <el-form-item
              label="库位类型："
              prop="stockLocationTypeId"
              ref="stockLocationType"
              :disabled="!rules.stockLocationTypeId.required"
            >
              <el-select
                v-model="ruleForm.stockLocationTypeId"
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
          <el-col :span="24">
            <el-form-item label="备注：" prop="remark" class="remark">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4 }"
                v-model="ruleForm.remark"
                placeholder="请输入"
                clearable
              />
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
        <!-- 基本信息 end -->
        <!-- 商品信息 -->
        <JFX-title title="商品信息" className="mr-t-15 title">
          <div>
            <el-button
              type="primary"
              @click="showImportGoodsModal"
              v-if="$route.query.source !== '1'"
            >
              商品导入
            </el-button>
            <el-button
              v-if="$route.query.source !== '1'"
              type="primary"
              @click="showChooseProductModal"
            >
              选择商品
            </el-button>
            <el-button type="primary" @click="showChoosePreSaleNumModal" v-else>
              选择商品
            </el-button>
            <el-button type="primary" @click="delTableItems">删除</el-button>
          </div>
        </JFX-title>
        <JFX-table
          :tableData.sync="tableData"
          :showPagination="false"
          :summary-method="summaryMethod"
          showSummary
          @selection-change="selectionChange"
        >
          <el-table-column
            type="selection"
            align="center"
            width="55"
          ></el-table-column>
          <el-table-column prop="seq" align="center" width="100" label="序号">
            <template slot-scope="{ row }">
              <el-input-number
                v-model.trim="row.seq"
                :precision="0"
                :controls="false"
                :min="0"
                style="width: 100%"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="goodsCode"
            align="center"
            label="商品编号"
            width="140"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="goodsName"
            align="center"
            label="商品名称"
            min-width="150"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="goodsNo"
            align="center"
            label="商品货号"
            width="120"
            show-overflow-tooltip
          >
            <template slot-scope="{ row, $index }">
              <template v-if="row.goodsNo">
                <div>{{ row.goodsNo || '' }}</div>
                <el-button
                  type="primary"
                  @click="
                    showChooseProductModal({
                      isEditGoodsNo: true,
                      barcode: row.barcode,
                      editIndex: $index
                    })
                  "
                >
                  修改货号
                </el-button>
              </template>
              <span v-else style="color: red">未关联仓库</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="barcode"
            align="center"
            label="条码"
            width="120"
            show-overflow-tooltip
          ></el-table-column>
          <!-- 销售数量 -->
          <el-table-column align="center" width="110">
            <template slot="header">
              <span class="need">销售数量</span>
            </template>
            <template slot-scope="{ row, $index }">
              <el-input-number
                v-model.trim="row.num"
                :precision="0"
                :controls="false"
                :min="0"
                style="width: 100%"
                @blur="calc('num', $index)"
                :disabled="$route.query.source === '1'"
              />
            </template>
          </el-table-column>
          <!-- 销售数量 end -->
          <!-- 销售单价 -->
          <el-table-column align="center" width="150">
            <template slot="header">
              <span class="need">
                销售单价
                <br />
                (不含税)
              </span>
            </template>
            <template slot-scope="{ row, $index }">
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
                style="width: 100%"
                @blur="calc('price', $index)"
              />
            </template>
          </el-table-column>
          <!-- 销售单价 end -->
          <!-- 销售总金额(不含税) -->
          <el-table-column align="center" width="130">
            <template slot="header">
              <span class="need">
                销售总金额
                <br />
                (不含税)
              </span>
            </template>
            <template slot-scope="{ row, $index }">
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
          </el-table-column>
          <!-- 销售总金额(不含税) end -->
          <!-- 销售总金额(含税) -->
          <el-table-column align="center" width="130">
            <template slot="header">
              <span class="need">
                销售总金额
                <br />
                (含税)
              </span>
            </template>
            <template slot-scope="{ row, $index }">
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
          </el-table-column>
          <!-- 销售总金额(含税) end -->
          <!-- 税率 -->
          <el-table-column align="center" width="100">
            <template slot="header">
              <span class="need">税率</span>
            </template>
            <template slot-scope="{ row, $index }">
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
          </el-table-column>
          <!-- 税率 end -->
          <!-- 税额 -->
          <el-table-column align="center" width="130">
            <template slot="header">
              <span class="need">税额</span>
            </template>
            <template slot-scope="{ row }">
              <el-input-number
                v-model.trim="row.tax"
                :precision="2"
                v-max-spot="2"
                :controls="false"
                :min="0"
                style="width: 100%"
                disabled
              />
            </template>
          </el-table-column>
          <!-- 税额 end -->
        </JFX-table>
        <!-- 商品信息 end -->
      </JFX-Form>

      <!-- 审核结果按钮 -->
      <div class="flex-c-c mr-t-30" v-if="$route.query.examine === '1'">
        <span class="clr-I" style="padding: 0 20px 4px 0">审核结果:</span>
        <el-radio v-model="auditResult" label="0">通过</el-radio>
        <el-radio v-model="auditResult" label="1">驳回</el-radio>
      </div>
      <div
        class="flex-c-c mr-t-30"
        v-if="$route.query.examine === '1' && auditResult === '1'"
      >
        <span class="clr-I" style="padding: 0 20px 4px 0">驳回原因:</span>
        <el-input
          v-model="rejectReason"
          type="textarea"
          placeholder="仅限输入100字以内"
          style="width: 200px"
        ></el-input>
      </div>
      <!-- 审核结果按钮 end -->
    </div>
    <!-- 底部按钮 -->
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="handleSave" :loading="saveBtnLoading">
        保 存
      </el-button>
      <el-button
        v-if="$route.query.examine !== '1'"
        v-permission="'sale_submitSaleOrder'"
        type="primary"
        :loading="saveBtnLoading"
        @click="handleSumbit"
      >
        提交
      </el-button>
      <el-button
        v-if="$route.query.examine === '1'"
        v-permission="'sale_examineSaleOrder'"
        type="primary"
        :loading="saveBtnLoading"
        @click="handleExamine"
      >
        审核
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
    <!-- 底部按钮 end -->

    <!-- 选择商品 -->
    <ChooseProduct
      v-if="chooseProduct.visible.show"
      :isVisible="chooseProduct.visible"
      :data="chooseProduct.data"
      :isRadio="chooseProduct.isRadio"
      @comfirm="
        ($event) =>
          editGoodsNo.isEditGoodsNo
            ? comfirmEditGoodsNo($event)
            : comfirmChooseProduct($event)
      "
    />
    <!-- 选择商品 end -->

    <!-- 导入商品 -->
    <JFX-Dialog
      :visible.sync="importGoodsVisible"
      :showClose="true"
      :width="'860px'"
      :top="'3vh'"
      title="导入商品"
      closeBtnText="取 消"
      confirmBtnText="确 认"
      @comfirm="importGoodsVisible.show = false"
    >
      <ImportFile
        :url="importSaleGoodsUrl"
        :selfDown="selfDown"
        :filterData="importGoodsData"
        :templateId="'143'"
        :downText="importSaleGoodsText"
        @downup="downGoodsTemplate"
        @successUpload="importGoodsSuccess"
      />
    </JFX-Dialog>
    <!-- 导入商品 end -->

    <!-- 生成采购订单 -->
    <SubmitShowPurchase
      v-if="purchaseOrder.visible.show"
      :submitShowPurchaseVisible="purchaseOrder.visible"
      :name="purchaseOrder.name"
      :id="purchaseOrder.id"
      @comfirm="createPurchaseOrder"
    />
    <!-- 生成采购订单 end -->

    <!-- 预售转销选择商品数量 -->
    <ChoosePreSaleNum
      v-if="choosePreSaleNum.visible.show"
      :choosePreSaleNumVisible="choosePreSaleNum.visible"
      :form="choosePreSaleNum.form"
      :itemList="choosePreSaleNum.itemList"
      @comfirm="(data) => closeChoosePreSaleNumModal(data)"
    />
    <!-- 预售转销选择商品数量 end -->

    <!-- 下载 -->
    <div class="hide-form" v-if="downVal">
      <form id="down-up" :action="actionUrl" method="post" target="_blank">
        <input type="hidden" name="json" v-model="downVal" />
      </form>
    </div>
    <!-- 下载 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  import {
    getCurrencySelectBean,
    getListByIds,
    getOrderUpdateMerchandiseInfo,
    getLocationManage,
    getBuStockLocationTypeConfigSelect
  } from '@a/base'
  import {
    saveOrModifyTempSaleOrder,
    getRateByCustomerAndMerchant,
    getDepotDetails,
    checkExistByPo,
    submitSaleOrder,
    toSaleOrderEdit,
    getSalePrice,
    getSalePriceManage,
    modifySaleOrder,
    importSaleGoodsUrl,
    exportItemsUrl,
    toSaleAddPage,
    preSaleEditPage,
    checkSaleExistPurchase,
    checkPreSale
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      /* 带校验的el-select */
      BeforeChangeSelect: () => import('@c/beforeChangeSelect/index'),
      /* 选择商品组件 */
      ChooseProduct: () => import('@c/choseGoods/index'),
      /* 导入组件 */
      ImportFile: () => import('@/components/importfile/index'),
      /* 生成采购订单 */
      SubmitShowPurchase: () => import('./components/SubmitShowPurchase'),
      /* 预售转销选择商品数量 */
      ChoosePreSaleNum: () => import('./components/ChoosePreSaleNum')
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
          poNo: '',
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
        /* 表单校验规则 */
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
        // 当前客户带出的税率
        customerTaxRate: '',
        /* 保存、提交、审核按钮loading状态 */
        saveBtnLoading: false,
        // 审核结果
        auditResult: '',
        rejectReason: '', // 驳回原因
        // 导入商品url
        importSaleGoodsUrl: importSaleGoodsUrl,
        // 导入商品组件状态
        importGoodsVisible: { show: false },
        // 导入商品组件请求参数
        importGoodsData: {},
        selfDown: false,
        importSaleGoodsText: '下载模板',
        // 总毛重
        totalGrossWeightSum: '',
        // 页面loading
        loading: false,
        downVal: '',
        actionUrl: '',
        // 生成采购订单组件
        purchaseOrder: {
          visible: { show: false },
          name: '',
          id: ''
        },
        // 预售转销选择商品数量
        choosePreSaleNum: {
          visible: { show: false },
          form: {},
          itemList: []
        },
        // 预售单号
        preSaleOrderCode: '',
        /* 用于删除商品 */
        delId: 0,
        /* 选择商品组件状态 */
        chooseProduct: {
          visible: { show: false },
          data: {},
          isRadio: false
        },
        /* 修改货号 */
        editGoodsNo: {
          isEditGoodsNo: false,
          index: 0
        },
        // 库位类型下拉列表数据
        stockLocationTypeList: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const {
          id,
          copyid,
          purchaseId,
          outDepotId,
          source,
          platformPurchaseIds,
          platformSalesNum,
          buId
        } = this.$route.query
        /* 复制 */
        if (copyid) {
          this.editInit(copyid)
          return false
        }
        /* 生成内部订单 */
        if (purchaseId && outDepotId) {
          this.editInit(null, purchaseId, outDepotId)
          return false
        }
        /* 预售单转销售单 */
        if (source === '1') {
          const json = sessionStorage.getItem('persaleObj')
          const { preSaleOrderCode } = JSON.parse(json)
          this.preSaleOrderCode = preSaleOrderCode
          this.preSaleInit(json)
          return false
        }
        /* 平台采购转销售 */
        if (source === '2') {
          this.addInit({
            outDepotId,
            buId,
            platformPurchaseIds,
            platformSalesNum
          })
          return false
        }
        /* 新增、编辑 */
        id ? this.editInit(id) : this.addInit()
      },
      /* 新增页面初始化 */
      async addInit(params) {
        try {
          this.loading = true
          /* 平台采购转销售 */
          if (params) {
            const {
              data: { saleOrderDTO, saleOrderItemList, errorMsg }
            } = await toSaleAddPage(params)
            /* 条码找不到商品的情况直接返回 */
            if (errorMsg) {
              this.$message.closeAll()
              this.$errorMsg(errorMsg || '系统异常')
              this.closeTag()
              return false
            }
            /* 获取表单和表格数据 */
            this.getFormAndTableParams(saleOrderDTO, saleOrderItemList)
            /* 获取表单下拉框改变状态 */
            this.getSelectState()
            return false
          }
          /* 获取公司信息 */
          const { merchantId, merchantName } = this.getUserInfo()
          this.ruleForm.merchantId = merchantId + '' || ''
          this.ruleForm.merchantName = merchantName || ''
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.loading = false
        }
      },
      /* 编辑页面初始化 */
      async editInit(id, purchaseId, depotId) {
        try {
          this.loading = true
          let saleOrderDTO = {}
          if (id) {
            const { data } = await toSaleOrderEdit({ id })
            saleOrderDTO = data.saleOrderDTO
          } else if (purchaseId && depotId) {
            const { data } = await toSaleOrderEdit({
              purchaseId,
              outDepotId: depotId
            })
            saleOrderDTO = data.saleOrderDTO
          }
          /* 获取表单和表格数据 */
          this.getFormAndTableParams(saleOrderDTO, saleOrderDTO.itemList)
          /* 获取表单下拉框改变状态 */
          this.getSelectState('edit')
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.loading = false
        }
      },
      /* 预售转销 */
      async preSaleInit(data) {
        try {
          this.loading = true
          const {
            data: { saleOrderDTO }
          } = await preSaleEditPage({ data })
          if (saleOrderDTO) {
            /* 获取表单和表格数据 */
            this.getFormAndTableParams(saleOrderDTO, saleOrderDTO.itemList)
          }
          /* 获取表单下拉框改变状态 */
          this.getSelectState()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.loading = false
        }
      },
      /**
       * 回显表单和表格数据
       * @param formData 表单数据
       * @param listData 商品列表数据
       */
      getFormAndTableParams(formData, listData) {
        for (const key in this.ruleForm) {
          this.ruleForm[key] =
            formData[key] !== undefined && formData[key] !== null
              ? formData[key] + ''
              : ''
        }
        if (listData && listData.length) {
          listData.forEach((item, index) => {
            this.$set(this.tableData.list, index, {
              ...item,
              seq: item.seq || index + 1,
              num: item.num || 1,
              taxRate: item.taxRate ? (+item.taxRate).toFixed(2) : '0.00',
              price: [null, ''].includes(item.price)
                ? undefined
                : (+item.price).toFixed(8) + '',
              amount: [null, ''].includes(item.amount)
                ? undefined
                : (+item.amount).toFixed(2),
              taxAmount: [null, ''].includes(item.taxAmount)
                ? undefined
                : (+item.taxAmount).toFixed(2) || 0,
              tax: [null, ''].includes(item.tax) ? undefined : item.tax,
              /* 开启价格管理时用于存放价格 */
              priceManageList: [],
              /* 用于删除商品 */
              delId: this.delId++
            })
            /* 计算商品信息 */
            this.calc('num', index)
          })
        }
      },
      /**
       * 获取请求参数
       * @param orderType 1-预审转销 2-非预售转销
       * @param operate 0-保存 1-提交 2-审核
       * @param auditResult 0-通过 1-驳回
       */
      getFetchParams(orderType, operate, auditResult) {
        const { source, id, platformPurchaseIds } = this.$route.query
        /* 订单类型 */
        orderType = source === '1' ? '1' : '2'
        const itemList = this.tableData.list.map((item) => ({
          ...item,
          price: item.price || 0,
          num: item.num || 0,
          amount: item.amount || 0,
          taxAmount: item.taxAmount || 0,
          tax: item.tax || 0,
          taxRate: item.taxRate ? item.taxRate + '' : '0.00'
        }))
        /* 请求参数 */
        const params = {
          ...this.ruleForm,
          id,
          orderType,
          operate,
          itemList
        }
        /* 平台转销售 */
        if (platformPurchaseIds) {
          params.platformPurchaseIds = platformPurchaseIds
        }
        /* 审核 */
        if (auditResult) {
          params.auditResult = auditResult
        }
        if (auditResult === '1') {
          params.rejectReason = this.rejectReason
        }
        return params
      },
      /* 保存 */
      handleSave() {
        /* 清空之前的校验 */
        this.$refs.ruleForm.clearValidate()
        /* 保存只校验事业部必填 */
        this.$refs.ruleForm.validateField('buId', async (valid) => {
          if (valid) {
            this.$errorMsg('请正确填写表单必填项')
            return false
          }
          /* 校验商品 */
          const isPassed = this.checkGoods(true)
          if (!isPassed) return false
          /* 获取请求参数 */
          const params = this.getFetchParams('2', '0')
          try {
            this.saveBtnLoading = true
            await saveOrModifyTempSaleOrder(params)
            this.$successMsg('操作成功')
            this.closeTag()
            this.linkTo('/sales/salesorder')
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.saveBtnLoading = false
          }
        })
      },
      /* 提交 */
      async handleSumbit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请正确填写表单必填项')
            return false
          }
          const { poNo } = this.ruleForm
          const { id: orderId } = this.$route.query
          /* 校验商品 */
          const isPassed = this.checkGoods()
          if (!isPassed) return false
          /* 获取请求参数 */
          const params = this.getFetchParams('2', '1')
          try {
            this.saveBtnLoading = true
            if (poNo) {
              const { data } = await checkExistByPo({ poNo, orderId })
              /* 当前已存在包含该po号的订单 */
              if (data.length) {
                this.$errorMsg(`PO号：${data[0]}已有存在销售订单信息`)
              } else {
                this.onSumbit('1', params)
              }
              return false
            }
            this.onSumbit('1', params)
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.saveBtnLoading = false
          }
        })
      },
      /* 审核 */
      handleExamine() {
        this.$refs.ruleForm.validate(async (valid) => {
          // 驳回状态下，不校验数据
          if (!valid && this.auditResult === '0') {
            this.$errorMsg('请正确填写表单必填项')
            return false
          }
          if (!this.auditResult) {
            this.$errorMsg('请先选择审核结果')
            return false
          }
          if (!this.rejectReason && this.auditResult === '1') {
            this.$errorMsg('请输入驳回原因')
            return false
          }
          if (this.rejectReason.length > 100 && this.auditResult === '1') {
            this.$errorMsg('驳回原因不能超过100个字')
            return false
          }
          /* 校验商品 驳回不校验 */
          if (this.auditResult === '0') {
            const isPassed = this.checkGoods()
            if (!isPassed) return false
          }

          /* 获取请求参数 */
          const params = this.getFetchParams('2', '2', this.auditResult)
          try {
            this.saveBtnLoading = true
            const poNo = this.ruleForm.poNo
            const { id: orderId } = this.$route.query
            // 审核通过才需要校验
            if (poNo && this.auditResult === '0') {
              const { data } = await checkExistByPo({ poNo, orderId })
              /* 当前已存在包含该po号的订单 */
              if (data.length) {
                this.$errorMsg(`PO号：${data[0]}已有存在销售订单信息`)
              } else {
                this.onSumbit('2', params)
              }
              return false
            }
            this.onSumbit('2', params)
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.saveBtnLoading = false
          }
        })
      },
      /* 提交或审核表单 */
      async onSumbit(type, params) {
        const { outDepotId: depotId, tallyingUnit } = this.ruleForm
        const enumsType = {
          1: '提交',
          2: '审核'
        }
        this.$showToast(`确定${enumsType[type]}该销售订单？`, async () => {
          try {
            this.saveBtnLoading = true
            // 非中转仓条件下， 提交或者审核为通过的前提下，校验库存可用量
            if (
              this.outDepotType !== 'd' &&
              (type === '1' || (type === '2' && this.auditResult === '0'))
            ) {
              // 校验结果,
              /**
               * 库存校验不通过
               * 提交：做提醒作用，点击可继续执行
               * 审核：不可继续执行
               */
              const validResult = await this.$checkInventoryNum(
                {
                  itemList: this.tableData.list.map((item) => {
                    return {
                      goodsId: item.goodsId,
                      goodsNo: item.goodsNo,
                      okNum: item.num || 0,
                      tallyingUnit: tallyingUnit || '',
                      depotId: depotId || '',
                      inventoryType: 2
                    }
                  })
                },
                // 弹框配置
                type === '1'
                  ? {
                      title: '可用量不足，确认是否操作',
                      btnText: '确认',
                      cancelBtnText: '取消'
                    }
                  : { title: '校验可用量' }
              )
              console.log('弹窗关闭回参', validResult)
              // 审核：校验不通过，不继续执行
              if (type === '2' && !validResult.isInventoryValidate) {
                return false
              }
              // 提交：校验不通过，点击取消, 不继续执行
              if (
                type === '1' &&
                !validResult.isInventoryValidate &&
                !validResult.isComfirm
              ) {
                return false
              }
            }

            /* 提交 */
            if (type === '1') {
              await submitSaleOrder(params)
              this.$successMsg(`${enumsType[type]}成功`)
              this.closeTag()
              this.linkTo('/sales/salesorder')
            }
            /* 执行审核 */
            if (type === '2') {
              await modifySaleOrder(params)
              /* 检查是否能生成采购单 */
              if (this.auditResult === '0') {
                const { poNo, merchantId, customerId } = this.ruleForm
                const {
                  data: { code, data: name }
                } = await checkSaleExistPurchase({
                  poNo,
                  merchantId,
                  customerId
                })
                if (code === '00') {
                  const { id } = this.$route.query
                  this.purchaseOrder.visible.show = true
                  this.purchaseOrder.id = id + ''
                  this.purchaseOrder.name = name
                  return false
                }
              }
              this.$successMsg(`${enumsType[type]}成功`)
              this.closeTag()
              this.linkTo('/sales/salesorder')
            }
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.saveBtnLoading = false
          }
        })
      },
      // 获取表单下拉框改变状态
      async getSelectState(type) {
        const { id } = this.$route.query
        const {
          merchantId,
          outDepotId,
          inDepotId,
          businessModel,
          customerId,
          buId,
          currency,
          isSameArea
        } = this.ruleForm
        // 下拉列表value转字符串
        this.ruleForm.customerId = customerId ? customerId + '' : ''
        this.ruleForm.buId = buId ? buId + '' : ''
        this.ruleForm.currency = currency ? currency + '' : ''
        this.ruleForm.inDepotId = inDepotId ? inDepotId + '' : ''
        this.ruleForm.outDepotId = outDepotId ? outDepotId + '' : ''
        this.ruleForm.businessModel = businessModel ? businessModel + '' : ''
        // 时间格式转换
        this.ruleForm.deliveryDate = this.ruleForm.deliveryDate
          ? this.ruleForm.deliveryDate.slice(0, 10)
          : ''
        // 导入商品参数
        this.importGoodsData = { orderId: id }
        // 有客户id
        if (customerId && merchantId && type !== 'edit') {
          // 根据客户带出税率
          const {
            data: { rate }
          } = await getRateByCustomerAndMerchant({
            customerId: customerId,
            merchantId: merchantId
          })
          this.customerTaxRate = rate ? rate.toFixed(2).toString() : '0.00'
          // 改变当前商品列表的税率
          const { list } = this.tableData
          if (list.length) {
            list.forEach((item, index) => {
              item.taxRate = rate ? rate.toFixed(2).toString() : '0.00'
              this.calc('taxRate', index)
            })
          }
        }
        // 根据事业部限制出库仓库
        if (buId) {
          delete this.selectOpt.out_warehousesList
          this.getSelectBeanByMerchantBuRel('out_warehousesList', {
            buId,
            type: 'a,c,d'
          })
        }
        // 库位类型
        this.reloadStockLocationType(this.ruleForm.stockLocationTypeId)
        // 获取出仓库状态
        if (outDepotId) {
          // 获取出仓库类型
          this.getDepotType('outDepot', outDepotId)
          // 校验出库仓
          this.validOutDepot()
        }
        // 获取入仓库状态
        if (inDepotId) {
          // 获取入仓库类型
          this.getDepotType('inDepot', inDepotId)
        }
        // 获取同关区状态
        if (isSameArea) {
          this.isSameAreaChange(isSameArea)
        }
        // 获取销售类型状态
        if (businessModel) {
          switch (businessModel) {
            // 采销执行和购销-整批结算时 po号必填
            case '3':
            case '1':
              this.rules.poNo.required = true
              break
            case '4':
              this.rules.poNo.required = false
              break
          }
        }
        if (customerId && buId) {
          /* 查询是否开启价格管理 */
          const { data: hasPriceManage } = await getSalePriceManage({
            customerId,
            buId
          })
          this.hasPriceManage = hasPriceManage
          /* 如果开启了价格管理，并且选择了币种则带出价格 */
          if (this.hasPriceManage && currency && this.tableData.list.length) {
            const { tallyingUnit: unitId } = this.ruleForm
            this.tableData.list.forEach(async (item) => {
              const json = JSON.stringify({
                customerId,
                currency,
                buId,
                unitId,
                commbarcode: item.commbarcode,
                goodId: item.goodsId
              })
              /* 查询价格 */
              const { data } = await getSalePrice({ json })
              if (data && data.length) {
                item.priceManageList = data.map((subItem) => ({
                  key: +(+subItem).toFixed(8),
                  value: +(+subItem).toFixed(8)
                }))
                const condition = item.priceManageList.find(
                  (e) => e.key === +(+item.price).toFixed(8)
                )
                /* 查询回显的单价是否存在列表中，存在就选中没有则置空 */
                item.price = condition ? +(+item.price).toFixed(8) : undefined
              } else {
                item.price = undefined
                item.amount = undefined
                item.taxAmount = undefined
                item.taxRate = '0.00'
                item.tax = undefined
                item.priceManageList = []
              }
            })
          }
        }
        /* 仓库关联货号  */
        outDepotId && this.changeGoodsInfoByDepot(outDepotId)
      },
      /* 显示选择商品弹窗 */
      showChooseProductModal({ isEditGoodsNo, barcode, editIndex }) {
        const { outDepotId, buId, customerId, currency } = this.ruleForm
        if (!outDepotId || !buId) {
          this.$errorMsg('请选择事业部和出仓仓库!')
          this.$refs.ruleForm.validateField(['buId', 'outDepotId'])
          return false
        }
        /* 开启价格管理的情况下 */
        if (this.hasPriceManage) {
          if (!customerId || !currency) {
            this.$errorMsg(
              '该公司事业部已开启销售价格管理，请先选择客户和销售币种'
            )
            return false
          }
          this.$refs.ruleForm.validateField(['currency', 'customerId'])
        }
        this.chooseProduct.data = {
          depotId: outDepotId,
          popupType: 2
        }
        /* 修改货号的情况 */
        if (isEditGoodsNo) {
          this.chooseProduct.data.barcode = barcode
          this.chooseProduct.isRadio = true
          this.editGoodsNo.isEditGoodsNo = isEditGoodsNo
          this.editGoodsNo.index = editIndex
        } else {
          this.chooseProduct.isRadio = false
          this.editGoodsNo.isEditGoodsNo = false
        }
        this.chooseProduct.visible.show = true
      },
      /* 确认选择商品 */
      comfirmChooseProduct(payload) {
        if (payload && payload.length) {
          /* 当前最大的序号 */
          const maxSeq = Math.max(
            ...[0].concat(this.tableData.list.map((item) => item.seq))
          )
          /* 价格管理开启后价格 */
          let resPrice = null
          /* 价格管理开启后价格列表 */
          let priceManageList = []
          payload.forEach(async (item, index) => {
            if (this.hasPriceManage) {
              const { customerId, currency, tallyingUnit, buId } = this.ruleForm
              const { commbarcode, id: goodId } = item
              try {
                const json = JSON.stringify({
                  customerId,
                  currency,
                  unitId: tallyingUnit,
                  buId,
                  commbarcode,
                  goodId
                })
                const { data } = await getSalePrice({ json })
                // 获取价格下拉列表
                if (data && Array.isArray(data) && data.length) {
                  priceManageList = data.map((subItem) => ({
                    key: subItem + '',
                    value: subItem
                  }))
                }
                // 价格列表只存在一个价格，则直接选中
                resPrice =
                  priceManageList.length === 1
                    ? priceManageList[0].value
                    : undefined
              } catch (error) {
                resPrice = undefined
                priceManageList = []
                this.$errorMsg(error.message)
              }
            }
            const i = this.tableData.list.length
            this.$set(this.tableData.list, i, {
              ...item,
              seq: maxSeq + index + 1,
              goodsId: item.id || '',
              goodsCode: item.goodsCode || '',
              goodsName: item.name || '',
              goodsNo: item.goodsNo || '',
              barcode: item.barcode || '',
              commbarcode: item.commbarcode || '',
              num: 1,
              price: this.hasPriceManage
                ? resPrice
                : item.filingPrice * (item.priceDeclareRatio || 1) || 0,
              amount: 0,
              taxAmount: 0,
              taxRate: (+this.customerTaxRate || 0).toFixed(2) || '0.00',
              tax: 0,
              priceManageList,
              delId: this.delId++
            })
            this.calc('num', i)
            // 价格没维护的情况下，置空
            if (resPrice === undefined) {
              this.tableData.list[i].price = undefined
              this.tableData.list[i].amount = undefined
              this.tableData.list[i].taxAmount = undefined
              this.tableData.list[i].tax = undefined
            }
          })
        }
        this.chooseProduct.visible.show = false
      },
      /* 确认修改货号 */
      comfirmEditGoodsNo(payload) {
        if (payload && payload.length) {
          const item = payload[0]
          const current = this.tableData.list[this.editGoodsNo.index]
          current.goodsId = item.id || ''
          current.goodsCode = item.goodsCode || ''
          current.goodsName = item.name || ''
          current.goodsNo = item.goodsNo || ''
        }
        this.editGoodsNo.isEditGoodsNo = false
        this.chooseProduct.isRadio = false
        this.chooseProduct.visible.show = false
      },
      // 删除商品
      delTableItems() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('当前没有选择任何商品')
          return false
        }
        this.$showToast('确定要删除勾选的商品？', () => {
          const ids = this.tableChoseList.map((item) => item.delId)
          this.tableData.list = this.tableData.list.filter(
            (item) => !ids.includes(item.delId)
          )
        })
      },
      // 计算表格行数据
      calc(type, index) {
        const data = this.tableData.list[index]
        let {
          price = 0,
          num = 0,
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
          num = 0,
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
      // 校验商品
      checkGoods(isSave) {
        let flag = true
        if (!isSave && !this.tableData.list.length) {
          this.$errorMsg('至少选择一件商品')
          flag = false
          return flag
        }
        if (isSave) {
          return flag
        }
        for (let i = 0; i < this.tableData.list.length; i++) {
          const item = this.tableData.list[i]
          if (!item.num || item.num < 0) {
            this.$errorMsg(`表格第${i + 1}行，商品数量必须是大于0的数`)
            flag = false
            break
          }
          if (
            item.price === null ||
            item.price === undefined ||
            item.price === '' ||
            item.price < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行，商品单价不能为空或者小于0`)
            flag = false
            break
          }
          if (
            item.amount === null ||
            item.amount === undefined ||
            item.amount === '' ||
            item.amount < 0
          ) {
            this.$errorMsg(
              `表格第${i + 1}行，总金额（不含税）不能为空或者小于0`
            )
            flag = false
            break
          }
          if (
            item.taxAmount === null ||
            item.taxAmount === undefined ||
            item.taxAmount === '' ||
            item.taxAmount < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行，总金额（含税）不能为空或者小于0`)
            flag = false
            break
          }
          if (item.taxRate === null || item.taxRate === undefined) {
            this.$errorMsg(`表格第${i + 1}行，税率不能为空`)
            flag = false
            break
          }
          if (item.tax === undefined || item.tax === null || item.tax < 0) {
            this.$errorMsg(`表格第${i + 1}行，税额不能为空或0`)
            flag = false
            break
          }
        }
        return flag
      },
      // 客户改变
      async customerIdChange(customerId) {
        if (!customerId) return false
        const { buId } = this.ruleForm
        try {
          // 是否开启价格管理
          if (customerId && buId) {
            const hasPriceManage = await getSalePriceManage({
              customerId,
              buId
            })
            this.hasPriceManage = hasPriceManage.data
          }
          // 根据客户带出币种
          const { data } = await getCurrencySelectBean({ customerId })
          data
            ? (this.ruleForm.currency = data)
            : this.$refs.currency.resetField()
          // 根据客户带出税率和销售类型
          const {
            data: { rate, businessModel }
          } = await getRateByCustomerAndMerchant({
            customerId: customerId,
            merchantId: this.ruleForm.merchantId
          })
          // 清空销售类型
          this.$refs.businessModel.resetField()
          this.ruleForm.businessModel = businessModel || ''
          this.customerTaxRate = rate ? rate.toFixed(2) + '' : ''
          // 改变当前商品列表的税率
          const list = this.tableData.list
          if (!list.length) return false
          list.forEach((item, index) => {
            item.taxRate = rate ? rate.toFixed(2).toString() : '0.00'
            this.calc('taxRate', index)
          })
          // 价格加载
          this.reloadPrice()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 事业部改变
      async buIdChange(buId) {
        // 清空出库仓库
        this.$refs.outDepotId.resetField()
        // 清空销售类型
        this.$refs.businessModel.resetField()
        // 清空商品列表
        this.tableData.list = []
        //  清空库位类型
        this.$refs.stockLocationType.resetField()
        // 库位类型
        this.reloadStockLocationType()
        const { customerId } = this.ruleForm
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
      // 销售类型改变
      businessModelChange(businessModel) {
        switch (businessModel) {
          // 采销执行和购销-整批结算时 po号必填
          case '3':
          case '1':
            this.rules.poNo.required = true
            break
          case '4':
            this.rules.poNo.required = false
            break
        }
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
      /* 切换出库仓前校验 */
      async outDepotBeforeChange(depotId) {
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
      /* 出库仓改变 */
      outDepotChange(depotId) {
        /* 清空销售类型 */
        this.$refs.businessModel.resetField()
        /* 切换仓库修改商品信息 */
        this.changeGoodsInfoByDepot(depotId)
        /* 获取出仓库类型 */
        this.getDepotType('outDepot', depotId)
        /* 校验出库仓 */
        this.validOutDepot()
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
          this.tableData.list.forEach((item, index) => {
            const { id, goodsCode, name, goodsNo } =
              data[index].merchandiseInfoModel || {}
            item.goodsId = id || ''
            item.goodsCode = goodsCode || ''
            item.goodsName = name || ''
            item.goodsNo = goodsNo || ''
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
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
                  // 当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库不禁用，可选仓库仅为备查库
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
      // 显示商品导入弹窗
      showImportGoodsModal() {
        const {
          outDepotId,
          customerId,
          currency,
          tallyingUnit,
          buId,
          businessModel
        } = this.ruleForm
        if (!buId) {
          this.$errorMsg('请先选择事业部!')
          return false
        }
        if (!outDepotId) {
          this.$errorMsg('请先选择出仓仓库!')
          return false
        }
        if (!customerId) {
          this.$errorMsg('请先选择客户!')
          return false
        }
        if (!businessModel) {
          this.$errorMsg('请先选择销售类型!')
          return false
        }
        if (this.tableData.list.length) {
          this.importSaleGoodsText = '商品导出'
        } else {
          this.importSaleGoodsText = '下载模板'
        }
        this.importGoodsData = {
          outDepotId: outDepotId || '',
          customerId: customerId || '',
          currency: currency || '',
          unitId: tallyingUnit || '',
          buId: buId || '',
          salePriceManage: this.hasPriceManage
        }
        this.selfDown = true
        this.importGoodsVisible.show = true
      },
      // 商品导出
      async downGoodsTemplate() {
        const itemList = this.tableData.list.map((item) => ({
          seq: item.seq ? item.seq + '' : '',
          goodsNo: item.goodsNo || '',
          num: item.num ? item.num + '' : '0',
          amount: item.amount ? item.amount + '' : '0',
          barcode: item.barcode || ''
        }))
        const json = JSON.stringify({ itemList })
        this.actionUrl =
          getBaseUrl(exportItemsUrl) +
          exportItemsUrl +
          `?token=${sessionStorage.getItem('token')}`
        this.downVal = json
        // 下载
        this.$nextTick(() => {
          const form = document.getElementById('down-up')
          form.submit()
          this.downVal = ''
        })
      },
      // 导入商品成功
      async importGoodsSuccess(res) {
        if (res.data.failure + '' !== '0') {
          return false
        }
        this.importGoodsVisible.show = false
        if (res.data && res.data.data && res.data.data.length) {
          const list = res.data.data
          const ids = list.map((item) => item.goodsId).toString()
          const { data } = await getListByIds({ ids })
          if (data && data.length) {
            if (this.tableData.list.length) {
              this.$showToast('确认覆盖已存在的所有商品信息？', async () => {
                this.tableData.list = []
                this.mergeImportGoods(data, list)
              })
            } else {
              this.mergeImportGoods(data, list)
            }
          }
        }
      },
      // 合并导入数据
      mergeImportGoods(data, list) {
        data.forEach((item, index) => {
          this.tableData.list[index] = {
            seq: list[index].seq || index + 1,
            // id: item.id ? item.id + '' : '',
            goodsId: list[index].goodsId || '',
            goodsCode: item.goodsCode || '',
            goodsName: item.name || '',
            goodsNo: list[index].goodsNo || item.goodsNo || '',
            barcode: list[index].barcode || item.barcode || '',
            num: list[index].num || item.num || 1,
            price: list[index].price || item.price || 0,
            amount: list[index].amount || 0,
            taxAmount: item.taxAmount || 0,
            taxRate: item.taxRate ? item.taxRate.toFixed(2) : '0.00',
            tax: item.tax || 0,
            brandName: item.brandName || '',
            priceManageList:
              list[index].priceList && list[index].priceList.length
                ? list[index].priceList.map((subItem) => ({
                    value: subItem + '',
                    label: subItem
                  }))
                : [],
            delId: this.delId++
          }
          // 计算每行商品信息
          this.calc('num', index, true)
        })
      },
      // 生成采购订单成功
      createPurchaseOrder() {
        this.purchaseOrder.visible.show = false
        this.$successMsg('操作成功')
        this.closeTag()
      },
      // 显示预售转销选择商品数量弹窗
      async showChoosePreSaleNumModal() {
        try {
          const { data } = await checkPreSale({ codes: this.preSaleOrderCode })
          if (data && data.length) {
            const itemList = data
            const { preSaleOrderCode, poNo, customerId } = data[0]
            this.choosePreSaleNum.form = { preSaleOrderCode, poNo, customerId }
            this.choosePreSaleNum.form.customerId = customerId + ''
            this.choosePreSaleNum.itemList = itemList
          }
          this.choosePreSaleNum.visible.show = true
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 关闭预售转销选择商品数量弹窗
      closeChoosePreSaleNumModal(payload) {
        this.choosePreSaleNum.visible.show = false
        this.choosePreSaleNum.form = {}
        this.choosePreSaleNum.itemList = []
        if (payload && payload.length) {
          this.tableData.list = []
          payload.forEach((item, index) => {
            this.tableData.list[index] = {
              seq: item.seq || index + 1,
              id: item.id + '',
              goodsId: item.goodsId + '',
              goodsCode: item.goodsCode || '',
              goodsName: item.goodsName || '',
              goodsNo: item.goodsNo || '',
              barcode: item.barcode || '',
              num: item.num || 1,
              price: item.price || 0,
              amount: item.amount || 0,
              taxAmount: item.taxAmount || 0,
              taxRate: item.taxRate ? item.taxRate.toFixed(2) : '0.00',
              tax: item.tax || 0,
              delId: this.delId++
            }
            // 计算每行商品信息
            this.calc('num', index)
          })
        }
      },
      // 合计方法
      summaryMethod({ data }) {
        const sums = []
        let nums = 0
        let amounts = 0
        let taxAmounts = 0
        let taxs = 0
        data.forEach((item) => {
          nums += item.num ? +item.num : 0
          amounts += item.amount ? +item.amount : 0
          taxAmounts += item.taxAmount ? +item.taxAmount : 0
          taxs += item.tax ? +item.tax : 0
        })
        sums[1] = '合计：'
        sums[6] = nums
        sums[8] = (+amounts).toFixed(2)
        sums[9] = (+taxAmounts).toFixed(2)
        sums[11] = (+taxs).toFixed(2)
        return sums
      },
      // 改变客户，币种，理货单位，商品列表，价格重新请求加载
      async reloadPrice() {
        // 客户、事业部 有值的情况下才能继续
        if (!this.ruleForm.customerId || !this.ruleForm.buId) return false
        // 存在商品的情况下继续
        if (!this.tableData.list.length) return false
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
          const ids = this.tableData.list.map((item) => item.goodsId).toString()
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
          // 开启价格管理，价格处理
          if (this.hasPriceManage) {
            const json = JSON.stringify({
              customerId: this.ruleForm.customerId,
              currency: this.ruleForm.currency,
              unitId: this.ruleForm.tallyingUnit,
              buId: this.ruleForm.buId,
              commbarcode: item.commbarcode,
              goodId: item.goodsId
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
              // 如何返回价格只有一个则直接选中
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
            item.price = basePriceMap[item.goodsId] || ''
          }
          if (item.price === undefined) {
            item.amount = undefined
            item.taxAmount = undefined
            item.tax = undefined
          } else {
            this.calc('price', index)
          }
        })
      },
      // 库位类型，事业部选择后调整
      async reloadStockLocationType(selectId) {
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
        // 选中
        selectId && (this.ruleForm.stockLocationTypeId = selectId)
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.sales-order-add {
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
    .title {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  ::v-deep.remark {
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
