package com.topideal.order.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 卖家 买家菜单加载 Created by acer on 2016/8/24.
 * 
 * @author Chen
 * @since
 */
@Controller
@RequestMapping(value = "/load/page.asyn")
public class LoadPageController {


	// 首页
	@RequestMapping(params = "act=2001")
	public String m100() {
		return "002";
	}

	// 采购订单列表
	@RequestMapping(params = "act=3001")
	public String m3001() {
		return "forward:/purchase/toPage.html";
	}

	// 采购订单列表-新增页面
	@RequestMapping(params = "act=30011")
	public String m30011() {
		return "forward:/purchase/toAddPage.html";
	}

	// 采购订单列表-编辑页面
	@RequestMapping(params = "act=30012")
	public String m30012(Long id) {
		return "forward:/purchase/toEditPage.html?id=" + id;
	}

	// 采购订单列表-详情页面
	@RequestMapping(params = "act=30013")
	public String m30013(Long id) {
		return "forward:/purchase/toDetailsPage.html?id=" + id;
	}

	// 采购订单列表-导入页面
	@RequestMapping(params = "act=30014")
	public String m30014() {
		return "forward:/purchase/toImportPage.html";
	}
	
	// 采购订单列表-采购合同编辑页面
	@RequestMapping(params = "act=30015")
	public String m30015(Long id) {
		return "forward:/purchase/toEditContractPage.html?id="+id;
	}
	
	// 采购订单列表-采购合同编辑页面
	@RequestMapping(params = "act=30016")
	public String m30016(Long id) {
		return "forward:/purchase/toDetailContractPage.html?id="+id;
	}
	
	// 采购订单列表-采购链路step1
	@RequestMapping(params = "act=30017")
	public String m30017(Long id) {
		return "forward:/purchase/toSaleStepOnePage.html";
	}
	
	// 采购订单列表-采购链路step1
	@RequestMapping(params = "act=300171")
	public String m300171(Long purchaseTradeLinkId) {
		return "forward:/purchase/backToSaleStepOnePage.html";
	}
	
	// 采购订单列表-采购链路step2
	@RequestMapping(params = "act=30018")
	public String m30018(Long id) {
		return "forward:/purchase/toSaleStepTwoPage.html?id="+id;
	}
	
	// 采购订单列表-嘉宝采购链路step2
	@RequestMapping(params = "act=300181")
	public String m300181(Long id) {
		return "forward:/purchase/toJBSaleStepTwoPage.html?id="+id;
	}
	
	// 采购订单列表-采购链路step3
	@RequestMapping(params = "act=30019")
	public String m30019(Long id) {
		return "forward:/purchase/toSaleStepThreePage.html?id="+id;
	}
	
	// 采购订单列表-打托入库
	@RequestMapping(params = "act=300101")
	public String m300101(Long id) {
		return "forward:/purchase/toPurchaseInPage.html?id="+id;
	}
	
	//融资订单新增
	@RequestMapping(params = "act=300102")
	public String m300102(String ids) {
		return "forward:/purchase/toFinancingPage.html";
	}

	// 预申报单列表
	@RequestMapping(params = "act=3002")
	public String m3002() {
		return "forward:/declare/toPage.html";
	}

	// 预申报单列表-编辑
	@RequestMapping(params = "act=30021")
	public String m30021() {
		return "forward:/declare/toEditPage.html";
	}

	// 预申报单列表-详情
	@RequestMapping(params = "act=30022")
	public String m30022() {
		return "forward:/declare/toDetailsPage.html";
	}

	// 预申报单列表-清关资料编辑
	@RequestMapping(params = "act=30023")
	public String m30023() {
		return "forward:/declare/toCustomsDeclarePage.html";
	}

	// 预申报单列表-清关资料编辑
	@RequestMapping(params = "act=30024")
	public String m30024() {
		return "forward:/declare/toCustomsDeclareDetailsPage.html";
	}

	// 理货单列表
	@RequestMapping(params = "act=3003")
	public String m3003() {
		return "forward:/tallying/toPage.html";
	}

