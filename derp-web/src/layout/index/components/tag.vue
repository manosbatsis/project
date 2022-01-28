<template>
  <div class="right-tag-box">
    <div class="item-bx" ref="tagbx" @mousewheel="scrollWheel">
      <div class="item-tag-bx" ref="all-tags">
        <div
          :class="{
            'item-tag': true,
            'item-tag-act': activePath === item.fullPath
          }"
          v-for="item in tags"
          :key="item.fullPath"
          @click="clickTag(item)"
        >
          {{ item.meta.title }}
          <i
            class="el-icon-circle-close"
            v-if="!item.meta.affix"
            :id="'closeTag_' + item.fullPath"
            @click.stop="closeTag(item, activePath === item.fullPath)"
          ></i>
        </div>
      </div>
    </div>
    <div class="tool-bx flex-c-c">
      <i class="el-icon-more fs-14 clr-III"></i>
      <div class="tool-list">
        <div class="tool-list-item b-n" @click="closeAll(false)">关闭全部</div>
        <div class="tool-list-item" @click="closeAll(true)">关闭其他区</div>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    computed: {
      tags() {
        // tag栏数据
        return this.$store.getters.tagsList
      },
      activePath() {
        // 当前w路由
        return this.$route.fullPath
      }
    },
    watch: {
      /** 监听路由，新增tag */
      $route(to, from) {
        /** 判断tag是否已存在 ， 不存在，添加tag */
        const isExist = this.tags.find((tag) => tag.fullPath === to.fullPath)
        !isExist && this.$store.dispatch('tags/AC_ADD_TAG', to)
      },
      /** 监听活动的tag变化  */
      async activePath(data) {
        this.scrollActive()
      }
    },
    mounted() {
      /** 首页固定临时解决方案，后续优化 */
      if (this.$route.name !== 'Home') {
        this.$store.dispatch('tags/AC_ADD_TAG', {
          fullPath: '/Home',
          meta: { title: '首页', affix: true, icon: 'el-icon-s-home' },
          name: 'Home',
          path: '/Home'
        })
      }
      /** 添加当前路由tag */
      this.$store.dispatch('tags/AC_ADD_TAG', this.$route)
    },
    methods: {
      /** tag切换 */
      clickTag(tag) {
        this.$router.replace(tag)
      },
      /** 关闭tag  */
      closeTag(tag, isActive) {
        if (isActive) {
          /** 当前tag，跳到下一个tag, 当前是最后一个，跳转到前一个tag */
          const closeTagIndex = this.tags.findIndex(
            (item) => item.fullPath === tag.fullPath
          )
          const lastTagIndex = this.tags.length - 1
          const switchTagIndex =
            closeTagIndex === lastTagIndex
              ? closeTagIndex - 1
              : closeTagIndex + 1
          this.$store.dispatch('tags/AC_DEL_TAG', tag)
          this.$router.replace(this.tags[switchTagIndex])
        } else {
          this.$store.dispatch('tags/AC_DEL_TAG', tag)
        }
      },
      /** tag横向滚动 */
      scrollWheel(event) {
        const $box = this.$refs.tagbx
        const scrollDirection = event.wheelDelta > 0 ? 'left' : 'right'
        const scrollWidth = $box.scrollWidth
        const clientWidth = $box.scrollWidth
        /** 盒子宽度 小于 滚动宽度   ## 不滚动 */
        if (clientWidth > scrollWidth) {
          return
        }
        let curScrollLeft = $box.scrollLeft
        const handle = scrollDirection === 'left' ? -120 : 120
        curScrollLeft += handle
        if (curScrollLeft <= 0) curScrollLeft = 0
        $box.scrollLeft = curScrollLeft
      },
      /** tag 滚动到活动的tag */
      async scrollActive() {
        await this.$nextTick()
        window.document.querySelector('.item-tag-act').scrollIntoView()
      },
      /**
       * 关闭全部
       * isKeepItSelf 是否保留当前tag
       */
      closeAll(isKeepItSelf) {
        /** 获取需要关闭的tag */
        const closeTagList = this.tags
          .filter((tag) => {
            /** 首页不关闭 */
            if (tag.name === 'Home') {
              return false
            }
            /** 保留本身 */
            if (isKeepItSelf && tag.fullPath === this.activePath) {
              return false
            }
            return true
          })
          .map(({ fullPath }) => {
            return { fullPath }
          })
        /** 关闭tag */
        closeTagList.forEach((tag) => {
          this.$store.dispatch('tags/AC_DEL_TAG', tag)
        })
        /** 不保留当前tag,跳转到首页 */
        if (!isKeepItSelf) {
          this.$router.replace({ name: 'Home' })
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  $toobWid: 46px;
  .right-tag-box {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: row;
  }
  .tool-bx {
    height: 100%;
    width: $toobWid;
    background-color: #ffffff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    text-align: center;
    font-size: 30px;
    color: #666;
    position: relative;
    &:hover .tool-list {
      display: block;
    }
  }
  .item-bx {
    width: calc(100% - #{$toobWid});
    height: 100%;
    overflow-x: scroll;
    background-color: #ffffff;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE 10+ */
    position: relative;
    &::-webkit-scrollbar {
      height: 3px;
    }
  }
  .item-tag-bx {
    height: 100%;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 0 10px;
  }
  .item-tag {
    padding: 0px 10px;
    border: solid 1px #eaeaea;
    height: 60%;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: 5px;
    font-size: 12px;
    color: #999;
    cursor: pointer;
    flex-shrink: 0;
    i {
      margin-left: 3px;
      font-size: 14px;
    }
    &:hover {
      color: #6ea9f3;
      border: solid 1px #6ea9f3;
    }
  }
  .item-tag-act {
    color: #6ea9f3;
    border: solid 1px #6ea9f3;
    &::before {
      display: inline-block;
      content: '';
      width: 6px;
      height: 6px;
      background-color: #6ea9f3;
      border-radius: 50%;
      margin-right: 3px;
    }
  }
  .tool-list {
    display: none;
    width: 80px;
    background-color: #ffffff;
    position: absolute;
    top: 100%;
    right: 5px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    z-index: 1;
  }
  .tool-list-item {
    width: 100%;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 12px;
    border-top: solid 1px #eaeaea;
    cursor: pointer;
    &:hover {
      color: #6ea9f3;
    }
  }
  .link-list {
    display: none;
    width: 120px;
    max-height: 50vh;
    overflow-y: scroll;
    background-color: #ffffff;
    font-size: 12px;
    position: absolute;
    top: 0;
    right: 80px;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE 10+ */
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    &::-webkit-scrollbar {
      display: none; /* Chrome Safari */
    }
  }
  .link-list-item {
    display: block;
    text-align: center;
    height: 40px;
    line-height: 40px;
    width: 70%;
    margin: 0 auto;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
    &:hover {
      color: #6ea9f3;
    }
  }
  .link-list-item-atc {
    color: #6ea9f3;
  }
</style>
