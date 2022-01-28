import request from '@u/http'
import qs from 'qs'

// 账期提醒配置-获取分页数据
export async function getListByPage(data) {
  return request({
    url: '/webapi/order/accountingReminderConfig/getListByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 获取事业部列表
export const getBuList = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getBuList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 事业群销售达成
export const getTargetAndAchieve = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getTargetAndAchieve.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 品牌在库天数
export const getBrandInWarehouse = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getBrandInWarehouse.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 品牌销量TOP8
export const getBrandSaleTop = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getBrandSaleTop.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 客户销量TOP8
export const getCusSaleTop = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getCusSaleTop.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 仓库效期预警
export const getDepotExpireWarning = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getDepotExpireWarning.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 仓库滞销预警
export const getDepotUnsellableWarning = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getDepotUnsellableWarning.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 商品在库天数
export const getInWarehouseData = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getInWarehouseData.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 库存量分析
export const getInventoryAnalysisData = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getInventoryAnalysisData.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 仓库滞销预警、仓库效期预警 获取公司
export const getMerchantList = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getMerchantList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 年度进销存分析
export const getYearFinanceInventoryAnalysis = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getYearFinanceInventoryAnalysis.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 仓库滞销预警获取仓库信息
export const getDepotList = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getDepotList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 仓库效期预警获取品牌信息
export const getBrandList = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getBrandList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 获取仓库滞销预警各品牌金额明细
export const getDepotUnsellableDetail = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getDepotUnsellableDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 获取部门信息
export const getDepartList = (data) => {
  return request({
    url: '/webapi/report/retailAdmin/getDepartList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售洞察 - 事业群销售达成
export const exportTargetAndAchieveUrl =
  '/webapi/report/retailAdmin/ExportTargetAndAchieve.asyn'

// 销售洞察 - 导出品牌销量
export const exportBrandUrl = '/webapi/report/retailAdmin/exportBrand.asyn'

// 销售洞察 - 仓库效期预警
export const exportDepotExpireWarningUrl =
  '/webapi/report/retailAdmin/exportDepotExpireWarning.asyn'

// 销售洞察 - 仓库滞销预警
export const exportDepotUnsellableWarningUrl =
  '/webapi/report/retailAdmin/exportDepotUnsellableWarning.asyn'

// 销售洞察 - 导出商品在库天数
export const exportInWarehouseDaysUrl =
  '/webapi/report/retailAdmin/exportInWarehouseDays.asyn'

// 销售洞察 - 导出库存量分析
export const exportInventoryAnalysisUrl =
  '/webapi/report/retailAdmin/exportInventoryAnalysis.asyn'

// 销售洞察 - 导出年度进销存分析
export const exportYearFinanceInventoryAnalysisUrl =
  '/webapi/report/retailAdmin/exportYearFinanceInventoryAnalysis.asyn'

// 销售洞察 - 导出客户销量
export const exportgetCustomersUrl =
  '/webapi/report/retailAdmin/exportgetCustomers.asyn'