	// 理货单列表-详情页面
	@RequestMapping(params = "act=30031")
	public String m30031() {
		return "forward:/tallying/toDetailsPage.html";
	}

	// 入库单列表
	@RequestMapping(params = "act=3004")
	public String m3004() {
		return "forward:/warehouse/toPage.html";
	}

	// 入库单列表-详情
	@RequestMapping(params = "act=30041")
	public String m30041() {
		return "forward:/warehouse/toDetailsPage.html";
	}

	// 入库单列表-入库导入
	@RequestMapping(params = "act=30042")
	public String m30042() {
		return "forward:/warehouse/toImportPage.html";
	}

	// 入库单列表-采购关联导入
	@RequestMapping(params = "act=30043")
	public String m30043() {
		return "forward:/warehouse/toRelationImportPage.html";
	}

	// 勾稽明细列表
	@RequestMapping(params = "act=3005")
	public String m3005() {
		return "forward:/difference/toPage.html";
	}

	// 差异分析列表
	@RequestMapping(params = "act=3006")
	public String m3006() {
		return "forward:/difference/toPageByCheckTheDetails.html";
	}

	// 采购订单跟踪报表
	@RequestMapping(params = "act=3007")
	public String m3007() {
		return "forward:/difference/toPageByFollowing.html";
	}
	
	// 采购退货订单报表
	@RequestMapping(params = "act=3008")
	public String m3008() {
		return "forward:/purchaseReturn/toPage.html";
	}
	
	// 采购退货订单报表详情
	@RequestMapping(params = "act=30081")
	public String m30081(Long id) {
		return "forward:/purchaseReturn/toDetailsPage.html?id =" + id;
	}
	
	// 采购退货订单报表详情
	@RequestMapping(params = "act=30082")
	public String m30082(Long id) {
		return "forward:/purchaseReturn/toEditPage.html?id =" + id;
	}
	
	// 采购退货订单报表详情
	@RequestMapping(params = "act=30083")
	public String m30083(String ids) {
		return "forward:/purchaseReturn/toAddPage.html?ids =" + ids;
	}
	
	// 采购退货订单报表详情
	@RequestMapping(params = "act=30084")
	public String m30084(Long id) {
		return "forward:/purchaseReturn/toOutDepotPage.html?id=" + id;
	}
	
	// 采购退货出库订单跟踪报表
	@RequestMapping(params = "act=3009")
	public String m3009() {
		return "forward:/purchaseReturnOdepot/toPage.html";
	}
	
	// 采购退货订单报表详情
	@RequestMapping(params = "act=30091")
	public String m30091(Long id) {
		return "forward:/purchaseReturnOdepot/toDetailsPage.html?id =" + id;
	}
	
	// 采购退货出库订单导出
	@RequestMapping(params = "act=30092")
	public String m30092() {
		return "forward:/purchaseReturnOdepot/toImportPage.html";
	}
	
	// 采购SD订单
	@RequestMapping(params = "act=3010")
	public String m3010() {
		return "forward:/purchaseSdOrder/toPage.html";
	}
	
	// 采购SD订单详情
	@RequestMapping(params = "act=30101")
	public String m30101(Long id) {
		return "forward:/purchaseSdOrder/toDetailsPage.html";
	}
	
	// 采购SD订单金额调整
	@RequestMapping(params = "act=30102")
	public String m30102(Long id) {
		return "forward:/purchaseSdOrder/toImportPage.html";
	}

	// 销售订单列表
	@RequestMapping(params = "act=4001")
	public String m4001() {
		return "forward:/sale/toPage.html";
	}

	// 销售订单列表-新增页面
	@RequestMapping(params = "act=40011")
	public String m40011() {
		return "forward:/sale/toAddPage.html";
	}

	// 销售订单列表-编辑页面
	@RequestMapping(params = "act=40012")
	public String m40012(Long id,String operate) {
		return "forward:/sale/toEditPage.html?id =" + id ;
	}

