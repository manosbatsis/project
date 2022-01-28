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

// 客商档案
const route = {
  path: '/contrast',
  name: 'contrast',
  component: () => import('@/layout/index/index'),
  meta: {
    title: '爬虫配置',
    icon: 'menu-icon el-icon-cpu'
  },
  children: [
    {
      path: '/contrast/ContrastComparisonList', // 路径
      component: () => import('@v/systemModel/contrast/ContrastComparisonList'), // 页面
      name: 'ContrastComparisonList', // 名称
      meta: {
        title: '爬虫商品对照表'
      }
    },
    {
      path: '/contrast/ContrastComparisonEdit', // 路径
      component: () => import('@v/systemModel/contrast/ContrastComparisonEdit'), // 页面
      name: 'ContrastComparisonEdit', // 名称
      hide: true,
      meta: {
        title: '爬虫商品对照编辑',
        nocache: true,
        routeParentUrl: '/contrast/ContrastComparisonList'
      }
    },
    {
      path: '/contrast/ContrastComparisonAdd', // 路径
      component: () => import('@v/systemModel/contrast/ContrastComparisonEdit'), // 页面
      name: 'ContrastComparisonAdd', // 名称
      hide: true,
      meta: {
        title: '爬虫商品对照新增',
        nocache: true,
        routeParentUrl: '/contrast/ContrastComparisonList'
      }
    },
    {
      path: '/contrast/ContrastComparisonDetail', // 路径
      component: () =>
        import('@v/systemModel/contrast/ContrastComparisonDetail'), // 页面
      name: 'ContrastComparisonDetail', // 名称
      hide: true,
      meta: {
        title: '爬虫商品对照详情',
        nocache: true,
        routeParentUrl: '/contrast/ContrastComparisonList'
      }
    },
    {
      path: '/contrast/ComparisonList', // 路径
      component: () => import('@v/systemModel/contrast/ComparisonList'), // 页面
      name: 'ComparisonList', // 名称
      meta: {
        title: '商品对照表'
      }
    },
    {
      path: '/contrast/ComparisonDetail', // 路径
      component: () => import('@v/systemModel/contrast/ComparisonDetail'), // 页面
      name: 'ComparisonDetail', // 名称
      hide: true,
      meta: {
        title: '商品对照表详情',
        nocache: true,
        routeParentUrl: '/contrast/ComparisonList'
      }
    },
    {
      path: '/contrast/ComparisonEdit', // 路径
      component: () => import('@v/systemModel/contrast/ComparisonEdit'), // 页面
      name: 'ComparisonEdit', // 名称
      hide: true,
      meta: {
        title: '商品对照表编辑',
        nocache: true,
        routeParentUrl: '/contrast/ComparisonList'
      }
    },
    {
      path: '/contrast/ComparisonAdd', // 路径
      component: () => import('@v/systemModel/contrast/ComparisonEdit'), // 页面
      name: 'ComparisonAdd', // 名称
      hide: true,
      meta: {
        title: '商品对照表新增',
        nocache: true,
        routeParentUrl: '/contrast/ComparisonList'
      }
    },
    {
      path: '/contrast/reptile',
      component: () => import('@v/systemModel/contrast/reptileList'),
      name: 'ReptileList',
      meta: {
        title: '爬虫数据列表'
      }
    },
    {
      path: '/contrast/reptileAdd', // 路径
      component: () => import('@v/systemModel/contrast/reptileAdd'), // 页面
      name: 'reptileAdd', // 名称
      hide: true,
      meta: {
        title: '新增爬虫配置',
        nocache: true,
        routeParentUrl: '/contrast/reptile'
      }
    },
    {
      path: '/contrast/reptileDetail', // 路径
      component: () => import('@v/systemModel/contrast/reptileDetail'), // 页面
      name: 'reptileDetail', // 名称
      hide: true,
      meta: {
        title: '爬虫配置详情',
        nocache: true,
        routeParentUrl: '/contrast/reptile'
      }
    },
    {
      path: '/contrast/LocationMap',
      component: () => import('@v/systemModel/contrast/LocationMap'),
      name: 'LocationMap',
      meta: {
        title: '库位映射表'
      }
    }
  ]
}

export default route
