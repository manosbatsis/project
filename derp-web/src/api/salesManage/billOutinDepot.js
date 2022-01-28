/**
 * 账单出库单 api
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 账单出库单 api
 */

// 销售管理 - 账单出库单 - 访问账单出库单列表
export const getBillOutinDepotList = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/listBillOutinDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 审核
export const auditBillOutinDepot = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/auditBillOutinDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 删除
export const delBillOutinDepot = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/delBillOutinDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 获取导出出库清单的数量
export const getBillOutinDepotCount = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 获取选中订单的所有商品和对应数量
export const getBillOutinDepotGoodsInfo = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/getOrderGoodsInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 库存可用量接口
export const getAvailableNum = (data) => {
  return request({
    url: '/webapi/inventory/queryAvailability/getAvailableNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 访问详情页面
export const getBillOutinDepotDetail = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/toDetailsPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 详情页分页查询明细
export const listBillOutinDepotItem = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/listBillOutinDepotItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 详情页分页查询批次
export const listBillOutinDepotBatch = (data) => {
  return request({
    url: '/webapi/order/billOutinDepot/listBillOutinDepotBatch.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 账单出库单 - 导出
export const exportBillOutinDepotUrl =
  '/webapi/order/billOutinDepot/exportAgreementCurrencyConfig.asyn'

// 销售管理 - 账单出库单 - 导入
export const importBillOutinDepotUrl =
  '/webapi/order/billOutinDepot/importBillOutinDepot.asyn'
