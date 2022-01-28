<template>
  <!-- 预申报单列表页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="公司："
              prop="merchantId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.merchantId"
                placeholder="请选择"
                clearable
                :data-list="getSelectMerchantBean('merchantList')"
              >
                <el-option
                  v-for="item in selectOpt.merchantList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
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
                :data-list="
                  getSelectList('settlementPriceWarnconfig_statusList')
                "
              >
                <el-option
                  v-for="item in selectOpt.settlementPriceWarnconfig_statusList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
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
          v-permission="'settlement_price_warnconfig_toAddPage'"
          @click="linkTo('/email/unitPriceAdd')"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'settlement_price_warnconfig_delete'"
          v-loading="delBtnLoading"
          @click="deleteTableItem"
        >
          删除
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
      <template slot="waveRange" slot-scope="{ row }">
        {{ row.waveRange ? row.waveRange + '%' : '' }}
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          style="padding-left: -6px"
          v-if="row.status === '1'"
          v-permission="'settlementPriceWarnconfig_audit'"
          @click="disableOrEnable(row.id, 0)"
        >
          禁用
        </el-button>
        <el-button
          type="text"
          v-if="row.status !== '1'"
          v-permission="'settlementPriceWarnconfig_audit'"
          @click="disableOrEnable(row.id, 1)"
        >
          启用
        </el-button>
        <el-button
          type="text"
          v-permission="'settlementPriceWarnconfig_toDetailsPage'"
          @click="linkTo(`/email/unitPriceDetail?id=${row.id}`)"
        >
          详情
        </el-button>
        <el-button
          type="text"
          v-permission="'settlementPriceWarnconfig_edit'"
          @click="linkTo(`/email/unitPriceEdit?id=${row.id}`)"
        >
          编辑
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import {
    settlementPriceWarnconfigList,
    delSettlementPriceWarnconfig,
    auditSettlementPriceWarnconfig
  } from '@a/emailManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        ruleForm: {
          merchantId: '',
          buId: '',
          status: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
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
            label: '波动范围',
            slotTo: 'waveRange',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '编辑日期',
            prop: 'modifyDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '编辑人',
            prop: 'modifierUsername',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '130',
            align: 'left'
          }
        ],
        delBtnLoading: false // 删除按钮loading
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
          const { data } = await settlementPriceWarnconfigList({
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
      // 禁用/启用
      disableOrEnable(id, status) {
        const msg = ['禁用', '启用']
        try {
          this.$showToast(`确定要${msg[status]}吗？`, async () => {
            await auditSettlementPriceWarnconfig({ id, status })
            this.$successMsg(`${msg[status]}成功`)
            this.getList()
          })
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
            this.delBtnLoading = true
            await delSettlementPriceWarnconfig({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          } finally {
            this.delBtnLoading = false
          }
        })
      },
      // 重置搜索框
      resetSearchForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
<style lang="scss" scoped></style>
