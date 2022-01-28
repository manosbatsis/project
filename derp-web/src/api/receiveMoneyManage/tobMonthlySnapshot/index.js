/**
 * tob月结快照
 */

import request from '@u/http'
import qs from 'qs'

// tob月结快照 - 收入月结分页数据
export const listToBTempMonthlySnapshot = (data) => {
  return request({
    url: '/webapi/order/toBTempRevenueMonthlySnapshot/listToBTempMonthlySnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// tob月结快照 - 收入月结刷新
export const flashTobTempMonthlySnapshot = (data) => {
  return request({
    url: '/webapi/order/toBTempRevenueMonthlySnapshot/flashTobTempMonthlySnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// tob月结快照 - 收入月结导出
export const exportToBTempRevenueMonthlySnapshotUrl =
  '/webapi/order/toBTempRevenueMonthlySnapshot/exportToBTempRevenueMonthlySnapshot.asyn'

// tob月结快照 - 费用月结分页数据
export const listToBTempMonthlySnapshotCost = (data) => {
  return request({
    url: '/webapi/order/toBTempCostMonthlySnapshot/listToBTempMonthlySnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// tob月结快照 - 费用月结刷新
export const flashTobTempMonthlySnapshotCost = (data) => {
  return request({
    url: '/webapi/order/toBTempCostMonthlySnapshot/flashTobTempMonthlySnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// tob月结快照 - 费用月结导出
export const exportToBTempRevenueMonthlySnapshotCostUrl =
  '/webapi/order/toBTempCostMonthlySnapshot/exportToBTempCostMonthlySnapshot.asyn'
