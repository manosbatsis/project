/**
 * 核算单价管理模块
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 成本单价差异表 api
 */

// 核算单价管理 - 成本单价差异表 - 获取成本单价差异表列表
export const getCostPriceDifferenceList = (data) => {
  return request({
    url: '/webapi/system/costPriceDifference/listCostPriceDifference.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价差异表 - 刷新
export const refreshCostPriceDifference = (data) => {
  return request({
    url: '/webapi/system/costPriceDifference/refreshCostPriceDifference.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价差异表 - 开店公司+开店事业部获取类型下拉列表
export const getBuStockLocationTypeConfigSelect = (data) => {
  return request({
    url: '/webapi/system/buStockLocationTypeConfig/getSelectBeanByBu.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价差异表 - 导出
export const exportCostPriceDifference =
  '/webapi/system/costPriceDifference/exportCostPriceDifference.asyn'
