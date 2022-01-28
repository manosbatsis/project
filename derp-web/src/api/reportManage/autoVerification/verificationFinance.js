import request from '@u/http'
import qs from 'qs'

/**
 * 业财自核对 api
 */

// 业财自核对 - 列表
export const listBusinessFinanceAutoVerification = (data) => {
  return request({
    url: '/webapi/report/businessFinanceAutoVeri/listBusinessFinanceAutoVerification.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 业财自核对 - 刷新
export const refreshexportBusinessFinanceAutoVerification = (data) => {
  return request({
    url: '/webapi/report/businessFinanceAutoVeri/refreshAutoVerification.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 业财自核对 - 导出
export const exportBusinessFinanceAutoVerification =
  '/webapi/report/businessFinanceAutoVeri/exportAutoVerification.asyn'
