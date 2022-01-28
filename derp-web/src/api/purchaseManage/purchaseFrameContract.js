import request from '@u/http'
import qs from 'qs'

// 采购框架合同 - 获取分页数据
export const purchaseFrameContractList = (data) => {
  return request({
    url: '/webapi/order/purchaseFrameContract/listByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购框架合同 - 获取详情数据
export const purchaseFrameContractDetail = (data) => {
  return request({
    url: '/webapi/order/purchaseFrameContract/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购框架合同 - 获取刷新时间
export const purchaseFrameContractRefreshTime = (data) => {
  return request({
    url: '/webapi/order/purchaseFrameContract/getRefreshTime.asyn',
    method: 'GET',
    data: qs.stringify(data)
  })
}

// 采购框架合同 - 导出
export const exportPurchaseFrameContract =
  '/webapi/order/purchaseFrameContract/exportContract.asyn'
