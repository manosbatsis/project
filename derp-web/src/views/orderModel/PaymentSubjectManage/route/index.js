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

// 费项管理
const route = {
  path: '/PaymentSubjectManage',
  name: 'PaymentSubjectManage',
  component: () => import('@/layout/index/index'),
  meta: {
    title: '费项管理',
    icon: 'menu-icon el-icon-tickets'
  },
  children: [
    {
      path: '/PaymentSubjectManage/ncreceipt', // 路径
      component: () => import('@v/orderModel/PaymentSubjectManage/NcReceipt'), // 页面
      name: 'NcReceipt', // 名称
      meta: {
        title: 'NC收支费项'
      }
    },
    {
      path: '/PaymentSubjectManage/settlementConfig', // 路径
      component: () =>
        import('@v/orderModel/PaymentSubjectManage/CostManage.vue'), // 页面
      name: 'settlementConfig', // 名称
      meta: {
        title: '应收费项配置'
      }
    }
  ]
}

export default route
