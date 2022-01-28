<template>
  <!-- 退货订单管理 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm')"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="部门："
              prop="departmentIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.departmentIds"
                placeholder="请选择"
                clearable
                :data-list="getDepartSelectBeanByUserId('departList')"
              >
                <el-option
                  v-for="item in selectOpt.departList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="母品牌："
              prop="brandSuperiorIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.brandSuperiorIds"
                placeholder="请选择"
                clearable
                :data-list="getBrandList('brandList')"
              >
                <el-option
                  v-for="item in selectOpt.brandList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="项目组："
              prop="buIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.buIds"
                placeholder="请选择"
                clearable
                :data-list="getSelectBeanByUserId('buIdList')"
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
              label="计划月份："
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'sale_amountTarget_export'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_amountTarget_import'"
          @click="importList"
        >
          导入
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_amountTarget_del'"
          @click="del"
        >
          删除
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column prop="departmentName" align="center" label="部门" />
      <el-table-column prop="buName" align="center" label="项目组" />
      <el-table-column prop="parentBrandName" align="center" label="母品牌" />
      <el-table-column prop="month" align="center" label="销售计划月份" />
      <el-table-column align="center" label="计划金额">
        <template slot-scope="{ row }">
          {{ row.currency }}
          &nbsp;
          {{ row.totalPlanAmount || 0 }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建人">
        <template slot-scope="{ row }">
          {{ row.creator }}
          <br />
          {{ row.createDate }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            type="text"
            v-permission="'saleAmountTarget_log'"
            @click="log(row)"
          >
            操作日志
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
    <!-- 日志 -->
    <log-list
      v-if="logVisible.show"
      :visible.sync="logVisible"
      :filterData="filterData"
      :type="'report'"
    ></log-list>
    <!-- 日志 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listSaleAmountTarget,
    exportAmountListUrl,
    getOrderCount,
    importAmountTargetUrl,
    delAmountTarget
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      logList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        searchProps: {
          departmentIds: '',
          brandSuperiorIds: '',
          buIds: '',
          month: ''
        },
        // 表格数据
        tableData: {
          list: [
            {
              children: [{}]
            },
            {
              children: [{}]
            }
          ],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        logVisible: { show: false },
        filterData: {}
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
          const {
            data: { list, total }
          } = await listSaleAmountTarget({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list
          this.tableData.total = total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      log(row) {
        var relCode = row.buId + '-' + row.month + '-' + row.parentBrandId
        this.logVisible.show = true
        this.filterData = { relCode, module: '2' }
      },
      // 导出
      async exportList() {
        let params = ''
        const choseList = this.tableChoseList
        if (choseList.length) {
          params = { ids: this.tableChoseList.map((item) => item.id).join(',') }
        } else {
          params = { ...this.searchProps }
        }
        // 先判断导出数量
        const { data: count } = await getOrderCount(params)
        if (count > 10000) {
          return this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
        }
        this.$exportFile(exportAmountListUrl, { params })
      },
      // 导入
      importList() {
        this.linkTo(
          '/common/importfile?templateId=' +
            145 +
            '&url=' +
            importAmountTargetUrl
        )
      },
      // 删除
      del() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const params = this.tableChoseList.map((item) => item.id).join(',')
          try {
            await delAmountTarget({ params })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      }
    }
  }
</script>
