/*
 * @Author: your name
 * @Date: 2021-12-22 18:03:10
 * @LastEditTime: 2021-12-22 18:33:52
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \derp-web\src\api\customerManage\supplierMerchandisePrice\index.js
 */
import request from '@u/http'
import qs from 'qs'
/**
 * 采购价格管理 api
 */

// 客商档案 - 采购价格管理 - 列表
export async function listSMPrice(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/listSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 统计各个状态下的采购价格数量
export async function statisticsStateNum(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/statisticsStateNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 提交
export async function submitSMPrice(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/submitSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 审核
export async function auditSMPrice(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/auditSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 删除
export async function delSMPrice(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/delSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 获取导出数量
export async function getOrderCount(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 采购价格管理详情
export async function getDetailsById(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 修改
export async function modifySMPrice(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/modifySMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 申请作废
export async function submitPurchaseInvalid(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/submitInvalid.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 作废审核
export async function invalidPurchaseAudit(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/invalidAudit.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 客商档案 - 采购价格管理 - 导出
export const exportSMPriceUrl =
  '/webapi/system/supplierMerchandisePrice/exportSMPrice.asyn'
// 客商档案 - 采购价格管理 - 导入
export const importSMPriceUrl =
  '/webapi/system/supplierMerchandisePrice/importSMPrice.asyn'

// 获取编码
export function getCode(params) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/getCode.asyn',
    method: 'GET',
    params
  })
}

/**
 * @description: 新建
 * @param {*} data
 * @return {*}
 */
export function addSMPrice(data) {
  return request({
    url: '/webapi/system/supplierMerchandisePrice/addSMPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
