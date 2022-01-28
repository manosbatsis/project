<template>
  <JFX-Dialog
    :visible.sync="isVisible"
    :showClose="true"
    @comfirm="comfirm"
    width="800px"
    :title="'系统公告'"
    :top="'80px'"
    :loading="loading"
    closeBtnText="取 消"
  >
    <div class="notice">
      <div class="notice__head">
        <h4>{{ detail.title }}</h4>
        <p>
          <span>公告类型: {{ detail.statusLabel }}</span>
          <span>发布时间: {{ detail.createDate }}</span>
        </p>
      </div>
      <div class="notice__body" v-html="detail.contentBody"></div>
    </div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { detailNotice } from '@a/noticeManage/index'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: { show: false },
      data: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        detail: {},
        loading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.data
        try {
          this.loading = true
          const { data } = await detailNotice({ id })
          this.detail = data || {}
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.loading = false
        }
      },
      comfirm() {
        this.$emit('comfirm')
      }
    }
  }
</script>
<style lang="scss" scoped>
  .notice {
    &__head {
      text-align: center;
      border-bottom: solid 1px #dcdcdc;
      padding-bottom: 6px;
      color: #797979;

      > h4 {
        font-size: 26px;
        font-weight: normal;
      }
    }

    &__body {
      height: 400px;
      padding: 0 20px;
      overflow-y: scroll;
    }
  }
</style>
