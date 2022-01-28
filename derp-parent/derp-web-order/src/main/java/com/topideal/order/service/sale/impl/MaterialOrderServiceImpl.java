package com.topideal.order.service.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.sale.MaterialOrderDao;
import com.topideal.dao.sale.MaterialOrderItemDao;
import com.topideal.entity.dto.sale.MaterialOrderDTO;
import com.topideal.entity.dto.sale.MaterialOrderItemDTO;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.sale.MaterialOrderItemModel;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreGoodsItem;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreOrderRoot;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreRecipient;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderItemJson;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.MaterialOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
@Service
public class MaterialOrderServiceImpl implements MaterialOrderService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(MaterialOrderServiceImpl.class);
	//表头
	@Autowired
	private MaterialOrderDao materialOrderDao;
	//表体
	@Autowired
	private MaterialOrderItemDao materialOrderItemDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 供应商
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	//商家
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	// 商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	//计量单位
	@Autowired
	private UnitMongoDao unitMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	//原产国
	@Autowired
	private CountryOriginMongoDao countryOriginMongoDao;
	//仓库关区
	@Autowired
	DepotCustomsRelMongoDao  depotCustomsRelMongoDao;
	//母品牌
	@Autowired
	private BrandSuperiorMongoDao brandSuperiorMongoDao;
	//模板
	@Autowired
	private FileTempDao fileTempDao;
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao ;
	@Autowired
	private BrandMongoDao brandMongoDao;
	/**
	 * 列表 分页
	 */
	@Override
	public MaterialOrderDTO listDTOByPage(MaterialOrderDTO dto, User user) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return materialOrderDao.listDTOByPage(dto);
	}
	/**
	 * 根据表头id获取表体
	 */
	@Override
	public List<MaterialOrderItemDTO> getItemDetail(Long orderId) throws Exception {
		MaterialOrderItemDTO dto = new MaterialOrderItemDTO();
		dto.setOrderId(orderId);
		return materialOrderItemDao.listDTO(dto);
	}

	/**
	 * 保存
	 */
	@Override
	public String saveOrModifyOrder(String json, User user) throws Exception {
		saveMaterialOrder(json, user,"0");
		return "保存成功";
	}
	/**
	 * 审核
	 */
	@Override
	public String auditMaterialOrder(String json, User user) throws Exception {
		MaterialOrderModel materialOrder = saveMaterialOrder(json, user,"1");
		if(materialOrder != null) {
			String msg = "";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", materialOrder.getOutDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

			// 仓库公司关联表
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", materialOrder.getMerchantId());
			depotMerchantRelParam.put("depotId", materialOrder.getOutDepotId());
			DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
				msg += "单号：" + materialOrder.getCode() + ",仓库ID为：" + materialOrder.getOutDepotId() + ",未查到该公司下调出仓库信息,审核失败\n";
				return msg;
			}

			// 获取领料单表体（商品信息）
			MaterialOrderItemModel materialOrderItem = new MaterialOrderItemModel();
			materialOrderItem.setOrderId(materialOrder.getId());
			List<MaterialOrderItemModel> itemList = materialOrderItemDao.list(materialOrderItem);

			// 若出库仓ISV仓库类型为卓志仓且对应该公司下进出接口指令为是，调用1.49 出库订单推送
			if (DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())
					&& depot.getISVDepotType().equals(DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_03)) {
				OutStoreOrderRootJson root = new OutStoreOrderRootJson();
				root.setContract_no(materialOrder.getContractNo());// 合同号
				root.setOrder_id(materialOrder.getCode()); // 订单编码
				root.setWarehouse_code(depot.getCode()); // 仓库编码
				root.setBusi_scene("20"); // 业务场景类型 20：领料出库
				root.setCreated_time(TimeUtils.formatFullTime(materialOrder.getCreateDate()));
				root.setEbc_code(materialOrder.getTopidealCode());
				root.setCustoms_code(depot.getCustomsNo());
				Map<String, OutStoreOrderItemJson> sumMap = new LinkedHashMap<>();
				for (int k = 0; k < itemList.size(); k++) {
					MaterialOrderItemModel orderItem = itemList.get(k);
					OutStoreOrderItemJson itemTemp = new OutStoreOrderItemJson();
					itemTemp.setIndex(orderItem.getSeq());
					itemTemp.setGood_id(orderItem.getGoodsNo()); // 商品货号
					itemTemp.setAmount(orderItem.getNum()); // 数量
					itemTemp.setGross_weight(orderItem.getGrossWeightSum()); // 毛重
					itemTemp.setNet_weight(orderItem.getNetWeightSum()); // 净重
					HashMap<String, Object> map = new HashMap<>();
					map.put("merchandiseId", orderItem.getGoodsId());
					MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(map);
					if (merchandise.getUnit() != null && merchandise.getUnit().longValue() > 0) {
						HashMap<String, Object> map2 = new HashMap<>();
						map2.put("unitId", merchandise.getUnit());
						UnitMongo unit = unitMongoDao.findOne(map2);
						itemTemp.setContracts_unit(unit.getCode()); // 单位
					}
					// 根据商品id查询商品母品牌 获取申报系数
					BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(orderItem.getGoodsId());

					// 申报单价*商家商品备案价申报系数
					itemTemp.setUnit_price(merchandise.getFilingPrice().multiply(new BigDecimal(brandSuperiorMongo.getPriceDeclareRatio())).doubleValue()); // 单价

					sumMap.put(itemTemp.getGood_id(), itemTemp);
				}
				try {
					if (!sumMap.isEmpty()) {
						root.setGood_list(new ArrayList<>(sumMap.values()));
					}
					JSONObject jsonObject = JSONObject.fromObject(root);
					jsonObject.put("method", EpassAPIMethodEnum.EPASS_E08_METHOD.getMethod());
					jsonObject.put("topideal_code", materialOrder.getTopidealCode());
					SendResult result = rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
					System.out.println("领料单审核发送出库订单消息返回：" + result.toString());
				} catch (Exception e) {
					msg += "单号：" + materialOrder.getCode() + ",推送异常\n";
					return msg;
				}
			} else if (DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())
					&& depot.getISVDepotType().equals(DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_04)) {
				// 若出库仓 ISV仓库类型为海外仓且对应该公司下进出接口指令为是，调用1.17 销售出仓
				SalesOutStoreOrderRoot root = new SalesOutStoreOrderRoot();
				root.setOrder_id(materialOrder.getCode());
				root.setWarehouse_id(depot.getCode());
				root.setDestion_code(materialOrder.getDestination()); // 目的地
				root.setBusi_mode("10"); // 进口模式 10：B2B
				Integer totalNum = 0;
				Map<String, SalesOutStoreGoodsItem> sumMap = new LinkedHashMap<>();
				for (int i = 0; i < itemList.size(); i++) {
					MaterialOrderItemModel item = itemList.get(i);
					Integer transferNum = item.getNum();
					SalesOutStoreGoodsItem itemRequest = new SalesOutStoreGoodsItem();
					itemRequest.setGoods_id(item.getGoodsNo());// 调出货号
					itemRequest.setGoods_name(item.getGoodsName()); // 商品名称
					itemRequest.setAmount(transferNum);// 数量
					itemRequest.setUom(materialOrder.getTallyingUnit());// 理货单位
					totalNum += transferNum;

					sumMap.put(itemRequest.getGoods_id(), itemRequest);
				}
				root.setTotal_num(totalNum);
				if (!sumMap.isEmpty()) {
					root.setGoods_list(new ArrayList<>(sumMap.values()));
				}
				// 收件人信息
				SalesOutStoreRecipient recipient = new SalesOutStoreRecipient();
				recipient.setName(materialOrder.getReceiverName());// 收件人姓名
				recipient.setCountry(materialOrder.getCountry());// 收件人国家
				recipient.setAddress(materialOrder.getReceiverAddress());// 收件人地址
				root.setRecipient(recipient);

				try {
					com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(root);
					jsonObject.put("method", EpassAPIMethodEnum.EPASS_E03_METHOD.getMethod());
					jsonObject.put("topideal_code", materialOrder.getTopidealCode());
					SendResult result = rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
					System.out.println("领料审核发送领料出仓消息返回：" + result.toString());
					if (result == null) {
						msg += "单号：" + materialOrder.getCode() + ",领料单审核提交审核领料单出仓服务异常\n";
						return msg;
					}
					if (!result.getSendStatus().name().equals("SEND_OK")) {// SEND_OK-成功
						msg += "单号：" + materialOrder.getCode() + ",领料单审核提交审核发送领料单出仓失败\n";
						return msg;
					}
				} catch (Exception e) {
					msg += "单号：" + materialOrder.getCode() + ",推送异常\n";
					return msg;
				}
			}

			// 出库仓不为中转仓，推库存冻结
			if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
					// 冻结库存
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String now = sdf.format(new Date());
					InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
					inventoryFreezeRootJson.setMerchantId(materialOrder.getMerchantId().toString());
					inventoryFreezeRootJson.setMerchantName(materialOrder.getMerchantName());
					inventoryFreezeRootJson.setDepotId(materialOrder.getOutDepotId().toString());
					inventoryFreezeRootJson.setDepotName(materialOrder.getOutDepotName());
					inventoryFreezeRootJson.setOrderNo(materialOrder.getCode());
					inventoryFreezeRootJson.setBusinessNo(materialOrder.getCode());
					inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0021);
					inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033);
					inventoryFreezeRootJson.setSourceDate(now);
					inventoryFreezeRootJson.setOperateType("0");
					List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
					for (MaterialOrderItemModel item : itemList) {
						InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

						inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
						inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
						inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());


						inventoryFreezeGoodsListJson.setDivergenceDate(now);
						inventoryFreezeGoodsListJson.setNum(item.getNum());
						// 海外仓冻结加理货单位
						if ("c".equals(depot.getType())) {
							if ("00".equals(materialOrder.getTallyingUnit())) {
								inventoryFreezeGoodsListJson.setUnit("0");
							} else if ("01".equals(materialOrder.getTallyingUnit())) {
								inventoryFreezeGoodsListJson.setUnit("1");
							} else if ("02".equals(materialOrder.getTallyingUnit())) {
								inventoryFreezeGoodsListJson.setUnit("2");
							}
						}
						inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
					}
					inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
					rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(),
							MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),
							MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			}

			// 修改单据状态
			materialOrder.setState(DERP_ORDER.MATERIALORDER_STATE_003);
			materialOrder.setAuditDate(TimeUtils.getNow());
			materialOrder.setAuditor(user.getId());
			materialOrder.setAuditName(user.getName());
			int flag = materialOrderDao.modify(materialOrder);

			if (flag == 0) {
				msg += "单号：" + materialOrder.getCode() + ",审核出错\n";
			}
			return msg;
		}
		return "审核成功";
	}

	private MaterialOrderModel saveMaterialOrder(String json, User user,String operate) throws Exception {
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long merchantId = Long.valueOf(jsonObj.getString("merchantId"));//公司
		String merchantName = (String) jsonObj.get("merchantName");

		String buIdStr = jsonObj.getString("buId"); // 事业部
		if (StringUtils.isBlank(buIdStr)) {
			throw new RuntimeException("保存失败，请选择事业部");
		}
		Long buId = Long.valueOf(buIdStr); // 事业部ID
		// 获取该事业部信息
		Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
		merchantBuRelParam.put("merchantId", merchantId);
		merchantBuRelParam.put("buId", buId);
		merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
		if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
			throw new RuntimeException("事业部ID为：" + buId + ",未查到该公司下事业部信息");
		}

		// 用户事业部
		boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), buId);
		if (!userRelateBu) {
			throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
		}
		Long id = null;//领料单id ，编辑时存在
		if(jsonObj.containsKey("id") && StringUtils.isNotBlank(jsonObj.getString("id"))) {
			id = Long.valueOf(jsonObj.getString("id"));
		}
		Long customerId = null;
		//客户信息
		CustomerInfoMogo customer = null;
		if (StringUtils.isNotBlank(jsonObj.getString("customerId"))) {
			customerId = Long.valueOf(jsonObj.getString("customerId"));
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("customerid", customerId);
			customer = customerInfoMongoDao.findOne(params2);
		}
		MaterialOrderModel materialOrderModel = new MaterialOrderModel();
		if (id != null) {
			materialOrderModel = materialOrderDao.searchById(id);
			// 判断状态是不是已审核
			if (DERP_ORDER.MATERIALORDER_STATE_003.equals(materialOrderModel.getState())) {
				throw new RuntimeException("保存失败，该领料单已审核");
			}
		}
		Long outDepotId = null;//出库仓
		DepotInfoMongo outDepot = null;
		if (StringUtils.isNotBlank(jsonObj.getString("outDepotId"))) {
			outDepotId = Long.valueOf(jsonObj.getString("outDepotId"));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", outDepotId);
			outDepot = depotInfoMongoDao.findOne(params);
		}
		Long inDepotId = null;//入库仓
		DepotInfoMongo inDepot = null;
		if (jsonObj.containsKey("inDepotId") && jsonObj.getString("inDepotId") != null && !"".equals(jsonObj.getString("inDepotId"))) {
			inDepotId = Long.valueOf(jsonObj.getString("inDepotId"));
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", inDepotId);
			inDepot = depotInfoMongoDao.findOne(params1);
		}
		String deliveryDate = (String) jsonObj.get("deliveryDate");//交货日期
		String tallyingUnit = (String) jsonObj.get("tallyingUnit");//理货单位
		String contractNo = (String) jsonObj.get("contractNo");//合同号
		String remark = (String) jsonObj.get("remark");//备注
		String isSameArea = (String) jsonObj.get("isSameArea"); //是否同关区
		String outdepotAddr = (String) jsonObj.get("outdepotAddr");// 出库地点
		Double billWeightD = 0.0;// 提单毛重
		if ((String) jsonObj.get("billWeight") != null && !"".equals(jsonObj.get("billWeight"))) {
			billWeightD = Double.valueOf((String) jsonObj.get("billWeight"));
		}
		String transport = (String) jsonObj.get("transport");// 运输方式
		String teu = (String) jsonObj.get("teu");// 标准箱TEU
		String trainno = (String) jsonObj.get("trainno");// 车次
		String torusNumberStr = (String) jsonObj.get("torusNumber");// 托数
		Integer torusNumber = null;//托数
		if (StringUtils.isNotBlank(torusNumberStr)) {
			torusNumber = Integer.valueOf(torusNumberStr);
		}
		String carrier = (String) jsonObj.get("carrier");// 承运商
		// 目的地和收件信息
		String country = (String) jsonObj.get("country");//国家
		String destination = (String) jsonObj.get("destination");//目的地
		String receiverName = (String) jsonObj.get("receiverName");//收货人
		String receiverAddress = (String) jsonObj.get("receiverAddress");//收货地址
		String mailMode = (String) jsonObj.get("mailMode");

		String poNo = (String) jsonObj.get("poNo"); // 获取所有的po号
		String boxNumStr = (String) jsonObj.get("boxNum");// 箱数str
		Integer boxNum = null;//箱数
		if (StringUtils.isNotBlank(boxNumStr)) {
			boxNum = Integer.valueOf(boxNumStr);
		}
		String torrPackType = (String) jsonObj.get("torrPackType");// 托盘材质
		String pack = (String) jsonObj.get("pack");// 包装
		String invoiceNo = (String) jsonObj.get("invoiceNo");// 发票单号
		String portDes = (String) jsonObj.get("portDes");// 卸货港
		//出仓关区
		Long outCustomsId = null;
		DepotCustomsRelMongo outDepotCustomsRelMongo = null;
		if (outDepot != null && jsonObj.containsKey("outCustomsId") && jsonObj.getString("outCustomsId") != null && !"".equals(jsonObj.getString("outCustomsId"))) {
			outCustomsId = Long.valueOf(jsonObj.getString("outCustomsId"));
			Map<String, Object> outDepotCustomsRelMap = new HashMap<String, Object>();
			outDepotCustomsRelMap.put("depotId", outDepotId);
			outDepotCustomsRelMap.put("customsAreaId", outCustomsId);
			outDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(outDepotCustomsRelMap);//平台关区信息
			if(outDepotCustomsRelMongo == null) {
				throw new RuntimeException("保存失败，出仓仓库："+ outDepot.getName() +" 未关联选中平台关区");
			}
		}
		//入仓关区
		Long inCustomsId = null;
		DepotCustomsRelMongo inDepotCustomsRelMongo = null;
		if (inDepot != null && jsonObj.containsKey("inCustomsId") && jsonObj.getString("inCustomsId") != null && !"".equals(jsonObj.getString("inCustomsId"))) {
			inCustomsId = Long.valueOf(jsonObj.getString("inCustomsId"));
			Map<String, Object> inDepotCustomsRelMap = new HashMap<String, Object>();
			inDepotCustomsRelMap.put("depotId", inDepotId);
			inDepotCustomsRelMap.put("customsAreaId", inCustomsId);
			inDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(inDepotCustomsRelMap);
			if(inDepotCustomsRelMongo == null) {
				throw new RuntimeException("保存失败，入仓仓库："+ inDepot.getName() +" 未关联选中平台关区");
			}
		}
		// 仓库公司关联表
		DepotMerchantRelMongo depotMerchantRelMongo= null;
		if (null != outDepot) {
			if(!DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_1.equals(outDepot.getIsValetManage())) {
				throw new RuntimeException("保存失败，出仓仓库："+outDepot.getName()+" 只能为代客管理仓");
			}
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", merchantId);
			merchantDepotBuRelParam.put("depotId", outDepot.getDepotId()); // 出仓仓库
			merchantDepotBuRelParam.put("buId", buId);
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao
					.findOne(merchantDepotBuRelParam);
			if (outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)) {
				throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",出仓仓库："
						+ outDepot.getDepotCode() + ",未查到该公司仓库事业部关联信息");
			}
			// 仓库公司关联表
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", merchantId);
			depotMerchantRelParam.put("depotId", outDepotId);
			depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
				throw new RuntimeException("仓库ID为：" + outDepotId + ",未查到该公司下调出仓库信息");
			}
		}
		String isInOutInstruction = null;
		if(depotMerchantRelMongo != null) {
			isInOutInstruction = depotMerchantRelMongo.getIsInOutInstruction();// 进出接口指令 1-是 0 - 否
		}
		//operate 0-保存 1-审核 ，审核时做必填校验
		if("1".equals(operate) ) {
			if (outDepotId == null) {
				throw new RuntimeException("保存失败，请选择出仓仓库");
			}
			if (StringUtils.isBlank(isSameArea)) {
					throw new RuntimeException("保存失败，是否同关区不能为空");
			} else {
				// 是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库；
				if (DERP.ISSAMEAREA_1.equals(isSameArea)) {
					if (inDepotId == null) {
						throw new RuntimeException("保存失败，入库仓库不能为空");
					} else if (inDepot == null) {
						throw new RuntimeException("保存失败，没有找到入库仓库信息");
					} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
						throw new RuntimeException("保存失败，入库仓库仅能为备查库");
					}
				}

			}

			if (StringUtils.isBlank(poNo)) {
				throw new RuntimeException("保存失败，PO单号不能为空");
			}
			if (StringUtils.isBlank(contractNo)) {
				throw new RuntimeException("保存失败，合同号不能为空");
			}

			// 出库仓时海外仓时：目的地不能为空,邮寄方式不能为空;邮寄方式为1.寄售时：收件人姓名、国家、海外仓详细地址不能为空
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())) {
				if (StringUtils.isBlank(destination)) {
					throw new RuntimeException("出库仓时海外仓目的地不能为空");
				} else if (StringUtils.isBlank(mailMode)) {
					throw new RuntimeException("出库仓时海外仓邮寄方式不能为空");
				}
				if (DERP_ORDER.SALEORDER_MAILMODE_1.equals(mailMode)) {
					if (StringUtils.isBlank(receiverName)) {
						throw new RuntimeException("出库仓时海外仓收件人姓名不能为空");
					} else if (StringUtils.isBlank(country)) {
						throw new RuntimeException("出库仓时海外仓国家不能为空");
					} else if (StringUtils.isBlank(receiverAddress)) {
						throw new RuntimeException("出库仓时海外仓详细地址不能为空");
					}
				}
			}
			// 标准箱TEU 当运输方式为海运时必填 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
			if (DERP.TRANSPORT_2.equals(transport)) {
				if (StringUtils.isBlank(teu)) {
					throw new RuntimeException("标准箱TEU不能为空");
				}
			} else if (DERP.TRANSPORT_3.equals(transport) || DERP.TRANSPORT_4.equals(transport)) {// 当运输方式为陆运、港到仓拖车时必填
				if (StringUtils.isBlank(trainno)) {
					throw new RuntimeException("车次不能为空");
				}
			}
			if(StringUtils.isBlank(torusNumberStr)) {
				throw new RuntimeException("托数不能为空");
			}
			if(StringUtils.isBlank(boxNumStr)) {
				throw new RuntimeException("箱数不能为空");
			}
			if(StringUtils.isBlank(torrPackType)) {
				throw new RuntimeException("托盘材质不能为空");
			}
			if(StringUtils.isBlank(pack)) {
				throw new RuntimeException("包装不能为空");
			}
			if(StringUtils.isBlank(invoiceNo)) {
				throw new RuntimeException("发票单号不能为空");
			}

		}

		// 保存表头数据
		materialOrderModel.setMerchantId(merchantId);
		materialOrderModel.setMerchantName(merchantName);
		materialOrderModel.setBuId(buId); // 事业部ID
		materialOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
		materialOrderModel.setCustomerId(customerId);
		materialOrderModel.setCustomerName(customer != null ? customer.getName(): "");
		materialOrderModel.setDeliveryDate(TimeUtils.parseDay(deliveryDate));
		materialOrderModel.setPoNo(poNo.trim());
		materialOrderModel.setOutdepotAddr(outdepotAddr);// 出库地点
		materialOrderModel.setBillWeight(billWeightD);
		materialOrderModel.setTransport(transport);
		materialOrderModel.setTeu(teu);
		materialOrderModel.setTrainno(trainno);
		materialOrderModel.setCarrier(carrier);
		materialOrderModel.setTorusNumber(torusNumber);// 托数
		materialOrderModel.setBoxNum(boxNum);// 箱数
		materialOrderModel.setTorrPackType(torrPackType);// 托盘材质
		materialOrderModel.setPack(pack);// 包装
		materialOrderModel.setInvoiceNo(invoiceNo);// 发票单号
		materialOrderModel.setPortDes(portDes);// 卸货港
		materialOrderModel.setIsSameArea(isSameArea);
		materialOrderModel.setContractNo(contractNo);
		materialOrderModel.setTallyingUnit(tallyingUnit);
		materialOrderModel.setRemark(remark);
		materialOrderModel.setCountry(country);
		materialOrderModel.setDestination(destination);
		materialOrderModel.setReceiverName(receiverName);
		materialOrderModel.setReceiverAddress(receiverAddress);
		materialOrderModel.setMailMode(mailMode);
		materialOrderModel.setTopidealCode(user.getTopidealCode());

		if(outDepot != null) {
			materialOrderModel.setOutDepotId(outDepotId);
			materialOrderModel.setOutDepotName(outDepot.getName());
			materialOrderModel.setOutDepotCode(outDepot.getCode());
			materialOrderModel.setInspectNo(outDepot.getInspectNo());
			materialOrderModel.setCustomsNo(outDepot.getCustomsNo());
		}
		if (inDepot != null) {
			materialOrderModel.setInDepotId(inDepotId);
			materialOrderModel.setInDepotName(inDepot.getName());
			materialOrderModel.setInDepotCode(inDepot.getCode());
		}

		if(outDepotCustomsRelMongo != null) {
			materialOrderModel.setOutCustomsId(outCustomsId);
			materialOrderModel.setOutCustomsCode(outDepotCustomsRelMongo.getCustomsAreaCode());
			materialOrderModel.setOutPlatformCustoms(outDepotCustomsRelMongo.getCustomsAreaName());
		}
		if(inDepotCustomsRelMongo != null) {
			materialOrderModel.setInCustomsId(inCustomsId);
			materialOrderModel.setInCustomsCode(inDepotCustomsRelMongo.getCustomsAreaCode());
			materialOrderModel.setInPlatformCustoms(inDepotCustomsRelMongo.getCustomsAreaName());
		}

		// 调入客户卓志编码
		String inCustomerTopNo = "";
		if (customerId != null && customerId.longValue() > 0) {
			Map<String, Object> cMap = new HashMap<String, Object>();
			cMap.put("customerid", customerId);
			CustomerInfoMogo inCustomerInfo = customerInfoMongoDao.findOne(cMap);

			if (inCustomerInfo != null)
				inCustomerTopNo = inCustomerInfo.getMainId();
		}
		// 同关区
		boolean isSame = outDepot != null && !StringUtils.isEmpty(outDepot.getCustomsNo()) && inDepot != null
				&& !StringUtils.isEmpty(inDepot.getCustomsNo())	&& outDepot.getCustomsNo().equals(inDepot.getCustomsNo());

		// 1.调出调入公司不同,出和入的仓库编码相同 -业务场景：40 ：账册内货权转移 服务类型：G0：库内调拨服务
		if (!user.getTopidealCode().equals(inCustomerTopNo) && outDepot != null && inDepot != null && outDepot.getCode().equals(inDepot.getCode())) {
			materialOrderModel.setModel("40");
			materialOrderModel.setServeTypes("G0");
		}
		// 2.调出调入公司不同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：50： 账册内货权转移调仓 服务类型：E0：区内调拨调出服务
		else if (!user.getTopidealCode().equals(inCustomerTopNo) && isSame && outDepot != null && !outDepot.getCode().equals(inDepot.getCode())) {
			materialOrderModel.setModel("50");
			materialOrderModel.setServeTypes("E0");
		}
		// 3.调出调入公司相同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：10： 账册内调仓 服务类型：E0：区内调拨调出服务
		else if (user.getTopidealCode().equals(inCustomerTopNo) && isSame && outDepot != null && !outDepot.getCode().equals(inDepot.getCode())) {
			materialOrderModel.setModel("10");
			materialOrderModel.setServeTypes("E0");
		}
		// 4.调出调入公司相同，出入仓库不同关区，出仓和入库仓库编码不同-业务场景：10 ：账册内调仓 服务类型：E0：区内调拨调出服务
		else if (user.getTopidealCode().equals(inCustomerTopNo) && !(isSame || (outDepot != null && StringUtils.isEmpty(outDepot.getCustomsNo()) &&
				inDepot != null	&& StringUtils.isEmpty(inDepot.getCustomsNo()))) && !outDepot.getCode().equals(inDepot.getCode())) {
			materialOrderModel.setModel("10");
			materialOrderModel.setServeTypes("E0");
		}
		// 解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		Integer totalNum = 0;
		//审核时 商品信息不能为空
		if("1".equals(operate) && (itemArr == null || itemArr.size() <= 0)) {
			throw new RuntimeException("商品信息不能为空");
		}
		List<MaterialOrderItemModel> itemList = new ArrayList<MaterialOrderItemModel>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsCode = (String) job.get("goodsCode");
			String goodsName = (String) job.get("goodsName");
			String goodsNo = (String) job.get("goodsNo");
			String barcode = (String) job.get("barcode");

			String seq = (String) job.get("seq");

			String numStr = (String) job.getString("num");
			Integer num1 = Integer.valueOf(numStr.trim());
			String brandName = (String) job.get("brandName");
			String boxNo = (String) job.get("boxNo");
			String itemContractNo = (String) job.get("contractNo");
			totalNum += num1;
			// 注入数据
			MaterialOrderItemModel itemModel = new MaterialOrderItemModel();
			if (id != null && job.containsKey("id") && StringUtils.isNotBlank(job.getString("id"))) {// 修改
				Long itemId = Long.valueOf(job.getString("id"));
				itemModel.setId(itemId);
			}
			itemModel.setBarcode(barcode);
			itemModel.setBrandName(brandName);
			itemModel.setBoxNo(boxNo);
			itemModel.setContractNo(itemContractNo);
			itemModel.setGoodsCode(goodsCode);
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsName(goodsName);
			itemModel.setGoodsNo(goodsNo);
			itemModel.setNum(num1);
			itemModel.setOrderId(id);
			itemModel.setTallyingUnit(tallyingUnit);
			if (StringUtils.isNotBlank(seq)) {
				itemModel.setSeq(Integer.valueOf(seq));
			} else {
				itemModel.setSeq(i + 1);
			}

			// 获取商品信息
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(paramMap);
			if (merchandise == null) { // 商品不存在修改失败
				throw new RuntimeException("商品ID为：" + goodsId + "，该商品不存在");
			}
			itemModel.setCommbarcode(merchandise.getCommbarcode()); // 标准条码

			if (job.get("grossWeight") != null && !"".equals(job.get("grossWeight"))) {
				Double grossWeight = Double.valueOf((String) job.get("grossWeight"));
				itemModel.setGrossWeight(grossWeight);
			}
			if (job.get("netWeight") != null && !"".equals(job.get("netWeight"))) {
				Double netWeight = Double.valueOf((String) job.get("netWeight"));
				itemModel.setNetWeight(netWeight);
			}
			if (job.get("grossWeightSum") != null && !"".equals(job.get("grossWeightSum"))) {
				Double grossWeightSum = Double.valueOf((String) job.get("grossWeightSum"));
				itemModel.setGrossWeightSum(grossWeightSum);
			}
			if ((String) job.get("netWeightSum") != null && !"".equals(job.get("netWeightSum"))) {
				Double netWeightSum = Double.valueOf((String) job.get("netWeightSum"));
				itemModel.setNetWeightSum(netWeightSum);
			}
			// 当出库仓配置-公司仓库进出接口指令为是时，“申报单价”，“毛重”，“净重”字段为必填项
			if (StringUtils.isNotBlank(isInOutInstruction) && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(isInOutInstruction)) {
				if (itemModel.getNetWeightSum() == null) {
					throw new RuntimeException("净重不能为空");
				}
				if (itemModel.getGrossWeightSum() == null ) {
					throw new RuntimeException("毛重不能为空");
				}
			}
			itemList.add(itemModel);
		}
		materialOrderModel.setTotalNum(totalNum);

		//新增保存
		if(id == null && "0".equals(operate)) {
			materialOrderModel.setState(DERP_ORDER.SALEORDER_STATE_001);//待审核
		}else if("1".equals(operate)) {//审核
			materialOrderModel.setState(DERP_ORDER.SALEORDER_STATE_002);//审核中
		}

		List<Long> itemIds = new ArrayList<Long>();
		Long num = null;//记录新增id
		if(id != null) {
			materialOrderModel.setId(id);
			materialOrderModel.setModifyDate(TimeUtils.getNow());
			materialOrderModel.setModifier(user.getId());
			materialOrderModel.setModifierUsername(user.getName());
			materialOrderDao.modify(materialOrderModel);
		}else {
			materialOrderModel.setCreateDate(TimeUtils.getNow());
			materialOrderModel.setCreater(user.getId());
			materialOrderModel.setCreateName(user.getName());
			materialOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LLD));// 领料单号;
			num = materialOrderDao.save(materialOrderModel);//记录新增id
		}

		for(MaterialOrderItemModel itemModel: itemList) {
			if(itemModel.getId() != null) {//编辑
				materialOrderItemDao.modify(itemModel);
				itemIds.add(itemModel.getId());
			}else {// 新增
				if(num == null) {//编辑、审核的时候 添加商品
					num = id;
				}
				itemModel.setOrderId(num);
				Long itemId = materialOrderItemDao.save(itemModel);
				itemIds.add(itemId);
			}
		}
		List<Long> reportIds = new ArrayList<>();
		if (id != null && itemIds.size() > 0) { // 如果是编辑
			// 根据表头Id删除不要的数据（除itemIds之外的数据）
			// 根据表头Id查询的数据（除itemIds之外的数据）
			List<MaterialOrderItemModel> reportDelItemList = materialOrderItemDao.getListByBesidesIds(itemIds, id);
			for (MaterialOrderItemModel saleOrderItemModel : reportDelItemList) {
				reportIds.add(saleOrderItemModel.getId());
			}
			// 批量删除数据,根据表头ID删除表体
			materialOrderItemDao.delBesidesIds(itemIds, id);
		}

		return materialOrderModel;
	}

	@Override
	public List<MaterialOrderDTO> listMaterialOrder(MaterialOrderDTO dto, User user) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return null;
			}
			dto.setBuList(buList);
		}
		return materialOrderDao.listDTO(dto);
	}

	/**
	 * 删除
	 */
	@Override
	public boolean delMaterialOrder(List ids) throws Exception {
		boolean flag = false;
		// 判断是否有已审核的订单
		for (Object id : ids) {
			// 获取销售订单信息
			MaterialOrderModel materialModel = materialOrderDao.searchById(Long.parseLong(id.toString()));
			// 单据状态不是待审核，
			if (materialModel.getState() != null && !materialModel.getState().equals(DERP_ORDER.SALEORDER_STATE_001)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			throw new RuntimeException("删除失败，存在状态不为待审核的订单");
		}
		int num = materialOrderDao.delMaterialOrder(ids);
		if (num >= 1) {
			return true;
		}
		return false;
	}
	@Override
	public MaterialOrderDTO searchDetail(Long id) throws Exception {
		MaterialOrderDTO dto = new MaterialOrderDTO();
		dto.setId(id);
		return materialOrderDao.getMaterialOrderDTO(dto);
	}

	/**
	 * 导出清关资料
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> exportDepotCustomsDeclares(Long id, List<Long> fileTempIds,String type) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		//仓库模板
		if (fileTempIds == null || fileTempIds.isEmpty()) {
			return null ;
		}

		MaterialOrderModel model = materialOrderDao.searchById(id);
		if (model == null) {
			return null ;
		}

		for (Long fileTempId : fileTempIds) {
			FileTempModel fileTempModel = fileTempDao.searchById(fileTempId);
			if (fileTempModel == null || StringUtils.isEmpty(fileTempModel.getToUrl())) {
				continue;
			}

			if ( DERP_ORDER.TEMP_CUSTOMSFILECONFIG_ZONGHEYICANG.equals(fileTempModel.getToUrl())) {
				Map<String, Object> firstCustomsInfo = getZONGHEYICANGFirstCustomsInfoDTO(id);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			}
		}

		return resultMap;
	}
	/**
	 * 封装综合一仓模板实体
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getZONGHEYICANGFirstCustomsInfoDTO(Long id) throws Exception{
		MaterialOrderDTO materialOrderDTO = new MaterialOrderDTO();
		materialOrderDTO.setId(id);
		materialOrderDTO = materialOrderDao.getMaterialOrderDTO(materialOrderDTO);
		MaterialOrderItemModel itemModel = new MaterialOrderItemModel();
		itemModel.setOrderId(id);
		List<MaterialOrderItemModel> itemList = materialOrderItemDao.list(itemModel);

		//根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", materialOrderDTO.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantParams);

		//根据“公司+仓库”获取关系表
		Map<String, Object> depotMerchantParams = new HashMap<String, Object>();
		depotMerchantParams.put("depotId", materialOrderDTO.getOutDepotId());
		depotMerchantParams.put("merchantId", materialOrderDTO.getMerchantId());
		DepotMerchantRelMongo depotMerchant = depotMerchantRelMongoDao.findOne(depotMerchantParams);

		//根据“入库仓库+关区”获取关系表
		Map<String, Object> depotCustomsParams = new HashMap<String, Object>();
		depotCustomsParams.put("depotId", materialOrderDTO.getInDepotId());
		depotCustomsParams.put("customsAreaId", materialOrderDTO.getInCustomsId());
		DepotCustomsRelMongo depotcustoms = depotCustomsRelMongoDao.findOne(depotCustomsParams);

		Map<String,Object> dto = new HashMap<>();
		if(materialOrderDTO != null) {
			if(depotMerchant != null) {
				dto.put("signNo",depotMerchant.getContractCode());// 合同号
			}

			dto.put("contractNo",materialOrderDTO.getContractNo());//合同号
			dto.put("signingDate",materialOrderDTO.getCreateDate());//签订日期
			dto.put("signingAddr","中国广州市");//签订地点
			dto.put("firstParty",merchant.getFullName());//甲方 公司全名
			dto.put("firstPartyAddr",merchant.getRegisteredAddress());//甲方地址
			dto.put("secondParty","广东卓志跨境电商供应链服务有限公司");//乙方
			dto.put("requirement","无");//特殊操作要求
			dto.put("payment","T/T");//付款方式
			dto.put("portDest",materialOrderDTO.getPortDes());//卸货港
			dto.put("payRules",materialOrderDTO.getPayRules());//付款条约
			dto.put("mark","N/M ");//唛头
			if(depotcustoms != null) {
				dto.put("eAddressee",depotcustoms.getRecCompanyEnname());//Consignee
				dto.put("addressee",depotcustoms.getRecCompanyName());//收货人
			}
			dto.put("shipDate",materialOrderDTO.getCreateDate());//装船时间
			dto.put("portLoading",materialOrderDTO.getOutdepotAddr());//装货港
			dto.put("consignee","NO");//境外收货人
			dto.put("invoiceNo",materialOrderDTO.getInvoiceNo());//发票号
			dto.put("pack",materialOrderDTO.getPack());//包装

			Set<String> originCountrySet = new HashSet<String>();
			List<Map<String,Object>> customsItemList = new ArrayList<>();
			Integer totalNum = 0;
			Double totalGrossWeight = 0.0;
			Double totalNetWeight = 0.0;
			for(MaterialOrderItemModel item : itemList) {
				Map<String,Object> customsItem = new HashMap<>();
				customsItem.put("goodsNo",item.getGoodsNo());//商品货号
				customsItem.put("goodsName",item.getGoodsName());//商品名称
				customsItem.put("num",item.getNum());//商品数量
				customsItem.put("totalNetWeight",item.getNetWeightSum() == null ? 0.0 : item.getNetWeightSum());// 毛重
				customsItem.put("totalGrossWeight",item.getGrossWeightSum() == null ? 0.0 : item.getGrossWeightSum());// 净重
				//根据商品id获取商品信息
				Map<String, Object> merchandiseParams = new HashMap<String, Object>();
				merchandiseParams.put("merchandiseId", item.getGoodsId());
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseParams);
				// 根据商品id查询商品母品牌 获取申报系数
				Double priceDeclareRatio = 1.0;
				BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(item.getGoodsId());
				if(brandSuperiorMongo != null && brandSuperiorMongo.getPriceDeclareRatio() != null) {
					priceDeclareRatio = brandSuperiorMongo.getPriceDeclareRatio();
				}
				BigDecimal price = merchandise.getFilingPrice().multiply(new BigDecimal(priceDeclareRatio)).setScale(2, BigDecimal.ROUND_HALF_UP);
				customsItem.put("price",price);//单价

				if (merchandise.getUnit() != null) {
					// 根据单位id获取单位信息
					Map<String, Object> unitParams = new HashMap<String, Object>();
					unitParams.put("unitId", merchandise.getUnit());
					UnitMongo unit = unitMongoDao.findOne(unitParams);
					if (unit != null) {
						customsItem.put("unit",unit.getName());// 单位
					}

				}
				// 根据国家id获取国家信息
				if (merchandise.getCountyId() != null) {
					Map<String, Object> countryParams = new HashMap<String, Object>();
					countryParams.put("countryOriginId", merchandise.getCountyId());
					CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);
					if (countryOrigin != null) {
						customsItem.put("countryName",countryOrigin.getName());// 原产国
						originCountrySet.add(countryOrigin.getName());
					}
				}
				customsItem.put("goodsSpec",merchandise.getSpec());// 商品规格
				customsItem.put("constituentRatio",merchandise.getComponent());// 成分占比
				customsItemList.add(customsItem);

				totalNum = totalNum + item.getNum();
				totalGrossWeight = totalGrossWeight + (item.getGrossWeightSum() == null ? 0.0 : item.getGrossWeightSum());
				totalNetWeight = totalNetWeight +(item.getNetWeightSum() == null ? 0.0 : item.getNetWeightSum());
			}
			String originCountry = "";
			if(originCountrySet != null && originCountrySet.size() > 0) {
				originCountry = StringUtils.join(originCountrySet, ";");
			}

			dto.put("country",originCountry);// 原产国 用分号隔开
			dto.put("itemList",customsItemList);
			dto.put("totalNum",totalNum);
			dto.put("totalGrossWeight",totalGrossWeight);
			dto.put("totalNetWeight",totalNetWeight);
		}

		return dto;
	}
	/**
	 * 导入领料单
	 */
	@Override
	public Map importMaterialOrder(List<List<Map<String, String>>> data, User user) throws Exception {
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
		// ps:序号是表头与表体关联的标识 序号与表头是1对1，表头与表体是1对多
		Map<String, MaterialOrderModel> modelMap = new HashMap<String, MaterialOrderModel>();
		Map<String, List<MaterialOrderItemModel>> itemMap = new HashMap<String, List<MaterialOrderItemModel>>();
		// 用于统计领料订单的总商品数量
		Map<String, Integer> totalMap = new HashMap<String, Integer>();
		// 检查某个领料订单编号+该仓库公司关联的信息
		Map<String, DepotMerchantRelMongo> checkGoodsNoByDepotMap = new HashMap<>();
		// 检查某个领料订单编号+该领料订单下的商品数量
		Map<String, Integer> goodsNumMap = new HashMap<String, Integer>();
		//记录单据总净重
		Map<String, Double> orderNetWeightSumMap = new HashMap<String, Double>();
		// 出仓仓库信息
		DepotInfoMongo outDepotInfo = null;
		for (int i = 0; i < 1; i++) {// 表头
			List<Map<String, String>> objects = data.get(i);
			for (int j = 1; j < objects.size()+1; j++) {
				Map<String, String> msg = new HashMap<String, String>();
				Map<String, String> map = objects.get(j-1);
				String index = map.get("序号");
				if (StringUtils.isBlank(index)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "领料单序号不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				String outDepotCode = map.get("出仓仓库自编码");
				if (StringUtils.isBlank(outDepotCode)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "出仓仓库自编码不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 获取出仓仓库信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("depotCode", outDepotCode);
				params.put("status", "1");
				outDepotInfo = depotInfoMongoDao.findOne(params);
				if (null == outDepotInfo) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "出仓仓库不存在或未启用");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 仓库公司关联表
				Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
				depotMerchantRelParam.put("merchantId", user.getMerchantId());
				depotMerchantRelParam.put("depotId", outDepotInfo.getDepotId());
				DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao
						.findOne(depotMerchantRelParam);
				if (depotMerchantRelMongo == null ) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "仓库ID为：" + outDepotInfo.getDepotId() + ",未查到该公司下调出仓库信息");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				String key = index;
				if (!checkGoodsNoByDepotMap.containsKey(key)) {
					checkGoodsNoByDepotMap.put(key, depotMerchantRelMongo);
				}
				String buCode = map.get("事业部");
				if (StringUtils.isBlank(buCode)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "事业部不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 获取该事业部信息
				Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
				merchantBuRelParam.put("merchantId", user.getMerchantId());
				merchantBuRelParam.put("buCode", buCode);
				merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
				MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
				if (merchantBuRelMongo == null ) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "事业部编码为：" + buCode + ",未查到该公司下事业部信息");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 校验公司-仓库-事业部的关联表
				Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
				merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
				merchantDepotBuRelParam.put("depotId", outDepotInfo.getDepotId()); // 出仓仓库
				merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
				MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
				if (outMerchantDepotBuRelMongo == null) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "事业部编码为：" + merchantBuRelMongo.getBuCode() + ",出仓仓库：" + outDepotCode+ ",未查到该公司仓库事业部关联信息");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 校验事业部与当前账号所绑定的事业部是否匹配
				boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
				if (!userRelateBu) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg","事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				String poNo = map.get("PO单号");
				if(StringUtils.isBlank(poNo)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "PO单号不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				poNo = poNo.trim() ;

				String customerName = map.get("客户");
				if(StringUtils.isBlank(customerName)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "客户不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				customerName = customerName.trim() ;
				// 获取客户信息
				Map<String, Object> customerMap = new HashMap<String, Object>();
				customerMap.put("name", customerName);
				customerMap.put("status", "1");
				CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(customerMap);
				if (customerInfo == null) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "客户信息不存在或未启用");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				Map<String,Object> customerQueryMap = new HashMap<String, Object>() ;
				customerQueryMap.put("merchantId", user.getMerchantId()) ;
				customerQueryMap.put("name", customerName) ;
				customerQueryMap.put("status", "1") ;
				CustomerMerchantRelMongo customer = customerMerchantRelMongoDao.findOne(customerQueryMap);
				if (customer == null) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "当前公司主体下客户信息不存在或未启用");
					msgList.add(msg);
					failure += 1;
					continue;
				}

				// 当出仓仓库为保税仓时，是否同关区必填；其他情况下非必填；
				DepotInfoMongo inDepotInfo = null;
				String isSameArea = map.get("是否同关区");
				if (StringUtils.isNotBlank(isSameArea)) {
					if ("是".equals(isSameArea)) {
						isSameArea = "1";
					} else if ("否".equals(isSameArea)) {
						isSameArea = "0";
					} else {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "是否同关区，填‘是’或‘否’即可");
						msgList.add(msg);
						failure += 1;
						continue;
					}
				}
				String inDepotCode = map.get("入仓仓库自编码");

				if ((outDepotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && "是".equals(isSameArea))) {
					if (StringUtils.isBlank(inDepotCode)) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "当出仓仓库为保税仓，且是否同关区为“是”，入仓仓库自编码不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 获取入仓仓库信息
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("depotCode", inDepotCode);
					params1.put("status", "1");
					inDepotInfo = depotInfoMongoDao.findOne(params1);
					if (null == inDepotInfo) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "入仓仓库不存在或未启用");
						msgList.add(msg);
						failure += 1;
						continue;
					}
				}

				if (outDepotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)) {
					if (StringUtils.isBlank(isSameArea)) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "当出仓仓库为保税仓时，是否同关区不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 1、当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库
					if ("1".equals(isSameArea)) {
						if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepotInfo.getType())) {
							msg.put("row", String.valueOf(j + 1));
							msg.put("msg", "入仓仓库只能为备查库");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}
				}

				String contractNo = map.get("报关合同号");
				if(StringUtils.isBlank(contractNo)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "报关合同号不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				contractNo = contractNo.trim();

				String invoiceNo = map.get("发票单号");
				if(StringUtils.isBlank(invoiceNo)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "发票单号不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				invoiceNo = invoiceNo.trim();

				String billWeightStr = map.get("提单毛重KG");
				if(StringUtils.isBlank(billWeightStr)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "提单毛重不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				billWeightStr = billWeightStr.trim();

				String torrNum = map.get("托数");
				if(StringUtils.isBlank(torrNum)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "托数不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				if(!StringUtils.isNumeric(torrNum)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "托数只能是正整数");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				torrNum = torrNum.trim();

				String boxNum = map.get("箱数");
				if(StringUtils.isBlank(boxNum)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "箱数不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				if(!StringUtils.isNumeric(boxNum)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "箱数只能是正整数");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				boxNum = boxNum.trim();

				String torrPackType = map.get("托盘材质");
				if(StringUtils.isBlank(torrPackType)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "托盘材质不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				torrPackType = torrPackType.trim();

				String transport = map.get("运输方式");
				transport = transport.trim();
				if(StringUtils.isNotBlank(transport) && DERP.getLabelByKey(DERP.transportList, transport.trim()) == null) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "请填写正确的运输方式编码");
					msgList.add(msg);
					failure += 1;
					continue;
				}

				String portDes = map.get("卸货港");
				if(StringUtils.isBlank(portDes)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "卸货港不能为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				portDes = portDes.trim();

				if (modelMap.containsKey(index)) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "领料单序号：" + index + "出现重复");
					msgList.add(msg);
					failure += 1;
					continue;
				}

				// 注入数据
				MaterialOrderModel materialOrderModel = new MaterialOrderModel();
				materialOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LLD));
				materialOrderModel.setMerchantId(user.getMerchantId());
				materialOrderModel.setMerchantName(user.getMerchantName());
				materialOrderModel.setTopidealCode(user.getTopidealCode());
				materialOrderModel.setCreateDate(TimeUtils.getNow());
				materialOrderModel.setCreater(user.getId());
				materialOrderModel.setCreateName(user.getName());
				materialOrderModel.setBuId(merchantBuRelMongo.getBuId()); // 事业部ID
				materialOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
				materialOrderModel.setIsSameArea(isSameArea);
				materialOrderModel.setCustomerId(customer.getCustomerId());
				materialOrderModel.setCustomerName(customer.getName());
				materialOrderModel.setPoNo(poNo);
				materialOrderModel.setOutDepotId(outDepotInfo.getDepotId());
				materialOrderModel.setOutDepotName(outDepotInfo.getName());
				materialOrderModel.setOutDepotCode(outDepotInfo.getCode());
				materialOrderModel.setInspectNo(outDepotInfo.getInspectNo());
				materialOrderModel.setCustomsNo(outDepotInfo.getCustomsNo());
				materialOrderModel.setContractNo(contractNo);
				materialOrderModel.setInvoiceNo(invoiceNo);
				materialOrderModel.setBillWeight(Double.valueOf(billWeightStr));
				materialOrderModel.setTorusNumber(Integer.valueOf(torrNum));
				materialOrderModel.setBoxNum(Integer.valueOf(boxNum));
				materialOrderModel.setTorrPackType(torrPackType);
				materialOrderModel.setPack(boxNum+"箱"+torrNum + "托"+torrPackType);
				materialOrderModel.setTransport(transport);
				materialOrderModel.setPortDes(portDes);
				if (inDepotInfo != null) {
					materialOrderModel.setInDepotId(inDepotInfo.getDepotId());
					materialOrderModel.setInDepotName(inDepotInfo.getName());
					materialOrderModel.setInDepotCode(inDepotInfo.getCode());
				}

				materialOrderModel.setState(DERP_ORDER.MATERIALORDER_STATE_001);

				// 调入客户卓志编码
				String inCustomerTopNo = "";
				if (customer.getCustomerId() != null && customer.getCustomerId().longValue() > 0) {
					Map<String, Object> cMap = new HashMap<String, Object>();
					cMap.put("customerid", customer.getCustomerId());
					CustomerInfoMogo inCustomerInfo = customerInfoMongoDao.findOne(cMap);
					if (inCustomerInfo != null)
						inCustomerTopNo = inCustomerInfo.getMainId();
				}
				if (inDepotInfo != null) {
					// 同关区
					boolean isSame = !StringUtils.isEmpty(outDepotInfo.getCustomsNo()) && inDepotInfo != null
							&& !StringUtils.isEmpty(inDepotInfo.getCustomsNo())
							&& outDepotInfo.getCustomsNo().equals(inDepotInfo.getCustomsNo());

					// 1.调出调入公司不同,出和入的仓库编码相同 -业务场景：40 ：账册内货权转移 服务类型：G0：库内调拨服务
					if (!user.getTopidealCode().equals(inCustomerTopNo)
							&& outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
						materialOrderModel.setModel("40");
						materialOrderModel.setServeTypes("G0");
					}
					// 2.调出调入公司不同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：50： 账册内货权转移调仓 服务类型：E0：区内调拨调出服务
					else if (!user.getTopidealCode().equals(inCustomerTopNo) && isSame
							&& !outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
						materialOrderModel.setModel("50");
						materialOrderModel.setServeTypes("E0");
					}
					// 3.调出调入公司相同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：10： 账册内调仓 服务类型：E0：区内调拨调出服务
					else if (user.getTopidealCode().equals(inCustomerTopNo) && isSame
							&& !outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
						materialOrderModel.setModel("10");
						materialOrderModel.setServeTypes("E0");
					}
					// 4.调出调入公司相同，出入仓库不同关区，出仓和入库仓库编码不同-业务场景：10 ：账册内调仓 服务类型：E0：区内调拨调出服务
					else if (user.getTopidealCode().equals(inCustomerTopNo)
							&& !(isSame || (StringUtils.isEmpty(outDepotInfo.getCustomsNo()) && inDepotInfo != null
									&& StringUtils.isEmpty(inDepotInfo.getCustomsNo())))
							&& !outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
						materialOrderModel.setModel("10");
						materialOrderModel.setServeTypes("E0");
					}
				}

				modelMap.put(index, materialOrderModel);

			}

		}
		if (failure == 0) {
			for (int i = 1; i < 2; i++) {// 表体
				List<Map<String, String>> objects = data.get(i);
				for (int j = 1; j < objects.size()+1; j++) {
					Map<String, String> msg = new HashMap<String, String>();
					Map<String, String> map = objects.get(j-1);
					String index = map.get("序号");
					if (StringUtils.isBlank(index)) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "商品序号不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					String goodsNo = map.get("商品货号");
					if (StringUtils.isBlank(goodsNo)) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "商品货号不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					goodsNo = goodsNo.trim();

					String goodsName = map.get("商品名称");
					goodsName = goodsName.trim();

					String num = map.get("领取数量");
					num = num.trim(); // 去掉空格后的值
					if (StringUtils.isBlank(num)) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "领取数量不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					if (!StringUtils.isNumeric(num)) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "领料数量必须为正整数");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 判断是否有相同领料订单号+商品货号
					String goodsKey = index + goodsNo;
					if (!goodsNumMap.containsKey(goodsKey)) {
						goodsNumMap.put(goodsKey, Integer.valueOf(num));
					}

					String netWeightStr = map.get("净重").trim();// 总净重

					// 注入数据
					MaterialOrderItemModel materialOrderItem = new MaterialOrderItemModel();

					// 获取商品信息(取对应公司仓库选品配置,不用管代理公司了)
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("goodsNo", goodsNo);
					params1.put("merchantId", user.getMerchantId());
					MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(params1);

					if (merchandiseInfo == null) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "序号:" + index + "，商品货号:" + goodsNo + "不存在");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					/**
					 * （1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
					 * （2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
					 * （3）当出库仓库在该公司下的“选品限制”为“无限制”时，可选的商品为该公司下所有的商品货号信息；
					 */
					String key = index;
					if (checkGoodsNoByDepotMap.containsKey(key)) {
						DepotMerchantRelMongo depotMerchantRelMongo = checkGoodsNoByDepotMap.get(index);

						// （1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
						if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRelMongo.getProductRestriction())) {
							if (!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(merchandiseInfo.getIsRecord())) {
								msg.put("row", String.valueOf(j + 1));
								msg.put("msg", "找不到该商品货号" + merchandiseInfo.getGoodsNo()
										+ "，当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品必须是该公司下的备案商品");
								msgList.add(msg);
								failure += 1;
								continue;
							}
						} else if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2
								.equals(depotMerchantRelMongo.getProductRestriction())) {
							// （2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
							if (!DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0.equals(merchandiseInfo.getOutDepotFlag())) {
								msg.put("row", String.valueOf(j + 1));
								msg.put("msg", "找不到该商品货号" + merchandiseInfo.getGoodsNo()
										+ "，当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号");
								msgList.add(msg);
								failure += 1;
								continue;
							}
						}
					}
					if (!modelMap.containsKey(index)) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "序号:" + index + "在表头不存在");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					Map<String, Object> params3 = new HashMap<String, Object>();
					params3.put("brandId", merchandiseInfo.getBrandId());
					BrandMongo brandMongo = brandMongoDao.findOne(params3);
					if (brandMongo != null) {
						materialOrderItem.setBrandName(brandMongo.getName());
					}
					materialOrderItem.setCommbarcode(merchandiseInfo.getCommbarcode());// 标准条码
					materialOrderItem.setBarcode(merchandiseInfo.getBarcode());
					materialOrderItem.setGoodsCode(merchandiseInfo.getCode());
					materialOrderItem.setGoodsId(merchandiseInfo.getMerchandiseId());
					materialOrderItem.setGoodsName(StringUtils.isBlank(goodsName)? merchandiseInfo.getName() : goodsName );
					materialOrderItem.setGoodsNo(merchandiseInfo.getGoodsNo());
					materialOrderItem.setNum(Integer.valueOf(num));

					// 序号
					materialOrderItem.setSeq(j);

					// 记录表体信息
					List<MaterialOrderItemModel> itemModelList = new ArrayList<MaterialOrderItemModel>();
					if (itemMap.containsKey(index)) {
						itemModelList = itemMap.get(index);
					}
					double grossWeight = 0.0;// 毛重
					double netWeight = 0.0;// 净重
					double netWeightSum = 0.0;
					// 导入时，根据商品货号获取毛重和净重
					grossWeight = merchandiseInfo.getGrossWeight() == null ? 0.0 : merchandiseInfo.getGrossWeight();
					netWeight = merchandiseInfo.getNetWeight() == null ? 0.0 : merchandiseInfo.getNetWeight();

					if (StringUtils.isNotBlank(netWeightStr)) {
						netWeightSum = Double.valueOf(netWeightStr);
					}else {
						netWeightSum = merchandiseInfo.getNetWeight() == null ? 0.0 : new BigDecimal(netWeight).multiply(new BigDecimal(num)).doubleValue();
					}
					materialOrderItem.setGrossWeight(grossWeight);
					materialOrderItem.setNetWeight(netWeight);
					materialOrderItem.setNetWeightSum(netWeightSum);

					itemModelList.add(materialOrderItem);
					itemMap.put(index, itemModelList);
					// 记录总商品数量
					totalMap.put(index, (totalMap.get(index) == null ? 0: totalMap.get(index))+Integer.valueOf(num));
					//记录总商品总净重
					orderNetWeightSumMap.put(index, (orderNetWeightSumMap.get(index) == null ? 0: orderNetWeightSumMap.get(index)) + netWeightSum);

				}
			}
		}
		if (failure == 0) {
			LOGGER.debug("<=========领料单导入保存数据开始===========");
			// 保存数据
			for (Entry<String, MaterialOrderModel> entry : modelMap.entrySet()) {
				Map<String, String> msg = new HashMap<String, String>();
				String index = entry.getKey();
				MaterialOrderModel materialOrder = entry.getValue();
				// 校验表头
				Integer totalNum = totalMap.get(index);
				if (totalNum == null || totalNum == 0) {
					msg.put("row", "");
					msg.put("msg", "序号：" + index + "的商品信息为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 校验表体
				List<MaterialOrderItemModel> itemList = itemMap.get(index);
				if (itemList == null || itemList.size() == 0) {
					msg.put("row", "");
					msg.put("msg", "序号：" + index + "的商品信息为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				if (failure == 0) {
					materialOrder.setTotalNum(totalNum);
					materialOrderDao.save(materialOrder);

					Double calGrossWeight = 0.0;
					for (int i = 0;i< itemList.size() ;i++) {
						MaterialOrderItemModel item = itemList.get(i);
						/**
						 * 提单毛重，自动重算各商品毛重，计算的公式如下：
						 *（1）前N-1个商品毛重=提单毛重*（对应商品净重/总商品净重），保留的毛重小数位与现在一致；
						 *（2）第N个商品毛重=总提单毛重-前N-1个商品毛重合计；
						 *（3）提单毛重=各商品毛重总和
						 */
						item.setOrderId(materialOrder.getId());
						Double grossWeightNum = 0.0;
						if(i == (itemList.size() - 1)) {
							grossWeightNum = materialOrder.getBillWeight() - calGrossWeight;
						}else {
							BigDecimal netWeightRatio = new BigDecimal(item.getNetWeightSum()).divide(new BigDecimal(orderNetWeightSumMap.get(index)),5,BigDecimal.ROUND_HALF_UP);
							grossWeightNum = new BigDecimal(materialOrder.getBillWeight()).multiply(netWeightRatio).doubleValue();
							calGrossWeight = calGrossWeight + grossWeightNum;
						}
						item.setGrossWeightSum(grossWeightNum);

						materialOrderItemDao.save(item);
						LOGGER.debug("<=========保存表体===========" + index + "&" + item.getGoodsNo());
					}
					success++;
				}
			}
			LOGGER.debug("<=========领料单导入保存数据结束===========");
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("msgList", msgList);
		return map;
	}
}
