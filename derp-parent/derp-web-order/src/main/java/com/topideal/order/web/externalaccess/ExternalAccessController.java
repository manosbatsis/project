//package com.topideal.order.web.externalaccess;
//
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
//import com.topideal.entity.dto.sale.PendingCheckOrderVo;
//import com.topideal.entity.dto.sale.PendingConfirmTallyingOrderVo;
//import com.topideal.entity.dto.sale.PendingShelfSaleOrderVo;
//import com.topideal.entity.vo.purchase.PurchaseOrderModel;
//import com.topideal.entity.vo.sale.BillOutinDepotModel;
//import com.topideal.order.service.purchase.PurchaseOrderService;
//import com.topideal.order.service.purchase.TallyingOrderService;
//import com.topideal.order.service.sale.SaleOrderService;
//import com.topideal.order.shiro.ShiroUtils;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.swing.text.View;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 提供给外部访问的类
// */
//@RequestMapping("/external")
//@Controller
//public class ExternalAccessController {
//
//	// 销售订单service
//	@Autowired
//	private SaleOrderService saleOrderService;
//
//	// 采购订单service
//	@Autowired
//	private PurchaseOrderService purchaseOrderService;
//
//	// 理货订单service
//	@Autowired
//	private TallyingOrderService tallyingOrderService;
//
//	@RequestMapping("/getPendingRecordOrders.asyn")
//	@ResponseBody
//	private ViewResponseBean getPendingRecordOrders() {
//		List<PurchaseOrderDTO> list = new ArrayList<>();
//		List<PurchaseOrderDTO> auditedList = new ArrayList<>();
//		List<PurchaseOrderDTO> finishedList = new ArrayList<>();
//		Long merchantId = null;
//		try {
//			// 响应结果集
//			PurchaseOrderDTO model = new PurchaseOrderDTO();
//			User user = ShiroUtils.getUser();
//			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//				merchantId = user.getMerchantId();
//				model.setMerchantId(merchantId);
//			}
//			list = purchaseOrderService.getPendingRecordOrders(model);
//			for (PurchaseOrderDTO dto : list) {
//				if (StringUtils.isBlank(dto.getBillStatus())) {
//					auditedList.add(dto);
//				} else {
//					finishedList.add(dto);
//				}
//			}
//			list.removeAll(list);
//			list.addAll(auditedList);
//			list.addAll(finishedList);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(list);
//	}
//
//	@RequestMapping("/getPendingShelfOrders.asyn")
//	@ResponseBody
//	private ViewResponseBean getPendingShelfOrders() {
//		List<PendingShelfSaleOrderVo> list = new ArrayList<PendingShelfSaleOrderVo>();
//		Long merchantId = null;
//		try {
//			// 响应结果集
//			Map<String, Object> paramMap = new HashMap<>();
//			User user = ShiroUtils.getUser();
//			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//				merchantId = user.getMerchantId();
//				paramMap.put("merchantId", merchantId);
//			}
//			list = saleOrderService.getPendingShelfSaleOrders(paramMap);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(list);
//	}
//
//	@RequestMapping("/getPendingConfirmOrders.asyn")
//	@ResponseBody
//	private ViewResponseBean getPendingConfirmOrders() {
//		List<PendingConfirmTallyingOrderVo> list = new ArrayList<PendingConfirmTallyingOrderVo>();
//		Long merchantId = null;
//		try {
//			// 响应结果集
//			Map<String, Object> paramMap = new HashMap<>();
//			User user = ShiroUtils.getUser();
//			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//				merchantId = user.getMerchantId();
//				paramMap.put("merchantId", merchantId);
//			}
//			list = tallyingOrderService.getPendingConfirmOrders(paramMap);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(list);
//	}
//
//	@RequestMapping("/getPendingCheckOrders.asyn")
//	@ResponseBody
//	private ViewResponseBean getPendingCheckOrders() {
//		List<PendingCheckOrderVo> list = new ArrayList<PendingCheckOrderVo>();
//		Long merchantId = null;
//		try {
//			// 响应结果集
//			Map<String, Object> paramMap = new HashMap<>();
//			User user = ShiroUtils.getUser();
//			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//				merchantId = user.getMerchantId();
//				paramMap.put("merchantId", merchantId);
//			}
//			list = saleOrderService.getPendingCheckOrders(paramMap);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(list);
//	}
//
//	/**
//	 * 统计各待办事项数量
//	 * @Param
//	 * @return
//	 */
//	@RequestMapping("/countPendingOrderNum.asyn")
//	@ResponseBody
//	private ViewResponseBean countPendingOrderNum() {
//		Map<String, Integer> numMap = new HashMap<>();
//		PurchaseOrderModel model = new PurchaseOrderModel();
//		Long merchantId = null;
//		try {
//			// 响应结果集
//			Map<String, Object> paramMap = new HashMap<>();
//			User user = ShiroUtils.getUser();
//			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//				merchantId = user.getMerchantId();
//				paramMap.put("merchantId", merchantId);
//				model.setMerchantId(merchantId);
//			}
//			Integer pendingRecordNum = purchaseOrderService.countPendingRecordOrders(model); //待录入
//			Integer pendingConfirmNum = tallyingOrderService.countPendingConfirmOrders(paramMap); //待确认
//			Integer pendingShelfNum = saleOrderService.countPendingShelfOrders(paramMap);//待上架
//			Integer pendingCheckNum = saleOrderService.countPendingCheckOrders(paramMap); //待审核
//			numMap.put("pendingRecordNum", pendingRecordNum);
//			numMap.put("pendingConfirmNum", pendingConfirmNum);
//			numMap.put("pendingShelfNum", pendingShelfNum);
//			numMap.put("pendingCheckNum", pendingCheckNum);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(numMap);
//	}
//}
