<template>
  <!-- 自动校验任务列表页面 -->
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
              label="任务类型："
              prop="taskType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.taskType"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('automaticCheckTask_taskTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.automaticCheckTask_taskTypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="创建人："
              prop="createName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.createName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="核对结果："
              prop="checkResult"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.checkResult"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('automaticCheckTask_checkResultList')"
              >
                <el-option
                  v-for="item in selectOpt.automaticCheckTask_checkResultList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="处理状态："
              prop="state"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.state"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('automaticCheckTask_stateList')"
              >
                <el-option
                  v-for="item in selectOpt.automaticCheckTask_stateList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="创建日期："
              prop="createDate"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.createDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="出仓仓库："
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
                @change="storePlatformCodeChange"
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
              label="店铺："
              prop="shopCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.shopCode"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in shopList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="任务编码："
              prop="taskCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.taskCode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item label="核对日期：" prop="date">
              <el-date-picker
                v-model="date"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
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
          v-permission="'POPCheckTask_import'"
          @click="linkTo('/automatic/addchecktask')"
        >
          创建POP核对任务
        </el-button>
        <el-button
          type="primary"
          v-permission="'POPAmountCheckTask_import'"
          @click="linkTo('/automatic/AddCheckMoneyTask')"
        >
          创建POP金额核对任务
        </el-button>
        <el-button
          type="primary"
          v-permission="'depotCheckTask_import'"
          @click="linkTo('/automatic/addwarehousetask')"
        >
          创建仓库核对任务
        </el-button>
        <el-button
          type="primary"
          v-permission="'POPCheckTask_import'"
          @click="showMarkupModal"
        >
          标记
        </el-button>
        <el-button
          type="primary"
          v-permission="'POPCheckTask_del'"
          @click="deleteTableItem"
        >
          删除
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      ref="table"
      :isHideCellBorder="true"
      :tableData.sync="tableData"
      :stripe="false"
      :row-style="rootRowStyle"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column
        type="selection"
        align="center"
        width="55"
      ></el-table-column>
      <el-table-column
        prop="taskCode"
        align="center"
        label="任务编码"
        min-width="120"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        prop="taskTypeLabel"
        align="center"
        label="任务类型"
        width="150"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        prop="checkEndDate"
        align="center"
        label="核对日期"
        min-width="120"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{
            row.checkEndDate && row.checkStartDate
              ? row.checkStartDate.slice(0, -2) +
                ' - ' +
                row.checkEndDate.slice(0, -2)
              : ''
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createName"
        align="center"
        label="创建人"
        width="150"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="createDate"
        align="center"
        label="创建时间"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="checkResultLabel"
        align="center"
        label="核对结果"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="stateLabel"
        align="center"
        label="处理状态"
        width="100"
      ></el-table-column>
      <el-table-column align="left" width="110" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            v-if="row.state === '3'"
            type="text"
            v-permission="'automaticCheckTask_export'"
            @click="exportItem(row.id)"
          >
            导出
          </el-button>
          <el-button
            v-if="row.state === '3'"
            type="text"
            @click="handleExpand(row)"
          >
            {{ row.show ? '收起' : '展开' }}
            <i :class="row.show ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" />
          </el-button>
        </template>
      </el-table-column>
      <el-table-column type="expand" label="展开" width="1">
        <template slot-scope="{ row }">
          <div class="inner-container">
            <el-table
              ref="subTable"
              class="inner-table"
              v-loading="row.loading"
              :data="row.children"
              :header-cell-style="{ background: '#e4e8ed' }"
              :border="false"
              stripe
            >
              <el-table-column
                prop="dataSourceLabel"
                align="center"
                min-width="120"
                label="数据源"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="outDepotName"
                align="center"
                label="核对仓库"
                min-width="140"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="storePlatformLabel"
                align="center"
                min-width="80"
                label="平台"
              ></el-table-column>
              <el-table-column
                prop="shopName"
                align="center"
                min-width="80"
                label="店铺"
              ></el-table-column>
              <el-table-column
                prop="remark"
                align="center"
                min-width="80"
                label="备注"
              ></el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
    <MarkCheckResults
      v-if="markCheckResults.visible.show"
      :isVisible.sync="markCheckResults.visible"
      :data="markCheckResults.data"
      @comfirm="getList"
    />
  </div>
</template>

<script>
  import {
    getTaskShopList,
    listAutomaticCheckTask,
    changeShopCodeLabel,
    exportAutomaticCheckTask,
    listTaskDetailsById,
    delAutomaticCheckTask
  } from '@a/reportManage'
  import commomMix from '@m/index'
  import MarkCheckResults from './components/MarkCheckResults'
  export default {
    mixins: [commomMix],
    components: {
      MarkCheckResults
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          taskType: '',
          createName: '',
          checkResult: '',
          state: '',
          createDate: '',
          outDepotId: '',
          storePlatformCode: '',
          shopCode: '',
          taskCode: ''
        },
        markCheckResults: {
          visible: { show: false },
          data: {}
        },
        date: [], // 核对日期
        shopList: [] // 店铺列表
      }
    },
    activated() {
      this.getShopList() // 获取店铺列表
      this.getList() // 获取表格数据
    },
    mounted() {
      this.getShopList() // 获取店铺列表
      this.getList() // 获取表格数据
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        // 订单日期
        this.searchProps.checkStartDate =
          this.date && this.date.length ? this.date[0] : ''
        this.searchProps.checkEndDate =
          this.date && this.date.length ? this.date[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listAutomaticCheckTask({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
          this.tableData.list = this.tableData.list.map((item) => ({
            ...item,
            show: false,
            loading: false,
            children: []
          }))
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 获取店铺列表
      async getShopList() {
        try {
          const { data } = await getTaskShopList()
          if (data && data.length) {
            this.shopList = data.map(({ shopCode, shopName }) => ({
              key: shopCode,
              value: shopName
            }))
          } else {
            this.shopList = []
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 平台名称改变
      async storePlatformCodeChange(storePlatformCode) {
        this.searchProps.shopCode = ''
        if (!storePlatformCode) {
          this.getShopList()
          return false
        }
        try {
          const {
            data: { shopList }
          } = await changeShopCodeLabel({ storePlatformCode })
          if (shopList && shopList.length) {
            this.shopList = shopList.map(({ shopCode, shopName }) => ({
              key: shopCode,
              value: shopName
            }))
          } else {
            this.shopList = []
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 导出
      exportItem(id) {
        this.$exportFile(exportAutomaticCheckTask, { id })
      },
      // 点击展开表格行
      async handleExpand(row) {
        const $table = this.$refs.table.$refs['el-table']
        this.tableData.list.forEach((item) => {
          if (row.id !== item.id) {
            item.show = false
            $table.toggleRowExpansion(item, false)
          }
        })
        // 当前点击的行
        row.show = !row.show
        $table.toggleRowExpansion(row, row.show)
        if (row.id) {
          try {
            row.loading = true
            const { data } = await listTaskDetailsById({ id: row.id })
            row.children = [{ ...data }]
          } catch (error) {
            this.$errorMsg(error.message)
          } finally {
            row.loading = false
          }
        }
      },
      // 显示标记组件
      showMarkupModal() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        const condition = this.tableChoseList.find((item) => item.state !== '3')
        if (condition) {
          this.$alertWarning(
            `任务编码为:${condition.taskCode}处理状态不是已完成`
          )
          return false
        }
        const target = this.tableChoseList.find((item) => item.isMark !== '0')
        if (target) {
          this.$alertWarning(`任务编码为:${target.taskCode}已标记过`)
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.markCheckResults.data = { ids }
        this.markCheckResults.visible.show = true
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
            await delAutomaticCheckTask({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      rootRowStyle({ row }) {
        if (row.show) {
          return { background: '#f0f7ff' }
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.date = []
          this.getList(1)
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  // 隐藏箭头
  ::v-deep.el-table__expand-column .cell {
    display: none;
  }

  ::v-deep.jfx-table .el-table__expanded-cell {
    padding: 0 !important;
  }

  // 展开内容的div
  ::v-deep.inner-container {
    padding: 20px 100px 30px 55px !important;
    background: #f0f7ff !important;

    // 嵌套表格
    ::v-deep.inner-table {
      border: 1px solid rgb(222, 231, 245);
    }

    .inner-table td,
    .inner-table th,
    .is-leaf {
      border-left: none;
      border-right: none;
      border-top: none;
    }
  }
</style>
