<template>
  <!-- 库存批次快照列表 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @search="getList(1)" @reset="resetSearchForm">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="查询月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                type="month"
                placeholder="请选择"
                style="width: 203px"
                value-format="yyyy-MM"
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
                :data-list="getBrandSelectBean('brandList')"
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
              label="商品货号："
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'inventoryBatch_exportInventoryBatch'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'inventoryBatch_exportInventoryBatch'"
          @click="flashBuInventory"
        >
          刷新
        </el-button>
        <span class="clr-r fs-14" style="margin-left: 10px">
          {{ this.flashDate ? `刷新日期：${this.flashDate}` : '' }}
        </span>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      ref="table"
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column
        type="selection"
        align="center"
        width="55"
      ></el-table-column>
      <el-table-column
        prop="merchantName"
        align="center"
        min-width="100"
        label="公司"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="buName"
        align="center"
        min-width="120"
        label="事业部"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="depotName"
        align="center"
        min-width="120"
        label="仓库名称"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="brandName"
        align="center"
        min-width="120"
        label="品牌"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="goodsNo"
        align="center"
        min-width="100"
        label="商品货号"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="goodsName"
        align="center"
        min-width="100"
        label="商品名称"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="barcode"
        align="center"
        min-width="100"
        label="商品条形码"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="surplusNum"
        align="center"
        width="90"
        label="库存数量"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="okNum"
        align="center"
        width="90"
        label="好品数量"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="wornNum"
        align="center"
        width="90"
        label="坏品数量"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="unitLabel"
        align="center"
        width="90"
        label="理货单位"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="month"
        align="center"
        width="100"
        label="归属月份"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="createDate"
        align="center"
        width="150"
        label="刷新日期"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="left" width="80" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" @click="handleExpand(row)">
            {{ row.show ? '收起' : '展开' }}
            <i :class="row.show ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" />
          </el-button>
        </template>
      </el-table-column>
      <el-table-column type="expand" width="1">
        <template slot-scope="{ row }">
          <div class="inner-container">
            <el-table
              :data="row.children"
              :border="false"
              :header-cell-style="{ background: '#e4e8ed' }"
              stripe
              class="inner-table"
              ref="subTable"
              v-loading="row.loading"
            >
              <el-table-column
                prop="batchNo"
                align="center"
                min-width="120"
                label="批次号"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="productionDate"
                align="center"
                min-width="120"
                label="生产日期"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="overdueDate"
                align="center"
                min-width="80"
                label="失效日期"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="effectiveIntervalLabel"
                align="center"
                min-width="80"
                label="效期区间"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="typeLabel"
                align="center"
                min-width="100"
                label="库存类型"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="num"
                align="center"
                min-width="100"
                label="批次库存数量"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="firstShelfDate"
                align="center"
                min-width="100"
                label="首次上架日期"
                show-overflow-tooltip
              ></el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listBuInventory,
    exportBuInventory,
    getBuInventoryItem,
    flashBuInventory
  } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          month: '',
          depotId: '',
          buId: '',
          brandId: '',
          goodsNo: '',
          barcode: '',
          goodsName: ''
        },
        // 刷新日期
        flashDate: ''
      }
    },
    activated() {
      const { buId } = this.$route.query
      this.searchProps.buId = buId || ''
      this.getList()
    },
    mounted() {
      const { buId } = this.$route.query
      this.searchProps.buId = buId || ''
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listBuInventory({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
          const len = this.tableData.list.length
          if (len) {
            this.tableData.list = this.tableData.list.map((item) => ({
              ...item,
              loading: false,
              children: []
            }))
            // 刷新日期取单据最后一条的createDate
            this.flashDate = this.tableData.list[len - 1].createDate || ''
          }
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 点击展开表格行
      async handleExpand(row) {
        const tableRef = this.$refs.table.$refs['el-table']
        // 先关闭之前展开的表格行
        this.tableData.list.forEach((item) => {
          if (row.id !== item.id) {
            item.show = false
            tableRef.toggleRowExpansion(item, false)
          } else {
            item.show = !item.show
            tableRef.toggleRowExpansion(item, item.show)
          }
        })
        // row.show = !row.show
        // tableRef.toggleRowExpansion(row, row.show)
        if (row.id) {
          try {
            row.loading = true
            const { data } = await getBuInventoryItem({ id: row.id })
            row.children = data || []
          } catch (error) {
            this.$errorMsg(error.message)
          } finally {
            row.loading = false
          }
        }
      },
      // 刷新
      flashBuInventory() {
        const { month, depotId, buId } = this.searchProps
        if (!month) {
          this.$errorMsg('请选择月份')
          return false
        }
        let message = '确定刷新筛选的数据?'
        if (!buId && !depotId) {
          message = '仓库未选择,确定刷新所有仓的数据?'
        }
        if (!buId) {
          message = '事业部未选择,确定刷新所有仓的数据?'
        }
        if (!depotId) {
          message = '仓库未选择,确定刷新所有仓的数据?'
        }
        this.$showToast(message, async () => {
          try {
            const { data } = await flashBuInventory({ month, depotId, buId })
            data !== '{}' ? this.$errorMsg(data) : this.$successMsg('刷新成功')
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.$exportFile(exportBuInventory, { ...this.searchProps })
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  // 隐藏箭头
  ::v-deep.el-table__expand-column .cell {
    display: none;
  }

  ::v-deep.jfx-table .el-table__expanded-cell {
    padding: 0 !important;
  }

  // 展开内容的div
  ::v-deep.inner-container {
    padding: 20px 100px 30px 55px !important;
    background: #f0f7ff !important;

    // 嵌套表格
    ::v-deep.inner-table {
      border: 1px solid rgb(222, 231, 245);
    }

    .inner-table td,
    .inner-table th,
    .is-leaf {
      border-left: none;
      border-right: none;
      border-top: none;
    }
  }
</style>
