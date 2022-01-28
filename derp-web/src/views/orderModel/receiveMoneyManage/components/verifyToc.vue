<template>
  <JFX-Dialog
    :visible.sync="verifyVisible"
    :showClose="true"
    @comfirm="comfirm"
    :width="'1000px'"
    :title="'核销'"
    :btnAlign="'center'"
    top="15vh"
  >
    <el-row>
      <el-col :span="12" class="mr-b-10">应收账单号： {{ data.code }}</el-col>
      <el-col :span="12" class="mr-b-10">客户： {{ data.customerName }}</el-col>
      <el-col :span="12" class="mr-b-10">
        应收金额： {{ data.currency }} {{ data.receivableAmount }}
      </el-col>
    </el-row>
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      @change="getList"
    >
      <template slot="receiveDate" slot-scope="{ row }">
        <template v-if="!row.isEdit">
          {{ row.receiveDate ? row.receiveDate.slice(0, 10) : '' }}
        </template>
        <el-date-picker
          @change="receiveChange"
          v-else
          style="width: 170px"
          v-model="row.receiveDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择日期"
        ></el-date-picker>
      </template>
      <template slot="price" slot-scope="{ row }">
        <template v-if="!row.isEdit">{{ row.price }}</template>
        <el-input-number
          v-else
          v-model="row.price"
          :controls="false"
          :precision="2"
        ></el-input-number>
      </template>
      <template slot="receiceNo" slot-scope="{ row }">
        <template v-if="!row.isEdit">{{ row.receiceNo }}</template>
        <el-input
          v-else
          v-model="row.receiceNo"
          placeholder="输入收款流水号"
        ></el-input>
      </template>
      <template slot="billInDate" slot-scope="{ row }">
        <template v-if="!row.isEdit">{{ row.verifyMonth }}</template>
        <el-date-picker
          v-else
          style="width: 170px"
          v-model="row.billInDate"
          type="month"
          value-format="yyyy-MM"
          placeholder="选择月份"
        ></el-date-picker>
      </template>
    </JFX-table>
    <el-upload
      class="mr-b-10"
      ref="upload"
      :data="{
        token,
        code: data.code
      }"
      :action="attachmentUploadFiles"
      :auto-upload="false"
    >
      <el-button slot="trigger" size="small" type="primary">附件上传</el-button>
    </el-upload>
  </JFX-Dialog>
</template>

<script>
  import commomMix from '@m/index'
  import { attachmentUploadFiles } from '@a/base/index'
  import {
    toCverify,
    tocSaveReceiveBillVerify,
    getVerifyMonth
  } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      verifyVisible: {
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
        attachmentUploadFiles,
        token: sessionStorage.getItem('token'),
        tableColumn: [
          {
            label: '本期收款日期',
            slotTo: 'receiveDate',
            minWidth: '200',
            align: 'center',
            need: true
          },
          {
            label: '本期核销金额',
            slotTo: 'price',
            minWidth: '150',
            align: 'center',
            need: true
          },
          {
            label: '收款流水号',
            slotTo: 'receiceNo',
            minWidth: '150',
            align: 'center'
          },
          {
            label: '核销月份',
            slotTo: 'billInDate',
            minWidth: '200',
            align: 'center',
            need: true
          },
          {
            label: '核销人',
            prop: 'verifier',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '核销时间',
            prop: 'verifyDate',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        selectVisible: {
          show: false,
          data: {}
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      async getList() {
        try {
          this.tableData.loading = true
          const { data } = await toCverify({
            id: this.data.id
          })
          this.tableData.list = data
          this.addItem()
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 新增核销记录
      addItem() {
        this.tableData.list.push({
          id: Math.random(),
          isEdit: Math.random(),
          receiveDate: '',
          price: '',
          receiceNo: '',
          billInDate: ''
        })
      },
      // 收款日期改变
      async receiveChange() {
        const { receiveDate } = this.tableData.list.slice(-1)[0]
        if (!receiveDate) {
          return
        }
        const { data } = await getVerifyMonth({
          receiveDateStr: receiveDate,
          // receiveDateStr: '2021-01-01',
          buId: this.data.buId || this.data.code
        })
        const verifyMonth = data.verifyMonth
        if (verifyMonth) {
          this.tableData.list.slice(-1)[0].billInDate = verifyMonth
        }
      },
      // 点击保存
      async comfirm() {
        // 校验
        const { receiceNo, receiveDate, price, billInDate } =
          this.tableData.list.slice(-1)[0]
        if (!receiveDate || !price || !billInDate) {
          return this.$errorMsg('本期收款日期,本期核销金额,核销月份必填')
        }
        // 上传文件
        try {
          this.$refs.upload.submit()
          const submitJson = {
            billId: this.data.id,
            receiceNo,
            receiveDateStr: receiveDate,
            verifyMonth: billInDate,
            price
          }
          console.log(submitJson)
          await tocSaveReceiveBillVerify(submitJson)
          this.$successMsg('核销成功')
          this.$emit('comfirm')
        } catch (err) {
          console.log(err)
        }
      }
    }
  }
</script>

<style></style>
