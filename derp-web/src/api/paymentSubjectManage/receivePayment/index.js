import request from '@u/http'
import qs from 'qs'

/**
 * 应收费项配置 api
 */

// 应收费项配置 - 平台费用映射 获取平台费用配置分页列表
export const platformSettlementConfigList = (data) => {
  return request({
    url: '/webapi/order/platformSettlementConfig/platformSettlementConfigList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置 - 平台费用映射 获取页面列表下拉
export const platformSettlementSelectList = (data) => {
  return request({
    url: '/webapi/order/platformSettlementConfig/toPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置 - 平台费用映射 新增
export const saveSettlementConfig = (data) => {
  return request({
    url: '/webapi/order/platformSettlementConfig/saveSettlementConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置 -  平台费用映射 编辑
export const modifySettlementConfig = (data) => {
  return request({
    url: '/webapi/order/platformSettlementConfig/modifySettlementConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置 - 平台费用映射 禁用、启用
export const isOrNotEnable = (data) => {
  return request({
    url: '/webapi/order/platformSettlementConfig/isOrNotEnable.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置 - 平台费用映射 访问详情页面
export const platformSettlementConfigDetail = (data) => {
  return request({
    url: '/webapi/order/platformSettlementConfig/toDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置 - 平台费用映射 详情
export const getSettlementConfigDetail = (data) => {
  return request({
    url: '/webapi/order/settlementConfig/toDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置 - 平台费用映射 导入
export const platformSettlementImport =
  '/webapi/order/platformSettlementConfig/import.asyn'

// 应收费项配置 - 平台费用映射 导出
export const platformSettlementExport =
  '/webapi/order/platformSettlementConfig/export.asyn'

// 应收费项配置  费项配置  获取上级费用项下拉框
export const parentProjectNameList = (data) => {
  return request({
    url: '/webapi/order/settlementConfig/parentProjectNameList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置  费项配置  分页
export const settlementConfigList = (data) => {
  return request({
    url: '/webapi/order/settlementConfig/settlementConfigList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置  费项配置  新增
export const tab1SaveSettlementConfig = (data) => {
  return request({
    url: '/webapi/order/settlementConfig/saveSettlementConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置  费项配置  导出
export const settlementExport = '/webapi/order/settlementConfig/export.asyn'

// 应收费项配置  费项配置  启用禁用
export const tab1IsOrNotEnable = (data) => {
  return request({
    url: '/webapi/order/settlementConfig/isOrNotEnable.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置  费项配置  详情
export const toDetail = (data) => {
  return request({
    url: '/webapi/order/settlementConfig/toDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收费项配置  费项配置  编辑
export const tab1ModifySettlementConfig = (data) => {
  return request({
    url: '/webapi/order/settlementConfig/modifySettlementConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
