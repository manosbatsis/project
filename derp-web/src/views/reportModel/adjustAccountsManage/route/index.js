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

// 核算单价管理
const route = {
  path: '/adjustAccounts',
  name: 'adjustAccounts',
  component: () => import('@/layout/index'),
  meta: {
    title: '核算单价管理',
    icon: 'menu-icon el-icon-c-scale-to-original'
  },
  children: [
    {
      path: '/adjustAccounts/corspriceList', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/corspriceList'), // 页面
      name: 'adjustAccounts-corspriceList', // 名称
      meta: {
        title: '成本单价列表'
      }
    },
    {
      path: '/adjustAccounts/corspriceDetail', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/corspriceDetail'), // 页面
      name: 'adjustAccounts-corspriceDetail', // 名称
      hide: true,
      meta: {
        title: '标准成本单价详情',
        nocache: true, // 是否不缓存数据
        routeParentUrl: '/adjustAccounts/corspriceList'
      }
    },
    {
      path: '/adjustAccounts/corspriceAdd', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/corspriceEdit'), // 页面
      name: 'corspriceAdd', // 名称
      hide: true,
      meta: {
        title: '标准成本单价新增',
        nocache: true, // 是否不缓存数据
        routeParentUrl: '/adjustAccounts/corspriceList'
      }
    },
    {
      path: '/adjustAccounts/corspriceEdit', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/corspriceEdit'), // 页面
      name: 'corspriceEdit', // 名称
      hide: true,
      meta: {
        title: '标准成本单价编辑',
        nocache: true, // 是否不缓存数据
        routeParentUrl: '/adjustAccounts/corspriceList'
      }
    },
    {
      path: '/adjustAccounts/weighPriceList', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/weighPriceList'), // 页面
      name: 'adjustAccounts-weighPriceList', // 名称
      meta: {
        title: '加权单价列表'
      }
    },
    {
      path: '/adjustAccounts/SDweighPriceList', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/SDweighPriceList'), // 页面
      name: 'adjustAccounts-SDweighPriceList', // 名称
      meta: {
        title: 'SD加权单价列表'
      }
    },
    {
      path: '/adjustAccounts/InterestSD', // 路径
      component: () => import('@v/reportModel/adjustAccountsManage/InterestSD'), // 页面
      name: 'InterestSD', // 名称
      meta: {
        title: '利息SD单价'
      }
    },
    {
      path: '/adjustAccounts/costPriceDifference', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/CostPriceDifference'), // 页面
      name: 'costPriceDifference', // 名称
      meta: {
        title: '成本单价差异表'
      }
    },
    {
      path: '/adjustAccounts/FixedCostPrice', // 路径
      component: () =>
        import('@v/reportModel/adjustAccountsManage/FixedCostPrice'), // 页面
      name: 'FixedCostPrice', // 名称
      meta: {
        title: '固定成本价盘维护'
      }
    }
  ]
}

export default route
