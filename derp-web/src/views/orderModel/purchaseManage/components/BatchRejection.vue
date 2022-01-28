<template>
  <!-- 批量驳回 -->
  <JFX-Dialog
    title="批量驳回"
    closeBtnText="取 消"
    :width="'500px'"
    :top="'80px'"
    :confirmBtnLoading="confirmBtnLoading"
    :visible="isVisible"
    @comfirm="comfirm"
  >
    <div style="padding: 0 20px">
      <h3 style="text-align: center">
        是否批量驳回（{{ codesTotal }}条）单据为:
      </h3>
      <p style="margin: 20px 0">{{ codes }}</p>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { purchaseBatchRejections } from '@a/purchaseManage'
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
        codes: '',
        ids: '',
        confirmBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    computed: {
      // 总单据数据
      codesTotal() {
        return this.codes.includes(',') ? this.codes.split(',').length : 1
      }
    },
    methods: {
      init() {
        const { codes, ids } = this.data
        this.codes = codes || ''
        this.ids = ids || ''
      },
      async comfirm() {
        const { ids } = this
        try {
          this.confirmBtnLoading = true
          await purchaseBatchRejections({ ids })
          this.$successMsg('批量驳回成功')
          this.$emit('update:isVisible', { show: false })
          this.$emit('comfirm')
        } catch (error) {
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.confirmBtnLoading = false
        }
      }
    }
  }
</script>
