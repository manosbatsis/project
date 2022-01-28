package com.topideal.inventory;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.inventory.shiro.ShiroUtils;

/**1
 *  库存管理 菜单加载 Created by acer on 2016/8/24.
 * @author Chen
 * @since
 */
@Controller
@RequestMapping(value = "/load/page.asyn")
public class LoadPageController {

	@RequestMapping(params = "act=1001")
	@ResponseBody
	public String test(HttpSession session) {
		User user= ShiroUtils.getUser();
		return "abcd";
	}

	// 首页
	@RequestMapping(params = "act=2001")
	public String m100(HttpSession session) {
		User user= ShiroUtils.getUser();
		System.out.println(user.getId());
		return "002";
	}
 
	//库存管理-期初库存列表
    @RequestMapping(params = "bls=6001")
    public String m6001(){
        return "forward:/initInventory/toPage.html";
    }
    
    //库存管理-批次库存明细列表
    @RequestMapping(params = "bls=6002")
    public String m6002(){
        return "forward:/inventoryBatch/toPage.html";
    }
    
    //库存管理-商品收发明细列表
    @RequestMapping(params = "bls=6003")
    public String m6003(){
        return "forward:/inventoryDetails/toPage.html";
    }
    
    //库存管理-商品收发汇总列表
    @RequestMapping(params = "bls=6004")
    public String m6004(){
        return "forward:/inventorySummary/toPage.html";
    }
    
    //库存管理-月库存转结列表
    @RequestMapping(params = "bls=6005")
    public String m6005(){
        return "forward:/monthlyAccount/toPage.html";
    }
    
    //库存管理-效期预警列表
    @RequestMapping(params = "bls=6006")
    public String m6006(){
        return "forward:/inventoryWarning/toPage.html";
    }
    
    //库存管理-实时库存列表
    @RequestMapping(params = "bls=6007")
    public String m6007(){
        return "forward:/inventoryRealTime/toPage.html";
    }
    
    //库存管理-库存期初-导入页面
    @RequestMapping(params = "bls=6008")
    public String m6008(){
        return "forward:/initInventory/toImportPage.html";
    }
    
    //库存管理-库存期初-新增页面
    @RequestMapping(params = "bls=6009")
    public String m6009(){
        return "forward:/initInventory/toAddPage.html";
    }
    
   
    //库存管理-商品库存明细 -批次库存明细
    @RequestMapping(params = "bls=6010")
    public String m6010(String goodsNo,Long depotId){
        return "forward:/inventoryBatch/inventoryBatchToPage.html";
    }
    
    
    //库存管理-月库存转结-月库存转结明细
    @RequestMapping(params = "bls=6011")
    public String m6011(Long id){
        return "forward:/monthlyAccountItem/toDetailPage.html";
    }
    
    
    //库存管理-商品库存明细
    @RequestMapping(params = "bls=6012")
    public String m6012(){
        return "forward:/productInventoryDetails/toPage.html";
    }
    
    //库存管理-商品汇总-商品库存明细 
    @RequestMapping(params = "bls=6013")
    public String m6013(String goodsNo,Long depotId){
        return "forward:/productInventoryDetails/productInventoryDetailsToPage.html";
    }
    
    
    //库存管理-库存批次快照
    @RequestMapping(params = "bls=6014")
    public String m6014(){
        return "forward:/inventoryBatchSnapshot/toPage.html";
    }
    
    
    //库存管理-库存商品快照
    @RequestMapping(params = "bls=6015")
    public String m6015(){
        return "forward:/inventoryGoodsSnapshot/toPage.html";
    }
    
    
    //库存管理-月结快照
    @RequestMapping(params = "bls=6016")
    public String m6016(){
        return "forward:/monthlyAccountSnapshot/toPage.html";
    }
    
    
    
    //库存管理-商品冻结和解冻
    @RequestMapping(params = "bls=6017")
    public String m6017(){
    	return "forward:/inventoryFreezeDetails/toPage.html";
    }
    
  //库存管理-实时库存快照
    @RequestMapping(params = "bls=6018")
    public String m6018(){
    	return "forward:/inventoryRealTimeSnapshot/toPage.html";
    }
    @RequestMapping(params = "bls=6019")
    public String m6019(){
    	return "forward:/buInventory/toPage.html";
    }
	//事业部库存管理-期初库存列表
    @RequestMapping(params = "bls=6020")
    public String m6020(){
        return "forward:/buInitInventory/toPage.html";
    }
    //事业部库存管理-期初库存导入
    @RequestMapping(params = "bls=6021")
    public String m6021(){
        return "forward:/buInitInventory/toImportPage.html";
    }
}
