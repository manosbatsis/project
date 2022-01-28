/**
 * 库位调整单列表(新) api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 库位调整单列表(新) - 列表
export const getLocationAdjustmentList = (data) => {
  return request({
    url: '/webapi/order/locationAdjustment/listLocationAdjustment.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表(新) - 刷新
export const refreshLocationAdjust = (data) => {
  return request({
    url: '/webapi/order/locationAdjustment/refreshLocationAdjust.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表(新) - 审核
export const auditLocationAdjust = (data) => {
  return request({
    url: '/webapi/order/locationAdjustment/auditLocationAdjust.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表(新) - 删除
export const delLocationAdjust = (data) => {
  return request({
    url: '/webapi/order/locationAdjustment/delLocationAdjust.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表(新) - 获取导出的数量
export const getLocationAdjustmentCount = (data) => {
  return request({
    url: '/webapi/order/locationAdjustment/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 库位调整单列表(新) - 导出
export const exportLocationAdjust =
  '/webapi/order/locationAdjustment/exportLocationAdjust.asyn'

// 销售管理 - 库位调整单列表(新) - 导入
export const importLocationAdjust =
  '/webapi/order/locationAdjustment/importLocationAdjust.asyn'
