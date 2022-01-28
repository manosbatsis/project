<template>
  <!-- 月库存转结页面 -->
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
              label="公司："
              prop="merchantName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.merchantName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="仓库："
              prop="depotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.depotId"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectBeanByMerchantRel('warehouseList')"
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
              label="结转月份："
              prop="settlementMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.settlementMonth"
                type="month"
                placeholder="请选择"
                style="width: 203px"
                value-format="yyyy-MM"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="结转状态："
              prop="state"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.state"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('monthlyAccount_stateList')"
              >
                <el-option
                  v-for="item in selectOpt.monthlyAccount_stateList"
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
          v-permission="'monthlyAccount_addMonthlyAccount'"
          @click="addMonthlyVisible.show = true"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'monthlyAccount_exportMonthlyAccountMap'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="info"
          v-permission="'monthlyAccount_refreshMonthlyBill'"
          @click="refreshMonthlyBill"
        >
          刷新月结账单
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
          style="padding-left: 6px"
          v-permission="'monthlyAccount_detail'"
          @click="linkTo(`/stock/monthlystockdetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-permission="'monthlyAccount_confirmMonthlySurplusNum'"
          v-if="
            row.state === '1' &&
            (row.depotType === '1' || row.depotType === '2')
          "
          @click="surplusNum(row)"
        >
          按计算库存量结转
        </el-button>
        <el-button
          type="text"
          v-permission="'monthlyAccount_updateNotSettlement'"
          v-if="row.state !== '1'"
          @click="updateNotSettlement(row.id)"
        >
          修改为未结转
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
    <!-- 新增月库存转结 -->
    <AddMonthlyStock
      v-if="addMonthlyVisible.show"
      :isVisible.sync="addMonthlyVisible"
      @comfirm="getList"
    />
    <!-- 新增月库存转结 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listMonthlyAccount,
    exportMonthlyAccountMap,
    refreshMonthlyBill,
    checkMonthlySurplusNum,
    confirmMonthlySurplusNum,
    updateNotSettlement
  } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    components: {
      // 新增月库存转结
      AddMonthlyStock: () => import('./components/AddMonthlyStock')
    },
    data() {
      return {
        // 查询参数
        searchProps: {
          merchantName: '',
          depotId: '',
          settlementMonth: '',
          state: ''
        },
        // 表格列结构
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '仓库名称',
            prop: 'depotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结转月份',
            prop: 'settlementMonth',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '期初时间',
            prop: 'firstDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '期末时间',
            prop: 'endDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '结转时间',
            prop: 'settlementDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '结转状态',
            prop: 'stateLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '120', align: 'left' }
        ],
        addMonthlyVisible: { show: false }
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
          const { data } = await listMonthlyAccount({
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
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        if (!this.tableChoseList.length) {
          this.$errorMsg('请选择一条记录！')
          return false
        }
        if (this.tableChoseList.length > 1) {
          this.$errorMsg('只能选择一条记录导出！')
          return false
        }
        this.$showToast('确定导出勾选数据？', async () => {
          const { id } = this.tableChoseList[0]
          this.$exportFile(exportMonthlyAccountMap, { id })
        })
      },
      // 刷新
      refreshMonthlyBill() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('请选择需要刷新的记录!')
          return false
        }
        const condition = this.tableChoseList.find((item) => item.state === '2')
        if (condition) {
          this.$errorMsg('已结转的数据不能重新生成月结账单!')
          return false
        }
        this.$showToast('确定刷新勾选的月结数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await refreshMonthlyBill({ ids })
            this.$successMsg('月结账单刷新成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 按计算库存量结转
      async surplusNum(row) {
        const { id, state } = row
        if (state === '2') {
          this.$errorMsg('该月份已结转!')
          return false
        }
        try {
          const { message } = await checkMonthlySurplusNum({ id })
          const msg =
            message === '操作成功' ? '校验无问题是否进行结转' : message
          this.$showToast(msg, () => {
            this.confirmMonthlySurplusNum(id)
          })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 确认按计算库存量结转
      async confirmMonthlySurplusNum(id) {
        try {
          await confirmMonthlySurplusNum({ id })
          this.$successMsg('结转成功')
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 修改为未转结
      async updateNotSettlement(id) {
        try {
          await updateNotSettlement({ id })
          this.$successMsg('修改成功')
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message)
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
