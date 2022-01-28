/**
 * 核算单价管理模块
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 固定成本价盘 api
 */

// 核算单价管理 - 固定成本价盘 - 获取固定成本价盘分页数据
export const getFixedCostPriceList = (data) => {
  return request({
    url: '/webapi/system/fixedCostPrice/listByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 固定成本价盘 - 删除
export const deleteFixedCostPrice = (data) => {
  return request({
    url: '/webapi/system/fixedCostPrice/delCostPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 固定成本价盘 - 审核
export const auditFixedCostPrice = (data) => {
  return request({
    url: '/webapi/system/fixedCostPrice/auditCostPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 固定成本价盘 - 导入
export const importFixedCostPrice =
  '/webapi/system/fixedCostPrice/importCostPrice.asyn'

// 核算单价管理 - 固定成本价盘 - 导出
export const exportFixedCostPrice =
  '/webapi/system/fixedCostPrice/exportCostPrice.asyn'
