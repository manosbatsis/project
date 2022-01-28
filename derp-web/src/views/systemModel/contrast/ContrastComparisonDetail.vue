<template>
  <!-- 爬虫商品对照详情-->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        平台名称： {{ detail.platformName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        平台用户名： {{ detail.platformUsername || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        公司： {{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        事业部： {{ detail.buName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        爬虫商品货号： {{ detail.crawlerGoodsNo || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        爬虫商品名称： {{ detail.crawlerGoodsName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        状态：{{ detail.statusLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        创建人：{{ detail.createUsername || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        创建时间： {{ detail.createDate || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        修改人：{{ detail.updateUsername || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        修改时间：{{ detail.modifyDate || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <el-tabs v-model="activeName" type="card" class="mr-t-15">
      <el-tab-pane label="基本信息" name="1">
        <JFX-table :tableData.sync="tableData" :showPagination="false">
          <el-table-column
            type="index"
            align="center"
            width="55"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="goodsNo"
            align="center"
            min-width="120"
            label="商品货号"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="goodsName"
            align="center"
            min-width="100"
            label="商品名称"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="barcode"
            align="center"
            min-width="120"
            label="条形码"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="num"
            align="center"
            width="80"
            label="数量"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="price"
            align="center"
            width="130"
            label="销售标准单价"
            show-overflow-tooltip
          ></el-table-column>
        </JFX-table>
      </el-tab-pane>
      <el-tab-pane label="变更记录" name="2">
        <!-- 表格 -->
        <JFX-table
          :tableData.sync="history"
          :showPagination="false"
          :tableHeight="'460px'"
        >
          <el-table-column
            type="index"
            label="序号"
            align="center"
            width="55"
          ></el-table-column>
          <el-table-column
            prop="operateDate"
            label="操作时间"
            align="center"
            width="140"
          ></el-table-column>
          <el-table-column
            prop="operater"
            label="操作人"
            align="center"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="operateResult"
            label="操作项"
            align="center"
            width="140"
          >
            <template slot-scope="scope">
              {{
                scope.row.operateAction ||
                scope.row.operateResult ||
                scope.row.operateRemark
              }}
            </template>
          </el-table-column>
          <el-table-column
            prop="operateRemark"
            label="备注"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
        </JFX-table>
        <!-- 表格 end-->
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import { getContrastdetail } from '@a/contrast'
  import { getOperateLogList } from '@a/base'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情的数据
        detail: {},
        activeName: '1',
        history: {
          list: []
        }
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const id = this.$route.query.id
        if (!id) return false
        const {
          data: { merchandiseContrastDTO = {}, itemDTO = [] }
        } = await getContrastdetail({ id })
        const { data: list } = await getOperateLogList({
          relCode: id,
          module: 13
        })
        this.history.list = list
        this.detail = merchandiseContrastDTO
        this.tableData.list = itemDTO
      }
    }
  }
</script>
