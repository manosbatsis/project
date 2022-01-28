<template>
  <!-- 供应商新增/编辑页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-Form :model="baseForm" ref="baseForm" :rules="rules">
      <JFX-title title="基本信息" className="mr-t-10" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="供应商名称：" prop="name">
            <el-input v-model="baseForm.name" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="供应商简称：" prop="simpleName">
            <el-input
              v-model="baseForm.simpleName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="营业执照号：" prop="creditCode">
            <el-input
              v-model="baseForm.creditCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="组织机构代码：" prop="orgCode">
            <el-input
              v-model="baseForm.orgCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="客户等级编码：" prop="codeGrade">
            <el-input
              v-model="baseForm.codeGrade"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="企业性质：" prop="nature">
            <el-input
              v-model="baseForm.nature"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="中文名：" prop="chinaName">
            <el-input
              v-model="baseForm.chinaName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="英文简称：" prop="enSimpleName">
            <el-input
              v-model="baseForm.enSimpleName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="英文名：" prop="enName">
            <el-input
              v-model="baseForm.enName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="主数据ID：" prop="mainId">
            <el-input
              v-model="baseForm.mainId"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="业务员：" prop="salesman">
            <el-input
              v-model="baseForm.salesman"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="省市区代码：" prop="cityCode">
            <el-input
              v-model="baseForm.cityCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="注册地：" prop="companyAddr">
            <el-input
              v-model="baseForm.companyAddr"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="企业地址：" prop="businessAddress">
            <el-input
              v-model="baseForm.businessAddress"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="企业英文地址：" prop="enBusinessAddress">
            <el-input
              v-model="baseForm.enBusinessAddress"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="采购币种：" prop="currency">
            <el-select
              v-model="baseForm.currency"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getCurrencySelectBean('currency_list')"
            >
              <el-option
                v-for="item in selectOpt.currency_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="是否内部公司：" prop="type">
            <el-select
              v-model="baseForm.type"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option value="1" label="是"></el-option>
              <el-option value="2" label="否"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
          v-if="baseForm.type === '1'"
        >
          <el-form-item label="内部公司：" prop="innerMerchantId">
            <el-select
              v-model="baseForm.innerMerchantId"
              placeholder="请选择"
              clearable
              filterable
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
          <el-form-item label="经营范围：" prop="operationScope">
            <el-input
              v-model="baseForm.operationScope"
              placeholder="请输入"
              clearable
              type="textarea"
              :autosize="{ minRows: 1, maxRows: 4 }"
              style="width: 193px"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="传真：" prop="fax">
            <el-input v-model="baseForm.fax" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="email：" prop="email">
            <el-input v-model="baseForm.email" placeholder="请输入" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注：" prop="remark" class="textarea-container">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4 }"
              v-model="baseForm.remark"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 基本信息 end-->
      <el-tabs v-model="activeName">
        <el-tab-pane label="银行信息" name="1">
          <el-row class="mr-b-10">
            <el-button type="primary" @click="addBank">新增</el-button>
            <el-button type="primary" @click="delBank">删除</el-button>
          </el-row>
          <!-- 银行信息 -->
          <JFX-table
            :tableData.sync="bankTableData"
            :tableColumn="bankTableColumn"
            :showPagination="false"
            showSelection
            @selection-change="bankTableSelectionChange"
          >
            <template slot="depositBank" slot-scope="{ row }">
              <el-input v-model="row.depositBank" clearable></el-input>
            </template>
            <template slot="bankAccount" slot-scope="{ row }">
              <el-input v-model="row.bankAccount" clearable></el-input>
            </template>
            <template slot="beneficiaryName" slot-scope="{ row }">
              <el-input v-model="row.beneficiaryName" clearable></el-input>
            </template>
            <template slot="bankAddress" slot-scope="{ row }">
              <el-input v-model="row.bankAddress" clearable></el-input>
            </template>
            <template slot="swiftCode" slot-scope="{ row }">
              <el-input v-model="row.swiftCode" clearable></el-input>
            </template>
            <template slot="merchantIdStr" slot-scope="{ row }">
              <el-select
                v-model="row.merchantIdStr"
                clearable
                filterable
                multiple
              >
                <el-option
                  v-for="item in selectOpt.bankCompanyList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </template>
          </JFX-table>
        </el-tab-pane>
        <el-tab-pane label="联系方式" name="2">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
              <el-form-item label="法人代表：" prop="legalPerson">
                <el-input
                  v-model="baseForm.legalPerson"
                  placeholder="请输入"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
              <el-form-item label="法人国籍：" prop="legalNationality">
                <el-input
                  v-model="baseForm.legalNationality"
                  placeholder="请输入"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
              <el-form-item label="法人代表身份证：" prop="legalCardNo">
                <el-input
                  v-model="baseForm.legalCardNo"
                  placeholder="请输入"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
              <el-form-item label="法人电话：" prop="legalTel">
                <el-input
                  v-model="baseForm.legalTel"
                  placeholder="请输入"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
              <el-form-item label="公司电话：" prop="oTelNo">
                <el-input
                  v-model="baseForm.oTelNo"
                  placeholder="请输入"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="资质信息" name="3" v-if="$route.query.id">
          <table class="table-container">
            <tr>
              <td style="width: 20%">资料名称</td>
              <td style="width: 40%">资料图片</td>
              <td style="width: 20%">修改人</td>
              <td style="width: 20%">修改时间</td>
            </tr>
            <tr>
              <td>营业执照副本</td>
              <td>
                <el-image
                  v-if="customerAptitude.charteredNo"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.charteredNo"
                  :preview-src-list="[customerAptitude.charteredNo]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{ 'edit-image-btn': customerAptitude.charteredNo }"
                  @click="uploadImage('1')"
                >
                  {{ customerAptitude.charteredNo ? '修改图片' : '上传图片' }}
                </el-button>
              </td>
              <td>{{ customerAptitude.chartModifierName }}</td>
              <td>{{ customerAptitude.chartModifyDate }}</td>
            </tr>
            <tr>
              <td>组织机构代码副本</td>
              <td>
                <el-image
                  v-if="customerAptitude.organizationCode"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.organizationCode"
                  :preview-src-list="[customerAptitude.organizationCode]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{
                    'edit-image-btn': customerAptitude.organizationCode
                  }"
                  @click="uploadImage('2')"
                >
                  {{
                    customerAptitude.organizationCode ? '修改图片' : '上传图片'
                  }}
                </el-button>
              </td>
              <td>{{ customerAptitude.orgModifierName }}</td>
              <td>{{ customerAptitude.orgModifyDate }}</td>
            </tr>
            <tr>
              <td>注册登记证</td>
              <td>
                <el-image
                  v-if="customerAptitude.registrationNo"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.registrationNo"
                  :preview-src-list="[customerAptitude.registrationNo]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{ 'edit-image-btn': customerAptitude.registrationNo }"
                  @click="uploadImage('3')"
                >
                  {{
                    customerAptitude.registrationNo ? '修改图片' : '上传图片'
                  }}
                </el-button>
              </td>
              <td>{{ customerAptitude.registModifierName }}</td>
              <td>{{ customerAptitude.registModifyDate }}</td>
            </tr>
            <tr>
              <td>供货记录</td>
              <td>
                <el-image
                  v-if="customerAptitude.supplyRecord"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.supplyRecord"
                  :preview-src-list="[customerAptitude.supplyRecord]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{ 'edit-image-btn': customerAptitude.supplyRecord }"
                  @click="uploadImage('4')"
                >
                  {{ customerAptitude.supplyRecord ? '修改图片' : '上传图片' }}
                </el-button>
              </td>
              <td>{{ customerAptitude.supplyModifierName }}</td>
              <td>{{ customerAptitude.supplyModifyDate }}</td>
            </tr>
            <tr>
              <td>证明信息</td>
              <td>
                <el-image
                  v-if="customerAptitude.proofInfo"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.proofInfo"
                  :preview-src-list="[customerAptitude.proofInfo]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{ 'edit-image-btn': customerAptitude.proofInfo }"
                  @click="uploadImage('5')"
                >
                  {{ customerAptitude.proofInfo ? '修改图片' : '上传图片' }}
                </el-button>
              </td>
              <td>{{ customerAptitude.proofModifierName }}</td>
              <td>{{ customerAptitude.proofModifyDate }}</td>
            </tr>
            <tr>
              <td>品牌授权</td>
              <td>
                <el-image
                  v-if="customerAptitude.brandAuthorization"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.brandAuthorization"
                  :preview-src-list="[customerAptitude.brandAuthorization]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{
                    'edit-image-btn': customerAptitude.brandAuthorization
                  }"
                  @click="uploadImage('6')"
                >
                  {{
                    customerAptitude.brandAuthorization
                      ? '修改图片'
                      : '上传图片'
                  }}
                </el-button>
              </td>
              <td>{{ customerAptitude.brandModifierName }}</td>
              <td>{{ customerAptitude.brandModifyDate }}</td>
            </tr>
            <tr>
              <td>银行流水</td>
              <td>
                <el-image
                  v-if="customerAptitude.bankFlow"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.bankFlow"
                  :preview-src-list="[customerAptitude.bankFlow]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{ 'edit-image-btn': customerAptitude.bankFlow }"
                  @click="uploadImage('7')"
                >
                  {{ customerAptitude.bankFlow ? '修改图片' : '上传图片' }}
                </el-button>
              </td>
              <td>{{ customerAptitude.bankModifierName }}</td>
              <td>{{ customerAptitude.bankModifyDate }}</td>
            </tr>
            <tr>
              <td>企业备案表</td>
              <td>
                <el-image
                  v-if="customerAptitude.keepRecord"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.keepRecord"
                  :preview-src-list="[customerAptitude.keepRecord]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{ 'edit-image-btn': customerAptitude.keepRecord }"
                  @click="uploadImage('8')"
                >
                  {{ customerAptitude.keepRecord ? '修改图片' : '上传图片' }}
                </el-button>
              </td>
              <td>{{ customerAptitude.keepModifierName }}</td>
              <td>{{ customerAptitude.keepModifyDate }}</td>
            </tr>
            <tr>
              <td>法人身份证</td>
              <td>
                <el-image
                  v-if="customerAptitude.legalCardNo"
                  style="width: 80px; height: 80px; border: 1px solid #ebeef5"
                  :src="customerAptitude.legalCardNo"
                  :preview-src-list="[customerAptitude.legalCardNo]"
                ></el-image>
                <el-button
                  type="primary"
                  :class="{ 'edit-image-btn': customerAptitude.legalCardNo }"
                  @click="uploadImage('9')"
                >
                  {{ customerAptitude.legalCardNo ? '修改图片' : '上传图片' }}
                </el-button>
              </td>
              <td>{{ customerAptitude.legalModifierName }}</td>
              <td>{{ customerAptitude.legalModifyDate }}</td>
            </tr>
          </table>
        </el-tab-pane>
        <el-tab-pane label="关联公司" name="4">
          <el-row class="mr-b-10">
            <el-button type="primary" @click="showModal">新 增</el-button>
            <el-button type="primary" @click="removeRelTableItem">
              删 除
            </el-button>
          </el-row>
          <!-- 关联公司 -->
          <JFX-table
            :tableData.sync="relTableData"
            :tableColumn="relTableColumn"
            :showPagination="false"
            showSelection
            @selection-change="selectionChange"
          >
            <template slot="purchasePriceManageLabel" slot-scope="{ row }">
              <el-checkbox v-model="row.purchasePriceManageLabel">
                启用
              </el-checkbox>
            </template>
            <template slot="rate" slot-scope="{ row }">
              <el-select
                v-model="row.tariffRateConfigId"
                placeholder="请选择"
                filterable
                clearable
              >
                <el-option
                  v-for="item in tariffRateConfigList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </template>
          </JFX-table>
        </el-tab-pane>
      </el-tabs>
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button @click="handleSubmit" type="primary">保 存</el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
    <!-- 关联公司组件 -->
    <RelatedCompany
      v-if="relatedCompany.visible.show"
      :companyVisible="relatedCompany.visible"
      :merchantIdList="relatedCompany.merchantIdList"
      :isDefaultChecked="false"
      @comfirm="(data) => closeModal(data)"
    ></RelatedCompany>
    <!-- 关联公司组件 end -->
    <!-- 上传组件 -->
    <div class="hide-form">
      <JFX-upload
        @success="successUpload"
        :url="action"
        :limit="10000"
        :multiple="false"
        :accept="'image/*'"
        :extParams="extParams"
      >
        <el-button ref="upload-btn" type="primary">上传</el-button>
        <span class="clr-II fs-10"></span>
      </JFX-upload>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  import {
    getSupplierEdit,
    uploadFileUrl,
    getCustomerMerchantRelList,
    modifySupplier,
    saveSupplier,
    getSupplierAdd,
    getMerchantList
  } from '@a/customerManage'
  export default {
    mixins: [commomMix],
    components: {
      // 关联公司
      RelatedCompany: () => import('./components/RelatedCompany')
    },
    data() {
      return {
        activeName: '1',
        // 基本信息
        baseForm: {
          source: '', // 来源 1-主数据 2-录入/导入
          name: '',
          simpleName: '',
          creditCode: '',
          orgCode: '',
          codeGrade: '',
          nature: '',
          chinaName: '',
          enSimpleName: '',
          enName: '',
          mainId: '',
          salesman: '',
          cityCode: '',
          companyAddr: '',
          businessAddress: '',
          enBusinessAddress: '',
          currency: '',
          type: '',
          operationScope: '',
          fax: '',
          email: '',
          remark: '',
          innerMerchantId: '',
          // 银行信息
          depositBank: '',
          bankAccount: '',
          beneficiaryName: '',
          bankAddress: '',
          swiftCode: '',
          // 联系方式
          legalPerson: '',
          legalNationality: '',
          legalCardNo: '',
          legalTel: '',
          oTelNo: ''
        },
        // 表单规则
        rules: {
          name: {
            required: true,
            message: '请输入供应商名称',
            trigger: 'blur'
          },
          creditCode: {
            required: true,
            message: '请输入营业执照号',
            trigger: 'blur'
          },
          orgCode: {
            required: true,
            message: '请输入组织机构代码',
            trigger: 'blur'
          },
          type: {
            required: true,
            message: '请选择是否内部公司',
            trigger: 'change'
          },
          innerMerchantId: {
            required: true,
            message: '请选择内部公司',
            trigger: 'change'
          }
        },
        // 关联公司表格结构
        relTableColumn: [
          {
            label: '公司编码',
            prop: 'code',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '公司名称',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '采购价格管理',
            slotTo: 'purchasePriceManageLabel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '税率',
            slotTo: 'rate',
            minWidth: '120',
            align: 'center',
            hide: true,
            need: true
          }
        ],
        // 关联公司表格数据
        relTableData: {
          list: []
        },
        // 关联公司表格结构
        bankTableColumn: [
          {
            label: '开户银行',
            slotTo: 'depositBank',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '银行账号',
            slotTo: 'bankAccount',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '银行账户',
            slotTo: 'beneficiaryName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '开户行地址',
            slotTo: 'bankAddress',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'Swift Code',
            slotTo: 'swiftCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '合作公司',
            slotTo: 'merchantIdStr',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        // 银行信息
        bankTableData: {
          list: []
        },
        deleteIds: [],
        // 已保存的关联公司id集合
        relListIds: [],
        // 上传图片url
        action: '',
        // 上传图片参数
        extParams: {},
        // 资质信息
        customerAptitude: {},
        // 资质信息map
        customerAptitudeMap: {
          1: 'charteredNo',
          2: 'organizationCode',
          3: 'registrationNo',
          4: 'supplyRecord',
          5: 'proofInfo',
          6: 'brandAuthorization',
          7: 'bankFlow',
          8: 'keepRecord',
          9: 'legalCardNo'
        },
        // 关联公司组件状态
        relatedCompany: {
          visible: { show: false },
          id: ''
        },
        // 税率下拉列表
        tariffRateConfigList: []
      }
    },
    mounted() {
      this.init()
      this.getUserMerchantSelectBean('bankCompanyList')
    },
    methods: {
      async init() {
        const { id, mainId, source } = this.$route.query
        id ? this.editInit(id) : this.addInit(mainId, source)
      },
      // 新增页面初始化
      async addInit(mainId, source) {
        if (!mainId || !source) {
          return
        }
        try {
          // 待引入客商列表调转
          const {
            data: { detail }
          } = await getSupplierAdd({ mainId, source })
          for (const key in this.baseForm) {
            this.baseForm[key] = detail[key] ? detail[key].toString() : ''
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 编辑页面初始化
      async editInit(id) {
        try {
          const {
            data: { detail, customerAptitude, customerBankList }
          } = await getSupplierEdit({ id })
          for (const key in this.baseForm) {
            this.baseForm[key] = detail[key] ? detail[key].toString() : ''
          }
          // 资质信息
          this.customerAptitude = customerAptitude || {}
          // 银行信息
          if (customerBankList && customerBankList.length) {
            this.bankTableData.list = customerBankList.map((item) => {
              return {
                ...item,
                merchantIdStr: item.merchantIdStr
                  ? item.merchantIdStr.split(',')
                  : ''
              }
            })
            this.bankTableData.bankListIds = customerBankList.map(
              (item) => item.id + ''
            )
          }
          const {
            data: { relList, tariffRateConfigList }
          } = await getCustomerMerchantRelList({ customerId: id })
          // 关联公司表格数据
          if (relList && relList.length) {
            this.relTableData.list = relList.map((item) => ({
              code: item.code || '',
              merchantName: item.merchant_name || '',
              purchasePriceManageLabel: item.purchase_price_manage === '1',
              tariffRateConfigId: item.rate_id || '',
              id: item.id ? item.id + '' : '',
              merchantId: item.merchant_id ? item.merchant_id + '' : ''
            }))
            this.relListIds = relList.map((item) => item.id + '')
          }
          // 税率下拉列表
          if (tariffRateConfigList && tariffRateConfigList.length) {
            this.tariffRateConfigList = tariffRateConfigList.map((item) => ({
              key: item.id,
              value: String(item.rate)
            }))
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 获取请求参数
      getFetchData() {
        const { id } = this.$route.query
        const merchantList = this.relTableData.list.map((item) => ({
          merchantId: item.merchantId,
          merchantName: item.merchantName,
          tariffRateConfigId: item.tariffRateConfigId,
          purchasePriceManage: item.purchasePriceManageLabel ? '1' : '0'
        }))
        const itemBankList = this.bankTableData.list.map((item) => ({
          bankMerchantIdsStr: item.merchantIdStr.join(','),
          depositBank: item.depositBank,
          bankAccount: item.bankAccount,
          beneficiaryName: item.beneficiaryName,
          bankAddress: item.bankAddress,
          swiftCode: item.swiftCode
        }))

        const json = { merchantList, itemBankList }
        if (id) {
          json.deleteIds = this.deleteIds.toString()
        }
        const params = {
          ...this.baseForm,
          json: JSON.stringify(json)
        }
        if (id) {
          params.id = id
        }
        return params
      },
      // 检查参数
      checkParam() {
        if (!this.relTableData.list.length) {
          return true
        }
        const flag = this.relTableData.list.every(
          (item) => item.tariffRateConfigId
        )
        if (flag) return flag
        this.$errorMsg('关联公司请选择税率')
        this.activeName = '4'
        return false
      },
      // 提交表单
      handleSubmit() {
        this.$refs.baseForm.validate(async (valid) => {
          if (valid) {
            try {
              const { id } = this.$route.query
              if (!this.checkParam()) return
              const params = this.getFetchData()
              if (id) {
                await modifySupplier(params)
              } else {
                await saveSupplier(params)
              }
              this.$successMsg(id ? '修改成功' : '保存成功')
              this.closeTag()
            } catch (error) {
              this.$errorMsg(error.message)
            }
          }
        })
      },
      // 上传/修改图片
      uploadImage(type) {
        this.$nextTick(() => {
          const { id } = this.$route.query
          this.extParams = { id, type }
          this.action =
            getBaseUrl(uploadFileUrl) +
            uploadFileUrl +
            '?token=' +
            sessionStorage.getItem('token')
          this.$refs['upload-btn'].$el.click()
        })
      },
      // 上传成功
      successUpload(res) {
        if (+res.code === 10000) {
          this.$successMsg('上传成功')
          this.customerAptitude[this.customerAptitudeMap[this.extParams.type]] =
            res.data
        } else {
          this.$errorMsg('上传出错')
        }
      },
      // 删除表格项
      removeRelTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          this.tableChoseList.forEach((item) => {
            if (this.relListIds.includes(item.id)) {
              this.deleteIds.push(item.id)
            }
          })
          const merchantIds = this.tableChoseList.map((item) => item.merchantId)
          this.relTableData.list = this.relTableData.list.filter(
            (item) => !merchantIds.includes(item.merchantId)
          )
        })
      },
      // 显示管理公司弹窗
      showModal() {
        this.relatedCompany.visible.show = true
        this.relatedCompany.merchantIdList = this.relTableData.list
          .map((item) => item.merchantId + '')
          .toString()
      },
      // 隐藏管理公司弹窗
      async closeModal(data) {
        this.relatedCompany.visible.show = false
        if (!data || !data.length) {
          return
        }
        // 加载下拉数据 获取
        const {
          data: { tariffRateConfigList }
        } = await getMerchantList({
          merchantIds: data.map((item) => item.id).toString()
        })
        // 税率下拉列表
        if (tariffRateConfigList && tariffRateConfigList.length) {
          this.tariffRateConfigList = tariffRateConfigList.map((item) => ({
            key: item.id,
            value: item.rate
          }))
        }
        data.forEach((item) => {
          this.relTableData.list.push({
            id: '',
            merchantId: item.id + '',
            merchantName: item.name,
            code: item.code,
            purchasePriceManageLabel: true
          })
        })
      },
      // 银行信息列表改变
      bankTableSelectionChange(row) {
        this.bankTableData.chooseList = row
      },
      // 添加银行信息
      addBank() {
        this.bankTableData.list.push({
          bankMerchantIdsStr: '',
          depositBank: '',
          bankAccount: '',
          beneficiaryName: '',
          bankAddress: '',
          swiftCode: '',
          id: Math.random()
        })
      },
      // 删除银行信息
      delBank() {
        if (!this.bankTableData.chooseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.bankTableData.chooseList.map((item) => item.id)
          this.bankTableData.list = this.bankTableData.list.filter(
            (item) => !ids.includes(item.id)
          )
        })
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

  .table-container {
    border: 1px solid #ebeef5;
    border-collapse: collapse;
    width: 100%;
    text-align: center;
    color: #666;
    font-size: 14px;
    td {
      border-left: 1px solid #ebeef5;
      border-bottom: 1px solid #ebeef5;
      height: 30px;
      padding: 4px;
    }
    .edit-image-btn {
      display: inline-block;
      position: relative;
      top: -50%;
      transform: translate(0, 50%);
    }
  }

  ::v-deep.textarea-container {
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
