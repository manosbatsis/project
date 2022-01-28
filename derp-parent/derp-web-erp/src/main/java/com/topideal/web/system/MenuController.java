package com.topideal.web.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 卖家  买家菜单加载
 * Created by acer on 2016/8/24.
 * @author Chen
 * @since 
 */
@Controller
@RequestMapping(value="/list/menu.asyn")
public class MenuController {

    //首页
    @RequestMapping(params ="act=100")
    public String m100(){
        return "forward:/home/toPage.html";
    }

    //用户列表
    @RequestMapping(params = "act=101")
    public String m101(){
        return "forward:/user/toPage.html";
    }

    //角色管理
    @RequestMapping(params = "act=102")
    public String m102(){
        return "forward:/role/toPage.html";
    }

    //角色管理
    @RequestMapping(params = "act=103")
    public String m103(){
        return "forward:/permission/toPage.html";
    }
    
    //商品列表
    @RequestMapping(params = "act=201")
    public String m201(){
        return "forward:/merchandise/toPage.html";
    }
    
    //产品列表
    @RequestMapping(params = "act=202")
    public String m202(){
        return "forward:/product/toPage.html";
    }
    //供应商商品列表
    @RequestMapping(params = "act=203")
    public String m203(){
        return "forward:/supplierMerchandise/toPage.html";
    }
    
    //客户列表
    @RequestMapping(params = "act=301")
    public String m301(){
        return "forward:/customer/toPage.html";
    }
    
  //客户(主数据)列表
    @RequestMapping(params = "act=305")
    public String m305(){
        return "forward:/customerMain/toPage.html";
    }
    
    //采销报价管理
    @RequestMapping(params = "act=302")
    public String m302(){
        return "forward:/mining/toPage.html";
    }
    
    //销售价格管理
    @RequestMapping(params = "act=303")
    public String m303(){
        return "forward:/customerSale/toPage.html";
    }
    
  //采购执行佣金配置
    @RequestMapping(params = "act=304")
    public String m304(){
        return "forward:/purchaseCommission/toPage.html";
    }
    
    //供应商列表
    @RequestMapping(params = "act=401")
    public String m401(){
        return "forward:/supplier/toPage.html";
    }
    //供应商商品价目表列表
    @RequestMapping(params = "act=402")
    public String m402(){
        return "forward:/supplierMerchandisePrice/toPage.html";
    }
  //供应商商品价目表列表
    @RequestMapping(params = "act=403")
    public String m403(){
        return "forward:/supplierInquiryPool/toPage.html";
    }
    //仓库列表
    @RequestMapping(params = "act=501")
    public String m501(){
        return "forward:/depot/toPage.html";
    }
    
    //库存管理-期初库存列表
    @RequestMapping(params = "act=601")
    public String m601(){
        return "forward:/initInventory/toPage.html";
    }
    
    //库存管理-批次库存明细列表
    @RequestMapping(params = "act=602")
    public String m602(){
        return "forward:/inventoryBatch/toPage.html";
    }
    
    //库存管理-商品收发明细列表
    @RequestMapping(params = "act=603")
    public String m603(){
        return "forward:/inventoryDetails/toPage.html";
    }
    
    //库存管理-商品收发汇总列表
    @RequestMapping(params = "act=604")
    public String m604(){
        return "forward:/inventorySummary/toPage.html";
    }
    
    //库存管理-月库存转结列表
    @RequestMapping(params = "act=605")
    public String m605(){
        return "forward:/monthlyAccount/toPage.html";
    }
    
    //库存管理-效期预警列表
    @RequestMapping(params = "act=606")
    public String m606(){
        return "forward:/inventoryWarning/toPage.html";
    }
    
    //库存管理-实时库存列表
    @RequestMapping(params = "act=607")
    public String m607(){
        return "forward:/inventoryRealTime/toPage.html";
    }
    
    //采购列表
    @RequestMapping(params = "act=701")
    public String m701(){
        return "forward:/purchase/toPage.html";
    }
    //理货单列表
    @RequestMapping(params = "act=702")
    public String m702(){
        return "forward:/tallying/toPage.html";
    }
    //采购入库列表
    @RequestMapping(params = "act=703")
    public String m703(){
        return "forward:/warehouse/toPage.html";
    }
    //预申报单列表
    @RequestMapping(params = "act=704")
    public String m704(){
        return "forward:/declare/toPage.html";
    }
    
    //预申报单列表
    @RequestMapping(params = "act=705")
    public String m705(){
        return "forward:/difference/toPage.html";
    }
    
