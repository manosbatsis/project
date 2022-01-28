package com.topideal.order.web.transfer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.order.service.purchase.TallyingItemBatchService;
import com.topideal.order.service.purchase.TallyingOrderService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 调拨理货单 controller
 * 
 */
@RequestMapping("/transferTallying")
@Controller
public class TransferTallyingOrderController {

	// 理货单service
	@Autowired
	private TallyingOrderService tallyingOrderService;
	// 理货单批次service
	 @Autowired
	 private TallyingItemBatchService tallyingItemBatchService;
	//调拨指令
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;


	/**
	 * 访问调拨理货单列表页面
	 */
	@RequestMapping("/toPageTransfer.html")
	public String toPageTransfer(Model model,HttpSession session) throws Exception {
		return "/derp/transfer/transfer-tallying-list";
	}

	/**
	 * 获取分页数据
	 */
	@RequestMapping("/listTallyingOrderTransfer.asyn")
	@ResponseBody
	private ViewResponseBean listTallyingOrderTransfer(TallyingOrderDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setType(DERP_ORDER.TALLYINGORDER_TYPE_2);// 调拨理货单
			// 响应结果集
			dto = tallyingOrderService.listTallyingOrderPage(dto, user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailPage.html")
	public String toDetailsPageTransfer(Model model, Long id, String pageSource) throws Exception {
		TallyingOrderDTO tallyingOrder = tallyingOrderService.searchDetail(id);
		model.addAttribute("detail", tallyingOrder);
		List<TallyingItemBatchDTO> list = tallyingItemBatchService.getGoodsAndBatch(id);
		model.addAttribute("batchBean", list);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(pageSource)) {
			model.addAttribute("pageSource", pageSource);
		}
		return "/derp/transfer/transfer-tallying-detail";
	}


	 /**
	  * 调拨理货单确认/驳回
	  * @param ids
	  * 理货单id 多个时用,号分隔
	  * 操作指令 010:确认 004:驳回
	  */
	@RequestMapping("/tallyConfirmTransfer.asyn")
	@ResponseBody
	public ViewResponseBean tallyConfirmTransfer(String ids, String state,HttpSession session) {
		User user= ShiroUtils.getUser(); 
		Map<String, String> data = new HashMap<String, String>();
		try {
			// 检查参数
			if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(state)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			String failCode = tallyingOrderService.updateTallyConfirmTransfer(ids, state, user.getId(),user.getName(),user.getTopidealCode());
			data.put("failCode", failCode);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(data);
	}

	
}
