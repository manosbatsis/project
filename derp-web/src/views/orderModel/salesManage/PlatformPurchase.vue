<template>
  <!-- 平台采购单页面 -->
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
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                clearable
                filterable
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
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO号："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                type="textarea"
                :rows="1"
                v-model.trim="searchProps.poNo"
                placeholder="多个PO用英文逗号隔开"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PR号："
              prop="prNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                type="textarea"
                :rows="1"
                v-model.trim="searchProps.prNo"
                placeholder="多个PR英文逗号隔开"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="单据状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('platformPurchase_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.platformPurchase_statusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="平台状态："
              prop="platformStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.platformStatus"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getSelectList('platformPurchase_platformStatusList')
                "
              >
                <el-option
                  v-for="item in selectOpt.platformPurchase_platformStatusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="采购币种："
              prop="currency"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.currency"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getCurrencySelectBean('currencyList')"
              >
                <el-option
                  v-for="item in selectOpt.currencyList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item label="下单日期：">
              <el-date-picker
                v-model="date"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item label="入库日期：">
              <el-date-picker
                v-model="date1"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
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
          v-permission="'platform_purchase_toSales'"
          @click="showToSalesOrderModal"
        >
          转销售订单
        </el-button>
        <el-button
          type="primary"
          v-permission="'platform_purchase_export'"
          @click="exportList"
        >
          批量导出
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
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
        prop="poNo"
        align="center"
        label="PO号"
        min-width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="prNo"
        align="center"
        label="PR号"
        min-width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="customerName"
        align="center"
        label="客户"
        min-width="100"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="orderTime"
        align="center"
        label="下单时间"
        width="150"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="deliverDate"
        align="center"
        label="入库时间"
        width="150"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="poNo"
        align="center"
        show-overflow-tooltip
        min-width="100"
        label="金额"
      >
        <template slot-scope="{ row }">
          {{ `${row.currency}  ${row.amount}` }}
        </template>
      </el-table-column>
      <el-table-column
        prop="num"
        align="center"
        min-width="100"
        show-overflow-tooltip
        label="数量"
      >
        <template slot-scope="{ row }">
          {{ `sku个数：${row.skusNum}` }}
          <br />
          {{ `数量：${row.num}` }}
        </template>
      </el-table-column>
      <el-table-column
        prop="platformDepotName"
        align="center"
        label="客户仓库"
        show-overflow-tooltip
        min-width="120"
      ></el-table-column>
      <el-table-column
        prop="platformStatus"
        align="center"
        label="平台状态"
        width="100"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="statusLabel"
        align="center"
        label="单据状态"
        width="80"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="操作" align="left" width="60" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            type="text"
            v-permission="'platform_purchase_details'"
            @click="jumpToDetail(row.id)"
          >
            详情
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 转销售订单组件 -->
    <TransferSales
      v-if="transferSales.visible.show"
      :isVisible.sync="transferSales.visible"
      :data="transferSales.data"
      @comfirm="getList"
    />
    <!-- 转销售订单组件 end -->

    <!-- 详情弹窗 -->
    <PlatformPurchaseDetail
      v-if="purchaseOrderDetail.visible.show"
      :isVisible.sync="purchaseOrderDetail.visible"
      :data="purchaseOrderDetail.data"
    />
    <!-- 详情弹窗 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listPlatformPurchaseOrder,
    exportPlatformPurchaseOrderUrl,
    checkOrderToSales
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      /* 转销售订单 */
      TransferSales: () => import('./components/TransferSales'),
      /* 详情 */
      PlatformPurchaseDetail: () =>
        import('./components/PlatformPurchaseDetail')
    },
    data() {
      return {
        /* 下单日期 */
        date: [],
        /* 入库日期 */
        date1: [],
        /* 查询数据 */
        searchProps: {
          customerId: '',
          poNo: '',
          prNo: '',
          status: '',
          currency: '',
          platformStatus: ''
        },
        /* 详情状态 */
        purchaseOrderDetail: {
          visible: { show: false },
          data: {}
        },
        /* 转销售订单状态 */
        transferSales: {
          visible: { show: false },
          data: {}
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      /* 获取表格数据 */
      async getList(pageNum) {
        /* 下单日期 */
        this.searchProps.orderTimeStartDate =
          this.date && this.date.length ? this.date[0] : ''
        this.searchProps.orderTimeEndDate =
          this.date && this.date.length ? this.date[1] : ''
        /* 入库日期 */
        this.searchProps.deliverStartDate =
          this.date1 && this.date1.length ? this.date1[0] : ''
        this.searchProps.deliverEndDate =
          this.date1 && this.date1.length ? this.date1[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listPlatformPurchaseOrder({
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
      /* 转销售订单 */
      async showToSalesOrderModal() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        const { currency, customerId } = this.tableChoseList[0]
        try {
          /* 校验是否能转销售订单 */
          const {
            data: { code, message }
          } = await checkOrderToSales({ ids })
          if (code === '01') {
            const isComfirm = await this.$showToast(message)
            if (!isComfirm) {
              return
            }
          }
          this.linkTo({
            path: '/sales/platformpurchaseTransferSales',
            query: {
              currency,
              customerId,
              ids
            }
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 显示详情弹窗 */
      jumpToDetail(id) {
        this.purchaseOrderDetail.visible.show = true
        this.purchaseOrderDetail.data = { id }
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          return this.$errorMsg('暂无数据可导出')
        }
        try {
          if (this.tableChoseList.length) {
            this.$showToast('确定导出勾选数据？', async () => {
              const ids = this.tableChoseList.map((item) => item.id).toString()
              this.$exportFile(exportPlatformPurchaseOrderUrl, { ids })
            })
          } else {
            this.$exportFile(exportPlatformPurchaseOrderUrl, {
              ...this.searchProps
            })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      /* 重置搜索表单 */
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.date = []
          this.date1 = []
          this.getList(1)
        })
      }
    }
  }
</script>
