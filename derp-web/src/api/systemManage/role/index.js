/**
 * @description 角色管理接口
 */

import request from '@u/http'
import qs from 'qs'

// 获取角色列表
export function getList(data) {
  return request({
    url: '/webapi/system/role/list.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 新增
export function add(data) {
  return request({
    url: '/webapi/system/role/saveRole.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 删除
export function del(data) {
  return request({
    url: '/webapi/system/role/del.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 分配权限
export function assignP(data) {
  return request({
    url: '/webapi/system/role/addPermission.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 查找权限底下有的绑定的用户
export function searchRoleUser(data) {
  return request({
    url: '/webapi/system/role/searchUserInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 权限绑定用户
export function bindUser(data) {
  return request({
    url: '/webapi/system/role/bindUser.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
