<template>
  <!-- 领料单新增/编辑页面 -->
  <div class="page-bx bgc-w edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 表单部分 -->
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 仓库信息 -->
      <JFX-title title="仓库信息" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="仓库自编码：" prop="depotCode">
            <el-input
              v-model.trim="ruleForm.depotCode"
              placeholder="请输入"
              clearable
              :disabled="!!$route.query.id"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="OP仓库编号：" prop="code">
            <el-input
              v-model.trim="ruleForm.code"
              placeholder="请输入"
              clearable
              :disabled="!!$route.query.id"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="仓库名称：" prop="name">
            <el-input
              v-model.trim="ruleForm.name"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="仓库类别：" prop="type">
            <el-select
              v-model="ruleForm.type"
              placeholder="请选择"
              clearable
              filterable
              @change="depotTypeChange"
            >
              <el-option
                v-for="item in [
                  { label: '保税仓', value: 'a' },
                  { label: '备查库', value: 'b' },
                  { label: '海外仓', value: 'c' },
                  { label: '中转仓', value: 'd' },
                  { label: '暂存区', value: 'e' },
                  { label: '销毁区', value: 'f' },
                  { label: '内贸仓', value: 'g' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="仓库类型：" prop="depotType">
            <el-select
              v-model="ruleForm.depotType"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('currencyCodeList')"
            >
              <el-option
                v-for="item in [
                  { label: '仓库(BBC)', value: '1' },
                  { label: '场站(BC)', value: '2' },
                  { label: '其他', value: '3' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="申报海关编号：" prop="customsNo">
            <el-input
              v-model.trim="ruleForm.customsNo"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="申报国检编号：" prop="inspectNo">
            <el-input
              v-model.trim="ruleForm.inspectNo"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="仓管员：" prop="adminName">
            <el-input
              v-model.trim="ruleForm.adminName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="联系电话：" prop="tel">
            <JFX-Input
              v-model.trim="ruleForm.tel"
              :min="0"
              placeholder="请输入"
              clearable
            ></JFX-Input>
            <!-- <el-input v-model.trim="ruleForm.tel" placeholder="请输入" clearable /> -->
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="仓库所在国家：" prop="country">
            <el-input
              v-model.trim="ruleForm.country"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="ISV仓库类型：" prop="isvdepotType">
            <el-select
              v-model="ruleForm.isvdepotType"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('depotInfo_ISVDepotTypeList')"
            >
              <el-option
                v-for="item in selectOpt.depotInfo_ISVDepotTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-row>
            <el-col :span="4">
              <el-form-item label="是否代销仓：" prop="isTopBooks">
                <el-checkbox
                  v-model="ruleForm.isTopBooks"
                  @change="isTopBooksChange"
                ></el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item
                label="是否启用："
                prop="status"
                style="margin-left: -34px"
              >
                <el-checkbox v-model="ruleForm.status"></el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item
                label="是否代客管理仓库："
                prop="isValetManage"
                style="margin-left: -34px"
              >
                <el-checkbox v-model="ruleForm.isValetManage"></el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="仓库详细地址："
            prop="address"
            class="textarea-con"
          >
            <div
              v-for="(item, index) in depotDetailAddress"
              :key="index"
              style="display: flex; margin-bottom: 10px"
            >
              <el-input
                v-model.trim="item.value"
                placeholder="请输入"
                clearable
              />
              <el-button
                type="primary"
                style="margin-left: 20px"
                v-if="ruleForm.isTopBooks && index === 0"
                @click="depotDetailAddress.push({ value: '' })"
              >
                添加
              </el-button>
              <el-button
                type="primary"
                style="margin-left: 20px"
                v-if="ruleForm.isTopBooks && index !== 0"
                @click="depotDetailAddress.splice(index, 1)"
              >
                删除
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 仓库信息 end -->
      <!-- 流程设置 -->
      <JFX-title title="流程设置" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="入仓下推指令appkey：" prop="isMerchant">
            <el-radio-group v-model="ruleForm.isMerchant">
              <el-radio label="1">公司key</el-radio>
              <el-radio label="2">关联公司key</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
          v-if="ruleForm.isMerchant === '2'"
        >
          <el-form-item label="选择关联公司：" prop="merchantId">
            <el-select
              v-model="ruleForm.merchantId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in merchantBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="是否批次效期强校验：" prop="batchValidation">
            <el-select
              v-model="ruleForm.batchValidation"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('depotInfo_batchValidationList')"
            >
              <el-option
                v-for="item in selectOpt.depotInfo_batchValidationList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <div style="margin-bottom: 20px; padding-left: 10px">
        <el-button type="text" @click="showChangeList" v-if="$route.query.id">
          变更记录
        </el-button>
      </div>
      <div style="margin-bottom: 20px; padding-left: 10px">
        <el-button type="primary" @click="addToTable">新增</el-button>
        <el-button type="primary" @click="deleteTableItem">删除</el-button>
      </div>
      <JFX-table
        :tableData.sync="tableData"
        :tableColumn="tableColumn"
        :showPagination="false"
        showSelection
        @selection-change="selectionChange"
      >
        <template slot="customsAreaId" slot-scope="{ row }">
          <el-select
            v-model="row.customsAreaId"
            placeholder="请选择"
            clearable
            filterable
            :data-list="getCustomsSelectBean('warehousesList')"
          >
            <el-option
              v-for="item in selectOpt.warehousesList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
        </template>
        <template slot="onlineRegisterNo" slot-scope="{ row }">
          <el-input v-model.trim="row.onlineRegisterNo" clearable />
        </template>
        <template slot="recCompanyName" slot-scope="{ row }">
          <el-input v-model.trim="row.recCompanyName" clearable />
        </template>
        <template slot="recCompanyEnname" slot-scope="{ row }">
          <el-input v-model.trim="row.recCompanyEnname" clearable />
        </template>
        <template slot="customerAddress" slot-scope="{ row }">
          <el-input v-model.trim="row.address" clearable />
        </template>
      </JFX-table>
      <!-- 流程设置 end -->
    </JFX-Form>
    <!-- 表单部分 end -->
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
    <!-- 变更记录 -->
    <ChangeList
      v-if="changeList.visible.show"
      :data="changeList.data"
      :isVisible="changeList.visible"
      @comfirm="changeList.visible.show = false"
    ></ChangeList>
    <!-- 变更记录 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getDepotEdit,
    getDepotCustomsRel,
    modifyDepot,
    saveDepot,
    getDepotAddPage
  } from '@a/warehouseFile'
  export default {
    mixins: [commomMix],
    components: {
      // 变更记录
      ChangeList: () => import('./components/ChangeList')
    },
    data() {
      return {
        // 表单数据
        ruleForm: {
          depotCode: '',
          code: '',
          name: '',
          type: '',
          depotType: '',
          customsNo: '',
          inspectNo: '',
          adminName: '',
          tel: '',
          country: '',
          isvdepotType: '',
          isTopBooks: '',
          status: true,
          address: '',
          isValetManage: '',
          isMerchant: '1',
          merchantId: '',
          batchValidation: ''
        },
        // 表单校验规则
        rules: {
          depotCode: {
            required: true,
            message: '请输入仓库自编码',
            trigger: 'blur'
          },
          code: {
            required: true,
            message: '请输入OP仓库编号',
            trigger: 'blur'
          },
          name: { required: true, message: '请输入仓库名称', trigger: 'blur' },
          type: {
            required: true,
            message: '请选择仓库类别',
            trigger: 'change'
          },
          depotType: {
            required: true,
            message: '请选择仓库类型',
            trigger: 'change'
          },
          customsNo: {
            required: false,
            message: '请输入申报海关编号',
            trigger: 'blur'
          },
          inspectNo: {
            required: false,
            message: '请输入申报国检编号',
            trigger: 'blur'
          },
          isvdepotType: {
            required: true,
            message: '请选择ISV仓库类型',
            trigger: 'change'
          },
          batchValidation: {
            required: true,
            message: '请选择是否批次效期强校验',
            trigger: 'change'
          }
        },
        // 表格列数据
        tableColumn: [
          {
            label: '平台关区',
            slotTo: 'customsAreaId',
            minWidth: '140',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '电商账册号',
            slotTo: 'onlineRegisterNo',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '收货人公司名称（中文）',
            slotTo: 'recCompanyName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '收货人公司名称（英文）',
            slotTo: 'recCompanyEnname',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '仓库地址',
            slotTo: 'customerAddress',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        // 仓库详细地址数组
        depotDetailAddress: [{ value: '' }],
        // 关联公司下拉列表
        merchantBean: [],
        // 操作按钮loading状态
        actionBtnLoading: false,
        // 表格删除id
        tableDelId: 0,
        // 变更记录组件状态
        changeList: {
          visible: { show: false }
        }
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
        try {
          const { data } = await getDepotAddPage()
          if (data && data.length) {
            this.merchantBean = data.map((item) => ({
              label: item.name,
              value: item.id + ''
            }))
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 编辑页面初始化
      async editInit(id) {
        const changeKeys = ['isTopBooks', 'status', 'isValetManage'] // 单选框状态
        try {
          const {
            data: { detail, merchantBean }
          } = await getDepotEdit({ id })
          for (const key in this.ruleForm) {
            if (changeKeys.includes(key)) {
              this.ruleForm[key] = detail[key] === '1'
              continue
            }
            this.ruleForm[key] = ![null, undefined].includes(detail[key])
              ? detail[key] + ''
              : ''
          }
          // 仓库详细地址数组
          const address = this.ruleForm.address
            ? this.ruleForm.address.split(',')
            : []
          if (address.length) {
            this.depotDetailAddress = address.map((item) => ({ value: item }))
          }
          if (merchantBean && merchantBean.length) {
            this.merchantBean = merchantBean.map((item) => ({
              label: item.name,
              value: item.id + ''
            }))
          }
          // 仓库类型为保税仓，申报海关编号、申报国检编号必填
          if (this.ruleForm.type && this.ruleForm.type === 'a') {
            this.rules.customsNo.required = true
            this.rules.inspectNo.required = true
          }
          // 获取表格数据
          const { data: list } = await getDepotCustomsRel({ depotId: id })
          if (list && list.length) {
            this.tableData.list = list.map((item) => ({
              ...item,
              customsAreaId: item.customsAreaId ? item.customsAreaId + '' : '',
              tableDelId: this.tableDelId++
            }))
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 保存
      handleSumbit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            if (!this.checkTableList()) return false
            const { id } = this.$route.query
            const itemList = this.tableData.list.map((item) => ({
              customsAreaConfigId: item.customsAreaId || '',
              onlineRegisterNo: item.onlineRegisterNo || '',
              recCompanyName: item.recCompanyName || '',
              recCompanyEnname: item.recCompanyEnname || '',
              customerAddress: item.address || ''
            }))
            const json = JSON.stringify({ itemList })
            const addressList = []
            this.depotDetailAddress.forEach((item) => {
              item.value && addressList.push(item.value)
            })
            const params = {
              ...this.ruleForm,
              isTopBooks: this.ruleForm.isTopBooks ? '1' : '0',
              status: this.ruleForm.status ? '1' : '0',
              isValetManage: this.ruleForm.isValetManage ? '1' : '0',
              ISVDepotType: this.ruleForm.isvdepotType || '',
              address: addressList.toString(),
              json,
              id
            }
            delete params.isvdepotType
            id ? await modifyDepot(params) : await saveDepot(params)
            this.$successMsg('操作成功')
            this.closeTag()
          } else {
            this.$errorMsg('请先正确填写表单信息！')
          }
        })
      },
      // 检验表格
      checkTableList() {
        let isPass = true
        const list = this.tableData.list
        for (let i = 0; i < list.length; i++) {
          const { customsAreaId } = list[i]
          if (!customsAreaId) {
            this.$errorMsg(`表格第${i + 1}行，平台关区不能为空。`)
            isPass = false
            break
          }
        }
        return isPass
      },
      // 添加表格行
      addToTable() {
        this.tableData.list.push({ tableDelId: this.tableDelId++ })
      },
      // 删除表格行
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('请选择要删除的数据！')
          return false
        }
        this.$showToast('确定要删除勾选的数据？', () => {
          const ids = this.tableChoseList.map((item) => item.tableDelId)
          this.tableData.list = this.tableData.list.filter(
            (item) => !ids.includes(item.tableDelId)
          )
        })
      },
      // 仓库类型改变
      depotTypeChange(depotType) {
        if (depotType && depotType === 'a') {
          this.rules.customsNo.required = true
          this.rules.inspectNo.required = true
        } else {
          this.rules.customsNo.required = false
          this.rules.inspectNo.required = false
        }
      },
      isTopBooksChange() {
        this.depotDetailAddress = [{ value: '' }]
      },
      // 显示变更记录
      showChangeList() {
        const { id } = this.$route.query
        this.changeList.data = { id }
        this.changeList.visible.show = true
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
    padding: 0 4px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 160px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  ::v-deep.textarea-con {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      font-size: 14px;
      color: #606266;
      line-height: 40px;
      -webkit-box-sizing: border-box;
      vertical-align: middle;
      box-sizing: border-box;
      padding: 0 4px 0 0;
      width: 160px;
    }
    .el-form-item__content {
      width: 700px;
    }
  }
</style>
