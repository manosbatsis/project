<template>
  <div class="page-bx bgc-w edit-bx yushen-edit-bx">
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 面包屑 -->
      <JFX-Breadcrumb :showGoback="!showBackbtn" />
      <!-- 面包屑 end -->
      <!-- title -->
      <JFX-title title="基本信息" className="mr-t-10 title-bx ">
        <div>
          <el-button type="text" :size="'small'" @click="baseOpen = !baseOpen">
            {{ baseOpen ? '收起' : '展开' }}
          </el-button>
        </div>
      </JFX-title>
      <el-row
        :gutter="10"
        class="fs-12 clr-II"
        :class="baseOpen ? ' open' : 'dedit-row close'"
      >
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="预申报单号：" prop="code">
            <el-input
              v-model.trim="ruleForm.code"
              disabled
              :title="ruleForm.code"
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="供应商：" prop="supplierName">
            <el-input
              v-model.trim="ruleForm.supplierName"
              disabled
              style="width: 100%"
              :title="ruleForm.supplierName"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="PO号：" prop="poNo">
            <el-input
              v-model.trim="ruleForm.poNo"
              type="textarea"
              clearable
              style="width: 100%"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="入仓仓库：" prop="depotId">
            <el-select
              v-model="ruleForm.depotId"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getSelectBeanByMerchantRel('cangkuList')"
              @change="changeDepot"
            >
              <el-option
                v-for="item in selectOpt.cangkuList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="事业部：" prop="buId">
            <el-select
              v-model="ruleForm.buId"
              disabled
              style="width: 100%"
              placeholder="请选择"
              clearable
              :data-list="getBUSelectBean('buList')"
            >
              <el-option
                v-for="item in selectOpt.buList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购订单号：" prop="purchaseCode">
            <el-input
              v-model.trim="ruleForm.purchaseCode"
              type="textarea"
              clearable
              style="width: 100%"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="LBX单号：" prop="lbxNo">
            <el-input
              v-model.trim="ruleForm.lbxNo"
              clearable
              style="width: 100%"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="业务模式：" prop="businessModel">
            <el-radio-group v-model="ruleForm.businessModel">
              <el-radio label="1" disabled>购销</el-radio>
              <el-radio label="2" disabled>代销</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="12">
          <el-form-item label="贸易条款：" prop="tradeTerms">
            <el-radio-group
              v-model="ruleForm.tradeTerms"
              :data-list="getSelectList('declareorder_tradeTermsList')"
            >
              <el-radio
                v-for="item in selectOpt.declareorder_tradeTermsList"
                :key="item.key"
                :label="item.key"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="12">
          <el-form-item label="付款条款：" prop="paymentProvision">
            <el-radio-group v-model="ruleForm.paymentProvision">
              <el-radio label="1">一次付款-付款发货</el-radio>
              <el-radio label="2">多次付款</el-radio>
              <el-radio label="3">一次付款-发货付款</el-radio>
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
          <el-form-item label="海外理货单位：" prop="tallyingUnit">
            <el-radio-group v-model="ruleForm.tallyingUnit">
              <el-radio
                v-for="item in tallyingUnitList"
                :key="item.key"
                :label="item.key"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <JFX-title title="地点及计划" className="mr-t-20 title-bx">
        <div>
          <el-button
            type="text"
            :size="'small'"
            @click="addressOpen = !addressOpen"
          >
            {{ addressOpen ? '收起' : '展开' }}
          </el-button>
        </div>
      </JFX-title>
      <el-row
        :gutter="10"
        class="fs-12 clr-II"
        :class="addressOpen ? ' open' : 'dedit-row close'"
      >
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="装货港：" prop="portLoading">
            <el-input
              v-model.trim="ruleForm.portLoading"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6" >
            <el-form-item label="目的地名称：" prop="destinationName" >
              <el-input v-model.trim="ruleForm.destinationName" clearable style="width: 100%"></el-input>
            </el-form-item>
          </el-col> -->
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="目的港名称：" prop="destinationPortName">
            <el-input
              v-model.trim="ruleForm.destinationPortName"
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
              :clearable="false"
              disabled
            >
              <el-option
                v-for="item in unloadPortList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
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
          <el-form-item label="进出口口岸：" prop="imExpPort">
            <el-input
              v-model.trim="ruleForm.imExpPort"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col
          class="mr-b-0"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
          v-if="ruleForm.depotId"
        >
          <el-form-item label="入库关区：" prop="inCustomsId">
            <el-select
              v-model="ruleForm.inCustomsId"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="
                getSelectCustomsArea('guanList', { depotId: ruleForm.depotId })
              "
            >
              <el-option
                v-for="item in selectOpt.guanList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="预计离港时间：" prop="estimatedDeliveryDate">
            <el-date-picker
              v-model="ruleForm.estimatedDeliveryDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期时间"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <!-- <el-col v-if="false" class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6" >
            <el-form-item label="预计装船时间：" prop="plannedShipDateStr" >
              <el-date-picker
                v-model="ruleForm.plannedShipDateStr"
                value-format="yyyy-MM-dd"
                type="date"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col> -->
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="备注：" prop="remark">
            <el-input
              v-model.trim="ruleForm.remark"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="装船时间：" prop="shipDateStr">
            <el-date-picker
              v-model="ruleForm.shipDateStr"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择装船时间"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <JFX-title title="物流信息" className="mr-t-20 title-bx">
        <div>
          <el-button
            type="text"
            :size="'small'"
            @click="logiscOpen = !logiscOpen"
          >
            {{ logiscOpen ? '收起' : '展开' }}
          </el-button>
        </div>
      </JFX-title>
      <el-row
        :gutter="10"
        class="fs-12 clr-II"
        :class="logiscOpen ? ' open' : 'dedit-row close'"
      >
        <el-col class="mr-b-0" :span="12">
          <el-form-item label="国际运输方式：" prop="transport">
            <el-radio-group
              v-model="ruleForm.transport"
              :data-list="getSelectList('countryTransportList')"
            >
              <el-radio
                v-for="item in selectOpt.countryTransportList"
                :key="item.key"
                :label="item.key"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="12">
          <el-form-item label="陆运方式：" prop="landTransport">
            <el-radio-group
              v-model="ruleForm.landTransport"
              :data-list="getSelectList('landTransportList')"
            >
              <el-radio
                v-for="item in selectOpt.landTransportList"
                :key="item.key"
                :label="item.key"
              >
                {{ item.value }}
              </el-radio>
            </el-radio-group>
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
          <el-form-item label="承运商：" prop="carrier">
            <el-input
              v-model.trim="ruleForm.carrier"
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
          <el-form-item label="头程提单号：" prop="ladingBill">
            <el-input
              v-model.trim="ruleForm.ladingBill"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="二程提单号：" prop="blNo">
            <el-input
              v-model.trim="ruleForm.blNo"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="车次：" prop="trainNumber">
            <el-input
              v-model.trim="ruleForm.trainNumber"
              clearable
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="托数：" prop="torrNum">
            <el-input
              v-model.trim="ruleForm.torrNum"
              clearable
              style="width: 100%"
              @change="changePackType"
            ></el-input>
            <!-- <el-input-number v-model.trim="ruleForm.torrNum" :precision="0" :controls="false" :min="0" style="width: 100%;" ></el-input-number> -->
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="箱数：" prop="boxNum">
            <el-input
              v-model.trim="ruleForm.boxNum"
              clearable
              style="width: 100%"
              @change="changePackType"
            ></el-input>
            <!-- <el-input-number v-model.trim="ruleForm.boxNum" :precision="0" :controls="false" :min="0" style="width: 100%;" @change="changePackType"></el-input-number> -->
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="包装方式：" prop="packType">
            <el-input
              v-model.trim="ruleForm.packType"
              clearable
              style="width: 100%"
              placeholder="XX箱/xx托XXX"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :span="12">
          <el-form-item label="托盘材质：" prop="palletMaterial">
            <el-radio-group
              v-model="ruleForm.palletMaterial"
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
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="提单毛重(kg)：" prop="billWeight">
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
          <el-form-item label="车型及数量：" prop="motorcycleType">
            <el-input
              v-model.trim="ruleForm.motorcycleType"
              clearable
              style="width: 100%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="标准箱TEU：" prop="standardCaseTeu">
            <el-input
              v-model.trim="ruleForm.standardCaseTeu"
              clearable
              style="width: 100%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <JFX-title title="物流委托书" className="mr-t-10 title-bx">
        <div>
          <el-button
            type="text"
            :size="'small'"
            @click="weituoOpen = !weituoOpen"
          >
            {{ weituoOpen ? '收起' : '展开' }}
          </el-button>
        </div>
      </JFX-title>
      <el-row
        :gutter="10"
        class="fs-12 clr-II"
        :class="weituoOpen ? ' open' : 'dedit-row close'"
      >
        <el-col class="mr-b-10" :span="24">
          <el-form-item label="选择模板：" prop="logisticsContactTemplateLink">
            <el-select
              v-model="ruleForm.logisticsContactTemplateLink"
              style="width: 180px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateLink"
            >
              <el-option
                v-for="item in tempalteList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="flex-bx">
            <span class="fs-14 clr-II">发货人/提货信息：</span>
            <el-select
              v-model="ruleForm.shipperTempId"
              style="width: 180px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('shipperTemp')"
            >
              <el-option
                v-for="item in shipperTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              @change="detailsChange('shipperTempDetails', 'shipperTempId')"
              v-model="ruleForm.shipperTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 500px"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="flex-bx">
            <span class="fs-14 clr-II">收货人/卸货信息：</span>
            <el-select
              v-model="ruleForm.consigneeTempId"
              style="width: 180px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('consigneeTemp')"
            >
              <el-option
                v-for="item in consigneeTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              @change="detailsChange('consigneeTempDetails', 'consigneeTempId')"
              v-model="ruleForm.consigneeTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 500px"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="flex-bx">
            <span class="fs-14 clr-II">通知人：</span>
            <el-select
              v-model="ruleForm.notifierTempId"
              style="width: 180px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('notifierTemp')"
            >
              <el-option
                v-for="item in notifierTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              @change="detailsChange('notifierTempDetails', 'notifierTempId')"
              v-model="ruleForm.notifierTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 500px"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="flex-bx">
            <span class="fs-14 clr-II">对接人：</span>
            <el-select
              v-model="ruleForm.dockingTempId"
              style="width: 180px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('dockingTemp')"
            >
              <el-option
                v-for="item in dockingTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              @change="detailsChange('dockingTempDetails', 'dockingTempId')"
              v-model="ruleForm.dockingTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 500px"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-20" :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="flex-bx">
            <span class="fs-14 clr-II">承运商信息：</span>
            <el-select
              v-model="ruleForm.carrierInformationTempId"
              style="width: 180px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('carrierInformationTemp')"
            >
              <el-option
                v-for="item in carrierInformationTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              @change="
                detailsChange(
                  'carrierInformationTempDetails',
                  'carrierInformationTempId'
                )
              "
              v-model="ruleForm.carrierInformationTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 500px"
            ></el-input>
          </div>
        </el-col>
      </el-row>
      <!-- 商品信息 和 装箱明细 -->
      <div class="diy-el-tabs-wrap">
        <div class="el-tabs-title-btnText" v-if="tabStatus === '0'">
          <el-button type="primary" :size="'small'" @click="openChoseProducts">
            选择商品
          </el-button>
          <el-button type="primary" :size="'small'" @click="dele">
            删除
          </el-button>
          <el-button
            type="primary"
            :size="'small'"
            @click="handleClickImportPacking"
            v-if="($route.query.id || $route.query.purchaseIds) && $route.query.isCopy !== '1'"
          >
            导入装箱明细
          </el-button>
        </div>
        <el-tabs v-model="tabStatus">
          <el-tab-pane label="商品信息" name="0">
            <!-- 表格 -->
            <JFX-table
              :tableData.sync="tableData"
              @selection-change="selectionChange"
              :showPagination="false"
              :showSummary="true"
              :summaryMethod="getSummary"
            >
              <el-table-column
                type="selection"
                width="55"
                align="center"
              ></el-table-column>
              <el-table-column
                label="序号"
                type="index"
                align="center"
                width="55"
              >
              </el-table-column>
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
              <el-table-column
                label="合同号"
                align="center"
                min-width="120"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <el-input
                    v-model.trim="scope.row.contractNo"
                    style="width: 100%"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="商品货号" align="center" min-width="140">
                <template slot-scope="scope">
                  <template v-if="scope.row.goodsNo">
                    {{ scope.row.goodsNo }}
                    <br />
                    <el-button
                      type="primary"
                      @click="changeGoodNo(scope.$index)"
                    >
                      修改货号
                    </el-button>
                  </template>
                  <span v-else style="color: red">未关联仓库</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="barcode"
                label="商品条形码"
                align="center"
                min-width="140"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="goodsName"
                label="商品名称"
                align="center"
                min-width="140"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                label="商品编码"
                align="center"
                min-width="140"
                show-overflow-tooltip
              >
                <template slot-scope="scope">{{
                  scope.row.goodsCode
                }}</template>
              </el-table-column>
              <el-table-column prop="num" align="center" min-width="100">
                <template slot="header">
                  <span class="need">申报数量</span>
                </template>
                <template slot-scope="scope">
                  <el-input-number
                    v-model.trim="scope.row.num"
                    :precision="0"
                    :controls="false"
                    :min="0"
                    style="width: 100%"
                    @blur="count('num', scope.$index)"
                  ></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="采购单价" align="center" min-width="100">
                <template slot-scope="scope">{{
                  scope.row.purchasePrice
                }}</template>
              </el-table-column>
              <el-table-column align="center" min-width="130">
                <template slot="header">
                  <span class="need">申报单价</span>
                </template>
                <template slot-scope="scope">
                  <el-input-number
                    v-model.trim="scope.row.price"
                    :precision="2"
                    v-max-spot="2"
                    :controls="false"
                    :min="0"
                    label="必填"
                    style="width: 100%"
                    @blur="count('price', scope.$index)"
                  ></el-input-number>
                </template>
              </el-table-column>
              <el-table-column
                prop="goodsAmount"
                label="申报总金额（RMB）"
                align="center"
                min-width="150"
              >
                <template slot-scope="scope">
                  <el-input-number
                    v-model.trim="scope.row.goodsAmount"
                    :precision="2"
                    v-max-spot="2"
                    :controls="false"
                    :min="0"
                    label="必填"
                    style="width: 100%"
                    @blur="count('goodsAmount', scope.$index)"
                  ></el-input-number>
                </template>
              </el-table-column>
              <el-table-column prop="brandName" align="center" min-width="130">
                <template slot="header">
                  <span>品牌类型</span>
                </template>
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.brandName"
                    clearable
                    style="width: 100%"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column
                prop="grossWeightSum"
                align="center"
                min-width="100"
              >
                <template slot="header">
                  <span class="need">毛重(kg)</span>
                </template>
                <template slot-scope="scope">
                  <JFX-Input
                    v-model.trim="scope.row.grossWeightSum"
                    :min="0"
                    :precision="5"
                    placeholder="请输入"
                    style="width: 100%"
                    @change="countSumWeight"
                  />
                </template>
              </el-table-column>
              <el-table-column
                prop="netWeightSum"
                align="center"
                min-width="100"
              >
                <template slot="header">
                  <span class="need">净重(kg)</span>
                </template>
                <template slot-scope="scope">
                  <JFX-Input
                    v-model.trim="scope.row.netWeightSum"
                    :min="0"
                    :precision="5"
                    placeholder="请输入"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" min-width="150">
                <template slot="header">
                  <span>成分占比</span>
                </template>
                <template slot-scope="scope">
                  <el-input
                    type="textarea"
                    :rows="2"
                    v-model="scope.row.constituentRatio"
                    :precision="2"
                    :controls="false"
                    :min="0"
                    style="width: 100%"
                  ></el-input>
                </template>
              </el-table-column>
            </JFX-table>
            <!-- 表格 end-->
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
      </div>

      <JFX-title title="附件列表" className="mr-t-20" />
      <div class="mr-t-15 mr-b-15 fs-14 clr-II">
        <JFX-upload
          @success="successUploadFujian"
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
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="saveForm" :loading="saveLoading">
        保 存
      </el-button>
      <el-button id="pur_cancle_btn" @click="closeTag" v-if="!showBackbtn">
        取 消
      </el-button>
    </div>
    <!-- 选择商品 -->
    <PreDeclareChooseGoods
      v-if="visible.show"
      :isVisible="visible"
      @comfirm="comfirmChooseGoods"
      :filterData="filterData"
    />
    <!-- 选择商品 end -->
    <!-- 选择货号 -->
    <chose-goods
      v-if="changeGoodnovisible.show"
      :isVisible.sync="changeGoodnovisible"
      @comfirm="comfirmGoodno"
      :data="changeGoodnoFilterData"
    ></chose-goods>
    <!-- 选择货号 end -->
    <!-- 导入 -->
    <div>
      <JFX-Dialog
        :visible.sync="dialogVisible"
        :showClose="true"
        @comfirm="dialogVisible.show = false"
        :width="'860px'"
        title="导入装箱明细"
        :top="'3vh'"
        closeBtnText="取 消"
        confirmBtnText="确 认"
      >
        <div class="import-bx">
          <!-- 导入装箱明细资料 -->
          <importfile
            :url="importPackingDetailsUrl"
            :filterData="importPackingData"
            :templateId="'169'"
            @successUpload="packingImportSuccessUpload"
          ></importfile>
        </div>
      </JFX-Dialog>
    </div>
    <!-- 导入 end -->
    <div class="hide-form" v-if="downVal">
      <form id="down-up" :action="actionUrl" method="post" target="_blank">
        <input type="hidden" name="json" v-model="downVal" />
      </form>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getDeclareDetail,
    modifyDeclare,
    importPackingDetailsUrl,
    listTemplateLink,
    listTemplate,
    generateDeclareOrder,
    getDeclareOrderCopyById
  } from '@a/purchaseManage/index'
  import { getDepotDetails } from '@a/salesManage/index'
  import { getBaseUrl } from '@u/tool'
  import {
    attachmentUploadFiles,
    getOrderUpdateMerchandiseInfo
  } from '@a/base/index'
  export default {
    mixins: [commomMix],
    components: {
      /* 选择商品 */
      PreDeclareChooseGoods: () => import('./components/PreDeclareChooseGoods'),
      /* 导入 */
      importfile: () => import('@/components/importfile/index'),
      /* 上传清关资料 */
      enclosureList: () => import('@c/enclosureList/index'),
      // 修改货号
      choseGoods: () => import('@c/choseGoods/index')
    },
    data() {
      return {
        ruleForm: {
          id: '',
          status: '',
          code: '',
          supplierName: '',
          supplierId: '',
          ladingBill: '',
          invoiceNo: '',
          contractNo: '',
          carrier: '',
          poNo: '',
          transport: '',
          depotId: '',
          buId: '',
          deliveryAddr: '',
          boxNum: '',
          billWeight: '',
          destinationPortName: '',
          paySubject: '',
          portLoading: '',
          portDestNo: '44011501',
          lbxNo: '',
          packType: '',
          payRules: '',
          blNo: '',
          estimatedDeliveryDate: '',
          plannedShipDateStr: '',
          imExpPort: '',
          shipper: '',
          mark: '',
          // destinationName: '',
          remark: '',
          businessModel: 1,
          tallyingUnit: '',
          palletMaterial: '',
          torrNum: '',
          inCustomsId: '',
          tradeTerms: '',
          paymentProvision: '',
          motorcycleType: '',
          standardCaseTeu: '',
          trainNumber: '',
          purchaseCode: '',
          landTransport: '',
          shipDateStr: '',
          // 物流委托书
          logisticsContactTemplateLink: '',
          logisticsContactTemplateLinkName: '',
          shipperTempId: '',
          shipperTempName: '',
          shipperTempDetails: '',
          consigneeTempId: '',
          consigneeTempName: '',
          consigneeTempDetails: '',
          notifierTempId: '',
          notifierTempName: '',
          notifierTempDetails: '',
          dockingTempId: '',
          dockingTempName: '',
          dockingTempDetails: '',
          carrierInformationTempId: '',
          carrierInformationTempName: '',
          carrierInformationTempDetails: ''
        },
        rules: {
          code: {
            required: true,
            message: '请输入预申报单号',
            trigger: 'blur'
          },
          supplierName: {
            required: true,
            message: '请输入供应商',
            trigger: 'blur'
          },
          ladingBill: {
            required: true,
            message: '请输入头程提单号',
            trigger: 'blur'
          },
          contractNo: {
            required: true,
            message: '请输入合同号',
            trigger: 'blur'
          },
          poNo: { required: true, message: '请输入po号', trigger: 'blur' },
          depotId: {
            required: true,
            message: '入仓仓库不能为空',
            trigger: 'change'
          },
          buId: {
            required: true,
            message: '事业部不能为空',
            trigger: 'change'
          },
          invoiceNo: {
            required: true,
            message: '发票号不能为空',
            trigger: 'blur'
          },
          deliveryAddr: {
            required: true,
            message: '收货地点不能为空',
            trigger: 'blur'
          },
          boxNum: { required: true, message: '箱数不能为空', trigger: 'blur' },
          portLoading: {
            required: true,
            message: '装货港不能为空',
            trigger: 'blur'
          },
          packType: {
            required: true,
            message: '包装方式不能为空',
            trigger: 'change'
          },
          payRules: {
            required: false,
            message: '付款条约不能为空',
            trigger: 'blur'
          },
          portDestNo: {
            required: true,
            message: '卸货港不能为空',
            trigger: 'change'
          },
          shipper: {
            required: true,
            message: '境外发货人不能为空',
            trigger: 'blur'
          },
          mark: { required: true, message: '唛头不能为空', trigger: 'blur' },
          palletMaterial: {
            required: true,
            message: '托盘材质不能为空',
            trigger: 'change'
          },
          transport: {
            required: true,
            message: '运输方式不能为空',
            trigger: 'change'
          },
          torrNum: { required: true, message: '托数不能为空', trigger: 'blur' },
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
          billWeight: {
            required: true,
            message: '提单毛重不能为空',
            trigger: 'change'
          },
          destinationPortName: {
            required: true,
            message: '目的港名称不能为空',
            trigger: 'change'
          },
          landTransport: {
            required: true,
            message: '陆运方式不能为空',
            trigger: 'change'
          },
          tallyingUnit: {
            required: true,
            message: '理货单位不能为空',
            trigger: 'change'
          }
        },
        tabStatus: 0,
        visible: { show: false },
        date: '',
        num: '',
        detail: {},
        unloadPortList: [
          { value: '44011501：南沙新港口岸', key: '44011501' },
          { value: '44010318：黄埔广保通码头口岸', key: '44010318' },
          { value: '21021001：大连保税区口岸', key: '21021001' },
          { value: '50010001：重庆口岸', key: '50010001' }
        ],
        filterData: {},
        dialogVisible: { show: false },
        actionUrl: '',
        downVal: '',
        importPackingDetailsUrl: importPackingDetailsUrl,
        saveLoading: false,
        showBackbtn: false,
        tallyingUnitList: [
          { key: '00', value: '托盘' },
          { key: '01', value: '箱' },
          { key: '02', value: '件' }
        ],
        baseOpen: true,
        addressOpen: true,
        logiscOpen: true,
        weituoOpen: true,
        action: '',
        tempalteList: [],
        //  1-发货人/提货信息 2-收货人/卸货信息 3-通知人 4-对接人 5-承运商信息
        shipperTempList: [],
        consigneeTempList: [],
        notifierTempList: [],
        dockingTempList: [],
        carrierInformationTempList: [],
        orderIds: '', // 用于选择商品传参、初始化时拼接表格的采购id
        goodsDelId: 0, // 用于删除商品
        // 改变货号弹框显示
        changeGoodnovisible: {
          show: false
        },
        // 改变货号传参
        changeGoodnoFilterData: {},
        /* 仓库类型 */
        depotType: '',
        // 导入箱装明细参数
        importPackingData: {},
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
      const { other } = this.$route.query
      this.showBackbtn = other === 'gh'
      this.getList() // 获取模板列表
      this.init()
    },
    methods: {
      /* 确认选择商品 */
      comfirmChooseGoods(data) {
        if (data && data.length) {
          const list = data.map((item) => ({
            ...item,
            goodsDelId: this.goodsDelId++
          }))
          this.tableData.list.push(...list)
          this.$nextTick(() => {
            this.countSumWeight()
            this.tableData.list.map((_, index) => this.count('price', index))
          })
        }
        this.visible.show = false
      },
      /* 仓库改变 */
      async changeDepot(id) {
        /* 记录仓库类型 */
        const {
          data: { type }
        } = await getDepotDetails({ id })
        this.depotType = type || ''
        // 数组没数据跳出
        if (!this.tableData.list.length) {
          return
        }
        // 获取仓库切换，商品信息切换
        const { data: updateGoods } = await getOrderUpdateMerchandiseInfo({
          depotId: this.ruleForm.depotId,
          itemList: this.tableData.list.map((item, index) => {
            return {
              barcode: item.barcode,
              index
            }
          }),
          orderType: 1
        })
        this.tableData.list.forEach((rowData, index) => {
          if (updateGoods[index].merchandiseInfoModel) {
            const updateData = updateGoods[index].merchandiseInfoModel
            const param = [
              'goodsId_id',
              'goodsName_name',
              'goodsSpec_spec',
              'constituentRatio_component',
              'goodsNo',
              'goodsCode',
              'grossWeight',
              'netWeight'
            ]
            // 更新相关信息
            for (const key of param) {
              if (key.includes('_')) {
                const [key1, key2] = key.split('_')
                rowData[key1] = updateData[key2]
              } else {
                rowData[key] = updateData[key]
              }
            }
            // 重新计算
            this.count('num', index)
          }
        })
      },
      // 修改货号
      changeGoodNo(index) {
        const { barcode } = this.tableData.list[index]
        this.changeGoodnoFilterData = {
          barcode,
          index,
          depotId: this.ruleForm.depotId
        }
        this.changeGoodnovisible.show = true
      },
      // 修改货号完成
      comfirmGoodno(list) {
        this.changeGoodnovisible.show = false
        const updateData = list[0]
        const index = this.changeGoodnoFilterData.index
        const rowData = this.tableData.list[index]
        console.log(updateData, rowData)
        const param = [
          'goodsId_id',
          'goodsName_name',
          'goodsSpec_spec',
          'constituentRatio_component',
          'goodsNo',
          'goodsCode',
          'grossWeight',
          'netWeight'
        ]
        // 更新相关信息
        for (const key of param) {
          if (key.includes('_')) {
            const [key1, key2] = key.split('_')
            rowData[key1] = updateData[key2]
          } else {
            rowData[key] = updateData[key]
          }
        }
        // 重新计算
        this.count('num', index)
      },
      async init() {
        const { query } = this.$route
        if (!query.id && !query.purchaseIds) return false
        try {
          let res = {}
          // query.purchaseIds 存在为新增 其他为编辑 编辑
          if (query.purchaseIds) {
            // 采购列表生成预申报单跳转 获取初始信息
            res = await generateDeclareOrder({ ids: query.purchaseIds }) // query.purchaseIds 为采购订单 ids
          } else {
            // 预申报单编辑 获取初始信息
            res = query.isCopy
              ? await getDeclareOrderCopyById({ id: query.id })
              : await getDeclareDetail({ id: query.id })
          }
          const targetData = res.data || {}
          this.detail = targetData
          this.tableData.list = targetData.itemList.map((item) => ({
            ...item,
            goodsDelId: this.goodsDelId++
          }))
          this.packingTableData.list =
            targetData?.packingList?.map((item, index) => {
              return {
                index: index + 1,
                ...item
              }
            }) || []
          this.orderIds = this.tableData.list
            .map((item) => item.purchaseId)
            .toString()
          this.tableData.list.map((_, index) => this.count('price', index))
          for (const key in this.ruleForm) {
            this.ruleForm[key] =
              targetData[key] !== undefined && targetData[key] !== null
                ? targetData[key] + ''
                : ''
          }
          // 转字符串
          this.ruleForm.transport = this.ruleForm.transport + ''
          this.ruleForm.buId = this.ruleForm.buId + ''
          this.ruleForm.depotId = this.ruleForm.depotId + ''
          this.ruleForm.inCustomsId = this.ruleForm.inCustomsId + ''
          /* 日期转换 */
          const { estimatedDeliveryDate, plannedShipDate, shipDate } =
            this.detail
          this.ruleForm.plannedShipDateStr = plannedShipDate?.slice(0, 10) || ''
          this.ruleForm.shipDateStr = shipDate?.slice(0, 10) || ''
          this.ruleForm.estimatedDeliveryDate =
            estimatedDeliveryDate?.slice(0, 10) || ''
          this.filterData = {
            code: this.detail.code
          }
          this.ruleForm.portDestNo = '44011501'
          /* 仓库类型 */
          this.depotType = this.detail?.depotType || ''
          this.action =
            getBaseUrl(attachmentUploadFiles) +
            attachmentUploadFiles +
            '?token=' +
            sessionStorage.getItem('token') +
            '&code=' +
            this.detail.code
        } catch (err) {
          console.log(err)
        }
      },
      /* 选择商品 */
      openChoseProducts() {
        const purchaseItemId = this.tableData.list
          .map((item) => item.purchaseItemId)
          .toString()
        const unNeedGoodsJson = this.tableData.list.length
          ? JSON.stringify({ purchaseItemId })
          : ''
        const { orderIds } = this
        this.filterData = { unNeedGoodsJson, orderIds }
        this.visible.show = true
      },
      dele() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        const arr = this.tableChoseList.map((item) => item.goodsDelId)
        const list = []
        this.tableData.list.forEach((item, index) => {
          if (!arr.includes(item.goodsDelId)) {
            list.push(item)
          }
        })
        this.tableData.list = list
        this.countSumWeight()
      },
      count(type, index) {
        const data = this.tableData.list[index]
        let {
          num,
          price = 0,
          goodsAmount,
          netWeight,
          grossWeight,
          grossWeightSum,
          netWeightSum
        } = data
        if (type === 'num' || type === 'price') {
          goodsAmount = num * price
          goodsAmount = goodsAmount.toFixed(3)
        } else {
          price = goodsAmount / num
          price = price.toFixed(9)
        }
        if (type === 'num') {
          num = num || 0
          grossWeight = grossWeight || 0
          netWeight = netWeight || 0
          grossWeightSum = (num * grossWeight).toFixed(5)
          netWeightSum = (num * netWeight).toFixed(5)
        }
        this.tableData.list.splice(index, 1, {
          ...data,
          goodsAmount,
          num,
          price,
          netWeightSum,
          grossWeightSum
        })
        this.countSumWeight()
      },
      // 保存数据
      saveForm() {
        // 临时的保证隐藏tag, 跟菜单栏
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请先完善信息')
            return false
          }
          if (!this.checkTableData()) return false
          this.saveLoading = true
          try {
            const items = this.tableData.list.map((item) => ({
              ...item,
              amount: item.goodsAmount || '0'
            }))
            await modifyDeclare({
              ...this.ruleForm,
              items,
              packingList: this.packingTableData.list
            })
            this.linkTo('/purchase/predeclarationList')
            this.$successMsg('保存成功!')
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.saveLoading = false
          }
        })
      },
      checkTableData() {
        let flag = true
        let totalGrossWeight = 0
        for (let i = 0; i < this.tableData.list.length; i++) {
          const {
            num,
            price,
            grossWeightSum,
            netWeightSum,
            brandName,
            goodsNo
          } = this.tableData.list[i]
          const rowTips = '表格第' + (i + 1) + '行,'
          if (!goodsNo) {
            this.$errorMsg(rowTips + '商品货号不能为空')
            flag = false
            break
          }
          if (!num || num <= 0) {
            this.$errorMsg(rowTips + '申报数量必填且不能小于0')
            flag = false
            break
          }
          if (!price || price <= 0) {
            this.$errorMsg(rowTips + '申报单价必填且不能小于0')
            flag = false
            break
          }
          if (!grossWeightSum || grossWeightSum <= 0) {
            this.$errorMsg(rowTips + '毛重必填且不能小于0')
            flag = false
            break
          }
          if (!brandName) {
            this.$errorMsg(rowTips + '品牌类型不能为空')
            flag = false
            break
          }
          if (!netWeightSum || netWeightSum <= 0) {
            this.$errorMsg(rowTips + '净重必填且不能小于0')
            flag = false
            break
          }
          if (Number(grossWeightSum) < Number(netWeightSum)) {
            this.$errorMsg(rowTips + '毛重不能小于净重')
            flag = false
            break
          }
          totalGrossWeight += Number(grossWeightSum)
        }
        if (
          flag &&
          Number(totalGrossWeight).toFixed(5).toString() !==
            Number(this.ruleForm.billWeight).toFixed(5).toString()
        ) {
          this.$errorMsg('总毛重必须等于总毛重')
          flag = false
        }
        return flag
      },
      openLay() {
        this.dialogVisible.show = false
      },
      // 导入装箱明细资料成功
      async packingImportSuccessUpload(res) {
        if (res.data.failure + '' !== '0') {
          return false
        }
        const list = res?.data?.data || []
        if (!list.length) {
          return false
        }
        this.dialogVisible.show = false
        const isExist = !!this.packingTableData.list.length
        if (isExist) {
          const isComfirm = await this.$showToast('确认覆盖已存在的箱装明细')
          if (!isComfirm) {
            return
          }
        }
        this.packingTableData.list = list.map((item, index) => {
          return {
            ...item,
            index: index + 1
          }
        })
      },
      // 计算总毛重
      countSumWeight() {
        let weight = 0
        let maxlen = 0
        this.tableData.list.map((item) => {
          const num = item.grossWeightSum ? +item.grossWeightSum : 0
          weight = weight + num
          if ((num + '').includes('.')) {
            const arr = (num + '').split('.')
            const len = arr[1].length
            if (len > maxlen) maxlen = len
          }
        })
        this.ruleForm.billWeight = maxlen > 0 ? weight.toFixed(maxlen) : weight
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
      changePackType() {
        const kk = this.selectOpt.order_torrpacktypeList.find((item) => {
          if (this.ruleForm.palletMaterial === item.key) {
            return item
          }
        })
        this.ruleForm.packType =
          (this.ruleForm.boxNum || '') +
          '箱/' +
          (this.ruleForm.torrNum || '') +
          '托' +
          (kk ? kk.value : '')
      },
      handleClickImportPacking() {
        // 判断是否存在一个调入商品
        const importPackingItemList = this.tableData.list
          .map((item) => ({
            // 商品id
            goodsId: item.goodsId,
            // 商品货号
            goodsNo: item.goodsNo,
            // 条形码
            barcode: item.barcode
          }))
          .filter((item) => item.goodsId)
        if (!importPackingItemList.length) {
          return this.$errorMsg('需存在商品才可以导入商品')
        }
        this.importPackingData = {
          itemList: JSON.stringify(importPackingItemList)
        }
        this.dialogVisible.show = true
      },
      // 获取模板列表
      async getList() {
        try {
          const relut = await listTemplateLink()
          this.tempalteList = relut.data || []
          const res = await listTemplate({ type: '' })
          const list = res.data || []
          list.forEach((item) => {
            item.id = item.id.toString()
          })
          this.shipperTempList = list.filter((item) => item.type === '1')
          this.consigneeTempList = list.filter((item) => item.type === '2')
          this.notifierTempList = list.filter((item) => item.type === '3')
          this.dockingTempList = list.filter((item) => item.type === '4')
          this.carrierInformationTempList = list.filter(
            (item) => item.type === '5'
          )
        } catch (err) {
          console.log(err)
        }
      },
      // 选择模板
      changeTemplateLink() {
        const { logisticsContactTemplateLink } = this.ruleForm
        const target =
          this.tempalteList.find(
            (item) => +item.id === logisticsContactTemplateLink
          ) || {}
        this.ruleForm.logisticsContactTemplateLinkName = target.name || ''
        const keyArr = [
          'shipperTemp',
          'consigneeTemp',
          'notifierTemp',
          'dockingTemp',
          'carrierInformationTemp'
        ]
        keyArr.forEach((item) => {
          const key1 = item + 'Id'
          this.ruleForm[key1] = target[key1] ? target[key1].toString() : ''
          this.changeTemplateItem(item)
        })
      },
      changeTemplateItem(key) {
        const id = this.ruleForm[key + 'Id']
        const target = this[key + 'List'].find((item) => +item.id === +id) || {}
        this.ruleForm[key + 'Name'] = target.name || ''
        this.ruleForm[key + 'Details'] = target.details || ''
      },
      // 上传成功
      successUploadFujian(res) {
        if (res.code + '' === '10000') {
          this.$successMsg('上传成功')
          this.$refs.enclosure.getEnclosureList(1)
        } else {
          this.$errorMsg(res.data)
        }
      },
      detailsChange(detailsKey, detailsSelectId) {
        const content = this.ruleForm[detailsKey]
        if (!content) {
          this.ruleForm[detailsSelectId] = ''
        }
      },
      /* 表格合计 */
      getSummary({ data }) {
        const sums = []
        let num = 0
        let goodsAmount = 0
        let grossWeightSum = 0
        let netWeightSum = 0
        data.forEach((item) => {
          num += item.num ? +item.num : 0
          goodsAmount += item.goodsAmount ? +item.goodsAmount : 0
          grossWeightSum += item.grossWeightSum ? +item.grossWeightSum : 0
          netWeightSum += item.netWeightSum ? +item.netWeightSum : 0
        })
        sums[9] = num
        sums[12] = (+goodsAmount).toFixed(2)
        sums[14] = (+grossWeightSum).toFixed(5)
        sums[15] = (+netWeightSum).toFixed(5)
        return sums
      }
    }
  }
</script>
<style>
  .yushen-edit-bx .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 135px;
  }
  .edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
<style lang="scss" scoped>
  .image-preview {
    display: inline-block;
  }
  .title-bx {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .tongji-item {
    display: inline-block;
    width: 220px;
  }
</style>
<style lang="scss" scoped>
  .hide-form {
    width: 100%;
    height: 3px;
    overflow: hidden;
    opacity: 0;
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
  .title-bx {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .open {
    height: auto;
    overflow: hidden;
    transition: height 1.2s;
  }
  .close {
    height: 0px;
    overflow: hidden;
    transition: height 1.2s;
  }
</style>
