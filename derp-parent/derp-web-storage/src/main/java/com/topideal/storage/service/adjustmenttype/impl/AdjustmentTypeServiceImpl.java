package com.topideal.storage.service.adjustmenttype.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentTypeDao;
import com.topideal.dao.AdjustmentTypeItemDao;
import com.topideal.entity.dto.AdjustmentTypeDTO;
import com.topideal.entity.dto.AdjustmentTypeItemDTO;
import com.topideal.entity.dto.AdjustmentTypeVo;
import com.topideal.entity.vo.AdjustmentTypeItemModel;
import com.topideal.entity.vo.AdjustmentTypeModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.storage.service.adjustmenttype.AdjustmentTypeService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类型调整
 * @author zhanghx
 */
@Service
public class AdjustmentTypeServiceImpl implements AdjustmentTypeService{
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(AdjustmentTypeServiceImpl.class);

	@Autowired
	private AdjustmentTypeDao adjustmentTypeDao;
	@Autowired
	private AdjustmentTypeItemDao adjustmentTypeItemDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	// 仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

	/**
	 * 分页
	 * @param dto
	 * @return
	 */
	@Override
	public AdjustmentTypeDTO listByPage(AdjustmentTypeDTO dto) throws SQLException{
		return adjustmentTypeDao.getListByPage(dto);
	}

