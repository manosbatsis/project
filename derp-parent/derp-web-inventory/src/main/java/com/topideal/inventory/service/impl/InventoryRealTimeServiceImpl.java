package com.topideal.inventory.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.taobao.api.domain.WmsInventoryQueryItem;
import com.taobao.api.domain.WmsInventoryQueryItemlist;
import com.taobao.api.request.WlbWmsInventoryQueryRequest;
import com.taobao.api.response.WlbWmsInventoryQueryResponse;
import com.topideal.api.tb.TaoBaoUtils;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import net.sf.json.JSONNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.gss.GSSUtils;
import com.topideal.api.gss.g002.ReadBatchStocksRequest;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.dto.InventoryRealTimeDTO;
import com.topideal.inventory.service.InventoryRealTimeService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *库存--实时库存service实现类
 */
@Service
public class InventoryRealTimeServiceImpl implements InventoryRealTimeService {


    //仓库信息dao
    @Autowired
    private  DepotInfoMongoDao depotInfoMongoDao;
    @Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;//商家仓库关系表
    //MongoDb的商家信息
    @Autowired
    private  MerchantInfoMongoDao   merchantInfoMongoDao;
    @Autowired
    private  MerchandiseInfoMogoDao MerchandiseInfoMogoDao;
    //MongoDb商品附表
    @Autowired
    private  MerchandiseScheduleMongoDao merchandiseScheduleMongoDao;
    
    @Autowired
    private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao  merchandiseInfoMogoDao;
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;

