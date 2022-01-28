<template>
  <!-- 效期预警页面 -->
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
              ></el-input>
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
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="效期："
              prop="validityType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="validityTypesList"
                placeholder="请选择"
                filterable
                clearable
                multiple
                collapse-tags
                style="width: 230px"
              >
                <el-option
                  v-for="item in validityLabel"
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
          v-permission="'inventoryWarning_exportInventoryWarning'"
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
      <template slot="surplusDate" slot-scope="{ row }">
        {{ validityRange(row) }}
      </template>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listInventoryWarning, exportInventoryWarning } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          merchantName: '',
          depotId: '',
          goodsNo: '',
          validityType: ''
        },
        validityTypesList: [],
        // 效期下拉列表数据
        validityLabel: [
          { key: '1', value: '0<x≤1/10' },
          { key: '2', value: '1/10<x≤1/5' },
          { key: '3', value: '1/5<x≤1/3' },
          { key: '4', value: '1/3<x≤1/2' },
          { key: '5', value: '1/2<x≤2/3' },
          { key: '6', value: '2/3以上' },
          { key: '7', value: '过期' }
        ],
        // 表格列结构
        tableColumn: [
          {
            label: '公司名称',
            prop: 'merchantName',
            minWidth: '120',
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
            label: '生产日期',
            prop: 'productionDate',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '失效日期',
            prop: 'overdueDate',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '批次号',
            prop: 'batchNo',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '总数量',
            prop: 'surplusNum',
            width: '90',
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
            label: '剩余失效(天)',
            prop: 'surplusDays',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '总效期(天)',
            prop: 'totalDays',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '剩余失效',
            prop: 'surplusDate',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '效期区间',
            slotTo: 'surplusDate',
            minWidth: '100',
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
          this.searchProps.validityTypes =
            this.validityTypesList && this.validityTypesList.length
              ? this.validityTypesList.toString()
              : ''
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listInventoryWarning({
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
        this.$exportFile(exportInventoryWarning, { ...this.searchProps })
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.validityTypesList = []
          this.getList(1)
        })
      },
      // 效期区间
      validityRange(row) {
        const surplusDays = row.surplusDays
        const totalDays = row.totalDays
        if (surplusDays / totalDays > 0 && surplusDays / totalDays <= 1 / 10) {
          return '0＜X≤1/10'
        } else if (
          surplusDays / totalDays > 1 / 10 &&
          surplusDays / totalDays <= 1 / 5
        ) {
          return '1/10＜X≤1/5'
        } else if (
          surplusDays / totalDays > 1 / 5 &&
          surplusDays / totalDays <= 1 / 3
        ) {
          return '1/5＜X≤1/3'
        } else if (
          surplusDays / totalDays > 1 / 3 &&
          surplusDays / totalDays <= 1 / 2
        ) {
          return '1/3＜X≤1/2'
        } else if (
          surplusDays / totalDays > 1 / 2 &&
          surplusDays / totalDays <= 2 / 3
        ) {
          return '1/2＜X≤2/3'
        } else if (surplusDays / totalDays > 2 / 3) {
          return '2/3以上'
        } else if (surplusDays / totalDays <= 0) {
          return '过期'
        }
        return ''
      }
    }
  }
</script>
