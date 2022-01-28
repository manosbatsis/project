package com.topideal.order.webapi.bill;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.sql.visitor.functions.If;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillDataDTO;
import com.topideal.entity.dto.bill.AdvanceBillDatasDTO;
import com.topideal.entity.dto.bill.AdvanceBillOperateItemDTO;
import com.topideal.entity.dto.bill.AdvanceBillVerifyItemDTO;
import com.topideal.entity.dto.bill.ReceiveBillToNCDTO;
import com.topideal.order.service.bill.AdvanceBillOperateItemService;
import com.topideal.order.service.bill.AdvanceBillService;
import com.topideal.order.service.bill.AdvanceBillVerifyItemService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.AdvanceAuditForm;
import com.topideal.order.webapi.bill.form.AdvanceBillCheckForm;
import com.topideal.order.webapi.bill.form.AdvanceBillForm;
import com.topideal.order.webapi.bill.form.AdvanceBillSaveForm;
import com.topideal.order.webapi.bill.form.AdvanceBillSearchForm;
import com.topideal.order.webapi.bill.form.AdvanceBillVerifyItemForm;
import com.topideal.order.webapi.bill.form.InvoiceTempForm;
import com.topideal.order.webapi.bill.form.InvoiceTemplateYSForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 预收账单
 */
