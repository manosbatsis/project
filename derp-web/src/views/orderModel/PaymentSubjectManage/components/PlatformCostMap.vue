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
              label="平台名称："
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
              label="平台费项名称："
              prop="name"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.name"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="本级费项名称："
              prop="settlementConfigId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.settlementConfigId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in settlementConfigList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="NC收支费项："
              prop="ncPaymentId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.ncPaymentId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in receivePaymentSubjectList"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'platformSettlementConfig_toAddPage'"
          @click="showModal('add')"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'platformSettlementConfig_export'"
          :loading="exportBtnLoading"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'platformSettlementConfig_import'"
          @click="importFile"
        >
          导入
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
          v-permission="'platformSettlementConfig_edit'"
          @click="showModal('edit', row.id)"
        >
          编辑
        </el-button>
        <el-button
          v-if="row.status === '0'"
          type="text"
          v-permission="'platformSettlementConfig_isOrNotEnable'"
          @click="changeStatus(row.id, '1')"
        >
          启用
        </el-button>
        <el-button
          v-if="row.status !== '0'"
          type="text"
          v-permission="'platformSettlementConfig_isOrNotEnable'"
          @click="changeStatus(row.id, '0')"
        >
          禁用
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 新增编辑组件 -->
    <PlatformCostMapEdit
      v-if="platformCostMapEdit.visible.show"
      :isVisible.sync="platformCostMapEdit.visible"
      :type="platformCostMapEdit.type"
      :data="platformCostMapEdit.data"
      @close="getList()"
    />
    <!-- 新增编辑组件 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    platformSettlementConfigList,
    platformSettlementSelectList,
    platformSettlementImport,
    platformSettlementExport,
    isOrNotEnable
  } from '@a/paymentSubjectManage'
  export default {
    mixins: [commomMix],
    components: {
      PlatformCostMapEdit: () => import('./PlatformCostMapEdit')
    },
    data() {
      return {
        /* 查询条件 */
        searchProps: {
          storePlatformCode: '',
          name: '',
          settlementConfigId: '',
          ncPaymentId: '',
          status: ''
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '平台名称',
            prop: 'storePlatformName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '平台费项名称',
            prop: 'name',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '本级费项名称',
            prop: 'settlementConfigName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'NC收支费项',
            prop: 'ncPaymentName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '最近编辑时间',
            prop: 'modifyDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '编辑人',
            prop: 'modifierName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '110', align: 'left' }
        ],
        exportBtnLoading: false,
        /* 本级费项名称列表 */
        settlementConfigList: [],
        /* NC收支费项列表 */
        receivePaymentSubjectList: [],
        /* 新增编辑组件状态 */
        platformCostMapEdit: {
          visible: {
            show: false
          },
          data: {},
          type: 'add'
        }
      }
    },
    mounted() {
      this.mapSelectListToPage()
      this.getList()
    },
    methods: {
      /* 获取表格数据 */
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await platformSettlementConfigList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      /* 获取下拉列表 */
      async mapSelectListToPage() {
        try {
          const {
            data: { settlementConfigList, receivePaymentSubjectList }
          } = await platformSettlementSelectList()
          if (receivePaymentSubjectList && receivePaymentSubjectList.length) {
            this.receivePaymentSubjectList = receivePaymentSubjectList.map(
              ({ id, name }) => ({
                label: name,
                value: id
              })
            )
          }
          if (settlementConfigList && settlementConfigList.length) {
            this.settlementConfigList = settlementConfigList.map(
              ({ id, projectName }) => ({
                label: projectName,
                value: id
              })
            )
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 导出 */
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.exportBtnLoading = true
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(platformSettlementExport, { ids })
          })
        } else {
          this.$exportFile(platformSettlementExport, { ...this.searchProps })
        }
        this.exportBtnLoading = false
      },
      /* 导入 */
      importFile() {
        this.linkTo(
          '/common/importfile?templateId=' +
            149 +
            '&url=' +
            platformSettlementImport
        )
      },
      /* 显示新增编辑弹窗 */
      showModal(type, id) {
        this.platformCostMapEdit.type = type
        this.platformCostMapEdit.visible.show = true
        if (type === 'edit' && id) {
          this.platformCostMapEdit.data = { id }
        } else {
          this.platformCostMapEdit.data = {}
        }
      },
      /* 禁用、启用 */
      changeStatus(id, status) {
        const helper = {
          0: '禁用',
          1: '启用'
        }
        this.$showToast(`确定要${helper[status]}吗？`, async () => {
          try {
            await isOrNotEnable({ id, status })
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
