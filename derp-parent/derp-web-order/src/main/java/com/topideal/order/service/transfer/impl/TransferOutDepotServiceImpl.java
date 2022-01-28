package com.topideal.order.service.transfer.impl;


import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.dao.transfer.TransferOrderItemDao;
import com.topideal.dao.transfer.TransferOutDepotDao;
import com.topideal.dao.transfer.TransferOutDepotItemDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.transfer.TransferOutDepotService;
import com.topideal.order.shiro.ShiroUtils;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 调拨出库service实现类
 * @author yucaifu
 */
@Service
public class TransferOutDepotServiceImpl implements TransferOutDepotService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TransferOutDepotServiceImpl.class);

	//调拨出库表
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;
	
	//调拨出库详情表
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	
	@Autowired
	private TransferOrderDao transferOrderDao;
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	/**
	 * 列表（分页）
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public TransferOutDepotDTO listTransferOutDepotPage(TransferOutDepotDTO dto, User user) throws SQLException {
		List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels.isEmpty()) {
			dto.setTotal(0);
			dto.setList(new ArrayList());
			return dto;
		}
		dto.setUserBuList(userBuRels);
		return transferOutDepotDao.listDTOByPage(dto);
	}
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public TransferOutDepotDTO searchDetail(Long id) throws SQLException {
		TransferOutDepotDTO dto = new TransferOutDepotDTO();
		dto.setId(id);
		return transferOutDepotDao.getByDto(dto);
	}
	
	public Integer listForCount(TransferOutDepotDTO dto, User user){
		List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels.isEmpty()) {
			return 0;
		}
		dto.setUserBuList(userBuRels);
		return transferOutDepotDao.listForCount(dto);
	}
	
	@Override
	public List<Map<String, Object>> listForMap(TransferOutDepotDTO dto, User user) {
		List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels.isEmpty()) {
			return new ArrayList<>();
		}
		dto.setUserBuList(userBuRels);
		return transferOutDepotDao.listForMap(dto);
	}
	@Override
	public List<Map<String, Object>> listForMapItem(TransferOutDepotDTO dto, User user) {
		List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels.isEmpty()) {
			return new ArrayList<>();
		}
		dto.setUserBuList(userBuRels);
		return transferOutDepotDao.listForMapItem(dto);
	}

	@Override
	public Map<String, String> saveTransferOutDepot(TransferOutDepotDTO dto, User user) throws Exception {
		Map<String, String> result = new HashMap<>();
		//查询调拨订单
		TransferOrderModel transferOrderModel = transferOrderDao.searchById(dto.getTransferOrderId());
		if (transferOrderModel == null) {
			result.put("code", "01");
			result.put("message", "该调拨订单不存在！");
			return result;
		}
		//校验该调拨订单是否已有存在调拨出库单
		//查询出库单是否存在
		TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
		transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());
		transferOutDepotModel = transferOutDepotDao.searchByModel(transferOutDepotModel);
		if (transferOutDepotModel != null){
			result.put("code", "01");
			result.put("message", "该调拨订单存在对应的出库单，不允许重复出库!");
			return result;
		}
		OrderExternalCodeModel transferOutDepotExistModel = new OrderExternalCodeModel();
		transferOutDepotExistModel.setExternalCode(transferOrderModel.getCode());
		transferOutDepotExistModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
		try {
			orderExternalCodeDao.save(transferOutDepotExistModel);
		} catch (Exception e) {
			LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			throw new RuntimeException("该调拨订单存在对应的出库单，不允许重复出库!");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//根据仓库id到mongoDB中查询调入仓库信息
		Map<String, Object> outDepotInfoMap = new HashMap<>();
		outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());//调入仓库id
		DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);

		//事业部
		Map<String, Object> buMap = new HashMap<>();
		buMap.put("merchantId",user.getMerchantId());
		buMap.put("buId", transferOrderModel.getBuId());
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
		if (merchantBuRelMongo == null) {
			throw new RuntimeException("该调拨订单下的公司事业部不存在!");
		}

		TransferOutDepotModel tModel = new TransferOutDepotModel();
		tModel.setTransferOrderId(transferOrderModel.getId());//调拨订单ID
		tModel.setTransferOrderCode(transferOrderModel.getCode());//调拨订单编号
		tModel.setMerchantId(transferOrderModel.getMerchantId());
		tModel.setMerchantName(transferOrderModel.getMerchantName());//商家名称
		tModel.setContractNo(transferOrderModel.getContractNo());//合同号
		tModel.setInDepotId(transferOrderModel.getInDepotId());//调入仓库id
		tModel.setInDepotName(transferOrderModel.getInDepotName());//调入仓库名称
		tModel.setOutDepotId(transferOrderModel.getOutDepotId());//调出仓库id
		tModel.setOutDepotName(transferOrderModel.getOutDepotName());//调出仓库名称
		tModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
		tModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户名称
		tModel.setLbxNo(transferOrderModel.getLbxNo());//LBX号
		tModel.setTransferDate(TimeUtils.parseDay(dto.getTransferOutDate()));//调出时间
		tModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);//CKZ("027","出库中"), //状态  '状态 015.待出仓   ,016.已出仓 027出库中',
		tModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK));//调拨出单号(对应调拨出库接口的调拨单号)
		tModel.setBuId(transferOrderModel.getBuId());
		tModel.setBuName(transferOrderModel.getBuName());
		tModel.setCreateName(user.getName());
		tModel.setCreater(user.getId());
		transferOutDepotDao.save(tModel);

		List<TransferOutDepotItemModel> itemModels = new ArrayList<>();//新增表体明细
		List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();//释放冻结商品列表
		List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//加库存商品
		List<TransferOutDepotItemDTO> goodsList = dto.getGoodsList();
		for (TransferOutDepotItemDTO itemDTO : goodsList) {

			if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfoMongo.getType())) {
				itemDTO.setTallyingUnit(null);
			}

			Map<String, Object> mParam = new HashMap<>();
			mParam.put("merchandiseId", itemDTO.getOutGoodsId());
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(mParam);
			TransferOutDepotItemModel itemModel = new TransferOutDepotItemModel();
			itemModel.setTransferDepotId(tModel.getId());//调拨出库ID
			itemModel.setOutGoodsId(itemDTO.getOutGoodsId());// 调出商品id
			itemModel.setOutGoodsCode(merchandise.getGoodsCode());//调出商品编码
			itemModel.setOutGoodsName(merchandise.getName());//调出商品名称
			itemModel.setOutGoodsNo(itemDTO.getOutGoodsNo());//调出商品货号
			itemModel.setTransferNum(itemDTO.getTransferNum());//好品数量
			itemModel.setWornNum(itemDTO.getWornNum());//坏品数量
			itemModel.setBarcode(itemDTO.getBarcode());
			itemModel.setOutCommbarcode(merchandise.getCommbarcode());
			//出库仓库若为批次效期强校验，则批次效期必填
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
				if (StringUtils.isEmpty(itemDTO.getTransferBatchNo())) {
					throw new RuntimeException("出库仓库为批次效期强校验，批次不能为空");
				}

				if (StringUtils.isEmpty(itemDTO.getProductionDateStr())) {
					throw new RuntimeException("出库仓库为批次效期强校验，生产日期不能为空");
				}

				if (StringUtils.isEmpty(itemDTO.getOverdueDateStr())) {
					throw new RuntimeException("出库仓库为批次效期强校验，失效日期不能为空");
				}
			}
			itemModel.setTransferBatchNo(itemDTO.getTransferBatchNo());
			if (!StringUtils.isEmpty(itemDTO.getProductionDateStr())) {
				itemModel.setProductionDate(sdf.parse(itemDTO.getProductionDateStr()));
			}
			if (!StringUtils.isEmpty(itemDTO.getOverdueDateStr())) {
				itemModel.setOverdueDate(sdf.parse(itemDTO.getOverdueDateStr()));
			}

			itemModels.add(itemModel);

			if (itemModel.getTransferNum() != null && itemModel.getTransferNum().intValue() > 0) {
				//拼装减库存商品
				InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();

				good.setGoodsId(String.valueOf(itemModel.getOutGoodsId()));
				good.setGoodsName(itemModel.getOutGoodsName());
				good.setGoodsNo(itemModel.getOutGoodsNo());
				good.setBarcode(itemModel.getBarcode());
				good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
				good.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0 是 1 否）
				good.setNum(itemModel.getTransferNum());
				good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
				good.setBatchNo(itemDTO.getTransferBatchNo()); //批次
				good.setProductionDate(itemDTO.getProductionDateStr()); //生产日期
				good.setOverdueDate(itemDTO.getOverdueDateStr()); //失效日期
				good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				String unit = itemDTO.getTallyingUnit();
				if (org.apache.commons.lang.StringUtils.isNotBlank(unit)) {
					switch (unit) {
						case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
						case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
						default: unit = DERP.INVENTORY_UNIT_2; break;
					}
				}
				good.setUnit(unit);
				subGoodsList.add(good);
			}
			if (itemModel.getWornNum() != null && itemModel.getWornNum().intValue() > 0) {
				//拼装减库存商品
				InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();

				good.setGoodsId(String.valueOf(itemModel.getOutGoodsId()));
				good.setGoodsName(itemModel.getOutGoodsName());
				good.setGoodsNo(itemModel.getOutGoodsNo());
				good.setBarcode(itemModel.getBarcode());

				good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
				good.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0 是 1 否）
				good.setNum(itemModel.getWornNum());
				good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
				good.setBatchNo(itemDTO.getTransferBatchNo()); //批次
				good.setProductionDate(itemDTO.getProductionDateStr()); //生产日期
				good.setOverdueDate(itemDTO.getOverdueDateStr()); //失效日期
				good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				String unit = itemDTO.getTallyingUnit();
				if (org.apache.commons.lang.StringUtils.isNotBlank(unit)) {
					switch (unit) {
						case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
						case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
						default: unit = DERP.INVENTORY_UNIT_2; break;
					}
				}
				good.setUnit(unit);
				subGoodsList.add(good);
			}
		}

		//保存表体
		for (TransferOutDepotItemModel itemModel : itemModels) {
			transferOutDepotItemDao.save(itemModel);
		}

		//释放冻结商品
		TransferOrderItemModel orderItemModel = new TransferOrderItemModel();
		orderItemModel.setTransferOrderId(transferOrderModel.getId());
		List<TransferOrderItemModel> itemList = transferOrderItemDao.list(orderItemModel);
		for (TransferOrderItemModel transferOrderItemModel : itemList) {
			InventoryFreezeGoodsListJson fgood = new InventoryFreezeGoodsListJson();
			fgood.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
			fgood.setGoodsName(transferOrderItemModel.getOutGoodsName());
			fgood.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
			fgood.setDivergenceDate(dto.getTransferOutDate());
			fgood.setNum(transferOrderItemModel.getTransferNum());
			if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)){
				if(transferOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)){
					fgood.setUnit(DERP.INVENTORY_UNIT_0);// 单位 字符串 0 托盘 1箱 2 件
				}else if(transferOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)){
					fgood.setUnit(DERP.INVENTORY_UNIT_1);
				}else{
					fgood.setUnit(DERP.INVENTORY_UNIT_2);
				}
			}
			freezeGoodList.add(fgood);
		}

		//释放冻结库存
		InventoryFreezeRootJson freezeAddLower = new InventoryFreezeRootJson();
		freezeAddLower.setMerchantId(String.valueOf(user.getMerchantId()));
		freezeAddLower.setMerchantName(user.getMerchantName());
		freezeAddLower.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
		freezeAddLower.setDepotName(outDepotInfoMongo.getName());
		freezeAddLower.setOrderNo(tModel.getCode());// 解冻取调拨出库单号
		freezeAddLower.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		freezeAddLower.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		freezeAddLower.setSourceDate(TimeUtils.formatFullTime());
		freezeAddLower.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
		freezeAddLower.setBusinessNo(transferOrderModel.getCode());
		freezeAddLower.setGoodsList(freezeGoodList);
		JSONObject jsonFree = JSONObject.fromObject(freezeAddLower);

		//拼装减库存接口参数
		InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
		inventoryRoot.setMerchantId(String.valueOf(tModel.getMerchantId()));
		inventoryRoot.setMerchantName(String.valueOf(tModel.getMerchantName()));
		inventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
		inventoryRoot.setDepotType(outDepotInfoMongo.getType());
		inventoryRoot.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
		inventoryRoot.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
		inventoryRoot.setDepotCode(outDepotInfoMongo.getCode());
		inventoryRoot.setDepotName(outDepotInfoMongo.getName());
		inventoryRoot.setOrderNo(tModel.getCode());
		inventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		inventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
		inventoryRoot.setOwnMonth(TimeUtils.formatMonth(tModel.getTransferDate()));
		inventoryRoot.setDivergenceDate(TimeUtils.format(tModel.getTransferDate(), "yyyy-MM-dd HH:mm:ss"));
		inventoryRoot.setBusinessNo(transferOrderModel.getCode());
		inventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
		inventoryRoot.setBuName(transferOrderModel.getBuName());
		inventoryRoot.setGoodsList(subGoodsList);
		//回调参数
		inventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
		inventoryRoot.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
		inventoryRoot.setCustomParam(new HashMap<String, Object>());
		//减库存
		JSONObject subjson = JSONObject.fromObject(inventoryRoot);
		// 把释放冻结存放到最下方防止已出定入出现异常接口报错
		rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		// 把减库存放到最下方防止已出定入出现异常接口报错
		rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		result.put("code", "00");
		result.put("message", "保存成功!");
		return result;
	}

	@Override
	public Map<String,Object> saveImportTransferOut(List<Map<String, String>> data, User user) throws Exception{
		List<ImportErrorMessage> errorList = new ArrayList<ImportErrorMessage>();//获取错误的信息
		int success = 0;//成功
		int pass = 0;//跳过
		int failure = 0;//失败
		//存储调拨出库表体信息
		Map<String, List<TransferOutDepotItemModel>> dataMap = new HashMap<String, List<TransferOutDepotItemModel>>();
		//存在的调拨订单信息
		Map<String, TransferOrderModel> existOrderMap = new HashMap<>();
		//判断相同调拨订单号对应的入库时间是否一致
		Map<String, String> outDepotDateMap = new HashMap<>();
		//相同订单号+商品货号导入数量总和
		Map<String, Integer> codeGoodsNoNumMap = new HashMap<>();

		//相同订单号+商品货号对应的调拨订单表体信息
		Map<String, List<TransferOrderItemModel>> codeGoodsNoItemsMap = new HashMap<>();

		//1. 必填项校验
		for (int i = 1; i <= data.size(); i++) {
			Map<String, String> map = data.get(i - 1);
			//存储调拨出库表体基本信息
			TransferOutDepotItemModel item=new TransferOutDepotItemModel();

			String transferOrderCode = map.get("调拨订单号");
			if (StringUtils.isEmpty(transferOrderCode)) {
				setErrorMsg(i, "调拨订单号不能为空", errorList);
				failure += 1;
				continue;
			}
			String outDepotDate = map.get("出库时间");
			if (StringUtils.isEmpty(outDepotDate)) {
				setErrorMsg(i, "出库时间不能为空", errorList);
				failure += 1;
				continue;
			}
			if (!isDate(outDepotDate.trim())) {
				setErrorMsg(i, "出库时间格式有误", errorList);
				failure += 1;
				continue;
			}
			String existOutDepotDate = outDepotDateMap.get(transferOrderCode);
			if (StringUtils.isNotBlank(existOutDepotDate) && !existOutDepotDate.equals(outDepotDate)) {
				setErrorMsg(i, "相同调拨订单号对应的出库时间不一致", errorList);
				failure += 1;
				continue;
			}
			if (StringUtils.isBlank(existOutDepotDate)) {//保存不同的出库时间
				outDepotDateMap.put(transferOrderCode, outDepotDate);
			}
			String goodsNo = map.get("调出商品货号");
			if(checkIsNullOrNot(i, goodsNo, "调出商品货号不能为空", errorList)) {
				failure += 1;
				continue;
			}
			item.setOutGoodsNo(goodsNo.trim());

			Integer totalOutNum = 0;
			String outGoodsNum = map.get("调出好品数量");
			String outBadNum = map.get("调出坏品数量");
			if (StringUtils.isBlank(outGoodsNum)  && StringUtils.isBlank(outBadNum)) {
				setErrorMsg(i, "调出好品或坏品数量至少有一个值", errorList);
				failure += 1;
				continue;
			}
			if(StringUtils.isNotBlank(outGoodsNum)) {
				if (!isNumber(outGoodsNum)) {
					setErrorMsg(i, "调出好品数量非数字类型", errorList);
					failure += 1;
					continue;
				}
				totalOutNum += Integer.valueOf(outGoodsNum);
				item.setTransferNum(Integer.parseInt(outGoodsNum.trim()));
			}

			if(StringUtils.isNotBlank(outBadNum)) {
				if (!isNumber(outBadNum)) {
					setErrorMsg(i, "调出坏品数量非数字类型", errorList);
					failure += 1;
					continue;
				}
				totalOutNum += Integer.valueOf(outBadNum);
				item.setWornNum(Integer.parseInt(outBadNum.trim()));
			}

			//校验调拨订单是否存在,key-调拨订单号 value-调拨实体
			TransferOrderModel model = existOrderMap.get(transferOrderCode);
			if (model == null) {
				TransferOrderModel transferOrderModel = new TransferOrderModel();
				transferOrderModel.setCode(transferOrderCode);
				model = transferOrderDao.searchByModel(transferOrderModel);

				if (model == null) {
					setErrorMsg(i, "调拨订单号不存在", errorList);
					failure += 1;
					continue;
				}
				if(DERP_ORDER.TRANSFERORDER_STATUS_013.equals(model.getStatus())){
					setErrorMsg(i, "调拨订单状态为未提交", errorList);
					failure += 1;
					continue;
				}
				existOrderMap.put(transferOrderCode, model);
			}


			/**判断商品货号是否存在*/
			String codeGoodsKey = transferOrderCode + "_" + goodsNo;
			List<TransferOrderItemModel> transferOrderItemModels = codeGoodsNoItemsMap.get(codeGoodsKey);
			if (transferOrderItemModels == null) {
				TransferOrderItemModel transferOrderItemModel = new TransferOrderItemModel();
				transferOrderItemModel.setTransferOrderId(model.getId());
				transferOrderItemModel.setOutGoodsNo(goodsNo);
				transferOrderItemModels = transferOrderItemDao.list(transferOrderItemModel);
			}

			if (transferOrderItemModels == null || transferOrderItemModels.isEmpty()) {
				setErrorMsg(i, "调拨订单：" + transferOrderCode + "没有找到对应的调出商品货号：" + goodsNo , errorList);
				failure += 1;
				continue;
			}

			//判断调拨订单对应的出库单是否存在
			TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
			transferOutDepotModel.setTransferOrderId(model.getId());
			TransferOutDepotModel existModel = transferOutDepotDao.searchByModel(transferOutDepotModel);
			if (existModel != null) {
				setErrorMsg(i, "调拨订单号：" + transferOrderCode + "对应的调拨出库单已经存在", errorList);
				failure += 1;
				continue;
			}

			/**出库时间必须大于关账日期/月结日期*/
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setDepotId(model.getOutDepotId());
			closeAccountsMongo.setBuId(model.getBuId());
			String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth="";
			if (org.apache.commons.lang.StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (org.apache.commons.lang.StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 出库时间
				if (TimeUtils.parseDay(outDepotDate).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(i, "出库时间必须大于关账日期/月结日期", errorList);
					failure += 1;
					continue;
				}
			}

			//根据仓库id到mongoDB中查询调出仓库信息
			Map<String, Object> outDepotInfoMap = new HashMap<>();
			outDepotInfoMap.put("depotId", model.getOutDepotId());//调出仓库id
			DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);

			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfoMongo.getType())) {
				item.setTallyingUnit(model.getTallyingUnit());
			}

			//批次号
			String batchNo = map.get("批次号");
			//生产日期
			String productionDate = map.get("生产日期");
			//失效日期
			String overdueDate = map.get("失效日期");
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
				if(checkIsNullOrNot(i, batchNo, "出库仓库为批次强校验，批次不能为空!", errorList)) {
					failure += 1;
					continue;
				}
				batchNo = batchNo.trim() ;
				item.setTransferBatchNo(batchNo);
				if(checkIsNullOrNot(i, productionDate, "出库仓库为批次强校验，生产日期不能为空!", errorList)) {
					failure += 1;
					continue;
				}
				if(checkIsNullOrNot(i, overdueDate, "出库仓库为批次强校验，失效日期不能为空!", errorList)) {
					failure += 1;
					continue;
				}
				overdueDate = overdueDate.trim() ;
			}
			if (StringUtils.isNotBlank(productionDate)) {
				if(!isDate(productionDate)) {
					setErrorMsg(i, "生产日期格式有误", errorList);
					failure += 1;
					continue;
				}
				item.setProductionDate(formatDate(productionDate));
			}
			if (StringUtils.isNotBlank(overdueDate)) {
				if(!isDate(overdueDate)) {
					setErrorMsg(i, "失效日期格式有误", errorList);
					failure += 1;
					continue;
				}
				item.setOverdueDate(formatDate(overdueDate));
			}
			if (StringUtils.isNotBlank(batchNo)) {
				batchNo = batchNo.trim() ;
				item.setTransferBatchNo(batchNo);
			}
			//如果调拨单号相同则添加调拨出库表体 key-调拨订单号 value-调拨出库表体实体
			List<TransferOutDepotItemModel> transferInDepotItemModels = new ArrayList<>();
			transferInDepotItemModels.add(item);
			if (dataMap.containsKey(transferOrderCode)) {
				transferInDepotItemModels.addAll(dataMap.get(transferOrderCode));
			}
			dataMap.put(transferOrderCode, transferInDepotItemModels);

			//2. 调拨订单号+调出商品货号出库总量（好品数量+坏品数量）需小于或等于调拨订单对应商品的调出总量
			if (codeGoodsNoNumMap.containsKey(codeGoodsKey)) {
				totalOutNum += codeGoodsNoNumMap.get(codeGoodsKey);
			}
			codeGoodsNoNumMap.put(codeGoodsKey, totalOutNum);
			Integer orderOutDepotNum = 0;
			for (TransferOrderItemModel orderItemModel : transferOrderItemModels) {
				orderOutDepotNum += orderItemModel.getTransferNum();
			}
			if (totalOutNum > orderOutDepotNum) {
				setErrorMsg(i, "调拨订单号：" + transferOrderCode + "调出商品货号：" + goodsNo + "的入库量需小于或等于调拨订单对应商品的出库量" , errorList);
				failure += 1;
				continue;
			}
		}

		//2.添加调拨出库订单以及调拨出库表体
		if(failure==0){
			for (String transferOrderCode : existOrderMap.keySet()) {
				OrderExternalCodeModel transferInDepotExistModel = new OrderExternalCodeModel();
				transferInDepotExistModel.setExternalCode(transferOrderCode);
				transferInDepotExistModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
				try {
					orderExternalCodeDao.save(transferInDepotExistModel);
				} catch (Exception e) {
					LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderCode + "  保存失败");
					throw new RuntimeException("该调拨订单存在对应的出库单，不允许重复出库!");
				}
				//根据调拨订单号获取调拨订单实体
				TransferOrderModel transferOrder = existOrderMap.get(transferOrderCode);
				//根据调拨订单号获取出库时间
				String inDepotDate = outDepotDateMap.get(transferOrderCode);
				//添加调拨出库订单
				TransferOutDepotModel tModel = new TransferOutDepotModel();
				tModel.setTransferOrderId(transferOrder.getId());//调拨订单ID
				tModel.setTransferOrderCode(transferOrder.getCode());//调拨订单编号
				tModel.setMerchantId(transferOrder.getMerchantId());
				tModel.setMerchantName(transferOrder.getMerchantName());//商家名称
				tModel.setContractNo(transferOrder.getContractNo());//合同号
				tModel.setInDepotId(transferOrder.getInDepotId());//调入仓库id
				tModel.setInDepotName(transferOrder.getInDepotName());//调入仓库名称
				tModel.setOutDepotId(transferOrder.getOutDepotId());//调出仓库id
				tModel.setOutDepotName(transferOrder.getOutDepotName());//调出仓库名称
				tModel.setInCustomerId(transferOrder.getInCustomerId());//调入客户id
				tModel.setInCustomerName(transferOrder.getInCustomerName());//调入客户名称
				tModel.setLbxNo(transferOrder.getLbxNo());//LBX号
				tModel.setBuName(transferOrder.getBuName());//事业部名称transferOutDepot_dataSourceList
				tModel.setBuId(transferOrder.getBuId());//事业部id
				tModel.setCreateName(user.getName());//创建人名称
				tModel.setCreater(user.getId());//创建人id
				tModel.setTransferDate(TimeUtils.parseFullTime(inDepotDate+ " 00:00:00"));//调出时间
				tModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_015);// 015-待出仓 016-已出仓 006-已删除 027-出库中
				tModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK));//调拨出单号(对应调拨出库接口的调拨单号)
				tModel.setDataSource(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_1);//单据来源 1-手工导入 2-接口生成
				//新增调拨出库
				Long id =transferOutDepotDao.save(tModel);

				//合并相同调入商品货号+批次+效期
				Map<String, TransferOutDepotItemModel> transferOutDepotItemModelMap = new HashMap<>();
				List<TransferOutDepotItemModel> transferOutDepotItemModels = dataMap.get(transferOrderCode);

				for (TransferOutDepotItemModel itemModel : transferOutDepotItemModels) {
					String key = itemModel.getOutGoodsNo() + "_" + itemModel.getTransferBatchNo() + "_" +
							itemModel.getProductionDate() + "_" + itemModel.getOverdueDate();

					TransferOutDepotItemModel existItemModel = transferOutDepotItemModelMap.get(key);
					if (existItemModel != null) {
						Integer goodsNum = itemModel.getTransferNum() == null ? 0 : itemModel.getTransferNum();
						Integer wornNum = itemModel.getWornNum() == null ? 0 : itemModel.getWornNum();
						Integer existGoodsNum = existItemModel.getTransferNum() == null ? 0 : existItemModel.getTransferNum();
						Integer existWornNum = existItemModel.getWornNum() == null ? 0 : existItemModel.getWornNum();
						itemModel.setTransferNum(goodsNum + existGoodsNum);//调拨数量(正常品数量)
						itemModel.setWornNum(wornNum + existWornNum);//坏货数量
					}
					transferOutDepotItemModelMap.put(key, itemModel);
				}

				//生成调拨入库单表体
				for (String key : transferOutDepotItemModelMap.keySet()) {
					TransferOrderModel orderModel=existOrderMap.get(transferOrderCode);
					TransferOutDepotItemModel transferOutDepotItemModel = transferOutDepotItemModelMap.get(key);

					transferOutDepotItemModel.setTransferDepotId(id);//调拨出库ID
					transferOutDepotItemModel.setOutGoodsNo(transferOutDepotItemModel.getOutGoodsNo());//调出商品货号
					transferOutDepotItemModel.setCreater(user.getId());//创建人

					TransferOrderItemModel itemOrder=new TransferOrderItemModel();
					itemOrder.setTransferOrderId(orderModel.getId());
					itemOrder.setOutGoodsNo(transferOutDepotItemModel.getOutGoodsNo());
					List<TransferOrderItemModel> orderItemList=transferOrderItemDao.list(itemOrder);
					for(TransferOrderItemModel goods:orderItemList) {
						if (goods.getOutGoodsNo().equals(transferOutDepotItemModel.getOutGoodsNo())) {
							transferOutDepotItemModel.setOutGoodsId(goods.getOutGoodsId());//调出商品id
							transferOutDepotItemModel.setOutGoodsCode(goods.getOutGoodsCode());//调出商品编码
							transferOutDepotItemModel.setOutGoodsName(goods.getOutGoodsName());//调出商品名称
							transferOutDepotItemModel.setOutCommbarcode(goods.getOutCommbarcode());//调出商品标准条码
							break;
						}
					}

					transferOutDepotItemDao.save(transferOutDepotItemModel);
				}
			}
			success = data.size() ;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", errorList);
		return map;
	}

	@Override
	public Map<String, Object> auditTransferOutDepot(List<Long> ids, User user) throws Exception {
		Map<String, Object> result = new HashMap<>();
		StringBuffer failureMsg = new StringBuffer();
		Integer failure = 0;

		List<TransferOutDepotModel> transferOutDepotModels = new ArrayList<>();
		Map<String, List<TransferOutDepotItemModel>> itemModelMap = new HashMap<>();
		Map<String, DepotInfoMongo> depotMap = new HashMap<>();
		for (Long id : ids) {
			TransferOutDepotModel model = transferOutDepotDao.searchById(id);

			if (!DERP_ORDER.TRANSFEROUTDEPOT_STATUS_015.equals(model.getStatus())) {
				failureMsg.append("调拨出库单单号:" + model.getCode() + "不是“待出仓”状态!\n");
				failure++;
				continue;
			}

			if (!DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_1.equals(model.getDataSource())) {
				failureMsg.append("调拨出库单单号:" + model.getCode() + "不是“手工导入”!\n");
				failure++;
				continue;
			}

			/**出库时间必须大于关账日期/月结日期*/
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setDepotId(model.getOutDepotId());
			closeAccountsMongo.setBuId(model.getBuId());
			String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth="";
			if (org.apache.commons.lang.StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (org.apache.commons.lang.StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 出库时间
				if (model.getTransferDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					failureMsg.append("调拨出库单单号:" + model.getCode() + "出库时间必须大于关账日期/月结日期”!\n");
					failure++;
					continue;
				}
			}

			// 根据仓库id获取仓库信息
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", model.getOutDepotId());
			DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(depotInfo_params);

			/**调拨入库表体信息*/
			TransferOutDepotItemModel itemModel = new TransferOutDepotItemModel();
			itemModel.setTransferDepotId(model.getId());
			List<TransferOutDepotItemModel> transferOutDepotItemModels = transferOutDepotItemDao.list(itemModel);

			/**出库仓库为批次强校验，批次效期必填*/
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
				for (TransferOutDepotItemModel transferOutDepotItemModel : transferOutDepotItemModels) {
					if (org.apache.commons.lang.StringUtils.isBlank(transferOutDepotItemModel.getTransferBatchNo())) {
						failureMsg.append("调拨出库单单号:" + model.getCode() + "入库仓库为批次强校验或者入库强校验时，批次不能为空!");
						failure++;
						break;
					}

					if (transferOutDepotItemModel.getProductionDate() == null) {
						failureMsg.append("调拨出库单单号:" + model.getCode() + "入库仓库为批次强校验或者入库强校验时，生产日期不能为空!");
						failure++;
						break;
					}

					if (transferOutDepotItemModel.getOverdueDate() == null) {
						failureMsg.append("调拨出库单单号:" + model.getCode() + "入库仓库为批次强校验或者入库强校验时，失效日期不能为空!");
						failure++;
						break;
					}
				}
			}
			transferOutDepotModels.add(model);
			itemModelMap.put(model.getCode(), transferOutDepotItemModels);
			depotMap.put(model.getCode(), outDepotInfoMongo);
		}

		if (failure > 0) {
			result.put("failure", failure);
			result.put("failureMsg", failureMsg);
			return result;
		}

		for (TransferOutDepotModel model : transferOutDepotModels) {
			/**推送库存加减接口*/
			List<TransferOutDepotItemModel> transferOutDepotItemModels = itemModelMap.get(model.getCode());
			TransferOrderModel transferOrderModel = transferOrderDao.searchById(model.getTransferOrderId());
			DepotInfoMongo outDepotInfoMongo = depotMap.get(model.getCode());
			// 加库存商品
			List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();

			// 释放冻结商品列表
			List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();
			//释放冻结商品
			TransferOrderItemModel orderItemModel = new TransferOrderItemModel();
			orderItemModel.setTransferOrderId(transferOrderModel.getId());
			List<TransferOrderItemModel> itemList = transferOrderItemDao.list(orderItemModel);
			for (TransferOrderItemModel transferOrderItemModel : itemList) {
				InventoryFreezeGoodsListJson fgood = new InventoryFreezeGoodsListJson();
				fgood.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
				fgood.setGoodsName(transferOrderItemModel.getOutGoodsName());
				fgood.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
				fgood.setDivergenceDate(TimeUtils.format(model.getTransferDate(), "yyyy-MM-dd"));
				fgood.setNum(transferOrderItemModel.getTransferNum());
				if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)){
					if(transferOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)){
						fgood.setUnit(DERP.INVENTORY_UNIT_0);// 单位 字符串 0 托盘 1箱 2 件
					}else if(transferOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)){
						fgood.setUnit(DERP.INVENTORY_UNIT_1);
					}else{
						fgood.setUnit(DERP.INVENTORY_UNIT_2);
					}
				}
				freezeGoodList.add(fgood);
			}

			//释放冻结库存
			InventoryFreezeRootJson freezeAddLower = new InventoryFreezeRootJson();
			freezeAddLower.setMerchantId(String.valueOf(user.getMerchantId()));
			freezeAddLower.setMerchantName(user.getMerchantName());
			freezeAddLower.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
			freezeAddLower.setDepotName(outDepotInfoMongo.getName());
			freezeAddLower.setOrderNo(model.getCode());// 解冻取调拨出库单号
			freezeAddLower.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
			freezeAddLower.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
			freezeAddLower.setSourceDate(TimeUtils.formatFullTime());
			freezeAddLower.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
			freezeAddLower.setBusinessNo(transferOrderModel.getCode());
			freezeAddLower.setGoodsList(freezeGoodList);
			JSONObject jsonFree = JSONObject.fromObject(freezeAddLower);

			/**推送加减库存接口*/
			for (TransferOutDepotItemModel outDepotItemModel : transferOutDepotItemModels) {

				if (outDepotItemModel.getTransferNum() != null && outDepotItemModel.getTransferNum().intValue() > 0){
					// 拼装加库存商品
					InvetAddOrSubtractGoodsListJson subGood = new InvetAddOrSubtractGoodsListJson();

					subGood.setGoodsId(String.valueOf(outDepotItemModel.getOutGoodsId()));
					subGood.setGoodsName(outDepotItemModel.getOutGoodsName());
					subGood.setGoodsNo(outDepotItemModel.getOutGoodsNo());
					subGood.setBarcode(outDepotItemModel.getBarcode());
					subGood.setProductionDate(TimeUtils.formatDay(outDepotItemModel.getProductionDate()));
					subGood.setOverdueDate(TimeUtils.formatDay(outDepotItemModel.getOverdueDate()));
					subGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
					subGood.setNum(outDepotItemModel.getTransferNum());
					subGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
					String unit = outDepotItemModel.getTallyingUnit();
					if (org.apache.commons.lang.StringUtils.isNotBlank(unit)) {
						switch (unit) {
							case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
							case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
							default: unit = DERP.INVENTORY_UNIT_2; break;
						}
					}
					subGood.setUnit(unit);
					subGood.setIsExpire(DERP.ISEXPIRE_1);
					if (outDepotItemModel.getOverdueDate() != null) {
						subGood.setIsExpire(TimeUtils.isNotIsExpire(new Timestamp(outDepotItemModel.getOverdueDate().getTime())));
					}
					subGood.setBatchNo(outDepotItemModel.getTransferBatchNo());
					subGood.setProductionDate(TimeUtils.formatDay(outDepotItemModel.getProductionDate()));
					subGood.setOverdueDate(TimeUtils.formatDay(outDepotItemModel.getOverdueDate()));
					subGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					subGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					subGoodsList.add(subGood);
				}

				if (outDepotItemModel.getWornNum() != null && outDepotItemModel.getWornNum().intValue() > 0){
					// 拼装加库存商品
					InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();

					aSGood.setGoodsId(String.valueOf(outDepotItemModel.getOutGoodsId()));
					aSGood.setGoodsName(outDepotItemModel.getOutGoodsName());
					aSGood.setGoodsNo(outDepotItemModel.getOutGoodsNo());
					aSGood.setBarcode(outDepotItemModel.getBarcode());
					aSGood.setProductionDate(TimeUtils.formatDay(outDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(outDepotItemModel.getOverdueDate()));
					aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);// 商品分类 （0 好品 1坏品） 字符串
					aSGood.setNum(outDepotItemModel.getWornNum());
					aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 操作类型（调增\调减） 字符串 0 调减 1调增
					String unit = outDepotItemModel.getTallyingUnit();
					if (org.apache.commons.lang.StringUtils.isNotBlank(unit)) {
						switch (unit) {
							case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
							case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
							default: unit = DERP.INVENTORY_UNIT_2; break;
						}
					}
					aSGood.setUnit(unit);
					aSGood.setIsExpire(DERP.ISEXPIRE_1);
					aSGood.setBatchNo(outDepotItemModel.getTransferBatchNo());
					aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());

					if (outDepotItemModel.getOverdueDate() != null) {
						aSGood.setIsExpire(TimeUtils.isNotIsExpire(new Timestamp(outDepotItemModel.getOverdueDate().getTime())));
					}
					aSGood.setProductionDate(TimeUtils.formatDay(outDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(outDepotItemModel.getOverdueDate()));
					subGoodsList.add(aSGood);
				}
			}
			// 拼装加库存接口参数
			InvetAddOrSubtractRootJson subInventoryRoot = new InvetAddOrSubtractRootJson();
			subInventoryRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
			subInventoryRoot.setMerchantName(transferOrderModel.getMerchantName());
			subInventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
			subInventoryRoot.setDepotType(outDepotInfoMongo.getType());
			subInventoryRoot.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
			subInventoryRoot.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
			subInventoryRoot.setDepotCode(outDepotInfoMongo.getCode());
			subInventoryRoot.setDepotName(outDepotInfoMongo.getName());
			subInventoryRoot.setOrderNo(model.getCode());
			subInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009);
			subInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011);
			subInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
			subInventoryRoot.setOwnMonth(TimeUtils.formatMonth(model.getTransferDate()));
			subInventoryRoot.setDivergenceDate(TimeUtils.format(model.getTransferDate(), "yyyy-MM-dd HH:mm:ss"));
			subInventoryRoot.setBusinessNo(transferOrderModel.getCode());
			subInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
			subInventoryRoot.setBuName(transferOrderModel.getBuName());
			subInventoryRoot.setGoodsList(subGoodsList);
			//回调参数
			subInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
			subInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
			subInventoryRoot.setCustomParam(new HashMap<String, Object>());

			// 把释放冻结存放到最下方防止已出定入出现异常接口报错
			rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			// 加库存
			JSONObject subjson = JSONObject.fromObject(subInventoryRoot);
			rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

			//更新状态为“出仓中”
			TransferOutDepotModel updateModel = new TransferOutDepotModel();
			updateModel.setId(model.getId());
			updateModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);
			transferOutDepotDao.modify(updateModel);
		}

		result.put("failure", failure);
		result.put("success", ids.size()-failure);
		result.put("failureMsg", failureMsg);
		return result;
	}

	@Override
	public List<Map<String, Object>> getItemSumByIds(List<Long> ids, String topidealCode) throws Exception {
		List<Map<String, Object>> items = transferOutDepotItemDao.getItemSumByIds(ids);
		return items;
	}

	/**
	 * 字符串转日期
	 */
	private Date formatDate(String date) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		if (!p.matcher(date).matches()) {
			return sdf2.parse(date);
		}
		return sdf1.parse(date);
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
	/**
	 * 判断是否是日期格式（yyyy-MM-dd、yyyy/MM/dd）
	 * @param date
	 * @return
	 */
	private boolean isDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		if (!p.matcher(date).matches()) {
			Pattern pattern = Pattern.compile("\\d{4}\\/\\d{1,2}\\/\\d{1,2}");
			return pattern.matcher(date).matches();
		}
		return true;
	}

	/**
	 * 判断是否是数值
	 * @param str
	 * @return
	 */
	private boolean isNumber(String str) {
		//采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
		//可以判断正负、整数小数

		boolean isInt = Pattern.compile("^-?[1-9]\\d*$").matcher(str).find();
		boolean isDouble = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$").matcher(str).find();

		return isInt || isDouble;

	}


	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index, String content, String msg, List<ImportErrorMessage> resultList) {
		if (org.apache.commons.lang.StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true;
		} else {
			return false;
		}

	}

}
