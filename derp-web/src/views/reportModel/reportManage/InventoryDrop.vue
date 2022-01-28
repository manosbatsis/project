<template>
  <!-- 存货跌价准备页面 -->
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
              label="报表月份："
              prop="reportMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.reportMonth"
                type="month"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                placeholder="选择日期时间"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.buIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
                :data-list="getBUSelectBean('buIdList')"
              >
                <el-option
                  v-for="item in selectOpt.buIdList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="仓库："
              prop="depotIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.depotIds"
                placeholder="请选择"
                clearable
                multiple
                filterable
                collapse-tags
                :data-list="
                  getSelectBeanByMerchantRel('depotList', {
                    isInvertoryFallingPrice: 1
                  })
                "
              >
                <el-option
                  v-for="item in selectOpt.depotList"
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
              label="计提比例："
              prop="depreciationReserveProportion"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.depreciationReserveProportion"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="item in depreciationList"
                  :key="item.vlaue"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="效期："
              prop="effectiveIntervals"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.effectiveIntervals"
                placeholder="请选择"
                clearable
                multiple
                filterable
                collapse-tags
                :data-list="getSelectList('fallingPrice_effectiveIntervalList')"
              >
                <el-option
                  v-for="item in selectOpt.fallingPrice_effectiveIntervalList"
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
          v-permission="'inventoryFallingPriceReserves_exportReserves'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'inventoryFallingPriceReserves_flashReserves'"
          @click="refreshList"
        >
          刷新报表
        </el-button>
        <span v-if="createDate" style="color: red; margin-left: 10px">
          生成时间：{{ createDate }}
        </span>
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
        prop="merchantName"
        align="center"
        min-width="80"
        label="公司名称"
      />
      <el-table-column
        prop="buName"
        align="center"
        min-width="80"
        label="事业部"
      />
      <el-table-column
        prop="depotName"
        align="center"
        min-width="80"
        label="仓库名称"
      />
      <el-table-column
        prop="reportMonth"
        align="center"
        min-width="80"
        label="报表月份"
      />
      <el-table-column
        prop="goodsNo"
        align="center"
        min-width="80"
        label="商品货号"
      />
      <el-table-column
        prop="goodsName"
        align="center"
        min-width="80"
        label="商品名称"
      />
      <el-table-column
        prop="productionDate"
        align="center"
        min-width="80"
        label="生产日期"
      />
      <el-table-column
        prop="overdueDate"
        align="center"
        min-width="120"
        label="失效日期"
      />
      <el-table-column
        prop="batchNo"
        align="center"
        min-width="80"
        label="批次号"
      />
      <el-table-column
        prop="inverntoryStatusLabel"
        align="center"
        min-width="80"
        label="库存状态"
      />
      <el-table-column
        prop="surplusNum"
        align="center"
        min-width="80"
        label="总库存"
      />
      <el-table-column
        prop="inverntoryTypeLabel"
        align="center"
        min-width="80"
        label="库存类型"
      />
      <el-table-column
        prop="surplusDays"
        align="center"
        min-width="80"
        label="剩余失效(天)"
      />
      <el-table-column
        prop="totalDays"
        align="center"
        min-width="80"
        label="总效期(天)"
      />
      <el-table-column
        prop="surplusProportion"
        align="center"
        min-width="80"
        label="剩余效期占比"
      >
        <template slot-scope="scope">
          {{ formatRate(scope.row.surplusProportion) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="effectiveIntervalLabel"
        align="center"
        min-width="80"
        label="效期区间"
      />
      <el-table-column
        prop="depreciationReserveProportion"
        align="center"
        min-width="80"
        label="跌价准备比例"
      >
        <template slot-scope="scope">
          {{ formatRate(scope.row.depreciationReserveProportion) }}
        </template>
      </el-table-column>

      <el-table-column
        prop="settlementPrice"
        align="center"
        min-width="80"
        label="单价"
      />
      <el-table-column
        prop="totalProvision"
        align="center"
        min-width="80"
        label="计提总额"
      />
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  import {
    listInventoryFallingPriceReserves,
    exportInventoryFallingPriceReserves,
    getCount,
    flashInventoryFallingPriceReserves
  } from '@a/reportManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        depreciationList: [
          { value: 1, label: '100%' },
          { value: 0.7, label: '70%' },
          { value: 0.3, label: '30%' },
          { value: 0, label: '0%' }
        ],
        // 查询数据
        searchProps: {
          buIds: '',
          reportMonth: dayjs().format('YYYY-MM'),
          depotIds: '',
          goodsNo: '',
          depreciationReserveProportion: '',
          effectiveIntervals: ''
        },
        createDate: '' // 刷新时间
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      formatRate(data) {
        if (!['0', null, undefined, ''].includes(data)) {
          data = parseFloat(data) * 100
          data = data.toFixed(2)

          return (data += '%')
        } else {
          return ''
        }
      },
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const params = { ...this.searchProps }
          for (const key in params) {
            if (params[key] instanceof Array) {
              params[key] = params[key].join(',')
            }
          }
          const {
            data: { list, total }
          } = await listInventoryFallingPriceReserves({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...params
          })
          this.tableData.list = list
          this.tableData.total = total
          if (this.tableData.list.length) {
            this.createDate = this.tableData.list[0].createDate || ''
          }
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
        const { data } = await getCount({ ...this.params })
        if (data > 10000) {
          return this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
        }
        this.$exportFile(exportInventoryFallingPriceReserves, {
          ...this.searchProps
        })
      },
      refreshList() {
        const { reportMonth } = this.searchProps
        if (!reportMonth) {
          return this.$alertWarning('请先选择报表月份')
        }
        this.$showToast('确定刷新该月份?', async () => {
          try {
            await flashInventoryFallingPriceReserves({
              reportMonth,
              buIds: this.searchProps.buIds.toString()
            })
            this.$successMsg('正在刷新,请稍后再查询')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
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

<style lang="scss" scoped></style>
