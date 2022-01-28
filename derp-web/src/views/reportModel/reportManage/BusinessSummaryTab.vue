<template>
  <!-- 事业部财务进销存页面 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetSearchForm"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.buId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getBUSelectBean('businessList')"
              >
                <el-option
                  v-for="item in selectOpt.businessList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="年月：" class="search-panel-item fs-14 clr-II">
              <el-date-picker
                v-model="searchProps.ownMonth"
                placeholder="请选择"
                type="month"
                value-format="yyyy-MM"
                :clearable="false"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品名称："
              prop="productName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.productName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="条码："
              prop="barcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.barcode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准品牌："
              prop="brandIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="brandIds"
                placeholder="请选择"
                filterable
                multiple
                clearable
                collapse-tags
                :data-list="getBrandParent('brandList')"
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
            <el-form-item
              label="仓库："
              prop="depotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="depotIds"
                placeholder="请选择"
                filterable
                clearable
                multiple
                collapse-tags
                :data-list="getSelectBeanByMerchantRel('warehouseList')"
              >
                <el-option
                  v-for="item in selectOpt.warehouseList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="货号："
              prop="goodsNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.goodsNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'buSummary_createTask'"
          v-loading="exportBtnLoading"
          @click="exportList('excel')"
        >
          生成excel
        </el-button>
        <el-button
          type="primary"
          v-permission="'buSummary_buFlashInventorSummary'"
          v-loading="flashBtnLoading"
          @click="flashReport"
        >
          刷新报表
        </el-button>
        <span v-if="createDate" style="color: red; margin-left: 10px">
          生成时间：{{ createDate }}
        </span>
        <span v-if="accountState" style="color: red; margin-left: 10px">
          月结状态：{{ accountState }}
        </span>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showIndex
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="monthInstorageNum" slot-scope="{ row }">
        <el-button type="text" @click="jumpDetail(row, '1')">
          {{ row.monthInstorageNum }}
        </el-button>
      </template>
      <template slot="monthOutstorageNum" slot-scope="{ row }">
        <el-button type="text" @click="jumpDetail(row, '0')">
          {{ row.monthOutstorageNum }}
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    buInventorSummaryList,
    createBuInventorSummaryTask,
    flashBuSummary,
    getMonthlyAccount
  } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          buId: '',
          ownMonth: '',
          productName: '',
          barcode: '',
          brandIds: '',
          depotIds: '',
          goodsNo: ''
        },
        // 生成时间
        accountState: '',
        createDate: '',
        brandIds: [],
        depotIds: [],
        // 表格列数据
        tableColumn: [
          {
            label: '仓库',
            prop: 'depotName',
            minWidth: '130',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '货号',
            prop: 'goodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '品牌',
            prop: 'brandName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '条码',
            prop: 'barcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '8位码',
            prop: 'factoryNo',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '理货单位',
            prop: 'unitLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本月期初库存',
            prop: 'monthBeginNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本月正常品期初库存',
            prop: 'monthBeginNormalNum',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '本月残次品期初库存',
            prop: 'monthBeginDamagedNum',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '本月入库',
            slotTo: 'monthInstorageNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本月出库',
            slotTo: 'monthOutstorageNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '来货残次',
            prop: 'monthInBadNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '出库残次',
            prop: 'monthOutBadNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '好品损益',
            prop: 'profitlossGoodNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '坏品损益',
            prop: 'profitlossDamagedNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '损益',
            prop: 'monthProfitlossNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本月期末库存',
            prop: 'monthEndNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本月正常品期末库存',
            prop: 'monthEndNormalNum',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '本月残次品期末库存',
            prop: 'monthEndDamagedNum',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '销售未确认',
            prop: 'monthSaleUnconfirmedNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '采购未上架',
            prop: 'monthPurchaseNotshelfNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本期调拨在途',
            prop: 'monthTransferNotshelfNum',
            width: '100',
            align: 'center',
            hide: true
          }
        ],
        exportBtnLoading: false,
        flashBtnLoading: false
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.searchProps.ownMonth = this.$formatDate(new Date(), 'yyyy-MM')
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        this.searchProps.brandIds = this.brandIds.length
          ? this.brandIds.toString()
          : ''
        this.searchProps.depotIds = this.depotIds.length
          ? this.depotIds.toString()
          : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await buInventorSummaryList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
          if (this.tableData.list.length) {
            this.createDate =
              this.tableData.list[this.tableData.list.length - 1].createDate ||
              ''
          }
          this.getMonthlyAccount() // 查询月结状态
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 查询月结状态
      async getMonthlyAccount() {
        const { ownMonth: settlementMonth } = this.searchProps
        const depotIds = this.depotIds.length ? this.depotIds.toString() : ''
        try {
          const { data } = await getMonthlyAccount({
            settlementMonth,
            depotIds
          })
          this.accountState = data && data.state === '2' ? '已结转' : '未结转'
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 刷新报表
      async flashReport() {
        const { ownMonth, buId } = this.searchProps
        const depotIds = this.depotIds.length ? this.depotIds.toString() : ''
        if (!ownMonth) {
          this.$errorMsg('请先选择查询月份')
          return false
        }
        try {
          this.flashBtnLoading = true
          await flashBuSummary({
            ownMonth,
            depotIds,
            buId,
            flashForward: false
          })
          this.$successMsg('正在刷新,请稍后再查询')
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.flashBtnLoading = false
        }
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出！')
          return false
        }
        const { ownMonth, buId } = this.searchProps
        let depotIds = ''
        let depotNames = ''
        if (this.depotIds.length) {
          depotIds = this.depotIds.toString()
          depotNames = this.depotIds
            .map((id) => {
              const target = this.selectOpt.warehouseList.find(
                (item) => item.key === id
              )
              return target ? target.value : ''
            })
            .toString()
        }
        if (!ownMonth) {
          this.$errorMsg('请先选择查询月份')
          return false
        }
        try {
          this.exportBtnLoading = true
          await createBuInventorSummaryTask({
            ownMonth,
            buId,
            depotIds,
            depotNames
          })
          this.$alertSuccess('请在报表任务列表查看进度')
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.exportBtnLoading = false
        }
      },
      jumpDetail(row, operateType) {
        const { merchantId, depotId, buId, ownMonth, goodsNo, unit } = row
        this.linkTo(
          `/report/BusinessSummaryDetail?merchantId=${
            merchantId || ''
          }&depotId=${depotId || ''}&buId=${buId || ''}&ownMonth=${
            ownMonth || ''
          }&goodsNo=${goodsNo || ''}&unit=${
            unit || ''
          }&operateType=${operateType}`
        )
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.searchProps.ownMonth = this.$formatDate(new Date(), 'yyyy-MM')
          this.depotIds = []
          this.brandIds = []
          this.getList(1)
        })
      }
    }
  }
</script>
