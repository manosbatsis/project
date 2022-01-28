/**
 * 客户额度预警 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 客户额度预警 - 查询列表数据 分页
export const getcustomerQuotaWarningList = (data) => {
  return request({
    url: '/webapi/order/customerQuotaWarning/listDTOByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 客户额度预警 - 刷新
export const refreshCustomerQuotaWarning = (data) => {
  return request({
    url: '/webapi/order/customerQuotaWarning/refreshCustomerQuotaWarning',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 客户额度预警 - 详情
export const getcustomerQuotaWarningDetail = (data) => {
  return request({
    url: '/webapi/order/customerQuotaWarning/getItemListByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 客户额度预警 - 导出
export const exportCustomerQuotaWarningUrl =
  '/webapi/order/customerQuotaWarning/exportCustomerQuotaWarning.asyn'
