<template>
  <div class="page-bx bgc-w all-order-edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <JFX-title title="基本信息" className="mr-t-20" />
    <!-- 表单 -->
    <JFX-Form class="mr-t-20" :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-row
        :gutter="10"
        class="fs-14 clr-II"
        v-if="ruleForm.code && !$route.query.isCopy"
      >
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="调拨订单号：" prop="code">
            <el-input
              v-model.trim="ruleForm.code"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="事业部：" prop="buId">
            <el-select
              v-model="ruleForm.buId"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getBUSelectBean('buListNew')"
              @change="changeBuId"
            >
              <el-option
                v-for="item in selectOpt.buListNew"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="调出仓库：" prop="outDepotId">
            <el-select
              v-model="ruleForm.outDepotId"
              placeholder="请选择"
              filterable
              clearable
              @change="changeOutDepotId"
            >
              <el-option
                v-for="item in selectOpt.outDepotIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="调入仓库：" prop="inDepotId">
            <el-select
              v-model="ruleForm.inDepotId"
              clearable
              filterable
              @change="changeInDepotId"
            >
              <el-option
                v-for="item in selectOpt.inDepotIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="是否同关区：" prop="isSameArea">
            <el-select
              v-model="ruleForm.isSameArea"
              placeholder="请选择"
              clearable
              @change="reloadRequiredField"
            >
              <el-option :label="'是'" :value="'1'"></el-option>
              <el-option :label="'否'" :value="'0'"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="调出公司：">
            <el-input
              v-model.trim="ruleForm.merchantName"
              disabled
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="调入公司：">
            <el-input
              v-model.trim="ruleForm.merchantName"
              disabled
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="发票号：" prop="invoiceNo">
            <el-input
              v-model.trim="ruleForm.invoiceNo"
              clearable
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="包装方式：" prop="packType">
            <el-input
              v-model.trim="ruleForm.packType"
              clearable
              placeholder="包装方式"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="箱数：" prop="cartons">
            <el-input-number
              v-model.trim="ruleForm.cartons"
              :precision="0"
              :controls="false"
              :min="0"
              style="width: 110%"
              @change="changePackType"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="运输方式：" prop="transport">
            <el-select
              v-model="ruleForm.transport"
              placeholder="请选择"
              clearable
              :data-list="getSelectList('transportList')"
              @change="changeTransport"
            >
              <el-option
                v-for="item in selectOpt.transportList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="标准箱TEU：" prop="standardCaseTeu">
            <el-input
              v-model.trim="ruleForm.standardCaseTeu"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="车次：" prop="trainNumber">
            <el-input
              v-model.trim="ruleForm.trainNumber"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="承运商：" prop="carrier">
            <el-input
              v-model.trim="ruleForm.carrier"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="托数：" prop="torrNum">
            <el-input-number
              v-model.trim="ruleForm.torrNum"
              :precision="0"
              :controls="false"
              :min="0"
              style="width: 110%"
              @change="changePackType"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="出库地点：" prop="outdepotAddr">
            <el-input
              v-model.trim="ruleForm.outdepotAddr"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="提单号：" prop="firstLadingBillNo">
            <el-input
              v-model.trim="ruleForm.firstLadingBillNo"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="收货地址：" prop="receivingAddress">
            <el-input
              v-model.trim="ruleForm.receivingAddress"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="收货人名称：" prop="consigneeUsername">
            <el-input
              v-model.trim="ruleForm.consigneeUsername"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="国家：" prop="country">
            <el-input
              v-model.trim="ruleForm.country"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="12"
          :lg="6"
          :xl="6"
          v-if="outDepotType === 'c'"
        >
          <el-form-item label="目的地：" prop="destination">
            <el-select v-model="ruleForm.destination" placeholder="请选择">
              <el-option
                v-for="item in mudiList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="12"
          :lg="6"
          :xl="6"
          v-if="outDepotType === 'c' || inDepotType === 'c'"
        >
          <el-form-item label="海外仓理货单位：" prop="tallyingUnit">
            <el-select
              v-model="ruleForm.tallyingUnit"
              placeholder="请选择"
              @change="changeTallyingUnit"
            >
              <el-option
                v-for="item in tallyingUnitList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="境外发货人：" prop="shipper">
            <el-input
              v-model.trim="ruleForm.shipper"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="唛头：" prop="mark">
            <el-input
              v-model.trim="ruleForm.mark"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="PO号：" prop="poNo">
            <el-input
              v-model.trim="ruleForm.poNo"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="合同号：" prop="contractNo">
            <el-input
              v-model.trim="ruleForm.contractNo"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="调入仓地址：" prop="depotScheduleId">
            <el-select
              v-model="ruleForm.depotScheduleId"
              placeholder="请选择"
              filterable
              clearable
              :disabled="inDepotType !== 'b'"
            >
              <el-option
                v-for="item in selectOpt.depotAddList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="提单毛重(Kg)：" prop="billWeight">
            <JFX-Input
              v-model.trim="ruleForm.billWeight"
              :min="0"
              :precision="5"
              placeholder="请输入"
              clearable
              @change="changeBillWeight"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="付款条约：" prop="payRules">
            <el-input
              v-model.trim="ruleForm.payRules"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="托盘材质：" prop="palletMaterial">
            <el-select
              v-model="ruleForm.palletMaterial"
              placeholder="请选择"
              clearable
              :data-list="getSelectList('order_torrpacktypeList')"
              @change="changePackType"
            >
              <el-option
                v-for="item in selectOpt.order_torrpacktypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="装货港：" prop="portLoading">
            <el-input
              v-model.trim="ruleForm.portLoading"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="卸货港：" prop="unloadPort">
            <el-input
              v-model.trim="ruleForm.unloadPort"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="入库关区：" prop="inCustomsId">
            <el-select
              v-model="ruleForm.inCustomsId"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in selectOpt.inCustomsIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="出库关区：" prop="outCustomsId">
            <el-select
              v-model="ruleForm.outCustomsId"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in selectOpt.outCustomsIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="LBX单号：" prop="lbxNo">
            <el-input
              v-model="ruleForm.lbxNo"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="库位类型："
            prop="stockLocationTypeId"
            ref="stockLocationTypeId"
          >
            <el-select
              v-model="ruleForm.stockLocationTypeId"
              placeholder="请选择"
              clearable
              filterable
              style="width: 100%"
              :disabled="!rules.stockLocationTypeId.required"
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
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="备注：" prop="remark">
            <el-input
              v-model.trim="ruleForm.remark"
              clearable
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>

    <div class="diy-el-tabs-wrap">
      <div class="el-tabs-title-btnText" v-if="tabStatus === '0'">
        <el-button
          type="primary"
          :size="'small'"
          @click="handleClickImportGood"
        >
          商品导入
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="handleClickImportPacking"
          id="pur_product_in"
        >
          导入箱装明细
        </el-button>
      </div>
      <el-tabs v-model="tabStatus">
        <el-tab-pane label="商品信息" name="0">
          <!-- 商品信息 -->
          <JFX-table :tableData.sync="tableData" :showPagination="false">
            <el-table-column align="center" width="70" label="操作">
              <template slot-scope="scope">
                <el-button type="text" @click="reduce(scope.$index)">
                  <i class="el-icon-minus fs-16"></i>
                </el-button>
                <el-button type="text" @click="add">
                  <i class="el-icon-plus fs-16"></i>
                </el-button>
              </template>
            </el-table-column>
            <el-table-column prop="code" label="序号" align="center" width="80">
              <template slot-scope="scope">
                <el-input-number
                  v-model.trim="scope.row.seq"
                  :data-setdefaut="setDefaut(scope.$index)"
                  :precision="0"
                  :controls="false"
                  :min="0"
                  style="width: 100%"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column
              prop="code"
              label="调出商品编码"
              align="center"
              min-width="120"
            >
              <template slot-scope="scope">
                <div>
                  <div>{{ scope.row.outGoodsCode }}</div>
                  <el-button
                    type="primary"
                    :size="'small'"
                    @click="choseOutGoods('outDepotId', scope.$index)"
                  >
                    选择商品
                  </el-button>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="outGoodsNo"
              label="调出商品货号"
              align="center"
              min-width="120"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="outGoodsName"
              label="调出商品名称"
              align="center"
              min-width="120"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="outUnit"
              label="调出单位"
              align="center"
              min-width="100"
            >
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.outUnit"
                  placeholder="请选择"
                  style="width: 100%"
                  :disabled="outDepotType === 'c'"
                >
                  <el-option
                    v-for="item in tallyingUnitList"
                    :key="item.key"
                    :label="item.value"
                    :value="item.key"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="100">
              <template slot="header">
                <span class="need">调出数量</span>
              </template>
              <template slot-scope="scope">
                <el-input-number
                  v-model.trim="scope.row.transferNum"
                  :precision="0"
                  :controls="false"
                  :min="0"
                  style="width: 100%"
                  @change="changeOutTransferNum(scope.$index)"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column
              prop="outBarcode"
              label="条形码"
              align="center"
              min-width="120"
            >
            </el-table-column>
            <el-table-column align="center" min-width="170">
              <template slot="header">
                <span class="need">调入商品编号</span>
              </template>
              <template slot-scope="scope">
                <div>
                  <div>
                    {{ scope.row.inGoodsCode }}
                  </div>
                  <el-button
                    type="primary"
                    :size="'small'"
                    @click="choseOutGoods('inDepotId', scope.$index)"
                  >
                    选择商品
                  </el-button>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="inGoodsNo"
              label="调入商品货号"
              align="center"
              min-width="120"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="inGoodsName"
              label="调入商品名称"
              align="center"
              min-width="120"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column label="调入单位" align="center" min-width="100">
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.inUnit"
                  placeholder="请选择"
                  style="width: 100%"
                  :disabled="inDepotType === 'c'"
                >
                  <el-option
                    v-for="item in tallyingUnitList"
                    :key="item.key"
                    :label="item.value"
                    :value="item.key"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="100">
              <template slot="header">
                <span class="need">调入数量</span>
              </template>
              <template slot-scope="scope">
                <!-- 调入或调出仓库为海外仓 且 海外理货单位 不为件 -->
                <el-input-number
                  v-model.trim="scope.row.inTransferNum"
                  :precision="0"
                  :controls="false"
                  :min="0"
                  style="width: 100%"
                  @change="calcRowWeightSum(scope.$index)"
                  :disabled="
                    !(
                      (outDepotType === 'c' || inDepotType === 'c') &&
                      ruleForm.tallyingUnit !== '02'
                    )
                  "
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column
              prop="brandName"
              label="品牌类型"
              align="center"
              min-width="120"
            >
              <template slot-scope="scope">
                <el-input
                  v-model.trim="scope.row.brandName"
                  disabled
                  style="width: 100%"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="130">
              <template slot="header">
                <span class="need">调拨单价</span>
              </template>
              <template slot-scope="scope">
                <el-input-number
                  v-model.trim="scope.row.price"
                  :precision="2"
                  v-max-spot="2"
                  :controls="false"
                  :min="0"
                  style="width: 100%"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="120">
              <template slot="header">
                <span class="need">毛重(kg)</span>
              </template>
              <template slot-scope="scope">
                <JFX-Input
                  v-model.trim="scope.row.grossWeightRowSum"
                  :min="0"
                  :precision="5"
                  placeholder="请输入"
                  style="width: 100%"
                  @change="calcTableTotal"
                />
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="120">
              <template slot="header">
                <span class="need">净重(kg)</span>
              </template>
              <template slot-scope="scope">
                <JFX-Input
                  v-model.trim="scope.row.netWeightRowSum"
                  :min="0"
                  :precision="5"
                  placeholder="请输入"
                  style="width: 100%"
                  @change="calcTableTotal"
                />
              </template>
            </el-table-column>
            <el-table-column
              prop="contNo"
              label="箱号"
              align="center"
              min-width="120"
            >
              <template slot-scope="scope">
                <el-input
                  v-model.trim="scope.row.contNo"
                  clearable
                  style="width: 100%"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column
              prop="bargainno"
              label="合同号"
              align="center"
              min-width="120"
            >
              <template slot-scope="scope">
                <el-input
                  v-model.trim="scope.row.bargainno"
                  clearable
                  style="width: 100%"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column
              prop="torrNo"
              label="托盘号"
              align="center"
              min-width="120"
            >
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.torrNo"
                  clearable
                  style="width: 100%"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column
              prop="boxNum"
              label="箱数"
              align="center"
              min-width="100"
            >
              <template slot-scope="scope">
                <el-input-number
                  v-model.trim="scope.row.boxNum"
                  :precision="0"
                  :controls="false"
                  :min="0"
                  style="width: 100%"
                ></el-input-number>
              </template>
            </el-table-column>
          </JFX-table>
          <div class="mr-t-15 flex-c-c">
            <span>
              调出总数量：{{ transferOutTotalNum || 0 }}&nbsp;&nbsp;&nbsp;&nbsp;
            </span>
            <span>
              调入总数量：{{ transferInTotalNum || 0 }}&nbsp;&nbsp;&nbsp;&nbsp;
            </span>
            <span
              >总毛重：{{ totalGrossWeight || 0 }}&nbsp;&nbsp;&nbsp;&nbsp;</span
            >
            <span>总净重：{{ totalNetWeight || 0 }}</span>
          </div>
          <!-- 商品信息 end-->
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

    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="save" :loading="saveloading">
        保 存
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
      <el-button
        type="primary"
        @click="auditForm"
        :loading="auditFormBtnLoading"
        v-permission="'transfer_saveTransferOrder'"
      >
        保存并审核
      </el-button>
    </div>
    <!-- 选择商品 -->
    <chose-goods
      v-if="choseGoods.visible.show"
      :isVisible.sync="choseGoods.visible"
      :customComfirm="customComfirm"
      @comfirm="comfirmInGoodsCallBack"
      :data="choseGoods.choseGoodsData"
    ></chose-goods>
    <!-- 选择商品 -->
    <!-- 导入 -->
    <div>
      <JFX-Dialog
        :visible.sync="importDialogVisible"
        :showClose="true"
        @comfirm="importDialogVisible.show = false"
        :width="'860px'"
        :title="importDialogTitle"
        :top="'3vh'"
        closeBtnText="取 消"
        confirmBtnText="确 认"
      >
        <div class="import-bx">
          <importfile
            v-if="isImportGoods"
            @downup="downup"
            :url="importTransferGoodsUrl"
            :filterData="imortGoodsFilterData"
            :selfDown="true"
            :templateId="'2333'"
            @successUpload="successUpload"
            downText="商品导出"
          ></importfile>
          <!-- 导入装箱明细资料 -->
          <importfile
            v-else
            :url="importPackingDetailsUrl"
            :filterData="importPackingData"
            :selfDown="false"
            :templateId="'157'"
            @successUpload="packingImportSuccessUpload"
          ></importfile>
        </div>
      </JFX-Dialog>
    </div>
    <!-- 导入 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import choseGoods from '@c/choseGoods/index'
  import {
    getTransferOrderDetail,
    getMustParameter,
    importTransferGoodsUrl,
    exportItemsUrl,
    saveTempTransferOrder,
    saveTransferOrder,
    updateTransferOrder,
    importPackingDetailsUrl
  } from '@a/transferManage/index'
  import {
    getDepotById,
    checkDepotMerchantRel,
    getListByIdsAndTypeAndInOutDepot,
    getOrderUpdateMerchandiseInfo,
    getBuStockLocationTypeConfigSelect,
    getLocationManage
  } from '@a/base'
  import { Decimal } from 'decimal.js'
  import { exportFilePost } from '@/utils/common'
  // eslint-disable-next-line no-unused-vars
  function dnum(num) {
    return new Decimal(num)
  }
  export default {
    mixins: [commomMix],
    components: {
      choseGoods,
      importfile: () => import('@/components/importfile/index')
    },
    data() {
      return {
        ruleForm: {
          id: '',
          code: '',
          outDepotId: '',
          inDepotId: '',
          isSameArea: '',
          buId: '',
          inCustomerId: '',
          invoiceNo: '',
          packType: '',
          cartons: '',
          transport: '',
          standardCaseTeu: '',
          trainNumber: '',
          torrNum: '',
          outdepotAddr: '',
          firstLadingBillNo: '',
          receivingAddress: '',
          consigneeUsername: '',
          country: '',
          destination: '',
          tallyingUnit: '',
          shipper: 'NO',
          poNo: '',
          contractNo: '',
          depotScheduleId: '',
          billWeight: '',
          lbxNo: '',
          remark: '',
          merchantName: '',
          carrier: '',
          mark: '',
          payRules: '',
          palletMaterial: '',
          portLoading: '',
          unloadPort: '',
          outCustomsId: '',
          inCustomsId: '',
          merchantId: '',
          stockLocationTypeId: ''
        },
        rules: {
          outDepotId: {
            required: true,
            message: '调出仓库不能为空'
          },
          inDepotId: {
            required: true,
            message: '调入仓库不能为空'
          },
          buId: {
            required: true,
            message: '事业部不能为空'
          },
          cartons: {
            required: true,
            message: '箱数不能为空'
          },
          transport: {
            required: true,
            message: '运输方式不能为空'
          },
          torrNum: {
            required: true,
            message: '运输方式不能为空'
          },
          receivingAddress: {
            required: true,
            message: '收货地址不能为空'
          },
          tallyingUnit: {
            required: true,
            message: '外仓理货单位不能为空'
          },
          payRules: {
            required: true,
            message: '付款条约不能为空'
          },
          palletMaterial: {
            required: true,
            message: '托盘材质不能为空'
          },
          unloadPort: {
            required: true,
            message: '卸货港不能为空'
          },
          destination: {
            required: true,
            message: '目的地不能为空'
          },
          contractNo: {
            required: true,
            message: '合同号不能为空'
          },
          outdepotAddr: {
            required: true,
            message: '出库地点不能为空'
          },
          isSameArea: {
            required: false,
            message: '是否同关区不能为空'
          },
          firstLadingBillNo: {
            required: false,
            message: '调入客户不能为空'
          },
          trainNumber: {
            required: false,
            message: '车次不能为空'
          },
          standardCaseTeu: {
            required: false,
            message: '标准箱TEU不能为空不能为空'
          },
          country: {
            required: false,
            message: '国家不能为空'
          },
          depotScheduleId: {
            required: false,
            message: '调入不能为空'
          },
          portLoading: {
            required: true,
            message: '装货港不能为空'
          },
          stockLocationTypeId: {
            required: false,
            message: '库位类型不能为空'
          },
          invoiceNo: {
            required: false,
            message: '发票号不能为空'
          },
          packType: {
            required: false,
            message: '包装方式不能为空'
          },
          consigneeUsername: {
            required: false,
            message: '收货人名称不能为空'
          }
        },
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10000,
          total: 10001
        },
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
        ],
        // 目的地
        mudiList: [
          { value: '广州机场', key: 'GZJC01' },
          { value: '黄埔状元谷', key: 'HP01' },
          { value: '香港本地', key: 'HK01' }
        ],
        // 海外仓理货单位
        tallyingUnitList: [
          { value: '托盘', key: '00' },
          { value: '箱', key: '01' },
          { value: '件', key: '02' }
        ],
        typeSelectList: [], // 库位类型下拉列表
        // 仓库数据
        outDepotType: '', // 调出仓库：仓库类型：a-保税仓，b-虚拟仓，c-海外仓，d-金融在途仓
        outIsTopBooks: '', // 调出仓库：是否是代销仓 1-是 0-否
        outDepotIsInOutInstruction: '', // 调出仓库是否是进出口指令
        inDepotType: '', // 调入仓库 仓库类型
        inDepotIsInOutInstruction: '', // 调入仓库 是否是进出口指令
        // 表格统计
        transferOutTotalNum: '', // 调出总数量
        transferInTotalNum: '', // 调入总数量
        totalGrossWeight: '', // 总毛重
        totalNetWeight: '', // 总净重
        tabStatus: '0', // 商品信息tab切换
        /** 选择商品弹框  */
        changeTableRowData: {}, // 修改表格某行记录数据
        customComfirm: null, // 选择商品是否自定义事件
        choseGoods: {
          visible: { show: false },
          choseGoodsData: {}
        },
        /** 导入弹框 */
        importDialogVisible: { show: false },
        importDialogTitle: '商品导入', // 导入弹框标题
        isImportGoods: false, // 是否是导入商品
        importPackingDetailsUrl: importPackingDetailsUrl, // 导入箱装明细url
        importTransferGoodsUrl: importTransferGoodsUrl, // 导入商品接口
        imortGoodsFilterData: {}, // 导入商品参数
        importPackingData: {}, // 箱装明细导入参数
        /* 按钮 */
        saveloading: false, // 保存loading
        auditFormBtnLoading: false // 保存并审核按钮 loading
      }
    },
    watch: {
      /** 调出仓库为进出口指令，关区必填 */
      outDepotIsInOutInstruction(val) {
        this.rules.isSameArea.required = val === '1'
      },
      /** 调入仓库为进出口指令，关区必填 */
      inDepotIsInOutInstruction(val) {
        this.rules.isSameArea.required = val === '1'
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const {
          query: { id }
        } = this.$route
        /* 设置公司 */
        this.setMerchantName()
        /* 设置默认一个商品一行数据 */
        this.add()
        // 编辑情况
        if (id) this.editInit(id)
      },
      // 设置公司
      async setMerchantName() {
        const userInfo = this.getUserInfo()
        this.ruleForm.merchantName = userInfo.merchantName || ''
        this.ruleForm.merchantId = userInfo.merchantId || ''
      },
      // 编辑/复制 初始化
      async editInit(id) {
        // 获取详情
        const { data } = await getTransferOrderDetail({ id })
        // 回填表单
        for (const key in this.ruleForm) {
          this.ruleForm[key] = String(data[key] || '')
        }
        // 回填表体
        const orderItemDTOS = data?.orderItemDTOS || []
        if (orderItemDTOS?.length) {
          this.tableData.list = orderItemDTOS.map((item) => ({
            ...item,
            grossWeightRowSum: item.grossWeightSum,
            netWeightRowSum: item.netWeightSum
          }))
          this.calcTableTotal()
        }
        /* 箱装明细 */
        this.packingTableData.list = data?.packingList || []

        /* 获取调出仓库下拉 */
        this.getOutDepotList()

        /* 获取出库关区下拉列表 */
        this.getOutCustomsIdList()
        /* 等待 获取调出仓库详情 */
        await this.getOutDepotDetail()
        /* 获取调入仓库下拉 */
        this.getInDepotList()

        /* 获取入库关区下拉列表 */
        this.getInCustomsIdList()
        /* 等待 获取调入仓库详情 */
        await this.getInDepotDetail()
        /* 获取必填字段 */
        this.reloadRequiredField()
        /* 获取仓库地址 */
        this.getDepotAddList()

        /* 获取库位类型下拉列表 */
        this.getTypeSeclectList()
        /* 查询是否启用库位管理 */
        this.getLocationManage()
      },
      /* 修改事业部 */
      async changeBuId() {
        await this.$nextTick()
        this.$refs.stockLocationTypeId.resetField()
        this.ruleForm.outDepotId = ''
        this.ruleForm.inDepotId = ''
        /** 调出仓库列表重新渲染 */
        this.getOutDepotList()
        /* 获取库位类型下拉列表 */
        this.getTypeSeclectList()
        /* 查询是否启用库位管理 */
        this.getLocationManage()
      },
      // 选择出库仓库
      async changeOutDepotId() {
        this.ruleForm.inDepotId = '' // 入库仓库
        this.ruleForm.outCustomsId = '' // 出库关区
        this.ruleForm.depotScheduleId = '' // 调入仓地址
        this.ruleForm.tallyingUnit = '' // 海外仓理货单位
        this.ruleForm.isSameArea = '' // 同关区
        /* 获取出库关区下拉列表 */
        this.getOutCustomsIdList()
        /* 等待 获取调出仓库详情 */
        await this.getOutDepotDetail()
        /* 获取调入仓库下拉 */
        this.getInDepotList()
        /* 更新商品列表 */
        this.updateGoodsList('out')
      },
      // 选择入库仓库
      async changeInDepotId() {
        this.ruleForm.inCustomsId = '' // 入库关区
        this.ruleForm.isSameArea = '' // 是否同关区
        this.ruleForm.tallyingUnit = '' // 海外仓理货单位
        this.inDepotType = '' // 仓库类型
        this.inDepotIsInOutInstruction = '' // 仓库进出口指令
        /* 获取入库关区下拉列表 */
        this.getInCustomsIdList()
        /* 获取调入仓库详情 */
        await this.getInDepotDetail()
        /* 获取必填字段 */
        this.reloadRequiredField()
        /* 获取仓库地址 */
        this.getDepotAddList()
        /* 更新商品列表 */
        this.updateGoodsList('in')
      },
      /* 改变理货单位： 理货单位改变为海外仓，赋值理货单位 */
      changeTallyingUnit() {
        const {
          outDepotType,
          ruleForm: { tallyingUnit }
        } = this
        if (outDepotType === 'c') {
          this.tableData.list.map((item) => {
            item.outUnit = item.inUnit = tallyingUnit
          })
        }
      },
      /* 改变运输方式 */
      changeTransport() {
        let { transport } = this.ruleForm
        transport = String(transport)
        // 当运输方式为海运、空运时必填提单号
        this.rules.firstLadingBillNo.required = ['1', '2'].includes(transport)
        // 当运输方式为陆运、港到仓拖车时必填
        this.rules.trainNumber.required = ['3', '4'].includes(transport)
        // 当运输方式为海运时必填
        this.rules.standardCaseTeu.required = ['2'].includes(transport)
      },
      /* 修改托盘材质 */
      changePackType() {
        const packTypeItem = this.selectOpt.order_torrpacktypeList.find(
          ({ key }) => {
            if (this.ruleForm.palletMaterial === key) {
              return true
            }
          }
        )
        this.ruleForm.packType =
          (this.ruleForm.cartons || '') +
          '箱/' +
          (this.ruleForm.torrNum || '') +
          '托' +
          (packTypeItem?.value || '')
      },
      /* 获取调出仓库下拉 */
      getOutDepotList() {
        delete this.selectOpt.outDepotIdList
        const { buId } = this.ruleForm
        if (!buId) return
        /* 仓库类别 a-保税仓，b-备查库，c-海外仓，d-中转仓,e-暂存区，f-销毁区 g-内贸仓 */
        this.getSelectBeanByMerchantBuRel('outDepotIdList', {
          type: 'a,b,c,e,d',
          buId
        })
      },
      /* 获取调入仓库下拉 */
      getInDepotList() {
        if (!this.ruleForm.outDepotId) return
        const { outDepotType, outIsTopBooks } = this
        let getInDepotParam = {}
        if (outDepotType === 'a') {
          // 当调出仓库为保税仓a时，调入仓库仅能选海外仓c、中转仓d 和保税仓a；
          getInDepotParam = { type: 'a,c,d' }
        } else if (outDepotType === 'b') {
          // 当调出仓库为备查库b时，调入仓库仅为暂存仓e可选；
          getInDepotParam = { type: 'e' }
        } else if (outDepotType === 'c') {
          // 当调出仓库为海外仓c时，调入仓库仅能选保税仓a和中转仓d；
          getInDepotParam = { type: 'a,d' }
        } else if (outDepotType === 'd') {
          // 当调出仓库为中转仓d时，调入仓库仅能选海外仓c和保税仓a；
          getInDepotParam = { type: 'a,c' }
        } else if (outDepotType === 'e' && outIsTopBooks === '1') {
          // 当调出仓库为暂存仓e且标识为代销仓时，调入仓库可选为销毁区f；
          getInDepotParam = { type: 'f' }
        } else if (outDepotType === 'e' && outIsTopBooks === '0') {
          // 当调出仓库为暂存仓e且标识为非代销仓时，调入仓库可选为备查库b；
          getInDepotParam = { type: 'b' }
        }
        this.getSelectBeanByMerchantRel('inDepotIdList', getInDepotParam)
      },
      /* 获取出库关区下拉列表 */
      getOutCustomsIdList() {
        delete this.selectOpt.outCustomsIdList
        const { outDepotId } = this.ruleForm.outDepotId
        if (!outDepotId) return
        this.getSelectCustomsArea('outCustomsIdList', {
          depotId: outDepotId
        })
      },
      /* 获取入库关区下拉列表 */
      getInCustomsIdList() {
        delete this.selectOpt.inCustomsIdList
        if (!this.ruleForm.inDepotId) return
        this.getSelectCustomsArea('inCustomsIdList', {
          depotId: this.ruleForm.inDepotId
        })
      },
      /* 获取调出仓库详情 */
      async getOutDepotDetail() {
        const { outDepotId } = this.ruleForm
        if (!outDepotId) return
        const {
          data: { type: outDepotType, isTopBooks: outIsTopBooks }
        } = await getDepotById({ id: outDepotId })
        this.outDepotType = outDepotType
        this.outIsTopBooks = outIsTopBooks
        // 调出仓库是否是进出口指令
        const {
          data: { isInOutInstruction }
        } = await checkDepotMerchantRel({ depotId: outDepotId })
        this.outDepotIsInOutInstruction = isInOutInstruction || ''
      },
      /* 获取调入仓库详情 */
      async getInDepotDetail() {
        const { inDepotId } = this.ruleForm
        if (!inDepotId) return
        const {
          data: { type: inDepotType }
        } = await getDepotById({ id: inDepotId })
        this.inDepotType = inDepotType
        /** 获取调入仓库 调出仓库 是否是进出口指令 */
        const {
          data: { isInOutInstruction }
        } = await checkDepotMerchantRel({
          depotId: inDepotId
        })
        this.inDepotIsInOutInstruction = isInOutInstruction || ''
      },
      /* 获取仓库地址下拉 */
      getDepotAddList() {
        delete this.selectOpt.depotAddList
        const {
          inDepotType,
          ruleForm: { inDepotId }
        } = this
        if (!inDepotId) return
        // 调入仓为备查仓时显示 显示 仓库地址
        inDepotType === 'b' &&
          this.getdepotArrList('depotAddList', {
            depotId: inDepotId
          })
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
      // 商品列表增加一条记录
      add() {
        this.tableData.list.push({
          outGoodsNo: '',
          outGoodsName: '',
          brandName: '',
          outUnit: '02', // 非海外仓 理货单为默认为 件
          transferNum: '',
          inGoodsNo: '',
          inGoodsName: '',
          inGoodsCode: '',
          inUnit: '02',
          inTransferNum: '',
          price: '',
          grossWeight: '', // 单个毛重
          grossWeightRowSum: '', // 数量 * 单个毛重
          netWeight: '', // 单个净重
          netWeightRowSum: '', // 数量 * 单个净重
          contNo: '',
          bargainno: '',
          remark: '',
          seq: '',
          outBarcode: '',
          torrNo: '',
          boxNum: ''
        })
      },
      // 商品列表删一条记录
      reduce(index) {
        const list = this.tableData.list
        list.splice(index, 1)
        !list.length && this.add()
        this.calcTableTotal()
      },
      // 设置默认序号
      setDefaut(index) {
        const array = this.tableData.list.map((item) => +item.seq || 0)
        const maxSeq = Math.max(...array)
        if (!this.tableData.list[index].seq) {
          this.tableData.list[index].seq = maxSeq + 1
        }
      },
      // 选择调出/调入商品
      choseOutGoods(depotType, index) {
        if (!this.ruleForm.outDepotId || !this.ruleForm.inDepotId) {
          const msg = `${!this.ruleForm.outDepotId ? '调出仓库，' : ''}${
            !this.ruleForm.inDepotId ? '调入仓库，' : ''
          }不能为空`
          return this.$errorMsg(msg)
        }
        const { outGoodsId, outBarcode } = this.tableData.list[index]
        // 选择的是判断调入商品，前置条件调出商品必填
        if (depotType === 'inDepotId' && !outGoodsId) {
          return this.$errorMsg('请先选择调出商品！')
        }
        // 修改的表格位置记录，以及修改的调出，调入记录, 是否是切换barCode
        this.changeTableRowData = { depotType, index, barcode: outBarcode }

        // 调出 选择商品
        if (depotType === 'outDepotId') {
          this.customComfirm = this.comfirmOutGoodsCallBack
          const unNeedOutGoodIds = this.tableData.list
            .filter((item) => !!item.outGoodsId)
            .map((item) => item.outGoodsId)
            .toString()
          this.choseGoods.choseGoodsData = {
            depotId: this.ruleForm.outDepotId,
            unNeedIds: unNeedOutGoodIds || '',
            barcode: outBarcode,
            popupType: 4
          }
        } else {
          // 调入选择商品
          this.customComfirm = null
          const unNeedinGoodIds = this.tableData.list
            .filter((item) => !!item.inGoodsId)
            .map((item) => item.inGoodsId)
            .toString()
          debugger
          this.choseGoods.choseGoodsData = {
            popupType: 4,
            depotId: this.ruleForm.inDepotId,
            unNeedIds: unNeedinGoodIds || '',
            barcode: outBarcode // outBarcode和inbarcode一致，可以共用
          }
        }
        this.choseGoods.visible.show = true
      },
      // 选择调出商品回调
      async comfirmOutGoodsCallBack(list) {
        this.choseGoods.visible.show = false
        const { index, barcode } = this.changeTableRowData
        // 选择的商品id
        const ids = list.map((item) => item.id).toString()
        // 传入当前列表中调入商品id，过滤调入商品
        const unNeedIds = this.tableData.list
          .filter(({ inGoodsId }) => !!inGoodsId)
          .map(({ inGoodsId }) => inGoodsId)
          .toString()
        // 重新获取商品列表
        const { data } = await getListByIdsAndTypeAndInOutDepot({
          type: 4,
          ids,
          unNeedIds,
          // 切换货号的时候传调出仓库id, 新增商品的时候传调入商品id
          depotId: barcode ? this.ruleForm.outDepotId : this.ruleForm.inDepotId
        })
        if (!data) return

        /** 是切换货号的情况 */
        if (barcode) {
          const updateData = data[0]
          const rowData = this.tableData.list[index]
          // 更新商品信息
          for (const key in updateData) {
            rowData[key] = updateData[key]
          }
          // 价格 = 备案单价 * 系数
          const priceDeclareRatio = rowData.priceDeclareRatio || 1
          rowData.price = rowData.outFilingPrice * priceDeclareRatio || ''
          // 毛重净重
          if (!rowData.grossWeight) rowData.grossWeight = 0
          if (!rowData.netWeight) rowData.netWeight = 0
          // 重新计算毛重净重
          this.calcRowWeightSum(index)
        } else {
          /** 新增情况 */
          const arr = []
          data.forEach((item) => {
            const {
              ruleForm: { tallyingUnit },
              outDepotType,
              inDepotType
            } = this
            const priceDeclareRatio = item.priceDeclareRatio || 1
            const price = item.outFilingPrice * priceDeclareRatio || ''
            arr.push({
              ...item,
              grossWeightRowSum: item.grossWeightSum || item.grossWeight || 0,
              netWeightRowSum: item.netWeightSum || item.netWeight || 0,
              seq: '',
              transferNum: '',
              inTransferNum: '',
              contNo: '',
              bargainno: '',
              remark: '',
              boxNum: '',
              torrNo: '',
              price,
              outUnit: outDepotType === 'c' ? tallyingUnit : '02',
              inUnit: inDepotType === 'c' ? tallyingUnit : '02'
            })
          })
          this.tableData.list.splice(index, 1, ...arr)
        }
        /** 计算总和 */
        this.calcTableTotal()
      },
      // 选择调入商品回调
      comfirmInGoodsCallBack(list) {
        this.choseGoods.visible.show = false
        const { index } = this.changeTableRowData
        /** 更新调入商品 */
        const rowData = this.tableData.list[index]
        const { goodsNo, goodsCode, id, name } = list[0]
        rowData.inGoodsId = id
        rowData.inGoodsName = name
        rowData.inGoodsCode = goodsCode
        rowData.inGoodsNo = goodsNo
      },
      /* 改变调出数量 */
      changeOutTransferNum(index) {
        const updateRow = this.tableData.list[index]
        const { transferNum } = updateRow
        // 调出 同步到 调入
        updateRow.inTransferNum = updateRow.transferNum = transferNum || 0
        /* 计算某条商品列表 毛重净重 */
        this.calcRowWeightSum(index)
      },
      /* 重新计算index+1行商品的毛重净重 */
      calcRowWeightSum(index) {
        const updateRow = this.tableData.list[index]
        const { inTransferNum, grossWeight, netWeight } = updateRow
        // 毛重净重计算,计算的时候,数量最小为1
        const updateRowGrossWeightSum =
          parseFloat(grossWeight || 0) * (inTransferNum || 1)
        const updateRowtotalNetWeightSum =
          parseFloat(netWeight || 0) * (inTransferNum || 1)
        updateRow.grossWeightRowSum = updateRowGrossWeightSum.toFixed(5)
        updateRow.netWeightRowSum = updateRowtotalNetWeightSum.toFixed(5)
        //  计算统计数据
        this.calcTableTotal()
      },
      /* 计算统计数据 */
      calcTableTotal() {
        let transferOutTotalNum = 0
        let transferInTotalNum = 0
        let totalGrossWeight = 0
        let totalNetWeight = 0
        const add = (num1, num2) =>
          dnum(+num1 || 0)
            .add(dnum(+num2 || 0))
            .toNumber()
        this.tableData.list.forEach((item) => {
          totalGrossWeight = add(totalGrossWeight, item.grossWeightRowSum)
          totalNetWeight = add(totalNetWeight, item.netWeightRowSum)
          transferInTotalNum = add(transferInTotalNum, item.inTransferNum)
          transferOutTotalNum = add(transferOutTotalNum, item.transferNum)
          item.grossWeightRowSum = (+item.grossWeightRowSum).toFixed(5)
          item.netWeightRowSum = (+item.netWeightRowSum).toFixed(5)
        })
        this.totalNetWeight = totalNetWeight.toFixed(5)
        this.transferInTotalNum = parseInt(transferInTotalNum, 10)
        this.transferOutTotalNum = parseInt(transferOutTotalNum, 10)
        this.ruleForm.billWeight = this.totalGrossWeight =
          totalGrossWeight.toFixed(5)
      },
      /* 修改提单毛重 */
      changeBillWeight() {
        const { billWeight } = this.ruleForm
        const list = this.tableData.list
        // 统计总量
        this.totalGrossWeight = this.ruleForm.billWeight = (
          +billWeight || 0
        ).toFixed(5)
        // 当前列表净重总量
        const netTotal = list.reduce((sum, cur) => {
          return dnum(sum).add(dnum(Number(cur.netWeightRowSum || 0)))
        }, 0)
        // 总和为0，全部为0
        if ([Number(billWeight), netTotal].includes(0)) {
          return list.forEach((item) => {
            item.grossWeightRowSum = 0
          })
        }
        // 分摊
        list.reduce((beforeTotal, item, index) => {
          const { netWeightRowSum = 0 } = item
          const isLast = list.length - 1 === index
          let setNum = 0 // 设置的值
          if (isLast) {
            // 最后一个倒减
            setNum = dnum(billWeight).sub(dnum(beforeTotal)).toNumber()
          } else {
            // 占比计算
            const rate = dnum(netWeightRowSum).div(dnum(netTotal)).toNumber()
            setNum = dnum(rate)
              .mul(dnum(Number(billWeight)))
              .toNumber()
            // 已分摊金额
            beforeTotal = dnum(beforeTotal).add(dnum(setNum)).toNumber()
          }
          item.grossWeightRowSum = setNum?.toFixed(5) || 0
          return beforeTotal
        }, 0)
      },
      // 打开商品导入
      handleClickImportGood() {
        const { outDepotId, inDepotId } = this.ruleForm
        if (!outDepotId || !inDepotId) {
          return this.$errorMsg('调出/调入仓库不能为空')
        }
        this.imortGoodsFilterData = { outDepotId, inDepotId }
        this.isImportGoods = true
        this.importDialogTitle = '商品导入'
        this.importDialogVisible.show = true
      },
      // 导出商品模板
      async downup() {
        // 导出参数
        const itemList = this.tableData.list.map((item) => {
          return {
            seq: String(item.seq || ''), // 序号
            outGoodsNo: String(item.outGoodsNo || ''), // 调入商品号
            inGoodsNo: String(item.inGoodsNo || ''), // 调出商品号
            grossWeightSum: String(item.grossWeightRowSum || ''), // 毛重
            netWeightSum: String(item.netWeightRowSum || ''), // 净重
            num: String(item.transferNum || ''), // 调出数量
            price: String(item.price || ''), // 调拨单价
            outBarcode: item.outBarcode + '', // 条形码
            torrNo: String(item.torrNo || ''), // 托盘号
            boxNum: String(item.boxNum || '') // 箱数
          }
        })
        exportFilePost(exportItemsUrl, { json: JSON.stringify({ itemList }) })
      },
      // 商品导入成功
      successUpload(e) {
        const uploadSuccessFlag =
          e?.data?.failure < 1 && e?.data?.data?.length > 0
        if (!uploadSuccessFlag) {
          return
        }
        const uploadList = e.data.data
        this.importDialogVisible.show = false
        this.$confirm('确认覆盖已存在的所有商品信息？', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const {
            inDepotType,
            outDepotType,
            ruleForm: { inDepotId, tallyingUnit }
          } = this
          // 根据ids、单据类型、仓库id获取商品信息
          const { data } = await getListByIdsAndTypeAndInOutDepot({
            type: 4,
            ids: uploadList.map(({ outGoodsId }) => outGoodsId).toString(),
            depotId: inDepotId
          })
          if (!data?.length) return
          this.tableData.list = data.map((item) => {
            const importData =
              uploadList.find(
                (gtem) => +gtem.outGoodsId === +item.outGoodsId
              ) || {}
            return {
              ...importData,
              ...item,
              inGoodsId: importData.inGoodsId || item.inGoodsId,
              inGoodsNo: importData.inGoodsNo || item.inGoodsNo,
              inGoodsCode: importData.inGoodsCode || item.inGoodsCode,
              inGoodsName: importData.inGoodsName || item.inGoodsName,
              inUnit: inDepotType === 'c' ? tallyingUnit : '02',
              inTransferNum: importData.num || '',
              outUnit: outDepotType === 'c' ? tallyingUnit : '02',
              transferNum: importData.num || 0,
              grossWeightRowSum:
                importData.grossWeightSum || importData.grossWeight || 0,
              netWeightRowSum:
                importData.netWeightSum || importData.netWeight || 0,
              price: importData.price || '',
              contNo: '',
              bargainno: '',
              remark: ''
            }
          })
          this.calcTableTotal()
        })
      },
      // 打开箱装明细导入
      handleClickImportPacking() {
        // 判断是否存在一个调入商品
        const importPackingItemList = this.tableData.list
          .map((item) => ({
            // 调入商品id
            inGoodsId: item.inGoodsId,
            // 调入商品货号
            inGoodsNo: item.inGoodsNo,
            // 调入条形码
            inBarcode: item.outBarcode
          }))
          .filter((item) => item.inGoodsId)
        if (!importPackingItemList.length) {
          return this.$errorMsg('需存在调入商品才可以导入商品')
        }
        this.importPackingData = {
          itemList: JSON.stringify(importPackingItemList)
        }
        this.importDialogTitle = '导入箱装明细'
        this.isImportGoods = false
        this.importDialogVisible.show = true
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
        this.importDialogVisible.show = false
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
      /* 根据调入，调出仓库，是否关区设置 部分字段是否必填 */
      async reloadRequiredField() {
        const { outDepotId, inDepotId, isSameArea } = this.ruleForm
        if (!outDepotId || !inDepotId) return
        const { data } = await getMustParameter({
          outDepotId,
          inDepotId,
          isSameArea
        })
        const rquiredFieldList = data?.replace(/Label/gi, '')?.split(',') || ''
        // 受控制的必填字段
        const controlFieldJson = {
          contractNo: '合同号',
          invoiceNo: '发票号',
          packType: '包装方式',
          cartons: '箱数',
          firstLadingBillNo: '提单号',
          consigneeUsername: '收货人名称',
          depotScheduleId: '调入仓地址',
          country: '国家',
          destination: '目的地',
          portLoading: '装货港'
        }
        // 循环设置受控制的字段
        for (const key in controlFieldJson) {
          this.rules[key].required = rquiredFieldList.includes(key + '')
        }
      },
      /* 切换调入调出仓库，更新商品列表 转销货号 changeType === out / in */
      async updateGoodsList(changeType) {
        const {
          outDepotType,
          inDepotType,
          ruleForm: { outDepotId, tallyingUnit, inDepotId }
        } = this
        // 仓库类型
        const depotType = changeType === 'out' ? outDepotType : inDepotType
        const depotId = changeType === 'out' ? outDepotId : inDepotId
        const list = this.tableData.list
        if (!outDepotId || !inDepotId || !list.length) return
        // 获取更新信息
        const { data: updateOutGoods } = await getOrderUpdateMerchandiseInfo({
          depotId,
          itemList: list.map(({ outBarcode: barcode }, index) => ({
            barcode,
            index
          })),
          orderType: 4
        })
        list.forEach((rowData, index) => {
          const updateRowData = updateOutGoods[index].merchandiseInfoModel
          if (updateRowData) {
            const {
              id,
              name,
              goodsCode,
              goodsNo,
              grossWeight,
              netWeight,
              filingPrice,
              priceDeclareRatio
            } = updateRowData
            // 改变的是调出仓
            if (changeType === 'out') {
              // 调出商品改变
              rowData.outGoodsId = id
              rowData.outGoodsName = name
              rowData.outGoodsCode = goodsCode
              rowData.outGoodsNo = goodsNo
              // 调入商品清空
              rowData.inGoodsId = ''
              rowData.inGoodsName = ''
              rowData.inGoodsCode = ''
              rowData.inGoodsNo = ''
              // 单个毛重
              rowData.grossWeight = grossWeight || 0
              // 单个净重
              rowData.netWeight = netWeight || 0
              // 调拨单价
              rowData.price = filingPrice * (priceDeclareRatio || 1) || 0
              // 调出单位
              rowData.outUnit = depotType === 'c' ? tallyingUnit : '02'
              // 改变调出数量
              this.calcRowWeightSum(index)
            }
            // 改变的是调入仓
            if (changeType === 'in') {
              // 调入商品覆盖
              rowData.inGoodsId = id
              rowData.inGoodsName = name
              rowData.inGoodsCode = goodsCode
              rowData.inGoodsNo = goodsNo
              // 调入单位
              rowData.inUnit = depotType === 'c' ? tallyingUnit : '02'
            }
          }
        })
        // 计算总和
        this.calcTableTotal()
      },
      // 获取参数
      getParms() {
        const { id: isEdit, isCopy } = this.$route.query
        const params = {}
        // 转字符串
        for (const key in this.ruleForm) {
          params[key] = String(this.ruleForm[key] || '')
        }
        delete params.id
        delete params.code
        // 编辑
        if (isEdit) params.orderId = isEdit
        // 复制
        if (isCopy) delete params.orderId
        // 调入仓地址
        params.depotScheduleAddress =
          this.selectOpt?.depotAddList?.find(
            ({ key }) => key === params.depotScheduleId
          )?.value || ''
        // 调入仓库名称
        params.inDepotName =
          this.selectOpt?.inDepotIdList?.find(
            ({ key }) => key === params.inDepotId
          )?.value || ''
        // 调出出库名称
        params.outDepotName =
          this.selectOpt?.outDepotIdList?.find(
            ({ key }) => key === params.outDepotId
          )?.value || ''
        // 调入调出客户只能選擇本公司
        params.inCustomerId = params.merchantId
        params.inCustomerName = params.merchantName
        // 商品列表
        params.goodsList = this.tableData.list
          .filter((item) => item.outGoodsId)
          .map((item) => {
            const result = {
              ...item,
              grossWeightSum: item.grossWeightRowSum,
              netWeightSum: item.netWeightRowSum
            }
            Object.keys(result).forEach((key) => {
              result[key] = String(result[key] || '')
            })
            return result
          })
        // 箱装明细
        params.packingList = this.packingTableData.list
        return params
      },
      // 保存不需要校验
      async save() {
        if (!this.ruleForm.buId) {
          return this.$errorMsg('事业部不能为空')
        }
        this.saveloading = true
        try {
          const {
            data: { code, message }
          } = await saveTempTransferOrder({
            json: JSON.stringify(this.getParms())
          })
          if (code === '00') {
            this.$successMsg('保存成功')
            this.closeTag()
          } else {
            this.$errorMsg(message)
          }
        } finally {
          this.saveloading = false
        }
      },
      // 校验提交审核参数
      async checkData() {
        const { outDepotType, inDepotType } = this

        // 校验商品列表参数
        const isValidGoodsList = this.tableData.list.every((item, index) => {
          const {
            outGoodsId,
            transferNum,
            inGoodsId,
            inTransferNum,
            price,
            grossWeightRowSum,
            netWeightRowSum,
            contNo,
            bargainno
          } = item
          const errTips = (tips) => {
            this.$errorMsg(`表格第${index + 1}行,${tips}`)
            return false
          }
          if (!outGoodsId) return errTips('请选择调出商品！')
          if (!inGoodsId) return errTips('请选择调入商品！')
          if (!transferNum || transferNum < 1) {
            return errTips('请输入调出数量且不能小于0')
          }
          if (!inTransferNum || inTransferNum < 1) {
            return errTips('请输入调入数量且不能小于0')
          }
          if (!price || price <= 0) return errTips('请输入调拨单价且不能小于0')
          if (!grossWeightRowSum) return errTips('【商品信息】毛重不能为空！')
          if (!netWeightRowSum) return errTips('【商品信息】净重不能为空！')
          if (parseFloat(grossWeightRowSum) < parseFloat(netWeightRowSum)) {
            return errTips('毛重不能小于净重，调出商品货号')
          }
          if (outDepotType === 'c' && inDepotType === 'a') {
            if (!contNo) return errTips('【商品信息】箱号不能为空！')
            if (!bargainno) return errTips('【商品信息】合同号不能为空！')
          }
          return true
        })
        if (!isValidGoodsList) return false

        // 校验库存可用量
        const { isInventoryValidate } = await this.$checkInventoryNum({
          itemList: this.tableData.list.map((item) => {
            return {
              goodsId: item.outGoodsId,
              goodsNo: item.outGoodsNo,
              okNum: item.transferNum || 0,
              tallyingUnit: this.ruleForm.tallyingUnit || '',
              depotId: this.ruleForm.outDepotId || '',
              inventoryType: 2
            }
          })
        })
        if (!isInventoryValidate) return false

        // 存在排序序号为空的商品
        const haveNullSeq = this.tableData.list.find((item) =>
          ['', null, undefined].includes(item.seq)
        )
        if (haveNullSeq) {
          const isComfirmHandle = await this.$showToast({
            content: '存在排序序号为空的商品，是否按现有排序提交？',
            type: 'warning'
          })
          if (!isComfirmHandle) return false
        }

        return true
      },
      // 保存并审核：1.二次确认是否审核 2.校验必填项 3.校验库存可用量 4.提交保存
      async auditForm() {
        const isContinue = await this.$showToast({
          content: '确定保存并审核该调拨订单?',
          type: 'warning'
        })
        if (!isContinue) return
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return this.$errorMsg('请填写必填信息')
          // 校验参数
          const isValidate = await this.checkData()
          if (!isValidate) return
          // 参数
          this.auditFormBtnLoading = true
          const submitParam = {
            json: JSON.stringify(this.getParms())
          }
          try {
            const isSaveFlag = this.ruleForm.id && !this.$route.query.isCopy
            const {
              data: { code, message }
            } = isSaveFlag
              ? await updateTransferOrder(submitParam)
              : await saveTransferOrder(submitParam)
            if (code === '00') {
              this.$successMsg('保存成功')
              this.closeTag()
            } else {
              this.$errorMsg(message)
            }
          } catch {
          } finally {
            this.auditFormBtnLoading = false
          }
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.all-order-edit-bx .el-form-item__label {
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
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  .image-preview {
    display: inline-block;
  }
  .flex-bw {
    display: flex;
    justify-content: space-between;
  }
</style>
