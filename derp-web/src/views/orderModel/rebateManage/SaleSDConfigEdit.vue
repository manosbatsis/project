<template>
  <!-- 预售订单新增/编辑页面 -->
  <div class="page-bx bgc-w sales-add-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <div class="mr-t-15"></div>
    <!-- 基本信息 -->
    <!-- <JFX-title title="基本信息" /> -->
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司：" prop="merchantId">
            <el-select
              v-model="ruleForm.merchantId"
              placeholder="请选择"
              @change="changeMerchantIdorReminderType"
            >
              <el-option
                v-for="item in selectOpt.merchant_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="事业部：" prop="buId" ref="buId">
            <el-select
              v-model="ruleForm.buId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in selectOpt.buList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="客户：" prop="customerId">
            <el-select
              v-model="ruleForm.customerId"
              placeholder="请选择"
              clearable
              filterable
              @change="changeCustomerId"
            >
              <el-option
                v-for="item in selectOpt.kuhuList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="生效时间：" prop="effectiveDateStr">
            <el-date-picker
              v-model="ruleForm.effectiveDateStr"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="失效时间：" prop="expirationDateStr">
            <el-date-picker
              v-model="ruleForm.expirationDateStr"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="平台备注：" prop="remark">
            <el-input
              v-model="ruleForm.remark"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <!-- 基本信息 end -->
    <!-- 单比例配置 -->
    <JFX-title title="单比例配置" className="mr-t-15 title-bx" />
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    >
      <template slot="sdType" slot-scope="{ $index, row }">
        <el-select
          v-model="row.sdTypeId"
          placeholder="请选择"
          clearable
          filterable
          @change="(val) => singleListChange(val, $index)"
        >
          <el-option
            v-for="item in row.singleList"
            :key="item.id"
            :label="item.sdType"
            :value="item.id"
          />
        </el-select>
      </template>
      <template slot="proportion" slot-scope="{ row }">
        <JFX-Input
          v-model.trim="row.proportion"
          :precision="5"
          placeholder="请输入"
          clearable
          style="width: 80%"
        />
      </template>
      <template slot="action" slot-scope="{ $index }">
        <el-button
          type="text"
          v-if="$index === tableData.list.length - 1"
          @click="addToSingleList"
        >
          增加
        </el-button>
        <el-button type="text" @click="removeSingleList($index)">
          删除
        </el-button>
      </template>
    </JFX-table>
    <!-- 单比例配置 end -->
    <!-- 多比例配置 -->
    <JFX-title title="多比例配置" className="mr-t-15 title-bx" />
    <div class="flex-b-c mr-b-10 clr-II">
      <div>合计SKU：{{ tableData2.list.length }}个</div>
      <div>
        <el-button type="primary" @click="showImportGoodsModal">导入</el-button>
        <el-button type="primary" @click="removeMultipleList">删除</el-button>
        <el-button type="primary" @click="sdChoseProduct.visible.show = true">
          选择商品
        </el-button>
      </div>
    </div>
    <JFX-table
      :tableData.sync="tableData2"
      :tableColumn="tableColumn2"
      :showPagination="false"
      showSelection
      @selection-change="selectionChange"
    >
      <template slot="sdType" slot-scope="{ $index, row }">
        <el-select
          v-model="row.sdTypeId"
          placeholder="请选择"
          clearable
          filterable
          @change="multipleListChange($index)"
        >
          <el-option
            v-for="item in multipleList"
            :key="item.id"
            :label="item.sdType"
            :value="item.id"
          />
        </el-select>
      </template>
      <template slot="proportion" slot-scope="{ row }">
        <JFX-Input
          v-model.trim="row.proportion"
          :precision="5"
          placeholder="请输入"
          clearable
          style="width: 80%"
        />
      </template>
    </JFX-table>
    <!-- 多比例配置 end -->
    <!-- 底部按钮 -->
    <div class="mr-t-30 flex-c-c">
      <el-button
        @click="handleSubmit('save')"
        type="primary"
        :loading="saveLoading"
      >
        保 存
      </el-button>
      <el-button
        @click="handleSubmit('audit')"
        type="primary"
        :loading="saveLoading"
      >
        审 核
      </el-button>
      <el-button @click="askClose">取 消</el-button>
    </div>
    <!-- 底部按钮 end -->
    <!-- 选择商品 -->
    <SDChoseProduct
      v-if="sdChoseProduct.visible.show"
      :visible.sync="sdChoseProduct.visible"
      :filterData="sdChoseProduct.filterData"
      @comfirm="comfirmChose"
    ></SDChoseProduct>
    <!-- 选择商品 end -->
    <!-- 导入商品 -->
    <JFX-Dialog
      :visible.sync="importGoods.visible"
      :showClose="true"
      :width="'860px'"
      :top="'3vh'"
      btnAlign="center"
      title="导入商品"
      closeBtnText="取 消"
      confirmBtnText="确 认"
      @comfirm="importGoods.visible.show = false"
    >
      <!-- 商品导入 -->
      <ImportFile
        :url="sdImportGoodsUrl"
        :filterData="importGoodsData"
        :templateId="'163'"
        @successUpload="importGoodsSuccess"
      ></ImportFile>
    </JFX-Dialog>
    <!-- 导入商品 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    saveSdSaleConfig,
    sdSaleConfigDetail,
    getSdSaleTypeList,
    sdImportGoodsUrl
  } from '@a/rebateManage/index'
  export default {
    mixins: [commomMix],
    components: {
      SDChoseProduct: () => import('./components/sdchoseProduct'),
      // 导入组件
      ImportFile: () => import('@/components/importfile/index')
    },
    data() {
      return {
        // 表单信息
        ruleForm: {
          id: '',
          merchantId: '',
          merchantName: '',
          buId: '',
          buName: '',
          customerId: '',
          customerName: '',
          effectiveDateStr:
            this.$formatDate(Date.now(), 'yyyy-MM-dd') + ' 00:00:00',
          expirationDateStr:
            this.$formatDate(Date.now(), 'yyyy-MM-dd') + ' 23:59:59',
          remark: '',
          operate: '1'
        },
        // 表单校验规则
        rules: {
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          effectiveDateStr: {
            required: true,
            message: '请选择生效时间',
            trigger: 'change'
          },
          expirationDateStr: {
            required: true,
            message: '请选择失效时间',
            trigger: 'change'
          },
          buId: { required: true, message: '请选择事业部', trigger: 'change' }
        },
        tableColumn: [
          {
            label: 'SD类型',
            slotTo: 'sdType',
            minWidth: '120',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '简称',
            prop: 'sdTypeName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '映射费项',
            prop: 'projectName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '比例',
            slotTo: 'proportion',
            minWidth: '100',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '160',
            align: 'center',
            hide: true
          }
        ],
        tableColumn2: [
          {
            label: 'SD类型',
            slotTo: 'sdType',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '简称',
            prop: 'sdTypeName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '映射费项',
            prop: 'projectName',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '标准品牌',
            prop: 'brandParent',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '标准条码',
            prop: 'commbarcode',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '比例',
            slotTo: 'proportion',
            minWidth: '100',
            align: 'center',
            hide: true,
            need: true
          }
        ],
        tableData2: {
          list: []
        },
        sdChoseProduct: {
          visible: { show: false },
          filterData: {}
        },
        importGoods: {
          visible: { show: false }
        },
        singleList: [],
        multipleList: [],
        saveLoading: false,
        delId: 0,
        sdImportGoodsUrl: sdImportGoodsUrl,
        importGoodsData: {}
      }
    },
    mounted() {
      // 获取公司列表
      const userInfo = this.getUserInfo()
      if (userInfo.userType === '1') {
        // 平台管理用户 admin
        this.getSelectMerchantBean('merchant_list') // 获取该用户下的公司列表
      } else {
        // 商户
        // 当前用户的主体公司
        this.$set(this.selectOpt, 'merchant_list', [])
        this.selectOpt.merchant_list = [
          { key: userInfo.merchantId, value: userInfo.merchantName }
        ]
      }
      this.init()
    },
    methods: {
      init() {
        const { id, type } = this.$route.query
        this.type = type
        this.pageInit()
        if (id) {
          this.editInit()
        }
      },
      // 获取单比例、多比例下拉框
      async getSdSaleTypeList() {
        try {
          const {
            data: { singleList = [], multipleList = [] }
          } = await getSdSaleTypeList()
          this.singleList = singleList || []
          this.multipleList = multipleList || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 页面初始化
      async pageInit() {
        // 获取公司名
        const userInfo = this.getUserInfo()
        this.ruleForm.merchantId = userInfo.merchantId || ''
        this.ruleForm.merchantName = userInfo.merchantName || ''
        this.changeMerchantIdorReminderType('init')
        // 获取单比例、多比例下拉框
        await this.getSdSaleTypeList()
        const { id } = this.$route.query
        if (!id) {
          this.tableData.list.push({ sdType: 1, singleList: this.singleList })
        }
      },
      // 编辑页面初始化
      async editInit() {
        const { id } = this.$route.query
        try {
          const { data } = await sdSaleConfigDetail({ id })
          for (const key in this.ruleForm) {
            this.ruleForm[key] = data[key] ? data[key].toString() : ''
          }
          this.ruleForm.effectiveDateStr = data.effectiveDate || ''
          this.ruleForm.expirationDateStr = data.expirationDate || ''
          this.ruleForm.customerId = data.customerId || ''
          if (data.singleList && data.singleList.length) {
            this.tableData.list = data.singleList.map((item) => ({
              ...item,
              sdTypeId: item.sdTypeId,
              sdTypeName: item.sdSimpleName,
              singleList: []
            }))
          }
          if (data.multipleList && data.multipleList.length) {
            this.tableData2.list = data.multipleList.map((item) => ({
              ...item,
              sdTypeId: item.sdTypeId,
              sdTypeName: item.sdSimpleName,
              delId: this.delId++
            }))
          }
          this.updateItemSingleList()
          if (this.type === 'copy') this.ruleForm.id = ''
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 删除
      removeTableItem() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        if (this.tableChoseList.length) {
          const ids = this.tableChoseList.map((item) => item.id)
          this.tableData.list = this.tableData.list.filter(
            (item) => !ids.includes(item.id)
          )
        }
      },
      // 提交表单
      async handleSubmit(type) {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const flag = this.checkList()
            if (!flag) return false
            const {
              effectiveDateStr,
              expirationDateStr,
              merchantId,
              buId,
              customerId
            } = this.ruleForm
            const start = new Date(effectiveDateStr).getTime()
            const end = new Date(expirationDateStr).getTime()
            if (end < start) {
              this.$errorMsg('失效日期不能小于生效日期')
              return false
            }
            const gdata =
              this.selectOpt.merchant_list.find(
                (item) => merchantId === item.key
              ) || {}
            this.ruleForm.merchantName = gdata.value || ''
            const hdata =
              this.selectOpt.buList.find((item) => buId === item.key) || {}
            this.ruleForm.buName = hdata.value || ''
            const kdata =
              this.selectOpt.kuhuList.find((item) => customerId === item.key) ||
              {}
            this.ruleForm.customerName = kdata.value || ''
            const itemList = []
            if (this.tableData.list.find((item) => item.sdTypeId)) {
              itemList.push(
                ...this.tableData.list.map((item) => {
                  if (item.sdTypeId) {
                    if (this.$route.query.type !== 'copy') {
                      return { ...item }
                    }
                    return { ...item, id: '' }
                  }
                })
              )
            }
            if (this.tableData2.list.find((item) => item.sdTypeId)) {
              itemList.push(
                ...this.tableData2.list.map((item) => {
                  if (item.sdTypeId) {
                    if (this.$route.query.type !== 'copy') {
                      return { ...item }
                    }
                    return { ...item, id: '' }
                  }
                })
              )
            }
            const opt = {
              ...this.ruleForm,
              itemList,
              operate: type === 'save' ? '1' : '2' // 1-保存，2-审核
            }
            this.saveLoading = true
            try {
              await saveSdSaleConfig(opt)
              this.$successMsg('操作成功')
              this.closeTag()
            } catch (error) {
              console.log(error)
            }
            this.saveLoading = false
          } else {
            this.$errorMsg('请先填写必填项')
          }
        })
      },
      // 修改供应商
      changeMerchantIdorReminderType(type) {
        delete this.selectOpt.buList
        delete this.selectOpt.kuhuList
        if (this.ruleForm.merchantId) {
          this.getBUSelectBean('buList', {
            merchantId: this.ruleForm.merchantId
          }) // 获取事业部
          this.getCustomerByMerchantId(
            'kuhuList',
            {
              cusType: 1,
              merchantId: this.ruleForm.merchantId,
              begin: 0,
              pageSize: 10000
            },
            { key: 'id', value: 'name' }
          ) // 获取客户列表
        }
        if (type !== 'init') {
          // 非初始情况
          this.ruleForm.customerId = ''
          this.ruleForm.buId = ''
        }
      },
      // 显示商品导入弹窗
      showImportGoodsModal() {
        this.selfDown = true
        this.importGoods.visible.show = true
      },
      checkList() {
        let flag = true
        if (
          this.tableData.list.find(
            (item) => !item.sdTypeId && !item.proportion
          ) &&
          !this.tableData2.list.length
        ) {
          this.$errorMsg('单比例、多比例配置不能都为空')
          flag = false
          return flag
        }
        if (
          this.tableData.list.find(
            (item) =>
              (item.sdTypeId && !item.proportion) ||
              (!item.sdTypeId && item.proportion)
          )
        ) {
          for (let i = 0; i < this.tableData.list.length; i++) {
            const { proportion, sdTypeId } = this.tableData.list[i]
            if (!sdTypeId) {
              this.$errorMsg(`单比例配置表格第${i + 1}行，SD类型不能为空`)
              flag = false
              break
            }
            if (!proportion) {
              this.$errorMsg(`单比例配置表格第${i + 1}行，比例不能为空`)
              flag = false
              break
            }
          }
        }
        if (this.tableData2.list.length) {
          for (let i = 0; i < this.tableData2.list.length; i++) {
            const { proportion, sdTypeId } = this.tableData2.list[i]
            if (!sdTypeId) {
              this.$errorMsg(`多比例配置表格第${i + 1}行，SD类型不能为空`)
              flag = false
              break
            }
            if (!proportion) {
              this.$errorMsg(`多比例配置表格第${i + 1}行，比例不能为空`)
              flag = false
              break
            }
          }
        }
        return flag
      },
      comfirm(list) {
        let flag = true
        // 校验
        list.forEach((item) => {
          const {
            sdTypeId,
            categoryId,
            brandParentId,
            isAllBrandParent,
            superiorParentBrandName,
            brandParentName,
            superiorParentBrandId
          } = item
          let target = null
          if (isAllBrandParent === '1') {
            // 全子品牌
            target = this.tableData.list.find(
              (gtem) =>
                +sdTypeId === +gtem.sdTypeId &&
                +categoryId === +gtem.categoryId &&
                +superiorParentBrandId === +gtem.superiorParentBrandId &&
                +isAllBrandParent === +gtem.isAllBrandParent
            )
          } else {
            target = this.tableData.list.find(
              (gtem) =>
                +sdTypeId === +gtem.sdTypeId &&
                +categoryId === +gtem.categoryId &&
                +superiorParentBrandId === +gtem.superiorParentBrandId &&
                +brandParentId === +gtem.brandParentId
            )
          }
          if (target) {
            flag = false
            this.$errorMsg(
              superiorParentBrandName + ',' + brandParentName + '存在重复数据'
            )
            return false
          }
        })
        if (!flag) return false
        this.tableData.list.push(...list)
        this.visible.show = false
      },
      async singleListChange(val, index) {
        const item = this.tableData.list[index]
        if (val) {
          const target = this.singleList.find((e) => e.id === item.sdTypeId)
          item.sdTypeName = target.sdTypeName || ''
          item.projectName = target.projectName || ''
        } else {
          item.sdTypeName = ''
          item.projectName = ''
        }
        this.updateItemSingleList()
        // const unNeedIds = []
        // this.tableData.list.forEach(({ sdTypeId }) => sdTypeId && unNeedIds.push(sdTypeId))
        // const { data: { singleList = [] } } = await getSdSaleTypeList({ unNeedIds: unNeedIds.toString(), sdType: 1 })
        // this.singleList = singleList || []
      },
      multipleListChange(index) {
        const item = this.tableData2.list[index]
        const target = this.multipleList.find((e) => e.id === item.sdTypeId)
        item.sdTypeName = target.sdTypeName || ''
        item.projectName = target.projectName || ''
      },
      // 单比例配置新增
      addToSingleList() {
        this.tableData.list.push({ sdType: 1, singleList: [] })
        this.updateItemSingleList()
      },
      // 更新列表中的单配置下拉
      updateItemSingleList() {
        try {
          const temp = []
          this.tableData.list.forEach((item) => {
            if (item.sdTypeId) {
              temp.push(item.sdTypeId)
            }
          })
          this.tableData.list.forEach(async (item) => {
            if (item.sdTypeId) {
              const unNeedIds = temp
                .filter((subItem) => subItem !== item.sdTypeId)
                .toString()
              const {
                data: { singleList = [] }
              } = await getSdSaleTypeList({ unNeedIds, sdType: 1 })
              item.singleList = singleList || []
            } else {
              const unNeedIds = temp.toString()
              const {
                data: { singleList = [] }
              } = await getSdSaleTypeList({ unNeedIds, sdType: 1 })
              item.singleList = singleList
            }
          })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 单比例配置删除
      async removeSingleList(index) {
        if (this.tableData.list.length <= 1) {
          this.$errorMsg('至少保留一项')
          return false
        }
        this.tableData.list.splice(index, 1)
        this.updateItemSingleList()
      },
      // 多比例配置删除
      removeMultipleList() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('当前没有选择任何商品')
          return false
        }
        this.$showToast('确定要删除勾选的商品？', () => {
          const ids = this.tableChoseList.map((item) => item.delId)
          this.tableData2.list = this.tableData2.list.filter(
            (item) => !ids.includes(item.delId)
          )
        })
      },
      // 确认选择商品
      comfirmChose(data) {
        if (data && data.length) {
          data.forEach((item) => {
            const {
              commBrandParentName: brandParent,
              commGoodsName: goodsName,
              commbarcode
            } = item
            this.tableData2.list.push({
              sdType: 2,
              brandParent,
              commbarcode,
              goodsName,
              delId: this.delId++
            })
          })
        }
        this.sdChoseProduct.visible.show = false
      },
      // 客户改变清空列表
      changeCustomerId() {
        this.tableData.list = [{ sdType: 1, singleList: this.singleList }]
        this.tableData2.list = []
      },
      downGoodsTemplate() {},
      importGoodsSuccess({ data }) {
        if (data.failure + '' !== '0') return false
        this.importGoods.visible.show = false
        const list = data.data
        if (list && list.length) {
          if (this.tableData2.list.length) {
            this.$showToast('确认覆盖已存在的所有商品信息？', async () => {
              this.tableData2.list = []
              this.tableData2.list = list.map((item) => ({
                ...item,
                sdTypeId: +item.sdTypeId,
                sdTypeName: item.sdSimpleName,
                delId: this.delId++
              }))
            })
          } else {
            this.tableData2.list = list.map((item) => ({
              ...item,
              sdTypeId: +item.sdTypeId,
              sdTypeName: item.sdSimpleName,
              delId: this.delId++
            }))
          }
        }
      },
      askClose() {
        this.$showToast('系统将不保留更改，是否取消?', () => {
          this.closeTag()
        })
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
  .title-bx {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  ::v-deep .el-icon-right {
    display: none;
  }
</style>
