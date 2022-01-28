<template>
  <div>
    <JFX-Dialog
      top="6vh"
      :visible.sync="isVisible"
      :showClose="true"
      :width="'80vw'"
      :title="'选择商品'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      @comfirm="comfirm"
    >
      <!-- 查询面板 -->
      <JFX-search-panel @reset="resetForm" @search="getList(1)" class="mr-b-20">
        <el-form :model="ruleForm" ref="ruleForm">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="商品名称："
                prop="name"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input
                  v-model.trim="ruleForm.name"
                  clearable
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="条形码："
                prop="barcode"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input
                  :disabled="!!data.barcode"
                  v-model.trim="ruleForm.barcode"
                  clearable
                  placeholder="请输入"
                />
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
                  style="width: 100%"
                  :data-list="getBrandSelectBean('brandSelectList')"
                >
                  <el-option
                    v-for="item in selectOpt.brandSelectList"
                    :key="item.key"
                    :label="item.value"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="商品货号："
                prop="goodsNo"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input
                  v-model.trim="ruleForm.goodsNo"
                  clearable
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="工厂编码："
                prop="factoryNo"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input
                  v-model.trim="ruleForm.factoryNo"
                  clearable
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </JFX-search-panel>
      <!-- 查询面板 end -->

      <!-- 表格 -->
      <JFX-table
        ref="root-table"
        :tableData.sync="tableData"
        :tableColumn="tableColumn"
        showSelection
        @selection-change="selectionChange"
        @change="getList"
      />
      <!-- 表格 end-->
    </JFX-Dialog>
  </div>
</template>
<script>
  import { getPopupList, getListByIds } from '@a/base'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      /* 商品列表的传参 */
      data: {
        type: Object,
        default: () => {}
      },
      customComfirm: {
        type: Function
      },
      /* 是否单选 */
      isRadio: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        /* 搜索条件 */
        ruleForm: {
          name: '',
          barcode: '',
          brandId: '',
          goodsNo: '',
          factoryNo: '',
          isBlur: '0' // 条码是否模糊查询：0-是 1-否
        },
        /* 表格列数据 */
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
            minWidth: '120',
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
            label: '商品货号',
            prop: 'goodsNo',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '品牌名称',
            prop: 'brandName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品条形码',
            prop: 'barcode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '工厂编码',
            prop: 'factoryNo',
            minWidth: '120',
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
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        /* 确认按钮loading */
        confirmBtnLoading: false
      }
    },
    mounted() {
      if (this.data.barcode) {
        this.ruleForm.barcode = this.data.barcode
        this.ruleForm.isBlur = '1'
      }
      this.getList()
    },
    methods: {
      /* 获取列表数据 */
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await getPopupList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm,
            ...this.data,
            barcode: this.ruleForm.barcode
          })
          this.tableData.list = data.list || []
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.tableData.loading = false
        }
      },
      /* 确认 */
      async comfirm() {
        // 执行确认后自定义的确认函数
        if (this.customComfirm) {
          this.customComfirm(this.tableChoseList)
          return false
        }
        if (!this.tableChoseList.length) {
          this.$emit('comfirm')
          return false
        }
        try {
          this.confirmBtnLoading = true
          const ids = this.tableChoseList.map((item) => item.id).toString()
          const { data } = await getListByIds({ ids })
          this.$emit('comfirm', data)
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.confirmBtnLoading = false
        }
      },
      /* 表格勾选方法 */
      async selectionChange(rows) {
        await this.$nextTick()
        if (this.isRadio && rows.length >= 2) {
          const table = this.$refs['root-table'].$refs['el-table']
          table.clearSelection()
          table.toggleRowSelection(rows.pop())
          return false
        }
        this.tableChoseList = rows
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
