/**
 * 应收关帐
 */

import request from '@u/http'
import qs from 'qs'

// 应收关帐 - 列表
export const listCloseAccount = (data) => {
  return request({
    url: '/webapi/order/receiveCloseAccount/listByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收关帐 - 更新关账与反关账
export const updateAccountStatus = (data) => {
  return request({
    url: '/webapi/order/receiveCloseAccount/updateAccountStatus.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收关帐 - 获取核销月份 (Tob/ToC)
export const getVerifyMonth = (data) => {
  return request({
    url: '/webapi/order/receiveCloseAccount/getVerifyMonth.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收关帐 - 刷新
export const closeAccountRefresh = (data) => {
  return request({
    url: '/webapi/order/receiveCloseAccount/refresh.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
