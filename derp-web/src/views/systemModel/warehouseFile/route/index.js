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

// 仓库档案
const route = {
  path: '/warehousefile',
  name: 'warehousefile',
  component: () => import('@/layout/index/index'),
  meta: {
    title: '仓库档案',
    icon: 'menu-icon el-icon-house'
  },
  children: [
    {
      path: '/warehousefile/AreaConfig', // 路径
      component: () => import('@v/systemModel/warehouseFile/AreaConfig'), // 页面
      name: 'AreaConfig', // 名称
      meta: {
        title: '关区配置'
      }
    },
    {
      path: '/warehousefile/DepotList', // 路径
      component: () => import('@v/systemModel/warehouseFile/DepotList'), // 页面
      name: 'DepotList', // 名称
      meta: {
        title: '仓库列表'
      }
    },
    {
      path: '/warehousefile/DepotEdit', // 路径
      component: () => import('@v/systemModel/warehouseFile/DepotEdit'), // 页面
      name: 'DepotEdit', // 名称
      hide: true,
      meta: {
        title: '仓库编辑',
        nocache: true,
        routeParentUrl: '/warehousefile/DepotList'
      }
    },
    {
      path: '/warehousefile/DepotAdd', // 路径
      component: () => import('@v/systemModel/warehouseFile/DepotEdit'), // 页面
      name: 'DepotAdd', // 名称
      hide: true,
      meta: {
        title: '仓库新增',
        nocache: true,
        routeParentUrl: '/warehousefile/DepotList'
      }
    },
    {
      path: '/warehousefile/DepotDetail', // 路径
      component: () => import('@v/systemModel/warehouseFile/DepotDetail'), // 页面
      name: 'DepotDetail', // 名称
      hide: true,
      meta: {
        title: '仓库详情',
        nocache: true,
        routeParentUrl: '/warehousefile/DepotList'
      }
    }
  ]
}

export default route
