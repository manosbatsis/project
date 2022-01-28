/**
 * 平台暂估费用单 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 平台暂估费用单 - 查询平台暂估费用单
export const listPlatformTemporaryOrder = (data) => {
  return request({
    url: '/webapi/order/platformTemporaryCostOrder/listPlatformTemporaryOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台暂估费用单 - 校验发货日期是否已关帐
export const checkPlatformTemporaryOrder = (data) => {
  return request({
    url: '/webapi/order/platformTemporaryCostOrder/checkDate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台暂估费用单 - 删除
export const deletePlatformTemporaryOrder = (data) => {
  return request({
    url: '/webapi/order/platformTemporaryCostOrder/delete.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台暂估费用单 - 查询详情
export const detailPlatformTemporaryOrder = (data) => {
  return request({
    url: '/webapi/order/platformTemporaryCostOrder/toDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台暂估费用单 - 修改
export const updatePlatformCost = (data) => {
  return request({
    url: '/webapi/order/platformTemporaryCostOrder/updatePlatformCost.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 销售管理 - 平台暂估费用单 - 生成暂估费用单
export async function savePlatCostOrder(data) {
  return request({
    url: '/webapi/order/platformTemporaryCostOrder/savePlatCostOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 平台暂估费用单 - 导出
export async function exportPlatFormTemporaryUrl(params) {
  return request({
    url: '/webapi/order/platformTemporaryCostOrder/exportPlatFormTemporary.asyn',
    method: 'GET',
    params
  })
}
