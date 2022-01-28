/**
 * 收款管理
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 预收款单 api
 */

// 收款管理 - 预收款单 - 获取分页数据
export async function listAdvanceBill(data) {
  return request({
    url: '/webapi/order/advanceBill/listAdvanceBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 保存或修改预收账单
// export async function saveModifyAdvanceBill(data) {
//   return request({
//     url: '/webapi/order/advanceBill/saveModifyAdvanceBill.asyn',
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     data
//   })
// }

// 收款管理 - 预收款单 - 审核
export async function auditAdvanceItem(data) {
  return request({
    url: '/webapi/order/advanceBill/auditAdvanceItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 校验是否是同客户+同事业部+同币种
export async function checkData(data) {
  return request({
    url: '/webapi/order/advanceBill/checkData.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 收款管理 - 预收款单 - 根据id批量删除预收账单
export async function deleteAdvanceItem(data) {
  return request({
    url: '/webapi/order/advanceBill/delete.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 根据id查看详情
export async function detailAdvanceItem(data) {
  return request({
    url: '/webapi/order/advanceBill/detail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 查看核销记录
export async function getVerifyItems(data) {
  return request({
    url: '/webapi/order/advanceBill/getVerifyItems.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 获取业务单据
export async function listAddBill(data) {
  return request({
    url: '/webapi/order/advanceBill/listAddBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 获取关联预收单提交NC信息
export async function listAdvanceBillsToNC(data) {
  return request({
    url: '/webapi/order/advanceBill/listAdvanceBillsToNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 保存核销
export async function saveAdvanceBillVerify(data) {
  return request({
    url: '/webapi/order/advanceBill/saveAdvanceBillVerify.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 提交
// export async function submitAdvanceBill(data) {
//   return request({
//     url: '/webapi/order/advanceBill/submitAdvanceBill.asyn',
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     data
//   })
// }

// 收款管理 - 预收款单 - 提交作废申请
export async function submitInvalidBill(data) {
  return request({
    url: '/webapi/order/advanceBill/submitInvalidBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 同步NC
export async function synNC(data) {
  return request({
    url: '/webapi/order/advanceBill/synNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 获取费项数据
export async function settlementConfigListByCustomer(data) {
  return request({
    url: '/webapi/order/settlementConfig/settlementConfigListByModuleType.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 获取费项下拉列表
export async function getSettlementConfigBean(data) {
  return request({
    url: '/webapi/order/settlementConfig/getSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 更新凭证信息
export async function updateVoucher(data) {
  return request({
    url: '/webapi/order/advanceBill/updateVoucher.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 校验预售单是否满足开票条件
export async function advanceBillValidateInfo(data) {
  return request({
    url: '/webapi/order/advanceBill/validateInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 预收账单开票页面
export async function advanceBillToInvoicePage(data) {
  return request({
    url: '/webapi/order/advanceBill/toInvoicePage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 预收款单 - 预收账单开票页面
export async function advanceBillSaveContract(data) {
  return request({
    url: '/webapi/order/advanceBill/saveContract.asyn',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

// 收款管理 - 预收款单 - 保存或修改提交预收账单
export async function saveModifySubmitAdvanceBill(data) {
  return request({
    url: '/webapi/order/advanceBill/saveModifySubmitAdvanceBill.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 收款管理 - 预收款单 - 预收账单导出PDF
export const exportAdvanceBillPdfUrl =
  '/webapi/order/advanceBill/exportAdvanceBillPdf.asyn'

// 收款管理 - 预收款单 - 预收账单导出
export const exportAdvanceBillUrl =
  '/webapi/order/advanceBill/exportAdvanceBill.asyn'

// to B 收款跟踪 列表分页
export async function listToBTempBillTrack(data) {
  return request({
    url: '/webapi/order/toBTempBillTrack/listToBTempBillTrack.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 删除
export async function batchDelReceiveBill(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/batchDelReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 收款跟踪 导出
export const exportToBTempTrackUrl =
  '/webapi/order/toBTempBillTrack/exportToBTempTrack.asyn'

// to B 核销表 分页
export async function listToBTempBillVerify(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/listToBTempBillVerify.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 核销表 刷新To B暂估核销单
export async function flashTobTemporaryReceiveBill(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/flashTobTemporaryReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 核销表 根据To B暂估核销ID获取详情
export async function getDetailsById(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 核销表 根据To B暂估核销ID分页获取核销明细
export async function toBTempBillVerifyList(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/listToBTempItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 核销表 据To B暂估核销ID分页获取核销返利明细
export async function toBTempBillVerifyRebateItemList(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/listToBTempRebateItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 核销表 获取To B暂估明细导出数量
export async function getTempDetailsCount(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/getTempDetailsCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// to B 核销表导出To B暂估明细
export const exportToBTempDetailsUrl =
  '/webapi/order/toBTempBillVerify/exportToBTempDetails.asyn'

// to B 核销表导出To B暂估明细
export const exportToBTempRebateDetailsUrl =
  '/webapi/order/toBTempBillVerify/exportToBTempRebateDetails.asyn'

// to B 核销表 刷新
export async function refreshTempBill(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/refreshTempBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
/**
 * 平台结算单 api
 */

// 收款管理 - 平台结算单 - 获取分页数据
export async function listPlatformStatementOrder(data) {
  return request({
    url: '/webapi/order/platformStatementOrder/listPlatformStatementOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台结算单 - 导出
export async function exportOrdersUrl(data) {
  return request({
    url: '/webapi/order/platformStatementOrder/exportOrders.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台结算单 - 刷新
export async function flashPlatformStatementOrders(data) {
  return request({
    url: '/webapi/order/platformStatementOrder/flashPlatformStatementOrders.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台结算单 - 创建应收
export async function saveToCReceiveBill(data) {
  return request({
    url: '/webapi/order/platformStatementOrder/saveToCReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台结算单 - 获取详情
export async function platformStatementGetDetailsById(data) {
  return request({
    url: '/webapi/order/platformStatementOrder/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台结算单 - 根据平台结算单ID分页获取表体
export async function listPlatformStatementItem(data) {
  return request({
    url: '/webapi/order/platformStatementOrder/listPlatformStatementItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台费用单 - 列表
export async function listPlatformCostOrder(data) {
  return request({
    url: '/webapi/order/platformCostOrder/listPlatformCostOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台费用单 - 详情
export async function platformCostGetDetailsById(data) {
  return request({
    url: '/webapi/order/platformCostOrder/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 平台费用单 - 根据平台费用单ID分页获取表体
export async function listPlatformCostOrderItem(data) {
  return request({
    url: '/webapi/order/platformCostOrder/listPlatformCostOrderItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 分页
export async function listTocReceiveBill(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/listTocReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 详情
export async function toCReceiveBillDetail(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/detail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 获取toc应收明细分页数据
export async function listReceiveItem(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/listReceiveItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 获取toc费用明细分页数据
export async function listReceiveCostItem(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/listReceiveCostItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 导出
export async function exportBillUrl(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/exportBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 导出结算单汇总
export async function exportSummaryBillUrl(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/exportSummaryBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 删除
export async function delToCReceiveBill(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/delToCReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 作废
export async function saveInvalidBill(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/saveInvalidBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 导出PDF
export const exportPDFUlr = '/webapi/order/toCReceiveBill/exportPDF.asyn'

// To C应收账单 获取店铺
export async function getShopInfo(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/getShopInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 预新增应收账单
export function saveAdvanceTocSettlementBill(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/saveAdvanceTocSettlementBill.asyn',
    method: 'GET',
    headers: {
      'Content-Type': 'application/text'
    },
    data: {
      unused: 0,
      ...data
    }
  })
}

// To C应收账单 新增Toc应收账单
export async function saveTocSettlementBill(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/saveTocSettlementBill.asyn',
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

// To C应收账单 同步Nc列表
export async function listReceiveBillsToNC(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/listReceiveBillsToNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 同步Nc
export async function toCsynNC(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/synNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 获取核销记录
export async function toCverify(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/getVerifyItems.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 保存核销记录
export async function tocSaveReceiveBillVerify(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/saveReceiveBillVerify.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// To C应收账单 校验toc单是否满足开票条件
export async function validateInfo(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/validateInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出失败原因
export const downLoadErrorUrl = '/webapi/order/toCReceiveBill/downLoad.asyn'

// 应收账龄报告 分页列表
export async function listReceiveAgingReport(data) {
  return request({
    url: '/webapi/order/receiveAgingReport/listReceiveAgingReport.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收账龄报告 导出
export const exportReceiveAgingReportUrl =
  '/webapi/order/receiveAgingReport/exportReceiveAgingReport.asyn'

// 应收账龄报告 刷新
export async function receiveAgingExportMonthlyReport(data) {
  return request({
    url: '/webapi/order/receiveAgingReport/exportMonthlyReport.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收账龄报告 刷新
export async function refreshReceiveAgingReport(data) {
  return request({
    url: '/webapi/order/receiveAgingReport/refreshReceiveAgingReport.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收账龄报告 根据id查看详情
export async function getReceiveAgingReportDetail(data) {
  return request({
    url: '/webapi/order/receiveAgingReport/getDetailsById.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 保存补扣款信息
export async function saveToCReceiveBillCost(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/saveToCReceiveBillCost.asyn',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

// 完结核销
export async function endTobTemporaryReceiveBill(data) {
  return request({
    url: '/webapi/order/toBTempBillVerify/endTobTemporaryReceiveBill.asyn',
    method: 'POST',
    // headers: {
    //   'Content-Type': 'application/json'
    // },
    data: qs.stringify(data)
  })
}

export async function getMaxRefreshDate(data) {
  return request({
    url: '/webapi/order/receiveAgingReport/getMaxRefreshDate.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC 应收账单 提交
export async function submitReceiveBill(params) {
  return request({
    url: '/webapi/order/toCReceiveBill/submitReceiveBill.asyn',
    method: 'GET',
    params
  })
}

// ToC 应收账单 审核
export async function auditItem(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/auditItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC 应收账单 发票预览
export const previewUrl = '/webapi/order/receiveBillInvoice/preview/'

// ToC 应收账单 发票详情
export async function toInvoicePageToC(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/toInvoicePage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收账单 保存发票
export async function saveContractToC(data) {
  return request({
    url: '/webapi/order/toCReceiveBill/saveSubmitInvoice.asyn',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

/**
 * 应收账单
 */

// 应收账单 发票详情
export async function toInvoicePage(data) {
  return request({
    url: '/webapi/order/receiveBill/toInvoicePage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 平台结算单 开票
export async function platformToInvoicePage(data) {
  return request({
    url: '/webapi/order/platformStatementOrder/toInvoicePage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收账单 生成发票
export async function saveContract(data) {
  return request({
    url: '/webapi/order/receiveBill/saveContract.asyn',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

// 应收账单 列表
export async function listReceiveBill(data) {
  return request({
    url: '/webapi/order/receiveBill/listReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收账单 刷新
export async function refreshReceiveBill(data) {
  return request({
    url: '/webapi/order/receiveBill/refreshReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 - 获取业务单据
export async function listAddOrder(data) {
  return request({
    url: '/webapi/order/receiveBill/listAddOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 - 保存新增
export async function saveReceiveBill(data) {
  return request({
    url: '/webapi/order/receiveBill/saveReceiveBill.asyn',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

// 收款管理 - 应收账单 - 删除
export async function delReceiveBill(data) {
  return request({
    url: '/webapi/order/receiveBill/delReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 根据应收账单号查询应收账单是否生成
export async function isBillSave(data) {
  return request({
    url: '/webapi/order/receiveBill/isBillSave.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 作废申请
export async function receiveBillSaveInvalidBill(data) {
  return request({
    url: '/webapi/order/receiveBill/saveInvalidBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 开票校验
export async function receiveBillvalidateInfo(data) {
  return request({
    url: '/webapi/order/receiveBill/validateInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 获取模板列表
export async function listFileTempInfo(data) {
  return request({
    url: '/webapi/order/fileTemp/listFileTempInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

//  收款管理 - 应收账单 导出pdf
export const exportPdfUrl = '/webapi/order/receiveBillInvoice/download.asyn'

//  收款管理 - 应收账单 导出excel
export const exportExcelUrl =
  '/webapi/order/receiveBill/exportReceiveBills.asyn'

// 收款管理 - 应收账单 更新凭证信息
export async function receiveBillUpdateVoucher(data) {
  return request({
    url: '/webapi/order/receiveBill/updateVoucher.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 查看发票 /webapi/order/receiveBillInvoice/preview/{fileName}
export const viewInvoiceUrl = '/webapi/order/receiveBillInvoice/preview/'

// 收款管理 - 应收账单 获取核销明细
export async function receiveBillGetVerifyItems(data) {
  return request({
    url: '/webapi/order/receiveBill/getVerifyItems.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款管理 - 应收账单 保存核销信息
export async function saveReceiveBillVerify(data) {
  return request({
    url: '/webapi/order/receiveBill/saveReceiveBillVerify.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 应收发票库 - 获取关联应收单提交NC信息
export const ToBLstReceiveBillsToNC = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/listReceiveBillsToNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 同步NC
export const ToBsynNC = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/synNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 创建应收页面明细导入
export const importAddReceiveItemsUrl =
  '/webapi/order/receiveBill/importAddReceiveItems.asyn'

// 获取费项下拉框
export function getSettlemenList(data) {
  return request({
    url: '/webapi/order/settlementConfig/getSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 根据适用模块获取费项配置列表分页
export function getSettlemenConfigList(data) {
  return request({
    url: '/webapi/order/settlementConfig/settlementConfigListByModuleType.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 根据标准条码获取标准品牌
export function getBrandParent(data) {
  return request({
    url: '/webapi/system/brandParent/getBrandParent.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 创建应收页面明细导入
export function saveAddReceiveBill(data) {
  return request({
    url: '/webapi/order/receiveBill/saveAddReceiveBill.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 费用明细导出
export const exportReceiveCostItemsUrl =
  '/webapi/order/receiveBill/exportReceiveCostItems.asyn'

// 获取应收账单明细
export function getDetail(data) {
  return request({
    url: '/webapi/order/receiveBill/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取勾稽预收单分页数据
export function listBeVerifyAdvanceBill(data) {
  return request({
    url: '/webapi/order/receiveBill/listBeVerifyAdvanceBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收明细导入
export const importReceiveItemsUrl =
  '/webapi/order/receiveBill/importReceiveItems.asyn'

// 应收明细导出
export const exportReceiveItemsUrl =
  '/webapi/order/receiveBill/exportReceiveItems.asyn'

// 应收账单提交
export function ToBsubmitReceiveBill(data) {
  return request({
    url: '/webapi/order/receiveBill/submitReceiveBill.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 应收账单  审核
export function ToBauditItem(data) {
  return request({
    url: '/webapi/order/receiveBill/auditItem.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 应收账单  编辑页面保存
export function saveReceiveBillFromEdit(data) {
  return request({
    url: '/webapi/order/receiveBill/saveReceiveBillFromEdit.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 应收账单  保存补扣款信息
export function saveReceiveBillCost(data) {
  return request({
    url: '/webapi/order/receiveBill/saveReceiveBillCost.asyn',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 应收账单 编辑页面——选择单据确认
export function confirmOrder(data) {
  return request({
    url: '/webapi/order/receiveBill/confirmOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 列表
export function listReceiveBillVerification(data) {
  return request({
    url: '/webapi/order/receiveBillVerification/listReceiveBillVerification.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 导出
export const receiveBillVerificationExportUrl =
  '/webapi/order/receiveBillVerification/export.asyn'

// 收款核销跟踪 刷新
export function receiveBillVerificationFlash(data) {
  return request({
    url: '/webapi/order/receiveBillVerification/flash.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 保存备注
export function saveNotes(data) {
  return request({
    url: '/webapi/order/receiveBillVerification/saveNotes.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 查询回款备注
export function toNotesPage(data) {
  return request({
    url: '/webapi/order/receiveBillVerification/toNotesPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收关账
export * from './closeAccount'
// 月结快照
export * from './billMonthlySnapshot'
// toc月结快照
export * from './tocTempReceiveBillMonthly'
// tob月结快照
export * from './tobMonthlySnapshot'
