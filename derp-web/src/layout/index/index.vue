<template>
  <div class="app-page">
    <!-- 左侧菜单 -->
    <div
      :class="!isCollapse ? 'app-page-left' : 'app-page-left app-page-left-suo'"
    >
      <div class="logo-bx flex-c-c">
        <span class="fs-20 clr-w" v-if="!isCollapse">{{ systemName }}</span>
      </div>
      <!-- 菜单数据获取完成后加载菜单 -->
      <left-menu v-if="isUserLoad" />
    </div>
    <!-- 右侧主体 -->
    <div
      :class="
        !isCollapse ? 'app-page-right' : 'app-page-right app-page-right-suo'
      "
    >
      <div class="page-right-top">
        <right-top v-if="isUserLoad" />
      </div>
      <div class="page-right-tag">
        <right-tag />
      </div>
      <div
        class="page-right-route page-right-route-hastag scroll-bx bgc-I"
        ref="viewScrollBox"
        @scroll="scrollView"
        v-if="isUserLoad"
      >
        <keep-alive>
          <router-view :key="$route.fullPath"></router-view>
        </keep-alive>
      </div>
    </div>
  </div>
</template>
<script>
  import leftMenu from './components/leftMenu'
  import rightTop from './components/rightTop'
  import rightTag from './components/tag'
  import { debounce } from '@u/tool'
  export default {
    components: {
      leftMenu,
      rightTop,
      rightTag
    },
    data() {
      return {
        isDev: process.env.NODE_ENV === 'development'
      }
    },
    computed: {
      isCollapse() {
        return this.$store.getters.isCollapse
      },
      isUserLoad() {
        try {
          return !!this.$store.getters.userInfo.isLoad
        } catch (error) {
          console.log('layout isLoad Error', error)
          return false
        }
      },
      systemName() {
        return this.$store.getters.userInfo.systemName || ''
      },
      tags() {
        // tag栏数据
        return this.$store.getters.tagsList
      }
    },
    watch: {
      /** 监听路由，滚动tag保存的位置  scrollView 记录位置 */
      async $route(to, from) {
        await this.$nextTick()
        this.tags.some((tag) => {
          if (tag.fullPath === to.fullPath) {
            const scrollTop = (tag.meta && tag.meta.scrollTop) || 0
            this.$refs.viewScrollBox.scrollTop = scrollTop
            return true
          }
        })
        this.addViewBoxClass()
      }
    },
    methods: {
      scrollView: debounce(function (event) {
        this.$store.dispatch('tags/AC_UPDATE_SCROLL', {
          fullPath: this.$route.fullPath,
          scrollTop: event.target.scrollTop
        })
      }),
      async addViewBoxClass() {
        // 给router-view加样式，用于匹配找到keep-alive
        await this.$nextTick()
        this.$refs.viewScrollBox.children[0].classList.add('AppContainer')
      }
    },
    mounted() {
      this.addViewBoxClass()
    }
  }
</script>
<style lang="scss">
  // 左边菜单栏宽度
  $leftMenuWid: 200px;
  $leftMenuWidSuo: 70px;
  // 左边菜单栏高度
  $leftMenuHei: 100vh;
  // 右侧顶部高度
  $topHei: 70px;
  // tab栏高度
  $tabHei: 50px;
  .app-page {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: row;
    justify-self: start;
  }
  /* 左边菜单 ***************/
  .app-page-left {
    width: $leftMenuWid;
    height: $leftMenuHei;
    background-color: #ffffff;
    box-shadow: 4px 0 5px -3px #c5c7ca;
    z-index: 1;
  }
  .app-page-left-suo {
    width: $leftMenuWidSuo;
  }
  /* 左边菜单 end ***************/
  /* 右侧 ***************/
  .app-page-right {
    width: calc(100vw - #{$leftMenuWid});
    height: 100vh;
    box-sizing: border-box;
  }
  .app-page-right-suo {
    width: calc(100vw - #{$leftMenuWidSuo});
  }
  /* 右侧 end***************/
  /* 右侧头部 **************/
  .page-right-top {
    height: $topHei;
    width: 100%;
    background-color: #6ea9f3;
  }
  /* 右侧头部 end **************/
  .page-right-tag {
    height: $tabHei;
    width: 100%;
    border-bottom: solid 1px #eaeaea;
    box-sizing: border-box;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    z-index: 1;
  }
  .page-right-route {
    width: 100%;
    height: calc(100% - #{$topHei});
    box-sizing: border-box;
    overflow-x: hidden;
    overflow-y: scroll;
    box-sizing: border-box;
    padding: 15px 0px 15px 15px;
  }
  .page-right-route-hastag {
    height: calc(100% - #{$topHei} - #{$tabHei});
  }
  .logo-bx {
    width: 100%;
    height: 70px;
    background-color: #6ea9f3;
  }
  .scroll-bx {
    &::-webkit-scrollbar {
      width: 12px; /*滚动条宽度*/
      height: 16px; /*滚动条高度*/
    }
    /*定义滚动条轨道 内阴影+圆角*/
    &::-webkit-scrollbar-track {
      border-radius: 10px; /*滚动条的背景区域的圆角*/
      background-color: transparent; /*滚动条的背景颜色*/
    }
    /*定义滑块 内阴影 + 圆角*/
    &::-webkit-scrollbar-thumb {
      border-radius: 10px; /*滚动条的圆角*/
      box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.2);
      background-color: rgba(0, 0, 0, 0.04); /*滚动条的背景颜色*/
    }
  }
  /* 滚动条样式 */
  ::-webkit-scrollbar-track-piece {
    background-color: #f8f8f8;
  }
  ::-webkit-scrollbar {
    width: 9px;
    height: 9px;
  }
  ::-webkit-scrollbar-thumb {
    background-color: #ddd;
    border-radius: 6px;
    background-clip: padding-box;
    min-height: 28px;
  }
  ::-webkit-scrollbar-thumb:hover {
    background-color: #bbb;
  }
</style>
