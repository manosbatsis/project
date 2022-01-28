/**
 * 核算单价管理模块
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 利息SD单价 api
 */

// 核算单价管理 - 利息SD单价 - 获取列表分页信息
export const listSDPrice = (data) => {
  return request({
    url: '/webapi/report/SDinterestPrice/listSDPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - 利息SD单价 - 导出
export const sdexportInterestPriceUrl =
  '/webapi/report/SDinterestPrice/sdexportInterestPrice.asyn'