	// 销售订单列表-详情页面
	@RequestMapping(params = "act=40013")
	public String m40013(Long id) {
		return "forward:/sale/toDetailsPage.html?id =" + id;
	}
  
	// 销售订单列表-购销代销导入页面
	@RequestMapping(params = "act=40014")
	public String m40014() {
		return "forward:/sale/toImportPage.html";
	}

	// 销售订单列表-打托出库页面
    @RequestMapping(params = "act=40015")
    public String m40015(Long id) {
        return "forward:/sale/toSaleOutPage.html?id =" + id;
    }

    // 销售订单列表-上架页面
	@RequestMapping(params = "act=40016")
	public String m40016(Long id) {
		return "forward:/sale/toSaleShelfPage.html?id =" + id;
	}

	// 销售订单列表-上架导入页面
	@RequestMapping(params = "act=40018")
	public String m40018() {
		return "forward:/sale/toShelfImportPage.html";
	}
	// 销售订单-销售类型为采购执行-审核页面
	@RequestMapping(params = "act=40019")
	public String m40019(Long id) {
		return "forward:/sale/toDetailsPage.html?id =" + id;
	}

	// 预售单转销售单-编辑页面
	@RequestMapping(params = "act=40020")
	public String m40020(String data) {
		return "forward:/sale/preSaleEditPage.html";
	}

	// 电商订单列表
	@RequestMapping(params = "act=4002")
	public String m4002() {
		return "forward:/order/toPage.html";
	}

	// 电商订单列表-详情页面
	@RequestMapping(params = "act=40021")
	public String m40021(Long id) {
		return "forward:/order/toDetailsPage.html?id =" + id;
	}
	
	// 电商订单列表-导入页面
	@RequestMapping(params = "act=40022")
	public String m40022() {
		return "forward:/order/toImportPage.html";
	}
	// 电商订单-事业部补录列表
	@RequestMapping(params = "act=40023")
	public String m40023() {
		return "forward:/order/toBusinessUnitRecordPage.html";
	}
	// 销售出库清单列表
	@RequestMapping(params = "act=4003")
	public String m4003() {
		return "forward:/saleOut/toPage.html";
	}

	// 销售出库清单列表-详情页面
	@RequestMapping(params = "act=40031")
	public String m40031(Long id) {
		return "forward:/saleOut/toDetailsPage.html?id =" + id;
	}
	// 销售出库清单列表-导入页面
	@RequestMapping(params = "act=40032")
	public String m40032() {
		return "forward:/saleOut/toImportPage.html";
	}

	// 销售出库差异分析表
	@RequestMapping(params = "act=4004")
	public String m4004() {
		return "forward:/saleAnalysis/toPage.html";
	}
	// 销售上架表
	@RequestMapping(params = "act=40041")
	public String m40041() {
		return "forward:/shelf/shelf.html";
	}

	// 销售上架入库表
	@RequestMapping(params = "act=40042")
	public String m40042() {
		return "forward:/saleShelfIdepot/shelfIdepot.html";
	}

	// 销售出库明细
	@RequestMapping(params = "act=4005")
	public String m4005() {
		return "forward:/saleOutDetails/toPage.html";
	}
	// 销售出库上架明细
	@RequestMapping(params = "act=4006")
	public String m4006() {
		return "forward:/saleOut/toSaleOutShelfPage.html";
	}
	
	// 销售出库--导入页面
	@RequestMapping(params = "act=4009")
	public String m4009() {
		return "forward:/saleOut/toImportPage.html";
	}
	
	// 电商订单退货列表
	@RequestMapping(params = "act=4008")
	public String m4008() {
		return "forward:/orderReturnIdepot/toPage.html";
	}
	// 电商订单退货列表-详情页面
	@RequestMapping(params = "act=40081")
	public String m40081(Long id) {
		return "forward:/orderReturnIdepot/toDetailsPage.html?id =" + id;
	}
	// 电商订单退货列表-导入页面
	@RequestMapping(params = "act=40082")
	public String m40082() {
		return "forward:/orderReturnIdepot/toImportPage.html";
	}

