<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <JFX-title title="选择事业部" className="mr-t-10" />
    <JFX-Form class="mr-t-20" :model="ruleForm" ref="ruleForm" :rules="rules">
      <el-form-item label="" prop="buId">
        <el-select
          v-model="ruleForm.buId"
          placeholder="请选择"
          filterable
          clearable
          :disabled="$route.query.id"
          :data-list="getBUSelectBean('bulist')"
        >
          <el-option
            v-for="item in selectOpt.bulist"
            :key="item.key"
            :label="item.value"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <!-- 商品信息 -->
      <JFX-title
        title="商品信息"
        className="mr-t-20"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <div v-if="!$route.query.id">
          <el-button type="primary" @click="showChoseProductsModal">
            选择商品
          </el-button>
          <el-button type="primary" @click="delTableItems">删除</el-button>
        </div>
      </JFX-title>
      <JFX-table
        :tableData.sync="tableData"
        :tableColumn="tableColumn"
        :showPagination="false"
        showSelection
        @selection-change="selectionChange"
      >
        <template slot="currency" slot-scope="{ row }">
          <el-select
            v-model="row.currency"
            placeholder="请选择"
            filterable
            clearable
            :data-list="getCurrencySelectBean('currencyList')"
          >
            <el-option
              v-for="item in selectOpt.currencyList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
            、
          </el-select>
        </template>
        <template slot="price" slot-scope="{ row }">
          <el-input-number
            v-model.trim="row.price"
            v-max-spot="5"
            :precision="5"
            :controls="false"
            :min="0"
            style="width: 100%"
          />
        </template>
        <template slot="effectiveDate" slot-scope="{ row }">
          <el-date-picker
            v-model="row.effectiveDate"
            type="month"
            placeholder="请选择"
            style="width: 193px"
            value-format="yyyy-MM"
          />
        </template>
        <template slot="isGroup" slot-scope="{ row }">
          {{ row.isGroup === '0' ? '否' : '是' }}
        </template>
        <template slot="action" slot-scope="{ row }">
          <el-button
            type="text"
            v-if="row.isGroup === '1'"
            @click="showGroupDetail(row.goodsId)"
          >
            组合详情
          </el-button>
        </template>
        <template slot="adjustPriceResult" slot-scope="{ row }">
          <div class="textarea-con">
            <el-input
              type="textarea"
              v-model="row.adjustPriceResult"
              clearable
              :rows="5"
              placeholder="请输入调价原因"
            ></el-input>
          </div>
        </template>
      </JFX-table>
      <!-- 商品信息 -->
      <!-- 修改历史记录 -->
      <JFX-title
        title="修改历史记录"
        className="mr-t-20"
        v-if="$route.query.id"
      />
      <JFX-table
        :tableData.sync="recordList"
        :tableColumn="recordListColumn"
        showIndex
        @change="getRecordPriceList"
        v-if="$route.query.id"
      ></JFX-table>
      <!-- 修改历史记录 -->
      <!-- 操作 -->
      <div class="flex-c-c">
        <el-button type="primary" @click="onSave">保存</el-button>
        <el-button @click="closeTag">取消</el-button>
      </div>
      <!-- 操作 end -->
    </JFX-Form>
    <!-- 选择商品 -->
    <ChoseProducts
      v-if="choseProducts.visible.show"
      :visible.sync="choseProducts.visible"
      :filterData="choseProducts.filterData"
      @comfirm="comfirmChoseProducts"
    ></ChoseProducts>
    <!-- 选择商品 -->
    <!-- 组合品详情 -->
    <JFX-Dialog
      title="组合品详情"
      closeBtnText="取 消"
      :width="'1100px'"
      :top="'80px'"
      :showClose="true"
      :visible="groupDetail.visible"
      btnAlign="center"
      @comfirm="groupDetail.visible.show = false"
    >
      <JFX-table
        :tableData.sync="groupDetail.data"
        :tableColumn="groupDetail.column"
        :showPagination="false"
        showIndex
      ></JFX-table>
    </JFX-Dialog>
    <!-- 组合品详情 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    saveSettlementPrice,
    modifySettlementPrice,
    settlementPriceDetail,
    listRecordPrice,
    listAllGroupMerchandiseByGroupId
  } from '@a/adjustAccountsManage'
  export default {
    mixins: [commomMix],
    components: {
      // 选择商品
      ChoseProducts: () => import('@c/choseProducts/index')
    },
    data() {
      return {
        // 表单数据
        ruleForm: {
          buId: ''
        },
        // 表单校验
        rules: {
          buId: { required: true, message: '请选择事业部', trigger: 'change' }
        },
        // 选择商品
        choseProducts: {
          visible: { show: false },
          filterData: {}
        },
        recordList: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        groupDetail: {
          visible: { show: false },
          data: {
            list: []
          },
          column: [
            {
              label: '商品条码',
              prop: 'barcode',
              minWidth: '100',
              align: 'center',
              hide: true
            },
            {
              label: '商品货号',
              prop: 'goodsNo',
              minWidth: '100',
              align: 'center',
              hide: true
            },
            {
              label: '商品名称',
              prop: 'name',
              minWidth: '150',
              align: 'center',
              hide: true
            },
            {
              label: '商品编码',
              prop: 'goodsCode',
              minWidth: '100',
              align: 'center',
              hide: true
            },
            {
              label: '单品数量',
              prop: 'groupNum',
              minWidth: '100',
              align: 'center',
              hide: true
            },
            {
              label: '商品价格',
              prop: 'groupPrice',
              minWidth: '100',
              align: 'center',
              hide: true
            }
          ]
        },
        // 表格列数据
        tableColumn: [
          {
            label: '条形码',
            prop: 'barcode',
            minWidth: '100',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '品牌',
            prop: 'brandName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '规格',
            prop: 'goodsSpec',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算币种',
            slotTo: 'currency',
            width: '210',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '标准成本单价',
            slotTo: 'price',
            width: '160',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '生效年月',
            slotTo: 'effectiveDate',
            width: '220',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '是否组合',
            slotTo: 'isGroup',
            width: '100',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '100', align: 'center' }
        ],
        // 表格列数据
        recordListColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '生效年月',
            prop: 'effectiveDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '标准成本单价',
            prop: 'price',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '结算币种',
            prop: 'currencyLabel',
            width: '100',
            align: 'center',
            hide: true
          },
          {
            label: '修改时间',
            prop: 'modifyDate',
            width: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '修改人员',
            prop: 'modifier',
            width: '120',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '调价原因',
            prop: 'adjustPriceResult',
            minWidth: '150',
            align: 'center',
            hide: true,
            need: true
          },
          {
            label: '审核时间',
            prop: 'examineDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '审核人员',
            prop: 'examiner',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            width: '100',
            align: 'center',
            hide: true
          }
        ],
        delId: 0
      }
    },
    mounted() {
      const { id } = this.$route.query
      id && this.editInit(id)
    },
    methods: {
      async editInit(id) {
        try {
          const {
            data: { itemList, detail }
          } = await settlementPriceDetail({ id })
          if (itemList && itemList.length) {
            this.tableData.list = itemList.map((item) => ({
              goodsNo: item.goodsNo || '',
              barcode: item.barcode || '',
              goodsName: item.goodsName || '',
              brandName: item.brandName || '',
              goodsSpec: item.goodsSpec || '',
              currency: item.currency || '',
              price: item.price || '',
              effectiveDate: item.effectiveDate || '',
              isGroup: item.isGroup || '',
              goodsId: item.goodsId || '',
              id: item.id || '',
              adjustPriceResult: item.adjustPriceResult || '',
              startDate: item.startDate || '',
              delId: this.delId++
            }))
          }
          this.ruleForm.buId = detail.buId + ''
          this.tableColumn.push({
            label: '调价原因',
            slotTo: 'adjustPriceResult',
            width: '220',
            align: 'center',
            need: true
          })
          this.getRecordPriceList()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 获取修改历史记录
      async getRecordPriceList() {
        try {
          this.recordList.loading = true
          const { data } = await listRecordPrice({
            begin: (this.recordList.pageNum - 1) * this.recordList.pageSize,
            pageSize: this.recordList.pageSize || 10,
            settlementPriceId: this.$route.query.id || ''
          })
          this.recordList.list = data.list || []
          this.recordList.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.recordList.loading = false
        }
      },
      // 显示选择商品弹窗
      showChoseProductsModal() {
        const unNeedIds = this.tableData.list.length
          ? this.tableData.list.map((item) => item.goodsId).toString()
          : ''
        this.choseProducts.visible.show = true
        this.choseProducts.filterData = { unNeedIds }
      },
      // 确认选择商品
      comfirmChoseProducts(payload) {
        this.choseProducts.visible.show = false
        if (payload && payload.length) {
          const list = payload.map((item) => ({
            goodsNo: item.goodsNo || '',
            barcode: item.barcode || '',
            goodsName: item.name || '',
            brandName: item.brandName || '',
            goodsSpec: item.spec || '',
            currency: item.currency || '',
            price: item.price || '',
            effectiveDate: item.effectiveDate || '',
            isGroup: item.isGroup || '',
            goodsId: item.id || '',
            adjustPriceResult: '',
            startDate: '',
            delId: this.delId++
          }))
          this.tableData.list.push(...list)
        }
      },
      // 校验商品
      checkGoods() {
        let flag = true
        if (!this.tableData.list.length) {
          this.$errorMsg('至少选择一件商品')
          flag = false
          return flag
        }
        const { id } = this.$route.query
        for (let i = 0; i < this.tableData.list.length; i++) {
          const { currency, price, effectiveDate, adjustPriceResult } =
            this.tableData.list[i]
          if (!currency) {
            this.$errorMsg(`表格第${i + 1}行，结算币种不能为空`)
            flag = false
            break
          }
          if (
            price === null ||
            price === undefined ||
            price === '' ||
            price < 0
          ) {
            this.$errorMsg(`表格第${i + 1}行，标准成本单价不能为空或者小于0`)
            flag = false
            break
          }
          if (!effectiveDate) {
            this.$errorMsg(`表格第${i + 1}行，生效年月不能为空`)
            flag = false
            break
          }
          if (id && !adjustPriceResult) {
            this.$errorMsg(`表格第${i + 1}行，调价原因不能为空`)
            flag = false
            break
          }
        }
        return flag
      },
      async showGroupDetail(goodsId) {
        this.groupDetail.visible.show = true
        try {
          const { data } = await listAllGroupMerchandiseByGroupId({ goodsId })
          this.groupDetail.list = data || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 删除商品
      delTableItems() {
        if (!this.tableChoseList.length) {
          this.$errorMsg('当前没有选择任何商品')
          return false
        }
        this.$showToast('确定要删除勾选的商品？', () => {
          const ids = this.tableChoseList.map((item) => item.delId)
          this.tableData.list = this.tableData.list.filter(
            (item) => !ids.includes(item.delId)
          )
        })
      },
      // 保存
      onSave() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) {
            this.$errorMsg('请正确填写表单内容')
            return false
          }
          if (!this.checkGoods()) return false
          const { id } = this.$route.query
          try {
            const params = {
              buId: this.ruleForm.buId || '',
              itemList: this.tableData.list || [],
              id
            }
            id
              ? await modifySettlementPrice(params)
              : await saveSettlementPrice(params)
            this.$successMsg(id ? '修改成功' : '保存成功')
            this.closeTag()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.textarea-con {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 130px;
    }
    .el-form-item__content {
      width: 700px;
    }
  }
</style>
