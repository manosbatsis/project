<template>
  <!-- 事业部财务进销存页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="年月：" class="search-panel-item fs-14 clr-II">
              <el-date-picker
                v-model="searchProps.month"
                placeholder="请选择"
                type="month"
                value-format="yyyy-MM"
                :clearable="false"
              ></el-date-picker>
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
              label="二级分类："
              prop="typeId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.typeId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getMerchandiseCatList('category_list', { level: 1 })
                "
              >
                <el-option
                  v-for="item in selectOpt.category_list"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="条码："
              prop="barcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.barcode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准品牌："
              prop="brandIds"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="brandIds"
                placeholder="请选择"
                filterable
                multiple
                clearable
                collapse-tags
                :data-list="getBrandParent('brandList')"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'buFinance_createTask'"
          v-loading="excelBtnLoading"
          @click="exportList('excel')"
        >
          生成excel
        </el-button>
        <el-button
          type="primary"
          v-permission="'buFinance_createSdTask'"
          v-loading="sdTaskBtnLoading"
          @click="exportList('sdTask')"
        >
          SD进销存导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'buFinance_createAllAccountTask'"
          v-loading="allTaskBtnLoading"
          @click="exportList('allTask')"
        >
          总账导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'buFinance_createTempEstimatePdfTask'"
          v-loading="pdfTaskBtnLoading"
          @click="exportList('pdfTask')"
        >
          暂估PDF导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'buFinance_createInterestTask'"
          v-loading="interestTaskBtnLoading"
          @click="exportList('interestTask')"
        >
          利息进销存导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'buFinance_buFlashInventorSummary'"
          v-loading="flashBtnLoading"
          @click="flashReport"
        >
          刷新报表
        </el-button>
        <el-button
          type="primary"
          v-permission="'buFinance_costDifferenceExport'"
          v-loading="costDifferenceBtnLoading"
          @click="exportList('costDifference')"
        >
          美赞臣成本差异导出
        </el-button>
        <span v-if="createDate" style="color: red; margin-left: 10px">
          生成时间：{{ createDate }}
        </span>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showIndex
      @selection-change="selectionChange"
      @change="getList"
    ></JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    buFinanceSummaryList,
    buFlashInventorSummary,
    createInventorSummaryTask,
    createInventorSummarySdTask,
    createInventorSummaryAllTask,
    createInventorSummaryPdfTask,
    createInventorSummaryInterestTask,
    createCostDifferenceTask
  } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          month: '',
          depotId: '',
          buId: '',
          typeId: '',
          barcode: '',
          brandIds: ''
        },
        // 生成时间
        createDate: '',
        // 标准品牌
        brandIds: [],
        // 表格列数据
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '130',
            align: 'center',
            hide: true
          },
          {
            label: '标准品牌',
            prop: 'brandName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '二级分类',
            prop: 'typeName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '货号',
            prop: 'goodsNo',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '标准条码',
            prop: 'commbarcode',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '条形码',
            prop: 'barcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '期初数量',
            prop: 'initNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '期初金额',
            prop: 'initAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '期初单价',
            prop: 'price',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本期采购入库数量',
            prop: 'warehouseNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '来货残损数量',
            prop: 'inDamagedNum',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '本期移库入',
            prop: 'moveInNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本期采购结转数量',
            prop: 'purchaseEndNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '本期采购结转金额',
            prop: 'purchaseEndAmount',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '本期单价',
            prop: 'tzPrice',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本期销售已上架数量',
            prop: 'saleShelfNum',
            width: '130',
            align: 'center',
            hide: true
          },
          {
            label: '出库残损数量',
            prop: 'outDamagedNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '本期移库出',
            prop: 'moveOutNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '本期销售结转数量',
            prop: 'saleEndNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '本期销售结转金额',
            prop: 'saleEndAmount',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '本月销毁数量',
            prop: 'destroyNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '盘盈数量',
            prop: 'profitNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '盘亏数量',
            prop: 'lossNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '本期损益结转数量',
            prop: 'lossOverflowNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '本期损益结转金额',
            prop: 'lossOverflowAmount',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '期末结存数量',
            prop: 'endNum',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '期末结存金额',
            prop: 'endAmount',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '累计采购在途数量',
            prop: 'addPurchaseNotshelfNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '累计销售在途数量',
            prop: 'addSaleNoshelfNum',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '累计调拨在途数量',
            prop: 'addTransferNoshelfNum',
            width: '120',
            align: 'center',
            hide: true
          }
        ],
        // 操作按钮loading
        excelBtnLoading: false,
        sdTaskBtnLoading: false,
        allTaskBtnLoading: false,
        pdfTaskBtnLoading: false,
        interestTaskBtnLoading: false,
        costDifferenceBtnLoading: false,
        flashBtnLoading: false
      }
    },
    activated() {
      this.formOtherPage()
      this.getList()
    },
    mounted() {
      this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
      this.formOtherPage()
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        this.searchProps.brandIds = this.brandIds.length
          ? this.brandIds.toString()
          : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await buFinanceSummaryList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
          if (this.tableData.list.length) {
            this.createDate =
              this.tableData.list[this.tableData.list.length - 1].createDate ||
              ''
          }
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 刷新报表
      async flashReport() {
        const { month, depotId, buId } = this.searchProps
        if (!month) {
          this.$errorMsg('请先选择查询月份')
          return false
        }
        try {
          this.flashBtnLoading = true
          await buFlashInventorSummary({ month, depotId, buId })
          this.$successMsg('正在刷新,请稍后再查询')
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.flashBtnLoading = false
        }
      },
      // 导出
      async exportList(type) {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出！')
          return false
        }
        const { month, buId, depotId } = this.searchProps
        const target = this.selectOpt.businessList.find(
          (item) => item.key === buId
        )
        const buName = target ? target.value : ''
        if (!month) {
          this.$errorMsg('请先选择查询月份')
          return false
        }
        try {
          switch (type) {
            case 'excel':
              this.excelBtnLoading = true
              await createInventorSummaryTask({
                ownMonth: month,
                buId,
                buName,
                depotId
              })
              break
            case 'sdTask':
              this.sdTaskBtnLoading = true
              await createInventorSummarySdTask({
                ownMonth: month,
                buId,
                buName,
                depotId
              })
              break
            case 'allTask':
              this.allTaskBtnLoading = true
              await createInventorSummaryAllTask({
                ownMonth: month,
                buId,
                buName,
                depotId
              })
              break
            case 'pdfTask':
              this.pdfTaskBtnLoading = true
              await createInventorSummaryPdfTask({
                ownMonth: month,
                buId,
                buName,
                depotId
              })
              break
            case 'interestTask':
              this.interestTaskBtnLoading = true
              await createInventorSummaryInterestTask({
                ownMonth: month,
                buId,
                buName,
                depotId
              })
              break
            case 'costDifference':
              this.costDifferenceBtnLoading = true
              await createCostDifferenceTask({
                ownMonth: month,
                buId,
                buName,
                depotId
              })
              break
          }
          this.$alertSuccess('请在报表任务列表查看进度')
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.excelBtnLoading = false
          this.sdTaskBtnLoading = false
          this.allTaskBtnLoading = false
          this.pdfTaskBtnLoading = false
          this.interestTaskBtnLoading = false
          this.costDifferenceBtnLoading = false
        }
      },
      // 其他页面跳转过来
      formOtherPage() {
        const { month, buId } = this.$route.params
        delete this.$route.params.month
        delete this.$route.params.buId
        if (month && buId) {
          this.searchProps.month = month
          this.searchProps.buId = buId + ''
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.searchProps.month = this.$formatDate(new Date(), 'yyyy-MM')
          this.brandIds = []
          this.getList(1)
        })
      }
    }
  }
</script>
