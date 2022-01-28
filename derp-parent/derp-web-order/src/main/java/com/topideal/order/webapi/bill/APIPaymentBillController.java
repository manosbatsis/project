package com.topideal.order.webapi.bill;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.spire.xls.ExcelVersion;
import com.spire.xls.FileFormat;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.order.service.bill.PaymentBillService;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.tools.pdf.PdfUtils;
import com.topideal.order.webapi.bill.form.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应付账单
 */
@RestController
@RequestMapping("/webapi/order/paymentBill")
@Api(tags = "应付账单")
public class APIPaymentBillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIPaymentBillController.class);

    @Autowired
    private PaymentBillService paymentBillService;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private RMQProducer rocketMQProducer;

    /**
     * 获取分页数据
     * @return
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listPaymentBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PaymentBillDTO> listPaymentBill(PaymentBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            PaymentBillDTO dto = new PaymentBillDTO() ;

            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            dto = paymentBillService.listPaymentBill(dto, user);

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
    public ResponseBean<PurchaseOrderDTO> listPurchaseOrder(PaymentBillSearchForm form) {
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

            dto = paymentBillService.listPurchaseOrderPage(dto, user) ;

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    
    @SuppressWarnings("unchecked")
	@PostMapping("/getVeriAdvancePaymentList.asyn")
    @ApiOperation(value = "勾稽预付款单", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PaymentVerificateItemDTO> getVeriAdvancePaymentList(PaymentVeriListForm form) {
    	try {
    		
    		String token = form.getToken();
    		
    		User user = ShiroUtils.getUserByToken(token);
    		
    		AdvancePaymentBillDTO billDTO = new AdvancePaymentBillDTO() ;
    		billDTO.setBuId(form.getBuId());
    		billDTO.setCurrency(form.getCurrency());
    		billDTO.setSupplierId(form.getSupplierId());
    		billDTO.setBegin(form.getBegin());
    		billDTO.setPageSize(form.getPageSize());
    		billDTO.setMerchantId(user.getMerchantId());
    		
    		PaymentVerificateItemDTO dto = paymentBillService.getVeriAdvancePaymentList(billDTO) ;
    		
    		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
    		
	    }   catch (DerpException e) {
	        return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
	    }   catch (Exception e) {
	        LOGGER.error(e.getMessage(), e);
	        return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
	    }
    	
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "校验是否是同供应商+同事业部+同币种")
    @PostMapping(value = "/checkData.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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

            paymentBillService.getCheckData(idList) ;

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
    @PostMapping(value = "/getAddPageInfo.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "采购订单ids，多个以‘,’隔开", required = true),
            @ApiImplicitParam(name = "type", value = "单据类型 1-采购订单 2-采购发票", required = true)
    })
    public ResponseBean<PaymentBillDTO> getAddPageInfo(String token, String ids,String type) {
        try {

            boolean isEmpty = new EmptyCheckUtils().addObject(token).addObject(ids).empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            List<Long> idList = StrUtils.parseIds(ids);

            PaymentBillDTO dto = paymentBillService.getAddPageInfo(idList,type) ;

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

        }   catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        }   catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 保存应收账单
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "保存或修改应付账单")
    @PostMapping(value = "/saveOrModifyPaymentBill.asyn")
    public ResponseBean<String> saveOrModifyPaymentBill(@RequestBody PaymentBillAddForm form) {
        try {

            User user = ShiroUtils.getUserByToken(form.getToken());

            PaymentBillDTO dto = new PaymentBillDTO() ;
            BeanUtils.copyProperties(form, dto);

            if(StringUtils.isNotBlank(form.getExpectedPaymentDateStr())) {
            	Timestamp expectedPaymentDate = TimeUtils.parse(form.getExpectedPaymentDateStr(), "yyyy-MM-dd");
                dto.setExpectedPaymentDate(expectedPaymentDate);
            }

            Long billId = paymentBillService.saveOrModifyPaymentBill(dto, user);

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
            @ApiImplicitParam(name = "ids", value = "应收账单id,多个以','隔开", required = true),
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
            
            paymentBillService.submitInvalidBill(list, invalidRemark, user);
            
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
            @ApiImplicitParam(name = "id", value = "应付账单id", required = true)})
    public ResponseBean<PaymentBillDTO> detail(long id, String token) {
        try {
        	
        	boolean isEmpty = new EmptyCheckUtils()
                    .addObject(token)
                    .addObject(id)
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
            PaymentBillDTO dto = paymentBillService.getDetail(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    /**
     * 根据id批量删除应付账单
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "根据id批量删除应付账单")
    @PostMapping(value = "/delete.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "应付账单id,多个以','隔开", required = true)})
    public ResponseBean<String> delete(String ids, String token) {
        try {
        	
            List<Long> list = StrUtils.parseIds(ids);
            User user = ShiroUtils.getUserByToken(token);
            
            paymentBillService.delPaymentBill(user, list);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
                
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    /**
     * 核销
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "核销")
    @PostMapping(value = "/verificate.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<String> verificate(PaymentVerificateItemForm form) {
        try {
        	
        	boolean isEmpty = new EmptyCheckUtils()
        			.addObject(form.getToken())
        			.addObject(form.getCurrentVerificateAmount())
        			.addObject(form.getPaymentDateStr())
        			.addObject(form.getPaymentId())
        			.empty();
        	
        	if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
        	User user = ShiroUtils.getUserByToken(form.getToken());
        	
        	PaymentVerificateItemDTO dto = new PaymentVerificateItemDTO() ;
        	
        	BeanUtils.copyProperties(form, dto);
        	
        	dto.setPaymentDate(TimeUtils.parse(form.getPaymentDateStr(), "yyyy-MM-dd"));
        	dto.setDraweeId(user.getId());
        	dto.setDrawee(user.getName());
        	
            paymentBillService.saveVerificate(dto, user);
            
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
    @PostMapping(value = "/auditPayment.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
        @ApiImplicitParam(name = "id", value = "预收账单id", required = true),
        @ApiImplicitParam(name = "invalidRemark", value = "备注", required = true),
        @ApiImplicitParam(name = "isPassed", value = "是否通过标识 1-通过 0-驳回", required = true)})
    public ResponseBean<String> auditPayment(Long id, String invalidRemark, 
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
        	
            paymentBillService.auditPayment(user, id, invalidRemark, isPassed);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
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
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true),
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
        	
        	if(!(DERP_ORDER.PAYMENT_BILL_AUDIT_METHOD_1.equals(auditMethod)
    				|| DERP_ORDER.PAYMENT_BILL_AUDIT_METHOD_2.equals(auditMethod))) {
    			throw new DerpException("审批方式必须为 1-OA审批 2-经分销审批") ;
    		}
        	
        	User user = ShiroUtils.getUserByToken(token);
        	
        	paymentBillService.modifyAuditMethod(user, id, auditMethod);
            
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());//未知异常
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
            @ApiImplicitParam(name = "id", value = "应付账单id", required = true)})
    public ResponseBean<PaymentItemDTO> getPaymentItems(Long id, String token) {
        try {
        	
            List<PaymentItemDTO> itemListDTO = paymentBillService.getPaymentItems(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, itemListDTO);//成功
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    /**
     * 获取费用明细
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "获取费用明细")
    @PostMapping(value = "/getCostItems.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应付账单id", required = true)})
    public ResponseBean<PaymentCostItemDTO> getCostItems(Long id, String token) {
        try {
        	
            List<PaymentCostItemDTO> itemListDTO = paymentBillService.getCostItems(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, itemListDTO);//成功
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取操作日志记录")
    @PostMapping(value = "/listOperateLog.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应付账单id", required = true)})
    public ResponseBean<OperateLogDTO> listOperateLog(String token,Long id) {
        try {
        	
            List<OperateLogDTO> dtoList = paymentBillService.listOperateLog(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dtoList);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取核销记录")
    @PostMapping(value = "/getVerificateList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true)})
    public ResponseBean<PaymentVerificateItemDTO> getVerificateList(String token,Long id) {
        try {
        	
            List<PaymentVerificateItemDTO> dtoList = paymentBillService.getVerificateList(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dtoList);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    
    /**
     * 获取付款明细
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "获取汇总明细")
    @PostMapping(value = "/getPaymentSummary.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应付账单id", required = true)})
    public ResponseBean<PaymentSummaryDTO> getPaymentSummary(Long id, String token) {
        try {
        	
            List<PaymentSummaryDTO> itemListDTO = paymentBillService.getPaymentSummary(id);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, itemListDTO);//成功
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }
    
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "更新凭证信息")
    @PostMapping(value = "/flashVoucher.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "应付账单id,多个以','隔开", required = true)})
    public ResponseBean<String> flashVoucher(String token,String ids) {
        try {
        	
        	if(StringUtils.isBlank(ids)) {
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
        	
        	List<Long> list = StrUtils.parseIds(ids);
        	List<String> strList = new ArrayList<>() ;
        	
        	for (Long id : list) {
        		PaymentBillDTO detail = paymentBillService.getDetail(id);
        		
        		strList.add(detail.getCode()) ;
			}
        	
        	JSONObject paramJson = new JSONObject() ;
        	paramJson.put("dataSource", "1") ;
        	paramJson.put("billCodes", StringUtils.join(strList.toArray(), ",")) ;
            
        	SendResult result =rocketMQProducer.send(paramJson.toString(), MQOrderEnum.TIMER_RECEVICE_BILL_VOUCHER_BACKFILL.getTopic(), MQOrderEnum.TIMER_RECEVICE_BILL_VOUCHER_BACKFILL.getTags());
            if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, "提送更新凭证MQ异常");//未知异常
            }
        	
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "同步NC")
    @PostMapping(value = "/synNC.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应付单id", required = true),
    })
    public ResponseBean<String> synNC(@RequestParam(value = "token", required = true) String token, 
    		Long id) {
    	try {
        	
        	if(id == null) {
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
        	
        	User user = ShiroUtils.getUserByToken(token);
        	
        	paymentBillService.syncNC(id, user) ;
        	
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
            
        } catch (DerpException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e);//未知异常
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取打印信息，同步NC信息共用")
    @PostMapping(value = "/getPrintingInfo.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true)})
    public ResponseBean<PaymentBillDTO> getPrintingInfo(String token,Long id) {
        try {
        	
        	User user = ShiroUtils.getUserByToken(token);
        	
        	PaymentBillDTO detail = paymentBillService.getDetail(id);
        	
        	//打印修改公司主体
        	detail = paymentBillService.changeMerchantInfo(detail) ;
        	
        	List<OperateLogDTO> dtoList = paymentBillService.listOperateLog(id);
        	List<PaymentPrintSummaryDTO> summaryList = paymentBillService.getListPrintSummary(id) ;
        	
        	dtoList = dtoList.stream().filter(dto -> (dto.getOperateAction().contains("审核"))
        			&& !"提交审核".equals(dto.getOperateAction())).collect(Collectors.toList()) ;
        	
        	String poNos = paymentBillService.getDetailPoNo(id) ;
        	
        	detail.setPoNos(poNos);
        	detail.setPrintSummaryList(summaryList);
        	
        	//构造财务审批列数据
			List<String> strList = new ArrayList<String>() ;

			//成功拼字符串索引
			
			for (OperateLogDTO log : dtoList) {
				
				if("驳回".equals(log.getOperateResult())) {
					
					strList.clear();
					
					continue ;
				}
				
				if(!"批准".equals(log.getOperateResult())) {
					continue ;
				}
				
				strList.add(log.getOperaterDepot() + "：" + log.getOperater()) ;
				
			}
			
			detail.setPrintAuditList(strList);
			
			//设置打印人
			detail.setPrinter(user.getName());
			detail.setPrintDate(TimeUtils.getNow());
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, detail);//成功
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    
    @ApiOperation(value = "导出PDF")
    @GetMapping(value = "/exportPDF.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
        @ApiImplicitParam(name = "id", value = "应付账单id", required = true)})
    public void exportPDF(Long id, HttpServletRequest request, HttpServletResponse response) {
    	
    	try {
			if(id == null) {
				return ;
			}
			
			PaymentBillDTO detail = paymentBillService.getDetail(id);
			
			detail = paymentBillService.changeMerchantInfo(detail) ;
			
			List<OperateLogDTO> dtoList = paymentBillService.listOperateLog(id);
			
			if(detail.getCreateDate() != null) {
				detail.setCreateDateStr(TimeUtils.format(detail.getCreateDate(), "yyyy-MM-dd"));
			}
			
			dtoList = dtoList.stream().filter(dto -> (dto.getOperateAction().contains("审核"))
        			&& !"提交审核".equals(dto.getOperateAction())).collect(Collectors.toList()) ;
			
			String poNos = paymentBillService.getDetailPoNo(id) ;
			
			detail.setPoNos(poNos);
			
			//构造财务审批列数据
			List<String> strList = new ArrayList<String>() ;

			//成功拼字符串索引
			int index = 0 ;
			
			String line = "" ;
			
			for (OperateLogDTO log : dtoList) {
				
				if("驳回".equals(log.getOperateResult())) {
					
					line = "" ;
					index = 0 ;
					strList.clear();
					
					continue ;
				}
				
				if(!"批准".equals(log.getOperateResult())) {
					continue ;
				}
				
				line += "   " + log.getOperaterDepot() + "：" + log.getOperater() + "      ";
				
				index += 1; 
				
				if(index % 3 == 0) {
					strList.add(line) ;
					
					line = "" ;
				}
				
			}
			
			detail.setPrintAuditList(strList);
			
			Workbook workbook = DownloadExcelUtil.exportTemplateExcel("PAYMENTBILLTEMP", detail);

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

			com.spire.xls.Workbook wb = new com.spire.xls.Workbook() ;
			wb.loadFromFile(tempFilePath, ExcelVersion.Version2007);
			//自适应尺寸
			wb.getConverterSetting().setSheetFitToPage(true);
			wb.getConverterSetting().setSheetFitToWidth(true);
			//装载字体
			wb.setCustomFontFilePaths(new String[] {PdfUtils.PATH + "SIMSUN.TTC"
					, PdfUtils.PATH + "msyh.ttf", PdfUtils.PATH + "SIMKAI.TTF"});
			
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
			
			fos.close();
			is.close();
			
			file.delete() ;
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
    }
    
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "更新打印信息")
    @PostMapping(value = "/updatePrintingInfo.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true)})
    public ResponseBean<String> updatePrintingInfo(String token, Long id) {
    	
    	try {
			boolean isEmpty = new EmptyCheckUtils()
					.addObject(token)
					.addObject(id)
					.empty();
			
			if(isEmpty){
			    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			User user = ShiroUtils.getUserByToken(token);
			
			paymentBillService.updatePrintingInfo(id, user) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功 
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
    	
    }

//    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @PostMapping(value="/exportPayment.asyn")
//    public ResponseBean exportPayment(PaymentBillForm form) throws Exception {
//        Map<String, Object> retMap = new HashMap<String, Object>();
//        retMap.put("code", "00");
//        retMap.put("message", "成功");
//
//        try {
//            //获取当前用户
//            User user=ShiroUtils.getUserByToken(form.getToken());
//
//            PaymentBillDTO dto = new PaymentBillDTO();
//            BeanUtils.copyProperties(form, dto);
//            dto.setIds(form.getPaymentIds());
//            dto.setMerchantId(user.getMerchantId());
//
//            int count = paymentBillService.countByDTO(dto);
//
//            if(count == 0) {
//                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
//            }
//
//            FileTaskMongo taskModel = new FileTaskMongo();
//            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
//            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_PAYMENT_BILL);
//            taskModel.setTaskName("应付帐单导出");
//            taskModel.setMerchantId(user.getMerchantId());
//            taskModel.setState("1");
//            taskModel.setRemark("应付帐单导出");
//            taskModel.setUsername(user.getName());
//            taskModel.setOwnMonth(TimeUtils.formatMonth(new Date()));
//            taskModel.setParam(JSON.toJSONString(dto));
//            taskModel.setCreateDate(TimeUtils.formatFullTime());
//            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
//            taskModel.setUserId(user.getId());
//            fileTaskService.save(taskModel);
//        } catch (Exception e) {
//            e.printStackTrace();
//            retMap.put("code", "01");
//            retMap.put("message", "网络故障");
//            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, retMap);
//        }
//        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
//    }

    /**
     * 预售单导出
     * */
    @ApiOperation(value = "导出")
    @GetMapping(value="/exportPayment.asyn")
    private void exportPayment(PaymentBillForm form,HttpServletResponse response, HttpServletRequest request) throws Exception{
        try {
            //获取当前用户
            User user=ShiroUtils.getUserByToken(form.getToken());

            PaymentBillDTO dto = new PaymentBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setIds(form.getPaymentIds());
            dto.setMerchantId(user.getMerchantId());
            int count = paymentBillService.countByDTO(dto);

            if(count == 0) {
//                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
                return;
            }

            List<ExportExcelSheet> sheets = paymentBillService.exportPaymentBillExcel(user,dto);

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, "应付帐单导出");
        }catch(Exception e){
            e.printStackTrace();
//            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
//        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }
}