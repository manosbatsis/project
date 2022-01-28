import request from '@u/http'
import qs from 'qs'

// 采购订单列表 - 获取分页数据
export async function listPurchaseOrder(data) {
  return request({
    url: '/webapi/order/purchase/listPurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 申请红冲保存
export async function saveApplyWriteOff(data) {
  return request({
    url: '/webapi/order/purchase/saveApplyWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 审核红冲保存
export async function saveAuditWriteOff(data) {
  return request({
    url: '/webapi/order/purchase/saveAuditWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 申请红冲校验
export async function validateApply(data) {
  return request({
    url: '/webapi/order/purchase/validateApply.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 审核红冲校验
export async function validateAuditWriteOff(data) {
  return request({
    url: '/webapi/order/purchase/validateAuditWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 获取采购订单列表各类型数量
export async function getPurchaseOrderTypeNum(data) {
  return request({
    url: '/webapi/order/purchase/getPurchaseOrderTypeNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 根据事业部和客户获取销售价格
export async function getCustomerBuIdPrice(data) {
  return request({
    url: '/webapi/order/purchase/getCustomerBuIdPrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 采购金额调整模态框获取详情
export async function getAmountAdjustmentDetail(data) {
  return request({
    url: '/webapi/order/purchase/getAmountAdjustmentDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-采购SD生成金额获取列表
export async function getSdAmountAdjustmentDetail(data) {
  return request({
    url: '/webapi/order/purchase/getSdAmountAdjustmentDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 采购金额修改
export async function saveAmountAdjustment(data) {
  return request({
    url: '/webapi/order/purchase/saveAmountAdjustment.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 采购金额确认
export async function saveConfirmAmountAdjustment(data) {
  return request({
    url: '/webapi/order/purchase/saveConfirmAmountAdjustment.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 获取采购订单详情明细
export async function getPurchaseOrderDetails(data) {
  return request({
    url: '/webapi/order/purchase/getPurchaseOrderDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 根据采购订单ID获取合同信息
export async function getContractInfoByPurchaseId(data) {
  return request({
    url: '/webapi/order/purchase/getContractInfoByPurchaseId.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 根据采购订单Id获取采购商品
export async function getPurchaseItem(data) {
  return request({
    url: '/webapi/order/purchase/getPurchaseItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 - 修改录入发票 和付款日期
export async function updatePurchaseOrderInvoice(data) {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/purchase/updatePurchaseOrderInvoice.asyn',
    method: 'POST',
    data
  })
}

// 采购与申报单 -导入箱装明细
export const importPackingDetailsUrl =
  '/webapi/order/declare/importPackingDetails.asyn'

// 采购订单列表 -完结入库校验
export async function endPurchaseOrderCheck(data) {
  return request({
    url: '/webapi/order/purchase/endPurchaseOrderCheck.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -完结入库
export async function endPurchaseOrder(data) {
  return request({
    url: '/webapi/order/purchase/endPurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -导入采购订单
export const importPurchaseUrl = '/webapi/order/purchase/importPurchase.asyn'

// 采购订单列表 -删除采购订单
export async function delPurchaseOrder(data) {
  return request({
    url: '/webapi/order/purchase/delPurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -生成预申报单
export async function generateDeclareOrder(data) {
  return request({
    url: '/webapi/order/purchase/generateDeclareOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -中转仓入库
export async function inWarehouse(data) {
  return request({
    url: '/webapi/order/purchase/inWarehouse.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -导出采购订单
export const exportPurchaseUrl = '/webapi/order/purchase/exportPurchase.asyn'

// 采购订单列表 -打托入库
export async function purchaseDelivery(data) {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/purchase/purchaseDelivery.asyn',
    method: 'POST',
    data
  })
}

// 采购订单列表 -采购转销售
export async function saveSaleOrder(data) {
  return request({
    url: '/webapi/order/purchase/saveSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -检查能否生成代采
export async function createFinancingOrderCheck(data) {
  return request({
    url: '/webapi/order/purchase/createFinancingOrderCheck.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -跳转融资代采页面
export async function toFinancingPage(data) {
  return request({
    url: '/webapi/order/purchase/toFinancingPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -获取卓普信接口，加载下拉框
export async function getSapienceApiInfo(data) {
  return request({
    url: '/webapi/order/purchase/getSapienceApiInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -提交卓普信
export async function submitSapience(data) {
  return request({
    url: '/webapi/order/purchase/submitSapience.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -SD单创建前校验
export async function createSdOrderCheck(data) {
  return request({
    url: '/webapi/order/purchase/createSdOrderCheck.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表 -SD单创建保存
export async function saveSdOrder(data) {
  return request({
    url: '/webapi/order/purchaseSdOrder/saveSdOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-校验链路创建前校验
export async function toSaleStepBeforeCheck(data) {
  return request({
    url: '/webapi/order/purchase/toSaleStepBeforeCheck.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-获取配置链路信息
export async function getTradeLinkList(data) {
  return request({
    url: '/webapi/order/purchase/getTradeLinkList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-跳转采购链路1获取链路信息
export async function toSaleStepOnePage(data) {
  return request({
    url: '/webapi/order/purchase/toSaleStepOnePage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-采购链路第一步保存链路信息
export async function saveOrUpdateLinkStepOne(data) {
  return request({
    url: '/webapi/order/purchase/saveOrUpdateLinkStepOne.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-采购链路跳转步骤2获取信息,生成预览订单
export async function toSaleStepTwoPage(data) {
  return request({
    url: '/webapi/order/purchase/toSaleStepTwoPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-返回采购链路1获取链路信息,
export async function backToSaleStepOnePage(data) {
  return request({
    url: '/webapi/order/purchase/backToSaleStepOnePage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-采购链路保存，修改商品信息,
export async function saveSaleStepGoodsInfo(data) {
  return request({
    url: '/webapi/order/purchase/saveSaleStepGoodsInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-采购链路跳转步骤3
export async function toSaleStepThreePage(data) {
  return request({
    url: '/webapi/order/purchase/toSaleStepThreePage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单列表-采购链路，源头订单审核，并入库
export async function saveFirstOrderStatusAndIdepot(data) {
  return request({
    url: '/webapi/order/purchase/saveFirstOrderStatusAndIdepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购链路，所有链路订单审核，并入库或出库
export async function saveLinkOrderAndDepot(data) {
  return request({
    url: '/webapi/order/purchase/saveLinkOrderAndDepot.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单编辑 获取仓库详情
export async function getDepotInfo(data) {
  return request({
    url: '/webapi/order/purchase/getDepotInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单编辑 查询采购价格管理
export async function getPurchasePriceManage(data) {
  return request({
    url: '/webapi/order/purchase/getPurchasePriceManage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单编辑 保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验
export async function saveOrUpdateTempOrder(data) {
  return request({
    url: '/webapi/order/purchase/saveOrUpdateTempOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单提交前校验合同号和PO号是否被使用
export async function checkContractNoAndPoNo(data) {
  return request({
    url: '/webapi/order/purchase/checkContractNoAndPoNo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 编辑采购订单必填校验保存
export async function modifyPurchaseOrder(data) {
  return request({
    url: '/webapi/order/purchase/modifyPurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 新增采购订单必填校验保存
export async function savePurchaseOrder(data) {
  return request({
    url: '/webapi/order/purchase/savePurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取模板列表
export async function listFileTempInfo(data) {
  return request({
    url: '/webapi/order/fileTemp/listFileTempInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 美赞、宝洁、拜耳采购合同编辑提交
export async function modifyJsonPurchaseContract(data) {
  return request({
    url: '/webapi/order/purchase/modifyJsonPurchaseContract.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 美赞、宝洁、拜耳采购合同编辑提交并提交采购订单
export async function submitJSONPurchaseContract(data) {
  return request({
    url: '/webapi/order/purchase/submitJSONPurchaseContract.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 获取归属时间
export async function getAttributionDate(data) {
  return request({
    url: '/webapi/order/purchase/getAttributionDate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 更新归属时间
export async function updateAttributionDate(data) {
  return request({
    url: '/webapi/order/purchase/updateAttributionDate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 美赞、宝洁、拜耳采购合同编辑提交并审核采购订单
export async function auditJSONPurchaseContract(data) {
  return request({
    url: '/webapi/order/purchase/auditJSONPurchaseContract.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 检查是否要生成内部供应商对应销售订单
export async function checkInnerMerchantSaleOrder(data) {
  return request({
    url: '/webapi/order/purchase/checkInnerMerchantSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 检查采购订单是否存在交易链路配置
export async function checkTradeLink(data) {
  return request({
    url: '/webapi/order/purchase/checkTradeLink.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 内部公司销售订单
export async function saveInnerMerchantSaleOrders(data) {
  return request({
    url: '/webapi/order/purchase/saveInnerMerchantSaleOrders.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 一般采购合同编辑提交
export async function modifyPurchaseContract(data) {
  return request({
    url: '/webapi/order/purchase/modifyPurchaseContract.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 一般采购合同编辑提交并提交采购订单
export async function submitPurchaseContract(data) {
  return request({
    url: '/webapi/order/purchase/submitPurchaseContract.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 一般采购合同编辑提交并提交采购订单
export async function auditPurchaseContract(data) {
  return request({
    url: '/webapi/order/purchase/auditPurchaseContract.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购合同导出PDF
export const exportPurchaseContractPdfUrl =
  '/webapi/order/purchase/exportPurchaseContractPdf.asyn'

// 采购链路嘉宝跳转步骤2获取信息,生成预览订单
export async function toJBSaleStepTwoPage(data) {
  return request({
    url: '/webapi/order/purchase/toJBSaleStepTwoPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出美赞、宝洁、拜耳合同文件
export const exportTempDateFileUrl =
  '/webapi/order/purchase/exportTempDateFile.asyn'

// 采购 项目额度配置预警 列表
export async function getListByPage(data) {
  return request({
    url: '/webapi/order/projectQuotaWarning/getListByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购 项目额度配置预警 导出
export const exportProjectQuotaWarningUrl =
  '/webapi/order/projectQuotaWarning/exportProjectQuotaWarning.asyn'

// 采购 项目额度配置预警 导出明细
export const exportProjectQuotaWarningDetailUrl =
  '/webapi/order/projectQuotaWarning/exportProjectQuotaWarningDetail.asyn'

// 采购 项目额度配置预警 刷新
export async function flashProjectQuotaWarningById(data) {
  return request({
    url: '/webapi/order/projectQuotaWarning/flashProjectQuotaWarningById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购 项目额度配置预警 详情
export async function getProjectQuotaWarningById(data) {
  return request({
    url: '/webapi/order/projectQuotaWarning/getProjectQuotaWarningById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

//  商品明细列表分页
export async function getItemListByPage(data) {
  return request({
    url: '/webapi/order/projectQuotaWarning/getItemListByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 根据多个采购订单构造生成销售订单详情
export async function genSaleOrderByPurchaseIds(data) {
  return request({
    url: '/webapi/order/purchase/genSaleOrderByPurchaseIds.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 批量驳回
export async function purchaseBatchRejections(data) {
  return request({
    url: '/webapi/order/purchase/batchRejection.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 - 进入OA审批页面
export const getOAAuditPage = (data) => {
  return request({
    url: '/webapi/order/purchase/getOAAuditPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
// 采购订单 - 发起OA审批
export const callOAAudit = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/purchase/callOAAudit.asyn',
    method: 'POST',
    data
  })
}

// 采购订单 - 获取采购试单申请编号列表
export const listOaBillCodeSelect = (data) => {
  return request({
    url: '/webapi/order/purchaseTryApplyOrder/listOaBillCodeSelect.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 - 获取采购框架合同列表
export const listFrameContracSelect = (data) => {
  return request({
    url: '/webapi/order/purchaseFrameContract/listFrameContracSelect.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 - 获取合同附件列表
export const listContract = (data) => {
  return request({
    url: '/webapi/order/purchase/listContract.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 - 获取采购审批方式
export const getPurchaseMethod = (data) => {
  return request({
    url: '/webapi/system/merchantBuRel/getPurchaseAuditMethod.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 - 获取业务员下拉
export const getUserInfoHasCode = (data) => {
  return request({
    url: '/webapi/system/role/getUserInfoHasCode.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 采购订单 - 上传附件
export const uploadFilesWithoutSave =
  '/webapi/order/attachment/uploadFilesWithoutSave.asyn'
