<template>
  <!-- 销售预申报单列表页面 -->
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
              label="销售预申报单号："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.code"
                clearable
                placeholder="请输入"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="销售订单编号："
              prop="saleOrderCodes"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.saleOrderCodes"
                clearable
                placeholder="请输入"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="出仓仓库："
              prop="outDepotId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.outDepotId"
                placeholder="请选择"
                :data-list="getSelectBeanByMerchantRel('warehouseList')"
                filterable
                clearable
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
              label="客户："
              prop="customerId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.customerId"
                placeholder="请选择"
                :data-list="getCustomerSelectBean('customer_list')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.customer_list"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="入库仓库：" prop="inDepotId">
              <el-select
                v-model="searchProps.inDepotId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getSelectBeanByMerchantRel('in_warehousesList')"
              >
                <el-option
                  v-for="item in selectOpt.in_warehousesList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="接口状态："
              prop="apiStatus"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.apiStatus"
                placeholder="请选择"
                :data-list="getSelectList('declareOrder_stateList')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.declareOrder_stateList"
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
          @click="deleteTableItem"
          v-permission="'sale_declare_delete'"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          @click="exportList"
          v-permission="'sale_declare_export'"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          @click="copyTableItem"
          v-permission="'sale_declare_copy'"
        >
          复制
        </el-button>
        <el-button
          type="primary"
          @click="logisticsContact.visible.show = true"
          v-permission="'sale_declare_weihu'"
        >
          维护物流联系人
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20 mr-b-10">
      <el-radio-group
        v-model="searchProps.status"
        :data-list="getSelectList('saleDeclare_statusList')"
        @change="getList(1)"
      >
        <template v-for="item in selectOpt.saleDeclare_statusList">
          <el-radio-button
            v-if="!['008', '009'].includes(item.key)"
            :key="item.key"
            :label="item.key"
          >
            {{ item.value }}({{ tabData[item.key] || 0 }})
          </el-radio-button>
        </template>
        <el-radio-button label="">全部({{ this.tabTotal }})</el-radio-button>
      </el-radio-group>
    </div>
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
          style="padding-left: 5px"
          v-permission="'sale_declare_detail'"
          @click="linkTo(`/sales/PredeclarationDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-if="
            ['001', '002', '003'].includes(row.status) && row.apiStatus === '0'
          "
          v-permission="'sale_declare_edit'"
          @click="linkTo(`/sales/PredeclarationEdit?id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'sale_declare_log'"
          @click="showModal('logList', row.code)"
        >
          日志
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 维护物流联系人 -->
    <LogisticsContact
      v-if="logisticsContact.visible.show"
      :visible.sync="logisticsContact.visible"
    ></LogisticsContact>
    <!-- 维护物流联系人 end -->
    <!-- 日志 -->
    <LogList
      v-if="logList.visible.show"
      :visible.sync="logList.visible"
      :filterData="logList.filterData"
    ></LogList>
    <!-- 日志 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    saleDeclareList,
    delSaleDeclareOrder,
    exportDeclareUrl,
    getDeclareCount,
    getDeclareTypeNum,
    copySaleDeclare
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      // 物流联系人
      LogisticsContact: () => import('./components/LogisticsContact'),
      // 日志
      LogList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        // 查询条件
        searchProps: {
          code: '',
          outDepotId: '',
          buId: '',
          customerId: '',
          inDepotId: '',
          saleOrderCodes: '',
          apiStatus: '',
          status: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '销售预申报单号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '出仓仓库',
            prop: 'outDepotName',
            minWidth: '120',
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
            label: '客户',
            prop: 'customerName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '销售订单编号',
            prop: 'saleOrderCodes',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '入库仓库',
            prop: 'inDepotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '订单状态',
            prop: 'statusLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '接口状态',
            prop: 'apiStatusLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '130', align: 'left' }
        ],
        // 日志组件
        logList: {
          filterData: {},
          visible: { show: false }
        },
        // 日志组件
        logisticsContact: {
          visible: { show: false }
        },
        // 标签页数据
        tabData: {},
        // 标签页数量
        tabTotal: 0
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
          const { data } = await saleDeclareList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
          // 获取标签页数量
          this.getDeclareTypeNum()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 获取标签页数量
      async getDeclareTypeNum() {
        const { data } = await getDeclareTypeNum({
          ...this.searchProps,
          status: ''
        })
        const helper = {}
        if (data && data.length) {
          data.forEach(({ status, num, total }) => {
            if (status) helper[status] = num
            if (total !== null && total !== undefined) this.tabTotal = total
          })
        }
        this.tabData = helper
      },
      // 删除表格项
      deleteTableItem() {
        const h = this.$createElement
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.$msgbox({
          title: '提示',
          message: h('div', null, [
            h('h3', { style: 'text-align: center' }, '确定删除选择对象？'),
            h(
              'div',
              { style: 'color: red' },
              '(注意：删除成功填写数据、上传附件将会作废，请谨慎删除)'
            )
          ]),
          type: 'warning',
          showCancelButton: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: async (action, instance, done) => {
            if (action === 'confirm') {
              instance.confirmButtonLoading = true
              instance.confirmButtonText = '执行中...'
              try {
                await delSaleDeclareOrder({ ids })
                this.$successMsg('操作成功')
                this.getList()
                done()
              } catch (error) {
                this.$errorMsg(error.message)
              } finally {
                instance.confirmButtonLoading = false
                instance.confirmButtonText = '确定'
              }
            } else {
              done()
            }
          }
        })
        // this.$showToast(h('h1', 'aa'), async() => {
        //   const ids = this.tableChoseList.map(item => item.id).toString()
        //   try {
        //     await delSaleDeclareOrder({ ids })
        //     this.$successMsg('操作成功')
        //     this.getList()
        //   } catch (error) {
        //     this.$errorMsg(error.message)
        //   }
        // })
      },
      // 复制预申报单
      async copyTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条数据！')
          return false
        }
        if (this.tableChoseList.length >= 2) {
          this.$alertWarning('只能选择一条数据！')
          return false
        }
        try {
          const { id } = this.tableChoseList[0]
          await copySaleDeclare({ id })
          this.linkTo(`/sales/PredeclarationAdd?copyId=${id}`)
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
          if (this.tableChoseList.length) {
            this.$showToast('确定导出勾选数据？', async () => {
              const ids = this.tableChoseList.map((item) => item.id).toString()
              this.$exportFile(exportDeclareUrl, { ids })
            })
          } else {
            const { data } = await getDeclareCount({ ...this.searchProps })
            if (data > 10000) {
              return this.$errorMsg('导出的数量过大，请填写筛选条件再导出')
            }
            this.$exportFile(exportDeclareUrl, { ...this.searchProps })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 显示日志弹窗
      showModal(type, data) {
        switch (type) {
          case 'logList':
            this.logList.filterData = { relCode: data, module: 6 }
            this.logList.visible.show = true
            break
        }
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
