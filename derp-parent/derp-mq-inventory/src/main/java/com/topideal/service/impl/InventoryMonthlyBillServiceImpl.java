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

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.annotation.SystemServiceLog;
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
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.InventoryMonthlyBillService;

/**
 * 月结账单
 * 
 * @author 联想302 baols 2018/06/11
 */
@Service
public class InventoryMonthlyBillServiceImpl implements InventoryMonthlyBillService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryMonthlyBillServiceImpl.class);

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

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201302000, model = DERP_LOG_POINT.POINT_13201302000_Label, keyword = "orderNo")
	public boolean synsInventoryMonthlyBill(String json, String keys, String topics, String tags) throws Exception {
		LOGGER.info("==========月结账单=================>" + json);
		String lastMonth = TimeUtils.getLastMonth(new Date());// 上月时间 年月
		String nowMonth = TimeUtils.format(new Date(), "yyyy-MM");//获取本月时间年月
		LOGGER.info("月结账单月份:>" + nowMonth);
		// 获取批次库存中所有的商家和仓库
		List<InventoryBatchModel> inveBatchMDList = inventoryBatchDao.getAllMerchantOrDepot();		
		// 获取上月加减明细的所有商家和仓库
		List<InventoryDetailsModel> inventoryDetailsMDList = inventoryDetailsDao.getAllDetailMerchantOrDepotByTime(lastMonth);
		//获取上个月的 月结数量大于0的商家和仓库
		List<MonthlyAccountModel> monthlyAccountMDList = monthlyAccountDao.getAllMonthlyMerchantOrDepotByTime(lastMonth);		
		
		// 用于存储 商品库存 , 上月加减明细, 上月月结不为0的 所有商家和仓库
		Map<String, String> merchantAndDepotKeyMap=new HashMap<>();
		List<Map<String,Object>> merchantAndDepotList=new ArrayList<>();
		// 批次库存获取商家和仓库
		for (InventoryBatchModel inventoryBatchModel : inveBatchMDList) {
			Long merchantId = inventoryBatchModel.getMerchantId();
			Long depotId = inventoryBatchModel.getDepotId();
			String key=merchantId+""+depotId;
			Map<String, Object> map=new HashMap<>();
			if (!merchantAndDepotKeyMap.containsKey(key)) {
				map.put("merchantId", inventoryBatchModel.getMerchantId());
				map.put("merchantName", inventoryBatchModel.getMerchantName());
				map.put("topidealCode", inventoryBatchModel.getTopidealCode());
				map.put("depotId", inventoryBatchModel.getDepotId());				
				Map<String, Object> params1 =new HashMap<String, Object>();
				params1.put("depotId", inventoryBatchModel.getDepotId());
				DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
				if(depotInfo == null){
					depotInfo=new DepotInfoMongo();
				}								
				map.put("depotName", depotInfo.getName());
				map.put("depotCode", depotInfo.getCode());
				map.put("depotType", depotInfo.getDepotType());
				map.put("isTopBooks", depotInfo.getIsTopBooks());
				merchantAndDepotList.add(map);
				merchantAndDepotKeyMap.put(key, key);
			}			
		}
		for (InventoryDetailsModel inventoryDetailsModel : inventoryDetailsMDList) {
			Long merchantId = inventoryDetailsModel.getMerchantId();
			Long depotId = inventoryDetailsModel.getDepotId();
			String key=merchantId+""+depotId;
			Map<String, Object> map=new HashMap<>();
			if (!merchantAndDepotKeyMap.containsKey(key)) {
				map.put("merchantId", inventoryDetailsModel.getMerchantId());
				map.put("merchantName", inventoryDetailsModel.getMerchantName());
				map.put("topidealCode", inventoryDetailsModel.getTopidealCode());
				map.put("depotId", inventoryDetailsModel.getDepotId());
				Map<String, Object> params1 =new HashMap<String, Object>();
				params1.put("depotId", inventoryDetailsModel.getDepotId());
				DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
				if(depotInfo == null){
					depotInfo=new DepotInfoMongo();
				}	
								
				map.put("depotName", depotInfo.getName());
				map.put("depotCode", depotInfo.getCode());
				map.put("depotType", depotInfo.getDepotType());
				map.put("isTopBooks", depotInfo.getIsTopBooks());	
				merchantAndDepotList.add(map);
				merchantAndDepotKeyMap.put(key, key);
			}
		}
		for (MonthlyAccountModel monthlyAccountModel : monthlyAccountMDList) {
			Long merchantId = monthlyAccountModel.getMerchantId();
			Long depotId = monthlyAccountModel.getDepotId();
			String key=merchantId+""+depotId;
			Map<String, Object> map=new HashMap<>();
			if (!merchantAndDepotKeyMap.containsKey(key)) {
				map.put("merchantId", monthlyAccountModel.getMerchantId());
				map.put("merchantName", monthlyAccountModel.getMerchantName());
				map.put("topidealCode", monthlyAccountModel.getTopidealCode());
				map.put("depotId", monthlyAccountModel.getDepotId());
				Map<String, Object> params1 =new HashMap<String, Object>();
				params1.put("depotId", monthlyAccountModel.getDepotId());
				DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
				if(depotInfo == null){
					depotInfo=new DepotInfoMongo();
				}				
				map.put("depotName", depotInfo.getName());
				map.put("depotCode", depotInfo.getCode());
				map.put("depotType", depotInfo.getDepotType());
				map.put("isTopBooks", depotInfo.getIsTopBooks());	
				merchantAndDepotList.add(map);
				merchantAndDepotKeyMap.put(key, key);
			}
		}
		// 根据商家仓库的月结		
		if (merchantAndDepotList != null && merchantAndDepotList.size() > 0) {
			for (Map<String, Object> merchantAndDepotMap : merchantAndDepotList) {				
				String merchantName = (String) merchantAndDepotMap.get("merchantName");
				String topidealCode = (String) merchantAndDepotMap.get("topidealCode");
				String depotName = (String) merchantAndDepotMap.get("depotName");
				String depotCode = (String) merchantAndDepotMap.get("depotCode");
				String depotType = (String) merchantAndDepotMap.get("depotType");
				String isTopBooks = (String) merchantAndDepotMap.get("isTopBooks");
				

				//月结表头数据
				MonthlyAccountModel monActModel = new MonthlyAccountModel();
				monActModel.setMerchantId((Long) merchantAndDepotMap.get("merchantId"));
				monActModel.setMerchantName(merchantName);
				monActModel.setDepotId((Long) merchantAndDepotMap.get("depotId"));
				monActModel.setDepotName(depotName);
				monActModel.setSettlementMonth(nowMonth);
				monActModel.setFirstDate(TimeUtils.getNowfirstDate());
				//monActModel.setOrderNo(CodeGeneratorUtil.getNo("YJSY"));
				monActModel.setOrderNo(SmurfsUtils.getID("YJSY"));
				monActModel.setTopidealCode(topidealCode);
				monActModel.setEndDate(TimeUtils.getNowEndDate());//本月的结转时间
				monActModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_1);// 未结转
				monActModel.setCreateDate(TimeUtils.getNow());
				//以后都是按库存量结转 没有按现存量结转了
				monActModel.setDepotType("2");
				//保存月结表头数据
				Long id = monthlyAccountDao.save(monActModel);
				if (id != null) {
					Timestamp firstDate =  monActModel.getFirstDate();
					//判断上个月是否有生成月结
					MonthlyAccountModel monthlyAccount = new MonthlyAccountModel();
					monthlyAccount.setMerchantId(monActModel.getMerchantId());
					monthlyAccount.setDepotId(monActModel.getDepotId());
					monthlyAccount.setSettlementMonth(TimeUtils.getLastMonth(firstDate));
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
						List<InventoryDetailsModel> addDetailsList = inventoryDetailsDao.getDetailsByParams(monActModel.getMerchantId(),monActModel.getDepotId(),TimeUtils.format(firstDate, "yyyy-MM"),"1");
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
						List<InventoryDetailsModel> subDetailsList = inventoryDetailsDao.getDetailsByParams(monActModel.getMerchantId(),monActModel.getDepotId(),TimeUtils.format(firstDate, "yyyy-MM"),"0");
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
							String[]keyArr = key.split(";");
							Integer surplusNum = entry.getValue();
							//保存月结明细
							MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
							monthlyAccountItemModel.setMerchantId(Long.parseLong(keyArr[0]));
							monthlyAccountItemModel.setDepotId(Long.parseLong(keyArr[1]));
							monthlyAccountItemModel.setGoodsId(Long.parseLong(keyArr[2]));						
							if(StringUtils.isNotBlank(keyArr[3]) && !keyArr[3].equals("null")){
								monthlyAccountItemModel.setBatchNo(keyArr[3]);
							}
							if(StringUtils.isNotBlank(keyArr[4]) && !keyArr[4].equals("null")){
								monthlyAccountItemModel.setProductionDate(TimeUtils.strToSqlDate(keyArr[4]));
							}
							if(StringUtils.isNotBlank(keyArr[5]) && !keyArr[5].equals("null")){
								monthlyAccountItemModel.setOverdueDate(TimeUtils.strToSqlDate(keyArr[5]));
								//是否过期 
								if(monthlyAccountItemModel.getOverdueDate().getTime()>monActModel.getEndDate().getTime()){							
									monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
								}else{
									monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_0);
								}
							}else{
								monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
							}
							monthlyAccountItemModel.setType(keyArr[6]);
							if(keyArr.length == 8 && !keyArr[7].equals("null")){
								monthlyAccountItemModel.setUnit(keyArr[7]);
							}
							monthlyAccountItemModel.setMonthlyAccountId(id);
							Map<String, Object> params =new HashMap<String, Object>();
							params.put("merchandiseId", monthlyAccountItemModel.getGoodsId());
							MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(params);
							monthlyAccountItemModel.setGoodsNo(merchandiseInfo.getGoodsNo());
							monthlyAccountItemModel.setGoodsName(merchandiseInfo.getName());
							Map<String, Object> params1 =new HashMap<String, Object>();
							params1.put("depotId", monthlyAccountItemModel.getDepotId());
							DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
							if(depotInfo != null){
								monthlyAccountItemModel.setDepotName(depotInfo.getName());
							}
							monthlyAccountItemModel.setSurplusNum(surplusNum);
							monthlyAccountItemModel.setCreateDate(TimeUtils.getNow());
							monthlyAccountItemModel.setAvailableNum(0);
							monthlyAccountItemModel.setBarcode(merchandiseInfo.getBarcode());//条形码
							monthlyAccountItemModel.setCommbarcode(merchandiseInfo.getCommbarcode());
							Long ids= monthlyAccountItemDao.save(monthlyAccountItemModel);
						}
					}else{//上个月没有月结
						Map<String,Integer> map = new HashMap<String,Integer>();
						
						MonthlyAccountModel monthly = new MonthlyAccountModel();
						monthly.setMerchantId(monActModel.getMerchantId());
						monthly.setDepotId(monActModel.getDepotId());
						monthly.setSettlementMonth(lastMonth);
						// 此商家本月之前是否有过月结  (月结生成和刷新月结专用)
						List<MonthlyAccountModel> monthlyList = monthlyAccountDao.getBeforeMonthMonthlyAccount(monthly);
						List<InitInventoryModel> initList=new ArrayList<>();
						// 如果此商家 此仓库没有月结过 就查期初  如果月结过 期初未0
						if (monthlyList.size()==0) {
							InitInventoryModel initModel = new InitInventoryModel();
							initModel .setMerchantId(monActModel.getMerchantId());
							initModel.setDepotId(monActModel.getDepotId());
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
						List<InventoryDetailsModel> addDetailsList = inventoryDetailsDao.getDetailsByParams(monActModel.getMerchantId(),monActModel.getDepotId(),TimeUtils.format(monActModel.getEndDate(), "yyyy-MM"),"1");
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
						List<InventoryDetailsModel> subDetailsList = inventoryDetailsDao.getDetailsByParams(monActModel.getMerchantId(),monActModel.getDepotId(),TimeUtils.format(monActModel.getEndDate(), "yyyy-MM"),"0");
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
							String[]keys1 = key.split(";");
							Integer surplusNum = entry.getValue();
							//根据条件获取月结明细
							MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
							monthlyAccountItemModel.setMerchantId(Long.parseLong(keys1[0]));
							monthlyAccountItemModel.setDepotId(Long.parseLong(keys1[1]));
							monthlyAccountItemModel.setGoodsId(Long.parseLong(keys1[2]));
							if(StringUtils.isNotBlank(keys1[3]) && !keys1[3].equals("null")){
								monthlyAccountItemModel.setBatchNo(keys1[3]);
							}
							if(StringUtils.isNotBlank(keys1[4]) && !keys1[4].equals("null")){
								monthlyAccountItemModel.setProductionDate(TimeUtils.strToSqlDate(keys1[4]));
							}
							if(StringUtils.isNotBlank(keys1[5]) && !keys1[5].equals("null")){
								monthlyAccountItemModel.setOverdueDate(TimeUtils.strToSqlDate(keys1[5]));
								//是否过期
								if(monthlyAccountItemModel.getOverdueDate().getTime()>monActModel.getEndDate().getTime()){									
									monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
								}else{
									monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_0);
								}
							}else{
								monthlyAccountItemModel.setIsExpire(DERP.ISEXPIRE_1);
							}
							monthlyAccountItemModel.setType(keys1[6]);
							if(keys1.length == 8 && !keys1[7].equals("null")){
								monthlyAccountItemModel.setUnit(keys1[7]);
							}
							monthlyAccountItemModel.setMonthlyAccountId(id);
							Map<String, Object> params =new HashMap<String, Object>();
							params.put("merchandiseId", monthlyAccountItemModel.getGoodsId());
							MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(params);
							monthlyAccountItemModel.setGoodsNo(merchandiseInfo.getGoodsNo());
							monthlyAccountItemModel.setGoodsName(merchandiseInfo.getName());
							Map<String, Object> params1 =new HashMap<String, Object>();
							params1.put("depotId", monthlyAccountItemModel.getDepotId());
							DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(params1);
							if(depotInfo != null){
								monthlyAccountItemModel.setDepotName(depotInfo.getName());
							}
							monthlyAccountItemModel.setSurplusNum(surplusNum);
							monthlyAccountItemModel.setCreateDate(TimeUtils.getNow());
							monthlyAccountItemModel.setAvailableNum(0);
							monthlyAccountItemModel.setBarcode(merchandiseInfo.getBarcode());//条形码
							monthlyAccountItemModel.setCommbarcode(merchandiseInfo.getCommbarcode());
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
				Integer totalNum = inventoryRealTimeSnapshotDao.getListTotalNum(monActModel.getMerchantId(),monActModel.getDepotId(),TimeUtils.format(TimeUtils.addDay(monActModel.getEndDate(), 1), "yyyy-MM-dd"));
				if(totalNum != null && totalNum > 0){
					//计算页数
					page = totalNum/pageSize;
					if(totalNum%pageSize != 0){
						page +=1;
					}
				}
				//获取实时库存
				for(int i = 0;i<page;i++){
					List<InventoryRealTimeSnapshotModel> list = inventoryRealTimeSnapshotDao.getListByParams(monActModel.getMerchantId(),monActModel.getDepotId(),TimeUtils.format(TimeUtils.addDay(monActModel.getEndDate(), 1), "yyyy-MM-dd"),begin,pageSize);
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
						String unit1 = keyArr[6];
						if (DERP.ORDER_TALLYINGUNIT_00.equals(unit1)) {
							unit="0";
						}else if(DERP.ORDER_TALLYINGUNIT_01.equals(unit1)){
							unit="1";
						}else if(DERP.ORDER_TALLYINGUNIT_02.equals(unit1)){
							unit="2";
						}
					}
					String stockType = null;
					if(StringUtils.isNotBlank(keyArr[7]) && !keyArr[7].equals("null")){
						stockType = keyArr[7];
					}
					//根据条件获取月结表体数据
					MonthlyAccountItemModel maItem = monthlyAccountItemDao.getItemByInventoryRealTimeSnapshot(merchantId,depotId,goodsId,batchNo,productionDate,overdueDate,unit,stockType,id);
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
							if(monAccIteModel.getOverdueDate().getTime()>monActModel.getEndDate().getTime()){								
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
							monAccIteModel.setType(DERP_INVENTORY.INVENTORY_TYPE_0);
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
						if(depotInfo != null){
							monAccIteModel.setDepotName(depotInfo.getName());
						}
						monAccIteModel.setSurplusNum(0);
						monAccIteModel.setMonthlyAccountId(id);
						monAccIteModel.setCommbarcode(merchandiseInfo.getCommbarcode());
						monthlyAccountItemDao.save(monAccIteModel);
					}
				}
				
				
				//修改首次上架日期
				updateAccountItemShaleDate(id);
				
			}
		}
		
		
		
		// =============================保存月结库存快照========================
		//根据当前时间查询月结详情
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("snapshotDate", TimeUtils.formatDay());
		map.put("settlementMonth", TimeUtils.getLastMonth(new Date()));
		List<MonthlyAccountItemModel> monthlyAccountItemModel = monthlyAccountItemDao.getMonthlyAccountItemByCreateDate(map);
		//遍历保存库存快照
		if (monthlyAccountItemModel != null && monthlyAccountItemModel.size() > 0) {
			for (MonthlyAccountItemModel itemModel : monthlyAccountItemModel) {
				MonthlyAccountSnapshotModel monthlyAccountSnapshotModel = new MonthlyAccountSnapshotModel();
				monthlyAccountSnapshotModel.setMerchantId(itemModel.getMerchantId());
				monthlyAccountSnapshotModel.setMerchantName(itemModel.getMerchantName());
				monthlyAccountSnapshotModel.setDepotId(itemModel.getDepotId());
				monthlyAccountSnapshotModel.setDepotName(itemModel.getDepotName());
				monthlyAccountSnapshotModel.setGoodsId(itemModel.getGoodsId());
				monthlyAccountSnapshotModel.setGoodsNo(itemModel.getGoodsNo());
				monthlyAccountSnapshotModel.setGoodsName(itemModel.getGoodsName());
				monthlyAccountSnapshotModel.setBatchNo(itemModel.getBatchNo());
				monthlyAccountSnapshotModel.setProductionDate(itemModel.getProductionDate());
				monthlyAccountSnapshotModel.setOverdueDate(itemModel.getOverdueDate());
				monthlyAccountSnapshotModel.setType(itemModel.getType());
				monthlyAccountSnapshotModel.setSurplusNum(itemModel.getSurplusNum());
				monthlyAccountSnapshotModel.setAvailableNum(itemModel.getAvailableNum());
				monthlyAccountSnapshotModel.setSettlementMonth(itemModel.getSettlementMonth());
				monthlyAccountSnapshotModel.setCreateDate(TimeUtils.getNow());
				monthlyAccountSnapshotModel.setUnit(itemModel.getUnit());
				monthlyAccountSnapshotModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_1);//状态：1未转结 2 已转结'
				monthlyAccountSnapshotModel.setCommbarcode(itemModel.getCommbarcode());// 标准条码
				Long id = monthlyAccountSnapshotDao.save(monthlyAccountSnapshotModel);				
				if (id == null) {
					LOGGER.error("新增月结库存快照失败");
					throw new RuntimeException("新增月结库存快照失败");
				}
			}
		}
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
			inventoryBatch.setDepotId(itemModel.getMerchantId());
			inventoryBatch.setGoodsId(itemModel.getGoodsId());
			inventoryBatch.setUnit(itemModel.getUnit());
			inventoryBatch.setBatchNo(itemModel.getBatchNo());
			inventoryBatch.setProductionDate(itemModel.getProductionDate());
			inventoryBatch.setOverdueDate(itemModel.getProductionDate());
			InventoryBatchModel batchMinDate = inventoryBatchDao.getMinDate(inventoryBatch);
			Timestamp createDate = null;
			if (batchMinDate==null) {
				InventoryBatchBackupModel inventoryBatchBackup=new InventoryBatchBackupModel();
				inventoryBatchBackup.setMerchantId(itemModel.getMerchantId());
				inventoryBatchBackup.setDepotId(itemModel.getMerchantId());
				inventoryBatchBackup.setGoodsId(itemModel.getGoodsId());
				inventoryBatchBackup.setUnit(itemModel.getUnit());
				inventoryBatchBackup.setBatchNo(itemModel.getBatchNo());
				inventoryBatchBackup.setProductionDate(itemModel.getProductionDate());
				inventoryBatchBackup.setOverdueDate(itemModel.getProductionDate());
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
