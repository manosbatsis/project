import request from '@u/http'
import qs from 'qs'

/**
 * 月结自核对 api
 */

// 月结自核对 - 列表
export const listMonthlyAccountAutoVeri = (data) => {
  return request({
    url: '/webapi/report/monthlyAccountAutoVeri/listMonthlyAccountAutoVeri.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 月结自核对 - 刷新
export const flashMonthlyAccountAutoVeri = (data) => {
  return request({
    url: '/webapi/report/monthlyAccountAutoVeri/flash.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 月结自核对 - 导出
export const exportAutoVerification =
  '/webapi/report/monthlyAccountAutoVeri/exportAutoVerification.asyn'
