/**
 * 销售管理模块 费用差异调整表
 */
import request from '@u/http'
import qs from 'qs'

// 列表
export const listCostDiffOrder = (data) => {
  return request({
    url: '/webapi/order/toCTempCostDiffOrder/listCostDiffOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