	// 上架单列表
	@RequestMapping(params = "act=4010")
	public String m4010() {
		return "forward:/shelf/shelf.html";
	}
	
	// 上架入库单详情
	@RequestMapping(params = "act=40101")
	public String m40101(Long id) {
		return "forward:/saleShelfIdepot/toDetailsPage.html?id =" + id;
	}

	// 上架单详情
	@RequestMapping(params = "act=40102")
	public String m40102(Long id) {
		return "forward:/shelf/toDetailsPage.html?id =" + id;
	}

	// 账单出库单列表
	@RequestMapping(params = "act=4011")
	public String m4011() {
		return "forward:/billOutinDepot/toPage.html";
	}
	
	// 账单出库单详情
	@RequestMapping(params = "act=40111")
	public String m40111(Long id) {
		return "forward:/billOutinDepot/toDetailsPage.html?id =" + id;
	}
	
	// 账单出库单详情
	@RequestMapping(params = "act=40112")
	public String m40112(Long id) {
		return "forward:/billOutinDepot/toImportPage.html";
	}
	
	// 销售退货列表
	@RequestMapping(params = "act=5001")
	public String m5001() {
		return "forward:/saleReturn/toPage.html";
	}

	// -详情页面
	@RequestMapping(params = "act=50011")
	public String m50011(Long id) {
		return "forward:/saleReturn/toDetailsPage.html?id =" + id;
	}

	// -导入页面(代销退货类型导入)
	@RequestMapping(params = "act=50012")
	public String m50012() {
		return "forward:/saleReturn/toImportPage.html";
	}
	// -导入页面(消费者退货类型导入)
	@RequestMapping(params = "act=50013")
	public String m50013() {
		return "forward:/saleReturn/toImportPage2.html";
	}
	// -新增页面
	@RequestMapping(params = "act=50014")
	public String m50014() {
		return "forward:/saleReturn/toAddPage.html";
	}
	// 销售退货订单列表-编辑页面
	@RequestMapping(params = "act=50015")
	public String m50015(Long id) {
		return "forward:/saleReturn/toEditPage.html?id =" + id;
	}
	// 销售退货订单列表-出库打托
	@RequestMapping(params = "act=50016")
	public String m50016(Long id) {
		return "forward:/saleReturn/toOutDepotReport.html?id =" + id;
	}
	// 跳转到销售退货上架入库页面
	@RequestMapping(params = "act=50017")
	public String m50017() {
		return "forward:/saleReturn/toSaleReturnInPage.html";
	}	

	// 销售退货入库列表
	@RequestMapping(params = "act=5002")
	public String m5002() {
		return "forward:/saleReturnIdepot/toPage.html";
	}

	// 销售退货入库列表-详情页面
	@RequestMapping(params = "act=50021")
	public String m50021(Long id) {
		return "forward:/saleReturnIdepot/toDetailsPage.html?id =" + id;
	}

	// 销售退货出库列表
	@RequestMapping(params = "act=5003")
	public String m5003() {
		return "forward:/saleReturnOdepot/toPage.html";
	}

	// 销售退货出库列表-详情页面
	@RequestMapping(params = "act=50031")
	public String m50031(Long id) {
		return "forward:/saleReturnOdepot/toDetailsPage.html?id =" + id;
	}

	// 销售退理货单列表
	@RequestMapping(params = "act=5004")
	public String m5004() {
		return "forward:/saleReturnTallyin/toPageTransfer.html";
	}

	// 销售退理货单详情
	@RequestMapping(params = "act=50041")
	public String m50041() {
		return "forward:/saleReturnTallyin/toDetailPage.html";
	}