	/**
     * 把两个数组组成一个map
     * @param keys   键数组
     * @param values 值数组
     * @return 键值对应的map
     */
    private Map<String, String> toMap(Object[] keys, Object[] values) {
        if (keys != null && values != null && keys.length == values.length) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (int i = 0; i < keys.length; i++) {
                map.put((String) keys[i], values[i].toString());
            }
            return map;
        }
        return null;
    }

    /**
     * 实时库存查询
     */

    /**
     * 实时库存查询
     */
	@Override
	public InventoryRealTimeDTO searchInventoryRealTime(Long merchantId, DepotInfoMongo depotMongo,
			String goodsNo, Integer pageNo, Integer pageSize,String merchantName,String topidealCode,String barcode) throws Exception {
		//封装接口返回的对象信息
		InventoryRealTimeDTO inventRealTimeModel= new InventoryRealTimeDTO();
		List<InventoryRealTimeDTO> stockInfoList = new ArrayList<InventoryRealTimeDTO>();
		Integer totalCount = 0;
		//查询商家仓库关联表
		Map<String, Object> dMerRelParams =new HashMap<String, Object>();
		dMerRelParams.put("merchantId", merchantId);
		dMerRelParams.put("depotId", depotMongo.getDepotId());
		DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(dMerRelParams);
		
		 if(depotMerchantRel==null||depotMongo.getWarehouseId()==null
 				||!DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRel.getIsInOutInstruction())) {
			inventRealTimeModel.setList(stockInfoList);
	        inventRealTimeModel.setPageNo(pageNo);
	        inventRealTimeModel.setPageSize(pageSize);
	        inventRealTimeModel.setTotal(totalCount);
			return inventRealTimeModel;
		}
		 //由于不支持条码查询   查询实时库存是用条码转换成货号 用货号转成主数据id
		 List<MerchandiseInfoMogo> goodsList=null;
		 
		 Map<String, Object> goodsMap=new HashMap<>();
		 if (StringUtils.isNotBlank(goodsNo))goodsMap.put("goodsNo", goodsNo);
		 if (StringUtils.isNotBlank(barcode))goodsMap.put("barcode", barcode);
		 goodsMap.put("merchantId", merchantId);
		 if (StringUtils.isNotBlank(goodsNo)||StringUtils.isNotBlank(barcode)) {
			 goodsList = merchandiseInfoMogoDao.findAll(goodsMap);
			 if (goodsList==null||goodsList.size()==0) {
				 inventRealTimeModel.setList(stockInfoList);
			     inventRealTimeModel.setPageNo(pageNo);
			     inventRealTimeModel.setPageSize(pageSize);
			     inventRealTimeModel.setTotal(totalCount);
			     return inventRealTimeModel;
			}
		 }
		 if (goodsList==null)goodsList=new ArrayList<>();
		 
		 
		 List<String> productCodeList = new ArrayList<String>();
		 for (MerchandiseInfoMogo merchandiseInfoMogo : goodsList) {
			 Map<String, Object> scheduleMap = new HashMap<String, Object>();
				scheduleMap.put("goodsNo", merchandiseInfoMogo.getGoodsNo());
				scheduleMap.put("merchantId", merchantId);
				List<MerchandiseScheduleMongo>  scheduleMongoList = merchandiseScheduleMongoDao.findAll(scheduleMap);
				if(scheduleMongoList!=null&&scheduleMongoList.size()>0) {
					for(MerchandiseScheduleMongo scheduleMongo:scheduleMongoList) {
						productCodeList.add(scheduleMongo.getUniques());
					}
				}	
		 }
		
		List<Long> warehouseIdList = new ArrayList<Long>();
		warehouseIdList.add(depotMongo.getWarehouseId());
		ReadBatchStocksRequest requestVo = new ReadBatchStocksRequest();
		requestVo.setWarehouseIds(warehouseIdList);
		requestVo.setMerchantCode(topidealCode);
		requestVo.setProductCodes(productCodeList);
		requestVo.setPageNo(pageNo);
		requestVo.setPageSize(pageSize);
		if(depotMongo.getCode().equals("820")){
			requestVo.setTenantCode("zhitong");
		}else {
			requestVo.setTenantCode("zhuozhi");
		}
		//调Gss查询实时快照
		String result = GSSUtils.currentStoreQueryBatch(requestVo);
		JSONObject jsonObject = JSONObject.fromObject(result);	
		totalCount =jsonObject.getInt("totalCount");
		// 获取实时库存商信息
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("batchStockVos"));
        if(jsonArray!=null&&jsonArray.size()>0) {
			 for(int i=0;i<jsonArray.size();i++) {
					JSONObject batchObj = jsonArray.optJSONObject(i);
					String productCode = (String) batchObj.get("productCode");//POG号
					Integer stockTypeInt = (Integer) batchObj.get("stockType");//是 库存类型:0好品，1坏品
					Integer realStockNum = (Integer) batchObj.get("realStockNum");//是  库存数量
					Integer lockStockNum = (Integer) batchObj.get("lockStockNum");//是 锁定库存
					Integer realFrozenStockNum = (Integer) batchObj.get("realFrozenStockNum");//是 冻结量
					String uom = (String) batchObj.get("uom");//否 单位 P：托，C: 箱，B：件
					if(StringUtils.isBlank(uom)||uom.equals("B")) {
						uom = DERP.ORDER_TALLYINGUNIT_02;
					}else if(uom.equals("C")) {
						uom = DERP.ORDER_TALLYINGUNIT_01;
					}else if(uom.equals("P")) {
						uom = DERP.ORDER_TALLYINGUNIT_00;
					}
					//库存总量=库存数量+锁定数量
					int totalNum = realStockNum+lockStockNum;
					//可用量=库存数量-冻结数量；
					int availableStockNum = realStockNum-realFrozenStockNum;
					
					//查找经分销商品
					Map<String, Object> scheduleMap = new HashMap<String, Object>();
					scheduleMap.put("uniques", productCode);
				    scheduleMap.put("merchantId", merchantId);
					MerchandiseScheduleMongo  scheduleMongo = merchandiseScheduleMongoDao.findOne(scheduleMap);
					MerchandiseInfoMogo merchandiseInfoMogo = null;
					if(scheduleMongo!=null) {
						Map<String, Object> merchdisdMap = new HashMap<String, Object>();
						merchdisdMap.put("merchandiseId", scheduleMongo.getGoodsId());
						merchandiseInfoMogo = MerchandiseInfoMogoDao.findOne(merchdisdMap);
					}
					
					// 实例化仓库对象保存实时库存查询信息
					InventoryRealTimeDTO inventRealTimeDTO = new InventoryRealTimeDTO();
					String expireTime = (String) batchObj.get("expireTime");
					if(StringUtils.isNotBlank(expireTime)){
						inventRealTimeDTO.setOverdueDate(TimeUtils.formatToDay(expireTime));//失效日期
					}
					
					String productionTime = (String) batchObj.get("productionTime");
					if(StringUtils.isNotBlank(productionTime)){
						inventRealTimeDTO.setProductionDate(TimeUtils.formatToDay(productionTime));//失效日期
					}
					inventRealTimeDTO.setDepotId(depotMongo.getDepotId());//仓库id
					inventRealTimeDTO.setDepotName(depotMongo.getName()); //仓库名称
					inventRealTimeDTO.setBatchNo(batchObj.getString("originBatchCode"));//WMS原始批次号
					inventRealTimeDTO.setQty(totalNum);//库存总量
					if(stockTypeInt.intValue()==0) {
						inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型stockType
					}else if(stockTypeInt.intValue()==1) {
						inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//库存类型stockType
					}
					inventRealTimeDTO.setUom(uom);
					inventRealTimeDTO.setRealFrozenStockNum(realFrozenStockNum);//库存冻结数量 realFrozenStockNum
					inventRealTimeDTO.setLockStockNum(lockStockNum);//库存锁定数量 lockStockNum
					inventRealTimeDTO.setRealStockNum(availableStockNum);//库存可用数量 realStockNum
					if(merchandiseInfoMogo!=null) {
						inventRealTimeDTO.setBarcode(merchandiseInfoMogo.getBarcode());
						inventRealTimeDTO.setGoodsName(merchandiseInfoMogo.getName());//商品名称
						inventRealTimeDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());//商品货号
					}
					inventRealTimeDTO.setOpgCode(productCode);
					inventRealTimeDTO.setMerchantName(merchantName);
					inventRealTimeDTO.setLpn((String)batchObj.get("lpn"));
					inventRealTimeDTO.setOverDays(TimeUtils.differentDays(TimeUtils.parseFullTime(expireTime), new Date()));
					stockInfoList.add(inventRealTimeDTO);
			  }
        }
        inventRealTimeModel.setList(stockInfoList);
        inventRealTimeModel.setPageNo(pageNo);
        inventRealTimeModel.setPageSize(pageSize);
        inventRealTimeModel.setTotal(totalCount);
		return inventRealTimeModel;
	}
	
	@Override
	public List<Map<String, Object>> exportInventoryRealTimeMap(InventoryRealTimeDTO   model,String batchNo) throws Exception {
		List<Map<String, Object>> returnMapList = new ArrayList<Map<String, Object>>();
		List<InventoryRealTimeDTO>	modelList=model.getList();
		if(modelList != null && modelList.size() > 0) {
			for (InventoryRealTimeDTO invRelModel : modelList) {
				Map<String, Object> paraMap = new HashMap<String, Object>();
		    	paraMap.put("merchantName", invRelModel.getMerchantName());
				paraMap.put("depotName", invRelModel.getDepotName());
				paraMap.put("goodsNo", invRelModel.getGoodsNo());
				paraMap.put("goodsName", invRelModel.getGoodsName());
				paraMap.put("barcode", invRelModel.getBarcode());
				paraMap.put("opgCode", invRelModel.getOpgCode());
				paraMap.put("productionDate", invRelModel.getProductionDate());
				paraMap.put("overdueDate", invRelModel.getOverdueDate());
				paraMap.put("batchNo", invRelModel.getBatchNo());
				paraMap.put("qty", invRelModel.getQty());
				paraMap.put("realStockNum", invRelModel.getRealStockNum());
				paraMap.put("lockStockNum", invRelModel.getLockStockNum());
				paraMap.put("realFrozenStockNum", invRelModel.getRealFrozenStockNum());
				paraMap.put("overDays", invRelModel.getOverDays());
				paraMap.put("stockType",invRelModel.getStockTypeLabel());
				paraMap.put("uom", invRelModel.getUomLabel());
				paraMap.put("lpn", invRelModel.getLpn());
				paraMap.put("createDate", TimeUtils.getNow());
				returnMapList.add(paraMap);
			}
		}
		return returnMapList;
	}

	
	/**
	 *  封装导出op和ofc实时库存信息
	 * @param depotId
	 * @param goodsId
	 * @param pageNo
	 * @param pageSize
	 * @param userInfoModel
	 * @return
	 */
	@Override
	public InventoryRealTimeDTO searchInventoryRealTimeForExport(Long merchantId, DepotInfoMongo depotMongo, String goodsNo,
			String merchantName, String topidealCode) throws Exception {
		//封装接口返回的对象信息
		InventoryRealTimeDTO inventRealTimeModel= new InventoryRealTimeDTO();
		List<InventoryRealTimeDTO> stockInfoList = new ArrayList<InventoryRealTimeDTO>();
		Integer totalCount = 0;
		Integer pageCount = 0;
		Integer pageSize = 100;
		 List<String> productCodeList = new ArrayList<String>();
		 if(StringUtils.isNotBlank(goodsNo)) {
			Map<String, Object> scheduleMap = new HashMap<String, Object>();
			scheduleMap.put("goodsNo", goodsNo);
			scheduleMap.put("merchantId", merchantId);
			List<MerchandiseScheduleMongo>  scheduleMongoList = merchandiseScheduleMongoDao.findAll(scheduleMap);
			if(scheduleMongoList!=null&&scheduleMongoList.size()>0) {
				for(MerchandiseScheduleMongo scheduleMongo:scheduleMongoList) {
					productCodeList.add(scheduleMongo.getUniques());
				}
			}
		 }	
		 
		List<Long> warehouseIdList = new ArrayList<Long>();
		warehouseIdList.add(depotMongo.getWarehouseId());
		ReadBatchStocksRequest requestVo = new ReadBatchStocksRequest();
		requestVo.setWarehouseIds(warehouseIdList);
		requestVo.setMerchantCode(topidealCode);
		requestVo.setProductCodes(productCodeList);
		requestVo.setPageNo(1);
		requestVo.setPageSize(pageSize);
		if(depotMongo.getCode().equals("820")){
			requestVo.setTenantCode("zhitong");
		}else {
			requestVo.setTenantCode("zhuozhi");
		}
		//调Gss查询实时快照
		String result = GSSUtils.currentStoreQueryBatch(requestVo);
		if(StringUtils.isBlank(result)) {
			return inventRealTimeModel;
		}
		JSONObject jsonObject = JSONObject.fromObject(result);	
		totalCount =jsonObject.getInt("totalCount");
		pageCount =jsonObject.getInt("pageCount");
		if(pageCount<=0) {
			return inventRealTimeModel;
		}
		for(int pageNo=1;pageNo<=pageCount.intValue();pageNo++) {
			requestVo.setPageNo(pageNo);
			result = GSSUtils.currentStoreQueryBatch(requestVo);
			jsonObject = JSONObject.fromObject(result);	
			// 获取实时库存商信息
			JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("batchStockVos"));
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject batchObj = jsonArray.optJSONObject(i);
				String productCode = (String) batchObj.get("productCode");//POG号
				Integer stockTypeInt = (Integer) batchObj.get("stockType");//是 库存类型:0好品，1坏品
				Integer realStockNum = (Integer) batchObj.get("realStockNum");//是  库存数量
				Integer lockStockNum = (Integer) batchObj.get("lockStockNum");//是 锁定库存
				Integer realFrozenStockNum = (Integer) batchObj.get("realFrozenStockNum");//是 冻结量
				String uom = (String) batchObj.get("uom");//否 单位 P：托，C: 箱，B：件
				if(StringUtils.isBlank(uom)||uom.equals("B")) {
					uom = DERP.ORDER_TALLYINGUNIT_02;
				}else if(uom.equals("C")) {
					uom = DERP.ORDER_TALLYINGUNIT_01;
				}else if(uom.equals("P")) {
					uom = DERP.ORDER_TALLYINGUNIT_00;
				}
				//库存总量=库存数量+锁定数量
				int totalNum = realStockNum+lockStockNum;
				//可用量=库存数量-冻结数量；
				int availableStockNum = realStockNum-realFrozenStockNum;
				
				//查找经分销商品
				Map<String, Object> scheduleMap = new HashMap<String, Object>();
				scheduleMap.put("uniques", productCode);
				scheduleMap.put("merchantId", merchantId);
				MerchandiseScheduleMongo  scheduleMongo = merchandiseScheduleMongoDao.findOne(scheduleMap);
				MerchandiseInfoMogo merchandiseInfoMogo = null;
				if(scheduleMongo!=null) {
					Map<String, Object> merchdisdMap = new HashMap<String, Object>();
					merchdisdMap.put("merchandiseId", scheduleMongo.getGoodsId());
					merchandiseInfoMogo = MerchandiseInfoMogoDao.findOne(merchdisdMap);
				}
				
				// 实例化仓库对象保存实时库存查询信息
				InventoryRealTimeDTO inventRealTimeDTO = new InventoryRealTimeDTO();
				String expireTime = (String) batchObj.get("expireTime");
				if(StringUtils.isNotBlank(expireTime)){
					inventRealTimeDTO.setOverdueDate(TimeUtils.formatToDay(expireTime));//失效日期
				}
				
				String productionTime = (String) batchObj.get("productionTime");
				if(StringUtils.isNotBlank(productionTime)){
					inventRealTimeDTO.setProductionDate(TimeUtils.formatToDay(productionTime));//失效日期
				}
				inventRealTimeDTO.setDepotId(depotMongo.getDepotId());//仓库id
				inventRealTimeDTO.setDepotName(depotMongo.getName()); //仓库名称
				inventRealTimeDTO.setBatchNo(batchObj.getString("originBatchCode"));//WMS原始批次号
				inventRealTimeDTO.setQty(totalNum);//库存总量
				if(stockTypeInt.intValue()==0) {
					inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型stockType
				}else if(stockTypeInt.intValue()==1) {
					inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//库存类型stockType
				}
				inventRealTimeDTO.setUom(uom);
				inventRealTimeDTO.setRealFrozenStockNum(realFrozenStockNum);//库存冻结数量 realFrozenStockNum
				inventRealTimeDTO.setLockStockNum(lockStockNum);//库存锁定数量 lockStockNum
				inventRealTimeDTO.setRealStockNum(availableStockNum);//库存可用数量 realStockNum
				if(merchandiseInfoMogo!=null) {
					inventRealTimeDTO.setGoodsName(merchandiseInfoMogo.getName());//商品名称
					inventRealTimeDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());//商品货号
					inventRealTimeDTO.setBarcode(merchandiseInfoMogo.getBarcode());
				}
				inventRealTimeDTO.setOpgCode(productCode);
				inventRealTimeDTO.setMerchantName(merchantName);
				inventRealTimeDTO.setLpn((String)batchObj.get("lpn"));
				inventRealTimeDTO.setOverDays(TimeUtils.differentDays(TimeUtils.parseFullTime(expireTime), new Date()));
				stockInfoList.add(inventRealTimeDTO);
		    }
		}

		inventRealTimeModel.setList(stockInfoList);
		return inventRealTimeModel;
	}
	/**
	 * 查询实时库存
	 * */
	public InventoryRealTimeDTO realTimeByPage(Long merchantId, InventoryRealTimeDTO inventoryRealTimeDTO) throws Exception {
		// 获取商家信息
		Map<String,Object> queryMap = new HashMap<String,Object>() ;
		queryMap.put("merchantId", merchantId) ;
		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(queryMap);

		/*查询所有启用的店铺*/
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("status", "1");//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("isSycnMerchandise", DERP_SYS.MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_1);//是否同步菜鸟商品 1-是
		merchantShopRelMap.put("depotId", inventoryRealTimeDTO.getDepotId());//仓库ID
		merchantShopRelMap.put("merchantId", merchantId);
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);
		if(merchantShopRelList==null||merchantShopRelList.size()<=0){//商家店铺数量为0,结束执行
			InventoryRealTimeDTO inventRealTimeModel= new InventoryRealTimeDTO();
			inventRealTimeModel.setList(new ArrayList<>());
			inventRealTimeModel.setPageNo(inventoryRealTimeDTO.getPageNo());
			inventRealTimeModel.setPageSize(inventoryRealTimeDTO.getPageSize());
			inventRealTimeModel.setTotal(0);
			return inventRealTimeModel;
		}
		InventoryRealTimeDTO inventRealTimeModel= new InventoryRealTimeDTO();
		
		 //由于不支持条码查询   查询实时库存是用条码转换成货号 用货号转成主数据id
		 List<MerchandiseInfoMogo> goodsList=null;
		 
		 Map<String, Object> goodsMap=new HashMap<>();
		 if (StringUtils.isNotBlank(inventoryRealTimeDTO.getGoodsNo()))goodsMap.put("goodsNo", inventoryRealTimeDTO.getGoodsNo());
		 if (StringUtils.isNotBlank(inventoryRealTimeDTO.getBarcode()))goodsMap.put("barcode", inventoryRealTimeDTO.getBarcode());
		 goodsMap.put("merchantId", merchantId);
		 if (StringUtils.isNotBlank(inventoryRealTimeDTO.getGoodsNo())||StringUtils.isNotBlank(inventoryRealTimeDTO.getBarcode())) {
			 goodsList = merchandiseInfoMogoDao.findAll(goodsMap);
			 if (goodsList==null||goodsList.size()==0) {
				 	inventRealTimeModel= new InventoryRealTimeDTO();
					inventRealTimeModel.setList(new ArrayList<>());
					inventRealTimeModel.setPageNo(inventoryRealTimeDTO.getPageNo());
					inventRealTimeModel.setPageSize(inventoryRealTimeDTO.getPageSize());
					inventRealTimeModel.setTotal(0);
					return inventRealTimeModel;
			}
		 }
		if (goodsList==null)goodsList=new ArrayList<>();				
		for(MerchantShopRelMongo mrchantShopRelMongo:merchantShopRelList){
			//获取仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", mrchantShopRelMongo.getDepotId());
			DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
			if(depotInfo==null){
				throw new RuntimeException("仓库id"+mrchantShopRelMongo.getDepotId()+":"+mrchantShopRelMongo.getDepotName()+"没找到对应仓库信息,结束执行");
			}
			// 多个货号循环
			if (StringUtils.isNotBlank(inventoryRealTimeDTO.getGoodsNo())||StringUtils.isNotBlank(inventoryRealTimeDTO.getBarcode())) {
				for (MerchandiseInfoMogo merchandiseInfoMogo : goodsList) {
					inventoryRealTimeDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
					inventRealTimeModel = this.synsGetDepotStockForALi(inventoryRealTimeDTO,mrchantShopRelMongo,merchantInfo,depotInfo);
				}
			}else {
				inventRealTimeModel = this.synsGetDepotStockForALi(inventoryRealTimeDTO,mrchantShopRelMongo,merchantInfo,depotInfo);
			}
			
		}
		return inventRealTimeModel;
	}

	/**
	 * 封装接口返回的对象信息
	 * */
	public InventoryRealTimeDTO synsGetDepotStockForALi(InventoryRealTimeDTO inventoryRealTimeDTO,MerchantShopRelMongo merchantShopRelMongo,MerchantInfoMongo merchantInfo,DepotInfoMongo depotInfoMongo)throws Exception {
		InventoryRealTimeDTO inventRealTimeModel= new InventoryRealTimeDTO();
		List<InventoryRealTimeDTO> stockInfoList = new ArrayList<InventoryRealTimeDTO>();
		Integer totalCount = 0;//总数
		WlbWmsInventoryQueryRequest requestVo = new WlbWmsInventoryQueryRequest();
		requestVo.setType(2L);//库存查询类型：2- 批次库存，库存按商品批次维度划分
		requestVo.setPageNo((long) inventoryRealTimeDTO.getPageNo());
		requestVo.setPageSize((long) inventoryRealTimeDTO.getPageSize());
		if(StringUtils.isNotBlank(inventoryRealTimeDTO.getGoodsNo())){
			requestVo.setItemId(inventoryRealTimeDTO.getGoodsNo());// 商品货号
		}

		//查询实时快照
		WlbWmsInventoryQueryResponse result = TaoBaoUtils.executeTBInventoryQuery(requestVo, merchantShopRelMongo.getSessionKey(),
				merchantShopRelMongo.getAppKey(), merchantShopRelMongo.getAppSecret());
		//请求阿里异常，记录日志
		if(result==null) {
			throw new RuntimeException(merchantInfo.getName()+","+depotInfoMongo.getName()+"请求阿里实时库存失败返回结果为空,结束执行");
		}
		String errorCode = result.getErrorCode();
		if(StringUtils.isNotBlank(errorCode)) {
			throw new RuntimeException(merchantInfo.getName()+","+depotInfoMongo.getName()+"请求阿里实时库存失败,结束执行");
		}
		JSONObject resultJson = JSONObject.fromObject(result);
		totalCount = (Integer) resultJson.get("totalCount");//总条数
		Integer totalPage = this.getTotalPage(totalCount, inventoryRealTimeDTO.getPageSize());//获取总页数
		if(totalCount<=0){
			inventRealTimeModel.setList(stockInfoList);
			inventRealTimeModel.setPageNo(inventoryRealTimeDTO.getPageNo());
			inventRealTimeModel.setPageSize(inventoryRealTimeDTO.getPageSize());
			inventRealTimeModel.setTotal(totalCount);
			return inventRealTimeModel;
		}
		// 获取实时库存商品信息
		JSONArray itemList =  JSONArray.fromObject(resultJson.get("itemList"));

		for(int i=0;i<itemList.size();i++) {
			JSONObject item = itemList.optJSONObject(i);
			JSONObject batch = (JSONObject) item.get("item");

			Integer inventoryType = (Integer) batch.get("inventoryType");  //库存类型，1为正品，101为残次品
			if(inventoryType!=1 && inventoryType!=101){ // 只取库存类型为正品和残次品的数据
				continue;
			}
			String storeCode = (String) batch.get("storeCode");//仓库编码
			String itemId = (String) batch.get("itemId");  //菜鸟商品货号
			String batchCode = (String) batch.get("batchCode");//库存批次号
			Integer totalNum = (Integer) batch.get("quantity");//库存数量

				/**
				 1.1健太商家主体-1000000204 只取storeCode为STE_17OR09661的库存信息；
				 1.2卓烨商家主体-0000134 只取storeCode为STORE_11180270、STORE_12093326的库存信息；
				 1.3嘉宝商家主体-0000138 只取storeCode为STORE_11180270的库存信息；
				 1.4广旺商家主体-1000000645 只取storeCode为STORE_11180270的库存信息；
				 * */
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
				Integer lockNum = (Integer) batch.get("lockQuantity");//锁定数量
				Integer availableNum = totalNum-lockNum;//库存总量=库存数量-锁定数量

				Map<String, Object> params =new HashMap<String, Object>();
				params.put("goodsNo", itemId);
				params.put("merchantId", merchantInfo.getMerchantId());
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(params);

				// 实例化仓库对象保存实时库存查询信息
				InventoryRealTimeDTO inventRealTimeDTO = new InventoryRealTimeDTO();				
				if(batch.has("produceDate")){
					if(batch.get("produceDate") instanceof JSONNull){
						inventRealTimeDTO.setProductionDate(null);
					}else{
						if(batch.get("produceDate")!=null){
							JSONObject produceDateJb = (JSONObject) batch.get("produceDate");
							Date produceDate=(Date) JSONObject.toBean(produceDateJb, Date.class);
							inventRealTimeDTO.setProductionDate(TimeUtils.format(produceDate, "yyyy-MM-dd HH:mm:ss"));//生产日期
						}
					}
				}
				if(batch.has("dueDate")){
					if(batch.get("dueDate") instanceof JSONNull){
						inventRealTimeDTO.setOverdueDate(null);
					}else {
						if(batch.get("dueDate")!=null){
							JSONObject dueDateJb = (JSONObject) batch.get("dueDate");//失效日期
							Date dueDate=(Date) JSONObject.toBean(dueDateJb, Date.class);
							inventRealTimeDTO.setOverdueDate(TimeUtils.format(dueDate, "yyyy-MM-dd HH:mm:ss"));//失效日期
							// 临保天数
							inventRealTimeDTO.setOverDays(TimeUtils.differentDays(dueDate, new Date()));
						}
					}
				}

				inventRealTimeDTO.setDepotId(depotInfoMongo.getDepotId());//仓库id
				inventRealTimeDTO.setDepotName(depotInfoMongo.getName()); //仓库名称
				inventRealTimeDTO.setBatchNo(batchCode);//批次号
				inventRealTimeDTO.setQty(totalNum.intValue());//库存总量
				if(inventoryType==1){//库存类型，1为正品，101为残次品
					inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型 0 好品  1 坏品
				}else if(inventoryType==101){
					inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
				}
				inventRealTimeDTO.setRealFrozenStockNum(0);//库存冻结数量
				inventRealTimeDTO.setLockStockNum(lockNum.intValue());//库存锁定数量
				inventRealTimeDTO.setRealStockNum(availableNum.intValue());//库存可用数量 realStockNum
				if(merchandise!=null) {
					inventRealTimeDTO.setBarcode(merchandise.getBarcode());
					inventRealTimeDTO.setGoodsName(merchandise.getName());//商品名称
					inventRealTimeDTO.setGoodsNo(merchandise.getGoodsNo());//商品货号
					inventRealTimeDTO.setOpgCode(merchandise.getUnique());//OPG
				}
				inventRealTimeDTO.setMerchantName(merchantInfo.getName());


				stockInfoList.add(inventRealTimeDTO);
			}
		inventRealTimeModel.setList(stockInfoList);
		inventRealTimeModel.setPageNo(inventoryRealTimeDTO.getPageNo());
		inventRealTimeModel.setPageSize(inventoryRealTimeDTO.getPageSize());
		inventRealTimeModel.setTotal(totalCount);
		return inventRealTimeModel;
	}
	/**
	 *  封装导出菜鸟实时库存信息
	 * @param depotId
	 * @param goodsId
	 * @param pageNo
	 * @param pageSize
	 * @param userInfoModel
	 * @return
	 */
	@Override
	public InventoryRealTimeDTO searchRookieInventoryRealForExport(Long merchantId,String merchantName,String topidealCode,DepotInfoMongo depotMongo, InventoryRealTimeDTO inventoryRealTimeDTO) throws Exception {
		//封装接口返回的对象信息
		InventoryRealTimeDTO inventRealTimeModel= new InventoryRealTimeDTO();
		List<InventoryRealTimeDTO> stockInfoList = new ArrayList<InventoryRealTimeDTO>();
		Integer totalCount = 0;
		Integer pageCount = 0;
		Integer pageSize = 50;

		WlbWmsInventoryQueryRequest requestVo = new WlbWmsInventoryQueryRequest();
		requestVo.setType(2L);//库存查询类型：2- 批次库存，库存按商品批次维度划分
		requestVo.setPageNo(1L);
		requestVo.setPageSize((long) pageSize);
		if(StringUtils.isNotBlank(inventoryRealTimeDTO.getGoodsNo())){
			requestVo.setItemId(inventoryRealTimeDTO.getGoodsNo());
		}

		/*查询所有启用的店铺*/
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("status", "1");//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("isSycnMerchandise", DERP_SYS.MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_1);//是否同步菜鸟商品 1-是
		merchantShopRelMap.put("depotId", inventoryRealTimeDTO.getDepotId());//仓库ID
		merchantShopRelMap.put("merchantId", merchantId);
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);
		if(merchantShopRelList==null||merchantShopRelList.size()<=0){//商家店铺数量为0,结束执行
			return inventRealTimeModel;
		}
		for (MerchantShopRelMongo merchantShopRelMongo : merchantShopRelList) {
			//查询实时快照
			WlbWmsInventoryQueryResponse result = TaoBaoUtils.executeTBInventoryQuery(requestVo, merchantShopRelMongo.getSessionKey(),
					merchantShopRelMongo.getAppKey(), merchantShopRelMongo.getAppSecret());
			//请求阿里异常，记录日志
			if(result==null) {
				return inventRealTimeModel;
			}
			JSONObject jsonObject = JSONObject.fromObject(result);
			totalCount = (Integer) jsonObject.get("totalCount");//总条数
			pageCount = this.getTotalPage(totalCount, pageSize);//获取总页数

			if(pageCount<=0) {
				return inventRealTimeModel;
			}
			for(int pageNo=1;pageNo<=pageCount.intValue();pageNo++) {
				requestVo.setPageNo((long) pageNo);
				result = TaoBaoUtils.executeTBInventoryQuery(requestVo, merchantShopRelMongo.getSessionKey(),
						merchantShopRelMongo.getAppKey(), merchantShopRelMongo.getAppSecret());
				jsonObject = JSONObject.fromObject(result);
				// 获取实时库存商信息
				JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("itemList"));
				for(int i=0;i<jsonArray.size();i++) {
					JSONObject item = jsonArray.optJSONObject(i);
					JSONObject batch = (JSONObject) item.get("item");

					Integer inventoryType = (Integer) batch.get("inventoryType");  //库存类型，1为正品，101为残次品
					if(inventoryType!=1 && inventoryType!=101){ // 只取库存类型为正品和残次品的数据
						continue;
					}
					String storeCode = (String) batch.get("storeCode");//仓库编码
					String itemId = (String) batch.get("itemId");  //菜鸟商品货号
					String batchCode = (String) batch.get("batchCode");//库存批次号
					Integer totalNum = (Integer) batch.get("quantity");//库存数量

					/**
					 1.1健太商家主体-1000000204 只取storeCode为STE_17OR09661的库存信息；
					 1.2卓烨商家主体-0000134 只取storeCode为STORE_11180270、STORE_12093326的库存信息；
					 1.3嘉宝商家主体-0000138 只取storeCode为STORE_11180270的库存信息；
					 1.4广旺商家主体-1000000645 只取storeCode为STORE_11180270的库存信息；
					 * */
					if(topidealCode.equals("1000000204")&&!storeCode.matches("STORE_1709661")) {
						continue;
					}
					if(topidealCode.equals("0000134")&&!storeCode.matches("STORE_11180270|STORE_12093326")) {
						continue;
					}
					if(topidealCode.equals("0000138")&&!storeCode.matches("STORE_11180270")) {
						continue;
					}
					if(topidealCode.equals("1000000645")&&!storeCode.matches("STORE_11180270")) {
						continue;
					}
					Integer lockNum = (Integer) batch.get("lockQuantity");//库存数量
					Integer availableNum = totalNum-lockNum;//库存总量=库存数量+锁定数量

					Map<String, Object> params =new HashMap<String, Object>();
					params.put("goodsNo", itemId);
					params.put("merchantId", merchantId);
					MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(params);

					// 实例化仓库对象保存实时库存查询信息
					InventoryRealTimeDTO inventRealTimeDTO = new InventoryRealTimeDTO();
					if(batch.has("produceDate")){
						if(batch.get("produceDate") instanceof JSONNull){
							inventRealTimeDTO.setProductionDate(null);
						}else{
							if(batch.get("produceDate")!=null){
								JSONObject produceDateJb = (JSONObject) batch.get("produceDate");
								Date produceDate=(Date) JSONObject.toBean(produceDateJb, Date.class);
								inventRealTimeDTO.setProductionDate(TimeUtils.format(produceDate, "yyyy-MM-dd HH:mm:ss"));//生产日期
							}
						}
					}
					if(batch.has("dueDate")){
						if(batch.get("dueDate") instanceof JSONNull){
							inventRealTimeDTO.setOverdueDate(null);
						}else {
							if(batch.get("dueDate")!=null){
								JSONObject dueDateJb = (JSONObject) batch.get("dueDate");//失效日期
								Date dueDate=(Date) JSONObject.toBean(dueDateJb, Date.class);
								inventRealTimeDTO.setOverdueDate(TimeUtils.format(dueDate, "yyyy-MM-dd HH:mm:ss"));//失效日期
								// 临保天数
								inventRealTimeDTO.setOverDays(TimeUtils.differentDays(dueDate, new Date()));
							}
						}
					}

					inventRealTimeDTO.setDepotId(depotMongo.getDepotId());//仓库id
					inventRealTimeDTO.setDepotName(depotMongo.getName()); //仓库名称
					inventRealTimeDTO.setBatchNo(batchCode);//批次号
					inventRealTimeDTO.setQty(totalNum.intValue());//库存总量
					if(inventoryType==1){//库存类型，1为正品，101为残次品
						inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型 0 好品  1 坏品
					}else if(inventoryType==101){
						inventRealTimeDTO.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
					}
					inventRealTimeDTO.setRealFrozenStockNum(0);//库存冻结数量
					inventRealTimeDTO.setLockStockNum(lockNum.intValue());//库存锁定数量
					inventRealTimeDTO.setRealStockNum(availableNum.intValue());//库存可用数量 realStockNum
					if(merchandise!=null) {
						inventRealTimeDTO.setGoodsName(merchandise.getName());//商品名称
						inventRealTimeDTO.setGoodsNo(merchandise.getGoodsNo());//商品货号
						inventRealTimeDTO.setOpgCode(merchandise.getUnique());//OPG
						inventRealTimeDTO.setBarcode(merchandise.getBarcode());
					}
					inventRealTimeDTO.setMerchantName(merchantName);
					stockInfoList.add(inventRealTimeDTO);
				}
			}
		}
		inventRealTimeModel.setList(stockInfoList);
		return inventRealTimeModel;
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
