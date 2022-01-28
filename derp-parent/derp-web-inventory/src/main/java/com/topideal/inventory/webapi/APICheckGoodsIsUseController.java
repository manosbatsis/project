package com.topideal.inventory.webapi;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.inventory.service.CheckGoodsIsUseService;
import com.topideal.inventory.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webapi/inventory/checkGoodsByInventory")
@Api(tags = "校验商品是否被使用")
public class APICheckGoodsIsUseController {
    // 采购订单service
    @Autowired
    private CheckGoodsIsUseService checkGoodsIsUseService;


    /**
     * 校验商品是否被使用
     * @param ids
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "校验商品是否被使用")
    @PostMapping(value="/checkGoods.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean checkGoods(String ids,String token)throws Exception {
        boolean flag = true;
        try {
            List<Long> list = StrUtils.parseIds(ids);
            flag = checkGoodsIsUseService.checkGoodsIsUse(list);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);
    }


    /**
     * 库存数据校验
     * @param ids
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "库存数据校验")
    @PostMapping(value="/checkDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean checkDepot(String ids,String token)throws Exception {
        boolean flag = true;
        try {
            User user= ShiroUtils.getUserByToken(token);

            Long merchantId = null ;

			/*if(user.getUserType() != "1") {
				merchantId = user.getMerchantId() ;
			}*/

            List<Long> list = StrUtils.parseIds(ids);
            flag = checkGoodsIsUseService.checkDepotIsUse(list);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);
    }
}
