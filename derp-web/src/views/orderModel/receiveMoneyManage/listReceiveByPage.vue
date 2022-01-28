<template>
  <!-- 月结快报 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetSearchForm"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="月结月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                :clearable="false"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                type="month"
                placeholder="选择日期时间"
              ></el-date-picker>
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
                  v-for="item in shopInfoList"
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
                clearable
                filterable
                :data-list="getCustomerSelectBean('customerList')"
              >
                <el-option
                  v-for="item in selectOpt.customerList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="运营类型："
              prop="shopTypeCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.shopTypeCode"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('merchantShopRel_shopTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.merchantShopRel_shopTypeList"
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
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'listReceiveByPage_flash'"
          @click="refresh"
        >
          刷新
        </el-button>
        <el-button
          type="primary"
          v-permission="'listReceiveByPage_export'"
          @click="exportExcel"
        >
          月结导出
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="shopTypeName" slot-scope="{ row }">
        {{ row.shopTypeName }}
        <br />
        {{ row.customerName }}
      </template>
      <template slot="storePlatformName" slot-scope="{ row }">
        {{ row.storePlatformName }}
        <br />
        {{ row.shopName }}
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listReceiveByPage,
    tocTempReceiveBillMonthlyRefresh,
    exportReceiveMonthly
  } from '@a/receiveMoneyManage/index'
  import { getShopInfoList } from '@a/salesManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: '',
          storePlatformCode: '',
          buId: '',
          shopCode: '',
          shopTypeCode: '',
          customerId: ''
        },
        shopInfoList: [],
        // 表格列数据
        tableColumn: [
          {
            label: '月份',
            prop: 'month',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '运营类型',
            slotTo: 'shopTypeName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            prop: 'buName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '平台',
            slotTo: 'storePlatformName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '外部订单号',
            prop: 'externalCode',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '系统订单号',
            prop: 'orderCode',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '母品牌',
            prop: 'parentBrandName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '单据类型',
            prop: 'orderTypeLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '暂估应收金额（RMB）',
            prop: 'temporaryRmbAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '平台结算金额（RMB）',
            prop: 'settlementRmbAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '冲销金额（RMB）',
            prop: 'writeOffAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '差异调整金额（RMB）',
            prop: 'adjustmentRmbAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '未核销金额（RMB）',
            prop: 'lastReceiveAmount',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '结算单号',
            prop: 'settlementCode',
            width: '150',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    activated() {
      this.getList()
      this.getShopInfoList()
    },
    mounted() {
      this.getList()
      this.getShopInfoList()
    },
    methods: {
      // 获取店铺列表
      async getShopInfoList() {
        try {
          const { data = [] } = await getShopInfoList()
          const tempList = data.map((item) => ({
            key: item.shopCode,
            value: item.shopName
          }))
          await this.$nextTick()
          this.shopInfoList = tempList
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listReceiveByPage({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list
          this.tableData.total = total
          return Promise.resolve()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      },
      // 刷新
      async refresh() {
        if (!this.searchProps.month) {
          return this.$warningMsg('请先选择月结月份')
        }
        await tocTempReceiveBillMonthlyRefresh({
          month: this.searchProps.month,
          orderType: '1' // orderType 0费用 1收入
        })
        this.$successMsg('刷新成功')
        this.getList()
      },
      // 导出excel
      async exportExcel() {
        if (!this.searchProps.month) {
          return this.$warningMsg('请先选择月结月份')
        }
        const exportJson = { ...this.searchProps }
        await exportReceiveMonthly(exportJson)
        this.$alertSuccess('请在任务列表查看进度')
      }
    }
  }
</script>
