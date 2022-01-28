<template>
  <!-- 预售订单新增/编辑页面 -->
  <div class="page-bx bgc-w sales-add-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <div class="mr-t-15"></div>
    <!-- 基本信息 -->
    <!-- <JFX-title title="基本信息" /> -->
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司：" prop="merchantId">
            <el-select
              v-model="ruleForm.merchantId"
              placeholder="请选择"
              @change="changeMerchantIdorReminderType"
            >
              <el-option
                v-for="item in selectOpt.merchant_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="事业部：" prop="buId" ref="buId">
            <el-select
              v-model="ruleForm.buId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in selectOpt.buList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="客户：" prop="customerId">
            <el-select
              v-model="ruleForm.customerId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in selectOpt.kuhuList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="生效时间：" prop="effectiveDateStr">
            <el-date-picker
              v-model="ruleForm.effectiveDateStr"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['00:00:00']"
              placeholder="选择生效时间"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="失效时间：" prop="expirationDateStr">
            <el-date-picker
              v-model="ruleForm.expirationDateStr"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['23:59:59']"
              placeholder="选择失效时间"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="平台：" prop="storePlatformCode">
            <el-select
              v-model="ruleForm.storePlatformCode"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('storePlatformList')"
              @change="changePlatformCode"
            >
              <el-option
                v-for="item in selectOpt.storePlatformList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <!-- 基本信息 end -->
    <!-- 商品信息 -->
    <JFX-title title="配置信息" className="mr-t-15 title-bx" />
    <!-- <el-row class="mr-b-10">
      <el-button type="primary" @click="removeTableItem">删除</el-button>
      <el-button type="primary" @click="showModal">增加</el-button>
    </el-row> -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      showSelection
      @selection-change="selectionChange"
    >
      <template slot="platformSettlementId" slot-scope="{ $index, row }">
        <el-select
          v-model="row.platformSettlementId"
          placeholder="请选择"
          clearable
          filterable
          @change="(val) => platformCostConfigChange(val, $index)"
        >
          <el-option
            v-for="item in platformCostConfigList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </template>
      <template slot="proportion" slot-scope="{ row }">
        <JFX-Input
          v-model.trim="row.ratio"
          :precision="5"
          placeholder="请输入"
          clearable
          style="width: 203px"
        />
      </template>
      <template slot="action" slot-scope="{ $index }">
        <el-button
          type="text"
          v-if="$index === tableData.list.length - 1"
          @click="addToList"
        >
          增加
        </el-button>
        <el-button type="text" @click="removeListItem($index)">删除</el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
    <!-- 商品信息 end -->
    <!-- 底部按钮 -->
    <div class="mr-t-30 flex-c-c">
      <el-button
        @click="handleSubmit('save')"
        type="primary"
        :loading="saveLoading"
      >
        保 存
      </el-button>
      <el-button
        @click="handleSubmit('audit')"
        type="primary"
        :loading="saveLoading"
      >
        审 核
      </el-button>
      <el-button @click="askClose">取 消</el-button>
    </div>
    <!-- 底部按钮 end -->
    <!-- <AddSaleSDConfigModal v-if="visible.show"
                          :isVisible="visible"
                          :customerId="ruleForm.customerId.toString()"
                          @comfirm="comfirm"
                          type="platformCostConfigEdit"
                          :storePlatformCode="ruleForm.storePlatformCode.toString()"
    ></AddSaleSDConfigModal> -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    savePlatFormConfig,
    platformCostConfigDetail
  } from '@a/rebateManage/index'
  import { getPlatformCostConfigSelectBean } from '@a/base/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 表单信息
        ruleForm: {
          id: '',
          merchantId: '',
          merchantName: '',
          buId: '',
          buName: '',
          customerId: '',
          customerName: '',
          effectiveDateStr:
            this.$formatDate(Date.now(), 'yyyy-MM-dd') + ' 00:00:00',
          expirationDateStr:
            this.$formatDate(Date.now(), 'yyyy-MM-dd') + ' 23:59:59',
          storePlatformName: '',
          storePlatformCode: '',
          operate: '1'
        },
        // 表单校验规则
        rules: {
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          effectiveDateStr: {
            required: true,
            message: '请选择生效时间',
            trigger: 'change'
          },
          expirationDateStr: {
            required: true,
            message: '请选择失效时间',
            trigger: 'change'
          },
          buId: { required: true, message: '请选择事业部', trigger: 'change' },
          storePlatformCode: {
            required: true,
            message: '请选择平台',
            trigger: 'change'
          }
        },
        // 表格列数据
        tableColumn: [
          {
            label: '平台费项',
            slotTo: 'platformSettlementId',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '映射系统费项',
            prop: 'settlementConfigName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '比例',
            slotTo: 'proportion',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            minWidth: '100',
            align: 'center',
            hide: true
          }
        ],
        // 核销组件状态
        visible: { show: false },
        type: 'add',
        saveLoading: false,
        tableData: {
          list: [{}],
          loading: false,
          pageNum: 1,
          pageSize: 10000,
          total: 10001
        },
        platformCostConfigList: []
      }
    },
    mounted() {
      // 获取公司列表
      const userInfo = this.getUserInfo()
      if (userInfo.userType === '1') {
        // 平台管理用户 admin
        this.getSelectMerchantBean('merchant_list') // 获取该用户下的公司列表
      } else {
        // 商户
        // 当前用户的主体公司
        this.$set(this.selectOpt, 'merchant_list', [])
        this.selectOpt.merchant_list = [
          { key: userInfo.merchantId, value: userInfo.merchantName }
        ]
      }
      this.init()
    },
    methods: {
      init() {
        const { id, type } = this.$route.query
        this.type = type
        this.pageInit()
        if (id) {
          this.editInit()
        }
      },
      // 页面初始化
      pageInit() {
        // 获取公司名
        const userInfo = this.getUserInfo()
        this.ruleForm.merchantId = userInfo.merchantId || ''
        this.ruleForm.merchantName = userInfo.merchantName || ''
        this.changeMerchantIdorReminderType('init')
      },
      // 获取下拉列表
      async getPlatformCostConfigList() {
        const { storePlatformCode } = this.ruleForm
        const { data } = await getPlatformCostConfigSelectBean({
          storePlatformCode
        })
        this.platformCostConfigList = data || []
      },
      // 编辑页面初始化
      async editInit() {
        const { id } = this.$route.query
        const { data } = await platformCostConfigDetail({ id })
        for (const key in this.ruleForm) {
          this.ruleForm[key] = data[key] ? data[key].toString() : ''
        }
        this.ruleForm.effectiveDateStr = data.effectiveDate || ''
        this.ruleForm.expirationDateStr = data.expirationDate || ''
        this.ruleForm.customerId = data.customerId || ''
        const { itemList } = data
        if (itemList && itemList.length) {
          this.tableData.list = itemList
        }
        if (this.type === 'copy') this.ruleForm.id = ''
        // 获取下拉列表
        this.getPlatformCostConfigList()
      },
      // 删除
      removeTableItem() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        if (this.tableChoseList.length) {
          const ids = this.tableChoseList.map((item) => item.id)
          this.tableData.list = this.tableData.list.filter(
            (item) => !ids.includes(item.id)
          )
        }
      },
      platformCostConfigChange(val, index) {
        if (val) {
          const item = this.tableData.list[index]
          const target = this.platformCostConfigList.find(
            (e) => e.id === item.platformSettlementId
          )
          item.settlementConfigName = target.settlementConfigName || ''
          item.platformSettlementName = target.name || ''
        }
      },
      // 提交表单
      async handleSubmit(type) {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const flag = this.checkList()
            if (!flag) return false
            const {
              effectiveDateStr,
              expirationDateStr,
              merchantId,
              buId,
              customerId,
              storePlatformCode
            } = this.ruleForm
            const start = effectiveDateStr.substring(0, 7).replace(/-/gi, '')
            const end = expirationDateStr.substring(0, 7).replace(/-/gi, '')
            if (end < start) {
              this.$errorMsg('失效月份不能小于生效月份')
              return false
            }
            const gdata =
              this.selectOpt.merchant_list.find(
                (item) => +merchantId === +item.key
              ) || {}
            this.ruleForm.merchantName = gdata.value || ''
            const hdata =
              this.selectOpt.buList.find((item) => +buId === +item.key) || {}
            this.ruleForm.buName = hdata.value || ''
            const kdata =
              this.selectOpt.kuhuList.find(
                (item) => +customerId === +item.key
              ) || {}
            this.ruleForm.customerName = kdata.value || ''
            const ldata =
              this.selectOpt.storePlatformList.find(
                (item) => String(storePlatformCode) === String(item.key)
              ) || {}
            this.ruleForm.storePlatformName = ldata.value || ''
            // this.tableData.list.forEach(item => {
            //   const { platformSettlementId, platformSettlementName, categoryId, categoryName, superiorParentBrandId, superiorParentBrandName, ratio, brandParentName, isAllBrandParent, brandParentId } = item
            //   itemList.push({ platformSettlementId, platformSettlementName, categoryId, categoryName, superiorParentBrandId, superiorParentBrandName, ratio, brandParentName, isAllBrandParent, brandParentId })
            // })
            const itemList = this.tableData.list
            const opt = {
              ...this.ruleForm,
              // effectiveDateStr: effectiveDateStr.substring(0, 7),
              // expirationDateStr: expirationDateStr.substring(0, 7),
              itemList,
              operate: type === 'save' ? '1' : '2' // 1-保存，2-审核
            }
            this.saveLoading = true
            try {
              await savePlatFormConfig(opt)
              this.$successMsg('操作成功')
              this.closeTag()
            } catch (error) {
              console.log(error)
            }
            this.saveLoading = false
          }
        })
      },
      checkList() {
        // 校验商品
        let flag = true
        if (!this.tableData.list.length) {
          this.$errorMsg('配置信息不能为空')
          flag = false
          return flag
        }
        for (let i = 0; i < this.tableData.list.length; i++) {
          const { ratio } = this.tableData.list[i]
          if (!ratio) {
            this.$errorMsg(`商品表格第${i + 1}行，比例不能为空！`)
            flag = false
            break
          }
        }
        return flag
      },
      addToList() {
        this.tableData.list.push({})
      },
      removeListItem(index) {
        if (this.tableData.list.length <= 1) {
          this.$errorMsg('至少保留一项')
          return false
        }
        this.tableData.list.splice(index, 1)
      },
      changePlatformCode() {
        this.getPlatformCostConfigList()
      },
      // 修改供应商
      changeMerchantIdorReminderType(type) {
        delete this.selectOpt.buList
        delete this.selectOpt.kuhuList
        if (this.ruleForm.merchantId) {
          this.getBUSelectBean('buList', {
            merchantId: this.ruleForm.merchantId
          }) // 获取事业部
          this.getCustomerByMerchantId(
            'kuhuList',
            {
              cusType: 1,
              merchantId: this.ruleForm.merchantId,
              begin: 0,
              pageSize: 10000
            },
            { key: 'id', value: 'name' }
          ) // 获取客户列表
        }
        if (type !== 'init') {
          // 非初始情况
          this.ruleForm.customerId = ''
          this.ruleForm.buId = ''
        }
      },
      // 显示弹窗
      showModal() {
        if (!this.ruleForm.storePlatformCode) {
          this.$errorMsg('请选择平台')
          return false
        }
        this.visible.show = true
      },
      // 隐藏弹窗
      closeModal(type) {
        switch (type) {
          // 选择单据
          case 'addSaleSDConfig':
            this.addSaleSDConfig.visible.show = false
            break
        }
        this.getList()
      },
      comfirm(list) {
        console.log(list)
        let flag = true
        // 校验
        list.forEach((item) => {
          const {
            superiorParentBrandId,
            categoryId,
            brandParentId,
            isAllBrandParent,
            superiorParentBrandName,
            brandParentName,
            platformSettlementId
          } = item
          let target = null
          if (isAllBrandParent === '1') {
            // 全子品牌
            target = this.tableData.list.find(
              (gtem) =>
                +platformSettlementId === +gtem.platformSettlementId &&
                +categoryId === +gtem.categoryId &&
                +superiorParentBrandId === +gtem.superiorParentBrandId &&
                +isAllBrandParent === +gtem.isAllBrandParent
            )
          } else {
            target = this.tableData.list.find(
              (gtem) =>
                +platformSettlementId === +gtem.platformSettlementId &&
                +categoryId === +gtem.categoryId &&
                +superiorParentBrandId === +gtem.superiorParentBrandId &&
                +brandParentId === +gtem.brandParentId
            )
          }
          if (target) {
            flag = false
            this.$errorMsg(
              superiorParentBrandName + ',' + brandParentName + '存在重复数据'
            )
            return false
          }
        })
        if (!flag) return false
        this.tableData.list.push(...list)
        this.visible.show = false
      },
      askClose() {
        this.$showToast('系统将不保留更改，是否取消?', () => {
          this.closeTag()
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.sales-add-bx .el-form-item__label {
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
  ::v-deep.sales-add-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  .title-bx {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  ::v-deep .el-icon-right {
    display: none;
  }
</style>
