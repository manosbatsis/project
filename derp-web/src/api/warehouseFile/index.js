/**
 * 仓库档案
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 关区配置 Api
 */

// 仓库档案 - 关区配置 - 获取列表分页数据
export async function getCustomsAreaList(data) {
  return request({
    url: '/webapi/system/customsArea/listCustomsArea.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 关区配置 - 删除
export async function delCustomsArea(data) {
  return request({
    url: '/webapi/system/customsArea/delCustoms.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 关区配置 - 新增关区配置信息
export async function addCustomsArea(data) {
  return request({
    url: '/webapi/system/customsArea/saveCustomsArea.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 关区配置 - 导出关区配置信息
export const exportCustomsAreaUrl =
  '/webapi/system/customsArea/exportCustomsArea.asyn'

// 仓库档案 - 关区配置 - 导入关区配置
export const importCustomsAreaUrl =
  '/webapi/system/customsArea/importCustomsArea.asyn'

// 仓库档案 - 仓库列表 - 获取列表分页数据
export async function listDepot(data) {
  return request({
    url: '/webapi/system/depot/listDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 删除
export async function delDepot(data) {
  return request({
    url: '/webapi/system/depot/delDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 禁用/启用
export async function auditDepot(data) {
  return request({
    url: '/webapi/system/depot/auditDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 访问编辑页面
export async function getDepotEdit(data) {
  return request({
    url: '/webapi/system/depot/toEditPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 获取仓库关区数据
export async function getDepotCustomsRel(data) {
  return request({
    url: '/webapi/system/customsArea/getDepotCustomsRel.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 编辑
export async function modifyDepot(data) {
  return request({
    url: '/webapi/system/depot/modifyDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 新增
export async function saveDepot(data) {
  return request({
    url: '/webapi/system/depot/saveDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 变更记录
export async function getChangeListById(data) {
  return request({
    url: '/webapi/system/depot/getListById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 访问详情
export async function getDepotDetail(data) {
  return request({
    url: '/webapi/system/depot/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 仓库档案 - 仓库列表 - 访问新增
export async function getDepotAddPage(data) {
  return request({
    url: '/webapi/system/depot/toAddPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
