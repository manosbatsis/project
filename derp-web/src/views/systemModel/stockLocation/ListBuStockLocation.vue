<template>
  <!-- 事业部库位类型 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->

    <!-- 搜索面板 -->
    <JFX-search-panel
      :showOpenBtn="false"
      @reset="resetSearchForm"
      @search="getList(1)"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
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
            <el-form-item label="状态：" prop="status">
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                filterable
                clearable
                style="width: 100%"
                :data-list="
                  getSelectList('bu_stock_location_type_config_statusList')
                "
              >
                <el-option
                  v-for="item in selectOpt.bu_stock_location_type_config_statusList"
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
          type="primary"
          v-permission="'buStockLocation_Add'"
          @click="showModal()"
        >
          新增
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->

    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-if="row.status === '1'"
          v-permission="'buStockLocation_ChangeStatus'"
          @click="handleToggleStatus(row)"
        >
          禁用
        </el-button>
        <el-button
          type="text"
          v-if="row.status !== '1'"
          v-permission="'buStockLocation_ChangeStatus'"
          @click="handleToggleStatus(row)"
        >
          启用
        </el-button>
        <el-button
          type="text"
          v-permission="'buStockLocation_Log'"
          @click="handleShowLog(row)"
          >日志</el-button
        >
      </template>
    </JFX-table>
    <!-- 表格 end-->

    <!-- 新增/编辑弹窗 -->
    <editBuLocation
      v-if="editBuLocation.visible.show"
      :isVisible.sync="editBuLocation.visible"
      :id="editBuLocation.id"
      @comfirm="getList"
    />
    <!-- 新增/编辑弹窗 end -->

    <!-- 日志 -->
    <log-list
      type="system"
      v-if="logVisible.show"
      :visible.sync="logVisible"
      :filterData="logVisible.filterData"
    ></log-list>
    <!-- 日志 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  // import { listByPage, exportMerchantBuRelUrl } from '@a/companyFile'
  import { listByPage, modifyStockLocationType } from '@a/stockLocation'
  export default {
    mixins: [commomMix],
    components: {
      /* 新增/编辑弹窗  */
      editBuLocation: () => import('./components/EditBuLocation'),
      logList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        /* 查询数据 */
        searchProps: {
          status: '',
          buId: ''
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '库位类型',
            prop: 'name',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建时间',
            prop: 'createDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建人',
            prop: 'createrName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '100',
            align: 'center'
          }
        ],
        /* 新增/编辑弹窗组件状态 */
        editBuLocation: {
          visible: { show: false }
        },
        /** 日志弹框 */
        logVisible: { show: false, filterData: {} }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      /* 获取表格数据 */
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listByPage({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      /* 显示新增编辑弹窗 */
      showModal() {
        this.editBuLocation.visible.show = true
      },
      closeModal() {
        this.editBuLocation.visible.show = false
        this.getList()
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      },
      // 切换启用/禁用
      handleToggleStatus(row) {
        const message = row.status === '1' ? '确认禁用?' : '确认启用?'
        this.$confirm(message, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          await modifyStockLocationType({
            id: row.id,
            status: row.status === '1' ? 0 : 1
          })
          this.$successMsg('操作成功')
          this.getList()
        })
      },
      // 日志
      handleShowLog(row) {
        this.logVisible.show = true
        this.logVisible.filterData = {
          id: row.id,
          module: 7
        }
      }
    }
  }
</script>
