package com.topideal.order.webapi.transfer;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.transfer.TransferOutInBean;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.transfer.form.TransferOutInForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调入调出流水  控制层
 * @author yucaifu
 */
@RestController
@RequestMapping("/webapi/transfer/transferOutIn")
@Api(tags = "调入调出流水")
public class APITransferOutInController {

	@Autowired
	private TransferOrderService transferOrderService;

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/transferOutInList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<TransferOutInBean> transferOutInList(TransferOutInForm form) {
		try{
			TransferOutInBean model = new TransferOutInBean();
			User user= ShiroUtils.getUserByToken(form.getToken());
			model.setMerchantId(user.getMerchantId());
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());
			model.setCode(form.getCode());
			model.setInDepotId(form.getInDepotId());
			model.setOutDepotId(form.getOutDepotId());
			model.setBuId(form.getBuId());
			model.setOutGoodsNo(form.getOutGoodsNo());
			model.setCreateDateStart(form.getCreateDateStart());
			model.setCreateDateEnd(form.getCreateDateEnd());
			// 响应结果集
			model = transferOrderService.listTransferOutInPage(model, user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	
	
}
