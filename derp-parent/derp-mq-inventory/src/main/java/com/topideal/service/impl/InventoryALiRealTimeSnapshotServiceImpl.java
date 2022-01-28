package com.topideal.service.impl;

import com.taobao.api.request.WlbWmsInventoryQueryRequest;
import com.taobao.api.response.WlbItemQueryResponse;
import com.taobao.api.response.WlbWmsInventoryQueryResponse;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.api.tb.TaoBaoUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.log.APILog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.InventoryALiRealTimeSnapshotService;
import com.topideal.tools.CollectionEnum;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 实时库存快照 
 *
 */
@Service
public class InventoryALiRealTimeSnapshotServiceImpl implements InventoryALiRealTimeSnapshotService {

	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(InventoryALiRealTimeSnapshotServiceImpl.class);
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
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;


	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201308300, model = DERP_LOG_POINT.POINT_13201308300_Label)
	public boolean synsInventoryRealTimeALi(String json, String keys, String topics, String tags) throws Exception {
		logger.info("=============获取阿里菜鸟仓实时 库存快照开始=================" + json);
		
		JSONObject jsonData = JSONObject.fromObject(json);
		//默认获取当前日期快照
		String snapshotDate = TimeUtils.formatDay();
		/*查询所有启用的店铺*/
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("status", "1");//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("isSycnMerchandise", DERP_SYS.MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_1);//是否同步菜鸟商品 1-是
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);
		if(merchantShopRelList==null||merchantShopRelList.size()<=0){
			logger.info("商家店铺数量为0,结束执行");
			throw new RuntimeException("商家店铺数量为0,结束执行");
		}

		//循环获取商家店铺中是步菜鸟商品的实时库存
		for(MerchantShopRelMongo mrchantShopRelMongo:merchantShopRelList){
			// 查询商家
			Map<String, Object> merchantInfoParams =new HashMap<String, Object>();
			merchantInfoParams.put("merchantId", mrchantShopRelMongo.getMerchantId());
			MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantInfoParams);
			if (merchantInfo==null) {
				logger.info("根据商家id没有查询到商家,商家id:"+mrchantShopRelMongo.getMerchantId());
				throw new RuntimeException("根据商家id没有查询到商家,商家id:"+mrchantShopRelMongo.getMerchantId());
			}

			//获取仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", mrchantShopRelMongo.getDepotId());
			DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
			if(depotInfo==null){
				logger.info("仓库id"+mrchantShopRelMongo.getDepotId()+":"+mrchantShopRelMongo.getDepotName()+"没找到对应仓库信息,结束执行");
				throw new RuntimeException("仓库id"+mrchantShopRelMongo.getDepotId()+":"+mrchantShopRelMongo.getDepotName()+"没找到对应仓库信息,结束执行");
			}

			Map<String, Object> dMerRelParams =new HashMap<String, Object>();
			dMerRelParams.put("merchantId", mrchantShopRelMongo.getMerchantId());
			dMerRelParams.put("depotId", mrchantShopRelMongo.getDepotId());
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(dMerRelParams);
			if (depotMerchantRel==null) {
				logger.info("根据商家id:"+mrchantShopRelMongo.getMerchantId()+"仓库id:"+mrchantShopRelMongo.getDepotId()+"仓库商家关联表没有对应信息");
				throw new RuntimeException("根据商家id:"+mrchantShopRelMongo.getMerchantId()+"仓库id:"+mrchantShopRelMongo.getDepotId()+"仓库商家关联表没有对应信息");
			}
			// WMS_360_01  抓取报错停止抓取
			if(depotMerchantRel!=null&&depotInfo.getCode().matches("WMS_360_02|WTX001|CNHZ001")){//保税仓，非代销仓,菜鸟仓从阿里接口获取快照
				logger.info("获取实时库存快照阿里======商家:"+merchantInfo.getName()+",仓库:"+depotInfo.getName());
				this.synsGetDepotStockForALi(mrchantShopRelMongo,merchantInfo, depotInfo, snapshotDate);
			}
		}
		logger.info("=============获取阿里菜鸟仓实时库存快照结束=================");
		return true;
	}

	/**
	 * 实时库存查询
	 * */
	public void synsGetDepotStockForALi(MerchantShopRelMongo merchantShopRelMongo,MerchantInfoMongo merchantInfo,DepotInfoMongo depotInfo,String snapshotDate)throws Exception {

		Integer pageNo = 1;
		Integer pageSize = 50;
		Integer totalCount = 0;//总数
		/**
		 * 仓库名称	   GSS仓库ID	  OP仓库编码	               仓库自编码
		   菜鸟二期天运2仓	300	    WMS_360_01	ERPWMS_360_0104
		    菜鸟一期金外滩	301	    WMS_360_02	ERPWMS_360_0205
		 * */
		WlbWmsInventoryQueryRequest requestVo = new WlbWmsInventoryQueryRequest();
		requestVo.setType(2L);//库存查询类型：2- 批次库存，库存按商品批次维度划分
		requestVo.setPageNo((long) pageNo);
		requestVo.setPageSize((long) pageSize);

		//查询实时快照
		WlbWmsInventoryQueryResponse result = TaoBaoUtils.executeTBInventoryQuery(requestVo, merchantShopRelMongo.getSessionKey(), merchantShopRelMongo.getAppKey(), merchantShopRelMongo.getAppSecret());
		logger.info(merchantInfo.getName()+","+depotInfo.getName()+","+merchantShopRelMongo.getShopName()+"请求阿里菜鸟实时库存第"+pageNo+"页返回结果:"+result);
		//请求阿里异常，记录日志
		if(result==null) {
			logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败返回结果为空,结束执行");
			throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败返回结果为空,结束执行");
		}
		String errorCode = result.getErrorCode();
		if(StringUtils.isNotBlank(errorCode)) {
			logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败,结束执行");
			throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败,结束执行");
		}
		JSONObject resultJson = JSONObject.fromObject(result);

		totalCount = (Integer) resultJson.get("totalCount");//总条数
		Integer totalPage = this.getTotalPage(totalCount, pageSize);//获取总页数
		if(totalPage>0){
			 //保存实时库存
			this.saveALiBatch(resultJson, merchantInfo, depotInfo);
		}
		if(totalPage > 1) {
			pageNo++;//从第二页开始取
			while(pageNo<=totalPage){
				requestVo.setPageNo((long) pageNo);
				result = TaoBaoUtils.executeTBInventoryQuery(requestVo,merchantShopRelMongo.getSessionKey(),merchantShopRelMongo.getAppKey(),merchantShopRelMongo.getAppSecret());
				logger.info(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存第"+pageNo+"页返回结果:"+result);
				//请求阿里异常，记录日志
				if(result==null){
					logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败返回结果为空,结束执行");
					throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败返回结果为空,结束执行");
				}
				errorCode = result.getErrorCode();
				if(StringUtils.isNotBlank(errorCode)) {
					logger.debug(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败,结束执行");
					throw new RuntimeException(merchantInfo.getName()+","+depotInfo.getName()+"请求阿里菜鸟实时库存失败,结束执行");
				}
				 //保存实时库存
				resultJson = JSONObject.fromObject(result);

				this.saveALiBatch(resultJson, merchantInfo, depotInfo);
				pageNo++;
			}
		}
   }
	/**
	 * 保存实时库存批次
	 */
	public void saveALiBatch(JSONObject resultJson,MerchantInfoMongo merchantInfo,
			DepotInfoMongo depotInfo) throws Exception {
		JSONArray batchList = JSONArray.fromObject(resultJson.get("itemList"));

		for(int i=0;i<batchList.size();i++){
			JSONObject item = batchList.optJSONObject(i);
			JSONObject batch = (JSONObject) item.get("item");
			logger.debug("请求阿里菜鸟实时库存相应的商品信息batch:"+batch);

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
			InventoryRealTimeSnapshotModel inventoryModel = new InventoryRealTimeSnapshotModel();
			inventoryModel.setMerchantId(merchantInfo.getMerchantId());
			inventoryModel.setMerchantName(merchantInfo.getName());
			inventoryModel.setDepotId(depotInfo.getDepotId());
			inventoryModel.setDepotName(depotInfo.getName());
			if(merchandise!=null){
				inventoryModel.setGoodsId(merchandise.getMerchandiseId());
				inventoryModel.setGoodsName(merchandise.getName());
				inventoryModel.setBarcode(merchandise.getBarcode());
				inventoryModel.setOpgCode(merchandise.getUnique());// OPG号
			}
			inventoryModel.setGoodsNo(itemId);
			inventoryModel.setBatchNo(batchCode);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(batch.has("produceDate")){
				if(batch.get("produceDate") instanceof JSONNull){
					inventoryModel.setProductionDate(null);
				}else{
					if(batch.get("produceDate")!=null){
						JSONObject produceDateJb = (JSONObject) batch.get("produceDate");
						Date produceDate=(Date) JSONObject.toBean(produceDateJb, Date.class);
						String produceDateStr= sdf.format(produceDate); //格式化成yyyy-MM-dd HH:mm:ss格式的时间字符串
						java.sql.Date produceSqlDate = new java.sql.Date(sdf.parse(produceDateStr).getTime());
						inventoryModel.setProductionDate(produceSqlDate);//生产日期
					}
				}
			}
			if(batch.has("dueDate")){
				if(batch.get("dueDate") instanceof JSONNull){
					inventoryModel.setOverdueDate(null);
				}else {
					if(batch.get("dueDate")!=null){
						JSONObject dueDateJb = (JSONObject) batch.get("dueDate");//失效日期
						Date dueDate=(Date) JSONObject.toBean(dueDateJb, Date.class);
						String dueDateStr= sdf.format(dueDate); //格式化成yyyy-MM-dd HH:mm:ss格式的时间字符串
						java.sql.Date dueSqlDate = new java.sql.Date(sdf.parse(dueDateStr).getTime());
						inventoryModel.setOverdueDate(dueSqlDate);
					}
				}
			}
			inventoryModel.setSnapshotSource(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_ALI);
			inventoryModel.setCreateDate(TimeUtils.getNow());// 创建时间取当前时间
			inventoryModel.setRequestTime(TimeUtils.getNow());
			inventoryModel.setQty(totalNum);//库存数量
			inventoryModel.setRealStockNum(availableNum);//库存可用数量
			inventoryModel.setLockStockNum(lockNum); //库存锁定数量
			inventoryModel.setRealFrozenStockNum(0);//库存冻结数量
			if(inventoryType==1){//库存类型，1为正品，101为残次品
				inventoryModel.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型 0 好品  1 坏品
			}else if(inventoryType==101){
				inventoryModel.setStockType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
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
