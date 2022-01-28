/**
 * 资金占用计算表 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 资金占用计算表 - 列表
export const ListOccupationRateConfig = (data) => {
  return request({
    url: '/webapi/order/occupationRateConfig/listDTOByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 资金占用费率配置新增/编辑
export const saveOccuptionRateCongfig = (data) => {
  return request({
    url: '/webapi/order/occupationRateConfig/saveOccuptionRateCongfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 资金占用费率配置删除
export const delOccuptionRateCongfig = (data) => {
  return request({
    url: '/webapi/order/occupationRateConfig/delOccuptionRateCongfig.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
