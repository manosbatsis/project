<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="ruleForm" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准条形码："
              prop="commbarcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.commbarcode"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.buId"
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
              label="生效年月："
              prop="effectiveMonth"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                clearable
                v-model="ruleForm.effectiveMonth"
                type="month"
                value-format="yyyy-MM"
                placeholder="请选择生效年月"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品名称："
              prop="goodsName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.goodsName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准品牌："
              prop="brandId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.brandId"
                placeholder="请选择"
                filterable
                clearable
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
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item label="更新时间：">
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
          v-permission="'sd_weightedPrice_export'"
          v-loading="exportBtnLoading"
          @click="exportList"
        >
          导出
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showIndex
      @change="getList"
    />
    <!-- 表格 end-->
  </div>
</template>
<script>
  import {
    sdWeightedPriceList,
    sdExportWeightedPrice
  } from '@a/adjustAccountsManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        ruleForm: {
          commbarcode: '',
          buId: '',
          effectiveMonth: '',
          goodsName: '',
          brandId: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '标准条码',
            prop: 'commbarcode',
            minWidth: '120',
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
            label: '结算币种',
            prop: 'currencyLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '加权单价',
            prop: 'price',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '生效年月',
            prop: 'effectiveMonth',
            width: '110',
            align: 'center',
            hide: true
          },
          {
            label: '更新时间',
            prop: 'createDate',
            width: '150',
            align: 'center',
            hide: true
          }
        ],
        date: [], // 更新时间
        exportBtnLoading: false // 导出按钮loading
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
        // 订单日期
        this.ruleForm.createDateStart =
          this.date && this.date.length ? this.date[0] : ''
        this.ruleForm.createDateEnd =
          this.date && this.date.length ? this.date[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await sdWeightedPriceList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.exportBtnLoading = true
        this.$exportFile(sdExportWeightedPrice, { ...this.ruleForm })
        this.exportBtnLoading = false
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
