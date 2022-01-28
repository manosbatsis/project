<template>
  <div class="page-bx bgc-w edit-bx">
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 面包屑 -->
      <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
      <!-- 面包屑 end -->
      <!-- 基本信息 -->
      <JFX-title title="基本信息" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="预申报单号：" prop="code">
            <el-input
              v-model="ruleForm.code"
              placeholder="请输入"
              clearable
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="客户：" prop="customerId">
            <el-select
              v-model="ruleForm.customerId"
              placeholder="请选择"
              clearable
              disabled
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
          <el-form-item label="事业部：" prop="buId" ref="buId">
            <el-select
              v-model="ruleForm.buId"
              placeholder="请选择"
              clearable
              filterable
              disabled
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
          <el-form-item label="PO单号：" prop="poNo">
            <el-input
              v-model="ruleForm.poNo"
              type="textarea"
              :autosize="{ minRows: 1, maxRows: 5 }"
              clearable
              placeholder="多PO输入时以&字符隔开"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="退出仓库：" prop="outDepotId" ref="outDepotId">
            <el-select
              v-model="ruleForm.outDepotId"
              placeholder="请选择"
              clearable
              filterable
              disabled
              @change="outDepotChange"
              :data-list="getSelectBeanByMerchantRel('out_warehousesList')"
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
          <el-form-item label="退入仓库：" prop="inDepotId" ref="outDepotId">
            <el-select
              v-model="ruleForm.inDepotId"
              placeholder="请选择"
              clearable
              filterable
              @change="inDepotChange"
              :data-list="getSelectBeanByMerchantRel('in_warehousesList')"
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
          <el-form-item label="退货币种：" prop="currency" ref="currency">
            <el-select
              v-model="ruleForm.currency"
              placeholder="请选择"
              clearable
              filterable
              disabled
              :data-list="getCurrencySelectBean('currencyList')"
            >
              <el-option
                v-for="item in selectOpt.currencyList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
              、
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="LBX单号：" prop="lbxNo">
            <el-input v-model="ruleForm.lbxNo" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="关联销售退单号：" prop="saleReturnOrderCodes">
            <el-input
              v-model="ruleForm.saleReturnOrderCodes"
              placeholder="请输入"
              clearable
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="退货原因：" prop="returnReason">
            <el-input
              v-model="ruleForm.returnReason"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
          v-if="inDepotType === 'c'"
        >
          <el-form-item label="理货单位：" prop="tallyingUnit">
            <el-select
              v-model="ruleForm.tallyingUnit"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in [
                  { value: '00', label: '托盘' },
                  { value: '01', label: '箱' },
                  { value: '02', label: '件' }
                ]"
                :key="item.value"
                :value="item.value"
                :label="item.label"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="12">
          <el-form-item label="贸易条款：" prop="tradeTerms">
            <el-radio-group
              v-model="ruleForm.tradeTerms"
              :data-list="getSelectList('saleReturnDeclare_tradeTermsList')"
            >
              <el-radio
                v-for="item in selectOpt.saleReturnDeclare_tradeTermsList"
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
      <!-- 物流信息 -->
      <JFX-title title="物流信息" className="mr-t-20" />
      <el-row :gutter="10" class="fs-12 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="装货港：" prop="loadPort">
            <el-input
              v-model.trim="ruleForm.loadPort"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="卸货港：" prop="portDestNo">
            <el-select
              v-model="ruleForm.portDestNo"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in [
                  { label: '44011501：南沙新港口岸', value: '44011501' },
                  { label: '44010318：黄埔广保通码头口岸', value: '44010318' },
                  { label: '21021001：大连保税区口岸', value: '21021001' },
                  { label: '50010001：重庆口岸', value: '50010001' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="收货地点：" prop="deliveryAddr">
            <el-input
              v-model.trim="ruleForm.deliveryAddr"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="境外发货人：" prop="shipper">
            <el-input
              v-model.trim="ruleForm.shipper"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="发票号：" prop="invoiceNo">
            <el-input
              v-model.trim="ruleForm.invoiceNo"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="报关合同号：" prop="contractNo">
            <el-input
              v-model.trim="ruleForm.contractNo"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="提单毛重(Kg)：" prop="billWeight">
            <JFX-Input
              v-model.trim="ruleForm.billWeight"
              :min="0"
              :precision="5"
              placeholder="请输入"
              clearable
              @change="countWeight"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="箱数：" prop="boxNum">
            <el-input-number
              v-model.trim="ruleForm.boxNum"
              :precision="0"
              :controls="false"
              :min="0"
              style="width: 100%"
              @change="changePackType"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="托数：" prop="torrNum">
            <el-input-number
              v-model.trim="ruleForm.torrNum"
              :precision="0"
              :controls="false"
              :min="0"
              style="width: 100%"
              @change="changePackType"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="包装方式：" prop="pack">
            <el-input
              v-model.trim="ruleForm.pack"
              clearable
              style="width: 100%"
              placeholder="XX箱/xx托XXX"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="出库关区：" prop="outCustomsId">
            <el-select
              v-model="ruleForm.outCustomsId"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in outCustomsList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="入库关区：" prop="inCustomsId">
            <el-select
              v-model="ruleForm.inCustomsId"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in inCustomsList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="头程提单号：" prop="ladingBill">
            <el-input
              v-model.trim="ruleForm.ladingBill"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="唛头：" prop="mark">
            <el-input
              v-model.trim="ruleForm.mark"
              clearable
              style="width: 100%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="备注：" prop="remark">
            <el-input
              v-model.trim="ruleForm.remark"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="12">
          <el-form-item label="托盘材质：" prop="torrPackType">
            <el-radio-group
              v-model="ruleForm.torrPackType"
              :data-list="getSelectList('order_torrpacktypeList')"
              @change="changePackType"
            >
              <el-radio
                v-for="item in selectOpt.order_torrpacktypeList"
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
      </el-row>
      <!-- 物流信息 end -->
      <!-- 表格 -->
      <JFX-title title="销售退货商品明细" className="mr-t-10 mr-t-20" />
      <JFX-table
        :tableData.sync="tableData"
        :tableColumn="tableColumn"
        :showPagination="false"
      >
        <template slot="seq" slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.seq"
            :precision="0"
            :controls="false"
            :min="0"
            style="width: 100%"
          ></el-input-number>
        </template>
        <template slot="num" slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.num"
            :precision="0"
            :min="0"
            :controls="false"
            disabled
            style="width: 100%"
          ></el-input-number>
        </template>
        <template slot="price" slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.price"
            v-max-spot="8"
            :precision="8"
            :controls="false"
            :min="0"
            disabled
            style="width: 100%"
          ></el-input-number>
        </template>
        <template slot="amount" slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.amount"
            v-max-spot="5"
            :precision="5"
            :controls="false"
            :min="0"
            disabled
            style="width: 100%"
          ></el-input-number>
        </template>
        <template slot="taxAmount" slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.taxAmount"
            v-max-spot="5"
            :precision="5"
            :controls="false"
            :min="0"
            disabled
            style="width: 100%"
          ></el-input-number>
        </template>
        <template slot="taxRate" slot-scope="{ row }">
          <el-select
            v-model="row.taxRate"
            placeholder="请选择"
            filterable
            disabled
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
        <template slot="batchNo" slot-scope="{ row }">
          <el-input v-model="row.batchNo" clearable />
        </template>
        <template slot="brandName" slot-scope="{ row }">
          <el-input v-model="row.brandName" clearable />
        </template>
        <template slot="grossWeightSum" slot-scope="{ row }">
          <JFX-Input
            v-model.trim="row.grossWeightSum"
            :precision="5"
            :min="0"
            style="width: 100%"
            @change="countSumWeight"
          ></JFX-Input>
        </template>
        <template slot="netWeightSum" slot-scope="{ row }">
          <JFX-Input
            v-model.trim="row.netWeightSum"
            :precision="5"
            :min="0"
            style="width: 100%"
          ></JFX-Input>
        </template>
        <template slot="boxNo" slot-scope="{ row }">
          <el-input v-model="row.boxNo" clearable />
        </template>
      </JFX-table>
      <!-- 表格 end-->
    </JFX-Form>
    <!-- 操作 -->
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" :loading="actionBtnLoading" @click="onSave">
        保 存
      </el-button>
      <el-button
        type="primary"
        :loading="actionBtnLoading"
        v-permission="'saleReturn_declare_examine'"
        @click="onExamine"
      >
        审 核
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
    <!-- 操作 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getDepotDetails, getCustomsAreaList } from '@a/salesManage/index'
  import {
    saleReturnToDeclareOrder,
    saveSaleDeclarReturnOrder,
    auditSaleDeclarReturnOrder,
    getSaleDeclarReturnDetail
  } from '@a/salesReturnManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 面包屑
        breadcrumb: [
          { path: '', meta: { title: '销售退货管理' } },
          {
            path: '/saleReturn/PreDeclarationList',
            meta: { title: '销售退货预申报单' }
          },
          !this.$route.query.id
            ? {
                path: '/saleReturn/PredeclarationAdd',
                meta: { title: '销售预申报单新增' }
              }
            : {
                path: `/saleReturn/PreDeclarationEdit?id=${this.$route.query.id}`,
                meta: { title: '销售预申报单编辑' }
              }
        ],
        // 表单数据
        ruleForm: {
          // 基本信息
          code: '',
          customerId: '',
          buId: '',
          poNo: '',
          outDepotId: '',
          inDepotId: '',
          currency: '',
          lbxNo: '',
          saleReturnOrderCodes: '',
          returnReason: '',
          tallyingUnit: '',
          tradeTerms: '',
          // 物流信息
          loadPort: '',
          portDestNo: '',
          deliveryAddr: '',
          shipper: '',
          invoiceNo: '',
          contractNo: '',
          billWeight: '',
          boxNum: '',
          torrNum: '',
          pack: '',
          outCustomsId: '',
          inCustomsId: '',
          ladingBill: '',
          mark: '',
          remark: '',
          torrPackType: '',
          transport: ''
        },
        // 表单校验
        rules: {
          code: {
            required: true,
            message: '请输入预申报单号',
            trigger: 'blur'
          },
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          buId: { required: true, message: '请选择事业部', trigger: 'change' },
          poNo: { required: true, message: '请输入PO单号', trigger: 'blur' },
          outDepotId: {
            required: false,
            message: '请选择退出仓库',
            trigger: 'change'
          },
          inDepotId: {
            required: true,
            message: '请选择退入仓库',
            trigger: 'change'
          },
          currency: {
            required: true,
            message: '请选择销售币种',
            trigger: 'change'
          },
          loadPort: {
            required: true,
            message: '请输入装货港',
            trigger: 'blur'
          },
          portDestNo: {
            required: true,
            message: '请选择卸货港',
            trigger: 'change'
          },
          deliveryAddr: {
            required: true,
            message: '请输入收货地点',
            trigger: 'blur'
          },
          shipper: {
            required: true,
            message: '请输入境外发货人',
            trigger: 'blur'
          },
          invoiceNo: {
            required: true,
            message: '请输入发票号',
            trigger: 'blur'
          },
          contractNo: {
            required: true,
            message: '请输入报关合同号',
            trigger: 'blur'
          },
          billWeight: {
            required: true,
            message: '请输入提单毛重（Kg）',
            trigger: 'blur'
          },
          boxNum: { required: true, message: '请输入箱数', trigger: 'blur' },
          pack: { required: true, message: '请输入包装方式', trigger: 'blur' },
          mark: { required: true, message: '请输入唛头', trigger: 'blur' },
          tallyingUnit: {
            required: false,
            message: '请选择理货单位',
            trigger: 'change'
          },
          tradeTerms: {
            required: true,
            message: '请选择贸易条款',
            trigger: 'change'
          }
        },
        // 表格列数据
        tableColumn: [
          {
            label: '序号',
            slotTo: 'seq',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '销售退货单号',
            prop: 'saleReturnOrderCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: 'PO单号',
            prop: 'poNo',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '退出商品货号',
            prop: 'outGoodsNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品货号',
            prop: 'inGoodsNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品名称',
            prop: 'inGoodsName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '退入商品条形码',
            prop: 'inBarcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '退货商品数量',
            slotTo: 'num',
            width: '150',
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
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '税额',
            prop: 'tax',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '批次号',
            slotTo: 'batchNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '品牌类型',
            slotTo: 'brandName',
            width: '160',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '毛重',
            slotTo: 'grossWeightSum',
            width: '120',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '净重',
            slotTo: 'netWeightSum',
            width: '120',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '箱号',
            slotTo: 'boxNo',
            width: '120',
            align: 'center',
            hide: true
          }
        ],
        // 操作按钮loading
        actionBtnLoading: false,
        // 出库仓类型
        inDepotType: '',
        // 出库关区列表
        outCustomsList: [],
        // 入库关区列表
        inCustomsList: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        const { id, saleReturnIds } = this.$route.query
        id ? this.editInit(id) : this.addInit(saleReturnIds)
      },
      async addInit(saleReturnIds) {
        try {
          const { data } = await saleReturnToDeclareOrder({ saleReturnIds })
          this.mapDataToPage(data || {})
          this.mapSelectListToPage()
          // 新增不需要预申报单号
          this.rules.code.required = false
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      async editInit(declareOrderId) {
        try {
          const { data } = await getSaleDeclarReturnDetail({ declareOrderId })
          this.mapDataToPage(data || {})
          this.mapSelectListToPage()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 回显数据
      mapDataToPage(data) {
        const { ruleForm } = this
        Object.keys(ruleForm).forEach((key) => {
          ruleForm[key] = ![undefined, null].includes(data[key])
            ? data[key] + ''
            : ''
        })
        // 包装方式转换格式
        const { id } = this.$route.query
        if (!id) {
          this.ruleForm.pack =
            (this.ruleForm.boxNum || '0') +
            '箱/' +
            (this.ruleForm.torrNum || '0') +
            '托'
        }
        const { itemList } = data
        if (itemList && itemList.length) {
          this.tableData.list = itemList.map((item) => ({
            ...item,
            brandName: item.brandName || '境外品牌(其他)'
          }))
        }
      },
      // 获取下拉列表
      mapSelectListToPage() {
        const { outDepotId, inDepotId } = this.ruleForm
        if (outDepotId) {
          this.getCustomsAreaList('outDepot', outDepotId)
        }
        if (inDepotId) {
          this.getCustomsAreaList('inDepot', inDepotId)
          this.inDepotChange(inDepotId)
        }
      },
      // 计算总毛重
      countSumWeight() {
        let total = 0 // 总毛重
        let maxlen = 0 // 最大小数位
        this.tableData.list.map((item) => {
          total += item.grossWeightSum ? +item.grossWeightSum : 0
          const s = total + ''
          if (s.includes('.')) {
            const len = s.split('.').length
            len > maxlen && (maxlen = len)
          }
        })
        this.ruleForm.billWeight = total.toFixed(maxlen)
      },
      // 计算毛重
      countWeight() {
        const billWeight = this.ruleForm.billWeight
          ? this.ruleForm.billWeight + ''
          : '0'
        let maxlen = 3
        if (billWeight.includes('.')) {
          const arr = billWeight.split('.')
          maxlen = arr[1].length
        }
        const list = this.tableData.list
        let sum = 0
        list.forEach((item) => {
          const netWeightSum = item.netWeightSum ? +item.netWeightSum : 0
          sum = sum + netWeightSum // 计算总净重
        })
        const len = list.length
        let acouSum = 0
        list.forEach((item, i) => {
          if (i !== len - 1) {
            const netWeightSum = item.netWeightSum ? +item.netWeightSum : 0
            const grossWeightSum =
              sum > 0 ? billWeight * (netWeightSum / sum) : 0
            item.grossWeightSum = grossWeightSum
              ? grossWeightSum.toFixed(maxlen)
              : 0
            acouSum = acouSum + Number(item.grossWeightSum)
          } else {
            const grossWeightSum = billWeight - acouSum
            item.grossWeightSum =
              grossWeightSum && grossWeightSum > 0
                ? grossWeightSum.toFixed(maxlen)
                : 0
          }
        })
        this.tableData.list = list
      },
      // 保存数据
      async onSave() {
        if (!this.checkTableData()) return false
        const { id } = this.$route.query
        const data = {
          id: id || '',
          ...this.ruleForm,
          itemList: this.tableData.list
        }
        try {
          this.saveLoading = true
          await saveSaleDeclarReturnOrder(data)
          this.$successMsg('保存成功!')
          this.closeTag()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.saveLoading = false
        }
      },
      // 审核
      onExamine() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            if (!this.checkTableData()) return false
            const { id } = this.$route.query
            const data = {
              id: id || '',
              ...this.ruleForm,
              itemList: this.tableData.list
            }
            try {
              this.saveLoading = true
              await auditSaleDeclarReturnOrder(data)
              this.$successMsg('保存成功!')
              this.closeTag()
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.saveLoading = false
            }
          } else {
            this.$errorMsg('请先完善信息')
          }
        })
      },
      // 校验表格项
      checkTableData() {
        let flag = true
        for (let i = 0; i < this.tableData.list.length; i++) {
          const {
            num,
            amount,
            price,
            taxAmount,
            grossWeightSum,
            netWeightSum,
            brandName
          } = this.tableData.list[i]
          if (num === null || num === undefined || num === '' || num < 0) {
            this.$errorMsg(`表格第${i + 1}行,退货商品数量不能小于0`)
            flag = false
            break
          }
          if (
            price === null ||
            price === undefined ||
            price === '' ||
            price < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行,退货商品单价不能小于0`)
            flag = false
            break
          }
          if (
            amount === null ||
            amount === undefined ||
            amount === '' ||
            amount < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行,退货总金额(不含税)不能小于0`)
            flag = false
            break
          }
          if (
            taxAmount === null ||
            taxAmount === undefined ||
            taxAmount === '' ||
            taxAmount < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行,退货总金额(含税)不能小于0`)
            flag = false
            break
          }
          if (
            grossWeightSum === null ||
            grossWeightSum === undefined ||
            grossWeightSum === '' ||
            grossWeightSum < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行,毛重不能小于0`)
            flag = false
            break
          }
          if (
            netWeightSum === null ||
            netWeightSum === undefined ||
            netWeightSum === '' ||
            netWeightSum < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行,净重不能小于0`)
            flag = false
            break
          }
          if (Number(grossWeightSum) < Number(netWeightSum)) {
            this.$errorMsg(`表格第${i + 1}行,毛重不能小于净重`)
            flag = false
            break
          }
          if (!brandName) {
            this.$errorMsg(`表格第${i + 1}行,品牌类型不能为空`)
            flag = false
            break
          }
        }
        return flag
      },
      // 包装改变
      changePackType() {
        const { order_torrpacktypeList: list } = this.selectOpt
        const { torrPackType, boxNum, torrNum } = this.ruleForm
        const target = list.find(({ key }) => torrPackType === key)
        this.ruleForm.pack =
          (boxNum || '0') +
          '箱/' +
          (torrNum || '0') +
          '托' +
          (target ? target.value : '')
      },
      // 获取出入库关区列表
      async getCustomsAreaList(type, depotId) {
        try {
          const { data } = await getCustomsAreaList({ depotId })
          if (!data || !data.length) {
            if (type === 'inDepot') {
              this.inCustomsList = []
              this.ruleForm.inCustomsId = ''
            } else {
              this.outCustomsList = []
              this.ruleForm.outCustomsId = ''
            }
            return false
          }
          const list = data.map((item) => ({
            key: item.value,
            value: item.label
          }))
          if (type === 'inDepot') {
            this.inCustomsList = list
            !this.ruleForm.inCustomsId &&
              (this.ruleForm.inCustomsId = list[0].key)
          } else {
            this.outCustomsList = list
            !this.ruleForm.outCustomsId &&
              (this.ruleForm.outCustomsId = list[0].key)
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 提货方式改变
      mailModeChange(mailMode) {
        if (mailMode === '1') {
          this.ruleForm.receiverName = ''
          this.ruleForm.country = ''
          this.ruleForm.receiverAddress = ''
        } else if (mailMode === '2') {
          this.ruleForm.receiverName = '卓志香港仓'
          this.ruleForm.country = '中国香港'
          this.ruleForm.receiverAddress = '香港 新界 元朗 流浮山路DD129'
        }
      },
      // 出库仓库改变
      outDepotChange(id) {
        // 获取出库关区列表
        this.getCustomsAreaList('outDepot', id)
      },
      // 入库仓改变
      async inDepotChange(id) {
        try {
          // 获取入库关区列表
          this.getCustomsAreaList('inDepot', id)
          const {
            data: { type }
          } = await getDepotDetails({ id })
          this.inDepotType = type || ''
          // 入库仓为海外仓情况理货单位必填
          this.rules.tallyingUnit.required = type === 'c'
          this.ruleForm.tallyingUnit =
            type === 'c' ? this.ruleForm.tallyingUnit : ''
        } catch (error) {
          this.$errorMsg(error.message)
        }
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
