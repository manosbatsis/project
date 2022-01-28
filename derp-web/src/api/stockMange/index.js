/**
 * 库存管理
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 批次库存明细列表 api
 */

// 库存管理 - 批次库存明细 - 列表
export const listInventoryBatch = (data) => {
  return request({
    url: '/webapi/inventory/inventoryBatch/listInventoryBatch.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 批次库存明细 - 导出
export const exportInventoryBatch =
  '/webapi/inventory/inventoryBatch/exportInventoryBatch.asyn'

/**
 * 商品库存明细 api
 */

// 库存管理 - 商品库存明细 - 列表
export const listProductInventoryDetails = (data) => {
  return request({
    url: '/webapi/inventory/productInventoryDetails/listProductInventoryDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 商品库存明细 - 导出
export const exportProductInventoryDetails =
  '/webapi/inventory/productInventoryDetails/exportProductInventoryDetails.asyn'

/**
 * 商品收发明细 api
 */

// 库存管理 - 商品收发明细 - 列表
export const listInventoryDetails = (data) => {
  return request({
    url: '/webapi/inventory/inventoryDetails/listInventoryDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 商品收发明细 - 导出
export const exportInventoryDetails =
  '/webapi/inventory/inventoryDetails/exportInventoryDetails.asyn'

/**
 * 效期预警 api
 */

// 库存管理 - 效期预警 - 列表
export const listInventoryWarning = (data) => {
  return request({
    url: '/webapi/inventory/inventoryWarning/listInventoryWarning.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 效期预警 - 导出
export const exportInventoryWarning =
  '/webapi/inventory/inventoryWarning/exportInventoryWarning.asyn'

/**
 * 实时库存 api
 */

// 库存管理 - 实时库存 - 列表
export const searchInventoryRealTime = (data) => {
  return request({
    url: '/webapi/inventory/inventoryRealTime/searchInventoryRealTime.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 实时库存 - 导出
export const exportInventoryRealTime =
  '/webapi/inventory/inventoryRealTime/exportInventoryRealTime.asyn'

/**
 * 月库存转结 api
 */

// 库存管理 - 月库存转结 - 列表
export const listMonthlyAccount = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccount/listMonthlyAccount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月库存转结 - 新增
export const saveMonthlyAccount = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccount/saveMonthlyAccount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月库存转结 - 刷新
export const refreshMonthlyBill = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccount/refreshMonthlyBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月库存转结 - 按库存量结转校验
export const checkMonthlySurplusNum = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccountItem/checkMonthlySurplusNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月库存转结 - 按库存量结转
export const confirmMonthlySurplusNum = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccountItem/confirmMonthlySurplusNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月库存转结 - 修改为未结转
export const updateNotSettlement = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccount/updateNotSettlement.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月库存转结 - 详情
export const toMonthlyDetailPage = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccountItem/toDetailPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月库存转结 - 导出
export const exportMonthlyAccountMap =
  '/webapi/inventory/monthlyAccount/exportMonthlyAccountMap.asyn'

/**
 * 商品收发汇总 api
 */

// 库存管理 - 商品收发汇总 - 列表
export const listInventorySummary = (data) => {
  return request({
    url: '/webapi/inventory/inventorySummary/listInventorySummary.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 商品收发汇总 - 导出
export const exportInventorySummary =
  '/webapi/inventory/inventorySummary/exportInventorySummary.asyn'

/**
 * 批次库存快照 api
 */

// 库存管理 - 批次库存快照 - 列表
export const listInventoryBatchSnapshot = (data) => {
  return request({
    url: '/webapi/inventory/inventoryBatchSnapshot/listInventoryBatchSnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 批次库存快照 - 导出
export const exportInventoryBatchSnapshotModelMap =
  '/webapi/inventory/inventoryBatchSnapshot/exportInventoryBatchSnapshotModelMap.asyn'

/**
 * 库存期初 api
 */

// 库存管理 - 库存期初 - 列表
export const listInitInventory = (data) => {
  return request({
    url: '/webapi/inventory/initInventory/listInitInventory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 库存期初 - 保存
export const saveInitInventory = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/inventory/initInventory/saveInitInventory.asyn',
    method: 'POST',
    data
  })
}

// 库存管理 - 库存期初 - 导入
export const importInitInventory =
  '/webapi/inventory/initInventory/importInitInventory.asyn'

/**
 * 库存商品快照列表 api
 */

// 库存管理 - 库存商品快照列表 - 列表
export const listInventoryGoodsSnapshot = (data) => {
  return request({
    url: '/webapi/inventory/inventoryGoodsSnapshot/listInventoryGoodsSnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 库存商品快照列表 - 导入
export const exportInventoryGoodsSnapshotModelMap =
  '/webapi/inventory/inventoryGoodsSnapshot/exportInventoryGoodsSnapshotModelMap.asyn'

/**
 * 月结快照列表 api
 */

// 库存管理 - 月结快照列表 - 列表
export const listMonthlyAccountSnapshot = (data) => {
  return request({
    url: '/webapi/inventory/monthlyAccountSnapshot/listMonthlyAccountSnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 月结快照列表 - 导入
export const exportMonthlyAccountSnapshotModelMap =
  '/webapi/inventory/monthlyAccountSnapshot/exportMonthlyAccountSnapshotModelMap.asyn'

/**
 * 实时库存快照 api
 */

// 库存管理 - 实时库存 - 列表
export const listInventoryRealTimeSnapshot = (data) => {
  return request({
    url: '/webapi/inventory/inventoryRealTimeSnapshot/listInventoryRealTimeSnapshot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 实时库存 - 导出
export const exportInventoryRealTimeSnapshot =
  '/webapi/inventory/inventoryRealTimeSnapshot/exportInventoryBatchSnapshotModelMap.asyn'

/**
 * 事业部库存 api
 */

// 库存管理 - 事业部库存 - 列表
export const listBuInventory = (data) => {
  return request({
    url: '/webapi/inventory/buInventory/listBuInventory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 事业部库存 - 展开
export const getBuInventoryItem = (data) => {
  return request({
    url: '/webapi/inventory/buInventory/getBuInventoryItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 事业部库存 - 刷新
export const flashBuInventory = (data) => {
  return request({
    url: '/webapi/inventory/buInventory/flashBuInventory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 事业部库存 - 访问列表页面
export const toBuInventoryPage = (data) => {
  return request({
    url: '/webapi/inventory/buInventory/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 库存管理 - 事业部库存 - 导出
export const exportBuInventory =
  '/webapi/inventory/buInventory/exportBuInventory.asyn'
