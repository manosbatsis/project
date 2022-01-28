<template>
  <!-- 销售订单列表页面 -->
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
              label="试单流水编号："
              prop="oaBillCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.oaBillCode"
                placeholder="请选择"
                filterable
                clearable
              >
                <el-option
                  v-for="item in tryApplyCodeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="供应商名称："
              prop="supplierId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.supplierId"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSupplierList('goongyingList')"
              >
                <el-option
                  v-for="item in selectOpt.goongyingList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="申请人："
              prop="createrName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.createrName"
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
          v-permission="'purchaseTrial_export'"
          @click="exportList"
        >
          导出
        </el-button>
        <span style="color: red; margin-left: 10px">
          刷新日期：{{ refreshTime }}
        </span>
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
          v-permission="'purchaseTrial_detail'"
          @click="linkTo(`/purchase/PurchaseTrialOrderDetail?id=${row.id}`)"
        >
          详情
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    purchaseTryApplyOrderList,
    exportPurchaseTryApplyOrder,
    purchaseTryApplyOrderRefreshTime,
    listOaBillCodeSelect
  } from '@a/purchaseManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        /* 查询数据 */
        searchProps: {
          oaBillCode: '',
          supplierId: '',
          createrName: ''
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '试单流水编号',
            prop: 'oaBillCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '我司签约主体',
            prop: 'merchantName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '供应商名称',
            prop: 'supplierName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '供应商类型',
            prop: 'supplierTypeLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '合同类型',
            prop: 'contractTypeLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '项目名称',
            prop: 'projectName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '申请人',
            prop: 'createrName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '业务部门',
            prop: 'businessDept',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'appStateLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '80',
            align: 'center'
          }
        ],
        /* 刷新时间 */
        refreshTime: '',
        /* 采购试单下拉列表 */
        tryApplyCodeList: []
      }
    },
    mounted() {
      /* 获取采购试单申请编号列表 */
      this.getOaBillCodeSelect()
      /* 获取刷新时间 */
      this.getRefreshTime()
      this.getList()
    },
    methods: {
      /* 获取表格数据 */
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await purchaseTryApplyOrderList({
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
      /* 获取采购试单申请编号列表 */
      async getOaBillCodeSelect() {
        try {
          const { data } = await listOaBillCodeSelect({
            counterpartContSignComy: '',
            allStatusFlag: true
          })
          if (!data?.length) return false
          this.tryApplyCodeList = data.map((item) => ({
            label: item.oaBillCode,
            key: item.oaBillCode
          }))
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      /* 获取刷新时间 */
      async getRefreshTime() {
        try {
          const { data } = await purchaseTryApplyOrderRefreshTime()
          this.refreshTime = data
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
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportPurchaseTryApplyOrder, { ids })
          })
          return false
        }
        this.$exportFile(exportPurchaseTryApplyOrder, { ...this.searchProps })
      },
      /* 重置搜索表单 */
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
