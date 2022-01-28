import request from '@u/http'
import qs from 'qs'

// 预申报单列表-获取分页数据
export async function listDeclare(data) {
  return request({
    url: '/webapi/order/declare/listDeclare.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表-选择商品弹窗
export async function getItemPopupListByPage(data) {
  return request({
    url: '/webapi/order/declare/getItemPopupListByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表-批量提交
export async function submitDeclareOrder(data) {
  return request({
    url: '/webapi/order/declare/submitDeclareOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表-批量取消
export async function cancelDeclare(data) {
  return request({
    url: '/webapi/order/declare/cancelDeclare.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表-复制前校验
export async function beforeCopyCheck(data) {
  return request({
    url: '/webapi/order/declare/getDeclareOrderCopyCheck.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表-复制前校验
export async function getDeclareOrderCopyById(data) {
  return request({
    url: '/webapi/order/declare/getDeclareOrderCopyById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表-预申报导出
export const exportDeclareUrl = '/webapi/order/declare/exportDeclare.asyn'

// 预申报单列表-一线清关资料导出
export const exportCustomsDeclareUrl =
  '/webapi/order/declare/exportCustomsDeclare.asyn'

// 预申报单列表-根据ID查找详情
export async function getDeclareDetail(data) {
  return request({
    url: '/webapi/order/declare/getDeclareDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出地址
export const exportGoodsUrl = '/webapi/order/declare/exportGoods.asyn'

// 下载商品模板
export async function exportGoods(data) {
  return request({
    url: '/webapi/order/declare/exportGoods.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 编辑预申报导入商品
export const importGoodsUrl = '/webapi/order/declare/importGoods.asyn'

// 编辑物流委托书
export const modifyLogisticsAttorney = (data) => {
  return request({
    url: '/webapi/order/declare/modifyLogisticsAttorney.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
