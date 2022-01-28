/**
 * 预售单列表 api
 */
import request from '@u/http'
import qs from 'qs'
// 销售管理 - 预售单列表 - 查询预售单列表信息
export const listPreSaleOrder = (data) => {
  return request({
    url: '/webapi/order/preSale/listPreSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 查询预售单列表信息
export const exportPreSaleOrder = (data) => {
  return request({
    url: '/webapi/order/preSale/exportPreSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 获取导出预售单的数量
export const getPreSaleOrderCount = (data) => {
  return request({
    url: '/webapi/order/preSale/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 删除
export const delPreSaleOrder = (data) => {
  return request({
    url: '/webapi/order/preSale/delPreSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 查询详情
export const getPreSaleDetails = (data) => {
  return request({
    url: '/webapi/order/preSale/toDetailsPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 新增/修改(仅保存)
export const saveOrModifyTempOrder = (data) => {
  return request({
    url: '/webapi/order/preSale/saveOrModifyTempOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 访问编辑页面
export const getPreSaleEditPage = (data) => {
  return request({
    url: '/webapi/order/preSale/toEditPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 修改并审核
export const modifyPreSaleOrder = (data) => {
  return request({
    url: '/webapi/order/preSale/modifyPreSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 提交并审核
export const addPreSaleOrder = (data) => {
  return request({
    url: '/webapi/order/preSale/addPreSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 根据ID获取仓库详情
export const getDepotDetails = (data) => {
  return request({
    url: '/webapi/system/depot/getDepotDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 校验预售单能否转成销售单
export const checkPreSale = (data) => {
  return request({
    url: '/webapi/order/preSale/checkPreSale.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 校验能否转成采购订单
export const checkPreOrderState = (data) => {
  return request({
    url: '/webapi/order/preSale/checkOrderState.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 生成采购订单
export const preGeneratePurchaseOrder = (data) => {
  return request({
    url: '/webapi/order/preSale/GeneratePurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 预售单列表 - 导出
export const exportPreSaleOrderUrl =
  '/webapi/order/preSale/exportPreSaleOrder.asyn'

// 销售管理 - 预售单列表 - 导入
export const importPreSaleUrl = '/webapi/order/preSale/importPreSale.asyn'
