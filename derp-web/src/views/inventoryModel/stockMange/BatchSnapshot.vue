<template>
  <!-- 库存批次快照列表 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @search="getList(1)" @reset="reset('searchForm')">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="公司："
              prop="merchantName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.merchantName"
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
              label="批次："
              prop="batchNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.batchNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="快照日期："
              prop="snapshotDate"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.snapshotDate"
                type="date"
                placeholder="请选择"
                value-format="yyyy-MM-dd"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品条码："
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'inventoryBatchSnapshot_exportBatchSnapshot'"
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
    ></JFX-table>
    <!-- 表格 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listInventoryBatchSnapshot,
    exportInventoryBatchSnapshotModelMap
  } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          merchantName: '',
          depotId: '',
          goodsNo: '',
          batchNo: '',
          snapshotDate: '',
          barcode: '',
          brandId: ''
        },
        // 表格列结构
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '仓库名称',
            prop: 'depotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品条码',
            prop: 'barcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '品牌名称',
            prop: 'brandName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '批次号',
            prop: 'batchNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '生产日期',
            prop: 'productionDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '有效期至',
            prop: 'overdueDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '库存余量',
            prop: 'surplusNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '仓库现余量',
            prop: 'availableNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '库存类型',
            prop: 'typeLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '是否过期',
            prop: 'isExpireLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '理货单位',
            prop: 'unitLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '托盘号',
            prop: 'lpn',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '创建时间',
            prop: 'createDate',
            width: '150',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listInventoryBatchSnapshot({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.$exportFile(exportInventoryBatchSnapshotModelMap, {
          ...this.searchProps
        })
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
