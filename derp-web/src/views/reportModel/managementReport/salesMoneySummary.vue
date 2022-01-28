<template>
  <!-- 财务进销存关账页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="reset('searchForm')" @search="getList(1)">
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
              label="部门："
              prop="departmentIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.departmentIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
                @change="departChange"
              >
                <el-option
                  v-for="item in selectOpt.departList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="母品牌："
              prop="parentBrandIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.parentBrandIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
                :data-list="getBrandList('brandList')"
              >
                <el-option
                  v-for="item in selectOpt.brandList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="子品牌："
              prop="brandParentIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.brandParentIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
                :data-list="getBrandParent('sonBrandList')"
              >
                <el-option
                  v-for="item in selectOpt.sonBrandList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="项目组："
              prop="buIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.buIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
              >
                <el-option
                  v-for="item in selectOpt.buList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="公司："
              prop="merchantIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.merchantIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
              >
                <el-option
                  v-for="item in selectOpt.merchantList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="渠道类型："
              prop="channelTypes"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.channelTypes"
                placeholder="请选择"
                filterable
                clearable
                multiple
                :data-list="getSelectList('sale_data_channelTypeList')"
              >
                <el-option
                  v-for="item in selectOpt.sale_data_channelTypeList"
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
              prop="customerIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
              >
                <el-option
                  v-for="item in selectOpt.customerList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="仓库："
              prop="outDepotIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.outDepotIds"
                placeholder="请选择"
                clearable
                filterable
                multiple
                collapse-tags
              >
                <el-option
                  v-for="item in selectOpt.depotList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="TO C平台："
              prop="storePlatformCodeList"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.storePlatformCodeList"
                placeholder="请选择"
                filterable
                clearable
                multiple
                collapse-tags
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-10">
      <el-col :span="17">
        <el-button
          type="primary"
          v-permission="'salesMoneySummaryExport'"
          @click="exportTalbe"
        >
          导出
        </el-button>
      </el-col>
      <el-col
        :span="4"
        style="
          border-left: 2px solid #0000ff;
          padding-left: 8px;
          position: relative;
          top: 5px;
          font-size: 12px;
          color: #666666;
        "
      >
        币种：HKD
      </el-col>
      <el-col :span="3"></el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <el-row v-loading="tableData.loading">
      <el-col :span="21">
        <JFX-table
          :tableColumn="tableColumn"
          :tableData.sync="tableData"
          @selection-change="selectionChange"
          :sortChange="sortChange"
          :showPagination="false"
          :span-method="tableSpanMethod"
          @change="getList"
          :isStickyOpen="true"
          class="mr-t-20"
        >
          <template slot="no" slot-scope="{ row }">
            {{ row.departmentSumFlag ? row.departmentSumTitle : row.no }}
          </template>
        </JFX-table>
      </el-col>
      <el-col :span="3" style="padding: 20px 0 20px 20px">
        <div style="border: 1px solid #dcdfe6; padding: 2px">
          <div class="clr-I fs-14" style="padding: 5px 0 0 8px">分组选项：</div>
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            border
            @change="handleCheckAllChange"
            class="sideCheckItem"
            style="margin-right: 10px"
          >
            全选
          </el-checkbox>
          <el-checkbox-group
            v-model="checkedList"
            @change="handleCheckedCitiesChange"
          >
            <el-checkbox
              class="sideCheckItem"
              border
              v-for="item in checkList"
              :label="item.label"
              :key="item.key"
            >
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </el-col>
    </el-row>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  import {
    getDepartmentSalesAmountStatistic,
    exportDepartmentSalesAmountStatistic,
    getMerchantList,
    getDepartmentSelectList,
    getBuList,
    getCustomerSelectList,
    getDepotSelectList
  } from '@a/reportManage'
  const checkList = [
    { label: '母品牌', key: 'groupByParentBrandFlag' },
    { label: '子品牌', key: 'groupByBrandParentFlag' },
    { label: '项目组', key: 'groupByBuIdFlag' },
    { label: '公司', key: 'groupByMerchantFlag' },
    { label: '渠道类型', key: 'groupByChannelTypeFlag' },
    { label: '客户名称', key: 'groupByCustomerFlag' },
    { label: '仓库', key: 'groupByDepotFlag' },
    { label: 'TO C平台', key: 'groupByPlatformFlag' }
  ]
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: dayjs().format('YYYY-MM'),
          departmentIds: '',
          parentBrandIds: '',
          brandParentIds: '',
          buIds: '',
          merchantIds: '',
          outDepotIds: '',
          customerIds: '',
          channelTypes: '',
          storePlatformCodeList: '',
          orderByAmount1: 'DESC',
          orderByAmount2: 'DESC',
          orderByAmount3: 'DESC'
        },
        // 表格数据
        tableData: {
          list: [],
          loading: false
        },
        // 表头字段
        tableColumn: [],
        baseColumn: [
          {
            label: '序号',
            slotTo: 'no',
            width: '50',
            align: 'center'
          },
          {
            label: '部门',
            prop: 'departmentName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '母品牌',
            prop: 'parentBrandName',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByParentBrandFlag'
          },
          {
            label: '子品牌',
            prop: 'brandParent',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByBrandParentFlag'
          },
          {
            label: '项目组',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByBuIdFlag'
          },
          {
            label: '公司名称',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByMerchantFlag'
          },
          {
            label: '渠道类型',
            prop: 'channelType',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByChannelTypeFlag'
          },
          {
            label: '客户',
            prop: 'customerName',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByCustomerFlag'
          },
          {
            label: '仓库',
            prop: 'outDepotName',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByDepotFlag'
          },
          {
            label: '平台',
            prop: 'storePlatformName',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByPlatformFlag'
          },
          {
            label: '月',
            prop: 'monthAmount1',
            minWidth: '120',
            align: 'right',
            hide: true,
            sort: 'custom'
          },
          {
            label: '月',
            prop: 'monthAmount2',
            minWidth: '120',
            align: 'right',
            hide: true,
            sort: 'custom'
          },
          {
            label: '月',
            prop: 'monthAmount3',
            minWidth: '120',
            align: 'right',
            hide: true,
            sort: 'custom'
          }
        ],
        // 侧边栏勾选
        checkAll: false,
        checkedList: ['母品牌'],
        checkList: checkList,
        isIndeterminate: true
      }
    },
    async mounted() {
      this.tableData.loading = true
      let [{ data: departResList }, { data: buList }, { data: mList }] =
        await Promise.all([
          getDepartmentSelectList(),
          getBuList(),
          getMerchantList()
        ])
      // 部门
      this.$set(this.selectOpt, 'departList', departResList)
      this.searchProps.departmentIds = departResList.map((item) => item.value)
      //  项目组
      this.$set(this.selectOpt, 'buList', buList)
      this.searchProps.buIds = buList.map((item) => item.id)
      // // 公司
      mList = mList.filter((item) => item !== null)
      this.$set(this.selectOpt, 'merchantList', mList)
      this.searchProps.merchantIds = mList.map((item) => item.id)

      this.getList()

      // 客户
      const { data: customerRes } = await getCustomerSelectList()
      this.$set(this.selectOpt, 'customerList', customerRes)

      // 仓库
      const { data: depotRes } = await getDepotSelectList()
      this.$set(this.selectOpt, 'depotList', depotRes)
    },
    methods: {
      // 拉取表格数据
      async getList() {
        this.tableData.loading = true
        const params = {
          ...this.searchProps
        }
        for (const key in params) {
          if (params[key] instanceof Array) {
            params[key] = params[key].join(',')
          }
        }
        this.checkedList.forEach((label) => {
          const item = checkList.find((sItem) => sItem.label === label)
          params[item.key] = true
        })
        // 字段隐藏
        this.tableColumn = this.baseColumn.filter(
          (item) => item.vifFlag === undefined || params[item.vifFlag]
        )
        // 字段文案调整
        const monField = this.getMonthFieldList(params.month)
        const tableColumn = this.tableColumn
        tableColumn[tableColumn.length - 1].label = monField.monthAmount3
        tableColumn[tableColumn.length - 2].label = monField.monthAmount2
        tableColumn[tableColumn.length - 3].label = monField.monthAmount1
        console.log(params)
        let { data } = await getDepartmentSalesAmountStatistic(params)
        data = data || []
        this.tableData.list = data.map((item) => {
          const formatNumber = (number) => {
            if ([null, undefined, ''].includes(number)) {
              return ''
            }
            return Number(number)
              .toFixed(2)
              .replace(/(\d)(?=(\d{3})+\.)/g, '$1,')
          }
          item.monthAmount3 = formatNumber(item.monthAmount3)
          item.monthAmount1 = formatNumber(item.monthAmount1)
          item.monthAmount2 = formatNumber(item.monthAmount2)
          return item
        })
        this.tableData.loading = false
      },
      // 部门改变
      async departChange() {
        const { departmentIds } = this.searchProps
        console.log(departmentIds)
        const { data: buList } = await getBuList({
          departmentIds: departmentIds.join(',')
        })
        this.$set(this.selectOpt, 'buList', buList)
        this.searchProps.buIds = []
      },
      //  单元格合并
      tableSpanMethod({ row, column, rowIndex, columnIndex }) {
        if (columnIndex === 0) {
          if (row.departmentSumFlag) {
            return [1, 2 + this.checkedList.length]
          }
        } else if (columnIndex < 2 + this.checkedList.length) {
          // } else if ([1, 2].includes(columnIndex)) {
          if (row.departmentSumFlag) {
            return [0, 0]
          }
        }
      },
      handleCheckAllChange(val) {
        this.checkedList = val ? checkList.map((item) => item.label) : []
        this.isIndeterminate = false
        this.getList()
      },
      handleCheckedCitiesChange(value) {
        const checkedCount = value.length
        this.checkAll = checkedCount === checkList.length
        this.isIndeterminate =
          checkedCount > 0 && checkedCount < checkList.length
        this.getList()
      },
      /**
       * 根据年月获取 月份字符串list [6月，7月，8月]
       * @param timeStr 传入的时间 yyyy-MM 格式
       */
      getMonthFieldList(timeStr) {
        return {
          monthAmount1: dayjs(timeStr).format('MM') + '月销售金额（万）',
          monthAmount2:
            dayjs(timeStr).subtract(1, 'month').format('MM') +
            '月销售金额（万）',
          monthAmount3:
            dayjs(timeStr).subtract(2, 'month').format('MM') +
            '月销售金额（万）'
        }
      },
      // 排序改变
      sortChange({ column, prop, order }) {
        console.log(column, prop, order)
        const that = this
        that.searchProps.orderByAmount1 = ''
        that.searchProps.orderByAmount2 = ''
        that.searchProps.orderByAmount3 = ''
        const props = {
          monthAmount1() {
            that.searchProps.orderByAmount1 =
              order === 'ascending' ? 'ASC' : 'DESC'
          },
          monthAmount2() {
            that.searchProps.orderByAmount2 =
              order === 'ascending' ? 'ASC' : 'DESC'
          },
          monthAmount3() {
            that.searchProps.orderByAmount3 =
              order === 'ascending' ? 'ASC' : 'DESC'
          }
        }
        props[prop]()
        this.getList()
      },
      // 导出
      exportTalbe() {
        if (this.tableData.list < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.getList()
        const params = {
          ...this.searchProps
        }
        this.checkedList.forEach((label) => {
          const item = checkList.find((sItem) => sItem.label === label)
          params[item.key] = true
        })
        console.log(params)
        this.$exportFile(exportDepartmentSalesAmountStatistic, { ...params })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .sideCheckItem {
    display: block;
    margin: 10px;
  }
</style>
