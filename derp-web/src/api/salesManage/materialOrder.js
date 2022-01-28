/**
 * 领料单 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 领料单 - 查询领料单列表分页数据
export const listMaterialOrder = (data) => {
  return request({
    url: '/webapi/order/materialOrder/listOrderByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料单 - 获取导出数量
export const getMaterialCount = (data) => {
  return request({
    url: '/webapi/order/materialOrder/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料单 - 复制、新增、编辑、详情获取单据信息
export const detailMaterialOrder = (data) => {
  return request({
    url: '/webapi/order/materialOrder/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料单 - 删除
export const delMaterialOrder = (data) => {
  return request({
    url: '/webapi/order/materialOrder/delMaterialOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料单 - 保存
export const saveMaterialOrder = (data) => {
  return request({
    url: '/webapi/order/materialOrder/saveMaterialOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料单 - 保存并审核
export const auditMaterialOrder = (data) => {
  return request({
    url: '/webapi/order/materialOrder/auditMaterialOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 领料单 - 导出清关资料
export const exportCustomsInfo =
  '/webapi/order/materialOrder/exportCustomsInfo.asyn'

// 销售管理 - 领料单 - 导出
export const exportMaterialOrderUrl =
  '/webapi/order/materialOrder/exportMaterialOrder.asyn'

// 销售管理 - 领料单 - 导入
export const importMaterialOrderUrl =
  '/webapi/order/materialOrder/importMaterialOrder.asyn'
