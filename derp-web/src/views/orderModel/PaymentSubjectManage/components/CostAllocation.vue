<template>
  <!-- 费项配置页面 -->
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
              label="所属层级："
              prop="level"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.level"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('settlementConfig_levelList')"
              >
                <el-option
                  v-for="item in selectOpt.settlementConfig_levelList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="上级费项名称："
              prop="parentProjectName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.parentProjectName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="本级费项名称："
              prop="projectName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.projectName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="主数据编码："
              prop="inExpCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.inExpCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="NC收支费项："
              prop="paymentSubjectId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.paymentSubjectId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in ncSelectList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('settlementConfig_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.settlementConfig_statusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="适用类型："
              prop="type"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.type"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('settlementConfig_typeList')"
              >
                <el-option
                  v-for="item in selectOpt.settlementConfig_typeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="适用模块："
              prop="moduleType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.moduleType"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('settlementConfig_moduleTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.settlementConfig_moduleTypeList"
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
          v-permission="'settlementConfig_toAddPage'"
          type="primary"
          @click="showModal('add')"
          >新增</el-button
        >
        <el-button
          :loading="exportBtnLoading"
          v-permission="'settlementConfig_export'"
          type="primary"
          @click="exportList"
          >导出</el-button
        >
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      ref="table"
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column
        prop="levelLabel"
        align="center"
        min-width="80"
        label="所属层级"
      />
      <el-table-column
        prop="parentProjectName"
        align="center"
        min-width="80"
        label="上级费项名称"
      />
      <el-table-column
        prop="projectName"
        align="center"
        min-width="120"
        label="本级费项名称"
      />
      <el-table-column
        prop="projectCode"
        align="center"
        min-width="120"
        label="本级编码"
      />
      <el-table-column
        prop="inExpCode"
        align="center"
        min-width="80"
        label="主数据编码"
      />
      <el-table-column
        prop="paymentSubjectName"
        align="center"
        width="100"
        label="NC收支费项"
      />
      <el-table-column
        prop="customerNames"
        align="center"
        min-width="80"
        label="适用客户"
      />
      <el-table-column
        prop="typeLabel"
        align="center"
        min-width="80"
        label="适用类型"
      />
      <el-table-column
        prop="statusLabel"
        align="center"
        min-width="80"
        label="状态"
      />
      <el-table-column
        prop="modifyDate"
        align="center"
        min-width="150"
        label="最近编辑时间"
      />
      <el-table-column
        prop="modifierName"
        align="center"
        min-width="80"
        label="编辑人"
      />
      <el-table-column align="center" width="100" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            v-permission="'settlementConfig_edit'"
            type="text"
            @click="showModal('edit', row.id)"
            >编辑</el-button
          >
          <el-button
            v-permission="'settlementConfig_isOrNotEnable'"
            type="text"
            v-if="row.status === '0'"
            @click="changeStatus(row.id, '1')"
            >启用</el-button
          >
          <el-button
            v-permission="'settlementConfig_isOrNotEnable'"
            type="text"
            v-else
            @click="changeStatus(row.id, '0')"
            >禁用</el-button
          >
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
    <!-- 编辑项 -->
    <CostAllocationEdit
      v-if="visible.show"
      :showDialog="visible"
      :dialogTitle="dialogTitle"
      @close="closeCostAllocationEdit"
      :id="costAllocationEditId"
    />
    <!-- 编辑项 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getNCSelectBean } from '@a/rebateManage/index.js'
  import {
    settlementConfigList,
    settlementExport,
    tab1IsOrNotEnable
  } from '@a/paymentSubjectManage'
  export default {
    mixins: [commomMix],
    components: {
      CostAllocationEdit: () => import('./CostAllocationEdit')
    },
    data() {
      return {
        // 搜索数据
        searchProps: {
          level: '',
          parentProjectName: '',
          projectName: '',
          inExpCode: '',
          paymentSubjectId: '',
          status: '',
          type: '',
          moduleType: ''
        },
        // 模态框title
        dialogTitle: '新增项目',
        // nc下拉
        ncSelectList: [],
        // 导出按钮loadng
        exportBtnLoading: false,
        // 编辑id
        costAllocationEditId: ''
      }
    },
    async mounted() {
      this.getList()
      const { data: ncData } = await getNCSelectBean()
      this.ncSelectList = ncData
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await settlementConfigList({
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
      // 打开新增编辑框
      showModal(type, id) {
        this.costAllocationEditId = id
        this.visible.show = true
        if (type === 'edit') {
          this.dialogTitle = '编辑项目'
        } else {
          this.dialogTitle = '新增项目'
        }
      },
      // 关闭新增编辑框
      closeCostAllocationEdit() {
        this.visible.show = false
        this.getList()
      },
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.exportBtnLoading = true
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(settlementExport, { ids })
          })
        } else {
          this.$exportFile(settlementExport, { ...this.searchProps })
        }
        this.exportBtnLoading = false
      },
      /* 禁用、启用 */
      changeStatus(id, status) {
        const helper = {
          0: '禁用',
          1: '启用'
        }
        this.$showToast(`确定要${helper[status]}吗？`, async () => {
          try {
            await tab1IsOrNotEnable({ id, status })
            this.getList()
            this.$successMsg(`${helper[status]}成功`)
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          }
        })
      }
    }
  }
</script>
