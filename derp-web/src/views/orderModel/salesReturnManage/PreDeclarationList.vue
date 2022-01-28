<template>
  <!-- 销售预申报单列表页面 -->
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
              label="预申报单号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.code"
                clearable
                placeholder="请输入"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="关联销退订单号："
              prop="saleReturnOrderCodes"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.saleReturnOrderCodes"
                clearable
                placeholder="请输入"
              ></el-input>
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
            <el-form-item
              label="退出仓库："
              prop="outDepotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.outDepotId"
                placeholder="请选择"
                :data-list="getSelectBeanByMerchantRel('warehouseList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.warehouseList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="退入仓库："
              prop="inDepotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.inDepotId"
                placeholder="请选择"
                :data-list="getSelectBeanByMerchantRel('warehouseList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.warehouseList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                :data-list="getCustomerSelectBean('customer_list')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.customer_list"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="订单状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                :data-list="getSelectList('saleReturnDeclare_statusList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.saleReturnDeclare_statusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO号："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.poNo"
                clearable
                placeholder="请输入"
              ></el-input>
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
          @click="exportList"
          v-permission="'saleReturn_declare_export'"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          @click="deleteTableItem"
          v-permission="'saleReturn_declare_delete'"
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
          style="padding-left: 5px"
          v-permission="'saleReturn_declare_detail'"
          @click="linkTo(`/saleReturn/PredeclarationDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-permission="'saleReturn_declare_edit'"
          v-if="row.status === '001'"
          @click="linkTo(`/saleReturn/PredeclarationEdit?id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'saleReturn_declare_log'"
          @click="showModal('logList', row.code)"
        >
          日志
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 日志 -->
    <LogList
      v-if="logList.visible.show"
      :visible.sync="logList.visible"
      :filterData="logList.filterData"
    ></LogList>
    <!-- 日志 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getSaleDeclarReturnList,
    delSaleDeclarReturnOrder,
    exportSaleDeclarReturnListUrl
  } from '@a/salesReturnManage'
  export default {
    mixins: [commomMix],
    components: {
      LogList: () => import('@c/logList/index.vue') // 日志组件
    },
    data() {
      return {
        // 查询条件
        searchProps: {
          code: '',
          saleReturnOrderCodes: '',
          buId: '',
          outDepotId: '',
          inDepotId: '',
          customerId: '',
          status: '',
          poNo: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '预申报单号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '关联销退订单号',
            prop: 'saleReturnOrderCodes',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'PO号',
            prop: 'poNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            prop: 'buName',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '退出仓库',
            prop: 'outDepotName',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '退入仓库',
            prop: 'inDepotName',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '客户',
            prop: 'customerName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '订单状态',
            prop: 'statusLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '接口状态',
            prop: 'apiStatusLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '130', align: 'left' }
        ],
        // 日志组件
        logList: {
          filterData: {},
          visible: { show: false }
        }
      }
    },
    mounted() {
      this.getList()
    },
    activated() {
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
          } = await getSaleDeclarReturnList({
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
      // 删除表格项
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await delSaleDeclarReturnOrder({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportSaleDeclarReturnListUrl, { ids })
          })
        } else {
          this.$exportFile(exportSaleDeclarReturnListUrl, {
            ...this.searchProps
          })
        }
      },
      // 显示日志弹窗
      showModal(type, data) {
        switch (type) {
          case 'logList':
            this.logList.filterData = { relCode: data, module: 17 }
            this.logList.visible.show = true
            break
        }
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
