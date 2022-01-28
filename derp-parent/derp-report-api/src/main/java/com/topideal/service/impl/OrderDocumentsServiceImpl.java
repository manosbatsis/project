package com.topideal.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.topideal.dao.reporting.ApiGoodsConfigDao;
import com.topideal.entity.dto.OrderDTO;
import com.topideal.entity.dto.PurchaseOrderDTO;
import com.topideal.entity.dto.SaleOrderDTO;
import com.topideal.entity.vo.reporting.ApiGoodsConfigModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.RepotApiEnum;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.order.OrderItemDao;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.SaleOrderDao;
import com.topideal.dao.order.SaleOrderItemDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.dto.ResponseRoot;
import com.topideal.entity.dto.api10003.DSOrder;
import com.topideal.entity.dto.api10003.DSOrderGoods;
import com.topideal.entity.dto.api10003.OrderGoods;
import com.topideal.entity.dto.api10003.PurchaseOrder;
import com.topideal.entity.dto.api10003.SaleOrder;
import com.topideal.entity.vo.order.OrderItemModel;
import com.topideal.entity.vo.order.OrderModel;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;
import com.topideal.entity.vo.order.PurchaseOrderModel;
import com.topideal.entity.vo.order.SaleOrderItemModel;
import com.topideal.entity.vo.order.SaleOrderModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.service.OrderDocumentsService;

import net.sf.json.JSONObject;

/**
 * 采购、销售、电商订单单据查询
 * @author Guobs
 *
 */
