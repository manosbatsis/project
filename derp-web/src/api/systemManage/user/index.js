/**
 * @description 用户管理接口
 */

import request from '@u/http'
import qs from 'qs'

// 获取用户列表
export function getUserList(data) {
  return request({
    url: '/webapi/system/user/list.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

//  根据公司id获得公司详情
export function getCompanyDetail(data) {
  return request({
    url: '/webapi/system/merchant/getMerchantInfoDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 根据事业部id获得事业部详情
export function getBussDetail(data) {
  return request({
    url: '/webapi/system/businessUnit/detail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 保存用户 新建用户
export function saveUser(data) {
  return request({
    url: '/webapi/system/user/saveUser.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 修改用户
export function editUser(data) {
  return request({
    url: '/webapi/system/user/editUser.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 启用停用用户
export function enableUser(data) {
  return request({
    url: '/webapi/system/user/enableUser.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 访问编辑页面
export function toEditPage(data) {
  return request({
    url: '/webapi/system/user/toEditPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 用户授权角色 ( 确定)操作
export function saveUserBindRole(data) {
  return request({
    url: '/webapi/system/user/saveUserBindRole.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
