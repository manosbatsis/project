<template>
  <!-- 销售订单列表页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="销售订单编号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                type="textarea"
                :rows="1"
                v-model.trim="searchProps.code"
                placeholder="多个订单用英文逗号隔开"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO单号："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                type="textarea"
                :rows="1"
                v-model.trim="searchProps.poNo"
                placeholder="多个PO用英文逗号隔开"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                :data-list="getCustomerSelectBean('customer_list')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.customer_list"
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
                v-model.trim="searchProps.createName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="出仓仓库："
              prop="outDepotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.outDepotId"
                placeholder="请选择"
                :data-list="getSelectBeanByMerchantRel('warehouseList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.warehouseList"
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
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.businessModel"
                placeholder="请选择"
                :data-list="getSelectList('saleOrder_businessModelList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.saleOrder_businessModelList"
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
                v-model="searchProps.buId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getBUSelectBean('businessList')"
              >
                <el-option
                  v-for="item in selectOpt.businessList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="单据标识："
              prop="orderType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.orderType"
                placeholder="请选择"
                :data-list="getSelectList('saleOrder_orderTypeList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.saleOrder_orderTypeList"
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
              prop="amountStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.amountStatus"
                placeholder="请选择"
                :data-list="getSelectList('saleOrder_amountStatusList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.saleOrder_amountStatusList"
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
                v-model="searchProps.amountConfirmStatus"
                placeholder="请选择"
                :data-list="getSelectList('saleOrder_amountConfirmStatusList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.saleOrder_amountConfirmStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item prop="writeOffStatus" label="红冲状态：">
              <el-select
                v-model="searchProps.writeOffStatus"
                placeholder="请选择"
                :data-list="getSelectList('saleOrder_writeOffStatusList')"
              >
                <el-option
                  v-for="item in selectOpt.saleOrder_writeOffStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item prop="date" label="订单日期：">
              <el-date-picker
                v-model="date1"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item prop="date2" label="上架日期：">
              <el-date-picker
                v-model="date2"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'sale_add'"
          @click="linkTo('/sales/salesorderadd')"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_import'"
          @click="importFile('1')"
        >
          导入
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_saleOrderStockOut'"
          :loading="transferWarehouse.loading"
          @click="showTransferWarehouse"
        >
          中转仓出库
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_delSaleOrder'"
          @click="deleteTableItem"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_createPreDeclaration'"
          @click="createDeclaration"
        >
          生成销售预申报单
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_exportSaleOrder'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_endStockOut'"
          @click="finishedDelivery"
        >
          完结出库
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_createPurchase'"
          @click="generatePurchaseOrder"
        >
          生成采购订单
        </el-button>
        <el-button
          type="primary"
          v-permission="'ownSaleOrder_add'"
          @click="addOwnSaleOrder"
        >
          新增内部销售订单
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_copy'"
          @click="copyTableItem"
        >
          复制
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_importSaleShelf'"
          @click="importFile('2')"
        >
          上架导入
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_batchRejection'"
          @click="showBatchRejectionModal"
        >
          批量驳回
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_applyRedPunch'"
          @click="showModal('applyRedPunch')"
          >申请红冲</el-button
        >
        <el-button
          type="primary"
          v-permission="'sale_auditRedPunch'"
          @click="showModal('auditRedPunch')"
          >审核红冲</el-button
        >
      </el-col>
    </el-row>
    <!-- 操作 end -->

    <!-- 订单状态 -->
    <div class="mr-t-20 mr-b-10">
      <el-radio-group
        v-model="searchProps.stateList"
        :data-list="getSelectList('saleOrder_stateList')"
        @change="getList(1)"
      >
        <template v-for="item in selectOpt.saleOrder_stateList">
          <!-- 【待出库】、【出库中】、【已删除】合并到全部 -->
          <el-radio-button
            v-if="!['017', '027', '006'].includes(item.key)"
            :key="item.key"
            :label="item.key"
          >
            {{ item.value }}({{ tabData[item.key] || 0 }})
          </el-radio-button>
        </template>
        <el-radio-button label="">全部({{ this.tabTotal }})</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <!-- 公司/事业部 -->
      <template slot="merchantAndBu" slot-scope="{ row }">
        {{ row.merchantName || '' }} <br />
        {{ row.buName || '' }}
      </template>

      <!-- PO单号 -->
      <template slot="poNo" slot-scope="{ row }">
        <template>
          {{ row.poNo }}
        </template>
        <div>
          <el-button
            v-if="['001', '003', '018', '019'].includes(row.state)"
            v-permission="'sale_changePo'"
            type="text"
            @click="showChangePoNoModal(row)"
          >
            修改
          </el-button>
        </div>
      </template>

      <!-- 数量/总金额 -->
      <template slot="totalNum" slot-scope="{ row }">
        {{ row.totalNum || 0 }} <br />
        {{ row.currency }} {{ row.totalAmount || 0 }}
      </template>

      <!-- 上架量/金额 -->
      <template slot="shelfNum" slot-scope="{ row }">
        {{ row.shelfNum || 0 }} <br />
        {{ row.currency }} {{ row.shelfAmount || 0 }}
      </template>

      <!-- 金额调整状态 -->
      <template slot="amountStatusLabel" slot-scope="{ row }">
        <div>
          <span>{{ row.amountStatusLabel }}</span>
          <span
            type="text"
            class="amount-text"
            v-permission="'sale_amount_adjust'"
            v-if="isShowChangeAmountBtn(row)"
            @click="showAmountAdjustMoadl(row, '1')"
          >
            修改
          </span>
        </div>
        <div>
          <span>{{ row.amountConfirmStatusLabel }}</span>
          <span
            type="text"
            class="amount-text"
            v-permission="'sale_amount_confirm'"
            v-if="
              row.state !== '001' &&
              row.state !== '008' &&
              row.hasFinanceClose === '0'
            "
            @click="showAmountAdjustMoadl(row, '2')"
          >
            确认
          </span>
        </div>
      </template>

      <!-- 创建人/订单日期 -->
      <template slot="createDate" slot-scope="{ row }">
        {{ row.createName }} <br />
        {{ row.createDate }}
      </template>

      <!-- 操作按钮 -->
      <template slot="action" slot-scope="{ row }">
        <!-- 订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库,019部分出库, 027:出库中,025：已签收 026：已上架 031-部分上架 -->
        <el-button
          type="text"
          v-permission="'sale_detail'"
          style="padding-left: 5px"
          @click="linkTo(`/sales/salesorderdetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-if="row.state === '008' && !row.writeOffStatus"
          v-permission="'sale_edit'"
          @click="linkTo(`/sales/salesorderedit?id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-if="row.state === '001' && !row.writeOffStatus"
          v-permission="'sale_examine'"
          @click="linkTo(`/sales/salesorderedit?id=${row.id}&examine=1`)"
        >
          审核
        </el-button>
        <el-button
          type="text"
          v-if="row.isShelf && !row.writeOffStatus"
          v-permission="'sale_shelfIsEnd'"
          @click="jumpPutOnSale(row.id, row.code)"
        >
          上架
        </el-button>
        <el-button
          type="text"
          v-if="
            (row.state === '003' || row.state === '019') &&
            row.outDepotIsInOutInstruction === '0' &&
            row.isGenerateDeclare === '0' &&
            !row.writeOffStatus
          "
          v-permission="'sale_out'"
          @click="saleOrderOut(row.id)"
        >
          打托出库
        </el-button>
        <el-button
          type="text"
          v-permission="'sale_toAttachment'"
          @click="linkTo(`/common/enclosuremanage?code=${row.code}`)"
        >
          附件
        </el-button>
        <el-button
          type="text"
          v-if="
            !row.saleCreditCode &&
            row.state === '026' &&
            row.businessModel === '3' &&
            !row.writeOffStatus
          "
          v-permission="'sale_createCredit'"
          @click="showFinancingApplication(row)"
        >
          赊销申请
        </el-button>
        <el-button
          type="text"
          v-permission="'sale_log'"
          @click="showModal('logList', row.code)"
        >
          日志
        </el-button>
        <el-button
          type="text"
          v-if="
            row.state === '003' &&
            row.isGenerateDeclare !== '1' &&
            !row.writeOffStatus
          "
          v-permission="'sale_reverse_audit'"
          @click="showModal('reverseAudit', row)"
        >
          反审
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->

    <!-- 中转仓出库组件 -->
    <TransferWarehouseOut
      v-if="transferWarehouse.visible.show"
      :ids="transferWarehouse.ids"
      :transferWarehouseVisible="transferWarehouse.visible"
      @comfirm="closeModal('transfer')"
    />
    <!-- 中转仓出库组件 end -->

    <!-- 签收组件 -->
    <SignForOrder
      v-if="signForOrder.visible.show"
      :id="signForOrder.id"
      :poNo="signForOrder.poNo"
      :businessModel="signForOrder.businessModel"
      :signForOrderVisible="signForOrder.visible"
      @comfirm="closeModal('receive')"
    />
    <!-- 签收组件 end -->

    <!-- 生成采购订单组件 -->
    <GeneratePurchaseOrder
      v-if="purchaseOrder.visible.show"
      :id="purchaseOrder.id"
      :data="purchaseOrder.data"
      :type="purchaseOrder.type"
      :purchaseOrderVisible="purchaseOrder.visible"
      @comfirm="closeModal('purchase')"
    />
    <!-- 生成采购订单组件 end -->

    <!-- 新增内部订单组件  -->
    <AddInternalOrder
      v-if="internalOrder.visible.show"
      :data="internalOrder.data"
      :internalOrderVisible="internalOrder.visible"
      @comfirm="closeModal('internalOrder')"
    />
    <!-- 新增内部订单组件  end -->

    <!-- 确认金额组件 -->
    <AmountModal
      v-if="amountModalVisible.show"
      :rowId="rowId"
      :amountType="amountType"
      :amountModalVisible="amountModalVisible"
      @cacel="amountModalVisible.show = false"
      @comfirm="closeModal('amount')"
    />
    <!-- 确认金额组件 end -->

    <!-- 清关资料导出 -->
    <ExportConfirm
      v-if="visible.show"
      :visible="visible"
      :targetData="targetData"
      @comfirm="exportConfirm"
    />
    <!-- 清关资料导出 end-->

    <!-- 赊销申请 -->
    <CreditApplication
      v-if="financingApplication.visible.show"
      :financingApplicationVisible="financingApplication.visible"
      :data="financingApplication.data"
      @comfirm="(isCancel) => closeModal('financing', isCancel)"
    />
    <!-- 赊销申请 end -->

    <!-- 选择上架单 -->
    <ChoosePutOnShelve
      v-if="choosePutOnShelve.visible.show"
      :id="choosePutOnShelve.id"
      :data="choosePutOnShelve.data"
      :ChoosePutOnShelveVisible="choosePutOnShelve.visible"
      @comfirm="closeModal('choosePutOnShelve')"
    />
    <!-- 选择上架单 end -->

    <!-- 日志 -->
    <LogList
      v-if="logList.visible.show"
      :visible.sync="logList.visible"
      :filterData="logList.filterData"
    />
    <!-- 日志 end-->

    <!-- 下载 -->
    <div class="hide-form" v-if="downVal">
      <form id="down-up" :action="actionUrl" method="post" target="_blank">
        <input type="hidden" name="json" v-model="downVal" />
      </form>
    </div>
    <!-- 下载 end -->

    <!-- 修改po -->
    <ChangePo
      v-if="changePo.visible.show"
      :isVisible.sync="changePo.visible"
      :data="changePo.data"
      @comfirm="getList"
    />
    <!-- 修改po end -->

    <!-- 反审 start -->
    <ReverseAudit
      v-if="reverseAudit.visible.show"
      :visible.sync="reverseAudit.visible"
      :data="reverseAudit.data"
      @close="closeModal('reverseAudit')"
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
    <!-- 申请红冲 start -->
    <apply-red-punch
      v-if="applyRedPunch.visible.show"
      :applyRedPunchVisible.sync="applyRedPunch.visible"
      :data="applyRedPunch.data"
      @comfirm="getList"
    />
    <!-- 申请红冲 end -->
    <!-- 审核红冲 start -->
    <audit-red-punch
      v-if="auditRedPunch.visible.show"
      :auditRedPunchVisible.sync="auditRedPunch.visible"
      :data="auditRedPunch.data"
      @comfirm="getList"
    />
    <!-- 审核红冲 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getExportTemplate } from '@a/base'
  import { getBaseUrl } from '@u/tool'
  import {
    listSaleOrder,
    delSaleOrder,
    exportSaleOrderUrl,
    getSaleOrderCount,
    importSaleUrl,
    importSaleShelfUrl,
    differenceRatio,
    endStockOut,
    checkExistFinanceClose,
    checkOrderState,
    showOwnPurchaseOrder,
    exportSaleCustomsInfo,
    shelfIsEnd,
    getSaleOutDepotList,
    checkCreateDeclare,
    getStockOutItemList,
    validateApplyWriteOff,
    getStatusNum
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      /* 中转仓出库 */
      TransferWarehouseOut: () => import('./components/TransferWarehouseOut'),
      /* 生成采购订单 */
      GeneratePurchaseOrder: () => import('./components/GeneratePurchaseOrder'),
      /* 生成内部订单 */
      AddInternalOrder: () => import('./components/AddInternalOrder'),
      /* 金额调整 */
      AmountModal: () => import('./components/AmountModal'),
      /* 签收 */
      SignForOrder: () => import('./components/SignForOrder'),
      /* 清关资料导出 */
      ExportConfirm: () => import('@c/exportConfirm'),
      /* 赊销申请 */
      CreditApplication: () => import('./components/CreditApplication'),
      /* 选择上架单 */
      ChoosePutOnShelve: () => import('./components/ChoosePutOnShelve'),
      /* 日志 */
      LogList: () => import('@c/logList/index.vue'),
      /* 修改PO */
      ChangePo: () => import('./components/ChangePoNo.vue'),
      /* 反审 */
      ReverseAudit: () => import('./components/SalesOrderReverseAudit.vue'),
      /* 批量驳回 */
      BatchRejection: () => import('./components/BatchRejection.vue'),
      /** 申请红冲 */
      applyRedPunch: () => import('./components/applyRedPunch.vue'),
      /** 审核红冲 */
      auditRedPunch: () => import('./components/auditRedPunch.vue')
    },
    data() {
      return {
        /* 查询数据 */
        searchProps: {
          code: '',
          outDepotId: '',
          customerId: '',
          poNo: '',
          stateList: '',
          businessModel: '',
          buId: '',
          orderType: '',
          amountStatus: '',
          amountConfirmStatus: '',
          createName: '',
          writeOffStatus: ''
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '销售订单编号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '出仓仓库',
            prop: 'outDepotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '公司/事业部',
            slotTo: 'merchantAndBu',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'PO单号',
            slotTo: 'poNo',
            width: '150',
            align: 'center'
          },
          {
            label: '客户',
            prop: 'customerName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '数量/总金额',
            slotTo: 'totalNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '上架量/金额',
            slotTo: 'shelfNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '金额调整状态',
            slotTo: 'amountStatusLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '生成预申报单',
            prop: 'isGenerateDeclareLabel',
            minWidth: '100',
            align: 'center',
            hide: true,
            formatter(row) {
              return row.isGenerateDeclare === '1' ? '是' : '否'
            }
          },
          {
            label: '创建人/订单日期',
            slotTo: 'createDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '订单状态',
            prop: 'stateLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '销售类型',
            prop: 'businessModelLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '红冲状态',
            prop: 'writeOffStatusLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '赊销单号',
            prop: 'saleCreditCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '130',
            align: 'left',
            fixed: 'right'
          }
        ],
        /* 订单状态 */
        stateList: [],
        /** 订单tabData数据 */
        tabData: {},
        tabTotal: 0,
        /* 订单日期 */
        date1: [],
        /* 上架日期 */
        date2: [],
        // 调整、确认金额状态
        amountType: '1',
        // 表格行id
        rowId: '',
        // 确认金额组件显示状态
        amountModalVisible: { show: false },
        // 中转仓出库组件状态
        transferWarehouse: {
          ids: '',
          visible: { show: false },
          loading: false
        },
        // 签收组件状态
        signForOrder: {
          id: '',
          poNo: '',
          businessModel: '',
          visible: { show: false }
        },
        // 生成采购订单组件状态
        purchaseOrder: {
          data: {},
          type: '',
          visible: { show: false }
        },
        // 新增内部订单组件状态
        internalOrder: {
          data: [],
          visible: { show: false }
        },
        // 融资申请组件状态
        financingApplication: {
          data: {},
          visible: { show: false }
        },
        // 融资赎回组件状态
        financingRedemption: {
          id: '',
          visible: { show: false }
        },
        // 选择上架单组件状态
        choosePutOnShelve: {
          id: '',
          data: [],
          visible: { show: false }
        },
        // 日志组件
        logList: {
          filterData: {},
          visible: { show: false }
        },
        // 修改po单号
        changePo: {
          data: {},
          visible: { show: false }
        },
        // 反审
        reverseAudit: {
          data: {},
          visible: { show: false }
        },
        // 批量驳回
        batchRejection: {
          data: {},
          visible: { show: false }
        },
        // 申请红冲
        applyRedPunch: {
          data: {},
          visible: { show: false }
        },
        // 审核红冲
        auditRedPunch: {
          data: {},
          visible: { show: false }
        },
        // 导出清关资料按钮loading
        qingGuanLoading: false,
        actionUrl: '',
        downVal: ''
      }
    },
    computed: {
      // 是否显示修改金额按钮
      isShowChangeAmountBtn() {
        return function (row) {
          const {
            state,
            amountConfirmStatus,
            hasFinanceClose,
            writeOffStatus
          } = row
          // 018已出库，031部分上架，026已上架 之外不显示
          if (!['018', '031', '026'].includes(state)) {
            return false
          }
          // 金额确认状态 0-未确认，1-确认通过，2-确认不通过
          // 确认通过 不显示
          if (amountConfirmStatus === '1') {
            return false
          }
          // 是否关账 0没关帐 1关帐
          // 已关账 不显示
          if (hasFinanceClose === '1') {
            return false
          }
          // 001待红冲，002已红冲 003红冲中 不显示
          if (['001', '002', '003'].includes(writeOffStatus)) {
            return false
          }
          return true
        }
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.getList()
    },
    methods: {
      /* 获取表格数据 */
      async getList(pageNum) {
        /* 订单日期 */
        this.searchProps.createDateStartDate =
          this.date1 && this.date1.length ? this.date1[0] : ''
        this.searchProps.createDateEndDate =
          this.date1 && this.date1.length ? this.date1[1] : ''
        /* 上架日期 */
        this.searchProps.shelfDateStartDate =
          this.date2 && this.date2.length ? this.date2[0] : ''
        this.searchProps.shelfDateEndDate =
          this.date2 && this.date2.length ? this.date2[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listSaleOrder({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
          // 获取标签页订单状态数量
          this.getTabDataNum()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      /** 获取标签页订单状态数量 */
      async getTabDataNum() {
        const { data } = await getStatusNum({
          ...this.searchProps,
          stateList: ''
        })
        if (!data?.length) return
        this.tabData = data.reduce((helper, dataItem) => {
          const { state, num, total } = dataItem
          if (state) helper[state] = num
          this.tabTotal = total || 0
          return helper
        }, {})
      },
      // 显示金额调整、确认弹窗
      async showAmountAdjustMoadl(row, type) {
        const { id, writeOffStatusLabel } = row
        if (['待红冲', '已红冲'].includes(writeOffStatusLabel)) {
          return this.$errorMsg(
            '红冲状态为【待红冲】、【已红冲】的销售订单不能修改'
          )
        }
        try {
          const {
            data: { code }
          } = await checkExistFinanceClose({ id })
          if (code === '00') {
            this.rowId = id + ''
            this.amountType = type
            this.amountModalVisible.show = true
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 删除表格项
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await delSaleOrder({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 生成销售预申报单
      async createDeclaration() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        // 红冲中不能生成销售预申报单
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          return this.$errorMsg(
            `生成失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        try {
          const { data } = await checkCreateDeclare({ ids })
          if (data) {
            this.linkTo(`/sales/PredeclarationAdd?saleIds=${ids}`)
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 导入
      importFile(type) {
        switch (type) {
          // 导入购销代销销售订单
          case '1':
            this.linkTo(
              '/common/importfile?templateId=' + 114 + '&url=' + importSaleUrl
            )
            break
          // 上架导入
          case '2':
            this.linkTo(
              '/common/importfile?templateId=' +
                142 +
                '&url=' +
                importSaleShelfUrl
            )
            break
        }
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          return this.$errorMsg('暂无数据可导出')
        }
        try {
          if (this.tableChoseList.length) {
            this.$showToast('确定导出勾选数据？', async () => {
              const ids = this.tableChoseList.map((item) => item.id).toString()
              this.$exportFile(exportSaleOrderUrl, { ids })
            })
          } else {
            const { data } = await getSaleOrderCount({ ...this.searchProps })
            if (data > 10000) {
              return this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
            }
            this.$exportFile(exportSaleOrderUrl, { ...this.searchProps })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 完结出库
      async finishedDelivery() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        if (this.tableChoseList.length > 1) {
          return this.$errorMsg('只能选择一条记录')
        }
        // 红冲中不能完结出库
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          return this.$errorMsg(
            `操作失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
        }
        const id = this.tableChoseList[0].id
        const {
          data: { orderCode, percent }
        } = await differenceRatio({ id })
        this.$showToast(
          `销售订单【${orderCode}】出库数量已达到${percent}，是否完结出库?`,
          async () => {
            try {
              await endStockOut({ ids: id })
              this.$successMsg('完结出库成功')
            } catch (error) {
              this.$errorMsg(error.message)
            }
          }
        )
      },
      // 显示中转仓出库弹窗
      async showTransferWarehouse() {
        let target = null
        target = this.tableChoseList.find(
          (item) => item.state !== '003' && item.state !== '019'
        )
        if (target) {
          this.$errorMsg(
            `出库失败，订单：${target.code}的单据状态不为：已审核或部分出库`
          )
          return false
        }
        target = this.tableChoseList.find((item) => item.outDepotType !== 'd')
        if (target) {
          this.$errorMsg(`出库失败，订单：${target.code}的仓库不是中转仓`)
          return false
        }
        target = this.tableChoseList.find(
          (item) => item.isGenerateDeclare === '1'
        )
        if (target) {
          this.$errorMsg(`出库失败，订单：${target.code}的已经生成预申报单`)
          return false
        }
        target = this.tableChoseList.find((item) => item.writeOffStatus)
        if (target) {
          this.$errorMsg(
            `出库失败，订单：${target.code}${target.writeOffStatusLabel}`
          )
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        try {
          // 中转仓出库组件状态 按钮loading
          this.transferWarehouse.loading = true
          const { data: goodsList } = await getStockOutItemList({ ids })
          const { isInventoryValidate } = await this.$checkInventoryNum({
            itemList: goodsList.map((item) => {
              return {
                goodsId: item.goodsId,
                goodsNo: item.goodsNo,
                okNum: item.num,
                depotId: this.tableChoseList[0].outDepotId || '',
                inventoryType: 1
              }
            })
          })
          if (!isInventoryValidate) return
          this.transferWarehouse.visible.show = true
          this.transferWarehouse.ids = ids
        } catch (err) {
          console.log(err)
        } finally {
          // 中转仓出库组件状态 按钮loading
          this.transferWarehouse.loading = false
        }
      },
      // 导出清关资料
      async exportCustomsInfo() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条数据！')
          return false
        }
        if (this.tableChoseList.length > 1) {
          this.$errorMsg('目前只支持单条数据导出！')
          return false
        }
        const {
          outDepotId = '',
          outCustomsId = '',
          inCustomsId = '',
          inDepotId = '',
          isSameArea = ''
        } = this.tableChoseList[0]
        const json = {
          outDepotId: outDepotId || '',
          inDepotId: inDepotId || '',
          outCustomsId: outCustomsId || '',
          inCustomsId: inCustomsId || '',
          isSameArea: isSameArea || ''
        }
        const exportParams = {}
        try {
          this.qingGuanLoading = true
          const res = await getExportTemplate({ json: JSON.stringify(json) })
          if (res.data.code === '00') {
            // 直接导出
            if (res.data.inList && res.data.inList.length > 0) {
              exportParams.inFileTempIds = res.data.inList[0].fileTempId + ''
            }
            if (res.data.outList && res.data.outList.length > 0) {
              exportParams.outFileTempIds = res.data.outList[0].fileTempId + ''
            }
            this.handleFormExportFile(exportParams)
          } else if (res.data.code === '01') {
            // 没有找到关联模板
            this.$alertWarning('该仓库暂无配置清关资料模板，请先完成模板配置！')
          } else {
            // 弹出选择模板
            this.targetData = res.data
            this.visible.show = true
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
        this.qingGuanLoading = false
      },
      // 选择模板后导出清关资料
      exportConfirm(data) {
        this.visible.show = false
        const inFileTempIds = data.inFileTemp
          .map((item) => item.fileTempId)
          .toString()
        const outFileTempIds = data.outFileTemp
          .map((item) => item.fileTempId)
          .toString()
        this.handleFormExportFile({ inFileTempIds, outFileTempIds })
      },
      // 下载清关资料
      async handleFormExportFile({ inFileTempIds, outFileTempIds }) {
        const id = this.tableChoseList[0].id + ''
        try {
          const json = JSON.stringify({ inFileTempIds, outFileTempIds, id })
          this.actionUrl =
            getBaseUrl(exportSaleCustomsInfo) +
            exportSaleCustomsInfo +
            `?token=${sessionStorage.getItem('token')}`
          this.downVal = json
          // 下载
          this.$nextTick(() => {
            const form = document.getElementById('down-up')
            form.submit()
            this.downVal = ''
          })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 复制表格项
      copyTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
        } else if (this.tableChoseList.length > 1) {
          this.$alertWarning('仅能选择一条数据！')
        } else {
          const id = this.tableChoseList[0].id
          this.linkTo(`/sales/salesorderadd?copyid=${id}`)
        }
      },
      // 生成采购订单
      async generatePurchaseOrder() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        if (this.tableChoseList.length > 1) {
          return this.$alertWarning('仅能选择一条数据！')
        }
        const { state, id } = this.tableChoseList[0]
        if (!['001', '002', '003'].includes(state)) {
          return this.$alertWarning('只有待审核、已审核状态下才能生成采购单')
        }
        try {
          const { data } = await checkOrderState({ id })
          this.purchaseOrder.data = data || {}
          this.purchaseOrder.id = id.toString()
          this.purchaseOrder.type = '1'
          this.purchaseOrder.visible.show = true
        } catch (error) {
          return this.$errorMsg(error.message)
        }
      },
      // 新增内部订单
      async addOwnSaleOrder() {
        try {
          const { data } = await showOwnPurchaseOrder()
          this.internalOrder.data = data || []
          this.internalOrder.visible.show = true
        } catch (error) {
          return this.$errorMsg(error.message)
        }
      },
      // 跳转到上架页面
      async jumpPutOnSale(id) {
        try {
          const { data } = await shelfIsEnd({ id })
          if (data) {
            this.$errorMsg('待上架数量为0，无需进行上架操作，请查看详情。')
            return false
          }
          const { data: list } = await getSaleOutDepotList({ saleOrderId: id })
          if (list.length === 1) {
            this.linkTo(
              `/sales/goodsonsale?id=${id}&saleOutCode=${list[0].code}&saleOutId=${list[0].id}`
            )
            return false
          }
          this.choosePutOnShelve.visible.show = true
          this.choosePutOnShelve.data = list
          this.choosePutOnShelve.id = id
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 跳转到打托出库页面
      async saleOrderOut(id) {
        try {
          this.linkTo(`/sales/salesorderissue?id=${id}`)
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      showFinancingApplication(payload) {
        this.financingApplication.visible.show = true
        this.financingApplication.data = payload || {}
      },
      // 显示弹窗
      async showModal(type, data) {
        const tableChoseList = this.tableChoseList
        const choseLength = tableChoseList.length
        const firstData = tableChoseList[0]
        switch (type) {
          case 'logList':
            this.logList.filterData = { relCode: data, module: 5 }
            this.logList.visible.show = true
            break
          // 反审
          case 'reverseAudit':
            this.reverseAudit.visible.show = true
            this.reverseAudit.data = data
            break
          // 申请红冲
          case 'applyRedPunch': {
            if (choseLength !== 1) {
              return this.$errorMsg(
                choseLength ? '只能选择一条记录' : '请选择一条记录'
              )
            }
            // 校验是否可以红冲
            const { data } = await validateApplyWriteOff({ id: firstData.id })
            if (data) {
              return this.$showToast(
                '此销售订单存在关联的销售退货订单，确定红冲吗？',
                () => {
                  this.applyRedPunch.visible.show = true
                  this.applyRedPunch.data = firstData
                }
              )
            }
            this.applyRedPunch.visible.show = true
            this.applyRedPunch.data = firstData
            break
          }
          // 审核红冲
          case 'auditRedPunch':
            if (choseLength !== 1) {
              return this.$errorMsg(
                choseLength ? '只能选择一条记录' : '请选择一条记录'
              )
            }
            if (firstData.writeOffStatus === '001') {
              this.auditRedPunch.visible.show = true
              this.auditRedPunch.data = firstData
            } else {
              this.$errorMsg('销售订单红冲状态不是【待红冲】！')
            }
        }
      },
      // 关闭弹窗
      closeModal(type, isCancel) {
        switch (type) {
          // 中转仓出库
          case 'transfer':
            this.transferWarehouse.visible.show = false
            this.transferWarehouse.ids = ''
            break
          // 签收
          case 'receive':
            this.signForOrder.visible.show = false
            this.signForOrder.id = ''
            this.signForOrder.poNo = ''
            this.signForOrder.businessModel = ''
            break
          // 生成采购订单
          case 'purchase':
            this.purchaseOrder.visible.show = false
            this.purchaseOrder.data = {}
            this.purchaseOrder.id = ''
            this.purchaseOrder.type = ''
            break
          // 新增内部订单
          case 'internalOrder':
            this.internalOrder.visible.show = false
            this.internalOrder.data = []
            break
          // 金额确认
          case 'amount':
            this.amountModalVisible.show = false
            break
          // 融资申请
          case 'financing':
            this.financingApplication.visible.show = false
            this.financingApplication.type = ''
            this.financingApplication.id = ''
            break
          // 选择上架单
          case 'choosePutOnShelve':
            this.choosePutOnShelve.visible.show = false
            this.choosePutOnShelve.data = []
            this.choosePutOnShelve.id = ''
            break
          // 反审
          case 'reverseAudit':
            this.reverseAudit.visible.show = false
            this.reverseAudit.data = null
        }
        if (!isCancel) {
          this.getList()
        }
      },
      // 显示批量驳回弹窗
      showBatchRejectionModal() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        // 红冲中不能操作
        const target = this.tableChoseList.find(
          (item) => item.writeOffStatusLabel
        )
        if (target) {
          return this.$errorMsg(
            `操作失败，${target.code}订单，${target.writeOffStatusLabel}`
          )
        }
        const condition = this.tableChoseList.find(
          (item) => item.state !== '001'
        )
        if (condition) {
          this.$alertWarning(`销售订单:"${condition.code}"状态不为待审核`)
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        const codes = this.tableChoseList.map((item) => item.code).toString()
        this.batchRejection.visible.show = true
        this.batchRejection.data = { ids, codes }
      },
      /* 显示修改po号弹窗 */
      showChangePoNoModal({ poNo = '', id: orderId = '' }) {
        this.changePo.visible.show = true
        this.changePo.data = { poNo, orderId }
      },
      /* 重置搜索表单 */
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.stateList = []
          this.date1 = []
          this.date2 = []
          this.getList(1)
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .amount-text {
    cursor: pointer;
    padding-left: 6px;
    color: #6ea9f3;
  }
</style>
