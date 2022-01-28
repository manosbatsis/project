<template>
  <div class="container bgc-w mr-t-20">
    <el-row>
      <div class="title">
        <div style="display: flex; align-items: center">
          <div style="margin-right: 20px">品牌销售 Top 10</div>
          <div
            v-for="(item, index) in tabsList"
            class="tabs"
            :key="index"
            :class="{ 'tabs--active': tabsIdx === index }"
            @click="tabsClick(index)"
          >
            <span>{{ item }}</span>
          </div>
        </div>
        <div>
          <span class="label">选择年月：</span>
          <el-date-picker
            v-model="deliverDate"
            type="month"
            placeholder="请选择"
            value-format="yyyy-MM"
            :clearable="false"
            @change="tabsClick(tabsIdx)"
          />
        </div>
      </div>
      <el-col :span="12">
        <div
          ref="pieChart"
          v-show="showPieChart"
          style="width: 100%; height: 356px"
        ></div>
        <div class="empty" v-show="!showPieChart">该日期下没有数据</div>
      </el-col>
      <el-col :span="12">
        <div
          ref="colunmChart"
          v-show="showColunmChart"
          style="width: 100%; height: 356px"
        ></div>
        <div class="empty" v-show="!showColunmChart">该日期下没有数据</div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import commomMix from '@m/index'
  import {
    brandSaleTop10PieChart,
    brandSaleTop10ColunmChart
  } from '@/views/systemModel/home/echarts'
  import {
    countSaleOutOrderSaleNum,
    getTop10SaleOutOrderBrand as getTopOrderBrand,
    countBillOutDepotOrderNum,
    getBillOutDepotTop10ByBrand,
    countOrderSaleNum,
    getTop10OrderBrand
  } from '@a/home'
  export default {
    mixins: [commomMix],
    data() {
      return {
        tabsIdx: 0, // tabs当前选择
        tabsList: ['购销', '代销', '采销执行', '一件代发', 'POP'], // tabs列表数据
        deliverDate: '', // 日期
        topPieData: [], // 饼状图数据
        topColumnData: [], // 柱状图数据
        showPieChart: true, // 是否显示饼状图
        showColunmChart: true, // 是否显示柱状图
        colors: [
          '#946EC6',
          '#EF5665',
          '#F4A869',
          '#FFCCCC',
          '#FFCC99',
          '#0099FF',
          '#9966FF',
          '#33CC66',
          '#CC9933',
          '#FF0099'
        ],
        pieChartDom: null,
        colunmChartDom: null
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        this.deliverDate = this.$formatDate(new Date(), 'yyyy-MM')
        this.tabsClick(this.tabsIdx)
      },
      async tabsClick(index) {
        this.tabsIdx = index
        const deliverDate = this.deliverDate
        try {
          let pieChartData = []
          let columnChartData = []
          switch (index) {
            case 0: {
              const params = { type: '1,4', deliverDate }
              const { data: pieData } = await countSaleOutOrderSaleNum(params)
              const { data: columnData } = await getTopOrderBrand(params)
              pieChartData = pieData
              columnChartData = columnData
              break
            }
            case 1: {
              const { data: pieData } = await countBillOutDepotOrderNum({
                deliverDate
              })
              const { data: columnData } = await getBillOutDepotTop10ByBrand({
                deliverDate
              })
              pieChartData = pieData
              columnChartData = columnData
              break
            }
            case 2: {
              const params = { type: '3', deliverDate }
              const { data: pieData } = await countSaleOutOrderSaleNum(params)
              const { data: columnData } = await getTopOrderBrand(params)
              pieChartData = pieData
              columnChartData = columnData
              break
            }
            case 3: {
              const params = { type: '002', deliverDate }
              const { data: pieData } = await countOrderSaleNum(params)
              const { data: columnData } = await getTop10OrderBrand(params)
              pieChartData = pieData
              columnChartData = columnData
              break
            }
            case 4: {
              const params = { type: '001', deliverDate }
              const { data: pieData } = await countOrderSaleNum(params)
              const { data: columnData } = await getTop10OrderBrand(params)
              pieChartData = pieData
              columnChartData = columnData
              break
            }
          }
          this.renderColumnChart(columnChartData)
          this.renderPieChart(pieChartData)
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 渲染饼状图 */
      async renderPieChart(data) {
        if (!data || !data.length) {
          this.showPieChart = false
          return false
        }
        const seriesData = data.map(({ name, saleNum }) => ({
          value: saleNum || 0,
          name: name || '',
          selected: true
        }))
        this.showPieChart = true
        await this.$nextTick()
        if (!this.pieChartDom) {
          this.pieChartDom = echarts.init(this.$refs.pieChart)
        }
        brandSaleTop10PieChart(this.pieChartDom, seriesData)
        window.addEventListener('resize', () => {
          this.pieChartDom.resize()
        })
      },
      /* 渲染柱状图 */
      async renderColumnChart(data) {
        if (!data || !data.length) {
          this.showColunmChart = false
          return false
        }
        const xAxisData = data.map(({ name }) => name)
        const seriesData = data.map(({ saleNum }) => saleNum)
        this.showColunmChart = true
        await this.$nextTick()
        if (!this.colunmChartDom) {
          this.colunmChartDom = echarts.init(this.$refs.colunmChart)
        }
        brandSaleTop10ColunmChart(this.colunmChartDom, {
          xAxisData,
          seriesData
        })
        window.addEventListener('resize', () => {
          this.colunmChartDom.resize()
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    border-left: 6px solid #72e8e1;
    .title {
      margin-bottom: 10px;
      padding: 18px 20px;
      border-bottom: 1px solid #ebeef5;
      height: 60px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-sizing: border-box;
      color: #333;
    }
    .tabs {
      padding: 18px 20px 14px;
      color: #666;
      border-bottom: 4px solid transparent;
      cursor: pointer;
      &--active {
        background-color: #f2f3f9;
        border-bottom: 4px solid #3488ca;
        color: #3488ca;
      }
    }
    .label {
      padding-right: 10px;
      color: #666;
    }
    .empty {
      width: 100%;
      height: 490px;
      line-height: 490px;
      text-align: center;
      color: #666;
    }
  }
</style>
