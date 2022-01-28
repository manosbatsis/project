import request from '@u/http'
import qs from 'qs'

/**
 * 云集账单校验 api
 */

// 云集账单校验 - 获取分页数据
export const listYunjiAutoVerification = (data) => {
  return request({
    url: '/webapi/report/yunjiAutoVeri/listYunjiAutoVerification.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 云集账单校验 - 统计导出数量
export const exportYunjiAutoVerificationNum = (data) => {
  return request({
    url: '/webapi/report/yunjiAutoVeri/getExportCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 云集账单校验 - 导出
export const exportYunjiAutoVerification =
  '/webapi/report/yunjiAutoVeri/export.asyn'
