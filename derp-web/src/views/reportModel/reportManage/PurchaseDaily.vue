<template>
  <!-- 购销采销日报页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="reset('searchForm')" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="品类："
              prop="productTypeId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.productTypeId"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="item in selectOpt.listMerchandiseCat"
                  :key="item.vlaue"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="品牌名称："
              prop="brandId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.brandId"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="item in selectOpt.listBrand"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="统计日期："
              prop="reportDate"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.reportDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期时间"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                clearable
                :data-list="getCustomerSelectBean('customerList')"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'gouXiaoPurchaseDaily_exportGouXiaoPurchaseDaily'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'gouXiaoPurchaseDaily_refreshDaily'"
          @click="refreshList"
        >
          刷新该日数据
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
      class="mr-t-20"
    >
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column
        prop="customerName"
        align="center"
        min-width="80"
        label="客户"
      />
      <el-table-column
        prop="productTypeName"
        align="center"
        min-width="80"
        label="品类"
      />
      <el-table-column
        prop="brandName"
        align="center"
        min-width="80"
        label="品牌"
      />
      <el-table-column
        prop="daySaleCount"
        align="center"
        min-width="80"
        label="当日销售量"
      />
      <el-table-column
        prop="daySaleAmount"
        align="center"
        min-width="80"
        label="当日销售额(RMB)"
      />
      <el-table-column
        prop="monSaleCount"
        align="center"
        min-width="80"
        label="当月销售量"
      />
      <el-table-column
        prop="monAvgCount"
        align="center"
        min-width="80"
        label="当月日均销售量"
      />
      <el-table-column
        prop="monSaleAmount"
        align="center"
        min-width="80"
        label="当月销售额(RMB)"
      />
      <el-table-column
        prop="yearSaleCount"
        align="center"
        min-width="80"
        label="年度销售量"
      />
      <el-table-column
        prop="yearSaleAmount"
        align="center"
        min-width="80"
        label="年度销售额(RMB)"
      />
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listGouXiaoPurchaseDaily,
    exportGouXiaoPurchaseDaily,
    refreshDaily,
    getOrderCount,
    PurchaseDailytoPage
  } from '@a/reportManage/index'
  import dayjs from 'dayjs'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          productTypeId: '',
          brandId: '',
          reportDate: dayjs().add(-1, 'DD').format('YYYY-MM-DD'),
          customerId: ''
        },
        // 表格数据
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 11
        }
      }
    },
    async mounted() {
      this.getList()
      const { data } = await PurchaseDailytoPage()
      this.$set(this.selectOpt, 'listMerchandiseCat', data.listMerchandiseCat)
      this.$set(this.selectOpt, 'listBrand', data.listBrand)
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listGouXiaoPurchaseDaily({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list
          this.tableData.total = total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      async exportList() {
        const { data } = await getOrderCount({ ...this.searchProps })
        if (data > 10000) {
          return this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
        }
        this.$exportFile(exportGouXiaoPurchaseDaily, { ...this.searchProps })
      },
      refreshList() {
        const { reportDate } = this.searchProps
        if (!reportDate) {
          return this.$alertWarning('请先选择统计日期')
        }
        this.$showToast('确定刷新该日期?', async () => {
          try {
            await refreshDaily({ reportDate })
            this.$successMsg('正在刷新,请稍后再查询')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      }
    }
  }
</script>
