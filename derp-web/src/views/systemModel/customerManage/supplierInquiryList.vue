<template>
  <!-- 供应商询价池列表页面 -->
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
              label="供应商选择："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in supplierBean"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="供应商品牌："
              prop="brandId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.brandId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in brandBean"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品名称："
              prop="goodsName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model.trim="searchProps.goodsName" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品条码："
              prop="goodsNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model.trim="searchProps.goodsNo" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品分类："
              prop="merchandiseCatId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.merchandiseCatId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in productTypeBean"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="原产国："
              prop="countryId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.countryId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in countryBean"
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
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'supplierInquiryPool_delSIPool'"
          @click="deleteTableItem"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          v-permission="'supplierInquiryPool_toImportPage'"
          @click="importFile"
        >
          批量导入
        </el-button>
        <el-button
          type="primary"
          v-permission="'supplierInquiryPool_exportSupplier'"
          @click="exportList"
        >
          批量导出
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
          v-permission="'supplierInquiryPool_toEditPage'"
          @click="linkTo(`/customer/supplierInquiryEdit?id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'supplierInquiryPool_toDetailsPage'"
          @click="linkTo(`/customer/supplierInquiryDetail?id=${row.id}`)"
        >
          详情
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listSIPool,
    delSIPool,
    importSIPoolUrl,
    exportSupplierInquiryPoolUrl,
    toPageSIPool
  } from '@a/customerManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          customerId: '',
          brandId: '',
          goodsName: '',
          goodsNo: '',
          merchandiseCatId: '',
          countryId: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '品类',
            prop: 'merchandiseCatName',
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
            label: '品牌',
            prop: 'brandName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '供应商',
            prop: 'customerName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '起订量',
            prop: 'minimum',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '计量单位',
            prop: 'unit',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '供货价',
            prop: 'supplyPrice',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '原产国',
            prop: 'countryName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '规格型号',
            prop: 'spec',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '录入日期',
            prop: 'createDate',
            width: '150',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '100', align: 'center' }
        ],
        supplierBean: [],
        brandBean: [],
        productTypeBean: [],
        countryBean: []
      }
    },
    activated() {
      this.getSelectList()
      this.getList()
    },
    mounted() {
      this.getSelectList()
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listSIPool({
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
      // 获取下拉列表
      async getSelectList() {
        try {
          const {
            data: { brandBean, countryBean, supplierBean, productTypeBean }
          } = await toPageSIPool()
          this.supplierBean = supplierBean || []
          this.brandBean = brandBean || []
          this.productTypeBean = productTypeBean || []
          this.countryBean = countryBean || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 删除表格项
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await delSIPool({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 导入
      importFile() {
        this.linkTo(
          '/common/importfile?templateId=' + 106 + '&url=' + importSIPoolUrl
        )
      },
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.$exportFile(exportSupplierInquiryPoolUrl, { ...this.searchProps })
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
