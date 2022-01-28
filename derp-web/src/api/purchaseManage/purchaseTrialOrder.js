import request from '@u/http'
import qs from 'qs'

// 采购试单申请 - 获取分页数据
export const purchaseTryApplyOrderList = (data) => {
  return request({
    url: '/webapi/order/purchaseTryApplyOrder/listByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购试单申请 - 获取详情数据
export const purchaseTryApplyOrderDetail = (data) => {
  return request({
    url: '/webapi/order/purchaseTryApplyOrder/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购框架合同 - 获取刷新时间
export const purchaseTryApplyOrderRefreshTime = (data) => {
  return request({
    url: '/webapi/order/purchaseTryApplyOrder/getRefreshTime.asyn',
    method: 'GET',
    data: qs.stringify(data)
  })
}

// 采购试单申请 - 导出
export const exportPurchaseTryApplyOrder =
  '/webapi/order/purchaseTryApplyOrder/exportContract.asyn'
