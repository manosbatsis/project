package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.MaterialOrderDao;
import com.topideal.dao.sale.MaterialOrderItemDao;
import com.topideal.dao.sale.MaterialOutDepotDao;
import com.topideal.dao.sale.MaterialOutDepotItemDao;
import com.topideal.entity.dto.sale.MaterialOutDepotDTO;
import com.topideal.entity.dto.sale.MaterialOutDepotItemDTO;
import com.topideal.entity.vo.sale.MaterialOrderItemModel;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.entity.vo.sale.MaterialOutDepotItemModel;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.MaterialOutDepotService;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Service
public class MaterialOutDepotServiceImpl implements MaterialOutDepotService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(MaterialOutDepotServiceImpl.class);
	//表头
	@Autowired
	private MaterialOutDepotDao materialOutDepotDao;
	//表体
	@Autowired
	private MaterialOutDepotItemDao materialOutDepotItemDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private MaterialOrderDao materialOrderDao;
	@Autowired
	private MaterialOrderItemDao materialOrderItemDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ

	/**
	 * 列表 分页
	 */
	@Override
	public MaterialOutDepotDTO listDTOByPage(MaterialOutDepotDTO dto, User user) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return materialOutDepotDao.listDTOByPage(dto);
	}
	/**
	 * 根据领料单id 获取表体
	 */
	@Override
	public List<MaterialOutDepotItemDTO> getItemDetail(Long orderId) throws Exception {
		MaterialOutDepotItemDTO dto = new MaterialOutDepotItemDTO();
		dto.setMaterialOutDepotId(orderId);
		return materialOutDepotItemDao.getDetailDTO(dto);
	}
	/**
	 * 根据条件查询领料出库单
	 */
	@Override
	public List<MaterialOutDepotDTO> listOrderDTO(MaterialOutDepotDTO dto, User user) throws Exception {
		List<MaterialOutDepotDTO> list = new ArrayList<MaterialOutDepotDTO>();
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				return list;
			}
			dto.setBuList(buList);
		}
		list = materialOutDepotDao.listDTO(dto);
		return list;
	}
	/**
	 * 根据id查询出库单
	 */
	@Override
	public MaterialOutDepotDTO searchDetail(Long id) throws Exception {
		MaterialOutDepotDTO dto = new MaterialOutDepotDTO();
		dto.setId(id);
		return materialOutDepotDao.getDetailDTO(dto);
	}
	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> importMaterialOutDepot(List<List<Map<String, String>>> data, User user) throws Exception {
		int success = 0;
		int failure = 0;
		List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
		Map<String , MaterialOutDepotModel> materialOutMap = new HashMap<String, MaterialOutDepotModel>();//记录表头信息
		Map<String , List<MaterialOutDepotItemModel>> materialOutItemMap = new HashMap<String,  List<MaterialOutDepotItemModel>>();//记录商品信息
		Map<String , Integer> checkGoodsNum = new HashMap<String, Integer>();//记录领料单编号+商品货号导入数量
		Map<String,List<String>> goodsNoListMap = new HashMap<String, List<String>>();//记录出库单商品
		List<String> materialIdAndCodeList = new ArrayList<String>();
		List<Map<String, String>> objects = data.get(0);
		for (int j = 1; j < objects.size()+1; j++) {
			Map<String, String> msg = new HashMap<String, String>();
			Map<String, String> map = objects.get(j-1);
			String code = map.get("领料单号");
			if (StringUtils.isBlank(code)) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			MaterialOrderModel materialOrderModel = new MaterialOrderModel();
			materialOrderModel.setCode(code);
			materialOrderModel.setMerchantId(user.getMerchantId());
			materialOrderModel = materialOrderDao.searchByModel(materialOrderModel);
			if(materialOrderModel == null) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号不存在");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			MaterialOutDepotModel materialOutDepotModel = new MaterialOutDepotModel();
			materialOutDepotModel.setMaterialOrderCode(code);
			materialOrderModel.setMerchantId(user.getMerchantId());
			materialOutDepotModel = materialOutDepotDao.searchByModel(materialOutDepotModel);
			if(materialOutDepotModel != null) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+" 已存在出库单，请勿重复导入");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if(!DERP_ORDER.MATERIALORDER_STATE_003.equals(materialOrderModel.getState())) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+" 状态不为“已审核”");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			String deliverDate = map.get("发货时间");
			if (StringUtils.isBlank(deliverDate)) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+" 发货时间不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			deliverDate = deliverDate.trim();
			if(!TimeUtils.isDate(deliverDate)){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+" 发货时间格式有误,正确格式为:yyyy-MM-dd HH:mm:ss");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			if(TimeUtils.daysBetween(TimeUtils.parse(deliverDate, "yyyy-MM-dd HH:mm:ss"), new Date()) < 0) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+" 发货时间不可超过当前时间");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			String goodsNo = map.get("商品货号");
			if (StringUtils.isBlank(goodsNo)) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+" 商品货号不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("goodsNo", goodsNo) ;
			params.put("merchantId", user.getMerchantId());
			params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(params);
			if(merchandise == null){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+"商品货号："+ goodsNo+" 在商品表不存在");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			String goodsName = map.get("商品名称");

			String transferNum = map.get("好品数量");
			String wornNum = map.get("坏品数量");
			if(StringUtils.isBlank(transferNum) && StringUtils.isBlank(wornNum)){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+"商品货号："+ goodsNo+" 好品数量或坏品数量必须有一个不为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}else if(("0".equals(transferNum) && "0".equals(wornNum)) ||
					("0".equals(transferNum) && StringUtils.isBlank(wornNum)) ||
					("0".equals(wornNum) && StringUtils.isBlank(transferNum))){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+"商品货号："+ goodsNo+" 好品数量或坏品数量必须有一个不为0或不为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if((StringUtils.isNotBlank(transferNum) && Integer.valueOf(transferNum).intValue() < 0) ||
					(StringUtils.isNotBlank(wornNum) && Integer.valueOf(wornNum).intValue() < 0)) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号："+code+"商品货号："+ goodsNo+" 好品数量或坏品数量不能小于0");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			MaterialOrderItemModel materialItemModel = new MaterialOrderItemModel();
			materialItemModel.setOrderId(materialOrderModel.getId());
			materialItemModel.setGoodsNo(goodsNo);
			materialItemModel = materialOrderItemDao.searchByModel(materialItemModel);
			if(materialItemModel == null) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "商品货号："+ goodsNo+" 在领料单号："+code+" 中不存在");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			Integer transferNumInt = StringUtils.isBlank(transferNum)?0:Integer.valueOf(transferNum);
			Integer wornNumInt = StringUtils.isBlank(wornNum)?0:Integer.valueOf(wornNum);
			Integer totalGoodsNum = transferNumInt+wornNumInt;	// 导入进来的好品+坏品数量
			Integer outDepotNum = 0;//已导入的出库量
			String key = materialOrderModel.getId() +","+ code +","+ goodsNo;
			if(checkGoodsNum.containsKey(key)) {
				outDepotNum = outDepotNum + checkGoodsNum.get(key);//当前导入文件 该商品累计的数量 + 已出库的数量

				Integer totalNumSum = checkGoodsNum.get(key) + totalGoodsNum;
				checkGoodsNum.put(key, totalNumSum);//累计导入数量
			}else {
				checkGoodsNum.put(key, totalGoodsNum);
			}

			//出库仓库，校验仓库是否批次强校验
            Map<String, Object> params1 = new HashMap<String, Object>();
            params1.put("depotId", materialOrderModel.getOutDepotId());
            DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			String batchNo = map.get("批次号");
			String productionDate = map.get("生产日期");
			String overDueDate = map.get("失效日期");
			if (depot.getBatchValidation()==null || DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())){
				if(StringUtils.isBlank(batchNo)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "领料单号:"+code+"，商品货号:"+goodsNo+"，批次号不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				if(StringUtils.isBlank(productionDate)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "领料单号:"+code+"，商品货号:"+goodsNo+"，生产日期不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				productionDate = productionDate.trim();
				if(!isDate(productionDate)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "领料单号:"+code+"，商品货号:"+goodsNo+"，生产日期格式有误，正确格式为:yyyy-MM-dd");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				if(StringUtils.isBlank(productionDate)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "领料单号:"+code+"，商品货号:"+goodsNo+"，失效日期不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				overDueDate = overDueDate.trim() ;
				if(!isDate(productionDate)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "领料单号:"+code+"，商品货号:"+goodsNo+"，失效日期格式有误，正确格式为:yyyy-MM-dd");
					msgList.add(msg);
					failure += 1;
					continue;
				}
			}
			String tallyingUnit = map.get("海外理货单位");
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()) && StringUtils.isBlank(tallyingUnit)) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "领料单号:"+code+"出库仓为海外仓，海外理货单位不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			tallyingUnit = tallyingUnit.trim();

			if(!materialOutMap.containsKey(code)) {
				//封装表头数据
				MaterialOutDepotModel mOutDepotModel = new MaterialOutDepotModel();
				mOutDepotModel.setMaterialOrderId(materialOrderModel.getId());// 领料订单id
				mOutDepotModel.setMerchantId(materialOrderModel.getMerchantId());// 商家ID
				mOutDepotModel.setMerchantName(materialOrderModel.getMerchantName());// 商家名称
				mOutDepotModel.setPoNo(materialOrderModel.getPoNo());// PO号
				mOutDepotModel.setOutDepotId(materialOrderModel.getOutDepotId());// 调出仓库id
				mOutDepotModel.setOutDepotName(materialOrderModel.getOutDepotName()); // 调出仓库名称
				mOutDepotModel.setCustomerId(materialOrderModel.getCustomerId());// 客户id(供应商)
				mOutDepotModel.setCustomerName(materialOrderModel.getCustomerName());// 客户名称
				mOutDepotModel.setDeliverDate(Timestamp.valueOf(deliverDate));// 发货时间
				mOutDepotModel.setStatus(DERP_ORDER.MATERIALORDER_STATE_017);// '状态 017-待出库
				mOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LLCK));// 出库清单编号自生成
				mOutDepotModel.setMaterialOrderCode(materialOrderModel.getCode());// 领料订单编号
				mOutDepotModel.setOrderSource("1");// 1手工导入 2.接口回传
				mOutDepotModel.setBuId(materialOrderModel.getBuId());
				mOutDepotModel.setBuName(materialOrderModel.getBuName());
				mOutDepotModel.setCurrency(materialOrderModel.getCurrency());
				mOutDepotModel.setCreater(user.getId());
				mOutDepotModel.setCreateName(user.getName());
				mOutDepotModel.setCreateDate(TimeUtils.getNow());

				materialOutMap.put(code, mOutDepotModel);
			}
			// 新增领料出库表体
			MaterialOutDepotItemModel itemModel = new MaterialOutDepotItemModel();
			itemModel.setGoodsId(merchandise.getMerchandiseId());// 商品id
			itemModel.setGoodsNo(goodsNo);// 库位货号
			itemModel.setGoodsCode(merchandise.getGoodsCode());// 商品编码
			itemModel.setGoodsName(merchandise.getName());// 商品名称
			itemModel.setBarcode(merchandise.getBarcode());// 货品条形码
			itemModel.setCommbarcode(merchandise.getCommbarcode());	// 标准条码
			itemModel.setTransferNum(transferNumInt);// 出库数量
			itemModel.setWornNum(wornNumInt);// 坏品数量
			itemModel.setTransferBatchNo(batchNo);// 调拨批次
			itemModel.setMaterialNum(materialItemModel.getNum());//领料数量
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
				itemModel.setTallyingUnit(tallyingUnit);// 领料订单理货单位
			}
			if(StringUtils.isNotBlank(productionDate)) {
				itemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
			}
			if(StringUtils.isNotBlank(overDueDate)) {
				itemModel.setOverdueDate(Timestamp.valueOf(overDueDate+" 00:00:00"));//失效日期
			}

			List<MaterialOutDepotItemModel> itemList = materialOutItemMap.get(code);
			if(itemList == null) {
				itemList = new ArrayList<MaterialOutDepotItemModel>() ;
			}
			itemList.add(itemModel);
			materialOutItemMap.put(code, itemList);

			List<String> goodsNoList = goodsNoListMap.get(materialOrderModel.getId()+"-"+code);
			if(goodsNoList == null) {
				goodsNoList = new ArrayList<String>();
			}
			goodsNoList.add(goodsNo);
			goodsNoListMap.put(materialOrderModel.getId()+"-"+code, goodsNoList);
			if(!materialIdAndCodeList.contains(materialOrderModel.getId()+"-"+code)) {
				materialIdAndCodeList.add(materialOrderModel.getId()+"-"+code);
			}
		}

		if(failure == 0) {
			for(String codeAndGoodsNo : checkGoodsNum.keySet()) {
				Integer totalOutNum = checkGoodsNum.get(codeAndGoodsNo);
				String id = codeAndGoodsNo.split(",")[0];
				String code = codeAndGoodsNo.split(",")[1];
				String goodsNo = codeAndGoodsNo.split(",")[2];

				MaterialOrderItemModel materialItemModel = new MaterialOrderItemModel();
				materialItemModel.setOrderId(Long.valueOf(id));
				materialItemModel.setGoodsNo(goodsNo);
				materialItemModel = materialOrderItemDao.searchByModel(materialItemModel);

				if(totalOutNum.intValue() != materialItemModel.getNum().intValue()) {
					Map<String, String> msg = new HashMap<String, String>();
					msg.put("row", "");
					msg.put("msg", "领料单号:"+code+"，商品货号："+goodsNo+"，累计出库单的好品数量+坏品数量与领料单的领取数量不一致");
					msgList.add(msg);
					failure += 1;
					continue;
				}
			}
			for(String materialIdAndCode : materialIdAndCodeList) {
				String materialId = materialIdAndCode.split("-")[0];
				String materialCode = materialIdAndCode.split("-")[1];
				MaterialOrderItemModel materialItemModel = new MaterialOrderItemModel();
				materialItemModel.setOrderId(Long.valueOf(materialId));
				List<MaterialOrderItemModel>  materialOrderItemList= materialOrderItemDao.list(materialItemModel);
				List<String> materialOrderGoodsNoList = materialOrderItemList.stream().map(MaterialOrderItemModel::getGoodsNo).collect(Collectors.toList());
				List<String> materialOutGoodsNoList = goodsNoListMap.get(materialIdAndCode);

				List<String> subtractList = (List<String>) CollectionUtils.subtract(materialOrderGoodsNoList, materialOutGoodsNoList);
				if(subtractList != null && subtractList.size() > 0) {
					Map<String, String> msg = new HashMap<String, String>();
					msg.put("row", "");
					msg.put("msg", "领料单号:"+materialCode+"，导入的出库单商品货号数量与领料单商品货号数量不一致，缺失商品为："+StringUtils.join(subtractList,",") );
					msgList.add(msg);
					failure += 1;
					continue;
				}
			}

		}

		if(failure == 0) {
			for (String materialCode : materialOutMap.keySet()) {
				MaterialOutDepotModel model = materialOutMap.get(materialCode);
				Long id = materialOutDepotDao.save(model);
				if(id != null) {
					 //合并相同调入商品货号+批次+效期
	                Map<String, MaterialOutDepotItemModel> map = new HashMap<>();
	                List<MaterialOutDepotItemModel> materialOutDepotItemModelModels = materialOutItemMap.get(materialCode);

	                for (MaterialOutDepotItemModel itemModel : materialOutDepotItemModelModels) {
	                    String key = itemModel.getGoodsNo() + "_" + itemModel.getTransferBatchNo() + "_" +itemModel.getProductionDate() + "_" + itemModel.getOverdueDate();

	                    MaterialOutDepotItemModel existItemModel = map.get(key);
	                    if (existItemModel != null) {
	                        Integer goodsNum = itemModel.getTransferNum() == null ? 0 : itemModel.getTransferNum();
	                        Integer wornNum = itemModel.getWornNum() == null ? 0 : itemModel.getWornNum();
	                        Integer existGoodsNum = existItemModel.getTransferNum() == null ? 0 : existItemModel.getTransferNum();
	                        Integer existWornNum = existItemModel.getWornNum() == null ? 0 : existItemModel.getWornNum();
	                        itemModel.setTransferNum(goodsNum + existGoodsNum);
	                        itemModel.setWornNum(wornNum + existWornNum);
	                    }
	                    map.put(key, itemModel);
	                }

	                for (String key : map.keySet()) {
	                	MaterialOutDepotItemModel itemModel = map.get(key);
	                	itemModel.setMaterialOutDepotId(id);
	                	materialOutDepotItemDao.save(itemModel);
					}
					success += 1;
				}
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_19, model.getCode(), "手工导入", null, null);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", msgList);
		return map;
	}
	/**
	 * 审核
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	@Override
	public void auditMaterialOutDepot(String ids, User user) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long id : idList) {
			MaterialOutDepotModel materialOutDepot = materialOutDepotDao.searchById(id);
			if(!DERP_ORDER.MATERIALORDER_STATE_017.equals(materialOutDepot.getStatus())){
				throw new RuntimeException("领料出库单："+materialOutDepot.getCode()+"必须为手工导入的单据");
			}
			if(!"1".equals(materialOutDepot.getOrderSource())){
				throw new RuntimeException("领料出库单："+materialOutDepot.getCode()+"不为“待出库”");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", materialOutDepot.getOutDepotId());
			params.put("status", DERP_SYS.DEPOTINFO_STATUS_1);
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

			MaterialOutDepotItemModel materialOutItemModel = new MaterialOutDepotItemModel();
			materialOutItemModel.setMaterialOutDepotId(id);
			List<MaterialOutDepotItemModel> itemList = materialOutDepotItemDao.list(materialOutItemModel);
			if(!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());

				InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
				inventoryFreezeRootJson.setMerchantId(materialOutDepot.getMerchantId().toString());
				inventoryFreezeRootJson.setMerchantName(materialOutDepot.getMerchantName());
				inventoryFreezeRootJson.setDepotId(materialOutDepot.getOutDepotId().toString());
				inventoryFreezeRootJson.setDepotName(materialOutDepot.getOutDepotName());
				inventoryFreezeRootJson.setOrderNo(materialOutDepot.getCode());
				inventoryFreezeRootJson.setBusinessNo(materialOutDepot.getMaterialOrderCode());
				inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0021);
				inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033);
				inventoryFreezeRootJson.setSourceDate(now);
				inventoryFreezeRootJson.setOperateType("1");//操作类型  0 冻结 1解冻
				List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
				for (MaterialOutDepotItemModel item : itemList) {
					InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

					inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
					inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());

					inventoryFreezeGoodsListJson.setDivergenceDate(now);
					inventoryFreezeGoodsListJson.setNum(item.getTransferNum()+item.getWornNum());// 释放冻结量=好品量+坏品量
					inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
				}
				try {
					//有表体才需要推库存
					if(inventoryFreezeGoodsListJsonList != null && inventoryFreezeGoodsListJsonList.size()>0){
						inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
						rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
					}
				} catch (Exception e) {
					LOGGER.error(
							"----------------------领料出库单[" + materialOutDepot.getCode() + "]审核释放库存失败----------------------");
				}
			}
			// 修改出库单据状态为出库中
			materialOutDepot.setStatus(DERP_ORDER.MATERIALOUTDEPOT_STATUS_027);	// 出库中
			materialOutDepot.setAuditDate(TimeUtils.getNow());// 审核时间
			materialOutDepot.setAuditor(user.getId());// 审核人id
			materialOutDepot.setAuditName(user.getName());// 审核人用户名
			materialOutDepotDao.modify(materialOutDepot);

			//修改领料单据状态为出库中
			MaterialOrderModel updateMaterialOrder = new MaterialOrderModel();
			updateMaterialOrder.setId(materialOutDepot.getMaterialOrderId());
			updateMaterialOrder.setState(DERP_ORDER.MATERIALORDER_STATE_027);
			updateMaterialOrder.setModifier(user.getId());
			updateMaterialOrder.setModifierUsername(user.getName());
			updateMaterialOrder.setModifyDate(TimeUtils.getNow());
			materialOrderDao.modify(updateMaterialOrder);

			//扣减销售出库库存量
			String materialOutDepotCode = materialOutDepot.getCode();
			String now = TimeUtils.formatFullTime();

			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.MATERIAL_OUT_DEPOT_PUSH_BACK.getTopic());
			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.MATERIAL_OUT_DEPOT_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			customParam.put("code", materialOutDepot.getMaterialOrderCode());// 领料单编码
			invetAddOrSubtractRootJson.setCustomParam(customParam);
			invetAddOrSubtractRootJson.setMerchantId(user.getMerchantId().toString());
			invetAddOrSubtractRootJson.setMerchantName(user.getMerchantName());
			invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode());	//公司编码
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", materialOutDepot.getOutDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params); 	// 获取仓库信息
			invetAddOrSubtractRootJson.setDepotId(materialOutDepot.getOutDepotId().toString());
			invetAddOrSubtractRootJson.setDepotName(materialOutDepot.getOutDepotName());
			invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
			invetAddOrSubtractRootJson.setDepotType(mongo.getType());
			invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
			invetAddOrSubtractRootJson.setOrderNo(materialOutDepotCode);
			invetAddOrSubtractRootJson.setBusinessNo(materialOutDepot.getMaterialOrderCode());	// 领料单编码
			invetAddOrSubtractRootJson.setBuId(String.valueOf(materialOutDepot.getBuId())); // 事业部
			invetAddOrSubtractRootJson.setBuName(materialOutDepot.getBuName());
			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0021);
			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033);
			invetAddOrSubtractRootJson.setSourceDate(now);	// 单据时间
			invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.formatFullTime(materialOutDepot.getDeliverDate()));	// 发货时间
			// 获取当前年月
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
			String now2 = sdf2.format(materialOutDepot.getDeliverDate());	// 发货时间
			invetAddOrSubtractRootJson.setOwnMonth(now2); // 归属月份
			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			for (MaterialOutDepotItemModel item : itemList) {
				// 好品
				if(item.getTransferNum()!=null && item.getTransferNum()>0){
					InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

					invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
					invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
					invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
					invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());


					invetAddOrSubtractGoodsListJson.setType("0");	// 商品分类 （0 好品 1坏品 ）
					invetAddOrSubtractGoodsListJson.setNum(item.getTransferNum());	//好品数量
					invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
					invetAddOrSubtractGoodsListJson.setBatchNo(item.getTransferBatchNo());
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(item.getOverdueDate()!=null) {
						isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);//是否过期  （0 是 1 否）

					if (item.getProductionDate()!=null) {
						Date productionDateTimestamp  = item.getProductionDate();
						invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
					}
					if (item.getOverdueDate()!=null) {
						Date overdueDateTimestamp = item.getOverdueDate();
						invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
						if(TimeUtils.parseFullTime(now).before(item.getOverdueDate())){
							//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
							invetAddOrSubtractGoodsListJson.setIsExpire("1");
						}else{
							invetAddOrSubtractGoodsListJson.setIsExpire("0");
						}
					}
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
						// 海外仓冻结加理货单位
						String inventoryUnit = "";
						if (DERP.ORDER_TALLYINGUNIT_00.equals(item.getTallyingUnit())) {
							inventoryUnit = DERP.INVENTORY_UNIT_0;
						} else if (DERP.ORDER_TALLYINGUNIT_01.equals(item.getTallyingUnit())) {
							inventoryUnit = DERP.INVENTORY_UNIT_1;
						} else if (DERP.ORDER_TALLYINGUNIT_02.equals(item.getTallyingUnit())) {
							inventoryUnit = DERP.INVENTORY_UNIT_2;
						}
						invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
					}
					invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
				}
				// 坏品
				if(item.getWornNum()!=null && item.getWornNum()>0){
					InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

					invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
					invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
					invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
					invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());


					invetAddOrSubtractGoodsListJson.setType("1");	// 商品分类 （0 好品 1坏品 ）
					invetAddOrSubtractGoodsListJson.setNum(item.getWornNum());	//坏品数量
					invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
					invetAddOrSubtractGoodsListJson.setBatchNo(item.getTransferBatchNo());
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(item.getOverdueDate()!=null) {
						isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);//是否过期  （0 是 1 否）
					if (item.getProductionDate()!=null) {
						Date productionDateTimestamp  = item.getProductionDate();
						invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
					}
					if (item.getOverdueDate()!=null) {
						Date overdueDateTimestamp = item.getOverdueDate();
						invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
						if(TimeUtils.parseFullTime(now).before(item.getOverdueDate())){
							//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
							invetAddOrSubtractGoodsListJson.setIsExpire("1");
						}else{
							invetAddOrSubtractGoodsListJson.setIsExpire("0");
						}
					}
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
						// 海外仓冻结加理货单位
						String inventoryUnit = "";
						if (DERP.ORDER_TALLYINGUNIT_00.equals(item.getTallyingUnit())) {
							inventoryUnit = DERP.INVENTORY_UNIT_0;
						} else if (DERP.ORDER_TALLYINGUNIT_01.equals(item.getTallyingUnit())) {
							inventoryUnit = DERP.INVENTORY_UNIT_1;
						} else if (DERP.ORDER_TALLYINGUNIT_02.equals(item.getTallyingUnit())) {
							inventoryUnit = DERP.INVENTORY_UNIT_2;
						}
						invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
					}
					invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
				}
			}
			try {
				invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
				rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			} catch (Exception e) {
				LOGGER.error("----------------------领料出库单[" + materialOutDepotCode + "]扣减库存失败----------------------");
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_19, materialOutDepot.getCode(), "审核", null, null);
		}
	}
	/**
	 * 删除
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	@Override
	public void delMaterialOutDepot(String ids, User user) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long id : idList) {
			MaterialOutDepotModel materialOutDepot = materialOutDepotDao.searchById(id);
			if(!"1".equals(materialOutDepot.getOrderSource())){
				throw new RuntimeException("领料出库单："+materialOutDepot.getCode()+"必须为手工导入的单据");
			}
			if(!DERP_ORDER.MATERIALORDER_STATE_017.equals(materialOutDepot.getStatus())){
				throw new RuntimeException("领料出库单："+materialOutDepot.getCode()+"不为“待出库”");
			}

			materialOutDepot.setStatus(DERP.DEL_CODE_006);
			materialOutDepotDao.modify(materialOutDepot);
		}
	}


	/**
	 * 判断是否yyyy-MM-dd
	 * @param date
	 * @return
	 */
	private boolean isDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
	}
}
