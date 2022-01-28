<template>
  <div>
    <JFX-Dialog
      :visible.sync="choseVisible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'850px'"
      :title="'选择预收单'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      top="15vh"
      :appendToBody="true"
    >
      <JFX-search-panel @reset="resetForm" @search="getList(1)">
        <JFX-Form :model="ruleForm" ref="ruleForm">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="预收单号："
                prop="code"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input v-model.trim="ruleForm.code" clearable></el-input>
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
  import { listBeVerifyAdvanceBill } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      choseVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      data: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    data() {
      return {
        ruleForm: {
          code: ''
        },
        tableColumn: [
          {
            label: '预收单号',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '销售单号',
            prop: 'orderCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: 'PO号',
            prop: 'poNo',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '预收金额',
            prop: 'currency',
            minWidth: '140',
            align: 'center',
            hide: true,
            formatter(row) {
              return row.currency + ' ' + row.amount
            }
          },
          {
            label: '待核销金额',
            prop: 'beVerifyAmount',
            minWidth: '140',
            align: 'center',
            hide: true,
            formatter(row) {
              return row.currency + ' ' + row.beVerifyAmount
            }
          },
          {
            label: '单据状态',
            prop: 'billStatusLabel',
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
          const { data } = await listBeVerifyAdvanceBill({
            ...this.ruleForm,
            ...this.data
          })
          if (data.list && data.list.length) {
            data.list = data.list.filter((item) => item.beVerifyAmount)
          } else {
            data.list = []
          }
          this.tableData = {
            list: data.list || []
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
        if (chooseListLength === 0) {
          return this.$errorMsg('请至少选一条记录')
        }
        this.$emit('comfirm', chooseList)
      }
    }
  }
</script>
