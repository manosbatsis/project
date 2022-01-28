<template>
  <el-row
    class="search-panel bgc-II mr-t-20"
    :class="openPanel ? '' : 'search-panel-close'"
    ref="search"
  >
    <el-col :span="19">
      <slot></slot>
      <!-- 使用示例 -->
      <!-- <el-form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="调拨订单号：" prop="name" class="search-panel-item fs-14 clr-II" >
              <el-input v-model="ruleForm.name"></el-input>
            </el-form-item>
          </el-col>

          <el-col class="search-panel-item fs-14 clr-II" :md="8" :lg="8" :xl="6">
            <span>
              调拨订单号：
            </span>
            <el-input v-model="ruleForm.input5" placeholder="请输入内容" clearable></el-input>
          </el-col>
        </el-row>
      </el-form> -->
      <!-- 使用示例 end-->
    </el-col>
    <el-col :span="5" class="search-panel-tool">
      <el-button
        type="text"
        v-if="!openPanel && showOpenBtn"
        @click="openPanel = true"
      >
        展开
        <i class="el-icon-arrow-down"></i>
      </el-button>
      <el-button
        type="text"
        v-if="openPanel && showOpenBtn"
        @click="openPanel = false"
      >
        收起
        <i class="el-icon-arrow-up"></i>
      </el-button>
      <el-button
        type="primary"
        :size="'small'"
        @click="handleClick('search')"
        id="jfx_search_btn"
      >
        查 询
      </el-button>
      <el-button @click="handleClick('reset')" id="jfx_reset_btn">
        重 置
      </el-button>
    </el-col>
  </el-row>
</template>
<script>
  /**
 * @description 搜索案板
 * 示例
 * <JFX-search-panel @reset="reset" @search="search">
 *    <el-form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="调拨订单号：" prop="name" class="search-panel-item fs-14 clr-II" >
              <el-input v-model="ruleForm.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </JFX-search-panel>
 *
 * 参数说明
 * solt 按照上面示例使用 否则导致样式混乱
 * showOpenBtn  是否显示展开/收起 按钮
 * reset func 点击重置执行
 * search func 点击搜索执行
 */
  export default {
    props: {
      // 是否显示展开/收起 按钮
      showOpenBtn: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        openPanel: false
      }
    },
    watch: {
      async openPanel() {
        await this.$nextTick()
        this.$refs.search.$el.scrollTop = 0
      }
    },
    mounted() {
      this.addOpenPanelByConditionEvent()
      this.$refs.search &&
        this.$refs.search.$el &&
        this.$refs.search.$el.addEventListener('keyup', (event) => {
          if (event.keyCode === 13) {
            this.$emit('search')
            return false
          }
        })
    },
    methods: {
      handleClick(type) {
        this.$emit(type)
      },
      // 监听搜索组件偏移
      addOpenPanelByConditionEvent() {
        const openFn = () => {
          if (this.$refs.search.$el.scrollTop >= 10) {
            this.openPanel = true
          }
        }
        this.$refs.search.$el.addEventListener('keyup', openFn)
        this.$refs.search.$el.addEventListener('click', openFn)
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.search-panel .el-select__tags-text {
    display: inline-block;
    //根据自己的需要调整文字宽度
    max-width: 75px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  //根据自己需要调整叉号的位置
  ::v-deep.search-panel .el-tag.el-tag--info .el-tag__close {
    top: -4px;
    right: -4px;
  }
</style>
