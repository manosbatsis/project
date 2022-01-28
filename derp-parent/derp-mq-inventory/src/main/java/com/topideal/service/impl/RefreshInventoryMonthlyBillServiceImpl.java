package com.topideal.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InitInventoryDao;
import com.topideal.dao.InventoryBatchBackupDao;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.dao.MonthlyAccountItemDao;
import com.topideal.dao.MonthlyAccountSnapshotDao;
import com.topideal.entity.vo.InitInventoryModel;
import com.topideal.entity.vo.InventoryBatchBackupModel;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.RefreshInventoryMonthlyBillService;

/**
 * 刷新月结账单
 * 
 * 杨创2020-05-26
 */
@Service
public class RefreshInventoryMonthlyBillServiceImpl implements RefreshInventoryMonthlyBillService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshInventoryMonthlyBillServiceImpl.class);

	// 月库存转结dao
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;
	// 月库存转结明细dao
	@Autowired
	private MonthlyAccountItemDao monthlyAccountItemDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private InventoryBatchDao inventoryBatchDao;// 批次库存明细
	@Autowired
	private InventoryBatchBackupDao inventoryBatchBackupDao;// 批次库存备份数据
	// MongoDb的商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 月结库存快照
	@Autowired
	private MonthlyAccountSnapshotDao monthlyAccountSnapshotDao;
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;
	//库存期初
	@Autowired
	private InitInventoryDao initInventoryDao;
	@Autowired
	private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201306900, model = DERP_LOG_POINT.POINT_13201306900_Label, keyword = "orderNo")
	public boolean synsRefreshMonthlyBill(String json, String keys, String topics, String tags,MonthlyAccountModel monthlyAccountModel) throws Exception {
		LOGGER.info("==========刷新月结账单开始=================>" + json);
		// 请先结转之前月份数据
		
		Timestamp endDate = monthlyAccountModel.getEndDate();
		Long monthlyAccountId = monthlyAccountModel.getId();
		//String message="";
		//String type="";
		//刷新月结明细，先删除旧的明细
		monthlyAccountItemDao.delItemByMonthlyAccountId(monthlyAccountId);
		//判断上个月是否有生成月结
		MonthlyAccountModel monthlyAccount = new MonthlyAccountModel();
		monthlyAccount.setMerchantId(monthlyAccountModel.getMerchantId());
		monthlyAccount.setDepotId(monthlyAccountModel.getDepotId());
		monthlyAccount.setSettlementMonth(TimeUtils.getLastMonth(monthlyAccountModel.getFirstDate()));
		List<MonthlyAccountModel> monthlyAccountList = monthlyAccountDao.list(monthlyAccount);
		if(monthlyAccountList != null && monthlyAccountList.size() > 0){//上个月有月结
			Map<String,Integer> map = new HashMap<String,Integer>();
			/*
			 * 获取月结余量/期末库存量 并加减本月份（月结的月份）的收发明细
			 */
			//获取上个月月结的明细
			List<MonthlyAccountItemModel> itemList = monthlyAccountItemDao.getItemListById(monthlyAccountList.get(0).getId());
			//遍历组装成结果集
			for(MonthlyAccountItemModel item : itemList){
				String key = item.getMerchantId()+";"+item.getDepotId()+";"+item.getGoodsId()+";"+item.getBatchNo()+";"+item.getProductionDate()+";"+item.getOverdueDate()+";"+item.getType()+";"+item.getUnit();
				Integer num = item.getSettlementNum();
				if(num == null){
					num = item.getSurplusNum();
				}
				if(map.containsKey(key)){
					num +=map.get(key); 
				}
				map.put(key, num);
			}
			//获取本月份（月结的月份）的收发明细-增
			List<InventoryDetailsModel> addDetailsList = inventoryDetailsDao.getDetailsByParams(monthlyAccountModel.getMerchantId(),monthlyAccountModel.getDepotId(),TimeUtils.format(endDate, "yyyy-MM"),"1");
			//遍历组装成结果集
			for(InventoryDetailsModel addDetails : addDetailsList){
				String key = addDetails.getMerchantId()+";"+addDetails.getDepotId()+";"+addDetails.getGoodsId()+";"+addDetails.getBatchNo()+";"+addDetails.getProductionDate()+";"+addDetails.getOverdueDate()+";"+addDetails.getType()+";"+addDetails.getUnit();
				Integer num = addDetails.getNum();
				if(map.containsKey(key)){
					num +=map.get(key); 
				}
				map.put(key, num);
			}
			//获取本月份（月结的月份）的收发明细-减
			List<InventoryDetailsModel> subDetailsList = inventoryDetailsDao.getDetailsByParams(monthlyAccountModel.getMerchantId(),monthlyAccountModel.getDepotId(),TimeUtils.format(endDate, "yyyy-MM"),"0");
			//遍历组装成结果集
			for(InventoryDetailsModel subDetails : subDetailsList){
				String key = subDetails.getMerchantId()+";"+subDetails.getDepotId()+";"+subDetails.getGoodsId()+";"+subDetails.getBatchNo()+";"+subDetails.getProductionDate()+";"+subDetails.getOverdueDate()+";"+subDetails.getType()+";"+subDetails.getUnit();
				Integer num = subDetails.getNum();
				if(map.containsKey(key)){
					num = map.get(key) - num;
				}else{
					num = num*-1;
				}
				map.put(key, num);
			}
			//遍历生成月结明细
			for(Map.Entry<String,Integer> entry:map.entrySet()){
				String key = entry.getKey();
				String[]keyStr = key.split(";");
				Integer surplusNum = entry.getValue();
				//根据条件获取月结明细
				MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
				monthlyAccountItemModel.setMerchantId(Long.parseLong(keyStr[0]));
				monthlyAccountItemModel.setDepotId(Long.parseLong(keyStr[1]));
				monthlyAccountItemModel.setGoodsId(Long.parseLong(keyStr[2]));				
				if(StringUtils.isNotBlank(keyStr[3]) && !keyStr[3].equals("null")){
					monthlyAccountItemModel.setBatchNo(keyStr[3]);
				}
				if(StringUtils.isNotBlank(keyStr[4]) && !keyStr[4].equals("null")){
					monthlyAccountItemModel.setProductionDate(TimeUtils.strToSqlDate(keyStr[4]));
				}
				if(StringUtils.isNotBlank(keyStr[5]) && !keyStr[5].equals("null")){
					monthlyAccountItemModel.setOverdueDate(TimeUtils.strToSqlDate(keyStr[5]));
					//是否过期
					if(monthlyAccountItemModel.getOverdueDate().getTime()>monthlyAccountModel.getEndDate().getTime()){
						monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
					}else{
						monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_0);
					}
				}else{
					monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
				}
				monthlyAccountItemModel.setType(keyStr[6]);
				if(keyStr.length == 8 && !keyStr[7].equals("null")){
					monthlyAccountItemModel.setUnit(keyStr[7]);
				}
				monthlyAccountItemModel.setMonthlyAccountId(monthlyAccountId);
				MonthlyAccountItemModel itemModel=monthlyAccountItemDao.getOne(monthlyAccountItemModel);
				if(itemModel!=null){//存在则修改明细的数量
					itemModel.setSurplusNum(surplusNum);
					int num=	monthlyAccountItemDao.updateAddMonthlySurplusNum(itemModel);
				}else{//不存在则新增明细 
					Map<String, Object> params =new HashMap<String, Object>();
					params.put("merchandiseId", monthlyAccountItemModel.getGoodsId());
					MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(params);
					monthlyAccountItemModel.setGoodsNo(merchandiseInfo.getGoodsNo());
					monthlyAccountItemModel.setGoodsName(merchandiseInfo.getName());
					Map<String, Object> params1 =new HashMap<String, Object>();
					params1.put("depotId", monthlyAccountItemModel.getDepotId());
					DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
					monthlyAccountItemModel.setDepotName(depotInfo.getName());
					monthlyAccountItemModel.setSurplusNum(surplusNum);
					monthlyAccountItemModel.setCreateDate(TimeUtils.getNow());
					monthlyAccountItemModel.setAvailableNum(0);
					monthlyAccountItemModel.setBarcode(merchandiseInfo.getBarcode());//条形码
					monthlyAccountItemModel.setCommbarcode(merchandiseInfo.getCommbarcode());// 添加标准条码
					Long ids= monthlyAccountItemDao.save(monthlyAccountItemModel);
				}
			}
		}else{//上个月没有月结
			Map<String,Integer> map = new HashMap<String,Integer>();
			
			MonthlyAccountModel monthly = new MonthlyAccountModel();
			monthly.setMerchantId(monthlyAccountModel.getMerchantId());
			monthly.setDepotId(monthlyAccountModel.getDepotId());
			monthly.setSettlementMonth(monthlyAccountModel.getSettlementMonth());
			// 此商家本月之前是否有过月结  (月结生成和刷新月结专用)
			List<MonthlyAccountModel> monthlyList = monthlyAccountDao.getBeforeMonthMonthlyAccount(monthly);
			List<InitInventoryModel> initList=new ArrayList<>();
			// 如果此商家 此仓库本月之前没有月结过 就查期初  如果月结过 期初未0
			if (monthlyList.size()==0) {
				//获取期初
				InitInventoryModel initModel = new InitInventoryModel();
				initModel .setMerchantId(monthlyAccountModel.getMerchantId());
				initModel.setDepotId(monthlyAccountModel.getDepotId());
				initList = initInventoryDao.list(initModel);
			}
			
			//遍历组装成结果集
			for(InitInventoryModel item : initList){
				String key = item.getMerchantId()+";"+item.getDepotId()+";"+item.getGoodsId()+";"+item.getBatchNo()+";"+item.getProductionDate()+";"+item.getOverdueDate()+";"+item.getType()+";"+item.getUnit();
				Integer num = item.getInitNum();
				if(num == null){
					num = item.getSurplusNum();
				}
				if(map.containsKey(key)){
					num +=map.get(key); 
				}
				map.put(key, num);
			}
			//获取本月份（月结的月份）的收发明细-增
			List<InventoryDetailsModel> addDetailsList = inventoryDetailsDao.getDetailsByParams(monthlyAccountModel.getMerchantId(),monthlyAccountModel.getDepotId(),TimeUtils.format(endDate, "yyyy-MM"),"1");
			//遍历组装成结果集
			for(InventoryDetailsModel addDetails : addDetailsList){
				String key = addDetails.getMerchantId()+";"+addDetails.getDepotId()+";"+addDetails.getGoodsId()+";"+addDetails.getBatchNo()+";"+addDetails.getProductionDate()+";"+addDetails.getOverdueDate()+";"+addDetails.getType()+";"+addDetails.getUnit();
				Integer num = addDetails.getNum();
				if(map.containsKey(key)){
					num +=map.get(key); 
				}
				map.put(key, num);
			}
			//获取本月份（月结的月份）的收发明细-减
			List<InventoryDetailsModel> subDetailsList = inventoryDetailsDao.getDetailsByParams(monthlyAccountModel.getMerchantId(),monthlyAccountModel.getDepotId(),TimeUtils.format(endDate, "yyyy-MM"),"0");
			//遍历组装成结果集
			for(InventoryDetailsModel subDetails : subDetailsList){
				String key = subDetails.getMerchantId()+";"+subDetails.getDepotId()+";"+subDetails.getGoodsId()+";"+subDetails.getBatchNo()+";"+subDetails.getProductionDate()+";"+subDetails.getOverdueDate()+";"+subDetails.getType()+";"+subDetails.getUnit();
				Integer num = subDetails.getNum();
				if(map.containsKey(key)){
					num = map.get(key) - num;
				}else{
					num = num*-1;
				}
				map.put(key, num);
			}
			//遍历生成月结明细
			for(Map.Entry<String,Integer> entry:map.entrySet()){
				String key = entry.getKey();
				String[]keyStr = key.split(";");
				Integer surplusNum = entry.getValue();
				//根据条件获取月结明细
				MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
				monthlyAccountItemModel.setMerchantId(Long.parseLong(keyStr[0]));
				monthlyAccountItemModel.setDepotId(Long.parseLong(keyStr[1]));
				monthlyAccountItemModel.setGoodsId(Long.parseLong(keyStr[2]));				
				if(StringUtils.isNotBlank(keyStr[3]) && !keyStr[3].equals("null")){
					monthlyAccountItemModel.setBatchNo(keyStr[3]);
				}
				if(StringUtils.isNotBlank(keyStr[4]) && !keyStr[4].equals("null")){
					monthlyAccountItemModel.setProductionDate(TimeUtils.strToSqlDate(keyStr[4]));
				}
				if(StringUtils.isNotBlank(keyStr[5]) && !keyStr[5].equals("null")){
					monthlyAccountItemModel.setOverdueDate(TimeUtils.strToSqlDate(keyStr[5]));
					//是否过期
					if(monthlyAccountItemModel.getOverdueDate().getTime()>monthlyAccountModel.getEndDate().getTime()){
						monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
					}else{
						monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_0);
					}
				}else{
					monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
				}
				monthlyAccountItemModel.setType(keyStr[6]);
				if(keyStr.length == 8 && !keyStr[7].equals("null")){
					monthlyAccountItemModel.setUnit(keyStr[7]);
				}
				monthlyAccountItemModel.setMonthlyAccountId(monthlyAccountId);
				MonthlyAccountItemModel itemModel=monthlyAccountItemDao.getOne(monthlyAccountItemModel);
				if(itemModel!=null){//存在则修改明细的数量
					itemModel.setSurplusNum(surplusNum);
					int num = monthlyAccountItemDao.updateAddMonthlySurplusNum(itemModel);
				}else{//不存在则新增明细 
					Map<String, Object> params =new HashMap<String, Object>();
					params.put("merchandiseId", monthlyAccountItemModel.getGoodsId());
					MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(params);
					monthlyAccountItemModel.setGoodsNo(merchandiseInfo.getGoodsNo());
					monthlyAccountItemModel.setGoodsName(merchandiseInfo.getName());
					Map<String, Object> params1 =new HashMap<String, Object>();
					params1.put("depotId", monthlyAccountItemModel.getDepotId());
					DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
					monthlyAccountItemModel.setDepotName(depotInfo.getName());
					monthlyAccountItemModel.setSurplusNum(surplusNum);
					monthlyAccountItemModel.setCreateDate(TimeUtils.getNow());
					monthlyAccountItemModel.setAvailableNum(0);
					monthlyAccountItemModel.setBarcode(merchandiseInfo.getBarcode());//条形码
					monthlyAccountItemModel.setCommbarcode(merchandiseInfo.getCommbarcode());// 添加标准条码
					Long ids= monthlyAccountItemDao.save(monthlyAccountItemModel);

				}
			}
		}
		//============刷新现存量--实时库存==============
		//用于存储所有实时库存信息
		List<InventoryRealTimeSnapshotModel> allList = new ArrayList<InventoryRealTimeSnapshotModel>();
		//分页获取实时库存-每次1000条
		Integer page = 0;         //页数
		Integer begin = 0;        //起始位置
		Integer pageSize = 1000;  //每次获取的数量
		//根据条件获取实时库存的总条数
		/**
		 * 1.如果当前时间月份等于于月结时时间  查的是当月月结 查实时库存取当前时间的日期  
		 * 2.如果当前时间大于月结时间  查的是月结月份下个月一号的数据
		 */
		String nowMonth = TimeUtils.format(new Date(), "yyyy-MM");//获取本月时间年月
		String settlementMonth = monthlyAccountModel.getSettlementMonth();
		String timeSnapshot=null;
		if (nowMonth.equals(settlementMonth)) {
			timeSnapshot=TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd");
		}else {
			timeSnapshot=TimeUtils.format(TimeUtils.addDay(monthlyAccountModel.getEndDate(), 1), "yyyy-MM-dd");			
		}
		Integer totalNum = inventoryRealTimeSnapshotDao.getListTotalNum(monthlyAccountModel.getMerchantId(),monthlyAccountModel.getDepotId(),timeSnapshot);
		if(totalNum != null && totalNum > 0){
			//计算页数
			page = totalNum/pageSize;
			if(totalNum%pageSize != 0){
				page +=1;
			}
		}
		//获取实时库存
		for(int i = 0;i<page;i++){
			List<InventoryRealTimeSnapshotModel> list = inventoryRealTimeSnapshotDao.getListByParams(monthlyAccountModel.getMerchantId(),monthlyAccountModel.getDepotId(),timeSnapshot,begin,pageSize);
			if(list != null && list.size()>0){
				allList.addAll(list);
			}
			begin += pageSize;
		}
		//遍历实时库存，相同的商品库存累加合并
		Map<String,Integer> irtsMap = new HashMap<String,Integer>();
		for(InventoryRealTimeSnapshotModel irtsModel: allList){
			String key = irtsModel.getMerchantId()+";"+irtsModel.getDepotId()+";"+irtsModel.getGoodsId()+";"+irtsModel.getBatchNo()+";"+irtsModel.getProductionDate()+";"+irtsModel.getOverdueDate()+";"+irtsModel.getUnit()+";"+irtsModel.getStockType();
			Integer num = irtsModel.getQty();
			if(irtsMap.containsKey(key)){
				num +=irtsMap.get(key);
			}
			irtsMap.put(key, num);
		}
		//匹配月结明细
		for(Map.Entry<String,Integer> entry:irtsMap.entrySet()){
			String key = entry.getKey();
			String[]keyArr = key.split(";");
			Integer num = entry.getValue();
			Long merchantId = Long.valueOf(keyArr[0]);
			Long depotId = Long.valueOf(keyArr[1]);
			//跳过匹配不到商品的实时库存
			if(StringUtils.isBlank(keyArr[2]) || keyArr[2].equals("null") ){
				continue;
			}
			Long goodsId = Long.valueOf(keyArr[2]);
			String batchNo = null;
			if(StringUtils.isNotBlank(keyArr[3]) && !keyArr[3].equals("null")){
				batchNo = keyArr[3];
			}
			java.sql.Date productionDate = null;
			if(StringUtils.isNotBlank(keyArr[4]) && !keyArr[4].equals("null")){
				productionDate = TimeUtils.strToSqlDate(keyArr[4]);
			}
			java.sql.Date overdueDate = null;
			if(StringUtils.isNotBlank(keyArr[5]) && !keyArr[5].equals("null")){
				overdueDate = TimeUtils.strToSqlDate(keyArr[5]);
			}
			String unit = null;
			if(StringUtils.isNotBlank(keyArr[6]) && !keyArr[6].equals("null")){
				if ("00".equals(keyArr[6])) {
					unit="0";
				}else if("01".equals(keyArr[6])){
					unit="1";
				}else if("02".equals(keyArr[6])){
					unit="2";
				}
			}
			String stockType = null;
			if(StringUtils.isNotBlank(keyArr[7]) && !keyArr[7].equals("null")){
				stockType = keyArr[7];
			}
			//根据条件获取月结表体数据
			MonthlyAccountItemModel maItem = monthlyAccountItemDao.getItemByInventoryRealTimeSnapshot(merchantId,depotId,goodsId,batchNo,productionDate,overdueDate,unit,stockType,monthlyAccountModel.getId());
			if(maItem != null){//有则更新
				Map<String, Object> parMap = new HashMap<String, Object>();
				parMap.put("availableNum", num);
				parMap.put("id", maItem.getId());
				monthlyAccountItemDao.updateAvailableNumOrBarcode(parMap);
			}else{//没有则新增表体
				MonthlyAccountItemModel monAccIteModel = new MonthlyAccountItemModel();
				monAccIteModel.setMerchantId(merchantId);
				monAccIteModel.setDepotId(depotId);
				monAccIteModel.setGoodsId(goodsId);
				if (StringUtils.isNotBlank(batchNo)) {
					monAccIteModel.setBatchNo(batchNo);
				}
				if (productionDate != null) {
					monAccIteModel.setProductionDate(productionDate);
				}
				if (overdueDate != null) {
					monAccIteModel.setOverdueDate(overdueDate);
					//是否过期
					if(monAccIteModel.getOverdueDate().getTime()>monthlyAccountModel.getEndDate().getTime()){
						monAccIteModel.setIsExpire(DERP.ISEXPIRE_1);
					}else{
						monAccIteModel.setIsExpire(DERP.ISEXPIRE_0);
					}
				}else{
					monAccIteModel.setIsExpire(DERP.ISEXPIRE_1);
				}
				if (StringUtils.isNotBlank(stockType)) {// 0 好品 1坏品
					monAccIteModel.setType(stockType);
				} else {
					monAccIteModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
				}
				monAccIteModel.setUnit(unit);
				monAccIteModel.setAvailableNum(num);
				Map<String, Object> params =new HashMap<String, Object>();
				params.put("merchandiseId", goodsId);
				MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(params);
				monAccIteModel.setGoodsNo(merchandiseInfo.getGoodsNo());
				monAccIteModel.setGoodsName(merchandiseInfo.getName());
				monAccIteModel.setBarcode(merchandiseInfo.getBarcode());
				Map<String, Object> params1 =new HashMap<String, Object>();
				params1.put("depotId", depotId);
				DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
				monAccIteModel.setDepotName(depotInfo.getName());
				monAccIteModel.setSurplusNum(0);
				monAccIteModel.setMonthlyAccountId(monthlyAccountModel.getId());
				monAccIteModel.setCommbarcode(merchandiseInfo.getCommbarcode());// 标准条码
				monthlyAccountItemDao.save(monAccIteModel);
			}
		}
		
		/**
		 * 修改首次上架日期
		 */
		updateAccountItemShaleDate(monthlyAccountId);
		

		//通知报表库删除数据
		/*try{//reportMQ
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("monthlyAccountId", monthlyAccountId);
			SendResult result = rocketMQProducer.send(jsonObj.toString(), MQReportEnum.DEL_MONTHLY_ACCOUNT_ITEM.getTopic(),MQReportEnum.DEL_MONTHLY_ACCOUNT_ITEM.getTags());
			LOGGER.info("删除报表库月结明细MsgId:"+result.getMsgId());
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("删除报表库月结明细异常："+e.getMessage());
		}*/
		return true;
	
	
	}
	/**
	 * 修改首次上架日期
	 * @param monthlyAccountId
	 * @throws Exception
	 */
	private void updateAccountItemShaleDate(Long monthlyAccountId) throws Exception {
		//查询批次库存数据 // 修改首次上架日期
				MonthlyAccountItemModel itemQuery=new MonthlyAccountItemModel(); 		
				itemQuery.setMonthlyAccountId(monthlyAccountId);		
				List<MonthlyAccountItemModel> itemQueryList = monthlyAccountItemDao.list(itemQuery);
				for (MonthlyAccountItemModel itemModel : itemQueryList) {
					
					// 查询批次库存最小的创建时间
					InventoryBatchModel inventoryBatch=new InventoryBatchModel();
					inventoryBatch.setMerchantId(itemModel.getMerchantId());
					inventoryBatch.setDepotId(itemModel.getDepotId());
					inventoryBatch.setGoodsId(itemModel.getGoodsId());
					inventoryBatch.setUnit(itemModel.getUnit());
					inventoryBatch.setBatchNo(itemModel.getBatchNo());
					inventoryBatch.setProductionDate(itemModel.getProductionDate());
					inventoryBatch.setOverdueDate(itemModel.getOverdueDate());
					InventoryBatchModel batchMinDate = inventoryBatchDao.getMinDate(inventoryBatch);
					Timestamp createDate = null;
					if (batchMinDate==null) {
						InventoryBatchBackupModel inventoryBatchBackup=new InventoryBatchBackupModel();
						inventoryBatchBackup.setMerchantId(itemModel.getMerchantId());
						inventoryBatchBackup.setDepotId(itemModel.getDepotId());
						inventoryBatchBackup.setGoodsId(itemModel.getGoodsId());
						inventoryBatchBackup.setUnit(itemModel.getUnit());
						inventoryBatchBackup.setBatchNo(itemModel.getBatchNo());
						inventoryBatchBackup.setProductionDate(itemModel.getProductionDate());
						inventoryBatchBackup.setOverdueDate(itemModel.getOverdueDate());
						InventoryBatchBackupModel backupMinDate = inventoryBatchBackupDao.getMinDate(inventoryBatchBackup);
						if (backupMinDate!=null)createDate=backupMinDate.getCreateDate();
					}else {
						createDate = batchMinDate.getCreateDate();
					}
					
					//修改月结表体
					if (createDate!=null) {
						MonthlyAccountItemModel saveItem=new MonthlyAccountItemModel();
						saveItem.setFirstShelfDate(new java.sql.Date(createDate.getTime()));
						saveItem.setId(itemModel.getId());
						monthlyAccountItemDao.modify(saveItem);				
					}
					
					
				}
		
	}
}
