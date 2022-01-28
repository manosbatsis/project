<template>
  <div class="page-bx bgc-w">
    <JFX-Breadcrumb />
    <!-- 搜索 -->
    <JFX-search-panel @reset="resetForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
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
                :data-list="getBUSelectBean('buIdList')"
              >
                <el-option
                  v-for="item in selectOpt.buIdList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="应收月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                :clearable="false"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                type="month"
                placeholder="选择月份"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="关帐状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('receiveCloseAccounts_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.receiveCloseAccounts_statusList"
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
    <el-row class="mr-t-20"
      ><el-col :span="24">
        <el-button
          type="primary"
          v-permission="'closeAccount_flash'"
          @click="flash"
        >
          刷新
        </el-button>
      </el-col></el-row
    >
    <!-- 表格 -->
    <div class="mr-b-20"></div>
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
          @click="updateState(row.id, '0')"
          v-permission="'closeAccount_updateState'"
          v-if="row.status === '029'"
        >
          关账
        </el-button>
        <el-button
          v-permission="'closeAccount_updateState'"
          type="text"
          @click="updateState(row.id, '1')"
          v-else
        >
          反关帐
        </el-button>
        <el-button
          v-permission="'closeAccount_openLog'"
          type="text"
          @click="openLog(row)"
        >
          日志
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 日志 -->
    <log-list
      v-if="logVisible.show"
      :visible.sync="logVisible"
      :filterData="filterData"
    ></log-list>
    <!-- 日志 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listCloseAccount,
    closeAccountRefresh,
    updateAccountStatus
  } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    components: {
      logList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        searchProps: {
          buId: '',
          month: '',
          status: ''
        },
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
            align: 'center',
            hide: true,
            minWidth: 100
          },
          {
            label: '事业部',
            prop: 'buName',
            align: 'center',
            hide: true,
            minWidth: 100
          },
          {
            label: '应收月份',
            prop: 'month',
            align: 'center',
            hide: true,
            minWidth: 100
          },
          {
            label: '期初时间',
            prop: 'firstDate',
            align: 'center',
            hide: true,
            minWidth: 100
          },
          {
            label: '期末时间',
            prop: 'endDate',
            align: 'center',
            hide: true,
            minWidth: 100
          },
          {
            label: '关帐时间',
            prop: 'closeDate',
            align: 'center',
            hide: true,
            minWidth: 100
          },

          {
            label: '关帐状态',
            prop: 'statusLabel',
            align: 'center',
            hide: true,
            minWidth: 100
          },
          {
            label: '操作',
            slotTo: 'action',
            align: 'center',
            hide: true
          }
        ],
        logVisible: { show: false },
        filterData: {}
      }
    },
    methods: {
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listCloseAccount({
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
      // 更
      async updateState(id, state) {
        try {
          await updateAccountStatus({ id, operationType: state })
          this.$successMsg('操作成功')
          this.getList()
        } catch (error) {
          console.log(error)
        }
      },
      //  打开日志
      openLog(row) {
        this.filterData = { relCode: row.id, module: 20 }
        this.logVisible.show = true
      },
      // 刷新
      async flash(row) {
        if (!this.searchProps.month) {
          return this.$warningMsg('请先选择应收月份')
        }
        await closeAccountRefresh({ month: this.searchProps.month })
        this.$successMsg('正在刷新')
        this.getList()
      },
      // 重置
      resetForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    },
    mounted() {
      this.getList()
    }
  }
</script>

<style></style>
