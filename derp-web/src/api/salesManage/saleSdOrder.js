/**
 * 销售SD api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 销售SD - 获取销售SD单分页列表
export const listSaleSD = (data) => {
  return request({
    url: '/webapi/order/saleSdOrder/listDTOByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售SD - 删除
export const delSaleSdOrder = (data) => {
  return request({
    url: '/webapi/order/saleSdOrder/delSaleSdOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售SD - 获取导出数量
export const getSaleSdCount = (data) => {
  return request({
    url: '/webapi/order/saleSdOrder/getCountOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售SD - 获取详情信息
export const detailPreSaleOrder = (data) => {
  return request({
    url: '/webapi/order/saleSdOrder/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售SD - 编辑
export const saveSaleSdOrder = (data) => {
  return request({
    url: '/webapi/order/saleSdOrder/saveSaleSdOrder.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 销售管理 - 销售SD - 导出
export const exportSaleSdOrderUrl =
  '/webapi/order/saleSdOrder/exportSaleSdOrder.asyn'

// 销售管理 - 销售SD - 导入销售SD单
export const importSaleSdOrderUrl =
  '/webapi/order/saleSdOrder/importSaleSdOrder.asyn'