@Service
public class OrderDocumentsServiceImpl implements OrderDocumentsService{

	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao ;
	@Autowired
	private SaleOrderDao saleOrderDao ;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao ;
	@Autowired
	private OrderDao orderDao ;
	@Autowired
	private OrderItemDao orderItemDao ;
	@Autowired
	private ApiGoodsConfigDao apiGoodsConfigDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseRoot queryOrder(JSONObject jsonData, Long merchantId) throws Exception {
		
		ResponseRoot responseRoot = new ResponseRoot();		
		// 根据商家id查询商家信息
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		if (merchantInfoModel==null) {
			responseRoot.setmCode("9999");
			responseRoot.setMessage("根据商家id没有查询到商家信息");
			return responseRoot;
		}		
		//单据类型编码 采购订单 101 、销售订单 102 、 电商订单103
		String orderType = jsonData.getString("orderType");
		
		String pageNoStr = null;
		String pageSizeStr = null;
		
		if (jsonData.get("pageNo")!=null
				&&StringUtils.isNotBlank(jsonData.getString("pageNo"))) {
			pageNoStr=jsonData.getString("pageNo");
		}
		if (jsonData.get("pageSize")!=null
				&&StringUtils.isNotBlank(jsonData.getString("pageSize"))) {
			pageSizeStr=jsonData.getString("pageSize");
		}
		if (jsonData.get("pageNo") == null || StringUtils.isBlank(pageNoStr)) {
			pageNoStr="1";
		}
		if (jsonData.get("pageSize") == null|| StringUtils.isBlank(pageSizeStr)) {
			pageSizeStr="100";
		}
		
		Integer pageSize = Integer.valueOf(pageSizeStr);// 每页条数
		Integer startIndex = (Integer.valueOf(pageNoStr)-1)*pageSize;// 开始下标
		
		Map<String, Object> queryMap = JsonToMap(jsonData) ;
		queryMap.put("begin", startIndex) ;
		queryMap.put("pageNo", pageNoStr) ;
		queryMap.put("pageSize", pageSize) ;
		queryMap.put("merchantId", merchantId) ;

		ApiGoodsConfigModel apiGoodsConfigModel = new ApiGoodsConfigModel();
		apiGoodsConfigModel.setMerchantId(merchantId);
		apiGoodsConfigModel.setStatus("1");
		List<MerchandiseInfoModel> merchandiseInfoModels = apiGoodsConfigDao.getConfigMerchandiseList(apiGoodsConfigModel);

		List<String> goodsNoList = new ArrayList<>();
		if (merchandiseInfoModels != null && merchandiseInfoModels.size() > 0) {
			goodsNoList = merchandiseInfoModels.stream().map(MerchandiseInfoModel::getGoodsNo).collect(Collectors.toList());
		}

		//获取采购订单
		if(RepotApiEnum.CGO.getKey().equals(orderType)) {
			responseRoot = getPurchaseOrder(queryMap) ;

			List<PurchaseOrder> orderList = new ArrayList<PurchaseOrder>() ;

			for (Object object : responseRoot.getDataList()) {
				PurchaseOrder purchaseOrder = (PurchaseOrder) object;

				List<OrderGoods> goodsList = new ArrayList<>();

				for (OrderGoods orderGoods : purchaseOrder.getGoodsList()) {
					if (goodsNoList.contains(orderGoods.getGoodsNo())) {
						goodsList.add(orderGoods);
					}
				}

				if (goodsList.size() > 0) {
					purchaseOrder.setGoodsList(goodsList);
					orderList.add(purchaseOrder);
				}

			}

			responseRoot.setDataList(orderList);

		}
		//获取销售订单
		else if(RepotApiEnum.XSO.getKey().equals(orderType)) {
			responseRoot = getSaleOrder(queryMap) ;

			List<SaleOrder> orderList = new ArrayList<>() ;

			for (Object object : responseRoot.getDataList()) {
				SaleOrder saleOrder = (SaleOrder) object;

				List<OrderGoods> goodsList = new ArrayList<>();

				for (OrderGoods orderGoods : saleOrder.getGoodsList()) {
					if (goodsNoList.contains(orderGoods.getGoodsNo())) {
						goodsList.add(orderGoods);
					}
				}

				if (goodsList.size() > 0) {
					saleOrder.setGoodsList(goodsList);
					orderList.add(saleOrder);
				}

			}

			responseRoot.setDataList(orderList);

		}
		//获取电商订单
		else if(RepotApiEnum.DSO.getKey().equals(orderType)) {
			responseRoot = getDSOrder(queryMap) ;

			List<DSOrder> orderList = new ArrayList<>() ;

			for (Object object : responseRoot.getDataList()) {
				DSOrder dsOrder = (DSOrder) object;

				List<DSOrderGoods> goodsList = new ArrayList<>();

				for (DSOrderGoods orderGoods : dsOrder.getGoodsList()) {
					if (goodsNoList.contains(orderGoods.getGoodsNo())) {
						goodsList.add(orderGoods);
					}
				}

				if (goodsList.size() > 0) {
					dsOrder.setGoodsList(goodsList);
					orderList.add(dsOrder);
				}

			}

			responseRoot.setDataList(orderList);
		}
		
		return responseRoot;
	}

