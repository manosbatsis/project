package com.topideal.order.web.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.service.bill.ToCTempReceiveBillService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * To C暂估应收表
 **/
@Controller
@RequestMapping("/tocTempReceiveBill")
public class ToCTempReceiveBillController {

    private static final Logger LOG = Logger.getLogger(ToCTempReceiveBillController.class) ;

    @Autowired
    private ToCTempReceiveBillService toCTempReceiveBillService;
    /**
     * 访问列表页面
     * @param model
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
        User user = ShiroUtils.getUser();
        List<MerchantShopRelMongo> shopList = toCTempReceiveBillService.getMerchantShopRelList(user.getMerchantId());
        model.addAttribute("shopList",shopList);
        return "derp/bill/toc-temp-receive-bill-list";
    }

    /**
     * 获取分页数据
     * @param dto
     */
    @RequestMapping("/listToCTempReceiveBill.asyn")
    @ResponseBody
    private ViewResponseBean listToCTempReceiveBill(TocTemporaryReceiveBillDTO dto) {
        try {
            User user = ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            toCTempReceiveBillService.listTocTempReceiveBillByPage(user,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 访问列表页面
     * @param model
     * */
    @RequestMapping("/toDetailsPage.html")
    public String toDetailsPage(Model model,Long id) throws Exception{
        TocTemporaryReceiveBillDTO dto = toCTempReceiveBillService.getDetails(id);
        model.addAttribute("detail", dto);
        return "derp/bill/toc-temp-receive-bill-details";
    }

    @RequestMapping("/listToCTempReceiveItem.asyn")
    @ResponseBody
    private ViewResponseBean listToCTempReceiveItem(TocTemporaryReceiveBillItemDTO dto) {
        try{
            User user=ShiroUtils.getUser();
            // 响应结果集
            dto = toCTempReceiveBillService.listToCTempReceiveItem(user,dto);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }

    @RequestMapping("/listToCTempReceiveCostItem.asyn")
    @ResponseBody
    private ViewResponseBean listToCTempReceiveCostItem(TocTemporaryReceiveBillCostItemDTO dto) {
        try{
            User user=ShiroUtils.getUser();
            // 响应结果集
            dto = toCTempReceiveBillService.listToCTempReceiveCostItem(user,dto);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 获取导出电商订单的数量
     */
    @RequestMapping("/getBillCount.asyn")
    @ResponseBody
    private ViewResponseBean getBillCount(TocTemporaryReceiveBillDTO dto) {
        Map<String,Object> data=new HashMap<String,Object>();
        try{
            User user=ShiroUtils.getUser();

            TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
            BeanUtils.copyProperties(dto, itemDTO);
            if (StringUtils.isNotBlank(dto.getIds())) {
                itemDTO.setBillIds(dto.getIds());
            }
            Integer itemNum = toCTempReceiveBillService.countTempBillNum(user,itemDTO);

            TocTemporaryReceiveBillCostItemDTO costItemDTO = new TocTemporaryReceiveBillCostItemDTO();
            BeanUtils.copyProperties(costItemDTO, itemDTO);
            if (StringUtils.isNotBlank(dto.getIds())) {
                costItemDTO.setBillIds(dto.getIds());
            }
            Integer costNum = toCTempReceiveBillService.countTempCostBillNum(user,costItemDTO);

            Integer count = itemNum + costNum;
            data.put("total",count);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(data);
    }

    /**
     * 导出
     * */
   /* @RequestMapping("/exportBill.asyn")
    private void exportBill(HttpServletResponse response, HttpServletRequest request, TocTemporaryReceiveBillDTO dto) throws Exception{
        User user = ShiroUtils.getUser();

        //表头信息
        String mainSheetName = "暂估应收列表";
        String[] mainColumns = {"公司","客户名称","平台","开店事业部","运营类型","店铺名称","应收月份","应收货款",
                "订单数量","已结算订单量","已结算应收金额","未结算订单量","剩余暂估应收金额","结算完成月份","结算状态"};
        String[] mainKeys = {"merchantName","customerName","storePlatformName","shopTypeName","shopName","buName","month","totalReceiveAmount",
                "billNum","alreadyReceiveNum","alreadyReceiveAmount", "noReceiveNum","noReceiveAmount", "settlementEndMonth", "status"};

        String itemSheetName = "暂估应收明细";
        String[] itemColumns = {"平台名称","店铺名称","应收月份","外部订单号","系统订单号","事业部","商品货号","商品名称","销售数量","暂估应收金额\n（RMB）",
                "退款金额\n（RMB）","平台结算货款\n（原币）","平台结算金额\n（RMB）","结算标识","结算日期","结算单号"};
        String[] itemKeys = {"storePlatformName","shopName","month","externalCode","orderCode","buName","goodsNo",
                "goodsName", "saleNum", "temporaryRmbAmount","refundAmount","settlementOriAmount","settlementRmbAmount",
                "settlementMark","settlementDate","settlementCode"};

        String itemCostSheetName = "暂估费用明细";
        String[] itemCostColumns = {"平台名称","店铺名称","应收月份","外部订单号","系统订单号","事业部","商品货号","商品名称","系统费项名称","平台费项名称",
                "暂估费用金额\n（RMB）","平台结算费用\n（原币）","平台结算费用\n（RMB）","核销状态","结算日期","结算单号"};
        String[] itemCostKeys = {"storePlatformName","shopName","month","externalCode","orderCode","buName","goodsNo",
                "goodsName", "projectName","platformProjectName", "temporaryRmbCost","settlementOriCost","settlementRmbCost",
                "settlementMark","settlementDate","settlementCode"};
        dto.setMerchantId(user.getMerchantId());
        List<Map<String,Object>> exportList = toCTempReceiveBillService.listForExport(dto,  user);
        //表体信息
        TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
        BeanUtils.copyProperties(dto, itemDTO);
        if (StringUtils.isNotBlank(dto.getIds())) {
            itemDTO.setBillIds(dto.getIds());
        }
        List<Map<String,Object>> itemList = toCTempReceiveBillService.listForExportTempItem(user, itemDTO);

        TocTemporaryReceiveBillCostItemDTO costItemDTO = new TocTemporaryReceiveBillCostItemDTO();
        BeanUtils.copyProperties(dto, costItemDTO);
        if (StringUtils.isNotBlank(dto.getIds())) {
            costItemDTO.setBillIds(dto.getIds());
        }
        List<Map<String,Object>> costItemList = toCTempReceiveBillService.listForExportTempCostItem(user, costItemDTO);

        //生成表格
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, exportList);
        ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);
        ExportExcelSheet costItemSheet = ExcelUtilXlsx.createSheet(itemCostSheetName, itemCostColumns, itemCostKeys, costItemList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
        sheets.add(mainSheet) ;
        sheets.add(itemSheet) ;
        sheets.add(costItemSheet) ;

        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
        //导出文件
        String fileName = "ToC暂估应收导出";
        FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
    }
*/
    /**
     * 累计暂估应收导出
     /*   * *//*
    @RequestMapping("/exportTempBill.asyn")
    private void exportTempBill(TocTemporaryReceiveBillDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception{
        User user = ShiroUtils.getUser();
        dto.setMerchantId(user.getMerchantId());
        TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
        BeanUtils.copyProperties(dto, itemDTO);
        if (StringUtils.isNotBlank(dto.getIds())) {
            itemDTO.setBillIds(dto.getIds());
        }
        itemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
        //表体信息
        List<Map<String,Object>> itemList = toCTempReceiveBillService.listForExportTempItem(user, itemDTO);

        String sheetName = "累计暂估应收";
        String[] columns = {"公司","客户","平台","店铺","应收月份","外部订单号", "事业部","商品件数","暂估应收金额","币种","结算标识"};
        String[] keys = {"merchantName","customerName","storePlatformName","shopName","month","externalCode", "buName", "saleNum",
                "temporaryRmbAmount","temporaryCurrency","settlementMark"};

        TocTemporaryReceiveBillCostItemDTO costItemDTO = new TocTemporaryReceiveBillCostItemDTO();
        BeanUtils.copyProperties(dto, costItemDTO);
        if (StringUtils.isNotBlank(dto.getIds())) {
            costItemDTO.setBillIds(dto.getIds());
        }
        costItemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
        List<Map<String,Object>> costItemList = toCTempReceiveBillService.listForExportTempCostItem(user, costItemDTO);

        String sheetCostName = "累计暂估费用";
        String[] costColumns = {"公司","客户","平台","店铺","应收月份","外部订单号", "事业部","平台费项名称", "系统费项名称","暂估费用金额","币种","结算标识"};
        String[] costKeys = {"merchantName","customerName","storePlatformName","shopName","month","externalCode", "buName", "platformProjectName",
                "projectName", "temporaryRmbCost","temporaryCurrency","settlementMark"};

        List<ExportExcelSheet> exportExcelSheets = new ArrayList<>();
        ExportExcelSheet itemSheet = new ExportExcelSheet();
        itemSheet.setColums(columns);
        itemSheet.setKeys(keys);
        itemSheet.setSheetNames(sheetName);
        itemSheet.setResultList(itemList);
        exportExcelSheets.add(itemSheet);

        ExportExcelSheet costItemSheet = new ExportExcelSheet();
        costItemSheet.setColums(costColumns);
        costItemSheet.setKeys(costKeys);
        costItemSheet.setSheetNames(sheetCostName);
        costItemSheet.setResultList(costItemList);
        exportExcelSheets.add(costItemSheet);
        //导出文件
        String fileName = "累计暂估应收导出";
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(exportExcelSheets) ;
        FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
    }
*/
    /**
     * 刷新
     */
//    @RequestMapping("/refreshBill.asyn")
//    @ResponseBody
//    private ViewResponseBean refreshBill(String ids) {
//        Map<String,Object> data=new HashMap<String,Object>();
//        try{
//            // 响应结果集
//            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
//            if (isNull) {
//                // 输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            data = toCTempReceiveBillService.refreshBills(ids);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(data);
//    }

    /**
     * 暂估汇总导出
     * */
    @RequestMapping("/exportSummaryOrder.asyn")
    private void exportSummaryOrder(TocTemporaryReceiveBillDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception{
        try {
            User user = ShiroUtils.getUser();
            dto.setMerchantId(user.getMerchantId());
            List<ExportExcelSheet> sheets = toCTempReceiveBillService.exportSummaryBill(dto, user);
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            String fileName = "ToC暂估汇总导出";
            FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
    }

}
