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
              label="生效月份："
              prop="effectiveDate"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="ruleForm.effectiveDate"
                type="month"
                value-format="yyyy-MM"
                placeholder="生效月份"
                clearable
              ></el-date-picker>
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
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'settlementPrice_examine'"
          @click="onExamine"
        >
          审核
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <div class="mr-t-20"></div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    ></JFX-table>
    <!-- 表格 end-->
    <JFX-Dialog
      title="审核"
      closeBtnText="取 消"
      :width="'300px'"
      :top="'150px'"
      :showClose="true"
      :visible="examineModal.visible"
      btnAlign="center"
      @comfirm="comfirm"
    >
      <div class="flex-c-c">
        <el-radio-group v-model="examineModal.status">
          <el-radio label="032">通过</el-radio>
          <el-radio label="033">不通过</el-radio>
        </el-radio-group>
      </div>
      <div style="color: red; padding: 10px 0; text-align: center">
        请再次确认审批操作!
      </div>
    </JFX-Dialog>
  </div>
</template>
<script>
  import { listExamineList, examinePriceList } from '@a/adjustAccountsManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        ruleForm: {
          barcode: '',
          effectiveDate: '',
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
            label: '生效年月',
            prop: 'effectiveDate',
            width: '120',
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
            label: '调整前单价',
            prop: 'historyPrice',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '调整后单价',
            prop: 'price',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '调价原因',
            prop: 'adjustPriceResult',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '提交人',
            prop: 'modifier',
            width: '120',
            align: 'center',
            hide: true
          }
        ],
        examineModal: {
          visible: { show: false },
          ids: '',
          status: '032'
        }
      }
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
          const { data } = await listExamineList({
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
      async comfirm() {
        try {
          if (!this.examineModal.status) {
            this.$errorMsg('请先选择审核结果')
            return false
          }
          await examinePriceList({
            ids: this.examineModal.ids,
            status: this.examineModal.status
          })
          this.$successMsg('操作成功')
          this.getList()
          this.examineModal.visible.show = false
          this.examineModal.ids = ''
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 审核
      async onExamine() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.examineModal.visible.show = true
        this.examineModal.ids = ids
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
<style lang="scss" scoped></style>