    //调拨单列表
    @RequestMapping(params = "act=801")
    public String m801(){
        return "forward:/transfer/toPage.html";
    }
    //调拨理货列表
    @RequestMapping(params = "act=802")
    public String m802(){
        return "forward:/tallying/toPageTransfer.html";
    }
   //调拨出库列表
    @RequestMapping(params = "act=803")
    public String m803(){
        return "forward:/transferOut/toPage.html";
    }
    //调拨入库列表
    @RequestMapping(params = "act=804")
    public String m804(){
        return "forward:/transferIn/toPage.html";
    }
    //调出调入流水
    @RequestMapping(params = "act=805")
    public String m805(){
        return "forward:/transferOutIn/toPage.html";
    }
    //销售订单列表
    @RequestMapping(params = "act=901")
    public String m901(){
        return "forward:/sale/toPage.html";
    }
    //电商订单列表
    @RequestMapping(params = "act=902")
    public String m902(){
        return "forward:/order/toPage.html";
    }
    //销售出库清单列表
    @RequestMapping(params = "act=903")
    public String m903(){
        return "forward:/saleOut/toPage.html";
    }
    //销售出库差异分析表
    @RequestMapping(params = "act=904")
    public String m904(){
        return "forward:/saleAnalysis/toPage.html";
    }
    //枚举值列表
    @RequestMapping(params = "act=1001")
    public String m1001(){
        return "forward:/enum/toPage.html";
    }
    
    //公司列表
    @RequestMapping(params = "act=1101")
    public String m1101(){
        return "forward:/merchant/toPage.html";
    }
    //公司店铺列表
    @RequestMapping(params = "act=1102")
    public String m1102(){
        return "forward:/merchantShop/toPage.html";
    }
    //公司事业部列表
    @RequestMapping(params = "act=1103")
    public String m1103(){
        return "forward:/merchantBuRel/toPage.html";
    }
    
    //接口配置列表
    @RequestMapping(params = "act=1201")
    public String m1201(){
        return "forward:/api/toPage.html";
    }
    //对外接口配置列表
    @RequestMapping(params = "act=1202")
    public String m1202(){
        return "forward:/apiExternal/toPage.html";
    }
    
    
    //接口配置列表
    @RequestMapping(params = "act=1301")
    public String m1301(){
        return "forward:/external/toPage.html";
    }
   /* //销售退货订单列表
    @RequestMapping(params = "act=1401")
    public String m1401(){
        return "forward:/saleReturn/toPage.html";
    }
    //销售退货订单列表
    @RequestMapping(params = "act=1402")
    public String m1402(){
        return "forward:/saleReturnIdepot/toPage.html";
    }
    //销售退货订单列表
    @RequestMapping(params = "act=1403")
    public String m1403(){
        return "forward:/saleReturnOdepot/toPage.html";
    }*/
    
    //费率管理
    @RequestMapping(params = "act=1405")
    public String m1405(){
        return "forward:/rate/toPage.html";
    }
    //费率配置管理
    @RequestMapping(params = "act=1406")
    public String m1406(){
        return "forward:/rateConfig/toPage.html";
    }
    
    //爬虫配置列表
    @RequestMapping(params = "act=1501")
    public String m1501(){
        return "forward:/reptile/toPage.html";
    }
    
    //云集仓库对照表
    @RequestMapping(params = "act=1502")
    public String m1502(){
    	return "forward:/reptile/toPage.html";
    }
    
    // 库位映射表
    @RequestMapping(params = "act=1503")
    public String m1503(){
    	return "forward:/inventoryLocationMapping/toPage.html";
    }
    
    // 库位映射表
    @RequestMapping(params = "act=15031")
    public String m1504(){
    	return "forward:/inventoryLocationMapping/toImportPage.html";
    }
    
    //品牌列表
    @RequestMapping(params = "act=1601")
    public String m1601(){
        return "forward:/brand/toPage.html";
    }
    
    //标准品牌列表
    @RequestMapping(params = "act=1602")
    public String m1602(){
        return "forward:/brandParent/toPage.html";
    }
    
    //标准品牌导入
    @RequestMapping(params = "act=16021")
    public String m16021(){
        return "forward:/brandParent/toImportPage.html";
    }
    
  //关联品牌列表
    @RequestMapping(params = "act=1701")
    public String m1701(){
        return "forward:/settlementPrice/toPage.html";
    }
    
    @RequestMapping(params = "act=1801")
    public String m1801(){
        return "forward:/email/toPage.html";
    }
    
    //事业部列表
    @RequestMapping(params = "act=1901")
    public String m1901(){
        return "forward:/businessUnit/toPage.html";
    }
    
    //标准条码列表
    @RequestMapping(params = "act=2001")
    public String m2001(){
        return "forward:/commbarcode/toPage.html";
    }
    
    //标准条码导入
    @RequestMapping(params = "act=20011")
    public String m20011(){
        return "forward:/commbarcode/toImportPage.html";
    }
    
    //标准条码详情
    @RequestMapping(params = "act=20012")
    public String m20012(){
        return "forward:/commbarcode/toDetailPage.html";
    }
    
    //组码列表
    @RequestMapping(params = "act=2002")
    public String m2002(){
        return "forward:/groupCommbarcode/toPage.html";
    }
    
  //组码列表
    @RequestMapping(params = "act=3001")
    public String m3001(){
        return "forward:/notice/toPage.html";
    }

}
