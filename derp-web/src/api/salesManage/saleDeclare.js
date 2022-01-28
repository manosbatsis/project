/**
 * 销售预申报单列表 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 销售预申报单列表 - 获取列表信息
export const saleDeclareList = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/listDTOByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 删除
export const delSaleDeclareOrder = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/delSaleDeclareOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 获取导出数量
export const getDeclareCount = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 推送出库指令
export const modifyPushOutOrder = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/modifyPushOutOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 保存
export const saveSaleDeclareOrder = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/saveSaleDeclareOrder.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 销售管理 - 销售预申报单列表 - 打托出库
export const saveSaleDeclareOut = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/saveSaleDeclareOut.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 销售管理 - 销售预申报单列表 - 访问销售单生产预申报单/编辑/详情/打托出库页面
export const searchDetail = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/searchDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 打托/确认清关资料/确认申报时间
export const updateTimeTrace = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/updateTimeTrace.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 获取列表销售预申报单各类型数量
export const getDeclareTypeNum = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/getDeclareTypeNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 选择商品弹窗
export const getSalePopupList = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/getSalePopupList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 编辑物流委托书
export const modifyLogisticsAttorney = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/modifyLogisticsAttorney.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 复制销售预申报单
export const copySaleDeclare = (data) => {
  return request({
    url: '/webapi/order/saleDeclare/copySaleDeclare.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售预申报单列表 - 一线清关资料导出
export const exportCustomsDeclareUrl =
  '/webapi/order/saleDeclare/exportCustomsDeclare.asyn'

// 销售管理 - 销售预申报单列表 - 预约物流
export const exportLogisticsDelegationUrl =
  '/webapi/order/saleDeclare/exportLogisticsDelegation.asyn'

// 销售管理 - 销售预申报单列表 - 导出
export const exportDeclareUrl = '/webapi/order/saleDeclare/exportOrder.asyn'

// 销售管理 - 销售预申报单列表 - 导出打托资料
export const exportPallteizeDataUrl =
  '/webapi/order/saleDeclare/exportPallteizeData.asyn'
