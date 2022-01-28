<template>
  <!-- 月结快报 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetSearchForm"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="月结月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                :clearable="false"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                type="month"
                placeholder="选择日期时间"
              ></el-date-picker>
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
                :data-list="getBUSelectBean('buIdList')"
              >
                <el-option
                  v-for="item in selectOpt.buIdList"
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

          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="po号："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.poNo" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="母品牌："
              prop="parentBrandId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.parentBrandId"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getBrandList('superiorParentBrandList')"
              >
                <el-option
                  v-for="item in selectOpt.superiorParentBrandList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'toBTempCostMonthlySnapshot_flash'"
          @click="refresh"
        >
          刷新
        </el-button>
        <el-button
          type="primary"
          v-permission="'toBTempCostMonthlySnapshot_export'"
          @click="exportExcel"
        >
          月结导出
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="shelfDate" slot-scope="{ row }">
        {{ row.shelfDate }}
        <br />
        {{ row.shelfCode }}
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listToBTempMonthlySnapshotCost,
    flashTobTempMonthlySnapshotCost,
    exportToBTempRevenueMonthlySnapshotCostUrl
  } from '@a/receiveMoneyManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: '',
          buId: '',
          poNo: '',
          parentBrandId: '',
          customerId: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '月结月份',
            prop: 'month',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '上架日期',
            slotTo: 'shelfDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: 'PO号',
            prop: 'poNo',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '客户名称',
            prop: 'customerName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
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
          },
          {
            label: 'SD类型',
            prop: 'sdTypeName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '上架好品量',
            prop: 'shelfNum',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '单价',
            prop: 'price',
            minWidth: '150',
            align: 'center',
            hide: true,
            formatter(row) {
              return (row.currency || '') + ' ' + (row.rebateAmount || 0)
            }
          },
          {
            label: 'SD比例',
            prop: 'sdRatio',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '暂估返利金额',
            prop: 'rebateAmount',
            minWidth: '150',
            align: 'center',
            hide: true,
            formatter(row) {
              return (row.currency || '') + ' ' + (row.rebateAmount || 0)
            }
          },
          {
            label: '核销金额',
            prop: 'verifiedAmount',
            minWidth: '150',
            align: 'center',
            hide: true,
            formatter(row) {
              return row.currency + ' ' + (row.verifiedAmount || 0)
            }
          },
          {
            label: '应收账单号',
            prop: 'receiveCode',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '入账月份',
            prop: 'creditMonth',
            minWidth: '150',
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
          const {
            data: { list, total }
          } = await listToBTempMonthlySnapshotCost({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list
          this.tableData.total = total
          return Promise.resolve()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      },
      // 刷新
      async refresh() {
        if (!this.searchProps.month) {
          return this.$warningMsg('请先选择月结月份')
        }
        await flashTobTempMonthlySnapshotCost({
          month: this.searchProps.month
        })
        this.$successMsg('刷新成功')
        this.getList()
      },
      // 导出excel
      exportExcel() {
        if (!this.searchProps.month) {
          return this.$warningMsg('请先选择月结月份')
        }
        const exportJson = { ...this.searchProps }
        this.$exportFile(exportToBTempRevenueMonthlySnapshotCostUrl, exportJson)
      }
    }
  }
</script>
