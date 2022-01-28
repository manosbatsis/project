/**
 * 销售赊销单 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 销售赊销单 - 赊销申请
export const applyCreditOrder = (data) => {
  return request({
    url: '/webapi/order/saleCredit/applyCreditOrder.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 销售管理 - 销售赊销单 - 申请还款
export const applyCreditRefund = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/saleCredit/applyRefund.asyn',
    method: 'POST',
    data
  })
}

// 销售管理 - 销售赊销单 - 还款试算
export const calCreditRepayAmount = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/saleCredit/calRepayAmount.asyn',
    method: 'POST',
    data
  })
}

// 销售管理 - 销售赊销单 - 确认还款
export const confirmCreditCreditBill = (data) => {
  return request({
    url: '/webapi/order/saleCredit/confirmCreditBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 确认放款
export const confirmCreditOrder = (data) => {
  return request({
    url: '/webapi/order/saleCredit/confirmOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 删除赊销单
export const deleteCreditOrder = (data) => {
  return request({
    url: '/webapi/order/saleCredit/deleteCreditOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 获取列表赊销单各类型数量
export const getCreditTypeNum = (data) => {
  return request({
    url: '/webapi/order/saleCredit/getCreditTypeNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 访问申请还款页面
export const getCreditRepayDetail = (data) => {
  return request({
    url: '/webapi/order/saleCredit/getRepayDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 获取列表信息
export const getCreditRepayList = (data) => {
  return request({
    url: '/webapi/order/saleCredit/listDTOByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 提交还款
export const saveCreditBill = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/saleCredit/saveCreditBill.asyn',
    method: 'POST',
    data
  })
}

// // 销售管理 - 销售赊销单 - 导出试算结果
// export const exportCalResult = data => {
//   return request({
//     headers: { 'Content-Type': 'application/json' },
//     url: '/webapi/order/saleCredit/exportCalResult.asyn',
//     method: 'POST',
//     responseType: 'blob',
//     data
//   })
// }

// 销售管理 - 销售赊销单 - 导出试算结果
export const exportCalResultUrl =
  '/webapi/order/saleCredit/exportCalResult.asyn'

// 销售管理 - 销售赊销单 - 访问详情页面
export const searchCreditDetail = (data) => {
  return request({
    url: '/webapi/order/saleCredit/searchDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 提交赊销单
export const submitCreditCreditOrder = (data) => {
  return request({
    url: '/webapi/order/saleCredit/submitCreditOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 收到保证金
export const updateCreditMarginOrder = (data) => {
  return request({
    url: '/webapi/order/saleCredit/updateMarginOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 确认收款页面获取收款账单
export const searchBillOrder = (data) => {
  return request({
    url: '/webapi/order/saleCredit/searchBillOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 详情页面获取收款账单详情
export const searchBillItemDetail = (data) => {
  return request({
    url: '/webapi/order/saleCredit/searchBillItemDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售赊销单 - 导出结算单
export const exportCreditOrderUrl =
  '/webapi/order/saleCredit/exportCreditOrder.asyn'
