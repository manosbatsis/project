<template>
  <div class="left-mune-box-content">
    <el-menu
      :default-active="activeMenu"
      :text-color="textColor"
      :active-text-color="activeTextColor"
      unique-opened
      :collapse="isCollapse"
    >
      <!-- 一级菜单  redirect-->
      <div v-for="(lev1, l1) in routeList" :key="l1">
        <el-submenu :index="lev1.path" v-if="!lev1.redirect && lev1.showMenu">
          <template slot="title" @click="linkTo">
            <i :class="lev1.meta.icon"></i>
            <span v-if="!isCollapse">{{ lev1.meta.title }}</span>
          </template>
          <!-- 二级级菜单 -->
          <div class="group-box" v-for="(lev2, l2) in lev1.children" :key="l2">
            <el-submenu v-if="!lev2.hide && lev2.children" :index="lev2.path">
              <template slot="title">{{ lev2.meta.title }}</template>
              <!-- 三级级菜单 -->
              <div v-for="(lev3, l3) in lev2.children" :key="l3">
                <el-menu-item
                  :index="lev3.path"
                  v-if="!lev3.hide"
                  @click="linkTo(lev3)"
                >
                  {{ lev3.meta.title }}
                </el-menu-item>
              </div>
              <!-- 三级级菜单 end -->
            </el-submenu>
            <el-menu-item
              v-else-if="!lev2.hide && lev2.showMenu"
              :index="lev2.path"
              @click="linkTo(lev2)"
            >
              {{ lev2.meta.title }}
            </el-menu-item>
          </div>
          <!-- 二级级菜单 end -->
        </el-submenu>
        <el-menu-item :index="lev1.redirect" v-else-if="lev1.showMenu">
          <div @click="linkTo(lev1, 'redirect')">
            <i :class="[lev1.meta.icon, 'firstMenuIcon']"></i>
            <span v-if="!isCollapse">{{ lev1.meta.title }}</span>
          </div>
        </el-menu-item>
      </div>
      <!-- 一级菜单 end-->
    </el-menu>
  </div>
</template>
<script>
  import menu from '@/router/menu-route'
  import config from '@u/config'
  export default {
    data() {
      return {
        backgroundColor: '#008080', // 'transparent', // 菜单的背景色（仅支持 hex 格式) #1C7887
        textColor: '#333', // 菜单的文字颜色（仅支持 hex 格式)
        activeTextColor: '#6ea9f3',
        routeList: [],
        urlTreeMap: new Map()
      }
    },
    computed: {
      activeMenu() {
        return this.$route.meta.routeParentUrl || this.$route.path
      },
      isCollapse() {
        return this.$store.getters.isCollapse
      }
    },
    mounted() {
      this.urlTreeMap = this.$store.getters.menuUrlMap
      // 权限控制
      this.routeList = this.pessionMenu()
      // 排序
      this.routeList = this.sortBySeq(this.routeList)
    },
    methods: {
      linkTo(data, key = 'path') {
        const path = data[key]
        if (this.$route.path !== path) {
          this.$router.replace(path)
        }
      },
      /**
       * @description 菜单匹配权限
       */
      pessionMenu() {
        // 循环本地路由  判断 传回来的路由中 是否存在，不存在设置showMenu为false
        menu.map((items) => {
          // 一级菜单地址匹配上,使用后端的name和排序seq, 样式
          if (this.urlTreeMap.get(items.path)) {
            items.seq = this.urlTreeMap.get(items.path).seq
            items.meta.title = this.urlTreeMap.get(items.path).name
            items.meta.bicon = this.urlTreeMap.get(items.path).icon
          }

          // 开发环境或者白名单 显示所有菜单
          if (!config.usePermission) {
            items.showMenu = items.showMenu === false ? items.showMenu : true
            return items
          }

          // 如果原先设置了showMenu=false 保持原值
          if (items.showMenu === false) {
            items.showMenu = false
            return items
          }

          // 有一个子菜单存在，父菜单也要存在
          if (items.children && items.children.length && !items.redirect) {
            let flage = false
            items.children = items.children.map((item) => {
              //  用生成的map的url进行匹配，每个子菜单判断是否显示
              item.showMenu = !!this.urlTreeMap.get(item.path)
              if (item.showMenu) {
                // 二级菜单名字用后端的
                item.meta.title = this.urlTreeMap.get(item.path).name
                // 二级菜单排序用后端的
                item.seq = this.urlTreeMap.get(item.path).seq
                flage = true
              }
              return item
            })
            items.showMenu = !!flage // 整个菜单组是否显示
            return items
          }

          // 一级菜单重定向直接显示 【销售洞察 首页】
          if (this.urlTreeMap.get(items.redirect)) {
            console.log(items.redirect)
            items.showMenu = true
            items.seq = this.urlTreeMap.get(items.redirect).seq
            items.meta.title = this.urlTreeMap.get(items.redirect).name
          }

          // 首页写死在前端，排在第一位
          if (items.name === 'Home') {
            items.showMenu = true
            items.seq = 1
          }

          return items
        })
        return menu
      },
      // 按照seq 排序
      sortBySeq(list) {
        // 比较方法
        function sortFn(a, b) {
          return (a.seq || 99) - (b.seq || 99)
        }
        // 二级菜单
        list = list.map((item) => {
          if (item.children && item.children.length) {
            item.children = item.children.sort(sortFn)
          }
          return item
        })
        // 一级菜单
        return list.sort(sortFn)
      }
    }
  }
</script>
<style lang="scss">
  .left-mune-box-content {
    width: 100%;
    height: calc(100vh - 70px);
    overflow-y: scroll;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE 10+ */
    z-index: 1;
    overflow-x: hidden;
    .el-menu {
      border: 0;
    }
    .el-submenu [class^='menu-icon'],
    .firstMenuIcon {
      font-size: 18px !important;
      vertical-align: middle;
      margin-right: 5px;
      width: 24px;
      text-align: center;
      color: #6ea9f3;
    }
    &::-webkit-scrollbar {
      width: 4px; /*滚动条宽度*/
      height: 4px; /*滚动条高度*/
    }
    /*定义滚动条轨道 内阴影+圆角*/
    &::-webkit-scrollbar-track {
      border-radius: 100px; /*滚动条的背景区域的圆角*/
      background-color: transparent; /*滚动条的背景颜色*/
    }
    /*定义滑块 内阴影 + 圆角*/
    &::-webkit-scrollbar-thumb {
      border-radius: 0px; /*滚动条的圆角*/
      box-shadow: inset 0 0 6px rgba(16, 164, 209, 0.2);
      background-color: rgba(0, 0, 0, 0.07); /*滚动条的背景颜色*/
    }
    .el-menu-item,
    .el-submenu__title {
      height: 42px;
      line-height: 42px;
    }
    .el-submenu .el-menu-item {
      height: 35px;
      line-height: 35px;
      font-size: 13px;
    }
  }
</style>
