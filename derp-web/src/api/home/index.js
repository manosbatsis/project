import request from '@u/http'
import qs from 'qs'

// 首页 - 待录入时间列表
export const getPendingRecordOrders = (data) => {
  return request({
    url: '/webapi/order/external/getPendingRecordOrders.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 待上架列表
export const getPendingShelfOrders = (data) => {
  return request({
    url: '/webapi/order/external/getPendingShelfOrders.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 待确认列表
export const getPendingConfirmOrders = (data) => {
  return request({
    url: '/webapi/order/external/getPendingConfirmOrders.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 待审核列表
export const getPendingCheckOrders = (data) => {
  return request({
    url: '/webapi/order/external/getPendingCheckOrders.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 待结转列表
export const getPendingCarryForward = (data) => {
  return request({
    url: '/webapi/inventory/external/getPendingCarryForward.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - SKU预警列表
export const getSkuPriceWarns = (data) => {
  return request({
    url: '/webapi/report/external/getSkuPriceWarns.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 根据商家、仓库统计当天效期预警
export const countInventoryWarning = (data) => {
  return request({
    url: '/webapi/report/external/countInventoryWarning.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 购销top10 饼状图
export const countSaleOutOrderSaleNum = (data) => {
  return request({
    url: '/webapi/report/external/countSaleOutOrderSaleNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 购销top10 柱状图
export const getTop10SaleOutOrderBrand = (data) => {
  return request({
    url: '/webapi/report/external/getTop10SaleOutOrderBrand.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 代销top10 饼状图
export const countBillOutDepotOrderNum = (data) => {
  return request({
    url: '/webapi/report/external/countBillOutDepotOrderNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 一件代发top10 柱状图
export const getTop10OrderBrand = (data) => {
  return request({
    url: '/webapi/report/external/getTop10OrderBrand.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 统计所有客户代销模式下销售top 10 的品牌
export const getBillOutDepotTop10ByBrand = (data) => {
  return request({
    url: '/webapi/report/external/getBillOutDepotTop10ByBrand.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - POPtop10 饼状图
export const countOrderSaleNum = (data) => {
  return request({
    url: '/webapi/report/external/countOrderSaleNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 汇总该公司主体下各事业部库存总量占所有仓库库存总量的比例
export const countBuInventory = (data) => {
  return request({
    url: '/webapi/inventory/external/countBuInventory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 事业部库存仪表盘-汇总
export const countBuInventoryRate = (data) => {
  return request({
    url: '/webapi/inventory/external/countBuInventoryRate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 统计各待办事项数量
export const countPendingOrderNum = (data) => {
  return request({
    url: '/webapi/order/external/countPendingOrderNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 统计SKU预警数量
export const countSkuPriceWarns = (data) => {
  return request({
    url: '/webapi/report/external/countSkuPriceWarns.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 首页 - 统计月库存结转中结转状态为未结转的仓库月结记录数量
export const countPendingCarryForwardNum = (data) => {
  return request({
    url: '/webapi/inventory/external/countPendingCarryForwardNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
