package com.topideal.order.web.transfer;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.constant.ConfigConstants;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.transfer.TransferOutInBean;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 调入调出流水  控制层
 * @author yucaifu
 */
@RequestMapping("/transferOutIn")
@Controller
public class TransferOutInController {

	
	// 仓库信息service
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
//	@Reference
//	private UserInfoService userInfoService;
	
	@Autowired
	private TransferOrderService transferOrderService;
	

	/**
	 * 访问列表页面
	 * @param model
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session) throws Exception{
		User user= ShiroUtils.getUser(); 
		return "/derp/transfer/transferoutin-list";
	}
	/**
	 * 获取分页数据
	 * @param model
	 * */
	@RequestMapping("/transferOutInList.asyn")
	@ResponseBody
	private ViewResponseBean transferOutInList(TransferOutInBean model) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			model.setMerchantId(user.getMerchantId());
			
			// 响应结果集
			model = transferOrderService.listTransferOutInPage(model, user);
		
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	
}
