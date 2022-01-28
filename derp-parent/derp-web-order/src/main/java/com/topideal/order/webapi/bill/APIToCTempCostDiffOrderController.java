package com.topideal.order.webapi.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.order.service.bill.ToCTempCostDiffOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.ToCTempCostDiffOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台暂估费用单
 */
@RestController
@RequestMapping("/webapi/order/toCTempCostDiffOrder")
@Api(tags = "费用差异调整单")
public class APIToCTempCostDiffOrderController {

    @Autowired
    private ToCTempCostDiffOrderService toCTempCostDiffOrderService;

    /**
     * 获取分页数据
     * */
    @ApiOperation(value = "查询费用差异调整单")
    @PostMapping(value="/listCostDiffOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TocTemporaryReceiveBillCostItemDTO> listCostDiffOrder(ToCTempCostDiffOrderForm form) {
        TocTemporaryReceiveBillCostItemDTO dto = new TocTemporaryReceiveBillCostItemDTO();
        try{
            BeanUtils.copyProperties(form, dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());

            // 响应结果集
            dto = toCTempCostDiffOrderService.listCostDiffOrder(user,dto);
        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

}
