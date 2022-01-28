// 接口管理
const route = {
  path: '/interface',
  name: 'interface',
  component: () => import('@/layout/index'),
  meta: {
    title: '接口管理',
    icon: 'menu-icon el-icon-receiving'
  },
  children: [
    {
      path: '/interface/configList', // 路径
      component: () => import('@v/systemModel/interfaceManage/configList'), // 页面
      name: 'interface-config', // 名称
      meta: {
        title: '接口配置管理'
      }
    },
    {
      path: '/interface/addConfig', // 路径
      component: () => import('@v/systemModel/interfaceManage/addConfig'), // 页面
      name: 'interface-config-add', // 名称
      hide: true,
      meta: {
        title: '接口配置管理新增',
        nocache: true,
        routeParentUrl: '/interface/configList'
      }
    },
    {
      path: '/interface/externalList', // 路径
      component: () => import('@v/systemModel/interfaceManage/externalList'), // 页面
      name: 'api-external-list', // 名称
      meta: {
        title: '对外接口配置'
      }
    },
    {
      path: '/interface/addExternal', // 路径
      component: () => import('@v/systemModel/interfaceManage/addExternal'), // 页面
      name: 'interface-external-add', // 名称
      hide: true,
      meta: {
        title: '对外接口配置新增',
        nocache: true,
        routeParentUrl: '/interface/externalList'
      }
    }
  ]
}

export default route
