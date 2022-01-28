/**
 *  参数说明
 *  path 路由的完整路径 (必填)
 *  name 名称 可填
 *  component 组件 (必填)
 *  redirect 跳转路径 redirect不为空时,不显示二级菜单
 *  hide  是否不展现在菜单栏 可填
 *  meta
 *    title 显示在tag栏, 左侧菜单栏名称名称 (必填)
 *    affix 是否固定tab栏
 *    nocache 是否不需要缓存  是相当于keepAlive false
 */

// 库位配置管理
const route = {
  path: '/stockLocation',
  name: 'notice',
  component: () => import('@/layout/index'),
  meta: {
    title: '库位类型配置',
    icon: 'menu-icon el-icon-document'
  },
  children: [
    {
      path: '/stockLocation/buStockLocation', // 路径
      component: () =>
        import('@v/systemModel/stockLocation/ListBuStockLocation.vue'), // 页面
      name: 'BuStockLocation', // 名称
      meta: {
        title: '事业部库位类型'
      }
    }
  ]
}

export default route
