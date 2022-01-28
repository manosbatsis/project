<template>
  <JFX-Dialog
    top="6vh"
    :visible.sync="isVisible"
    :showClose="true"
    :width="'1200px'"
    :btnAlign="'center'"
    :title="title"
    :closeBtnText="cancelBtnText"
    :confirmBtnText="btnText"
    @comfirm="closeDialog(true)"
    @close="closeDialog(false)"
  >
    <JFX-table :tableData.sync="data" :tableHeight="'480px'">
      <el-table-column
        prop="depotName"
        label="仓库"
        align="center"
        width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="goodsNo"
        label="货号"
        align="center"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="isExpireLabel"
        label="是否过期"
        align="center"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="massage"
        label="提示"
        align="center"
        width="220"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="num"
        label="出库量"
        align="center"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="surplusNum"
        label="库存余量"
        align="center"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="typeLabel"
        label="好坏品类型"
        align="center"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="unitLabel"
        label="理货单位"
        align="center"
        width="110"
      ></el-table-column>
    </JFX-table>
  </JFX-Dialog>
</template>

<script>
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => ({
          show: false
        })
      },
      title: {
        type: String,
        default: '校验库存可用量'
      },
      btnText: {
        type: String,
        default: '确定'
      },
      cancelBtnText: {
        type: String,
        default: '关闭'
      },
      data: {
        type: Object,
        default: () => {}
      },
      close: Function
    },
    methods: {
      // isComfirm 是否是确认按钮的关闭
      closeDialog(isComfirm) {
        this.isVisible.show = false
        this.close &&
          this.close({
            isComfirm
          })
      }
    }
  }
</script>

<style></style>
