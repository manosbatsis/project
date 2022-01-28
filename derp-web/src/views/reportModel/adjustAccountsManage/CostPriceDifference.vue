<template>
  <!-- 销售订单列表页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->

    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
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
                @change="buIdChange"
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
            <el-form-item
              label="类型："
              prop="stockLocationTypeId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.stockLocationTypeId"
                placeholder="请选择"
                filterable
                clearable
              >
                <el-option
                  v-for="item in typeSelectList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="条形码："
              prop="barcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.barcode"
                placeholder="请输入"
                clearable
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
                v-model.trim="searchProps.goodsName"
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
          v-permission="'cost_price_difference_refresh'"
          v-loading="refreshBtnLoading"
          type="primary"
          @click="refreshTableItem"
        >
          刷新
        </el-button>
        <el-button
          v-permission="'cost_price_difference_export'"
          type="primary"
          @click="exportList"
        >
          导出
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->

    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="agio" slot-scope="{ row }">
        {{ ((row.purchasePrice || 0) - (row.fixedCost || 0)).toFixed(8) }}
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getCostPriceDifferenceList,
    refreshCostPriceDifference,
    exportCostPriceDifference
  } from '@a/adjustAccountsManage'
  import { getBuStockLocationTypeConfigSelect } from '@a/base'
  export default {
    mixins: [commomMix],
    data() {
      return {
        searchProps: {
          buId: '',
          stockLocationTypeId: '',
          goodsName: '',
          barcode: ''
        },
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '条形码',
            prop: 'barcode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '类型',
            prop: 'stockLocationTypeName',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '固定成本价',
            prop: 'fixedCost',
            minWidth: '150',
            align: 'center',
            hide: true,
            formatter: (row) => `${row.currency} ${row.fixedCost}`
          },
          {
            label: '采购价盘',
            prop: 'purchasePrice',
            minWidth: '150',
            align: 'center',
            hide: true,
            formatter: (row) => `${row.currency} ${row.purchasePrice}`
          },
          {
            label: '成本价差',
            prop: 'agio',
            minWidth: '150',
            align: 'center',
            hide: true,
            formatter: (row) =>
              `${row.currency} ${(
                (row.purchasePrice || 0) - (row.fixedCost || 0)
              ).toFixed(8)}`
          }
        ],
        typeSelectList: [],
        refreshBtnLoading: false
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.getList()
    },
    methods: {
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await getCostPriceDifferenceList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return
        }
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportCostPriceDifference, { ids })
          })
          return
        }
        this.$exportFile(exportCostPriceDifference, { ...this.searchProps })
      },
      async refreshTableItem() {
        try {
          this.refreshBtnLoading = true
          await refreshCostPriceDifference()
          this.$successMsg('刷新成功')
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.refreshBtnLoading = false
        }
      },
      async getTypeSeclectList(buId) {
        try {
          const { data } = await getBuStockLocationTypeConfigSelect({ buId })
          this.typeSelectList = data || []
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      buIdChange(buId) {
        this.getTypeSeclectList(buId)
      },
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
