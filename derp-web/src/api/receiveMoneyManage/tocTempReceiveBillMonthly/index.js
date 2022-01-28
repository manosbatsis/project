/**
 * toc月结快照
 */

import request from '@u/http'
import qs from 'qs'

// toc月结快照 - 收入月结分页数据
export const listReceiveByPage = (data) => {
  return request({
    url: '/webapi/order/tocTempReceiveBillMonthly/listReceiveByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// toc月结快照 - 费用月结分页数据
export const listCostByPage = (data) => {
  return request({
    url: '/webapi/order/tocTempReceiveBillMonthly/listCostByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// toc月结快照 - 费用导出
export const exportCostMonthly = (data) => {
  return request({
    url: '/webapi/order/tocTempReceiveBillMonthly/exportCostMonthly.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// toc月结快照 - 收入导出
export const exportReceiveMonthly = (data) => {
  return request({
    url: '/webapi/order/tocTempReceiveBillMonthly/exportReceiveMonthly.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// toc月结快照 - 刷新
export const tocTempReceiveBillMonthlyRefresh = (data) => {
  return request({
    url: '/webapi/order/tocTempReceiveBillMonthly/refresh.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
