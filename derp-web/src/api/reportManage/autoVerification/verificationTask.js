import request from '@u/http'
import qs from 'qs'

/**
 * 自动校验任务 api
 */

// 自动校验任务 - 改变电商平台时同时改变店铺的下拉选项
export const changeShopCodeLabel = (data) => {
  return request({
    url: '/webapi/report/automaticCheckTask/changeShopCodeLabel.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 自动校验任务 - 删除
export const delAutomaticCheckTask = (data) => {
  return request({
    url: '/webapi/report/automaticCheckTask/delAutomaticCheckTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 自动校验任务 - 获取仓库信息(商家维度)
export const getDepotByDataSource = (data) => {
  return request({
    url: '/webapi/report/automaticCheckTask/getDepotByDataSource.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 自动校验任务 - 获取分页数据
export const listAutomaticCheckTask = (data) => {
  return request({
    url: '/webapi/report/automaticCheckTask/listAutomaticCheckTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 自动校验任务 - 查看任务详情
export const listTaskDetailsById = (data) => {
  return request({
    url: '/webapi/report/automaticCheckTask/listTaskDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 自动校验任务 - 标识核对结果
export const modifyTaskCheckResult = (data) => {
  return request({
    url: '/webapi/report/automaticCheckTask/modifyCheckResult.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 自动校验任务 - 生成核对数据
export const saveTaskCheckResult = (data) => {
  return request({
    headers: { 'Content-Type': 'multipart/form-data' },
    url: '/webapi/report/automaticCheckTask/saveCheckResult.asyn',
    method: 'POST',
    data
  })
}

// 自动校验任务 - 获取店铺列表
export const getTaskShopList = (data) => {
  return request({
    url: '/webapi/report/automaticCheckTask/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 自动校验任务 - 导出
export const exportAutomaticCheckTask =
  '/webapi/report/automaticCheckTask/downLoad.asyn'
