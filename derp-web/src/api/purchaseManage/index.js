import request from '@u/http'
import qs from 'qs'

// 理货单列表 获取分页数据
export async function listTallyingOrder(data) {
  return request({
    url: '/webapi/order/tallying/listTallyingOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 理货单列表 修改理货单状态（确认/驳回）
export async function modifyOrderState(data) {
  return request({
    url: '/webapi/order/tallying/modifyOrderState.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 理货单列表-导出理货单
export const exportTallyingOrderUrl =
  '/webapi/order/tallying/exportTallyingOrder.asyn'

// 理货单列表 理货单详情
export async function getTallyingDetailById(data) {
  return request({
    url: '/webapi/order/tallying/getDetailById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取采购退货单列表
export async function listPurchaseReturnOrder(data) {
  return request({
    url: '/webapi/order/purchaseReturn/listPurchaseReturnOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 删除采购退货订单
export async function delPurchaseReturnOrder(data) {
  return request({
    url: '/webapi/order/purchaseReturn/delPurchaseReturnOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出采购退货订单
export const exportPurchaseReturnUrl =
  '/webapi/order/purchaseReturn/exportPurchaseReturn.asyn'

// 根据采购退货ID获取详情
export async function getReturnOrderDetail(data) {
  return request({
    url: '/webapi/order/purchaseReturn/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 打托出库保存
export async function purchaseReturnDelivery(data) {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/purchaseReturn/purchaseReturnDelivery.asyn',
    method: 'POST',
    data
  })
}

// 采购退货出库订单列表
export async function listPurchaseReturnOdepot(data) {
  return request({
    url: '/webapi/order/purchaseReturnOdepot/listPurchaseReturnOdepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购退货出库订单列表 根据ID查询详情
export async function getpurchaseReturnOdepotDetail(data) {
  return request({
    url: '/webapi/order/purchaseReturnOdepot/getDetailsPById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购退货出库订单列表 导出采购退货出库订单
export const exportPurchaseReturnOdepotUrl =
  '/webapi/order/purchaseReturnOdepot/exportPurchaseReturnOdepot.asyn'

// 采购退货出库订单列表 导入采购退货出库订单
export const importOrderUrl =
  '/webapi/order/purchaseReturnOdepot/importOrder.asyn'

// 采购退货出库订单列表 审核采购退货出库订单
export async function auditPurchaseReturnOrder(data) {
  return request({
    url: '/webapi/order/purchaseReturnOdepot/auditPurchaseReturnOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购退货出库订单列表 采购退货新增弹框查询
export async function getListPurchaseOrderByPage(data) {
  return request({
    url: '/webapi/order/purchase/getListPurchaseOrderByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购退货出库订单列表 检验订单参数是否相同
export async function isSameParameter(data) {
  return request({
    url: '/webapi/order/purchaseReturn/isSameParameter.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 访问新增页面
export async function toAddPage(data) {
  return request({
    url: '/webapi/order/purchaseReturn/toAddPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购入库单列表 获取分页数据
export async function listPurchaseWarehouse(data) {
  return request({
    url: '/webapi/order/warehouse/listPurchaseWarehouse.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购入库单列表 删除入库单
export async function delBatchByIds(data) {
  return request({
    url: '/webapi/order/warehouse/delBatchByIds.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购入库单列表 入库单明细导出
export const exportRelationUrl = '/webapi/order/warehouse/exportRelation.asyn'

// 采购入库单列表 入库单勾稽明细导出
export const purchaseExportRelationUrl =
  '/webapi/order/warehouse/purchaseExportRelation.asyn'

// 采购入库单列表 删除入库单
export async function confirmDepot(data) {
  return request({
    url: '/webapi/order/warehouse/confirmDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购入库单列表 根据ID查找详情
export async function getWarehouseDetail(data) {
  return request({
    url: '/webapi/order/warehouse/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购入库单列表 入库单导入
export const importWarehouseUrl = '/webapi/order/warehouse/importWarehouse.asyn'

// 采购入库单列表 关联采购单导入
export const importRelationUrl = '/webapi/order/warehouse/importRelation.asyn'

// 采购入库单列表 入库单勾稽校验仓库是否为在途仓
export async function checkWarehouseDepotType(data) {
  return request({
    url: '/webapi/order/warehouse/checkWarehouseDepotType.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

export async function checkDepotMerchantRel(data) {
  return request({
    url: '/webapi/system/depot/checkDepotMerchantRel.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购退货订单 采购退货非必填校验保存
export async function savePurchaseReturnOrder(data) {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/purchaseReturn/savePurchaseReturnOrder.asyn',
    method: 'POST',
    data
  })
}

// 采购退货订单 采购退货必填校验保存
export async function saveRequirePurchaseReturnOrder(data) {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/purchaseReturn/saveRequirePurchaseReturnOrder.asyn',
    method: 'POST',
    data
  })
}

// 预申报单列表 预申报单修改
export async function modifyDeclare(data) {
  return request({
    url: '/webapi/order/declare/modifyDeclare.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 预申报单列表 获取一线清关资料
export async function getFirstCustomsDeclareInfo(data) {
  return request({
    url: '/webapi/order/declare/getFirstCustomsDeclareInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表 编辑清关资料
export async function modifyCustomsDeclare(data) {
  return request({
    url: '/webapi/order/declare/modifyCustomsDeclare.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表 获取导出清关模板
export async function getExportTemplate(data) {
  return request({
    url: '/webapi/order/customsFileConfig/getExportTemplate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购SD列表 分页
export async function purchaseSdOrderList(data) {
  return request({
    url: '/webapi/order/purchaseSdOrder/purchaseSdOrderList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购SD列表 导出
export const exportOrderUrl = '/webapi/order/purchaseSdOrder/exportOrder.asyn'

// 采购SD列表 删除SD单
export async function delSdOrder(data) {
  return request({
    url: '/webapi/order/purchaseSdOrder/delSdOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购SD列表 导入
export const purchaseSdOrderImportOrderUrl =
  '/webapi/order/purchaseSdOrder/importOrder.asyn'

// 采购SD列表 根据ID获取金额调整明细
export async function purchaseSdOrderAmountAdjustmentDetail(data) {
  return request({
    url: '/webapi/order/purchaseSdOrder/getAmountAdjustmentDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购SD列表 取详情
export async function purchaseSdOrderDetailById(data) {
  return request({
    url: '/webapi/order/purchaseSdOrder/getDetailById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购SD列表 取详情
export async function purchaseSdOrderSaveAmountAdjustment(data) {
  return request({
    url: '/webapi/order/purchaseSdOrder/saveAmountAdjustment.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 获取物流联系人列表
export async function listTemplate(data) {
  return request({
    url: '/webapi/order/common/listTemplate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 新增或编辑物流联系人
export async function saveOrUpdateLogisTemplate(data) {
  return request({
    url: '/webapi/order/common/saveOrUpdateLogisTemplate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 删除物流联系人
export async function delTemplate(data) {
  return request({
    url: '/webapi/order/common/delTemplate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 新增或编辑物流联系常用模版
export async function saveOrUpdateLogisTemplateLink(data) {
  return request({
    url: '/webapi/order/common/saveOrUpdateLogisTemplateLink.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 获取物流联系常用模版列表
export async function listTemplateLink(data) {
  return request({
    url: '/webapi/order/common/listTemplateLink.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 物流联系常用模版 删除
export async function delTemplateLink(data) {
  return request({
    url: '/webapi/order/common/delTemplateLink.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 更新轨迹时间
export async function updateTrajectory(data) {
  return request({
    url: '/webapi/order/declare/updateTrajectory.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单 更新轨迹时间
export const exportLogisticsDelegationUrl =
  '/webapi/order/declare/exportLogisticsDelegation.asyn'

// 预申报单 获取预申报单各类型数量
export async function getDeclareTypeNum(data) {
  return request({
    url: '/webapi/order/declare/getDeclareTypeNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 预申报单列表 预申报单修改
export async function declareDelivery(data) {
  return request({
    url: '/webapi/order/declare/declareDelivery.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 生成预申报单前检查
export async function generateDeclareOrderCheck(data) {
  return request({
    url: '/webapi/order/purchase/generateDeclareOrderCheck.asyn',
    method: 'POST',
    data: qs.stringify(data)
    // headers: { 'Content-Type': 'application/json' },
    // data
  })
}

// 根据采购订单ID获取发票信息
export async function getInvocieByPurchaseId(data) {
  return request({
    url: '/webapi/order/purchaseInvoice/getInvocieByPurchaseId.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 保存发票
export async function saveInvoice(data) {
  return request({
    url: '/webapi/order/purchaseInvoice/saveInvoice.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 反审
export function auditCounterTrial(data) {
  return request({
    url: '/webapi/order/purchase/auditCounterTrial.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 根据事业部+供应商+币种+商品查询采购单价
export function getPurchasePriceByGoodsId(data) {
  return request({
    url: '/webapi/order/purchase/getPurchasePriceByGoodsId.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 若仓库是海外仓，获取商品的箱托数量
export function getDepotGoodsNum(data) {
  return request({
    url: '/webapi/order/purchase/getDepotGoodsNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 导入
export const importPurchaseGoodsUrl =
  '/webapi/order/purchase/importPurchaseGoods.asyn'

// 采购退选择商品弹窗
export function getPurchaseItemPopupByPage(data) {
  return request({
    url: '/webapi/order/purchaseReturn/getPurchaseItemPopupByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

export * from './purchaseOrder' // 采购订单
export * from './preDeclaration' // 采购预申报单
export * from './invoiceList' // 发票列表
export * from './purchaseFrameContract' // 采购框架合同
export * from './purchaseTrialOrder' // 采购试单申请
