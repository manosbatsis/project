/**
 * @description 接口档案
 */

import request from '@u/http'
import qs from 'qs'

/**
 * 接口配置管理 列表
 */
export function listApi(data) {
  return request({
    url: '/webapi/system/api/listApi.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 接口配置管理 新增
 */
export function saveApi(data) {
  return request({
    url: '/webapi/system/api/saveApi.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 接口配置管理 删除
 */
export function delApi(data) {
  return request({
    url: '/webapi/system/api/delApi.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 接口配置管理 启用禁用
 */
export function auditApi(data) {
  return request({
    url: '/webapi/system/api/auditApi.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 接口配置管理 详情
 */
export function toDetailsPage(data) {
  return request({
    url: '/webapi/system/api/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 接口配置管理 编辑
 */
export function modifyApi(data) {
  return request({
    url: '/webapi/system/api/modifyApi.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 对外接口配置 列表
 */
export function listApiExternal(data) {
  return request({
    url: '/webapi/system/apiExternal/listApiExternal.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 对外接口配置 新增
 */
export function saveApiExternal(data) {
  return request({
    url: '/webapi/system/apiExternal/saveApiExternal.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 对外接口配置 生成key
 */
export function getAppKeyAndAppSecret(data) {
  return request({
    url: '/webapi/system/apiExternal/getAppKeyAndAppSecret.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 对外接口配置 删除
 */
export function delApiExternal(data) {
  return request({
    url: '/webapi/system/apiExternal/delApiExternal.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 对外接口配置 启用禁用
 */
export function auditApiExternal(data) {
  return request({
    url: '/webapi/system/apiExternal/auditApiExternal.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 对外接口配置 详情
 */
export function etoDetailsPage(data) {
  return request({
    url: '/webapi/system/apiExternal/toDetailsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 对外接口配置 编辑
 */
export function modifyApiExternal(data) {
  return request({
    url: '/webapi/system/apiExternal/modifyApiExternal.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
