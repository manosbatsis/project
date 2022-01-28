// 系统管理
const route = {
  path: '/system',
  name: 'system',
  component: () => import('@/layout/index'),
  meta: {
    title: '系统管理',
    icon: 'menu-icon el-icon-coin'
  },
  children: [
    {
      path: '/system/userlist', // 路径
      component: () => import('@v/systemModel/systemManage/userList'), // 页面
      name: 'system-userList', // 名称
      meta: {
        title: '用户管理'
      }
    },
    {
      path: '/system/userlist/add',
      component: () => import('@v/systemModel/systemManage/addUser'),
      name: 'system-userList-add',
      hide: true,
      meta: {
        nocache: true,
        title: '新增用户',
        routeParentUrl: '/system/userlist'
      }
    },
    {
      path: '/system/roleList', // 路径
      component: () => import('@v/systemModel/systemManage/roleList'), // 页面
      name: 'system-roleList', // 名称
      meta: {
        title: '角色管理'
      }
    },
    {
      path: '/system/roleAssign', // 路径
      component: () => import('@v/systemModel/systemManage/roleListAssign'), // 页面
      name: 'system-roleAssign', // 名称
      hide: true,
      meta: {
        title: '分配权限',
        nocache: true,
        routeParentUrl: '/system/roleList'
      }
    },
    {
      path: '/system/permission', // 路径
      component: () => import('@v/systemModel/systemManage/permissionList'), // 页面
      name: 'system-permission', // 名称
      meta: {
        title: '权限管理'
      }
    },
    {
      path: '/system/permission/addPermisson', // 路径
      component: () => import('@v/systemModel/systemManage/addPermission'), // 页面
      name: 'system-permission-add', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '新增权限',
        routeParentUrl: '/system/permission'
      }
    }
  ]
}

export default route
