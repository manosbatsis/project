package com.topideal.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.enums.InventoryThingStatusEnum;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.json.storage.j03.AdjustmentTypeGoodsListJson;
import com.topideal.json.storage.j03.AdjustmentTypeRootJson;
import com.topideal.service.InventoryBatchValidityCheckService;

import net.sf.json.JSONObject;


/**
 *  校验批次库存明细商品效期是否已过期
 * @author baols
 *
 */
@Service
public class InventoryBatchValidityCheckServiceImpl implements InventoryBatchValidityCheckService {

	  /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryBatchValidityCheckServiceImpl.class);

	@Autowired
	private InventoryBatchDao  inventoryBatchDao;//批次库存明细
	@Autowired
	 private RMQProducer rocketMQProducer;// mq	
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;//库存收发明细
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201301900,model=DERP_LOG_POINT.POINT_13201301900_Label,keyword="orderNo")
	public boolean synsInventoryBatchValidityCheck(String json,String keys,String topics,String tags) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("==========校验批次库存明细商品效期是否已过期接口=================>"+json);
		SimpleDateFormat  dateFormat =new SimpleDateFormat("yyyy-MM-dd");	    
		InventoryBatchModel  model=new  InventoryBatchModel();
		// 获取已经过期 但是 过期状态还是未过期 的数据
		List<InventoryBatchModel> inventoryBatchList=   inventoryBatchDao.getIsexpireInventoryBatchlist();
		// 用于存放过期的 商品批次库存信息 
		Map<String, AdjustmentTypeRootJson>adjustmentTypeRootJsonMap=new HashMap<>();
		// 推送仓储list
		List<AdjustmentTypeRootJson> adjustmentTypeRootJsonList=new ArrayList<>();
		// 库存加减明细 list
		List<InventoryDetailsModel> inventoryDetailsList=new ArrayList<>();
		List<InventoryBatchModel> isExpireInventoryBatchList=new ArrayList<>();
		

		if(inventoryBatchList!=null&&inventoryBatchList.size()>0){
			for(InventoryBatchModel inventoryBatchModel:inventoryBatchList ){
				if(inventoryBatchModel.getOverdueDate()!=null){
					String overdueDate =dateFormat.format(inventoryBatchModel.getOverdueDate());
					Timestamp overdueTimestamp= Timestamp.valueOf(overdueDate+" 00:00:00");
					String isExpire = TimeUtils.isNotIsExpire(overdueTimestamp);//判断是否过期是否过期（0是 1否）
					if(DERP.ISEXPIRE_0.equals(isExpire)){
						InventoryBatchModel batchModel=new  InventoryBatchModel();
						batchModel.setIsExpire(DERP.ISEXPIRE_0);//是过期
						batchModel.setModifyDate(TimeUtils.getNow());
						batchModel.setId(inventoryBatchModel.getId());
					  int num=	inventoryBatchDao.modify(batchModel);
					  if(num>0){
						  // 存放过期批次库存商品
						  isExpireInventoryBatchList.add(inventoryBatchModel);
						  LOGGER.info("商家为："+inventoryBatchModel.getMerchantName()+"仓库为："+inventoryBatchModel.getDepotName()+"商品货号为："+inventoryBatchModel.getGoodsNo()+"批次为："+inventoryBatchModel.getBatchNo()+"效期为："+inventoryBatchModel.getOverdueDate()+" 在批次库存明细中已变更为已过期");
					  }else{
						  LOGGER.error("商家为："+inventoryBatchModel.getMerchantName()+"仓库为："+inventoryBatchModel.getDepotName()+"商品货号为："+inventoryBatchModel.getGoodsNo()+"批次为："+inventoryBatchModel.getBatchNo()+"效期为："+inventoryBatchModel.getOverdueDate()+"更新失败");
						  throw new Exception("商家为："+inventoryBatchModel.getMerchantName()+"仓库为："+inventoryBatchModel.getDepotName()+"商品货号为："+inventoryBatchModel.getGoodsNo()+"批次为："+inventoryBatchModel.getBatchNo()+"效期为："+inventoryBatchModel.getOverdueDate()+"更新失败");
					  }
					  					  
					}
				}
			}
		}
		
		// 生产加减明细 和推送仓储实体
		for (InventoryBatchModel inventoryBatchModel : isExpireInventoryBatchList) {
			 // 存储 推送仓储 生成类型调整单的批次库存
			  Long merchantId = inventoryBatchModel.getMerchantId();
			  Long depotId = inventoryBatchModel.getDepotId();
			  String key=merchantId+","+depotId;
			  // 推送仓储表头信息
			  if (!adjustmentTypeRootJsonMap.containsKey(key)) {
				  AdjustmentTypeRootJson adjustmentTypeRootJson=new AdjustmentTypeRootJson();
				  adjustmentTypeRootJson.setMerchantId(String.valueOf(merchantId));//商家ID
				  adjustmentTypeRootJson.setMerchantName(inventoryBatchModel.getMerchantName());//商家名称
				  adjustmentTypeRootJson.setDepotId(String.valueOf(depotId));//仓库ID
				  adjustmentTypeRootJson.setDepotName(inventoryBatchModel.getDepotName());//仓库名称
				  adjustmentTypeRootJson.setTopidealCode(inventoryBatchModel.getTopidealCode());//商家卓志编码
				  adjustmentTypeRootJson.setModel(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_5);// 自然调整
				  adjustmentTypeRootJson.setSourceCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LXTZO));//来源单据号LXTZO"
				  Date date = new Date();// 新建此时的的系统时间
				  String adjustmentTime = TimeUtils.formatFullTime(date);
				  adjustmentTypeRootJson.setAdjustmentTime(adjustmentTime);//调整时间
				  adjustmentTypeRootJson.setSourceTime(adjustmentTime);//单据所属日期
				  String months = TimeUtils.formatMonth(date);						  
				  adjustmentTypeRootJson.setMonths(months);//月份
				  adjustmentTypeRootJsonMap.put(key, adjustmentTypeRootJson);
				  List<AdjustmentTypeGoodsListJson> goodsList = adjustmentTypeRootJson.getGoodsList();
				  if (goodsList==null) {
					  goodsList=new ArrayList<>();
				  }
				  // 表体
				  AdjustmentTypeGoodsListJson adjustmentTypeGoodsListJson = getAdjustmentTypeGoodsListJson(inventoryBatchModel);
				  goodsList.add(adjustmentTypeGoodsListJson);
				  adjustmentTypeRootJson.setGoodsList(goodsList);
				  //存储推送仓储类型调整单 自然过期 实体
				  adjustmentTypeRootJsonList.add(adjustmentTypeRootJson);
				  InventoryDetailsModel addDetailsModel = getInventoryDetailsModel(inventoryBatchModel,adjustmentTypeRootJson, DERP_INVENTORY.INVENTORY_OPERATETYPE_1);						  
				  // 加减明细 调增
				  inventoryDetailsList.add(addDetailsModel);
				  InventoryDetailsModel decreaseDetailsModel = getInventoryDetailsModel(inventoryBatchModel,adjustmentTypeRootJson, DERP_INVENTORY.INVENTORY_OPERATETYPE_0);						  
				  // 加减明细调减
				  inventoryDetailsList.add(decreaseDetailsModel);
				  
			  }else {
				  AdjustmentTypeRootJson adjustmentTypeRootJson = adjustmentTypeRootJsonMap.get(key);
				  List<AdjustmentTypeGoodsListJson> goodsList = adjustmentTypeRootJson.getGoodsList();
				  
				  
				  // 获取 推送仓储表体
				  AdjustmentTypeGoodsListJson adjustmentTypeGoodsListJson = getAdjustmentTypeGoodsListJson(inventoryBatchModel);
				  goodsList.add(adjustmentTypeGoodsListJson);						  
				  adjustmentTypeRootJson.setGoodsList(goodsList);

				  // 加减明细 调增						  
				  InventoryDetailsModel addDetailsModel = getInventoryDetailsModel(inventoryBatchModel,adjustmentTypeRootJson, "1");						  
				  inventoryDetailsList.add(addDetailsModel);
				  // 加减明细调减
				  InventoryDetailsModel decreaseDetailsModel = getInventoryDetailsModel(inventoryBatchModel,adjustmentTypeRootJson, "0");
				  inventoryDetailsList.add(decreaseDetailsModel);
			  }
		}
		
		// 生成加减明细
		for (InventoryDetailsModel inventoryDetailsModel : inventoryDetailsList) {
			inventoryDetailsDao.save(inventoryDetailsModel);
		}
		// 推送仓储
		for (AdjustmentTypeRootJson adjustmentTypeRootJson : adjustmentTypeRootJsonList) {
			String adjustmentTypeMQStr = JSONObject.fromObject(adjustmentTypeRootJson).toString();
			// 自营库存更新推送库存			
			rocketMQProducer.send(adjustmentTypeMQStr, MQStorageEnum.DERP_STORAGE_NATURAL_EXPIRE.getTopic(),MQStorageEnum.DERP_STORAGE_NATURAL_EXPIRE.getTags());						
		}
		
		return true;
	}
	
	// 获取 推仓储 表体
	public AdjustmentTypeGoodsListJson  getAdjustmentTypeGoodsListJson (InventoryBatchModel inventoryBatchModel){
		AdjustmentTypeGoodsListJson adjustmentTypeGoodsListJson =new AdjustmentTypeGoodsListJson();
		  adjustmentTypeGoodsListJson.setGoodsId(String.valueOf(inventoryBatchModel.getGoodsId()));
		  adjustmentTypeGoodsListJson.setGoodsName(inventoryBatchModel.getGoodsName());
		  adjustmentTypeGoodsListJson.setGoodsNo(inventoryBatchModel.getGoodsNo());						  
		  adjustmentTypeGoodsListJson.setBatchNo(inventoryBatchModel.getBatchNo());
		  adjustmentTypeGoodsListJson.setBarcode(inventoryBatchModel.getBarcode());		  
		  adjustmentTypeGoodsListJson.setStockType(inventoryBatchModel.getType());						  
		  String productionDate=null;
		  if (inventoryBatchModel.getProductionDate()!=null) {			  
			  productionDate =TimeUtils.formatDay(inventoryBatchModel.getProductionDate());
		  }
		  String overdueDate=null;
		  if (inventoryBatchModel.getOverdueDate()!=null) {
			  overdueDate=TimeUtils.formatDay(inventoryBatchModel.getOverdueDate());
		  }
		  adjustmentTypeGoodsListJson.setProductionDate(productionDate);
		  
		  adjustmentTypeGoodsListJson.setOverdueDate(overdueDate);
		  adjustmentTypeGoodsListJson.setAdjustTotal(inventoryBatchModel.getSurplusNum());
		  adjustmentTypeGoodsListJson.setUnit(inventoryBatchModel.getUnit());
		  
		return adjustmentTypeGoodsListJson;
	}
	
	
	// 生成加减明细
	public InventoryDetailsModel getInventoryDetailsModel (InventoryBatchModel inventoryBatchModel,AdjustmentTypeRootJson adjustmentTypeRootJson,String operateType){
		InventoryDetailsModel indetailModel = new InventoryDetailsModel();
		//indetailModel.setStorePlatformName(null);
		indetailModel.setMerchantId(inventoryBatchModel.getMerchantId());
		indetailModel.setMerchantName(inventoryBatchModel.getMerchantName());
		indetailModel.setIsTopBooks(inventoryBatchModel.getIsTopBooks());
		indetailModel.setDepotId(inventoryBatchModel.getDepotId());
		indetailModel.setDepotName(inventoryBatchModel.getDepotName());
		indetailModel.setDepotCode(inventoryBatchModel.getDepotCode());
		indetailModel.setTopidealCode(inventoryBatchModel.getTopidealCode());
		indetailModel.setDepotType(inventoryBatchModel.getDepotType());
		indetailModel.setGoodsId(inventoryBatchModel.getGoodsId());
		indetailModel.setGoodsName(inventoryBatchModel.getGoodsName());
		indetailModel.setBarcode(inventoryBatchModel.getBarcode());
		indetailModel.setOrderNo(adjustmentTypeRootJson.getSourceCode());
		indetailModel.setBusinessNo(adjustmentTypeRootJson.getSourceCode());
		indetailModel.setSource(SourceStatusEnum.LETZD.getKey());
		indetailModel.setSourceType(InventoryStatusEnum.ZRGQ.getKey());
		indetailModel.setBatchNo(inventoryBatchModel.getBatchNo());
		indetailModel.setNum(inventoryBatchModel.getSurplusNum());
		indetailModel.setProductionDate(inventoryBatchModel.getProductionDate());
		indetailModel.setOverdueDate(inventoryBatchModel.getOverdueDate());	
		if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(operateType)) {// 自然过期出
			indetailModel.setThingStatus(InventoryThingStatusEnum.ZRGQC.getKey());//自然过期出
			indetailModel.setIsExpire(DERP.ISEXPIRE_1);// 未过期
		}else {
			indetailModel.setThingStatus(InventoryThingStatusEnum.ZRGQR.getKey());// 自然过期入
			indetailModel.setIsExpire(DERP.ISEXPIRE_0);//已过期
		}
		
		indetailModel.setThingName(
				InventoryThingStatusEnum.getInventoryThingStatusEnumValue(indetailModel.getThingStatus()));
		indetailModel.setSourceDate(TimeUtils.getNow());
		indetailModel.setCreateDate(TimeUtils.getNow());
		indetailModel.setUnit(inventoryBatchModel.getUnit());
		indetailModel.setOperateType(operateType);
		indetailModel.setType(inventoryBatchModel.getType());
		
		//归属月份
		indetailModel.setOwnMonth(adjustmentTypeRootJson.getMonths());
		//出入时间
		indetailModel.setDivergenceDate(Timestamp.valueOf(adjustmentTypeRootJson.getAdjustmentTime()));
		indetailModel.setGoodsNo(inventoryBatchModel.getGoodsNo());
		indetailModel.setInventoryBatchId(inventoryBatchModel.getId());// 保存批次库存id
		indetailModel.setCommbarcode(inventoryBatchModel.getCommbarcode());									
		return indetailModel;
	}
 


}
