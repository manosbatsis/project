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
              label="条形码："
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
              label="仓库："
              prop="depotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.depotId"
                placeholder="请选择"
                filterable
                clearable
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
            <el-form-item label="年月：" class="search-panel-item fs-14 clr-II">
              <el-date-picker
                v-model="searchProps.month"
                placeholder="请选择"
                type="month"
                value-format="yyyy-MM"
                :clearable="false"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品名称："
              prop="goodsName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.goodsName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="品牌："
              prop="brandId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.brandId"
                placeholder="请选择"
                filterable
                clearable
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->

    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
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
    />
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getBuLocationSummaryList, getMonthlyAccount } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        searchProps: {
          barcode: '',
          depotId: '',
          month: '',
          goodsName: '',
          brandId: '',
          buId: ''
        },
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '150'
          },
          {
            label: '仓库名称',
            prop: 'depotName',
            minWidth: '150'
          },
          {
            label: '报表月份',
            prop: 'month',
            width: '120'
          },
          {
            label: '条形码',
            prop: 'barcode',
            minWidth: '150'
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '150'
          },
          {
            label: '标准品牌',
            prop: 'brandName',
            minWidth: '120'
          },
          {
            label: '母品牌',
            prop: 'superiorParentBrand',
            minWidth: '120'
          },
          {
            label: '库位类型',
            prop: 'stockLocationTypeName',
            width: '100'
          },
          {
            label: '本月期初量',
            prop: 'monthBeginNum',
            width: '100'
          },
          {
            label: '本月入库量',
            prop: 'monthInstorageNum',
            width: '100'
          },
          {
            label: '本月出库量',
            prop: 'monthOutstorageNum',
            width: '100'
          },
          {
            label: '本月损益量',
            prop: 'monthProfitlossNum',
            width: '100'
          },
          {
            label: '本月调整数量',
            prop: 'monthAdjustmentNum',
            width: '100'
          },
          {
            label: '本月期末量',
            prop: 'monthEndNum',
            width: '100'
          },
          {
            label: '好品期末量',
            prop: 'monthEndNormalNum',
            width: '100'
          },
          {
            label: '坏品期末量',
            prop: 'monthEndDamagedNum',
            width: '100'
          }
        ],
        /* 月结状态 */
        accountState: '',
        /* 生成时间 */
        createDate: ''
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
      this.getMonthlyAccount()
      this.getList()
    },
    methods: {
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await getBuLocationSummaryList({
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
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      /* 查询月结状态 */
      async getMonthlyAccount() {
        const { month: settlementMonth, depotId: depotIds } = this.searchProps
        try {
          const {
            data: { state }
          } = await getMonthlyAccount({
            settlementMonth,
            depotIds
          })
          this.accountState = state === '2' ? '已结转' : '未结转'
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
          this.getList(1)
        })
      }
    }
  }
</script>
