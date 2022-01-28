<template>
  <!-- 加价比例配置页面 -->
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
              label="事业部编码："
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
              label="事业部名称："
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
              label="部门名称："
              prop="departmentName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.departmentName"
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
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'businessUnit_add'"
          @click="showModal('add')"
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
      @selection-change="selectionChange"
      @change="getList"
      showSelection
    >
      <template slot="saleRebate" slot-scope="{ row }">
        {{ row.saleRebate * 100 }}
      </template>
      <template slot="purchaseCommission" slot-scope="{ row }">
        {{ row.purchaseCommission * 100 }}
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'businessUnit_edit'"
          @click="showModal('edit', row.id)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'businessUnit_delBusinessUnits'"
          @click="deleteTableItem(row.id)"
        >
          删除
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 新增/编辑弹窗 -->
    <EditBuManage
      v-if="editBuManage.visible.show"
      :showDialog.sync="editBuManage.visible"
      :title="editBuManage.title"
      :id="editBuManage.id"
      @comfirm="closeModal"
    ></EditBuManage>
    <!-- 新增/编辑弹窗 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listBusinessUnits, delBusinessUnits } from '@a/companyFile'
  export default {
    mixins: [commomMix],
    components: {
      EditBuManage: () => import('./components/EditBuManage')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          code: '',
          name: '',
          departmentName: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '事业部编码',
            prop: 'code',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事业部名称',
            prop: 'name',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '部门名称',
            prop: 'departmentName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '编辑时间',
            prop: 'modifyDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '100', align: 'center' }
        ],
        editBuManage: {
          visible: { show: false },
          title: '',
          id: ''
        }
      }
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
          const { data } = await listBusinessUnits({
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
      showModal(type, data) {
        if (type === 'edit') {
          this.editBuManage.title = '编辑'
          this.editBuManage.id = data + ''
        } else {
          this.editBuManage.title = '新增'
          this.editBuManage.id = ''
        }
        this.editBuManage.visible.show = true
      },
      // 删除表格项
      deleteTableItem(id) {
        this.$showToast('确定删除数据？', async () => {
          try {
            await delBusinessUnits({ id })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      closeModal() {
        this.editBuManage.visible.show = false
        this.editBuManage.title = ''
        this.editBuManage.id = ''
        this.getList()
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
