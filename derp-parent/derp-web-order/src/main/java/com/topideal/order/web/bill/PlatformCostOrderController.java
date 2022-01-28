package com.topideal.order.web.bill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;
import com.topideal.order.service.bill.PlatformCostOrderService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * @Description: 平台费用单
 * @Author: 杨创
 * @Date: 2020/08/31 14:53
 **/
@Controller
@RequestMapping("/platformCostOrder")
public class PlatformCostOrderController {

    @Autowired
    private PlatformCostOrderService PlatformCostOrderService;

    /**
     * 访问列表页面
     * @param model
     * */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception{
        return "derp/bill/platform-cost-order-list";
    }

    /**
     * 获取分页数据
     * @param dto
     * */
    @RequestMapping("/listLatformCostOrder.asyn")
    @ResponseBody
    private ViewResponseBean listLatformCostOrder(PlatformCostOrderDTO dto) {
        try{
            User user= ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto=PlatformCostOrderService.listLatformCostOrderByPage(dto);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }
    /**
     * 访问列表页面
     * @param model
     * */
    @RequestMapping("/toDetailsPage.html")
    public String toDetailsPage(Model model,Long id) throws Exception{
    	PlatformCostOrderDTO dto= new PlatformCostOrderDTO();
    	dto.setId(id);
    	dto = PlatformCostOrderService.getDetails(dto);
    	model.addAttribute("detail", dto);
        return "derp/bill/platform-cost-order-details";
    }
    
    @RequestMapping("/listLatformCostOrderItem.asyn")
    @ResponseBody
    private ViewResponseBean listLatformCostOrderItem(PlatformCostOrderItemModel model) {

        try{       	
            // 响应结果集
            model=PlatformCostOrderService.listLatformCostOrderItemByPage(model);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(model);
    }
	
    
    
}
