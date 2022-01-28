<template>
  <!-- 客户列表页面 -->
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
              label="开店公司："
              prop="merchantName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.merchantName" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="店铺名称："
              prop="shopName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.shopName" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="店铺编码："
              prop="shopCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.shopCode" clearable />
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
              label="状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('merchantShopRel_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.merchantShopRel_statusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="数据来源："
              prop="dataSourceCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.dataSourceCode"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('dataSourceList')"
              >
                <el-option
                  v-for="item in selectOpt.dataSourceList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
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
                clearable
                filterable
                :data-list="getDepotSelectBean('depotList')"
              >
                <el-option
                  v-for="item in selectOpt.depotList"
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
          v-permission="'merchantShop_toAddPage'"
          @click="linkTo('/companyFile/addShop')"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'merchantShop_delShop'"
          @click="deleteTableItem"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          v-permission="'merchantShop_exportShop'"
          @click="exportList"
        >
          导出
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
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'merchantShop_toDetailsPage'"
          style="padding-left: 6px"
          @click="linkTo(`/companyFile/shopDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-permission="'merchantShop_toEditPage'"
          @click="linkTo(`/companyFile/addShop?id=${row.id}`)"
        >
          编辑
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listShop, delShop, exportShopUrl } from '@a/companyFile/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          merchantName: '',
          shopName: '',
          shopCode: '',
          storePlatformCode: '',
          status: '',
          dataSourceCode: '',
          depotId: ''
        },
        // 表格列数据
        tableColumn: [
          //   												操作
          {
            label: '开店公司',
            prop: 'merchantName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '平台名称',
            prop: 'storePlatformName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '仓库名称',
            prop: 'depotName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '店铺名称',
            prop: 'shopName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '店铺编码',
            prop: 'shopCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '专营母品牌',
            prop: 'superiorParentBrandName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '结算币种',
            prop: 'currencyLabel',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '数据来源',
            prop: 'dataSourceName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '运营类型',
            prop: 'shopTypeName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '创建时间',
            prop: 'createDate',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '操作员',
            prop: 'operator',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '120',
            align: 'left',
            fixed: 'right'
          }
        ],
        relatedCompany: {
          id: '',
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
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listShop({
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
      // 删除表格项
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await delShop({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        this.$exportFile(exportShopUrl, { ...this.searchProps })
      },
      // 显示选择关联公司弹窗
      showModal(data) {
        this.relatedCompany.visible.show = true
        this.relatedCompany.id = data + ''
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
