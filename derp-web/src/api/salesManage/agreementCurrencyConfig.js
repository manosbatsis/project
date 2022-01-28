/**
 * 协议单价 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 协议单价 - 协议单价列表查询
export const getAgreementCurrencyList = (data) => {
  return request({
    url: '/webapi/order/agreementCurrencyConfig/listAgreementCurrencyConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 协议单价 - 删除
export const delAgreementCurrency = (data) => {
  return request({
    url: '/webapi/order/agreementCurrencyConfig/delAgreementCurrencyConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 协议单价 - 获取导出协议单价的数量
export const getAgreementCurrencyCount = (data) => {
  return request({
    url: '/webapi/order/agreementCurrencyConfig/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 协议单价 - 新增
export const addAgreementCurrency = (data) => {
  return request({
    url: '/webapi/order/agreementCurrencyConfig/addAgreementCurrencyConfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 协议单价 - 导出
export const exportAgreementCurrencyUrl =
  '/webapi/order/agreementCurrencyConfig/exportAgreementCurrencyConfig.asyn'

// 销售管理 - 协议单价 - 导入
export const importAgreementCurrencyUrl =
  '/webapi/order/agreementCurrencyConfig/importAgreementCurrencyConfig.asyn'
