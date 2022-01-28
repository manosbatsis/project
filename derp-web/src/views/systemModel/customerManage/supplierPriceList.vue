<template>
  <!-- 供应商商品价目表页面 -->
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
              label="供应商："
              prop="customerIdsStr"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerIdsStr"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSupplierSelectBean('supplier_list')"
              >
                <el-option
                  v-for="item in selectOpt.supplier_list"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="标准条码："
              prop="commbarcodes"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.commbarcodes"
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
                v-model="searchProps.buId"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getBUSelectBean('buList')"
                @change="buChange"
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
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="创建人："
              prop="createName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.createName"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="库存类型："
              prop="stockLocationTypeId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.stockLocationTypeId"
                placeholder="请选择"
                clearable
                filterable
              >
                <el-option
                  v-for="item in typeSelectList"
                  :key="item.key"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="数据来源："
              prop="dataSource"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.dataSource"
                placeholder="请选择"
                clearable
                filterable
                :data-list="
                  getSelectList('supplierMerchandisePrice_dataSourceList')
                "
              >
                <el-option
                  v-for="item in selectOpt.supplierMerchandisePrice_dataSourceList"
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
          v-permission="'supplierMerchandisePriceAdd'"
          @click="linkTo('/customer/supplierpricelistAdd')"
          >新增</el-button
        >
        <el-button
          type="primary"
          v-permission="'supplierMerchandisePriceCopy'"
          @click="handleClickCopy"
          >复制</el-button
        >
        <el-button
          type="primary"
          @click="removeItem"
          v-permission="'supplierMerchandisePrice_delSMPrice'"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          @click="submitItem"
          v-permission="'supplierMerchandisePrice_submit'"
        >
          提交
        </el-button>
        <el-button
          type="primary"
          @click="
            linkTo(
              '/common/importfile?templateId=' +
                103 +
                '&url=' +
                importSMPriceUrl
            )
          "
          v-permission="'supplierMerchandisePrice_toImportPage'"
        >
          批量导入
        </el-button>
        <el-button
          type="primary"
          @click="auditItem"
          v-permission="'supplierMerchandisePrice_audit'"
        >
          审核
        </el-button>
        <el-button
          type="primary"
          @click="showApplyCancel"
          v-permission="'supplierMerchandisePrice_applyCancel'"
        >
          申请作废
        </el-button>
        <el-button
          type="primary"
          @click="showCancelAudit"
          v-permission="'supplierMerchandisePrice_cancelAudit'"
        >
          作废审核
        </el-button>
        <el-button
          type="primary"
          @click="exportList"
          v-permission="'supplierMerchandisePrice_export'"
        >
          导出
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <div class="mr-t-20 mr-b-10">
      <el-radio-group v-model="searchProps.state" @change="getList(1)">
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
      <template slot="supplyPrice" slot-scope="{ row }">
        {{ row.currencyLabel + ' ' + row.supplyPriceLabel }}
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
          v-if="['002', '004'].includes(row.state)"
          v-permission="'supplierMerchandisePrice_edit'"
          style="padding-left: 6px"
          @click="linkTo('/customer/supplierpricelistEdit?id=' + row.id)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'supplierMerchandisePrice_log'"
          @click="showModal('logList', row.id)"
        >
          日志
        </el-button>
        <el-button
          type="text"
          v-permission="'supplierMerchandisePriceUpload'"
          @click="
            linkTo('/customer/supplierpricelistEdit?isUpload=1&id=' + row.id)
          "
          >附件管理</el-button
        >
      </template>
    </JFX-table>
    <audit
      v-if="auditVisible.show"
      :visible="auditVisible"
      @comfirm="getList()"
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
    listSMPrice,
    statisticsStateNum,
    submitSMPrice,
    delSMPrice,
    getOrderCount,
    exportSMPriceUrl,
    importSMPriceUrl
  } from '@a/customerManage/index'
  import { getBuStockLocationTypeConfigSelect } from '@a/base'
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
        importSMPriceUrl,
        // 查询数据
        searchProps: {
          customerIdsStr: '',
          commbarcodes: '',
          createName: '',
          state: '002',
          buId: '',
          stockLocationTypeId: '',
          dataSource: ''
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
            label: '供应商',
            slotTo: 'customerName',
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
            label: '标准条码',
            prop: 'commbarcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '库位类型',
            prop: 'stockLocationTypeName',
            width: '100',
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
            label: '采购价',
            slotTo: 'supplyPrice',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'stateLabel',
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
            label: '数据来源',
            prop: 'dataSourceLabel',
            width: '100',
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
            width: '90',
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
          type: 'purchase'
        },
        // 作废审核
        cancelAudit: {
          visible: { show: false },
          ids: '',
          type: 'purchase'
        },
        // 日志组件
        logList: {
          filterData: {},
          visible: { show: false }
        },
        typeSelectList: []
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
          } = await listSMPrice({
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
        const { data } = await statisticsStateNum({
          ...this.searchProps,
          state: ''
        })
        const {
          submitNum,
          auditNum,
          beAuditNum,
          rejectNum,
          areadyInvalidNum,
          invalidNum,
          allNum
        } = data
        this.totalObj = {
          '002': submitNum,
          '001': auditNum,
          '003': beAuditNum,
          '004': rejectNum,
          '005': invalidNum,
          '007': areadyInvalidNum,
          total: allNum
        }
      },
      // 提交数据项
      submitItem() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        this.$showToast('确定提交选中对象？', async () => {
          await submitSMPrice({
            ids: this.tableChoseList.map((item) => item.id).toString()
          })
          this.$successMsg('操作成功')
          this.getList()
        })
      },
      auditItem() {
        if (!this.tableChoseList.length) {
          return this.$alertWarning('至少选择一条记录！')
        }
        this.auditVisible.show = true
        this.auditVisible.ids = this.tableChoseList
          .map((item) => item.id)
          .toString()
      },
      // 删除数据项
      removeItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
        } else {
          this.$showToast('确定删除选中对象？', async () => {
            await delSMPrice({
              ids: this.tableChoseList.map((item) => item.id).toString()
            })
            this.$successMsg('操作成功')
            this.getList()
          })
        }
      },
      async exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return
        }
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportSMPriceUrl, { ids })
          })
          return
        }
        try {
          const { data: count } = await getOrderCount({ ...this.searchProps })
          if (count > 10000) {
            this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
            return
          }
          this.$exportFile(exportSMPriceUrl, { ...this.searchProps })
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 复制
      handleClickCopy() {
        if (this.tableChoseList.length !== 1) {
          return this.$alertWarning('请选择一条记录！')
        }
        const { id: copyId } = this.tableChoseList[0]
        this.linkTo('/customer/supplierpricelistAdd?isCopy=1&copyId=' + copyId)
      },
      // 显示申请作废弹窗
      showApplyCancel() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        this.applyCancel.visible.show = true
        this.applyCancel.type = 'purchase'
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
        this.cancelAudit.type = 'purchase'
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
            this.logList.filterData = { id: data, module: 2 }
            this.logList.visible.show = true
            break
        }
      },
      buChange(buId) {
        if (!buId) return
        this.getTypeSeclectList(buId)
      },
      async getTypeSeclectList(buId) {
        if (!buId) return
        try {
          const { data } = await getBuStockLocationTypeConfigSelect({
            buId
          })
          this.typeSelectList = data || []
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
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
