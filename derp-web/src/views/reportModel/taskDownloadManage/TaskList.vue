<template>
  <!-- 业财自核对页面 -->
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
              label="任务类型："
              prop="taskType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.taskType"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('fileTask_taskTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.fileTask_taskTypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="任务模块："
              prop="module"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.module"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('fileTask_moduleList')"
              >
                <el-option
                  v-for="item in selectOpt.fileTask_moduleList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="归属月份："
              prop="ownMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.ownMonth"
                type="month"
                value-format="yyyy-MM"
                placeholder="请选择年月份"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
      class="mr-t-20"
    >
      <el-table-column type="index" align="center" width="55" label="序号" />
      <el-table-column
        prop="moduleLabel"
        align="center"
        width="110"
        label="所属模块"
        show-overflow-tooltip
      />
      <el-table-column
        prop="taskName"
        align="center"
        min-width="80"
        label="任务类型"
        show-overflow-tooltip
      />
      <el-table-column
        prop="ownMonth"
        align="center"
        width="110"
        label="归属月份"
        show-overflow-tooltip
      />
      <el-table-column
        prop="stateLabel"
        align="center"
        width="110"
        label="状态"
        show-overflow-tooltip
      />
      <el-table-column
        prop="remark"
        align="center"
        min-width="80"
        label="描述"
        show-overflow-tooltip
      />
      <el-table-column
        prop="username"
        align="center"
        min-width="80"
        label="操作员"
        show-overflow-tooltip
      />
      <el-table-column
        prop="createDate"
        align="center"
        width="150"
        label="创建时间"
        show-overflow-tooltip
      />
      <el-table-column
        prop="modifyDate"
        align="center"
        width="150"
        label="结束时间"
        show-overflow-tooltip
      />
      <el-table-column align="center" width="110" label="操作">
        <template slot-scope="{ row }">
          <el-button
            v-if="row.state === '3'"
            type="text"
            v-permission="'fileTask_downLoad'"
            @click="exportItem(row.code)"
          >
            下载报表
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>

<script>
  import { listFileTask, downLoadFileTask } from '@a/reportManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          taskType: '',
          module: '',
          ownMonth: ''
        }
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
          const { data } = await listFileTask({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 导出
      exportItem(code) {
        this.$exportFile(downLoadFileTask, { code })
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
