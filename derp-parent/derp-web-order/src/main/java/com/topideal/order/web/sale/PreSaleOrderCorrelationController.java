package com.topideal.order.web.sale;

import java.sql.SQLException;
import java.util.ArrayList;
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

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.order.service.sale.PreSaleOrderCorrelationService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * @Description: 预售勾稽明细
 * @Date: 2020/06/02 15:29
 **/
@Controller
@RequestMapping("/preSaleOrderCorrelation")
public class PreSaleOrderCorrelationController {

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
     * 访问列表页面
     * */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
        return "/derp/sale/pre-sale-order-correlation-list";
    }

    /**
     * 获取分页数据
     * */
    @RequestMapping("/listPreSaleOrderCorrelation.asyn")
    @ResponseBody
    public ViewResponseBean listPreSaleOrderCorrelation(PreSaleOrderCorrelationDTO dto) {
        try{
            User user= ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto = preSaleOrderCorrelationService.listPreSaleOrderCorrelationByPage(dto,user);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 访问详情页面
     * */
    @RequestMapping("/toDetailsPage.html")
    public String toDetailsPage(Model model, String code, String goodsNo)throws Exception{
        PreSaleOrderCorrelationDTO dto = new PreSaleOrderCorrelationDTO();
        User user = ShiroUtils.getUser();
        dto.setMerchantId(user.getMerchantId());
        dto.setGoodsNo(goodsNo);
        dto.setPreSaleOrderCode(code);
        //表头信息
        PreSaleOrderCorrelationDTO mainInfo = preSaleOrderCorrelationService.detailPreSaleOrderCorrelation(dto);
        List<PreSaleOrderCorrelationDTO> details = preSaleOrderCorrelationService.detailByCodeAndGoodsNo(dto);
        model.addAttribute("mainInfo", mainInfo);
        model.addAttribute("details", details);
        return "/derp/sale/pre-sale-order-correlation-detail";
    }

    /**
     * 根据查询条件导出
     */
    @RequestMapping("/exportPreSaleOrderCorrelation.asyn")
    public void exportPreSaleOrderCorrelation(PreSaleOrderCorrelationDTO dto, HttpServletResponse response, HttpServletRequest request) throws SQLException {
        User user= ShiroUtils.getUser();
        dto.setMerchantId(user.getMerchantId());

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
    }


}
