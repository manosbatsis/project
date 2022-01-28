/*
 * @Author: your name
 * @Date: 2021-12-23 11:43:35
 * @LastEditTime: 2021-12-23 11:47:29
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \derp-web\src\api\customerManage\customerSale.js
 */
/**
 * 销售价格管理订单
 */
import request from '@u/http'
import qs from 'qs'

// 客商档案 - 销售价格管理 获取列表
export function listSalePrice(data) {
  return request({
    url: '/webapi/system/customerSale/listSalePrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 统计各个状态数据
export function listSalePriceCount(data) {
  return request({
    url: '/webapi/system/customerSale/listSalePriceCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 删除
export function delPriceSale(data) {
  return request({
    url: '/webapi/system/customerSale/delPriceSale.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 导入
export const importPriceSaleUrl =
  '/webapi/system/customerSale/importPriceSale.asyn'

export const exportPriceSaleUrl = '/webapi/system/customerSale/export.asyn'

// 客商档案 - 销售价格管理 提交
export function salesSubmitSMPrice(data) {
  return request({
    url: '/webapi/system/customerSale/submitSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 提交
export function salesAuditSMPrice(data) {
  return request({
    url: '/webapi/system/customerSale/auditSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 根据ID获取详情
export function getSalePriceDetails(data) {
  return request({
    url: '/webapi/system/customerSale/getSalePriceDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 编辑
export function editSMPrice(data) {
  return request({
    url: '/webapi/system/customerSale/editSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 申请作废
export function submitSaleInvalid(data) {
  return request({
    url: '/webapi/system/customerSale/submitInvalid.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 作废审核
export function auditSaleInvalid(data) {
  return request({
    url: '/webapi/system/customerSale/auditInvalid.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 新增
export function addCustomerPrice(data) {
  return request({
    url: '/webapi/system/customerSale/addCustomerPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 销售价格管理 获取附件管理 编码
export function salesPriceGetCode(params) {
  return request({
    url: '/webapi/system/customerSale/getCode.asyn',
    method: 'GET',
    params
  })
}
