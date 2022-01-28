<template>
  <el-row>
    <el-col :span="22">
      <div class="breadcrumb" v-if="breadcrumbList.length > 0">
        <span class="status-bar bgc-act"></span>
        <span class="fs-14 clr-II">当前位置：</span>
        <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
          <el-breadcrumb-item
            v-for="(item, index) in breadcrumbList"
            :key="index"
            :to="
              item.isRootPath || index === breadcrumbList.length - 1
                ? ''
                : { path: item.fullPath || item.path }
            "
            :replace="true"
          >
            <span
              class="fs-14 clr-II fw-nor breadcrumb-item"
              :class="index === breadcrumbList.length - 1 ? ' clr-act' : ''"
            >
              {{ item.meta.title }}
            </span>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </el-col>
    <el-col :span="2" class="fs-12" v-if="showGoback">
      <JFX-back :backPathUrl="backPathUrl" />
    </el-col>
  </el-row>
</template>
<script>
  /**
   * @description 面包屑 全局组件
   * 使用示例:
   *  <JFX-Breadcrumb :showGoback="true" />
   */
  import routeList from '@/router/menu-route'
  export default {
    props: {
      /* 是否显示返回按钮 */
      showGoback: {
        type: Boolean,
        default: false
      },
      backPathUrl: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        /* 当前渲染的面包屑列表 */
        breadcrumbList: [],
        /* 路由配置列表 */
        routeList: []
      }
    },
    computed: {
      currentRoutes() {
        return this.$route.matched
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        /* 路由配置列表 */
        this.routeList = routeList || []
        /* 根据路由生成面包屑 */
        this.createListByRoute()
      },
      /* 根据路由生成面包屑 */
      createListByRoute() {
        if (!this.currentRoutes.length) return false
        /* 所有路由配置列表 */
        const allRouteList = this.routeList.reduce((pre, cur) => {
          if (cur.children) {
            return [...pre, ...cur.children]
          }
          return pre
        }, [])
        /* 生成面包屑列表 */
        this.currentRoutes.forEach((route, index) => {
          if (route.meta.routeParentUrl) {
            const target = allRouteList.find(
              (item) => item.path === route.meta.routeParentUrl
            )
            target && this.breadcrumbList.push(target)
          }
          /* 一级菜单点击不跳转 */
          this.breadcrumbList.push(
            index === 0 ? { ...route, isRootPath: true } : route
          )
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .breadcrumb {
    height: 20px;
    display: flex;
    align-items: center;
    user-select: none;
    &-item {
      cursor: pointer;
    }
  }
  .status-bar {
    display: inline-block;
    width: 4px;
    height: 80%;
    margin-right: 5px;
  }
</style>
