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
              label="公司编码："
              prop="merchantCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.merchantCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="公司简称："
              prop="merchantName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.merchantName"
                placeholder="请输入"
                clearable
              />
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
                :data-list="getAllSelectBean('businessList')"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->

    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'merchant_bu_rel_add'"
          @click="showModal()"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'merchant_bu_rel_export'"
          @click="exportList"
        >
          导出
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
          v-permission="'merchant_bu_rel_edit'"
          @click="showModal(row.id)"
        >
          编辑
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->

    <!-- 新增/编辑弹窗 -->
    <EditCompanyBusiness
      v-if="editCompanyBusiness.visible.show"
      :isVisible.sync="editCompanyBusiness.visible"
      :id="editCompanyBusiness.id"
      @comfirm="getList"
    />
    <!-- 新增/编辑弹窗 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listMerchantBuRel, exportMerchantBuRelUrl } from '@a/companyFile'
  export default {
    mixins: [commomMix],
    components: {
      /* 新增/编辑弹窗  */
      EditCompanyBusiness: () => import('./components/EditCompanyBusiness')
    },
    data() {
      return {
        /* 查询数据 */
        searchProps: {
          merchantCode: '',
          merchantName: '',
          buId: ''
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '公司编码',
            prop: 'merchantCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '公司简称',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事业部编码',
            prop: 'buCode',
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
            label: '状态',
            prop: 'statusLabel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '采购价格管理',
            prop: 'purchasePriceManageLabel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '销售价格管理',
            prop: 'salePriceManageLabel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '采购审批方式',
            prop: 'purchaseAuditMethodLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '库位管理',
            prop: 'stockLocationManageLabel',
            width: '100',
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
        editCompanyBusiness: {
          visible: { show: false },
          id: ''
        }
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
          const { data } = await listMerchantBuRel({
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
      /* 导出 */
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        if (this.tableChoseList.length) {
          this.$confirm('确定导出勾选数据？', '提示', {
            type: 'warning'
          }).then(() => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportMerchantBuRelUrl, { ids })
          })
          return false
        }
        this.$exportFile(exportMerchantBuRelUrl, { ...this.searchProps })
      },
      /* 显示新增编辑弹窗 */
      showModal(id) {
        console.log(123)
        this.editCompanyBusiness.id = id
        this.editCompanyBusiness.visible.show = true
      },
      closeModal() {
        this.editCompanyBusiness.visible.show = false
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