	// 预售单列表
	@RequestMapping(params = "act=6001")
	public String m6001() {
		return "forward:/preSale/toPage.html";
	}
	// 预售单列表-详情页面
	@RequestMapping(params = "act=60011")
	public String m60011(Long id) {
		return "forward:/preSale/toDetailsPage.html?id =" + id;
	}
	// 预售勾稽明细列表
	@RequestMapping(params = "act=7001")
	public String m7001() {
		return "forward:/preSaleOrderCorrelation/toPage.html";
	}
	// 预售勾稽明细列表-详情页面
	@RequestMapping(params = "act=70011")
	public String m70011(String code, String goodsNo) {
		return "forward:/preSaleOrderCorrelation/toDetailsPage.html";
	}
	// 预售单列表-新增页面
	@RequestMapping(params = "act=60012")
	public String m60012() {
		return "forward:/preSale/toAddPage.html";
	}
	// 预售单列表-编辑页面
	@RequestMapping(params = "act=60013")
	public String m60013() {
		return "forward:/preSale/toEditPage.html";
	}
	// 预售单列表-导入页面
	@RequestMapping(params = "act=60014")
	public String m60014() {
		return "forward:/preSale/toImportPage.html";
	}

	// 调拨单列表
	@RequestMapping(params = "act=8001")
	public String m8001() {
		return "forward:/transfer/toPage.html";
	}

	// 跳转到调拨详情页面
	@RequestMapping(params = "act=80011")
	public String m80011() {
		return "forward:/transfer/toDetailPage.html";
	}

	// 跳转到调拨单编辑页面
	@RequestMapping(params = "act=80012")
	public String m80012() {
		return "forward:/transfer/toEditPage.html";
	}

	/*
	 * //根据类型加载仓库
	 * 
	 * @RequestMapping(params = "act=80013") public String m80013(String
	 * inDepotType) { return "forward:/transfer/loadIngDepotByType.asyn"; }
	 */
	// 跳转到新增页面
	@RequestMapping(params = "act=80014")
	public String m80014(String inDepotType) {
		return "forward:/transfer/toAddPage.html";
	}

	// 跳转到导入页面
	@RequestMapping(params = "act=80015")
	public String m80015(String inDepotType) {
		return "forward:/transfer/toImportPage.html";
	}

	// 跳转到上架入库页面
	@RequestMapping(params = "act=80016")
	public String m80016() {
		return "forward:/transfer/toTransferInPage.html";
	}

	// 跳转到出库打托页面
	@RequestMapping(params = "act=80017")
	public String m80017() {
		return "forward:/transfer/toTransferOutPage.html";
	}

	// 调拨理货列表
	@RequestMapping(params = "act=8002")
	public String m8002() {
		return "forward:/transferTallying/toPageTransfer.html";
	}

	// 调拨理货详情
	@RequestMapping(params = "act=80021")
	public String m80021(Long id) {
		return "forward:/transferTallying/toDetailPage.html?id=" + id;
	}

	// 调拨出库列表
	@RequestMapping(params = "act=8003")
	public String m8003() {
		return "forward:/transferOut/toPage.html";
	}

	// 调拨出库详情
	@RequestMapping(params = "act=80031")
	public String m80031(Long id) {
		return "forward:/transferOut/toDetailPage.html?id=" + id;
	}

	// 调拨入库列表
	@RequestMapping(params = "act=8004")
	public String m8004() {
		return "forward:/transferIn/toPage.html";
	}

	// 调拨入库详情
	@RequestMapping(params = "act=80041")
	public String m80041(Long id) {
		return "forward:/transferIn/toDetailPage.html";
	}

	// 调出调入流水
	@RequestMapping(params = "act=8005")
	public String m8005() {
		return "forward:/transferOutIn/toPage.html";
	}
	
