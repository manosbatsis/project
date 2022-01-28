package com.topideal.order.webapi.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.order.service.purchase.PurchaseFrameContractService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.PurchaseFrameContractForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 11:58
 * @Description: 采购框架合同
 */
@RestController
@RequestMapping("/webapi/order/purchaseFrameContract")
@Api(tags = "采购框架合同")
public class APIPurchaseFrameContractController {
    private static final Logger LOG = Logger.getLogger(APIPurchaseFrameContractController.class) ;

    @Autowired
    private PurchaseFrameContractService service;
    @Autowired
    private RMQProducer rocketMQProducer;
    
    
    @ApiOperation(value = "获取框架合同或者试单的币种和交货方式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "frameContractNo", value = "框架合同单号"),
            @ApiImplicitParam(name = "tryApplyCode", value = "试单单号")
            
    })
    @PostMapping(value = "/getContracOrTryApplyOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<PurchaseFrameContractDTO> getContracOrTryApplyOrder(String token,String frameContractNo,String tryApplyCode) {
        try {
        	User user = ShiroUtils.getUserByToken(token);
        	PurchaseFrameContractDTO dto =service.getContracOrTryApplyOrder(user,frameContractNo,tryApplyCode);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }
    
    
    
    
    @ApiOperation(value = "框架合同下拉框")
    @PostMapping(value = "/listFrameContracSelect.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<List<PurchaseFrameContractDTO>> getListSelectBean(PurchaseFrameContractForm form) {    	
    	try {
    		List<PurchaseFrameContractDTO>dtoAddList=new ArrayList<PurchaseFrameContractDTO>();
    		PurchaseFrameContractDTO dtoAdd = new PurchaseFrameContractDTO();
    		dtoAdd.setContractNo("无");
    		dtoAddList.add(dtoAdd);
    		
            User user = ShiroUtils.getUserByToken(form.getToken());
            PurchaseFrameContractDTO dto = new PurchaseFrameContractDTO();
            BeanUtils.copyProperties(form, dto);           
            dto.setMerchantId(user.getMerchantId());
            List<PurchaseFrameContractDTO> dtoList = service.listFrameContracSelect(dto);
        	if (dtoList.size()>0)dtoAddList.addAll(dtoList);
            	
			

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dtoAddList);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listByPage(PurchaseFrameContractForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            PurchaseFrameContractDTO dto = new PurchaseFrameContractDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = service.listByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取刷新时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @GetMapping(value = "/getRefreshTime.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getRefreshTime(String token) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            PurchaseFrameContractDTO dto = new PurchaseFrameContractDTO();
            dto.setMerchantId(user.getMerchantId());
            String lastestTime = service.getLatestTime(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, lastestTime);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "刷新")
    @PostMapping(value = "/refresh.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean refresh(PurchaseFrameContractForm form) {
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(form.getContractStartTime())
                    .addObject(form.getContractEndTime())
                    .empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(form.getToken());

            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());
            body.put("beginDate", form.getContractStartTime());
            body.put("endDate", form.getContractEndTime());

            JSONObject json = JSONObject.fromObject(body);
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.GET_PURCHASE_FRAME_CONTRACT.getTopic()
                    , MQOrderEnum.GET_PURCHASE_FRAME_CONTRACT.getTags());

            if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "刷新消息发送失败");
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导出")
    @GetMapping(value="/exportContract.asyn")
    public void exportContract(PurchaseFrameContractForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //获取当前用户
            User user=ShiroUtils.getUserByToken(form.getToken());

            PurchaseFrameContractDTO dto = new PurchaseFrameContractDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setIds(form.getIds());
            dto.setMerchantId(user.getMerchantId());
            int count = service.countByDTO(dto);

            if(count == 0) {
                return;
            }

            List<ExportExcelSheet> sheets = service.exportContractExcel(user,dto);

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, "采购框架合同导出");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "获取详情数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    @PostMapping(value = "/getDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getDetail(String token, Long id) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            if(id == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018.getCode(), "ID不能为空");

            }
            PurchaseFrameContractDTO dto = new PurchaseFrameContractDTO();
            dto.setMerchantId(user.getMerchantId());
            dto.setId(id);
            dto = service.getDetail(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }
}
