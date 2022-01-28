<template>
  <!-- 商品库存明细页面 -->
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
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="仓库："
              prop="depotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="depotIdsList"
                placeholder="请选择"
                multiple
                filterable
                collapse-tags
                clearable
                style="width: 230px"
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
              label="商品货号："
              prop="goodsNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.goodsNo"
                placeholder="请输入"
                clearable
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品条码："
              prop="barcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.barcode"
                placeholder="请输入"
                clearable
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="品牌："
              prop="brandId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.brandId"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getBrandSelectBean('brandList')"
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
          v-permission="'productInventoryDetails_exportPInventoryDetails'"
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
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'productInventoryDetails_detail'"
          @click="jumpToDetail(row.goodsNo, row.depotId)"
        >
          详情
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listProductInventoryDetails,
    exportProductInventoryDetails
  } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询参数
        searchProps: {
          merchantName: '',
          depotIds: '',
          goodsNo: '',
          barcode: '',
          brandId: ''
        },
        depotIdsList: [],
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
            label: '商品货号',
            prop: 'goodsNo',
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
            label: '商品条码',
            prop: 'barcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '品牌名称',
            prop: 'brandName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '库存数量',
            prop: 'surplusNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '冻结数量',
            prop: 'freezeNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '坏品数量',
            prop: 'badNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '过期数量',
            prop: 'okayNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '可用量',
            prop: 'availableNum',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '单位',
            prop: 'unitLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '80',
            align: 'center',
            fixed: 'right'
          }
        ]
      }
    },
    activated() {
      this.formOtherPage()
      this.getList()
    },
    mounted() {
      this.formOtherPage()
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.searchProps.depotIds =
            this.depotIdsList && this.depotIdsList.length
              ? this.depotIdsList.toString()
              : ''
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listProductInventoryDetails({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
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
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.$exportFile(exportProductInventoryDetails, { ...this.searchProps })
      },
      // 其他页面跳转过来
      formOtherPage() {
        const { goodsNo, depotId } = this.$route.params
        delete this.$route.params.goodsNo
        delete this.$route.params.depotId
        if (goodsNo && depotId) {
          !this.depotIdsList.includes(depotId.toString()) &&
            this.depotIdsList.push(depotId.toString())
          this.searchProps.goodsNo = goodsNo
        }
      },
      // 跳转到批次库存明细
      jumpToDetail(goodsNo, depotId) {
        this.linkTo({
          name: 'BatchStock',
          params: { goodsNo, depotId }
        })
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.depotIdsList = []
          this.getList(1)
        })
      }
    }
  }
</script>
