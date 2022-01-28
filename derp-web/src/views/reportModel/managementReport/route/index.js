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

// 经营管理宝柏哦
const route = {
  path: '/managementReport',
  name: 'managementReport',
  component: () => import('@/layout/index/index'),
  meta: {
    title: '经营管理报表',
    icon: 'menu-icon el-icon-tickets'
  },
  children: [
    {
      path: '/managementReport/salesMoneySummary', // 路径
      component: () =>
        import('@v/reportModel/managementReport/salesMoneySummary'), // 页面
      name: 'salesMoneySummary', // 名称
      meta: {
        title: '部门销售金额统计'
      }
    },
    {
      path: '/managementReport/salesReachSummary', // 路径
      component: () =>
        import('@v/reportModel/managementReport/salesReachSummary'), // 页面
      name: 'salesReachSummary', // 名称
      meta: {
        title: '部门销售达成统计'
      }
    },
    {
      path: '/managementReport/DepartmentStockSummary', // 路径
      component: () =>
        import('@v/reportModel/managementReport/DepartmentStockSummary'), // 页面
      name: 'DepartmentStockSummary', // 名称
      meta: {
        title: '部门库存统计'
      }
    },
    {
      path: '/managementReport/DepartmentStockEmpty', // 路径
      component: () =>
        import('@v/reportModel/managementReport/DepartmentStockEmpty'), // 页面
      name: 'DepartmentStockEmpty', // 名称
      meta: {
        title: '部门库存清空天数统计'
      }
    }
  ]
}

export default route
