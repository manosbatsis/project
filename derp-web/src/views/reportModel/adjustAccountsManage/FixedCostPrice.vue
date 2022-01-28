<template>
  <!-- 销售订单列表页面 -->
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
              label="条形码："
              prop="barcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.barcode"
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
                v-model.trim="searchProps.goodsName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="生效年月：" prop="effectiveDate">
              <el-date-picker
                v-model="searchProps.effectiveDate"
                value-format="yyyy-MM"
                type="month"
                placeholder="选择日期时间"
                clearable
              />
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
                :data-list="getSelectList('fixedCostPrice_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.fixedCostPrice_statusList"
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
          v-permission="'fixed_cost_price_import'"
          type="primary"
          @click="importFile"
        >
          导入
        </el-button>
        <el-button
          v-permission="'fixed_cost_price_audit'"
          v-loading="auditBtnLoading"
          type="primary"
          @click="auditTableItem"
        >
          审核
        </el-button>
        <el-button
          v-permission="'fixed_cost_price_delete'"
          v-loading="deleteBtnLoading"
          type="primary"
          @click="deleteTableItem"
        >
          删除
        </el-button>
        <el-button
          v-permission="'fixed_cost_price_export'"
          type="primary"
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
      <template slot="action" slot-scope="{ row }">
        <el-button
          v-permission="'fixed_cost_price_log'"
          type="text"
          @click="showLogModal(row.id)"
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
      type="system"
    />
    <!-- 日志 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getFixedCostPriceList,
    deleteFixedCostPrice,
    auditFixedCostPrice,
    importFixedCostPrice,
    exportFixedCostPrice
  } from '@a/adjustAccountsManage'
  export default {
    mixins: [commomMix],
    components: {
      /* 日志 */
      LogList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        searchProps: {
          buId: '',
          barcode: '',
          goodsName: '',
          effectiveDate: '',
          status: ''
        },
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '标准品牌',
            prop: 'brandParentName',
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
            label: '固定成本价',
            prop: 'fixedCost',
            minWidth: '150',
            align: 'center',
            hide: true,
            formatter: (row) => `${row.currency} ${row.fixedCost}`
          },
          {
            label: '生效年月',
            prop: 'effectiveDate',
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
            align: 'center',
            hide: true
          }
        ],
        logModalState: {
          filterData: {},
          visible: { show: false }
        },
        auditBtnLoading: false,
        deleteBtnLoading: false
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
          const { data } = await getFixedCostPriceList({
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
          this.$showToast('确定导出勾选数据？', () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportFixedCostPrice, { ids })
          })
          return
        }
        this.$exportFile(exportFixedCostPrice, { ...this.searchProps })
      },
      auditTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return
        }
        const condition = this.tableChoseList.find(
          (item) => item.status !== '0'
        )
        if (condition) {
          this.$errorMsg('仅对状态为“待审核”状态可进行此操作')
          return
        }
        this.$showToast('确定审核当前选择项？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            this.auditBtnLoading = true
            await auditFixedCostPrice({ ids })
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
          (item) => item.status !== '0'
        )
        if (condition) {
          this.$errorMsg('仅对状态为“待审核”状态可进行此操作')
          return
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            this.deleteBtnLoading = true
            await deleteFixedCostPrice({ ids })
            this.$successMsg('删除成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.deleteBtnLoading = false
          }
        })
      },
      showLogModal(id) {
        this.logModalState.filterData = { id, module: 8 }
        this.logModalState.visible.show = true
      },
      importFile() {
        const templateId = 171
        const url = `/common/importfile?templateId=${templateId}&url=${importFixedCostPrice}`
        this.linkTo(url)
      },
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
