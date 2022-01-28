package com.topideal.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryAddSubOrderDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.entity.vo.InventoryAddSubOrderModel;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j07.InventoryWriteOffRootJson;
import com.topideal.service.InventoryWriteOffService;
import com.topideal.system.JsonUtils;

import net.sf.json.JSONObject;

/**
 * 库存红冲单据
 * 
 * @author 联想302 baols 2018-06-11
 */
@Service
public class InventoryWriteOffServiceImpl implements InventoryWriteOffService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryWriteOffServiceImpl.class);
	//库存收发明细
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;
	//库存加减接口来源单据表
	@Autowired
	private InventoryAddSubOrderDao inventoryAddSubOrderDao ;
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;
	 @Autowired
	 private RMQProducer rocketMQProducer;
	/**
	 * 库存红冲接口
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_20400000002, model = DERP_LOG_POINT.POINT_20400000002_Label, keyword = "orderNo")
	public boolean synsInventoryWriteOff(String json, String keys,String topics,String tags) throws Exception {
	
		//一、json报文转实体
		InventoryWriteOffRootJson writeOffRootJson =(InventoryWriteOffRootJson)JsonUtils.toBean(JSONObject.fromObject(json), InventoryWriteOffRootJson.class);
		//二、报文参数校验
		checkJsonData(writeOffRootJson);
		
		//三、唯一单号校验
		String orderNo = writeOffRootJson.getOrderNo();
		InventoryAddSubOrderModel submodel=new InventoryAddSubOrderModel();
		submodel.setOrderNo(orderNo);
		submodel.setSourceType("3");//来源类型 1-库存扣减 2-库存回滚,3.库存红冲
		submodel.setCreateDate(TimeUtils.getNow());
		try{
			inventoryAddSubOrderDao.save(submodel);
		}catch(Exception e){
			LOGGER.error("红冲单号已存在,库存红冲失败："+orderNo);
			throw new RuntimeException("红冲单号已存在,库存红冲失败："+orderNo);
		}		
		//查询加减流水
		InventoryDetailsModel inventoryDetailsModel =new InventoryDetailsModel();
		inventoryDetailsModel.setOrderNo(writeOffRootJson.getSourceOrderNo());
		List<InventoryDetailsModel> list = inventoryDetailsDao.list(inventoryDetailsModel);
		if (list==null||list.size()<=0) {			
			throw new RuntimeException("根据红冲单号没有查到加减明细数据,单号:"+orderNo);
		}
		
		
		

		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		//封装红冲推送库存		
		for (InventoryDetailsModel model : list) {
			if (!writeOffRootJson.getMerchantId().equals(model.getMerchantId().toString())) {
				throw new RuntimeException("红冲单号:"+writeOffRootJson.getSourceOrderNo()+"存在不同商家的流水");
			}
			if (model.getNum().intValue()<0) {
				throw new RuntimeException("红冲单号:"+writeOffRootJson.getSourceOrderNo()+"流水的数量不能小于0");
			}
			
			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			invetAddOrSubtractGoodsListJson.setGoodsId(model.getGoodsId().toString());
			invetAddOrSubtractGoodsListJson.setGoodsName(model.getGoodsName());
			invetAddOrSubtractGoodsListJson.setGoodsNo(model.getGoodsNo());
			invetAddOrSubtractGoodsListJson.setBarcode(model.getBarcode());
			invetAddOrSubtractGoodsListJson.setBatchNo(model.getBatchNo());		
			String isExpire = DERP.ISEXPIRE_1;
			
			if (model.getOverdueDate()!=null) {
				Date overdueDateTimestamp = model.getOverdueDate();
				invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
				if(model.getOverdueDate()!=null) {
					isExpire = TimeUtils.isNotIsExpireByDate(model.getOverdueDate());//判断是否过期是否过期（0是 1否）
				}
			}

			invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);
			if (model.getProductionDate()!=null) {
				Date productionDateTimestamp  = model.getProductionDate();
				invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
			}
			invetAddOrSubtractGoodsListJson.setType(model.getType());
			invetAddOrSubtractGoodsListJson.setNum(model.getNum());
			invetAddOrSubtractGoodsListJson.setUnit(model.getUnit());
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(model.getOperateType())) {
				invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
			}
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(model.getOperateType())) {
				invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
			}
			
			invetAddOrSubtractGoodsListJson.setCommbarcode(model.getCommbarcode());
			if (model.getBuId()!=null)invetAddOrSubtractGoodsListJson.setBuId(model.getBuId().toString());			
			if (StringUtils.isNotBlank(model.getBuName()))invetAddOrSubtractGoodsListJson.setBuName(model.getBuName());
			
			
			if (model.getStockLocationTypeId()!=null)invetAddOrSubtractGoodsListJson.setStockLocationTypeId(model.getStockLocationTypeId().toString());
			if (StringUtils.isNotBlank(model.getStockLocationTypeName()))invetAddOrSubtractGoodsListJson.setStockLocationTypeName(model.getStockLocationTypeName());

			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}
		//封装头数据
		InventoryDetailsModel inventoryDetails = list.get(0);
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setDivergenceDate(writeOffRootJson.getDivergenceDate());	// 发货时间
		String ownMonth= TimeUtils.formatMonthForStr(writeOffRootJson.getDivergenceDate());
		// 获取当前年月
		invetAddOrSubtractRootJson.setOwnMonth(ownMonth); // 归属月份
		
		invetAddOrSubtractRootJson.setBackTopic(writeOffRootJson.getBackTopic());
		invetAddOrSubtractRootJson.setBackTags(writeOffRootJson.getBackTags());
		Map<String, Object> customParam = writeOffRootJson.getCustomParam();
		if(customParam==null)customParam = new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setCustomParam(customParam);
		invetAddOrSubtractRootJson.setMerchantId(inventoryDetails.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(inventoryDetails.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(inventoryDetails.getTopidealCode());	//公司编码
		invetAddOrSubtractRootJson.setDepotId(inventoryDetails.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(inventoryDetails.getDepotName());
		invetAddOrSubtractRootJson.setDepotCode(inventoryDetails.getDepotCode());
		invetAddOrSubtractRootJson.setDepotType(inventoryDetails.getDepotType());
		invetAddOrSubtractRootJson.setIsTopBooks(inventoryDetails.getIsTopBooks());
		
		invetAddOrSubtractRootJson.setOrderNo(orderNo);//红冲单号
		invetAddOrSubtractRootJson.setBusinessNo(inventoryDetails.getBusinessNo());	
		invetAddOrSubtractRootJson.setBuId(String.valueOf(inventoryDetails.getBuId())); // 事业部
		invetAddOrSubtractRootJson.setBuName(inventoryDetails.getBuName());
		invetAddOrSubtractRootJson.setSource(inventoryDetails.getSource());
		invetAddOrSubtractRootJson.setSourceType(inventoryDetails.getSourceType());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now1 = sdf.format(new Date());
		invetAddOrSubtractRootJson.setSourceDate(now1);	// 单据时间		
		invetAddOrSubtractRootJson.setIsWriteOff("1");//是否红冲单：1-是，(红冲标识如果为空或者0都表示非红冲单据)	
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		
		
		//四、查询是否月结
		MonthlyAccountModel account = new MonthlyAccountModel();
		account.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
		account.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
		account.setSettlementMonth(invetAddOrSubtractRootJson.getOwnMonth());
		List<MonthlyAccountModel> accountList = monthlyAccountDao.getMonthlyAccount(account);
		if(accountList!=null&&accountList.size()>0&&StringUtils.isNotBlank(accountList.get(0).getState())
				&&accountList.get(0).getState().equals(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2)){//状态：1未转结 2 已转结
			String message = "单号："+invetAddOrSubtractRootJson.getOrderNo()+invetAddOrSubtractRootJson.getDepotName()
					+invetAddOrSubtractRootJson.getOwnMonth()+"已结转,库存红冲失败";
			LOGGER.error(message);
			throw new RuntimeException(message);
		}	
		String jsonMq = JSONObject.fromObject(invetAddOrSubtractRootJson).toString();
		LOGGER.info("红冲单据推送库存加减接口:"+jsonMq);
		rocketMQProducer.send(jsonMq, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		
		return true;
	}


	/**
	 * 业务报文参数校验
	 * @param invetAddOrSubtractRootJson
	 * @throws Exception
     */
	private void checkJsonData(InventoryWriteOffRootJson writeOffRootJson) throws Exception {
		
		if (StringUtils.isBlank(writeOffRootJson.getMerchantId())) {
			LOGGER.error("商家id为空");
			throw new RuntimeException("商家id为空");
		}
		if (StringUtils.isBlank(writeOffRootJson.getMerchantName())) {
			LOGGER.error("商家名称为空");
			throw new RuntimeException("商家名称为空");
		}
		if (StringUtils.isBlank(writeOffRootJson.getSourceOrderNo())) {
			LOGGER.error("红冲单号对应的出入库单号不能为空");
			throw new RuntimeException("红冲单号对应的出入库单号不能为空");
		}
		
		if (StringUtils.isBlank(writeOffRootJson.getOrderNo())) {
			LOGGER.error("来源单据号为空");
			throw new RuntimeException("来源单据号为空");
		}
		if (StringUtils.isBlank(writeOffRootJson.getDivergenceDate())) {
			LOGGER.error("出入时间为空");
			throw new RuntimeException("出入时间为空");
		}

		String orderNo = writeOffRootJson.getOrderNo();
		//目前非采购入库 销售出库、上架入库 单补能红冲 
		if (!(orderNo.startsWith("CGRK")||orderNo.startsWith("XSCO")||orderNo.startsWith("XSCK")||orderNo.startsWith("SJRK"))) {
			LOGGER.error("目前只支持采购入库，销售出库，上架入库的红冲");
			throw new RuntimeException("目前只支持采购入库，销售出库，上架入库的红冲");
		}
	}

}









