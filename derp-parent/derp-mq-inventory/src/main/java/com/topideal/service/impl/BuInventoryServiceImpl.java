package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.BuInventoryDao;
import com.topideal.dao.BuInventoryItemDao;
import com.topideal.dao.InitInventoryDao;
import com.topideal.dao.InventoryBatchBackupDao;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.dao.MonthlyAccountItemDao;
import com.topideal.entity.vo.BuInventoryItemModel;
import com.topideal.entity.vo.BuInventoryModel;
import com.topideal.entity.vo.InventoryBatchBackupModel;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.BuInventoryMongoDao;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.BuInventoryService;

import net.sf.json.JSONObject;

/**
 * 事业部库存接口
 * 杨创  2020-04-13
 */
@Service
public class BuInventoryServiceImpl implements BuInventoryService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BuInventoryServiceImpl.class);
	//事务部库存
	@Autowired
	private BuInventoryDao buInventoryDao;
	@Autowired
	private BuInventoryItemDao buInventoryItemDao;
	//库存收发明细
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;
	@Autowired
	private InventoryBatchDao inventoryBatchDao;// 批次库存明细
	@Autowired
	private InventoryBatchBackupDao inventoryBatchBackupDao;// 批次库存备份数据
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb
	@Autowired
	private BuInventoryMongoDao buInventoryMongoDao;// 事业部库存mongo	
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;//商品
	@Autowired
	private BusinessUnitMongoDao businessUnitMongoDao;// 事业部
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao;//标准条码
	@Autowired
	private BrandParentMongoDao brandParentMongoDao;//标准品牌

	@Autowired
	private BrandMongoDao brandMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;//mq
    @Autowired
    private MonthlyAccountDao monthlyAccountDao;
    @Autowired 
    private MonthlyAccountItemDao monthlyAccountItemDao;//月结表体数据
    @Autowired 
    private InitInventoryDao initInventoryDao;//期初
    //@Autowired 
   // private InventoryLocationMappingMongoDao InventoryLocationMappingMongoDao;// 

	/**
	 * 事业部库存接口
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201306700, model = DERP_LOG_POINT.POINT_13201306700_Label, keyword = "orderNo")
	public boolean synsBuInventory(String json, String keys,String topics,String tags) throws Exception {
		
		LOGGER.info("刷新事业部json:"+json);
		JSONObject jsonObj = JSONObject.fromObject(json);
		// 强制刷新  1强制刷新
		String falg = (String) jsonObj.get("falg");
		// 如果日期不为空取系统当前月份 如果日期为空不取
		String month = (String) jsonObj.get("month");//月份
		Integer merchantIdInteger  = (Integer) jsonObj.get("merchantId");//商家
		Integer depotIdInteger = (Integer) jsonObj.get("depotId");//仓库				
		Long merchantId=null;Long depotId=null;   		
		if (merchantIdInteger!=null)merchantId=Long.valueOf(merchantIdInteger);
		if (depotIdInteger!=null)depotId=Long.valueOf(depotIdInteger);
		if (StringUtils.isBlank(month)) throw new RuntimeException("刷新月份不能为空");		
		if (month.length()!=7)throw new RuntimeException("刷新月份格式不正确");
			
		// 录入期初是是3月份 必须保证 刷新月份大于3月份  只能刷新 3月份之后数据
		if (Timestamp.valueOf("2020-07-01 00:00:00").getTime()>=Timestamp.valueOf(month+"-01 00:00:00").getTime()) {
			if ("1".equals(falg)) {				
			}else {
				throw new RuntimeException("刷新月份必须大于7月份");		
			}					
		}		
		//页面的刷新判断是否结转
		if (merchantId!=null&&depotId!=null) {
			//判断是否结转 true 已经结转  flase 未结转
			Boolean checkFlag=checkMonthlyAccount(merchantId,depotId,month);
			if (checkFlag) {
            	LOGGER.info("商家ID:" + merchantId + ",仓库Id：" + depotId + "月份：" + month + "已结转,结束执行");
                throw new RuntimeException("商家ID:" + merchantId + ",仓库Id：" + depotId + "月份：" + month + "已结转,结束执行");
            }
		}
				
		Map<String, Object> params =new HashMap<String, Object>();
		// 获取事业部
		List<BusinessUnitMongo> businessUnitList= businessUnitMongoDao.findAll(params);
		Map<Long, BusinessUnitMongo>businessUnitMap=new HashMap<>();
		// 事业部
		for (BusinessUnitMongo mongo : businessUnitList) {
			businessUnitMap.put(mongo.getBusinessUnitId(), mongo);
		}
		businessUnitList=null;
		// 标准条码
		Map<String, CommbarcodeMongo>commbarcodeMap=new HashMap<>();
		List<CommbarcodeMongo> commbarcodeList = commbarcodeMongoDao.findAll(params);
		for (CommbarcodeMongo mongo : commbarcodeList) {
			commbarcodeMap.put(mongo.getCommbarcode(), mongo);
		}	
		commbarcodeList=null;
		// 标准品牌
		Map<Long, BrandParentMongo> brandParentMap=new HashMap<>();
		List<BrandParentMongo> brandParentList = brandParentMongoDao.findAll(params);
		for (BrandParentMongo mongo : brandParentList) {
			brandParentMap.put(mongo.getBrandParentId(), mongo);
		}
		brandParentList=null;
		// 查询商家
		Map<Long, MerchantInfoMongo> merchantInfoMap=new HashMap<>();
		List<MerchantInfoMongo> merchantInfoList = merchantInfoMongoDao.findAll(params);		
		for (MerchantInfoMongo mongo : merchantInfoList) {
			merchantInfoMap.put(mongo.getMerchantId(), mongo);
		}
		merchantInfoList=null;
		//查询仓库
		Map<Long, DepotInfoMongo> depotInfoMap=new HashMap<>();
		List<DepotInfoMongo> depotInfoList= depotInfoMongoDao.findAll(params);
		for (DepotInfoMongo mongo : depotInfoList) {
			depotInfoMap.put(mongo.getDepotId(), mongo);
		}
		depotInfoList=null;
		//查询品牌
		Map<Long, BrandMongo> brandMap=new HashMap<>();
		List<BrandMongo> brandList = brandMongoDao.findAll(params); 
		for (BrandMongo mongo : brandList) {
			brandMap.put(mongo.getBrandId(), mongo);
		}
		brandList=null;
		// 商家事业部
		Map<String, MerchantBuRelMongo> merchantBuRelMap=new HashMap<>();
		List<MerchantBuRelMongo> merchantBuRelList = merchantBuRelMongoDao.findAll(params);
		for (MerchantBuRelMongo mongo : merchantBuRelList) {
			String merchantBuKeyString=mongo.getMerchantId()+","+mongo.getBuId();
			merchantBuRelMap.put(merchantBuKeyString, mongo);
		}
		merchantBuRelList=null;
		
		//存储已经结转的商家仓库
		Map<String, String>monthlyAccountMap=new HashMap<>();
		MonthlyAccountModel monthlyAccount=new MonthlyAccountModel();
		monthlyAccount.setMerchantId(merchantId);
		monthlyAccount.setDepotId(depotId);
		monthlyAccount.setSettlementMonth(month);
		monthlyAccount.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2);
		List<MonthlyAccountModel> monthlyAccountList = monthlyAccountDao.list(monthlyAccount);
		for (MonthlyAccountModel monthlyAccountModel : monthlyAccountList) {
			Long gruopMerchantId = monthlyAccountModel.getMerchantId();
			Long gruopdepotId = monthlyAccountModel.getDepotId();
			
			String key=monthlyAccountModel.getMerchantId()+","+monthlyAccountModel.getDepotId();
			Boolean checkFlag = checkMonthlyAccount(gruopMerchantId, gruopdepotId, month);
			// 已经结转并且结转日期和当前日期比较是否大于1 如果大于1不在刷新
			if (checkFlag) {
				monthlyAccountMap.put(key, key);
			}
						
		}
		monthlyAccountList=null;

		// 本月
		Map<String, Object> thisMonthMap=new HashMap<String, Object>();
		thisMonthMap.put("merchantId", merchantId);
		thisMonthMap.put("depotId", depotId);
		thisMonthMap.put("month", month);//本月
		// 上月		
		Map<String, Object> lastMonthMap=new HashMap<String, Object>();
		lastMonthMap.put("merchantId", merchantId);
		lastMonthMap.put("depotId", depotId);
		lastMonthMap.put("month", TimeUtils.getUpMonth(month));//上一个月
		
		// 判断哪些 商家 仓库 事业部  商品 要进行 生成 库存
		Map<String, Map<String, Object>> goodsKeymap= new HashMap<>();
		
		/**
		 * 1 获取事业期初  
		 * 按商家, 仓库, 事业部,调增调减类型, 商品 ,好坏品 ,效期
		 */
		List<Map<String, Object>> buInitInventoryListMap = initInventoryDao.getBuInitInventory(thisMonthMap);
		Map<String, Integer> surplusNumBuInitMap=new HashMap<>();		
		Map<String, Integer> wornNumBuInitMap=new HashMap<>();
		Map<String, Integer> okNumBuInitMap=new HashMap<>();
		for (Map<String, Object> map : buInitInventoryListMap) {			
			Long merchantIdMap = (Long) map.get("merchant_id");
			Long depotIdMap = (Long) map.get("depot_id");
			Long buId = (Long) map.get("bu_id");			
			Long goodsId = (Long) map.get("goods_id");
			String type = (String) map.get("type");
			BigDecimal num = (BigDecimal) map.get("num");
			String unit = (String) map.get("unit");
			if (StringUtils.isBlank(unit)) {
				unit=null;
			}
			
			if (num==null) {
				num=new BigDecimal(0);
			}
			int intNum=num.intValue();
						
			String key=merchantIdMap+","+depotIdMap+","+buId+","+goodsId+","+unit;
			// 期初总量
			if (surplusNumBuInitMap.containsKey(key)) {
				Integer surplusNum = surplusNumBuInitMap.get(key);
				surplusNumBuInitMap.put(key, intNum+surplusNum);
			}else {
				surplusNumBuInitMap.put(key, intNum);
			}
			// 坏品量
			if (DERP.ISDAMAGE_1.equals(type)) {
				if (wornNumBuInitMap.containsKey(key)) {
					Integer wornNum = wornNumBuInitMap.get(key);
					wornNumBuInitMap.put(key, intNum+wornNum);
				}else {
					wornNumBuInitMap.put(key, intNum);
				}
			}else if (DERP.ISDAMAGE_0.equals(type)) {
				if (okNumBuInitMap.containsKey(key)) {
					Integer okNum = okNumBuInitMap.get(key);
					okNumBuInitMap.put(key, intNum+okNum);
				}else {
					okNumBuInitMap.put(key, intNum);
				}
			}
			goodsKeymap.put(key, map);
		}
		buInitInventoryListMap=null;
			
		//  获取的上月 按商家 仓库事业库存分组的统计量		
		List<Map<String, Object>> lastMonthBuInventoryListMap = buInventoryDao.getMonthBuInventory(lastMonthMap);
		Map<String, Integer> surplusNumBuInventoryMap=new HashMap<>();		
		Map<String, Integer> wornNumBuInventoryMap=new HashMap<>();
		Map<String, Integer> okNumBuInventoryMap=new HashMap<>();
		for (Map<String, Object> map : lastMonthBuInventoryListMap) {
			Long merchantIdMap = (Long) map.get("merchant_id");
			Long depotIdMap = (Long) map.get("depot_id");
			Long buId = (Long) map.get("bu_id");			
			Long goodsId = (Long) map.get("goods_id");
			BigDecimal surplusNum = (BigDecimal) map.get("surplus_num");
			BigDecimal okNum = (BigDecimal) map.get("ok_num");
			BigDecimal wornNum = (BigDecimal) map.get("worn_num");
			String unit = (String) map.get("unit");
			if (StringUtils.isBlank(unit)) {
				unit=null;
			}
			
			String key=merchantIdMap+","+depotIdMap+","+buId+","+goodsId+","+unit;
			if (surplusNum==null) {
				surplusNum=new BigDecimal(0);
			}
			if (okNum==null) {
				okNum=new BigDecimal(0);
			}
			if (wornNum==null) {
				wornNum=new BigDecimal(0);
			}
			surplusNumBuInventoryMap.put(key, surplusNum.intValue());// 事业部  库存总量
			wornNumBuInventoryMap.put(key, wornNum.intValue());  // 事业部 库存 坏品量
			okNumBuInventoryMap.put(key, okNum.intValue()); // 事业部库存 好品品量
			goodsKeymap.put(key, map);
		}
		lastMonthBuInventoryListMap=null;
		/**
		 * 2 获取本月 商家 仓库 事业部 的 加的明细  
		 *  按商家, 仓库, 事业部,调增调减类型, 商品 ,好坏品 ,效期
		 */
		List<Map<String, Object>> goodsThisMonthAddOrSubListMap = inventoryDetailsDao.getMonthBuAddOrSubInventory(thisMonthMap);	

		Map<String, Integer> surplusNumDetailsMap=new HashMap<>();		
		Map<String, Integer> wornNumDetailsMap=new HashMap<>();
		Map<String, Integer> okNumDetailsMap=new HashMap<>();
		for (Map<String, Object> map : goodsThisMonthAddOrSubListMap) {			
			Long merchantIdMap = (Long) map.get("merchant_id");
			Long depotIdMap = (Long) map.get("depot_id");
			Long buId = (Long) map.get("bu_id");			
			Long goodsId = (Long) map.get("goods_id");
			String operateType = (String) map.get("operate_type");
			String type = (String) map.get("type");
			BigDecimal num = (BigDecimal) map.get("num");
			String unit = (String) map.get("unit");
			if (StringUtils.isBlank(unit)) {
				unit=null;
			}
			
			if (num==null) {
				num=new BigDecimal(0);
			}
			int intNum=num.intValue();
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(operateType)&&num.intValue()>0) {
				intNum=-1*intNum;
			}
			
			String key=merchantIdMap+","+depotIdMap+","+buId+","+goodsId+","+unit;
			// 加减明细总量
			if (surplusNumDetailsMap.containsKey(key)) {
				Integer surplusNum = surplusNumDetailsMap.get(key);
				surplusNumDetailsMap.put(key, intNum+surplusNum);
			}else {
				surplusNumDetailsMap.put(key, intNum);
			}
			// 加减明细坏品量
			if (DERP.ISDAMAGE_1.equals(type)) {
				if (wornNumDetailsMap.containsKey(key)) {
					Integer wornNum = wornNumDetailsMap.get(key);
					wornNumDetailsMap.put(key, intNum+wornNum);
				}else {
					wornNumDetailsMap.put(key, intNum);
				}
			}else if (DERP.ISDAMAGE_0.equals(type)) {
				if (okNumDetailsMap.containsKey(key)) {
					Integer okeNum = okNumDetailsMap.get(key);
					okNumDetailsMap.put(key, intNum+okeNum);
				}else {
					okNumDetailsMap.put(key, intNum);
				}
			}
			goodsKeymap.put(key, map);
			
		}
		goodsThisMonthAddOrSubListMap=null;
		
		List<BuInventoryModel>buInventoryList =new ArrayList<BuInventoryModel>();
		Set<String> keySet = goodsKeymap.keySet();
		//Map<String, String> delMap= new HashMap<>();
		for (String key : keySet) {			
			Integer thisSurplusNum=0;Integer thisWornNumNum=0;Integer thisOkNumNum=0;						
			// 上月的 事业部库存
			Integer buInventorySurplusNum = surplusNumBuInventoryMap.get(key);
			Integer buInventoryWornNum = wornNumBuInventoryMap.get(key);
			Integer buInventoryOkNum = okNumBuInventoryMap.get(key);
			
			if (buInventoryWornNum==null)buInventoryWornNum=0;
			if (buInventoryOkNum==null)buInventoryOkNum=0;
			// 本月的加减明细
			Integer detailSurplusNum = surplusNumDetailsMap.get(key);
			Integer detailWornNum = wornNumDetailsMap.get(key);
			Integer detailOkNum = okNumDetailsMap.get(key);
			if (detailSurplusNum==null) detailSurplusNum=0;
			if (detailWornNum==null)detailWornNum=0;
			if (detailOkNum==null)detailOkNum=0;
			// 如果上月事业部库存为空   判断之前该 该商家,仓库,事业部   之前有没有事业部库存  有的话 期初为0 没有的话取 期初			
			Integer buInitSurplusNum=0;Integer buInitwornNum=0;Integer buInitOkNum=0;
			if (buInventorySurplusNum==null) {// 用null 盘点上月是否有值
				if (buInventorySurplusNum==null)buInventorySurplusNum=0;
				int count = buInventoryDao.getBeforeMonthBuInventory(lastMonthMap);
				if (count>0) {// 期初为0
					
				}else{
					buInitSurplusNum=surplusNumBuInitMap.get(key);
					buInitwornNum=wornNumBuInitMap.get(key);
					buInitOkNum=okNumBuInitMap.get(key);					
				}
				
				if (buInitSurplusNum==null)buInitSurplusNum=0;
				if (buInitwornNum==null) buInitwornNum=0;
				if (buInitOkNum==null) buInitOkNum=0;
	
				thisSurplusNum=buInventorySurplusNum+detailSurplusNum+buInitSurplusNum;
				thisWornNumNum=buInventoryWornNum+detailWornNum+buInitwornNum;
				thisOkNumNum=buInventoryOkNum+detailOkNum+buInitOkNum;
				
			}else {
				if (buInventorySurplusNum==null) {
					buInventorySurplusNum=0;
				}
				thisSurplusNum=buInventorySurplusNum+detailSurplusNum;
				thisWornNumNum=buInventoryWornNum+detailWornNum;
				thisOkNumNum=buInventoryOkNum+detailOkNum;
			}
			
			String[]keyArr = key.split(",");
			LOGGER.info("商家id,仓库id,事业部id,商品id,单位");
			
			// 查询商家
			MerchantInfoMongo merchantInfoMongo = merchantInfoMap.get(Long.parseLong(keyArr[0]));
			if (merchantInfoMongo==null) {
				throw new RuntimeException("根据商家id没有查询到商家,商家id:"+Long.parseLong(keyArr[0]));
			}
			
			//查询仓库
			DepotInfoMongo depotInfoMongo = depotInfoMap.get(Long.parseLong(keyArr[1]));
			if (depotInfoMongo==null) {
				throw new RuntimeException("根据仓库id没有查询到仓库,仓库id:"+Long.parseLong(keyArr[1]));
			}
			// 查询事业部
			String merchantBuKey=Long.parseLong(keyArr[0])+","+ Long.parseLong(keyArr[2]); 
			MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMap.get(merchantBuKey);
			if (merchantBuRelMongo==null) {
				throw new RuntimeException("根据商家id:"+keyArr[0]+"事业部id:"+Long.parseLong(keyArr[2])+"没有找到事业部");
			}
			// 获取事业部
			BusinessUnitMongo businessUnitMongo = businessUnitMap.get(Long.parseLong(keyArr[2]));
			if (businessUnitMongo==null) {
				throw new RuntimeException("事业部id:"+Long.parseLong(keyArr[2])+"没有找到事业部");
			}
			
			//查询商品
			Map<String, Object> goodsParams =new HashMap<String, Object>();
			goodsParams.put("merchandiseId", Long.parseLong(keyArr[3]));
			MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(goodsParams);
			if (merchandiseInfo==null) {
				throw new RuntimeException("根据商品id没有查到商品,商品id:"+Long.parseLong(keyArr[3]));
			}
			
			//		获取品牌	
			BrandMongo brandMongo=null;
			if (merchandiseInfo.getBrandId()!=null) {
			 brandMongo = brandMap.get(merchandiseInfo.getBrandId());
			}
			if (brandMongo==null)brandMongo=new BrandMongo();
			// 标准品牌
			BrandParentMongo brandParentMongo=null;
			if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
				CommbarcodeMongo commbarcodeMongo = commbarcodeMap.get(merchandiseInfo.getCommbarcode());
				if (commbarcodeMongo!=null&&commbarcodeMongo.getCommBrandParentId()!=null) {
					brandParentMongo = brandParentMap.get(commbarcodeMongo.getCommBrandParentId());
				}
			}
			if (brandParentMongo==null)brandParentMongo=new BrandParentMongo();

			
			//保存月结明细			
			BuInventoryModel buInventoryModel=new BuInventoryModel();
			buInventoryModel.setMerchantId(merchantInfoMongo.getMerchantId());
			buInventoryModel.setMerchantName(merchantInfoMongo.getName());
			buInventoryModel.setTopidealCode(merchantInfoMongo.getTopidealCode());
			buInventoryModel.setDepotId(depotInfoMongo.getDepotId());
			buInventoryModel.setDepotName(depotInfoMongo.getName());
			buInventoryModel.setDepotCode(depotInfoMongo.getCode());
			buInventoryModel.setDepotType(depotInfoMongo.getType());
			buInventoryModel.setBuId(Long.parseLong(keyArr[2]));
			buInventoryModel.setBuName(merchantBuRelMongo.getBuName());
			buInventoryModel.setGoodsId(merchandiseInfo.getMerchandiseId());	
			buInventoryModel.setGoodsNo(merchandiseInfo.getGoodsNo());
			buInventoryModel.setGoodsName(merchandiseInfo.getName());
			buInventoryModel.setBarcode(merchandiseInfo.getBarcode());
			buInventoryModel.setCommbarcode(merchandiseInfo.getCommbarcode());
			buInventoryModel.setBrandId(brandMongo.getBrandId());
			buInventoryModel.setBrandName(brandMongo.getName());
			buInventoryModel.setSurplusNum(thisSurplusNum);
			buInventoryModel.setWornNum(thisWornNumNum);
			buInventoryModel.setOkNum(thisOkNumNum);
			buInventoryModel.setDepartmentId(businessUnitMongo.getDepartmentId());
			buInventoryModel.setDepartmentName(businessUnitMongo.getDepartmentName());
			buInventoryModel.setParentBrandId(brandParentMongo.getBrandParentId());
			buInventoryModel.setParentBrandName(brandParentMongo.getName());
			buInventoryModel.setSuperiorParentBrandId(brandParentMongo.getSuperiorParentBrandId());
			buInventoryModel.setSuperiorParentBrand(brandParentMongo.getSuperiorParentBrand());
			if(StringUtils.isNotBlank(keyArr[4]) && !keyArr[4].equals("null")){
				buInventoryModel.setUnit(keyArr[4]);
			}			
			buInventoryModel.setMonth(month);			
			buInventoryList.add(buInventoryModel);
			merchandiseInfo=null;
			brandMongo=null;
			merchantInfoMongo=null;
			depotInfoMongo=null;
			businessUnitMongo=null;
		}
		
		
		Map<String, Object>buInventoryDelMap=new HashMap<>();
		if (merchantId!=null) buInventoryDelMap.put("merchantId", merchantId);
		if (depotId!=null)buInventoryDelMap.put("depotId", depotId);
		buInventoryDelMap.put("month", month);//本月	
		//获取目前有的事业部库存
		List<Map<String, Object>> buInventoryGruop = buInventoryDao.getBuInventoryGruop(buInventoryDelMap);
		for (Map<String, Object> gruopMap : buInventoryGruop) {
			Long gruopMerchantId = (Long) gruopMap.get("merchant_id");
			Long gruopdepotId = (Long) gruopMap.get("depot_id");
			Boolean checkFlag = checkMonthlyAccount(gruopMerchantId, gruopdepotId, month);
			// 已经结转的数据不再删除
			if (checkFlag) {
				continue;
			}
			Map<String, Object>delGrupMap=new HashMap<>();
			delGrupMap.put("merchantId", gruopMerchantId);
			delGrupMap.put("depotId", gruopdepotId);
			delGrupMap.put("month", month);//本月
			buInventoryDao.delBuInventory(delGrupMap);
			// 删除mongdb数据
			buInventoryMongoDao.remove(delGrupMap);
			// 删除事业部库存表体
			buInventoryItemDao.delBuInventoryItem(delGrupMap);
		}
		buInventoryGruop=null;
		// 存储的是非结转 并且事业部库存的数据
		Map<String,List<BuInventoryModel>>buInventoryMap=new HashMap<>();	
		// 存储美赞的数据
		//Map<String,List<BuInventoryModel>>buInventoryMapBX=new HashMap<>();	
		// 循环生成 保存到mysql和mongdb
		for (BuInventoryModel model : buInventoryList) {
			String key=model.getMerchantId()+","+model.getDepotId();
			//已经结转的数据不再生成事业部库存
			if (monthlyAccountMap.containsKey(key)) {
				continue;
			}
			buInventoryDao.save(model);
			 // 存储大于0的事业部库存
			 String merchantDepotKey= model.getMerchantId()+","+model.getDepotId()+month;
			 

			 if (buInventoryMap.containsKey(merchantDepotKey)) {
					List<BuInventoryModel> list = buInventoryMap.get(merchantDepotKey);
					list.add(model);
					buInventoryMap.put(merchantDepotKey, list);
				}else {
					List<BuInventoryModel> list =new ArrayList<>();
					list.add(model);
					buInventoryMap.put(merchantDepotKey, list);
				}
			 
		}
		businessUnitMap=null;
		commbarcodeMap=null;
		brandParentMap=null;
		merchantInfoMap=null;
		depotInfoMap=null;
		brandMap=null;
		merchantBuRelMap=null;
		monthlyAccountMap=null;
		

		
		// 非美赞逻辑
		saveBuInventoryItem(buInventoryMap);
		buInventoryMap=null;
		LOGGER.info("生成事业部库存结束");
		return true;
	}

	/**
	 * 生成事业部库存表体
	 * @param saveItemList
	 * @throws Exception 
	 */
	private void saveBuInventoryItem(Map<String,List<BuInventoryModel>>buInventoryMap) throws Exception {
		Set<String> keySet = buInventoryMap.keySet();
		// 同一商家仓库的月份的事业部库存放到一起
		for (String merchantDepotKey : keySet) {
			List<BuInventoryModel> buInventoryList = buInventoryMap.get(merchantDepotKey);
			if (buInventoryList==null||buInventoryList.size()==0)continue;						
			BuInventoryModel buInventoryModel = buInventoryList.get(0);
			Long merchantId = buInventoryModel.getMerchantId();
			Long depotId = buInventoryModel.getDepotId();
			String month = buInventoryModel.getMonth();

			//查询商家
			Map<String, Object>parmMap=new HashMap<String, Object>();
			parmMap.put("merchantId", merchantId);
			MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(parmMap);			
			if (merchantInfoMongo==null) {
				throw new RuntimeException("事业部库存表体获取商家为空");
			}
			// 查询商家仓库月份 库存余量大于0的月结数据(涉及均摊所以要查询所有的月结数据)			
			Map<String, List<MonthlyAccountItemModel>>accountItemMap=getMonthlyAccountItemMap(merchantId,depotId,month);
			//封装表体数据			
			List<BuInventoryItemModel>saveitemALLList=new ArrayList<BuInventoryItemModel>();
			for (BuInventoryModel model : buInventoryList) {				
				Long goodsId = model.getGoodsId();
				String goodsNo = model.getGoodsNo();
				Integer okNum = model.getOkNum();
				if (okNum==null) okNum=0;
				Integer wornNum = model.getWornNum();
				if (wornNum==null) wornNum=0;
				//判断是否是美赞商品
				LOGGER.info("生成事业部库存判断美赞商品前goodsId:"+goodsId+"goodsNo:"+goodsNo);
				/*
				 * if (okNum.intValue()==0&&wornNum.intValue()==0) { }else {
				 * InventoryLocationMappingMongo oriGoodsNoMappingModel =
				 * InventoryLocationMappingMongoDao.getOriGoodsNoMappingModel(merchantInfoMongo.
				 * getTopidealCode(), goodsNo); if (oriGoodsNoMappingModel!=null) { goodsId =
				 * oriGoodsNoMappingModel.getOriginalGoodsId();// 根据库位货号获取原货号 } }
				 */
				
				
				if (okNum.intValue()>0) {
					// 均摊好品
					List<BuInventoryItemModel> shareOkNumList = shareOkNum(goodsId,model,accountItemMap);
					saveitemALLList.addAll(shareOkNumList);
					shareOkNumList=null;
				}else if (okNum.intValue()<0) {// 负数的明细()也要生成事业部库存明细
					String type=DERP_INVENTORY.INITINVENTORY_TYPE_0;
					BuInventoryItemModel fuShuItem =fuShuItem(goodsId,model,okNum.intValue(),type);
					saveitemALLList.add(fuShuItem);
				}
	
				// 均摊坏品
				if (wornNum.intValue()>0) {
					List<BuInventoryItemModel> shareWornNumList = shareWornNum(goodsId,model,accountItemMap);			
					saveitemALLList.addAll(shareWornNumList);
					shareWornNumList=null;
				}else if (wornNum.intValue()<0) {// 负数的明细()也要生成事业部库存明细
					String type=DERP_INVENTORY.INITINVENTORY_TYPE_1;
					BuInventoryItemModel fuShuItem =fuShuItem(goodsId,model,wornNum.intValue(),type);
					saveitemALLList.add(fuShuItem);
				}	
			}
			merchantInfoMongo=null;
			// 生成表体
			for (BuInventoryItemModel saveItem : saveitemALLList) {
				buInventoryItemDao.save(saveItem);
			}
			saveitemALLList=null;
		}

	}
	
	/**
	 * 负数生成事业部库存明细封装
	 * @param goodsId
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	private BuInventoryItemModel fuShuItem(Long goodsId, BuInventoryModel model,int num,String type) throws Exception {
		java.sql.Date  firstShelfDate = getFirstShelfDate(model);
		//封装表体		
		BuInventoryItemModel saveItem=new BuInventoryItemModel();
		saveItem.setBuInventoryId(model.getId());
		saveItem.setGoodsId(model.getGoodsId());
		saveItem.setGoodsNo(model.getGoodsNo());
		saveItem.setDepotId(model.getDepotId());
		saveItem.setMonth(model.getMonth());
		saveItem.setMerchantId(model.getMerchantId());
		saveItem.setNum(num);
		//saveItem.setBatchNo(item.getBatchNo());
		//saveItem.setProductionDate(item.getProductionDate());
		//saveItem.setOverdueDate(item.getOverdueDate());
		//saveItem.setEffectiveInterval(surplusInterval);
		saveItem.setFirstShelfDate(firstShelfDate);
		saveItem.setType(type);
		return saveItem;		
	}

	/**
	 * 均摊坏品
	 * @param goodsId
	 * @param saveitemList
	 * @param model
	 * @param accountItemMap
	 * @throws Exception 
	 */
	private List<BuInventoryItemModel> shareWornNum(Long goodsId, BuInventoryModel model,
			Map<String, List<MonthlyAccountItemModel>> accountItemMap) throws Exception {
		//封装表体
		List<BuInventoryItemModel>saveitemList=new ArrayList<BuInventoryItemModel>();
		Integer wornNum = model.getWornNum();if (wornNum==null) wornNum=0;
		String unit = model.getUnit();
		if (StringUtils.isBlank(unit)) unit="";
		String itemKey=goodsId+","+unit+","+DERP_INVENTORY.INITINVENTORY_TYPE_1;
		List<MonthlyAccountItemModel> itemList = accountItemMap.get(itemKey);
		if (itemList==null)itemList=new ArrayList<MonthlyAccountItemModel>();
		List<MonthlyAccountItemModel>removeItemList=new ArrayList<>();
		for (MonthlyAccountItemModel item : itemList) {
			Integer surplusNum = item.getSurplusNum();
			if (surplusNum==null)continue;
			if (wornNum.intValue()<=0) {
				break;
			}
			int num=0;
			if (wornNum.intValue()>surplusNum.intValue()) {
				num=surplusNum.intValue();
			}else {
				num=wornNum;
			}
			wornNum=wornNum.intValue()-surplusNum.intValue();	
			int shengyuNum=surplusNum.intValue()-num;
			 if (shengyuNum>0) {
				 item.setSurplusNum(shengyuNum);
			 }else {
				 removeItemList.add(item);// 放到移除数据里面
			}

			
			// 获取区间
			String surplusInterval =null;
			if (item.getProductionDate()!=null&&item.getOverdueDate()!=null) {
				Date productionDate = item.getProductionDate();
				Date overdueDate = item.getOverdueDate();
				//剩余失效（天数）
                int surplusDays = 0;
                int nowMonth = TimeUtils.getNowMonth();
                String month = model.getMonth();
                if (month.equals(nowMonth)) {
                	surplusDays = TimeUtils.differentDays(overdueDate, TimeUtils.strToSqlDate(TimeUtils.format(new Date(), "yyyy-MM-dd")));		                        
                } else {
                	surplusDays = TimeUtils.differentDays(overdueDate, TimeUtils.strToSqlDate(TimeUtils.getNextMonthFirstDay(month))) + 1;
                }
              //总效期
                int totalDays = TimeUtils.differentDays(overdueDate, productionDate);
                //剩余失效
//                double surplusRate = String.format("%.2f",((totalDays != 0) ? ((double) surplusDays / (double)totalDays) : 0));
                double surplusRate = (totalDays != 0) ? ((double) surplusDays / (double)totalDays) : 0;
                surplusInterval = this.getSurplusInterval(surplusRate);
			}
			// 均摊表体					
			BuInventoryItemModel saveItem=new BuInventoryItemModel();
			saveItem.setBuInventoryId(model.getId());
			saveItem.setGoodsId(model.getGoodsId());
			saveItem.setGoodsNo(model.getGoodsNo());
			saveItem.setDepotId(model.getDepotId());
			saveItem.setMonth(model.getMonth());
			saveItem.setMerchantId(model.getMerchantId());
			saveItem.setNum(num);
			saveItem.setBatchNo(item.getBatchNo());
			saveItem.setProductionDate(item.getProductionDate());
			saveItem.setOverdueDate(item.getOverdueDate());
			saveItem.setEffectiveInterval(surplusInterval);
			saveItem.setFirstShelfDate(item.getFirstShelfDate());
			saveItem.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
			saveitemList.add(saveItem);
			
		}
		// 还有剩余表体 
		if (wornNum.intValue()>0) {
			BuInventoryItemModel saveItem=new BuInventoryItemModel();
			saveItem.setBuInventoryId(model.getId());
			saveItem.setGoodsId(model.getGoodsId());
			saveItem.setGoodsNo(model.getGoodsNo());
			saveItem.setDepotId(model.getDepotId());
			saveItem.setMonth(model.getMonth());
			saveItem.setMerchantId(model.getMerchantId());
			saveItem.setNum(wornNum);
			//saveItem.setBatchNo(item.getBatchNo());
			//saveItem.setProductionDate(item.getProductionDate());
			//saveItem.setOverdueDate(item.getOverdueDate());
			//saveItem.setEffectiveInterval(surplusInterval);
			// 获取首次上架日期
			java.sql.Date firstShelfDate = getFirstShelfDate(model);
			saveItem.setFirstShelfDate(firstShelfDate);
			saveItem.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
			saveitemList.add(saveItem);
		}			
		itemList.removeAll(removeItemList);// 移除已经核销数据
		accountItemMap.put(itemKey, itemList);
		return saveitemList;
		
	}

	/**
	 * 获取首次上架日期
	 * @param model
	 * @throws Exception 
	 */
	private java.sql.Date getFirstShelfDate(BuInventoryModel model) throws Exception {
		// 查询批次库存最小的创建时间
		InventoryBatchModel inventoryBatch=new InventoryBatchModel();
		inventoryBatch.setMerchantId(model.getMerchantId());
		inventoryBatch.setDepotId(model.getDepotId());
		inventoryBatch.setGoodsId(model.getGoodsId());
		inventoryBatch.setUnit(model.getUnit());
		InventoryBatchModel batchMinDate = inventoryBatchDao.getMinDate(inventoryBatch);
		Timestamp createDate = null;
		if (batchMinDate==null) {
			InventoryBatchBackupModel inventoryBatchBackup=new InventoryBatchBackupModel();
			inventoryBatchBackup.setMerchantId(model.getMerchantId());
			inventoryBatchBackup.setDepotId(model.getDepotId());
			inventoryBatchBackup.setGoodsId(model.getGoodsId());
			inventoryBatchBackup.setUnit(model.getUnit());
			InventoryBatchBackupModel backupMinDate = inventoryBatchBackupDao.getMinDate(inventoryBatchBackup);
			if (backupMinDate!=null)createDate=backupMinDate.getCreateDate();
		}else {
			createDate = batchMinDate.getCreateDate();
		}
		if (createDate!=null) {
			java.sql.Date firstShelfDate = new java.sql.Date(createDate.getTime());
			return firstShelfDate;
		}
		
		return null;
	}

	/**
	 * 均摊好品
	 * @param saveitemList
	 * @param model
	 * @param accountItemMap
	 * @throws Exception 
	 */
	private List<BuInventoryItemModel> shareOkNum(Long goodsId, BuInventoryModel model,
			Map<String, List<MonthlyAccountItemModel>> accountItemMap) throws Exception {
		//封装表体
		List<BuInventoryItemModel>saveitemList=new ArrayList<BuInventoryItemModel>();
		Integer okNum = model.getOkNum();if (okNum==null) okNum=0;		
		String unit = model.getUnit();
		if (StringUtils.isBlank(unit)) unit="";
		String itemKey=goodsId+","+unit+","+DERP_INVENTORY.INITINVENTORY_TYPE_0;
		List<MonthlyAccountItemModel> itemList = accountItemMap.get(itemKey);
		if (itemList==null) itemList=new ArrayList<MonthlyAccountItemModel>();
		List<MonthlyAccountItemModel>removeItemList=new ArrayList<>();
		for (MonthlyAccountItemModel item : itemList) {
			Integer surplusNum = item.getSurplusNum();
			if (surplusNum==null)continue;
			if (okNum.intValue()<=0) {
				break;
			}
			int num=0;
			if (okNum.intValue()>surplusNum.intValue()) {
				num=surplusNum.intValue();
			}else {
				num=okNum;
			}
			okNum=okNum.intValue()-surplusNum.intValue();	
			int shengyuNum=surplusNum.intValue()-num;
			 if (shengyuNum>0) {
				 item.setSurplusNum(shengyuNum);
			 }else {
				 removeItemList.add(item);// 放到移除数据里面
			}
			 
			
			// 获取区间
			String surplusInterval =null;
			if (item.getProductionDate()!=null&&item.getOverdueDate()!=null) {
				Date productionDate = item.getProductionDate();
				Date overdueDate = item.getOverdueDate();
				//剩余失效（天数）
                int surplusDays = 0;
                int nowMonth = TimeUtils.getNowMonth();
                String month = model.getMonth();
                if (month.equals(nowMonth)) {
                	surplusDays = TimeUtils.differentDays(overdueDate, TimeUtils.strToSqlDate(TimeUtils.format(new Date(), "yyyy-MM-dd")));		                        
                } else {
                	surplusDays = TimeUtils.differentDays(overdueDate, TimeUtils.strToSqlDate(TimeUtils.getNextMonthFirstDay(month))) + 1;
                }
              //总效期
                int totalDays = TimeUtils.differentDays(overdueDate, productionDate);
                //剩余失效
//                double surplusRate = String.format("%.2f",((totalDays != 0) ? ((double) surplusDays / (double)totalDays) : 0));
                double surplusRate = (totalDays != 0) ? ((double) surplusDays / (double)totalDays) : 0;
                surplusInterval = this.getSurplusInterval(surplusRate);
			}
			// 均摊表体					
			BuInventoryItemModel saveItem=new BuInventoryItemModel();
			saveItem.setBuInventoryId(model.getId());
			saveItem.setGoodsId(model.getGoodsId());
			saveItem.setGoodsNo(model.getGoodsNo());
			saveItem.setDepotId(model.getDepotId());
			saveItem.setMonth(model.getMonth());
			saveItem.setMerchantId(model.getMerchantId());
			saveItem.setNum(num);
			saveItem.setBatchNo(item.getBatchNo());
			saveItem.setProductionDate(item.getProductionDate());
			saveItem.setOverdueDate(item.getOverdueDate());
			saveItem.setEffectiveInterval(surplusInterval);
			saveItem.setFirstShelfDate(item.getFirstShelfDate());
			saveItem.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
			saveitemList.add(saveItem);
		}
		// 还有剩余表体 
		if (okNum.intValue()>0) {
			BuInventoryItemModel saveItem=new BuInventoryItemModel();
			saveItem.setBuInventoryId(model.getId());
			saveItem.setGoodsId(model.getGoodsId());
			saveItem.setGoodsNo(model.getGoodsNo());
			saveItem.setDepotId(model.getDepotId());
			saveItem.setMonth(model.getMonth());
			saveItem.setMerchantId(model.getMerchantId());
			saveItem.setNum(okNum);
			//saveItem.setBatchNo(item.getBatchNo());
			//saveItem.setProductionDate(item.getProductionDate());
			//saveItem.setOverdueDate(item.getOverdueDate());
			//saveItem.setEffectiveInterval(surplusInterval);
			java.sql.Date  firstShelfDate = getFirstShelfDate(model);
			saveItem.setFirstShelfDate(firstShelfDate);
			saveItem.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
			saveitemList.add(saveItem);
		}
		
		itemList.removeAll(removeItemList);// 移除已经核销数据
		accountItemMap.put(itemKey, itemList);		
		return saveitemList;
	}

	/**
	 * 获取月结表体数据
	 * @param merchantId
	 * @param depotId
	 * @param month
	 * @return
	 */
    private Map<String, List<MonthlyAccountItemModel>> getMonthlyAccountItemMap(Long merchantId, Long depotId,
			String month) {
		Map<String, List<MonthlyAccountItemModel>>accountItemMap=new HashMap<String,  List<MonthlyAccountItemModel>>();
    	MonthlyAccountItemModel accountItem=new MonthlyAccountItemModel();
		accountItem.setMerchantId(merchantId);
		accountItem.setDepotId(depotId);
		accountItem.setSettlementMonth(month);
		List<MonthlyAccountItemModel> monthlyAccountItemList = monthlyAccountItemDao.getMonthlyAccountItem(accountItem);
		for (MonthlyAccountItemModel itemQuery : monthlyAccountItemList) {
			Long goodsId = itemQuery.getGoodsId();
			String unit = itemQuery.getUnit();
			if (StringUtils.isBlank(unit)) unit="";
			String type = itemQuery.getType();
			String itemKey=goodsId+","+unit+","+type;
			if (accountItemMap.containsKey(itemKey)) {
				List<MonthlyAccountItemModel> list = accountItemMap.get(itemKey);
				list.add(itemQuery);
				accountItemMap.put(itemKey, list);
			}else {
				List<MonthlyAccountItemModel> list =new ArrayList<MonthlyAccountItemModel>();
				list.add(itemQuery);
				accountItemMap.put(itemKey, list);
			}
		}
		return accountItemMap;
	}

	private String getSurplusInterval(double rate) {//0＜X≤1/10
        double three = 3.0;
        double one = 1;
        double two = 2;
        if (rate > 0 && rate <= 0.1) {//0＜X≤1/10
            return DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_1;
        } else if (rate > 0.1 && rate <= 0.2) {//"1/10＜X≤1/5"
            return DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_2;
        } else if (rate > 0.2 && rate <= one/three) {//"1/5＜X≤1/3";
            return DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_3;
        } else if (rate > one/three && rate <= 0.5) {//"1/3＜X≤1/2"
            return DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_4;
        } else if (rate > 0.5 && rate <= two/three) {//"1/2＜X≤2/3";
            return DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_5;
        } else if (rate > two/three) {//"2/3以上";
            return DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_6;
        } else {// "过期";
            return DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_7;
        }

    }
	
	/**
	 * 
	 * @param merchantId
	 * @param depotId
	 * @return
	 * @throws SQLException 
	 */
	private Boolean checkMonthlyAccount(Long merchantId, Long depotId,String month) throws SQLException {
		 //查询仓库本月结转单状态是否已结转
        MonthlyAccountModel accountModel = new MonthlyAccountModel();
        accountModel.setMerchantId(merchantId);
        accountModel.setDepotId(depotId);
        accountModel.setSettlementMonth(month);
        accountModel = monthlyAccountDao.searchByModel(accountModel);
		// 状态：1未转结 2 已转结
        if (accountModel != null && accountModel.getState().equals(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2)) {
            // 计算结转时间跟当前时间相差天数
            int dayNum = TimeUtils.daysBetween(accountModel.getSettlementDate(), new Date());
            if (dayNum > 1) {
            	LOGGER.info("商家ID:" + merchantId + ",仓库Id：" + depotId + "月份：" + month + "已结转,结束执行");
               return true;
            }
        }
		return false;
	}


	
}









