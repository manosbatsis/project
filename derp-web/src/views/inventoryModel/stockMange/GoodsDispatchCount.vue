<template>
  <!-- 商品收发汇总页面 -->
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
              label="结算月份："
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.currentMonth"
                type="month"
                placeholder="请选择"
                style="width: 203px"
                :clearable="false"
                value-format="yyyy-MM"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button type="primary" @click="exportList('确定导出筛选的数据？')">
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
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          style="padding-left: 6px"
          @click="jumpToDetail(row.goodsNo, row.depotId)"
        >
          详情
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listInventorySummary, exportInventorySummary } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          merchantName: '',
          depotId: '',
          currentMonth: '',
          goodsNo: ''
        },
        // 表格列结构
        tableColumn: [
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
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '本月期初库存',
            prop: 'openingInventoryNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本月累计入库',
            prop: 'inDepotTotal',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本月累计出库',
            prop: 'outDepotTotal',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '库存余额',
            prop: 'surplusNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '销售在途量',
            prop: 'saleOnwayNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '电商在途量',
            prop: 'eOnwayNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '调拨出库在途',
            prop: 'transferOutNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '可用库存量',
            prop: 'availableNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '单位',
            prop: 'unitLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '库存周转天数(按120天订单算)',
            prop: 'turnoverDays',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '在库库存断销时间',
            prop: 'noSaleDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '80',
            align: 'left',
            fixed: 'right'
          }
        ]
      }
    },
    mounted() {
      this.searchProps.currentMonth = this.$formatDate(new Date(), 'yyyy-MM')
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listInventorySummary({
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
        this.$exportFile(exportInventorySummary, { ...this.searchProps })
      },
      // 跳转到商品库存明细
      jumpToDetail(goodsNo, depotId) {
        this.linkTo({
          name: 'GoodsStock',
          params: { goodsNo, depotId }
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
