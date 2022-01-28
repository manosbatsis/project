<template>
  <div class="page-bx bgc-w audit-for-oa">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->

    <JFX-Form class="mr-t-20" :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 框架合同信息 -->
      <JFX-title title="框架合同信息" className="mr-t-20" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="是否为框架下的订单：" prop="frameContractFlag">
            <el-radio-group
              v-model="ruleForm.frameContractFlag"
              :disabled="contractDisable"
            >
              <el-radio :label="true">是</el-radio>
              <el-radio :label="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="框架合同编号：" prop="frameContractNo">
            <el-input
              v-model.trim="ruleForm.frameContractNo"
              placeholder="请输入"
              clearable
              :disabled="contractDisable"
            />
          </el-form-item>
        </el-col>
        <!-- 采购方式!=试单不可改, 采购方式=试单，可以修改 -->
        <el-col
          v-if="ruleForm.purchaseMethod === '3'"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="试单申请编号：" prop="tryApplyCode">
            <el-input
              v-model.trim="ruleForm.tryApplyCode"
              placeholder="请输入"
              clearable
              :disabled="contractDisable"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="业务员：" prop="businessManager">
            <el-select
              v-model="ruleForm.businessManager"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in userList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="合同模板：" prop="contractVersion">
            <!-- 采购方式!=试单不可改, 采购方式=试单，可以修改 -->
            <el-select
              v-model="ruleForm.contractVersion"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="ruleForm.purchaseMethod !== '3'"
              :data-list="
                getSelectList('purchaseFrameContract_contractVersionList')
              "
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_contractVersionList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="我司签约主体：" prop="merchantName">
            <el-input
              v-model.trim="ruleForm.merchantName"
              placeholder="请输入"
              clearable
              :disabled="contractDisable"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="供应商名称：" prop="supplierName">
            <el-input
              v-model.trim="ruleForm.supplierName"
              placeholder="请输入"
              clearable
              :disabled="contractDisable"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="供应商类型：" prop="supplierType">
            <el-select
              v-model="ruleForm.supplierType"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="tempContractDisable.supplierType"
              :data-list="
                getSelectList('purchaseFrameContract_supplierTypeList')
              "
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_supplierTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col
          v-if="ruleForm.supplierType === '2'"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="其他供应商类型：" prop="otherSupplierType">
            <el-input
              v-model.trim="ruleForm.otherSupplierType"
              placeholder="请输入"
              clearable
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <!-- :disabled="ruleForm.purchaseMethod !== '3'" -->
          <el-form-item label="协议开始日期：" prop="contractStartTime">
            <el-date-picker
              v-model="ruleForm.contractStartTime"
              type="date"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              clearable
              :disabled="
                tempContractDisable.contractStartTime &&
                ruleForm.purchaseMethod !== '3'
              "
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <!-- :disabled="ruleForm.purchaseMethod !== '3'" -->
          <el-form-item label="协议结束日期：" prop="contractEndTime">
            <el-date-picker
              v-model="ruleForm.contractEndTime"
              type="date"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              clearable
              :disabled="
                tempContractDisable.contractEndTime &&
                ruleForm.purchaseMethod !== '3'
              "
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购类型：" prop="purchaseType">
            <el-select
              v-model="ruleForm.purchaseType"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="tempContractDisable.purchaseType"
              :data-list="
                getSelectList('purchaseFrameContract_purchaseTypeList')
              "
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_purchaseTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="商品类型：" prop="comtyType">
            <el-select
              v-model="ruleForm.comtyType"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="tempContractDisable.comtyType"
              :data-list="getSelectList('purchaseFrameContract_comtyTypeList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_comtyTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 商品类型为其他才显示 -->
        <el-col
          v-if="ruleForm.comtyType === '7'"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="其他商品类型：" prop="otherComty">
            <el-input
              v-model.trim="ruleForm.otherComty"
              placeholder="请输入"
              clearable
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="对应财务经理：" prop="financeManager">
            <!-- 采购方式!=试单不可改, 采购方式=试单，可以修改 -->
            <!-- :disabled="ruleForm.purchaseMethod !== '3'" -->
            <el-select
              v-model="ruleForm.financeManager"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="
                tempContractDisable.financeManager &&
                ruleForm.purchaseMethod !== '3'
              "
              :data-list="
                getSelectList('purchaseFrameContract_financeManagerList')
              "
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_financeManagerList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 框架合同信息 end -->

      <!-- 采购信息 -->
      <JFX-title title="采购信息" className="mr-t-20" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购订单编号：" prop="poNo">
            <el-input
              type="textarea"
              v-model.trim="ruleForm.poNo"
              placeholder="请输入"
              clearable
              :autosize="{ minRows: 1, maxRows: 4 }"
              :disabled="purchaseDisabled"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购方式：" prop="purchaseMethod">
            <el-select
              v-model="ruleForm.purchaseMethod"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="purchaseDisabled"
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
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="交货方式：" prop="tradeTerms">
            <el-select
              v-model="ruleForm.tradeTerms"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="purchaseDisabled"
              :data-list="getSelectList('purchaseOrder_tradeTermsList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseOrder_tradeTermsList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 交货方式选择其他，则显示 -->
        <el-col
          v-if="ruleForm.tradeTerms === '8'"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="其他交货方式：" prop="otherTradeTerms">
            <el-input
              v-model.trim="ruleForm.otherTradeTerms"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="合作模式：" prop="cooperationModel">
            <el-select
              v-model="ruleForm.cooperationModel"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getSelectList('purchaseOrder_cooperationModeList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseOrder_cooperationModeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 合作模式选择其他，则显示 -->
        <el-col
          v-if="ruleForm.cooperationModel === '5'"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="其他合作模式：" prop="otherCooperationModel">
            <el-input
              v-model.trim="ruleForm.otherCooperationModel"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购可用额度：" prop="purchaseQuota">
            <JFX-Input
              v-model.trim="ruleForm.purchaseQuota"
              :precision="6"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            >
              <span slot="append">万元人民币</span>
            </JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="SkU数量（个）：" prop="skuNum">
            <JFX-Input
              v-model.trim="ruleForm.skuNum"
              :precision="0"
              :min="0"
              :disabled="purchaseDisabled"
              clearable
              style="width: 100%"
              placeholder="请输入"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购总数量（件）：" prop="purchaseTotalNum">
            <JFX-Input
              v-model.trim="ruleForm.purchaseTotalNum"
              :precision="0"
              :min="0"
              :disabled="purchaseDisabled"
              clearable
              style="width: 100%"
              placeholder="请输入"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购币种：" prop="purchaseCurreny">
            <el-select
              v-model="ruleForm.purchaseCurreny"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :disabled="purchaseDisabled"
              :list-data="getCurrencySelectBean('currencyList')"
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
          <el-form-item label="采购总金额：" prop="purchaseAmount">
            <JFX-Input
              v-model.trim="ruleForm.purchaseAmount"
              :precision="6"
              :min="0"
              :disabled="purchaseDisabled"
              clearable
              style="width: 100%"
              placeholder="请输入"
            >
              <span slot="append">万元</span>
            </JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="预计毛利率（%）：" prop="esGrossMargin">
            <JFX-Input
              v-model.trim="ruleForm.esGrossMargin"
              :precision="2"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购折算人民币金额：" prop="purchaseRmbAmount">
            <JFX-Input
              v-model.trim="ruleForm.purchaseRmbAmount"
              :precision="6"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            >
              <span slot="append">万元人民币</span>
            </JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="新品单次采购金额："
            prop="newProductSingleAmount"
          >
            <JFX-Input
              v-model.trim="ruleForm.newProductSingleAmount"
              :precision="6"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            >
              <span slot="append">万元人民币</span>
            </JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="补货单次采购金额："
            prop="supplyProductSingleAmount"
          >
            <JFX-Input
              v-model.trim="ruleForm.supplyProductSingleAmount"
              :precision="6"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            >
              <span slot="append">万元人民币</span>
            </JFX-Input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 采购信息 end -->

      <!-- 客户信息 -->
      <!-- 采购方式选了以销定采，则显示客户信息标签内容 -->
      <template v-if="ruleForm.purchaseMethod === '0'">
        <JFX-title title="客户信息" className="mr-t-20" />
        <el-row :gutter="10" class="fs-14 clr-II">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="客户名称：" prop="customerId">
              <el-select
                v-model="ruleForm.customerId"
                placeholder="请选择"
                clearable
                filterable
                style="width: 100%"
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
            <el-form-item label="客户预付款：" prop="hasCustomerPrepayment">
              <el-radio-group
                v-model="ruleForm.hasCustomerPrepayment"
                @change="hasCustomerPrepaymentChange"
              >
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col
            v-if="!!ruleForm.hasCustomerPrepayment"
            :xs="24"
            :sm="12"
            :md="12"
            :lg="8"
            :xl="6"
          >
            <el-form-item
              label="客户预付款币种："
              prop="preCustomerPaymentCurreny"
            >
              <el-select
                v-model="ruleForm.preCustomerPaymentCurreny"
                style="width: 100%"
                placeholder="请选择"
                filterable
                clearable
                :list-data="getCurrencySelectBean('currencyList')"
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
          <el-col
            v-if="!!ruleForm.hasCustomerPrepayment"
            :xs="24"
            :sm="12"
            :md="12"
            :lg="8"
            :xl="6"
          >
            <el-form-item
              label="客户预付款金额："
              prop="preCustomerPaymentAmount"
            >
              <JFX-Input
                v-model.trim="ruleForm.preCustomerPaymentAmount"
                :precision="6"
                :min="0"
                clearable
                style="width: 100%"
                placeholder="请输入"
              >
                <span slot="append">万元人民币</span>
              </JFX-Input>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <!-- 客户信息 end -->

      <!-- 结算信息 -->
      <JFX-title title="结算信息" className="mr-t-20" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="结算方式：" prop="settlementModel">
            <el-select
              v-model="ruleForm.settlementModel"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              disabled
              :data-list="getSelectList('purchaseFrameContract_settleMentList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_settleMentList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="结算条款：" prop="settlementProvision">
            <el-select
              disabled
              v-model="ruleForm.settlementProvision"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="
                getSelectList('purchaseOrder_settlementProvisionList')
              "
            >
              <el-option
                v-for="item in selectOpt.purchaseOrder_settlementProvisionList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col
          v-if="['2', '3'].includes(ruleForm.settlementProvision)"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="结算账期：" prop="settlementDate">
            <JFX-Input
              v-model.trim="ruleForm.settlementDate"
              :precision="0"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            >
              <span slot="append">自然日</span>
            </JFX-Input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="是否有预付款：" prop="hasPrepayment">
            <el-radio-group v-model="ruleForm.hasPrepayment" disabled>
              <el-radio :label="true">是</el-radio>
              <el-radio :label="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <!-- 预付款值为有，则显示 -->
        <el-col
          v-if="!!ruleForm.hasPrepayment"
          class="mr-b-0"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="预付款币种：" prop="prePaymentCurrency">
            <el-select
              v-model="ruleForm.prePaymentCurrency"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :list-data="getCurrencySelectBean('currencyList')"
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
        <!-- 预付款值为有，则显示 -->
        <el-col
          v-if="!!ruleForm.hasPrepayment"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="预付款金额：" prop="prePaymentAmount">
            <JFX-Input
              v-model.trim="ruleForm.prePaymentAmount"
              :precision="2"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="约定银行变更条款：" prop="bankChangeProvision">
            <el-input
              v-model.trim="ruleForm.bankChangeProvision"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 结算信息 end -->
      <!-- 合同其他信息 -->
      <JFX-title title="合同其他信息" className="mr-t-20" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :span="24">
          <el-form-item
            label="供应商描述："
            prop="supplierDesc"
            class="textarea__container"
          >
            <el-input
              type="textarea"
              v-model="ruleForm.supplierDesc"
              style="width: 100%"
              :autosize="{ minRows: 1, maxRows: 5 }"
              placeholder="请输入"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="供应商商品描述："
            prop="supplierMerchandiseDesc"
            class="textarea__container"
          >
            <el-input
              type="textarea"
              v-model="ruleForm.supplierMerchandiseDesc"
              style="width: 100%"
              :autosize="{ minRows: 1, maxRows: 5 }"
              placeholder="请输入"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 合同其他信息 end -->

      <!-- 用印基本信息 -->
      <JFX-title title="用印基本信息" className="mr-t-20" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="用印顺序：" prop="sealOrder">
            <el-select
              v-model="ruleForm.sealOrder"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getSelectList('purchaseFrameContract_sealOrderList')"
              @change="changeSealOrder"
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_sealOrderList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col
          v-if="ruleForm.sealOrder !== '2'"
          class="mr-b-0"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
        >
          <el-form-item label="用印类型：" prop="sealType">
            <el-select
              v-model="ruleForm.sealType"
              style="width: 100%"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getSelectList('purchaseFrameContract_sealTypeList')"
            >
              <el-option
                v-for="item in selectOpt.purchaseFrameContract_sealTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 用印基本信息 end -->
      <!-- 文件信息 -->
      <JFX-title title="文件信息" className="mr-t-20" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col :span="24">
          <el-form-item
            class="upload__container"
            label="盖章文件上传："
            required
          >
            <div>
              <div
                class="upload__container__item"
                v-for="(item, index) in sealUploadList"
                :key="item.attachmentCode"
              >
                <span
                  @click="
                    downloadAttachmen(item.attachmentUrl, item.attachmentName)
                  "
                >
                  {{ item.attachmentName || '' }}
                </span>
                <span @click="sealUploadList.splice(index, 1)">
                  <i class="el-icon-circle-close"></i>
                </span>
              </div>
            </div>
            <JFX-upload
              :url="sealUploadUrl"
              :limit="10000"
              :multiple="false"
              @success="onSealUpload"
            >
              <el-button type="primary"> 上传文件 </el-button>
            </JFX-upload>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item class="upload__container" label="其他辅助性文件上传：">
            <div>
              <div
                class="upload__container__item"
                v-for="item in otherUploadList"
                :key="item.attachmentCode"
              >
                <span
                  @click="
                    downloadAttachmen(item.attachmentUrl, item.attachmentName)
                  "
                >
                  {{ item.attachmentName || '' }}
                </span>
                <span @click="otherUploadList.splice(index, 1)">
                  <i class="el-icon-circle-close"></i>
                </span>
              </div>
            </div>
            <JFX-upload
              :url="otherUploadUrl"
              :limit="10000"
              :multiple="false"
              @success="onotherUpload"
            >
              <el-button type="primary"> 上传文件 </el-button>
            </JFX-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 文件信息 end -->
    </JFX-Form>

    <!-- 底部按钮 -->
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" @click="onSubmit" :loading="saveLoading">
        提 交
      </el-button>
      <el-button @click="onCancel">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  import {
    getOAAuditPage,
    callOAAudit,
    listContract,
    uploadFilesWithoutSave,
    getUserInfoHasCode
  } from '@a/purchaseManage'
  import { downloadFileUrl } from '@a/base'
  export default {
    mixins: [commomMix],
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          frameContractDataId: '',
          tryApplyDataId: '',
          poNo: '',
          /* 框架合同信息 */
          frameContractFlag: '',
          frameContractNo: '',
          tryApplyCode: '',
          businessManager: '',
          contractVersion: '',
          merchantName: '',
          merchantId: '',
          ourContSignComy: '',
          counterpartContSignComy: '',
          supplierName: '',
          supplierId: '',
          supplierType: '',
          otherSupplierType: '',
          contractStartTime: '',
          contractEndTime: '',
          purchaseType: '',
          comtyType: '',
          otherComty: '',
          financeManager: '',
          buId: '',
          buName: '',
          /* 采购信息 */
          codes: '',
          purchaseMethod: '',
          tradeTerms: '',
          otherTradeTerms: '',
          cooperationModel: '',
          otherCooperationModel: '',
          purchaseQuota: '',
          skuNum: '',
          purchaseTotalNum: '',
          purchaseCurreny: '',
          purchaseAmount: '',
          esGrossMargin: '',
          purchaseRmbAmount: '',
          newProductSingleAmount: '',
          supplyProductSingleAmount: '',
          /* 客户信息 */
          customerId: '',
          customerName: '',
          customerCode: '',
          hasCustomerPrepayment: '',
          preCustomerPaymentCurreny: '',
          preCustomerPaymentAmount: '',
          /* 结算信息 */
          settlementModel: '',
          settlementProvision: '',
          settlementDate: '',
          hasPrepayment: '',
          prePaymentCurrency: '',
          prePaymentAmount: '',
          bankChangeProvision: '',
          /* 合同其他信息 */
          supplierDesc: '',
          supplierMerchandiseDesc: '',
          /* 用印基本信息 */
          sealOrder: '',
          sealType: '',
          /* 文件信息 */
          contractUrlList: '',
          commonUrlList: ''
        },
        rules: {
          /* 框架合同信息 */
          frameContractFlag: {
            required: true,
            message: '是否为框架下的订单不能为空',
            trigger: 'change'
          },
          frameContractNo: {
            required: true,
            message: '框架合同编号不能为空',
            trigger: 'blur'
          },
          tryApplyCode: {
            required: true,
            message: '试单申请编号不能为空',
            trigger: 'blur'
          },
          businessManager: {
            required: true,
            message: '业务员不能为空',
            trigger: 'change'
          },
          contractVersion: {
            required: true,
            message: '合同模板不能为空',
            trigger: 'change'
          },
          merchantName: {
            required: true,
            message: '我司签约主体不能为空',
            trigger: 'blur'
          },
          supplierName: {
            required: true,
            message: '供应商名称不能为空',
            trigger: 'blur'
          },
          supplierType: {
            required: true,
            message: '供应商类型不能为空',
            trigger: 'change'
          },
          otherSupplierType: {
            required: true,
            message: '其他供应商类型不能为空',
            trigger: 'blur'
          },
          contractStartTime: {
            required: true,
            message: '协议开始日期不能为空',
            trigger: 'change'
          },
          contractEndTime: {
            required: true,
            message: '协议结束日期不能为空',
            trigger: 'change'
          },
          purchaseType: {
            required: true,
            message: '供应商名称不能为空',
            trigger: 'change'
          },
          comtyType: {
            required: true,
            message: '商品类型不能为空',
            trigger: 'change'
          },
          otherComty: {
            required: true,
            message: '其他商品类型不能为空',
            trigger: 'blur'
          },
          financeManager: {
            required: true,
            message: '对应财务经理不能为空',
            trigger: 'change'
          },
          /* 采购信息 */
          poNo: {
            required: true,
            message: '采购订单编号不能为空',
            trigger: 'blur'
          },
          purchaseMethod: {
            required: true,
            message: '采购方式不能为空',
            trigger: 'change'
          },
          tradeTerms: {
            required: true,
            message: '交货方式不能为空',
            trigger: 'change'
          },
          otherTradeTerms: {
            required: true,
            message: '其他交货方式不能为空',
            trigger: 'blur'
          },
          cooperationModel: {
            required: true,
            message: '合作模式不能为空',
            trigger: 'change'
          },
          otherCooperationModel: {
            required: true,
            message: '其他合作模式不能为空',
            trigger: 'blur'
          },
          purchaseQuota: {
            required: true,
            message: '采购可用额度不能为空',
            trigger: 'blur'
          },
          skuNum: {
            required: true,
            message: 'SkU数量不能为空',
            trigger: 'blur'
          },
          purchaseTotalNum: {
            required: true,
            message: '采购总数量不能为空',
            trigger: 'blur'
          },
          purchaseCurreny: {
            required: true,
            message: '采购币种不能为空',
            trigger: 'change'
          },
          purchaseAmount: {
            required: true,
            message: '采购总金额不能为空',
            trigger: 'blur'
          },
          esGrossMargin: {
            required: true,
            message: '预计毛利率不能为空',
            trigger: 'blur'
          },
          purchaseRmbAmount: {
            required: true,
            message: '采购折算人民币金额不能为空',
            trigger: 'blur'
          },
          newProductSingleAmount: {
            required: true,
            message: '新品单次采购金额不能为空',
            trigger: 'blur'
          },
          supplyProductSingleAmount: {
            required: true,
            message: '补货单次采购金额不能为空',
            trigger: 'blur'
          },
          /* 客户信息 */
          customerId: {
            required: true,
            message: '客户名称不能为空',
            trigger: 'change'
          },
          hasCustomerPrepayment: {
            required: true,
            message: '客户预付款不能为空',
            trigger: 'change'
          },
          preCustomerPaymentCurreny: {
            required: true,
            message: '客户预付款币种不能为空',
            trigger: 'change'
          },
          preCustomerPaymentAmount: {
            required: true,
            message: '客户预付款金额不能为空',
            trigger: 'blur'
          },
          /* 结算信息 */
          settlementModel: {
            required: true,
            message: '结算方式不能为空',
            trigger: 'change'
          },
          settlementProvision: {
            required: true,
            message: '结算条款不能为空',
            trigger: 'change'
          },
          settlementDate: {
            required: true,
            message: '结算账期不能为空',
            trigger: 'blur'
          },
          hasPrepayment: {
            required: true,
            message: '是否有预付款不能为空',
            trigger: 'change'
          },
          prePaymentCurrency: {
            required: true,
            message: '预付款币种不能为空',
            trigger: 'change'
          },
          prePaymentAmount: {
            required: true,
            message: '预付款金额不能为空',
            trigger: 'blur'
          },
          bankChangeProvision: {
            required: true,
            message: '约定银行变更条款不能为空',
            trigger: 'blur'
          },
          /* 合同其他信息 */
          supplierDesc: {
            required: true,
            message: '供应商描述不能为空',
            trigger: 'blur'
          },
          supplierMerchandiseDesc: {
            required: true,
            message: '供应商商品描述不能为空',
            trigger: 'blur'
          },
          /* 用印基本信息 */
          sealOrder: {
            required: true,
            message: '用印顺序不能为空',
            trigger: 'change'
          },
          sealType: {
            required: true,
            message: '用印类型不能为空',
            trigger: 'change'
          }
        },
        /* 按钮的loading状态 */
        saveLoading: false,
        /* 框架合同的交货信息 */
        frameContractDeliveryType: '',
        /* 用户列表 */
        userList: [],
        /* 采购订单的id集合 */
        purchaseIds: '',
        /* 盖章文件上传url */
        sealUploadUrl: '',
        /* 其他辅助性文件上传url */
        otherUploadUrl: '',
        /* 盖章文件上传列表 */
        sealUploadList: [],
        /* 其他辅助性文件上传列表 */
        otherUploadList: [],
        /* 框架合同信息禁用状态 */
        contractDisable: true,
        /* Fix: 临时方案针对某几个字段禁用 */
        tempContractDisable: {
          supplierType: true,
          contractStartTime: true,
          contractEndTime: true,
          purchaseType: true,
          comtyType: true,
          financeManager: true
        },
        /* 采购信息禁用状态 */
        purchaseDisabled: true
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        const { ids } = this.$route.query
        if (ids) {
          this.purchaseIds = ids
          /* 获取表单数据 */
          this.getFormData()
          /* 获取下拉列表 */
          this.getAllSelectList()
        }
      },
      /* 获取表单数据 */
      async getFormData() {
        /* Fix: 临时方案针对某几个字段禁用 */
        const tempDisableFields = [
          'supplierType',
          'contractStartTime',
          'contractEndTime',
          'purchaseType',
          'comtyType',
          'financeManager'
        ]
        try {
          const { data } = await getOAAuditPage({ ids: this.purchaseIds })
          Object.keys(this.ruleForm).forEach((key) => {
            if (typeof data[key] === 'boolean') {
              this.ruleForm[key] = data[key]
              return
            }
            this.ruleForm[key] = ![null, undefined].includes(data[key])
              ? data[key].toString()
              : ''
          })
          /* Fix: 临时方案针对某几个字段禁用 */
          tempDisableFields.forEach((field) => {
            this.tempContractDisable[field] = !!this.ruleForm[field]
          })
          if (this.ruleForm.sealOrder === '2') {
            this.ruleForm.sealType = '0'
          }
          /* 文件上传url */
          this.sealUploadUrl = this.otherUploadUrl =
            getBaseUrl(uploadFilesWithoutSave) +
            uploadFilesWithoutSave +
            '?token=' +
            sessionStorage.getItem('token') +
            '&code=' +
            this.$route.query.codes
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取下拉列表 */
      getAllSelectList() {
        /* 获取所有已启用用户 */
        this.getUserList()
        /* 获取附件列表 */
        this.getContractList()
      },
      /* 获取所有已启用用户 */
      async getUserList() {
        try {
          const { data } = await getUserInfoHasCode({
            disable: 0,
            begin: 0,
            pageSize: 999
          })
          this.userList = data?.map((item) => ({
            key: item.value,
            value: item.text
          }))
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取附件列表 */
      async getContractList() {
        try {
          const { data } = await listContract({
            codes: this.$route.query.codes
          })
          if (!data?.length) return false
          this.sealUploadList.push(...data)
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 保存 */
      onSubmit() {
        this.$refs.ruleForm.validate((valid) => {
          if (!valid) {
            this.$errorMsg('请正确填写表单必填项！')
            return false
          }
          if (!this.sealUploadList.length) {
            this.$errorMsg('至少上传一份盖章文件！')
            return false
          }
          const params = {
            ...this.ruleForm,
            contractList: this.sealUploadList,
            commonFileList: this.otherUploadList,
            hasCustomerPrepayment: !!this.ruleForm.hasCustomerPrepayment,
            ids: this.$route.query.ids
          }
          if (!this.ruleForm.hasCustomerPrepayment) {
            params.preCustomerPaymentCurreny = ''
          }
          try {
            this.saveLoading = true
            this.$showToast(
              '提交后就发起OA审批了，经分销就无法修改，需要OA审批人驳回发起人，才能修改二次提交。是否继续提交？',
              async () => {
                await callOAAudit(params)
                this.$successMsg('保存成功')
                this.closeTag()
              }
            )
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.saveLoading = false
          }
        })
      },
      /* 取消 */
      onCancel() {
        this.$confirm(
          '你将关闭该界面，数据不会被保存，是否继续关闭？',
          '提示',
          { type: 'warning' }
        ).then(() => {
          this.closeTag()
        })
      },
      /* 客户预付款切换 */
      hasCustomerPrepaymentChange(hasCustomerPrepayment) {
        if (hasCustomerPrepayment) {
          this.ruleForm.preCustomerPaymentCurreny =
            this.ruleForm.purchaseCurreny
        } else {
          this.ruleForm.preCustomerPaymentCurreny = ''
          this.ruleForm.preCustomerPaymentAmount = ''
        }
      },
      /* 盖章文件上传 */
      onSealUpload({ code, message, data: { attachmentInfo } }) {
        if (code !== '10000') {
          this.$errorMsg(message || '上传失败')
          return false
        }
        this.sealUploadList.push(attachmentInfo)
      },
      /* 其他辅助性文件上传 */
      onotherUpload({ code, message, data: { attachmentInfo } }) {
        if (code !== '10000') {
          this.$errorMsg(message || '上传失败')
          return false
        }
        this.otherUploadList.push(attachmentInfo)
      },
      /* 下载附件 */
      downloadAttachmen(url, fileName) {
        this.$exportFile(downloadFileUrl, {
          url: encodeURI(url),
          fileName: encodeURI(fileName)
        })
      },
      /* 用印顺序改变 */
      changeSealOrder(sealOrder) {
        if (sealOrder === '2') {
          this.ruleForm.sealType = '0'
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.audit-for-oa {
    .el-form-item__label {
      text-align: right;
      vertical-align: middle;
      font-size: 14px;
      color: #606266;
      line-height: 40px;
      padding: 0 4px 0 0;
      box-sizing: border-box;
      width: 160px;
    }
    .el-form-item {
      display: flex;
      align-items: center;
      margin-bottom: 14px;
    }
    .el-form-item__content {
      width: calc(100% - 160px);
      box-sizing: border-box;
      padding-right: 20px;
    }
    .el-input-group__append {
      padding: 0 4px;
    }
  }

  ::v-deep.textarea__container {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 160px;
    }
    .el-form-item__content {
      flex: 1;
      padding-right: 20px;
    }
  }

  ::v-deep.upload__container {
    display: flex;
    align-items: flex-start !important;
    > div {
      min-height: 40px;
      line-height: 40px;
    }
    &__item {
      display: inline-block;
      margin-right: 10px;
      > span {
        cursor: pointer;
      }
      > span:hover {
        color: blue;
      }
      :last-child {
        width: 30px;
      }
    }
  }
</style>
