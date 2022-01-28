package com.topideal.order.webapi.externalaccess;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.dto.sale.PendingCheckOrderVo;
import com.topideal.entity.dto.sale.PendingConfirmTallyingOrderVo;
import com.topideal.entity.dto.sale.PendingShelfSaleOrderVo;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.purchase.PurchaseOrderService;
import com.topideal.order.service.purchase.TallyingOrderService;
import com.topideal.order.service.sale.SaleOrderService;
import com.topideal.order.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供给外部访问的类
 */
@RestController
@RequestMapping("/webapi/order/external")
@Api(tags = "checkGoods-外部访问接口")
public class APIExternalAccessController {

	// 销售订单service
	@Autowired
	private SaleOrderService saleOrderService;

	// 采购订单service
	@Autowired
	private PurchaseOrderService purchaseOrderService;

	// 理货订单service
	@Autowired
	private TallyingOrderService tallyingOrderService;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;

	@SuppressWarnings("unchecked")
	@PostMapping("/getPendingRecordOrders.asyn")
	@ApiOperation(value = "获取采购待办记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	private ResponseBean<List<PurchaseOrderDTO>> getPendingRecordOrders(@RequestParam(value = "token", required = true) String token) {
		List<PurchaseOrderDTO> list = new ArrayList<>();
		List<PurchaseOrderDTO> auditedList = new ArrayList<>();
		List<PurchaseOrderDTO> finishedList = new ArrayList<>();
		Long merchantId = null;
		try {
			// 响应结果集
			PurchaseOrderDTO model = new PurchaseOrderDTO();
			User user = ShiroUtils.getUserByToken(token) ;

			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
				merchantId = user.getMerchantId();
				model.setMerchantId(merchantId);

				List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(!buList.isEmpty()) {
					model.setBuIds(buList);
				}
			}

			list = purchaseOrderService.getPendingRecordOrders(model);
			for (PurchaseOrderDTO dto : list) {
				if (StringUtils.isBlank(dto.getBillStatus())) {
					auditedList.add(dto);
				} else {
					finishedList.add(dto);
				}
			}
			list.removeAll(list);
			list.addAll(auditedList);
			list.addAll(finishedList);
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list) ;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/getPendingShelfOrders.asyn")
	@ApiOperation(value = "获取上架记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	private ResponseBean<List<PendingShelfSaleOrderVo>> getPendingShelfOrders(@RequestParam(value = "token", required = true) String token) {
		List<PendingShelfSaleOrderVo> list = new ArrayList<PendingShelfSaleOrderVo>();
		Long merchantId = null;
		try {
			// 响应结果集
			Map<String, Object> paramMap = new HashMap<>();
			User user = ShiroUtils.getUserByToken(token) ;
			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
				merchantId = user.getMerchantId();
				paramMap.put("merchantId", merchantId);

				List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(!buList.isEmpty()) {
					paramMap.put("buList", buList);
				}
			}
			list = saleOrderService.getPendingShelfSaleOrders(paramMap);

		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list) ;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/getPendingConfirmOrders.asyn")
	@ApiOperation(value = "获取采购理货记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	private ResponseBean<List<PendingConfirmTallyingOrderVo>> getPendingConfirmOrders(@RequestParam(value = "token", required = true) String token) {
		Long merchantId = null;
		try {
			// 响应结果集
			Map<String, Object> paramMap = new HashMap<>();
			User user = ShiroUtils.getUserByToken(token) ;

			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
				merchantId = user.getMerchantId();
				paramMap.put("merchantId", merchantId);

				List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(!buList.isEmpty()) {
					paramMap.put("buList", buList);
				}
			}

			List<PendingConfirmTallyingOrderVo> list = tallyingOrderService.getPendingConfirmOrders(paramMap);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list) ;

		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}


	}

	@SuppressWarnings("unchecked")
	@PostMapping("/getPendingCheckOrders.asyn")
	@ApiOperation(value = "获取销售记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	private ResponseBean<List<PendingCheckOrderVo>> getPendingCheckOrders(@RequestParam(value = "token", required = true) String token) {
		List<PendingCheckOrderVo> list = new ArrayList<PendingCheckOrderVo>();
		Long merchantId = null;
		try {
			// 响应结果集
			Map<String, Object> paramMap = new HashMap<>();
			User user = ShiroUtils.getUserByToken(token) ;

			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
				merchantId = user.getMerchantId();
				paramMap.put("merchantId", merchantId);

				List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(!buList.isEmpty()) {
					paramMap.put("buList", buList);
				}
			}

			list = saleOrderService.getPendingCheckOrders(paramMap);

		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list) ;
	}

	/**
	 * 统计各待办事项数量
	 * @Param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/countPendingOrderNum.asyn")
	@ApiOperation(value = "统计各待办事项数量")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	private ResponseBean<Map<String, Integer>> countPendingOrderNum(@RequestParam(value = "token", required = true) String token) {
		Map<String, Integer> numMap = new HashMap<>();
		PurchaseOrderDTO dto = new PurchaseOrderDTO();
		Long merchantId = null;
		try {
			// 响应结果集
			Map<String, Object> paramMap = new HashMap<>();
			User user = ShiroUtils.getUserByToken(token) ;

			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
				merchantId = user.getMerchantId();
				paramMap.put("merchantId", merchantId);
				dto.setMerchantId(merchantId);

				List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(!buList.isEmpty()) {
					dto.setBuIds(buList);
				}
			}

			Integer pendingRecordNum = purchaseOrderService.countPendingRecordOrders(dto); //待录入
			Integer pendingConfirmNum = tallyingOrderService.countPendingConfirmOrders(paramMap); //待确认
			Integer pendingShelfNum = saleOrderService.countPendingShelfOrders(paramMap);//待上架
			Integer pendingCheckNum = saleOrderService.countPendingCheckOrders(paramMap); //待审核

			numMap.put("pendingRecordNum", pendingRecordNum);
			numMap.put("pendingConfirmNum", pendingConfirmNum);
			numMap.put("pendingShelfNum", pendingShelfNum);
			numMap.put("pendingCheckNum", pendingCheckNum);

		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, numMap) ;
	}
}
