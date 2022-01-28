<template>
  <!-- 销售赊销单页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="外部交易单号："
              prop="externalCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.externalCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="结算日期："
              prop="saleOrderCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.settlementDate"
                :clearable="false"
                :format="'yyyy-MM-dd'"
                :value-format="'yyyy-MM-dd'"
                type="date"
                placeholder="选择日期时间"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="平台："
              prop="storePlatformCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.storePlatformCode"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('storePlatformList')"
              >
                <el-option
                  v-for="item in selectOpt.storePlatformList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="电商订单号："
              prop="orderCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.orderCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="平台结算单号："
              prop="settlementCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.settlementCode"
                placeholder="请输入"
                clearable
              />
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
          @click="exportList"
          v-permission="'toCTempCostDiffOrder_export'"
        >
          导出
        </el-button>
      </el-col>
    </el-row>
    <el-row class="mr-t-20"></el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listCostDiffOrder
    //  exportCreditOrderUrl
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          externalCode: '',
          settlementDate: '',
          storePlatformCode: '',
          orderCode: '',
          settlementCode: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '平台结算单',
            prop: 'settlementCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '外部交易单号',
            prop: 'externalCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '电商订单号',
            prop: 'orderCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '平台',
            prop: 'storePlatformName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '店铺名称',
            prop: 'shopName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '单据类型',
            prop: 'orderTypeLabel',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '平台费项',
            prop: 'platformProjectName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '结算日期',
            prop: 'settlementDate',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '费用调整金额',
            prop: 'adjustmentRmbAmount',
            minWidth: '140',
            align: 'center',
            hide: true,
            formatter(row) {
              if (row.adjustmentRmbAmount) {
                return 'RMB ' + row.adjustmentRmbAmount
              }
              return ''
            }
          }
        ]
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        // 订单日期
        this.searchProps.createDateStartDate =
          this.date && this.date.length ? this.date[0] : ''
        this.searchProps.createDateEndDate =
          this.date && this.date.length ? this.date[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listCostDiffOrder({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
          this.getDeclareTypeNum()
        } catch (err) {}
        this.tableData.loading = false
      },
      // 导出
      async exportList() {
        this.$errorMsg('敬请期待')

        // if (this.tableData.total < 1) {
        //   this.$errorMsg('暂无数据可导出')
        //   return false
        // }
        // if (!this.tableChoseList.length) {
        //   this.$errorMsg('至少勾选一条数据！')
        //   return false
        // }
        // if (this.tableChoseList.length > 1) {
        //   this.$errorMsg('只能选择一条数据！')
        //   return false
        // }
        // try {
        //   if (this.tableChoseList.length) {
        //     this.$showToast('确定导出勾选数据？', async () => {
        //       const id = this.tableChoseList[0].id
        //       // const ids = this.tableChoseList.map(item => item.id).toString()
        //       this.$exportFile(exportCreditOrderUrl, { id })
        //     })
        //   } else {
        //     this.$exportFile(exportCreditOrderUrl, { ...this.searchProps })
        //   }
        // } catch (error) {
        //   this.$errorMsg(error.message)
        // }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.date = []
          this.getList(1)
        })
      }
    }
  }
</script>
