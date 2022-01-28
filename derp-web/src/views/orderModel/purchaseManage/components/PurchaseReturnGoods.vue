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
        <JFX-Form :model="ruleForm" ref="ruleForm">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="商品名称："
                prop="goodsName"
                class="search-panel-item fs-14 clr-II"
              >
                <el-input
                  v-model.trim="ruleForm.goodsName"
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
                  v-model.trim="ruleForm.barcode"
                  clearable
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </JFX-Form>
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
  import { getPurchaseItemPopupByPage } from '@a/purchaseManage/index'
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
      }
    },
    data() {
      return {
        /* 搜索条件 */
        ruleForm: {
          goodsName: '',
          barcode: ''
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
            prop: 'goodsName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '商品条形码',
            prop: 'barcode',
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
      this.getList()
    },
    methods: {
      /* 获取列表数据 */
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await getPurchaseItemPopupByPage({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm,
            ...this.data
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
        if (!this.tableChoseList.length) {
          this.$emit('comfirm')
          return false
        }
        this.confirmBtnLoading = true
        this.$emit('comfirm', this.tableChoseList)
        this.confirmBtnLoading = false
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
