package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.gss.GSSUtils;
import com.topideal.api.gss.g001.HisStockQueryRequest;
import com.topideal.api.gss.g002.ReadBatchStocksRequest;
import com.topideal.api.ofc.OFCUtils;
import com.topideal.api.ofc.c01.OFCCurrentStoreRequest;
import com.topideal.api.op.OPUtils;
import com.topideal.api.op.v2_14.CurrentStoreRequest;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchandiseScheduleMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchandiseScheduleMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.InventoryRealTimeSnapshotService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 实时库存快照 
 * 
 * @author 联想302 baols 2018/06/11
 */
@Service
public class InventoryRealTimeSnapshotServiceImpl implements InventoryRealTimeSnapshotService {

	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(InventoryRealTimeSnapshotServiceImpl.class);
    //实时库存快照 
	@Autowired
	private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao;
	// MongoDb的商家信息
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
    //仓库信息
	@Autowired
	private DepotInfoMongoDao  depotInfoMongoDao;
	
    //商品信息
	@Autowired
	private MerchandiseInfoMogoDao  merchandiseInfoMogoDao;
	 //商品信息
	 @Autowired
	 private MerchandiseScheduleMongoDao merchandiseScheduleMongoDao;
	 @Autowired
	 private DepotMerchantRelMongoDao depotMerchantRelMongoDao;//商家仓库关系表


	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201302300, model = DERP_LOG_POINT.POINT_13201302300_Label)
	public boolean synsInventoryRealTimeInterface(String json, String keys, String topics, String tags) throws Exception {
		
		logger.info("=============获取实时库存快照开始=================" + json);
        Map<String, Object> params =new HashMap<String, Object>();
        List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(params);
        if(merchantList ==null||merchantList.size()<=0){
        	logger.debug("mongdb商家数量为0,结束执行");
			throw new RuntimeException("mongdb商家数量为0,结束执行");
        }
        params =new HashMap<String, Object>();
        List<DepotInfoMongo>  depotList= depotInfoMongoDao.findAll(params);
		if(depotList==null||depotList.size()<=0){
			logger.debug("mongdb仓库数量为0,结束执行");
			throw new RuntimeException("mongdb仓库数量为0,结束执行");
		}

		//循环获取商家的每个仓库实时库存
		for(MerchantInfoMongo merchantInfo:merchantList){
			for(DepotInfoMongo depotInfo:depotList){
				
				Map<String, Object> dMerRelParams =new HashMap<String, Object>();
				dMerRelParams.put("merchantId", merchantInfo.getMerchantId());
				dMerRelParams.put("depotId", depotInfo.getDepotId());
				DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(dMerRelParams);
				//下推指令的仓库,非菜鸟
				if(depotMerchantRel!=null&&!depotInfo.getCode().matches("WMS_360_01|WMS_360_02|WTX001|CNHZ001")&&depotInfo.getWarehouseId()!=null
						&&DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRel.getIsInOutInstruction())){
					logger.info("获取GSS批次库存快照======商家:"+merchantInfo.getName()+",仓库:"+depotInfo.getName());
					this.synsDepotStockForGSSBatch(merchantInfo, depotInfo);
				}
			}
		}
		logger.info("=============获取实时库存快照结束=================");
		return true;
	}
	
	/*@Override
	@SystemServiceLog(point = "13201302300", model = "Gss菜鸟仓实时库存快照 ")
	public boolean synsInventoryRealTimeGss(String json, String keys, String topics, String tags) throws Exception {
		logger.info("=============Gss获取实时 库存快照开始=================" + json);
		
		JSONObject jsonData = JSONObject.fromObject(json);
		//若无指定快照日期则默认获取当前日期快照
		String snapshotDate = (String) jsonData.get("snapshotDate");
		if(StringUtils.isBlank(snapshotDate)){
			snapshotDate = TimeUtils.formatDay();
		}
        Map<String, Object> params =new HashMap<String, Object>();
        List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(params);
        if(merchantList ==null||merchantList.size()<=0){
        	logger.debug("mongdb商家数量为0,结束执行");
			throw new RuntimeException("mongdb商家数量为0,结束执行");
        }
        params =new HashMap<String, Object>();
        List<DepotInfoMongo>  depotList= depotInfoMongoDao.findAll(params);
		if(depotList==null||depotList.size()<=0){
			logger.debug("mongdb仓库数量为0,结束执行");
			throw new RuntimeException("mongdb仓库数量为0,结束执行");
		}
		//循环获取商家的每个仓库实时库存
		for(MerchantInfoMongo merchantInfo:merchantList){
			for(DepotInfoMongo depotInfo:depotList){
				
				Map<String, Object> dMerRelParams =new HashMap<String, Object>();
				dMerRelParams.put("merchantId", merchantInfo.getMerchantId());
				dMerRelParams.put("depotId", depotInfo.getDepotId());
				DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(dMerRelParams);
				
				if(depotMerchantRel!=null&&depotInfo.getCode().matches("WMS_360_01|WMS_360_02|WTX001|CNHZ001")){//保税仓，非代销仓,菜鸟仓从gss接口获取快照
					
					logger.info("获取实时库存快照结GSS======商家:"+merchantInfo.getName()+",仓库:"+depotInfo.getName());
					this.synsGetDepotStockForGSS(merchantInfo, depotInfo, snapshotDate);
				}
			}
		}
		logger.info("=============Gss获取实时库存快照结束=================");
		return true;
	}*/

	/**
	 * Gss实时库存查询
	 * */
	/*public void synsGetDepotStockForGSS(MerchantInfoMongo merchantInfo,DepotInfoMongo depotInfo,String snapshotDate)throws Exception {
		String result = null;
		int pageNo = 1;
		int pageSize = 100;
		int totalPage = 0;//总页数
		*//**
		 * 仓库名称	   GSS仓库ID	  OP仓库编码	               仓库自编码
		   菜鸟二期天运2仓	300	    WMS_360_01	ERPWMS_360_0104
		    菜鸟一期金外滩	301	    WMS_360_02	ERPWMS_360_0205
		 * *//*
		List<Integer> WarehouseIds = new ArrayList<Integer>();
		WarehouseIds.add(depotInfo.getWarehouseId().intValue());
		HisStockQueryRequest requestVo = new HisStockQueryRequest();
		requestVo.setWarehouseIds(WarehouseIds);
		requestVo.setMerchantCode(merchantInfo.getTopidealCode());
		requestVo.setSnapshotDate(snapshotDate);
		requestVo.setPageNo(pageNo);
		requestVo.setPageSize(pageSize);
		requestVo.setTenantCode("zhuozhi");
		//调Gss查询实时快照
		result = GSSUtils.currentStoreQuery(requestVo);
		logger.info(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS实时库存第"+pageNo+"返回结果:"+result);
		if(StringUtils.isBlank(result)){
			logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS实时库存失败返回结果为空,结束执行");
			throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS实时库存失败返回结果为空,结束执行");
		}
		JSONObject resultJson = JSONObject.fromObject(result);
		totalPage = resultJson.getInt("pageCount");
		if(totalPage>0){
			 //保存实时库存
			this.saveGSSBatch(resultJson, merchantInfo, depotInfo);
		}
		if(totalPage > 1) {
			pageNo++;//从第二页开始取
			while(pageNo<=totalPage){
				requestVo.setPageNo(pageNo);
				result = GSSUtils.currentStoreQuery(requestVo);
				logger.info(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS实时库存第"+pageNo+"返回结果:"+result);
				if(StringUtils.isBlank(result)){
					logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS实时库存失败返回结果为空,结束执行");
					throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS实时库存失败返回结果为空,结束执行");
				}
				 //保存实时库存
				resultJson = JSONObject.fromObject(result);
				this.saveGSSBatch(resultJson, merchantInfo, depotInfo);
				pageNo++;
			}
		}
   }*/
	/**
	 * 保存op实时库存批次
	 */
	/*public void saveGSSBatch(JSONObject resultJson,MerchantInfoMongo merchantInfo,
			DepotInfoMongo depotInfo) throws Exception {
		JSONArray batchList = JSONArray.fromObject(resultJson.get("dabaoBatchStockVos"));
		for(int i=0;i<batchList.size();i++){
			JSONObject batch = batchList.optJSONObject(i);
			String productName = (String) batch.get("productName");//是	商品名称
			String ean13 = (String) batch.get("ean13");//是	商品条码
			String batchNo = (String) batch.get("batchNo");//是	菜鸟大宝批次编码 与op、ofc批次不一致暂时无用
			String gcode = (String) batch.get("gCode");//是	货号
			String productCode = (String) batch.get("productCode");//是	商品编码OPG号--
			String productionTime = (String) batch.get("productionDate");//是 生产日期
			String expireTime = (String) batch.get("expireDate");//是  失效时间
			int totalNum = batch.getInt("totalNum");//是  实物库存（总库存）
			//int availableNum = batch.getInt("availableNum");//可用库存
			//int totalOccupyNum = batch.getInt("totalOccupyNum");//总占用
			String storeCode = (String) batch.get("storeCode");//仓库编码

			*//**
			 1.1健太商家主体-1000000204 只取storeCode为STE_17OR09661的库存信息；
			 1.2卓烨商家主体-0000134 只取storeCode为STORE_11180270、STORE_12093326的库存信息；
			 1.3嘉宝商家主体-0000138 只取storeCode为STORE_11180270的库存信息；
			 1.4广旺商家主体-1000000645 只取storeCode为STORE_11180270的库存信息；
			 * *//*
			if(merchantInfo.getTopidealCode().equals("1000000204")&&!storeCode.matches("STORE_1709661")) {
				continue;
			}
			if(merchantInfo.getTopidealCode().equals("0000134")&&!storeCode.matches("STORE_11180270|STORE_12093326")) {
				continue;
			}
			if(merchantInfo.getTopidealCode().equals("0000138")&&!storeCode.matches("STORE_11180270")) {
				continue;
			}
			if(merchantInfo.getTopidealCode().equals("1000000645")&&!storeCode.matches("STORE_11180270")) {
				continue;
			}

			int lockNum = batch.getInt("lockNum");//锁定库存
			int availableNum = totalNum-lockNum;
			String stockStatus = (String) batch.get("stockStatus");//库存状态：正品|残次|残品|效期残
			String calDate = batch.getString("calDate");//是	快照日期
            if(StringUtils.isBlank(stockStatus)){
            	throw new RuntimeException("货号："+gcode+",批次号："+batchNo+"库存状态为空,结束执行");
            }
            if(!stockStatus.matches("正品|残次|残品|效期残")){
            	throw new RuntimeException("货号："+gcode+",批次号："+batchNo+"状态:"+stockStatus+",库存状态不正确,结束执行");
            }
			
			Map<String, Object> params =new HashMap<String, Object>();
			params.put("goodsNo", gcode);
			params.put("merchantId", merchantInfo.getMerchantId());
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(params);
			InventoryRealTimeSnapshotModel inventoryModel = new InventoryRealTimeSnapshotModel();
			inventoryModel.setMerchantId(merchantInfo.getMerchantId());
			inventoryModel.setMerchantName(merchantInfo.getName());
			inventoryModel.setDepotId(depotInfo.getDepotId());
			inventoryModel.setDepotName(depotInfo.getName());
			if(merchandise!=null){
				inventoryModel.setGoodsId(merchandise.getMerchandiseId());
			}
			inventoryModel.setGoodsNo(gcode);
			inventoryModel.setGoodsName(productName);
			inventoryModel.setBatchNo(batchNo);
			if(StringUtils.isNotBlank(productionTime)){
				inventoryModel.setProductionDate(TimeUtils.strToSqlDate(productionTime));
			}
			if(StringUtils.isNotBlank(expireTime)){
				inventoryModel.setOverdueDate(TimeUtils.strToSqlDate(expireTime));
			}
			if(merchandise!=null){
			     inventoryModel.setBarcode(merchandise.getBarcode());
			}else{
				inventoryModel.setBarcode(ean13);
			}
			inventoryModel.setOpgCode(productCode);
			inventoryModel.setSnapshotSource(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_GSS_CN);
			inventoryModel.setCreateDate(TimeUtils.parseDay(calDate+" 00:00:00"));
			inventoryModel.setRequestTime(TimeUtils.getNow());
			inventoryModel.setQty(totalNum);
			inventoryModel.setRealStockNum(availableNum);
			inventoryModel.setLockStockNum(lockNum);
			//inventoryModel.setRealFrozenStockNum(totalOccupyNum);
			if(stockStatus.trim().equals("正品")){//好品  0 正常品  1 残次品
				inventoryModel.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
			}else if(stockStatus.trim().matches("残次|残品|效期残")){
				inventoryModel.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
			}
			Long id = inventoryRealTimeSnapshotDao.save(inventoryModel);
			if(id==null){
				logger.error("新增实时库存失败");
				throw new Exception("新增实时库存失败");
			}
		}
   }*/
	/**
	 * Gss实时库存查询-非菜鸟
	 * */
	public void synsDepotStockForGSSBatch(MerchantInfoMongo merchantInfo,DepotInfoMongo depotInfo)throws Exception {
		String result = null;
		int pageNo = 1;
		int pageSize = 100;
		int totalPage = 0;//总页数
		List<Long> warehouseIdList = new ArrayList<Long>();
		warehouseIdList.add(depotInfo.getWarehouseId());
		
		ReadBatchStocksRequest requestVo = new ReadBatchStocksRequest();
		requestVo.setWarehouseIds(warehouseIdList);
		requestVo.setMerchantCode(merchantInfo.getTopidealCode());
		requestVo.setPageNo(pageNo);
		requestVo.setPageSize(pageSize);
		if(depotInfo.getCode().equals("820")){
			requestVo.setTenantCode("zhitong");
		}else{
			requestVo.setTenantCode("zhuozhi");
		}

		//调Gss查询实时快照
		result = GSSUtils.currentStoreQueryBatch(requestVo);
		logger.info(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS批次库存第"+pageNo+"返回结果:"+result);
		if(StringUtils.isBlank(result)){
			logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS批次库存失败返回结果为空,结束执行");
			throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS批次库存失败返回结果为空,结束执行");
		}
		JSONObject resultJson = JSONObject.fromObject(result);
		totalPage = resultJson.getInt("pageCount");
		if(totalPage>0){
			 //保存实时库存
			this.saveGSSBatchCondition(resultJson, merchantInfo, depotInfo);
		}
		if(totalPage > 1) {
			pageNo++;//从第二页开始取
			while(pageNo<=totalPage){
				requestVo.setPageNo(pageNo);
				result = GSSUtils.currentStoreQueryBatch(requestVo);
				logger.info(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS批次库存第"+pageNo+"返回结果:"+result);
				if(StringUtils.isBlank(result)){
					logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS批次库存失败返回结果为空,结束执行");
					throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求GSS批次库存失败返回结果为空,结束执行");
				}
				 //保存实时库存
				resultJson = JSONObject.fromObject(result);
				this.saveGSSBatchCondition(resultJson, merchantInfo, depotInfo);
				pageNo++;
			}
		}
   }
	/**
	 * 保存GSS批次库存
	 * @throws Exception
	 */
	public void saveGSSBatchCondition(JSONObject resultJson,MerchantInfoMongo merchantInfo,
			DepotInfoMongo depotInfo) throws Exception {
		JSONArray batchList = JSONArray.fromObject(resultJson.get("batchStockVos"));
		for(int i=0;i<batchList.size();i++){
			JSONObject batch = batchList.optJSONObject(i);
			String productCode = (String) batch.get("productCode");//是	商品编码OPG号
			String expireTime = (String) batch.get("expireTime");//是  失效时间
			String productionTime = (String) batch.get("productionTime");//是 生产日期
			String batchNo = (String) batch.get("originBatchCode");//是	wms原始批次
			Integer stockTypeInt = (Integer) batch.get("stockType");//是 库存类型:0好品，1坏品
			Integer realStockNum = (Integer) batch.get("realStockNum");//是  库存数量
			Integer lockStockNum = (Integer) batch.get("lockStockNum");//是 锁定库存
			Integer realFrozenStockNum = (Integer) batch.get("realFrozenStockNum");//是 冻结量
			String uom = (String) batch.get("uom");//否 单位 P：托，C: 箱，B：件
			
			//库存总量=库存数量+锁定数量
			int totalNum = realStockNum+lockStockNum;
			//可用量=库存数量-冻结数量；
			int availableStockNum = realStockNum-realFrozenStockNum;
			
            if(stockTypeInt==null||(stockTypeInt.intValue()!=0&&stockTypeInt.intValue()!=1)){
            	throw new RuntimeException("库存类型不正确-结束执行,OPG号："+productCode+",批次号："+batchNo+"库存类型:"+stockTypeInt);
            }
            
			//opg查询商品附表对应货号
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("uniques", productCode);
            queryMap.put("merchantId", merchantInfo.getMerchantId());
            MerchandiseScheduleMongo merchandiseScheduleMongo = merchandiseScheduleMongoDao.findOne(queryMap);
        	//查询商品
            MerchandiseInfoMogo merchandise = null;
            if(merchandiseScheduleMongo!=null) {
            	Map<String, Object> params =new HashMap<String, Object>();
    			params.put("merchandiseId", merchandiseScheduleMongo.getGoodsId());
    			merchandise = merchandiseInfoMogoDao.findOne(params);
            }
			
			InventoryRealTimeSnapshotModel inventoryModel = new InventoryRealTimeSnapshotModel();
			inventoryModel.setMerchantId(merchantInfo.getMerchantId());
			inventoryModel.setMerchantName(merchantInfo.getName());
			inventoryModel.setDepotId(depotInfo.getDepotId());
			inventoryModel.setDepotName(depotInfo.getName());
			if(merchandise!=null){
				inventoryModel.setGoodsId(merchandise.getMerchandiseId());
				inventoryModel.setGoodsNo(merchandise.getGoodsNo());
				inventoryModel.setGoodsName(merchandise.getName());
				inventoryModel.setBarcode(merchandise.getBarcode());
			}
			inventoryModel.setBatchNo(batchNo);
			if(StringUtils.isNotBlank(productionTime)){
				inventoryModel.setProductionDate(TimeUtils.strToSqlDate(productionTime));
			}
			if(StringUtils.isNotBlank(expireTime)){
				inventoryModel.setOverdueDate(TimeUtils.strToSqlDate(expireTime));
			}
			inventoryModel.setOpgCode(productCode);
			inventoryModel.setSnapshotSource(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_GSS);
			inventoryModel.setCreateDate(TimeUtils.getNow());
			inventoryModel.setRequestTime(TimeUtils.getNow());
			inventoryModel.setQty(totalNum);
			inventoryModel.setRealStockNum(availableStockNum);
			inventoryModel.setLockStockNum(lockStockNum);
			inventoryModel.setRealFrozenStockNum(realFrozenStockNum);
		
			if(stockTypeInt.intValue()==0){//库存类型:0好品，1坏品
				inventoryModel.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
			}else if(stockTypeInt.intValue()==1){
				inventoryModel.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
			}
			if(depotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
				if(StringUtils.isBlank(uom)||uom.equals("B")) {//单位 P：托，C: 箱，B：件
					inventoryModel.setUnit(DERP.ORDER_TALLYINGUNIT_02);
				}else if(uom.equals("C")) {
					inventoryModel.setUnit(DERP.ORDER_TALLYINGUNIT_01);
				}else if(uom.equals("P")) {
					inventoryModel.setUnit(DERP.ORDER_TALLYINGUNIT_00);
				}
			}
			Long id = inventoryRealTimeSnapshotDao.save(inventoryModel);
			if(id==null){
				logger.error("新增实时库存失败");
				throw new Exception("新增实时库存失败");
			}
		}
   }
	//计算总页数
	private int getTotalPage(int totalCount,int pageSize){
		int totalPage = 0;
		if(totalCount%pageSize == 0){
			totalPage = totalCount/pageSize;
		}else{
			totalPage = totalCount/pageSize + 1;
		}
		return totalPage;
	}

}
