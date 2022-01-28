<template>
  <!-- 库存期初列表页面 -->
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
              label="库存类型："
              prop="type"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.type"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('initInventory_typeList')"
              >
                <el-option
                  v-for="item in selectOpt.initInventory_typeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="数据来源："
              prop="source"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.source"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('initInventory_sourceList')"
              >
                <el-option
                  v-for="item in selectOpt.initInventory_sourceList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="状态："
              prop="state"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.state"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('initInventory_stateList')"
              >
                <el-option
                  v-for="item in selectOpt.initInventory_stateList"
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
        <el-button type="primary" @click="importFile">批量导入</el-button>
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
  import { listInitInventory, importInitInventory } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          depotId: '',
          goodsName: '',
          goodsNo: '',
          barcode: '',
          type: '',
          source: '',
          state: '',
          buId: ''
        },
        inventoryTypeLabel: [
          { key: '1', value: '好品' },
          { key: '2', value: '坏品' }
        ],
        dataSourcesLabel: [
          { key: '1', value: '录入/导入' },
          { key: '2', value: 'OP' },
          { key: '3', value: 'OFC' }
        ],
        statusLabel: [
          { key: '1', value: '未确认' },
          { key: '2', value: '已确认' }
        ],
        // 表格列结构
        tableColumn: [
          {
            label: '仓库',
            prop: 'depotName',
            minWidth: '120',
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
            label: '商品货号',
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
            label: '商品条码',
            prop: 'barcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '批次号',
            prop: 'batchNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '生产日期',
            prop: 'productionDate',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '有效期至',
            prop: 'overdueDate',
            width: '110',
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
            label: '库存数量',
            prop: 'initNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '录入时间',
            prop: 'createDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'stateLabel',
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
            label: '标准条码',
            prop: 'commbarcode',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    activated() {
      this.getList()
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
          const { data } = await listInitInventory({
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
      // 导入
      importFile() {
        this.linkTo(
          '/stock/ImportStockOpening?templateId=' +
            105 +
            '&url=' +
            importInitInventory
        )
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