	// 爬虫商品对照表
	@RequestMapping(params = "act=11001")
	public String m11001() {
		return "forward:/contrast/toPage.html";
	}
	// 爬虫商品对照表详情
	@RequestMapping(params = "act=110011")
	public String m110011(Long id) {
		return "forward:/contrast/detail.html?id="+id;
	}
	//爬虫商品对照表新增
	@RequestMapping(params = "act=110012")
	public String m110012() {
		return "forward:/contrast/toAddPage.html";
	}
	//爬虫商品对照表编辑
	@RequestMapping(params = "act=110013")
	public String m110013(Long id) {
		return "forward:/contrast/toEditPage.html?id="+id;
	}
	//爬虫唯品出库仓关系列表
	@RequestMapping(params = "act=110014")
	public String m110014() {
		return "forward:/vipshop/toPage.html";
	}
	// 唯品出库仓关系列表-批量导入页面
	@RequestMapping(params = "act=1100141")
	public String m1100141() {
		return "forward:/vipshop/toImportPage.html";
	}
	//爬虫云集仓库对照表
	@RequestMapping(params = "act=110015")
	public String m110015() {
		return "forward:/yunjiDepot/toPage.html";
	}
	// 云集仓库对照表-新增页面
	@RequestMapping(params = "act=1100151")
	public String m1100151() {
		return "forward:/yunjiDepot/toAddPage.html";
	}
	// 附件管理-详细页面
	@RequestMapping(params = "act=200001")
	public String m200001(String codeid) {
		return "forward:/attachment/toPage.html?code=" + codeid;
	}
	
	
	// 组合品对照表
	@RequestMapping(params = "act=11003")
	public String m11003() {
		return "forward:/groupMerchandiseContrast/toPage.html";
	}
	// 组合品对照表详情
	@RequestMapping(params = "act=110031")
	public String m110031(Long id) {
		return "forward:/groupMerchandiseContrast/toDetailsPage.html?id="+id;
	}
	//组合品对照表新增
	@RequestMapping(params = "act=110032")
	public String m110032() {
		return "forward:/groupMerchandiseContrast/toAddPage.html";
	}
	//组合品对照表编辑
	@RequestMapping(params = "act=110033")
	public String m110033(Long id) {
		return "forward:/groupMerchandiseContrast/toEditPage.html?id="+id;
	}
	// 组合品对照表导入页面
	@RequestMapping(params = "act=110034")
	public String m110034(HttpSession session) {
		return "forward:/groupMerchandiseContrast/toImportPage.html";
	}

	// 促销记录列表
	@RequestMapping(params = "act=12001")
	public String m12001() {
		return "forward:/promotionRecord/toPage.html";
	}

	// 模型配置列表
	@RequestMapping(params = "act=12002")
	public String m12002() {
		return "forward:/modelSetting/toPage.html";
	}
	
	// 宝洁商品货期配置列表
	@RequestMapping(params = "act=12003")
	public String m12003() {
		return "forward:/goodsTimeConfig/toPage.html";
	}
	
	// 宝洁商品货期配置-导入页面
	@RequestMapping(params = "act=12004")
	public String m12004() {
		return "forward:/goodsTimeConfig/toImportPage.html";
	}
	
	// 采购计划配置列表
	@RequestMapping(params = "act=12005")
	public String m12005() {
		return "forward:/purchasePlanning/toPage.html";
	}

	// 事业部移库单列表
	@RequestMapping(params = "act=13001")
	public String m13001() {
		return "forward:/buMoveInventory/toPage.html";
	}
	// 事业部移库单详情
	@RequestMapping(params = "act=13002")
	public String m13002() {
		return "forward:/buMoveInventory/toDetailsPage.html";
	}

	// 事业部移库单列表-导入页面
	@RequestMapping(params = "act=13003")
	public String m13003() {
		return "forward:/buMoveInventory/toImportPage.html";
	}
	
	//文件模版列表
	@RequestMapping(params = "act=16001")
	public String m16001() {
		return "forward:/fileTemp/toPage.html";
	}
	
	//文件模版编辑
	@RequestMapping(params = "act=16002")
	public String m16002(String id) {
		return "forward:/fileTemp/toEditPage.html" ;
	}

	//文件模版预览
	@RequestMapping(params = "act=16003")
	public String m16003(String id) {
		return "forward:/fileTemp/toViewPage.html" ;
	}

	//清关单证配置列表
	@RequestMapping(params = "act=16011")
	public String m16011() {
		return "forward:/customsFileConfig/toPage.html";
	}

