package com.topideal.order.web.platformdata;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.platformdata.OrealPurchaseOrderDTO;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderItemModel;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;
import com.topideal.order.service.platformdata.OrealPurchaseOrderService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 欧莱采购订单品管理
 **/
@RequestMapping("/orealPurchaseOrder")
@Controller
public class OrealPurchaseOrderController {


    @Autowired
    private OrealPurchaseOrderService orealPurchaseOrderService;

    /**
     * 访问列表页面
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {

        return "derp/platformdata/oreal-purchase-order-list";
    }

    /**
     * 采购订单详情
     * @param model
     * @param id
     * @return
     * @throws Exception
     */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		OrealPurchaseOrderDTO orderModel=new OrealPurchaseOrderDTO();
		orderModel.setId(id);
		orderModel = orealPurchaseOrderService.searchDTODetail(orderModel);
		
		OrealPurchaseOrderItemModel itemModel=new OrealPurchaseOrderItemModel();
		itemModel.setOrealPurchaseOrderId(id);
		List<OrealPurchaseOrderItemModel> itemList = orealPurchaseOrderService.getOrderItrmList(itemModel);		
		int vdef5Total=0;
		int nordernumTotal=0;
		for (OrealPurchaseOrderItemModel item : itemList) {
			Integer vdef5 = item.getVdef5();
			if (vdef5!=null) vdef5Total=vdef5Total+vdef5;
			Integer nordernum = item.getNordernum();
			if (nordernum!=null) nordernumTotal=nordernumTotal+nordernum;
		}
		model.addAttribute("detail", orderModel);
		model.addAttribute("itemList", itemList);
		model.addAttribute("vdef5Total", vdef5Total);
		model.addAttribute("nordernumTotal", nordernumTotal);
		return "/derp/platformdata/oreal-purchase-order-details";
	}
	
    
    /**
     * 获取分页数据
     */
    @RequestMapping("/listByPage.asyn")
    @ResponseBody
    private ViewResponseBean ListByPage(OrealPurchaseOrderDTO dto) {
        try {
            User user= ShiroUtils.getUser();
            dto.setMerchantId(user.getMerchantId());
            dto = orealPurchaseOrderService.getListByPage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

	
    /**
     * 导出
     * @throws IOException
     */
    @RequestMapping("/export.asyn")
    public void export(HttpServletResponse response, HttpServletRequest request,OrealPurchaseOrderDTO dto) throws Exception {
        String sheetName = "欧莱雅采购订单导出";
        String[] COLUMNS = { "订单号", "CSR号 ","订单日期", "品牌","供应商","业务类型","采购类型","收货地址","公司","事业部","数据来源","创建时间","厂商编码","经销商编码",
        		"存货名称","建议采购订单数量","CSR确认数量","建议零售价","备注"};
		String[] KEYS = {"vordercode","vdef7","dorderdate","vdef1","custname","vdef13","docname","adress","merchant_name","bu_name","source","create_date",
				"invbasdoc","cinvmecode","invname","vdef5","nordernum","refsaleprice","vmemo"};
		
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());
        // 获取导出的信息
        List<Map<String, Object>> list = orealPurchaseOrderService.getExportList(dto);
        for (Map<String, Object> map : list) {
        	String source = (String) map.get("source");
        	map.put("source",DERP_SYS.getLabelByKey(DERP_SYS.supplierMerchandise_sourceList, source));
		}
        // 生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS, list) ;
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
    }
}
