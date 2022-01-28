<template>
  <!-- 云集采销日报页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm', getList)"
      @search="getList(1)"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品名称："
              prop="goodsNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.goodsNo" clearable></el-input>
            </el-form-item>
          </el-col>
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
                  v-for="item in selectOpt.merchantList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="统计日期："
              prop="sDate"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.sDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期时间"
              ></el-date-picker>
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
                  v-for="item in selectOpt.brandList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
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
          v-permission="'yunJiPurchaseDaily_exportYunJiPurchaseDaily'"
          type="primary"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          v-permission="'yunJiPurchaseDaily_mqUpdateByDay'"
          type="primary"
          @click="refreshList"
        >
          刷新
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
        prop="goodsNo"
        align="center"
        min-width="80"
        label="商品货号"
      />
      <el-table-column
        prop="barcode"
        align="center"
        min-width="80"
        label="条形码"
      />
      <el-table-column
        prop="goodsName"
        align="center"
        min-width="80"
        label="商品名称"
      />
      <el-table-column
        prop="brandName"
        align="center"
        min-width="80"
        label="商品品牌"
      />
      <el-table-column
        prop="productTypeName"
        align="center"
        min-width="80"
        label="二级分类"
      />
      <el-table-column
        prop="daySaleNum"
        align="center"
        min-width="80"
        label="当日销量"
      />
      <el-table-column
        prop="monthSaleNum"
        align="center"
        min-width="80"
        label="当月销量"
      />
      <el-table-column
        prop="yearSaleNum"
        align="center"
        min-width="80"
        label="年度销量"
      />
      <el-table-column
        prop="dayInventoryNum"
        align="center"
        min-width="80"
        label="保税仓当日库存"
      />
      <el-table-column
        prop="dayReturnBinNum"
        align="center"
        min-width="80"
        label="退货仓当日库存"
      />
      <el-table-column
        prop="snapshotDate"
        align="center"
        min-width="80"
        label="快照时间"
      />
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  import {
    listMerchandiseCatSelectBean,
    listBrandSelectBean,
    listYunJiDailySalesReport,
    getDailySalesReportCount,
    getDailySalesReportCountUrl,
    freshDailySalesByDay
  } from '@a/reportManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          goodsNo: '',
          productTypeId: '',
          sDate: dayjs().subtract(1, 'day').format('YYYY-MM-DD'),
          brandId: ''
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
      // 获取品牌 品类下拉
      const [{ data: merchantList }, { data: brandList }] = await Promise.all([
        listMerchandiseCatSelectBean(),
        listBrandSelectBean()
      ])
      this.$set(this.selectOpt, 'merchantList', merchantList)
      this.$set(
        this.selectOpt,
        'brandList',
        brandList.map((item) => {
          return { label: item.name, value: item.id }
        })
      )
      console.log(merchantList, brandList)
      this.getList()
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const params = { ...this.searchProps }
          const {
            data: { list, total }
          } = await listYunJiDailySalesReport({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...params
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
        const params = { ...this.searchProps }
        for (const key in params) {
          if (params[key] instanceof Array) {
            params[key] = params[key].join(',')
          }
        }
        const { data } = await getDailySalesReportCount({ ...this.params })
        if (data > 10000) {
          return this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
        }
        this.$exportFile(getDailySalesReportCountUrl, {
          ...this.searchProps
        })
      },
      refreshList() {
        const { sDate } = this.searchProps
        if (!sDate) {
          return this.$alertWarning('请先选择统计日期')
        }
        this.$showToast('确定刷新该统计日期?', async () => {
          try {
            await freshDailySalesByDay({ sDate })
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      }
    }
  }
</script>
