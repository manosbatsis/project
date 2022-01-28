/**
 * 电商订单 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 电商订单 - 查询电商订单列表信息
export const getDeliveryOrderManageList = (data) => {
  return request({
    url: '/webapi/order/order/listOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 查询电商订单列表信息
export const getlistItemDetailsById = (data) => {
  return request({
    url: '/webapi/order/order/listItemDetailsById.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 获取导出电商订单的数量
export const getDeliveryOrderManageCount = (data) => {
  return request({
    url: '/webapi/order/order/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 获取商品信息列表
export const getShopInfoList = (data) => {
  return request({
    url: '/webapi/order/order/getShopInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 订单审核
export const examineDeliveryOrderManage = (data) => {
  return request({
    url: '/webapi/order/order/examineOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 删除手工导入订单
export const delDeliveryOrderManage = (data) => {
  return request({
    url: '/webapi/order/order/delOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 访问详情页面
export const getDeliveryOrderManageDetail = (data) => {
  return request({
    url: '/webapi/order/order/toDetailsPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 检查手工导入订单是否满足条件
export const ordercheckConditions = (data) => {
  return request({
    url: '/webapi/order/order/checkConditionsOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 导出
// export const exportDeliveryOrderManageUrl = '/webapi/order/order/exportOrder.asyn'

// 销售管理 - 电商订单 - 导出
export const exportDeliveryOrderManageUrl = (data) => {
  return request({
    url: '/webapi/order/order/exportOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 导入
export const importDeliveryOrderManageUrl =
  '/webapi/order/order/importOrder.asyn'

// 销售管理 - 电商订单 - 查询电商退订单列表信息
export const getReturnOrderManageList = (data) => {
  return request({
    url: '/webapi/order/orderReturnIdepot/listOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 查看某个商品的详情
export const getReturnOrderManageById = (data) => {
  return request({
    url: '/webapi/order/orderReturnIdepot/listItemDetailsById.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 审核
export const examineReturnOrderManage = (data) => {
  return request({
    url: '/webapi/order/orderReturnIdepot/examineOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 删除手工导入订单
export const delReturnOrderManage = (data) => {
  return request({
    url: '/webapi/order/orderReturnIdepot/delOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 获取导出电商订单退货的数量
export const getReturnOrderManageCount = (data) => {
  return request({
    url: '/webapi/order/orderReturnIdepot/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 访问详情页面
export const getReturnOrderManageDetail = (data) => {
  return request({
    url: '/webapi/order/orderReturnIdepot/toDetailsPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 检查手工导入订单是否满足条件
export const checkConditionsOrder = (data) => {
  return request({
    url: '/webapi/order/orderReturnIdepot/checkConditionsOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 导出
export const exportReturnOrderManageUrl =
  '/webapi/order/orderReturnIdepot/exportOrder.asyn'

// 销售管理 - 电商订单 - 导入
export const importReturnOrderManageUrl =
  '/webapi/order/orderReturnIdepot/importOrder.asyn'

// 销售管理 - 电商订单 - 获取事业部补录列表信息
export const listBusinessUnitRecord = (data) => {
  return request({
    url: '/webapi/order/order/listBusinessUnitRecord.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 事业部补录列表--批量更新
export const updateBusinessUnitRecord = (data) => {
  return request({
    url: '/webapi/order/order/updateBusinessUnitRecord.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 电商订单 - 导出
export const businessUnitRecordExportUrl =
  '/webapi/order/order/businessUnitRecordExport.asyn'
