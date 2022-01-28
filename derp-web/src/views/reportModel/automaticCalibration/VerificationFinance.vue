<template>
  <!-- 业财自核对页面 -->
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
              label="年月份："
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                type="month"
                value-format="yyyy-MM"
                placeholder="请选择年月份"
                :clearable="false"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="核对结果："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                filterable
                clearable
                :data-list="
                  getSelectList(
                    'businessFinanceAutomaticVerification_statusList'
                  )
                "
              >
                <el-option
                  v-for="item in selectOpt.businessFinanceAutomaticVerification_statusList"
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
        <el-button
          type="primary"
          v-permission="'businessFinance_auto_veri_export'"
          :loading="exportBtnLoading"
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
      @selection-change="selectionChange"
      @change="getList"
      class="mr-t-20"
    >
      <el-table-column
        prop="month"
        align="center"
        min-width="80"
        label="报表月份"
        show-overflow-tooltip
      />
      <el-table-column
        prop="buName"
        align="center"
        min-width="80"
        label="事业部"
        show-overflow-tooltip
      />
      <el-table-column
        prop="buInventorySummaryEndNum"
        align="center"
        min-width="80"
        label="事业部业务进销存"
        show-overflow-tooltip
      />
      <el-table-column
        prop="buFinanceSummaryEndNum"
        align="center"
        min-width="80"
        label="事业部财务进销存"
        show-overflow-tooltip
      />
      <el-table-column
        prop="createDate"
        align="center"
        min-width="80"
        label="最新更新时间"
        show-overflow-tooltip
      />
      <el-table-column
        prop="statusLabel"
        align="center"
        min-width="80"
        label="校验结果"
        show-overflow-tooltip
      />
      <el-table-column align="center" width="110" label="操作">
        <template slot-scope="{ row }">
          <el-button
            type="text"
            v-permission="'monthly_account_auto_veri_refresh'"
            @click="handleRefresh(row.buId, 'false')"
          >
            统计
          </el-button>
          <el-button
            type="text"
            v-permission="'monthly_account_auto_veri_refresh'"
            @click="handleRefresh(row.buId, 'true')"
          >
            刷新
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>

<script>
  import {
    listBusinessFinanceAutoVerification,
    refreshexportBusinessFinanceAutoVerification,
    exportBusinessFinanceAutoVerification
  } from '@a/reportManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: '',
          status: '',
          buId: ''
        }
      }
    },
    activated() {
      this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
      this.getList()
    },
    mounted() {
      this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listBusinessFinanceAutoVerification({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
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
        this.exportBtnLoading = true
        this.$exportFile(exportBusinessFinanceAutoVerification, {
          ...this.searchProps
        })
        this.exportBtnLoading = false
      },
      // 刷新、统计
      handleRefresh(buId, refresh) {
        const msg = {
          false: '统计',
          true: '刷新'
        }
        this.$showToast(`确认${msg[refresh]}吗？`, async () => {
          const { month } = this.searchProps
          try {
            await refreshexportBusinessFinanceAutoVerification({
              buId,
              month,
              refresh
            })
            this.$successMsg(`${msg[refresh]}成功`)
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
          this.getList(1)
        })
      }
    }
  }
</script>
