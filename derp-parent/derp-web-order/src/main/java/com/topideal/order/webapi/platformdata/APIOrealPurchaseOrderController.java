package com.topideal.order.webapi.platformdata;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.platformdata.OrealPurchaseOrderDTO;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderItemModel;
import com.topideal.order.service.platformdata.OrealPurchaseOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.platformdata.form.OrealPurchaseOrderForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 欧莱雅订单管理
 **/

@RestController
@RequestMapping("/webapi/order/orealPurchaseOrder")
@Api(tags = "欧莱雅采购订单管理")
public class APIOrealPurchaseOrderController {


    @Autowired
    private OrealPurchaseOrderService orealPurchaseOrderService;

    /**
     * 访问列表页面
     */
    /*@RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {

        return "derp/platformdata/oreal-purchase-order-list";
    }*/

    /**
     * 欧莱雅采购订单详情
     * @param model
     * @param id
     * @return
     * @throws Exception
     */

	@ApiOperation(value = "欧莱雅采购订单详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调整单id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id) throws Exception {
		try {
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
			Map<String, Object> resultMap=new HashMap<String, Object>();
			resultMap.put("detail", orderModel);
			resultMap.put("itemList", itemList);
			resultMap.put("vdef5Total", vdef5Total);
			resultMap.put("nordernumTotal", nordernumTotal);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	
    
    /**
     * 获取分页数据
     */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean ListByPage(OrealPurchaseOrderForm form) {
        try {
        	OrealPurchaseOrderDTO dto=new OrealPurchaseOrderDTO();
        	dto.setVordercode(form.getVordercode());
        	dto.setVdef7(form.getVdef7());
        	dto.setDorderdateStartDate(form.getDorderdateStartDate());
        	dto.setDorderdateEndDate(form.getDorderdateEndDate());
        	dto.setBuId(form.getBuId());
        	dto.setBegin(form.getBegin());
        	dto.setPageSize(form.getPageSize());

            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto = orealPurchaseOrderService.getListByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }

    }

	
    /**
     * 导出
     * @throws IOException
     */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/export.asyn")
    public void export(HttpServletResponse response, HttpServletRequest request,OrealPurchaseOrderForm form) throws Exception {
        
    	try {
    		String sheetName = "欧莱雅采购订单导出";
            String[] COLUMNS = { "订单号", "CSR号 ","订单日期", "品牌","供应商","业务类型","采购类型","收货地址","公司","事业部","数据来源","创建时间","厂商编码","经销商编码",
            		"存货名称","建议采购订单数量","CSR确认数量","建议零售价","备注"};
    		String[] KEYS = {"vordercode","vdef7","dorderdate","vdef1","custname","vdef13","docname","adress","merchant_name","bu_name","source","create_date",
    				"invbasdoc","cinvmecode","invname","vdef5","nordernum","refsaleprice","vmemo"};
    		OrealPurchaseOrderDTO dto=new OrealPurchaseOrderDTO();
    		dto.setVordercode(form.getVordercode());
        	dto.setVdef7(form.getVdef7());
        	dto.setDorderdateStartDate(form.getDorderdateStartDate());
        	dto.setDorderdateEndDate(form.getDorderdateEndDate());
        	dto.setBuId(form.getBuId());
        	
    		User user= ShiroUtils.getUserByToken(form.getToken());
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
    		//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
    	
    	
    	
    }
}
