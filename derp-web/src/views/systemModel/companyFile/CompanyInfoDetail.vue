<template>
  <!-- 客户额度预警页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        公司简称：{{ detail.name || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        卓志编码：{{ detail.topidealCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        公司全称：{{ detail.fullName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        商品备案价申报系数：{{ detail.priceDeclareRatio || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        备注：{{ detail.remark || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        是否代理：{{ detail.isProxyLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        授权码：{{ detail.permission || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        联系电话：{{ detail.tel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        勾稽完结百分比(%)：{{
          detail.articulationPercent ? detail.articulationPercent * 100 : '- -'
        }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        成本核算币种：{{ detail.accountCurrencyLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        核算单价：{{ detail.accountPriceLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        提醒财务付款邮箱：{{ detail.financePayEmail || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        英文名称：{{ detail.englishName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        接收清关资料邮箱：{{ detail.email || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        盘点结果通知邮箱：{{ detail.inventoryResultEmail || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        注册地址：{{ detail.registeredAddress || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        注册地址(英文)：{{ detail.englishRegisteredAddress || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        注册地类型：{{ detail.registeredTypeLabel || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        注册登记证：{{ detail.registrationCert || '- -' }}
      </el-col>
    </el-row>
    <!-- 基本信息 end -->
    <!-- 银行信息 -->
    <JFX-title title="银行信息" />
    <el-row :gutter="10" class="fs-12 clr-II detail-row">
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        开户银行：{{ detail.depositBank || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        银行账户：{{ detail.beneficiaryName || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        银行账号：{{ detail.bankAccount || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        Swift Code：{{ detail.swiftCode || '- -' }}
      </el-col>
      <el-col class="mr-b-10" :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        开户行地址：{{ detail.bankAddress || '- -' }}
      </el-col>
    </el-row>
    <!-- 银行信息 end -->
    <!-- 仓库信息 -->
    <JFX-title title="仓库信息" class="mr-t-15" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    >
      <template slot="isInOutInstruction" slot-scope="{ row }">
        <div>{{ row.isInOutInstruction === '1' ? '是' : '否' }}</div>
      </template>
      <template slot="isInvertoryFallingPrice" slot-scope="{ row }">
        <div>{{ row.isInvertoryFallingPrice === '1' ? '是' : '否' }}</div>
      </template>
      <template slot="isInDependOut" slot-scope="{ row }">
        <div>{{ row.isInDependOut === '1' ? '是' : '否' }}</div>
      </template>
      <template slot="isOutDependIn" slot-scope="{ row }">
        <div>{{ row.isOutDependIn === '1' ? '是' : '否' }}</div>
      </template>
      <template slot="productRestriction" slot-scope="{ row }">
        <div>
          {{
            { 1: '仅备案商品', 2: '仅外仓统一码', 3: '无限制' }[
              row.productRestriction
            ] || ''
          }}
        </div>
      </template>
    </JFX-table>
    <!-- 仓库信息 end -->
    <!-- 代理公司 -->
    <JFX-title title="代理公司" class="mr-t-15" />
    <JFX-table
      :tableData.sync="agencyTableData"
      :tableColumn="agencyTableColumn"
      :showPagination="false"
    ></JFX-table>
    <!-- 代理公司 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getMerchantDetail, getBuNameByMerchantDepot } from '@a/companyFile'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 详情数据
        detail: {},
        tableColumn: [
          {
            label: '仓库名称',
            prop: 'depotName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '仓库编号',
            prop: 'depotCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '合同编号',
            prop: 'contractCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '进出接口指令',
            slotTo: 'isInOutInstruction',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '统计存货跌价',
            slotTo: 'isInvertoryFallingPrice',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '选品限制',
            slotTo: 'productRestriction',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '以入定出',
            slotTo: 'isInDependOut',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '以出定入',
            slotTo: 'isOutDependIn',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '绑定事业部',
            prop: 'buName',
            width: '150',
            align: 'center',
            hide: false
          }
        ],
        agencyTableData: {
          list: []
        },
        agencyTableColumn: [
          {
            label: '公司简称',
            prop: 'code',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '公司编号',
            prop: 'name',
            minWidth: '140',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.getDetail()
    },
    methods: {
      async getDetail() {
        const { id } = this.$route.query
        if (!id) return false
        try {
          const {
            data: { detail, depotMerchantRelList, list }
          } = await getMerchantDetail({ id })
          this.detail = detail || {}
          if (depotMerchantRelList && depotMerchantRelList.length) {
            this.tableData.list = depotMerchantRelList.map((item) => ({
              ...item,
              buName: ''
            }))
          }
          this.agencyTableData.list = list || []
          if (this.tableData.list.length) {
            this.tableData.list.forEach(async (item) => {
              if (item.depotId) {
                const { data } = await getBuNameByMerchantDepot({
                  depotId: item.depotId,
                  merchantId: id
                })
                item.buName = data || ''
              }
            })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      }
    }
  }
</script>