@RestController
@RequestMapping("/webapi/order/advanceBill")
@Api(tags = "预收账单")
public class APIAdvanceBillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIAdvanceBillController.class);

    @Autowired
    private AdvanceBillService advanceBillService;
    @Autowired
    private AdvanceBillVerifyItemService advanceBillVerifyItemService;
    @Autowired
    private AdvanceBillOperateItemService advanceBillOperateItemService;

    
    @ApiOperation(value = "生成发票")
    @PostMapping(value = "/saveContract.asyn")
    public ResponseBean saveContract(@RequestBody InvoiceTemplateYSForm templateForm) {
        try {
            if (StringUtils.isBlank(templateForm.getIds()) ||
                    StringUtils.isBlank(templateForm.getCodes())) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "关联预收单不能为空");
            }

            if (StringUtils.isBlank(String.valueOf(templateForm.getFileTempId()))) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "关联发票模板不能为空");
            }

            User user = ShiroUtils.getUserByToken(templateForm.getToken());

            advanceBillService.saveContract(user, templateForm);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), e.getMessage());
        }
    }
    
    @ApiOperation(value = "预收账单开票页面")
    @PostMapping(value = "/toInvoicePage.asyn")
    public ResponseBean toInvoicePage(InvoiceTempForm form) {

        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            //返回发票模板的html
            String excelHtml = advanceBillService.generateInvoiceHtml(form.getTempId(), form.getIds(), form.getInvoiceStatus(), user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, excelHtml);

        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }
    
    @ApiOperation(value = "校验预售单是否满足开票条件")
    @PostMapping(value = "/validateInfo.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "预收账单id，多个则以英文逗号隔开", required = true)})
    public ResponseBean validateInfo(String token, String ids) {
        try {
        	if (StringUtils.isBlank(ids)) {
        		return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请勾选预收账单");
			}
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请勾选预收账单");
            }
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = advanceBillService.validate(list);
            String code = resultMap.get("code");
            String message = resultMap.get("msg");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), e.getMessage());
        }
    }
    
    
	/**
	 * 预售单导出
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportAdvanceBill.asyn")
	private ResponseBean exportAdvanceBill(AdvanceBillForm form,HttpServletResponse response, HttpServletRequest request) throws Exception{
		try {
			  	User user = ShiroUtils.getUserByToken(form.getToken());
	            AdvanceBillDTO dto=new AdvanceBillDTO();
	            dto.setBillMonth(form.getBillMonth());
	            dto.setBillStatus(form.getBillStatus());
	            dto.setCustomerId(form.getCustomerId());
	            dto.setCode(form.getCode());
	            dto.setNcStatus(form.getNcStatus());
	            dto.setMerchantId(user.getMerchantId());
	            dto.setBuId(form.getBuId());
	            dto.setInvoiceStatus(form.getInvoiceStatus());
	            dto.setIds(form.getIds());
	        	Map<String, Object> rusultMap = advanceBillService.exportAdvanceBill(user,dto);
	        	//预收账单头
	        	List<Map<String, Object>> advanceBillList = (List<Map<String, Object>>) rusultMap.get("advanceBillList");
	        	for (Map<String, Object> map : advanceBillList) {
	        		String bill_status = (String) map.get("bill_status");
	        		String billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_billStatusList, bill_status);
	        		String invoice_status = (String) map.get("invoice_status");
	        		String invoiceStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_invoiceStatusList, invoice_status);
	        		map.put("bill_status", billStatusLabel);
	        		map.put("invoice_status", invoiceStatusLabel);
				}
	        	
	        	//预售账单体
	        	List<Map<String, Object>> advanceBillItemList = (List<Map<String, Object>>) rusultMap.get("advanceBillItemList");
	        	String[] columns1={"公司","事业部","客户名称","预收账单号","结算币种","预收总额","已收总额","待核销金额","账单状态","账单月份","开票状态","创建时间"};
	        	String[] keys1={"merchant_name","bu_name","customer_name","code","currency","sumAmount","advanceVerifyPrice","receiveVerifyPrice","bill_status","billMonth","invoice_status","create_date"};       	
	        	String sheetName1 = "预收列表" ;	        	
	        	ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, advanceBillList);
	         	String[] columns2={"预收账单号","结算费项","销售订单号","PO号","金额","币种"};
	        	String[] keys2={"code","project_name","rel_code","po_no","amount","currency"};
	        	String sheetName2 = "预收明细" ;
	        	ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, advanceBillItemList);	        	
	        	List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
	    		sheets.add(mainSheet) ;
	    		sheets.add(itemSheet) ;
	        	
	    		//生成表格
	    		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
	        	//导出文件
	        	FileExportUtil.export2007ExcelFile(wb, response, request, "预收导出");
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
    
    /**
     * 获取分页数据
     * @return
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listAdvanceBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<AdvanceBillDTO> listAdvanceBill(AdvanceBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            AdvanceBillDTO dto=new AdvanceBillDTO();
            dto.setBillMonth(form.getBillMonth());
            dto.setBillStatus(form.getBillStatus());
            dto.setCustomerId(form.getCustomerId());
            dto.setCode(form.getCode());
            dto.setNcStatus(form.getNcStatus());
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            dto.setBuId(form.getBuId());
            dto.setInvoiceStatus(form.getInvoiceStatus());
            dto = advanceBillService.listAdvanceBillByPage(user, dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取业务单据")
    @PostMapping(value = "/listAddBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<AdvanceBillDataDTO> listAddBill(AdvanceBillSearchForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            AdvanceBillDatasDTO dto=new AdvanceBillDatasDTO();
            dto.setCustomerId(form.getCustomerId());
            dto.setMerchantId(user.getMerchantId());
            dto.setCurrency(form.getCurrency());
            dto.setRelCode(form.getRelCodeStr());
            dto.setPoNo(form.getPoNoStr());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            dto.setBuId(form.getBuId());
            AdvanceBillDataDTO dataDTO = advanceBillService.listAddOrderByPage(user, dto,form.getOrderType());
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dataDTO);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "校验是否是同客户+同事业部+同币种")
    @PostMapping(value = "/checkData.asyn")
    private ResponseBean checkData(@RequestBody AdvanceBillCheckForm form) {
        AdvanceBillDTO dto = new AdvanceBillDTO();
        try {
            Map<String, Object> map = advanceBillService.checkBuMerchantCurrency(form.getList());
            String code = (String) map.get("code");
            if ("00".equals(code)) {
                AdvanceBillSaveForm formData = (AdvanceBillSaveForm) map.get("formData");
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, formData);//成功
            } else {
                String message = (String) map.get("message");
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 保存预收账单
     * @return
     */
    @ApiOperation(value = "保存或修改提交预收账单")
    @PostMapping(value = "/saveModifySubmitAdvanceBill.asyn")
    public ResponseBean saveModifySubmitAdvanceBill(@RequestBody AdvanceBillSaveForm form) {
        try {
            AdvanceBillDTO dto = new AdvanceBillDTO();
            BeanUtils.copyProperties(form, dto);
            Map<String, Object> map = advanceBillService.saveAdvanceBill(form.getType(),dto);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 提交作废申请
     * @return
     */
    @ApiOperation(value = "提交作废申请")
    @PostMapping(value = "/submitInvalidBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true),
            @ApiImplicitParam(name = "invalidRemark", value = "备注", required = true)})
    public ResponseBean submitInvalidBill(long id, String invalidRemark, String token) {
        try {
            Map<String, Object> map = advanceBillService.submitInvalidBill(id, invalidRemark, token);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 查看详情
     * @return
     */
    @ApiOperation(value = "根据id查看详情")
    @PostMapping(value = "/detail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    public ResponseBean<AdvanceBillDTO> detail(long id, String token) {
        try {
            AdvanceBillDTO dto = advanceBillService.getDetail(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 根据id批量删除预收账单
     * @return
     */
    @ApiOperation(value = "根据id批量删除预收账单")
    @PostMapping(value = "/delete.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "预收账单id,多个以','隔开", required = true)})
    public ResponseBean delete(String ids, String token) {
        try {
            List list = StrUtils.parseIds(ids);
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> map = advanceBillService.delAdvanceBill(user.getId(), list);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }


    /**
     * 审核
     * @return
     */
    @ApiOperation(value = "审核")
    @PostMapping(value = "/auditAdvanceItem.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean auditAdvanceItem(AdvanceAuditForm form) {
        try {
            Map<String, Object> map = advanceBillService.auditAdvanceItem(form);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 获取关联预收单提交NC信息
     * @return
     */
    @ApiOperation(value = "获取关联预收单提交NC信息")
    @PostMapping(value = "/listAdvanceBillsToNC.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    public ResponseBean<ReceiveBillToNCDTO> listAdvanceBillsToNC(Long id, String token) {
        try {
            List<ReceiveBillToNCDTO> ncList = advanceBillService.listAdvanceBillsToNCById(token, id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, ncList);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 同步NC
     * @return
     */
    @ApiOperation(value = "同步NC")
    @PostMapping(value = "/synNC.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    public ResponseBean synNC(Long id, String token) {
        try {
            Map<String, Object> map = advanceBillService.synNC(id, token);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 查看核销记录
     *
     * @return
     */
    @ApiOperation(value = "查看核销记录")
    @PostMapping(value = "/getVerifyItems.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    public ResponseBean<AdvanceBillVerifyItemDTO> getVerifyItems(Long id, String token) {
        try {
            List<AdvanceBillVerifyItemDTO> verifyItemModels = advanceBillVerifyItemService.getAdvanceById(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, verifyItemModels);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }


    @ApiOperation(value = "保存核销")
    @PostMapping(value = "/saveAdvanceBillVerify.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveAdvanceBillVerify(AdvanceBillVerifyItemForm form) {
        try {
            AdvanceBillVerifyItemDTO dto = new AdvanceBillVerifyItemDTO();
            BeanUtils.copyProperties(form, dto);

            Map<String, Object> map = advanceBillVerifyItemService.saveVerifyItem(form.getToken(), dto);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 更新凭证信息
     * @return
     */
    @ApiOperation(value = "更新凭证信息")
    @PostMapping(value = "/updateVoucher.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "预收账单id,多个以','隔开", required = true)})
    public ResponseBean updateVoucher(String ids,String token) {
        try {
            List list = StrUtils.parseIds(ids);
            Map<String, Object> map = advanceBillService.updateVoucher(list);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }


    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取操作日志记录")
    @PostMapping(value = "/listAdvanceOperateBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "预收账单id", required = true)})
    private ResponseBean<AdvanceBillOperateItemDTO> listAdvanceOperateBill(String token,Long id) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            List<AdvanceBillOperateItemDTO> dto = advanceBillOperateItemService.listAdvanceBillOperateItem(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    /**
     * 预收账单导出PDF
     * @param
     * @return int
     * @throws IOException
     */
    @GetMapping("/exportAdvanceBillPdf.asyn")
    @ApiOperation(value = "预收账单导出PDF")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "预收账单ids", required = true)})
    public void exportAdvanceBillPdf(HttpServletResponse response, HttpServletRequest request,
                                     @RequestParam(value = "ids", required = true) String ids,
                                     @RequestParam(value = "token", required = true) String token) throws Exception {
        try {
            if (StringUtils.isNotBlank(ids)) {
                List list = StrUtils.parseIds(ids);
                String pdfFile = advanceBillService.exportNcBillPDF(list);
                File filePath = new File(pdfFile);
                //转换中文否则可能会产生乱码
                String downloadFilename = URLEncoder.encode("预收账单打印模板.zip", "UTF-8");
                // 指明response的返回对象是文件流
                response.setContentType("application/octet-stream");
                // 设置在下载框默认显示的文件名
                response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                File[] pdfFiles = filePath.listFiles();
                if (pdfFiles != null && pdfFiles.length > 0) {
                    for (File file : pdfFiles) {
                        //获取模板,压缩到zip文件中
                        InputStream in = new FileInputStream(file);
                        zos.putNextEntry(new ZipEntry(file.getName()));
                        byte[] buffer = new byte[1024];
                        int r = 0;
                        while ((r = in.read(buffer)) != -1) {
                            zos.write(buffer, 0, r);
                        }
                        in.close();
                    }
                    deleteDir(pdfFile);
                }
                zos.flush();
                zos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }


}