<template>
  <div>
    <JFX-Dialog
      :visible.sync="isVisible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'600px'"
      :title="'完结核销'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      top="6vh"
    >
      <div class="mr-b-15">
        <p class="mr-b-10">勾选单据点击确认完结核销即可</p>
        <el-checkbox-group v-model="checkList">
          <el-checkbox label="0" class="mr-b-10">
            完结发货订单与退款订单正负红冲，待核销金额=0（总单量：{{
              punchNum || 0
            }}）
          </el-checkbox>
          <el-checkbox label="1">
            剩余发货订单与退款订单正负红冲，待核销金额≠0（总单量：{{
              nonPunchNum || 0
            }}）
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </JFX-Dialog>
  </div>
</template>
<script>
  import { listEndReceiveBill, endReceiveBill } from '@a/reportManage'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      filterData: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        checkList: [],
        nonPunchNum: 0,
        punchNum: 0,
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async comfirm() {
        let isEndPunch = ''
        if (this.checkList.length === 0) {
          this.$errorMsg('请选择选项')
          return false
        }
        if (this.checkList.length >= 2) {
          isEndPunch = '2'
        } else {
          isEndPunch = this.checkList[0]
        }
        try {
          this.confirmBtnLoading = true
          await endReceiveBill({
            ...this.filterData,
            isEndPunch
          })
          this.$successMsg('操作成功')
          this.$emit('comfirm')
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.confirmBtnLoading = false
        }
      },
      async init() {
        try {
          const {
            data: { nonPunchNum = 0, punchNum = 0 }
          } = await listEndReceiveBill({ ...this.filterData })
          this.nonPunchNum = nonPunchNum
          this.punchNum = punchNum
        } catch (error) {
          this.$errorMsg(error.message)
        }
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
