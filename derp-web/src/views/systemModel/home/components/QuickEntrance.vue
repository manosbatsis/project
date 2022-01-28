<template>
  <div class="container bgc-w">
    <div class="title">
      <div>
        <span
          class="title__btn"
          :class="{ 'title__btn--active': tabsIdx === 0 }"
          @click="tabsIdx = 0"
        >
          快捷入口
        </span>
        /
        <span
          class="title__btn"
          :class="{ 'title__btn--active': tabsIdx === 1 }"
          @click="getChartData"
        >
          事业部库存
        </span>
      </div>
      <el-button
        v-if="tabsIdx === 1"
        type="primary"
        @click="linkTo(`/stock/businessstock?buId=${currentBuId}`)"
      >
        详情
      </el-button>
    </div>
    <!-- 快捷入口 -->
    <el-row v-if="tabsIdx === 0">
      <el-col
        v-for="item in tabsList"
        :span="8"
        :key="item"
        class="flex-c-c"
        style="height: 200px"
      >
        <QuickEntranceCard :type="item" />
      </el-col>
    </el-row>
    <!-- 快捷入口 end -->
    <!-- 事业部库存 -->
    <el-row v-if="tabsIdx === 1" style="height: 200px">
      <el-col :span="6">
        <div
          v-show="showPieChart"
          ref="pieChart"
          style="width: 100%; height: 200px"
        ></div>
        <div class="empty" v-show="!showPieChart">暂无数据</div>
      </el-col>

      <el-col :span="18" style="position: relative">
        <div
          v-show="showPanelChart"
          ref="panelChart"
          style="width: 100%; height: 200px"
        ></div>
        <span
          class="prev"
          :style="{
            color: currentPieColor,
            cursor: preIdx === 0 ? 'not-allowed' : 'pointer'
          }"
          @click="goPrev"
        >
          &lt;
        </span>
        <span
          class="next"
          :style="{
            color: currentPieColor,
            cursor: nextIdx === PanelData.length ? 'not-allowed' : 'pointer'
          }"
          @click="goNext"
        >
          &gt;
        </span>
        <div class="empty" v-show="!showPanelChart">暂无数据</div>
      </el-col>
    </el-row>
    <!-- 事业部库存 end -->
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import commomMix from '@m/index'
  import {
    buStockPieChart,
    buStockPanelChart
  } from '@/views/systemModel/home/echarts'
  import { countBuInventory, countBuInventoryRate } from '@a/home'
  export default {
    mixins: [commomMix],
    components: {
      /* 快捷入口卡片组件 */
      QuickEntranceCard: () => import('../components/QuickEntranceCard')
    },
    data() {
      return {
        tabsIdx: 0,
        colors: [
          '#FFCC99',
          '#946EC6',
          '#FFCC66',
          '#ee9ca7',
          '#91c7ae',
          '#61a0a8',
          '#FCD63D',
          '#DAA3FF',
          '#75B1FF',
          '#FF94C1',
          '#FF9900'
        ],
        tabsList: ['purchase', 'sale', 'transfer'],
        showPieChart: true,
        showPanelChart: true,
        currentBuId: '',
        currentBuName: '',
        currentPieColor: '',
        preIdx: 0,
        nextIdx: 4,
        PanelData: [],
        PanelDom: null,
        pieChartDom: null
      }
    },
    methods: {
      async getChartData() {
        this.tabsIdx = 1
        this.fetchPieData()
      },
      /* 获取饼图数据 */
      async fetchPieData() {
        try {
          const { data } = await countBuInventory()
          if (data && data.length) {
            /* 随机颜色 */
            const colors = this.colors
              .slice()
              .sort(() => (Math.random() > 0.5 ? -1 : 1))
            const chartData = data.map((item, index) => {
              /* 防止颜色选择超过数组长度 */
              const i = index % this.colors.length
              return {
                value: item.surplusNum || 0,
                name: item.buName || '',
                id: item.buId || '',
                itemStyle: {
                  color: colors[i]
                },
                label: {
                  color: '#fff',
                  textBorderWidth: 2,
                  textBorderColor: colors[i]
                },
                selected: true
              }
            })
            this.currentBuId = data[0].buId
            this.currentBuName = data[0].buName
            this.currentPieColor = colors[0]
            this.fetchPanelData(data[0].buId, colors[0])
            this.showPieChart = true
            await this.$nextTick()
            this.pieChartDom = echarts.init(this.$refs.pieChart)
            /* 渲染饼状图 */
            buStockPieChart(this.pieChartDom, chartData)
            window.addEventListener('resize', () => {
              this.pieChartDom.resize()
            })
            /* 注册点击事件 */
            this.pieChartDom.on('click', ({ data }) => {
              if (data.name === this.currentBuName) {
                this.pieChartDom.dispatchAction({
                  type: 'pieUnSelect',
                  name: this.currentBuName
                })
              }
              this.pieChartDom.dispatchAction({
                type: 'pieSelect',
                name: data.name
              })
              this.preIdx = 0
              this.nextIdx = 4
              this.fetchPanelData(data.id, data.itemStyle.color)
            })
          } else {
            this.showPieChart = false
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 获取仪表盘数据
      async fetchPanelData(buId, color) {
        try {
          const { data } = await countBuInventoryRate({ buId })
          if (data && data.length) {
            this.PanelData = data.map(({ rate, name, num: value }) => ({
              data: [{ name, value }],
              color,
              rate
            }))
            this.showPanelChart = true
            await this.$nextTick()
            /* 渲染仪表盘 */
            this.PanelDom = echarts.init(this.$refs.panelChart)
            buStockPanelChart(this.PanelDom, this.PanelData.slice(0, 4))
            window.addEventListener('resize', () => {
              this.PanelDom.resize()
            })
          } else {
            this.showPanelChart = false
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      async goPrev() {
        if (this.preIdx === 0) return false
        this.preIdx--
        this.nextIdx--
        await this.$nextTick()
        this.PanelDom.clear()
        /* 渲染仪表盘 */
        buStockPanelChart(
          this.PanelDom,
          this.PanelData.slice(this.preIdx, this.nextIdx)
        )
      },
      async goNext() {
        if (this.nextIdx === this.PanelData.length) return false
        this.preIdx++
        this.nextIdx++
        await this.$nextTick()
        this.PanelDom.clear()
        /* 渲染仪表盘 */
        buStockPanelChart(
          this.PanelDom,
          this.PanelData.slice(this.preIdx, this.nextIdx)
        )
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    border-left: 6px solid #72e8e1;
    .title {
      padding: 18px 20px;
      border-bottom: 1px solid #ebeef5;
      height: 60px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-sizing: border-box;
      color: #333;
      &__btn {
        cursor: pointer;
        &--active {
          font-weight: bolder;
          color: #409eff;
        }
      }
    }
    .empty {
      width: 100%;
      height: 200px;
      line-height: 200px;
      text-align: center;
      color: #666;
    }
    .prev {
      position: absolute;
      left: 3%;
      bottom: 16%;
      cursor: pointer;
      z-index: 3;
      user-select: none;
    }
    .next {
      position: absolute;
      right: 3%;
      bottom: 16%;
      cursor: pointer;
      z-index: 3;
      user-select: none;
    }
  }
</style>
