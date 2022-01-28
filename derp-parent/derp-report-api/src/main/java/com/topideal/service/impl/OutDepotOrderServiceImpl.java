package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.RepotApiEnum;
import com.topideal.common.tools.DPMoney;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.storage.AdjustmentInventoryDao;
import com.topideal.dao.storage.AdjustmentTypeDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.order.SaleOutDepotDao;
import com.topideal.dao.order.SaleReturnOdepotDao;
import com.topideal.dao.storage.TakesStockResultsDao;
import com.topideal.dao.order.TransferOutDepotDao;
import com.topideal.dao.order.WayBillItemDao;
import com.topideal.entity.dto.ResponseRoot;
import com.topideal.entity.dto.api10001.Detail;
import com.topideal.entity.dto.api10001.Goods;
import com.topideal.entity.dto.api10001.OutDepotOrder;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.service.OutDepotOrderService;

import net.sf.json.JSONObject;
@Service
public class OutDepotOrderServiceImpl implements OutDepotOrderService{
    
	@Autowired
	public TransferOutDepotDao transferOutDepotDao;
	@Autowired
	public SaleOutDepotDao saleOutDepotDao;
	@Autowired
	public OrderDao orderDao;// 电商订单
	@Autowired
	public WayBillItemDao wayBillItemDao;// 运单表体
	@Autowired
	public AdjustmentInventoryDao adjustmentInventoryDao;
	@Autowired
	public SaleReturnOdepotDao saleReturnOdepotDao;
	@Autowired
	public AdjustmentTypeDao adjustmentTypeDao;
	@Autowired
	public TakesStockResultsDao takesStockResultsDao;	
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;//商家仓库关系表
	
	
	/**
	 * 出库单
	 * */
	public ResponseRoot queryOutDepotOrder(JSONObject jsonData,Long merchantId) throws Exception{
		ResponseRoot responseRoot = null;
		String orderType = (String)jsonData.get("orderType");
		Integer pageNo = (Integer)jsonData.get("pageNo");
		Integer pageSize = (Integer)jsonData.get("pageSize");
		if(pageNo==null||pageNo.intValue()<=0) pageNo = 1;
		if(pageSize==null||pageSize.intValue()<=0) pageSize = 100;
		Integer startIndex = (pageNo-1)*pageSize;
		Map<String, Object> dMerRelParams =new HashMap<String, Object>();
		dMerRelParams.put("merchantId", merchantId);
		
		// 用于存放仓库对应商家和仓库关系表
		Map<Long, DepotMerchantRelMongo>depotMap=new HashedMap();
		List<DepotMerchantRelMongo> findAll = depotMerchantRelMongoDao.findAll(dMerRelParams);
		for (DepotMerchantRelMongo depotMerchantRelMongo : findAll) {
			depotMap.put(depotMerchantRelMongo.getDepotId(), depotMerchantRelMongo);
		}
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("merchantId", merchantId);
		dataMap.put("startTime", (String)jsonData.get("startTime"));
		dataMap.put("endTime", (String)jsonData.get("endTime"));
		dataMap.put("startIndex", startIndex);
		dataMap.put("pageSize", pageSize);
		if(orderType.equals(RepotApiEnum.XSCK.getKey())){//002--销售出库单
			responseRoot = seleOutDepot(dataMap, orderType,depotMap);
		}else if(orderType.equals(RepotApiEnum.XSTC.getKey())){//004--销售退货出
			responseRoot = saleReturnOutdepot(dataMap, orderType,depotMap);
		}else if(orderType.equals(RepotApiEnum.DSDD.getKey())){//005--电商订单出库
			//responseRoot = orderOutdepot(dataMap, orderType);
			responseRoot = orderOutdepot(merchantId, orderType,startIndex,pageSize,(String)jsonData.get("startTime"),(String)jsonData.get("endTime"),depotMap);
		}else if(orderType.equals(RepotApiEnum.DBCK.getKey())){//007--调拨出库单
			responseRoot = transferOutDepot(dataMap, orderType,depotMap);
		}else if(orderType.equals(RepotApiEnum.YJSYC.getKey())
				||orderType.equals(RepotApiEnum.XHC.getKey())
				||orderType.equals(RepotApiEnum.QTC.getKey())){//008月结损益出  010销毁出 011其他出
			responseRoot = adjustmentInventoryOutDepot(dataMap, orderType,depotMap);
		}else if(orderType.equals(RepotApiEnum.PKC.getKey())){//014--盘点结果出
			responseRoot = takesStockResultsOutDepot(dataMap, orderType,depotMap);
		}else if(orderType.equals(RepotApiEnum.XQTZC.getKey())
				 ||orderType.equals(RepotApiEnum.HHBGC.getKey())){//017货号变更出 019效期调整出
			responseRoot = adjustmentType23OutDepot(dataMap, orderType,depotMap);
		}else if(orderType.equals(RepotApiEnum.HHPC.getKey())
				 ||orderType.equals(RepotApiEnum.DHLHC.getKey())){//015-好/坏品互转出  021-大货理货出
			responseRoot = adjustmentType14OutDepot(dataMap, orderType,depotMap);
		}
		
		return responseRoot;
	}
	/**销售出库单
	 * 002--销售出库单
	 * */
    public ResponseRoot seleOutDepot(Map<String, Object> paramMap,String orderType,Map<Long, DepotMerchantRelMongo>depotMap){
    	ResponseRoot responseRoot = new ResponseRoot();
		
		Integer totalCount = saleOutDepotDao.getOutDepotOrderByTimeCount(paramMap);
		responseRoot.setTotalCount(totalCount);
		List<Map<String, Object>> orderList = saleOutDepotDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("sale_order_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("out_depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}
			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("deliver_date"));
			outOrder.setContractNo((String)orderMap.get("contract_no"));
			outOrder.setPoNo((String)orderMap.get("po_no"));
			outOrder.setCustomerCode((String)orderMap.get("customer_code"));
			outOrder.setCustomerName((String)orderMap.get("customer_name"));
			outOrder.setCurrency((String)orderMap.get("currency"));
			String status = (String)orderMap.get("status");
			if(status.equals(DERP.DEL_CODE_006)){
				outOrder.setStatus("02");//01-已出库、02-已删除（指已出库后又删除回滚）
			}else{
				outOrder.setStatus("01");
			}
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = saleOutDepotDao.getOutDepotItemByCodes(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("goods_no"));
			goods.setGoodsName((String)goodMap.get("goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			goods.setPrice(DPMoney.moneyFormat((BigDecimal) goodMap.get("price")));
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setTotalNum(totalNum.intValue());
			goods.setUnit((String)goodMap.get("tallying_unit"));//理货单位 00-托盘 01-箱 02/空-件
			goods.setRemark((String)goodMap.get("remark"));
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			batchParamMap.put("unit",goods.getUnit());
			List<Map<String, Object>> batchListMap = saleOutDepotDao.getItemBatchByCode(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();
				BigDecimal num = (BigDecimal)batchMap.get("transfer_num");
				batch.setNum(num.intValue());//正常数量
				batch.setWornNum(0);//坏品数量
				batch.setExpireNum(0);//过期数量
				batch.setProductionDate((String)batchMap.get("production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("transfer_batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
		return responseRoot;
    }
    /**销售退货出库单
	 * 004--销售退货出库单
	 * */
    public ResponseRoot saleReturnOutdepot(Map<String, Object> paramMap,String orderType, Map<Long, DepotMerchantRelMongo>depotMap){
    	ResponseRoot responseRoot = new ResponseRoot();
    
		//统计数量
 		Integer totalCount = saleReturnOdepotDao.getOutDepotOrderByTimeCount(paramMap);
 		responseRoot.setTotalCount(totalCount);
 		
    	List<Map<String, Object>> orderList = saleReturnOdepotDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("order_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
 			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("out_depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}			
			
			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("return_out_date"));
			outOrder.setCustomerCode((String)orderMap.get("customer_code"));
			outOrder.setCustomerName((String)orderMap.get("customer_name"));
			outOrder.setCurrency((String)orderMap.get("currency"));
			String status = (String)orderMap.get("status");
			if(status.equals(DERP.DEL_CODE_006)){
				outOrder.setStatus("02");//01-已出库、02-已删除
			}else{
				outOrder.setStatus("01");
			}
			
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = saleReturnOdepotDao.getOutDepotItemByCodes(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("out_goods_no"));
			goods.setGoodsName((String)goodMap.get("out_goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			BigDecimal price = (BigDecimal) goodMap.get("price");
			goods.setPrice(price.toString());
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setRemark((String)goodMap.get("remark"));
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			List<Map<String, Object>> batchListMap = saleReturnOdepotDao.getItemBatchByCode(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();
				BigDecimal num = (BigDecimal)batchMap.get("return_num");
				BigDecimal wornNum = (BigDecimal)batchMap.get("worn_num");
				BigDecimal expireNum = (BigDecimal)batchMap.get("expire_num");
				batch.setNum(num==null?0:num.intValue());//正常数量
				batch.setWornNum(wornNum==null?0:wornNum.intValue());//坏品数量
				batch.setExpireNum(expireNum==null?0:expireNum.intValue());//过期数量
				batch.setProductionDate((String)batchMap.get("production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("return_batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
		return responseRoot;
    }
    /**电商订单出库
 	 * 005--电商订单出库
 	 * */
    /* public ResponseRoot orderOutdepot(Map<String, Object> paramMap,String orderType){
     	ResponseRoot responseRoot = new ResponseRoot();

     	//统计数量
 		Integer totalCount = orderDao.getOutDepotOrderByTimeCount(paramMap);
 		responseRoot.setTotalCount(totalCount);
 		
    	List<Map<String, Object>> orderList = orderDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("external_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
			if(depotType.matches("e|b")){//非卓志仓给仓库自编码；卓志仓给OP卓志编码
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}
			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("deliver_date"));
			outOrder.setCustomerName((String)orderMap.get("customer_name"));
			
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = orderDao.getOutDepotItemByCodes(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("goods_no"));
			goods.setGoodsName((String)goodMap.get("goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			BigDecimal price = (BigDecimal)goodMap.get("price");
			goods.setPrice(DPMoney.moneyFormat((BigDecimal)goodMap.get("price")));
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setRemark((String)goodMap.get("remark"));
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			batchParamMap.put("price",price);
			List<Map<String, Object>> batchListMap = orderDao.getItemBatchByCode(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();
				BigDecimal num = (BigDecimal)batchMap.get("num");
				batch.setNum(num.intValue());//正常数量
				batch.setWornNum(0);//坏品数量
				batch.setExpireNum(0);//过期数量
				batch.setProductionDate((String)batchMap.get("production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
 		return responseRoot;
  	  	 
    	 
     }*/
    	 
     
     /**电商订单出库
  	 * 005--电商订单出库
  	 * */
      public ResponseRoot orderOutdepot(Long merchantId, String orderType,Integer startIndex,Integer pageSize,String startTime,String endTime, 
    		  Map<Long, DepotMerchantRelMongo>depotMap) throws Exception{
    	
    	// 根据商家id查询商家信息
  		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
  		if (merchantInfoModel==null) {
  			ResponseRoot responseRoot =new ResponseRoot();
  			responseRoot.setmCode("9999");
  			responseRoot.setMessage("根据商家id没有查询到商家信息");
  			return responseRoot;
  		}

    	  // 获取总数
  		Integer listCount = orderDao.getListCount(merchantInfoModel.getId(),startTime,endTime);
  		// 分页获取调拨入库信息
  		List< Map<String, Object>> orderMapList = orderDao.getListPage(merchantInfoModel.getId(),startTime,endTime,startIndex,pageSize);
  		if (orderMapList==null||orderMapList.size()==0) {  			
  			ResponseRoot responseRoot =new ResponseRoot();
  			responseRoot.setmCode("0000");
  			responseRoot.setMessage("无订单");
  			responseRoot.setTotalCount(listCount);
  			responseRoot.setDataList(orderMapList);			
  			return responseRoot;
  		}	
  		
  		// 用于电商订单 和商品 和批次拼接
  		Map<String, OutDepotOrder>orderCheckMap=new HashedMap();
  		List <Long>ids=new ArrayList<>();
  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  		for (Map<String, Object> orderMap : orderMapList) {
  			Long id = (Long)orderMap.get("id");// 电商订单id
  			String code =  (String)orderMap.get("code");
  			String externalCode = (String) orderMap.get("external_code");
  			String depotCode =  (String)orderMap.get("depot_code");//仓库编码
  			String depotCode1 =  (String)orderMap.get("depot_code1");//仓库自编码
  			String depotName =  (String)orderMap.get("depot_name");// 仓库名称
  			Timestamp deliverDate =  (Timestamp)orderMap.get("deliver_date");
  			String customerCode = (String) orderMap.get("customer_code");// 客户编码
  			String customerName = (String) orderMap.get("customer_name");// 客户
  			String depotType =  (String)orderMap.get("depot_type");// 仓库类型 
  			String status =  (String)orderMap.get("status");// 电商订单状态
			String currency =  (String)orderMap.get("currency");//币种
			String storePlatformName =  (String)orderMap.get("store_platform_name");//平台编码

  			OutDepotOrder outDepotOrder =new OutDepotOrder();
  			outDepotOrder.setTopidealCode(merchantInfoModel.getTopidealCode());
  			outDepotOrder.setCode(code);
  			outDepotOrder.setBusinessNo(externalCode);
  			outDepotOrder.setOrderType(RepotApiEnum.DSDD.getKey());
  			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outDepotOrder.setDepotCode(depotCode);
			}else{
				outDepotOrder.setDepotCode(depotCode1);
			}
			
  			outDepotOrder.setDepotName(depotName);
  			// 发货日期
  			if (deliverDate!=null) {
  				outDepotOrder.setDivergenceDate(sdf.format(deliverDate));
  			}			
  			outDepotOrder.setCustomerCode("");//客户编码 电商订单客户填平台进去
  			outDepotOrder.setCustomerName("");// 客户名称
  			if(status.equals(DERP.DEL_CODE_006)){
  				outDepotOrder.setStatus("02");//01-已出库、02-已删除
  			}else{
  				outDepotOrder.setStatus("01");
  			}
			outDepotOrder.setCurrency(currency);
  			ids.add(id);	// 用于后面的电商订单商品查询
  			orderCheckMap.put(id.toString(), outDepotOrder);
  		}
  		// 查询订单是批次表 拼接成对应的商品 根据 订单id和订单商品 和单价进行拼接
  		Map<String, Goods> itemcheckMap= new HashedMap();
  		// 存放批次的
  		List<Detail>detailList= new ArrayList<>(); 		
  		List<Map<String, Object>> itemMapList =new ArrayList<>();
  		if (ids.size()>0) {
  			// 根据电商订单ids查询 运单和运单信息表
  			itemMapList = wayBillItemDao.getList(ids);
  		}
  		
  		for (Map<String, Object> itemMap : itemMapList) {
  			
  			Long orderId = (Long) itemMap.get("order_id");// 电商订单id
  			Long goodsId =(Long) itemMap.get("goods_id");
  			String goodsNo = (String) itemMap.get("goods_no");
  			String goodsName = (String) itemMap.get("goods_name");
  			String brandName = (String) itemMap.get("brand_name");
  			String commbarcode = (String) itemMap.get("commbarcode");
  			BigDecimal price = (BigDecimal) itemMap.get("price");	
  			if (price==null) {
  				price=new BigDecimal(0);
  			}
  			
  			
  			Date productionDate = (Date) itemMap.get("production_date");
			Date overdueDate = (Date) itemMap.get("overdue_date");
  			String batchNo = (String) itemMap.get("batch_no");
  			
  		  // 归属月份
  			String isExpire=DERP.ISEXPIRE_1;//0 过期 1 未过期 默认未过期
  			Timestamp deliverDate = (Timestamp) itemMap.get("deliver_date");
			if (deliverDate!=null&&overdueDate!=null) {
				String falg = TimeUtils.timeComparisonSize(TimeUtils.parseDay(format.format(overdueDate)), deliverDate);// 失效日期和归属日期比较 返回1  失效日期大于归属日期未过期  0标识已经过期
				if ("0".equals(falg)) {
					isExpire=DERP.ISEXPIRE_0;
				}
			}
			Integer num=0;	// 好品		
			Integer wornNum=0;// 坏品
			Integer expireNum=0;// 过期品
			Integer goodsNum = (Integer) itemMap.get("num");

	  		//如果已过期 直接设置为过期品 否则 盘点好品坏品
			if (DERP.ISEXPIRE_0.equals(isExpire)) {// 已过期
				expireNum=goodsNum;
			}else {//未过期
				num=goodsNum;// 电商订单没有坏品 要么过期 要么是好品
			}			
  			 			
  			String tallyingUnit="";// 电商订单理货单位为null 默认为件
  			String orderKey=orderId+"";//合并成订单的key
  			String key=orderId+","+goodsId+tallyingUnit+price;// 合并成商品的key
  			//批次
  			Detail detail=new Detail();
  			detail.setNum(num);//正常品
  			detail.setWornNum(wornNum);//坏品
  			detail.setExpireNum(expireNum);//过期品
  			if (productionDate!=null) {
  				detail.setProductionDate(format.format(productionDate));
  			}
  			if (overdueDate!=null) {
  				detail.setOverdueDate(format.format(overdueDate));
  			}									
  			detail.setBatchNo(batchNo);			
  			detailList.add(detail);	
  			// 合并成对外的三层结构	(商品批次合并成商品和批次结构)
  			if (itemcheckMap.containsKey(key)) {// 如果key相同覆盖原来的商品数量
  				Goods goods = itemcheckMap.get(key);
  				goods.setTotalNum(goods.getTotalNum()+num+wornNum+expireNum);	
  				List<Detail> goodsDetailList = goods.getDetailList();
  				if (goodsDetailList==null) {
  					goodsDetailList=new ArrayList<>();
  				}
  				goodsDetailList.add(detail);
  				goods.setDetailList(goodsDetailList);
  				itemcheckMap.put(key, goods);
  			}else {// 如果key不存在 存入
  				Goods goods = new Goods();
  				goods.setGoodsNo(goodsNo);
  				goods.setGoodsName(goodsName);
  				goods.setBrankName(brandName);
  				goods.setCommonBarcode(commbarcode);
  				goods.setPrice(DPMoney.moneyFormat(price));
  				goods.setTotalNum(num+wornNum+expireNum);
  				goods.setUnit(tallyingUnit);
  				//inGoods.setRemark(remark);	
  				List<Detail> goodsDetailList = goods.getDetailList();
  				if (goodsDetailList==null) {
  					goodsDetailList=new ArrayList<>();
  				}
  				goodsDetailList.add(detail);
  				goods.setDetailList(goodsDetailList);
  				itemcheckMap.put(key, goods);
  			}
  											
  		}
  		// 合并成对外的三层结构	(订单和商品合并)
  		Set<String> keySet = itemcheckMap.keySet();
  		for (String key : keySet) {			
  			Goods goods = itemcheckMap.get(key);// 商品key 逗号前的是订单号
  			List<String> asList = Arrays.asList(key.split(","));
  			String orderKey = asList.get(0);																		
  			OutDepotOrder outDepotOrder = orderCheckMap.get(orderKey);//获取订单
  			List<Goods> goodList = outDepotOrder.getGoodList();//获取商品
  			if (goodList==null) {
  				goodList=new ArrayList<>();
  			}
  			goodList.add(goods);
  			outDepotOrder.setGoodList(goodList);
  			orderCheckMap.put(orderKey, outDepotOrder);
  			
  		}
  		Collection<OutDepotOrder> values = orderCheckMap.values();
  		
  		List<OutDepotOrder> outDepotOrderList = new ArrayList<OutDepotOrder>(values);
  		List<OutDepotOrder> outDepotOrderNewList = new ArrayList<OutDepotOrder>();
  		for (OutDepotOrder outDepotOrder : outDepotOrderList) {  			
  			List<Goods> goodList2 = outDepotOrder.getGoodList();
  			
  			if (goodList2==null||goodList2.size()==0) {			
  				outDepotOrder.setGoodList(new ArrayList<>());				
  			}else {
  				for (Goods goods : goodList2) {
  					List<Detail> goodsDetailList = goods.getDetailList();
  					if (goodsDetailList==null||goodsDetailList.size()==0) {
  						goods.setDetailList(new ArrayList<>());
  					}
  				}
  			}	
  			/**
  			if ("02".equals(outDepotOrder.getStatus())) {//删除状态的表头可以为空对象 其他过滤掉表体为空的数据 过滤掉表体不是 需要的事业部订单
  				if (goodList2!=null&&goodList2.size()>0) {
  					outDepotOrderNewList.add(outDepotOrder);
				}
			}else {
				if (goodList2!=null&&goodList2.size()>0) {
					outDepotOrderNewList.add(outDepotOrder);
				}
			}**/
  			if (goodList2!=null&&goodList2.size()>0) {
				outDepotOrderNewList.add(outDepotOrder);
			}
  			
  		}
  		ResponseRoot responseRoot =new ResponseRoot();
  		responseRoot.setTotalCount(listCount);
  		responseRoot.setDataList(outDepotOrderNewList);
  		
  		return responseRoot;
     	  
      }
     
     
     
     
     
	/**调拨出库单
	 * 007--调拨出库单
	 * */
    public ResponseRoot transferOutDepot(Map<String, Object> paramMap,String orderType,Map<Long, DepotMerchantRelMongo>depotMap){
    	ResponseRoot responseRoot = new ResponseRoot();
    	
		Integer totalCount = transferOutDepotDao.getOutDepotOrderByTimeCount(paramMap);
		responseRoot.setTotalCount(totalCount);
    	List<Map<String, Object>> orderList = transferOutDepotDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("transfer_order_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("out_depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}
			
			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("transfer_date"));
			outOrder.setContractNo((String)orderMap.get("contract_no"));
			outOrder.setCustomerCode((String)orderMap.get("merchant_code"));
			outOrder.setCustomerName((String)orderMap.get("in_customer_name"));
			outOrder.setCurrency(DERP.CURRENCYCODE_CNY);//调拨默认人民币
			String status = (String)orderMap.get("status");
			if(status.equals(DERP.DEL_CODE_006)){
				outOrder.setStatus("02");//01-已出库、02-已删除
			}else{
				outOrder.setStatus("01");
			}
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = transferOutDepotDao.getOutDepotItemByCodes(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("out_goods_no"));
			goods.setGoodsName((String)goodMap.get("out_goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			goods.setPrice(DPMoney.moneyFormat((BigDecimal)goodMap.get("price")));
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setUnit((String)goodMap.get("tallying_unit"));//理货单位 00-托盘 01-箱 02/空-件
			goods.setRemark((String)goodMap.get("remark"));
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			batchParamMap.put("unit",goods.getUnit());
			List<Map<String, Object>> batchListMap = transferOutDepotDao.getItemBatchByCode(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();
				BigDecimal num = (BigDecimal)batchMap.get("transfer_num");
				batch.setNum(num.intValue());//正常数量
				batch.setWornNum(0);//坏品数量
				batch.setExpireNum(0);//过期数量
				batch.setProductionDate((String)batchMap.get("production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("transfer_batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
    	return responseRoot;
    }
    
    /**库存调整单出
	 * 008-月结损益出  010-销毁出 011-其他出
	 * */
    public ResponseRoot adjustmentInventoryOutDepot(Map<String, Object> paramMap,String orderType,Map<Long, DepotMerchantRelMongo>depotMap){
    	ResponseRoot responseRoot = new ResponseRoot();
		/**orderType:008月结损益出  010销毁出 011其他出
		 * model业务类别: 1-销毁 2-月结损益 3-其他出入
		 */
		if(orderType.equals(RepotApiEnum.YJSYC.getKey())){
			paramMap.put("model", DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2);//月结损益出
		}else if(orderType.equals(RepotApiEnum.XHC.getKey())){
			paramMap.put("model", DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_1);//销毁出
		}else if(orderType.equals(RepotApiEnum.QTC.getKey())){
			paramMap.put("model", DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_3);//其他出
		}
		
		Integer totalCount = adjustmentInventoryDao.getOutDepotOrderByTimeCount(paramMap);
		responseRoot.setTotalCount(totalCount);
    	List<Map<String, Object>> orderList = adjustmentInventoryDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("source_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
			
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}

			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("source_time"));
			String status = (String)orderMap.get("status");
			if(status.equals(DERP.DEL_CODE_006)){
				outOrder.setStatus("02");//01-已出库、02-已删除
			}else{
				outOrder.setStatus("01");
			}
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = adjustmentInventoryDao.getOutDepotItemByCodes(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("goods_no"));
			goods.setGoodsName((String)goodMap.get("goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			goods.setPrice("0.000");
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setUnit((String)goodMap.get("tallying_unit"));//理货单位 00-托盘 01-箱 02/空-件
			goods.setRemark((String)goodMap.get("remark"));
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			batchParamMap.put("unit",goods.getUnit());
			List<Map<String, Object>> batchListMap = adjustmentInventoryDao.getItemBatchByCode(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();
				//是否坏品 0-否 1-是
				String isDamage = (String)batchMap.get("is_damage");
				BigDecimal num = (BigDecimal)batchMap.get("adjust_total");
				String sourceTime = (String)batchMap.get("source_time");//归属日期
				String overdueDate = (String)batchMap.get("overdue_date");//失效日期
				long sourceTimeLong =0;
				long overdueDateLong =0;
				if(StringUtils.isNotBlank(sourceTime)&&StringUtils.isNotBlank(overdueDate)){
					sourceTimeLong = TimeUtils.parseFullTime(sourceTime+" 23:59:59").getTime();
					overdueDateLong = TimeUtils.parseDay(overdueDate).getTime();
				}
				batch.setNum(0);//正常数量
				batch.setWornNum(0);//坏品数量
				batch.setExpireNum(0);//过期数量
				if(StringUtils.isNotBlank(isDamage)&&isDamage.equals(DERP.ISDAMAGE_1)){//坏品
					batch.setWornNum(num.intValue());//坏品数量
				}else if(StringUtils.isNotBlank(sourceTime)&&StringUtils.isNotBlank(overdueDate)
						&&sourceTimeLong>overdueDateLong){//过期
					batch.setExpireNum(num.intValue());//过期数量
				}else{
					batch.setNum(num.intValue());//正常数量
				}
				batch.setProductionDate((String)batchMap.get("production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("old_batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
		return responseRoot;
    }
    /**盘点结果出
     * 014 盘亏出
	 * */
    public ResponseRoot takesStockResultsOutDepot(Map<String, Object> paramMap,String orderType,Map<Long, DepotMerchantRelMongo>depotMap){
    	ResponseRoot responseRoot = new ResponseRoot();
		
		Integer totalCount = takesStockResultsDao.getOutDepotOrderByTimeCount(paramMap);
		responseRoot.setTotalCount(totalCount);
    	List<Map<String, Object>> orderList = takesStockResultsDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("source_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}

			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("source_time"));
			String status = (String)orderMap.get("status");
			if(status.equals(DERP.DEL_CODE_006)){
				outOrder.setStatus("02");//01-已出库、02-已删除
			}else{
				outOrder.setStatus("01");
			}
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = takesStockResultsDao.getOutDepotItemByCodes(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("goods_no"));
			goods.setGoodsName((String)goodMap.get("goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			goods.setPrice("0.000");
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setUnit((String)goodMap.get("tally_unit"));//理货单位 00-托盘 01-箱 02/空-件
			goods.setRemark((String)goodMap.get("remark"));
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			batchParamMap.put("unit",goods.getUnit());
			List<Map<String, Object>> batchListMap = takesStockResultsDao.getItemBatchByCode(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();

				String isDamage = (String)batchMap.get("is_damage");
				BigDecimal num = (BigDecimal)batchMap.get("deficient_num");
				Timestamp pushTime = (Timestamp)batchMap.get("source_time");//归属日期
				String overdueDate = (String)batchMap.get("overdue_date");//失效日期
				long sourceTimeLong =0;
				long overdueDateLong =0;
				if(pushTime!=null&&StringUtils.isNotBlank(overdueDate)){
					sourceTimeLong = pushTime.getTime();
					overdueDateLong = TimeUtils.parseDay(overdueDate).getTime();
				}
				batch.setNum(0);//正常数量
				batch.setWornNum(0);//坏品数量
				batch.setExpireNum(0);//过期数量
				if(StringUtils.isNotBlank(isDamage)&&isDamage.equals(DERP.ISDAMAGE_1)){//坏品
					batch.setWornNum(num.intValue());//坏品数量
				}else if(pushTime!=null&&StringUtils.isNotBlank(overdueDate)
						&&sourceTimeLong>overdueDateLong){//过期
					batch.setExpireNum(num.intValue());//过期数量
				}else{
					batch.setNum(num.intValue());//正常数量
				}
				batch.setProductionDate((String)batchMap.get("production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
		return responseRoot;
   }
    
    /**类型调整
     * 017货号变更出 019效期调整出 
	 */
    public ResponseRoot adjustmentType23OutDepot(Map<String, Object> paramMap,String orderType,Map<Long, DepotMerchantRelMongo>depotMap){
    	ResponseRoot responseRoot = new ResponseRoot();
		/**orderType:017货号变更出 019效期调整出
		 * model业务类别: 1.好坏品互转 2.货号变更 3效期调整,4大货理货
		 */
		if(orderType.equals(RepotApiEnum.HHBGC.getKey())){
			paramMap.put("model", DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2);//货号变更出
		}else if(orderType.equals(RepotApiEnum.XQTZC.getKey())){
			paramMap.put("model", DERP_STORAGE.ADJUSTMENTTYPE_MODEL_3);//效期调整出
		}
		
		Integer totalCount = adjustmentTypeDao.getOutDepotOrderByTimeCount(paramMap);
		responseRoot.setTotalCount(totalCount);
    	List<Map<String, Object>> orderList = adjustmentTypeDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("source_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}
			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("push_time"));
			String status = (String)orderMap.get("status");
			if(status.equals(DERP.DEL_CODE_006)){
				outOrder.setStatus("02");//01-已出库、02-已删除
			}else{
				outOrder.setStatus("01");
			}
			
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = adjustmentTypeDao.getOutDepotItemByCodes(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("old_goods_no"));
			goods.setGoodsName((String)goodMap.get("old_goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			goods.setPrice("0.000");
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setUnit((String)goodMap.get("tallying_unit"));//理货单位 00-托盘 01-箱 02/空-件
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			batchParamMap.put("unit",goods.getUnit());
			List<Map<String, Object>> batchListMap = adjustmentTypeDao.getItemBatchByCode(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();

				String isDamage = (String)batchMap.get("is_damage");
				BigDecimal num = (BigDecimal)batchMap.get("adjust_total");
				Timestamp pushTime = (Timestamp)batchMap.get("push_time");//归属日期
				String overdueDate = (String)batchMap.get("old_overdue_date");//失效日期
				long sourceTimeLong =0;
				long overdueDateLong =0;
				if(pushTime!=null&&StringUtils.isNotBlank(overdueDate)){
					sourceTimeLong = pushTime.getTime();
					overdueDateLong = TimeUtils.parseDay(overdueDate).getTime();
				}
				batch.setNum(0);//正常数量
				batch.setWornNum(0);//坏品数量
				batch.setExpireNum(0);//过期数量
				if(StringUtils.isNotBlank(isDamage)&&isDamage.equals("1")){//坏品
					batch.setWornNum(num.intValue());//坏品数量
				}else if(pushTime!=null&&StringUtils.isNotBlank(overdueDate)
						&&sourceTimeLong>overdueDateLong){//过期
					batch.setExpireNum(num.intValue());//过期数量
				}else{
					batch.setNum(num.intValue());//正常数量
				}
				batch.setProductionDate((String)batchMap.get("old_production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("old_overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("old_batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
		return responseRoot;
    }
    /**类型调整
     * 015-好/坏品互转出  021-大货理货出
	 * */
    public ResponseRoot adjustmentType14OutDepot(Map<String, Object> paramMap,String orderType,Map<Long, DepotMerchantRelMongo>depotMap){
    	ResponseRoot responseRoot = new ResponseRoot();
		/**orderType:015-好/坏品互转出  021-大货理货出
		 * model业务类别: 1.好坏品互转 2.货号变更 3效期调整,4大货理货
		 */
		if(orderType.equals(RepotApiEnum.HHPC.getKey())){
			paramMap.put("model", DERP_STORAGE.ADJUSTMENTTYPE_MODEL_1);//好坏品互转出
		}else if(orderType.equals(RepotApiEnum.DHLHC.getKey())){
			paramMap.put("model", DERP_STORAGE.ADJUSTMENTTYPE_MODEL_4);//大货理货出
		}
		//查询出库单
		Integer totalCount = adjustmentTypeDao.getOutDepotOrderByTimeCount(paramMap);
		responseRoot.setTotalCount(totalCount);
    	List<Map<String, Object>> orderList = adjustmentTypeDao.getOutDepotOrderByTime(paramMap);
    	List<String> codes = new ArrayList<String>();
    	if(orderList==null||orderList.size()<=0){
    		return responseRoot;
    	}
    	List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();
		for(Map<String, Object> orderMap:orderList){
			OutDepotOrder outOrder = new OutDepotOrder();
			outOrder.setTopidealCode((String)orderMap.get("topideal_code"));
			outOrder.setCode((String)orderMap.get("code"));
			outOrder.setBusinessNo((String)orderMap.get("source_code"));
			outOrder.setOrderType(orderType);
			String depotType = (String)orderMap.get("type");
 			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				outOrder.setDepotCode((String)orderMap.get("dcode"));
			}else{
				outOrder.setDepotCode((String)orderMap.get("depot_code"));
			}
			outOrder.setDepotName((String)orderMap.get("name"));
			outOrder.setDivergenceDate((String)orderMap.get("push_time"));
			String status = (String)orderMap.get("status");
			if(status.equals(DERP.DEL_CODE_006)){
				outOrder.setStatus("02");//01-已出库、02-已删除
			}else{
				outOrder.setStatus("01");
			}
			outOrderList.add(outOrder);
			codes.add(outOrder.getCode());
		}
		//查询所有出库单商品
		Map<String, List<Goods>> codeGoodsMap =new HashMap<String, List<Goods>>();//存放所有出库单的商品 key=code
		List<Map<String, Object>> goodsListMap = adjustmentTypeDao.getOutDepotItemByCodes14(codes);
		for(Map<String, Object> goodMap:goodsListMap){
			Goods goods = new Goods();
			String code = (String)goodMap.get("code");
			goods.setGoodsNo((String)goodMap.get("goods_no"));
			goods.setGoodsName((String)goodMap.get("goods_name"));
			goods.setBrankName((String)goodMap.get("brank_name"));
			goods.setCommonBarcode((String)goodMap.get("commbarcode"));
			goods.setPrice("0.000");
			BigDecimal totalNum = (BigDecimal)goodMap.get("total_num");
			goods.setTotalNum(totalNum.intValue());
			goods.setUnit((String)goodMap.get("tallying_unit"));//理货单位 00-托盘 01-箱 02/空-件
			
			//查询批次数据放到goods去
			Map<String, Object> batchParamMap = new HashMap<String, Object>();
			batchParamMap.put("code",code);
			batchParamMap.put("goodsNo",goods.getGoodsNo());
			batchParamMap.put("unit",goods.getUnit());
			List<Map<String, Object>> batchListMap = adjustmentTypeDao.getItemBatchByCode14(batchParamMap);
			List<Detail> detailList = new ArrayList<Detail>();
			for(Map<String, Object> batchMap:batchListMap){
				Detail batch = new Detail();
				String isDamage = (String)batchMap.get("is_damage");
				BigDecimal num = (BigDecimal)batchMap.get("adjust_total");
				Timestamp pushTime = (Timestamp)batchMap.get("push_time");//归属日期
				String overdueDate = (String)batchMap.get("overdue_date");//失效日期
				long sourceTimeLong =0;
				long overdueDateLong =0;
				if(pushTime!=null&&StringUtils.isNotBlank(overdueDate)){
					sourceTimeLong = pushTime.getTime();
					overdueDateLong = TimeUtils.parseDay(overdueDate).getTime();
				}
				batch.setNum(0);//正常数量
				batch.setWornNum(0);//坏品数量
				batch.setExpireNum(0);//过期数量
				if(StringUtils.isNotBlank(isDamage)&&isDamage.equals("1")){//坏品
					batch.setWornNum(num.intValue());//坏品数量
				}else if(pushTime!=null&&StringUtils.isNotBlank(overdueDate)
						&&sourceTimeLong>overdueDateLong){//过期
					batch.setExpireNum(num.intValue());//过期数量
				}else{
					batch.setNum(num.intValue());//正常数量
				}
				batch.setProductionDate((String)batchMap.get("production_date"));//生产日期
				batch.setOverdueDate((String)batchMap.get("overdue_date"));//失效日期
				batch.setBatchNo((String)batchMap.get("old_batch_no"));//批次号
				detailList.add(batch);
			}
			goods.setDetailList(detailList);
			
			List<Goods> listTemp = null;
			if(codeGoodsMap.get(code)==null){
				listTemp = new ArrayList<Goods>();
			}else{
				listTemp = codeGoodsMap.get(code);
			}
			listTemp.add(goods);
			codeGoodsMap.put(code, listTemp);
		}
    	//循环出库单拼装商品和批次信息
		for(OutDepotOrder order:outOrderList){
			List<Goods> goodsListTemp = codeGoodsMap.get(order.getCode());
			if(goodsListTemp==null) goodsListTemp = new ArrayList<Goods>();
			order.setGoodList(goodsListTemp);
		}
		responseRoot.setDataList(outOrderList);
		return responseRoot;
    }
	
}
