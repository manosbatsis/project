/**
 * 资金占用计算表 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 资金占用计算表 - 列表
export const getOccupationCapitalStatisticsList = (data) => {
  return request({
    url: '/webapi/order/occupationCapitalStatistics/listDTOByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 资金占用计算表 - 获取资金占用统计表导出数量
export const getOccupationCapitalStatisticsCount = (data) => {
  return request({
    url: '/webapi/order/occupationCapitalStatistics/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 资金占用计算表 - 导出
export const occupationCapitalStatisticsExport =
  '/webapi/order/occupationCapitalStatistics/export.asyn'
