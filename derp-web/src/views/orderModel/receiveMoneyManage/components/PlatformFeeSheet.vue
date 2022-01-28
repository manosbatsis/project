<template>
  <!-- 平台费用单页面 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm', getList)"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="费用单号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.code"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="平台单号："
              prop="billCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.billCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="创建时间："
              prop="createDateStr"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.createDateStr"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期时间"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="费用项目："
              prop="itemProjectName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.itemProjectName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="费用类型："
              prop="costType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.costType"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('platformCostOrder_costTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.platformCostOrder_costTypeList"
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
              label="单据状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getSelectList('platformCostOrder_platformCostStatusList')
                "
              >
                <el-option
                  v-for="item in selectOpt.platformCostOrder_platformCostStatusList"
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
                :data-list="getBUSelectBean('buList')"
              >
                <el-option
                  v-for="item in selectOpt.buList"
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
    <!-- 表格 -->
    <JFX-table
      ref="table"
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
      class="mr-t-20"
    >
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column
        prop="code"
        align="center"
        min-width="150"
        label="费用单号"
      />
      <el-table-column
        prop="customerName"
        align="center"
        min-width="150"
        label="客户"
        show-overflow-tooltip
      />
      <el-table-column
        prop="billCode"
        align="center"
        min-width="150"
        label="平台账单号"
        show-overflow-tooltip
      />
      <el-table-column
        prop="itemProjectName"
        align="center"
        min-width="80"
        label="费用项目"
      />
      <el-table-column
        prop="buName"
        align="center"
        min-width="100"
        label="事业部"
      />
      <el-table-column prop="num" align="center" min-width="60" label="数量" />
      <el-table-column
        prop="currency"
        align="center"
        min-width="60"
        label="币种"
      />
      <el-table-column
        prop="amount"
        align="center"
        min-width="80"
        label="金额"
      />
      <el-table-column
        prop="costTypeLabel"
        align="center"
        min-width="80"
        label="费用类型"
      />
      <el-table-column
        prop="createDate"
        align="center"
        min-width="150"
        label="创建日期"
      />
      <el-table-column
        prop="statusLabel"
        align="center"
        min-width="80"
        label="单据状态"
      />
      <el-table-column align="center" width="120" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            type="text"
            @click="linkTo(`/receivemoney/feelistdetail/${row.id}`)"
          >
            详情
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { listPlatformCostOrder } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        searchProps: {
          code: '',
          billCode: '',
          createDateStr: '',
          itemProjectName: '',
          costType: '',
          customerId: '',
          status: '',
          buId: ''
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listPlatformCostOrder({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (err) {
          console.log(err)
        } finally {
          this.tableData.loading = false
        }
      }
    }
  }
</script>
