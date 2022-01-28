package com.topideal.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryAddSubOrderDao;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.entity.vo.InventoryAddSubOrderModel;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.InventoryQuantityAddOrLowerService;
import com.topideal.system.JsonUtils;

import net.sf.json.JSONObject;

/**
 * 库存数量增减
 * 
 * @author 联想302 baols 2018-06-11
 */
@Service
public class InventoryQuantityAddOrLowerServiceImpl implements InventoryQuantityAddOrLowerService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryQuantityAddOrLowerServiceImpl.class);
	//批次库存收发明细
	@Autowired
	private InventoryBatchDao inventoryBatchDao;
	//库存收发明细
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;
	//库存加减接口来源单据表
	@Autowired
	private InventoryAddSubOrderDao inventoryAddSubOrderDao ;
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;//商品
	@Autowired
	private BrandMongoDao brandMongoDao;
	 @Autowired
	 private RMQProducer rocketMQProducer;
	/**
	 * 调增、调减 库存方法
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201301700, model = DERP_LOG_POINT.POINT_13201301700_Label, keyword = "orderNo")
	public boolean synsAddInventoryQuantity(String json, String keys,String topics,String tags) throws Exception {
		//一、json报文转实体
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson =
				(InvetAddOrSubtractRootJson)JsonUtils.toBean(JSONObject.fromObject(json), InvetAddOrSubtractRootJson.class,
						JsonUtils.getMappingMap("goodsList", InvetAddOrSubtractGoodsListJson.class));
		
		
		//查询仓库
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("depotId", Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
		DepotInfoMongo depot = depotInfoMongoDao.findOne(paramMap);
		if(depot==null){
			LOGGER.error("depotId："+invetAddOrSubtractRootJson.getDepotId() + "仓库不存在");
			throw new RuntimeException("depotId："+invetAddOrSubtractRootJson.getDepotId() + "仓库不存在");
		}
		//二、报文参数校验
		checkJsonData(invetAddOrSubtractRootJson,depot);
		//2、商品信息
		List<InvetAddOrSubtractGoodsListJson> goodsList=invetAddOrSubtractRootJson.getGoodsList();
		//三、唯一单号校验
		InventoryAddSubOrderModel model=new InventoryAddSubOrderModel();
		model.setOrderNo(invetAddOrSubtractRootJson.getOrderNo());
		model.setBusinessNo(invetAddOrSubtractRootJson.getBusinessNo());
		model.setSourceType("1");//来源类型 1-库存扣减 2-库存回滚
		model.setCreateDate(TimeUtils.getNow());
		try{
			inventoryAddSubOrderDao.save(model);
		}catch(Exception e){
			LOGGER.error("单号已存在,库存扣减失败："+invetAddOrSubtractRootJson.getOrderNo());
			throw new RuntimeException("单号已存在,库存扣减失败："+invetAddOrSubtractRootJson.getOrderNo());
	    }

		//四、业务逻辑处理
		//1、检查商家、仓库、月份是否已结转
		MonthlyAccountModel account = new MonthlyAccountModel();
		account.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
		account.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
		account.setSettlementMonth(invetAddOrSubtractRootJson.getOwnMonth());
		List<MonthlyAccountModel> accountList = monthlyAccountDao.getMonthlyAccount(account);
		if(accountList!=null&&accountList.size()>0&&StringUtils.isNotBlank(accountList.get(0).getState())
				&&accountList.get(0).getState().equals(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2)){//状态：1未转结 2 已转结
			String message = "单号："+invetAddOrSubtractRootJson.getOrderNo()+invetAddOrSubtractRootJson.getDepotName()
					+invetAddOrSubtractRootJson.getOwnMonth()+"已结转,库存扣减失败";
			LOGGER.error(message);
			throw new RuntimeException(message);
		}
		
		//2、将需要扣减的商品进行分组合并校验扣减量是否大于库存余量
		mergeGoodsAndCheckNum(invetAddOrSubtractRootJson,depot);
        //五、库存加减校验
		for (InvetAddOrSubtractGoodsListJson goodsListVo : goodsList) {
			InventoryBatchModel inBatchModel = new InventoryBatchModel();
			inBatchModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
			inBatchModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
			inBatchModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
			inBatchModel.setType(goodsListVo.getType());
			inBatchModel.setIsExpire(goodsListVo.getIsExpire());
			//设置批次信息
			if(StringUtils.isNotBlank(goodsListVo.getBatchNo())) {
				inBatchModel.setBatchNo(goodsListVo.getBatchNo());
			}
			if(StringUtils.isNotBlank(goodsListVo.getUnit())){//单位
				inBatchModel.setUnit(goodsListVo.getUnit());
			}
			//商品数量
			int delOrAddNum = goodsListVo.getNum();// 当前商品扣减量
			//调减(扣减的都是好品)
			if(DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {
				// 校验归属月份是否结转和商品可用量是否够扣减
				// 扣减库存
				lowerInventory(invetAddOrSubtractRootJson, delOrAddNum, goodsListVo, inBatchModel,depot);
			//调增
			}else if(DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(goodsListVo.getOperateType())) {
				// 新增库存
				 this.addInventory(invetAddOrSubtractRootJson, delOrAddNum, goodsListVo, inBatchModel);
			}
		}
		//回调通知业务端
		if(StringUtils.isNotBlank(invetAddOrSubtractRootJson.getBackTopic())){
			LOGGER.debug("回调通知业务端开始====="+invetAddOrSubtractRootJson.getOrderNo());
			BackRootJson backRootJson = new BackRootJson();
			backRootJson.setCode(invetAddOrSubtractRootJson.getOrderNo());
			backRootJson.setMerchantName(invetAddOrSubtractRootJson.getMerchantName());
			Map<String, Object> customParam = invetAddOrSubtractRootJson.getCustomParam();
			if(customParam==null){
				customParam = new HashMap<String, Object>();
			}
			backRootJson.setCustomParam(customParam);
			JSONObject tootJson = JSONObject.fromObject(backRootJson);
			SendResult sendResult = rocketMQProducer.send(tootJson.toString(),invetAddOrSubtractRootJson.getBackTopic(),
					invetAddOrSubtractRootJson.getBackTags());
			LOGGER.debug(invetAddOrSubtractRootJson.getOrderNo()+"回调通知业务端结果====="+sendResult);
		}
		return true;
	}


	/**
	 * 业务报文参数校验
	 * @param invetAddOrSubtractRootJson
	 * @throws Exception
     */
	private void checkJsonData(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson,DepotInfoMongo depot) throws Exception {
		//1、校验表头参数
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isBlank(invetAddOrSubtractRootJson.getMerchantId())) {
			LOGGER.error("商家id为空");
			throw new RuntimeException("商家id为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getMerchantName())) {
			LOGGER.error("商家名称为空");
			throw new RuntimeException("商家名称为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getTopidealCode())) {
			LOGGER.error("商家编码为空");
			throw new RuntimeException("商家编码为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDepotId())) {
			LOGGER.error("仓库id为空");
			throw new RuntimeException("仓库id为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDepotCode())) {
			LOGGER.error("仓库编码为空");
			throw new RuntimeException("仓库编码为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getIsTopBooks())) {
			LOGGER.error("是否代销仓为空");
			throw new RuntimeException("是否代销仓为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDepotName())) {
			LOGGER.error("仓库名称为空");
			throw new RuntimeException("仓库名称为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getOrderNo())) {
			LOGGER.error("来源单据号为空");
			throw new RuntimeException("来源单据号为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getSource())) {
			LOGGER.error("单据为空");
			throw new RuntimeException("单据为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getSourceType())) {
			LOGGER.error("单据类型为空");
			throw new RuntimeException("单据类型为空");
		} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getOwnMonth())) {
			LOGGER.error("归属月份为空");
			throw new RuntimeException("归属月份为空");
		}else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDivergenceDate())) {
			LOGGER.error("出入时间为空");
			throw new RuntimeException("出入时间为空");
		}else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getSourceDate())) {
			LOGGER.error("单据日期为空");
			throw new RuntimeException("单据日期为空");
		}
		
		//目前非采购入库 销售出库、上架入库 单补能红冲 
		String isWriteOff = invetAddOrSubtractRootJson.getIsWriteOff();	
		String orderNo = invetAddOrSubtractRootJson.getOrderNo();
		if (DERP.ISWRITEOFF1.equals(isWriteOff)) {
			if (!(orderNo.startsWith("CGRK")||orderNo.startsWith("XSCO")||orderNo.startsWith("XSCK")||orderNo.startsWith("SJRK"))) {
				LOGGER.error("目前只支持采购入库，销售出库，上架入库的红冲");
				throw new RuntimeException("目前只支持采购入库，销售出库，上架入库的红冲");
			}			
		}
		
		
		if (!(invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_008)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0026)				
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0012)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0013)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0014)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0016)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0017)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0019))) {
				if (StringUtils.isBlank(invetAddOrSubtractRootJson.getBuId())) {
					LOGGER.error("表头事业部ID空");
					throw new RuntimeException("表头事业部ID为空");
				}
				if (StringUtils.isBlank(invetAddOrSubtractRootJson.getBuName())) {
					LOGGER.error("表头事业部名称为空");
					throw new RuntimeException("表头事业部名称为空");
				}
			}
		
		
		//2、校验出入时间的年月和归属月份是否一致
		String temTime= TimeUtils.formatMonth(format.parse(invetAddOrSubtractRootJson.getDivergenceDate()));
		if(!temTime.equals(invetAddOrSubtractRootJson.getOwnMonth())){
			LOGGER.error("出入时间的年月和归属月份的年月不相同");
			throw new RuntimeException("出入时间的年月和归属月份的年月不相同");
		}
		// 3、校验表体商品信息
		List<InvetAddOrSubtractGoodsListJson> goodsListVo = invetAddOrSubtractRootJson.getGoodsList();
		if(goodsListVo==null||goodsListVo.size()<1){
			LOGGER.error("商品信息为空");
			throw new RuntimeException("商品信息为空");
		}
		for(InvetAddOrSubtractGoodsListJson goods : goodsListVo) {			
			if (StringUtils.isBlank(goods.getGoodsId())) {
				LOGGER.error("商品id为空");
				throw new RuntimeException("商品id为空");
			}
			if (StringUtils.isBlank(goods.getGoodsNo())) {
				LOGGER.error("商品货号为空");
				throw new RuntimeException("商品货号为空");
			}
			// 电商订单退货    表体事业部 id和名称不能为空
			if (invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_008)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0026)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0012)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0013)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0014)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0016)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0017)
					||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0019)
					) {
				if (StringUtils.isBlank(goods.getBuId())) {
					LOGGER.error("表体事业部ID为空");
					throw new RuntimeException("表体事业部ID为空");
				}
				if (StringUtils.isBlank(goods.getBuName())) {
					LOGGER.error("表体事业部名称为空");
					throw new RuntimeException("表体事业部名称为空");
				}
			}
			
			
			//查询商品
			Map<String, Object> merchandiseMap = new HashMap<String, Object>();
			merchandiseMap.put("merchandiseId", Long.valueOf(goods.getGoodsId()));
			merchandiseMap.put("merchantId", Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
			merchandiseMap.put("goodsNo", goods.getGoodsNo());
			MerchandiseInfoMogo merchandiseMogo = merchandiseInfoMogoDao.findOne(merchandiseMap);
			if (merchandiseMogo==null) {
				LOGGER.debug("根据商品id没有查询到对应的商品,商品货号 :"+goods.getGoodsNo());
				throw new RuntimeException("根据商品id没有查询到对应的商品,商品货号 :"+goods.getGoodsNo()) ;
			}
			// 如果商品标准条码不能为空
			if (StringUtils.isBlank(merchandiseMogo.getCommbarcode())) {
				LOGGER.debug("商品标准条码不能为空,商品货号 :"+goods.getGoodsNo());
				throw new RuntimeException("商品标准条码不能为空,商品货号 :"+goods.getGoodsNo()) ;
			}
			// 把标准条码设置进去
			goods.setCommbarcode(merchandiseMogo.getCommbarcode());
			goods.setBarcode(merchandiseMogo.getBarcode());
			
			//海外仓   单位必填
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(invetAddOrSubtractRootJson.getDepotType())){
				//单位
				String unit = goods.getUnit();
				if(StringUtils.isBlank(goods.getUnit())){
					LOGGER.error("海外仓单位必填");
					throw new RuntimeException("海外仓单位必填");
				}
				
				if (!(DERP.INVENTORY_UNIT_0.equals(unit)||DERP.INVENTORY_UNIT_1.equals(unit)||DERP.INVENTORY_UNIT_2.equals(unit))) {
					throw new RuntimeException("海外仓单位格式必须为 0或者1 或者2");
				}
				
				
			}else {// 非海外仓不能给理货单位
				if(StringUtils.isNotBlank(goods.getUnit())){
					LOGGER.error("非海外仓理货单必须为空");
					throw new RuntimeException("非海外仓理货单必须为空");
				}
			}
			//非空校验
			 if (StringUtils.isBlank(goods.getGoodsId())) {
				LOGGER.error("商品id为空");
				throw new RuntimeException("商品id为空");
			} else if (StringUtils.isBlank(goods.getGoodsName())) {
				LOGGER.error("商品名称为空");
				throw new RuntimeException("商品名称为空");
			}else if (StringUtils.isBlank(goods.getBarcode())) {
				LOGGER.error("商品条码为空");
				throw new RuntimeException("商品条码为空");
			} else if (StringUtils.isBlank(goods.getOperateType())) {
				LOGGER.error("操作类型为空");
				throw new RuntimeException("操作类型为空");
			} else if(!StrUtils.stringReg(goods.getOperateType(),"[01]")){
				LOGGER.error("操作类型为无效数字");
				throw new RuntimeException("操作类型为无效数字");
			}else if(goods.getNum()<=0){
				LOGGER.error("库存数量不能小于等于0");
				throw new RuntimeException("库存数量不能小于等于0");
			}
			 if(StringUtils.isNotBlank(goods.getBatchNo())){
				 goods.setBatchNo(goods.getBatchNo().trim());
			 }
			//仓库是否批次效期强校验
			if(depot.getBatchValidation()==null||depot.getBatchValidation().equals(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1)
					||(depot.getBatchValidation().equals(DERP_SYS.DEPOTINFO_BATCHVALIDATION_2)&&DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(goods.getOperateType()))){
				if(StringUtils.isBlank(goods.getBatchNo())) {
					LOGGER.error("批次号为空");
					throw new RuntimeException("批次号为空");
				}
				if(StringUtils.isBlank(goods.getProductionDate())) {
					LOGGER.error("生产日期为空");
					throw new RuntimeException("生产日期为空");
				}
				if(StringUtils.isBlank(goods.getOverdueDate())) {
					LOGGER.error("失效日期为空");
					throw new RuntimeException("失效日期为空");
				}
			}

			//效期调整单据批次效期必填
			if(invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018)){
				if(StringUtils.isBlank(goods.getBatchNo())) {
					LOGGER.error("批次号为空");
					throw new RuntimeException("批次号为空");
				}
				if(StringUtils.isBlank(goods.getProductionDate())) {
					LOGGER.error("生产日期为空");
					throw new RuntimeException("生产日期为空");
				}
				if(StringUtils.isBlank(goods.getOverdueDate())) {
					LOGGER.error("失效日期为空");
					throw new RuntimeException("失效日期为空");
				}
			}
			//好坏品互转 则好/坏品类型必填
			if(invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0016)){
				if(StringUtils.isBlank(goods.getType())) {
					LOGGER.error("商品分类type为空");
					throw new RuntimeException("商品分类type为空");
				}
			}
			
			//失效日期不为空，是否过期必填
			if(StringUtils.isNotBlank(goods.getOverdueDate())&&StringUtils.isBlank(goods.getIsExpire())) {
				LOGGER.error("是否过期为空");
				throw new RuntimeException("是否过期为空");
			}else if(StringUtils.isNotBlank(goods.getIsExpire())&&!goods.getIsExpire().matches("0|1")){
				LOGGER.error("是否过期字段为无效数字");
				throw new RuntimeException("是否过期字段为无效数字");
			}else if(StringUtils.isBlank(goods.getIsExpire())){
				//业务端没传，则默认为未过期 0 过滤  1 未过期
				goods.setIsExpire(DERP.ISEXPIRE_1);
			}
			
			if(StringUtils.isNotBlank(goods.getType())&&!goods.getType().matches("0|1")) { 
				LOGGER.error("商品分类type字段为无效数字");
				throw new RuntimeException("商品分类type字段为无效数字");
			}else if(StringUtils.isBlank(goods.getType())){
				//业务端没传,则默认好品 0 好品  1 坏品
				goods.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
			}
		}
	}
	/**
	 * 合并商品数量并校验扣减量是否大于库存余量
	 * @param invetAddOrSubtractRootJson
	 * @throws Exception
     */
	private void mergeGoodsAndCheckNum(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson,DepotInfoMongo depot) throws Exception{
		List<InvetAddOrSubtractGoodsListJson> goodsList = invetAddOrSubtractRootJson.getGoodsList();
		Map<String, Map<String, Object>> lowerGoodsMap = new HashMap<String, Map<String,Object>>(); 
		//效期调整按批次效期合并
		if(invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018)){
			for(InvetAddOrSubtractGoodsListJson goodsListVo : goodsList) {
				if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) { // 扣减
					//key=商品id+效期+好品/坏品+理货单位+批次 0 好品  1 坏品
					String type=StringUtils.isBlank(goodsListVo.getType())?"0":goodsListVo.getType();
					String key = goodsListVo.getGoodsId()+"-"+goodsListVo.getProductionDate()
								+"-"+goodsListVo.getOverdueDate()+"-"+type+"-" + goodsListVo.getUnit()
								+"-"+goodsListVo.getBatchNo();
					
					if (lowerGoodsMap.containsKey(key)) {
						Map<String, Object> goods =lowerGoodsMap.get(key);
						int numAll = (int) goods.get("num");
						numAll = numAll+goodsListVo.getNum();
						goods.put("num", numAll);
					} else {
						Map<String, Object> goods = new HashMap<String, Object>();
						goods.put("goodsId", goodsListVo.getGoodsId());
						goods.put("batchNo", goodsListVo.getBatchNo());
						goods.put("productionDate", goodsListVo.getProductionDate());
						goods.put("overdueDate", goodsListVo.getOverdueDate());
						goods.put("type", type);
						goods.put("unit", goodsListVo.getUnit());
						goods.put("num", goodsListVo.getNum());
						lowerGoodsMap.put(key, goods);
					}
				}
			}
		}else{
			/**其他扣减 
			 * key1=商品id+好/坏品+是否过期+理货单位(无批次的数量包含了有批次的数量)
			 * key2=商品id+好/坏品+是否过期+理货单位+批次
			 * */
			for(InvetAddOrSubtractGoodsListJson goodsListVo : goodsList) {
				if(DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) { // 扣减
						//0 好品  1 坏品
						String type=StringUtils.isBlank(goodsListVo.getType())?"0":goodsListVo.getType();
						//0 过滤  1 未过期
						String isExpire=StringUtils.isBlank(goodsListVo.getIsExpire())?"1":goodsListVo.getIsExpire();
						String key1 = goodsListVo.getGoodsId()+"-"+type+"-"+isExpire+"-"+goodsListVo.getUnit();
						String key2 = goodsListVo.getGoodsId()+"-"+type+"-"+isExpire+"-"+goodsListVo.getUnit()
									  +"-"+goodsListVo.getBatchNo();
				
					if (lowerGoodsMap.containsKey(key1)) {
						Map<String, Object> goods =lowerGoodsMap.get(key1);
						int numAll = (int) goods.get("num");
						numAll = numAll+goodsListVo.getNum();
						goods.put("num", numAll);
					} else {
						Map<String, Object> goods = new HashMap<String, Object>();
						goods.put("goodsId", goodsListVo.getGoodsId());
						goods.put("type", type);
						goods.put("isExpire",isExpire);
						goods.put("unit", goodsListVo.getUnit());
						goods.put("num", goodsListVo.getNum());
						lowerGoodsMap.put(key1, goods);
					}
					if(StringUtils.isNotBlank(goodsListVo.getBatchNo())){
						if(lowerGoodsMap.containsKey(key2)) {
							Map<String, Object> goods =lowerGoodsMap.get(key2);
							int numAll = (int) goods.get("num");
							numAll = numAll+goodsListVo.getNum();
							goods.put("num", numAll);
						}else {
							Map<String, Object> goods = new HashMap<String, Object>();
							goods.put("goodsId", goodsListVo.getGoodsId());
							goods.put("type", type);
							goods.put("isExpire",isExpire);
							goods.put("unit", goodsListVo.getUnit());
							goods.put("batchNo", goodsListVo.getBatchNo());
							goods.put("num", goodsListVo.getNum());
							lowerGoodsMap.put(key2, goods);
						}
					}
				}
			}
		}
		
		//合并完成校验扣减量是否大于库存余量
		if(!lowerGoodsMap.isEmpty()){
			for(Map<String, Object> goodsMap:lowerGoodsMap.values()){
		        String goodsId = (String)goodsMap.get("goodsId");
		        String batchNo = (String)goodsMap.get("batchNo");
		        String productionDate = (String)goodsMap.get("productionDate");
		        String overdueDate = (String)goodsMap.get("overdueDate");
		        String type = (String)goodsMap.get("type");//0 好品  1 坏品
		        String isExpire = (String)goodsMap.get("isExpire");//0 过滤  1 未过期
		        String unit = (String)goodsMap.get("unit");
		        int subNum = (int) goodsMap.get("num");
		        
				InventoryBatchModel ibmodel = new InventoryBatchModel();
				ibmodel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
				ibmodel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
				ibmodel.setGoodsId(Long.valueOf(goodsId));
				
				//效期调整单 或仓库配置 强校验
				if(invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018)
						||(depot.getBatchValidation()==null||depot.getBatchValidation().equals("1"))){
					ibmodel.setBatchNo(batchNo);
				}
				
				ibmodel.setType(type);
				if(invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018)){//效期调整
					ibmodel.setProductionDate(TimeUtils.strToSqlDate(productionDate));
					ibmodel.setOverdueDate(TimeUtils.strToSqlDate(overdueDate));
				}else{
					ibmodel.setIsExpire(isExpire);
				}
				ibmodel.setUnit(unit);
				InventoryBatchModel	inventoryBatch = inventoryBatchDao.getInventoryBatchModelAllSurplusNum(ibmodel);
				if(inventoryBatch == null||inventoryBatch.getSurplusNum()==null) {
					String message = "商品id："+goodsId;
					if(StringUtils.isNotBlank(batchNo)) message+=" 批次：" + batchNo;
					LOGGER.error(message+" 无实际库存量");
					throw new RuntimeException(message+" 无实际库存量");
				}
				// 该商品单据出库不够扣减
				if(subNum > inventoryBatch.getSurplusNum()){
					String message = "商品id："+goodsId;
					if(StringUtils.isNotBlank(batchNo)) message+=" 批次：" + batchNo;
					LOGGER.error(message+" 扣减量大于实际库存量");
					throw new RuntimeException(message+" 扣减量大于实际库存量");
				}
			}  
		}

	}

	/**
	 * 减库存量动作
	 * @param invetAddOrSubtractRootJson  表头信息
	 * @param delOrAddNum              表体信息中需要扣减和增加的数量
	 * @param goodsListVo               //商品信息
	 * @param inBatchModel              批次库存明细
	 * @return
	 * @throws Exception
	 */
	private void lowerInventory(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson, int delOrAddNum,
			InvetAddOrSubtractGoodsListJson goodsListVo, InventoryBatchModel inBatchModel,DepotInfoMongo depot) throws Exception {
		
		if(StringUtils.isNotBlank(goodsListVo.getProductionDate())){
		    inBatchModel.setProductionDate(TimeUtils.strToSqlDate(goodsListVo.getProductionDate()));
		}
		if(StringUtils.isNotBlank(goodsListVo.getOverdueDate())){
			inBatchModel.setOverdueDate(TimeUtils.strToSqlDate(goodsListVo.getOverdueDate()));
		}
		
		// 先查询匹配指定效期批次，无则按先失效先出库
		List<InventoryBatchModel> batchModelList = inventoryBatchDao.listOrbyOverdueDate(inBatchModel);
		if(!invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018)){
			inBatchModel.setProductionDate(null);//清空效期
			inBatchModel.setOverdueDate(null);
			List<InventoryBatchModel> batchModelList2 = inventoryBatchDao.listOrbyOverdueDate(inBatchModel);
			batchModelList = mergeBatch(batchModelList, batchModelList2);//合并批次
			
			//根据仓库是否批次强校验判断是否扣其他批次 1-强校验 0-弱校验
			if(StringUtils.isNotBlank(inBatchModel.getBatchNo())&&
					depot.getBatchValidation()!=null&&(
			depot.getBatchValidation().equals(DERP_SYS.BATCHVALIDATION_0)||depot.getBatchValidation().equals(DERP_SYS.BATCHVALIDATION_2)
							)){
				
				inBatchModel.setBatchNo(null);//清空批次号
				batchModelList2 = inventoryBatchDao.listOrbyOverdueDate(inBatchModel);
				batchModelList = mergeBatch(batchModelList, batchModelList2);//合并批次
			}
			
		}

		if(batchModelList == null || batchModelList.size() <1){
			String message = "商品id："+goodsListVo.getGoodsId();
			if(StringUtils.isNotBlank(goodsListVo.getBatchNo())) message+=" 批次："+goodsListVo.getBatchNo();
			LOGGER.error(message+"未查到批次库存明细信息，无法扣减");
			throw new RuntimeException(message+"未查到批次库存明细信息，无法扣减");
		}
		//调减量
		int lessNum=0;
		for(InventoryBatchModel inventoryBatchModel : batchModelList) {
			
			if(delOrAddNum == 0){
				break; //跳出当前循环
			}
			if(delOrAddNum > inventoryBatchModel.getSurplusNum()) {// 接口需扣减的数量大于当前批次的库存数量
				delOrAddNum = delOrAddNum - inventoryBatchModel.getSurplusNum();
				lessNum=inventoryBatchModel.getSurplusNum();
			}else if(delOrAddNum <=inventoryBatchModel.getSurplusNum()) {// 接口需扣减的数量小于当前批次的库存数量
				lessNum=delOrAddNum;//扣减的数里
				delOrAddNum = 0;
			}
			//扣减库存
			Map<String,Object>  parMap=new HashMap<String,Object>();
			parMap.put("onWayNum", lessNum);
			parMap.put("id", inventoryBatchModel.getId());
			int num = 0;
			try{
				num =inventoryBatchDao.updateLowerGoodsNum(parMap);
			}catch(Exception e){
				LOGGER.error("批次库存信息id为："+inventoryBatchModel.getId()+"库存量扣减失败");
				throw new RuntimeException(DERP.MQ_FAILTYPE_03 + " lowerFail");
			}
			if(num<1){
				LOGGER.error("单据号为：" + invetAddOrSubtractRootJson.getOrderNo()+ "库存量扣减失败");
				throw new RuntimeException("单据号为：" + invetAddOrSubtractRootJson.getOrderNo()+ "库存量扣减失败");
			}
			// 保存商品收发明细
			 this.saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inventoryBatchModel,lessNum);
		}
		if (delOrAddNum!=0) {
			throw new RuntimeException("(并发)单据号为：" + invetAddOrSubtractRootJson.getOrderNo()+ "库存量不够扣减");
		}
	}

	/**
	 * 增加库存量动作
	 * @param invetAddOrSubtractRootJson
	 * @param delOrAddNum
	 * @param goodsListVo
	 * @param inBatchModel
     * @throws Exception
     */
	private  void addInventory(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson, int delOrAddNum,
			InvetAddOrSubtractGoodsListJson goodsListVo, InventoryBatchModel inBatchModel) throws Exception {
		
		if(StringUtils.isNotBlank(goodsListVo.getProductionDate())){
			inBatchModel.setProductionDate(TimeUtils.strToSqlDate(goodsListVo.getProductionDate()));
		}
		if(StringUtils.isNotBlank(goodsListVo.getOverdueDate())){
			inBatchModel.setOverdueDate(TimeUtils.strToSqlDate(goodsListVo.getOverdueDate()));
		}		
		//查询批次信息
		inBatchModel = inventoryBatchDao.searchInventoryBatchForAdd(inBatchModel);
		//查找到批次库存信息，调增  否则，保存
		if (inBatchModel == null) {
			Long id = saveInventoryBatch(invetAddOrSubtractRootJson, goodsListVo);// 新增批次库存
			inBatchModel=new InventoryBatchModel();
			inBatchModel.setId(id);// 把新增的批次id保存到 批次实体中
			// 保存库存收发明细
			this.saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inBatchModel,0);
		//调增
		}else{
			Map<String,Object>  parMap=new HashMap<String,Object>();
			parMap.put("onWayNum", delOrAddNum);
			parMap.put("id", inBatchModel.getId());
			int num = inventoryBatchDao.updateAddGoodsNum(parMap);
			if(num<1){
				LOGGER.error("单据号为：" + invetAddOrSubtractRootJson.getOrderNo()+ " 更新批次商品库存明细失败");
				throw new RuntimeException("单据号为：" + invetAddOrSubtractRootJson.getOrderNo()+ " 更新批次商品库存明细失败");
			}
			// 保存库存收发明细
			 saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inBatchModel,0);
		}
	}

	/**
	 * 保存批次库存信息
	 * @param invetAddOrSubtractRootJson
	 * @param goodsListVo
	 * @throws Exception
     */
	private Long saveInventoryBatch(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson,
			InvetAddOrSubtractGoodsListJson goodsListVo) throws Exception {
		//查询商品
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchandiseId", Long.valueOf(goodsListVo.getGoodsId()));
		MerchandiseInfoMogo merchandiseMogo = merchandiseInfoMogoDao.findOne(paramMap);
		
		//查询品牌
		BrandMongo brandMongo = null;
		if(merchandiseMogo!=null && merchandiseMogo.getBrandId()!=null){
			paramMap = new HashMap<String, Object>();
			paramMap.put("brandId", merchandiseMogo.getBrandId());
			brandMongo = brandMongoDao.findOne(paramMap);
		}
		
		InventoryBatchModel inventoryBatchModel = new InventoryBatchModel();
		inventoryBatchModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
		inventoryBatchModel.setMerchantName(invetAddOrSubtractRootJson.getMerchantName());
		inventoryBatchModel.setIsTopBooks(invetAddOrSubtractRootJson.getIsTopBooks());
		inventoryBatchModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
		inventoryBatchModel.setDepotName(invetAddOrSubtractRootJson.getDepotName());
		inventoryBatchModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
		inventoryBatchModel.setGoodsName(goodsListVo.getGoodsName());
		inventoryBatchModel.setBatchNo(goodsListVo.getBatchNo());
		inventoryBatchModel.setSurplusNum(goodsListVo.getNum());
		inventoryBatchModel.setCreateDate(TimeUtils.getNow());
		inventoryBatchModel.setUnit(goodsListVo.getUnit());
		inventoryBatchModel.setGoodsNo(goodsListVo.getGoodsNo());
		inventoryBatchModel.setTopidealCode(invetAddOrSubtractRootJson.getTopidealCode());
		inventoryBatchModel.setDepotCode(invetAddOrSubtractRootJson.getDepotCode());
		inventoryBatchModel.setDepotType(invetAddOrSubtractRootJson.getDepotType());
		inventoryBatchModel.setBarcode(goodsListVo.getBarcode());
		inventoryBatchModel.setType(goodsListVo.getType());
		inventoryBatchModel.setIsExpire(goodsListVo.getIsExpire());
		//归属月份
		inventoryBatchModel.setOwnMonth(invetAddOrSubtractRootJson.getOwnMonth());
		//设置生产日期
		if (StringUtils.isNotBlank(goodsListVo.getProductionDate())) {
			inventoryBatchModel.setProductionDate(TimeUtils.strToSqlDate(goodsListVo.getProductionDate()));
		}
		//设置失效日期
		if (StringUtils.isNotBlank(goodsListVo.getOverdueDate())) {
			inventoryBatchModel.setOverdueDate(TimeUtils.strToSqlDate(goodsListVo.getOverdueDate()));
		}
		if(brandMongo!=null){
			inventoryBatchModel.setBrandId(brandMongo.getBrandId());
			inventoryBatchModel.setBrandName(brandMongo.getName());
		}
		inventoryBatchModel.setCommbarcode(goodsListVo.getCommbarcode());
		Long id = inventoryBatchDao.save(inventoryBatchModel);
		if (id == null) {
			LOGGER.error("新增批次库存明细失败");
			throw new RuntimeException("新增批次库存明细失败");
		}
		return id;
	}

	/**
	 * 保存库存收发明细
	 * @param invetAddOrSubtractRootJson
	 * @param goodsListVo
	 * @param inBatchModel
	 * @param lessNum
	 * @throws Exception
	 */
	private void saveInventoryDetails(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson,
									  InvetAddOrSubtractGoodsListJson goodsListVo,
									  InventoryBatchModel inBatchModel,int lessNum) throws Exception {
		InventoryDetailsModel indetailModel = new InventoryDetailsModel();
		indetailModel.setShopCode(invetAddOrSubtractRootJson.getShopCode());
		indetailModel.setStorePlatformName(invetAddOrSubtractRootJson.getStorePlatformName());
		indetailModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
		indetailModel.setMerchantName(invetAddOrSubtractRootJson.getMerchantName());
		indetailModel.setIsTopBooks(invetAddOrSubtractRootJson.getIsTopBooks());
		indetailModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
		indetailModel.setDepotName(invetAddOrSubtractRootJson.getDepotName());
		indetailModel.setDepotCode(invetAddOrSubtractRootJson.getDepotCode());
		indetailModel.setTopidealCode(invetAddOrSubtractRootJson.getTopidealCode());
		indetailModel.setDepotType(invetAddOrSubtractRootJson.getDepotType());
		indetailModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
		indetailModel.setGoodsName(goodsListVo.getGoodsName());
		indetailModel.setBarcode(goodsListVo.getBarcode());
		indetailModel.setOrderNo(invetAddOrSubtractRootJson.getOrderNo());
		indetailModel.setBusinessNo(invetAddOrSubtractRootJson.getBusinessNo());
		indetailModel.setSource(invetAddOrSubtractRootJson.getSource());
		indetailModel.setSourceType(invetAddOrSubtractRootJson.getSourceType());
		if (StringUtils.isNotBlank(invetAddOrSubtractRootJson.getBuId())) {
			indetailModel.setBuId(Long.valueOf(invetAddOrSubtractRootJson.getBuId()));
		}
		// 电商订单或者电商退货订单 取表体的事业部
		if (invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_008)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0026)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0012)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0013)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0014)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0016)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0017)
				||invetAddOrSubtractRootJson.getSourceType().equals(DERP_INVENTORY.INVENTORY_SOURCETYPE_0019)) {			
			indetailModel.setBuId(Long.valueOf(goodsListVo.getBuId()));
			indetailModel.setBuName(goodsListVo.getBuName());
			
		}else {
			if (StringUtils.isNotBlank(invetAddOrSubtractRootJson.getBuId())) {
				indetailModel.setBuId(Long.valueOf(invetAddOrSubtractRootJson.getBuId()));
			}
			indetailModel.setBuName(invetAddOrSubtractRootJson.getBuName());
		}
		
		
		// 调减  设置效期
		if(DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())){//先失效先出(扣减)
			indetailModel.setBatchNo(inBatchModel.getBatchNo());			
			indetailModel.setProductionDate(inBatchModel.getProductionDate());
			indetailModel.setOverdueDate(inBatchModel.getOverdueDate());
			//如何是采购入库红冲，销售出库红冲，或者上架入库红冲 操作类型转化数量乘以-1
			if (DERP.ISWRITEOFF1.equals(invetAddOrSubtractRootJson.getIsWriteOff())) {
				indetailModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
				indetailModel.setNum(lessNum*-1);
			}else {
				indetailModel.setOperateType(goodsListVo.getOperateType());
				indetailModel.setNum(lessNum);
			}
			
		}else{
			indetailModel.setBatchNo(goodsListVo.getBatchNo());			
			if(StringUtils.isNotBlank(goodsListVo.getProductionDate())){
				indetailModel.setProductionDate(TimeUtils.strToSqlDate(goodsListVo.getProductionDate()));
			}
			if(StringUtils.isNotBlank(goodsListVo.getOverdueDate())){
				indetailModel.setOverdueDate(TimeUtils.strToSqlDate(goodsListVo.getOverdueDate()));
			}
			////如何是采购入库红冲，销售出库红冲，或者上架入库红冲 操作类型转化 数量乘以-1
			if (DERP.ISWRITEOFF1.equals(invetAddOrSubtractRootJson.getIsWriteOff())) {//转化后的操作类型和数量
				indetailModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
				indetailModel.setNum(goodsListVo.getNum()*-1);
			}else {//原来的操作类型  和数量
				indetailModel.setOperateType(goodsListVo.getOperateType());
				indetailModel.setNum(goodsListVo.getNum());
			}
		}
		
		
		//单据状态
		if(DERP_INVENTORY.INVENTORY_SOURCETYPE_0013.equals(invetAddOrSubtractRootJson.getSourceType())){//销毁
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0010);
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_001.equals(invetAddOrSubtractRootJson.getSourceType())) {// 采购入库
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_001);
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_003.equals(invetAddOrSubtractRootJson.getSourceType())) {// 销售出库
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_002);
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_005.equals(invetAddOrSubtractRootJson.getSourceType())) {// 销售退货入库
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_003);
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_006.equals(invetAddOrSubtractRootJson.getSourceType())) {// 销售退货出库
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_004);
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_008.equals(invetAddOrSubtractRootJson.getSourceType())) {// 电商订单出库
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_005);
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0010.equals(invetAddOrSubtractRootJson.getSourceType())) {// 调拨入库
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_006);
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0011.equals(invetAddOrSubtractRootJson.getSourceType())) {// 调拨出库
			indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_007);
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0015.equals(invetAddOrSubtractRootJson.getSourceType())) {// 盘点结果单
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_009);
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_008);
			}
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0014.equals(invetAddOrSubtractRootJson.getSourceType())) {// 其他入其他出
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0011);
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0012);
			}

		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0012.equals(invetAddOrSubtractRootJson.getSourceType())) {// 月结损益

			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0014);
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0013);
			}
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0016.equals(invetAddOrSubtractRootJson.getSourceType())) {// 好坏品互转
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0016);
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0015);
			}
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0018.equals(invetAddOrSubtractRootJson.getSourceType())) {// 效期调整
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0018);
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0017);
			}
		} else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0017.equals(invetAddOrSubtractRootJson.getSourceType())) {// 货号变更
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0020);
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0019);
			}
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0019.equals(invetAddOrSubtractRootJson.getSourceType())) {//大货理货
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0021);
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0022);
			}
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0020.equals(invetAddOrSubtractRootJson.getSourceType())) {// 唯品红
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0023);// 唯品红冲入
			}			
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0021.equals(invetAddOrSubtractRootJson.getSourceType())) {
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0025);// 采购执行出
			} else {// 增
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0024);// 采购执行入
			}
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0022.equals(invetAddOrSubtractRootJson.getSourceType())) {// 国检抽样
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0026);// 国检抽样
			} 		
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0024.equals(invetAddOrSubtractRootJson.getSourceType())) {// 唯品盘点
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0030);// 唯品盘点出
			}else {
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0029);// 唯品盘点入
			}	
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0025.equals(invetAddOrSubtractRootJson.getSourceType())) {// 唯品报废
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0031);// 唯品报废出
			} 		
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0026.equals(invetAddOrSubtractRootJson.getSourceType())) {// 电商订单退货
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减				
			}else {
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0032);//  电商订单退货
			} 		
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0027.equals(invetAddOrSubtractRootJson.getSourceType())) {// 客退
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减				
			}else {
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0033);// 客退入库
			} 		
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0028.equals(invetAddOrSubtractRootJson.getSourceType())) {// 账单销售
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0034);// 账单销售出库
			}else {				
			} 		
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0029.equals(invetAddOrSubtractRootJson.getSourceType())) {// 上架
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减	
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0035);// 上架入库
			}else {
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0035);// 上架入库
			} 		
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0030.equals(invetAddOrSubtractRootJson.getSourceType())) {// 上架
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减	
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0036);// 采购退货出库
			}		
		}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0033.equals(invetAddOrSubtractRootJson.getSourceType())) {// 上架
			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goodsListVo.getOperateType())) {// 减	
				indetailModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0041);//领料单出库
			}		
		}
		
		indetailModel.setThingName(DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_thingStatusList, indetailModel.getThingStatus()));
		/*indetailModel.setThingName(
				InventoryThingStatusEnum.getInventoryThingStatusEnumValue(indetailModel.getThingStatus()));*/
		indetailModel.setSourceDate(TimeUtils.parseFullTime(invetAddOrSubtractRootJson.getSourceDate()));
		indetailModel.setCreateDate(TimeUtils.getNow());
		indetailModel.setUnit(goodsListVo.getUnit());
		indetailModel.setType(goodsListVo.getType());
		indetailModel.setIsExpire(goodsListVo.getIsExpire());
		//归属月份
		indetailModel.setOwnMonth(invetAddOrSubtractRootJson.getOwnMonth());
		//出入时间
		indetailModel.setDivergenceDate(TimeUtils.parseFullTime(invetAddOrSubtractRootJson.getDivergenceDate()));
		indetailModel.setGoodsNo(goodsListVo.getGoodsNo());
		indetailModel.setInventoryBatchId(inBatchModel.getId());// 保存批次库存id
		indetailModel.setCommbarcode(goodsListVo.getCommbarcode());
		if (StringUtils.isNotBlank(goodsListVo.getStockLocationTypeId())) indetailModel.setStockLocationTypeId(Long.valueOf(goodsListVo.getStockLocationTypeId()));
        if (StringUtils.isNotBlank(goodsListVo.getStockLocationTypeName())) indetailModel.setStockLocationTypeName(goodsListVo.getStockLocationTypeName());
		/*
		 * if (StringUtils.isNotBlank(goodsListVo.getLocationGoodsId())) {
		 * indetailModel.setLocationGoodsId(Long.valueOf(goodsListVo.getLocationGoodsId(
		 * ))); } if (StringUtils.isNotBlank(goodsListVo.getLocationGoodsNo())) {
		 * indetailModel.setLocationGoodsNo(goodsListVo.getLocationGoodsNo()); }
		 */
		
		Long id = inventoryDetailsDao.save(indetailModel);
		if (id == null) {
			LOGGER.error("新增商品收发明细失败");
			throw new RuntimeException("新增商品收发明细失败");
		}
	}
	/**合并扣减批次
	 * batchList2 合并到batchList1上
	 * */
	public List<InventoryBatchModel> mergeBatch(List<InventoryBatchModel> batchList1,
			List<InventoryBatchModel> batchList2){
		
		if(batchList2==null||batchList2.size()<=0){
			return batchList1;
		}
		if(batchList1==null||batchList1.size()<=0){
			batchList1 = batchList2;
			return batchList1;
		}
		//合并批次
		List<InventoryBatchModel> batchTempList = new ArrayList<InventoryBatchModel>();
		for(InventoryBatchModel batch2:batchList2){
			boolean exit = false;//是否存在
			for(InventoryBatchModel batch:batchList1){
				if(batch.getId().longValue()==batch2.getId().longValue()){
					exit=true;//存在
					break;
				}
			}
			if(exit==false) batchTempList.add(batch2);
		}
		batchList1.addAll(batchTempList);
		return batchList1;
	}
}









