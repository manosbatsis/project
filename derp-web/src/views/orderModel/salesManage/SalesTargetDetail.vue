<template>
  <!-- 销售目标详情页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <el-row :gutter="10" class="fs-14 clr-II mr-b-20">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        事业部： {{ data.buName }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        销售计划月份： {{ data.month }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
        计划维度： {{ data.typeLabel }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <table
      class="pure-table pure-table-bordered clr-I fs-12"
      v-if="data.type === '1'"
    >
      <thead>
        <tr>
          <td>商品条码</td>
          <td>商品名称</td>
          <td>标准品牌</td>
          <td>品类</td>
          <td>购销</td>
          <td>电商订单</td>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in tableData.list" :key="index">
          <td>{{ item.barcode }}</td>
          <td>{{ item.goodsName }}</td>
          <td>{{ item.brandParent }}</td>
          <td>{{ item.typeName }}</td>
          <td>{{ item.toBNum }}</td>
          <td>{{ item.toCNum }}</td>
        </tr>
      </tbody>
    </table>
    <table
      class="pure-table pure-table-bordered clr-I fs-12"
      v-if="data.type === '2'"
    >
      <thead>
        <tr>
          <td>商品条码</td>
          <td>商品名称</td>
          <td>标准品牌</td>
          <td v-for="(item, index) in platFormlist" :key="index">{{ item }}</td>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in tableData.list" :key="index">
          <td>{{ item.barcode }}</td>
          <td>{{ item.goodsName }}</td>
          <td>{{ item.brandParent }}</td>
          <td
            v-for="(pItem, index) in Object.keys(item.platformCount)"
            :key="index"
          >
            {{ item.platformCount[pItem] }}
          </td>
        </tr>
      </tbody>
    </table>
    <table
      class="pure-table pure-table-bordered clr-I fs-12"
      v-if="data.type === '3'"
    >
      <thead>
        <tr>
          <td>商品条码</td>
          <td>商品名称</td>
          <td>标准品牌</td>
          <td v-for="(item, index) in shopNamelist" :key="index">{{ item }}</td>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in tableData.list" :key="index">
          <td>{{ item.barcode }}</td>
          <td>{{ item.goodsName }}</td>
          <td>{{ item.brandParent }}</td>
          <td
            v-for="(sitem, index) in Object.keys(item.shopCount)"
            :key="index"
          >
            {{ item.shopCount[sitem] }}
          </td>
        </tr>
      </tbody>
    </table>
    <!-- 商品信息 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { saleTargetGetDetail } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 表格数据
        tableData: {
          list: [{}],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        data: {},
        platFormlist: [],
        shopNamelist: []
      }
    },
    async mounted() {
      try {
        const { data } = await saleTargetGetDetail({ ...this.$route.query })
        this.data = data
        this.tableData.list = data.list || []
        this.shopNamelist = data.shopNamelist
        this.platFormlist = data.platFormlist
      } catch (err) {
        console.log(err)
      }
    }
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
