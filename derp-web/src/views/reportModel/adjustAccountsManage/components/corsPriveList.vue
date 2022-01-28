<template>
  <!-- 预申报单列表页面 -->
  <div>
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetSearchForm"
      @search="getList(1)"
      style="margin-top: 0px"
    >
      <JFX-Form :model="ruleForm" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="条形码："
              prop="barcode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="ruleForm.barcode"
                placeholder="请输入"
                clearable
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="商品名称："
              prop="goodsName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="ruleForm.goodsName"
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
                v-model="ruleForm.brandId"
                placeholder="请选择"
                filterable
                clearable
              >
                <el-option
                  v-for="item in brandList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="是否组合："
              prop="isGroup"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.isGroup"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('settlementPrice_isGroupList')"
              >
                <el-option
                  v-for="item in selectOpt.settlementPrice_isGroupList"
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
                v-model="ruleForm.status"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectList('settlementPrice_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.settlementPrice_statusList"
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
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'settlementPrice_add'"
          @click="linkTo('/adjustAccounts/corspriceAdd')"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'settlementPrice_import'"
          @click="importFile"
        >
          批量导入
        </el-button>
        <el-button
          type="primary"
          v-permission="'settlementPrice_exportSettlementPrice'"
          v-loading="exportBtnLoading"
          @click="exportList"
        >
          批量导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'settlementPrice_toExamine'"
          v-loading="examineBtnLoading"
          @click="onExamine"
        >
          提交审核
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
          v-permission="'settlementPrice_details'"
          @click="linkTo(`/adjustAccounts/corspriceDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-if="row.status !== '001'"
          v-permission="'settlementPrice_edit'"
          @click="linkTo(`/adjustAccounts/corspriceEdit?id=${row.id}`)"
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
  import {
    settlementPriceList,
    getSettlementPriceCount,
    exportSettlementPriceUrl,
    importSettlementPriceUrl,
    examineSettlementPrice,
    getSettlementPricetBrand
  } from '@a/adjustAccountsManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 表单数据
        ruleForm: {
          barcode: '',
          goodsName: '',
          brandId: '',
          isGroup: '',
          status: '',
          buId: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '所属公司',
            prop: 'merchantName',
            minWidth: '100',
            align: 'center',
            hide: true
          },
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
            label: '条形码',
            prop: 'barcode',
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
            label: '规格型号',
            prop: 'goodsSpec',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '结算币种',
            prop: 'currencyLabel',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '标准成本单价',
            prop: 'price',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '生效年月',
            prop: 'effectiveDate',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '是否组合',
            prop: 'isGroupLabel',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '修改时间',
            prop: 'modifyDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '80',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '90', align: 'left' }
        ],
        exportBtnLoading: false, // 导出按钮loading
        examineBtnLoading: false, // 审核按钮loading
        brandList: [] // 品牌列表
      }
    },
    activated() {
      this.getBrandList()
      this.getList()
    },
    mounted() {
      this.getBrandList()
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await settlementPriceList({
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
      // 获取品牌列表
      async getBrandList() {
        try {
          const { data } = await getSettlementPricetBrand()
          if (data && data.length) {
            this.brandList = data.map(({ label, value }) => ({ label, value }))
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 导出
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        try {
          this.exportBtnLoading = true
          const { data: epoxrtNum } = await getSettlementPriceCount({
            ...this.searchProps
          })
          if (epoxrtNum > 10000) {
            this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
            return false
          }
          this.$exportFile(exportSettlementPriceUrl, { ...this.searchProps })
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.exportBtnLoading = false
        }
      },
      // 审核
      async onExamine() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        try {
          this.examineBtnLoading = true
          await examineSettlementPrice({ ids })
          this.$successMsg('审核成功')
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.examineBtnLoading = false
        }
      },
      // 导入
      importFile() {
        this.linkTo(
          '/common/importfile?templateId=' +
            117 +
            '&url=' +
            importSettlementPriceUrl
        )
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
