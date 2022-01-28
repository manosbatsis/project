package com.topideal.storage.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/load/page.asyn")
public class LoadPageController {

	// 盘点指令首页
	@RequestMapping(params = "act=9001")
	public String m9001() {
		return "forward:/takesstock/toPage.html";
	}

	// 新增盘点指令页面
	@RequestMapping(params = "act=90011")
	public String m90011() {
		return "forward:/takesstock/toAdd.html";
	}
	// 编辑盘点指令页面
	@RequestMapping(params = "act=90012")
	public String m90012() {
		return "forward:/takesstock/toEdit.html";
	}
	//盘点申请详情页面
	@RequestMapping(params = "act=90013")
	public String m90013() {
		return "forward:/takesstock/toDetailPage.html";
	}
	
	// 盘点结果列表
	@RequestMapping(params = "act=9002")
	public String m9002() {
		return "forward:/takesstockresult/toPage.html";
	}
	// 盘点结果详情
	@RequestMapping(params = "act=90021")
	public String m90021() {
		return "forward:/takesstockresult/toDetailPage.html";
	}
	// 盘点结果导入
	@RequestMapping(params = "act=90022")
	public String m90022() {
		return "forward:/takesstockresult/toImportPage.html";
	}

	// 仓储类型调整
	@RequestMapping(params = "act=10001")
	public String m10001() {
		return "forward:/adjustmentType/toPage.html";
	}

	// 仓储类型调整-详情
	@RequestMapping(params = "act=100011")
	public String m100011(Long id) {
		return "forward:/adjustmentType/toDetailsPage.html?id="+id;
	}
	
	// 仓储类型调整-导入页面
	@RequestMapping(params = "act=100012")
	public String m100012() {
		return "forward:/adjustmentType/toImportPage.html";
	}

	// 仓储库存调整
	@RequestMapping(params = "act=10002")
	public String m10002() {
		return "forward:/adjustmentInventory/toPage.html";
	}

	// 仓储库存调整-导入页面
	@RequestMapping(params = "act=100021")
	public String m100021() {
		return "forward:/adjustmentInventory/toImportPage.html";
	}
	
	// 仓储库存调整-详情页面
	@RequestMapping(params = "act=100022")
	public String m100022() {
		return "forward:/adjustmentInventory/toDetailsPage.html";
	}

}
