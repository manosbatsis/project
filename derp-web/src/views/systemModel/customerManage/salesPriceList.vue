<template>
  <!-- 销售价格管理页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
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
                :data-list="getCustomerSelectBean('customerList')"
              >
                <el-option
                  v-for="item in selectOpt.customerList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="创建人："
              prop="createName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.createName" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准条码："
              prop="commbarcodes"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model="searchProps.commbarcodes" clearable></el-input>
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
                filterable
                clearable
                :data-list="getBUSelectBean('buList')"
              >
                <el-option
                  v-for="item in selectOpt.buList"
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
          v-permission="'customerSale_toAddPage'"
          @click="linkTo('/customer/salespriceAdd')"
          >新增</el-button
        >
        <el-button
          type="primary"
          v-permission="'customerSale_Copy'"
          @click="handleClickCopy"
          >复制</el-button
        >
        <el-button
          type="primary"
          @click="removeItem"
          v-permission="'customerSale_delPriceSale'"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          @click="
            linkTo(
              '/common/importfile?templateId=' +
                116 +
                '&url=' +
                importPriceSaleUrl
            )
          "
          v-permission="'customerSale_toImportPage'"
        >
          批量导入
        </el-button>
        <el-button
          type="primary"
          v-permission="'customerSale_submit'"
          @click="submitItem"
        >
          提交
        </el-button>
        <el-button
          type="primary"
          v-permission="'customerSale_audit'"
          @click="auditItem"
        >
          审核
        </el-button>
        <el-button
          type="primary"
          @click="showApplyCancel"
          v-permission="'customerSale_applyCancel'"
        >
          申请作废
        </el-button>
        <el-button
          type="primary"
          @click="showCancelAudit"
          v-permission="'customerSale_cancelAudit'"
        >
          作废审核
        </el-button>
        <el-button
          type="primary"
          v-permission="'customerSale_export'"
          @click="exportList"
        >
          导出
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <div class="mr-t-20 mr-b-10">
      <el-radio-group v-model="searchProps.status" @change="getList(1)">
        <el-radio-button label="002">
          待提交({{ totalObj['002'] }})
        </el-radio-button>
        <el-radio-button label="001">
          待审核({{ totalObj['001'] }})
        </el-radio-button>
        <el-radio-button label="003">
          已审核({{ totalObj['003'] }})
        </el-radio-button>
        <el-radio-button label="004">
          已驳回({{ totalObj['004'] }})
        </el-radio-button>
        <el-radio-button label="005">
          待作废({{ totalObj['005'] }})
        </el-radio-button>
        <el-radio-button label="007">
          已作废({{ totalObj['007'] }})
        </el-radio-button>
        <el-radio-button label="">
          全部({{ totalObj['total'] }})
        </el-radio-button>
      </el-radio-group>
    </div>
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="customerName" slot-scope="{ row }">
        {{ row.customerCode }}
        <br />
        {{ row.customerName }}
      </template>
      <template slot="salePrice" slot-scope="{ row }">
        {{ row.currencyLabel + ' ' + row.salePriceLabel }}
      </template>
      <template slot="createName" slot-scope="{ row }">
        {{ row.createName || '' }}
        <br />
        {{ row.createDate || '' }}
      </template>
      <template slot="auditName" slot-scope="{ row }">
        {{ row.auditName || '' }}
        <br />
        {{ row.auditDate || '' }}
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-if="['002', '004'].includes(row.status)"
          v-permission="'customerSale_edit'"
          style="padding-left: 6px"
          @click="linkTo('/customer/salespriceEdit?id=' + row.id)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'customerSale_log'"
          @click="showModal('logList', row.id)"
        >
          日志
        </el-button>
        <el-button
          type="text"
          v-permission="'customerSaleUpload'"
          @click="linkTo('/customer/salespriceEdit?isUpload=1&id=' + row.id)"
        >
          附件管理
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <audit
      v-if="auditVisible.show"
      :visible="auditVisible"
      @comfirm="getList()"
      type="sales"
    />
    <!-- 申请作废 -->
    <ApplyCancel
      v-if="applyCancel.visible.show"
      :isVisible="applyCancel.visible"
      :ids="applyCancel.ids"
      :type="applyCancel.type"
      @comfirm="closeApplyCancel"
    ></ApplyCancel>
    <!-- 申请作废 end -->
    <!-- 作废审核 -->
    <CancelAudit
      v-if="cancelAudit.visible.show"
      :isVisible="cancelAudit.visible"
      :ids="cancelAudit.ids"
      :type="cancelAudit.type"
      @comfirm="closeCancelAudit"
    ></CancelAudit>
    <!-- 作废审核 end -->
    <!-- 日志 -->
    <LogList
      v-if="logList.visible.show"
      :visible.sync="logList.visible"
      :filterData="logList.filterData"
      type="system"
    ></LogList>
    <!-- 日志 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listSalePrice,
    listSalePriceCount,
    delPriceSale,
    importPriceSaleUrl,
    salesSubmitSMPrice,
    exportPriceSaleUrl
  } from '@a/customerManage'
  export default {
    mixins: [commomMix],
    components: {
      audit: () => import('./components/PriceListAudit'),
      ApplyCancel: () => import('./components/ApplyCancel'),
      CancelAudit: () => import('./components/CancelAudit'),
      LogList: () => import('@c/logList/index.vue') // 日志
    },
    data() {
      return {
        importPriceSaleUrl,
        // 查询数据
        searchProps: {
          customerIds: '',
          commbarcodes: '',
          brandId: '',
          createName: '',
          status: '002'
        },
        // 表格列数据
        tableColumn: [
          {
            label: '所属公司',
            prop: 'merchantName',
            minWidth: '140',
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
            label: '客户名称',
            slotTo: 'customerName',
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
            label: '商品名称',
            prop: 'goodsName',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '品牌',
            prop: 'brandName',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '规格型号',
            prop: 'spec',
            width: '90',
            align: 'center',
            hide: true
          },
          {
            label: '销售价',
            slotTo: 'salePrice',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '报价生效时间',
            prop: 'effectiveDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '报价失效时间',
            prop: 'expiryDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '创建人',
            slotTo: 'createName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '审核人',
            slotTo: 'auditName',
            width: '150',
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
        // 统计数据
        totalObj: {},
        auditVisible: {
          show: false
        },
        // 申请作废
        applyCancel: {
          visible: { show: false },
          ids: '',
          type: 'sale'
        },
        // 作废审核
        cancelAudit: {
          visible: { show: false },
          ids: '',
          type: 'sale'
        },
        // 日志组件
        logList: {
          filterData: {},
          visible: { show: false }
        }
      }
    },
    mounted() {
      this.getList()
    },
    activated() {
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
          } = await listSalePrice({
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
          this.getStaticsNum()
        }
      },
      // 获取统计行数据
      async getStaticsNum() {
        const { data } = await listSalePriceCount({
          ...this.searchProps,
          status: ''
        })
        const {
          beSubmited,
          beAudit,
          beAreadyAudit,
          beRejected,
          beAlreadyInvalid,
          beInvalid,
          beAll
        } = data
        this.totalObj = {
          '002': beSubmited,
          '001': beAudit,
          '003': beAreadyAudit,
          '004': beRejected,
          '005': beInvalid,
          '007': beAlreadyInvalid,
          total: beAll
        }
      },
      // 删除数据项
      removeItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
        } else {
          this.$showToast('确定删除选中对象？', async () => {
            await delPriceSale({
              ids: this.tableChoseList.map((item) => item.id).toString()
            })
            this.$successMsg('操作成功')
            this.getList()
          })
        }
      },
      // 提交数据项
      submitItem() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        this.$showToast('确定提交选中对象？', async () => {
          await salesSubmitSMPrice({
            ids: this.tableChoseList.map((item) => item.id).toString()
          })
          this.$successMsg('操作成功')
          this.getList()
        })
      },
      // 导出
      async exportList() {
        this.$exportFile(exportPriceSaleUrl, { ...this.searchProps })
      },
      // 审核
      auditItem() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        this.auditVisible.show = true
        this.auditVisible.ids = this.tableChoseList
          .map((item) => item.id)
          .toString()
      },
      // 复制
      handleClickCopy() {
        if (this.tableChoseList.length !== 1) {
          return this.$alertWarning('请选择一条记录！')
        }
        const { id: copyId } = this.tableChoseList[0]
        this.linkTo('/customer/salespriceAdd?isCopy=1&copyId=' + copyId)
      },
      // 显示申请作废弹窗
      showApplyCancel() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        this.applyCancel.visible.show = true
        this.applyCancel.type = 'sale'
        this.applyCancel.ids = this.tableChoseList
          .map((item) => item.id)
          .toString()
      },
      // 关闭申请作废弹窗
      closeApplyCancel() {
        this.applyCancel.visible.show = false
        this.getList()
      },
      // 显示作废审核弹窗
      showCancelAudit() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        this.cancelAudit.visible.show = true
        this.cancelAudit.type = 'sale'
        this.cancelAudit.ids = this.tableChoseList
          .map((item) => item.id)
          .toString()
      },
      // 关闭作废审核弹窗
      closeCancelAudit() {
        this.cancelAudit.visible.show = false
        this.getList()
      },
      // 显示弹窗
      showModal(type, data) {
        switch (type) {
          case 'logList':
            this.logList.filterData = { id: data, module: 3 }
            this.logList.visible.show = true
            break
        }
      },
      resetForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
