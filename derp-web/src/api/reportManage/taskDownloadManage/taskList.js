import request from '@u/http'
import qs from 'qs'

/**
 * 报表任务列表 api
 */

// 报表任务列表 - 列表
export const listFileTask = (data) => {
  return request({
    url: '/webapi/report/fileTask/listFileTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 报表任务列表 - 导出
export const downLoadFileTask = '/webapi/report/fileTask/downLoadFileTask.asyn'
