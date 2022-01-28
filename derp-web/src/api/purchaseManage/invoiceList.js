import request from '@u/http'
import qs from 'qs'

// 发票 - 分页
export const purchaseInvoiceList = (data) => {
  return request({
    url: '/webapi/order/purchaseInvoice/getListByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 发票 - 详情
export const purchaseInvoiceDetail = (data) => {
  return request({
    url: '/webapi/order/purchaseInvoice/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 发票 - 删除
export const purchaseInvoiceDelete = (data) => {
  return request({
    url: '/webapi/order/purchaseInvoice/delInvoice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
