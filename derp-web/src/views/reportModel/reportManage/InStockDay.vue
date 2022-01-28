<template>
  <!-- 财务进销存关账页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm')"
      @search="getList(1)"
      :showOpenBtn="false"
    >
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
              label="报表月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                type="month"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                placeholder="选择日期时间"
              ></el-date-picker>
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
          v-permission="'inWarehouseDays_flashInWarehouseDays'"
          type="primary"
          @click="refreshList"
        >
          刷新数据
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
      <el-table-column type="index" align="center" width="55" />
      <el-table-column
        prop="buName"
        align="center"
        min-width="80"
        label="事业部"
      />
      <el-table-column
        prop="totalNum"
        align="center"
        min-width="80"
        label="库存总量"
      />
      <el-table-column
        prop="totalAmount"
        align="center"
        min-width="80"
        label="库存总金额"
      />
      <el-table-column
        prop="currency"
        align="center"
        min-width="80"
        label="统计币种"
      />
      <el-table-column
        prop="statisticsDate"
        align="center"
        min-width="80"
        label="统计截止日期"
      />
      <el-table-column
        prop="month"
        align="center"
        min-width="80"
        label="报表月份"
      />
      <el-table-column
        prop="createDate"
        align="center"
        min-width="120"
        label="刷新时间"
      />
      <el-table-column align="center" width="100" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            type="text"
            @click="
              linkTo(
                `/report/InStockDayDetail?buId=${row.buId}&month=${row.month}`
              )
            "
          >
            详情
          </el-button>
          <el-button
            type="text"
            v-permission="'inWarehouseDays_exportInWarehouseDetails'"
            @click="exportDetail(row)"
          >
            导出
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listInWarehouseDays,
    flashInWarehouseDays,
    exportInWarehouseDetails
  } from '@a/reportManage/index'
  import dayjs from 'dayjs'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          buId: '',
          month: dayjs().format('YYYY-MM')
        },
        // 表格数据
        tableData: {
          list: [{}],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 11
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.loading = true
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          const {
            data: { list, total }
          } = await listInWarehouseDays({
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
      refreshList() {
        const { month } = this.searchProps
        this.$showToast(`确定刷新${month ? '该月份' : ''}数据？`, async () => {
          try {
            await flashInWarehouseDays({ month })
            this.$successMsg('正在刷新，稍后点击【查询】，查询结果')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      async exportDetail({ month, buId }) {
        const { data } = await exportInWarehouseDetails({ month, buId })
        if (data.code === '00') {
          this.$successMsg('请在任务列表中查看进度')
        }
      },
      /**
       * 根据年月获取当前月的第一天和最后一天
       * @param timeStr 传入的时间 yyyy-MM 格式
       */
      getMonthFirstLastDay(timeStr) {
        const year = timeStr.split('-')[0]
        const month = timeStr.split('-')[1]
        const firstDay = new Date(year, month - 1, 1)
        const lastDay = new Date(
          firstDay.getFullYear(),
          firstDay.getMonth() + 1,
          0
        )
        return [
          this.$formatDate(firstDay, 'yyyy-MM-dd'),
          this.$formatDate(lastDay, 'yyyy-MM-dd')
        ]
      }
    }
  }
</script>