	//应收账单列表
	@RequestMapping(params = "act=14001")
	public String m14001() {
		return "forward:/receiveBill/toPage.html";
	}
	// 应收账单详情
	@RequestMapping(params = "act=14002")
	public String m14002() {
		return "forward:/receiveBill/toDetailsPage.html";
	}
	// 创建应收
	@RequestMapping(params = "act=14026")
	public String m14026() {
		return "forward:/receiveBill/toAddPage.html";
	}

	//结算项目配置
	@RequestMapping(params = "act=14003")
	public String m14003() {
		return "forward:/settlementConfig/toPage.html";
	}
	//补充费用信息
	@RequestMapping(params = "act=14004")
	public String m14004() {
		return "forward:/receiveBill/toFreeADDPage.html";
	}
	//审核页面
	@RequestMapping(params = "act=14005")
	public String m14005() {
		return "forward:/receiveBill/toAuditPage.html";
	}
	//收款核销跟踪
	@RequestMapping(params = "act=14006")
	public String m14006() {
		return "forward:/receiveBillVerification/toPage.html";
	}
	//应收账单开票页面
	@RequestMapping(params = "act=14007")
	public String m14007() {
		return "forward:/receiveBill/toInvoicePage.html";
	}
	// 平台费用单
	@RequestMapping(params = "act=14008")
	public String m14008() {
		return "forward:/platformCostOrder/toPage.html";
	}
	@RequestMapping(params = "act=14009")
	public String m14009() {
		return "forward:/platformCostOrder/toDetailsPage.html";
	}
	// 应收发票库列表
	@RequestMapping(params = "act=14010")
	public String m14010() {
		return "forward:/receiveBillInvoice/toPage.html";
	}
	// 应收发票库附件上传页面
	@RequestMapping(params = "act=14011")
	public String m14011() {
		return "forward:/receiveBillInvoice/toInvoiceUploadPage.html";
	}
	// 平台结算单
	@RequestMapping(params = "act=14012")
	public String m14012() {
		return "forward:/platformStatementOrder/toPage.html";
	}
	@RequestMapping(params = "act=14013")
	public String m14013() {
		return "forward:/platformStatementOrder/toDetailsPage.html";
	}
	// 平台结算单开票页面
	@RequestMapping(params = "act=14014")
	public String m14014() {
		return "forward:/platformStatementOrder/toInvoicePage.html";
	}
	//  To C暂估应收表页面
	@RequestMapping(params = "act=14015")
	public String m14015() {
		return "forward:/tocTempReceiveBill/toPage.html";
	}
	//  To C暂估应收表详情页面
	@RequestMapping(params = "act=14016")
	public String m14016() {
		return "forward:/tocTempReceiveBill/toDetailsPage.html";
	}

	//  平台费用映射
	@RequestMapping(params = "act=14017")
	public String m14017() {
		return "forward:/platformSettlementConfig/toPage.html";
	}
	
	//  To C应收结算单页面
	@RequestMapping(params = "act=14018")
	public String m14018() {
		return "forward:/toCReceiveBill/toPage.html";
	}
	//  To C应收结算单新增页面
	@RequestMapping(params = "act=14019")
	public String m14019() {
		return "forward:/toCReceiveBill/toImportPage.html";
	}
	//  To C应收结算单详情
	@RequestMapping(params = "act=14023")
	public String m14023() {
		return "forward:/toCReceiveBill/toDetailsPage.html";
	}
	//To C应收结算单补充费用信息
	@RequestMapping(params = "act=14024")
	public String m14024() {
		return "forward:/toCReceiveBill/toFreeADDPage.html";
	}
	//To C应收结算单审核页面
	@RequestMapping(params = "act=14025")
	public String m14025() {
		return "forward:/toCReceiveBill/toAuditPage.html";
	}
	@RequestMapping(params = "act=14027")
	public String m14027() {
		return "forward:/toCReceiveBill/toInvoicePage.html";
	}
	//  平台费用映射导入
	@RequestMapping(params = "act=14020")
	public String m14020() {
		return "forward:/platformSettlementConfig/importPage.html";
	}
	@RequestMapping(params = "act=14021")
	public String m14021() {
		return "forward:/receiveBillInvoice/toReplace.html";
	}
	// 协议单价列表
	@RequestMapping(params = "act=15001")
	public String m15001() {
		return "forward:/agreementCurrencyConfig/toPage.html";
	}
	//协议单价新增
	@RequestMapping(params = "act=15002")
	public String m15002() {
		return "forward:/agreementCurrencyConfig/toAddPage.html";
	}
	// 协议单价-导入页面
	@RequestMapping(params = "act=15003")
	public String m15003() {
		return "forward:/agreementCurrencyConfig/toImportPage.html";
	}
	// NC收支费项-首页
	@RequestMapping(params = "act=15007")
	public String m15007() {
		return "forward:/receivePaymentSubject/toPage.html";
	}

