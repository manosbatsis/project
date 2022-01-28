<template>
  <!-- 商品收发明细页面 -->
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
              label="批次号："
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
              label="来源单据号："
              prop="orderNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.orderNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="业务单据号："
              prop="businessNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.businessNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事物类型："
              prop="thingStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.thingStatus"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('inventory_thingStatusList')"
              >
                <el-option
                  v-for="item in selectOpt.inventory_thingStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
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
              label="标准条码："
              prop="commbarcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.commbarcode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="订单范围："
              prop="orderTimeRange"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.orderTimeRange"
                placeholder="请选择"
                filterable
                :clearable="false"
              >
                <el-option
                  v-for="item in orderScopeLabel"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="操作类型："
              prop="operateType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.operateType"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('inventory_operateTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.inventory_operateTypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item
              label="归属时间："
              prop="date"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="date"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                clearable
                :default-time="['00:00:00', '23:59:59']"
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
          v-permission="'inventoryDetails_exportInventoryDetails'"
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
  import { listInventoryDetails, exportInventoryDetails } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          merchantName: '',
          depotId: '',
          buId: '',
          goodsNo: '',
          batchNo: '',
          orderNo: '',
          businessNo: '',
          thingStatus: '',
          barcode: '',
          commbarcode: '',
          orderTimeRange: '1',
          operateType: ''
        },
        // 归属时间
        date: [],
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
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事物类型',
            prop: 'thingStatusLabel',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '来源单据号',
            prop: 'orderNo',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '业务单据号',
            prop: 'businessNo',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '变更日期',
            prop: 'divergenceDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '条码',
            prop: 'barcode',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '标准条码',
            prop: 'commbarcode',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '110',
            align: 'center',
            hide: true
          },
          {
            label: '批次号',
            prop: 'batchNo',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '生产日期',
            prop: 'productionDate',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '有效期至',
            prop: 'overdueDate',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '数量',
            prop: 'num',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '单位',
            prop: 'unitLabel',
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
            label: '库位货号',
            prop: 'locationGoodsNo',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '操作类型',
            prop: 'operateTypeLabel',
            width: '90',
            align: 'center',
            hide: true
          }
        ],
        // 订单范围下拉列表数据
        orderScopeLabel: [
          { value: '1', label: '近12个月数据' },
          { value: '2', label: '12个月以前数据' }
        ]
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        // 归属时间
        this.searchProps.startTime =
          this.date && this.date.length ? this.date[0] : ''
        this.searchProps.endTime =
          this.date && this.date.length ? this.date[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listInventoryDetails({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (err) {}
        this.tableData.loading = false
      },
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportInventoryDetails, {
              orderTimeRange: this.searchProps.orderTimeRange,
              ids
            })
          })
        } else {
          this.$exportFile(exportInventoryDetails, { ...this.searchProps })
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.date = []
          this.getList(1)
        })
      }
    }
  }
</script>
