<template>
  <!-- 预售订单新增/编辑页面 -->
  <div class="page-bx bgc-w sales-add-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <JFX-Form class="mr-t-20" :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="出仓仓库：" prop="outDepotId">
            <el-select
              v-model="ruleForm.outDepotId"
              placeholder="请选择"
              :data-list="getSelectBeanByMerchantRel('warehouseList')"
              filterable
              clearable
              @change="outDepotChange"
            >
              <el-option
                v-for="item in selectOpt.warehouseList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核对日期：" prop="date">
            <el-date-picker
              v-model="ruleForm.date"
              type="datetimerange"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="平台名称："
            prop="storePlatformCode"
            ref="storePlatformCode"
          >
            <el-select
              v-model="ruleForm.storePlatformCode"
              placeholder="请选择"
              clearable
              filterable
              :disabled="depotCode === 'ERPWMS_360_0402'"
              :data-list="getSelectList('storePlatformList')"
            >
              <el-option
                v-for="item in selectOpt.storePlatformList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="店铺：" prop="shopCode" ref="shopCode">
            <el-select
              v-model="ruleForm.shopCode"
              placeholder="请选择"
              clearable
              :disabled="depotCode === 'ERPWMS_360_0402'"
              filterable
            >
              <el-option
                v-for="item in shopList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文件：" prop="file">
            <el-upload
              action=""
              :on-change="selectFile"
              :show-file-list="false"
              :auto-upload="false"
            >
              <el-button slot="trigger" type="primary"> 选取文件 </el-button>
              <span style="color: #797979; margin-left: 6px">
                {{ fileName || '' }}
              </span>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <el-row class="flex-c-c">
      <el-button type="primary" @click="onSaveForm" :loading="saveBtnLoading">
        开始核对
      </el-button>
      <el-button @click="closeTag">取销</el-button>
    </el-row>
    <JFX-title title="填写Excle模板小贴士：" className="mr-t-10" />
    <el-row style="color: #797979">
      <el-col :span="24" style="color: #0000ff">
        一、天猫-出仓仓库为天运二期仓
      </el-col>
      <el-col :span="24">【数据准备顺序】</el-col>
      <el-col :span="24">
        1、订单管理列表页（查询发货时间和已出库状态的查询条件，导出当月的发货单据流水）
      </el-col>
      <el-col :span="24">
        2、【库存中心】-【出入库流水台账】导出当月发货出库的流水数据
      </el-col>
      <el-col :span="24">
        3、以出入库流水中的（外部流水号）对应订单管理的出库数据核对是否一致；并拿到每个订单号的实际发货时间填充回到订单管理订单流水中的发货时间。
      </el-col>
      <el-col :span="24">
        4、以合并的源数据导入到系统POP流水校验进行校验（注：导入的数据表格必须要有
        <span style="color: #ff0000">
          “发货时间”、“交易订单号”、“商品数量”、“货品id”
        </span>
        四个必填字段，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
      </el-col>
    </el-row>
    <el-row style="color: #797979">
      <el-col :span="24" style="color: #0000ff">
        二、天猫-出仓仓库为非天运二期仓
      </el-col>
      <el-col :span="24">【数据准备顺序】</el-col>
      <el-col :span="24">
        1、订单管理列表页（查询发货时间和已出库状态的查询条件，导出当月的发货单据流水）；
      </el-col>
      <el-col :span="24">
        2、库存中心】-【出入库流水台账】导出当月发货出库的流水数据；
      </el-col>
      <el-col :span="24">
        3、以出入库流水中的（外部流水号）对应订单管理的出库数据核对是否一致；并拿到每个订单号的实际发货时间填充回到订单管理订单流水中的发货时间。
      </el-col>
      <el-col :span="24">
        4、以合并的源数据导入到系统POP流水校验进行校验（注：导入的数据表格必须要有
        <span style="color: #ff0000">
          “发货时间”、“交易订单号”、“商品数量”、“货品编码”
        </span>
        四个必填字段，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
      </el-col>
    </el-row>
    <el-row style="color: #797979">
      <el-col :span="24" style="color: #0000ff">三、京东</el-col>
      <el-col :span="24">【数据准备顺序】</el-col>
      <el-col :span="24">
        1、【订单管理】-【订单查询与跟踪】导出当月已出库、已完结的流水数据；
      </el-col>
      <el-col :span="24">
        2、找蓝精灵提供查询商品对照关系的对接接口（【订单中心-订单货号拦截】），当前无对接接口可通过蓝精灵后台导出，或是找蓝精灵开发数据库里拉一份对照关系数据；并人工替换掉商品信息;
      </el-col>
      <el-col :span="24">
        3、将导出的流水数据导入到系统中进行校验（注：导入的数据表格必须要有
        <span style="color: #ff0000">“订单号 ”、“商品ID ”、“订购数量”</span>
        三个必填字段，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
      </el-col>
    </el-row>
    <el-row style="color: #797979">
      <el-col :span="24" style="color: #0000ff">
        四、出仓仓库为综合1仓的电商订单（拼多多、有赞、第e仓、小小包、澳新、斑马）
      </el-col>
      <el-col :span="24">【数据准备顺序】</el-col>
      <el-col :span="24">
        1、原有的流水链接（经分销库存流水明细中的‘业务单据号’）与经分销发货订单中的‘发货单号’关联匹配，以获取完整的数据（包含字段公司名称、仓库名称、商品OPG、商品货号、商品名称、交易类型、交易数量、单位、平台订单号、业务单据号、原始批次号、生产日期、失效日期、交易创建时间、发货时间）；
      </el-col>
      <el-col :span="24">
        2、根据“公司”为维度区分数据，并导入系统做订单校验（注：导入数据表格必须要有
        <span style="color: #ff0000">
          “发货时间”、“平台订单号”、“商品货号”、“交易数量”、“原始批次号”
        </span>
        五个必填字段，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
        【附链接】
        <span style="color: #007bff">仓库收发明细 Ofc电商订单对应关系</span>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    saveTaskCheckResult,
    changeShopCodeLabel,
    getTaskShopList
  } from '@a/reportManage'
  import { getDepotDetails } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 表单数据
        ruleForm: {
          outDepotId: '',
          storePlatformCode: '',
          shopCode: '',
          file: null,
          date: []
        },
        // 表单校验规则
        rules: {
          outDepotId: {
            required: true,
            message: '请选择出仓仓库',
            trigger: 'change'
          },
          date: {
            required: true,
            message: '请选择核对日期',
            trigger: 'change'
          },
          storePlatformCode: {
            required: true,
            message: '请选择平台名称',
            trigger: 'change'
          },
          shopCode: {
            required: true,
            message: '请选择店铺',
            trigger: 'change'
          },
          file: {
            required: true,
            message: '请选择文件',
            trigger: 'change'
          }
        },
        shopList: [], // 店铺列表
        fileName: '', // 文件名
        saveBtnLoading: false, // 保存按钮loading
        depotCode: '' // 出库仓编码
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        this.getShopList() // 获取店铺列表
      },
      // 保存
      onSaveForm() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请正确填写表单信息')
            return false
          }
          this.ruleForm.checkStartDate =
            this.ruleForm.date && this.ruleForm.date.length
              ? this.ruleForm.date[0]
              : ''
          this.ruleForm.checkEndDate =
            this.ruleForm.date && this.ruleForm.date.length
              ? this.ruleForm.date[1]
              : ''
          try {
            this.saveBtnLoading = true
            const formData = new FormData()
            for (const key in this.ruleForm) {
              if (key !== 'date') {
                formData.append(key, this.ruleForm[key])
              }
            }
            formData.append('taskType', '1')
            const { data } = await saveTaskCheckResult(formData)
            this.$successMsg(`已创建任务:${data},请在列表查看进度`)
            this.closeTag()
          } catch (error) {
            this.$errorMsg(error.message || '系统异常')
          } finally {
            this.saveBtnLoading = false
          }
        })
      },
      // 获取店铺列表
      async getShopList() {
        try {
          const { data } = await getTaskShopList()
          if (data && data.length) {
            this.shopList = data.map(({ shopCode, shopName }) => ({
              key: shopCode,
              value: shopName
            }))
          } else {
            this.shopList = []
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 出仓仓库改变
      async outDepotChange(id) {
        try {
          const {
            data: { depotCode }
          } = await getDepotDetails({ id })
          this.depotCode = depotCode || ''
          // 当出仓仓库为综合1仓时
          if (this.depotCode === 'ERPWMS_360_0402') {
            // 平台名称、店铺禁用
            this.rules.shopCode.required = false
            this.rules.storePlatformCode.required = false
            await this.$nextTick()
            // 清空校验
            this.$refs.shopCode.resetField()
            this.$refs.storePlatformCode.resetField()
          } else {
            this.rules.shopCode.required = true
            this.rules.storePlatformCode.required = true
          }
          const {
            data: { shopList }
          } = await changeShopCodeLabel({ outDepotId: id })
          if (shopList && shopList.length) {
            this.shopList = shopList.map(({ shopCode, shopName }) => ({
              key: shopCode,
              value: shopName
            }))
          } else {
            this.getShopList()
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 选择文件
      selectFile(file) {
        this.fileName = file.name
        this.ruleForm.file = file.raw
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.sales-add-bx .el-form-item__label {
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
  ::v-deep.sales-add-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
