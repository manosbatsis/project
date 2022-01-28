<template>
  <!-- 库位调整单列表页面 -->
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
              label="库位调整单号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.code"
                placeholder="请输入"
                clearable
              />
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
                filterable
                clearable
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
              label="客户名称："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                :data-list="getCustomerSelectBean('customer_list')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.customer_list"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="单据类型："
              prop="orderType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.orderType"
                placeholder="请选择"
                filterable
                clearable
                :data-list="
                  getSelectList('locationAdjustmentOrder_orderTypeList')
                "
              >
                <el-option
                  v-for="item in selectOpt.locationAdjustmentOrder_orderTypeList"
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
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('locationAdjustmentOrder_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.locationAdjustmentOrder_statusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="归属月份："
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->

    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          v-permission="'sale_location_adjustment_import'"
          type="primary"
          @click="importFile"
        >
          导入
        </el-button>
        <el-button
          v-permission="'sale_location_adjustment_export'"
          type="primary"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          v-permission="'sale_location_adjustment_refresh'"
          v-loading="refreshBtnLoading"
          type="primary"
          @click="refreshTableItem"
        >
          刷新
        </el-button>
        <el-button
          v-permission="'sale_location_adjustment_audit'"
          v-loading="auditBtnLoading"
          type="primary"
          @click="auditTableItem"
        >
          审核
        </el-button>
        <el-button
          v-permission="'sale_location_adjustment_delete'"
          v-loading="deleteBtnLoading"
          type="primary"
          @click="deleteTableItem"
        >
          删除
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
          v-permission="'sale_location_adjustment_log'"
          type="text"
          @click="showLogModal(row.code)"
        >
          日志
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->

    <!-- 日志 -->
    <LogList
      v-if="logModalState.visible.show"
      :visible.sync="logModalState.visible"
      :filterData="logModalState.filterData"
    />
    <!-- 日志 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getLocationAdjustmentList,
    refreshLocationAdjust,
    auditLocationAdjust,
    delLocationAdjust,
    getLocationAdjustmentCount,
    exportLocationAdjust,
    importLocationAdjust
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      /* 日志 */
      LogList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        searchProps: {
          code: '',
          buId: '',
          customerId: '',
          orderType: '',
          status: '',
          month: ''
        },
        tableColumn: [
          {
            label: '库位调整单号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '调整单据类型',
            prop: 'orderTypeLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '仓库',
            prop: 'depotName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '客户',
            prop: 'customerName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '平台',
            prop: 'platformName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '订单号',
            prop: 'orderCode',
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
            label: '调增类型',
            prop: 'inStockLocationTypeName',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '调减类型',
            prop: 'outStockLocationTypeName',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '调整数量',
            prop: 'adjustNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '归属月份',
            prop: 'month',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建人',
            prop: 'createName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '100',
            align: 'left'
          }
        ],
        logModalState: {
          filterData: {},
          visible: { show: false }
        },
        auditBtnLoading: false,
        deleteBtnLoading: false,
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
          const { data } = await getLocationAdjustmentList({
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
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportLocationAdjust, { ids })
          })
          return
        }
        try {
          const { data: count } = await getLocationAdjustmentCount({
            ...this.searchProps
          })
          if (count > 10000) {
            this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
            return
          }
          this.$exportFile(exportLocationAdjust, {
            ...this.searchProps
          })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      refreshTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return
        }
        const condition = this.tableChoseList.find(
          (item) => item.status !== '001'
        )
        if (condition) {
          this.$errorMsg('仅对状态为“待审核”状态可进行此操作')
          return
        }
        this.$showToast('确定刷新当前选择项？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            this.refreshBtnLoading = true
            await refreshLocationAdjust({ ids })
            this.$successMsg('刷新成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.refreshBtnLoading = false
          }
        })
      },
      auditTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return
        }
        const condition = this.tableChoseList.find(
          (item) => item.status !== '001'
        )
        if (condition) {
          this.$errorMsg('仅对状态为“待审核”状态可进行此操作')
          return
        }
        this.$showToast('确定审核当前选择项？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            this.auditBtnLoading = true
            await auditLocationAdjust({ ids })
            this.$successMsg('审核成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.auditBtnLoading = false
          }
        })
      },
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return
        }
        const condition = this.tableChoseList.find(
          (item) => item.status !== '001'
        )
        if (condition) {
          this.$errorMsg('仅对状态为“待审核”状态可进行此操作')
          return
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            this.deleteBtnLoading = true
            await delLocationAdjust({ ids })
            this.$successMsg('删除成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.deleteBtnLoading = false
          }
        })
      },
      importFile() {
        const templateId = 170
        const url = `/common/importfile?templateId=${templateId}&url=${importLocationAdjust}`
        this.linkTo(url)
      },
      showLogModal(relCode) {
        this.logModalState.filterData = { relCode, module: 23 }
        this.logModalState.visible.show = true
      },
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
