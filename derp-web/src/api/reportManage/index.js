/**
 * 报表管理
 */
import request from '@u/http'
import qs from 'qs'

/**
 * 累计汇总表 api
 */

// 累计汇总表 - 获取分页数据
export async function buFinanceAddSummaryList(data) {
  return request({
    url: '/webapi/report/buFinanceAdd/buFinanceAddSummaryList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 累计汇总表 - 访问列表页面
export async function buFinanceAddSummaryToPage(data) {
  return request({
    url: '/webapi/report/buFinanceAdd/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 累计汇总表 - 累计总表导出
export const exportBuFinanceAddSummaryUrl =
  '/webapi/report/buFinanceAdd/export.asyn'

/**
 * 累计销售汇总表 api
 */

// 累计销售汇总表 - 获取分页数据
export async function buFinanceSaleList(data) {
  return request({
    url: '/webapi/report/buFinanceAddSale/buFinanceAddSaleList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 累计销售汇总表 - 访问列表页面
export async function buFinanceSaleToPage(data) {
  return request({
    url: '/webapi/report/buFinanceAddSale/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 累计销售汇总表 - 累计总表导出
export const exportbuFinanceSaleUrl =
  '/webapi/report/buFinanceAddSale/export.asyn'

/**
 * 累计采购汇总表 api
 */

// 累计采购汇总表 - 获取分页数据
export async function buFinancePurchaseList(data) {
  return request({
    url: '/webapi/report/buFinanceAddWarehouse/buFinanceAddWarehouseList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 累计采购汇总表 - 访问列表页面
export async function buFinancePurchaseToPage(data) {
  return request({
    url: '/webapi/report/buFinanceAddWarehouse/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 累计采购汇总表 - 累计总表导出
export const exportBuFinancePurchaseUrl =
  '/webapi/report/buFinanceAddWarehouse/export.asyn'

/**
 * ToC暂估应收表 api
 */

// ToC暂估应收表 - 暂估收入完结核销
export async function endReceiveBill(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/endReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 统计导出数量
export async function getBillCount(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/getBillCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 暂估收入完结核销列表
export async function listEndReceiveBill(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/listEndReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 获取暂估费用分页数据
export async function listToCTempCostReceiveBill(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/listToCTempCostReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 获取暂估收入分页数据
export async function listToCTempReceiveBill(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/listToCTempReceiveBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 根据账单id获取暂估费用明细
export async function listToCTempReceiveCostItem(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/listToCTempReceiveCostItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 根据账单id获取暂估应收明细
export async function listToCTempReceiveItem(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/listToCTempReceiveItem.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 刷新
export async function refreshBill(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/refreshBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 获取暂估收入详情
export async function tocTempReceiveDetail(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/getDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 获取暂估费用详情
export async function tocTempCostReceiveDetail(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/getCostDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 获取当前商家关联的店铺
export async function getShopInfoList(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 暂估收入导出
// export const exportBillUrl = '/webapi/order/tocTempReceiveBill/exportBill.asyn'

// ToC暂估应收表 - 暂估收入导出
export async function exportBillUrl(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/exportBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 累计暂估应收导出
export async function exportTempBillUrl(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/exportTempBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 暂估收入汇总导出
export async function exportSummaryOrderUrl(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/exportSummaryOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 累计暂估应收导出
// export const exportTempBillUrl = '/webapi/order/tocTempReceiveBill/exportTempBill.asyn'

// ToC暂估应收表 - 暂估费用导出
// export const exportCostBillUrl = '/webapi/order/tocTempReceiveBill/exportCostBill.asyn'

// ToC暂估应收表 - 暂估收入导出
export async function exportCostBillUrl(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/exportCostBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 累计暂估费用导出
export async function exportTempCostBillUrl(data) {
  return request({
    url: '/webapi/order/tocTempReceiveBill/exportTempCostBill.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 累计暂估费用导出
// export const exportTempCostBillUrl = '/webapi/order/tocTempReceiveBill/exportTempCostBill.asyn'

// ToC暂估应收表 - 暂估费用汇总导出
export const exportCostSummaryOrderUrl = (data) => {
  return request({
    url: '/webapi/order/tocTempReceiveBill/exportCostSummaryOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 收款核销跟踪 api
 */

// 收款核销跟踪 - 获取分页数据
export const listReceiveBillVerification = (data) => {
  return request({
    url: '/webapi/order/receiveBillVerification/listReceiveBillVerification.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 - 刷新
export const flashReceiveBillVerification = (data) => {
  return request({
    url: '/webapi/order/receiveBillVerification/flash.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 - 查询回款备注
export const toNotesPage = (data) => {
  return request({
    url: '/webapi/order/receiveBillVerification/toNotesPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 - 保存备注
export const saveNotes = (data) => {
  return request({
    url: '/webapi/order/receiveBillVerification/saveNotes.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 收款核销跟踪 - 获取详情
export const detailReceiveBillVerification = (data) => {
  return request({
    url: '/webapi/order/receiveBillVerification/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// ToC暂估应收表 - 累计暂估应收导出
export const exportReceiveBillVerificationUrl =
  '/webapi/order/receiveBillVerification/export.asyn'

/**
 * 应收发票库 api
 */

// 应收发票库 - 获取分页数据
export const listReceiveBillInvoice = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/listReceiveBillInvoice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 获取发票详情
export const getInvoiceDetail = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/getInvoiceDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 完成发票签章
export const modifyInvoice = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/modifyInvoice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 获取关联应收账单
export const listReceiveBills = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/listReceiveBills.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 校验发票
export const validateSynNC = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/validateSynNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 获取关联应收单提交NC信息
export const listReceiveBillsToNC = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/listReceiveBillsToNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 同步NC
export const synNC = (data) => {
  return request({
    url: '/webapi/order/receiveBillInvoice/synNC.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 应收发票库 - 导出应收对账
export const exportBillInvoiceUrl =
  '/webapi/order/receiveBillInvoice/exportBillInvoice.asyn'

// 应收发票库 - 导出
export const exportInvoiceUrl =
  '/webapi/order/receiveBillInvoice/exportInvoice.asyn'

// 应收发票库 - 导出应收单NC提交信息
export const exportBillNcUrl = '/webapi/order/receiveBillInvoice/download.asyn'

// 应收发票库 - 发票预览
export const previewInvoice = '/webapi/order/receiveBillInvoice/preview/'

// 应收发票库 - 替换发票文件
export const replaceInvoiceUrl =
  '/webapi/order/receiveBillInvoice/replaceInvoice.asyn'

/**
 * 部门销售金额统计报表 api
 */
// 列表
export const getDepartmentSalesAmountStatistic = (data) => {
  return request({
    url: '/webapi/report/management/getDepartmentSalesAmountStatistic.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出部门销售金额统计
export const exportDepartmentSalesAmountStatistic =
  '/webapi/report/management/exportDepartmentSalesAmountStatistic.asyn'

// 获取公司列表
export const getMerchantList = (data) => {
  return request({
    url: '/webapi/report/management/getMerchantList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取公司列表
export const getBuList = (data) => {
  return request({
    url: '/webapi/report/management/getBuList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取部门
export const getDepartmentSelectList = (data) => {
  return request({
    url: '/webapi/report/management/getDepartmentSelectList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

//  获取客户
export const getCustomerSelectList = (data) => {
  return request({
    url: '/webapi/report/management/getCustomerSelectList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

//  获取仓库
export const getDepotSelectList = (data) => {
  return request({
    url: '/webapi/report/management/getDepotSelectList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 部门销售达成统计报表 api
 */
// 列表
export const getDepartmentSalesAchieveStatistic = (data) => {
  return request({
    url: '/webapi/report/management/getDepartmentSalesAchieveStatistic.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出部门销售金额统计
export const exportDepartmentSalesAchieveStatistic =
  '/webapi/report/management/exportDepartmentSalesAchieveStatistic.asyn'

/**
 * 部门销售达成统计报表 api
 */

// 列表
export const getDepartmentInventoryStatistic = (data) => {
  return request({
    url: '/webapi/report/management/getDepartmentInventoryStatistic.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出部门库存统计
export const exportDepartmentInventoryStatistic =
  '/webapi/report/management/exportDepartmentInventoryStatistic.asyn'

/**
 * 部门销售达成统计报表 api
 */

// 列表
export const getDepartmentInventoryCleanStatistic = (data) => {
  return request({
    url: '/webapi/report/management/getDepartmentInventoryCleanStatistic.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出部门库存清空天数统计
export const exportDepartmentInventoryCleanStatistic =
  '/webapi/report/management/exportDepartmentInventoryCleanStatistic.asyn'

/**
 * 销售类型达成
 */
//  销售类型达成 列表
export const listSaleTargetAchievement = (data) => {
  return request({
    url: '/webapi/report/saleTargetAchievement/listSaleTargetAchievement.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出销售类型达成
export const exportAmountList =
  '/webapi/report/saleTargetAchievement/exportAmountList.asyn'

//  销售类型达成 刷新
export const flashSaleTargetAchievement = (data) => {
  return request({
    url: '/webapi/report/saleTargetAchievement/flashSaleTargetAchievement.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

//  销售类型达成 获取存在的店铺
export const saleTargetAchievementtoDpPage = (data) => {
  return request({
    url: '/webapi/report/saleTargetAchievement/toDpPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

//  销售类型达成 获取存在的电商平台
export const saleTargetAchievementtoDsPage = (data) => {
  return request({
    url: '/webapi/report/saleTargetAchievement/toDsPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 购销采销日报
 */

//   购销采销日报 列表
export const listGouXiaoPurchaseDaily = (data) => {
  return request({
    url: '/webapi/report/gouXiaoPurchaseDaily/listGouXiaoPurchaseDaily.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 获取导出数量
export const getOrderCount = (data) => {
  return request({
    url: '/webapi/report/gouXiaoPurchaseDaily/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出购销采销日报
export const exportGouXiaoPurchaseDaily =
  '/webapi/report/gouXiaoPurchaseDaily/exportGouXiaoPurchaseDaily.asyn'

// 购销采销日报 刷新
export const refreshDaily = (data) => {
  return request({
    url: '/webapi/report/gouXiaoPurchaseDaily/refreshDaily.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
// 购销采销日报 获取页面数据
export const PurchaseDailytoPage = (data) => {
  return request({
    url: '/webapi/report/gouXiaoPurchaseDaily/toPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 存货跌价准备报表
 */
// 列表
export const listInventoryFallingPriceReserves = (data) => {
  return request({
    url: '/webapi/report/inventoryFallingPriceReserves/listInventoryFallingPriceReserves.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 导出存货跌价准备报表
export const exportInventoryFallingPriceReserves =
  '/webapi/report/inventoryFallingPriceReserves/exportInventoryFallingPriceReserves.asyn'

// 存货跌价准备报表 刷新
export const flashInventoryFallingPriceReserves = (data) => {
  return request({
    url: '/webapi/report/inventoryFallingPriceReserves/flashInventoryFallingPriceReserves.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
// 获取导出数量
export const getCount = (data) => {
  return request({
    url: '/webapi/report/inventoryFallingPriceReserves/getCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 商品在库天数统计
 */
// 列表
export const listInWarehouseDays = (data) => {
  return request({
    url: '/webapi/report/inWarehouseDays/listInWarehouseDays.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
// 刷新
export const flashInWarehouseDays = (data) => {
  return request({
    url: '/webapi/report/inWarehouseDays/flashInWarehouseDays.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
// 导出
export const exportInWarehouseDetails = (data) => {
  return request({
    url: '/webapi/report/inWarehouseDays/exportInWarehouseDetails.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
// 详情
export const InWarehousetoDetail = (data) => {
  return request({
    url: '/webapi/report/inWarehouseDays/toDetail.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 财务进销存关账 api
 */

// 财务进销存关账 - 获取分页数据
export const listFnanceClose = (data) => {
  return request({
    url: '/webapi/report/financeClose/listFnanceClose.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 财务进销存关账 - 关账/反关账
export const updateNotClose = (data) => {
  return request({
    url: '/webapi/report/financeClose/updateNotClose.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 财务进销存关账 - 关账/反关账
export const updateClose = (data) => {
  return request({
    url: '/webapi/report/financeClose/close.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 事业部财务进销存 api
 */

// 事业部财务进销存 - 获取分页数据
export const buFinanceSummaryList = (data) => {
  return request({
    url: '/webapi/report/buFinance/buFinanceSummaryList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部财务进销存 - 刷新报表
export const buFlashInventorSummary = (data) => {
  return request({
    url: '/webapi/report/buFinance/buFlashInventorSummary.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部财务进销存 - 生成excel
export const createInventorSummaryTask = (data) => {
  return request({
    url: '/webapi/report/buFinance/createTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部财务进销存 - SD进销存导出
export const createInventorSummarySdTask = (data) => {
  return request({
    url: '/webapi/report/buFinance/createSdTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部财务进销存 - 总账导出
export const createInventorSummaryAllTask = (data) => {
  return request({
    url: '/webapi/report/buFinance/createAllAccountTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部财务进销存 - 暂估PDF导出
export const createInventorSummaryPdfTask = (data) => {
  return request({
    url: '/webapi/report/buFinance/createTempEstimatePdfTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部财务进销存 - 创建利息经销存导出任务
export const createInventorSummaryInterestTask = (data) => {
  return request({
    url: '/webapi/report/buFinance/createInterestTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部财务进销存 - 美赞臣成本差异导出
export const createCostDifferenceTask = (data) => {
  return request({
    url: '/webapi/report/buFinance/createCostDifferenceTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 事业部业务进销存 api
 */

// 事业部业务进销存 - 获取分页数据
export const buInventorSummaryList = (data) => {
  return request({
    url: '/webapi/report/buSummary/buInventorSummaryList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部业务进销存 - 生成excel
export const createBuInventorSummaryTask = (data) => {
  return request({
    url: '/webapi/report/buSummary/createTask.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部业务进销存 - 获取分页数据
export const flashBuSummary = (data) => {
  return request({
    url: '/webapi/report/buSummary/buFlashInventorSummary.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部业务进销存 - 获取库位进销存汇总分页数据
export const getBuLocationSummaryList = (data) => {
  return request({
    url: '/webapi/report/buLocationSummary/listByPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部业务进销存 - 查询月结状态
export const getMonthlyAccount = (data) => {
  return request({
    url: '/webapi/report/buSummary/getMonthlyAccount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 事业部业务进销存 - 详情
export const buInventorSummaryDetail = (data) => {
  return request({
    url: '/webapi/report/buSummary/toDetailPage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 云集采销日报 api
 */

// 云集采销日报 获取商品品类下拉框
export const listMerchandiseCatSelectBean = (data) => {
  return request({
    url: '/webapi/report/yunjiDailySalesReport/listMerchandiseCatSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 云集采销日报 获取品牌下拉框
export const listBrandSelectBean = (data) => {
  return request({
    url: '/webapi/report/yunjiDailySalesReport/listBrandSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 云集采销日报 列表
export const listYunJiDailySalesReport = (data) => {
  return request({
    url: '/webapi/report/yunjiDailySalesReport/listYunJiDailySalesReport.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 云集采销日报 - 导出
export const getDailySalesReportCountUrl =
  '/webapi/report/yunjiDailySalesReport/exportDailySalesReport.asyn'

// 云集采销日报 -  获取导出数量
export const getDailySalesReportCount = (data) => {
  return request({
    url: '/webapi/report/yunjiDailySalesReport/getDailySalesReportCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 云集采销日报 -  刷新
export const freshDailySalesByDay = (data) => {
  return request({
    url: '/webapi/report/yunjiDailySalesReport/freshDailySalesByDay.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

/**
 * 唯品PO账单核销报表 api
 */
// 唯品PO账单核销报表  PO商品核销表 -  列表
export const listVipPoBillVeriList = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/listVipPoBillVeriList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 唯品PO账单核销报表  PO商品核销表 -  获取数据截止时间
export const getDataTime = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/getDataTime.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 唯品PO账单核销报表  PO商品核销表 -  汇总表导出
export const exportMainUrl =
  '/webapi/report/vipPoBillVerification/exportMain.asyn'

// 唯品PO账单核销报表  PO商品核销表 - 明细表导出
export async function exportDetails(params) {
  return request({
    url: '/webapi/report/vipPoBillVerification/exportDetails.asyn',
    method: 'GET',
    params
  })
}

// 唯品PO账单核销报表  PO商品核销表 - 刷新
export const flashVipPoBillVeris = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/flashVipPoBillVeris.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

export * from './autoVerification' // 自动化校验

export * from './taskDownloadManage' // 报表任务管理

// 唯品PO账单核销报表  PO商品核销表 - 详情
export const vipPoBillVerificationToDetail = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/toDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 唯品PO账单核销报表  PO商品核销表 - 详情内表格数据
export const getDetailsByType = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/getDetailsByType.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 唯品PO账单核销报表  po完结 - 列表
export const listVipPoBillVeriPoList = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/listVipPoBillVeriPoList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 唯品PO账单核销报表  po完结 获取未结算量
export const countUnsettledAccount = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/countUnsettledAccount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 唯品PO账单核销报表  po完结 获取未结算量
export const changeStatus = (data) => {
  return request({
    url: '/webapi/report/vipPoBillVerification/changeStatus.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
