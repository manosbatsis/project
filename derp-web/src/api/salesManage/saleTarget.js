import request from '@u/http'
import qs from 'qs'

// 销售管理 - 销售目标 - 销售目标列表
export const listSaleTarget = (data) => {
  return request({
    url: '/webapi/report/saleTarget/listSaleTarget.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售目标 - 销售目标列表导出
export const exportSaleTargetUrl =
  '/webapi/report/saleTarget/exportSaleTarget.asyn'

// 销售管理 - 销售目标 - 销售目标列导入
export const importSaleTargetUrl =
  '/webapi/report/saleTarget/importSaleTarget.asyn'

// 销售管理 - 销售目标 - 删除
export const delSaleTarget = (data) => {
  return request({
    url: '/webapi/report/saleTarget/delSaleTarget.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售目标 - 删除
export const saleTargetGetDetail = (data) => {
  return request({
    url: '/webapi/report/saleTarget/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售目标 - 月度销售额目标
export const listSaleAmountTarget = (data) => {
  return request({
    url: '/webapi/report/saleAmountTarget/listSaleAmountTarget.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售目标 - 月度销售额目标导出
export const exportAmountListUrl =
  '/webapi/report/saleAmountTarget/exportAmountList.asyn'

// 销售管理 - 销售目标 - 获取月度销售额目标导出数量
export const getOrderCount = (data) => {
  return request({
    url: '/webapi/report/saleAmountTarget/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售目标 - 月度销售额目标导入
export const importAmountTargetUrl =
  '/webapi/report/saleAmountTarget/importAmountTarget.asyn'

// 销售管理 - 销售目标 - 月度销售额目标删除
export const delAmountTarget = (data) => {
  return request({
    url: '/webapi/report/saleAmountTarget/delAmountTarget.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
