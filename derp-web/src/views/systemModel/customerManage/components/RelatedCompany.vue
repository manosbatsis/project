<template>
  <!-- 选择关联公司组件 -->
  <JFX-Dialog
    :visible.sync="companyVisible"
    :showClose="true"
    :width="'60vw'"
    :title="'选择关联公司'"
    :top="'80px'"
    closeBtnText="取 消"
    :confirmBtnLoading="confirmBtnLoading"
    @comfirm="comfirm"
  >
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      ref="rootTable"
      showSelection
      @selection-change="selectionChange"
    ></JFX-table>
    <!-- 表格 end-->
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { getMerchantInfo, getMerchantRel } from '@a/customerManage'
  export default {
    mixins: [commomMix],
    props: {
      companyVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      merchantIdList: {
        // 选择过滤的已关联的 merchantIdList,
        type: String,
        default: ''
      },
      id: {
        type: String,
        default: ''
      },
      isDefaultChecked: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        // 表格列数据
        tableColumn: [
          {
            label: '公司编码',
            prop: 'code',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '公司名称',
            prop: 'name',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { merchantIdList, id, isDefaultChecked } = this
        try {
          const { data: list } = await getMerchantInfo()
          let resultList = list || []
          // 过滤数据， 主要用在 新建编辑客户 和 供应商
          if (merchantIdList) {
            // 客户id集合
            const merchantIds = this.merchantIdList
              .split(',')
              .map((item) => Number(item))
            resultList = resultList.filter(
              (item) => !merchantIds.includes(item.id)
            )
          }
          this.tableData.list = resultList
          // 回选, 主要用在,供应商列表选择
          if (id && isDefaultChecked) {
            const {
              data: { relList }
            } = await getMerchantRel({ id })
            // 客户id集合
            const merchantIds = relList.map((item) => item.merchantId)
            await this.$nextTick()
            this.tableData.list.forEach((item) => {
              if (merchantIds.includes(item.id)) {
                this.$refs.rootTable.$refs['el-table'].toggleRowSelection(
                  item
                )
              }
            })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      comfirm() {
        if (!this.tableChoseList.length) {
          this.$emit('comfirm')
          return false
        }
        this.$emit('comfirm', this.tableChoseList)
      }
    }
  }
</script>
