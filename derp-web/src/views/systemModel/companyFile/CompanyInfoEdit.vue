<template>
  <!-- 领料单新增/编辑页面 -->
  <div class="page-bx bgc-w edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 表单部分 -->
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 基本信息 -->
      <JFX-title title="基本信息" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司简称：" prop="name">
            <el-input
              v-model.trim="ruleForm.name"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="卓志编码：" prop="topidealCode">
            <el-input
              v-model.trim="ruleForm.topidealCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司全称：" prop="fullName">
            <el-input
              v-model.trim="ruleForm.fullName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="提醒财务付款邮箱：" prop="financePayEmail">
            <el-input
              v-model.trim="ruleForm.financePayEmail"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="盘点结果通知邮箱：" prop="inventoryResultEmail">
            <el-input
              v-model.trim="ruleForm.inventoryResultEmail"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="注册地址(英文)："
            prop="englishRegisteredAddress"
          >
            <el-input
              v-model.trim="ruleForm.englishRegisteredAddress"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="接收清关资料邮箱：" prop="email">
            <el-input
              v-model.trim="ruleForm.email"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="备注：" prop="remark">
            <el-input
              v-model.trim="ruleForm.remark"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="是否代理：" prop="isProxy">
            <el-select
              v-model="ruleForm.isProxy"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in [
                  { label: '否', value: '0' },
                  { label: '是', value: '1' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="授权码：" prop="permission">
            <el-input
              v-model.trim="ruleForm.permission"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="联系电话：" prop="tel">
            <el-input
              v-model.trim="ruleForm.tel"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="勾稽完结百分比(%)：" prop="articulationPercent">
            <JFX-Input
              v-model.trim="ruleForm.articulationPercent"
              :precision="1"
              :min="0"
              clearable
              style="width: 100%"
              placeholder="请输入"
            ></JFX-Input>
            <!-- <el-input v-model.trim="ruleForm.articulationPercent" placeholder="请输入" clearable /> -->
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="英文名称：" prop="englishName">
            <el-input
              v-model.trim="ruleForm.englishName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="成本核算币种：" prop="accountCurrency">
            <el-select
              v-model="ruleForm.accountCurrency"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('currencyCodeList')"
            >
              <el-option
                v-for="item in selectOpt.currencyCodeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="核算单价：" prop="accountPrice">
            <el-select
              v-model="ruleForm.accountPrice"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('merchantInfo_accountPriceList')"
            >
              <el-option
                v-for="item in selectOpt.merchantInfo_accountPriceList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="注册地址(中文)：" prop="registeredAddress">
            <el-input
              v-model.trim="ruleForm.registeredAddress"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="注册地类型：" prop="registeredType">
            <el-select
              v-model="ruleForm.registeredType"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('merchantInfo_registeredTypeList')"
            >
              <el-option
                v-for="item in selectOpt.merchantInfo_registeredTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="注册登记证：" prop="registrationCert">
            <el-input
              v-model.trim="ruleForm.registrationCert"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 基本信息 end -->
      <!-- 银行信息 -->
      <JFX-title title="银行信息" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="开户银行：" prop="depositBank">
            <el-input
              v-model.trim="ruleForm.depositBank"
              placeholder="请输入"
              clearable
              style="width: 400px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行账号：" prop="bankAccount">
            <el-input
              v-model.trim="ruleForm.bankAccount"
              placeholder="请输入"
              clearable
              style="width: 400px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行账户：" prop="beneficiaryName">
            <el-input
              v-model.trim="ruleForm.beneficiaryName"
              placeholder="请输入"
              clearable
              style="width: 400px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Swift Code：" prop="swiftCode">
            <el-input
              v-model.trim="ruleForm.swiftCode"
              placeholder="请输入"
              clearable
              style="width: 400px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户行地址：" prop="bankAddress">
            <el-input
              v-model.trim="ruleForm.bankAddress"
              placeholder="请输入"
              clearable
              style="width: 400px"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 银行信息 end -->
    </JFX-Form>
    <!-- 表单部分 end -->
    <!-- 仓库信息 -->
    <JFX-title title="仓库信息" className="mr-t-15" />
    <div style="margin-bottom: 20px">
      <el-button type="primary" @click="addToTable('depot')">新增</el-button>
      <el-button type="primary" @click="deleteTableItem('depot')">
        删除
      </el-button>
    </div>
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      showSelection
      @selection-change="selectionChange"
    >
      <template slot="depotId" slot-scope="{ row, $index }">
        <el-select
          v-model="row.depotId"
          placeholder="请选择"
          clearable
          filterable
          @change="($event) => tableSelectChange($event, $index, 'depot')"
        >
          <el-option
            v-for="item in row.warehousesList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
      <template slot="contractCode" slot-scope="{ row }">
        <el-input v-model.trim="row.contractCode" clearable />
      </template>
      <!-- 选择了以入定出或者以出定入,不能选择进出接口指令-->
      <template slot="isInOutInstruction" slot-scope="{ row }">
        <el-checkbox
          v-model="row.isInOutInstruction"
          :disabled="row.isInDependOut || row.isOutDependIn"
        >
          是
        </el-checkbox>
      </template>
      <template slot="isInvertoryFallingPrice" slot-scope="{ row }">
        <el-checkbox v-model="row.isInvertoryFallingPrice">是</el-checkbox>
      </template>
      <template slot="productRestriction" slot-scope="{ row }">
        <el-select
          v-model="row.productRestriction"
          placeholder="请选择"
          clearable
          filterable
        >
          <el-option
            v-for="item in [
              { label: '仅备案商品', value: '1' },
              { label: '仅外仓统一码', value: '2' },
              { label: '无限制', value: '3' }
            ]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
      <!-- 选择了进出接口指令,不能选择以入定出 -->
      <template slot="isInDependOut" slot-scope="{ row }">
        <el-checkbox
          v-model="row.isInDependOut"
          :disabled="row.isInOutInstruction"
        >
          是
        </el-checkbox>
      </template>
      <!-- 选择了进出接口指令,不能选择以出定入 -->
      <template slot="isOutDependIn" slot-scope="{ row }">
        <el-checkbox
          v-model="row.isOutDependIn"
          :disabled="row.isInOutInstruction"
        >
          是
        </el-checkbox>
      </template>
      <template slot="depotBuRel" slot-scope="{ row, $index }">
        <el-select
          v-model="row.depotBuRel"
          placeholder="请选择"
          clearable
          multiple
          filterable
          @click="($event) => tableBuChange($event, $index)"
        >
          <el-option
            v-for="item in merchantBuRelList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
    </JFX-table>
    <!-- 仓库信息 end -->
    <!-- 代理公司 -->
    <JFX-title title="代理公司" className="mr-t-15" />
    <div style="margin-bottom: 20px">
      <el-button type="primary" @click="addToTable('merchant')">新增</el-button>
      <el-button type="primary" @click="deleteTableItem('merchant')">
        删除
      </el-button>
    </div>
    <JFX-table
      :tableData.sync="agencyTableData"
      :tableColumn="agencyTableColumn"
      :showPagination="false"
      showSelection
      @selection-change="selectionChange"
    >
      <template slot="merchantId" slot-scope="{ row, $index }">
        <el-select
          v-model="row.merchantId"
          placeholder="请选择"
          clearable
          filterable
          @change="($event) => tableSelectChange($event, $index, 'merchant')"
        >
          <el-option
            v-for="item in row.agencyMerchantList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
    </JFX-table>
    <!-- 代理公司 end -->
    <!-- 底部按钮 -->
    <div class="flex-c-c mr-t-30">
      <el-button
        type="primary"
        :loading="actionBtnLoading"
        @click="handleSumbit"
      >
        保存
      </el-button>
      <el-button @click="closeTag">取消</el-button>
    </div>
    <!-- 底部按钮 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getDepotDetails } from '@a/salesManage/index'
  import { getDepotSelectBean } from '@a/base'
  import {
    getMerchantEditPage,
    getSelectInfoByMerchantId,
    getMerchantSelectBean,
    getMerchantInfoDetails,
    modifyMerchant,
    saveMerchant
  } from '@a/companyFile'
  export default {
    mixins: [commomMix],
    data() {
      return {
        ruleForm: {
          name: '',
          topidealCode: '',
          fullName: '',
          financePayEmail: '',
          inventoryResultEmail: '',
          englishRegisteredAddress: '',
          email: '',
          remark: '',
          isProxy: '',
          permission: '',
          tel: '',
          articulationPercent: '',
          englishName: '',
          accountCurrency: '',
          accountPrice: '',
          registeredAddress: '',
          registeredType: '',
          depositBank: '',
          bankAccount: '',
          beneficiaryName: '',
          swiftCode: '',
          bankAddress: '',
          registrationCert: ''
        },
        rules: {
          name: { required: true, message: '请输入公司简称', trigger: 'blur' },
          topidealCode: {
            required: true,
            message: '请输入卓志编码',
            trigger: 'blur'
          },
          fullName: {
            required: true,
            message: '请输入公司全称',
            trigger: 'blur'
          },
          englishRegisteredAddress: {
            required: true,
            message: '请输入注册地址(英文)',
            trigger: 'blur'
          },
          articulationPercent: {
            required: true,
            message: '请输入勾稽完结百分比(%)',
            trigger: 'blur'
          },
          accountCurrency: {
            required: true,
            message: '请选择成本核算币种',
            trigger: 'change'
          },
          accountPrice: {
            required: true,
            message: '请选择核算单价',
            trigger: 'change'
          },
          registeredAddress: {
            required: true,
            message: '请输入注册地址(中文)',
            trigger: 'blur'
          },
          registeredType: {
            required: true,
            message: '请选择注册地类型',
            trigger: 'change'
          },
          depositBank: {
            required: true,
            message: '请输入开户银行',
            trigger: 'blur'
          },
          bankAccount: {
            required: true,
            message: '请输入银行账号',
            trigger: 'blur'
          },
          beneficiaryName: {
            required: true,
            message: '请输入银行账户',
            trigger: 'blur'
          },
          swiftCode: {
            required: true,
            message: '请输入SwiftCode',
            trigger: 'blur'
          },
          bankAddress: {
            required: true,
            message: '请输入开户行地址',
            trigger: 'blur'
          }
        },
        tableColumn: [
          {
            label: '仓库名称',
            slotTo: 'depotId',
            minWidth: '140',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '仓库编号',
            prop: 'depotCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '合同编号',
            slotTo: 'contractCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '进出接口指令',
            slotTo: 'isInOutInstruction',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '统计存货跌价',
            slotTo: 'isInvertoryFallingPrice',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '选品限制',
            slotTo: 'productRestriction',
            minWidth: '120',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '以入定出',
            slotTo: 'isInDependOut',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '以出定入',
            slotTo: 'isOutDependIn',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '绑定事业部',
            slotTo: 'depotBuRel',
            width: '180',
            align: 'center',
            hide: true
          }
        ],
        agencyTableData: {
          list: []
        },
        agencyTableColumn: [
          {
            label: '公司简称',
            slotTo: 'merchantId',
            minWidth: '140',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '公司编号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          }
        ],
        merchantBuRelList: [],
        agencyMerchantList: [],
        warehousesList: [],
        actionBtnLoading: false,
        tableDelId: 0
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        const { id } = this.$route.query
        id ? this.editInit(id) : this.addInit()
      },
      // 新增页面初始化
      async addInit() {
        this.mapSelectToPage()
      },
      // 编辑页面初始化
      async editInit(id) {
        try {
          const {
            data: { detail, depotMerchantRelList, merchantBuRelList, list }
          } = await getMerchantEditPage({ id })
          for (const key in this.ruleForm) {
            this.ruleForm[key] = ![null, undefined].includes(detail[key])
              ? detail[key] + ''
              : ''
          }
          // 勾稽完结百分比需要乘于百分比
          this.ruleForm.articulationPercent = this.ruleForm.articulationPercent
            ? (this.ruleForm.articulationPercent * 100).toFixed(1)
            : ''
          this.merchantBuRelList = merchantBuRelList || []
          if (depotMerchantRelList && depotMerchantRelList.length) {
            this.tableData.list = depotMerchantRelList.map((item) => ({
              ...item,
              depotId: item.depotId ? item.depotId + '' : '',
              isInOutInstruction: item.isInOutInstruction === '1',
              isInvertoryFallingPrice: item.isInvertoryFallingPrice === '1',
              isInDependOut: item.isInDependOut === '1',
              isOutDependIn: item.isOutDependIn === '1',
              warehousesList: [],
              tableDelId: this.tableDelId++
            }))
          }
          if (list && list.length) {
            this.agencyTableData.list = list.map((item) => ({
              ...item,
              merchantId: item.id ? item.id + '' : '',
              agencyMerchantList: []
            }))
          }
          // 将已选择关联的事业部回显
          if (this.tableData.list.length) {
            const { id } = this.$route.query
            this.tableData.list.forEach(async (item) => {
              const { data } = await getSelectInfoByMerchantId({
                depotId: item.depotId,
                merchantId: id
              })
              data && data.length && (item.depotBuRel = data.split(','))
            })
          }
          // 获取下拉数据
          await this.mapSelectToPage()
          // 仓库表格下拉列表将已选择的仓库从下拉列表中去除
          await this.duplicateTable()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 获取下拉数据
      async mapSelectToPage() {
        await this.getWarehousesList()
        await this.getAgencyMerchantList()
      },
      // 获取仓库下拉数据
      async getWarehousesList() {
        try {
          const { data: warehousesList } = await getDepotSelectBean()
          this.warehousesList = warehousesList || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 获取代理公司下拉数据
      async getAgencyMerchantList() {
        try {
          const { data: agencyMerchantList } = await getMerchantSelectBean({
            isProxy: 1
          })
          this.agencyMerchantList = agencyMerchantList || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 仓库表格下拉列表将已选择的仓库从下拉列表中去除
      async duplicateTable() {
        if (this.tableData.list.length) {
          this.tableData.list.forEach((item) => {
            const depotIds = []
            this.tableData.list.forEach(
              (item) => item.depotId && depotIds.push(item.depotId + '')
            )
            depotIds.splice(depotIds.indexOf(item.depotId) >>> 0, 1)
            item.warehousesList = this.warehousesList.filter(
              (subItem) => !depotIds.includes(subItem.value)
            )
          })
        }
        if (this.agencyTableData.list.length) {
          this.agencyTableData.list.forEach((item) => {
            const merchantIds = []
            this.agencyTableData.list.forEach(
              (item) =>
                item.merchantId && merchantIds.push(item.merchantId + '')
            )
            merchantIds.splice(merchantIds.indexOf(item.merchantId) >>> 0, 1)
            item.agencyMerchantList = this.agencyMerchantList.filter(
              (subItem) => !merchantIds.includes(subItem.value)
            )
          })
        }
      },
      // 添加表格行
      addToTable(type) {
        switch (type) {
          case 'depot': {
            const list = this.tableData.list
            const lastItem = list[list.length - 1]
            if (!list.length || (lastItem && lastItem.depotId)) {
              this.tableData.list.push({
                isInOutInstruction: false,
                isInvertoryFallingPrice: false,
                isInDependOut: false,
                isOutDependIn: false,
                warehousesList: [],
                tableDelId: this.tableDelId++,
                depotCode: ''
              })
            }
            this.duplicateTable()
            break
          }
          case 'merchant': {
            const list = this.agencyTableData.list
            const lastItem = list[list.length - 1]
            if (!list.length || (lastItem && lastItem.merchantId)) {
              this.agencyTableData.list.push({
                tableDelId: this.tableDelId++,
                agencyMerchantList: [],
                code: ''
              })
            }
            this.duplicateTable()
            break
          }
        }
      },
      // 删除表格行
      deleteTableItem(type) {
        switch (type) {
          case 'depot': {
            if (this.tableChoseList.length) {
              this.$showToast('确认要删除吗？', () => {
                const ids = this.tableChoseList.map((item) => item.tableDelId)
                this.tableData.list = this.tableData.list.filter(
                  (item) => !ids.includes(item.tableDelId)
                )
                this.duplicateTable()
              })
            }
            break
          }
          case 'merchant': {
            if (this.tableChoseList.length) {
              this.$showToast('确认要删除吗？', () => {
                const ids = this.tableChoseList.map((item) => item.tableDelId)
                this.agencyTableData.list = this.agencyTableData.list.filter(
                  (item) => !ids.includes(item.tableDelId)
                )
                this.duplicateTable()
              })
            }
            break
          }
        }
      },
      // 表格下拉选择事件
      async tableSelectChange(id, index, type) {
        try {
          switch (type) {
            case 'depot': {
              const item = this.tableData.list[index]
              if (!id) {
                item.depotCode = ''
                this.duplicateTable()
                return false
              }
              this.duplicateTable()
              const {
                data: { code }
              } = await getDepotDetails({ id })
              item.depotCode = code || ''
              break
            }
            case 'merchant': {
              const item = this.agencyTableData.list[index]
              if (!id) {
                item.code = ''
                this.duplicateTable()
                return false
              }
              this.duplicateTable()
              const {
                data: { code }
              } = await getMerchantInfoDetails({ id })
              item.code = code || ''
              break
            }
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 关联事业部选择
      tableBuChange(id, index) {
        if (!id) return false
        if (!this.tableData.list[index].depotId) {
          this.$errorMsg(`仓库信息表格第${index + 1}行，请先选择仓库!`)
        }
      },
      // 校验表格数据
      checkTableData() {
        let isPass = true
        for (let i = 0; i < this.tableData.list.length; i++) {
          const item = this.tableData.list[i]
          if (!item.depotId) {
            this.$errorMsg(`仓库信息表格第${i + 1}行，仓库名称不能为空!`)
            isPass = false
            break
          }
          if (item.depotId && !item.productRestriction) {
            this.$errorMsg(`仓库信息表格第${i + 1}行，选品限制不能为空!`)
            isPass = false
            break
          }
        }

        for (let i = 0; i < this.agencyTableData.list.length; i++) {
          const item = this.agencyTableData.list[i]
          if (!item.merchantId) {
            this.$errorMsg(`代理公司表格第${i + 1}行，公司简称不能为空!`)
            isPass = false
            break
          }
        }
        return isPass
      },
      // 保存
      handleSumbit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            if (!this.checkTableData()) return false
            const id = this.$route.query.id
            const merchantDepotList = this.tableData.list.map((item) => ({
              depotId: item.depotId ? item.depotId + '' : '',
              depotCode: item.depotCode ? item.depotCode + '' : '',
              contractCode: item.contractCode ? item.contractCode + '' : '',
              isInOutInstruction: item.isInOutInstruction ? '1' : '0',
              isInvertoryFallingPrice: item.isInvertoryFallingPrice ? '1' : '0',
              productRestriction: item.productRestriction
                ? item.productRestriction + ''
                : '',
              isInDependOut: item.isInDependOut ? '1' : '0',
              isOutDependIn: item.isOutDependIn ? '1' : '0',
              buIdList:
                item.depotBuRel && item.depotBuRel.length ? item.depotBuRel : []
            }))
            const merchantIdList = this.agencyTableData.list.length
              ? this.agencyTableData.list.map((item) => item.merchantId)
              : []
            const params = {
              ...this.ruleForm,
              merchantDepotList,
              merchantIdList,
              yname: this.ruleForm.name,
              id
            }
            try {
              this.actionBtnLoading = true
              id ? await modifyMerchant(params) : await saveMerchant(params)
              this.$successMsg('操作成功')
              this.closeTag()
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.actionBtnLoading = false
            }
          } else {
            this.$errorMsg('请正确填写表单必填项')
          }
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
    width: 160px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
