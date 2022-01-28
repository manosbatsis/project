<template>
  <!-- PO完结组件 -->
  <div class="page-bx bgc-w" style="padding-top: 0">
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm', getList)"
      @search="getList(1)"
      style="margin-top: 0"
    >
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
              label="统计时间："
              prop="month"
              class="search-panel-item fs-14 clr-II timeTypeFormItem"
            >
              <div style="width: 70px; flex-shrink: 0" class="timeTypeSelect">
                <el-select
                  v-model="timeType"
                  style="width: 100%"
                  @change="timeTimeChange"
                >
                  <el-option
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                    v-for="(item, index) in [
                      { label: '月份', value: 'month' },
                      { label: '季度', value: 'season' },
                      { label: '年份', value: 'year' }
                    ]"
                  ></el-option>
                </el-select>
              </div>

              <el-date-picker
                v-if="timeType === 'month'"
                v-model="searchProps.month"
                type="month"
                value-format="yyyy-MM"
                placeholder="选择日期时间"
              ></el-date-picker>
              <el-date-picker
                v-if="timeType === 'year'"
                v-model="searchProps.year"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="选择日期时间"
              ></el-date-picker>
              <div style="flex-grow: 1" v-if="timeType === 'season'">
                <ElQuarterPicker
                  v-model="searchProps.season"
                  format="yyyy-第q季度"
                  value-format="yyyy-第q季度"
                  placeholder="请选择季度"
                />
              </div>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品名称："
              prop="goodsName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.goodsName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准条码："
              prop="commbarcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.commbarcode"
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
          @click="exportList"
          v-permission="'saleTargetAchievement_export'"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          @click="flashList"
          v-permission="'saleTargetAchievement_flash'"
        >
          刷新
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
      class="mr-t-20"
    >
      <el-table-column
        prop="buName"
        align="center"
        min-width="80"
        label="事业部"
      />
      <el-table-column
        prop="month"
        align="center"
        min-width="80"
        label="归属月份"
      />
      <el-table-column
        prop="commbarcode"
        align="center"
        min-width="80"
        label="标准条码"
      />
      <el-table-column
        prop="goodsName"
        align="center"
        min-width="200"
        label="商品名称"
      />
      <el-table-column
        prop="brandParent"
        align="center"
        min-width="80"
        label="标准品牌"
      />
      <el-table-column
        align="center"
        min-width="120"
        v-for="(item, index) in expandList"
        :key="index"
        :label="`${item} / 完成率`"
      >
        <template slot-scope="{ row }">
          {{
            `${row[item + '_num']} / ${(row[item + '_rate'] * 100).toFixed(2)}%`
          }}
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  import {
    listSaleTargetAchievement,
    exportAmountList,
    flashSaleTargetAchievement,
    saleTargetAchievementtoDsPage
  } from '@a/reportManage/index'
  export default {
    mixins: [commomMix],
    components: {
      ElQuarterPicker: () => import('@c/ElQuarterPicker/index')
    },
    data() {
      return {
        timeType: 'month',
        // 查询数据
        searchProps: {
          buId: '',
          month: dayjs().format('YYYY-MM'),
          year: '',
          season: '',
          goodsName: '',
          commbarcode: '',
          type: 2
        },
        // 表格数据
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10
          // total: 11
        },
        expandList: []
      }
    },
    async mounted() {
      this.getList()
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.loading = true
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          // 表头刷新
          const { data: expandList } = await saleTargetAchievementtoDsPage()
          this.expandList = expandList
          const {
            data: { list, total }
          } = await listSaleTargetAchievement({
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
      exportList() {
        this.$exportFile(exportAmountList, { ...this.searchProps })
      },
      flashList() {
        this.$showToast('确定刷新数据？', async () => {
          try {
            await flashSaleTargetAchievement({ ...this.searchProps })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      timeTimeChange() {
        this.searchProps.year = ''
        this.searchProps.month = ''
        this.searchProps.season = ''
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.timeTypeFormItem .el-form-item__content {
    display: flex;
  }
</style>
