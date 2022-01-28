<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->

    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetForm" @search="getList(1)">
      <JFX-Form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="采购订单编号："
              prop="codes"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                type="textarea"
                :autosize="{ minRows: 1, maxRows: 3 }"
                v-model.trim="ruleForm.codes"
                clearable
                style="width: 110%"
                placeholder="多个订单用英文逗号隔开"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO号："
              prop="poNos"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                type="textarea"
                :autosize="{ minRows: 1, maxRows: 3 }"
                v-model.trim="ruleForm.poNos"
                clearable
                style="width: 110%"
                placeholder="多个订单用英文逗号隔开"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="供应商："
              prop="supplierId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.supplierId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSupplierList('gongyingList')"
              >
                <el-option
                  v-for="item in selectOpt.gongyingList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.buId"
                placeholder="请选择"
                filterable
                clearable
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
            <el-form-item
              label="入库仓库："
              prop="depotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.depotId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectBeanByMerchantRel('ruku_list')"
              >
                <el-option
                  v-for="item in selectOpt.ruku_list"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="创建人："
              prop="createName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.createName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="入库单编码："
              prop="warehouseCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.warehouseCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="业务模式："
              prop="businessModel"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.businessModel"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('purchaseOrder_businessModelList')"
              >
                <el-option
                  v-for="item in selectOpt.purchaseOrder_businessModelList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="是否完结："
              prop="isEnd"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.isEnd"
                placeholder="请选择"
                clearable
                :data-list="getSelectList('purchaseOrder_isEndList')"
              >
                <el-option
                  v-for="item in selectOpt.purchaseOrder_isEndList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="金额调整状态："
              prop="amountAdjustmentStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.amountAdjustmentStatus"
                placeholder="请选择"
                clearable
                :data-list="
                  getSelectList('purchaseOrder_amountAdjustmentStatusList')
                "
              >
                <el-option
                  v-for="item in selectOpt.purchaseOrder_amountAdjustmentStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="金额确认状态："
              prop="amountConfirmStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.amountConfirmStatus"
                placeholder="请选择"
                clearable
                :data-list="
                  getSelectList('purchaseOrder_amountConfirmStatusList')
                "
              >
                <el-option
                  v-for="item in selectOpt.purchaseOrder_amountConfirmStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="融资申请号："
              prop="financingNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.financingNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="红冲状态："
              prop="writeOffStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.writeOffStatus"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('purchaseOrder_writeOffStatusList')"
              >
                <el-option
                  v-for="item in selectOpt.purchaseOrder_writeOffStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item prop="date" label="录单日期：">
              <el-date-picker
                clearable
                v-model="createDate"
                type="daterange"
                :default-time="['00:00:00', '23:59:59']"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item prop="date" label="发票日期：">
              <el-date-picker
                clearable
                v-model="invoiceDate"
                type="daterange"
                :default-time="['00:00:00', '23:59:59']"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->

    <!-- 操作 -->
    <el-row class="mr-t-20 action-bar">
      <el-col :span="24">
        <el-button
          v-permission="'purchase_add'"
          type="primary"
          @click="linkTo('/purchase/purchaseOrderedit/add')"
        >
          新增
        </el-button>
        <el-button
          v-permission="'purchase_endPurchaseOrderCheck'"
          type="primary"
          :loading="finishLoading"
          @click="finishIn"
        >
          完结入库
        </el-button>
        <el-button
          v-permission="'purchase_import'"
          type="primary"
          @click="importFile"
        >
          导入
        </el-button>
        <el-button
          v-permission="'purchase_delPurchaseOrder'"
          type="primary"
          @click="deleteTableItem"
        >
          删除
        </el-button>
        <el-button
          v-permission="'purchase_generateDeclareOrder'"
          type="primary"
          @click="createShen"
        >
          生成预申报单
        </el-button>
        <el-button
          v-permission="'purchase_inWarehouse'"
          type="primary"
          @click="purchaseInWarehouse"
        >
          中转仓入库
        </el-button>
        <el-button
          v-permission="'purchase_trade_link'"
          type="primary"
          @click="createTransactionLink"
        >
          创建交易链路
        </el-button>
        <el-button
          v-permission="'purchase_sd_create'"
          :loading="btnLoading.createSD"
          type="primary"
          @click="createSD"
        >
          创建采购SD单
        </el-button>
        <el-button
          v-permission="'purchase_exportPurchase'"
          type="primary"
          @click="exportFile"
        >
          导出
        </el-button>
        <el-button type="primary" @click="copy" v-permission="'purchase_copy'">
          复制
        </el-button>
        <el-button
          v-permission="'purchase_to_sale'"
          type="primary"
          @click="toSale"
        >
          转销售订单
        </el-button>
        <el-button
          v-permission="'purchase_to_financing'"
          type="primary"
          @click="createFinancing"
        >
          生成融资代采
        </el-button>
        <el-button
          v-permission="'purchase_batchRejection'"
          type="primary"
          @click="showBatchRejectionModal"
        >
          批量驳回
        </el-button>
        <el-button
          v-permission="'purchase_applyRedDashed'"
          type="primary"
          @click="applyRedDashed"
        >
          申请红冲
        </el-button>
        <el-button
          v-permission="'purchase_auditRedDashed'"
          type="primary"
          @click="auditRedDashed"
        >
          审核红冲
        </el-button>
        <el-button
          v-permission="'purchase_auditForOa'"
          v-loading="btnLoading.auditForOa"
          type="primary"
          @click="auditForOa"
        >
          发起OA审批
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->

    <!-- tab栏 -->
    <div class="mr-t-20 mr-b-10">
      <el-radio-group
        v-model="ruleForm.status"
        :data-list="getSelectList('purchaseOrder_statusList')"
        @change="getList(1)"
      >
        <el-radio-button
          v-for="item in selectOpt.purchaseOrder_statusList"
          :key="item.key"
          :label="item.key"
        >
          {{ item.value }}({{ tabNumMap[item.key] || 0 }})
        </el-radio-button>
        <el-radio-button label="">全部({{ tabTotalNum }})</el-radio-button>
      </el-radio-group>
    </div>
    <!-- tab栏 end -->

    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <!-- 数量/总金额 -->
      <template slot="goodsAmount" slot-scope="{ row }">
        <div>{{ row.totalNum }}</div>
        <div>{{ row.currency }} {{ row.goodsAmount }}</div>
      </template>

      <!-- 金额调整 -->
      <template slot="AmountConfirm" slot-scope="{ row }">
        <div>
          <span style="margin-right: 4px">
            {{ row.amountAdjustmentStatusLabel }}
          </span>
          <el-button
            type="text"
            v-permission="'purchase_changeAmount'"
            v-if="
              row.status !== '001' &&
              row.isAmountAdjustmentAble === '1' &&
              !row.writeOffStatus
            "
            @click="adjustAmount(row, 'edit')"
          >
            修改
          </el-button>
        </div>
        <div>
          <span style="margin-right: 4px">
            {{ row.amountConfirmStatusLabel }}
          </span>
          <el-button
            type="text"
            v-permission="'purchase_confirmAmount'"
            v-if="
              row.status !== '001' &&
              row.isAmountConfirmAble === '1' &&
              !row.writeOffStatus
            "
            @click="adjustAmount(row, 'comfirm')"
          >
            确认
          </el-button>
        </div>
      </template>

      <!-- 付款状态/发票日期 -->
      <template slot="billStatusLabel" slot-scope="{ row }">
        <div>{{ row.billStatusLabel }}</div>
        <div>{{ row.drawInvoiceDate }}</div>
      </template>

      <!-- 创建人/录单日期 -->
      <template slot="createName" slot-scope="{ row }">
        <div>{{ row.createName }}</div>
        <div>{{ row.createDate }}</div>
      </template>

      <!-- 操作 -->
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          style="padding-left: 5px"
          @click="linkTo('/purchase/purchaseOrderedit/edit?id=' + row.id)"
          v-if="row.status === '001'"
          v-permission="'purchase_edit'"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          :style="row.status !== '001' ? 'padding-left: 5px' : ''"
          @click="linkTo('/purchase/purchaseOrderdetail?id=' + row.id)"
          v-permission="'purchase_detail'"
        >
          详情
        </el-button>
        <el-button
          type="text"
          @click="
            linkTo('/purchase/purchaseOrderedit/edit?audit=true&id=' + row.id)
          "
          v-if="row.status === '002' && row.auditMethod === '2'"
          v-permission="'purchase_audit'"
        >
          审核
        </el-button>
        <el-button
          type="text"
          @click="
            linkTo(
              '/purchase/checkContract?id=' +
                row.id +
                '&customerId=' +
                row.supplierId
            )
          "
          v-permission="'purchase_view_contract'"
        >
          查看合同
        </el-button>
        <el-button
          v-if="row.requestId"
          v-permission="'purchase_approval_process'"
          type="text"
          @click="approvalProcess(row.requestId)"
        >
          审批流程
        </el-button>
        <el-button
          type="text"
          v-if="+row.isInvoiceAble === 1"
          v-permission="'purchase_receiveInvoice'"
          @click="linkTo('/purchase/receiveInvoiceEdit?id=' + row.id)"
        >
          收到发票
        </el-button>
        <el-button
          type="text"
          @click="openPayment(row)"
          v-if="['003', '007', '005'].includes(row.status) && !row.payDate"
          v-permission="'purchase_alreadyPay'"
        >
          录入付款
        </el-button>
        <el-button
          v-if="+row.isInDepotAble === 1"
          v-permission="'purchase_in'"
          type="text"
          @click="linkTo('/purchase/inwarehouse?id=' + row.id)"
        >
          上架入库
        </el-button>
        <!-- <el-button
          type="text"
          v-permission="'purchase_toAttachment'"
          @click="linkTo('/common/enclosureManage?code=' + row.code)"
        >
          附件管理
        </el-button> -->
        <el-button
          type="text"
          v-permission="'purchase_log'"
          @click="openLog(row)"
        >
          日志
        </el-button>
        <el-button
          v-if="row.statusLabel === '已审核' && row.isGenerateLabel == '否'"
          v-permission="'purchase_reverse_audit'"
          type="text"
          @click="
            () => {
              auditCounterTrialVisible.show = true
              auditCounterTrialVisible.id = row.id
            }
          "
        >
          反审
        </el-button>
      </template>
    </JFX-table>

    <!-- 录入发票时间-->
    <receive-invoice
      v-if="invoiceVisible.show"
      :visible.sync="invoiceVisible"
      @comfirm="getList"
      :filterData="filterData1"
    />
    <!-- 录入发票时间 end-->

    <!-- 录入付款时间 -->
    <input-pay-time
      v-if="inputPayTimeVisible.show"
      :visible.sync="inputPayTimeVisible"
      :filterData="filterData1"
      @comfirm="getList"
    />
    <!-- 录入付款时间 end -->

    <!-- 采购金额确认/修改 -->
    <purchase-amount
      v-if="purchaseAmount.visible.show"
      :isVisible.sync="purchaseAmount.visible"
      :filterData="purchaseAmount.data"
      @comfirm="getList"
    />
    <!-- 采购金额确认/修改 end-->

    <!--中转入库-->
    <JFX-Dialog
      :visible.sync="transferWarehouse"
      :showClose="true"
      @comfirm="comfirmPurchaseInWarehouse"
      :width="'30vw'"
      :title="'中转入库'"
      :btnAlign="'center'"
      top="30vh"
      v-loading="transferWarehouseLoading"
    >
      <div>
        <div class="change-lve">
          <span class="need">入库时间：</span>
          <el-date-picker
            v-model="inWarehouseTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions"
            placeholder="选择日期"
          ></el-date-picker>
        </div>
        <div class="mr-t-20 fs-14 clr-II">对选中的单据进行中转仓入库确认！</div>
      </div>
    </JFX-Dialog>
    <!--中转入库 end-->

    <!-- 转销售订单 -->
    <ToSalesOrder
      v-if="toSalesOrder.visible.show"
      :isVisible.sync="toSalesOrder.visible"
      :data="toSalesOrder.data"
    />
    <!-- 转销售订单 end -->

    <!-- 日志 -->
    <purchase-order-log
      v-if="logVisible.show"
      :visible.sync="logVisible"
      :filterData="filterData"
    />
    <!-- 日志 end-->

    <!-- 完结入库 -->
    <JFX-Dialog
      :visible.sync="finishedStorage.visible"
      :showClose="true"
      :width="'450px'"
      :title="'完结入库'"
      :btnAlign="'center'"
      top="30vh"
      @comfirm="comfirmFinishedStorage"
    >
      <div class="detail-row">
        <div class="mr-b-15">
          <i class="el-icon-warning" style="color: #e6a23c" />
          <span>{{ this.finishedStorage.desc }}</span>
        </div>
        <div class="mr-b-15">
          <span class="need">完结日期</span>
          <el-date-picker
            v-model="finishedStorage.date"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          />
        </div>
      </div>
    </JFX-Dialog>
    <!-- 完结入库 end -->

    <!-- 反审 start -->
    <auditCounterTrial
      v-if="auditCounterTrialVisible.show"
      :visible.sync="auditCounterTrialVisible"
      @close="
        () => {
          getList()
          auditCounterTrialVisible.id = ''
        }
      "
    />
    <!-- 反审 end -->

    <!-- 批量驳回 -->
    <BatchRejection
      v-if="batchRejection.visible.show"
      :isVisible.sync="batchRejection.visible"
      :data="batchRejection.data"
      @comfirm="getList"
    />
    <!-- 批量驳回 end -->

    <!-- 申请、审核红冲 -->
    <RedDashedModal
      v-if="redDashedModal.visible.show"
      :isVisible.sync="redDashedModal.visible"
      :type="redDashedModal.type"
      :data="redDashedModal.data"
      @comfirm="getList"
    />
    <!-- 申请、审核红冲 end -->
  </div>
