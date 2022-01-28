import request from '@u/http'
import qs from 'qs'

/**
 * SD加权单价 api
 */

// 核算单价管理 - SD加权单价 - 分页
export const sdWeightedPriceList = (data) => {
  return request({
    url: '/webapi/report/SDweightedPrice/listSDPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 核算单价管理 - SD加权单价 - 导出
export const sdExportWeightedPrice =
  '/webapi/report/SDweightedPrice/sdexportWeightedPrice.asyn'
