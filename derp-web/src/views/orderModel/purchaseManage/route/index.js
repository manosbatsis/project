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

// 采购管理
const route = {
  path: '/purchase',
  name: 'purchase',
  component: () => import('@/layout/index'),
  meta: {
    title: '采购管理',
    icon: 'menu-icon el-icon-box'
  },
  children: [
    {
      path: '/purchase/purchaseorderList', // 路径
      component: () => import('@v/orderModel/purchaseManage/purchaseOrderList'), // 页面
      name: 'purchase-purchaseOrderList', // 名称
      meta: {
        title: '采购订单列表'
      }
    },
    {
      path: '/purchase/purchaseOrderedit/add', // 路径
      component: () => import('@v/orderModel/purchaseManage/purchaseOrderEdit'), // 页面
      name: 'purchase-purchaseOrderAdd', // 名称
      hide: true,
      meta: {
        title: '新增采购订单',
        nocache: true,
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/purchaseOrderedit/edit', // 路径
      component: () => import('@v/orderModel/purchaseManage/purchaseOrderEdit'), // 页面
      name: 'purchase-purchaseOrderEdit', // 名称
      hide: true,
      meta: {
        title: '编辑采购订单',
        nocache: true,
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/AuditForOa', // 路径
      component: () => import('@v/orderModel/purchaseManage/AuditForOa'), // 页面
      name: 'purchase-AuditForOa', // 名称
      hide: true,
      meta: {
        title: '发起oa审批',
        nocache: true,
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/purchaseOrderdetail', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/purchaseOrderDetail'), // 页面
      name: 'purchase-purchaseOrderDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购订单详情',
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/inwarehouse', // 路径
      component: () => import('@v/orderModel/purchaseManage/inWarehouse'), // 页面
      name: 'purchase-inWarehouse', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '打托入库',
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/predeclarationList', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/preDeclarationList'), // 页面
      name: 'purchase-preDeclarationList', // 名称
      meta: {
        title: '预申报单列表'
      }
    },
    {
      path: '/purchase/predeclareInwarehouse', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/predeclareInwarehouse'), // 页面
      name: 'purchase-predeclareInwarehouse', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '预申报单打托入库',
        routeParentUrl: '/purchase/predeclarationList'
      }
    },
    {
      path: '/purchase/predeclarationDetail', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/predeclarationDetail'), // 页面
      name: 'purchase-predeclarationDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '预申报单详情',
        routeParentUrl: '/purchase/predeclarationList'
      }
    },
    {
      path: '/purchase/predeclarationEdit', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/predeclarationEdit'), // 页面
      name: 'purchase-predeclarationEdit', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '编辑预申报单',
        routeParentUrl: '/purchase/predeclarationList'
      }
    },
    {
      path: '/purchase/editClearInfo', // 路径
      component: () => import('@v/orderModel/purchaseManage/editClearInfo'), // 页面
      name: 'purchase-editClearInfo', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '编辑清关资料',
        routeParentUrl: '/purchase/predeclarationList'
      }
    },
    {
      path: '/purchase/tallyBillList', // 路径
      component: () => import('@v/orderModel/purchaseManage/tallyBillList'), // 页面
      name: 'purchase-tallyBillList', // 名称
      meta: {
        title: '理货单列表'
      }
    },
    {
      path: '/purchase/tallyBilldetail', // 路径
      component: () => import('@v/orderModel/purchaseManage/tallyBillDetail'), // 页面
      name: 'purchase-tallyBillDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '理货订单详情',
        routeParentUrl: '/purchase/tallyBillList'
      }
    },
    {
      path: '/purchase/purchaseWarehouseList', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/purchaseWarehouseList'), // 页面
      name: 'purchase-purchaseWarehouseList', // 名称
      meta: {
        title: '采购入库列表'
      }
    },
    {
      path: '/purchase/purchaseWarehouseDetail', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/purchaseWarehouseDetail'), // 页面
      name: 'purchase-purchaseWarehouseDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购入库单详情',
        routeParentUrl: '/purchase/purchaseWarehouseList'
      }
    },
    {
      path: '/purchase/returnGoodsList', // 路径
      component: () => import('@v/orderModel/purchaseManage/returnGoodsList'), // 页面
      name: 'purchase-returnGoodsList', // 名称
      meta: {
        title: '采购退货列表'
      }
    },
    {
      path: '/purchase/returnGoodsEdit/:type', // 路径
      component: () => import('@v/orderModel/purchaseManage/returnGoodsEdit'), // 页面
      name: 'purchase-returnGoodsEdit', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '编辑采购退货订单', // type => add, edit
        routeParentUrl: '/purchase/returnGoodsList'
      }
    },
    {
      path: '/purchase/outWarehouse', // 路径
      component: () => import('@v/orderModel/purchaseManage/outWarehouse'), // 页面
      name: 'purchase-outWarehouse', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '打托出库',
        routeParentUrl: '/purchase/returnGoodsList'
      }
    },
    {
      path: '/purchase/returnGoodsDetail', // 路径
      component: () => import('@v/orderModel/purchaseManage/returnGoodsDetail'), // 页面
      name: 'purchase-returnGoodsDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购退货订单详情', // id 为01 为新增 其他未编辑
        routeParentUrl: '/purchase/returnGoodsList'
      }
    },
    {
      path: '/purchase/exWarehouseList', // 路径
      component: () => import('@v/orderModel/purchaseManage/exWarehouseList'), // 页面
      name: 'purchase-exWarehouseList', // 名称
      meta: {
        title: '采购退货出库列表'
      }
    },
    {
      path: '/purchase/exWarehouseDetail', // 路径
      component: () => import('@v/orderModel/purchaseManage/exWarehouseDetail'), // 页面
      name: 'purchase-exWarehouseDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购退货出库订单详情', // id 为01 为新增 其他未编辑
        routeParentUrl: '/purchase/exWarehouseList'
      }
    },
    {
      path: '/purchase/SDList', // 路径
      component: () => import('@v/orderModel/purchaseManage/SDList'), // 页面
      name: 'purchase-SDList', // 名称
      meta: {
        title: '采购SD单列表'
      }
    },
    {
      path: '/purchase/SDdetail', // 路径
      component: () => import('@v/orderModel/purchaseManage/SDdetail'), // 页面
      name: 'purchase-SDdetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购SD单详情',
        routeParentUrl: '/purchase/SDList'
      }
    },
    {
      path: '/purchase/contract', // 路径
      component: () => import('@v/orderModel/purchaseManage/contract'), // 页面
      name: 'purchase-contract', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '编辑采购合同',
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/procurementLink', // 路径
      component: () => import('@v/orderModel/purchaseManage/procurementLink'), // 页面
      name: 'purchase-procurementLink', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购链路',
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/checkContract', // 路径
      component: () => import('@v/orderModel/purchaseManage/checkContract'), // 页面
      name: 'purchase-purchseContract', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购合同详情',
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/addFinanceOrder', // 路径
      component: () => import('@v/orderModel/purchaseManage/addFinanceOrder'), // 页面
      name: 'purchase-addFinanceOrder', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '新增融资订单',
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/quotaWarmList', // 路径
      component: () => import('@v/orderModel/purchaseManage/quotaWarmList'), // 页面
      name: 'purchase-quotaWarmList', // 名称
      meta: {
        nocache: true,
        title: '项目额度预警'
      }
    },
    {
      path: '/purchase/quotaWarmingDetail', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/quotaWarmingDetail'), // 页面
      name: 'purchase-quotaWarmingDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: ' 项目额度预警详情',
        routeParentUrl: '/purchase/quotaWarmList'
      }
    },
    {
      path: '/purchase/invoiceList', // 路径
      component: () => import('@v/orderModel/purchaseManage/invoiceList'), // 页面
      name: 'purchase-invoiceList', // 名称
      meta: {
        title: '发票列表'
      }
    },
    {
      path: '/purchase/invoiceDetail', // 路径
      component: () => import('@v/orderModel/purchaseManage/invoiceDetail'), // 页面
      name: 'purchase-invoiceDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '发票详情',
        routeParentUrl: '/purchase/invoiceList'
      }
    },
    {
      path: '/purchase/receiveInvoiceEdit', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/receiveInvoiceEdit'), // 页面
      name: 'purchase-receiveInvoice', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '收到发票',
        routeParentUrl: '/purchase/purchaseorderList'
      }
    },
    {
      path: '/purchase/PurchaseFrameContract', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/PurchaseFrameContract'), // 页面
      name: 'purchase-PurchaseFrameContract', // 名称
      meta: {
        title: '采购框架合同列表'
      }
    },
    {
      path: '/purchase/PurchaseFrameContractDetail', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/PurchaseFrameContractDetail'), // 页面
      name: 'purchase-PurchaseFrameContractDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购框架合同详情',
        routeParentUrl: '/purchase/PurchaseFrameContract'
      }
    },
    {
      path: '/purchase/PurchaseTrialOrder', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/PurchaseTrialOrder'), // 页面
      name: 'purchase-PurchaseTrialOrder', // 名称
      meta: {
        title: '采购试单申请列表'
      }
    },
    {
      path: '/purchase/PurchaseTrialOrderDetail', // 路径
      component: () =>
        import('@v/orderModel/purchaseManage/PurchaseTrialOrderDetail'), // 页面
      name: 'purchase-PurchaseTrialOrderDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '采购试单申请详情',
        routeParentUrl: '/purchase/PurchaseTrialOrder'
      }
    }
  ]
}

export default route
