<template>
  <!-- 财务进销存关账页面 -->
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
              label="归属月："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <template slot="label">
                <el-select v-model="searchProps.timeType">
                  <el-option value="m" label="月份">月份</el-option>
                  <el-option value="y" label="年份">年份</el-option>
                </el-select>
              </template>
              <el-date-picker
                v-if="searchProps.timeType === 'm'"
                v-model="searchProps.month"
                :clearable="false"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                type="month"
                placeholder="选择日期时间"
              ></el-date-picker>
              <el-date-picker
                v-if="searchProps.timeType === 'y'"
                v-model="searchProps.year"
                :clearable="false"
                :format="'yyyy'"
                :value-format="'yyyy'"
                type="year"
                placeholder="选择日期时间"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="部门："
              prop="departmentIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.departmentIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
                @change="departChange"
              >
                <el-option
                  v-for="item in selectOpt.departList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="母品牌："
              prop="parentBrandIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.parentBrandIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
                :data-list="getBrandList('brandList')"
              >
                <el-option
                  v-for="item in selectOpt.brandList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="项目组："
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
              >
                <el-option
                  v-for="item in selectOpt.buList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-10">
      <el-col :span="21">
        <el-button
          type="primary"
          @click="exportTalbe"
          v-permission="'salesReachSummaryExport'"
        >
          导出
        </el-button>
      </el-col>
      <el-col
        :span="3"
        style="
          border-left: 2px solid #0000ff;
          padding-left: 8px;
          position: relative;
          top: 5px;
          font-size: 12px;
          color: #666666;
        "
      >
        币种：HKD
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableColumn="tableColumn"
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      :showPagination="false"
      :span-method="tableSpanMethod"
      @change="getList"
      :isStickyOpen="true"
      class="mr-t-20"
    >
      <template slot="no" slot-scope="{ row }" v-if="!tableData.loading">
        {{ row.departmentSumFlag ? row.departmentSumTitle : row.no }}
      </template>
      <template slot="month" slot-scope="{ row }">
        目标：{{ row.monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveAmount | formatThousands }}
        <br />
        完成度: {{ row.monthCompletionPercentage | formatThousandsPercentage }}
      </template>
      <template slot="year" slot-scope="{ row }">
        目标：{{ row.yearTargetAmount | formatThousands }}
        <br />
        达成：{{ row.yearAchieveAmount | formatThousands }}
        <br />
        完成度: {{ row.yearCompletionPercentage | formatThousandsPercentage }}
      </template>
      <template slot="month1" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[0].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[0].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[0].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month2" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[1].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[1].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[1].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month3" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[2].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[2].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[2].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month4" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[3].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[3].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[3].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month5" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[4].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[4].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[4].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month6" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[5].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[5].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[5].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month7" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[6].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[6].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[6].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month8" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[7].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[7].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[7].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month9" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[8].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[8].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[8].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month10" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[9].monthTargetAmount | formatThousands }}
        <br />
        达成：{{ row.monthAchieveList[9].monthAchieveAmount | formatThousands }}
        <br />
        完成度:
        {{
          row.monthAchieveList[9].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month11" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[10].monthTargetAmount | formatThousands }}
        <br />
        达成：{{
          row.monthAchieveList[10].monthAchieveAmount | formatThousands
        }}
        <br />
        完成度:
        {{
          row.monthAchieveList[10].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
      <template slot="month12" slot-scope="{ row }">
        目标：{{ row.monthAchieveList[11].monthTargetAmount | formatThousands }}
        <br />
        达成：{{
          row.monthAchieveList[11].monthAchieveAmount | formatThousands
        }}
        <br />
        完成度:
        {{
          row.monthAchieveList[11].monthCompletionPercentage
            | formatThousandsPercentage
        }}
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  import {
    getDepartmentSalesAchieveStatistic,
    exportDepartmentSalesAchieveStatistic,
    getDepartmentSelectList,
    getBuList
  } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    filters: {
      formatThousands(number) {
        if ([null, undefined, ''].includes(number)) {
          return ''
        }
        return Number(number)
          .toFixed(2)
          .replace(/(\d)(?=(\d{3})+\.)/g, '$1,')
      },
      formatThousandsPercentage(number) {
        if ([null, undefined, ''].includes(number) || number < 0) {
          return '-'
        }
        return (
          Number(number)
            .toFixed(2)
            .replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + '%'
        )
      }
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          timeType: 'm',
          month: dayjs().format('YYYY-MM'),
          year: dayjs().format('YYYY'),
          departmentIds: '',
          parentBrandIds: '',
          buIds: ''
        },
        // 表格数据
        tableData: {
          list: [],
          loading: false
        },
        // 表头字段
        tableColumn: [],
        baseColumn: [
          {
            label: '序号',
            slotTo: 'no',
            width: '50',
            align: 'center'
          },
          {
            label: '部门',
            prop: 'departmentName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '母品牌',
            prop: 'parentBrandName',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    async mounted() {
      this.tableData.loading = true
      const [{ data: departResList }, { data: buList }] = await Promise.all([
        getDepartmentSelectList(),
        getBuList()
      ])
      // 部门
      this.$set(this.selectOpt, 'departList', departResList)
      this.searchProps.departmentIds = departResList.map((item) => item.value)
      //  项目组
      this.$set(this.selectOpt, 'buList', buList)
      this.searchProps.buIds = buList.map((item) => item.id)

      this.getList()
    },
    methods: {
      dayjs,
      // 拉取表格数据
      async getList() {
        // loading
        this.tableData.loading = true
        // 查询参数调整
        const params = {
          ...this.searchProps
        }
        for (const key in params) {
          if (params[key] instanceof Array) {
            params[key] = params[key].join(',')
          }
        }
        const timeType = params.timeType
        if (timeType === 'm') {
          delete params.year
        } else {
          delete params.month
        }
        delete params.timeType
        console.log(params)
        // 回参
        let { data } = await getDepartmentSalesAchieveStatistic(params)
        data = data || []
        this.tableColumn = JSON.parse(JSON.stringify(this.baseColumn))
        if (timeType === 'm') {
          data = data.map((item) => {
            const itemMonthAchieve = item.monthAchieveList[0]
            return {
              ...item,
              ...itemMonthAchieve
            }
          })
          this.tableColumn.push({
            label:
              Number(dayjs(this.searchProps.month).format('MM')) +
              '月销售金额（万）',
            slotTo: 'month',
            minWidth: '120',
            align: 'right',
            hide: true
          })
          this.tableColumn.push({
            label:
              dayjs(this.searchProps.month).format('YYYY') + '年销售金额（万）',
            slotTo: 'year',
            minWidth: '120',
            align: 'right',
            hide: true
          })
        } else if (timeType === 'y') {
          let curMonth = Number(dayjs().format('MM'))
          if (
            dayjs().format('YYYY') !==
            dayjs(this.searchProps.year).format('YYYY')
          ) {
            curMonth = 12
          }
          for (let start = 1; start <= curMonth; start++) {
            this.tableColumn.push({
              label: start + '月销售金额（万）',
              slotTo: 'month' + start,
              minWidth: '120',
              align: 'right',
              hide: true
            })
          }
        }
        this.tableData.list = data
        this.tableData.loading = false
      },
      //  单元格合并
      tableSpanMethod({ row, column, rowIndex, columnIndex }) {
        if (columnIndex === 0) {
          if (row.departmentSumFlag) {
            return [1, 3]
          }
        } else if ([1, 2].includes(columnIndex)) {
          if (row.departmentSumFlag) {
            return [0, 0]
          }
        }
      },
      // 导出
      exportTalbe() {
        if (this.tableData.list < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.getList()
        const params = {
          ...this.searchProps
        }
        for (const key in params) {
          if (params[key] instanceof Array) {
            params[key] = params[key].join(',')
          }
        }
        const timeType = params.timeType
        if (timeType === 'm') {
          delete params.year
        } else {
          delete params.month
        }
        delete params.timeType
        console.log(params)
        this.$exportFile(exportDepartmentSalesAchieveStatistic, { ...params })
      },
      // 部门改变
      async departChange() {
        const { departmentIds } = this.searchProps
        console.log(departmentIds)
        const { data: buList } = await getBuList({
          departmentIds: departmentIds.join(',')
        })
        this.$set(this.selectOpt, 'buList', buList)
        this.searchProps.buIds = []
      }
    }
  }
</script>
<style lang="scss" scoped></style>
