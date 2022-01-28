package com.topideal.order.webapi.bill;

import com.spire.xls.FileFormat;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.order.service.bill.AdvancePaymentBillService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.tools.pdf.PdfUtils;
import com.topideal.order.webapi.bill.form.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 预付账单
 */
@RestController
@RequestMapping("/webapi/order/advancePaymentBill")
@Api(tags = "预付账单")
public class APIAdvancePaymentBillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIAdvancePaymentBillController.class);

    @Autowired
    private AdvancePaymentBillService advancePaymentBillService;

    /**
     * 获取分页数据
     * @return
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listAdvancePaymentBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<AdvancePaymentBillDTO> listAdvancePaymentBill(AdvancePaymentBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            AdvancePaymentBillDTO dto = new AdvancePaymentBillDTO() ;

            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            dto = advancePaymentBillService.listAdvancePaymentBill(dto, user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        }catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取采购单据单据")
    @PostMapping(value = "/listPurchaseOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<AdvanceBillDataDTO> listPurchaseOrder(AdvancePaymentBillSearchForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            String codes = form.getCodes();

            if(StringUtils.isNotBlank(codes)){

                if(codes.contains("&")){
                    codes = codes.replaceAll("&", ",") ;
                }

                if(codes.contains("\r\n")){
                    codes = codes.replaceAll("\r\n", ",") ;
                }else if(codes.contains("\r")){
                    codes = codes.replaceAll("\r", ",") ;
                }else if(codes.contains("\n")){
                    codes = codes.replaceAll("\n", ",") ;
                }


            }

            String poNos = form.getPoNos();

            if(StringUtils.isNotBlank(poNos)){

                if(poNos.contains("&")){
                    poNos = poNos.replaceAll("&", ",") ;
                }

                if(poNos.contains("\r\n")){
                    poNos = poNos.replaceAll("\r\n", ",") ;
                }else if(poNos.contains("\r")){
                    poNos = poNos.replaceAll("\r", ",") ;
                }else if(poNos.contains("\n")){
                    poNos = poNos.replaceAll("\n", ",") ;
                }


            }

            PurchaseOrderDTO dto = new PurchaseOrderDTO() ;
            dto.setPoNos(poNos);
            dto.setCodes(codes);
            dto.setMerchantId(user.getMerchantId());
            dto.setSupplierId(form.getSupplierId());
            dto.setBuId(form.getBuId());
            dto.setCurrency(form.getCurrency());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());

            dto = advancePaymentBillService.listPurchaseOrderPage(dto, user) ;

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "校验是否是同供应商+同事业部+同币种")
    @PostMapping(value = "/checkData.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "采购订单ids，多个以‘,’隔开", required = true),
    })
    public ResponseBean<String> checkData(String token, String ids) {
        try {

            boolean isEmpty = new EmptyCheckUtils().addObject(token).addObject(ids).empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            List<Long> idList = StrUtils.parseIds(ids);

            advancePaymentBillService.getCheckData(idList) ;

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, true);//未知异常

        }   catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        }   catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @SuppressWarnings("unchecked")
	@ApiOperation(value = "跳转新增页面获取数据")
    @PostMapping(value = "/getAddPageInfo.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "采购订单ids，多个以‘,’隔开", required = true),
    })
    public ResponseBean<AdvancePaymentBillDTO> getAddPageInfo(String token, String ids) {
        try {

            boolean isEmpty = new EmptyCheckUtils().addObject(token).addObject(ids).empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            List<Long> idList = StrUtils.parseIds(ids);

            AdvancePaymentBillDTO dto = advancePaymentBillService.getAddPageInfo(idList) ;

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

        }   catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        }   catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 保存预收账单
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "保存或修改预付账单")
    @PostMapping(value = "/saveOrModifyAdvancePaymentBill.asyn")
    public ResponseBean<String> saveOrModifyAdvancePaymentBill(@RequestBody AdvancePaymentBillAddForm form) {
        try {

            User user = ShiroUtils.getUserByToken(form.getToken());

            AdvancePaymentBillDTO dto = new AdvancePaymentBillDTO() ;
            BeanUtils.copyProperties(form, dto);

            Timestamp expectedPaymentDate = TimeUtils.parse(form.getExpectedPaymentDateStr(), "yyyy-MM-dd");
            dto.setExpectedPaymentDate(expectedPaymentDate);

            Long billId = advancePaymentBillService.saveOrModifyAdvancePaymentBill(dto, user);

            Map<String, Object> returnMap = new HashMap<String, Object>() ;
            
            returnMap.put("billId", billId) ;
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, returnMap);//成功

        }   catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        }    catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    /**
     * 提交作废申请
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "提交作废申请")
    @PostMapping(value = "/submitInvalidBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "预收账单id,多个以','隔开", required = true),
            @ApiImplicitParam(name = "invalidRemark", value = "备注", required = true)})
    public ResponseBean<String> submitInvalidBill(String ids, String invalidRemark, String token) {
        try {
        	
        	boolean isEmpty = new EmptyCheckUtils().addObject(ids)
                    .addObject(token)
                    .addObject(invalidRemark)
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
            List<Long> list = StrUtils.parseIds(ids);
            User user = ShiroUtils.getUserByToken(token);
            
            advancePaymentBillService.submitInvalidBill(list, invalidRemark, user);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        }  catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 查看详情
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "根据id查看详情")
    @PostMapping(value = "/detail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预付账单id", required = true)})
    public ResponseBean<AdvancePaymentBillDTO> detail(long id, String token) {
        try {
        	
        	boolean isEmpty = new EmptyCheckUtils()
                    .addObject(token)
                    .addObject(id)
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
            AdvancePaymentBillDTO dto = advancePaymentBillService.getDetail(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    /**
     * 根据id批量删除预收账单
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "根据id批量删除预收账单")
    @PostMapping(value = "/delete.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "预付账单id,多个以','隔开", required = true)})
    public ResponseBean<String> delete(String ids, String token) {
        try {
        	
            List<Long> list = StrUtils.parseIds(ids);
            User user = ShiroUtils.getUserByToken(token);
            
            advancePaymentBillService.delAdvanceBill(user, list);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
                
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    /**
     * 付款
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "付款")
    @PostMapping(value = "/payment.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<String> payment(AdvancePaymentRecordItemForm form) {
        try {
        	
        	boolean isEmpty = new EmptyCheckUtils()
        			.addObject(form.getToken())
        			.addObject(form.getPaymentAmount())
        			.addObject(form.getPaymentDateStr())
        			.empty();
        	
        	if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
        	User user = ShiroUtils.getUserByToken(form.getToken());
        	
        	AdvancePaymentRecordItemDTO dto = new AdvancePaymentRecordItemDTO() ;
        	
        	BeanUtils.copyProperties(form, dto);
        	
        	dto.setPaymentDate(TimeUtils.parse(form.getPaymentDateStr(), "yyyy-MM-dd"));
        	dto.setCreatorId(user.getId());
        	dto.setCreatorName(user.getName());
        	
            advancePaymentBillService.savePayment(dto, user);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
                
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    


    /**
     * 审核
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "审核")
    @PostMapping(value = "/auditAdvancePayment.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
        @ApiImplicitParam(name = "id", value = "预收账单id", required = true),
        @ApiImplicitParam(name = "invalidRemark", value = "备注", required = true),
        @ApiImplicitParam(name = "isPassed", value = "是否通过标识 1-通过 0-驳回", required = true)})
    public ResponseBean<String> auditAdvancePayment(Long id, String invalidRemark, 
    		String token, String isPassed) {
        try {
        	
        	boolean isEmpty = new EmptyCheckUtils()
        			.addObject(id)
        			.addObject(invalidRemark)
        			.addObject(token)
        			.addObject(isPassed)
        			.empty();
        	
        	if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
        	if(!("1".equals(isPassed) || 
        			("0".equals(isPassed)))) {
        		throw new DerpException("是否通过标识必须为 1-通过 0-驳回") ;
        	}
        	
        	User user = ShiroUtils.getUserByToken(token);
        	
            advancePaymentBillService.auditAdvancePayment(user, id, invalidRemark, isPassed);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 编辑提交，修改选择审批方式
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "编辑提交，修改选择审批方式")
    @PostMapping(value = "/modifyAuditMethod.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true),
    		@ApiImplicitParam(name = "auditMethod", value = "审批方式 1-OA审批 2-经分销审批", required = true)
    })
    public ResponseBean<String> modifyAuditMethod(Long id, String token, String auditMethod) {
        try {
            
        	boolean isEmpty = new EmptyCheckUtils()
        			.addObject(token)
        			.addObject(auditMethod)
        			.addObject(id)
        			.empty();
        	
        	if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
        	if(!(DERP_ORDER.ADVANCE_PAYMENT_BILL_AUDIT_METHOD_1.equals(auditMethod)
    				|| DERP_ORDER.ADVANCE_PAYMENT_BILL_AUDIT_METHOD_2.equals(auditMethod))) {
    			throw new DerpException("审批方式必须为 1-OA审批 2-经分销审批") ;
    		}
        	
        	User user = ShiroUtils.getUserByToken(token);
        	
        	advancePaymentBillService.modifyAuditMethod(user, id, auditMethod);
            
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    /**
     * 获取付款明细
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "获取付款明细")
    @PostMapping(value = "/getPaymentItems.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    public ResponseBean<AdvanceBillVerifyItemDTO> getPaymentItems(Long id, String token) {
        try {
        	
            List<AdvancePaymentBillItemDTO> itemListDTO = advancePaymentBillService.getPaymentItems(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, itemListDTO);//成功
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取操作日志记录")
    @PostMapping(value = "/listOperateLog.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    private ResponseBean<OperateLogDTO> listOperateLog(String token,Long id) {
        try {
        	
            List<OperateLogDTO> dtoList = advancePaymentBillService.listOperateLog(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dtoList);//成功
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取付款记录")
    @PostMapping(value = "/getRecordItemList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    private ResponseBean<AdvancePaymentRecordItemDTO> getRecordItemList(String token,Long id) {
        try {
        	
            List<AdvancePaymentRecordItemDTO> dtoList = advancePaymentBillService.getRecordItemList(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dtoList);//成功
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取打印信息")
    @PostMapping(value = "/getPrintingInfo.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    private ResponseBean<AdvancePaymentBillDTO> getPrintingInfo(String token,Long id) {
        try {
        	
        	AdvancePaymentBillDTO detail = advancePaymentBillService.getDetail(id);
        	
        	detail = advancePaymentBillService.changeMerchantInfo(detail) ;
        	
        	List<OperateLogDTO> dtoList = advancePaymentBillService.listOperateLog(id);
        	
        	dtoList = dtoList.stream().filter(dto -> (dto.getOperateAction().contains("审核"))
        			&& !"提交审核".equals(dto.getOperateAction()))
					.collect(Collectors.toList()) ;
        	
        	String poNos = advancePaymentBillService.getDetailPoNo(id) ;
        	
        	detail.setPoNos(poNos);
        	
        	detail.setOperateList(dtoList);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, detail);//成功
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    
    @ApiOperation(value = "导出excel")
    @GetMapping(value = "/exportExcel.asyn")
    public void exportExcel(AdvancePaymentBillForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	String token = form.getToken();
    	User user = ShiroUtils.getUserByToken(token);
    	
    	AdvancePaymentBillExportDTO exportDTO = new AdvancePaymentBillExportDTO() ;
    	BeanUtils.copyProperties(form, exportDTO);
    	
    	exportDTO.setMerchantId(user.getMerchantId());
    	
    	List<AdvancePaymentBillExportDTO> exportList = advancePaymentBillService.exportExcel(exportDTO, user);
    	List<AdvancePaymentBillItemExportDTO> itemList = new ArrayList<AdvancePaymentBillItemExportDTO>();
    	List<AdvancePaymentRecordItemExportDTO> recordItemList = new ArrayList<AdvancePaymentRecordItemExportDTO>();
    	
    	String[] mainCols = {"预付账单号", "商家名称", "事业部名称", "供应商名称", "供应商银行账号", "供应商银行账户", 
    			"供应商开户银行", "Swift Code", "供应商开户行地址", "结算币种", "预计付款日期", "请款原因", "采购金额", 
    			"预付总额", "已付总额"} ;
    	
    	String[] mainKeys = {"code", "merchantName", "buName", "supplierName", "bankAccount", "beneficiaryName",
    			"depositBank", "swiftCode", "bankAddress", "currency", "expectedPaymentDate", "paymentReason", "purchaseAmount",
    			"prepaymentAmount", "paymentAmount"} ;
    	
    	String[] itemCols = {"预付账单号", "采购单号", "费项名称", "父级费项名称", "申请付款金额", "采购金额", "币种"} ;
    	
    	String[] itemKeys = {"advancePaymentCode", "purchaseCode", "projectName", "parentProjectName", "prepaymentAmount", "purchaseAmount","currency"} ;
    	
    	String[] recordCols = {"预付账单号", "本次付款日期", "本次付款金额", "付款流水号", "核销单号"} ;
    	
    	String[] recordKeys = {"advancePaymentCode", "paymentDate", "paymentAmount", "serialNo", "verificationNo"} ;
            
        for (AdvancePaymentBillExportDTO dto : exportList) {
			
        	List<AdvancePaymentBillItemExportDTO> tempItemList = dto.getItemList();
        	List<AdvancePaymentRecordItemExportDTO> tempRecordItemList = dto.getRecordItemList();
        	
        	itemList.addAll(tempItemList) ;
        	recordItemList.addAll(tempRecordItemList) ;
        	
		}
        
        List<ExportExcelSheet> sheets = new ArrayList<>() ;
        
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("预付账单", mainCols, mainKeys, exportList);
        ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("预付账单付款明细", itemCols, itemKeys, itemList);
        ExportExcelSheet recordSheet = ExcelUtilXlsx.createSheet("预付账单付款记录", recordCols, recordKeys, recordItemList);
        
        sheets.add(mainSheet) ;
        sheets.add(itemSheet) ;
        sheets.add(recordSheet) ;
        
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
        
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, "预付账单");
    }
    
    @ApiOperation(value = "导出PDF")
    @GetMapping(value = "/exportPDF.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
        @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    public void exportPDF(Long id, HttpServletRequest request, HttpServletResponse response) {
    	
    	try {
			if(id == null) {
				return ;
			}
			
			AdvancePaymentBillDTO detail = advancePaymentBillService.getDetail(id);
			
			detail = advancePaymentBillService.changeMerchantInfo(detail) ;
			
			List<OperateLogDTO> dtoList = advancePaymentBillService.listOperateLog(id);
			
			if(detail.getCreateDate() != null) {
				detail.setCreateDateStr(TimeUtils.format(detail.getCreateDate(), "yyyy-MM-dd"));
			}
			
			dtoList = dtoList.stream().filter(dto -> (dto.getOperateAction().contains("审核"))
        			&& !"提交审核".equals(dto.getOperateAction()))
					.collect(Collectors.toList()) ;
			
			String poNos = advancePaymentBillService.getDetailPoNo(id) ;
			
			detail.setPoNos(poNos);
			
			detail.setOperateList(dtoList);
			
			Workbook workbook = DownloadExcelUtil.exportTemplateExcel("ADVANCEPAYMENTBILLTEMP", detail);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			byte[] fileBytes = bos.toByteArray();

			InputStream is = new ByteArrayInputStream(fileBytes);

			String basePath = ApolloUtilDB.orderBasepath+"/tempFile/"+detail.getMerchantId();
			
			File baseFile = new File(basePath) ;
			if(!baseFile.exists()) {
				baseFile.mkdirs() ;
			}
			
			String tempFilePath = basePath + File.separator + detail.getCode() + ".xlsx" ;
			String tempPDFFilePath = basePath + File.separator + detail.getCode() + ".pdf" ;
			File tempFile = new File(tempFilePath) ;
			FileOutputStream fos = new FileOutputStream(tempFile) ;

			byte[] buffer = new byte[1024];
			int ch = 0;
			while ((ch = is.read(buffer)) != -1) {
				fos.write(buffer, 0, ch);
			}

			fos.close();
			is.close();
			
			com.spire.xls.Workbook wb = new com.spire.xls.Workbook() ;
			wb.loadFromFile(tempFilePath);
			wb.setCustomFontFilePaths(new String[] {PdfUtils.PATH + "SIMSUN.TTC"});
			wb.saveToFile(tempPDFFilePath, FileFormat.PDF);
			
			File file = new File(tempPDFFilePath);
			FileInputStream input = new FileInputStream(file);
			
			FileExportUtil.setHeader(response, request, detail.getCode() + ".pdf");
			response.setContentType("application/pdf");
			response.setContentLength(input.available());
			
			ServletOutputStream out = response.getOutputStream();
			
			byte[] b = new byte[input.available()];
			while ((input.read(b)) != -1) {
			    out.write(b);
			}
			
			out.flush();
			input.close();
			
			tempFile.delete() ;
			file.delete() ;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		}
    }
}