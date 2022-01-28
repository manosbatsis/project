<template>
  <!-- PO商品核销表组件 -->
  <div class="page-bx bgc-w" style="padding-top: 0">
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm', getList)"
      @search="getList(1)"
      :showOpenBtn="true"
      style="margin-top: 0"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO号："
              prop="po"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.po"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准条码："
              prop="commbarcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.commbarcode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="天数："
              prop="days"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.days"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="item in daySelectList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.buId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getBUSelectBean('businessList')"
              >
                <el-option
                  v-for="item in selectOpt.businessList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getCustomerSelectBean('customerList')"
              >
                <el-option
                  v-for="item in selectOpt.customerList"
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
          v-permission="'vipPoBillVerification_exportMain'"
          type="primary"
          @click="exportMain"
        >
          汇总表导出
        </el-button>
        <el-button
          v-permission="'vipPoBillVerification_exportDatails'"
          type="primary"
          @click="exportDatails"
        >
          明细表导出
        </el-button>
        <el-button
          v-permission="'vipPoBillVerification_flashVipPoBillVeris'"
          type="primary"
          @click="refreshList"
        >
          刷新列表
        </el-button>
        <span class="tips fs-12" v-if="billDate"
          >数据统计截止时间：{{ billDate }}</span
        >
        <span class="tips fs-12" v-if="labelTime"
          >生成时间：{{ labelTime }}</span
        >
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
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column
        prop="merchantName"
        align="center"
        min-width="100"
        label="公司"
      />
      <el-table-column
        prop="depotName"
        align="center"
        min-width="120"
        label="仓库"
      />
      <el-table-column
        prop="buName"
        align="center"
        min-width="120"
        label="事业部"
      />
      <el-table-column prop="po" align="center" min-width="120" label="PO号" />
      <el-table-column
        prop="customerName"
        align="center"
        min-width="120"
        label="客户名称"
      />
      <el-table-column
        prop="poDate"
        align="center"
        min-width="80"
        label="po时间"
      />
      <el-table-column
        prop="commbarcode"
        align="center"
        min-width="120"
        label="标准条码"
      />
      <el-table-column
        prop="superiorParentBrand"
        align="center"
        min-width="80"
        label="母品牌"
      />
      <el-table-column align="center" min-width="80" label="销售单价">
        <template slot-scope="{ row }">
          {{ `${row.currency || ''}  ${row.salePrice || ''}` }}
        </template>
      </el-table-column>
      <el-table-column
        prop="unsettledAccount"
        align="center"
        min-width="80"
        label="库存量"
      />
      <el-table-column align="center" min-width="80" label="库存金额">
        <template slot-scope="{ row }">
          {{ `${row.currency || ''}  ${row.inventoryAmount || ''}` }}
        </template>
      </el-table-column>
      <el-table-column
        prop="shelfTotalAccount"
        align="center"
        min-width="80"
        label="上架总量"
      />
      <el-table-column
        prop="shelfTotalAmount"
        align="center"
        min-width="80"
        label="上架总金额"
      />
      <el-table-column
        prop="shelfDamagedAccount"
        align="center"
        min-width="100"
        label="上架残损量"
      />
      <el-table-column
        prop="firstShelfDate"
        align="center"
        min-width="150"
        label="首次上架时间"
      />
      <el-table-column
        prop="billTotalAccount"
        align="center"
        min-width="80"
        label="账单总量"
      />
      <el-table-column
        prop="billRecentDate"
        align="center"
        min-width="150"
        label="最近账单时间"
      />
      <el-table-column
        prop="outDepotTotalAccont"
        align="center"
        min-width="100"
        label="销售出库总量"
      />
      <el-table-column
        prop="nationalInspectionSampleAccount"
        align="center"
        min-width="80"
        label="国检抽样"
      />
      <el-table-column
        prop="saleReturnAccount"
        align="center"
        min-width="100"
        label="销售退数量"
      />
      <el-table-column
        prop="transferInAccount"
        align="center"
        min-width="80"
        label="调拨入库"
      />
      <el-table-column
        prop="transferOutAccount"
        align="center"
        min-width="80"
        label="调拨出库"
      />
      <el-table-column
        prop="vipHcAccount"
        align="center"
        min-width="80"
        label="唯品红冲"
      />
      <el-table-column
        prop="vipHcAccount"
        align="center"
        min-width="80"
        label="盘盈数量"
      />
      <el-table-column
        prop="inventoryDeficientAccount"
        align="center"
        min-width="80"
        label="盘亏数量"
      />
      <el-table-column
        prop="scrapAccount"
        align="center"
        min-width="80"
        label="报废数量"
      />
      <el-table-column prop="days" align="center" min-width="80" label="天数" />
      <el-table-column align="center" width="100" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            type="text"
            @click="
              linkTo(
                `/report/poverifidetail?poNo=${row.po}&commbarcode=${row.commbarcode}&depotId=${row.depotId}`
              )
            "
          >
            详情
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import {
    listVipPoBillVeriList,
    getDataTime,
    exportMainUrl,
    exportDetails,
    flashVipPoBillVeris
  } from '@a/reportManage/index'
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          po: '',
          commbarcode: '',
          days: '',
          buId: '',
          customerId: ''
        },
        daySelectList: [
          { key: '0', value: '30天以内' },
          { key: '30', value: '30~60天' },
          { key: '60', value: '60~90天' },
          { key: '90', value: '90天及以上' },
          { key: '120', value: '120天及以上' },
          { key: '180', value: '180天及以上' }
        ],
        billDate: '',
        // 表格数据
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 11
        }
      }
    },
    computed: {
      // 计算生成时间，对比数据中的modifyDate，获取最大的
      labelTime() {
        let timeArr = this.tableData.list.filter((item) => !!item.modifyDate)
        // 为空退出
        if (!timeArr.length) {
          return ''
        }
        // 转换
        timeArr = timeArr.map((item) => new Date(item.modifyDate).getTime())
        // 取最大值
        const result = Math.max(...timeArr)
        if (!result) {
          return
        }
        return dayjs(result).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    async mounted() {
      this.getList()
      const { data } = await getDataTime()
      if (data.billDate) {
        this.billDate = data.billDate
      }
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const params = { ...this.searchProps }
          const {
            data: { list, total }
          } = await listVipPoBillVeriList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...params
          })
          this.tableData.list = list
          this.tableData.total = total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 汇总表导出
      exportMain() {
        this.$showToast('确定导出选中对象？', () => {
          this.$exportFile(exportMainUrl, { ...this.searchProps })
        })
      },
      // 明细表导出
      async exportDatails() {
        // this.$exportFile(exportDetailsUrl, { ...this.searchProps })
        try {
          const { data } = await exportDetails({ ...this.searchProps })
          if (data.code === '00') {
            this.$alertSuccess('导出成功，请在报表任务列表查看进度')
          }
        } catch (err) {
          console.log(err)
        }
      },
      // 刷新
      refreshList() {
        const { po } = this.searchProps
        let params = {}
        let tips = '确定刷新?'
        if (po) {
          params = { po }
          tips = '确定刷新该PO号?'
        }
        this.$showToast(tips, async () => {
          try {
            const { data } = await flashVipPoBillVeris(params)
            if (data.code === '00') {
              this.$successMsg('正在刷新，稍后点击【查询】，查询结果')
            }
          } catch (err) {
            console.log(err)
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .tips {
    color: red;
    padding-left: 10px;
  }
</style>