	/**
	 * 电商订单
	 * @param queryMap
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResponseRoot getDSOrder(Map<String, Object> queryMap) throws SQLException {
		
		ResponseRoot root = new ResponseRoot() ;
		
		List<OrderDTO> orderList = orderDao.getRepotApiList(queryMap) ;
		
		Integer total = orderDao.getRepotApiListCount(queryMap) ;
		
		List<DSOrder> dsList = new ArrayList<DSOrder>() ;
		//暂时用这个方法
		List<Long>buIdList=new ArrayList<Long>();
		buIdList.add(12L);
		buIdList.add(33L);
		buIdList.add(34L);


		for (OrderDTO orderModel : orderList) {
			DSOrder dSOrder = new DSOrder() ;
			
			dSOrder.setCode(orderModel.getCode());
			dSOrder.setCurrency(orderModel.getCurrency());
			dSOrder.setDeliverDate(orderModel.getDeliverDate());
			dSOrder.setDepotName(orderModel.getDepotName());
			dSOrder.setExOrderId(orderModel.getExOrderId());
			dSOrder.setExternalCode(orderModel.getExternalCode());
			dSOrder.setShopCode(orderModel.getShopCode());
			dSOrder.setShopName(orderModel.getShopName());
			dSOrder.setTradingDate(orderModel.getTradingDate()); 
			
			if(orderModel.getPayment() != null) {
				dSOrder.setPayment(orderModel.getPayment().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			if(orderModel.getTaxes() != null) {
				dSOrder.setTaxes(orderModel.getTaxes().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			if(orderModel.getWayFrtFee() != null) {
				dSOrder.setWayFrtFee(orderModel.getWayFrtFee().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			if(orderModel.getDiscount() != null) {
				dSOrder.setDiscount(orderModel.getDiscount().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			String status = orderModel.getStatus();
			String statusLabel="";
			if (DERP_ORDER.ORDER_STATUS_006.equals(orderModel.getStatus())) {
				statusLabel="已删除";
			}else {
				statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, status);
				if (StringUtils.isBlank(statusLabel))statusLabel=status;
			}				
			
			dSOrder.setStatus(statusLabel);
			dSOrder.setStorePlatformName(orderModel.getStorePlatformNameLabel());
			
			OrderItemModel item = new OrderItemModel() ;
			item.setOrderId(orderModel.getId());
			List<OrderItemModel> itemList = orderItemDao.list(item);
			
			List<DSOrderGoods> goodsList = new ArrayList<DSOrderGoods>() ;
			for (OrderItemModel tempItem : itemList) {
				Long buId = tempItem.getBuId();
				if (!buIdList.contains(buId)) {
					continue;
				}
				DSOrderGoods goods = new DSOrderGoods() ;
				
				goods.setBarcode(tempItem.getBarcode());
				goods.setGoodsName(tempItem.getGoodsName());
				goods.setGoodsNo(tempItem.getGoodsNo());
				goods.setNum(tempItem.getNum());
				
				if(tempItem.getOriginalDecTotal() != null) {
					goods.setAmount(tempItem.getOriginalDecTotal().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				if(tempItem.getOriginalPrice() != null) {
					goods.setPrice(tempItem.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				if(tempItem.getPrice() != null) {
					goods.setDecPrice(tempItem.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				if(tempItem.getDecTotal() != null) {
					goods.setDecTotal(tempItem.getDecTotal().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				goodsList.add(goods) ;
			}
			
			dSOrder.setGoodsList(goodsList);
			
			
			if (goodsList.size()>0)dsList.add(dSOrder);
		}
		
		root.setDataList(dsList);
		root.setTotalCount(total);
		
		return root;
	}

	/**
	 * 销售订单
	 * @param queryMap
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResponseRoot getSaleOrder(Map<String, Object> queryMap) throws SQLException {
		
		ResponseRoot root = new ResponseRoot() ;
		
		List<SaleOrderDTO> saleList = saleOrderDao.getRepotApiList(queryMap) ;
		
		Integer total = saleOrderDao.getRepotApiListCount(queryMap) ;
		
		List<SaleOrder> orderList = new ArrayList<SaleOrder>() ;
		
		for (SaleOrderDTO saleOrderModel : saleList) {
			SaleOrder order = new SaleOrder() ;
			
			order.setAuditDate(saleOrderModel.getAuditDate());
			order.setCode(saleOrderModel.getCode());
			order.setCreateDate(saleOrderModel.getCreateDate());
			order.setCurrency(saleOrderModel.getCurrency());
			order.setCustomerMainId(saleOrderModel.getCustomerMainId());
			order.setInDepotName(saleOrderModel.getInDepotName());
			order.setOutDepotName(saleOrderModel.getOutDepotName());
			order.setPoNo(saleOrderModel.getPoNo());
			order.setMonth(saleOrderModel.getOwnMonth());
			order.setContractNo(saleOrderModel.getContractNo());
			order.setTallyingunit(saleOrderModel.getTallyingUnitLabel());
			
			String status = saleOrderModel.getState();
			String statusLabel="";
			if (DERP_ORDER.ORDER_STATUS_006.equals(saleOrderModel.getState())) {
				statusLabel="已删除";
			}else {
				statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_stateList, status);
				if (StringUtils.isBlank(statusLabel))statusLabel=status;
			}				
			order.setStatus(statusLabel);
			order.setBusinessModel(saleOrderModel.getBusinessModelLabel());
			
			SaleOrderItemModel item = new SaleOrderItemModel() ;
			item.setOrderId(saleOrderModel.getId());
			List<SaleOrderItemModel> itemList = saleOrderItemDao.list(item);

			List<OrderGoods> goodsList = new ArrayList<OrderGoods>() ;
			for (SaleOrderItemModel tempItem : itemList) {
				OrderGoods goods = new OrderGoods() ;
				
				goods.setBarcode(tempItem.getBarcode());
				goods.setGoodsName(tempItem.getGoodsName());
				goods.setGoodsNo(tempItem.getGoodsNo());
				goods.setNum(tempItem.getNum());
				
				if(tempItem.getAmount() != null) {
					goods.setAmount(tempItem.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				if(tempItem.getPrice() != null) {
					goods.setPrice(tempItem.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				goodsList.add(goods) ;
			}
			
			order.setGoodsList(goodsList);
			
			orderList.add(order) ;
		}
		
		root.setDataList(orderList);
		root.setTotalCount(total);
		
		return root;
	}

	/**
	 * 采购订单
	 * @param queryMap
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResponseRoot getPurchaseOrder(Map<String, Object> queryMap) throws Exception {
		
		ResponseRoot root = new ResponseRoot() ;
		
		List<PurchaseOrderDTO> puchaseList = purchaseOrderDao.getRepotApiList(queryMap) ;
		
		Integer total = purchaseOrderDao.getRepotApiListCount(queryMap) ;
		
		List<PurchaseOrder> orderList = new ArrayList<PurchaseOrder>() ;
		
		for (PurchaseOrderDTO purchaseOrderDTO : puchaseList) {
			PurchaseOrder order = new PurchaseOrder() ;
			
			order.setAuditDate(purchaseOrderDTO.getAuditDate());
			order.setCode(purchaseOrderDTO.getCode());
			order.setCreateDate(purchaseOrderDTO.getCreateDate());
			order.setCurrency(purchaseOrderDTO.getCurrency());
			order.setCustomerMainId(purchaseOrderDTO.getSupplierMainId());
			order.setDepotName(purchaseOrderDTO.getDepotName());
			order.setEndDate(purchaseOrderDTO.getEndDate());
			order.setIsEnd(purchaseOrderDTO.getIsEnd());
			order.setPoNo(purchaseOrderDTO.getPoNo());
			order.setTallyingunit(purchaseOrderDTO.getTallyingUnitLabel());
			
			String status = purchaseOrderDTO.getStatus();
			String statusLabel="";
			if (DERP_ORDER.ORDER_STATUS_006.equals(purchaseOrderDTO.getStatus())) {
				statusLabel="已删除";
			}else {
				statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, status);
				if (StringUtils.isBlank(statusLabel))statusLabel=status;
			}							
			order.setStatus(statusLabel);		
			PurchaseOrderItemModel item = new PurchaseOrderItemModel() ;
			item.setPurchaseOrderId(purchaseOrderDTO.getId());			
			List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(item);
			
			List<OrderGoods> goodsList = new ArrayList<OrderGoods>() ;
			for (PurchaseOrderItemModel tempItem : itemList) {
				OrderGoods goods = new OrderGoods() ;
				
				goods.setBarcode(tempItem.getBarcode());
				goods.setGoodsName(tempItem.getGoodsName());
				goods.setGoodsNo(tempItem.getGoodsNo());
				goods.setNum(tempItem.getNum());
				
				if(tempItem.getAmount() != null) {
					goods.setAmount(tempItem.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				if(tempItem.getPrice() != null) {
					goods.setPrice(tempItem.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				
				goodsList.add(goods) ;
			}
			
			order.setGoodsList(goodsList);
			
			orderList.add(order) ;
		}
		
		root.setDataList(orderList);
		root.setTotalCount(total);
		
		return root;
	}

	@SuppressWarnings("unchecked")
	private Map<String,Object> JsonToMap(JSONObject j) {
	    Map<String,Object> map = new HashMap<>();
	    Iterator<String> iterator = j.keys();
	    while(iterator.hasNext())
	    {
	        String key = (String)iterator.next();
	        Object value = j.get(key);
	        map.put(key, value);
	    }
	    return map;
	}
}
