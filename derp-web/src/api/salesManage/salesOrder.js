/**
 * 销售订单 api
 */
import request from '@u/http'
import qs from 'qs'

// 销售管理 - 销售订单 - 查询销售订单列表信息
export const listSaleOrder = (data) => {
  return request({
    url: '/webapi/order/sale/listSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 删除
export const delSaleOrder = (data) => {
  return request({
    url: '/webapi/order/sale/delSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 获取导出销售订单的数量
export const getSaleOrderCount = (data) => {
  return request({
    url: '/webapi/order/sale/getOrderCount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 计算销售订单出库的百分比
export const differenceRatio = (data) => {
  return request({
    url: '/webapi/order/sale/differenceRatio.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 完结出库
export const endStockOut = (data) => {
  return request({
    url: '/webapi/order/sale/endStockOut.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 中转仓出库
export const saleOrderStockOut = (data) => {
  return request({
    url: '/webapi/order/sale/saleOrderStockOut.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 判断订单商品上架月份是否已关账
export const checkExistFinanceClose = (data) => {
  return request({
    url: '/webapi/order/sale/checkExistFinanceClose.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 显示金额调整信息
export const showOrderAmount = (data) => {
  return request({
    url: '/webapi/order/sale/showOrderAmount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 金额调整
export const amountAdjust = (data) => {
  return request({
    url: '/webapi/order/sale/amountAdjust.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 金额确认
export const amountConfirm = (data) => {
  return request({
    url: '/webapi/order/sale/amountConfirm.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 访问详情页面
export const listSaleDetail = (data) => {
  return request({
    url: '/webapi/order/sale/toDetailsPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 获取关区列表
export const getCustomsAreaList = (data) => {
  return request({
    url: '/webapi/system/customsArea/getSelectBean.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 校验仓库
export const checkDepotMerchantRel = (data) => {
  return request({
    url: '/webapi/system/depot/checkDepotMerchantRel.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 新增/修改
export const saveOrModifyTempSaleOrder = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/sale/saveOrModifyTempOrder.asyn',
    method: 'POST',
    data
  })
}

// 销售管理 - 销售订单 - 根据商家客户id带出税率
export const getRateByCustomerAndMerchant = (data) => {
  return request({
    url: '/webapi/system/customer/getRateByCustomerAndMerchant.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 校验PO号在销售-po关联表是否已存在
export const checkExistByPo = (data) => {
  return request({
    url: '/webapi/order/sale/checkExistByPo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 访问编辑页面
export const toSaleOrderEdit = (data) => {
  return request({
    url: '/webapi/order/sale/toEditPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 提交
export const submitSaleOrder = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/sale/submitSaleOrder.asyn',
    method: 'POST',
    data
  })
}

// 销售管理 - 销售订单 - 获取商品单价
export const getSalePrice = (data) => {
  return request({
    url: '/webapi/order/sale/getSalePrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 获取商品单价
export const getSalePriceManage = (data) => {
  return request({
    url: '/webapi/order/sale/getSalePriceManage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 修改并审核
export const modifySaleOrder = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/sale/modifySaleOrder.asyn',
    method: 'POST',
    data
  })
}

// 销售管理 - 销售订单 - 修改PO号
export const modifyPoNo = (data) => {
  return request({
    url: '/webapi/order/sale/modifyPoNo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 -反审
export const reverseAudit = (data) => {
  return request({
    url: '/webapi/order/sale/reverseAudit.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 中转仓获取商品明细
export const getStockOutItemList = (data) => {
  return request({
    url: '/webapi/order/sale/getStockOutItemList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 商品导出
export const exportItemsUrl = '/webapi/order/sale/exportItems.asyn'

// 销售管理 - 销售订单 - 获取选中订单的所有商品和对应数量
export const getSaleOrderGoodsInfo = (data) => {
  return request({
    url: '/webapi/order/sale/getOrderGoodsInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 签收
export const receiveSaleOrder = (data) => {
  return request({
    url: '/webapi/order/sale/receiveSaleOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 校验订单能否生成采购订单
export const checkOrderState = (data) => {
  return request({
    url: '/webapi/order/sale/checkOrderState.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 列表页面 生成采购订单
export const GeneratePurchaseOrder = (data) => {
  return request({
    url: '/webapi/order/sale/GeneratePurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 根据PO号判断是否存在采购单
export const checkExistPurchaseByPO = (data) => {
  return request({
    url: '/webapi/order/sale/checkExistPurchaseByPO.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 判断是否存在采购单
export const checkSaleExistPurchase = (data) => {
  return request({
    url: '/webapi/order/sale/checkExistPurchase.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 查询可生成销售订单的采购订单列表信息
export const showOwnPurchaseOrder = (data) => {
  return request({
    url: '/webapi/order/sale/showOwnPurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 采购转销售，检查商品信息
export const checkGoodsInfo = (data) => {
  return request({
    url: '/webapi/order/purchase/checkGoodsInfo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 判断是否已经完全上架
export const shelfIsEnd = (data) => {
  return request({
    url: '/webapi/order/sale/shelfIsEnd.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 上架页面数据回显
export const toSaleShelfPage = (data) => {
  return request({
    url: '/webapi/order/sale/toSaleShelfPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 保存上架商品信息
export const saveSaleShelf = (data) => {
  return request({
    url: '/webapi/order/sale/saveSaleShelf.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 销售管理 - 销售订单 - 提交并审核生成采购订单
export const createPurchaseOrder = (data) => {
  return request({
    url: '/webapi/order/sale/createPurchaseOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 根据销售订单号和po号查询平台采购单
export const getListByPoNo = (data) => {
  return request({
    url: '/webapi/order/platformPurchaseOrder/listPlatformPurchaseOrderByCodeAndPoNo.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 判断是否可以出库打托
export const checkOutDepotOrder = (data) => {
  return request({
    url: '/webapi/order/sale/checkOutDepotOrder.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 查询出库打托详情
export const toSaleOutPage = (data) => {
  return request({
    url: '/webapi/order/sale/toSaleOutPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 保存出库打托信息
export const saveSaleOut = (data) => {
  return request({
    headers: { 'Content-Type': 'application/json' },
    url: '/webapi/order/sale/saveSaleOut.asyn',
    method: 'POST',
    data
  })
}

// 销售管理 - 销售订单 - 融资申请、融资赎回弹窗展示
export const getFinanceDetail = (data) => {
  return request({
    url: '/webapi/order/sale/getFinanceDetail.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 融资申请
export const applyFinance = (data) => {
  return request({
    url: '/webapi/order/sale/applyFinance.asyn',
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    data
  })
}

// 销售管理 - 销售订单 - 还款试算
export const calRepayAmount = (data) => {
  return request({
    url: '/webapi/order/sale/calRepayAmount.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 申请赎回
export const applyRansom = (data) => {
  return request({
    url: '/webapi/order/sale/applyRansom.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 融资申请作废
export const cancelRansom = (data) => {
  return request({
    url: '/webapi/order/sale/cancelRansom.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 确认赎回
export const confirmRansom = (data) => {
  return request({
    url: '/webapi/order/sale/confirmRansom.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 访问新增页面
export const toSaleAddPage = (data) => {
  return request({
    url: '/webapi/order/sale/toAddPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 查询预售转销详情
export const preSaleEditPage = (data) => {
  return request({
    url: '/webapi/order/sale/preSaleEditPage.html',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 点击上架，弹窗展示销售出库列表
export const getSaleOutDepotList = (data) => {
  return request({
    url: '/webapi/order/saleOut/getSaleOutDepotList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 判断是否可生成预申报单
export const checkCreateDeclare = (data) => {
  return request({
    url: '/webapi/order/sale/checkCreateDeclare.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 销售转采购获取采购价格
export const getPurchasePriceItemList = (data) => {
  return request({
    url: '/webapi/order/sale/getPurchasePriceItemList.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 是否开启采购价格管理
export const getPurchasePriceManage = (data) => {
  return request({
    url: '/webapi/order/sale/getPurchasePriceManage.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 销售转采购获取采购价格
export const getPurchasePrice = (data) => {
  return request({
    url: '/webapi/order/sale/getPurchasePrice.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 批量驳回
export const saleBatchRejection = (data) => {
  return request({
    url: '/webapi/order/sale/batchRejection.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 导出清关资料
export const exportSaleCustomsInfo = '/webapi/order/sale/exportCustomsInfo.asyn'

// 销售管理 - 销售订单 - 导出
export const exportSaleOrderUrl = '/webapi/order/sale/exportSaleOrder.asyn'

// 销售管理 - 销售订单 - 导入购销代销销售订单
export const importSaleUrl = '/webapi/order/sale/importSale.asyn'

// 销售管理 - 销售订单 - 商品导入
export const importSaleGoodsUrl = '/webapi/order/sale/importSaleGoods.asyn'

// 销售管理 - 销售订单 - 上架导入
export const importSaleShelfUrl = '/webapi/order/sale/importSaleShelf.asyn'

// 销售管理 - 销售订单 - 申请判断是否可以红冲
export const validateApplyWriteOff = (data) => {
  return request({
    url: '/webapi/order/sale/validateApplyWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 申请红冲
export const saveWriteOff = (data) => {
  return request({
    url: '/webapi/order/sale/saveWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 点击审核红冲带出申请原因
export const showAuditWriteOff = (data) => {
  return request({
    url: '/webapi/order/sale/showAuditWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 审核红冲前校验
export const validateAuditWriteOff = (data) => {
  return request({
    url: '/webapi/order/sale/validateAuditWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 审核红冲
export const auditWriteOff = (data) => {
  return request({
    url: '/webapi/order/sale/auditWriteOff.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 销售管理 - 销售订单 - 获取列表各状态数量
export const getStatusNum = (data) => {
  return request({
    url: '/webapi/order/sale/getStatusNum.asyn',
    method: 'POST',
    data: qs.stringify(data)
  })
}
