<template>
  <div>
    <JFX-Dialog
      :visible.sync="remarkBillVerificationVisible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'800px'"
      :title="'回款备注'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      top="15vh"
    >
      <JFX-Form ref="form" :model="form" :rules="rules" class="remarkEdit">
        <el-form-item label="备注：" prop="notes">
          <el-input type="textarea" v-model="form.notes"></el-input>
        </el-form-item>
      </JFX-Form>
      <JFX-table
        :tableData.sync="tableData"
        :tableColumn="tableColumn"
        @change="getList"
      ></JFX-table>
    </JFX-Dialog>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { saveNotes, toNotesPage } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      remarkBillVerificationVisible: {
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
        form: {
          notes: ''
        },
        confirmBtnLoading: false,
        rules: {
          notes: { required: true, message: '请输入备注', trigger: 'change' }
        },
        tableColumn: [
          {
            label: '备注人',
            prop: 'notesName'
          },
          {
            label: '备注时间',
            prop: 'createDate'
          },
          {
            label: '备注信息',
            prop: 'notes'
          }
        ]
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      async getList() {
        const { receiveCode } = this.data
        try {
          this.tableData.loading = true
          const { data } = await toNotesPage({
            receiveCode
          })
          this.tableData.list = data
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 提交
      async comfirm() {
        this.$refs.form.validate(async (valid) => {
          if (!valid) {
            return this.$errorMsg('请补充必填信息')
          }
          const submitJson = {
            ...this.form,
            receiveCode: this.data.receiveCode
          }
          await saveNotes(submitJson)
          this.$successMsg('操作成功')
          this.$emit('comfirm')
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.remarkEdit .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 80px;
  }
</style>
