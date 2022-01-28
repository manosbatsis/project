/**
 * 公司档案 api
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 税率配置 Api
 */

// 公司档案 - 税率配置 - 获取列表分页数据
export const listTariffRate = (data) => {
  return request({
    url: '/webapi/system/tariffRate/listTariffRate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 税率配置 - 删除
export const delTarffRate = (data) => {
  return request({
    url: '/webapi/system/tariffRate/delTarffRate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 税率配置 - 新增税率配置信息复制地址复制文档
export const saveTariffRate = (data) => {
  return request({
    url: '/webapi/system/tariffRate/saveTariffRate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 交易链路配置 - 列表
export const listTradeLinkConfig = (data) => {
  return request({
    url: '/webapi/order/tradeLinkConfig/listTradeLinkConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 交易链路配置 - 删除
export const deleConfig = (data) => {
  return request({
    // url: '/webapi/system/groupCommbarcode/delete.asyn',
    url: '/webapi/order/tradeLinkConfig/delTradeLinkConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 交易链路配置 - 保存
export const saveTradeLinkConfig = (data) => {
  return request({
    url: '/webapi/order/tradeLinkConfig/saveTradeLinkConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 交易链路配置 - 修改
export const modifyTradeLinkConfig = (data) => {
  return request({
    url: '/webapi/order/tradeLinkConfig/modifyTradeLinkConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 部门管理 - 访问列表页面
export const listDepartmentInfo = (data) => {
  return request({
    url: '/webapi/system/departmentInfo/listDepartmentInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 部门管理 - 编辑
export const modifyDepartmentInfo = (data) => {
  return request({
    url: '/webapi/system/departmentInfo/modifyDepartmentInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 部门管理 - 新增
export const saveDepartmentInfo = (data) => {
  return request({
    url: '/webapi/system/departmentInfo/saveDepartmentInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 获取列表分页数据
export const listMerchant = (data) => {
  return request({
    url: '/webapi/system/merchant/listMerchant.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 禁用启用公司信息
export const isOrNotEnable = (data) => {
  return request({
    url: '/webapi/system/merchant/isOrNotEnable.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// // 公司档案 - 公司信息 - 删除
// export const delMerchant = (data) => {
//   return request({
//     url: '/webapi/system/merchant/delMerchant.asyn',
//     method: 'POST',
//     data: qs.stringify(data)
//   })
// }

// 公司档案 - 公司信息 - 获取公司下拉列表
export const getMerchantSelectBean = (data) => {
  return request({
    url: '/webapi/system/merchant/getSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 获取下拉列表
export const getMerchantAndBuRelSelect = (data) => {
  return request({
    url: '/webapi/system/merchantBuRel/getSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 访问编辑页面
export const getMerchantEditPage = (data) => {
  return request({
    url: '/webapi/system/merchant/toEditPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 根据商家和仓库,获取商家仓库事业部表id数据
export const getSelectInfoByMerchantId = (data) => {
  return request({
    url: '/webapi/system/merchantDepotBuRel/getSelectInfoByMerchantId.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 根据ID获取商品详情
export const getMerchantInfoDetails = (data) => {
  return request({
    url: '/webapi/system/merchant/getMerchantInfoDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 修改
export const modifyMerchant = (data) => {
  return request({
    url: '/webapi/system/merchant/modifyMerchant.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 公司档案 - 公司信息 - 新增
export const saveMerchant = (data) => {
  return request({
    url: '/webapi/system/merchant/saveMerchant.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 公司档案 - 公司信息 - 访问详情页面
export const getMerchantDetail = (data) => {
  return request({
    url: '/webapi/system/merchant/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司信息 - 根据商家和仓库,获取商家仓库事业部表名称数据
export const getBuNameByMerchantDepot = (data) => {
  return request({
    url: '/webapi/system/merchantDepotBuRel/getBuNameByMerchantDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 事业部管理 - 获取列表分页数据
export const listBusinessUnits = (data) => {
  return request({
    url: '/webapi/system/businessUnit/listBusinessUnits.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 事业部管理 - 查询详情
export const detailBusinessUnits = (data) => {
  return request({
    url: '/webapi/system/businessUnit/detail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 事业部管理 - 保存、修改数据
export const saveBusinessUnit = (data) => {
  return request({
    url: '/webapi/system/businessUnit/saveBusinessUnit.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 事业部管理 - 获取部门名称下拉
export const getDepartSelectBean = (data) => {
  return request({
    url: '/webapi/system/businessUnit/getDepartSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 事业部管理 - 删除
export const delBusinessUnits = (data) => {
  return request({
    url: '/webapi/system/businessUnit/delBusinessUnits.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 店铺信息 - 列表
export const listShop = (data) => {
  return request({
    url: '/webapi/system/merchantShop/listShop.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 店铺信息 - 新增
export const saveShop = (data) => {
  return request({
    url: '/webapi/system/merchantShop/saveShop.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 店铺信息 - 获取编辑详情
export const shopToEditPage = (data) => {
  return request({
    url: '/webapi/system/merchantShop/toEditPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 店铺信息 - 编辑
export const modifyShop = (data) => {
  return request({
    url: '/webapi/system/merchantShop/modifyShop.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 店铺信息 - 编辑
export const shopDetail = (data) => {
  return request({
    url: '/webapi/system/merchantShop/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 店铺信息 - 编辑
export const delShop = (data) => {
  return request({
    url: '/webapi/system/merchantShop/delShop.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 店铺信息 - 导出
export const exportShopUrl = '/webapi/system/merchantShop/exportShop.asyn'

/**
 * 公司事业部 Api
 */

// 公司档案 - 公司事业部 - 获取列表分页数据
export const listMerchantBuRel = (data) => {
  return request({
    url: '/webapi/system/merchantBuRel/listMerchantBuRel.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司事业部 - 获取详情
export const detailMerchantBuRel = (data) => {
  return request({
    url: '/webapi/system/merchantBuRel/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司事业部 - 修改
export const modifyMerchantBuRel = (data) => {
  return request({
    url: '/webapi/system/merchantBuRel/modifyMerchantBuRel.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司事业部 - 新增
export const saveMerchantBuRel = (data) => {
  return request({
    url: '/webapi/system/merchantBuRel/saveMerchantBuRel.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公司档案 - 公司事业部 - 导出
export const exportMerchantBuRelUrl =
  '/webapi/system/merchantBuRel/exportMerchantBuRel.asyn'
