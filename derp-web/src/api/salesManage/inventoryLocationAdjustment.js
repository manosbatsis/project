/**
 * 库位调整单列表 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 库位调整单列表 - 库位调整单列表信息
export const getInventoryLocationList = (data) => {
  return request({
    url: '/webapi/order/inventoryLocationAdjustment/listInventoryLocationAdjust.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表 - 库位调整单列表信息
export const getInventoryLocationCount = (data) => {
  return request({
    url: '/webapi/order/inventoryLocationAdjustment/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表 - 访问详情页面
export const getInventoryLocationDetail = (data) => {
  return request({
    url: '/webapi/order/inventoryLocationAdjustment/toDetailsPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表 - 删除
export const delInventoryLocationDetail = (data) => {
  return request({
    url: '/webapi/order/inventoryLocationAdjustment/delInventoryLocationAdjust.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表 - 审核
export const auditInventoryLocationDetail = (data) => {
  return request({
    url: '/webapi/order/inventoryLocationAdjustment/auditInventoryLocationAdjust.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表 - 导出
export const exportInventoryLocationUrl =
  '/webapi/order/inventoryLocationAdjustment/exportInventoryLocationAdjust.asyn'

// 销售管理 - 库位调整单列表 - 导入
export const importInventoryLocationUrl =
  '/webapi/order/inventoryLocationAdjustment/importInventoryLocationAdjust.asyn'
