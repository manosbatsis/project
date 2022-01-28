package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InitInventoryDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.dao.MonthlyAccountItemDao;
import com.topideal.entity.dto.MonthlyAccountItemDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.MonthlyAccountItemService;
import com.topideal.mongo.dao.BatchValidationInfoMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.BatchValidationInfoMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.tools.BaseUtils.Operator;

import net.sf.json.JSONObject;

/**
 * 库存管理-月结账单商品明细-service实现类
 */
@Service
public class MonthlyAccountItemServiceImpl implements MonthlyAccountItemService {
	protected Logger LOGGER = LoggerFactory.getLogger(MonthlyAccountItemServiceImpl.class);
	// 月结账单商品明细dao
	@Autowired
	private MonthlyAccountItemDao monthlyAccountItemDao;
	@Autowired
    private MonthlyAccountDao monthlyAccountDao;
	//库存期初
	@Autowired
	private InitInventoryDao initInventoryDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;
	@Autowired
	private BatchValidationInfoMongoDao batchValidationInfoMongoDao;
	@Autowired
	private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	/**
	 * 月结账单商品明细列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public MonthlyAccountItemModel listMonthlyAccountItem(MonthlyAccountItemModel model) throws SQLException {
		return monthlyAccountItemDao.searchByPage(model);
	}

	/**
	 * 根据主键id 查询月库存转结详情
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public List<MonthlyAccountItemDTO> searchlist(MonthlyAccountItemDTO model,Long depotId, Timestamp firstDate) throws SQLException {
		List<MonthlyAccountItemDTO> list= null;
		/*
		 * 根据仓库配置，使用不同的分组统计
		 */
		//查询条件
		Map<Operator,Map<String,Object>> params = new HashMap<Operator,Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("depotId", depotId);
		params.put(Operator.eq, map);
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("effectiveTime", firstDate.getTime());
		params.put(Operator.le, map1);
		//排序字段及方式
		List<Map<String, String>> sortList = new ArrayList<Map<String, String>>();
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "effectiveTime");
		sortList.add(sort);
		Map<String, String> sort1 = new HashMap<String, String>();
		sort1.put("derection", "DESC");
		sort1.put("property", "createDate");
		sortList.add(sort1);
		//获取仓库配置的变更记录
		BatchValidationInfoMongo bvInfo = batchValidationInfoMongoDao.findOne(params,sortList);
		String bv = "1";//是否强校验
		if(bvInfo != null){
			bv = bvInfo.getBatchValidation();
		}else{//变更记录为空，取仓库当前配置
			Map<String, Object> depotParams = new HashMap<String, Object>();
			depotParams.put("depotId", depotId);
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotParams);
			if(depot != null){
				bv = depot.getBatchValidation();
			}
		}
		if("1".equals(bv)){//按批次分组
			list = monthlyAccountItemDao.getMonthlyAccountItemModelMapList(model);
		}else{//按商品分组
			list = monthlyAccountItemDao.getItemListGroupGoods(model);
		}
		return list;
	}
	
	/**
	 * 校验月结现存量和 库存余量是否相等
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<MonthlyAccountItemModel>  checkMonthlySurplusNum(MonthlyAccountItemModel model) throws SQLException {		
		return monthlyAccountItemDao.checkMonthlySurplusNum(model);
	}

	/**
	 * 按计算库存量结转
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public boolean confirmMonthlySurplusNum(MonthlyAccountItemModel model) throws SQLException {
		// TODO Auto-generated method stub
        boolean falg=true;
		try {
			List<MonthlyAccountItemModel> monAtItemModelList = monthlyAccountItemDao.list(model);
			if (monAtItemModelList != null && monAtItemModelList.size() > 0) {
				for (MonthlyAccountItemModel itemModel : monAtItemModelList) {
					itemModel.setSettlementNum(itemModel.getSurplusNum());
					monthlyAccountItemDao.modify(itemModel);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			falg=false;
		}
		return falg;
	}

	
	/**
	 * 按计算仓库现存量结转
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String,Object>> createMonthlyAvailableNum(Long depotId, Timestamp firstDate,MonthlyAccountItemModel model) throws SQLException {
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		//获取仓库配置变更记录
		Map<Operator,Map<String,Object>> params = new HashMap<Operator,Map<String,Object>>();
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("depotId", depotId);
		params.put(Operator.eq, map2);
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("effectiveTime", firstDate.getTime());
		params.put(Operator.le, map1);
		//排序字段及方式
		List<Map<String, String>> sortList = new ArrayList<Map<String, String>>();
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "effectiveTime");
		sortList.add(sort);
		Map<String, String> sort1 = new HashMap<String, String>();
		sort1.put("derection", "DESC");
		sort1.put("property", "createDate");
		sortList.add(sort1);
		//获取仓库配置的变更记录
		BatchValidationInfoMongo bvInfo = batchValidationInfoMongoDao.findOne(params,sortList);
		String bv = "1";//是否强校验
		if(bvInfo != null){
			bv = bvInfo.getBatchValidation();
		}else{//变更记录为空，取仓库当前配置
			Map<String, Object> depotParams = new HashMap<String, Object>();
			depotParams.put("depotId", depotId);
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotParams);
			if(depot != null){
				bv = depot.getBatchValidation();
			}
		}
		/*
		 * 根据配置按不同维度计算差异
		 */
		List<Map<String,Object>> differenceList = null;
		if("1".equals(bv)){//按批次获取有差异的数据
			differenceList = monthlyAccountItemDao.getDifferenceList(model);
		}else{//按货号获取有差异的数据
			differenceList = monthlyAccountItemDao.getDifferenceGroupGoods(model);
		}
		//根据差异生成调整单
		if (differenceList != null && differenceList.size() > 0) {
			for (Map<String,Object> map : differenceList) {
				Long merchantId = Long.valueOf(map.get("merchant_id").toString());
				Long depotId1 = Long.valueOf(map.get("depot_id").toString());
				Long goodsId = Long.valueOf(map.get("goods_id").toString());
				String batchNo = null;
				if(map.containsKey("batch_no") && map.get("batch_no") != null && !"".equals(map.get("batch_no").toString())){
					batchNo = map.get("batch_no").toString();
				}
				String type = map.get("type").toString();
				String unit = (String)map.get("unit");
				Integer totalSurplusNum = Integer.valueOf(map.get("total_surplus_num").toString());
				Integer totalAvailableNum = Integer.valueOf(map.get("total_available_num").toString());
				//批次库存余量小于仓库现存量，生成月结损益单调增记录。调增数量=该批次仓库现存量-该批次库存余量
				if(totalSurplusNum < totalAvailableNum){
					//需要调增的数量
					Integer num = totalAvailableNum - totalSurplusNum;
					//根据条件获取需要调增的记录（根据配置确定排序方式 弱校验：无批次优先；强校验：有批次优先且按失效日期desc）
					List<MonthlyAccountItemModel> itemList = monthlyAccountItemDao.getAddItemByParam(merchantId,depotId1,goodsId,batchNo,unit,type,bv);
					if(itemList != null && itemList.size()>0){
						Map<String,Object> dataMap = new HashMap<String,Object>();
						dataMap.put("model", itemList.get(0));
						dataMap.put("num", num);
						dataMap.put("type", "1");
						mapList.add(dataMap);
					}
				}else{//批次库存余量大于现存量，生成月结损益单调减记录。调减数量=该批次库存余量-该批次仓库现存量。
					//需要调减的数量
					Integer num = totalSurplusNum - totalAvailableNum;
					//根据条件获取需要调减的记录（根据配置确定排序方式 弱校验：无批次优先；强校验：有批次优先且按失效日期asc）
					List<MonthlyAccountItemModel> itemList = monthlyAccountItemDao.getSubItemByParam(merchantId,depotId1,goodsId,batchNo,unit,type,bv);
					for(MonthlyAccountItemModel iModel:itemList){
						if(num == 0){//需要调减的数量为0， 则不需要再执行
							break;
						}
						//可扣减的数量
						Integer differenceNum = iModel.getSurplusNum();
//						Integer differenceNum = iModel.getSurplusNum()-iModel.getAvailableNum();
//						if(differenceNum == 0){//无可扣减的数量
//							continue;
//						}
						if(differenceNum < num){//可扣减的数量小于需要调减的数量，调整数量为可扣减的数量
							Map<String,Object> dataMap = new HashMap<String,Object>();
							dataMap.put("model", iModel);
							dataMap.put("num", differenceNum);
							dataMap.put("type", "0");
							mapList.add(dataMap);
							num = num - differenceNum;
						}else{//可扣减的数量大于或等于需要调减的数量，调整数量为需要调减的数量
							Map<String,Object> dataMap = new HashMap<String,Object>();
							dataMap.put("model", iModel);
							dataMap.put("num", num);
							dataMap.put("type", "0");
							mapList.add(dataMap);
							num = 0;
						}
					}
				}
			}
		}
		return mapList;
	}
	
	
	
	/**
	 * 按现存量结转 (修改月结状态,同时更新期末库存值)
	 * @param model
	 * @return
	 * @throws SQLException
	 */

	@Override
	public boolean confirmMonthlyAvailableNum(MonthlyAccountItemModel model) throws SQLException {
        boolean falg=true; 
        int count=0;
		try {
			List<MonthlyAccountItemModel> monAtItemModelList = monthlyAccountItemDao.list(model);
			if (monAtItemModelList != null && monAtItemModelList.size() > 0) {
				for (MonthlyAccountItemModel itemModel : monAtItemModelList) {
					itemModel.setSettlementNum(itemModel.getAvailableNum());
					itemModel.setModifyDate(TimeUtils.getNow());
					count+=monthlyAccountItemDao.updateMonthlyAccountItemModelSettlementNum(itemModel);
				}
			}
			if(count>0&&count==monAtItemModelList.size()){
				
			}else{
				falg=false; 
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			falg=false; 
		}
		return falg;
	}

	@Override
	public List<Map<String, Object>> exportMonthlyAccountItemMap(MonthlyAccountItemModel model)
			throws SQLException {
		List<Map<String, Object>> list = null;
		MonthlyAccountModel  monthlyAccountModel= monthlyAccountDao.searchById(model.getMonthlyAccountId());
		/*
		 * 根据仓库配置，使用不同的分组统计
		 */
		//查询条件
		Map<Operator,Map<String,Object>> params = new HashMap<Operator,Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("depotId", monthlyAccountModel.getDepotId());
		params.put(Operator.eq, map);
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("effectiveTime", monthlyAccountModel.getFirstDate().getTime());
		params.put(Operator.le, map1);
		//排序字段及方式
		List<Map<String, String>> sortList = new ArrayList<Map<String, String>>();
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "effectiveTime");
		sortList.add(sort);
		Map<String, String> sort1 = new HashMap<String, String>();
		sort1.put("derection", "DESC");
		sort1.put("property", "createDate");
		sortList.add(sort1);
		//获取仓库配置的变更记录
		BatchValidationInfoMongo bvInfo = batchValidationInfoMongoDao.findOne(params,sortList);
		String bv = "1";//是否强校验
		if(bvInfo != null){
			bv = bvInfo.getBatchValidation();
		}else{//变更记录为空，取仓库当前配置
			Map<String, Object> depotParams = new HashMap<String, Object>();
			depotParams.put("depotId", monthlyAccountModel.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotParams);
			if(depot != null){
				bv = depot.getBatchValidation();
			}
		}
		if("1".equals(bv)){//按批次分组
			list = monthlyAccountItemDao.exportMonthlyAccountItemModelMapList(model);
		}else{//按商品分组
			list = monthlyAccountItemDao.exportItemListGroupGoods(model);
		}
		return list;
	}

	
	
	@Override
	public void refreshMonthlyBill(MonthlyAccountModel monthlyAccountModel ,Timestamp endDate,Long monthlyAccountId) throws Exception {
		
		
		//刷新月结明细，先删除旧的明细
		monthlyAccountItemDao.delItemByMonthlyAccountId(monthlyAccountId);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("monthlyAccountId", monthlyAccountId.toString());
		jsonObj.put("tag", "1");//tag "1" 来源页面
		SendResult result = rocketMQProducer.send(jsonObj.toString(), MQInventoryEnum.INVENTORY_REFRESH_MONTHLY_BILL.getTopic(),MQInventoryEnum.INVENTORY_REFRESH_MONTHLY_BILL.getTags());
		LOGGER.info("刷新月结数据MsgId:"+result.getMsgId());

	}

	
}
