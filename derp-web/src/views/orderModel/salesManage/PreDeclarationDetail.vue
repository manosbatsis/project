<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :breadcrumb="breadcrumb" showGoback />
    <!-- 面包屑 end -->
    <JFX-title title="物流轨迹" className="mr-t-10" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row linex">
      <!-- 流程 -->
      <div class="line-t-nx fs-12">
        <div
          class="line-item"
          v-for="(item, i) in lineArr"
          :key="i"
          :class="item.class"
        >
          <div class="mindle-bx mr-t-5 mr-b-5">
            <span
              class="mindle-bx-line"
              :style="+i === 0 ? 'opacity: 0' : ''"
            ></span>
            <span class="mindle-num fs-20">{{ i + 1 }}</span>
            <span
              class="mindle-bx-line"
              :style="+i === 10 ? 'opacity: 0' : ''"
            ></span>
          </div>
          <div class="t-c pd-15">
            <p>{{ item.text }}</p>
            <p>{{ item.date }}</p>
          </div>
        </div>
      </div>
      <!-- 流程 -->
      <!-- 操作 -->
      <div class="line-t-nx">
        <div style="width: 170px" class="flex-c-c"></div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="exportPallteize"
            v-permission="'sale_declare_pushOut'"
          >
            导出打托资料
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="showModal('outWarehouse')"
            :disabled="!!detail.pallteizeDate"
            v-permission="'sale_declare_out'"
          >
            确认打托
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="exportCustomsDeclare"
            v-permission="'sale_declare_exportCustoms'"
          >
            导出清关资料
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="showModal('customsConfirm')"
            :disabled="!!detail.customsConfirmDate"
            v-permission="'sale_declare_customsConfirm'"
          >
            确认清关资料
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            :disabled="!!detail.pushOutDate"
            v-if="detail.depotIsInOutInstruction === '1'"
            @click="pushOut"
            v-permission="'sale_declare_pushOut'"
          >
            推送出库指令
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="showModal('confirmDeclaration')"
            :disabled="!!detail.confirmDeclarationDate"
            v-permission="'sale_declare_confirmDeclaration'"
          >
            确认申报时间
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="showModal('logisticsCommission')"
            v-permission="'sale_declare_logistics'"
          >
            预约物流
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="jumpPutOut"
            v-if="detail.depotIsInOutInstruction !== '1'"
            :disabled="
              ['004', '005', '007', '008'].includes(detail.status) ||
              detail.depotIsInOutInstruction === '1'
            "
            v-permission="'sale_declare_putOut'"
          >
            打托出库
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c">
          <el-button
            type="primary"
            @click="jumpPutOnSale"
            :disabled="!['005', '004'].includes(detail.status)"
            v-permission="'sale_declare_putOnSale'"
          >
            上架
          </el-button>
        </div>
        <div style="width: 170px" class="flex-c-c"></div>
      </div>
      <!-- 操作 end -->
    </el-row>
    <!-- title -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <!-- title end -->
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        预申报单号：{{ detail.code || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        客户：{{ detail.customerName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        出仓仓库：{{ detail.outDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        订单状态： {{ detail.statusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        PO号：{{ detail.poNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        销售订单号：{{ detail.saleOrderCodes || '- -' }}
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
        入仓仓库： {{ detail.inDepotName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        销售币种： {{ detail.currency || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        海外仓理货单位： {{ detail.tallyingUnitLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        LBX单号： {{ detail.lbxNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        推送出仓指令时间：{{ detail.pushOutDate || '- -' }}
      </el-col>
      <!-- <el-col  class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6" >
        付款条款：{{detail.paymentTermsLabel || '- -'}}
      </el-col> -->
    </el-row>
    <JFX-title title="地点及计划" className="mr-t-10" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        装货港/出库地点: {{ detail.loadPort || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        香港目的地名称： {{ detail.destination || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        目的港/目的地名称：{{ detail.destinationPort || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        卸货港： {{ detail.portDesLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        出库地点： {{ detail.deliveryAddr || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        进出口口岸： {{ detail.imExpPort || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        出库关区：{{ detail.outCustomsName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        入库关区：{{ detail.inCustomsName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        装船时间： {{ detail.shipDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        启运港：{{ detail.departurePort || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        备注： {{ detail.remark || '- -' }}
      </el-col>
    </el-row>
    <JFX-title title="物流信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        运输方式：{{ detail.transportLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        境外发货人： {{ detail.shipper || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        承运商：{{ detail.carrier || '- -' }}
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
        箱数：{{ detail.boxNum || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        托盘材质：{{ detail.torrPackTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        包装方式: {{ detail.pack || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        提单毛重KG： {{ detail.billWeight || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        车型及数量：{{ detail.motorcycleType || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        标准箱TEU：{{ detail.teu || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        唛头： {{ detail.mark || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        车次：{{ detail.trainNo || '- -' }}
      </el-col>
    </el-row>
    <!-- 001-待打托 002待清关 003-待物流委托 004-已出库 005-部分上架 006已删除 007-已上架 008-出库中 009-待出库 -->
    <JFX-title title="物流委托书" className="mr-t-10" style="overflow: hidden">
      <el-button
        @click="showEditLogisticsDelegationModal"
        v-if="['001', '002', '003', '004', '009'].includes(detail.status)"
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
    <JFX-title title="收货信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        提货方式：{{ detail.mailModeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        收货人姓名：{{ detail.receiverName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        国家：{{ detail.country || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        详情地址：{{ detail.receiverAddress || '- -' }}
      </el-col>
    </el-row>
    <!-- title -->
    <JFX-title title="商品信息" className="mr-t-15" />
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    ></JFX-table>
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
    <EnclosureList :code="detail.code" ref="enclosure" />
    <!-- 清关资料导出确认 -->
    <ExportConfirm
      v-if="exportConfirm.visible.show"
      :isVisible="exportConfirm.visible"
      :targetData="exportConfirm.targetData"
      @comfirm="(data) => handleExportConfirm(data)"
    ></ExportConfirm>
    <!-- 清关资料导出确认 end-->
    <!-- 选择上架单 -->
    <DeclareChoosePutOnt
      v-if="choosePutOnShelve.visible.show"
      :data="choosePutOnShelve.data"
      :ChoosePutOnShelveVisible="choosePutOnShelve.visible"
      @comfirm="choosePutOnShelve.visible.show = false"
    ></DeclareChoosePutOnt>
    <!-- 选择上架单 end -->
    <!-- 功能-->
    <JFX-Dialog
      v-if="all.visible.show"
      :visible.sync="all.visible"
      :showClose="true"
      :width="'650px'"
      :title="all.title"
      :top="'30vh'"
      closeBtnText="取 消"
      confirmBtnText="确 认"
      @comfirm="closeModal"
    >
      <JFX-Form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="10" class="edit-bx" v-loading="saveLoading">
          <!-- 确认打托 -->
          <el-col :span="24" v-if="all.type === 'outWarehouse'">
            <el-form-item
              label="打托时间："
              prop="pallteizeDate"
              :rules="{
                required: true,
                message: '请选择打托时间',
                trigger: 'change'
              }"
            >
              <el-date-picker
                v-model="ruleForm.pallteizeDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择"
                :picker-options="pickerOptions"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="打托明细：" required>
              <JFX-upload
                @success="successUpload"
                :url="action"
                :limit="10000"
                :multiple="false"
              >
                <el-button id="sale-upload-btn" type="primary">
                  选择文件
                </el-button>
                <span style="padding-left: 10px">{{ fileName }}</span>
              </JFX-upload>
            </el-form-item>
            <el-form-item>
              <span class="clr-II fs-10" style="margin-left: 40px">
                (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
              </span>
            </el-form-item>
          </el-col>
          <!-- 确认打托 end -->
          <!-- 确认清关资料 -->
          <el-col :span="24" v-if="all.type === 'customsConfirm'">
            <el-form-item
              label="确认清关时间："
              prop="customsConfirmDate"
              :rules="{
                required: true,
                message: '请选择确认清关时间',
                trigger: 'change'
              }"
            >
              <el-date-picker
                v-model="ruleForm.customsConfirmDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择"
                :default-value="defaultTimeValue"
                :picker-options="pickerOptions"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="附件上传：" required>
              <JFX-upload
                @success="successUpload"
                :url="action"
                :limit="10000"
                :multiple="false"
              >
                <el-button id="sale-upload-btn" type="primary">
                  选择文件
                </el-button>
                <span style="padding-left: 10px">{{ fileName }}</span>
              </JFX-upload>
            </el-form-item>
            <el-form-item>
              <span class="clr-II fs-10" style="margin-left: 40px">
                (支持JPG、JPEG、BMP、PNG、GIF、PDF、DOCX、DOC、XLS、XLSX格式)
              </span>
            </el-form-item>
          </el-col>
          <!-- 确认清关资料 end -->
          <!-- 确认申报时间 -->
          <el-col :span="24" v-if="all.type === 'confirmDeclaration'">
            <el-form-item
              label="确认申报时间："
              prop="confirmDeclarationDate"
              :rules="{
                required: true,
                message: '请选择申报时间',
                trigger: 'change'
              }"
            >
              <el-date-picker
                v-model="ruleForm.confirmDeclarationDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="附件上传：">
              <JFX-upload
                @success="successUpload"
                :url="action"
                :limit="10000"
                :multiple="false"
              >
                <el-button id="sale-upload-btn" type="primary">
                  报关单上传
                </el-button>
                <span style="padding-left: 10px">{{ fileName }}</span>
              </JFX-upload>
            </el-form-item>
          </el-col>
          <!-- 确认申报时间 end -->
          <!-- 选择导出物流委托时间 -->
          <el-col
            :span="24"
            v-if="
              all.type === 'logisticsCommission' &&
              !detail.logisticsCommissionDate
            "
          >
            <el-form-item
              label="物流委托时间："
              prop="logisticsCommissionDate"
              :rules="{
                required: true,
                message: '请选择导出物流委托时间',
                trigger: 'change'
              }"
            >
              <el-date-picker
                v-model="ruleForm.logisticsCommissionDate"
                type="datetime"
                style="width: 320px"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <!-- 选择导出物流委托时间 end -->
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
  import commomMix from '@m/index'
  import { getExportTemplate, attachmentUploadFiles } from '@a/base'
  import { getBaseUrl } from '@u/tool'
  import {
    searchDetail,
    modifyPushOutOrder,
    updateTimeTrace,
    exportLogisticsDelegationUrl,
    exportCustomsDeclareUrl,
    getSaleOutDepotList,
    exportPallteizeDataUrl
  } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    components: {
      // 确认清关导出
      ExportConfirm: () => import('./components/ExportConfirm'),
      // 附件列表
      EnclosureList: () => import('@c/enclosureList/index'),
      // 选择上架
      DeclareChoosePutOnt: () => import('./components/DeclareChoosePutOnt'),
      // 修改物流委托
      EditLogisticsDelegation: () =>
        import('./components/editLogisticsDelegation')
    },
    data() {
      return {
        // 面包屑
        breadcrumb: [
          { path: '', meta: { title: '销售管理' } },
          {
            path: '/sales/PreDeclarationList',
            meta: { title: '销售预申报单列表' }
          },
          {
            path: '/sales/PreDeclarationDetail',
            meta: { title: '销售预申报单详情' }
          }
        ],
        // 详情数据
        detail: {},
        // 进度
        lineArr: [
          { text: '已接单' },
          { text: '完成打托' },
          { text: '提交一线清关资料' },
          { text: '审核一线清关资料' },
          { text: '已推接口' },
          { text: '完成申报' },
          { text: '预约物流' },
          { text: '已出库' },
          { text: '部分上架' },
          { text: '已上架' }
        ],
        // 表格列数据
        tableColumn: [
          {
            label: '序号',
            prop: 'seq',
            width: '55',
            align: 'center',
            hide: true
          },
          {
            label: '销售订单号',
            prop: 'saleOrderCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: 'po号',
            prop: 'poNo',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '条形码',
            prop: 'barcode',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品编码',
            prop: 'goodsCode',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '申报数量',
            prop: 'num',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '销售单价',
            prop: 'salePrice',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '申报单价',
            prop: 'price',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '申报总金额（RMB）',
            prop: 'amount',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '品牌类型',
            prop: 'brandName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '毛重',
            prop: 'grossWeightSum',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '净重',
            prop: 'netWeightSum',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '箱数',
            prop: 'boxNum',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '箱号',
            prop: 'boxNo',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '托盘号',
            prop: 'torrNo',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '成分占比',
            prop: 'constituentRatio',
            width: '150',
            align: 'center',
            hide: true
          }
        ],
        // 功能弹窗状态
        all: {
          title: '',
          type: '',
          visible: { show: false }
        },
        // 清关资料导出确认状态
        exportConfirm: {
          targetData: {},
          visible: { show: false }
        },
        // 表单数据
        ruleForm: {
          pallteizeDate: '',
          customsConfirmDate: '',
          confirmDeclarationDate: '',
          logisticsCommissionDate: ''
        },
        // 选择上架单组件状态
        choosePutOnShelve: {
          data: [],
          visible: { show: false }
        },
        // 上传的文件名称
        fileName: '',
        // 保存按钮的loading状态
        saveLoading: false,
        // 附件上传url
        action: '',
        // 日期控件配置
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now()
          }
        },
        // 修改物流委托信息
        editLogisticsDelegation: {
          visible: { show: false }
        }
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        try {
          const { data } = await searchDetail({ id })
          this.detail = data
          this.tableData.list = this.detail.itemList || []
          // 上传附件url
          this.action =
            getBaseUrl(attachmentUploadFiles) +
            attachmentUploadFiles +
            '?token=' +
            sessionStorage.getItem('token') +
            '&code=' +
            this.detail.code
          // 流程时间
          const {
            createDate,
            pushOutDate,
            pallteizeDate,
            exportPallteizeDate,
            customsSubmitDate,
            customsConfirmDate,
            confirmDeclarationDate,
            logisticsCommissionDate,
            deliverDate,
            firstShelfDate,
            endShelfDate
          } = this.detail
          // 物流轨迹
          this.lineArr = [
            {
              text: '已接单',
              date: createDate,
              class: createDate ? 'clr-act' : ''
            },
            {
              text: '导出打托资料',
              date: exportPallteizeDate,
              class: exportPallteizeDate ? 'clr-act' : ''
            },
            {
              text: '完成打托',
              date: pallteizeDate,
              class: pallteizeDate ? 'clr-act' : ''
            },
            {
              text: '提交一线清关资料',
              date: customsSubmitDate,
              class: customsSubmitDate ? 'clr-act' : ''
            },
            {
              text: '审核一线清关资料',
              date: customsConfirmDate,
              class: customsConfirmDate ? 'clr-act' : ''
            },
            {
              text: '已推接口',
              date: pushOutDate,
              class: pushOutDate ? 'clr-act' : ''
            },
            {
              text: '完成申报',
              date: confirmDeclarationDate,
              class: confirmDeclarationDate ? 'clr-act' : ''
            },
            {
              text: '预约物流',
              date: logisticsCommissionDate,
              class: logisticsCommissionDate ? 'clr-act' : ''
            },
            {
              text: '已出库',
              date: deliverDate,
              class: deliverDate ? 'clr-act' : ''
            },
            {
              text: '部分上架',
              date: firstShelfDate,
              class: firstShelfDate ? 'clr-act' : ''
            },
            {
              text: '已上架',
              date: endShelfDate,
              class: endShelfDate ? 'clr-act' : ''
            }
          ]
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 推送出库指令
      async pushOut() {
        // if (!this.detail.customsConfirmDate) {
        //   this.$errorMsg('请先确认清关资料')
        //   return false
        // }
        const { id } = this.$route.query
        try {
          await modifyPushOutOrder({ id })
          this.$successMsg('推送出库指令成功')
          this.init()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 显示弹窗
      showModal(type) {
        const { id } = this.$route.query
        const { outDepotType, confirmDeclarationDate } = this.detail
        switch (type) {
          case 'outWarehouse':
            this.all.type = 'outWarehouse'
            this.all.title = '确认打托'
            this.ruleForm.pallteizeDate = this.$formatDate(
              new Date(),
              'yyyy-MM-dd HH:mm:ss'
            )
            break
          case 'customsConfirm':
            this.all.type = 'customsConfirm'
            this.all.title = '确认清关资料'
            this.ruleForm.customsConfirmDate = this.$formatDate(
              new Date(),
              'yyyy-MM-dd HH:mm:ss'
            )
            break
          case 'confirmDeclaration':
            this.all.type = 'confirmDeclaration'
            this.all.title = '确认申报时间'
            this.ruleForm.confirmDeclarationDate = this.$formatDate(
              new Date(),
              'yyyy-MM-dd HH:mm:ss'
            )
            break
          case 'logisticsCommission':
            if (outDepotType !== 'd' && !confirmDeclarationDate) {
              this.$errorMsg('请先确认申报完成时间！')
              return false
            }
            if (this.detail.logisticsCommissionDate) {
              this.$exportFile(exportLogisticsDelegationUrl, {
                id,
                orderTime: this.detail.logisticsCommissionDate
              })
              this.init()
              return false
            }
            this.ruleForm.logisticsCommissionDate = this.$formatDate(
              new Date(),
              'yyyy-MM-dd HH:mm:ss'
            )
            this.all.type = 'logisticsCommission'
            this.all.title = '选择导出物流委托时间'
            break
        }
        this.all.visible.show = true
        this.fileName = ''
      },
      // 关闭弹窗
      closeModal() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const type = this.all.type
            const { id } = this.$route.query
            const {
              pallteizeDate,
              customsConfirmDate,
              confirmDeclarationDate,
              logisticsCommissionDate
            } = this.ruleForm
            try {
              switch (type) {
                case 'outWarehouse':
                  if (!this.fileName) {
                    this.$errorMsg('请上传附件')
                    return false
                  }
                  await updateTimeTrace({
                    id,
                    orderTime: pallteizeDate,
                    operate: 2
                  })
                  break
                case 'customsConfirm':
                  if (!this.fileName) {
                    this.$errorMsg('请上传附件')
                    return false
                  }
                  await updateTimeTrace({
                    id,
                    orderTime: customsConfirmDate,
                    operate: 4
                  })
                  break
                case 'confirmDeclaration':
                  await updateTimeTrace({
                    id,
                    orderTime: confirmDeclarationDate,
                    operate: 5
                  })
                  break
                case 'logisticsCommission':
                  await updateTimeTrace({
                    id,
                    orderTime: logisticsCommissionDate,
                    operate: 6
                  })
                  this.$exportFile(exportLogisticsDelegationUrl, {
                    id,
                    orderTime: logisticsCommissionDate
                  })
                  this.init()
                  break
              }
              this.all.visible.show = false
              this.all.type = ''
              this.all.title = ''
              this.init()
            } catch (error) {
              this.$errorMsg(error.message)
            }
          }
        })
      },
      // 显示修改物流弹窗
      showEditLogisticsDelegationModal() {
        this.editLogisticsDelegation.visible.show = true
      },
      // 关闭修改物流弹窗
      closeEditLogisticsDelegationModal() {
        this.editLogisticsDelegation.visible.show = false
        this.init()
      },
      // 跳转到上架页面
      async jumpPutOnSale() {
        const { id: saleDeclareOrderId } = this.$route.query
        const { outDepotType, confirmDeclarationDate } = this.detail
        if (outDepotType !== 'd' && !confirmDeclarationDate) {
          this.$errorMsg('请先确认申报完成时间！')
          return false
        }
        try {
          const { data: list } = await getSaleOutDepotList({
            saleDeclareOrderId
          })
          if (list.length === 1) {
            this.linkTo(
              `/sales/goodsonsale?id=${list[0].saleOrderId}&saleOutCode=${list[0].code}&saleOutId=${list[0].id}`
            )
            return false
          }
          this.choosePutOnShelve.visible.show = true
          this.choosePutOnShelve.data = list
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 跳转到打托出库页面
      jumpPutOut() {
        const { outDepotType, confirmDeclarationDate } = this.detail
        if (outDepotType !== 'd' && !confirmDeclarationDate) {
          this.$errorMsg('请先确认申报完成时间！')
          return false
        }
        this.linkTo(`/sales/DeclareOrderIssue?id=${this.detail.id}`)
      },
      // 导出清关资料
      async exportCustomsDeclare() {
        const detail = this.detail
        const json = {
          outDepotId: detail.outDepotId || '',
          inDepotId: detail.inDepotId || '',
          outCustomsId: detail.outCustomsId || '',
          inCustomsId: detail.inCustomsId || '',
          isSameArea: detail.isSameArea || ''
        }
        try {
          const res = await getExportTemplate({ json: JSON.stringify(json) })
          if (res.data.code === '01') {
            // 没有找到关联模板
            this.$alertWarning('该仓库暂无配置清关资料模板，请先完成模板配置！')
          } else {
            // 弹出选择模板
            const { id } = this.$route.query
            this.exportConfirm.targetData = res.data
            this.exportConfirm.targetData.id = id || ''
            if (this.detail.customsSubmitDate) {
              this.exportConfirm.targetData.customsSubmitDate =
                this.detail.customsSubmitDate
            }
            this.exportConfirm.visible.show = true
          }
          // const exportParams = {}
          // if (res.data.code === '00') { // 直接导出
          //   if (res.data.inList && res.data.inList.length > 0) {
          //     exportParams.inFileTempIds = res.data.inList[0].fileTempId + ''
          //   }
          //   if (res.data.outList && res.data.outList.length > 0) {
          //     exportParams.outFileTempIds = res.data.outList[0].fileTempId + ''
          //   }
          //   this.handleExportConfirm(exportParams)
          // } else if (res.data.code === '01') { // 没有找到关联模板
          //   this.$alertWarning('该仓库暂无配置清关资料模板，请先完成模板配置！')
          // } else { // 弹出选择模板
          //   const { id } = this.$route.query
          //   this.exportConfirm.targetData = res.data
          //   this.exportConfirm.targetData.id = id || ''
          //   if (this.detail.customsSubmitDate) {
          //     this.exportConfirm.targetData['customsSubmitDate'] = this.detail.customsSubmitDate
          //   }
          //   this.exportConfirm.visible.show = true
          // }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 导出打托资料
      exportPallteize() {
        const { id } = this.$route.query
        this.$exportFile(exportPallteizeDataUrl, { id })
        setTimeout(() => {
          this.init()
        }, 300)
      },
      // 选择模板后导出清关资料
      handleExportConfirm(data) {
        this.exportConfirm.visible.show = false
        const { id } = this.$route.query
        const { inFileTemp = [], outFileTemp = [], orderTime = '' } = data
        const inFileTempIds = inFileTemp
          .map((item) => item.fileTempId)
          .toString()
        const outFileTempIds = outFileTemp
          .map((item) => item.fileTempId)
          .toString()
        this.$exportFile(exportCustomsDeclareUrl, {
          id,
          inFileTempIds,
          outFileTempIds,
          orderTime
        })
        this.init()
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
    width: 170px;
    height: 100px;
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
    width: 130px;
  }
</style>
