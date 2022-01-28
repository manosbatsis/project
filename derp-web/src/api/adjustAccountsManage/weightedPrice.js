import request from '@u/http'
import qs from 'qs'

/**
 * 加权单价 api
 */

// 核算单价管理 - 加权单价 - 分页
export const weightedPriceList = (data) => {
  return request({
    url: '/webapi/report/weightedPrice/listPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 加权单价 - 导出
export const exportWeightedPrice =
  '/webapi/report/weightedPrice/exportWeightedPrice.asyn'
