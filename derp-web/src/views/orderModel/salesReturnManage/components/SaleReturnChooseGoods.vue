<template>
  <div>
    <JFX-Dialog
      :visible.sync="isVisible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'80vw'"
      :title="'选择商品'"
      :btnAlign="'center'"
      top="6vh"
    >
      <JFX-search-panel @reset="resetForm" @search="getList(1)">
        <JFX-Form :model="ruleForm" ref="ruleForm">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="商品条码："
                prop="barcode"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input v-model.trim="ruleForm.barcode" clearable />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="商品货号："
                prop="goodsNo"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input v-model.trim="ruleForm.goodsNo" clearable />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="PO单号："
                prop="poNo"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input v-model.trim="ruleForm.poNo" clearable />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="商品名称："
                prop="goodsName"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input v-model.trim="ruleForm.goodsName" clearable />
              </el-form-item>
            </el-col>
          </el-row>
        </JFX-Form>
      </JFX-search-panel>
      <el-row class="company-page">
        <el-col :span="24" class="mr-t-20">
          <!-- 表格 -->
          <JFX-table
            :tableData.sync="tableData"
            :tableColumn="tableColumn"
            :tableHeight="'480px'"
            @change="getList"
            @selection-change="selectionChange"
            showSelection
          ></JFX-table>
          <!-- 表格 end-->
        </el-col>
      </el-row>
    </JFX-Dialog>
  </div>
</template>
<script>
  import { getSalePopupList } from '@a/salesReturnManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      data: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        /* 表单数据 */
        ruleForm: {
          barcode: '',
          goodsNo: '',
          poNo: '',
          goodsName: ''
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: 'po号',
            prop: 'poNo',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'inGoodsName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'inGoodsNo',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '品牌名称',
            prop: 'brandName',
            width: '140',
            align: 'center',
            hide: true
          },
          {
            label: '商品条形码',
            prop: 'barcode',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '工厂编码',
            prop: 'factoryNo',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '计量单位',
            prop: 'unitName',
            width: '80',
            align: 'center',
            hide: true
          },
          {
            label: '平台备案关区',
            prop: 'customsAreaNames',
            width: '140',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      /* 确认 */
      async comfirm() {
        if (!this.tableChoseList.length) {
          this.$emit('comfirm')
          return false
        }
        this.$emit('comfirm', this.tableChoseList)
      },
      /* 获取商品列表 */
      async getList() {
        console.log(this.data)
        try {
          this.tableData.loading = true
          const { data } = await getSalePopupList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm,
            ...this.data
          })
          this.tableData.list = data || []
        } catch (err) {
          this.$errorMsg(err.message)
        } finally {
          this.tableData.loading = false
        }
      },
      /* 重置搜索栏 */
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .company-page {
    width: 100%;
    margin-bottom: 10px;
    min-height: 200px;
  }
  .fen-pei-bx {
    margin-top: -30px;
    border-top: solid 1px #eaeaea;
    border-bottom: solid 1px #eaeaea;
    height: 80px;
  }
  .dialog-footer {
    height: 30px;
    margin-top: -30px;
  }
</style>
