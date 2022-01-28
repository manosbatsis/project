<template>
  <!-- 选择发票模板组件 -->
  <JFX-Dialog
    title="选择发票模板"
    closeBtnText="取 消"
    :width="'800px'"
    :top="'80px'"
    :showClose="true"
    :visible.sync="isVisible"
    @comfirm="comfirm"
  >
    <JFX-Form :model="ruleForm" :rules="rules">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="发票名称：" prop="name">
            <el-input
              v-model="ruleForm.name"
              placeholder="请输入"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <el-button type="primary" @click="getList">查询</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <JFX-table
      :tableData.sync="tableData"
      :tableHeight="'480px'"
      :showSelection="true"
      :tableColumn="tableColumn"
      @selection-change="selectionChange"
      @change="getList"
    ></JFX-table>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { listFileTempInfo } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: { show: false },
      data: {
        type: Array,
        default: function () {
          return []
        }
      }
    },
    data() {
      return {
        ruleForm: {
          name: ''
        },
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        tableColumn: [
          {
            label: '发票模板名称',
            prop: 'title',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '发票编码',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '适用客户',
            prop: 'customers',
            minWidth: '140',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 提交表单
      comfirm() {
        if (!this.tableChoseList.length || this.tableChoseList.length > 1) {
          return this.$alertWarning('只能选择一个模块')
        }
        this.$emit('comfirm')
        const selectTmeplate = this.tableChoseList[0]
        // const isWp = true
        const isWp =
          selectTmeplate.toUrl && selectTmeplate.toUrl.includes('WEIPIN')
        const isDouble =
          selectTmeplate.toUrl && selectTmeplate.toUrl.includes('DOUBLE')
        const ids = this.data.map((item) => item.id).toString(',')
        const codes = this.data.map((item) => item.billCode).toString(',')
        this.linkTo(
          `/receiveBill/toInvoicePage?tempId=${
            selectTmeplate.id
          }&ids=${ids}&codes=${codes}${isWp ? '&isWp=1' : ''}${
            isDouble ? '&isDouble=1' : ''
          }&invoiceStatus=2`
        )
      },
      // 拉取表格数据
      async getList() {
        try {
          this.tableData.loading = true
          const { data } = await listFileTempInfo({
            ...this.ruleForm,
            customerIds: this.data.map((item) => item.customerId).toString(),
            type: 2
          })
          this.tableData = {
            list: data
          }
        } catch (err) {
          this.tableData.loading = false
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.el-form-item {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 100px;
    }
    .el-form-item__content {
      flex: 1;
    }
  }
</style>
