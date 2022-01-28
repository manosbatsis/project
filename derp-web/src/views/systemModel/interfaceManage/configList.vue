<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @search="getList(1)" @reset="resetSearchForm">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label=" 平台名称："
              prop="platformName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.platformName"
                clearable
                placeholder="请输入"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="app_key："
              prop="appKey"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.appKey"
                clearable
                placeholder="请输入"
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
          v-permission="'api_toAddPage'"
          @click="linkTo(`/interface/addConfig`)"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'api_delApi'"
          @click="removeItem"
        >
          删除
        </el-button>
        <!-- <el-button type="primary" >批量导入</el-button>
        <el-button type="primary" >批量导出</el-button> -->
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
          v-permission="'api_auditApi'"
          @click="auditItem(row.id, '0')"
          style="padding-left: 6px"
        >
          禁用
        </el-button>
        <el-button
          type="text"
          v-if="row.status === '0'"
          v-permission="'api_auditApi'"
          @click="auditItem(row.id, '1')"
          style="padding-left: 6px"
        >
          启用
        </el-button>
        <el-button
          type="text"
          v-permission="'api_toEditPage'"
          @click="
            linkTo(`/interface/addConfig?id=${row.id}`, '接口配置管理编辑')
          "
          style="padding-left: 6px"
        >
          编辑
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listApi, delApi, auditApi } from '@a/interfaceManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          platformName: '',
          appKey: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '平台名称',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'app_key',
            prop: 'appKey',
            minWidth: '120',
            align: 'center',
            hide: false
          },
          {
            label: '密钥',
            prop: 'appSecret',
            minWidth: '120',
            align: 'center',
            hide: false
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '备注',
            prop: 'remark',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '120',
            align: 'left',
            fixed: 'right'
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
      // 获取表格数据 src\views\systemModel\\configList.vue
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listApi({
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
      // 删除数据项
      removeItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
        } else {
          this.$showToast('确定删除选中对象？', async () => {
            await delApi({
              ids: this.tableChoseList.map((item) => item.id).toString()
            })
            this.$successMsg('操作成功')
            this.getList()
          })
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      },
      // 启用/禁用
      auditItem(id, status) {
        this.$showToast('确定启用/禁用选中对象？', async () => {
          await auditApi({ id, status })
          this.$successMsg('操作成功')
          this.getList()
        })
      }
    }
  }
</script>
