package com.topideal.report.web;


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


	//经销存汇总报表列表
	@RequestMapping(params = "act=12001")
	public String m12001() {
		return "forward:/summary/toPage.html";
	}
	//经销存汇总报表详情页面
	@RequestMapping(params = "act=120011")
	public String m120011(Long merchantId,Long depotId,String ownMonth,String barcode) {
		return "forward:/summary/toDetailPage.html";
	}
	
	// 新云集采销日报
	@RequestMapping(params = "act=12002")
	public String m12002() {
		return "forward:/yunjiDailySalesReport/toPage.html";
	}
	// 购销采销日报
	@RequestMapping(params = "act=12003")
	public String m12003() {
		return "forward:/gouXiaoPurchaseDaily/toPage.html";
	}
	
	//财务经销存汇总报表列表
	@RequestMapping(params = "act=12004")
	public String m12004() {
		return "forward:/finance/toPage.html";
	}
	//报表任务列表
	@RequestMapping(params = "act=12005")
	public String m12005() {
		return "forward:/fileTask/toPage.html";
	}
	//财务经销存汇总报表列表
	@RequestMapping(params = "act=12006")
	public String m12006() {
		return "forward:/financeJb/toPageJb.html";
	}
	//财务进销存报表关账列表
	@RequestMapping(params = "act=12007")
	public String m12007() {
		return "forward:/financeClose/toPage.html";
	}
	//商品在库天数统计
	@RequestMapping(params = "act=12008")
	public String m12008() {
		return "forward:/inWarehouseDays/toPage.html";
	}
	//商品在库天数统计明细
	@RequestMapping(params = "act=12009")
	public String m12009(String month , String buId) {
		return "forward:/inWarehouseDays/toDetail.html";
	}
	//唯品PO账单核销列表
	@RequestMapping(params = "act=12010")
	public String m12010() {
		return "forward:/vipPoBillVerification/toPage.html";
	}
	//唯品PO账单核销明细
	@RequestMapping(params = "act=12011")
	public String m12011(String poNo , String commbarcode) {
		return "forward:/vipPoBillVerification/toDetail.html" ;
	}
	
	//唯品PO账单核销PO完结列表
	@RequestMapping(params = "act=12012")
	public String m12012() {
		return "forward:/vipPoBillVerification/toPoPage.html";
	}
	
	//存货准备降价列表
	@RequestMapping(params = "act=12013")
	public String m12013() {
		return "forward:/inventoryFallingPriceReserves/toPage.html";
	}
	//事业部财务经销存汇总
	@RequestMapping(params = "act=12014")
	public String m12014() {
		return "forward:/buFinance/toPage.html";
	}	
	//经销存汇总报表列表
	@RequestMapping(params = "act=12015")
	public String m12015() {
		return "forward:/buSummary/toPage.html";
	}
	//经销存汇总报表详情页面
	@RequestMapping(params = "act=120151")
	public String m120151(Long merchantId,Long depotId,Long buId,String ownMonth,String barcode) {
		return "forward:/buSummary/toDetailPage.html";
	}
	
	//结算价格列表
	@RequestMapping(params = "act=13001")
	public String m13001() {
		return "forward:/settlementPrice/toPage.html";
	}
	//结算价格新增
	@RequestMapping(params = "act=13002")
	public String m13002() {
		return "forward:/settlementPrice/toAddPage.html";
	}
	//结算价格编辑
	@RequestMapping(params = "act=13003")
	public String m13003(Long id) {
		return "forward:/settlementPrice/toEditPage.html";
	}
	//结算价格详情
	@RequestMapping(params = "act=13004")
	public String m13004(Long id) {
		return "forward:/settlementPrice/toDetailsPage.html";
	}
	//结算价格导入
	@RequestMapping(params = "act=13005")
	public String m13005() {
		return "forward:/settlementPrice/toImportPage.html";
	}
	
	//成本单价审核页
	@RequestMapping(params = "act=13006")
	public String m13006() {
		return "forward:/settlementPrice/toExaminePage.html";
	}
	
	//唯品自动化校验页
	@RequestMapping(params = "act=14001")
	public String m14001() {
		return "forward:/vipAutoVeri/toPage.html";
	}
	
	//云集自动化校验页
	@RequestMapping(params = "act=14002")
	public String m14002() {
		return "forward:/yunjiAutoVeri/toPage.html";
	}
	//自动检验任务页
	@RequestMapping(params = "act=14003")
	public String m14003() {
		return "forward:/automaticCheckTask/toPage.html";
	}
	
	//自动检验仓库导入页
	@RequestMapping(params = "act=140032")
	public String m140032() {
		return "forward:/automaticCheckTask/toDepotCheckImportPage.html";
	}

	//自动检验POP核对
	@RequestMapping(params = "act=14004")
	public String m14004() {
		return "forward:/automaticCheckTask/toAddPOPPage.html";
	}
	
	//自动检验POP核对
	@RequestMapping(params = "act=14005")
	public String m14005() {
		return "forward:/automaticCheckTask//toAddpopAmountPage.html";
	}
	
	// 标准成本单价预警配置列表
    @RequestMapping(params = "act=15001")
    public String m15001(){
        return "forward:/settlementPriceWarnconfig/toPage.html";
    }
	//新增
	@RequestMapping(params = "act=15002")
	public String m15002() {
		return "forward:/settlementPriceWarnconfig/toAddPage.html";
	}
	// 编辑
	@RequestMapping(params = "act=15003")
	public String m15003() {
		return "forward:/settlementPriceWarnconfig/toEditPage.html";
	}
	// 详情
	@RequestMapping(params = "act=15004")
	public String m15004() {
		return "forward:/settlementPriceWarnconfig/toDetailsPage.html";
	}

	//业财自核对
	@RequestMapping(params = "act=16001")
	public String m16001() {
		return "forward:/businessFinanceAutoVeri/toPage.html";
	}
	
	//月结自核对
	@RequestMapping(params = "act=16002")
	public String m16002() {
		return "forward:/monthlyAccountAutoVeri/toPage.html";
	}
	


	// 结算配置列表
	@RequestMapping(params = "act=17001")
	public String m17001() {
		return "forward:/settlementConfig/toPage.html";
	}
	// 结算配置表修改
	@RequestMapping(params = "act=17002")
	public String m17002(Long id) {
		return "forward:/settlementConfig/toEditPage.html?id =" + id;
	}

	// 销售目标列表
	@RequestMapping(params = "act=18001")
	public String m18001() {
		return "forward:/saleTarget/toPage.html";
	}
	
	// 销售目标列表
	@RequestMapping(params = "act=18002")
	public String m18002() {
		return "forward:/saleTarget/toImportPage.html";
	}
	
	// 销售目标导入
	@RequestMapping(params = "act=18003")
	public String m18003() {
		return "forward:/saleTarget/toDetailPage.html";
	}

	// 月度销售额目标列表
	@RequestMapping(params = "act=18004")
	public String m18004() {
		return "forward:/saleAmountTarget/toPage.html";
	}

	// 月度销售额目标导入
	@RequestMapping(params = "act=18005")
	public String m18005() {
		return "forward:/saleAmountTarget/toImportPage.html";
	}
	
	// 销售目标达成列表
	@RequestMapping(params = "act=19001")
	public String m19001() {
		return "forward:/saleTargetAchievement/toGxPage.html";
	}
	
	// 电商目标导入
	@RequestMapping(params = "act=19002")
	public String m19002() {
		return "forward:/saleTargetAchievement/toDsPage.html";
	}

	// 电商目标导入
	@RequestMapping(params = "act=19003")
	public String m19003() {
		return "forward:/saleTargetAchievement/toDpPage.html";
	}
		
	// 加权单价列表
	@RequestMapping(params = "act=20001")
	public String m20001() {
		return "forward:/weightedPrice/toPage.html";
	}
	// 加权单价列表
	@RequestMapping(params = "act=21001")
	public String m21001() {
		return "forward:/SDweightedPrice/toPage.html";
	}
	// 财务经销存累计汇总表
	@RequestMapping(params = "act=22001")
	public String m22001() {
		return "forward:/buFinanceAdd/toPage.html";
	}

	// 访问供应链周报
	@RequestMapping(params = "act=30001")
	public String m30001() {
		return "forward:/retailAdmin/toPage.html";
	}
}
