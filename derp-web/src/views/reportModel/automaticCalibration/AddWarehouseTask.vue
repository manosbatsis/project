<template>
  <!-- 预售订单新增/编辑页面 -->
  <div class="page-bx bgc-w edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <div class="flex-c-c">
      <JFX-Form class="mr-t-20" :model="ruleForm" ref="ruleForm" :rules="rules">
        <el-form-item label="数据源：" prop="dataSource" ref="dataSource">
          <el-radio
            v-model="ruleForm.dataSource"
            label="1"
            @change="dataSourceChange"
          >
            GSS报表
          </el-radio>
          <el-radio
            v-model="ruleForm.dataSource"
            label="2"
            @change="dataSourceChange"
          >
            菜鸟后台
          </el-radio>
        </el-form-item>
        <el-form-item label="核对仓库：" prop="outDepotId" ref="outDepotId">
          <el-radio
            v-model="ruleForm.outDepotId"
            v-for="item in depotList"
            :label="item.label"
            :key="item.label"
            @change="outDepotChange"
          >
            {{ item.name }}
          </el-radio>
        </el-form-item>
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
        <el-form-item label="上传文件：" prop="file" ref="file">
          <el-row type="flex">
            <el-upload
              action=""
              :on-change="selectFile"
              :show-file-list="false"
              :auto-upload="false"
            >
              <el-button slot="trigger" type="primary"> 选取文件 </el-button>
            </el-upload>
            <el-button
              type="text"
              style="padding: 0 0 0 10px"
              @click="downTemplate(130)"
            >
              卓志保税仓导入模板
            </el-button>
            <el-button type="text" @click="downTemplate(131)">
              菜鸟仓导入模板
            </el-button>
          </el-row>
        </el-form-item>
        <el-form-item>
          <div style="color: #797979; padding-left: 130px">
            {{ fileName ? '上传文件：' + fileName : '' }}
          </div>
        </el-form-item>
      </JFX-Form>
    </div>
    <el-row class="flex-c-c mr-t-20">
      <el-button type="primary" @click="onSaveForm" :loading="saveBtnLoading">
        开始核对
      </el-button>
      <el-button @click="closeTag">取销</el-button>
    </el-row>
    <JFX-title title="说明" className="mr-t-10" />
    <div class="clr-II desc">
      <p>1、请按导入模板格式及字段导入数据</p>
      <p>
        2、若核对的仓库是卓志保税仓，导入的数据来自GSS库存流水报表；若核对的仓库是菜鸟仓，导入的数据来自菜鸟后台库存流水
      </p>
      <p>3、若核对的是卓志保税仓：</p>
      <ul>
        <li>
          1)、系统只核对业务类型为：3 4 5 6 13 14 15 16 31 32 62 63 66 67 80 81
          84 85 100 104的数据。
        </li>
        <li>
          2)、当业务类型为3/4时，业务单号填入ERP采购单或调拨指令单或代销退货订单
        </li>
        <li>
          3)、当业务类型为5/6时，业务单号填入ERP外部电商订单编号或调拨指令单或购销订单或采购退货单
        </li>
        <li>4)、当业务类型为13/14/15/16时，业务单号填入ERP盘点单</li>
        <li>
          5)、当业务类型为31/3/2/62/63/66/67/80/81/84/85，业务单号填入ERP库存类型调整单
        </li>
        <li>6)、当业务类型为100/104，业务单号填入ERP库存调整单</li>
      </ul>
      <p>4、若核对的是菜鸟仓：</p>
      <ul>
        <li>
          1）、系统只核对单据类型为：交易出库 退货入库 采购入库 盘点出库
          盘点入库 普通出库 的数据
        </li>
        <li>2）、请先将EXCEL的货品编码替换成ERP货号</li>
      </ul>
    </div>
  </div>
</template>

<script>
  import { getBaseUrl } from '@u/tool'
  import { downTemplateUrl } from '@a/base'
  import { saveTaskCheckResult, getDepotByDataSource } from '@a/reportManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 基本信息
        ruleForm: {
          dataSource: '',
          outDepotId: '',
          date: [],
          file: null
        },
        // 表单校验规则
        rules: {
          dataSource: {
            required: true,
            message: '请选择数据源',
            trigger: 'change'
          },
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
          file: {
            required: true,
            message: '请选择文件',
            trigger: 'change'
          }
        },
        depotList: [], // 核对仓库列表
        fileName: '', // 文件名
        saveBtnLoading: false // 保存按钮loading
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        this.dataSourceChange('1')
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
            formData.append('taskType', '2')
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
      // 数据源改变
      async dataSourceChange(dataSource) {
        if (dataSource) {
          await this.$nextTick()
          this.$refs.dataSource.clearValidate()
          this.$refs.outDepotId.resetField()
        }
        try {
          const depotCode =
            dataSource === '1' ? 'ERPWMS_360_0402,ERPHK0103' : ''
          const { data } = await getDepotByDataSource({
            dataSource,
            depotCode
          })
          if (data && data.length) {
            this.depotList = data.map((item) => ({
              label: item.id,
              name: item.name
            }))
          }
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        }
      },
      // 下载模板
      downTemplate(templateId) {
        const url =
          getBaseUrl(downTemplateUrl) +
          downTemplateUrl +
          `?token=${sessionStorage.getItem('token')}&ids=${templateId}`
        window.open(url, '_blank')
      },
      async outDepotChange(outDepotId) {
        if (outDepotId) {
          await this.$nextTick()
          this.$refs.outDepotId.clearValidate()
        }
      },
      // 选择文件
      async selectFile(file) {
        this.fileName = file.name
        this.ruleForm.file = file.raw
        if (this.ruleForm.file) {
          await this.$nextTick()
          this.$refs.file.clearValidate()
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.edit-bx .el-form-item__label {
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
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
  .desc {
    margin-left: 10px;
    p {
      margin-bottom: 10px;
    }
    li {
      margin-left: 20px;
      margin-bottom: 10px;
    }
  }
</style>
