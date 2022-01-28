/**
 * 领料出库单 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 领料出库单 - 查询领料出库单列表分页信息
export const listMaterialOut = (data) => {
  return request({
    url: '/webapi/order/materialOutDepot/listMaterialOut.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料出库单 - 获取导出数量
export const getMaterialOutCount = (data) => {
  return request({
    url: '/webapi/order/materialOutDepot/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料出库单 - 详情获取单据信息
export const detailMaterialOut = (data) => {
  return request({
    url: '/webapi/order/materialOutDepot/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料出库单 - 导出
export const exportMaterialOutDepotUrl =
  '/webapi/order/materialOutDepot/exportMaterialOutDepot.asyn'

// 销售管理 - 领料出库单 - 导入
export const importMaterialOutDepotUrl =
  '/webapi/order/materialOutDepot/importMaterialOutDepot.asyn'

// 销售管理 - 领料出库单 - 删除
export const delMaterialOutDepot = (data) => {
  return request({
    url: '/webapi/order/materialOutDepot/delMaterialOutDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料出库单 - 审核
export const auditOutMaterialOrder = (data) => {
  return request({
    url: '/webapi/order/materialOutDepot/auditMaterialOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
