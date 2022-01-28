<template>
  <!-- 税率配置页面 -->
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
              label="公司编码："
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
              label="公司简称："
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
              label="卓志编码："
              prop="topidealCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.topidealCode"
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
          v-permission="'merchant_toAddPage'"
          @click="linkTo('/companyFile/CompanyInfoAdd')"
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
          v-permission="'merchant_toEditPage'"
          @click="linkTo(`/companyFile/CompanyInfoEdit?id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'merchant_toDetailsPage'"
          @click="linkTo(`/companyFile/CompanyInfoDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-if="row.status === '1'"
          v-permission="'merchant_isOrNotEnable'"
          @click="auditMerchandies(row.id, '0')"
        >
          禁用
        </el-button>
        <el-button
          type="text"
          v-else
          v-permission="'merchant_isOrNotEnable'"
          @click="auditMerchandies(row.id, '1')"
        >
          启用
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>
<script>
  import { listMerchant, isOrNotEnable } from '@a/companyFile'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        searchProps: {
          code: '',
          name: '',
          topidealCode: ''
        },
        tableColumn: [
          {
            label: '公司编码',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '公司简称',
            prop: 'name',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '卓志编码',
            prop: 'topidealCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '联系电话',
            prop: 'tel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '接收清关资料邮箱',
            prop: 'email',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '备注',
            prop: 'remark',
            minWidth: '120',
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
          {
            label: '状态',
            prop: 'statusLabel',
            width: '110',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '130', align: 'left' }
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
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listMerchant({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list || []
          this.tableData.total = total || 0
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 禁用或 启用
      auditMerchandies(id, status) {
        const helper = {
          1: '启用',
          0: '禁用'
        }
        this.$confirm(`你确认要${helper[status]}吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          await isOrNotEnable({ id, status })
          this.$successMsg('操作成功')
          this.getList()
        })
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
