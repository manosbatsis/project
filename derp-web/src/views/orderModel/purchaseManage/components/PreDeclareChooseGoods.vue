<template>
  <div>
    <JFX-Dialog
      :visible.sync="isVisible"
      :showClose="true"
      :width="'80vw'"
      :title="'选择商品'"
      :btnAlign="'center'"
      @comfirm="comfirm"
      top="6vh"
    >
      <JFX-search-panel @reset="resetForm" @search="getList(1)">
        <JFX-Form :model="ruleForm" ref="ruleForm">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="条形码："
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
                label="商品品牌："
                prop="brandId"
                class="search-panel-item fs-14 clr-II"
              >
                <el-select
                  v-model.trim="ruleForm.brandId"
                  placeholder="请选择"
                  filterable
                  clearable
                  :data-list="getBrandSelectBean('brandSelectList')"
                >
                  <el-option
                    v-for="item in selectOpt.brandSelectList"
                    :key="item.key"
                    :label="item.value"
                    :value="item.key"
                  ></el-option>
                </el-select>
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
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="工厂编码："
                prop="factoryNo"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input v-model.trim="ruleForm.factoryNo" clearable />
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
            @selection-change="selectionChange"
            @change="getList"
            :tableHeight="'480px'"
          >
            <el-table-column type="selection" align="center" width="55">
            </el-table-column>
            <el-table-column
              prop="merchantName"
              label="公司"
              align="center"
              width="120"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="purchase"
              label="采购订单号"
              align="center"
              min-width="130"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="goodsName"
              label="商品名称"
              align="center"
              min-width="120"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="goodsNo"
              label="商品货号"
              align="center"
              min-width="130"
            >
            </el-table-column>
            <el-table-column
              prop="purchasePrice"
              label="采购单价"
              align="center"
              width="110"
            >
            </el-table-column>
            <el-table-column
              prop="brandName"
              label="品牌名称"
              align="center"
              width="120"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="barcode"
              label="商品条形码"
              align="center"
              width="150"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="factoryNo"
              label="工厂编码"
              align="center"
              width="110"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="unitName"
              label="计量单位"
              align="center"
              width="80"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="customsAreaNames"
              label="平台备案关区"
              align="center"
              width="130"
              show-overflow-tooltip
            >
            </el-table-column>
          </JFX-table>
          <!-- 表格 end-->
        </el-col>
      </el-row>
    </JFX-Dialog>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getItemPopupListByPage } from '@a/purchaseManage'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      filterData: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        ruleForm: {
          goodsName: '',
          barcode: '',
          brandId: '',
          goodsNo: '',
          factoryNo: ''
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      async comfirm() {
        if (!this.tableChoseList.length) {
          this.$emit('comfirm')
          return false
        }
        this.$emit('comfirm', this.tableChoseList || [])
      },
      /* 获取列表数据 */
      async getList(pageNum) {
        try {
          this.tableData.loading = true
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          const { data } = await getItemPopupListByPage({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm,
            ...this.filterData
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
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
