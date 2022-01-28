package com.topideal.order.webapi.sale;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.order.service.sale.PreSaleOrderCorrelationService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.PreSaleOrderCorrelationResponseDTO;
import com.topideal.order.webapi.sale.form.PreSaleOrderCorrelationForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * webapi 预售勾稽明细
 **/
@RestController
@RequestMapping("/webapi/order/preSaleOrderCorrelation")
@Api(tags = "预售勾稽明细")
public class APIPreSaleOrderCorrelationController {

    private static final String[] MAIN_COLUMNS = {"预售单号", "PO号","商品货号", "商品条码", "标准条码", "商品名称",
            "客户", "事业部", "出仓仓库", "预售数量", "销售数量", "待销数量","出库数量","预售余量","预售单价","预售金额"} ;
    private static final String[] MAIN_KEYS = {"pre_sale_order_code","po_no","goods_no","barcode","commbarcode","goods_name","customer_name","bu_name","out_depot_name",
            "pre_num","sale_num", "pre_sale_num", "out_num", "surplus_num", "price","amount"};
    
    private static final String[] DETIAL_COLUMNS = {"预售单号", "商品货号", "商品条码", "商品名称",
            "客户", "事业部", "出仓仓库", "销售订单号", "销售数量", "销售出库单号","出库数量", "出库时间"} ;
    String[] DETIAL_KEYS = {"pre_sale_order_code","goods_no","barcode","goods_name","customer_name","bu_name","out_depot_name","sale_order_code",
            "sale_num","sale_out_depot_code","out_num","out_depot_date"};

    @Autowired
    private PreSaleOrderCorrelationService preSaleOrderCorrelationService;

    /**
     * 获取分页数据
     * */
    @ApiOperation(value = "查询预售勾稽列表信息")	
	@PostMapping(value="/listPreSaleOrderCorrelation.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PreSaleOrderCorrelationDTO> listPreSaleOrderCorrelation(PreSaleOrderCorrelationForm form) {
    	PreSaleOrderCorrelationDTO dto = new PreSaleOrderCorrelationDTO();
    	try{
            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto.setPreSaleOrderCode(form.getPreSaleOrderCode());
            dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
            dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
            dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
            dto.setGoodsNo(form.getGoodsNo());
            dto.setOutDepotStartDate(form.getOutDepotStartDate());
            dto.setOutDepotEndDate(form.getOutDepotEndDate());
            dto.setAuditStartDate(form.getAuditStartDate());
            dto.setAuditEndDate(form.getAuditEndDate());
            dto.setBarcode(form.getBarcode());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            
            dto = preSaleOrderCorrelationService.listPreSaleOrderCorrelationByPage(dto,user);
            
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    /**
     * 访问详情页面
     * */
    @ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "code", value = "预售单号", required = true),
		@ApiImplicitParam(name = "goodsNo", value = "商品货号", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PreSaleOrderCorrelationResponseDTO> toDetailsPage(String token, String code, String goodsNo)throws Exception{
    	PreSaleOrderCorrelationResponseDTO  responseDTO = new PreSaleOrderCorrelationResponseDTO();
    	try {
    		PreSaleOrderCorrelationDTO dto = new PreSaleOrderCorrelationDTO();
    		User user = ShiroUtils.getUserByToken(token);
            dto.setMerchantId(user.getMerchantId());
            dto.setGoodsNo(goodsNo);
            dto.setPreSaleOrderCode(code);
            //表头信息
            PreSaleOrderCorrelationDTO mainInfo = preSaleOrderCorrelationService.detailPreSaleOrderCorrelation(dto);
            List<PreSaleOrderCorrelationDTO> details = preSaleOrderCorrelationService.detailByCodeAndGoodsNo(dto);
            responseDTO.setMainInfo(mainInfo);
            responseDTO.setDetails(details);
    	}catch(Exception e) {
    		e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
    	}
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
    }

    /**
     * 根据查询条件导出
     */
    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportPreSaleOrderCorrelation.asyn")
    public ResponseBean exportPreSaleOrderCorrelation(PreSaleOrderCorrelationForm form, HttpServletResponse response, HttpServletRequest request) throws SQLException {
        try {
        	PreSaleOrderCorrelationDTO dto = new PreSaleOrderCorrelationDTO();
        	 User user= ShiroUtils.getUserByToken(form.getToken());
             // 设置商家id
             dto.setMerchantId(user.getMerchantId());
             dto.setPreSaleOrderCode(form.getPreSaleOrderCode());
             dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
             dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
             dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
             dto.setGoodsNo(form.getGoodsNo());
             dto.setOutDepotStartDate(form.getOutDepotStartDate());
             dto.setOutDepotEndDate(form.getOutDepotEndDate());
             dto.setAuditStartDate(form.getAuditStartDate());
             dto.setAuditEndDate(form.getAuditEndDate());
             dto.setBarcode(form.getBarcode());
             dto.setIds(form.getIds());
            

            //表头信息
            List<Map<String,Object>> listForExport = preSaleOrderCorrelationService.listForExport(dto,user);
            //表头信息
            String sheetName1 = "预售勾稽汇总表";

            //商品信息
            List<Map<String,Object>> itemList = preSaleOrderCorrelationService.detailForExport(dto,user);
            String sheetName2 = "预售勾稽明细表";
            
            //生成表格
            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, MAIN_COLUMNS, MAIN_KEYS, listForExport);
    		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, DETIAL_COLUMNS, DETIAL_KEYS, itemList);
    		
    		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
    		sheets.add(mainSheet) ;
    		sheets.add(itemSheet) ;
            
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            String fileName = "预售勾稽明细";
            FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    	
    }


}
