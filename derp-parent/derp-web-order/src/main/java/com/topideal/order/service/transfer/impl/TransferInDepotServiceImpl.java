package com.topideal.order.service.transfer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.transfer.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.transfer.TransferInDepotService;
import com.topideal.order.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 调拨入库service实现类
 * @author yucaifu
 */
@Service
public class TransferInDepotServiceImpl implements TransferInDepotService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TransferInDepotServiceImpl.class);

	//调拨入库表
	@Autowired
	private TransferInDepotDao transferInDepotDao;
	
	//调拨入库详情表
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao;
	@Autowired
	private TransferOrderDao transferOrderDao;
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
    private TransferOrderItemDao transferOrderItemDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	/**
	 * 分页查询
	 * */
    public TransferInDepotDTO searchByPage(TransferInDepotDTO dto, User user) throws SQLException{
        List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels == null || userBuRels.size() == 0) {
			dto.setTotal(0);
			dto.setList(new ArrayList());
			return dto;
		}
		dto.setUserBuList(userBuRels);
        return transferInDepotDao.searchDTOByPage(dto);
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TransferInDepotDTO  searchById(Long id)throws SQLException {
        return transferInDepotDao.searchDTOById(id);
    }
    
    public Integer listForCount(TransferInDepotDTO dto, User user){
		List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels.isEmpty()) {
			return 0;
		}
		dto.setUserBuList(userBuRels);
		return transferInDepotDao.listForCount(dto);
    }
	@Override
	public List<Map<String, Object>> listForMap(TransferInDepotDTO dto, User user) {
		List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels.isEmpty()) {
			return new ArrayList<>();
		}
		dto.setUserBuList(userBuRels);
	 	return transferInDepotDao.listForMap(dto);
	}

	@Override
	public List<Map<String, Object>> listForMapItem(TransferInDepotDTO dto, User user) {
		List<Long> userBuRels = userBuRelMongoDao.getBuListByUser(user.getId());
		if (userBuRels.isEmpty()) {
			return new ArrayList<>();
		}
		dto.setUserBuList(userBuRels);
		return transferInDepotDao.listForMapItem(dto);
	}

	@Override
	public Map<String, String> saveTransferInDepot(TransferInDepotDTO dto, User user) throws Exception {
    	Map<String, String> result = new HashMap<>();
    	//查询调拨订单
		TransferOrderModel transferOrderModel = transferOrderDao.searchById(dto.getTransferOrderId());
		if (transferOrderModel == null) {
			result.put("code", "01");
			result.put("message", "该调拨订单不存在！");
			return result;
		}

		//校验该调拨订单是否存在调拨出库单，若不存在则不能上架入库
		TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
		outDepotModel.setTransferOrderId(dto.getTransferOrderId());
		outDepotModel = transferOutDepotDao.getByModel(outDepotModel);
		if (outDepotModel == null) {
			result.put("code", "01");
			result.put("message", "该调拨订单还未出库，不允许上架入库!");
			return result;
		}

		//校验该调拨订单是否已有存在调拨入库单
		//查询入库单是否存在
		TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
		transferInDepotModel.setTransferOrderId(transferOrderModel.getId());
		transferInDepotModel = transferInDepotDao.searchByModel(transferInDepotModel);
		if (transferInDepotModel != null){
			result.put("code", "01");
			result.put("message", "该调拨订单存在对应的入库单，不允许重复入库!");
			return result;
		}
		OrderExternalCodeModel transferInDepotExistModel = new OrderExternalCodeModel();
		transferInDepotExistModel.setExternalCode(transferOrderModel.getCode());
		transferInDepotExistModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_4));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
		try {
			orderExternalCodeDao.save(transferInDepotExistModel);
		} catch (Exception e) {
			LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			throw new RuntimeException("该调拨订单存在对应的入库单，不允许重复入库!");
		}
		//根据仓库id到mongoDB中查询调入仓库信息
		Map<String, Object> inDepotInfoMap = new HashMap<>();
		inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());//调入仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);

		//事业部
		Map<String, Object> buMap = new HashMap<>();
		buMap.put("merchantId",user.getMerchantId());
		buMap.put("buId", transferOrderModel.getBuId());
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
		if (merchantBuRelMongo == null) {
			throw new RuntimeException("该调拨订单下的公司事业部不存在!");
		}

		List<TransferInDepotItemDTO> itemDTOs = dto.getGoodsList();
		List<TransferInDepotItemDTO> newItemDtos = new ArrayList<>();

		/**若入库仓库为批次强校验或者入库强校验时，根据上架入库信息上架入库；反之，回填卓志仓出库批次效期，默认先失效先入库原则*/
		if (!DERP_SYS.DEPOTINFO_BATCHVALIDATION_0.equals(inDepotInfoMongo.getBatchValidation())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (TransferInDepotItemDTO itemDTO : itemDTOs) {
				if (StringUtils.isBlank(itemDTO.getTransferBatchNo())) {
					throw new RuntimeException("入库仓库为批次强校验或者入库强校验时，批次不能为空!");
				}

				if (StringUtils.isBlank(itemDTO.getProductionDateStr())) {
					throw new RuntimeException("入库仓库为批次强校验或者入库强校验时，生产日期不能为空!");
				}

				if (StringUtils.isBlank(itemDTO.getOverdueDateStr())) {
					throw new RuntimeException("入库仓库为批次强校验或者入库强校验时，失效日期不能为空!");
				}

				TransferInDepotItemDTO newItem = new TransferInDepotItemDTO();
				BeanUtils.copyProperties(itemDTO, newItem);
				newItem.setProductionDate(sdf.parse(itemDTO.getProductionDateStr()));
				newItem.setOverdueDate(sdf.parse(itemDTO.getOverdueDateStr()));
				newItemDtos.add(newItem);
			}

		} else {

			//回填卓志仓出库批次效期，默认先失效先入库原则
			for (TransferInDepotItemDTO itemDTO : itemDTOs) {
				List<TransferOutDepotItemModel> outDepotItemModels = transferOutDepotItemDao.getItemListByTransferIdAndGoodsId(outDepotModel.getId(), itemDTO.getOutGoodsId());
				Integer inSurplusWornNum = itemDTO.getWornNum(); //依次扣减出库数量后剩余坏品数量
				Integer inSurplusGoodNum = itemDTO.getTransferNum();//依次扣减出库数量后剩余好品数量
				for (int i = 0; i < outDepotItemModels.size(); i++) {
					Integer outWornNum = outDepotItemModels.get(i).getWornNum();//调出坏品数量
					Integer outGoodNum = outDepotItemModels.get(i).getTransferNum();//调出好品数量
					Integer outNum = outWornNum + outGoodNum;
					// 1.如果调出商品的好品数量+坏品数量 >= 调入商品数量则直接取先失效的批次、效期
					// 2.如果是扣减批次的最后一个批次，则全部入库到该批次下
					if (outNum >= inSurplusWornNum+inSurplusGoodNum || i == outDepotItemModels.size()-1) {
						TransferInDepotItemDTO newItem = new TransferInDepotItemDTO();
						BeanUtils.copyProperties(itemDTO, newItem);
						newItem.setWornNum(inSurplusWornNum);
						newItem.setTransferNum(inSurplusGoodNum);
						newItem.setTransferBatchNo(outDepotItemModels.get(i).getTransferBatchNo());
						newItem.setProductionDate(outDepotItemModels.get(i).getProductionDate());
						newItem.setOverdueDate(outDepotItemModels.get(i).getOverdueDate());
						newItemDtos.add(newItem);
						break;
					} else {
						//如果调出商品的好品数量+坏品数量 < 调入商品数量则依次扣减先失效的批次、效期
						if (outNum >= inSurplusWornNum && outNum-inSurplusWornNum < inSurplusGoodNum) {
							TransferInDepotItemDTO newItem = new TransferInDepotItemDTO();
							BeanUtils.copyProperties(itemDTO, newItem);
							newItem.setWornNum(inSurplusWornNum);
							newItem.setTransferNum(outNum-inSurplusWornNum);
							newItem.setTransferBatchNo(outDepotItemModels.get(i).getTransferBatchNo());
							newItem.setProductionDate(outDepotItemModels.get(i).getProductionDate());
							newItem.setOverdueDate(outDepotItemModels.get(i).getOverdueDate());
							newItemDtos.add(newItem);
							inSurplusGoodNum = inSurplusGoodNum - (outNum-inSurplusWornNum);
							inSurplusWornNum = 0;
						} else if (outNum < inSurplusWornNum) { //如果调出商品的好品数量+坏品数量 < 调入商品坏品数量则依次扣减先失效的批次、效期
							TransferInDepotItemDTO newItem = new TransferInDepotItemDTO();
							BeanUtils.copyProperties(itemDTO, newItem);
							newItem.setWornNum(outNum);
							newItem.setTransferNum(0);
							newItem.setTransferBatchNo(outDepotItemModels.get(i).getTransferBatchNo());
							newItem.setProductionDate(outDepotItemModels.get(i).getProductionDate());
							newItem.setOverdueDate(outDepotItemModels.get(i).getOverdueDate());
							newItemDtos.add(newItem);
							inSurplusWornNum -= outNum;
						}
					}
				}
				//如果对应调出商品未出库
				if (outDepotItemModels == null || outDepotItemModels.size() == 0) {
					newItemDtos.add(itemDTO);
				}
			}
		}

		TransferInDepotModel inDepotModel = new TransferInDepotModel();
		inDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));
		inDepotModel.setTransferOrderId(dto.getTransferOrderId());
		inDepotModel.setTransferOrderCode(transferOrderModel.getCode());
		inDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028);
		inDepotModel.setMerchantId(user.getMerchantId());
		inDepotModel.setCreater(user.getId());
		inDepotModel.setCreateName(user.getName());
		inDepotModel.setTransferDate(TimeUtils.parseDay(dto.getTransferInDate()));
		inDepotModel.setInDepotId(transferOrderModel.getInDepotId());
		inDepotModel.setInDepotName(transferOrderModel.getInDepotName());
		inDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());
		inDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());
		inDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());
		inDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());
		inDepotModel.setMerchantId(transferOrderModel.getMerchantId());// 商家ID
		inDepotModel.setMerchantName(transferOrderModel.getMerchantName());// 商家名称
		inDepotModel.setContractNo(transferOrderModel.getContractNo());// 合同号
		inDepotModel.setBuId(transferOrderModel.getBuId());
		inDepotModel.setBuName(transferOrderModel.getBuName());
		transferInDepotDao.save(inDepotModel);

		List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();// 加库存商品
		//调拨入库单表体
		for (TransferInDepotItemDTO itemDTO : newItemDtos) {

			if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType())) {
				itemDTO.setTallyingUnit(null);
			}

			Map<String, Object> mParam = new HashMap<>();
			mParam.put("merchandiseId", itemDTO.getInGoodsId());
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(mParam);
			TransferInDepotItemModel transferInDepotItemModel = new TransferInDepotItemModel();
			transferInDepotItemModel.setTransferDepotId(inDepotModel.getId());// 调拨入库ID
			transferInDepotItemModel.setInGoodsId(itemDTO.getInGoodsId());// 调入商品id
			transferInDepotItemModel.setInGoodsName(merchandise.getName());// 调入商品名称
			transferInDepotItemModel.setInGoodsNo(itemDTO.getInGoodsNo());// 调入商品货号
			transferInDepotItemModel.setInGoodsCode(merchandise.getGoodsCode());// 调入商品编码
			transferInDepotItemModel.setInCommbarcode(merchandise.getCommbarcode());// 调入商品标准条码
			transferInDepotItemModel.setTransferNum(itemDTO.getTransferNum());// 调拨数量
			transferInDepotItemModel.setWornNum(itemDTO.getWornNum());// 调拨数量
			transferInDepotItemModel.setTallyingUnit(itemDTO.getTallyingUnit());// 理货单位
			transferInDepotItemModel.setBarcode(itemDTO.getBarcode());
			transferInDepotItemModel.setTransferBatchNo(itemDTO.getTransferBatchNo());
			transferInDepotItemModel.setOverdueDate(itemDTO.getOverdueDate());
			transferInDepotItemModel.setProductionDate(itemDTO.getProductionDate());
			transferInDepotItemDao.save(transferInDepotItemModel);

			if (itemDTO.getTransferNum() != null && itemDTO.getTransferNum().intValue() > 0){
				// 拼装加库存商品
				InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();

				aSGood.setGoodsId(String.valueOf(transferInDepotItemModel.getInGoodsId()));
				aSGood.setGoodsName(transferInDepotItemModel.getInGoodsName());
				aSGood.setGoodsNo(transferInDepotItemModel.getInGoodsNo());
				aSGood.setBarcode(transferInDepotItemModel.getBarcode());
				aSGood.setProductionDate(TimeUtils.formatDay(transferInDepotItemModel.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(transferInDepotItemModel.getOverdueDate()));
				aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);// 商品分类 （0 好品 1坏品） 字符串
				aSGood.setNum(transferInDepotItemModel.getTransferNum());
				aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 操作类型（调增\调减） 字符串 0 调减 1调增
				String unit = itemDTO.getTallyingUnit();
				if (StringUtils.isNotBlank(unit)) {
					switch (unit) {
						case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
						case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
						default: unit = DERP.INVENTORY_UNIT_2; break;
					}
				}
				aSGood.setUnit(unit);
				aSGood.setIsExpire(DERP.ISEXPIRE_1);
				aSGood.setBatchNo(itemDTO.getTransferBatchNo());
				aSGood.setProductionDate(TimeUtils.formatDay(itemDTO.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(itemDTO.getOverdueDate()));
				aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				subGoodsList.add(aSGood);
			}

			if (itemDTO.getWornNum() != null && itemDTO.getWornNum().intValue() > 0){
				// 拼装加库存商品
				InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();

				aSGood.setGoodsId(String.valueOf(transferInDepotItemModel.getInGoodsId()));
				aSGood.setGoodsName(transferInDepotItemModel.getInGoodsName());
				aSGood.setGoodsNo(transferInDepotItemModel.getInGoodsNo());
				aSGood.setBarcode(transferInDepotItemModel.getBarcode());
				aSGood.setProductionDate(TimeUtils.formatDay(transferInDepotItemModel.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(transferInDepotItemModel.getOverdueDate()));
				aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);// 商品分类 （0 好品 1坏品） 字符串
				aSGood.setNum(transferInDepotItemModel.getWornNum());
				aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 操作类型（调增\调减） 字符串 0 调减 1调增
				String unit = itemDTO.getTallyingUnit();
				if (StringUtils.isNotBlank(unit)) {
					switch (unit) {
						case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
						case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
						default: unit = DERP.INVENTORY_UNIT_2; break;
					}
				}
				aSGood.setUnit(unit);
				aSGood.setIsExpire(DERP.ISEXPIRE_1);
				aSGood.setBatchNo(itemDTO.getTransferBatchNo());
				aSGood.setProductionDate(TimeUtils.formatDay(itemDTO.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(itemDTO.getOverdueDate()));
				aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				subGoodsList.add(aSGood);
			}
		}
		// 拼装加库存接口参数
		InvetAddOrSubtractRootJson subInventoryRoot = new InvetAddOrSubtractRootJson();
		subInventoryRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
		subInventoryRoot.setMerchantName(transferOrderModel.getMerchantName());
		subInventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
		subInventoryRoot.setDepotType(inDepotInfoMongo.getType());
		subInventoryRoot.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
		subInventoryRoot.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
		subInventoryRoot.setDepotCode(inDepotInfoMongo.getCode());
		subInventoryRoot.setDepotName(inDepotInfoMongo.getName());
		subInventoryRoot.setOrderNo(inDepotModel.getCode());
		subInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		subInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010); //DBRK("0010","调拨入库"),
		subInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
		subInventoryRoot.setOwnMonth(TimeUtils.formatMonth(inDepotModel.getTransferDate()));
		subInventoryRoot.setDivergenceDate(TimeUtils.format(inDepotModel.getTransferDate(), "yyyy-MM-dd HH:mm:ss"));
		subInventoryRoot.setBusinessNo(transferOrderModel.getCode());
		subInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
		subInventoryRoot.setBuName(transferOrderModel.getBuName());
		subInventoryRoot.setGoodsList(subGoodsList);
		//回调参数
		subInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
		subInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
		subInventoryRoot.setCustomParam(new HashMap<String, Object>());
		// 加库存
		JSONObject subjson = JSONObject.fromObject(subInventoryRoot);
		rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		result.put("code", "00");
		result.put("message", "保存成功!");
		return result;
	}

	@Override
	public Map<String, Object> saveImportTransferIn(User user, List<Map<String, String>> data) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		//存在的调拨订单信息
		Map<String, TransferOrderModel> existOrderMap = new HashMap<>();

		//调拨订单归类
		Map<String, List<TransferInDepotItemModel>> transferInMap = new HashMap<>();

		//判断相同调拨订单号对应的入库时间是否一致
        Map<String, String> inDepotDateMap = new HashMap<>();

        //相同订单号+商品货号导入数量总和
		Map<String, Integer> codeGoodsNoNumMap = new HashMap<>();

		//相同订单号+商品货号对应的调拨订单表体信息
		Map<String, List<TransferOrderItemModel>> codeGoodsNoItemsMap = new HashMap<>();

		//1. 必填项校验
		for (int j = 1; j <= data.size(); j++) {
			Map<String, String> row = data.get(j - 1);
			TransferInDepotItemModel itemModel = new TransferInDepotItemModel();
			/**调拨订单号*/
			String transferOrderCode = row.get("调拨订单号");
			if(checkIsNullOrNot(j, transferOrderCode, "调拨订单号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			transferOrderCode = transferOrderCode.trim() ;

			/**入库时间*/
			String inDepotDate = row.get("入库时间");
			if(checkIsNullOrNot(j, inDepotDate, "入库时间不能为空", resultList)) {
				failure += 1;
				continue;
			}
			inDepotDate = inDepotDate.trim() ;
			if(!isDate(inDepotDate)) {
				setErrorMsg(j, "入库时间格式有误", resultList);
				failure += 1;
				continue;
			}

			String existInDepotDate = inDepotDateMap.get(transferOrderCode);
			if (StringUtils.isNotBlank(existInDepotDate) && !existInDepotDate.equals(inDepotDate)) {
                setErrorMsg(j, "相同调拨订单号对应的入库时间不一致", resultList);
                failure += 1;
                continue;
            }

			if (StringUtils.isBlank(existInDepotDate)) {
                inDepotDateMap.put(transferOrderCode, inDepotDate);
            }

			/**调入商品货号*/
			String goodsNo = row.get("调入商品货号");
			if(checkIsNullOrNot(j, goodsNo, "调入商品货号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			goodsNo = goodsNo.trim() ;
            itemModel.setInGoodsNo(goodsNo);

			Integer totalInNum = 0;
			/**调入好品数量*/
			String goodNum = row.get("调入好品数量");
			/**调入好品数量*/
			String wornNum = row.get("调入坏品数量");

			if (StringUtils.isBlank(goodNum) && StringUtils.isBlank(wornNum)) {
				setErrorMsg(j, "调入好品数量和调入坏品数量至少一个有值", resultList);
				failure += 1;
				continue;
			}

			if(StringUtils.isNotBlank(goodNum)) {
				if (!isNumber(goodNum)) {
					setErrorMsg(j, "调入好品数量非数字类型", resultList);
					failure += 1;
					continue;
				}
				goodNum = goodNum.trim();
				totalInNum += Integer.valueOf(goodNum);
				itemModel.setTransferNum(Integer.valueOf(goodNum));
			}

			if(StringUtils.isNotBlank(wornNum)) {
				if (!isNumber(wornNum)) {
					setErrorMsg(j, "调入坏品数量非数字类型", resultList);
					failure += 1;
					continue;
				}
				wornNum = wornNum.trim();
				totalInNum += Integer.valueOf(wornNum);
				itemModel.setWornNum(Integer.valueOf(wornNum));
			}

			/**校验调拨订单是否存在*/
			TransferOrderModel model = existOrderMap.get(transferOrderCode);
			if (model == null) {
				TransferOrderModel transferOrderModel = new TransferOrderModel();
				transferOrderModel.setCode(transferOrderCode);
				model = transferOrderDao.searchByModel(transferOrderModel);

				if (model == null) {
					setErrorMsg(j, "调拨订单号不存在", resultList);
					failure += 1;
					continue;
				}

				if (DERP_ORDER.TRANSFERORDER_STATUS_013.equals(model.getStatus())) {
					setErrorMsg(j, "调拨订单：" + transferOrderCode + "未提交", resultList);
					failure += 1;
					continue;
				}
				existOrderMap.put(transferOrderCode, model);
			}

            /**入库时间必须大于关账日期/月结日期*/
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setDepotId(model.getInDepotId());
			closeAccountsMongo.setBuId(model.getBuId());
			String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库时间
				if (TimeUtils.parseDay(inDepotDate).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j, "入库时间必须大于关账日期/月结日期", resultList);
					failure += 1;
					continue;
				}
			}

			//根据仓库id到mongoDB中查询调入仓库信息
			Map<String, Object> inDepotInfoMap = new HashMap<>();
			inDepotInfoMap.put("depotId", model.getInDepotId());//调入仓库id
			DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);

			/**批次号*/
			String batchNo = row.get("批次号");
			/**生产日期*/
			String productionDate = row.get("生产日期");
			/**失效日期*/
			String overdueDate = row.get("失效日期");
			if (!DERP_SYS.DEPOTINFO_BATCHVALIDATION_0.equals(inDepotInfoMongo.getBatchValidation())) {
				if(checkIsNullOrNot(j, batchNo, "入库仓库为批次强校验或者入库强校验时，批次不能为空!", resultList)) {
					failure += 1;
					continue;
				}

				if(checkIsNullOrNot(j, productionDate, "入库仓库为批次强校验或者入库强校验时，生产日期不能为空!", resultList)) {
					failure += 1;
					continue;
				}
				productionDate = productionDate.trim() ;

				if(checkIsNullOrNot(j, overdueDate, "入库仓库为批次强校验或者入库强校验时，失效日期不能为空!", resultList)) {
					failure += 1;
					continue;
				}
				overdueDate = overdueDate.trim() ;
			}

			if (StringUtils.isNotBlank(batchNo)) {
				batchNo = batchNo.trim() ;
				itemModel.setTransferBatchNo(batchNo);
			}

			if (StringUtils.isNotBlank(productionDate)) {
				productionDate = productionDate.trim() ;
				if(!isDate(productionDate)) {
					setErrorMsg(j, "生产日期格式有误", resultList);
					failure += 1;
					continue;
				}
				itemModel.setProductionDate(formatDate(productionDate));
			}

			if (StringUtils.isNotBlank(overdueDate)) {
				overdueDate = overdueDate.trim() ;
				if(!isDate(overdueDate)) {
					setErrorMsg(j, "失效日期格式有误", resultList);
					failure += 1;
					continue;
				}
				itemModel.setOverdueDate(formatDate(overdueDate));
			}

			/**判断商品货号是否存在*/
			String codeGoodsKey = transferOrderCode + "_" + goodsNo;
			List<TransferOrderItemModel> transferOrderItemModels = codeGoodsNoItemsMap.get(codeGoodsKey);
			if (transferOrderItemModels == null) {
				TransferOrderItemModel transferOrderItemModel = new TransferOrderItemModel();
				transferOrderItemModel.setTransferOrderId(model.getId());
				transferOrderItemModel.setInGoodsNo(goodsNo);
				transferOrderItemModels = transferOrderItemDao.list(transferOrderItemModel);
			}

			if (transferOrderItemModels == null || transferOrderItemModels.isEmpty()) {
				setErrorMsg(j, "调拨订单：" + transferOrderCode + "没有找到对应的调入商品货号：" + goodsNo , resultList);
				failure += 1;
				continue;
			}

			/**判断调拨订单对应的入库单是否存在*/
			TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
			transferInDepotModel.setTransferOrderId(model.getId());
			TransferInDepotModel existModel = transferInDepotDao.searchByModel(transferInDepotModel);
			if (existModel != null) {
				setErrorMsg(j, "调拨订单号：" + transferOrderCode + "对应的调拨入库单已经存在", resultList);
				failure += 1;
				continue;
			}

			//校验调拨订单号+调入商品货号入库量需小于或等于调拨订单对应商品的入库量(可能存在不同批次效期的相同订单+货号的记录)

			if (codeGoodsNoNumMap.containsKey(codeGoodsKey)) {
				totalInNum += codeGoodsNoNumMap.get(codeGoodsKey);
			}
			codeGoodsNoNumMap.put(codeGoodsKey, totalInNum);
			Integer orderInDepotNum = 0;
			for (TransferOrderItemModel orderItemModel : transferOrderItemModels) {
				orderInDepotNum += orderItemModel.getInTransferNum();
			}
			if (totalInNum > orderInDepotNum) {
				setErrorMsg(j, "调拨订单号：" + transferOrderCode + "调入商品货号：" + goodsNo + "的入库量需小于或等于调拨订单对应商品的入库量" , resultList);
				failure += 1;
				continue;
			}

			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType())) {
				itemModel.setTallyingUnit(model.getTallyingUnit());
			}

			List<TransferInDepotItemModel> transferInDepotItemModels = new ArrayList<>();
			transferInDepotItemModels.add(itemModel);
			if (transferInMap.containsKey(transferOrderCode)) {
				transferInDepotItemModels.addAll(transferInMap.get(transferOrderCode));
			}
			transferInMap.put(transferOrderCode, transferInDepotItemModels);
		}

		//2. 生成调拨入库单和表体
        if (failure == 0) {
            for (String transferOrderCode : existOrderMap.keySet()) {
				OrderExternalCodeModel transferInDepotExistModel = new OrderExternalCodeModel();
				transferInDepotExistModel.setExternalCode(transferOrderCode);
				transferInDepotExistModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_4));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
				try {
					orderExternalCodeDao.save(transferInDepotExistModel);
				} catch (Exception e) {
					LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderCode + "  保存失败");
					throw new RuntimeException("该调拨订单存在对应的入库单，不允许重复入库!");
				}

                TransferOrderModel transferOrderModel = existOrderMap.get(transferOrderCode);
                String inDepotDate = inDepotDateMap.get(transferOrderCode);

                TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
                transferInDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));
                transferInDepotModel.setTransferOrderCode(transferOrderCode);
                transferInDepotModel.setTransferOrderId(transferOrderModel.getId());
                transferInDepotModel.setInDepotId(transferOrderModel.getInDepotId());
                transferInDepotModel.setInDepotName(transferOrderModel.getInDepotName());
                transferInDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());
                transferInDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());
                transferInDepotModel.setBuId(transferOrderModel.getBuId());
                transferInDepotModel.setBuName(transferOrderModel.getBuName());
                transferInDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_011);
                transferInDepotModel.setContractNo(transferOrderModel.getContractNo());
                transferInDepotModel.setMerchantId(transferOrderModel.getMerchantId());
                transferInDepotModel.setMerchantName(transferOrderModel.getMerchantName());
                transferInDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
                transferInDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户名称
                transferInDepotModel.setTransferDate(TimeUtils.parseDay(inDepotDate));//调入时间
				transferInDepotModel.setCreateDate(TimeUtils.getNow());
				transferInDepotModel.setCreateName(user.getName());
				transferInDepotModel.setCreater(user.getId());
				transferInDepotModel.setSource(DERP_ORDER.TRANSFERINDEPOT_SOURCE_3); //手工导入

                Long id = transferInDepotDao.save(transferInDepotModel);

                //合并相同调入商品货号+批次+效期
                Map<String, TransferInDepotItemModel> transferInDepotItemModelMap = new HashMap<>();
                List<TransferInDepotItemModel> transferInDepotItemModels = transferInMap.get(transferOrderCode);

                for (TransferInDepotItemModel itemModel : transferInDepotItemModels) {
                    String key = itemModel.getInGoodsNo() + "_" + itemModel.getTransferBatchNo() + "_" +
                            itemModel.getProductionDate() + "_" + itemModel.getOverdueDate();

                    TransferInDepotItemModel existItemModel = transferInDepotItemModelMap.get(key);
                    if (existItemModel != null) {
                        Integer goodsNum = itemModel.getTransferNum() == null ? 0 : itemModel.getTransferNum();
                        Integer wornNum = itemModel.getWornNum() == null ? 0 : itemModel.getWornNum();
                        Integer existGoodsNum = existItemModel.getTransferNum() == null ? 0 : existItemModel.getTransferNum();
                        Integer existWornNum = existItemModel.getWornNum() == null ? 0 : existItemModel.getWornNum();
                        itemModel.setTransferNum(goodsNum + existGoodsNum);
                        itemModel.setWornNum(wornNum + existWornNum);
                    }
                    transferInDepotItemModelMap.put(key, itemModel);
                }

                //生成调拨入库单表体
                for (String key : transferInDepotItemModelMap.keySet()) {
                    TransferInDepotItemModel transferInDepotItemModel = transferInDepotItemModelMap.get(key);
                    transferInDepotItemModel.setTransferDepotId(id);

                    //商品信息
                    Map<String, Object> mParam = new HashMap<>();
                    mParam.put("goodsNo", transferInDepotItemModel.getInGoodsNo());
                    mParam.put("merchantId", transferInDepotModel.getMerchantId());
                    MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(mParam);

                    transferInDepotItemModel.setInGoodsId(merchandise.getMerchandiseId());
                    transferInDepotItemModel.setInCommbarcode(merchandise.getCommbarcode());
                    transferInDepotItemModel.setInGoodsCode(merchandise.getGoodsCode());
                    transferInDepotItemModel.setInGoodsName(merchandise.getName());

                    transferInDepotItemDao.save(transferInDepotItemModel);
                }

            }
            success = data.size() ;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("pass", pass);
        map.put("failure", failure);
        map.put("message", resultList);
        return map;
	}

	@Override
	public Map<String, Object> auditTransferInDepot(List<Long> ids, User user) throws Exception {
		Map<String, Object> result = new HashMap<>();
		StringBuffer failureMsg = new StringBuffer();
		Integer failure = 0;

		List<TransferInDepotModel> transferInDepotModels = new ArrayList<>();
		Map<String, List<TransferInDepotItemModel>> itemModelMap = new HashMap<>();
		Map<String, DepotInfoMongo> depotMap = new HashMap<>();
		for (Long id : ids) {
			TransferInDepotModel model = transferInDepotDao.searchById(id);

			if (!model.getStatus().equals(DERP_ORDER.TRANSFERINDEPOT_STATUS_011)) {
				failureMsg.append("调拨入库单单号:" + model.getCode() + "不是“待入仓”状态!\n");
				failure++;
				continue;
			}

			if (!DERP_ORDER.TRANSFERINDEPOT_SOURCE_3.equals(model.getSource())) {
				failureMsg.append("调拨出库单单号:" + model.getCode() + "不是“手工导入”!\n");
				failure++;
				continue;
			}

			/**入库时间必须大于关账日期/月结日期*/
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setDepotId(model.getInDepotId());
			closeAccountsMongo.setBuId(model.getBuId());
			String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库时间
				if (model.getTransferDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					failureMsg.append("调拨出库单单号:" + model.getCode() + "入库时间必须大于关账日期/月结日期”!\n");
					failure++;
					continue;
				}
			}

			// 根据仓库id获取仓库信息
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", model.getInDepotId());
			DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(depotInfo_params);

			/**调拨入库表体信息*/
			TransferInDepotItemModel itemModel = new TransferInDepotItemModel();
			itemModel.setTransferDepotId(model.getId());
			List<TransferInDepotItemModel> transferInDepotItemModels = transferInDepotItemDao.list(itemModel);

			/**入库仓库为批次强校验或者入库强校验时，批次效期必填*/
			if (!DERP_SYS.DEPOTINFO_BATCHVALIDATION_0.equals(inDepotInfoMongo.getBatchValidation())) {
				for (TransferInDepotItemModel transferInDepotItemModel : transferInDepotItemModels) {
					if (StringUtils.isBlank(transferInDepotItemModel.getTransferBatchNo())) {
						failureMsg.append("调拨入库单单号:" + model.getCode() + "入库仓库为批次强校验或者入库强校验时，批次不能为空!");
						failure++;
						break;
					}

					if (transferInDepotItemModel.getProductionDate() == null) {
						failureMsg.append("调拨入库单单号:" + model.getCode() + "入库仓库为批次强校验或者入库强校验时，生产日期不能为空!");
						failure++;
						break;
					}

					if (transferInDepotItemModel.getOverdueDate() == null) {
						failureMsg.append("调拨入库单单号:" + model.getCode() + "入库仓库为批次强校验或者入库强校验时，失效日期不能为空!");
						failure++;
						break;
					}
				}
			}
			transferInDepotModels.add(model);
			itemModelMap.put(model.getCode(), transferInDepotItemModels);
			depotMap.put(model.getCode(), inDepotInfoMongo);
		}

		if (failure > 0) {
			result.put("failure", failure);
			result.put("failureMsg", failureMsg);
			return result;
		}

		for (TransferInDepotModel model : transferInDepotModels) {
			/**推送库存加减接口*/
			List<TransferInDepotItemModel> transferInDepotItemModels = itemModelMap.get(model.getCode());
			TransferOrderModel transferOrderModel = transferOrderDao.searchById(model.getTransferOrderId());
			DepotInfoMongo inDepotInfoMongo = depotMap.get(model.getCode());
			// 加库存商品
			List<InvetAddOrSubtractGoodsListJson> addGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			for (TransferInDepotItemModel inDepotItemModel : transferInDepotItemModels) {

				if (inDepotItemModel.getTransferNum() != null && inDepotItemModel.getTransferNum().intValue() > 0){
					// 拼装加库存商品
					InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();

					aSGood.setGoodsId(String.valueOf(inDepotItemModel.getInGoodsId()));
					aSGood.setGoodsName(inDepotItemModel.getInGoodsName());
					aSGood.setGoodsNo(inDepotItemModel.getInGoodsNo());
					aSGood.setBarcode(inDepotItemModel.getBarcode());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);// 商品分类 （0 好品 1坏品） 字符串
					aSGood.setNum(inDepotItemModel.getTransferNum());
					aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 操作类型（调增\调减） 字符串 0 调减 1调增
					String unit = inDepotItemModel.getTallyingUnit();
					if (StringUtils.isNotBlank(unit)) {
						switch (unit) {
							case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
							case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
							default: unit = DERP.INVENTORY_UNIT_2; break;
						}
					}
					aSGood.setUnit(unit);
					aSGood.setIsExpire(DERP.ISEXPIRE_1);
					aSGood.setBatchNo(inDepotItemModel.getTransferBatchNo());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					addGoodsList.add(aSGood);
				}

				if (inDepotItemModel.getWornNum() != null && inDepotItemModel.getWornNum().intValue() > 0){
					// 拼装加库存商品
					InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();

					aSGood.setGoodsId(String.valueOf(inDepotItemModel.getInGoodsId()));
					aSGood.setGoodsName(inDepotItemModel.getInGoodsName());
					aSGood.setGoodsNo(inDepotItemModel.getInGoodsNo());
					aSGood.setBarcode(inDepotItemModel.getBarcode());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);// 商品分类 （0 好品 1坏品） 字符串
					aSGood.setNum(inDepotItemModel.getWornNum());
					aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 操作类型（调增\调减） 字符串 0 调减 1调增
					String unit = inDepotItemModel.getTallyingUnit();
					if (StringUtils.isNotBlank(unit)) {
						switch (unit) {
							case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
							case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
							default: unit = DERP.INVENTORY_UNIT_2; break;
						}
					}
					aSGood.setUnit(unit);
					aSGood.setIsExpire(DERP.ISEXPIRE_1);
					aSGood.setBatchNo(inDepotItemModel.getTransferBatchNo());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					addGoodsList.add(aSGood);
				}
			}
			// 拼装加库存接口参数
			InvetAddOrSubtractRootJson addInventoryRoot = new InvetAddOrSubtractRootJson();
			addInventoryRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
			addInventoryRoot.setMerchantName(transferOrderModel.getMerchantName());
			addInventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
			addInventoryRoot.setDepotType(inDepotInfoMongo.getType());
			addInventoryRoot.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
			addInventoryRoot.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
			addInventoryRoot.setDepotCode(inDepotInfoMongo.getCode());
			addInventoryRoot.setDepotName(inDepotInfoMongo.getName());
			addInventoryRoot.setOrderNo(model.getCode());
			addInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009);
			addInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010);
			addInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
			addInventoryRoot.setOwnMonth(TimeUtils.formatMonth(model.getTransferDate()));
			addInventoryRoot.setDivergenceDate(TimeUtils.format(model.getTransferDate(), "yyyy-MM-dd HH:mm:ss"));
			addInventoryRoot.setBusinessNo(transferOrderModel.getCode());
			addInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
			addInventoryRoot.setBuName(transferOrderModel.getBuName());
			addInventoryRoot.setGoodsList(addGoodsList);
			//回调参数
			addInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
			addInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
			addInventoryRoot.setCustomParam(new HashMap<String, Object>());
			// 加库存
			JSONObject subjson = JSONObject.fromObject(addInventoryRoot);
			rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

			//更新状态
			TransferInDepotModel updateModel = new TransferInDepotModel();
			updateModel.setId(model.getId());
			updateModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028);
			transferInDepotDao.modify(updateModel);

		}

		result.put("failure", failure);
		result.put("success", ids.size()-failure);
		result.put("failureMsg", failureMsg);
		return result;
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
		if (StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true;
		} else {
			return false;
		}

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
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index, String msg, List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
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
}
