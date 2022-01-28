<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- title -->
    <JFX-title title="事业部" className="mr-t-10" />
    <!-- title end -->
    <el-row :gutter="10" class="fs-14 clr-II detail-row">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        公司：{{ detail.merchantName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        事业部：{{ detail.buName || '- -' }}
      </el-col>
    </el-row>
    <!-- title -->
    <JFX-title title="商品详情" className="mr-t-10" />
    <!-- title end -->
    <el-row :gutter="10" class="fs-14 clr-II detail-row">
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        商品名称：{{ detail.goodsName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        条形码：{{ detail.barcode || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        品牌：{{ detail.brandName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        规格型号：{{ detail.goodsSpec || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        品类：{{ detail.productTypeName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        原产国：{{ detail.countyName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        计量单位：{{ detail.unitName || '- -' }}
      </el-col>
      <el-col class="mr-b-15" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        是否组合：{{ ['否', '是'][detail.isGroup] || '- -' }}
      </el-col>
    </el-row>
    <!-- 组合品详情 -->
    <JFX-title
      title="组合品详情"
      v-if="detail.isGroup === '1'"
      className="mr-t-20"
    />
    <JFX-table
      :tableData.sync="groupListData"
      :tableColumn="groupListColumn"
      :showPagination="false"
      showIndex
      v-if="detail.isGroup === '1'"
    ></JFX-table>
    <!-- 组合品详情 end -->
    <!-- 标准成本单价记录 -->
    <JFX-title title="标准成本单价记录" className="mr-t-20" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      showIndex
    ></JFX-table>
    <!-- 标准成本单价记录 end -->
    <!-- 修改历史记录 -->
    <JFX-title title="修改历史记录" className="mr-t-20" />
    <JFX-table
      :tableData.sync="recordList"
      :tableColumn="recordListColumn"
      showIndex
      @change="getRecordPriceList"
      v-if="$route.query.id"
    ></JFX-table>
    <!-- 修改历史记录 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    settlementPriceDetail,
    listRecordPrice,
    listAllGroupMerchandiseByGroupId
  } from '@a/adjustAccountsManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        detail: {},
        groupListData: {
          list: []
        },
        groupListColumn: [
          {
            label: '商品条码',
            prop: 'barcode',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'name',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '商品编码',
            prop: 'goodsCode',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '单品数量',
            prop: 'groupNum',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '商品价格',
            prop: 'groupPrice',
            minWidth: '100',
            align: 'center',
            hide: true
          }
        ],
        // 表格列数据
        tableColumn: [
          {
            label: '生效年月',
            prop: 'effectiveDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '标准成本单价',
            prop: 'price',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算币种',
            prop: 'currency',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建日期',
            prop: 'createDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '修改日期',
            prop: 'modifyDate',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        // 表格列数据
        recordListColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '生效年月',
            prop: 'effectiveDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '标准成本单价',
            prop: 'price',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算币种',
            prop: 'currencyLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '修改时间',
            prop: 'modifyDate',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '修改人员',
            prop: 'modifier',
            width: '120',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '调价原因',
            prop: 'adjustPriceResult',
            width: '120',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '审核时间',
            prop: 'examineDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '审核人员',
            prop: 'examiner',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '100',
            align: 'center',
            hide: true
          }
        ],
        recordList: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        }
      }
    },
    activated() {
      this.init()
      this.getRecordPriceList()
    },
    mounted() {
      this.init()
      this.getRecordPriceList()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        if (!id) return false
        const {
          data: { detail, itemList }
        } = await settlementPriceDetail({ id })
        this.detail = detail || {}
        this.tableData.list = itemList || []
        if (detail.isGroup === '1' && this.tableData.list.length) {
          const { goodsId } = this.tableData.list[0]
          const { data } = await listAllGroupMerchandiseByGroupId({ goodsId })
          this.groupListData.list = data || []
        }
      },
      // 获取修改历史记录
      async getRecordPriceList() {
        try {
          this.recordList.loading = true
          const { data } = await listRecordPrice({
            begin: (this.recordList.pageNum - 1) * this.recordList.pageSize,
            pageSize: this.recordList.pageSize || 10,
            settlementPriceId: this.$route.query.id
          })
          this.recordList.list = data.list || []
          this.recordList.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.recordList.loading = false
        }
      }
    }
  }
</script>
