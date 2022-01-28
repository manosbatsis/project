<template>
  <!-- 部门库存统计页面 -->
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
              label="库存日期："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                :clearable="false"
                :format="'yyyy-MM-dd'"
                :value-format="'yyyy-MM-dd'"
                type="date"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-10">
      <el-col :span="17">
        <el-button
          type="primary"
          v-permission="'DepartmentStockSummaryExport'"
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
          :showPagination="false"
          :span-method="tableSpanMethod"
          @change="getList"
          :isStickyOpen="true"
          class="mr-t-20"
        >
          <template slot="no" slot-scope="{ row }">
            {{ row.departmentSumFlag ? row.departmentSumTitle : row.no }}
          </template>
          <template slot="num" slot-scope="{ row }">
            {{ row.num ? numberFormat(row.num) : 0 }}
          </template>
          <template slot="inventoryAmount" slot-scope="{ row }">
            {{ row.inventoryAmount ? numberFormat(row.inventoryAmount) : 0 }}
          </template>
          <!-- <template slot="amountRate" slot-scope="{ row }">
            {{ row.amountRate ? numberFormat(row.amountRate) + '%' : '0%' }}
          </template> -->
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
  import { numberFormat } from '@u/tool'
  import {
    getDepartmentInventoryStatistic,
    exportDepartmentInventoryStatistic,
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
    { label: '仓库', key: 'groupByDepotFlag' },
    { label: '效期区间', key: 'groupByEffectiveInterval' }
  ]
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: dayjs().format('YYYY-MM-DD'),
          departmentIds: '',
          parentBrandIds: '',
          brandParentIds: '',
          buIds: '',
          merchantIds: '',
          outDepotIds: ''
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
            minWidth: '150',
            align: 'center',
            hide: true,
            vifFlag: 'groupByParentBrandFlag'
          },
          {
            label: '子品牌',
            prop: 'brandParent',
            minWidth: '150',
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
            label: '仓库',
            prop: 'depotName',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByDepotFlag'
          },
          {
            label: '效期区间',
            prop: 'effectiveIntervalLabel',
            minWidth: '120',
            align: 'center',
            hide: true,
            vifFlag: 'groupByEffectiveInterval'
          },
          {
            label: '在库库存数量（件）',
            slotTo: 'num',
            minWidth: '120',
            align: 'right',
            hide: true
          },
          {
            label: '在库库存金额（万）',
            slotTo: 'inventoryAmount',
            minWidth: '120',
            align: 'right',
            hide: true
          },
          {
            label: '金额占比',
            prop: 'amountRateLabel',
            minWidth: '120',
            align: 'right',
            hide: true
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
      // 公司
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
      numberFormat,
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
        const { data } = await getDepartmentInventoryStatistic(params)
        this.tableData.list = data || []
        this.tableData.loading = false
      },
      // 部门改变
      async departChange() {
        const { departmentIds } = this.searchProps
        const { data: buList } = await getBuList({
          departmentIds: departmentIds.join(',')
        })
        this.$set(this.selectOpt, 'buList', buList)
        this.searchProps.buIds = []
      },
      //  单元格合并
      tableSpanMethod({ row, columnIndex }) {
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
        this.$exportFile(exportDepartmentInventoryStatistic, { ...params })
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
<style lang="scss" scoped>
  .sideCheckItem {
    display: block;
    margin: 10px;
  }
</style>
