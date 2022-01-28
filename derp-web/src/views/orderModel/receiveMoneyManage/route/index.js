// 收款管理
const route = {
  path: '/receiveMoney',
  name: 'receiveMoney',
  component: () => import('@/layout/index'),
  meta: {
    title: '收款管理',
    icon: 'menu-icon el-icon-bank-card'
  },
  children: [
    {
      path: '/receiveBill/list', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/listReceiveBill'), // 页面
      name: 'listReceiveBill', // 名称
      meta: {
        title: 'To B 应收账单'
      }
    },
    {
      path: '/receiveBill/add', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/listReceiveBillAdd'), // 页面
      name: 'listReceiveBillAdd', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '新增应收账单',
        routeParentUrl: '/receiveBill/list'
      }
    },
    {
      path: '/receiveBill/detail', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/listReceiveBillDetail'), // 页面
      name: 'listReceiveBillDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '应收详情',
        routeParentUrl: '/receiveBill/list'
      }
    },
    {
      path: '/receiveBill/addBillCost', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/listReceiveBilladdBillCost'), // 页面
      name: 'listReceiveBilladdBillCost', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '补充费用信息',
        routeParentUrl: '/receiveBill/list'
      }
    },
    {
      path: '/receiveBillVerification/list', // 路径
      component: () =>
        import(
          '@v/orderModel/receiveMoneyManage/listReceiveBillVerificationTap'
        ), // 页面
      name: 'listReceiveBillVerification', // 名称
      meta: {
        title: '收款核销跟踪'
      }
    },
    {
      path: '/receivemoney/toBTrackList', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toBTrackList.vue'), // 页面
      name: 'receivemoney-toBTrackList', // 名称
      meta: {
        title: 'To B收款跟踪表'
      }
    },
    {
      path: '/receivemoney/toBWriteoff', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toBWriteoff.vue'), // 页面
      name: 'receivemoney-toBWriteoff', // 名称
      meta: {
        title: 'To B暂估核销列表'
      }
    },
    {
      path: '/receivemoney/toBWriteoffDetail', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toBWriteoffDetail.vue'), // 页面
      name: 'receivemoney-toBWriteoffDetail', // 名称
      hide: true,
      meta: {
        title: 'To B暂估核销详情',
        nocache: true,
        routeParentUrl: '/receivemoney/toBWriteoff'
      }
    },
    {
      path: '/receivemoney/receivableAccountList', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/receivableAccountList.vue'), // 页面
      name: 'receivemoney-receivableAccountList', // 名称
      meta: {
        title: '应收账龄报告'
      }
    },
    {
      path: '/receivemoney/receivableAccountDetail', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/receivableAccountDetail.vue'), // 页面
      name: 'receivemoney-receivableAccountDetail', // 名称
      hide: true,
      meta: {
        title: '应收账龄报告详情',
        nocache: true,
        routeParentUrl: '/receivemoney/receivableAccountList'
      }
    },
    {
      path: '/receivemoney/AdvanceReceipt', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/AdvanceReceipt'), // 页面
      name: 'AdvanceReceipt', // 名称
      meta: {
        title: '预收账单列表'
      }
    },
    {
      path: '/receivemoney/AdvanceReceiptAdd', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/AdvanceReceiptEdit'), // 页面
      name: 'AdvanceReceiptAdd', // 名称
      hide: true,
      meta: {
        title: '预收账单新增',
        nocache: true,
        routeParentUrl: '/receivemoney/AdvanceReceipt'
      }
    },
    {
      path: '/receivemoney/AdvanceReceiptEdit', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/AdvanceReceiptEdit'), // 页面
      name: 'AdvanceReceiptEdit', // 名称
      hide: true,
      meta: {
        title: '预收账单编辑',
        nocache: true,
        routeParentUrl: '/receivemoney/AdvanceReceipt'
      }
    },
    {
      path: '/receivemoney/AdvanceReceiptDetail', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/AdvanceReceiptDetail'), // 页面
      name: 'AdvanceReceiptDetail', // 名称
      hide: true,
      meta: {
        title: '预收账单详情',
        nocache: true,
        routeParentUrl: '/receivemoney/AdvanceReceipt'
      }
    },
    {
      path: '/receivemoney/receiveinvoice', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/ReceiveInvoice'), // 页面
      name: 'ReceiveInvoice', // 名称
      meta: {
        title: '应收发票库'
      }
    },
    {
      path: '/receivemoney/uploadinvoice', // 路径
      component: () => import('@v/orderModel/receiveMoneyManage/UploadInvoice'), // 页面
      name: 'UploadInvoice', // 名称
      hide: true,
      meta: {
        title: '上传盖章发票',
        nocache: true,
        routeParentUrl: '/receivemoney/receiveinvoice'
      }
    },
    {
      path: '/receivemoney/ReplacementInvoice', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/ReplacementInvoice'), // 页面
      name: 'ReplacementInvoice', // 名称
      hide: true,
      meta: {
        title: '替换发票',
        nocache: true,
        routeParentUrl: '/receivemoney/receiveinvoice'
      }
    },
    {
      path: '/receivemoney/toCSettlementList', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toCSettlementList'), // 页面
      name: 'toCSettlementList', // 名称
      meta: {
        title: 'To C 应收账单'
      }
    },
    {
      path: '/receivemoney/toCSettlemenDetail', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toCSettlemenDetail'), // 页面
      name: 'toCSettlemenDetail', // 名称
      hide: true,
      meta: {
        title: '应收详情',
        nocache: true,
        routeParentUrl: '/receivemoney/toCSettlementList'
      }
    },
    {
      path: '/receivemoney/toCSettlementEdit', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toCSettlementEdit'), // 页面
      name: 'toCSettlementEdit', // 名称
      hide: true,
      meta: {
        title: 'to_c应收编辑',
        nocache: true,
        routeParentUrl: '/receivemoney/toCSettlementList'
      }
    },
    {
      // 预付开票
      path: '/receivemoney/createBill', // 路径
      component: () => import('@v/orderModel/receiveMoneyManage/createBill'), // 页面
      name: 'createBill', // 名称
      hide: true,
      meta: {
        title: '开票',
        nocache: true,
        routeParentUrl: '/receivemoney/toCSettlementList'
      }
    },
    {
      path: '/receivemoney/repairDeduction', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/repairDeduction'), // 页面
      name: 'repairDeduction', // 名称
      hide: true,
      meta: {
        title: '补充费用信息',
        nocache: true,
        routeParentUrl: '/receivemoney/toCSettlementList'
      }
    },
    {
      path: '/receivemoney/tocestimated', // 路径
      component: () => import('@v/orderModel/receiveMoneyManage/TocEstimated'), // 页面
      name: 'TocEstimated', // 名称
      meta: {
        title: 'To C暂估应收表'
      }
    },
    {
      path: '/receivemoney/tocestimateddetail', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/TocEstimatedDetail'), // 页面
      name: 'TocEstimatedDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: 'To C暂估应收表详情',
        routeParentUrl: '/receivemoney/tocestimated'
      }
    },
    {
      // tob 开票
      path: '/receiveBill/toInvoicePage',
      component: () => import('@v/orderModel/receiveMoneyManage/toInvoicePage'), // 页面
      name: 'toInvoicePage', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '开票',
        routeParentUrl: '/receiveBill/list'
      }
    },
    {
      // toc开票
      path: '/receivemoney/toInvoicePageToc', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toInvoicePageToC'), // 页面
      name: 'toInvoicePageToC', // 名称
      hide: true,
      meta: {
        title: '开票',
        nocache: true,
        routeParentUrl: '/receivemoney/toCSettlementList'
      }
    },
    {
      // 预收开票
      path: '/receivemoney/toInvoicePageAdvanceBill', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/toInvoicePageAdvance'), // 页面
      name: 'toInvoicePageAdvanceBill', // 名称
      hide: true,
      meta: {
        title: '开票',
        nocache: true,
        routeParentUrl: '/receivemoney/AdvanceReceipt'
      }
    },
    {
      path: '/receivemoney/statementplatform', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/StatementPlatform'), // 页面
      name: 'StatementPlatform', // 名称
      meta: {
        title: '平台结算单'
      }
    },
    {
      path: '/receivemoney/platformdetail/:id', // 路径
      component: () =>
        import('@v/orderModel/receiveMoneyManage/PlatformDetail'), // 页面
      name: 'PlatformDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '平台结算单详情',
        routeParentUrl: '/receivemoney/statementplatform'
      }
    },
    {
      path: '/receivemoney/feelistdetail/:id', // 路径
      component: () => import('@v/orderModel/receiveMoneyManage/FeelistDetail'), // 页面
      name: 'FeelistDetail', // 名称
      hide: true,
      meta: {
        nocache: true,
        title: '平台费用单详情',
        routeParentUrl: '/receivemoney/statementplatform'
      }
    },
    {
      path: '/receivemoney/CloseAccount',
      component: () => import('@v/orderModel/receiveMoneyManage/closeAccount'),
      name: 'CloseAccount',
      meta: {
        title: '应收关账'
      }
    }
  ]
}

export default route
