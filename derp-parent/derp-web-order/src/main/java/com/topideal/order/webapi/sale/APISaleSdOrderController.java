package com.topideal.order.webapi.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.SaleSdOrderDTO;
import com.topideal.entity.dto.sale.SaleSdOrderItemDTO;
import com.topideal.order.service.sale.SaleSdOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.SaleSdOrderForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/webapi/order/saleSdOrder")
@Api(tags = "销售SD单列表")
public class APISaleSdOrderController {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(APISaleSdOrderController.class);

	@Autowired
	private SaleSdOrderService saleSdOrderService;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	
	@ApiOperation(value = "获取销售SD单分页列表")
    @PostMapping(value = "/listDTOByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SaleSdOrderDTO> listDTOByPage(SaleSdOrderForm form) {
		SaleSdOrderDTO dto = new SaleSdOrderDTO();
        try {
            BeanUtils.copyProperties(form,dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto = saleSdOrderService.listDTOByPage(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	
	@ApiOperation(value = "获取详情信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售SD单据id", required = true)
	})
    @PostMapping(value = "/getDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SaleSdOrderDTO> getDetail(String token,Long id) {
		SaleSdOrderDTO dto = new SaleSdOrderDTO();
        try {
        	 boolean isNull = new EmptyCheckUtils().addObject(id).empty();
             if (isNull) {
                 //输入信息不完整
                 return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
             }
            // 响应结果集
            dto = saleSdOrderService.searchDetail(id);
            List<SaleSdOrderItemDTO> itemList = saleSdOrderService.searchItemDetail(id,dto.getOrderType());
            dto.setItemList(itemList);            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	
	@ApiOperation(value = "获取导出数量")
	@PostMapping(value="/getCountOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<Integer> getCountOrder(SaleSdOrderForm form) {
        try {        	
        	SaleSdOrderDTO dto = new SaleSdOrderDTO();
            BeanUtils.copyProperties(form,dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            List<Map<String,Object>> result = saleSdOrderService.exportSaleSdOrder(dto, user , "1");//1-统计
            Integer total = result != null ? result.size():0;
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, total);//成功
        } catch (Exception e) {
            e.printStackTrace(); 
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
	
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSaleSdOrder.asyn")
    public void exportSaleSdOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleSdOrderForm form) {
        try {        	
        	SaleSdOrderDTO dto = new SaleSdOrderDTO();
            BeanUtils.copyProperties(form,dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            List<Map<String,Object>> result = saleSdOrderService.exportSaleSdOrder(dto, user,"");
            String sheetName = "销售SD单";
	        String[] columns={"公司","事业部","客户","业务单号","来源单号","PO号","归属日期","商品货号","商品名称","标准品牌","SD类型","币种"
	        		,"SD比例","上架数量","销售单价","SD金额"};
	        String[] keys={"merchantName","buName","customerName","businessCode","orderCode","poNo","ownDate","goodsNo","goodsName",
	        		"commBrandParentName","sdTypeName","currency","sdRatio", "num","price","sdAmount"};
	        //生成表格
	        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
        } catch (Exception e) {
            e.printStackTrace();            
        }
    }
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据id，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/delSaleSdOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delSaleSdOrder(String token, String ids) {
        try {   
        	User user = ShiroUtils.getUserByToken(token);
        	saleSdOrderService.delSaleSdOrder(ids, user);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
    }
	
	@ApiOperation(value = "编辑")
	@PostMapping(value="/saveSaleSdOrder.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean<String> saveSaleSdOrder(@RequestBody SaleSdOrderForm form) {
        try {        	
        	SaleSdOrderDTO dto = new SaleSdOrderDTO();
            BeanUtils.copyProperties(form,dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            String message = saleSdOrderService.saveSaleSdOrder(dto, user);           
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
        } catch (Exception e) {
            e.printStackTrace(); 
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "导入销售SD单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/importSaleSdOrder.asyn",headers = "content-type=multipart/form-data")
    public ResponseBean<UploadResponse> importSaleSdOrder(String token, HttpSession session,
                                                        @ApiParam(value = "上传的文件", required = true)
                                                        @RequestParam(value = "file", required = true) MultipartFile file) {
        try{
            Map resultMap = new HashMap();//返回的结果集
            if(file==null){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            List<List<Map<String,String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            resultMap = saleSdOrderService.importSaleSdOrder(data,user);            
            
        	//生成to B暂估
            List<String> shelfCodeList = (List<String>)resultMap.get("shelfCodeList");
            if(shelfCodeList != null && shelfCodeList.size() > 0) {            	
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("orderCodes", StringUtils.join(shelfCodeList, ","));
            	map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);
            	rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
            	resultMap.remove("shelfCodeList");
            }
            List<String> rerturnCodeList = (List<String>)resultMap.get("returnCodeList");
            if(rerturnCodeList != null && rerturnCodeList.size() > 0) {            	
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("orderCodes", StringUtils.join(rerturnCodeList, ","));
            	map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);
            	rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
            	resultMap.remove("rerturnCodeList");
            }

            Integer success = (Integer)resultMap.get("success");
            Integer failure = (Integer)resultMap.get("failure");
            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
}
