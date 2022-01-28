/**
 * 事业部库位类型 api
 */
import request from '@u/http'
import qs from 'qs'

// 事业部库位类型 - 分页
export const listByPage = (data) => {
  return request({
    url: '/webapi/system/buStockLocationTypeConfig/listByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部库位类型 - 新增
export const addStockLocationType = (data) => {
  return request({
    url: '/webapi/system/buStockLocationTypeConfig/addStockLocationType.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部库位类型 - 编辑
export const modifyStockLocationType = (data) => {
  return request({
    url: '/webapi/system/buStockLocationTypeConfig/modifyStockLocationType.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