	/**
	 * 详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AdjustmentTypeDTO getDetails(AdjustmentTypeDTO dto) throws SQLException {
		return adjustmentTypeDao.getDetails(dto);
	}

	@Override
	public AdjustmentTypeDTO getAdjustDetails(Long id) throws SQLException {
		AdjustmentTypeDTO dto = adjustmentTypeDao.getAdjustDetails(id);
		Map<String, Object> param = new HashMap<>();
		param.put("depotId", dto.getDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(param);
		dto.setDepotType(depot.getType());
		dto.setDepotCode(depot.getCode());
		return dto ;
	}

	/**
	 * 保存事业部
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, String> confirmInDepot(User user, AdjustmentTypeDTO dto) throws Exception {
		Map<String, String> result = new HashMap<>();
		AdjustmentTypeModel model = adjustmentTypeDao.searchById(dto.getId());
		
		List<AdjustmentTypeItemDTO> dtos = dto.getItemList();
		List<Long> buIds = new ArrayList<>();
		for (AdjustmentTypeItemDTO itemDTO : dtos) {
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
			AdjustmentTypeItemModel itemModel = new AdjustmentTypeItemModel();
			itemModel.setId(itemDTO.getId());
			itemModel.setBuId(itemDTO.getBuId());
			itemModel.setBuName(buRelMongoDaoOne.getBuName());
			adjustmentTypeItemDao.modify(itemModel);// 修改事业部
			buIds.add(itemDTO.getBuId());
		}
		
		//判断调整类型单的所有表体信息是否都存在事业部，若是则推送库存加减接口并修改单据状态
		Long count = adjustmentTypeItemDao.countNoExistBu(dto.getId());
		//都存在事业部，推送库存加减接口并修改单据状态
		if (count == 0) {
			// 状态为待调整才可推库存
			if (!model.getStatus().equals(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020)) {	 // 020-待调整
				result.put("code", "01");
				result.put("message", "单据不是待调整状态，无法推送库存扣减");
				return result;
			}
			for (Long buId : buIds) {
				FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
				closeAccountsMongo.setMerchantId(model.getMerchantId());
				closeAccountsMongo.setDepotId(model.getDepotId());
				closeAccountsMongo.setBuId(buId);
				String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
				String maxCloseAccountsMonth="";
				if (StringUtils.isNotBlank(maxdate)) {
					// 获取该月份下月时间
					String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
					maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
				}
				if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
					// 关账下个月日期大于 入库日期
					if (model.getPushTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
						result.put("code", "01");
						result.put("message", "归属日期必须大于关账日期/月结日期");
						return result;
					}
				}
			}
			//推送库存扣减
			this.confirmToAddInventory(model, user.getTopidealCode());
			/*修改状态*/
			model.setAdjustmentTime(TimeUtils.getNow());
			model.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_022);// 处理中
			model.setConfirmUserId(user.getId());
			model.setConfirmUsername(user.getName());
			model.setConfirmTime(TimeUtils.getNow());
			model.setModifyDate(TimeUtils.getNow());
			adjustmentTypeDao.modify(model);
		}
		result.put("code", "00");
		result.put("message", "保存成功！");
		return result;
	}

	/**
	 * 单据确认后生成对应的库存调整收发明细、库存做相应的数量调整
	 * @param model
	 */
	private void confirmToAddInventory(AdjustmentTypeModel model, String topidealCode) throws Exception {
		List<InvetAddOrSubtractGoodsListJson> itemMQList = new ArrayList<>();
		AdjustmentTypeItemModel itemModel = new AdjustmentTypeItemModel();
		itemModel.setTAdjustmentTypeId(model.getId());
		List<AdjustmentTypeItemModel> itemModels = adjustmentTypeItemDao.list(itemModel);
		//仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", model.getDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);
		
		for (AdjustmentTypeItemModel adjustmentTypeItem : itemModels) {
			//查询事业部
			Map<String, Object> buMap = new HashMap<>();
			buMap.put("buId", adjustmentTypeItem.getBuId());
			buMap.put("merchantId", model.getMerchantId());
			buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
			MerchantBuRelMongo buRelMongo = merchantBuRelMongoDao.findOne(buMap);

			if (model.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2)) {	// 2-货号变更
				// 入的货号 增  出的货号减(此处为入)
				//调增时：判断仓库是否是批次强校验或者入库强校验；
				//调减时：判断仓库是否是批次强校验
				if (depot.getBatchValidation().matches(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1 + "|" + DERP_SYS.DEPOTINFO_BATCHVALIDATION_2)) {
					if (StringUtils.isBlank(adjustmentTypeItem.getOldBatchNo())) {
						throw new RuntimeException("批次不能为空！");
					}

					if (adjustmentTypeItem.getProductionDate() == null) {
						throw new RuntimeException("生产日期不能为空！");
					}

					if (adjustmentTypeItem.getOverdueDate() == null) {
						throw new RuntimeException("失效日期不能为空！");
					}
				}
				InvetAddOrSubtractGoodsListJson itemMQModel2 = new InvetAddOrSubtractGoodsListJson();
				itemMQModel2.setBarcode(adjustmentTypeItem.getBarcode());// 商品条形码
				itemMQModel2.setGoodsId(String.valueOf(adjustmentTypeItem.getGoodsId()));//商品ID
				itemMQModel2.setGoodsName(adjustmentTypeItem.getGoodsName());//商品名称
				itemMQModel2.setGoodsNo(adjustmentTypeItem.getGoodsNo());//商品货号
				itemMQModel2.setBatchNo(adjustmentTypeItem.getOldBatchNo());//批次号

				if (adjustmentTypeItem.getProductionDate() != null) {
					itemMQModel2.setProductionDate(TimeUtils.formatDay(adjustmentTypeItem.getProductionDate()));//生产日期
				}
				String isExpire = DERP.ISEXPIRE_1;
				if (adjustmentTypeItem.getOverdueDate() != null) {
					itemMQModel2.setOverdueDate(TimeUtils.formatDay(adjustmentTypeItem.getOverdueDate()));//失效日期
					isExpire = TimeUtils.isNotIsExpireByDate(adjustmentTypeItem.getOverdueDate());
				}
				itemMQModel2.setIsExpire(isExpire);//是否过期  （0 是 1 否）
				itemMQModel2.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品 2 过期）
				itemMQModel2.setNum(adjustmentTypeItem.getAdjustTotal());//数量
				String outTallyingUnit = adjustmentTypeItem.getTallyingUnit();
				if ("P".equals(outTallyingUnit)) {
					itemMQModel2.setUnit(DERP.INVENTORY_UNIT_0);// 香港仓必填
				}else if ("C".equals(outTallyingUnit)) {
					itemMQModel2.setUnit(DERP.INVENTORY_UNIT_1);// 香港仓必填
				}else if ("B".equals(outTallyingUnit)) {
					itemMQModel2.setUnit(DERP.INVENTORY_UNIT_2);// 香港仓必填
				}
				itemMQModel2.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//字符串 0 调减 1调增
				// 事业部
				itemMQModel2.setBuId(String.valueOf(adjustmentTypeItem.getBuId()));
				itemMQModel2.setBuName(buRelMongo.getBuName());
				itemMQList.add(itemMQModel2);

				InvetAddOrSubtractGoodsListJson itemMQModel1 = new InvetAddOrSubtractGoodsListJson();
				Map<String, Object> mParam = new HashMap<>();
				mParam.put("merchantId", model.getMerchantId());
				mParam.put("goodsNo", adjustmentTypeItem.getOldGoodsNo());
				mParam.put("status",DERP_SYS.MERCHANDISEINFO_STATUS_1);
				MerchandiseInfoMogo oldGoods = merchandiseInfoMogoDao.findOne(mParam);
				itemMQModel1.setBarcode(adjustmentTypeItem.getOldBarcode());
				itemMQModel1.setGoodsId(String.valueOf(oldGoods.getMerchandiseId()));//商品ID
				itemMQModel1.setGoodsName(adjustmentTypeItem.getGoodsName());//商品名称
				itemMQModel1.setGoodsNo(adjustmentTypeItem.getOldGoodsNo());//商品货号
				itemMQModel1.setBatchNo(adjustmentTypeItem.getOldBatchNo());//批次号
				itemMQModel1.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品 2 过期）
				itemMQModel1.setNum(adjustmentTypeItem.getAdjustTotal());//数量

				itemMQModel1.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//字符串 0 调减 1调增

				if (adjustmentTypeItem.getProductionDate() != null) {
					itemMQModel1.setProductionDate(TimeUtils.formatDay(adjustmentTypeItem.getProductionDate()));//生产日期
				}
				isExpire = DERP.ISEXPIRE_1;
				if (adjustmentTypeItem.getOverdueDate() != null) {
					itemMQModel1.setOverdueDate(TimeUtils.formatDay(adjustmentTypeItem.getOverdueDate()));//失效日期
					isExpire = TimeUtils.isNotIsExpireByDate(adjustmentTypeItem.getOverdueDate());
				}
				itemMQModel1.setIsExpire(isExpire);//是否过期  （0 是 1 否）

				if ("P".equals(outTallyingUnit)) {
					itemMQModel1.setUnit(DERP.INVENTORY_UNIT_0);// 香港仓必填
				}else if ("C".equals(outTallyingUnit)) {
					itemMQModel1.setUnit(DERP.INVENTORY_UNIT_1);// 香港仓必填
				}else if ("B".equals(outTallyingUnit)) {
					itemMQModel1.setUnit(DERP.INVENTORY_UNIT_2);// 香港仓必填
				}
				// 事业部
				itemMQModel1.setBuId(String.valueOf(adjustmentTypeItem.getBuId()));
				itemMQModel1.setBuName(buRelMongo.getBuName());
				itemMQModel1.setStockLocationTypeId(String.valueOf(adjustmentTypeItem.getStockLocationTypeId()));
				itemMQModel1.setStockLocationTypeName(adjustmentTypeItem.getStockLocationTypeName());
				itemMQList.add(itemMQModel1);
			}else {
				//调增时：判断仓库是否是批次强校验或者入库强校验；
				//调减时：判断仓库是否是批次强校验
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) ||
						(DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation()) &&
								DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(adjustmentTypeItem.getType()))) {
					if (StringUtils.isBlank(adjustmentTypeItem.getOldBatchNo())) {
						throw new RuntimeException("批次不能为空！");
					}

					if (adjustmentTypeItem.getProductionDate() == null) {
						throw new RuntimeException("生产日期不能为空！");
					}

					if (adjustmentTypeItem.getOverdueDate() == null) {
						throw new RuntimeException("失效日期不能为空！");
					}
				}
				InvetAddOrSubtractGoodsListJson itemMQModel = new InvetAddOrSubtractGoodsListJson();

				itemMQModel.setBarcode(adjustmentTypeItem.getBarcode());// 商品条形码
				itemMQModel.setGoodsId(String.valueOf(adjustmentTypeItem.getGoodsId()));//商品ID
				itemMQModel.setGoodsName(adjustmentTypeItem.getGoodsName());//商品名称
				itemMQModel.setGoodsNo(adjustmentTypeItem.getGoodsNo());//商品货号

				itemMQModel.setBatchNo(adjustmentTypeItem.getOldBatchNo());//批次号
				if (adjustmentTypeItem.getProductionDate() != null) {
					itemMQModel.setProductionDate(TimeUtils.formatDay(adjustmentTypeItem.getProductionDate()));//生产日期
				}
				String isExpire = DERP.ISEXPIRE_1;
				if (adjustmentTypeItem.getOverdueDate() != null) {
					itemMQModel.setOverdueDate(TimeUtils.formatDay(adjustmentTypeItem.getOverdueDate()));//失效日期
					isExpire = TimeUtils.isNotIsExpireByDate(adjustmentTypeItem.getOverdueDate());
				}
				itemMQModel.setIsExpire(isExpire);//是否过期  （0 是 1 否）
				itemMQModel.setType(adjustmentTypeItem.getIsDamage());//商品分类 （0 好品 1坏品 2 过期）
				itemMQModel.setNum(adjustmentTypeItem.getAdjustTotal());//数量
				String tallyingUnit = adjustmentTypeItem.getTallyingUnit();
				// api 传值是  00：托盘01：箱02：件 和我们数据对应
				if (StringUtils.isNotBlank(tallyingUnit)) {
					if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
						itemMQModel.setUnit(DERP.INVENTORY_UNIT_0);// 库存 单位	字符串 0 托盘 1箱  2 件
					}else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
						itemMQModel.setUnit(DERP.INVENTORY_UNIT_1);// 库存 单位	字符串 0 托盘 1箱  2 件
					}else if (DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
						itemMQModel.setUnit(DERP.INVENTORY_UNIT_2);// 库存 单位	字符串 0 托盘 1箱  2 件
					}
				}
				// 事业部
				itemMQModel.setBuId(String.valueOf(adjustmentTypeItem.getBuId()));
				itemMQModel.setBuName(buRelMongo.getBuName());
				itemMQModel.setOperateType(adjustmentTypeItem.getType());//字符串 0 调减 1调增
				itemMQModel.setStockLocationTypeId(String.valueOf(adjustmentTypeItem.getStockLocationTypeId()));
				itemMQModel.setStockLocationTypeName(adjustmentTypeItem.getStockLocationTypeName());
				itemMQList.add(itemMQModel);
			}
		}

		InvetAddOrSubtractRootJson addMQModel = new InvetAddOrSubtractRootJson();
		addMQModel.setBusinessNo(model.getSourceCode());// 业务单号
		addMQModel.setMerchantId(String.valueOf(model.getMerchantId()));//商家ID
		addMQModel.setMerchantName(model.getMerchantName());//商家名称
		addMQModel.setTopidealCode(topidealCode);//商家编码
		addMQModel.setDepotId(String.valueOf(model.getDepotId()));//仓库ID
		addMQModel.setDepotName(model.getDepotName());//仓库名称
		addMQModel.setDepotCode(depot.getDepotCode());//仓库编码
		addMQModel.setDepotType(depot.getType());//仓库类型
		addMQModel.setOrderNo(model.getCode());//订单号
		addMQModel.setIsTopBooks(depot.getIsTopBooks());//是否代销仓
		addMQModel.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0014);//LETZD("0014","类型调整单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
		if (model.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_1)) {
			addMQModel.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0016); //好坏互转
		} else if (model.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2)) {
			addMQModel.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0017); //货号变更
		} else if (model.getModel().equals(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_4)) {
			addMQModel.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0019); //大货理货
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		addMQModel.setSourceDate(now);//单据时间
		// 获取当前年月
		addMQModel.setOwnMonth(TimeUtils.formatMonth(model.getPushTime()));//归属月份
		addMQModel.setDivergenceDate(TimeUtils.formatFullTime(model.getPushTime()));//出入时间
		addMQModel.setGoodsList(itemMQList);// 商品信息
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		addMQModel.setBackTags(MQPushBackStorageEnum.STORAGE_TRANSFER_ORDER_PUSH_BACK.getTags());//回调标签
		addMQModel.setBackTopic(MQPushBackStorageEnum.STORAGE_TRANSFER_ORDER_PUSH_BACK.getTopic());//回调主题
		customParam.put("code", model.getCode());// 电商订单内部单号
		addMQModel.setCustomParam(customParam);////自定义回调参数
		String jsonMQObject = JSONObject.fromObject(addMQModel).toString();

		rocketMQProducer.send(jsonMQObject, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

	}
	/**
	 * 导入“好坏品互转”的库存类型调整单据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map importAdjustment(List<List<Map<String, String>>> data, User user) throws Exception {

		// 以“仓库名称 +调整时间”为维度生成列表记录
		Map<String, List<AdjustmentTypeVo>> dataMap = new HashMap<String, List<AdjustmentTypeVo>>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		// 校验“相同仓库+相同调整日期+相同商品货号+相同事业部+相同批次号（若无则不需）+相同调整前/后类型”是否存在多条记录
		Map<String, Object> checkKeyMap= new HashMap<>();
		Map<String, AdjustmentTypeVo> headDepotMap = new HashMap<>();
		//判断表头的序号是否能在商品信息中找到对应的信息
		Map<String, Integer> serialNumMap = new HashMap<>();
		List<ImportMessage> errorList = new ArrayList<ImportMessage>();
		/**************************参数检查start*************************/
		// 表头必填字段的校验
		for (int i = 0; i < data.get(0).size(); i++) {
			Map<String, String> map = data.get(0).get(i);
			try {
				String serialNum = map.get("批量导入序号").trim();
				if (StringUtils.isEmpty(serialNum)) {
					setErrorMsg(i+1, "批量导入序号不能为空", errorList);
					failure += 1;
					continue;
				}
				if (serialNumMap.containsKey(serialNum)) {
					setErrorMsg(i+1, "批量导入序号不能重复", errorList);
					failure += 1;
					continue;
				}
				String adjustmentTypeName = map.get("业务类别").trim();
				if (StringUtils.isEmpty(adjustmentTypeName)) {
					setErrorMsg(i+1, "业务类别不能为空", errorList);
					failure += 1;
					continue;
				}
				if (!"好坏品互转".equals(adjustmentTypeName)) {
					setErrorMsg(i+1, "业务类别只能为“好坏品互转”", errorList);
					failure += 1;
					continue;
				}
				String depotCode = map.get("调整仓库");
				if (StringUtils.isEmpty(depotCode)) {
					setErrorMsg(i+1, "调整仓库不能为空", errorList);
					failure += 1;
					continue;
				}
				depotCode = depotCode.trim();
				// 根据仓库编码获取仓库信息
				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotCode", depotCode);
				DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);
				if (depot == null) {
					setErrorMsg(i+1, "该调整仓库仓库编码不存在", errorList);
					failure += 1;
					continue;
				}
				// 仓库公司关联表
				Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
				depotMerchantRelParam.put("merchantId", user.getMerchantId());
				depotMerchantRelParam.put("depotId", depot.getDepotId());
				DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
				if(depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)){
					setErrorMsg(i+1, "调整仓库为："+depotCode+",未查到该公司下仓库信息", errorList);
					failure += 1;
					continue;
				}				
				String adjustmentTimeStr = map.get("调整日期");
				if (StringUtils.isEmpty(adjustmentTimeStr)) {
					setErrorMsg(i+1, "调整日期不能为空", errorList);
					failure += 1;
					continue;
				}
				//判断调整日期格式是否正确
				if (StringUtils.isNotBlank(adjustmentTimeStr)) {
					String msg = this.isDateContent(adjustmentTimeStr, "调整日期");
					if (StringUtils.isNotBlank(msg)) {
						setErrorMsg(i+1, msg, errorList);
						failure += 1;
						continue;
					}
				}
				
				if(TimeUtils.daysBetween(TimeUtils.parse(adjustmentTimeStr, "yyyy-MM-dd"), new Date()) < 0) {
					setErrorMsg(i+1, "调整日期不可超过当前时间", errorList);
					failure += 1;
					continue;
				}
				String sourceCode = map.get("来源单据号");
				if (StringUtils.isEmpty(sourceCode)) {
					setErrorMsg(i+1, "来源单据号不能为空", errorList);
					failure += 1;
					continue;
				}
				sourceCode = sourceCode.trim();
				String remark = map.get("调整原因");
				AdjustmentTypeVo adjustmentTypeVo = new AdjustmentTypeVo();
				adjustmentTypeVo.setSerialNum(serialNum);
				adjustmentTypeVo.setAdjustmentTime(adjustmentTimeStr+" 00:00:00");// yyyy-MM-dd HH:mm:ss
				adjustmentTypeVo.setDepotInfoMongo(depot);
				adjustmentTypeVo.setDepotCode(depotCode);
				adjustmentTypeVo.setDepotMerchantRelMongo(depotMerchantRelMongo);
				adjustmentTypeVo.setAdjustmentTypeName(adjustmentTypeName);	//好坏品互转
				adjustmentTypeVo.setAdjustmentRemark(remark);
				adjustmentTypeVo.setSourceCode(sourceCode);// 来源单据号
				headDepotMap.put(serialNum, adjustmentTypeVo);
				serialNumMap.put(serialNum, i+1);
			} catch (Exception e) {
				e.printStackTrace();
                setErrorMsg(i+1, e.getMessage(), errorList);
				failure += 1;
				continue;
			}
		}
		if (failure == 0) {
			// 商品信息必填字段的校验
			for (int j = 0; j < data.get(1).size(); j++) {
				Map<String, String> map = data.get(1).get(j);
				try {
					String serialNum = map.get("对应表头序号");
					if (StringUtils.isEmpty(serialNum) && !StringUtils.isNumeric(serialNum)) {
						setErrorMsg(j+1, "对应表头序号不能为空", errorList);
						failure += 1;
						continue;
					}
					if (!headDepotMap.containsKey(serialNum)) {
						setErrorMsg(j+1, "对应表头序号在表头不存在", errorList);
						failure += 1;
						continue;
					}
					serialNum = serialNum.trim();
					if (serialNumMap.containsKey(serialNum)) {
						serialNumMap.remove(serialNum);
					}
					String goodsNo = map.get("商品货号");
					DepotInfoMongo depot = headDepotMap.get(serialNum).getDepotInfoMongo();
					DepotMerchantRelMongo depotMerchantRelMongo = headDepotMap.get(serialNum).getDepotMerchantRelMongo();
					if (StringUtils.isEmpty(goodsNo)) {
						setErrorMsg(j+1, "商品货号不能为空", errorList);
						failure += 1;
						continue;
					}
					goodsNo = goodsNo.trim();

					String buCode = map.get("事业部");
					if (StringUtils.isEmpty(buCode)) {
						setErrorMsg(j+1, "事业部不能为空", errorList);
						failure += 1;
						continue;
					}
					buCode = buCode.trim();

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

					AdjustmentTypeVo vo = headDepotMap.get(serialNum);
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(user.getMerchantId());
					closeAccountsMongo.setDepotId(depot.getDepotId());
					closeAccountsMongo.setBuId(buRelMongoDaoOne.getBuId());
					String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxdate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 调整日期
						if (Timestamp.valueOf(vo.getAdjustmentTime()).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							setErrorMsg(j+1, "调整日期必须大于关账日期", errorList);
							failure += 1;
							continue;
						}
					}
					String goodsName = map.get("商品名称");
					String batchNo = map.get("原批次号");
					String productionDate = map.get("生产日期");
					String overdueDate = map.get("失效日期");

					/**
					 * 1.若仓库类型为批次强校验或入库强校验时，则批次号、生产日期、失效日期必填；
					 * 3.若仓库类型是海外仓，理货单位必填,并且填的是理货单位编码
					 */
					// 如果仓库设置了批次效期强校验 批次效期必填
					if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())||DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())){
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
						String msg = this.isDateContent(productionDate, "生产日期");
						productionDate+= " 00:00:00";
						if (StringUtils.isNotBlank(msg)) {
							setErrorMsg(j+1, msg, errorList);
							failure += 1;
							continue;
						}
					}
					if (StringUtils.isNotBlank(overdueDate)) {
						String msg = this.isDateContent(overdueDate, "失效日期");
						overdueDate+= " 00:00:00";
						if (StringUtils.isNotBlank(msg)) {
							setErrorMsg(j+1, msg, errorList);
							failure += 1;
							continue;
						}
					}
					String oldGoodType = map.get("调整前商品类型");
					if (StringUtils.isEmpty(oldGoodType)) {
						setErrorMsg(j+1, "调整前商品类型不能为空", errorList);
						failure += 1;
						continue;
					}
					oldGoodType = oldGoodType.trim();
					if(!oldGoodType.equals("好品") && !oldGoodType.equals("坏品")){	
						setErrorMsg(j+1, "调整前商品类型填写有误，请填写“好品”或“坏品”", errorList);
						failure += 1;
						continue;
					}
					String newGoodType = map.get("调整后商品类型");
					if (StringUtils.isEmpty(newGoodType)) {
						setErrorMsg(j+1, "调整后商品类型不能为空", errorList);
						failure += 1;
						continue;
					}
					if(!newGoodType.equals("好品") && !newGoodType.equals("坏品")){	
						setErrorMsg(j+1, "调整后商品类型填写有误，请填写“好品”或“坏品”", errorList);
						failure += 1;
						continue;
					}
					String num = map.get("总调整数量");
					if (StringUtils.isEmpty(num)) {
						setErrorMsg(j+1, "总调整数量不能为空", errorList);
						failure += 1;
						continue;
					}
					if (!isNumeric(num)) {
						setErrorMsg(j+1, "总调整数量必须为整数", errorList);
						failure += 1;
						continue;
					}
					
					//海外仓 必填项
					String tallyingUnit = map.get("理货单位");
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
						if (StringUtils.isEmpty(tallyingUnit)) {
							setErrorMsg(j+1, "调整仓库为香港仓时，理货单位不能为空", errorList);
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
					}else{
						// 不填即默认为空
						tallyingUnit = null;
					}
					// 商品信息
					Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
					merchandiseInfo_params.put("goodsNo", goodsNo);
					merchandiseInfo_params.put("merchantId", user.getMerchantId());
					merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
					// 获取商品信息
					MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
					if (merchandiseInfo == null) {
						setErrorMsg(j+1, "商品货号不存在", errorList);
						failure += 1;
						continue;
					}
					//当仓库在该商家下的“选品限制”为“仅备案商品”时，可选的商品为该商家下仅为备案商品；
					if (depotMerchantRelMongo != null && DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRelMongo.getProductRestriction())) {
						if (!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(merchandiseInfo.getIsRecord())) {
							setErrorMsg(j+1, "该商品不是备案商品", errorList);
							failure += 1;
							continue;
						}
					}
					//当仓库 在该商家下的“选品限制”为“仅外仓统一码”时，可选的商品为该商家下仅为标识为外仓统一码的商品货号
					else if (depotMerchantRelMongo != null && DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRelMongo.getProductRestriction())) {
						if (!DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0.equals(merchandiseInfo.getOutDepotFlag())) {
							setErrorMsg(j+1, "该商品不是“仅外仓统一码”商品", errorList);
							failure += 1;
							continue;
						}
						// 若选品限制是外仓统一码
						Map<String, Object> paramMap = new HashMap<String, Object>();
						List<MerchandiseInfoMogo> inMerchandiseList = new ArrayList<>();
						paramMap.put("commbarcode", merchandiseInfo.getCommbarcode());
						paramMap.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);// 是外仓统一码
						paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
						paramMap.put("merchantId", user.getMerchantId());
						inMerchandiseList = merchandiseInfoMogoDao.findAll(paramMap);
						if(inMerchandiseList.size()==0){
							setErrorMsg(j+1, "根据标准条码为:"+merchandiseInfo.getCommbarcode()+"仓库的选品限制是外仓统一码未查询到符合条件的商品", errorList);
							failure += 1;
							continue;
						}else if(inMerchandiseList.size()>1){
							setErrorMsg(j+1, "根据标准条码为:"+merchandiseInfo.getCommbarcode()+"仓库的选品限制是外仓统一码查询到多个符合条件的商品", errorList);
							failure += 1;
							continue;
						}else{
							merchandiseInfo = inMerchandiseList.get(0);
						}
					}
					/**
					 * 校验“对应表头序号+相同仓库+相同调整日期+相同商品货号+相同事业部+相同批次号（若无则不需）+相同调整前/后类型”是否存在多条记录，
					 * 有则报错提示需合并数据；调整前/后类型不允许是同种类型
					 */
					String depotIdStr = depot.getDepotId()!=null?depot.getDepotId().toString():"0";
					String key = serialNum+depotIdStr+vo.getAdjustmentTime()+goodsNo+buCode+batchNo+oldGoodType+newGoodType;
					if(checkKeyMap.containsKey(key)){
						setErrorMsg(j+1, "对应表头序号:"+serialNum+"仓库自编码:"+depot.getDepotCode() +",调整日期:"+vo.getAdjustmentTime() +",商品货号:"+goodsNo + ",事业部:" + buCode
								+",批次号:"+batchNo+",调整前商品类型:"+oldGoodType+",调整后商品类型:"+newGoodType+",有多条数据,合并后导入", errorList);
						failure += 1;
						continue;
					}else{
						checkKeyMap.put(key, key);
					}

					AdjustmentTypeVo adjustmentTypeVo = new AdjustmentTypeVo();
					BeanUtils.copyProperties(vo, adjustmentTypeVo);
					adjustmentTypeVo.setDepotId(depot.getDepotId());
					adjustmentTypeVo.setDepotName(depot.getName());
					adjustmentTypeVo.setGoodsNo(merchandiseInfo.getGoodsNo());
					adjustmentTypeVo.setAdjustTotal(Integer.parseInt(num));
					adjustmentTypeVo.setBatchNo(batchNo);
					adjustmentTypeVo.setProductionDate(productionDate);
					adjustmentTypeVo.setOverdueDate(overdueDate);
					adjustmentTypeVo.setGoodsCode(merchandiseInfo.getGoodsCode());
					adjustmentTypeVo.setGoodsName(merchandiseInfo.getName());
					adjustmentTypeVo.setGoodsId(merchandiseInfo.getMerchandiseId());
					adjustmentTypeVo.setBarcode(merchandiseInfo.getBarcode());
					adjustmentTypeVo.setTallyingUnit(tallyingUnit);
					adjustmentTypeVo.setBuName(buRelMongoDaoOne.getBuName());
					adjustmentTypeVo.setBuId(buRelMongoDaoOne.getBuId());
					adjustmentTypeVo.setOldGoodType(oldGoodType);
					adjustmentTypeVo.setNewGoodType(newGoodType);
					if (buStockLocationTypeConfigMongo != null) {
						adjustmentTypeVo.setStockLocationTypeId(buStockLocationTypeConfigMongo.getBuStockLocationTypeId());
						adjustmentTypeVo.setStockLocationTypeName(buStockLocationTypeConfigMongo.getName());
					}

					// 以“对应表头序号+仓库名称 +调整时间”为维度生成列表记录
					String flagKey = serialNum+adjustmentTypeVo.getDepotId()+adjustmentTypeVo.getAdjustmentTime();
					if(dataMap.get(flagKey)==null){
						List<AdjustmentTypeVo> list = new ArrayList<AdjustmentTypeVo>();
						list.add(adjustmentTypeVo);
						dataMap.put(flagKey, list);
					}else{
						dataMap.get(flagKey).add(adjustmentTypeVo);
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
			for(List<AdjustmentTypeVo> list: dataMap.values()) {//start---002
				AdjustmentTypeVo vo = list.get(0);
			    Long id = null;
				// 新增表头
				String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LXTZO);
				AdjustmentTypeModel model = new AdjustmentTypeModel();
				model.setCode(code);// 类型调整编码
				model.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020);// 待调整
				model.setModel(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_1);// 业务类别 1.好坏品互转
				model.setAdjustmentTypeName(vo.getAdjustmentTypeName());//类型调整名称
				model.setSourceCode(vo.getSourceCode());	// 来源单据号
				model.setAdjustmentRemark(vo.getAdjustmentRemark());	//调整原因
				model.setMerchantId(user.getMerchantId());// 商家id
				model.setMerchantName(user.getMerchantName());// 商家名称
				model.setDepotId(vo.getDepotId());// 仓库id
				model.setDepotName(vo.getDepotName());// 仓库名称
				model.setAdjustmentTime(TimeUtils.parseFullTime(vo.getAdjustmentTime()));	// 调整时间
				model.setCodeTime(TimeUtils.getNow());// 单据时间
				model.setPushTime(TimeUtils.parseFullTime(vo.getAdjustmentTime()));//归属日期
				model.setSource(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_02);	// 手工导入
				model.setCreateUserId(user.getId());// 用户id
				model.setCreateUsername(user.getUsername());// 用户名
				model.setCreateDate(TimeUtils.getNow());
				id = adjustmentTypeDao.save(model);
				//循环列表生成表体
				for(AdjustmentTypeVo voItem:list){//start----003
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//调整前表体信息
					AdjustmentTypeItemModel beforeItem = new AdjustmentTypeItemModel();
					beforeItem.settAdjustmentTypeId(id);
					beforeItem.setGoodsId(voItem.getGoodsId());// 商品id
					beforeItem.setGoodsCode(voItem.getGoodsCode());// 商品编码
					beforeItem.setGoodsNo(voItem.getGoodsNo());// 商品货号
					beforeItem.setGoodsName(voItem.getGoodsName());// 商品名称
					beforeItem.setBarcode(voItem.getBarcode()); //条码
					beforeItem.setOldBatchNo(voItem.getBatchNo());// 原始批次号
					beforeItem.setTallyingUnit(voItem.getTallyingUnit());// 理货单位
					beforeItem.setBuId(voItem.getBuId());
					beforeItem.setBuName(voItem.getBuName());
					beforeItem.setOldGoodType(voItem.getOldGoodType()+"-");
					beforeItem.setAdjustTotal(voItem.getAdjustTotal());
					beforeItem.setCreateDate(TimeUtils.getNow());
					beforeItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_0);
					beforeItem.setStockLocationTypeId(voItem.getStockLocationTypeId());
					beforeItem.setStockLocationTypeName(voItem.getStockLocationTypeName());
					if ("好品".equals(voItem.getOldGoodType())) {
						beforeItem.setIsDamage(DERP.ISDAMAGE_0);
					} else if ("坏品".equals(voItem.getOldGoodType())) {
						beforeItem.setIsDamage(DERP.ISDAMAGE_1);
					}

					if(StringUtils.isNotBlank(voItem.getProductionDate())){
						beforeItem.setProductionDate(sdf.parse(voItem.getProductionDate()));// 生产日期
					}
					if(StringUtils.isNotBlank(voItem.getOverdueDate())){
						beforeItem.setOverdueDate(sdf.parse(voItem.getOverdueDate()));// 失效日期
					}
					adjustmentTypeItemDao.save(beforeItem);
					//调整后表体信息
					AdjustmentTypeItemModel afterItem = new AdjustmentTypeItemModel();
					afterItem.settAdjustmentTypeId(id);
					afterItem.setGoodsId(voItem.getGoodsId());// 商品id
					afterItem.setGoodsCode(voItem.getGoodsCode());// 商品编码
					afterItem.setGoodsNo(voItem.getGoodsNo());// 商品货号
					afterItem.setGoodsName(voItem.getGoodsName());// 商品名称
					afterItem.setBarcode(voItem.getBarcode()); //条码
					afterItem.setOldBatchNo(voItem.getBatchNo());// 原始批次号
					afterItem.setTallyingUnit(voItem.getTallyingUnit());// 理货单位
					afterItem.setBuId(voItem.getBuId());
					afterItem.setBuName(voItem.getBuName());
					afterItem.setNewGoodType(voItem.getNewGoodType()+"+");
					afterItem.setAdjustTotal(voItem.getAdjustTotal());
					afterItem.setCreateDate(TimeUtils.getNow());
					afterItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_1);
					afterItem.setStockLocationTypeId(voItem.getStockLocationTypeId());
					afterItem.setStockLocationTypeName(voItem.getStockLocationTypeName());
					if ("好品".equals(voItem.getNewGoodType())) {
						afterItem.setIsDamage(DERP.ISDAMAGE_0);
					} else if ("坏品".equals(voItem.getNewGoodType())) {
						afterItem.setIsDamage(DERP.ISDAMAGE_1);
					}
					if(StringUtils.isNotBlank(voItem.getProductionDate())){
						afterItem.setProductionDate(sdf.parse(voItem.getProductionDate()));// 生产日期
					}
					if(StringUtils.isNotBlank(voItem.getOverdueDate())){
						afterItem.setOverdueDate(sdf.parse(voItem.getOverdueDate()));// 失效日期
					}
					adjustmentTypeItemDao.save(afterItem);
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
	private void setErrorMsg(int index , String msg ,List<ImportMessage> resultList) {
		ImportMessage message = new ImportMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
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
	 * 批量删除
	 */
	@Override
	public boolean deleteByIds(List<Long> list) throws Exception {
		int flag = 0;
		for (Long id : list) {
			AdjustmentTypeModel model = adjustmentTypeDao.searchById(id);
			// 仅对单据来源为“手工导入”且单据状态为“待调整”的调整单可进行操作删除，支持批量删除。
			if (!DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020.equals(model.getStatus()) ||
				  !DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_02.equals(model.getSource())) {
				flag = 1;
				break;
			}
		}
		if (flag == 1) {
			throw new Exception("删除失败，单据来源必须为手工导入并且状态必须为待调整");
		}
		for (Long id : list) {
			AdjustmentTypeModel upmodel = new AdjustmentTypeModel();
			upmodel.setId(id);
			upmodel.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_006);// 已删除
			upmodel.setModifyDate(TimeUtils.getNow());
			adjustmentTypeDao.modify(upmodel);
		}
		return true;
	}
	/**
	 * 确认调整:对于手工导入且单据状态为待调整的单据可操作
	 */
	@Override
	public boolean confirmAdjustment(Long id, User user) throws Exception {
		AdjustmentTypeModel searchModel = adjustmentTypeDao.searchById(id);
		// 对于手工导入且单据状态为待调整的单据可操作项为：确认调整
		if(!DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020.equals(searchModel.getStatus()) &&
			!DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_02.equals(searchModel.getSource())){
			throw new RuntimeException("对于手工导入且单据状态为待调整的单据才可操作确认调整");
		}
		AdjustmentTypeItemModel itemModel = new AdjustmentTypeItemModel();
		itemModel.settAdjustmentTypeId(id);
		List<AdjustmentTypeItemModel> itemModels = adjustmentTypeItemDao.list(itemModel);
		for (AdjustmentTypeItemModel item : itemModels) {
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(searchModel.getDepotId());
			closeAccountsMongo.setBuId(item.getBuId());
			String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 归属日期
				if (searchModel.getPushTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("确认调整失败，归属日期必须大于关账日期");
				}
			}
		}
		//推送库存扣减
		this.confirmToAddInventory(searchModel, user.getTopidealCode());
		/*修改状态*/
		AdjustmentTypeModel updateModel = new AdjustmentTypeModel();
		updateModel.setId(id);
		updateModel.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_022);// 处理中
		updateModel.setConfirmUserId(user.getId());
		updateModel.setConfirmUsername(user.getName());
		updateModel.setConfirmTime(TimeUtils.getNow());
		updateModel.setModifyDate(TimeUtils.getNow());
		adjustmentTypeDao.modify(updateModel);
		return true;
	}

	@Override
	public List<Map<String, Object>> listForExport(AdjustmentTypeDTO dto) {
		List<Map<String, Object>> mapList = adjustmentTypeDao.listForExport(dto);
		for (Map<String, Object> map : mapList) {
			String status = (String) map.get("status");
			map.put("status", DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentType_statusList, status));
			String model = (String) map.get("model");
			map.put("model", DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentType_modelList, model));
			String source = (String) map.get("source");
			map.put("source", DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentType_sourceList, source));
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> listForExportItem(AdjustmentTypeDTO dto) {
		List<Map<String, Object>> items = adjustmentTypeDao.listForExportItem(dto);
		for (Map<String, Object> map : items) {
			String type = (String) map.get("type");
			map.put("type", DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustment_typeList, type));
			String isDamage = (String) map.get("is_damage");
			map.put("is_damage", DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_isDamageList, isDamage));
			String tallyUnit = (String) map.get("tallying_unit");
			map.put("tallying_unit", DERP.getLabelByKey(DERP.unitList, tallyUnit));
			String model = (String) map.get("model");
			if (model.matches(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_3 + "|" + DERP_STORAGE.ADJUSTMENTTYPE_MODEL_5)) {
				Date overdueDate = (Date)map.get("overdue_date");
				if (overdueDate != null) {
					String isExpire = TimeUtils.isNotIsExpireByDate(overdueDate);
					map.put("is_expire", DERP.getLabelByKey(DERP.isExpireList, isExpire));
				}
			}
		}
		return items;
	}
}
