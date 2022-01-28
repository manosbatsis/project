/**
 * @description 权限管理接口
 */

import request from '@u/http'
import qs from 'qs'

// 获取权限列表
export function getList(data) {
  return request({
    url: '/webapi/system/permission/list.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取权限列表树
export function getTree(data) {
  return request({
    url: '/webapi/system/permission/listTree.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取权限列表树结构数据
export function getTreeAll(data) {
  return request({
    url: '/webapi/system/permission/listTreeAll.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 新增权限
export function add(data) {
  return request({
    url: '/webapi/system/permission/save.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 删除权限
export function del(data) {
  return request({
    url: '/webapi/system/permission/del.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 通过角色ID查询权限信息
export function getRolePermissions(data) {
  return request({
    url: '/webapi/system/permission/searchByRoleId.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 详情
export function getDetail(data) {
  return request({
    url: '/webapi/system/permission/detail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 修改
export function modify(data) {
  return request({
    url: '/webapi/system/permission/modifyPermission.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
