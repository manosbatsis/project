<template>
  <!-- 效期预警组件 -->
  <div class="bgc-w container">
    <div class="title">
      <div>效期预警</div>
      <el-button type="primary" @click="linkTo('/stock/validitywarn')">
        详情
      </el-button>
    </div>
    <div>
      <span class="label">统计仓库:</span>
      <el-select
        v-model="depotId"
        placeholder="请选择"
        filterable
        :clearable="false"
        @change="depotIdChange"
      >
        <el-option
          v-for="item in depotList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>
    <!-- 饼状图 -->
    <div
      ref="pieChart"
      style="width: 100%; height: 490px"
      v-show="showChart"
    ></div>
    <div class="empty" v-show="!showChart">该仓库下没有数据</div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import commomMix from '@m/index'
  import { InventoryWarningPieChart } from '@/views/systemModel/home/echarts'
  import { countInventoryWarning } from '@a/home'
  import { getSelectBeanByMerchantRel, getDepotSelectBean } from '@a/base'
  export default {
    mixins: [commomMix],
    data() {
      return {
        showChart: true,
        depotId: '',
        depotList: [],
        pieChartDom: null
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { userType = '1' } = sessionStorage.getItem('userInfo')
          ? JSON.parse(sessionStorage.getItem('userInfo'))
          : {}
        /* 判断是否为admin用户 */
        userType && userType + '' === '1'
          ? await this.getAllDepotList()
          : await this.getDepotList()
        this.depotIdChange(this.depotId)
      },
      /* 获取仓库列表 */
      async getDepotList() {
        try {
          const { data } = await getSelectBeanByMerchantRel({
            type: 'a,c',
            isInvertoryFallingPrice: 1
          })
          this.depotList = data || []
          this.depotId = this.depotList.length ? this.depotList[0].value : ''
          return data
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取所有仓库列表 admin用户下使用 */
      async getAllDepotList() {
        try {
          const { data: aTypeDepotList } = await getDepotSelectBean({
            type: 'a'
          })
          const { data: cTypeDepotList } = await getDepotSelectBean({
            type: 'c'
          })

          this.depotList = [...aTypeDepotList, ...cTypeDepotList] || []
          this.depotId = this.depotList.length ? this.depotList[0].value : ''
          return [...aTypeDepotList, ...cTypeDepotList]
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 仓库改变 */
      async depotIdChange(depotId) {
        if (!depotId) return false
        try {
          const { data } = await countInventoryWarning({ depotId })
          if (data && data.length) {
            const chartData = data.map(
              ({ effectiveIntervalLabel, surplusNum }) => ({
                value: surplusNum || 0,
                name: effectiveIntervalLabel || '',
                selected: true
              })
            )
            await this.$nextTick()
            /* 渲染饼状图 */
            this.pieChartDom = echarts.init(this.$refs.pieChart)
            InventoryWarningPieChart(this.pieChartDom, chartData)
            window.addEventListener('resize', () => {
              this.pieChartDom.resize()
            })
            this.showChart = true
          } else {
            this.showChart = false
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    border-left: 6px solid #daa3ff;
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
    .label {
      margin-left: 20px;
      margin-bottom: 10px;
      padding-right: 10px;
      color: #666;
    }
    .empty {
      width: 100%;
      height: 482px;
      line-height: 490px;
      text-align: center;
      color: #666;
    }
  }
</style>
