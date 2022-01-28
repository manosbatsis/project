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
        应收金额： {{ data.currency }} {{ data.receivablePrice }}
      </el-col>
      <el-col :span="12" class="mr-b-10">发票号码：{{ data.invoiceNo }}</el-col>
    </el-row>
    <el-row class="mr-b-10">
      <el-button type="primary" @click="addVerify"> 勾稽预收单 </el-button>
      <el-button type="primary" @click="addItem"> 新增 </el-button>
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
          v-else
          @change="receiveChange(row)"
          style="width: 170px"
          v-model="row.receiveDate"
          type="date"
          :disabled="row.type === 1"
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
          :disabled="row.type === 1"
          v-model="row.receiceNo"
          placeholder="输入收款流水号"
        ></el-input>
      </template>
      <template slot="verifyMonth" slot-scope="{ row }">
        <template v-if="!row.isEdit">{{ row.verifyMonth }}</template>
        <el-date-picker
          v-else
          style="width: 170px"
          v-model="row.verifyMonth"
          type="month"
          value-format="yyyy-MM"
          placeholder="选择月份"
        ></el-date-picker>
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button type="text" v-if="row.isEdit" @click="del(row)"
          >删除</el-button
        >
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
    <!-- 新增核销记录 start -->
    <ChoseVerifyAdvanceBill
      v-if="selectVisible.show"
      :choseVisible="selectVisible"
      :data="selectVisible.data"
      @comfirm="addVerifyAfter"
    />
    <!-- 新增核销记录 end -->
  </JFX-Dialog>
</template>

<script>
  import commomMix from '@m/index'
  import { attachmentUploadFiles } from '@a/base/index'
  import {
    receiveBillGetVerifyItems,
    saveReceiveBillVerify,
    getVerifyMonth
  } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    components: {
      // 选择新增核销记录
      ChoseVerifyAdvanceBill: () => import('./ChoseVerifyAdvanceBill')
    },
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
            minWidth: '160',
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
            slotTo: 'verifyMonth',
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
          },
          {
            label: '操作',
            slotTo: 'action',
            minWidth: '120',
            align: 'center'
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
          const { data } = await receiveBillGetVerifyItems({
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
          type: 2,
          receiveDate: '',
          price: '',
          receiceNo: '',
          verifyMonth: ''
        })
      },
      // 新增核销记录，选择预收款弹窗
      addVerify() {
        const { currency, buId, customerId, id } = this.data
        this.selectVisible.data = {
          currency,
          buId,
          customerId,
          receiveBillId: id,
          ids: this.tableData.list
            .filter((item) => item.type === 1)
            .map((item) => item.advanceItemId)
            .toString()
        }
        this.selectVisible.show = true
      },
      // 新建
      addVerifyAfter(addList) {
        this.selectVisible.show = false
        addList.map((item) => {
          this.tableData.list.push({
            id: item.advanceId,
            advanceItemId: item.advanceItemId,
            type: 1,
            isEdit: Math.random(),
            receiveDate: item.receiveDate,
            price: item.beVerifyAmount,
            receiceNo: item.code,
            verifier: item.verifier,
            verifyDate: item.verifyDate,
            verifyMonth: item.verifyMonth || ''
          })
        })
      },
      // 删除选的那一行
      del(row) {
        const index = this.tableData.list.findIndex(
          (item) => item.isEdit === row.isEdit
        )
        this.tableData.list.splice(index, 1)
      },
      // 收款日期改变
      async receiveChange(row) {
        const { receiveDate } = row
        if (!receiveDate) {
          return
        }
        const { data } = await getVerifyMonth({
          receiveDateStr: receiveDate,
          buId: this.data.buId
        })
        const verifyMonth = data.verifyMonth
        if (verifyMonth) {
          row.verifyMonth = verifyMonth
        }
      },
      // 点击保存
      async comfirm() {
        // 校验
        const isEmptyData = this.tableData.list.some((item, index) => {
          const { receiveDate, price, verifyMonth } = item
          if (![receiveDate, price, verifyMonth].every((item) => !!item)) {
            this.$errorMsg(
              `第${index + 1}行，本期收款日期,本期核销金额,核销月份必填`
            )
            return true
          }
          return false
        })
        if (isEmptyData) return
        // 上传文件
        try {
          this.$refs.upload.submit()
          const submitJson = {
            billId: this.data.id,
            verifyItems: this.tableData.list
              .filter((item) => item.isEdit)
              .map((item) => {
                return {
                  advanceId: item.type === 1 ? item.id : '',
                  price: item.price,
                  receiceNo: item.receiceNo,
                  receiveDateStr: item.receiveDate.slice(0, 10),
                  type: item.type,
                  verifyMonth: item.verifyMonth
                }
              })
          }
          console.log(submitJson)
          await saveReceiveBillVerify(submitJson)
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
