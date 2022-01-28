/**
 * 平台采购单 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 平台采购单 - 查询平台采购单列表信息
export const listPlatformPurchaseOrder = (data) => {
  return request({
    url: '/webapi/order/platformPurchaseOrder/listPlatformPurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台采购单 - 查询详情
export const detailPlatformPurchaseOrder = (data) => {
  return request({
    url: '/webapi/order/platformPurchaseOrder/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台采购单 - 弹框获取平台采购单明细
export const getPlatformPurchaseOrderItems = (data) => {
  return request({
    url: '/webapi/order/platformPurchaseOrder/getPlatformPurchaseOrderItems.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台采购单 - 检查能否转销售单
export const checkOrderToSales = (data) => {
  return request({
    url: '/webapi/order/platformPurchaseOrder/checkOrderToSales.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台采购单 - 导出
export const exportPlatformPurchaseOrderUrl =
  '/webapi/order/platformPurchaseOrder/exportPlatformPurchaseOrder.asyn'

// 销售管理 - 平台采购单 - 转销售单获取销售货号
export const getSaleItems = (data) => {
  return request({
    url: '/webapi/order/platformPurchaseOrder/getSaleItems.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 销售管理 - 平台采购单 - 转销售订单保存
export const savePlatFormPurchaseToSales = (data) => {
  return request({
    url: '/webapi/order/platformPurchaseOrder/savePlatFormPurchaseToSales.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}
