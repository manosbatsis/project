<template>
  <div>
    <JFX-Dialog
      :visible.sync="openTemplateVisible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'850px'"
      :title="'选择发票模板'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      top="15vh"
    >
      <JFX-search-panel @reset="resetForm" @search="getList(1)">
        <JFX-Form :model="ruleForm" ref="ruleForm">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="发票名称："
                prop="title"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input v-model.trim="ruleForm.title" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </JFX-Form>
      </JFX-search-panel>
      <JFX-table
        :tableData.sync="tableData"
        :tableHeight="'480px'"
        :showSelection="true"
        :tableColumn="tableColumn"
        @selection-change="selectionChange"
        @change="getList"
      ></JFX-table>
    </JFX-Dialog>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { listFileTempInfo } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      openTemplateVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      data: {
        type: Array,
        default() {
          return {}
        }
      },
      type: {
        default: ''
      },
      diyComfirm: {
        default: false
      }
    },
    data() {
      return {
        ruleForm: {
          title: ''
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
        ],
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 获取模板
      async getList() {
        try {
          this.tableData.loading = true
          const { data } = await listFileTempInfo({
            ...this.ruleForm,
            customerIds: this.data.map((item) => item.customerId).toString(),
            type: this.type || 2 // 发票类型
          })
          this.tableData = {
            list: data
          }
        } catch (err) {
          this.tableData.loading = false
        }
      },
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      },
      // 提交
      async comfirm() {
        const chooseList = this.tableChoseList
        const chooseListLength = chooseList.length
        if (chooseListLength !== 1) {
          return this.$errorMsg(
            chooseListLength ? '只能选择一个模板' : '请选择一个模板'
          )
        }
        console.log(chooseList, this.data)
        const selectTmeplate = chooseList[0]
        // 自定义开票状态
        if (this.diyComfirm) {
          this.$emit('comfirm', {
            template: selectTmeplate,
            selectTable: this.data
          })
          return
        }
        // 用于tob应收和 平台结算单 开票跳转
        // const isWp = true
        const isWp =
          selectTmeplate.toUrl && selectTmeplate.toUrl.includes('WEIPIN')
        const isDouble =
          selectTmeplate.toUrl && selectTmeplate.toUrl.includes('DOUBLE')
        const ids = this.data.map((item) => item.id).toString(',')
        const codes = this.data.map((item) => item.code).toString(',')
        this.linkTo(
          `/receiveBill/toInvoicePage?tempId=${
            selectTmeplate.id
          }&ids=${ids}&codes=${codes}${isWp ? '&isWp=1' : ''}${
            isDouble ? '&isDouble=1' : ''
          }&invoiceStatus=1`
        )
        this.$emit('comfirm')
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.invalidAllyEdit .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 130px;
  }
</style>
