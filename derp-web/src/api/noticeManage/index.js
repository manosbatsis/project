/**
 * 公告管理
 */
import request from '@u/http'
import qs from 'qs'

// 公告管理 - 公告列表 - 获取列表分页数据
export const listNotice = (data) => {
  return request({
    url: '/webapi/system/notice/listNotice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公告管理 - 公告列表 - 获取详情
export const detailNotice = (data) => {
  return request({
    url: '/webapi/system/notice/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公告管理 - 公告列表 - 访问编辑页面
export const editPageNotice = (data) => {
  return request({
    url: '/webapi/system/notice/toEditPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公告管理 - 公告列表 - 新增、编辑
export const saveOrModify = (data) => {
  return request({
    url: '/webapi/system/notice/saveOrModify.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公告管理 - 公告列表 - 修改状态 发布，删除
export const modifyStatus = (data) => {
  return request({
    url: '/webapi/system/notice/modifyStatus.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公告管理 - 公告列表 - 获取用户登录公告
export const getUserNotice = (data) => {
  return request({
    url: '/webapi/system/notice/getNoticeByUser.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公告管理 - 公告列表 - 获取上一条，下一条公告
export const getAroundNotice = (data) => {
  return request({
    url: '/webapi/system/notice/getAroundNotice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 公告管理 - 公告列表 - 修改公告为已读状态
export const changeReadStatus = (data) => {
  return request({
    url: '/webapi/system/notice/changeReadStatus.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