	// 平台采购列表
	@RequestMapping(params = "act=17001")
	public String m17001() {
		return "forward:/platformPurchaseOrder/toPage.html";
	}

	// 库位调整单列表
	@RequestMapping(params = "act=18001")
	public String m18001() {
		return "forward:/inventoryLocationAdjustment/toPage.html";
	}
	// 库位调整单列表-导入页面
	@RequestMapping(params = "act=18002")
	public String m18002() {
		return "forward:/inventoryLocationAdjustment/toImportPage.html";
	}
	// 库位调整单列表-详情页面
	@RequestMapping(params = "act=18003")
	public String m18003(Long id) {
		return "forward:/inventoryLocationAdjustment/toDetailsPage.html?id =" + id;
	}
	// 交易链路配置列表
	@RequestMapping(params = "act=19001")
	public String m19001() {
		return "forward:/tradeLinkConfig/toPage.html";
	}
	
	// SD类型配置列表
	@RequestMapping(params = "act=20001")
	public String m20001() {
		return "forward:/sdTypeConfig/toPage.html";
	}

	// 采购SD配置列表
	@RequestMapping(params = "act=20002")
	public String m20002() {
		return "forward:/sdPurchaseConfig/toPage.html";
	}
	
	// 采购SD配置新增
	@RequestMapping(params = "act=200021")
	public String m200021() {
		return "forward:/sdPurchaseConfig/toAddPage.html";
	}
	
	// 采购SD配置编辑
	@RequestMapping(params = "act=200022")
	public String m200022(Long id) {
		return "forward:/sdPurchaseConfig/toEditPage.html";
	}
	
	// 采购SD配置编辑
	@RequestMapping(params = "act=200023")
	public String m200023(Long id) {
		return "forward:/sdPurchaseConfig/toCopyPage.html";
	}
	
	// 采购SD配置编辑
	@RequestMapping(params = "act=200024")
	public String m200024(Long id) {
		return "forward:/sdPurchaseConfig/toDetail.html";
	}

	//平台数据
	@RequestMapping(params = "act=210001")
	public String m210001() {
		return "forward:/platformMerchandise/toPage.html";
	}
	//平台商品导入
	@RequestMapping(params = "act=210002")
	public String m210002() {
		return "forward:/platformMerchandise/toPlatformMerchandiseImportPage.html";
	}
	//平台商品箱规导入
	@RequestMapping(params = "act=210003")
	public String m210003() {
		return "forward:/platformMerchandise/toCartonSizeImportPage.html";
	}
	
	//采购计划报表
	@RequestMapping(params = "act=20003")
	public String m20003() {
		return "forward:/purchasingReports/toPage.html";
	}
	//欧莱雅采购订单
	@RequestMapping(params = "act=22001")
	public String m22001() {
		return "forward:/orealPurchaseOrder/toPage.html";
	}
	//欧莱雅采购订单详情
	@RequestMapping(params = "act=22002")
	public String m22002() {
		return "forward:/orealPurchaseOrder/toDetailsPage.html";
	}	
	
}
