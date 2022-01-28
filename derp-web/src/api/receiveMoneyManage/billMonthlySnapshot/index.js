/**
 * 月结快照
 */

import request from '@u/http'
import qs from 'qs'

// 月结快照 - 列表
export const listBillMonthlySnapshot = (data) => {
  return request({
    url: '/webapi/order/billMonthlySnapshot/listBillMonthlySnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 月结快照 - 刷新
export const refreshMonthlySnapshot = (data) => {
  return request({
    url: '/webapi/order/billMonthlySnapshot/refreshMonthlySnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 月结快照 - 导出
export const exportMonthlySnapshotUrl =
  '/webapi/order/billMonthlySnapshot/exportMonthlySnapshot.asyn'
