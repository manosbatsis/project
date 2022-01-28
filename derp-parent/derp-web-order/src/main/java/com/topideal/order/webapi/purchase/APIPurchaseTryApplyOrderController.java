package com.topideal.order.webapi.purchase;

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
import com.topideal.entity.dto.purchase.PurchaseTryApplyOrderDTO;
import com.topideal.order.service.purchase.PurchaseTryApplyOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.PurchaseFrameContractForm;
import com.topideal.order.webapi.purchase.form.PurchaseTryApplyOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 11:58
 * @Description: 采购框架合同
 */
@RestController
@RequestMapping("/webapi/order/purchaseTryApplyOrder")
@Api(tags = "采购试单申请")
public class APIPurchaseTryApplyOrderController {
    private static final Logger LOG = Logger.getLogger(APIPurchaseTryApplyOrderController.class) ;

    @Autowired
    private PurchaseTryApplyOrderService service;
    @Autowired
    private RMQProducer rocketMQProducer;


    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listByPage(PurchaseTryApplyOrderForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            PurchaseTryApplyOrderDTO dto = new PurchaseTryApplyOrderDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = service.listByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
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
    public void exportContract(PurchaseTryApplyOrderForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //获取当前用户
            User user=ShiroUtils.getUserByToken(form.getToken());

            PurchaseTryApplyOrderDTO dto = new PurchaseTryApplyOrderDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            int count = service.countByDTO(dto);

            if(count == 0) {
                return;
            }

            List<ExportExcelSheet> sheets = service.exportContractExcel(user,dto);

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, "采购试单导出");
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
            PurchaseTryApplyOrderDTO dto = new PurchaseTryApplyOrderDTO();
            dto.setMerchantId(user.getMerchantId());
            dto.setId(id);
            dto = service.getDetail(user,dto);
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
            PurchaseTryApplyOrderDTO dto = new PurchaseTryApplyOrderDTO();
            dto.setMerchantId(user.getMerchantId());
            String lastestTime = service.getLatestTime(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, lastestTime);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "采购试单申请编号下拉")
    @PostMapping(value = "/listOaBillCodeSelect.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<List<PurchaseTryApplyOrderDTO>> listOaBillCodeSelect(PurchaseTryApplyOrderForm form) {
        try {
        	List<PurchaseTryApplyOrderDTO>dtoAddList=new ArrayList<PurchaseTryApplyOrderDTO>();
        	PurchaseTryApplyOrderDTO dtoAdd = new PurchaseTryApplyOrderDTO();
    		dtoAdd.setOaBillCode("无");
    		dtoAddList.add(dtoAdd);
        	
            User user = ShiroUtils.getUserByToken(form.getToken());
            PurchaseTryApplyOrderDTO dto = new PurchaseTryApplyOrderDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            List<PurchaseTryApplyOrderDTO> dtoList = service.listOaBillCodeSelect(user,dto);
            if (dtoList.size()>0)dtoAddList.addAll(dtoList);           
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dtoAddList);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }
}
