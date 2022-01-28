import request from '@u/http'
import qs from 'qs'

/**
 * 唯品账单校验 api
 */

// 唯品账单校验 - 获取分页数据
export const listVipAutoVerification = (data) => {
  return request({
    url: '/webapi/report/vipAutoVeri/listVipAutoVerification.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 唯品账单校验 - 导出
export const exportVipAutoVerification =
  '/webapi/report/vipAutoVeri/export.asyn'
