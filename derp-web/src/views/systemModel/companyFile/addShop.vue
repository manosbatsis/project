<template>
  <!-- 客户新增/编辑页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <JFX-Form :model="baseForm" ref="baseForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="店铺类型：" prop="storeTypeCode">
            <el-select
              v-model="baseForm.storeTypeCode"
              placeholder="请选择"
              clearable
              filterable
              @change="storeTypeCodeChange()"
              :data-list="getSelectList('merchantShopRel_storeTypeList')"
            >
              <el-option
                v-for="item in selectOpt.merchantShopRel_storeTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="运营类型：" prop="shopTypeCode">
            <el-select
              v-model="baseForm.shopTypeCode"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('merchantShopRel_shopTypeList')"
            >
              <el-option
                v-for="item in selectOpt.merchantShopRel_shopTypeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="店铺名称：" prop="shopName">
            <el-input
              v-model="baseForm.shopName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="店铺编码：" prop="shopCode">
            <el-input
              v-model="baseForm.shopCode"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="数据来源：" prop="dataSourceCode">
            <el-select
              v-model="baseForm.dataSourceCode"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('dataSourceList')"
            >
              <el-option
                v-for="item in selectOpt.dataSourceList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="状态：" prop="status">
            <el-select
              v-model="baseForm.status"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in [
                  { key: '1', value: '启用' },
                  { key: '0', value: '禁用' }
                ]"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            label="开店公司："
            prop="merchantId"
            v-show="baseForm.storeTypeCode"
          >
            <template v-if="baseForm.storeTypeCode === 'DZD'">
              <el-select
                v-model="baseForm.merchantId"
                placeholder="请选择"
                clearable
                filterable
                @change="merchantChange()"
                :data-list="getSelectMerchantBean('merchantList')"
              >
                <el-option
                  v-for="item in selectOpt.merchantList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </template>
            <template v-else-if="baseForm.storeTypeCode === 'WBD'">
              <el-input
                v-model="baseForm.merchantId"
                placeholder="请输入"
                clearable
              />
            </template>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="客户：" prop="customerId">
            <el-select
              v-model="baseForm.customerId"
              placeholder="请选择"
              clearable
              filterable
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
          <el-form-item label="平台名称：" prop="storePlatformCode">
            <el-select
              v-model="baseForm.storePlatformCode"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectList('storePlatformList')"
            >
              <el-option
                v-for="item in selectOpt.storePlatformList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="仓库：" prop="depotId">
            <el-select
              v-model="baseForm.depotId"
              placeholder="请选择"
              clearable
              filterable
              :list-data="getDepotSelectBean('depotIdList')"
              @change="depotChange()"
            >
              <el-option
                v-for="item in selectOpt.depotIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item
            v-if="baseForm.storeTypeCode === 'DZD'"
            label="开店事业部："
            prop="buId"
          >
            <el-select
              v-model="baseForm.buId"
              placeholder="请选择"
              clearable
              filterable
              @change="buIdChange"
            >
              <el-option
                v-for="item in selectOpt.buIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="店铺统一ID：" prop="shopUnifyId">
            <el-input
              v-model="baseForm.shopUnifyId"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="专营母品牌：" prop="superiorParentBrandId">
            <el-select
              v-model="baseForm.superiorParentBrandId"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getBrandList('brandList')"
            >
              <el-option
                v-for="item in selectOpt.brandList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="结算币种：" prop="currency">
            <el-select
              v-model="baseForm.currency"
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
          <el-form-item
            v-if="baseForm.storeTypeCode === 'DZD'"
            label="库位类型："
            prop="stockLocationTypeId"
          >
            <el-select
              v-model="baseForm.stockLocationTypeId"
              placeholder="请选择"
              clearable
              filterable
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
      </el-row>
    </JFX-Form>
    <!-- 货主信息 -->
    <JFX-title title="店铺货主信息" className="mr-t-10" />
    <JFX-table
      :tableData.sync="tableData"
      :showPagination="false"
      showSelection
      @selection-change="selectionChange"
    >
      <el-table-column prop="action" label="操作" width="120">
        <template slot-scope="scope">
          <el-button type="text" @click="addTable">新增</el-button>
          <el-button type="text" @click="delTable(scope)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column label="货主公司">
        <template slot-scope="{ row }">
          <el-select v-model="row.t_mid" @change="tableMidChange(row)">
            <el-option
              v-for="item in t_midList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="货主事业部">
        <template slot-scope="{ row }">
          <el-select v-model="row.t_bid" @change="changeTableBu(row)">
            <el-option
              v-for="item in row.t_bidList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="库位类型">
        <template slot-scope="{ row }">
          <el-select v-model="row.t_stockLocationTypeId" clearable>
            <el-option
              v-for="item in row.t_stockLocationTypeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
    </JFX-table>
    <div class="mr-t-30 flex-c-c">
      <el-button @click="handleSubmit" type="primary">保 存</el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getBUSelectBean,
    getBuStockLocationTypeConfigSelect,
    getLocationManage
  } from '@a/base'
  import { saveShop, shopToEditPage, modifyShop } from '@a/companyFile/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 基本信息
        baseForm: {
          storeTypeCode: '',
          shopTypeCode: '',
          shopName: '',
          shopCode: '',
          dataSourceCode: '',
          status: '',
          merchantId: '',
          customerId: '',
          storePlatformCode: '',
          depotId: '',
          buId: '',
          shopUnifyId: '',
          superiorParentBrandId: '',
          currency: '',
          stockLocationTypeId: ''
        },
        // 表单规则
        rules: {
          storeTypeCode: {
            required: true,
            message: '请输入店铺类型',
            trigger: 'change'
          },
          shopTypeCode: {
            required: true,
            message: '请输入运营类型',
            trigger: 'change'
          },
          shopName: {
            required: true,
            message: '请输入店铺名称',
            trigger: 'change'
          },
          shopCode: {
            required: true,
            message: '请输入店铺编码',
            trigger: 'change'
          },
          dataSourceCode: {
            required: true,
            message: '请输入数据来源',
            trigger: 'change'
          },
          merchantId: {
            required: false,
            message: '请选择开店公司',
            trigger: 'change'
          },
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          storePlatformCode: {
            required: true,
            message: '请选择平台名称',
            trigger: 'change'
          },
          depotId: { required: true, message: '请选择仓库', trigger: 'change' },
          buId: {
            required: true,
            message: '请选择开店事业部',
            trigger: 'change'
          },
          shopUnifyId: {
            required: true,
            message: '请输入店铺统一ID',
            trigger: 'change'
          },
          superiorParentBrandId: {
            required: true,
            message: '请输入专营母品牌',
            trigger: 'change'
          },
          currency: {
            required: true,
            message: '请输入结算币种',
            trigger: 'change'
          },
          stockLocationTypeId: {
            required: false,
            message: '请选择库位类型',
            trigger: 'change'
          }
        },
        t_midList: [],
        typeSelectList: []
      }
    },
    mounted() {
      this.getSelectMerchantBean('merchantList')
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        id ? this.editInit(id) : this.addInit()
      },
      addInit() {
        this.addTable()
      },
      async editInit(id) {
        const {
          data: { detail, shipperList }
        } = await shopToEditPage({ id })
        for (const key in this.baseForm) {
          this.baseForm[key] = detail[key] ? String(detail[key]) : ''
        }
        // 触发店铺改变
        this.storeTypeCodeChange(
          String(detail.depotId),
          String(detail.merchantId || ''),
          Number(detail.customerId)
        )
        // 单主店
        if (detail.storeTypeCode === 'DZD') {
          // 触发开店公司改变
          this.merchantChange(
            Number(detail.customerId),
            String(detail.buId || ''),
            String(detail.depotId)
          )
        } else {
          // 外部店
          this.baseForm.merchantId = detail.merchantName
        }
        // 触发仓库改变
        this.depotChange(String(detail.buId || ''))
        // 店铺货主信息恢复
        if (shipperList.length) {
          const resultList = []
          for (let i = 0; i < shipperList.length; i++) {
            const itemdata = shipperList[i]
            const { data: Tbidlist } = await getBUSelectBean({
              merchantId: itemdata.merchantId,
              depotId1: detail.depotId
            })
            const { data: tockLocationTypeList } =
              await getBuStockLocationTypeConfigSelect({
                buId: itemdata.buId,
                merchantId: itemdata.merchantId
              })
            resultList[i] = {
              id: itemdata.id,
              t_mid: itemdata?.merchantId?.toString(),
              t_bid: itemdata?.buId?.toString(),
              t_bidList: Tbidlist,
              t_stockLocationTypeId: itemdata?.stockLocationTypeId?.toString(),
              t_stockLocationTypeList: tockLocationTypeList
            }
          }
          this.tableData.list = resultList
        } else {
          this.addTable()
        }
      },
      // 改变店铺类型
      async storeTypeCodeChange(initDepotId, initMerchantId, initCustomerId) {
        const { storeTypeCode } = this.baseForm
        // 清空仓库
        this.baseForm.depotId = initDepotId || ''
        // 清空开店公司
        this.baseForm.merchantId = initMerchantId || ''
        // 单主店
        if (storeTypeCode === 'DZD') {
          // 清空加载开店公司
          this.baseForm.merchantId = initMerchantId || ''
          // 清空客户，客户根据开店公司加载
          this.$set(this.selectOpt, 'customerList', [])
          this.baseForm.customerId = initCustomerId || ''
          // 开店公司必选
          this.rules.merchantId.required = true
          /* 查询是否启用库位管理 */
          this.getLocationManage()
          /* 获取库位类型下拉列表 */
          this.getTypeSeclectList()
        }
        // 外部店
        if (storeTypeCode === 'WBD') {
          /* 清空库位类型 */
          this.baseForm.stockLocationTypeId = ''
          // 加载全部仓库
          this.baseForm.depotId = initDepotId || ''
          delete this.selectOpt.depotIdList
          this.getDepotSelectBean('depotIdList')
          // 加载全部客户
          this.baseForm.customerId = initCustomerId || ''
          delete this.selectOpt.customerList
          this.getSelectBeanByDto(
            'customerList',
            { cusType: '1', name: '' },
            { key: 'id', value: 'name', code: '' }
          )
          // 开店公司不必选
          this.rules.merchantId.required = false
        }
        this.$refs.baseForm.clearValidate()
        this.initTableSelectData()
      },
      //  开店公司改变
      merchantChange(initCustomerId, initBuId, initDepotId) {
        const { merchantId } = this.baseForm
        // 客户下拉重置
        this.baseForm.customerId = initCustomerId || ''
        delete this.selectOpt.customerList
        this.getCustomerByMerchantId(
          'customerList',
          { merchantId, cusType: '1' },
          { key: 'id', value: 'name', code: '' }
        )
        // 事业部重置
        this.baseForm.buId = initBuId || ''
        delete this.selectOpt.buIdList
        this.getBUSelectBean('buIdList', { merchantId })
        // 仓库
        this.baseForm.depotId = initDepotId || ''
        delete this.selectOpt.depotIdList
        this.getSelectBeanByMerchantRel('depotIdList', { merchantId })
        this.initTableSelectData()
      },
      // 仓库改变
      depotChange(initBuId) {
        const { merchantId, depotId, storeTypeCode } = this.baseForm
        // 改变事业部
        this.baseForm.buId = initBuId || ''
        if (storeTypeCode === 'DZD') {
          delete this.selectOpt.buIdList
          this.getBUSelectBean('buIdList', { merchantId, depotId1: depotId })
        }
        this.initTableSelectData(true)
      },
      // 初始化 货主信息 下拉菜单可选择select
      // 是否是改变仓库操作
      initTableSelectData(isChangeDepot = false) {
        const { merchantId, depotId } = this.baseForm
        // 单主店
        if (this.baseForm.storeTypeCode === 'DZD') {
          // 清空选择公司下拉
          this.t_midList = this.selectOpt.merchantList
            ? this.selectOpt.merchantList.filter(
                (item) => item.key === merchantId
              )
            : []
        }
        // 外部店
        if (this.baseForm.storeTypeCode === 'WBD') {
          // 清空选择公司下拉
          this.t_midList = this.selectOpt.merchantList || []
        }
        // 表当清空
        this.tableData.list.forEach(async (item) => {
          // 除操作仓库外，其他操作，都清空表单
          if (!isChangeDepot) {
            item.t_bid = ''
            item.t_mid = ''
          }
          // 操作仓库，并且货主公司有值，事业部下拉重新加载
          if (item.t_mid && isChangeDepot) {
            item.t_bid = ''
            const { data } = await getBUSelectBean({
              merchantId: item.t_mid,
              depotId1: depotId
            })
            item.t_bidList = data
          }
        })
      },
      // 表格货品公司改变
      async tableMidChange(row) {
        const { depotId } = this.baseForm
        const { data } = await getBUSelectBean({
          merchantId: row.t_mid,
          depotId1: depotId
        })
        row.t_bidList = data
      },
      // 添加表格
      addTable() {
        this.tableData.list.push({
          id: Math.random(),
          t_mid: '',
          t_bid: '',
          t_bidList: [],
          t_stockLocationTypeId: '',
          t_stockLocationTypeList: []
        })
      },
      // 删除表格
      delTable({ $index }) {
        if (this.tableData.list.length === 1) {
          return this.$warningMsg('最少存在一条数据')
        }
        this.tableData.list.splice($index, 1)
      },
      checkParam() {
        const { buId, storeTypeCode } = this.baseForm
        const tableList = this.tableData.list.filter((item) => item.t_mid)
        // 外部店
        if (storeTypeCode === 'WBD' && !tableList.length) {
          this.$warningMsg('店铺编码为外部店时，店铺信息至少存在一条记录')
          return false
        }
        // 是否和开店事业部重复
        // 是否两个都为空
        // 开店事业部和开店公司，不能重复
        const isExistMid = [] // 已存在的 m_id 公司id
        const isExistBid = [] // 已存在的 b_id 事业部id
        const flag = tableList.every((item, index) => {
          if (item.t_bid || item.t_mid) {
            if (item.t_bid && item.t_mid) {
              // 是否和开店事业部重复
              if (item.t_bid === buId) {
                this.$warningMsg(
                  `第${index + 1}行,货主事业部不能和开店事业部重复`
                )
                return false
              }
              // 公司是否重复
              if (isExistMid.includes(item.t_mid)) {
                // 事业部是否重复
                if (isExistBid.includes(item.t_bid)) {
                  this.$warningMsg(
                    `第${index + 1}行,该货主公司或货主事业部重复`
                  )
                  return false
                } else {
                  isExistBid.push(item.t_bid)
                }
              } else {
                isExistMid.push(item.t_mid)
                isExistBid.push(item.t_bid)
              }
              return true
            } else {
              this.$warningMsg(
                `第${index + 1}行，${
                  item.t_bid ? '货主公司' : '货主事业部'
                }不能为空`
              )
              return false
            }
          } else {
            return true
          }
        })
        return flag
      },
      getParam() {
        const result = {
          ...this.baseForm,
          buName:
            this.baseForm.buId && this.selectOpt.buIdList
              ? this.selectOpt.buIdList.filter(
                  (item) => item.key === this.baseForm.buId
                )[0].value
              : ''
        }
        // 外部店
        if (this.baseForm.storeTypeCode === 'WBD') {
          result.merchantName = this.baseForm.merchantId
          result.merchantId = ''
        } else {
        }
        const selectOpt = this.selectOpt
        result.shipperList = this.tableData.list
          .filter((item) => item.t_bid && item.t_mid)
          .map((item) => {
            const merchantName = selectOpt.merchantList.filter(
              (mItem) => mItem.key === item.t_mid
            )[0]?.value
            const stockLocationTypeName = item.t_stockLocationTypeList.filter(
              (sItem) => sItem.value === item.t_stockLocationTypeId
            )[0]?.label
            return {
              buId: item.t_bid,
              buName: item.t_bidList.filter(
                (bItem) => bItem.value === item.t_bid
              )[0].label,
              merchantId: item.t_mid,
              merchantName,
              stockLocationTypeId: item.t_stockLocationTypeId,
              stockLocationTypeName
            }
          })
        if (this.$route.query.id) {
          result.id = this.$route.query.id
        }
        return {
          json: JSON.stringify(result)
        }
      },
      // 提交表单
      handleSubmit() {
        this.$refs.baseForm.validate(async (valid) => {
          if (!valid) {
            return this.$warningMsg('请先填写必填字段')
          }
          if (!this.checkParam()) {
            return false
          }
          const param = this.getParam()
          this.$route.query.id ? await modifyShop(param) : await saveShop(param)
          this.$successMsg('保存成功')
          this.closeTag()
        })
      },
      /* 改变事业部 */
      async buIdChange(buId) {
        const { storeTypeCode, merchantId } = this.baseForm
        if (!buId || !merchantId || storeTypeCode !== 'DZD') {
          return
        }
        /* 查询是否启用库位管理 */
        this.getLocationManage()
        /* 获取库位类型下拉列表 */
        this.getTypeSeclectList()
      },
      /* 查询是否启用库位管理 */
      async getLocationManage() {
        const { buId, merchantId } = this.baseForm
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
      /* 获取库位类型下拉列表 */
      async getTypeSeclectList() {
        const { buId, merchantId } = this.baseForm
        if (!buId || !merchantId) return
        try {
          const { data } = await getBuStockLocationTypeConfigSelect({
            buId,
            merchantId
          })
          this.typeSelectList = data || []
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 改变表格的货主事业部 */
      async changeTableBu(row) {
        const { t_bid: buId, t_mid: merchantId } = row
        try {
          const { data } = await getBuStockLocationTypeConfigSelect({
            buId,
            merchantId
          })
          row.t_stockLocationTypeList = data || []
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
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