</template>
<script>
  import {
    listPurchaseOrder,
    endPurchaseOrderCheck,
    endPurchaseOrder,
    importPurchaseUrl,
    delPurchaseOrder,
    inWarehouse,
    createFinancingOrderCheck,
    exportPurchaseUrl,
    createSdOrderCheck,
    toSaleStepBeforeCheck,
    generateDeclareOrderCheck,
    genSaleOrderByPurchaseIds,
    saveSdOrder,
    validateApply,
    validateAuditWriteOff,
    getPurchaseOrderTypeNum,
    getOAAuditPage
  } from '@a/purchaseManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    components: {
      /* 收到发票 */
      receiveInvoice: () => import('./components/receiveInvoice'),
      /* 录入付款时间 */
      inputPayTime: () => import('./components/inputPayTime'),
      /* 采购金额确认/修改 */
      purchaseAmount: () => import('./components/purchaseAmount'),
      /* 转销售订单 */
      ToSalesOrder: () => import('./components/ToSalesOrder'),
      /* 日志 */
      purchaseOrderLog: () => import('@c/logList/index.vue'),
      /* 反审 */
      auditCounterTrial: () => import('./components/auditCounterTrial'),
      /* 批量驳回 */
      BatchRejection: () => import('./components/BatchRejection.vue'),
      /* 申请、审核红冲 */
      RedDashedModal: () => import('./components/RedDashedModal.vue')
    },
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          codes: '',
          depotId: '',
          supplierId: '',
          buId: '',
          poNos: '',
          status: '',
          warehouseCode: '',
          businessModel: '',
          isEnd: '',
          amountConfirmStatus: '',
          amountAdjustmentStatus: '',
          createStartDate: '',
          createEndDate: '',
          financingNo: '',
          createName: '',
          writeOffStatus: ''
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '采购订单编号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '入库仓库',
            prop: 'depotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '供应商',
            prop: 'supplierName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: 'PO号',
            prop: 'poNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '订单状态',
            prop: 'statusLabel',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '红冲状态',
            prop: 'writeOffStatusLabel',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '数量/总金额',
            slotTo: 'goodsAmount',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '生成预申报单',
            prop: 'isGenerateLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '金额调整',
            slotTo: 'AmountConfirm',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '付款状态/发票日期',
            slotTo: 'billStatusLabel',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '业务模式',
            prop: 'businessModelLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '融资申请号',
            prop: 'financingNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建人/录单日期',
            slotTo: 'createName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '150',
            align: 'left',
            fixed: 'right'
          }
        ],
        invoiceVisible: { show: false },
        inputPayTimeVisible: { show: false },
        transferWarehouse: { show: false },
        salesOrdereVisible: { show: false },
        logVisible: { show: false },
        auditCounterTrialVisible: { show: false, id: '' },
        inWarehouseTime: '',
        filterData: {},
        filterData1: {},
        finishLoading: false,
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now()
          }
        },
        transferWarehouseLoading: false,
        // 完结入库
        finishedStorage: {
          visible: { show: false },
          date: '',
          desc: ''
        },
        // 批量驳回
        batchRejection: {
          data: {},
          visible: { show: false }
        },
        /* 金额调整组件状态 */
        purchaseAmount: {
          visible: { show: false },
          data: {}
        },
        /* 转销售订单组件状态 */
        toSalesOrder: {
          visible: { show: false },
          data: {}
        },
        /* 申请、审核红冲 */
        redDashedModal: {
          visible: { show: false },
          type: '',
          data: {}
        },
        /* 按钮的loading */
        btnLoading: {
          createSD: false,
          auditForOa: false
        },
        /* tab数量map */
        tabNumMap: {},
        /* tab总数量 */
        tabTotalNum: 0,
        /* 录单时间 */
        createDate: [],
        /* 发票时间 */
        invoiceDate: []
      }
    },
    mounted() {
      /* 获取tab数量 */
      this.getTabNum()
      this.getList()
    },
    activated() {
      /* 获取tab数量 */
      this.getTabNum()
      this.getList()
    },
    methods: {
      async getList(pageNum) {
        /* 录单日期 */
        this.ruleForm.createStartDate =
          this.createDate && this.createDate.length > 0
            ? this.createDate[0]
            : ''
        this.ruleForm.createEndDate =
          this.createDate && this.createDate.length > 0
            ? this.createDate[1]
            : ''

        /* 发票日期 */
        this.ruleForm.invoiceStartDate =
          this.invoiceDate && this.invoiceDate.length > 0
            ? this.invoiceDate[0]
            : ''
        this.ruleForm.invoiceEndDate =
          this.invoiceDate && this.invoiceDate.length > 0
            ? this.invoiceDate[1]
            : ''

        try {
          this.tableData.loading = true
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          const { data } = await listPurchaseOrder({
            ...this.ruleForm,
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10
          })
          this.tableData.list = data?.list || []
          this.tableData.total = data?.total || 0
          /* 获取tab数量 */
          this.getTabNum()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      /* 金额调整 */
      adjustAmount(row, type = 'edit') {
        this.purchaseAmount.data = { id: row.id, type }
        this.purchaseAmount.visible.show = true
      },
      /* 删除 */
      deleteTableItem() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.$confirm('确定删除选中对象？', '提示', { type: 'warning' }).then(
          async () => {
            try {
              await delPurchaseOrder({ ids })
              this.$successMsg('删除成功')
              this.getList()
            } catch (error) {
              this.$errorMsg(error.message)
            }
          }
        )
      },
      // 收到发票
      openInvoice(row) {
        this.filterData1 = { id: row.id, code: row.code }
        this.invoiceVisible.show = true
      },
      // 录入收款
      openPayment(row) {
        this.filterData1 = { id: row.id, code: row.code }
        this.inputPayTimeVisible.show = true
      },
      // 完入结库
      async finishIn() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        try {
          this.finishLoading = true
          const res = await endPurchaseOrderCheck({ ids })
          if (res.data.includes('状态必须为已审核')) {
            this.$errorMsg(res.data)
            throw new Error(false)
          }
          const resultData = res.data
          const resultLength = resultData.length
          const resultSub = resultData.substring(resultLength - 5, resultLength)
          if (resultSub.includes('占0.0%')) {
            this.$errorMsg(resultSub)
            throw new Error(false)
          } else {
            this.finishedStorage.date = ''
            this.finishedStorage.desc = res.data
            this.finishedStorage.visible.show = true
          }
        } catch (error) {
          console.log(error)
        }
        this.finishLoading = false
      },
      // 确认完结出库
      async comfirmFinishedStorage() {
        const { date: endDateStr } = this.finishedStorage
        if (!endDateStr) {
          this.$errorMsg('请先选择完结日期！')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        try {
          this.finishLoading = true
          await endPurchaseOrder({ ids, endDateStr })
          this.finishedStorage.visible.show = false
          this.$successMsg('完结入库成功')
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.finishLoading = false
        }
      },
      // 导入
      importFile() {
        this.linkTo(
          '/common/importfile?templateId=' + 107 + '&url=' + importPurchaseUrl
        )
      },
      // 预申报单
      createShen() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.$confirm('确定生成选中对象？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          await generateDeclareOrderCheck({ ids })
          this.$successMsg('生成预申报成功')
          this.linkTo('/purchase/predeclarationEdit?purchaseIds=' + ids)
        })
      },
      // 中转仓入库
      purchaseInWarehouse() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        this.inWarehouseTime = ''
        this.transferWarehouse.show = true
      },
      // 确认中转仓入库
      async comfirmPurchaseInWarehouse() {
        if (!this.inWarehouseTime) {
          this.$errorMsg('至少选择入库时间')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.transferWarehouseLoading = true
        try {
          await inWarehouse({ ids, inWarehouseDateStr: this.inWarehouseTime })
          this.$successMsg('操作成功')
          this.transferWarehouse.show = false
          this.getList()
        } catch (error) {
          console.log(error)
        }
        this.transferWarehouseLoading = false
      },
      /* 导出 */
      exportFile() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        if (this.tableChoseList.length > 0) {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          this.$exportFile(exportPurchaseUrl, { ids })
          return false
        }
        this.$exportFile(exportPurchaseUrl, { ...this.ruleForm })
      },
      /* 重置搜索条件 */
      resetForm() {
        this.reset('ruleForm', () => {
          this.createDate = []
          this.invoiceDate = []
          this.getList(1)
        })
      },
      /* 转销售单 */
      async toSale() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('请至少选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        const poNo = this.tableChoseList.map((item) => item.poNo).join('&')
        const buId = this.tableChoseList[0].buId
        try {
          await genSaleOrderByPurchaseIds({ ids })
          this.toSalesOrder.visible.show = true
          this.toSalesOrder.data = { ids, poNo, buId }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 复制
      copy() {
        if (this.tableChoseList.length !== 1) {
          this.$errorMsg('请选择一条记录！')
          return false
        }
        const id = this.tableChoseList[0].id
        this.linkTo('/purchase/purchaseOrderedit/add?copyId=' + id)
      },
      openLog(row) {
        this.filterData = { relCode: row.code }
        this.logVisible.show = true
      },
      // 显示批量驳回弹窗
      showBatchRejectionModal() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        /* 存在状态不为已提交的单据 */
        const statusCondition = this.tableChoseList.find(
          (item) => item.status !== '002'
        )
        if (statusCondition) {
          this.$alertWarning(`采购订单:"${statusCondition.code}"状态不为已提交`)
          return false
        }
        /* 存在审批方式为OA的单据 */
        const auditMethodCondition = this.tableChoseList.find(
          (item) => item.auditMethod === '1'
        )
        if (auditMethodCondition) {
          this.$alertWarning(
            `所选的采购订单${auditMethodCondition.code}的审批方式为OA，不能批量驳回`
          )
          return false
        }
        this.batchRejection.data = {
          ids: this.tableChoseList.map((item) => item.id).toString(),
          codes: this.tableChoseList.map((item) => item.code).toString()
        }
        this.batchRejection.visible.show = true
      },
      /* 创建交易链路 */
      async createTransactionLink() {
        if (this.tableChoseList.length !== 1) {
          this.$errorMsg('请选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        const id = this.tableChoseList[0].id
        try {
          await toSaleStepBeforeCheck({ id })
          this.linkTo('/purchase/procurementLink?id=' + id)
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 生成融资代采 */
      async createFinancing() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        try {
          const {
            data: { financingNos }
          } = await createFinancingOrderCheck({ ids })
          /* 已有推送融资信息记录 */
          if (financingNos) {
            this.$confirm(
              '该采购订单已有推送融资信息记录，是否确认重新触发推送新的代采订单信息?',
              '提示',
              { type: 'warning' }
            ).then(() => {
              this.linkTo('/purchase/addFinanceOrder?ids=' + ids)
            })
            return false
          }
          this.linkTo('/purchase/addFinanceOrder?ids=' + ids)
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 创建采购SD单 */
      async createSD() {
        if (this.tableChoseList.length !== 1) {
          this.$errorMsg('请选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        const { id } = this.tableChoseList[0]
        try {
          this.btnLoading.createSD = true
          await createSdOrderCheck({ id })
          await saveSdOrder({ id })
          this.$successMsg('生成成功')
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.btnLoading.createSD = false
        }
      },
      /* 申请红冲 */
      async applyRedDashed() {
        const count = this.tableChoseList.length
        if (!count) {
          this.$errorMsg('至少选择一条数据！')
          return false
        }
        if (count > 1) {
          this.$errorMsg('仅能选择一条数据！')
          return false
        }
        const { id } = this.tableChoseList[0]
        try {
          const { data: hasRelRuturnOrder } = await validateApply({ id })
          const showModal = () => {
            this.redDashedModal.visible.show = true
            this.redDashedModal.type = 'apply'
            this.redDashedModal.data = { id }
          }
          /* 该采购单有关联的采购退订单 */
          if (hasRelRuturnOrder) {
            this.$confirm(
              '采购订单存在关联采购退订单，是否继续红冲？',
              '操作确认',
              { type: 'warning' }
            ).then(() => {
              showModal()
            })
            return false
          }
          showModal()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 审核红冲 */
      async auditRedDashed() {
        const count = this.tableChoseList.length
        if (!count) {
          this.$errorMsg('至少选择一条数据！')
          return false
        }
        if (count > 1) {
          this.$errorMsg('仅能选择一条数据！')
          return false
        }
        const { id } = this.tableChoseList[0]
        try {
          const { data } = await validateAuditWriteOff({ id })
          this.redDashedModal.visible.show = true
          this.redDashedModal.type = 'audit'
          this.redDashedModal.data = { ...data, id }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 发起OA审批 */
      async auditForOa() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        /* 红冲中不能生成销售预申报单 */
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
          return
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        const codes = this.tableChoseList.map((item) => item.code).toString()
        try {
          this.btnLoading.auditForOa = true
          await getOAAuditPage({ ids })
          this.linkTo(`/purchase/AuditForOa?ids=${ids}&codes=${codes}`)
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.btnLoading.auditForOa = false
        }
      },
      /* 获取tab数量 */
      async getTabNum() {
        const { data } = await getPurchaseOrderTypeNum({
          ...this.ruleForm,
          status: ''
        })
        if (!data?.length) return false
        this.tabNumMap = data.reduce((helper, dataItem) => {
          const { status, num, total } = dataItem
          if (status) helper[status] = num
          this.tabTotalNum = total || 0
          return helper
        }, {})
      },
      /* 查看审批流程 */
      approvalProcess(requestId) {
        /* oa的地址 */
        const oaUrl =
          process.env.VUE_APP_ENV === 'production'
            ? 'https://oa.topideal.com'
            : 'https://oatest.topideal.com'
        const url = `${oaUrl}/spa/workflow/static4form/index.html?#/main/workflow/req?requestid=${requestId}`
        window.open(url, '_blank')
      }
    }
  }
</script>

<style lang="scss" scoped>
  .change-lve {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
  }
  .action-bar {
    .el-button {
      margin: 4px 10px 0 0;
    }
  }
</style>
