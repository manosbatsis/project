<template>
  <!-- 财务进销存关账页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetSearchForm"
      @search="getList(1)"
      class="mr-b-20"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="归属月："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                type="month"
                value-format="yyyy-MM"
                placeholder="选择日期时间"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="帐表状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('financeInventorySummary_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.financeInventorySummary_statusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="firstDay">
        {{ getDay('firstDay') }}
      </template>
      <template slot="lastDay">
        {{ getDay('lastDay') }}
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'financeClose_detail'"
          @click="jumpToDetail(row.month, row.buId)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-if="row.status === '029'"
          v-permission="'financeClose_close'"
          @click="updateNotClose('1', row)"
        >
          关账
        </el-button>
        <el-button
          type="text"
          v-if="row.status === '030'"
          v-permission="'financeClose_updateNotClose'"
          @click="updateNotClose('2', row)"
        >
          反关账
        </el-button>
        <el-button
          type="text"
          v-permission="'financeClose_detail'"
          @click="showLogList(row)"
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
      type="report"
    ></LogList>
    <!-- 日志 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  import { listFnanceClose, updateNotClose, updateClose } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    components: {
      // 日志组件
      LogList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          month: '',
          status: '',
          buId: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
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
            label: '归属月份',
            prop: 'month',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '期初时间',
            slotTo: 'firstDay',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '期末时间',
            slotTo: 'lastDay',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '关账时间',
            prop: 'closeDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '账表状态',
            prop: 'statusLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '140', align: 'left' }
        ],
        // 日志组件
        logList: {
          filterData: {},
          visible: { show: false }
        }
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.getList()
    },
    computed: {
      // 获取当前月的第一天和最后一天
      getDay() {
        return (type) =>
          ({
            firstDay: dayjs().startOf('month').format('YYYY-MM-DD'),
            lastDay: dayjs().endOf('month').format('YYYY-MM-DD')
          }[type] || '')
      }
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listFnanceClose({
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
      // 关账/反关账
      updateNotClose(type, row) {
        const { month, buId } = row
        if (!month) {
          this.$errorMsg('月份不能为空')
          return false
        }
        if (!buId) {
          this.$errorMsg('事业部不能为空')
          return false
        }
        const message = {
          1: '关帐',
          2: '反关帐'
        }
        this.$showToast(`确定${message[type]}吗?`, async () => {
          try {
            if (type === '1') {
              await updateClose({ month, buId })
            } else {
              await updateNotClose({ month, buId })
            }
            this.$successMsg(`${message[type]}成功`)
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 显示日志
      showLogList(row) {
        const merchantId = row.merchantId + ''
        const buId = row.buId + ''
        const month = row.month + ''
        const relCode = `${merchantId},${buId},${month}`
        this.logList.filterData = { relCode, module: 1 }
        this.logList.visible.show = true
      },
      // 跳转到事业部财务进销存
      jumpToDetail(month, buId) {
        this.linkTo({
          name: 'BusinessFinancial',
          params: { month, buId }
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
