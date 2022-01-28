<template>
  <!-- 预申报单列表页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetForm" @search="getList(1)">
      <JFX-Form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="采购订单编号："
              prop="purchaseOrderCode"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.purchaseOrderCode"
                clearable
                placeholder="请输入"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO号："
              prop="poNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.poNo"
                placeholder="请输入"
                clearable
              ></el-input>
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
                :data-list="getBUSelectBean('buList')"
              >
                <el-option
                  v-for="item in selectOpt.buList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="供应商："
              prop="supplierId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="ruleForm.supplierId"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSupplierList('suppList')"
              >
                <el-option
                  v-for="item in selectOpt.suppList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="发票号码："
              prop="invoiceNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.invoiceNo"
                placeholder="请输入"
                clearable
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="开票人："
              prop="payName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.payName"
                placeholder="请输入"
                clearable
              ></el-input>
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
          v-permission="'invoiceList_delete'"
          type="primary"
          @click="removeTableItem"
        >
          删除
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column type="selection" align="center" width="55">
      </el-table-column>
      <el-table-column
        prop="purchaseOrderCode"
        label="采购订单编号"
        align="center"
        min-width="180"
      >
      </el-table-column>
      <el-table-column
        prop="poNo"
        label="PO号"
        align="center"
        min-width="180"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        prop="buName"
        label="事业部"
        align="center"
        min-width="150"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        prop="supplierName"
        label="供应商"
        align="center"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column align="center" min-width="120">
        <template slot="header">
          发票总金额
          <br />
          （不含税）
        </template>
        <template slot-scope="scope">
          {{ (scope.row.currency || '') + ' ' + (scope.row.amount || '') }}
        </template>
      </el-table-column>
      <el-table-column align="center" min-width="120">
        <template slot="header">
          发票总金额
          <br />
          （含税）
        </template>
        <template slot-scope="scope">
          {{ (scope.row.currency || '') + ' ' + (scope.row.taxAmount || '') }}
        </template>
      </el-table-column>
      <el-table-column
        prop="invoiceDate"
        label="收到发票日期"
        align="center"
        min-width="110"
      >
        <template slot-scope="scope">
          {{
            scope.row.invoiceDate
              ? scope.row.invoiceDate.substring(0, 10)
              : '- -'
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="invoiceNo"
        label="发票号码"
        align="center"
        min-width="120"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        prop="drawInvoiceDate"
        label="发票日期"
        align="center"
        min-width="110"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{
            scope.row.drawInvoiceDate
              ? scope.row.drawInvoiceDate.substring(0, 10)
              : '- -'
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="payName"
        label="开票人"
        align="center"
        min-width="110"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        prop="paymentDate"
        label="预计付款日期"
        align="center"
        width="110"
      >
        <template slot-scope="scope">
          {{
            scope.row.paymentDate
              ? scope.row.paymentDate.substring(0, 10)
              : '- -'
          }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="left"
        fixed="right"
        :width="btnsWidth"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          <el-button
            v-permission="'invoiceList_detail'"
            type="text"
            @click="linkTo('/purchase/invoiceDetail?id=' + row.id)"
          >
            详情
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import { purchaseInvoiceList, purchaseInvoiceDelete } from '@a/purchaseManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          payName: '',
          invoiceNo: '',
          purchaseOrderCode: '',
          supplierId: '',
          poNo: '',
          buId: ''
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      async getList(pageNum) {
        try {
          this.tableData.loading = true
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          const { data } = await purchaseInvoiceList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      /* 删除 */
      removeTableItem() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        const ids = this.tableChoseList.map((item) => item.id).toString()
        this.$confirm('确定删除选中对象？', '提示', { type: 'warning' }).then(
          async () => {
            await purchaseInvoiceDelete({ ids })
            this.$successMsg('删除成功')
            this.getList()
          }
        )
      },
      /* 重置搜索 */
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
