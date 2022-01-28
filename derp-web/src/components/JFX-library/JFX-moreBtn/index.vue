<template>
  <div class="moreTxt">
    <!-- 开启切换 -->
    <template v-if="isShowBtn">
      <!-- 状态 -->
      <slot name="more" v-if="moreStatus"></slot>
      <el-button
        type="text"
        v-if="moreStatus"
        @click="moreStatus = false"
        class="inlineBtn"
      >
        更多
      </el-button>
      <!-- 状态 -->
      <slot name="hide" v-if="!moreStatus"></slot>
      <el-button
        type="text"
        v-if="!moreStatus"
        @click="moreStatus = true"
        class="inlineBtn"
      >
        隐藏
      </el-button>
    </template>
    <!-- 不开启切换 -->
    <slot name="hide" v-else></slot>
  </div>
</template>

<script>
  /**
 * 更多隐藏按钮切换
 * 使用说明
 *  <JFX-more-btn v-if="row.poNo" :isShowBtn="row.poNo && row.poNo.split('&').length > 2">
 *       <!- 隐藏显示 按钮出现 ->
        <template slot="more">
            PO：{{ row.poNo.split('&').slice(0,2).join('&') }}
        </template>
        <!- 全部显示 ->
        <template slot="hide">
            PO：{{ row.poNo }}
        </template>
    </JFX-more-btn>

 */
  export default {
    props: {
      //   是否开启切换状态
      isShowBtn: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        moreStatus: true
      }
    },
    methods: {}
  }
</script>

<style lang="scss" scoped>
  ::v-deep.inlineBtn {
    display: contents;
  }
</style>
