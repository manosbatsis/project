import request from '@u/http'
import qs from 'qs'

/**
 * 成本单价列表 api
 */

// 核算单价管理 - 成本单价列表 - 单价管理分页
export const settlementPriceList = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/listPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 获取导出的数量
export const getSettlementPriceCount = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/getSettlementPriceCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 提交审核
export const examineSettlementPrice = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/toExamine.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 获取品牌列表
export const getSettlementPricetBrand = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 详情
export const settlementPriceDetail = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 新增
export const saveSettlementPrice = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/report/settlementPrice/saveSettlementPrice.asyn',
    method: 'POST',
    data
  })
}

// 核算单价管理 - 成本单价列表 - 修改
export const modifySettlementPrice = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/report/settlementPrice/modifySettlementPrice.asyn',
    method: 'POST',
    data
  })
}

// 核算单价管理 - 成本单价列表 - 获取变更记录分页数据
export const listRecordPrice = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/listRecordPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 根据商品ID查看组合商品的详情
export const listAllGroupMerchandiseByGroupId = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/listAllGroupMerchandiseByGroupId.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 获取审批记录分页数据
export const listExamineList = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/listExamineList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 审核
export const examinePriceList = (data) => {
  return request({
    url: '/webapi/report/settlementPrice/examine.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 成本单价列表 - 导出
export const exportSettlementPriceUrl =
  '/webapi/report/settlementPrice/exportSettlementPrice.asyn'

// 核算单价管理 - 成本单价列表 - 导入
export const importSettlementPriceUrl =
  '/webapi/report/settlementPrice/importPrice.asyn'
