<template>
  <div class="page-bx edit-bx bgc-w">
    <JFX-Breadcrumb :showGoback="true" />
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部： {{ detail.buName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        数据截止时间： {{ detail.statisticsDate }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        报表月份： {{ detail.month }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        库存总量： {{ detail.totalNum }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        库存总金额： {{ detail.totalAmount }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        统计币种： {{ detail.currency }}
      </el-col>
    </el-row>
    <table class="pure-table pure-table-bordered clr-I fs-12">
      <tbody>
        <tr>
          <td>在库天数</td>
          <td v-for="(item, index) in detailList" :key="index">
            {{ item.inWarehouseRange }}
          </td>
        </tr>
        <tr>
          <td>库存数量</td>
          <td v-for="(item, index) in detailList" :key="index">
            {{ item.totalNum }}
          </td>
        </tr>
        <tr>
          <td>库存金额</td>
          <td v-for="(item, index) in detailList" :key="index">
            {{ item.totalAmount }}
          </td>
        </tr>
        <tr>
          <td>加权金额</td>
          <td v-for="(item, index) in detailList" :key="index">
            {{ item.weightedAmount }}
          </td>
        </tr>
        <tr>
          <td>库存金额占比</td>
          <td v-for="(item, index) in detailList" :key="index">
            {{ item.proportion }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import { InWarehousetoDetail } from '@a/reportManage/index'
  export default {
    data() {
      return {
        detail: {},
        detailList: []
      }
    },
    async mounted() {
      const { buId, month } = this.$route.query
      const { data } = await InWarehousetoDetail({ buId, month })
      this.detail = data
      this.detailList = data.detailList
    },
    methods: {}
  }
</script>

<style lang="scss" scoped>
  .pure-table {
    border-collapse: collapse;
    border-spacing: 0;
    empty-cells: show;
    border: 1px solid #cbcbcb;
    width: 95%;
    margin-top: 25px;
    text-align: center;
  }
  .pure-table td,
  .pure-table th {
    border-left: 1px solid #cbcbcb;
    border-width: 0 0 0 1px;
    font-size: inherit;
    margin: 0;
    overflow: visible;
    padding: 0.5em 1em;
  }
  .pure-table-bordered td {
    border-bottom: 1px solid #cbcbcb;
  }
  .pure-table td {
    background-color: transparent;
  }
</style>
