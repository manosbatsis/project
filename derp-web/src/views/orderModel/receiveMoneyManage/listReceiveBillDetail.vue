<template>
  <!-- 应收详情页面 -->
  <div class="page-bx edit-bx bgc-w">
    <JFX-Breadcrumb showGoback />
    <el-tabs v-model="tabIndex">
      <el-tab-pane label="应收汇总" name="1" class="pd-15">
        <div class="mr-b-20"></div>
        <div class="fs-22 clr-I flex-c-c mr-b-20">
          {{ detail.month }}{{ bill.customerName }}应收账单
        </div>
        <div class="fs-14 flex-b-c mr-b-20" style="color: blue">
          <div>事业部：{{ bill.buName }}</div>
          <div>账单创建日期：{{ bill.createDate }}</div>
        </div>
        <el-row :gutter="10" class="fs-12 clr-II">
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            客户：{{ bill.customerName || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            应收账单号：{{ bill.code || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <JFX-more-btn
              v-if="bill.relCode"
              :isShowBtn="bill.relCode && bill.relCode.split('&').length > 1"
            >
              <template slot="more">
                关联业务单：{{ bill.relCode.split('&').slice(0, 1).join('&') }}
              </template>
              <template slot="hide">关联业务单：{{ bill.relCode }}</template>
            </JFX-more-btn>
            <template v-else>关联业务单："- -"</template>
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            开票状态：{{ bill.invoiceStatusLabel || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            发票号码：{{ bill.invoiceNo || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            账单状态：{{ bill.billStatusLabel || '- -' }}
            <el-button
              style="display: contents"
              type="text"
              v-if="['02', '03', '04'].includes(bill.billStatus)"
              @click="linkToReceiveBillVerification(bill.code)"
            >
              收款跟踪
            </el-button>
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <JFX-more-btn
              v-if="bill.poNo"
              :isShowBtn="bill.poNo && bill.poNo.split('&').length > 1"
            >
              <template slot="more">
                PO号：{{ bill.poNo.split('&').slice(0, 1).join('&') }}
              </template>
              <template slot="hide">PO号：{{ bill.poNo }}</template>
            </JFX-more-btn>
            <template v-else>PO号：- -</template>
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            渠道：{{ bill.ncChannelName || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            销售模式：{{ bill.saleModelLabel || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            结算类型：{{ bill.settlementTypeLabel || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            分类：{{ bill.sortTypeLabel || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            入账日期：{{ bill.creditDate || '- -' }}
          </el-col>
        </el-row>
        <table class="pure-table pure-table-bordered clr-I fs-12">
          <tbody>
            <tr>
              <td colspan="7">应收款项</td>
            </tr>
            <tr>
              <td colspan="2">项目</td>
              <td>数量</td>
              <td>金额（含税）</td>
              <td>税额</td>
              <td>金额（不含税）</td>
              <td>币种</td>
            </tr>
            <tr v-for="(item, index) in receiveMap" :key="index + 't1'">
              <td v-if="index === 0" :rowspan="receiveMap.length">
                商品销售收入
              </td>
              <td>{{ item.project_name }}</td>
              <td>{{ item.totalNum }}</td>
              <td>{{ item.totalTaxAmount }}</td>
              <td>{{ item.totalTax }}</td>
              <td>{{ item.totalPrice }}</td>
              <td>{{ bill.currency }}</td>
            </tr>
            <tr v-for="(item, index) in deductionMap" :key="index + 't2'">
              <td>{{ item.parentProjectName }}</td>
              <td>{{ item.projectName }}</td>
              <td>{{ item.num }}</td>
              <td>{{ item.taxAmount }}</td>
              <td>{{ item.tax }}</td>
              <td>{{ item.price }}</td>
              <td>{{ bill.currency }}</td>
            </tr>
            <tr>
              <td colspan="2">合计总额：{{ detail.totalPriceLabel }}</td>
              <td>{{ detail.totalNum }}</td>
              <td>{{ detail.totalTaxAmount }}</td>
              <td>{{ detail.totalTax }}</td>
              <td>{{ detail.totalPrice }}</td>
              <td>{{ bill.currency }}</td>
            </tr>
          </tbody>
        </table>
      </el-tab-pane>
      <el-tab-pane label="应收详情" name="2" class="pd-15">
        <div class="mr-b-20"></div>
        <div class="fs-22 clr-I flex-c-c mr-b-20">
          {{ detail.month }}{{ bill.customerName }}应收账单
        </div>
        <div class="fs-14 flex-b-c mr-b-20" style="color: blue">
          <div>事业部：{{ bill.buName }}</div>
          <div>账单创建日期：{{ bill.createDate }}</div>
        </div>
        <el-row :gutter="10" class="fs-12 clr-II">
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            账期：{{ bill.accountPeriod || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            开票日期：{{ bill.invoiceDate || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            结算总额：{{ detail.totalPrice || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            结算币种：{{ bill.currency || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            已回款：{{ detail.alreadyPrice || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            未回款：{{
              (detail.totalPrice || 0) - (detail.alreadyPrice || 0) || '- -'
            }}
          </el-col>
        </el-row>
        <el-tabs v-model="innerTab">
          <el-tab-pane name="1" label="应收明细" class="pd-15">
            <el-row class="mr-b-20">
              <el-button v-if="isEdit" type="primary" @click="delItem">
                删除
              </el-button>
              <el-button v-if="isEdit" type="primary" @click="importItem">
                导入
              </el-button>
              <el-button
                v-if="isEdit && ['1', '2', '3', '7'].includes(bill.orderType)"
                type="primary"
                @click="chooseItem"
              >
                选择单据
              </el-button>
            </el-row>
            <JFX-table
              :tableData.sync="itemList.tableData"
              :tableColumn="itemList.column"
              showSelection
              @selection-change="(rows) => (itemList.choseList = rows)"
            >
              <template slot="projectName" slot-scope="{ row, $index }">
                <template v-if="!isAudit">{{ row.projectName }}</template>
                <el-input
                  v-else
                  readonly
                  @click.native="selectProject(row, $index)"
                  v-model="row.projectName"
                ></el-input>
              </template>
            </JFX-table>
          </el-tab-pane>
          <el-tab-pane name="2" label="费用明细" class="pd-15">
            <JFX-table
              :tableData.sync="costItemDTOS.tableData"
              :tableColumn="costItemDTOS.column"
              showSelection
            >
              <template slot="projectName" slot-scope="{ row, $index }">
                <template v-if="!isAudit">{{ row.projectName }}</template>
                <el-input
                  v-else
                  readonly
                  @click.native="selectProject(row, $index)"
                  v-model="row.projectName"
                ></el-input>
              </template>
            </JFX-table>
          </el-tab-pane>
        </el-tabs>
      </el-tab-pane>
      <el-tab-pane label="附件审批记录" name="3" class="pd-15">
        <JFX-title title="附件列表" className="mr-t-10" />
        <JFX-upload
          v-if="action"
          @success="successUpload"
          :url="action"
          :limit="10000"
          :multiple="true"
        >
          <el-button id="sale-upload-btn" type="primary">上传文件</el-button>
        </JFX-upload>
        <EnclosureList
          v-if="bill.code"
          :showAction="true"
          :code="bill.code"
          ref="enclosure"
          class="mr-t-15"
        />
        <JFX-title title="操作记录" className="mr-t-10" />
        <JFX-table
          :tableData.sync="operator.tableData"
          :tableColumn="operator.column"
          showSelection
        ></JFX-table>
      </el-tab-pane>
      <el-tab-pane label="核销记录" name="4" class="pd-15">
        <JFX-title title="NC信息" className="mr-t-10" />
        <el-row :gutter="10" class="fs-12 clr-II mr-b-20">
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            NC单据号：{{ bill.ncCode || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            NC单据状态：{{ bill.ncStatusLabel || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            NC同步时间：{{ bill.ncSynDate || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            NC同步人：{{ bill.synchronizer || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            凭证编号：{{ bill.voucherCode || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            凭证状态：{{ bill.voucherStatusLabel || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            凭证名称：{{ bill.voucherName || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            凭证序号：{{ bill.voucherIndex || '- -' }}
          </el-col>
          <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            会计期间：{{ bill.period || '- -' }}
          </el-col>
        </el-row>
        <!-- <el-row class="mr-b-10 mr-t-10">
          <el-button type="primary" v-if="isEdit" @click="addVerify">
            勾稽预收单
          </el-button>
          <el-button type="primary" v-if="isEdit" @click="delVerify">
            删除
          </el-button>
        </el-row> -->
        <JFX-table
          :tableData.sync="verifyList.tableData"
          :tableColumn="verifyList.column"
          showSelection
          @selection-change="(rows) => (verifyList.choseList = rows)"
        ></JFX-table>
      </el-tab-pane>
    </el-tabs>
    <!-- 审核 start -->
    <el-row class="mr-t-15 mr-b-15 pd-15" v-if="isAudit">
      <JFX-Form :model="audit.form" ref="auditForm" :rules="audit.rules">
        <el-form-item label="结算类型：" prop="settlementTypeLabel">
          <el-input
            readonly=""
            v-model="audit.form.settlementTypeLabel"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="入账日期："
          prop="creditDate"
          v-if="bill.billStatus !== '05'"
        >
          <el-date-picker
            v-model="audit.form.creditDate"
            :clearable="false"
            :format="'yyyy-MM-dd'"
            :value-format="'yyyy-MM-dd'"
            type="date"
            placeholder="选择日期时间"
            :pickerOptions="audit.pickerOptions"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="审核结果：" prop="auditResult">
          <el-radio-group
            v-model="audit.form.auditResult"
            @change="auditResultChange"
          >
            <el-radio :label="'00'">审批通过</el-radio>
            <el-radio :label="'01'">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注：" prop="auditRemark">
          <el-input type="textarea" v-model="audit.form.auditRemark"></el-input>
        </el-form-item>
      </JFX-Form>
    </el-row>
    <!-- 审核 end -->
    <!-- 新增核销记录 start -->
    <ChoseVerifyAdvanceBill
      v-if="verifyList.add.visible.show"
      :visible="verifyList.add.visible"
      :data="verifyList.add.data"
      @comfirm="addVerifyAfter"
    />
    <!-- 新增核销记录 end -->
    <!-- 应收明细导入 start -->
    <JFX-Dialog
      :visible.sync="importItems.visible"
      :showClose="true"
      :width="'860px'"
      :top="'3vh'"
      title="应收明细导入"
      :showFooter="false"
      @comfirm="importItems.visible.show = false"
    >
      <!-- 费用明细导入 -->
      <ImportFile
        :url="importReceiveItemsUrl"
        :selfDown="true"
        :filterData="importItems.filterData"
        :downText="'明细导出'"
        @downup="downItemsTemplate"
        @successUpload="importItemsSuccess"
      ></ImportFile>
    </JFX-Dialog>
    <!-- 应收明细导入 end -->
    <!-- 选择费项 start -->
    <ChooseSettlement
      v-if="settlement.visible.show"
      :settlementVisible="settlement.visible"
      :customerId="settlement.customerId"
      @comfirm="selectProjectAfter"
    ></ChooseSettlement>
    <!-- 选择费项 end -->
    <!-- 选择单据 -->
    <ChooseDocument
      v-if="chooseDocument.visible.show"
      :selectDocumentVisible="chooseDocument.visible"
      :billData="bill"
      @comfirm="chooseItemAfter"
    ></ChooseDocument>
    <!-- 选择单据 end -->
    <div class="mr-t-30 flex-c-c">
      <el-button v-if="isEdit" @click="addMoney" type="primary">
        添加补扣款
      </el-button>
      <el-button v-if="isEdit" @click="submit" type="primary">提交</el-button>
      <el-button v-if="isEdit" @click="save" type="primary">保存</el-button>
      <el-button v-if="isAudit" @click="auditToB" type="primary">
        审核
      </el-button>
      <el-button @click="closeTag">取消</el-button>
    </div>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getDetail,
    importReceiveItemsUrl,
    exportReceiveItemsUrl,
    ToBsubmitReceiveBill,
    ToBauditItem,
    saveReceiveBillFromEdit
  } from '@a/receiveMoneyManage/index'
  import { attachmentUploadFiles } from '@a/base/index'
  import { getBaseUrl } from '@u/tool'
  import { exportFilePost } from '@u/common'
  export default {
    mixins: [commomMix],
    components: {
      // 附件列表
      EnclosureList: () => import('@c/enclosureList/index'),
      // 选择新增核销记录
      ChoseVerifyAdvanceBill: () =>
        import('./components/ChoseVerifyAdvanceBill'),
      // 导入组件
      ImportFile: () => import('@/components/importfile/index'),
      // 选择单据
      ChooseDocument: () =>
        import('./components/ChooseDocumentReceiveBillDetail'),
      // 选择费项
      ChooseSettlement: () => import('./components/ChooseSettlement')
    },
    data() {
      return {
        importReceiveItemsUrl,
        tabIndex: '1',
        innerTab: '1',
        isEdit: false, // 是否是编辑模式
        isAudit: true, // 是否是审核模式
        action: '', // 上传附件地址rul
        detail: {}, // 总详情集合
        bill: {}, // 账单信息
        receiveMap: {}, // 应收汇总表格
        deductionMap: {}, // 应收汇总表格
        // 应收明细
        itemList: {
          column: [
            { label: '系统业务单号', prop: 'code', align: 'center' },
            { label: '结算费项', slotTo: 'projectName', align: 'center' },
            { label: 'PO号', prop: 'poNo', align: 'center' },
            { label: '平台SKU编码', prop: 'platformSku', align: 'center' },
            { label: '标准条码', prop: 'commbarcode', align: 'center' },
            { label: '商品货号', prop: 'goodsNo', align: 'center' },
            { label: '商品名称', prop: 'goodsName', align: 'center' },
            { label: '税率', prop: 'taxRate', align: 'center' },
            { label: '数量', prop: 'num', align: 'center' },
            { label: '结算金额（不含税）', prop: 'price', align: 'center' },
            { label: '税额', prop: 'tax', align: 'center' },
            { label: '结算金额（含税）', prop: 'taxAmount', align: 'center' },
            { label: '母品牌', prop: 'parentBrandName', align: 'center' },
            { label: 'NC收支费项', prop: 'paymentSubjectName', align: 'center' }
          ],
          tableData: { list: [] },
          choseList: []
        },
        importItems: {
          visible: { show: false },
          filterData: {}
        },
        // 选择单据
        chooseDocument: {
          visible: { show: false }
        },
        // 费用明细
        costItemDTOS: {
          column: [
            { label: '费用类型', slotTo: 'projectName', align: 'center' },
            { label: '类型', prop: 'billTypeLabel', align: 'center' },
            { label: 'PO号', prop: 'poNo', align: 'center' },
            { label: '标准条码', prop: 'commbarcode', align: 'center' },
            { label: '商品货号', prop: 'goodsNo', align: 'center' },
            { label: '商品名称', prop: 'goodsName', align: 'center' },
            { label: '发票描述', prop: 'invoiceDescription', align: 'center' },
            { label: '母品牌', prop: 'parentBrandName', align: 'center' },
            { label: '数量', prop: 'num', align: 'center' },
            { label: '税率', prop: 'taxRate', align: 'center' },
            { label: '费用金额（不含税）', prop: 'price', align: 'center' },
            { label: '税额', prop: 'tax', align: 'center' },
            { label: '费用金额（含税）', prop: 'taxAmount', align: 'center' },
            { label: '备注', prop: 'remark', align: 'center' }
          ],
          tableData: { list: [] }
        },
        // 核销记录
        verifyList: {
          column: [
            {
              label: '核销单号',
              prop: 'advanceCode',
              align: 'center'
            },
            {
              label: '单据类型',
              prop: 'typeLabel',
              align: 'center'
            },
            {
              label: '收款日期',
              prop: 'receiveDate',
              align: 'center'
            },
            {
              label: '核销金额',
              prop: 'price',
              align: 'center'
            },
            {
              label: '收款流水单号',
              prop: 'receiceNo',
              align: 'center',
              hide: true
            },
            {
              label: '核销人',
              prop: 'verifier',
              align: 'center',
              hide: true
            },
            {
              label: '核销时间',
              prop: 'verifyDate',
              align: 'center',
              hide: true
            }
          ],
          tableData: {
            list: []
          },
          // 新增核销记录模板框
          add: {
            visible: { show: false },
            data: {}
          },
          choseList: []
        },
        // 操作记录
        operator: {
          column: [
            { label: '操作人', prop: 'operator', align: 'center' },
            { label: '操作结果', prop: 'operateNodeLabel', align: 'center' },
            { label: '备注', prop: 'remark', align: 'center' },
            { label: '操作时间', prop: 'operateDate', align: 'center' }
          ],
          tableData: { list: [] }
        },
        // 审核
        audit: {
          form: {
            settlementTypeLabel: '',
            creditDate: '',
            auditResult: '00',
            auditRemark: ''
          },
          rules: {
            settlementTypeLabel: { required: true },
            creditDate: { required: true, message: '请选择入账日期' },
            auditResult: { required: true, message: '请选择审核结果' },
            auditRemark: { required: true, message: '请输入审核备注' }
          },
          pickerOptions: {
            disabledDate(time) {
              return time.getTime() > Date.now()
            }
          }
        },
        // 选择费项
        settlement: {
          visible: { show: false },
          customerId: '',
          row: '',
          index: ''
        }
      }
    },
    async mounted() {
      const { id, isEdit, isAudit } = this.$route.query
      this.isEdit = !!isEdit

      const { data } = await getDetail({ id })
      this.detail = data
      this.bill = data.bill
      this.receiveMap = data.receiveMap || []
      this.deductionMap = data.deductionMap || []
      this.verifyList.tableData.list = data.verifyItemModels || []
      this.itemList.tableData.list = data.itemDTOS || []
      this.costItemDTOS.tableData.list = data.costItemDTOS || []
      this.operator.tableData.list = data.receiveBillOperateModels || []
      // 上传附件地址rul
      this.action =
        getBaseUrl(attachmentUploadFiles) +
        attachmentUploadFiles +
        '?token=' +
        sessionStorage.getItem('token') +
        '&code=' +
        data.bill.code
      // 审核处理
      this.isAudit = !!isAudit
      if (this.isAudit) {
        this.audit.form.settlementTypeLabel = this.bill.settlementTypeLabel
        // 作废不显示creditDate 入账日期不必填
        if (this.bill.billStatus === '05') {
          this.audit.rules.creditDate.required = false
        }
      }
    },
    methods: {
      // 跳转到收款跟踪
      linkToReceiveBillVerification(code) {
        this.linkTo({
          path: '/receiveBillVerification/list',
          params: {
            code
          }
        })
      },
      // 附件上传成功
      successUpload(res) {
        if (res.code + '' === '10000') {
          setTimeout(() => {
            this.$refs.enclosure.getEnclosureList(1)
          }, 1000)
        } else {
          this.$errorMsg(res.data)
        }
      },
      // 导入应付明细
      importItem() {
        this.importItems.filterData = { billId: this.bill.id }
        this.importItems.visible.show = true
      },
      // 下载模板
      downItemsTemplate() {
        const itemList = this.itemList.tableData.list.map((item) => {
          return {
            projectCode: item.projectCode || '',
            barcode: item.barcode || '',
            code: item.code || '',
            projectName: item.projectName || '',
            poNo: item.poNo || '',
            platformSku: item.platformSku || '',
            goodsNo: item.goodsNo || '',
            taxRate: item.taxRate ? String(item.taxRate) : '',
            num: item.num || '',
            price: item.price ? String(item.price) : ''
          }
        })
        exportFilePost(exportReceiveItemsUrl, {
          json: JSON.stringify({ itemList })
        })
      },
      // 导入成功
      importItemsSuccess(res) {
        if (res.data.failure + '' !== '0') {
          return false
        }
        this.importItems.visible.show = false
        const importList = res.data.itemList
        // 先清空
        this.itemList.tableData.list = []
        importList.forEach((item) => {
          if (!item.id) {
            item.id = Math.random()
          }
          this.itemList.tableData.list.push(item)
        })
      },
      // 删除应付明细
      delItem() {
        if (!this.itemList.choseList.length) {
          return this.$errorMsg('请至少选择一条')
        }
        const choseids = this.itemList.choseList.map((item) => item.id)
        this.itemList.tableData.list = this.itemList.tableData.list.filter(
          (item) => !choseids.includes(item.id)
        )
      },
      // 选择单据
      chooseItem() {
        this.chooseDocument.visible.show = true
      },
      // 选择单据回填
      chooseItemAfter(list) {
        this.chooseDocument.visible.show = false
        list.forEach((item) => {
          if (!item.id) {
            item.id = Math.random()
          }
          this.itemList.tableData.list.push(item)
        })
      },
      // 选择费项
      selectProject(row, index) {
        this.settlement.visible.show = true
        this.settlement.customerId = String(this.bill.customerId)
        this.settlement.row = row
        this.settlement.index = index
      },
      // 选择费项回填
      selectProjectAfter(selectProject) {
        this.settlement.visible.show = false
        const data = this.settlement.row
        data.projectName = selectProject.projectName
        data.projectCode = selectProject.projectCode
        data.projectId = selectProject.id
      },
      // 新增核销记录
      addVerify() {
        const { currency, buId, customerId, id } = this.bill
        this.verifyList.add.data = {
          currency,
          buId,
          customerId,
          receiveBillId: id,
          ids: this.verifyList.tableData.list
            .filter((item) => item.advanceItemId || item.advanceId)
            .map((item) => item.advanceItemId || item.advanceId)
            .toString(',')
        }
        this.verifyList.add.visible.show = true
      },
      // 新增核销记录回调
      addVerifyAfter(addList) {
        this.verifyList.add.visible.show = false
        addList.map((item) => {
          this.verifyList.tableData.list.push({
            advanceItemId: item.advanceItemId,
            advanceCode: item.code,
            typeLabel: '预收款单',
            receiveDate: item.receiveDate,
            price: item.amount,
            receiceNo: item.orderCode,
            verifier: item.verifier,
            verifyDate: item.verifyDate
          })
        })
      },
      // 删除核销记录
      delVerify() {
        if (!this.verifyList.choseList.length) {
          return this.$errorMsg('请至少选择一条')
        }
        const choseids = this.verifyList.choseList.map(
          (item) => item.advanceItemId
        )
        this.verifyList.tableData.list = this.verifyList.tableData.list.filter(
          (item) => !choseids.includes(item.advanceItemId)
        )
      },
      // 添加补扣款
      addMoney() {
        this.$showToast(
          '导入应收明细请先保存，跳转到补扣款页面数据将丢失，是否跳转',
          async () => {
            this.closeTagAndJump(
              `/receiveBill/addBillCost?id=${this.bill.id}&customerId=${this.bill.customerId}&sortType=${this.bill.sortType}`
            )
          }
        )
      },
      // 获取参数
      getParam() {
        const advanceIds = this.verifyList.tableData.list
          .map((item) => item.advanceItemId || item.advanceId)
          .join(',')
        const itemList = this.itemList.tableData.list.map((item) => {
          return {
            projectId: item.projectId,
            projectCode: item.projectCode,
            goodsId: item.goodsId,
            parentBrandId: item.parentBrandId,
            code: item.code,
            poNo: item.poNo,
            platformSku: item.platformSku,
            goodsNo: item.goodsNo,
            taxRate: item.taxRate,
            num: item.num,
            price: item.price
          }
        })
        return {
          billId: this.bill.id,
          advanceIds,
          itemList
        }
      },
      // 提交
      async submit() {
        await ToBsubmitReceiveBill(this.getParam())
        this.$successMsg('提交成功')
        this.closeTag()
        this.linkTo('/receiveBill/list')
      },
      // 保存
      async save() {
        await saveReceiveBillFromEdit(this.getParam())
        this.$successMsg('保存成功')
        this.closeTag()
        this.linkTo('/receiveBill/list')
      },
      // 审核选择结果改变
      auditResultChange() {
        this.audit.rules.creditDate.required =
          this.audit.form.auditResult === '00'
      },
      // 审核
      auditToB() {
        this.$refs.auditForm.validate(async (validate) => {
          if (!validate) {
            return this.$errorMsg('请先完善必填项')
          }
          let flag = true
          const costItemDTOS = this.costItemDTOS.tableData.list.map(
            (item, index) => {
              if (!item.projectId && flag) {
                flag = false
                this.$errorMsg(`费用明细第${index}行，结算费项不能为空`)
              }
              return {
                id: item.id,
                projectId: item.projectId,
                projectName: item.projectName
              }
            }
          )
          const itemDTOS = this.itemList.tableData.list.map((item, index) => {
            if (!item.projectId && flag) {
              flag = false
              this.$errorMsg(`应收明细第${index}行，结算费项不能为空`)
            }
            return {
              id: item.id,
              projectId: item.projectId,
              projectName: item.projectName
            }
          })
          if (!flag) return
          const auditParam = {
            ...this.audit.form,
            billId: this.bill.id,
            costItemDTOS,
            itemDTOS
          }
          await ToBauditItem(auditParam)
          this.$successMsg('操作成功')
          this.closeTag()
          this.linkTo('/receiveBill/list')
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .pure-table {
    border-collapse: collapse;
    border-spacing: 0;
    empty-cells: show;
    border: 1px solid #cbcbcb;
    width: 95%;
    margin-top: 25px;
    text-align: center;
  }
  .pure-table td,
  .pure-table th {
    border-left: 1px solid #cbcbcb;
    border-width: 0 0 0 1px;
    font-size: inherit;
    margin: 0;
    overflow: visible;
    padding: 0.5em 1em;
  }
  .pure-table-bordered td {
    border-bottom: 1px solid #cbcbcb;
  }
  .pure-table td {
    background-color: transparent;
  }
</style>
