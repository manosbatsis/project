<template>
  <!-- 关区配置页面 -->
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
              label="仓库编号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.code"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="仓库名称 ："
              prop="name"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.name"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="仓库类别："
              prop="type"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.type"
                placeholder="请选择"
                :data-list="getSelectList('depotInfo_typeList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.depotInfo_typeList"
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
          v-permission="'depot_toAddPage'"
          @click="linkTo(`/warehousefile/DepotAdd`)"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'depot_delDepot'"
          @click="deleteTableItem"
        >
          删除
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
          v-permission="'depot_toEditPage'"
          @click="linkTo(`/warehousefile/DepotEdit?id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'depot_toDetailsPage'"
          @click="linkTo(`/warehousefile/DepotDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-permission="'depot_auditDepot'"
          @click="audit(row.id, row.status === '1' ? '0' : '1')"
        >
          {{ row.status === '1' ? '禁用' : '启用' }}
        </el-button>
        <el-button type="text" v-permission="'depot_log'" @click="openLog(row)">
          日志
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
    <!-- 日志 -->
    <log-list
      v-if="logVisible.show"
      :visible.sync="logVisible"
      :filterData="filterData"
      :type="'system'"
    ></log-list>
    <!-- 日志 end-->
  </div>
</template>
<script>
  import { listDepot, delDepot, auditDepot } from '@a/warehouseFile'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    components: {
      logList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          code: '',
          name: '',
          type: ''
        },
        tableColumn: [
          {
            label: '仓库自编码',
            prop: 'depotCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: 'op仓库编号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '仓库名称',
            prop: 'name',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '仓库地址',
            prop: 'address',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '仓库类别',
            prop: 'typeLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '仓管员',
            prop: 'adminName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '创建时间',
            prop: 'createDate',
            width: '150',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '130', align: 'left' }
        ],
        // 日志弹框
        logVisible: { show: false },
        filterData: {}
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
          const { data } = await listDepot({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 删除表格项
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await delDepot({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 禁用/启用
      audit(id, status) {
        const helper = {
          0: '禁用',
          1: '启用'
        }
        this.$showToast(`确定要${helper[status]}吗？`, async () => {
          try {
            await auditDepot({ id, status })
            this.$successMsg(`${helper[status]}成功`)
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 日志
      openLog(row) {
        this.filterData = { relCode: row.id, module: 6 }
        this.logVisible.show = true
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
