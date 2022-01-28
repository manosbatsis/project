package com.topideal.storage.service.adjustmenttype.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentInventoryDao;
import com.topideal.dao.AdjustmentInventoryItemDao;
import com.topideal.entity.dto.AdjustmentInventoryDTO;
import com.topideal.entity.dto.AdjustmentInventoryItemDTO;
import com.topideal.entity.dto.AdjustmentInventoryVo;
import com.topideal.entity.dto.ImportErrorMessage;
import com.topideal.entity.vo.AdjustmentInventoryItemModel;
import com.topideal.entity.vo.AdjustmentInventoryModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.storage.service.adjustmenttype.AdjustmentInventoryService;

import net.sf.json.JSONObject;

/**
 * 库存调整
 * 
 * @author zhanghx
 */
@Service
public class AdjustmentInventoryServiceImpl implements AdjustmentInventoryService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(AdjustmentInventoryServiceImpl.class);

	// 库存调整
	@Autowired
	private AdjustmentInventoryDao adjustmentInventoryDao;
	// 商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 库存调整表体
	@Autowired
	private AdjustmentInventoryItemDao adjustmentInventoryItemDao;
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	/**
	 * 分页
	 * 
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AdjustmentInventoryDTO listByPage(AdjustmentInventoryDTO dto) throws SQLException {
		return adjustmentInventoryDao.searchByDTOPage(dto);
	}

	/**
	 * 导入单据入库确认
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> auditAdjustment(List<Long> ids, User user) throws Exception {
		Map<String, Object> result = new HashMap<>();
		StringBuffer failureMsg = new StringBuffer();
		Integer failure = 0;
		for (Long id : ids) {
			AdjustmentInventoryModel model = adjustmentInventoryDao.searchById(id);

			if(!model.getStatus().equals(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020)){
				failureMsg.append("库存调整单单号:" + model.getCode() +"不是“待调整”状态!\n");
				failure++;
				continue;
			}

			if (!model.getSource().equals(DERP_STORAGE.ADJUSTMENTINVENTORY_SOURCE_02)) {
				failureMsg.append("库存调整单单号:" + model.getCode() +"不是“手工录入”!\n");
				failure++;
				continue;
			}

			// 根据仓库id获取仓库信息
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", model.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);

			//获取商品集合
			AdjustmentInventoryItemModel itemModel = new AdjustmentInventoryItemModel();
			itemModel.setTAdjustmentInventoryId(model.getId());//表头id
			List<AdjustmentInventoryItemModel> resultList = adjustmentInventoryItemDao.list(itemModel);

			if(resultList!=null && resultList.size()>0){
				boolean flag = true;
				for (AdjustmentInventoryItemModel item : resultList) {
					//查询事业部
					Map<String, Object> buMap = new HashMap<>();
					buMap.put("buId", item.getBuId());
					buMap.put("merchantId", user.getMerchantId());
					buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
					MerchantBuRelMongo buRelMongo = merchantBuRelMongoDao.findOne(buMap);
					if (buRelMongo == null) {
						failureMsg.append("库存调整单单号:" + model.getCode() +"事业部:" + item.getBuName() + "不存在\n");
						failure++;
						flag = false; //不继续校验推送库存扣减
						break;
					}
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(model.getMerchantId());
					closeAccountsMongo.setDepotId(model.getDepotId());
					closeAccountsMongo.setBuId(item.getBuId());
					String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxdate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 入库日期
						if (model.getSourceTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							failureMsg.append("库存调整单单号:" + model.getCode() +"确认失败，归属日期必须大于关账日期/月结日期\n");
							flag = false; //不继续校验推送库存扣减
							break;
						}
					}

					if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) ||
							(DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())
									&& DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(item.getType()))) {
						if (StringUtils.isBlank(item.getOldBatchNo())) {
							failureMsg.append("库存调整单单号:" + model.getCode() +"批次号不能为空");
							flag = false; //不继续校验推送库存扣减
							break;
						}

						if (item.getProductionDate() == null) {
							failureMsg.append("库存调整单单号:" + model.getCode() +"生产日期不能为空");
							flag = false; //不继续校验推送库存扣减
							break;
						}

						if (item.getOverdueDate() == null) {
							failureMsg.append("库存调整单单号:" + model.getCode() +"失效日期不能为空");
							flag = false; //不继续校验推送库存扣减
							break;
						}
					}
				}
				if (flag) {
					pushMQ(model, user.getTopidealCode(), resultList);
					/*修改状态*/
					model.setAdjustmentTime(TimeUtils.getNow());
					model.setStatus(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_022);// 处理中
					model.setConfirmUserId(user.getId());
					model.setConfirmUsername(user.getName());
					adjustmentInventoryDao.modify(model);
				}
			}
		}
		result.put("failure", failure);
		result.put("success", ids.size()-failure);
		result.put("failureMsg", failureMsg);
		return result;
	}

	/**
	 * 库存调整单推送mq
	 * @param model
	 * @param topidealCode
	 * @param list
	 * @param
	 */
	private void pushMQ(AdjustmentInventoryModel model, String topidealCode, List<AdjustmentInventoryItemModel> list) throws Exception{
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", model.getDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		
		String sourceType = "";
		if(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_1.equals(model.getModel())){
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0013;
		}else if(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2.equals(model.getModel())){
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0012;
		}else if(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_3.equals(model.getModel())){
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0014;
		}

		//加减库存
		InvetAddOrSubtractRootJson addSubRoot = new InvetAddOrSubtractRootJson();
		addSubRoot.setMerchantId(model.getMerchantId().toString());
		addSubRoot.setMerchantName(model.getMerchantName());
		addSubRoot.setTopidealCode(topidealCode);
		addSubRoot.setDepotId(model.getDepotId().toString());
		addSubRoot.setDepotName(model.getDepotName());
		addSubRoot.setDepotCode(mongo.getCode());
		addSubRoot.setDepotType(mongo.getType());
		addSubRoot.setIsTopBooks(mongo.getIsTopBooks());
		addSubRoot.setOrderNo(model.getCode());
		addSubRoot.setSource(SourceStatusEnum.KCTZD.getKey());//库存调整单
		addSubRoot.setSourceType(sourceType);//调整入
		addSubRoot.setSourceDate(TimeUtils.formatFullTime());//
		addSubRoot.setBusinessNo(model.getSourceCode());
		// 获取当前年月
		String ownMonth = TimeUtils.formatMonth(new Date());
		String divergenceDate = TimeUtils.formatFullTime();
		if(StringUtils.isNotBlank(model.getMonths())&&model.getSourceTime()!=null){
			ownMonth = model.getMonths();
			divergenceDate = TimeUtils.formatFullTime(model.getSourceTime());
		}
		addSubRoot.setOwnMonth(ownMonth);
		addSubRoot.setDivergenceDate(divergenceDate);
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		//加减库存商品列表
		List<InvetAddOrSubtractGoodsListJson> goodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for(AdjustmentInventoryItemModel item : list) {

			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(item.getOverdueDate()!=null){				
				isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}
			
			String type = item.getType();// 0 调减 1调增
			//加减库存
			InvetAddOrSubtractGoodsListJson addsubGoods = new InvetAddOrSubtractGoodsListJson();
			addsubGoods.setGoodsId(item.getGoodsId().toString());
			addsubGoods.setGoodsName(item.getGoodsName());
			addsubGoods.setGoodsNo(item.getGoodsNo());
			addsubGoods.setBarcode(item.getBarcode());

			addsubGoods.setBatchNo( item.getOldBatchNo());
			addsubGoods.setIsExpire(isExpire);
			if(item.getProductionDate()!=null&&item.getOverdueDate()!=null){
				addsubGoods.setProductionDate(sdf3.format(item.getProductionDate()));
				addsubGoods.setOverdueDate(sdf3.format(item.getOverdueDate()));
			}
			if(DERP.ISDAMAGE_1.equals(item.getIsDamage())){
				addsubGoods.setType(DERP.ISDAMAGE_1);//坏品
			}else{
				addsubGoods.setType(DERP.ISDAMAGE_0);//好品
			}
			addsubGoods.setNum(item.getAdjustTotal());
			// 海外仓必填
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				if (DERP.ORDER_TALLYINGUNIT_00.equals(item.getTallyingUnit())) {
					addsubGoods.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
				}else if(DERP.ORDER_TALLYINGUNIT_01.equals(item.getTallyingUnit())){
					addsubGoods.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
				}else if(DERP.ORDER_TALLYINGUNIT_02.equals(item.getTallyingUnit())){
					addsubGoods.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
				}
				
			}
			addsubGoods.setOperateType(type);// 字符串 0 调减 1调增
			addsubGoods.setBuName(item.getBuName());
			addsubGoods.setBuId(String.valueOf(item.getBuId()));
			addsubGoods.setStockLocationTypeId(String.valueOf(item.getStockLocationTypeId()));
			addsubGoods.setStockLocationTypeName(item.getStockLocationTypeName());
			goodsList.add(addsubGoods);
		}
		
		addSubRoot.setGoodsList(goodsList);
		Map<String, Object> customParam=new HashMap<String, Object>();
		addSubRoot.setBackTags(MQPushBackStorageEnum.STORAGE_ADJUSTMENT_INVENTORY_AUDIT_PUSH_BACK.getTags());//回调标签
		addSubRoot.setBackTopic(MQPushBackStorageEnum.STORAGE_ADJUSTMENT_INVENTORY_AUDIT_PUSH_BACK.getTopic());//回调主题
		customParam.put("code", model.getCode());// 电商订单内部单号
		addSubRoot.setCustomParam(customParam);////自定义回调参数
		JSONObject jsonObject = JSONObject.fromObject(addSubRoot);
		LOGGER.info("----------------------" + model.getCode() + "库存调整单审核json:"+jsonObject);
		rocketMQProducer.send(jsonObject.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

	}

	/**
	 * 批量删除
	 * 
	 * @param list
	 * @return
	 */
	@Override
	public boolean deleteByIds(List<Long> list) throws Exception {
		int flag = 0;
		for (Long id : list) {
			AdjustmentInventoryModel model = adjustmentInventoryDao.searchById(id);
			// 仅对单据来源为“手工导入”且单据状态为“待调整”的调整单可进行操作删除，支持批量删除。
			if (!DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020.equals(model.getStatus()) ||
				  !DERP_STORAGE.ADJUSTMENTINVENTORY_SOURCE_02.equals(model.getSource())) {
				flag = 1;
				break;
			}
		}
		if (flag == 1) {
			throw new Exception("删除失败，单据来源必须为手工导入并且状态必须为待调整");
		}
		for (Long id : list) {
			AdjustmentInventoryModel model = adjustmentInventoryDao.searchById(id);
			if(model.getModel().equals(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2)){//月结损益
				//获取商品集合
				AdjustmentInventoryItemModel itemModel = new AdjustmentInventoryItemModel();
				itemModel.setTAdjustmentInventoryId(id);//表头id
				List<AdjustmentInventoryItemModel> resultList = adjustmentInventoryItemDao.list(itemModel);
				pushFreeze(model, resultList);
			}
			
			AdjustmentInventoryModel upmodel = new AdjustmentInventoryModel();
			upmodel.setId(id);
			upmodel.setStatus(DERP.DEL_CODE_006);// 已删除
			adjustmentInventoryDao.modify(upmodel);
		}
		return true;
	}
	
	/*
	 * 推送mq释放冻结**/
	public void pushFreeze(AdjustmentInventoryModel model,List<AdjustmentInventoryItemModel> resultList) throws Exception{
		
		if(resultList==null&&resultList.size()<=0){
			return ;
		}
		
		//释放冻结商品列表
		List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();
		for(AdjustmentInventoryItemModel item:resultList){
			//判断是否月结损益 是则需要释放库存
			if(item.getType().equals(DERP_STORAGE.ADJUSTMENT_TYPE_0)){//调减、月结损益
					//释放冻结商品
					InventoryFreezeGoodsListJson fgood = new InventoryFreezeGoodsListJson();
					fgood.setGoodsId(String.valueOf(item.getGoodsId()));
					fgood.setGoodsName(item.getGoodsName());
					fgood.setGoodsNo(item.getGoodsNo());
					fgood.setDivergenceDate(TimeUtils.formatFullTime());
					fgood.setNum(item.getAdjustTotal());
					//fgood.setUnit("2");//	单位	字符串 0 托盘 1箱  2 件
					freezeGoodList.add(fgood);
			}
		}
		
		if(freezeGoodList!=null&&freezeGoodList.size()>0){//调减 、月结损益
			//释放冻结库存
			InventoryFreezeRootJson freezeAddLower = new InventoryFreezeRootJson();
			freezeAddLower.setMerchantId(model.getMerchantId().toString());
			freezeAddLower.setMerchantName(model.getMerchantName());
			freezeAddLower.setDepotId(model.getDepotId().toString());
			freezeAddLower.setDepotName(model.getDepotName());
			freezeAddLower.setOrderNo(model.getCode());
			freezeAddLower.setSource(SourceStatusEnum.KCTZD.getKey());
			freezeAddLower.setSourceType(InventoryStatusEnum.YJSY.getKey());
			freezeAddLower.setSourceDate(TimeUtils.formatFullTime());
			freezeAddLower.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
			freezeAddLower.setGoodsList(freezeGoodList);
			freezeAddLower.setBusinessNo(model.getCode());
			JSONObject jsonFree = JSONObject.fromObject(freezeAddLower);
			rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		}
		
	}

	/**
	 * 导入
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map 	importAdjustment(List<List<Map<String, String>>> data, User user) throws Exception {		
		//以仓库ID+归属月份为key value存储导入的数据
		Map<String, List<AdjustmentInventoryVo>> dataMap = new HashMap<String, List<AdjustmentInventoryVo>>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		// 用于同一仓库 商品 货品 批次 生产日期 失效日期  相同的要合并导入（相同的业务类别（其他出入，销毁）情况下）
		Map<String, Object> accessKeyMap= new HashMap<>();
		Map<String, Object> destroyKeyMap= new HashMap<>();
		// 根据代销仓审核逻辑 如果是 坏品或者过期品 调减 导入 如果传批次 此单批次必须都填,如果效期都填 此单效期必须都填

		//表头：批量导入序号、业务类别、仓库自编码、归属日期、事业部
		//表体：对应表头序号、商品货号、商品名称、调整类型、商品类型、调整数量、批次号、生产日期、失效日期、PO单号、 PO单时间、海外仓理货单位、备注

		Map<String, AdjustmentInventoryVo> headDepotMap = new HashMap<>();

		//判断表头的序号是否能在商品信息中找到对应的信息
		Map<String, Integer> serialNumMap = new HashMap<>();

		List<ImportErrorMessage> errorList = new ArrayList<ImportErrorMessage>();
		/**************************参数检查start*************************/
		// 表头必填字段的校验
		for (int i = 0; i < data.get(0).size(); i++) {
			Map<String, String> map = data.get(0).get(i);
			try {
				String serialNum = map.get("批量导入序号").trim();
				if (StringUtils.isEmpty(serialNum) && !StringUtils.isNumeric(serialNum)) {
					setErrorMsg(i+1, "批量导入序号不能为空且为整数", errorList);
					failure += 1;
					continue;
				}

				if (serialNumMap.containsKey(serialNum)) {
					setErrorMsg(i+1, "批量导入序号不能重复", errorList);
					failure += 1;
					continue;
				}

				String serviceType = map.get("业务类别").trim();
				if (StringUtils.isEmpty(serviceType)) {
					setErrorMsg(i+1, "业务类别不能为空", errorList);
					failure += 1;
					continue;
				}
				if (!("销毁（非自营仓）".equals(serviceType))) {
					setErrorMsg(i+1, "业务类别只能为“销毁（非自营仓）”", errorList);
					failure += 1;
					continue;
				}

				String depotCode = map.get("仓库自编码").trim();
				if (StringUtils.isEmpty(depotCode)) {
					setErrorMsg(i+1, "仓库自编码不能为空", errorList);
					failure += 1;
					continue;
				}
				// 根据仓库id获取仓库信息
				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotCode", depotCode);
				DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);
				if (depot == null) {
					setErrorMsg(i+1, "仓库编码不存在", errorList);
					failure += 1;
					continue;
				}

				String sourceTime = map.get("归属日期").trim();
				if (StringUtils.isEmpty(sourceTime)) {
					setErrorMsg(i+1, "归属日期不能为空", errorList);
					failure += 1;
					continue;
				}
				
				if(TimeUtils.daysBetween(TimeUtils.parse(sourceTime, "yyyy-MM-dd"), new Date()) < 0) {
					setErrorMsg(i+1, "归属日期不可超过当前时间", errorList);
					failure += 1;
					continue;
				}

				AdjustmentInventoryVo adjustmentInventoryVo = new AdjustmentInventoryVo();
				adjustmentInventoryVo.setSerialNum(serialNum);
				adjustmentInventoryVo.setDepotInfoMongo(depot);
				adjustmentInventoryVo.setDepotCode(depotCode);
				adjustmentInventoryVo.setServiceType(serviceType);
				adjustmentInventoryVo.setSourceTime(sourceTime);
				headDepotMap.put(serialNum, adjustmentInventoryVo);
				serialNumMap.put(serialNum, i+1);
			} catch (Exception e) {
				e.printStackTrace();
                setErrorMsg(i+1, e.getMessage(), errorList);
				failure += 1;
				continue;
			}
		}

		if (failure == 0) {
			// 商品信息必填字段的校验：对应表头序号、商品货号、调整类型、商品类型、调整数量、批次号、生产日期、失效日期、海外仓理货单位
			for (int j = 0; j < data.get(1).size(); j++) {
				Map<String, String> map = data.get(1).get(j);
				try {
					String serialNum = map.get("对应表头序号").trim();
					if (StringUtils.isEmpty(serialNum) && !StringUtils.isNumeric(serialNum)) {
						setErrorMsg(j+1, "对应表头序号不能为空且为整数", errorList);
						failure += 1;
						continue;
					}
					if (!headDepotMap.containsKey(serialNum)) {
						setErrorMsg(j+1, "对应表头序号在表头不存在", errorList);
						failure += 1;
						continue;
					}
					if (serialNumMap.containsKey(serialNum)) {
						serialNumMap.remove(serialNum);
					}

					String goodsNo = map.get("商品货号").trim();
					if (StringUtils.isEmpty(goodsNo)) {
						setErrorMsg(j+1, "商品货号不能为空", errorList);
						failure += 1;
						continue;
					}
					String type = map.get("调整类型").trim();
					if (StringUtils.isEmpty(type)) {
						setErrorMsg(j+1, "调整类型不能为空", errorList);
						failure += 1;
						continue;
					}
					String stockType = map.get("商品类型").trim();//0-好品 1-坏品
					if (StringUtils.isEmpty(stockType)) {
						setErrorMsg(j+1, "商品类型不能为空", errorList);
						failure += 1;
						continue;
					}
					String num = map.get("调整数量").trim();
					if (StringUtils.isEmpty(num)) {
						setErrorMsg(j+1, "调整数量不能为空", errorList);
						failure += 1;
						continue;
					}

					String batchNo = map.get("批次号");
					String productionDate = map.get("生产日期");
					String overdueDate = map.get("失效日期");

					/**
					 * 1.若仓库类型为批次强校验，则批次号、生产日期、失效日期必填；
					 * 2.类型为调增且仓库若为批次强校验，或入库强校验时，批次号、生产日期、失效日期必填；
					 * 3.若仓库类型是海外仓，理货单位必填,并且填的是理货单位编码
					 */
					DepotInfoMongo depot = headDepotMap.get(serialNum).getDepotInfoMongo();
					// 如果仓库设置了批次效期强校验 批次效期必填
					if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())||checkDjustmentInventory(type,depot.getBatchValidation())){
						if (StringUtils.isEmpty(batchNo)) {
							setErrorMsg(j+1, "批次号不能为空", errorList);
							failure += 1;
							continue;
						}
						if (StringUtils.isEmpty(productionDate)) {
							setErrorMsg(j+1, "生产日期不能为空", errorList);
							failure += 1;
							continue;
						}

						if(StringUtils.isEmpty(overdueDate)) {
							setErrorMsg(j+1, "失效日期不能为空", errorList);
							failure += 1;
							continue;
						}

					}
					//判断生产日期、失效日期格式是否正确
					if (StringUtils.isNotBlank(productionDate)) {
						productionDate = productionDate.trim();
						String msg = this.isDateContent(productionDate, "生产日期");
						productionDate+= " 00:00:00";
						if (StringUtils.isNotBlank(msg)) {
							setErrorMsg(j+1, msg, errorList);
							failure += 1;
							continue;
						}
					}
					if (StringUtils.isNotBlank(overdueDate)) {
						overdueDate = overdueDate.trim();
						String msg = this.isDateContent(overdueDate, "失效日期");
						overdueDate+= " 00:00:00";
						if (StringUtils.isNotBlank(msg)) {
							setErrorMsg(j+1, msg, errorList);
							failure += 1;
							continue;
						}
					}

					String buCode = map.get("事业部").trim();
					if (StringUtils.isEmpty(buCode)) {
						setErrorMsg(j+1, "事业部不能为空", errorList);
						failure += 1;
						continue;
					}
					Map<String, Object> buMap = new HashMap<>();
					buMap.put("merchantId", user.getMerchantId());
					buMap.put("buCode", buCode);
					buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
					MerchantBuRelMongo buRelMongoDaoOne = merchantBuRelMongoDao.findOne(buMap);
					if (buRelMongoDaoOne == null) {
						setErrorMsg(j+1, "事业部在公司下不存在", errorList);
						failure += 1;
						continue;
					}
					Map<String, Object> depotBuMap = new HashMap<>();
					depotBuMap.put("merchantId", user.getMerchantId());
					depotBuMap.put("depotId", depot.getDepotId());
					depotBuMap.put("buId", buRelMongoDaoOne.getBuId());
					MerchantDepotBuRelMongo merchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(depotBuMap);
					if (merchantDepotBuRelMongo == null) {
						setErrorMsg(j+1, "事业部在公司仓库下不存在", errorList);
						failure += 1;
						continue;
					}

					boolean isUserRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), buRelMongoDaoOne.getBuId());
					if (!isUserRelateBu) {
						setErrorMsg(j+1, "该事业部没有关联用户", errorList);
						failure += 1;
						continue;
					}

					String stockLocationType = map.get("库位类型");
					if (buRelMongoDaoOne.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0) &&
							StringUtils.isEmpty(stockLocationType)) {
						setErrorMsg(j+1, "公司事业部启用库位管理，库位类型不能为空", errorList);
						failure += 1;
						continue;
					}

					BuStockLocationTypeConfigMongo buStockLocationTypeConfigMongo = null;
					if (buRelMongoDaoOne.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0)) {
						stockLocationType = stockLocationType.trim();
						Map<String, Object> buStockLocationTypeParams = new HashMap<>();
						buStockLocationTypeParams.put("merchantId", user.getMerchantId());
						buStockLocationTypeParams.put("buId", buRelMongoDaoOne.getBuId());
						buStockLocationTypeParams.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
						buStockLocationTypeParams.put("name", stockLocationType);
						buStockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(buStockLocationTypeParams);

						if (buStockLocationTypeConfigMongo == null) {
							setErrorMsg(j+1, "库位类型在公司事业部下不存在", errorList);
							failure += 1;
							continue;
						}
					}


					AdjustmentInventoryVo vo = headDepotMap.get(serialNum);
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(user.getMerchantId());
					closeAccountsMongo.setDepotId(depot.getDepotId());
					closeAccountsMongo.setBuId(buRelMongoDaoOne.getBuId());
					String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxDate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 入库日期
						if (Timestamp.valueOf(vo.getSourceTime()+" 00:00:00").getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							setErrorMsg(j+1, "归属日期必须大于关账日期", errorList);
							failure += 1;
							continue;
						}
					}
					//海外仓 必填项
					String tallyingUnit = map.get("海外仓理货单位").trim();
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
						if (StringUtils.isEmpty(tallyingUnit)) {
							setErrorMsg(j+1, "海外仓理货单位不能为空", errorList);
							failure += 1;
							continue;
						}

						if (!(DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)
								||DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)
								||DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit))) {
							setErrorMsg(j+1, "海外仓理货单位编码不正确", errorList);
							failure += 1;
							continue;
						}
					}

					Map<String, Object> relDepotParam = new HashMap<>();
					relDepotParam.put("merchantId", user.getMerchantId());
					relDepotParam.put("depotId", depot.getDepotId());
					DepotMerchantRelMongo merchantRelMongo = depotMerchantRelMongoDao.findOne(relDepotParam);
					Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
					merchandiseInfo_params.put("goodsNo", goodsNo);
					merchandiseInfo_params.put("merchantId", user.getMerchantId());
					merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
					MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
					if (merchandiseInfo == null) {
						setErrorMsg(j+1, "商品货号不存在", errorList);
						failure += 1;
						continue;
					}
					//当仓库在该商家下的“选品限制”为“仅备案商品”时，可选的商品为该商家下仅为备案商品；
					if (merchantRelMongo != null && DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(merchantRelMongo.getProductRestriction())) {
						if (!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(merchandiseInfo.getIsRecord())) {
							setErrorMsg(j+1, "该商品不是备案商品", errorList);
							failure += 1;
							continue;
						}
					}
					//当仓库 在该商家下的“选品限制”为“仅外仓统一码”时，可选的商品为该商家下仅为标识为外仓统一码的商品货号
					else if (merchantRelMongo != null && DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(merchantRelMongo.getProductRestriction())) {
						if (!DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0.equals(merchandiseInfo.getOutDepotFlag())) {
							setErrorMsg(j+1, "该商品不是“仅外仓统一码”商品", errorList);
							failure += 1;
							continue;
						}
					}



					if (!type.matches(DERP_STORAGE.ADJUSTMENT_TYPE_0 + "|" + DERP_STORAGE.ADJUSTMENT_TYPE_1)) {
						setErrorMsg(j+1, "调整类型只能是0或1", errorList);
						failure += 1;
						continue;
					} else if ("销毁（非自营仓）".equals(vo.getServiceType()) && DERP_STORAGE.ADJUSTMENT_TYPE_1.equals(type)) {
						setErrorMsg(j+1, "业务类别为销毁（非自营仓）时，调整类型只能是0", errorList);
						failure += 1;
						continue;
					}

					if (!stockType.matches(DERP_STORAGE.ADJUSTMENT_TYPE_0 + "|" + DERP_STORAGE.ADJUSTMENT_TYPE_1)) {
						setErrorMsg(j+1, "商品类型只能是0或1", errorList);
						failure += 1;
						continue;
					}
					if (!isNumeric(num)) {
						setErrorMsg(j+1, "调整数量必须为整数", errorList);
						failure += 1;
						continue;
					}

					//  拼接对应表头序号+仓库+货号+商品类型+批次+生产日期+失效日期 如果 同一仓库 商品 货号 批次 生产日期 失效日期 都相同要合并导入
					// 此处是为了解决 又加又减 审核可用量不足而设置的
					if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
						tallyingUnit=null;
					}
					if ("销毁（非自营仓）".equals(vo.getServiceType())) {
						String key = serialNum + "_" + depot.getDepotId() + "_" + buRelMongoDaoOne.getBuCode() + "_" +
								vo.getSourceTime() + "_" + goodsNo + "_" + stockType + "_" + batchNo + "_" +
								productionDate + "_" + overdueDate + "_" + tallyingUnit + "_" + type ;
						if (accessKeyMap.containsKey(key)) {
							setErrorMsg(j+1,"对应表头序号:"+serialNum+ "仓库自编码:"+depot.getDepotCode() + ",事业部编码:" + buRelMongoDaoOne.getBuCode()
									+",归属日期:"+vo.getSourceTime() +",商品类型:"+stockType + ",调整类型:" + type
									+",货号:"+goodsNo+",批次:"+batchNo+",生产日期:"+productionDate+",失效日期:"+overdueDate+"有多条数据,合并后导入", errorList);
							failure += 1;
							continue;
						}else {
							accessKeyMap.put(key, key);
							vo.setModel(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_1);
						}
					}

					String remark = map.get("备注");

					AdjustmentInventoryVo adjustmentInventoryVo = new AdjustmentInventoryVo();
					BeanUtils.copyProperties(vo, adjustmentInventoryVo);

					adjustmentInventoryVo.setDepotId(depot.getDepotId());
					adjustmentInventoryVo.setDepotName(depot.getName());
					adjustmentInventoryVo.setGoodsNo(goodsNo);
					adjustmentInventoryVo.setNum(Integer.parseInt(num));
					adjustmentInventoryVo.setType(type);
					adjustmentInventoryVo.setStockType(stockType);
					adjustmentInventoryVo.setBatchNo(batchNo);
					adjustmentInventoryVo.setProductionDate(productionDate);
					adjustmentInventoryVo.setOverdueDate(overdueDate);
					adjustmentInventoryVo.setGoodsCode(merchandiseInfo.getGoodsCode());
					adjustmentInventoryVo.setGoodsName(merchandiseInfo.getName());
					adjustmentInventoryVo.setGoodsId(merchandiseInfo.getMerchandiseId());
					adjustmentInventoryVo.setRemark(remark);
					adjustmentInventoryVo.setTallyingUnit(tallyingUnit);
					adjustmentInventoryVo.setBuName(buRelMongoDaoOne.getBuName());
					adjustmentInventoryVo.setBuId(buRelMongoDaoOne.getBuId());

					if (buStockLocationTypeConfigMongo != null) {
						adjustmentInventoryVo.setStockLocationTypeId(buStockLocationTypeConfigMongo.getBuStockLocationTypeId());
						adjustmentInventoryVo.setStockLocationTypeName(buStockLocationTypeConfigMongo.getName());
					}

					if(dataMap.get(serialNum+adjustmentInventoryVo.getDepotId()+adjustmentInventoryVo.getSourceTime()+vo.getModel())==null){
						List<AdjustmentInventoryVo> list = new ArrayList<AdjustmentInventoryVo>();
						list.add(adjustmentInventoryVo);
						dataMap.put(serialNum+adjustmentInventoryVo.getDepotId()+adjustmentInventoryVo.getSourceTime()+adjustmentInventoryVo.getModel(), list);
					}else{
						dataMap.get(serialNum+adjustmentInventoryVo.getDepotId()+adjustmentInventoryVo.getSourceTime()+adjustmentInventoryVo.getModel()).add(adjustmentInventoryVo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrorMsg(j+1, e.getMessage(), errorList);
					failure += 1;
					continue;
				}
			}
			if (serialNumMap.size() > 0) {
				for (Map.Entry<String, Integer> entry : serialNumMap.entrySet()) {
					setErrorMsg(entry.getValue(), "表头批量导入序号在商品信息中没有找到对应表头序号", errorList);
					failure += 1;
				}
			}
		}
		/**************************参数检查end*************************/
		
		if (failure == 0) {//start---001
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			for(List<AdjustmentInventoryVo> list: dataMap.values()) {//start---002
			    AdjustmentInventoryVo vo = list.get(0);
			    Long id = null;
				// 新增表头
//				String code = CodeGeneratorUtil.getNo(DERP.UNIQUEID_PREFIX_KCTZ);
				String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KCTZ);
				AdjustmentInventoryModel model = new AdjustmentInventoryModel();
				model.setCode(code);// 库存调整编码
				model.setStatus(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020);// 待调整
				model.setModel(vo.getModel());// 业务类别
				model.setMerchantId(user.getMerchantId());// 商家id
				model.setMerchantName(user.getMerchantName());// 商家名称
				model.setDepotId(vo.getDepotId());// 仓库id
				model.setDepotName(vo.getDepotName());// 仓库名称
				model.setCreateUserId(user.getId());// 用户id
				model.setCreateUsername(user.getName());// 用户名
				model.setSourceTime(TimeUtils.parse(vo.getSourceTime(), "yyyy-MM-dd")); //归属时间
				model.setMonths(TimeUtils.formatMonth(model.getSourceTime()));// 当前月
				model.setSource(DERP_STORAGE.ADJUSTMENTINVENTORY_SOURCE_02);
				StringBuffer allRemark = new StringBuffer();
				for(AdjustmentInventoryVo voItem:list){
					if(StringUtils.isNotBlank(voItem.getRemark())){
						allRemark.append("|"+voItem.getRemark());
					}
				}
				if(allRemark.length()>1) allRemark = allRemark.deleteCharAt(0);
				model.setRemark(allRemark.toString());
				try{
					id = adjustmentInventoryDao.save(model);
				} catch(Exception e){
					LOGGER.error("------------------"+code+"库存调整导入表头新增异常---------------------");
				}
				
				//循环列表生成表体
				for(AdjustmentInventoryVo voItem:list){//start----003
					Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
					merchandiseInfo_params.put("goodsNo", voItem.getGoodsNo());
					merchandiseInfo_params.put("merchantId", user.getMerchantId());
					merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
					MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
					AdjustmentInventoryItemModel item = new AdjustmentInventoryItemModel();
					item.setTAdjustmentInventoryId(id);// 表头id
					item.setGoodsId(voItem.getGoodsId());// 商品id
					item.setGoodsCode(voItem.getGoodsCode());// 商品编码
					item.setGoodsNo(voItem.getGoodsNo());// 商品货号
					item.setGoodsName(voItem.getGoodsName());// 商品名称
					item.setBarcode(merchandise.getBarcode()); //条码
					item.setCommbarcode(merchandise.getCommbarcode()); //标准条码
					item.setType(voItem.getType());// 调整类型 0 调减  1调增
					item.setIsDamage(voItem.getStockType());// 商品类型 0-好品 1-坏品
					item.setOldBatchNo(voItem.getBatchNo());// 原始批次号
					item.setTallyingUnit(voItem.getTallyingUnit());// 理货单位
					item.setPoNo(vo.getPoNo()); //po单号
					item.setBuId(voItem.getBuId());
					item.setBuName(voItem.getBuName());
					item.setStockLocationTypeId(voItem.getStockLocationTypeId());
					item.setStockLocationTypeName(voItem.getStockLocationTypeName());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try{
						if(StringUtils.isNotBlank(voItem.getProductionDate())){
							item.setProductionDate(sdf.parse(voItem.getProductionDate()));// 生产日期
						}
					} catch(Exception e){
						e.printStackTrace();
						LOGGER.error("------------------"+vo.getGoodsNo()+"库存调整导入商品生产日期格式不对---------------------");
					}
					try{
						if(StringUtils.isNotBlank(voItem.getOverdueDate())){
							item.setOverdueDate(sdf.parse(voItem.getOverdueDate()));// 失效日期
						}
					} catch(Exception e){
						e.printStackTrace();
						LOGGER.error("------------------"+vo.getGoodsNo()+"库存调整导入商品失效日期格式不对---------------------");
					}
					try{
						if(StringUtils.isNotBlank(voItem.getPoDate())){
							ts = Timestamp.valueOf(voItem.getPoDate());
							item.setPoDate(ts);// PO单时间
						}
					} catch(Exception e){
						e.printStackTrace();
						LOGGER.error("------------------"+vo.getGoodsNo()+"库存调整导入商品PO单时间格式不对---------------------");
					}
					item.setAdjustTotal(voItem.getNum());//调整数量
					try{
						adjustmentInventoryItemDao.save(item);
					} catch(Exception e){
						e.printStackTrace();
						LOGGER.error("------------------"+vo.getGoodsNo()+"库存调整导入商品新增异常---------------------");
					}
				}//end----003
				success ++;
			}//end---002
		}//end---001

		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", errorList);
		return map;
	}

	/**
	 * 对调整单，类型为调增且仓库若为批次效期强校验，或入库强校验时，批次号、生产日期、失效日期必填；
	 * @param type
	 * @param depotinfo
	 * @return
	 */
	private boolean checkDjustmentInventory(String type,String depotinfo){
		if("1".equals(type)){
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depotinfo)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 正则表达式判断是否为数值
	 */
	public boolean isNumeric(String str) {
		String regex = "[0-9]*";
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AdjustmentInventoryDTO getDetails(AdjustmentInventoryDTO dto) throws SQLException {
		return adjustmentInventoryDao.getDetails(dto);
	}

	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AdjustmentInventoryModel searchByModel(AdjustmentInventoryModel model) throws SQLException {
		return adjustmentInventoryDao.searchByModel(model);
	}

	@Override
	public List<Map<String, Object>> getItemByInventoryIds(List<Long> inventoryIds,User user) throws Exception {		 
		List<Map<String, Object>> itemList =adjustmentInventoryItemDao.getItemByInventoryIds(inventoryIds);
		for(Map<String, Object> itemMap:itemList){
			//获取仓库编码、仓库类型
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("depotId", itemMap.get("depot_id"));
			DepotInfoMongo depot = depotInfoMongoDao.findOne(map);
			itemMap.put("depot_code", depot.getDepotCode());
			itemMap.put("depot_type", depot.getType());//仓库类型
		}
		return itemList;
	}
	
	@Override
	public int modify(AdjustmentInventoryModel model) throws SQLException {
		return adjustmentInventoryDao.modify(model);
	}

	@Override
	public List<Map<String, Object>> getByInventoryIds(List<Long> inventoryIds) {
		return adjustmentInventoryItemDao.getByInventoryIds(inventoryIds);
	}

	@Override
	public Map<String, String> saveBuDetails(AdjustmentInventoryDTO dto, User user) throws Exception {
		Map<String, String> result = new HashMap<>();
		AdjustmentInventoryModel model = adjustmentInventoryDao.searchById(dto.getId());
		List<AdjustmentInventoryItemDTO> dtos = dto.getItemList();
		List<Long> buIds = new ArrayList<>();
		for (AdjustmentInventoryItemDTO itemDTO : dtos) {
			//校验事业部是否存在
			Map<String, Object> buMap = new HashMap<>();
			buMap.put("merchantId", user.getMerchantId());
			buMap.put("buId", itemDTO.getBuId());
			buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
			MerchantBuRelMongo buRelMongoDaoOne = merchantBuRelMongoDao.findOne(buMap);
			if (buRelMongoDaoOne == null) {
				throw new RuntimeException("事业部在公司下不存在");
			}
			Map<String, Object> depotBuMap = new HashMap<>();
			depotBuMap.put("merchantId", user.getMerchantId());
			depotBuMap.put("depotId", model.getDepotId());
			depotBuMap.put("buId", itemDTO.getBuId());
			MerchantDepotBuRelMongo merchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(depotBuMap);
			if (merchantDepotBuRelMongo == null) {
				throw new RuntimeException("事业部在公司仓库下不存在");
			}
			AdjustmentInventoryItemModel itemModel = new AdjustmentInventoryItemModel();
			itemModel.setId(itemDTO.getId());
			itemModel.setBuId(itemDTO.getBuId());
			itemModel.setBuName(buRelMongoDaoOne.getBuName());
			adjustmentInventoryItemDao.modify(itemModel);
			buIds.add(itemDTO.getBuId());
		}

		//判断库存调整单的所有表体信息是否都存在事业部，若是则推送库存加减接口并修改单据状态
		Long count = adjustmentInventoryItemDao.countNoExistBu(dto.getId());
		//推送库存加减接口并修改单据状态
		if (count == 0) {
			if (!model.getStatus().equals(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020)) {
				result.put("code", "01");
				result.put("message", "单据不是待调整状态，无法推送库存扣减");
				return result;
			}
			for (Long buId : buIds) {
				FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
				closeAccountsMongo.setMerchantId(model.getMerchantId());
				closeAccountsMongo.setDepotId(model.getDepotId());
				closeAccountsMongo.setBuId(buId);
				String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
				String maxCloseAccountsMonth="";
				if (StringUtils.isNotBlank(maxdate)) {
					// 获取该月份下月时间
					String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
					maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
				}
				if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
					// 关账下个月日期大于 入库日期
					if (model.getSourceTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
						result.put("code", "01");
						result.put("message", "归属日期必须大于关账日期/月结日期");
						return result;
					}
				}
			}

			// 根据仓库id获取仓库信息
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", model.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);

			//获取商品集合
			AdjustmentInventoryItemModel itemModel = new AdjustmentInventoryItemModel();
			itemModel.setTAdjustmentInventoryId(model.getId());//表头id
			List<AdjustmentInventoryItemModel> resultList = adjustmentInventoryItemDao.list(itemModel);
			for (AdjustmentInventoryItemModel item : resultList) {
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) ||
						(DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())
								&& DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(item.getType()))) {
					if (StringUtils.isBlank(item.getOldBatchNo())) {
						result.put("code", "01");
						result.put("message", "批次号不能为空");
						return result;
					}

					if (item.getProductionDate() == null) {
						result.put("code", "01");
						result.put("message", "生产日期不能为空");
						return result;
					}

					if (item.getOverdueDate() == null) {
						result.put("code", "01");
						result.put("message", "失效日期不能为空");
						return result;
					}
				}
			}
			pushMQ(model, user.getTopidealCode(), resultList);
			/*修改状态*/
			model.setAdjustmentTime(TimeUtils.getNow());
			model.setStatus(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_022);// 处理中
			model.setConfirmUserId(user.getId());
			model.setConfirmUsername(user.getName());
			adjustmentInventoryDao.modify(model);
		}
		result.put("code", "00");
		result.put("message", "保存成功！");
		return result;
	}

	@Override
	public List<Map<String, Object>> listForExport(AdjustmentInventoryDTO dto) {
		List<Map<String, Object>> mapList = adjustmentInventoryDao.listForExport(dto);
		for (Map<String, Object> map : mapList) {
			String status = (String) map.get("status");
			map.put("status", DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentInventory_statusList, status));
			String model = (String) map.get("model");
			map.put("model", DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentInventory_modelList, model));
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> listForExportItem(AdjustmentInventoryDTO dto) {
		List<Map<String, Object>> items = adjustmentInventoryDao.listForExportItem(dto);
		for (Map<String, Object> map : items) {
			String type = (String) map.get("type");
			map.put("type", DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustment_typeList, type));
			String isDamage = (String) map.get("is_damage");
			map.put("is_damage", DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_isDamageList, isDamage));
			String tallyUnit = (String) map.get("tallying_unit");
			map.put("tallying_unit", DERP.getLabelByKey(DERP.unitList, tallyUnit));
		}
		return items;
	}

	/**
	 * 判断导入的日期是否是文本格式且日期是否正确
	 */
	private String isDateContent(String dateStr, String explain) {
		String datePattern = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

		if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
			return explain + "只能为文本格式";
		}

		if (!dateStr.matches(datePattern)) {
			return explain + "日期不正确";
		}
		return null;
	}

	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}
}
