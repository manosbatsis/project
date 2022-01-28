<template>
  <!-- 新增关区组件 -->
  <JFX-Dialog
    title="是否批次效期强校验-变更记录"
    closeBtnText="取 消"
    :width="'1100px'"
    :top="'150px'"
    :showClose="true"
    :visible="isVisible"
    @comfirm="comfirm"
  >
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
    ></JFX-table>
  </JFX-Dialog>
</template>
<script>
  import { getChangeListById } from '@a/warehouseFile'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: { show: false },
      data: () => {}
    },
    data() {
      return {
        tableColumn: [
          {
            label: '仓库自编码',
            prop: 'depotCode',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '仓库名称',
            prop: 'depotName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '是否批次效期强校验',
            prop: 'batchValidation',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建/修改时间',
            prop: 'createDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '月结生效时间',
            prop: 'effectiveTime',
            minWidth: '120',
            align: 'center',
            hide: true
          }
        ],
        btnFlag: true
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.data
        try {
          const { data } = await getChangeListById({ id })
          this.tableData.list = data || []
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      comfirm() {
        this.$emit('comfirm')
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.form__container {
    .el-form-item {
      padding-right: 30px;
      margin-bottom: 20px;
      display: flex;
      .el-form-item__label {
        width: 100px;
      }
      .el-form-item__content {
        flex: 1;
      }
    }
  }
</style>
