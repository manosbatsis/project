<template>
  <!-- 事业部财务进销存页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb style="margin-bottom: 20px" showGoback />
    <!-- 面包屑 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showIndex
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="monthInstorageNum" slot-scope="{ row }">
        <el-button type="text" @click="jumpDetail(row, '1')">
          {{ row.monthInstorageNum }}
        </el-button>
      </template>
      <template slot="monthOutstorageNum" slot-scope="{ row }">
        <el-button type="text" @click="jumpDetail(row, '0')">
          {{ row.monthOutstorageNum }}
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { buInventorSummaryDetail } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 表格列数据
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '130',
            align: 'center',
            hide: true
          },
          {
            label: '来源单号',
            prop: 'code',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '业务单号',
            prop: 'businessNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '单据类型',
            prop: 'thingName',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '仓库',
            prop: 'depotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '出入时间',
            prop: 'divergenceDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '合同号',
            prop: 'contractNo',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: 'PO号',
            prop: 'poNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '客户/供应商',
            prop: 'customerName',
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
            prop: 'goodsName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '数量',
            prop: 'num',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '理货单位',
            prop: 'unitLabel',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '备注',
            prop: 'remark',
            minWidth: '100',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await buInventorSummaryDetail({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.$route.query
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      }
    }
  }
</script>
