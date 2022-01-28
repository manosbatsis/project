/**
 * 事业部移库单列表 api
 */
import request from '@u/http'
import qs from 'qs'
// 销售管理 - 事业部移库单列表 - 查询事业部移库列表
export const getBuMoveInventoryList = (data) => {
  return request({
    url: '/webapi/order/buMoveInventory/listBuMoveInventory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 事业部移库单列表 - 访问详情页面
export const getBuMoveInventoryDetail = (data) => {
  return request({
    url: '/webapi/order/buMoveInventory/toDetailsPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 事业部移库单列表 - 获取导出的事业部移库单的数量
export const getBuMoveInventoryCount = (data) => {
  return request({
    url: '/webapi/order/buMoveInventory/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 事业部移库单列表 - 审核
export const auditBuMoveInventory = (data) => {
  return request({
    url: '/webapi/order/buMoveInventory/auditBuMoveInventory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 事业部移库单列表 - 删除
export const delBuMoveInventory = (data) => {
  return request({
    url: '/webapi/order/buMoveInventory/delBuMoveInventory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 事业部移库单列表 - 导出
export const exportBuMoveInventoryUrl =
  '/webapi/order/buMoveInventory/exportBuMoveInventory.asyn'

// 销售管理 - 事业部移库单列表 - 导入
export const importBuMoveInventoryUrl =
  '/webapi/order/buMoveInventory/importBuMoveInventory.asyn'
