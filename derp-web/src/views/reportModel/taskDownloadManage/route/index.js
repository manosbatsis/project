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

// 自动化校验
const route = {
  path: '/task',
  name: 'task',
  component: () => import('@/layout/index/index'),
  meta: {
    title: '任务下载管理',
    icon: 'menu-icon el-icon-setting'
  },
  children: [
    {
      path: '/task/TaskList', // 路径
      component: () => import('@v/reportModel/taskDownloadManage/TaskList'), // 页面
      name: 'TaskList', // 名称
      meta: {
        title: '报表任务列表'
      }
    }
  ]
}

export default route
