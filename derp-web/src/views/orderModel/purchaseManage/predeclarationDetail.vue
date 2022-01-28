<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <JFX-title title="物流轨迹" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row linex">
      <div class="line-t-nx fs-12">
        <div
          class="line-item"
          v-for="(item, i) in lineArr"
          :key="i"
          :class="item.class"
        >
          <div class="t-c flex-c-c pd-10" style="min-height: 40px">
            {{ item.tips1 }}
          </div>
          <div class="mindle-bx mr-t-5 mr-b-5">
            <span
              class="mindle-bx-line"
              :style="+i === 0 ? 'opacity: 0' : ''"
            ></span>
            <span class="mindle-num fs-20">{{ i + 1 }}</span>
            <span
              class="mindle-bx-line"
              :style="+i === lineArr.length - 1 ? 'opacity: 0' : ''"
            ></span>
          </div>
          <div class="t-c pd-15" v-html="item.tips2"></div>
        </div>
      </div>
      <div class="line-t-nx" v-if="detail.id">
        <div style="width: 130px"></div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            v-if="detail.logisticsCommissionDate"
            @click="exportLogisticsDelegation(1)"
            v-permission="'declare_exportLogisticsCommission'"
          >
            导出国际物流委托
          </el-button>
          <el-button
            type="primary"
            v-else
            @click="openLay('exportLogisticsCommission')"
            v-permission="'declare_exportLogisticsCommission'"
          >
            导出国际物流委托
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="openLay('confirmBookingInfo')"
            :disabled="!!detail.confirmBookingDate"
            v-permission="'declare_confirmBookingInfo'"
          >
            确认订舱信息
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="openLay('deliveryConfirm')"
            :disabled="!!detail.shipDate"
            v-permission="'declare_deliveryConfirm'"
          >
            确认提货
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="exportCustomsDeclare"
            v-permission="'declare_exportCustomsDeclare'"
          >
            导出清关资料
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="openLay('confirmClearanceInfo')"
            :disabled="!!detail.customsConfirmDate"
            v-permission="'declare_confirmClearanceInfo'"
          >
            确认清关资料
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="submitDeclareOrder"
            :disabled="
              !['002', '004'].includes(detail.status) ||
              !!detail.pushWarehouseDate
            "
            v-permission="'declare_submitDeclareOrder'"
            :loading="submitDeclareOrderLoading"
            v-if="+detail.depotIsInOutInstruction === 1"
          >
            推送入仓指令
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="openLay('confirmArriveSeaDate')"
            :disabled="!!detail.arriveSeaDate"
          >
            确认到港
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="openLay('confirmDeclarationTime')"
            :disabled="!!detail.confirmDeclarationDate"
            v-permission="'declare_confirmDeclarationTime'"
          >
            完成申报
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="exportLogisticsDelegation(2)"
            v-permission="'declare_confirmDeclarationTime'"
          >
            导出物流委托
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="openLay('confirmCat')"
            :disabled="!!detail.confirmCatDate"
            v-permission="'declare_confirmDeclarationTime'"
          >
            确认订车
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="openLay('confirmWarehousingTime')"
            :disabled="!!detail.confirmDepotDate"
            v-permission="'declare_confirmWarehousingTime'"
          >
            确认入仓
          </el-button>
        </div>
        <div style="width: 130px" class="flex-c-c">
          <el-button
            type="primary"
            @click="submitDeclareOrder"
            :disabled="!['002', '004'].includes(detail.status)"
            v-permission="'declare_submitDeclareOrder'"
            :loading="submitDeclareOrderLoading"
            v-if="+detail.depotIsInOutInstruction !== 1"
          >
            上架入库
          </el-button>
        </div>
      </div>
    </el-row>
    <!-- title -->
    <JFX-title title="基本信息" className="mr-t-10 " />
    <!-- title end -->
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        预申报单号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        供应商：{{ detail.supplierName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        业务模式：{{ detail.businessModelLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        入仓仓库：{{ detail.depotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        付款条款：{{
          ['', '一次付款-付款发货', '多次付款', '一次付款-发货付款'][
            detail.paymentProvision
          ] || '- -'
        }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        订单状态： {{ detail.statusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        PO号：{{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        采购订单号：{{ detail.purchaseCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        贸易条款：{{ detail.tradeTermsLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        创建人： {{ detail.createName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        创建时间： {{ detail.createDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        审核人： {{ detail.submitter || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        审核时间： {{ detail.submitDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        推送入仓指令时间：{{ detail.submitDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        LBX单号： {{ detail.lbxNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        海外仓理货单位： {{ detail.tallyingUnitLabel || '- -' }}
      </el-col>
    </el-row>
    <JFX-title title="地点及计划" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        装货港: {{ detail.portLoading || '- -' }}
      </el-col>
      <!-- <el-col  class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6" >
        目的地名称： {{detail.destinationName || '- -'}}
      </el-col> -->
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        目的港名称：{{ detail.destinationPortName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        卸货港： {{ detail.portDestNoLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        收货地点： {{ detail.deliveryAddr || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        进出口口岸： {{ detail.imExpPort || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        入库关区：{{ detail.inPlatformCustoms || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        预计到港时间：{{ detail.arriveDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        预计离港时间：{{
          detail.estimatedDeliveryDate
            ? detail.estimatedDeliveryDate.slice(0, 10)
            : '- -'
        }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        备注： {{ detail.remark || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        装船时间： {{ detail.shipDate ? detail.shipDate.slice(0, 10) : '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        付款条约：{{ detail.payRules || '- -' }}
      </el-col>
      <!-- <el-col  class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6" >
        销售渠道：{{detail.channel || '- -'}}
      </el-col> -->
    </el-row>
    <JFX-title title="物流信息" className="mr-t-10 " />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        国际运输方式：{{ detail.transportLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        陆运方式：{{ detail.landTransportLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        境外发货人： {{ detail.shipper || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        承运商：{{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        发票号：{{ detail.invoiceNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        报关合同号：{{ detail.contractNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        提单号：{{ detail.ladingBill || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        二程提单号： {{ detail.blNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        托数：{{ detail.torrNum || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        托盘材质：{{ detail.palletMaterialLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        箱数：{{ detail.boxNum || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        包装方式: {{ detail.packType || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        提单毛重KG： {{ detail.billWeight || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        车型及数量：{{ detail.motorcycleType || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        标准箱TEU：{{ detail.standardCaseTeu || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        唛头： {{ detail.mark || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        车次：{{ detail.trainNumber || '- -' }}
      </el-col>
    </el-row>
    <JFX-title title="物流委托书" className="mr-t-10" style="overflow: hidden">
      <el-button
        @click="showEditLogisticsDelegationModal"
        v-if="['001', '002', '004'].includes(detail.status)"
        style="float: right"
        type="primary"
      >
        编辑
      </el-button>
    </JFX-title>
    <el-row :gutter="10" class="fs-12 clr-II">
      <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <div class="flex-bx">
          <span class="fs-14 clr-II">
            发货人/提货信息：{{ detail.shipperTempName || '- -' }}
          </span>
        </div>
        <div
          class="flex-bx mr-t-5"
          style="
            white-space: pre-line;
            width: 500px;
            height: 90px;
            overflow: auto;
          "
          v-text="detail.shipperTempDetails"
        ></div>
      </el-col>
      <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <div class="flex-bx">
          <span class="fs-14 clr-II">
            收货人/卸货信息：{{ detail.consigneeTempName || '- -' }}
          </span>
        </div>
        <div
          class="flex-bx mr-t-5"
          style="
            white-space: pre-line;
            width: 500px;
            height: 90px;
            overflow: auto;
          "
          v-text="detail.consigneeTempDetails"
        ></div>
      </el-col>
      <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <div class="flex-bx">
          <span class="fs-14 clr-II">
            通知人：{{ detail.notifierTempName || '- -' }}
          </span>
        </div>
        <div
          class="flex-bx mr-t-10"
          style="
            white-space: pre-line;
            width: 500px;
            height: 90px;
            overflow: auto;
          "
          v-text="detail.notifierTempDetails"
        ></div>
      </el-col>
      <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <div class="flex-bx">
          <span class="fs-14 clr-II">
            对接人：{{ detail.dockingTempName || '- -' }}
          </span>
        </div>
        <div
          class="flex-bx mr-t-10"
          style="
            white-space: pre-line;
            width: 500px;
            height: 90px;
            overflow: auto;
          "
          v-text="detail.dockingTempDetails"
        ></div>
      </el-col>
      <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <div class="flex-bx">
          <span class="fs-14 clr-II">
            承运商信息：{{ detail.carrierInformationTempName || '- -' }}
          </span>
        </div>
        <div
          class="flex-bx mr-t-5"
          style="
            white-space: pre-line;
            width: 500px;
            height: 90px;
            overflow: auto;
          "
          v-text="detail.carrierInformationTempDetails"
        ></div>
      </el-col>
    </el-row>
    <!-- title -->
    <el-tabs v-model="tabStatus">
      <el-tab-pane label="商品信息" name="0">
        <JFX-table
          :tableData.sync="tableData"
          :showPagination="false"
          :showSummary="true"
          :summaryMethod="null"
        >
          <el-table-column
            type="index"
            label="序号"
            align="center"
            width="50"
          ></el-table-column>
          <el-table-column
            label="采购订单号"
            align="center"
            width="140"
            show-overflow-tooltip
          >
            <template slot-scope="scope">{{ scope.row.purchase }}</template>
          </el-table-column>
          <el-table-column
            label="PO号"
            align="center"
            width="140"
            show-overflow-tooltip
          >
            <template slot-scope="scope">{{ scope.row.poNo }}</template>
          </el-table-column>
          <el-table-column label="合同号" align="center" min-width="110">
            <template slot-scope="scope">{{ scope.row.contractNo }}</template>
          </el-table-column>
          <el-table-column
            label="商品编码"
            align="center"
            min-width="140"
            show-overflow-tooltip
          >
            <template slot-scope="scope">{{ scope.row.goodsCode }}</template>
          </el-table-column>
          <el-table-column
            label="条形码"
            align="center"
            min-width="140"
            show-overflow-tooltip
          >
            <template slot-scope="scope">{{ scope.row.barcode }}</template>
          </el-table-column>
          <el-table-column
            prop="goodsName"
            label="商品名称"
            align="center"
            min-width="150"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="商品货号" align="center" min-width="120">
            <template slot-scope="scope">{{ scope.row.goodsNo }}</template>
          </el-table-column>
          <el-table-column
            prop="num"
            label="申报数量"
            align="center"
            min-width="80"
          ></el-table-column>
          <el-table-column label="采购单价" align="center" min-width="80">
            <template slot-scope="scope">{{
              scope.row.purchasePrice
            }}</template>
          </el-table-column>
          <el-table-column label="申报单价" align="center" min-width="80">
            <template slot-scope="scope">{{ scope.row.price }}</template>
          </el-table-column>
          <el-table-column
            prop="amount"
            label="申报总金额(RMB)"
            align="center"
            min-width="130"
          ></el-table-column>
          <el-table-column
            prop="brandName"
            label="品牌类型"
            align="center"
            min-width="110"
          ></el-table-column>
          <el-table-column
            prop="grossWeightSum"
            label="毛重（KG）"
            align="center"
            min-width="90"
          ></el-table-column>
          <el-table-column
            prop="netWeightSum"
            label="净重（KG）"
            align="center"
            min-width="90"
          ></el-table-column>
          <el-table-column label="箱号" align="center">
            <template slot-scope="scope">{{ scope.row.boxNo }}</template>
          </el-table-column>
          <el-table-column label="托盘号" align="center">
            <template slot-scope="scope">{{ scope.row.palletNo }}</template>
          </el-table-column>
          <el-table-column label="生产批次号" align="center" min-width="90">
            <template slot-scope="scope">{{ scope.row.batchNo }}</template>
          </el-table-column>
          <el-table-column
            prop="constituentRatio"
            label="成分占比"
            align="center"
            min-width="100"
            show-overflow-tooltip
          ></el-table-column>
        </JFX-table>
      </el-tab-pane>
      <el-tab-pane label="装箱明细" name="1">
        <JFX-table
          :tableData.sync="packingTableData"
          :tableColumn="packingTableColumn"
          :showPagination="false"
        >
        </JFX-table>
      </el-tab-pane>
    </el-tabs>
    <!-- 表格 end-->
    <JFX-title title="附件列表" className="mr-t-20" />
    <div class="mr-t-15 mr-b-15 fs-14 clr-II">
      <JFX-upload
        @success="successUpload"
        :url="action"
        :limit="10000"
        :multiple="false"
      >
        <el-button id="sale-upload-btn" type="primary">上传附件</el-button>
        <span class="clr-II fs-10">
          (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
        </span>
      </JFX-upload>
    </div>
    <enclosure-list
      :code="detail.code"
      v-if="detail.code"
      ref="enclosure"
    ></enclosure-list>
    <!-- 清关资料导出确认 -->
    <export-confirm
      v-if="visible.show"
      :visible="visible"
      :targetData="targetData"
      :isRadio="false"
      @comfirm="comfirmQingGuan"
    ></export-confirm>
    <!-- 清关资料导出确认 end-->
    <!-- 功能-->
    <JFX-Dialog
      v-if="visible1.show"
      :visible.sync="visible1"
      :showClose="true"
      @comfirm="save"
      :width="'650px'"
      :title="title"
      :top="'30vh'"
      closeBtnText="取 消"
      confirmBtnText="确 认"
    >
      <JFX-Form :model="editrRuleForm" ref="editrRuleForm">
        <el-row :gutter="10" class="edit-bx" v-loading="saveLoading">
          <el-col
            :span="24"
            v-if="btnHandleType === 'exportLogisticsCommission'"
          >
            <el-form-item
              label="物流委托时间："
              prop="exportLogisticsDate"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.exportLogisticsDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择"
                :picker-options="pickerOptions"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmDeclarationTime'">
            <el-form-item
              label="完成申报时间："
              prop="confirmDeclarationDate"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.confirmDeclarationDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col
            :span="24"
            class="flex-l-c"
            v-if="btnHandleType === 'confirmDeclarationTime'"
          >
            <el-form-item label="报关单上传：">
              <JFX-upload
                @success="successUpload"
                :url="action"
                :limit="10000"
                :multiple="false"
              >
                <el-button id="sale-upload-btn" type="primary">
                  选择文件
                </el-button>
                {{ fileName }}
              </JFX-upload>
              <span class="clr-II fs-10">
                (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmWarehousingTime'">
            <el-form-item
              label="提货时间："
              prop="pickUpDate"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.pickUpDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmWarehousingTime'">
            <el-form-item
              label="入仓时间："
              prop="confirmWarehousingDate"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.confirmWarehousingDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmBookingInfo'">
            <el-form-item
              label="预计离港时间："
              prop="deliveryDate"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.deliveryDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmBookingInfo'">
            <el-form-item
              label="预计到港时间："
              prop="arrivalDate"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.arrivalDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmBookingInfo'">
            <el-form-item
              label="确认订舱时间："
              prop="confirmBookingInfoData"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.confirmBookingInfoData"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
                :picker-options="pickerOptions"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'deliveryConfirm'">
            <el-form-item
              label="确认提货时间："
              prop="deliveryConfirmDate"
              :rules="{
                required: true,
                message: '不能为空!',
                trigger: 'change'
              }"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.deliveryConfirmDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col
            :span="24"
            class="flex-l-c"
            v-if="btnHandleType === 'deliveryConfirm'"
          >
            <el-form-item label="上传附件：" required>
              <JFX-upload
                @success="successUpload"
                :url="action"
                :limit="10000"
                :multiple="false"
              >
                <el-button id="sale-upload-btn" type="primary">
                  选择文件
                </el-button>
                {{ fileName }}
              </JFX-upload>
              <span class="clr-II fs-10">
                (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmClearanceInfo'">
            <el-form-item
              label="确认清关资料："
              prop="confirmClearanceInfoDate"
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.confirmClearanceInfoDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col
            :span="24"
            class="flex-l-c"
            v-if="btnHandleType === 'confirmClearanceInfo'"
          >
            <el-form-item label="上传附件：" required>
              <JFX-upload
                @success="successUpload"
                :url="action"
                :limit="10000"
                :multiple="false"
              >
                <el-button id="sale-upload-btn" type="primary">
                  选择文件
                </el-button>
                {{ fileName }}
              </JFX-upload>
              <span class="clr-II fs-10">
                (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="btnHandleType === 'confirmCat'">
            <el-form-item label="确认订车时间：" prop="confirmCatDate" required>
              <el-date-picker
                clearable
                v-model="editrRuleForm.confirmCatDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col
            :span="24"
            class="flex-l-c"
            v-if="btnHandleType === 'confirmCat'"
          >
            <el-form-item label="上传附件：">
              <JFX-upload
                @success="successUpload"
                :url="action"
                :limit="10000"
                :multiple="false"
              >
                <el-button id="sale-upload-btn" type="primary">
                  选择文件
                </el-button>
                {{ fileName }}
              </JFX-upload>
              <span class="clr-II fs-10">
                (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
              </span>
            </el-form-item>
          </el-col>
          <el-col
            :span="24"
            class="flex-l-c"
            v-if="btnHandleType === 'confirmArriveSeaDate'"
          >
            <el-form-item
              label="到港时间："
              prop="confirmArriveSeaDate"
              required
            >
              <el-date-picker
                clearable
                v-model="editrRuleForm.confirmArriveSeaDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-Dialog>
    <!-- 功能 end -->
    <!-- 修改物流委托 start -->
    <EditLogisticsDelegation
      v-if="editLogisticsDelegation.visible.show"
      :visible="editLogisticsDelegation.visible"
      :detail="detail"
      @close="closeEditLogisticsDelegationModal"
    />
    <!-- 修改物流委托 end -->
  </div>
</template>
<script>
  import {
    getDeclareDetail,
    getExportTemplate,
    exportCustomsDeclareUrl,
    updateTrajectory,
    exportLogisticsDelegationUrl,
    submitDeclareOrder
  } from '@a/purchaseManage/index'
  import commomMix from '@m/index'
  import { attachmentUploadFiles } from '@a/base/index'
  import { getBaseUrl } from '@u/tool'
  export default {
    mixins: [commomMix],
    components: {
      exportConfirm: () => import('@c/exportConfirm/index'),
      enclosureList: () => import('@c/enclosureList/index'),
      // 修改物流委托
      EditLogisticsDelegation: () =>
        import('./components/editLogisticsDelegation')
    },
    data() {
      return {
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10000,
          total: 10001
        },
        detail: {},
        lineArr: [
          { tips1: '', tips2: '已接单' },
          { tips1: '', tips2: '订舱' },
          { tips1: '', tips2: '订舱信息确认' },
          { tips1: '计划装船时间 ', tips2: '工厂提货（装船）' },
          { tips1: '', tips2: '提交一线清关资料 ' },
          { tips1: '', tips2: '审核一线清关资料 ' },
          { tips1: '', tips2: '推送接口 ' },
          { tips1: '', tips2: '空海运到港' },
          { tips1: '', tips2: '申报完成 ' },
          { tips1: '', tips2: '陆运订车 ' },
          { tips1: '', tips2: '订车信息确认 ' },
          { tips1: '', tips2: '提货入仓 ' },
          { tips1: '', tips2: '已上架 ' }
        ],
        visible: { show: false },
        visible1: { show: false },
        editrRuleForm: {
          exportLogisticsDate: '',
          confirmDeclarationDate: '',
          confirmWarehousingDate: '',
          deliveryDate: '',
          arrivalDate: '',
          confirmBookingInfoData: '',
          deliveryConfirmDate: '',
          confirmClearanceInfoDate: '',
          confirmCatDate: '',
          confirmArriveSeaDate: '',
          pickUpDate: ''
        },
        /**
         * exportLogisticsCommission 导出物流委托
         * confirmBookingInfo 确认订舱信息
         * deliveryConfirm 确认提货
         * confirmClearanceInfo 确认清关资料
         * confirmDeclarationTime 完成申报时间
         * confirmWarehousingTime 确认入仓时间
         * confirmArriveSeaDate 确认到港时间
         */
        btnHandleType: '',
        saveLoading: false,
        // 日期控件配置
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now()
          }
        },
        action: '',
        submitDeclareOrderLoading: false,
        fileName: '',
        // 修改物流委托信息
        editLogisticsDelegation: {
          visible: { show: false }
        },
        // 商品明细 箱装明细 tab 切换
        tabStatus: '0',
        // 箱装明细列表
        packingTableData: {
          list: []
        },
        // 箱装明细字段
        packingTableColumn: [
          {
            label: '序号',
            prop: 'index',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品条形码',
            prop: 'barcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '柜号',
            prop: 'cabinetNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '托盘号',
            prop: 'torrNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '箱数',
            prop: 'boxNum',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '件数',
            prop: 'piecesNum',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { query } = this.$route
        if (!query.id) return false
        try {
          const res = await getDeclareDetail({ id: query.id })
          this.detail = res.data
          // console.log(this.detail.depotIsInOutInstruction)
          this.tableData.list = this.detail.itemList || []
          this.packingTableData.list = this.detail.packingList || []
          this.action =
            getBaseUrl(attachmentUploadFiles) +
            attachmentUploadFiles +
            '?token=' +
            sessionStorage.getItem('token') +
            '&code=' +
            this.detail.code
          this.lineArr = [
            {
              tips1: '',
              tips2: '已接单 ' + (this.detail.createDate || ''),
              class: this.detail.createDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2: '订舱 ' + (this.detail.logisticsCommissionDate || ''),
              class: this.detail.logisticsCommissionDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2: '确认订舱信息 ' + (this.detail.confirmBookingDate || ''),
              class: this.detail.confirmBookingDate ? 'clr-act' : ''
            },
            {
              tips1:
                '预计离港时间 ' +
                (this.detail.estimatedDeliveryDate
                  ? this.detail.estimatedDeliveryDate.slice(0, 10)
                  : ''),
              tips2: '工厂提货（装船）' + (this.detail.shipDate || ''),
              class: this.detail.shipDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2:
                '提交一线清关资料 ' + (this.detail.customsSubmitDate || ''),
              class: this.detail.customsSubmitDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2:
                '审核一线清关资料 ' + (this.detail.customsConfirmDate || ''),
              class: this.detail.customsConfirmDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2: '推送接口 ' + (this.detail.pushWarehouseDate || ''),
              class: this.detail.pushWarehouseDate ? 'clr-act' : ''
            },
            {
              tips1:
                '预计到港时间 ' +
                (this.detail.arriveDate
                  ? this.detail.arriveDate.slice(0, 10)
                  : ''),
              tips2: '空海运到港 ' + (this.detail.arriveSeaDate || ''),
              class: this.detail.arriveSeaDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2: '完成申报 ' + (this.detail.confirmDeclarationDate || ''),
              class: this.detail.confirmDeclarationDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2: '陆运订车 ' + (this.detail.landCommissionDate || ''),
              class: this.detail.landCommissionDate ? 'clr-act' : ''
            },
            {
              tips1: '',
              tips2: '确认订车信息 ' + (this.detail.confirmCatDate || ''),
              class: this.detail.confirmCatDate ? 'clr-act' : ''
            },
            // { tips1: '', tips2: '提货入仓' +  + '提货：' + (this.detail.confirmDepotDate || '') + '\n' + '入仓：' + (this.detail.pickUpDate || ''), class: (this.detail.confirmDepotDate || this.detail.pickUpDate) ? 'clr-act' : '' },
            {
              tips1: '',
              tips2: `<div>提货入仓</div><div>提货：${
                this.detail.pickUpDate || ''
              }</div><div>入仓：${this.detail.confirmDepotDate || ''}</div>`,
              class:
                this.detail.confirmDepotDate || this.detail.pickUpDate
                  ? 'clr-act'
                  : ''
            },
            {
              tips1: '',
              tips2: '已上架 ' + (this.detail.shelfDate || ''),
              class: this.detail.shelfDate ? 'clr-act' : ''
            }
          ]
        } catch (err) {}
      },
      // 导出清关资料
      async exportCustomsDeclare() {
        // isSameArea - 是否同关区,  outDepotId - 出仓id,         inDepotId - 入仓id,        outCustomsId - 出仓关区, inCustomsId - 入仓同关区
        const {
          outDepotId = '',
          outCustomsId = '',
          inCustomsId = '',
          depotId
        } = this.detail
        const opt = {
          outDepotId: outDepotId || '',
          inDepotId: depotId,
          outCustomsId: outCustomsId || '',
          inCustomsId: inCustomsId || ''
        }
        let fileTempIds = ''
        try {
          const res = await getExportTemplate({ json: JSON.stringify(opt) })
          if (res.data.code === '00') {
            // 直接导出
            // 导出前先提交清关时间
            if (!this.detail.customsSubmitDate) {
              await updateTrajectory({
                id: this.detail.id,
                customsSubmitDate: this.$formatDate(
                  Date.now(),
                  'yyyy-MM-dd HH:mm:ss'
                ),
                type: 5
              })
              this.init()
            }
            if (res.data.inList && res.data.inList.length > 0) {
              fileTempIds = res.data.inList[0].fileTempId
            }
            if (res.data.outList && res.data.outList.length > 0) {
              fileTempIds = res.data.outList[0].fileTempId
            }
            this.$exportFile(exportCustomsDeclareUrl, {
              id: this.detail.id,
              fileTempIds
            })
          } else if (res.data.code === '01') {
            // 没有找到关联模板
            this.$alertWarning('该仓库暂无配置清关资料模板，请先完成模板配置！')
          } else {
            // 弹出选择模板
            this.targetData = res.data
            this.visible.show = true
          }
        } catch (err) {
          console.log(err)
        }
      },
      // 选择模板后导出清关资料
      async comfirmQingGuan({ inFileTemp }) {
        const { id } = this.detail
        // 导出前先提交清关时间
        if (!this.detail.customsSubmitDate) {
          await updateTrajectory({
            id,
            customsSubmitDate: this.$formatDate(
              Date.now(),
              'yyyy-MM-dd HH:mm:ss'
            ),
            type: 5
          })
          this.init()
        }
        const fileTempIds = inFileTemp.map((item) => item.fileTempId).toString()
        this.visible.show = false
        this.$exportFile(exportCustomsDeclareUrl, {
          id,
          fileTempIds
        })
      },
      // 打开按钮操作
      /**
       * exportLogisticsCommission 导出物流委托
       * confirmBookingInfo 确认订舱信息
       * deliveryConfirm 确认提货
       * confirmClearanceInfo 确认清关资料
       * confirmDeclarationTime 完成申报时间
       * confirmWarehousingTime 确认入仓时间
       * confirmArriveSeaDate 确认到港时间
       */
      openLay(type) {
        this.btnHandleType = type
        const nowDate = this.$formatDate(Date.now(), 'yyyy-MM-dd HH:mm:ss')
        const { depotType, confirmDeclarationDate } = this.detail
        switch (type) {
          case 'exportLogisticsCommission': {
            this.title = '选择导出物流委托时间'
            this.editrRuleForm.exportLogisticsDate = nowDate
            break
          }
          case 'confirmBookingInfo': {
            this.title = '确认订舱时间'
            // this.editrRuleForm.deliveryDate = nowDate
            // this.editrRuleForm.arrivalDate = nowDate
            this.editrRuleForm.confirmBookingInfoData = nowDate
            break
          }
          case 'deliveryConfirm': {
            this.editrRuleForm.deliveryConfirmDate = nowDate
            this.title = '确认提货'
            break
          }
          case 'confirmClearanceInfo': {
            this.editrRuleForm.confirmClearanceInfoDate = nowDate
            this.title = '确认清关资料'
            break
          }
          case 'confirmDeclarationTime': {
            this.editrRuleForm.confirmDeclarationDate = nowDate
            this.title = '完成申报'
            break
          }
          case 'confirmWarehousingTime': {
            if (depotType !== 'd' && !confirmDeclarationDate) {
              this.$errorMsg('请先选择完成申报时间！')
              return false
            }
            this.editrRuleForm.confirmWarehousingDate = nowDate
            this.editrRuleForm.pickUpDate = nowDate
            this.title = '提货入仓'
            break
          }
          case 'confirmCat': {
            if (depotType !== 'd' && !confirmDeclarationDate) {
              this.$errorMsg('请先选择完成申报时间！')
              return false
            }
            this.editrRuleForm.confirmCatDate = nowDate
            this.title = '确认订车'
            break
          }
          case 'confirmArriveSeaDate': {
            this.title = '确认到港'
            this.editrRuleForm.confirmArriveSeaDate = nowDate
            break
          }
          default:
            break
        }
        this.fileName = ''
        this.visible1.show = true
      },
      async save() {
        if (this.saveLoading) return false
        const btnHandleType = this.btnHandleType
        const {
          exportLogisticsDate,
          deliveryDate,
          arrivalDate,
          confirmBookingInfoData,
          deliveryConfirmDate,
          confirmClearanceInfoDate,
          confirmDeclarationDate,
          confirmWarehousingDate,
          confirmCatDate,
          confirmArriveSeaDate,
          pickUpDate
        } = this.editrRuleForm
        let opt = {}
        // type 更新类型 2-订舱/车 3- 订舱/车信息确认 4-工厂提货（装船）5- 提交一线清关资料 6-审核一线清关资料 7-申报完成 8-提货入仓
        switch (btnHandleType) {
          case 'exportLogisticsCommission': {
            // 订舱/车
            if (!exportLogisticsDate) {
              this.$errorMsg('物流委托时间不能为空')
              return false
            }
            opt = { type: 2, logisticsCommissionDate: exportLogisticsDate }
            break
          }
          case 'confirmBookingInfo': {
            // 订舱/车信息确
            if (!deliveryDate) {
              this.$errorMsg('预计离港时间不能为空')
              return false
            }
            if (!arrivalDate) {
              this.$errorMsg('预计到港时间不能为空')
              return false
            }
            if (!confirmBookingInfoData) {
              this.$errorMsg('确认订舱时间不能为空')
              return false
            }
            const deliveryDateTimes = new Date(
              deliveryDate.replace(/-/, '/')
            ).getTime()
            const arrivalDateTimes = new Date(
              arrivalDate.replace(/-/, '/')
            ).getTime()
            const confirmBookingInfoDataTimes = new Date(
              confirmBookingInfoData.replace(/-/, '/')
            ).getTime()
            if (deliveryDateTimes < confirmBookingInfoDataTimes) {
              this.$errorMsg('预计离港时间小于确认订舱时间!')
              return false
            }
            if (arrivalDateTimes < confirmBookingInfoDataTimes) {
              this.$errorMsg('预计到港时间小于确认订舱时间!')
              return false
            }
            opt = {
              type: 3,
              estimatedDeliveryDate: deliveryDate,
              arriveDate: arrivalDate,
              confirmBookingDate: confirmBookingInfoData
            }
            break
          }
          case 'deliveryConfirm': {
            // 工厂提货（装船）
            if (!deliveryConfirmDate) {
              this.$errorMsg('确认提货时间不能为空')
              return false
            }
            if (!this.fileName) {
              this.$errorMsg('请上传附件')
              return false
            }
            opt = { type: 4, shipDate: deliveryConfirmDate }
            break
          }
          case 'confirmClearanceInfo': {
            if (!confirmClearanceInfoDate) {
              this.$errorMsg('确认清关资料不能为空')
              return false
            }
            if (!this.fileName) {
              this.$errorMsg('请上传附件')
              return false
            }
            opt = { type: 6, customsConfirmDate: confirmClearanceInfoDate }
            break
          }
          case 'confirmDeclarationTime': {
            if (!confirmDeclarationDate) {
              this.$errorMsg('完成申报时间不能为空')
              return false
            }
            opt = { type: 8, confirmDeclarationDate: confirmDeclarationDate }
            break
          }
          case 'confirmWarehousingTime': {
            if (!confirmWarehousingDate) {
              this.$errorMsg('入仓时间不能为空')
              return false
            }
            if (!pickUpDate) {
              this.$errorMsg('提货时间不能为空')
              return false
            }
            opt = {
              type: 11,
              confirmDepotDate: confirmWarehousingDate,
              pickUpDate
            }
            break
          }
          case 'confirmCat': {
            if (!confirmCatDate) {
              this.$errorMsg('确认订车时间不能为空')
              return false
            }
            opt = { type: 10, confirmCatDate }
            break
          }
          case 'confirmArriveSeaDate': {
            if (!confirmArriveSeaDate) {
              this.$errorMsg('到港时间不能为空')
              return false
            }
            opt = { type: 12, arriveSeaDate: confirmArriveSeaDate }
            break
          }
          default:
            break
        }
        this.saveLoading = true
        try {
          opt.id = this.detail.id
          await updateTrajectory(opt)
          this.$successMsg('操作成功')
          setTimeout(() => {
            this.init()
          }, 300)
          this.visible1.show = false
          // 导出物流
          if (this.btnHandleType === 'exportLogisticsCommission') {
            this.exportLogisticsDelegation(1)
          }
        } catch (error) {
          console.log(error)
        }
        this.saveLoading = false
      },
      // 上传成功
      successUpload(res) {
        if (res.code + '' === '10000') {
          this.fileName = res.data.attachmentInfo.attachmentName || ''
          this.$successMsg('上传成功')
          this.$refs.enclosure.getEnclosureList(1)
        } else {
          this.$errorMsg(res.data)
        }
      },
      // 导出物流委托
      exportLogisticsDelegation(type) {
        const { depotType, id, confirmDeclarationDate } = this.detail
        if (type === 1) {
          this.$exportFile(exportLogisticsDelegationUrl, { id, type })
        } else {
          if (depotType !== 'd' && !confirmDeclarationDate) {
            this.$errorMsg('请先选择完成申报时间！')
            return false
          }
          this.$exportFile(exportLogisticsDelegationUrl, { id, type })
          setTimeout(() => {
            this.init()
          }, 300)
        }
      },
      // 推送指令
      async submitDeclareOrder() {
        const { depotType, confirmDeclarationDate } = this.detail
        if (+this.detail.depotIsInOutInstruction === 1) {
          // 直接提交
          if (!this.detail.customsConfirmDate) {
            this.$errorMsg('请先确认清关资料！')
            return false
          }
          this.submitDeclareOrderLoading = true
          try {
            await submitDeclareOrder({ id: this.detail.id })
            this.$successMsg('操作成功')
            this.init()
          } catch (error) {
            console.log(error)
          }
          this.submitDeclareOrderLoading = false
        } else {
          // 跳转上架入库
          if (depotType !== 'd' && !confirmDeclarationDate) {
            this.$errorMsg('请先选择完成申报时间！')
            return false
          }
          this.linkTo('/purchase/predeclareInwarehouse?id=' + this.detail.id)
        }
      },
      // 显示修改物流弹窗
      showEditLogisticsDelegationModal() {
        this.editLogisticsDelegation.visible.show = true
      },
      // 关闭修改物流弹窗
      closeEditLogisticsDelegationModal() {
        this.editLogisticsDelegation.visible.show = false
        this.init()
      }
    }
  }
</script>
<style lang="scss" scoped>
  .tongji-item {
    display: inline-block;
    width: 220px;
  }
  .pad-t-10 {
    padding-bottom: 10px;
    padding-top: 10px;
  }
  .flex-bx {
    display: flex;
    box-sizing: border-box;
    padding-left: 20px;
  }
  .line-t-nx {
    display: flex;
    overflow: auto;
  }
  .line-item {
    width: 130px;
    height: 160px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-direction: column;
  }
  .mindle-bx {
    height: 30px;
    display: flex;
    justify-content: center;
    flex-direction: row;
    align-items: center;
    width: 100%;
  }
  .mindle-num {
    display: inline-flex;
    justify-content: center;
    align-items: center;
    height: 30px;
    width: 30px;
    border-radius: 50%;
    border: solid 1px #999;
  }
  .mindle-bx-line {
    display: inline-flex;
    flex: 1;
    height: 1px;
    background: #999;
  }
  .t-c {
    text-align: center;
  }
  .linex {
    box-sizing: border-box;
    padding: 30px 0;
  }
  .clr-act {
    .mindle-num {
      border: solid 1px #6ea9f3;
    }
    .mindle-bx-line {
      background: #6ea9f3;
    }
  }
  ::v-deep.edit-bx .el-form-item__label {
    width: 160px;
  }
</style>
