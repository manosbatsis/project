package com.topideal.order.webapi.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.dto.bill.PlatformCostOrderItemDTO;
import com.topideal.order.service.bill.PlatformCostOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.PlatformCostOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webapi/order/platformCostOrder")
@Api(tags = "平台费用单")
public class APIPlatformCostOrderController {

    @Autowired
    private PlatformCostOrderService platformCostOrderService;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listPlatformCostOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<PlatformCostOrderDTO> listPlatformCostOrder(PlatformCostOrderForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            PlatformCostOrderDTO dto = new PlatformCostOrderDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = platformCostOrderService.listLatformCostOrderByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "根据平台费用单ID获取详情")
    @PostMapping("/getDetailsById.asyn")
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "平台费用单id", required = true) })
    public ResponseBean<PlatformCostOrderDTO> getDetailsById(@RequestParam(value="token", required = true)String token, Long id) throws Exception{
        try {
            PlatformCostOrderDTO dto= new PlatformCostOrderDTO();
            dto.setId(id);
            dto = platformCostOrderService.getDetails(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "根据平台费用单ID分页获取表体")
    @PostMapping("/listPlatformCostOrderItem.asyn")
    private ResponseBean<PlatformCostOrderItemDTO> listPlatformCostOrderItem(PlatformCostOrderForm form) {

        try{
            PlatformCostOrderItemDTO dto = new PlatformCostOrderItemDTO();
            dto.setPlatformCostOrderId(form.getPlatformCostOrderId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            // 响应结果集
            dto = platformCostOrderService.listPlatformCostOrderItemByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }
	
    
    
}
