package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.http.HttpClientUtil;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.SaleOutDepotService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 销售出库service实现类
 */
@Service
public class SaleOutDepotServiceImpl implements SaleOutDepotService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory
			.getLogger(SaleOutDepotServiceImpl.class);
	// 销售出库表头
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库表体
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private SaleShelfDao saleShelfDao;
	//财务经销存关账mongdb
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 仓库Mongo
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	// 仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao;
	@Autowired
	private SaleDeclareOrderItemDao saleDeclareOrderItemDao;
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;
	/**
	 * 列表（分页）
	 * @param
	 * @return
	 */
	@Override
	public SaleOutDepotDTO listSaleOutDepotByPage(SaleOutDepotDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return saleOutDepotDao.queryDTOListByPage(dto);
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public SaleOutDepotDTO searchDetail(Long id) throws SQLException {
		SaleOutDepotDTO dto = new SaleOutDepotDTO();
		dto.setId(id);
		return saleOutDepotDao.searchDtoById(id);
	}

	/**
	 * 根据表头ID获取详细信息
	 * @param id
	 * @return
	 */
	@Override
	public List<SaleOutDepotItemDTO> listItemBySaleOutDepotId(Long id) throws SQLException {
		SaleOutDepotItemDTO dto = new SaleOutDepotItemDTO();
		dto.setSaleOutDepotId(id);
		return saleOutDepotItemDao.listItemDTO(dto);
	}

	/**
	 * 审核
	 * @param list
	 * @param userId
	 * @param userName
	 * @param merchantId
	 * @param merchantName
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String,Object> auditSaleOutDepot(List list, Long userId, String userName, Long merchantId, String merchantName,String topidealCode) throws Exception {
		int num = 0;
		//失败原因
		StringBuffer failureMsg = new StringBuffer();
		for(Object id:list){
			Map<String, Integer> auditedNumMap = new HashMap<>();
			boolean flag = true;
			SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(Long.parseLong(id.toString()));
			// 1.单据状态必须为待审核并且是手工导入;2.销售类型不能为采购执行；3.出库仓库类型必须为保税仓且非下推接口指令的仓库
			if(!DERP_ORDER.SALEOUTDEPOT_STATUS_017.equals(saleOutDepotModel.getStatus()) ||
					!DERP_ORDER.SALEOUTDEPOT_SOURCE_1.equals(saleOutDepotModel.getOrderSource())){
					failureMsg.append("审核失败，必须是手工导入且状态为待出库\n");
					flag = false;
					continue;
			}else if(DERP_ORDER.SALEOUTDEPOT_SALETYPE_3.equals(saleOutDepotModel.getSaleType())){
				failureMsg.append("审核失败，销售类型不能为采销执行\n");
				flag = false;
				continue;
			}
			//mongo查询参数集合
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", saleOutDepotModel.getOutDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params);
			if (depot == null) {
				failureMsg.append("审核失败，仓库ID为:"+saleOutDepotModel.getOutDepotId()+",未查到该出仓仓库信息\n");
				flag = false;
				continue;
			}
			// 仓库公司关联表
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", merchantId);
			depotMerchantRelParam.put("depotId", saleOutDepotModel.getOutDepotId());
			DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if(depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)){
				failureMsg.append("审核失败，仓库ID为:"+saleOutDepotModel.getOutDepotId()+",未查到该公司下出仓仓库信息\n");
				flag = false;
				continue;
			}
//			if(!((DERP_SYS.DEPOTINFO_TYPE_A.equals(depot.getType()) || DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) &&
//				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_0.equals(depotMerchantRelMongo.getIsInOutInstruction()))){
//				failureMsg.append("审核失败，出库仓库类型必须为保税仓/中转仓，且非下推接口指令的仓库\n");
//				flag = false;
//				continue;
//			}
			SaleOrderModel saleOrder = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
			if(StringUtils.isNotBlank(saleOrder.getWriteOffStatus())){
				failureMsg.append("审核失败，销售订单:"+saleOrder.getCode()+"红冲状态不为空\n");
				flag = false;
				continue;
			}

			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOutDepotModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleOutDepotModel.getOutDepotId());
			closeAccountsMongo.setBuId(saleOutDepotModel.getBuId());
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}

			// 关账日期和发货日期比较
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (saleOutDepotModel.getDeliverDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					failureMsg.append("审核失败，归属日期(发货日期)必须大于关账日期/月结日期库\n");
					flag = false;
					continue;
				}
			}
			//是否关联销售预申报单
			SaleDeclareOrderDTO declareDTO = new SaleDeclareOrderDTO();
			declareDTO.setSaleOrderCodes(saleOutDepotModel.getSaleOrderCode());
			List<SaleDeclareOrderDTO> declareList = saleDeclareOrderDao.listSaleDeclareOrder(declareDTO);
			if(declareList != null  && declareList.size() > 0 && saleOutDepotModel.getSaleDeclareOrderId() == null) {
				failureMsg.append("销售出库订单:"+saleOutDepotModel.getCode()+"对应的销售订单已生成销售预申报单，关联销售预申报单不能为空\n");
				flag = false;
				continue ;
			}
			if(StringUtils.isNotBlank(saleOutDepotModel.getSaleDeclareOrderCode())){
				SaleOutDepotModel queryOutDepotModel = new SaleOutDepotModel();
				queryOutDepotModel.setSaleDeclareOrderCode(saleOutDepotModel.getSaleDeclareOrderCode());
				queryOutDepotModel.setSaleOrderCode(saleOutDepotModel.getSaleOrderCode());
				List<SaleOutDepotModel> queryOutDepotList = saleOutDepotDao.list(queryOutDepotModel);
				if(queryOutDepotList != null && queryOutDepotList.size() > 1) {
					failureMsg.append("销售订单:"+saleOutDepotModel.getSaleOrderCode()+",关联销售预申报单:"+saleOutDepotModel.getSaleDeclareOrderCode()+"存在多条出库单，审核失败\n");
					flag = false;
					continue ;
				}
			}

			// 销售出库商品数量是否小于等于销售订单商品可核销量
			// 获取销售出库的商品信息
			SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
			saleOutDepotItem.setSaleOutDepotId(saleOutDepotModel.getId());
			List<SaleOutDepotItemModel> itemList = saleOutDepotItemDao.list(saleOutDepotItem);
			for(SaleOutDepotItemModel item:itemList) {
				if(item.getSaleItemId() == null){
					failureMsg.append("销售明细id为空，请及时维护"+"\n");
					flag = false;
					break;
				}

				//获取销售订单某商品信息
				SaleOrderItemModel saleOrderItem = saleOrderItemDao.searchById(item.getSaleItemId());

				String key = saleOutDepotModel.getSaleOrderCode() +"_"+ item.getSaleItemId() + "_" + item.getGoodsNo();
				Integer transferNum = item.getTransferNum()==null?0:item.getTransferNum();// 好品数量
				Integer wornNum = item.getWornNum()==null?0:item.getWornNum();	// 坏品数量
				Integer auditedNum = transferNum + wornNum;	// 好品+坏品

				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("saleItemId",item.getSaleItemId());
				Integer outDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//已出库数量

				if (auditedNumMap.containsKey(key)) {
					auditedNum += auditedNumMap.get(key);
				}
				if ((saleOrderItem.getNum() - outDepotNum) < auditedNum) {
					failureMsg.append("审核失败，销售出库单号：" + saleOutDepotModel.getCode() + ",商品货号:" + item.getGoodsNo() +  "可核销量不足,余量为"+(saleOrderItem.getNum() - outDepotNum)+"\n");
					flag = false;
					break;
				}
				if(saleOutDepotModel.getSaleDeclareOrderId() != null) {
					SaleDeclareOrderItemModel declareItemModel = new SaleDeclareOrderItemModel();
					declareItemModel.setDeclareOrderId(saleOutDepotModel.getSaleDeclareOrderId());
					declareItemModel.setSaleOrderId(saleOutDepotModel.getSaleOrderId());
					declareItemModel.setGoodsId(item.getGoodsId());
					List<SaleDeclareOrderItemModel> declareItemList = saleDeclareOrderItemDao.list(declareItemModel);
					if(declareItemList == null || declareItemList.size() < 1) {
						failureMsg.append("审核失败，销售预申报：" + saleOutDepotModel.getSaleDeclareOrderCode() + ",商品货号:" + item.getGoodsNo() +  "不存在\n");
						flag = false;
						break;
					}

				}
				auditedNumMap.put(key, auditedNum);
			}
			if(saleOutDepotModel.getSaleDeclareOrderId() != null) {
				for(String key : auditedNumMap.keySet()) {
					String saleOrderCode= key.split("_")[0];
					String saleItemId= key.split("_")[1];
					String goodsNo= key.split("_")[2];
					Integer auditedNum = auditedNumMap.get(key);
					SaleDeclareOrderItemModel declareItemModel = new SaleDeclareOrderItemModel();
					declareItemModel.setDeclareOrderId(saleOutDepotModel.getSaleDeclareOrderId());
					declareItemModel.setSaleOrderCode(saleOrderCode);
					declareItemModel.setSaleItemId(Long.valueOf(saleItemId));
					declareItemModel = saleDeclareOrderItemDao.searchByModel(declareItemModel);
					if(!declareItemModel.getNum().equals(auditedNum)) {
						failureMsg.append("审核失败，销售出库单号：" + saleOutDepotModel.getCode() + ",商品货号:" + goodsNo +  "出库数量（好品数量+坏品数量）必须等于申报数量\n");
						flag = false;
						break;
					}

				}
			}

			if (flag) {
				// 释放并减少冻结量
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				// 海外仓冻结加理货单位
				String inventoryUnit = "";
				if (DERP.ORDER_TALLYINGUNIT_00.equals(saleOrder.getTallyingUnit())) {
					inventoryUnit = DERP.INVENTORY_UNIT_0;
				} else if (DERP.ORDER_TALLYINGUNIT_01.equals(saleOrder.getTallyingUnit())) {
					inventoryUnit = DERP.INVENTORY_UNIT_1;
				} else if (DERP.ORDER_TALLYINGUNIT_02.equals(saleOrder.getTallyingUnit())) {
					inventoryUnit = DERP.INVENTORY_UNIT_2;
				}
				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotId", saleOutDepotModel.getOutDepotId());// 根据仓库id
				DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params); 	// 获取仓库信息
				//中转仓不需要释放冻结库存
				if(!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
					InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
					inventoryFreezeRootJson.setMerchantId(saleOrder.getMerchantId().toString());
					inventoryFreezeRootJson.setMerchantName(saleOrder.getMerchantName());
					inventoryFreezeRootJson.setDepotId(saleOrder.getOutDepotId().toString());
					inventoryFreezeRootJson.setDepotName(saleOrder.getOutDepotName());
					inventoryFreezeRootJson.setOrderNo(saleOutDepotModel.getCode());
					inventoryFreezeRootJson.setBusinessNo(saleOrder.getCode());
					inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
					inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_002);
					inventoryFreezeRootJson.setSourceDate(now);
					inventoryFreezeRootJson.setOperateType("1");//操作类型  0 冻结 1解冻
					List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
					for (SaleOutDepotItemModel item : itemList) {
						InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

						inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
						inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
						inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());

						inventoryFreezeGoodsListJson.setDivergenceDate(now);
						inventoryFreezeGoodsListJson.setNum(item.getTransferNum()+item.getWornNum());// 释放冻结量=好品量+坏品量
						if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
							inventoryFreezeGoodsListJson.setUnit(inventoryUnit);
						}
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
								"----------------------销售出库单[" + saleOutDepotModel.getCode() + "]审核释放库存失败----------------------");
					}
				}
				// 修改出库单据状态为出库中
				saleOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);	// 出库中
				saleOutDepotModel.setAuditDate(TimeUtils.getNow());// 审核时间
				saleOutDepotModel.setAuditor(userId);// 审核人id
				saleOutDepotModel.setAuditName(userName);// 审核人用户名
				num += saleOutDepotDao.modify(saleOutDepotModel);

				//修改销售单据状态为出库中
				SaleOrderModel updateSaleOrder = new SaleOrderModel();
				updateSaleOrder.setId(saleOutDepotModel.getSaleOrderId());
				updateSaleOrder.setState(DERP_ORDER.SALEORDER_STATE_027);
				updateSaleOrder.setModifier(userId);
				updateSaleOrder.setModifierUsername(userName);
				updateSaleOrder.setModifyDate(TimeUtils.getNow());
				saleOrderDao.modify(updateSaleOrder);

				//扣减销售出库库存量
				String saleOutDepotCode = saleOutDepotModel.getCode();
				Date date = new Date();
				String now1 = sdf.format(new Date());
				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTopic());
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTags());
				Map<String, Object> customParam = new HashMap<String, Object>();
				customParam.put("saleId", saleOutDepotModel.getSaleOrderId());	// 销售订单ID
				invetAddOrSubtractRootJson.setCustomParam(customParam);
				invetAddOrSubtractRootJson.setMerchantId(saleOutDepotModel.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(saleOutDepotModel.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(topidealCode);	//公司编码
				invetAddOrSubtractRootJson.setDepotId(saleOutDepotModel.getOutDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(saleOutDepotModel.getOutDepotName());
				invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
				invetAddOrSubtractRootJson.setDepotType(mongo.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
				invetAddOrSubtractRootJson.setOrderNo(saleOutDepotCode);
				invetAddOrSubtractRootJson.setBusinessNo(saleOutDepotModel.getSaleOrderCode());	// 销售订单Code
				invetAddOrSubtractRootJson.setBuId(String.valueOf(saleOutDepotModel.getBuId())); // 事业部
				invetAddOrSubtractRootJson.setBuName(saleOutDepotModel.getBuName());
				//invetAddOrSubtractRootJson.setSource(SourceStatusEnum.XSCK.getKey());
				invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_003);
				//invetAddOrSubtractRootJson.setSourceType(InventoryStatusEnum.XSCK.getKey());
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
				invetAddOrSubtractRootJson.setSourceDate(now1);	// 单据时间
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				invetAddOrSubtractRootJson.setDivergenceDate(sdf1.format(saleOutDepotModel.getDeliverDate()));	// 发货时间
				// 获取当前年月
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
				String now2 = sdf2.format(saleOutDepotModel.getDeliverDate());	// 发货时间
				invetAddOrSubtractRootJson.setOwnMonth(now2); // 归属月份
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
				for (SaleOutDepotItemModel item : itemList) {
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
							Timestamp currentDate = new Timestamp(date.getTime());
							if(currentDate.before(item.getOverdueDate())){
								//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
								invetAddOrSubtractGoodsListJson.setIsExpire("1");
							}else{
								invetAddOrSubtractGoodsListJson.setIsExpire("0");
							}
						}
						if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
							invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(saleOrder.getStockLocationTypeId() == null ? "" : saleOrder.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(saleOrder.getStockLocationTypeName());
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
							Timestamp currentDate = new Timestamp(date.getTime());
							if(currentDate.before(item.getOverdueDate())){
								//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
								invetAddOrSubtractGoodsListJson.setIsExpire("1");
							}else{
								invetAddOrSubtractGoodsListJson.setIsExpire("0");
							}
						}
						if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
							invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(saleOrder.getStockLocationTypeId() == null ? "" : saleOrder.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(saleOrder.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}
				}
				try {
					invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
					rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				} catch (Exception e) {
					LOGGER.error("----------------------销售出库单[" + saleOutDepotCode + "]扣减库存失败----------------------");
				}
			}
		}

        Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",num);
		map.put("failure",list.size()-num);
		map.put("failureMsg", failureMsg);
        return map;
	}
	/**
	 * 获取选中出库单的所有商品和对应数量（相同商品数量叠加）
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String,Integer> getOrderGoodsInfo(List<Long> ids) throws Exception {
		Map<String,Integer> map = new HashMap<String,Integer>();
			for (Long id : ids) {
				// 根据id获取信息
				SaleOutDepotDTO saleOutDepotDTO = saleOutDepotDao.searchDtoById(Long.parseLong(id.toString()));
				// 单据状态必须为待审核并且是手工导入
				if(!DERP_ORDER.SALEOUTDEPOT_STATUS_017.equals(saleOutDepotDTO.getStatus()) ||
						!DERP_ORDER.SALEOUTDEPOT_SOURCE_1.equals(saleOutDepotDTO.getOrderSource())){
					throw new RuntimeException("审核失败，必须是手工导入且状态为待出库");
				}
				//获取仓库信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("depotId", saleOutDepotDTO.getOutDepotId());
				DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params);

				//获取出库表体（商品信息）
				SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
				saleOutDepotItem.setSaleOutDepotId(saleOutDepotDTO.getId());
				List<SaleOutDepotItemModel> itemList = saleOutDepotItemDao.list(saleOutDepotItem);
				for(SaleOutDepotItemModel item:itemList){
					Integer num = 0;
					if(map.containsKey(item.getGoodsId()+"-"+saleOutDepotDTO.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getGoodsNo()+"-"+saleOutDepotDTO.getTallyingUnit())){
						num = map.get(item.getGoodsId()+"-"+saleOutDepotDTO.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getGoodsNo()+"-"+saleOutDepotDTO.getTallyingUnit());
					}
					num+=item.getTransferNum();
					map.put(item.getGoodsId()+"-"+saleOutDepotDTO.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getGoodsNo()+"-"+saleOutDepotDTO.getTallyingUnit(), num);
				}
			}
		return map;
	}
	/**
	 * 根据条件获取销售出库清单
	 * @param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SaleOutDepotDTO> listSaleOutDepot(SaleOutDepotDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<SaleOutDepotDTO>();
			}
			dto.setBuList(buIds);
		}
		return saleOutDepotDao.listSaleOutDepot(dto);
	}
	/**
	 * 根据条件获取出库明细（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleOutDepotDTO listSaleOutDepotDetailsByPage(SaleOutDepotDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return saleOutDepotDao.queryDTODetailsListByPage(dto);
	}
	/**
	 * 根据条件获取出库明细
	 * @param dto
	 * @return
	 */
	@Override
	public List<SaleOutDepotDTO> listSaleOutDepotDetails(SaleOutDepotDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<SaleOutDepotDTO>();
			}
			dto.setBuList(buIds);
		}
		return saleOutDepotDao.queryDTODetailsList(dto);
	}

	/*
	 * 列表查询
	 */
	@Override
	public List<SaleOutDepotModel> listSaleOutDepotModel(SaleOutDepotModel saleOutDepotModel) throws Exception {
		return saleOutDepotDao.list(saleOutDepotModel);
	}

	@Override
	public SaleShelfDTO listSaleOutShelfByPage(SaleShelfDTO dto) throws SQLException {
		return saleShelfDao.listDTOByPage(dto);
	}
	/**
     * 把两个数组组成一个map
     * @param keys   键数组
     * @param values 值数组
     * @return 键值对应的map
     */
    private Map<String, String> toMap(Object[] keys, Object[] values) {
        if (keys != null && values != null && keys.length == values.length) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (int i = 0; i < keys.length; i++) {
                map.put((String) keys[i], values[i].toString());
            }
            return map;
        }
        return null;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> importSaleOutDepot(User user, List<List<Map<String, String>>> data) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;

        Map<String , SaleOutDepotModel> saleCodeOrderMap = new HashMap<String , SaleOutDepotModel>() ;
		Map<String , List<SaleOutDepotItemModel>> saleOutCodeOrderItemMap = new HashMap<String , List<SaleOutDepotItemModel>>();
		Map<String , Integer> checkGoodsNum = new HashMap<String, Integer>();//记录销售退编号+商品货号维度

		List<Map<String, String>> objects = data.get(0);
		for (int j = 1; j < objects.size()+1; j++) {
			Map<String, String> map = objects.get(j-1);
			//mongo查询参数集合
			Map<String, Object> params = new HashMap<String, Object>();
			//外部交易单号
			String saleCode = map.get("销售订单编号") ;
			if(checkIsNullOrNot(j, saleCode, "销售订单编号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			saleCode = saleCode.trim() ;

			SaleOrderModel saleModel= new SaleOrderModel();
			saleModel.setCode(saleCode);
			saleModel.setMerchantId(user.getMerchantId());
			SaleOrderModel saleOrderModel = saleOrderDao.searchByModel(saleModel);
			// 已存在该销售订单，并且已审核
			if(saleOrderModel==null){
				setErrorMsg(j, "该销售订单:"+saleCode+"不存在", resultList);
				failure += 1;
				continue ;
			}else if(saleOrderModel!=null && !saleOrderModel.getCode().equals(saleCode)){
				setErrorMsg(j, "该销售订单编号不存在", resultList);
				failure += 1;
				continue ;
			}else if(!(saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_003)
					|| saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_019))){
				setErrorMsg(j, "该销售订单订单状态不为“已审核”/“部分出库”", resultList);
				failure += 1;
				continue ;
			}else if(DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderModel.getBusinessModel())){
				// 销售类型不能为采购执行
				setErrorMsg(j, "该销售订单:"+saleCode+"的销售类型为采销执行", resultList);
				failure += 1;
				continue ;
			}else if(null == saleOrderModel.getBuId()){	// 该销售订单事业部为空
				setErrorMsg(j, "该销售订单:"+saleCode+"没有存事业部的信息", resultList);
				failure += 1;
				continue ;
			}
			if(org.apache.commons.lang.StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
				setErrorMsg(j, "该销售订单:"+saleCode+"红冲状态不为空", resultList);
				failure += 1;
				continue ;
			}
			//关联销售预申报单
			String saleDeclareCode = map.get("关联销售预申报单");
			SaleDeclareOrderDTO declareDTO = new SaleDeclareOrderDTO();
			declareDTO.setSaleOrderCodes(saleCode);
			List<SaleDeclareOrderDTO> declareList = saleDeclareOrderDao.listSaleDeclareOrder(declareDTO);
			if(declareList != null  && declareList.size() > 0 && StringUtils.isBlank(saleDeclareCode)) {
				setErrorMsg(j, "该销售订单:"+saleCode+"已生成销售预申报单，关联销售预申报单不能为空", resultList);
				failure += 1;
				continue ;
			}
			declareDTO = null;
			if(StringUtils.isNotBlank(saleDeclareCode)) {
				saleDeclareCode = saleDeclareCode.trim();
				declareDTO = new SaleDeclareOrderDTO();
				declareDTO.setCode(saleDeclareCode);
				declareDTO.setSaleOrderCodes(saleCode);
				declareDTO = saleDeclareOrderDao.searchDetail(declareDTO);
				if(declareDTO == null) {
					setErrorMsg(j, "销售订单关联销售预申报单:"+saleDeclareCode+"不存在", resultList);
					failure += 1;
					continue ;
				}
				SaleOutDepotModel queryOutDepotModel = new SaleOutDepotModel();
				queryOutDepotModel.setSaleDeclareOrderCode(saleDeclareCode);
				queryOutDepotModel.setSaleOrderCode(saleCode);
				List<SaleOutDepotModel> queryOutDepotList = saleOutDepotDao.list(queryOutDepotModel);
				if(queryOutDepotList != null && queryOutDepotList.size() > 0) {
					setErrorMsg(j, "销售订单关联销售预申报单:"+saleDeclareCode+"已存在出库单，无法导入", resultList);
					failure += 1;
					continue ;
				}
			}


			//发货时间
			String deliverDate = map.get("发货时间") ;
			if(checkIsNullOrNot(j, deliverDate, "发货时间不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			deliverDate = deliverDate.trim();
			if(!isDate(deliverDate)){
				setErrorMsg(j, "发货时间格式有误,正确格式为:yyyy-MM-dd HH:mm:ss", resultList);
				failure += 1;
				continue ;
			}

			if(TimeUtils.daysBetween(TimeUtils.parse(deliverDate, "yyyy-MM-dd"), new Date()) < 0) {
				setErrorMsg(j, "发货时间不可超过当前时间", resultList);
				failure += 1;
				continue;
			}

			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOrderModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleOrderModel.getOutDepotId());
			closeAccountsMongo.setBuId(saleOrderModel.getBuId());
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}

			// 关账日期和发货日期比较
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (Timestamp.valueOf(deliverDate).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j, "归属日期(发货日期)必须大于关账日期/月结日期", resultList);
					failure += 1;
					continue ;
				}
			}

			// 封装数据
			if(!saleCodeOrderMap.containsKey(saleCode+deliverDate+saleDeclareCode)) {
				SaleOutDepotModel saleOutDepotSaveModel = new SaleOutDepotModel();
				saleOutDepotSaveModel.setSaleOrderId(saleOrderModel.getId());
				saleOutDepotSaveModel.setMerchantId(user.getMerchantId());
				saleOutDepotSaveModel.setMerchantName(user.getMerchantName());
				saleOutDepotSaveModel.setPoNo(saleOrderModel.getPoNo());
				saleOutDepotSaveModel.setOutDepotId(saleOrderModel.getOutDepotId());
				saleOutDepotSaveModel.setOutDepotName(saleOrderModel.getOutDepotName());
				saleOutDepotSaveModel.setCustomerId(saleOrderModel.getCustomerId());
				saleOutDepotSaveModel.setCustomerName(saleOrderModel.getCustomerName());
				saleOutDepotSaveModel.setSaleType(saleOrderModel.getBusinessModel());
				saleOutDepotSaveModel.setDeliverDate(TimeUtils.parse(deliverDate, "yyyy-MM-dd"));
				saleOutDepotSaveModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_017);	// 状态 017-待出库
				saleOutDepotSaveModel.setCreater(user.getId());
				saleOutDepotSaveModel.setCreateDate(TimeUtils.getNow());
				saleOutDepotSaveModel.setSaleOrderCode(saleOrderModel.getCode());
				saleOutDepotSaveModel.setLbxNo(saleOrderModel.getLbxNo());
				saleOutDepotSaveModel.setReceiveName(saleOrderModel.getReceiverName());
				saleOutDepotSaveModel.setRemark(saleOrderModel.getRemark());	// 备注
				saleOutDepotSaveModel.setPoDate(saleOrderModel.getPoDate());
				saleOutDepotSaveModel.setCurrency(saleOrderModel.getCurrency());
				saleOutDepotSaveModel.setOrderSource("1"); // 订单来源  1手工导入 2.接口回传
				saleOutDepotSaveModel.setBuId(saleOrderModel.getBuId());// 事业部ID
				saleOutDepotSaveModel.setBuName(saleOrderModel.getBuName()); // 事业部名称
				if(declareDTO != null) {
					saleOutDepotSaveModel.setSaleDeclareOrderId(declareDTO.getId());
					saleOutDepotSaveModel.setSaleDeclareOrderCode(declareDTO.getCode());
				}


				saleCodeOrderMap.put(saleCode+deliverDate+saleDeclareCode, saleOutDepotSaveModel) ;
			}


			String goodsNo = map.get("商品货号") ;
			if(checkIsNullOrNot(j, saleCode, "商品货号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			goodsNo = goodsNo.trim();
//			SaleOrderItemModel itemParam= new SaleOrderItemModel();
//			itemParam.setOrderId(saleOrderModel.getId());
//			itemParam.setGoodsNo(goodsNo);
//			List<SaleOrderItemModel> itemParamList = saleOrderItemDao.list(itemParam);
//			if (itemParamList==null || itemParamList.size() < 1) {
//				setErrorMsg(j, "该货号"+goodsNo+"是不存在对应的销售订单中", resultList);
//				failure += 1;
//				continue ;
//			}

			goodsNo = goodsNo.trim() ;
			if(declareDTO != null){
				SaleDeclareOrderItemModel item = new SaleDeclareOrderItemModel();
				item.setDeclareOrderId(declareDTO.getId());
				item.setSaleOrderId(saleOrderModel.getId());
				item.setGoodsNo(goodsNo);
				List<SaleDeclareOrderItemModel> itemList = saleDeclareOrderItemDao.list(item);
				if(itemList == null || itemList.size() < 1) {
					setErrorMsg(j, "销售预申报单:"+saleDeclareCode+"销售订单："+saleOrderModel.getCode()+"商品货号："+goodsNo+"不存在", resultList);
					failure += 1;
					continue ;
				}
			}

			MerchandiseInfoMogo merchandise =null;
			if(StringUtils.isNotBlank(goodsNo)){
				params.put("goodsNo", goodsNo) ;
				params.put("merchantId", user.getMerchantId());
				merchandise = merchandiseInfoMogoDao.findOne(params);
				if(merchandise == null) {
					setErrorMsg(j, "表体商品信息不存在", resultList);
					failure += 1;
					continue ;
				}
			}
			String salePrice = map.get("销售单价");

			// 好品数量
			String num = map.get("好品数量") ;
			// 坏品数量
			String wornNum = map.get("坏品数量") ;
			if(StringUtils.isBlank(num) && StringUtils.isBlank(wornNum)){
				setErrorMsg(j, "好品数量或坏品数量必须有一个不为空", resultList);
				failure += 1;
				continue ;
			}else if("0".equals(num) && "0".equals(wornNum)){
				setErrorMsg(j, "好品数量或坏品数量必须有一个不为0", resultList);
				failure += 1;
				continue ;
			}

			/**
			 * 导入时校验导入的商品货号的出库总量必须小于或等于销量-已出库的出库量，
			 * 若大于则导入失败，失败提示文案“销售订单号+货号+剩余出库量为多少”；
			 */
			Integer numInt = StringUtils.isBlank(num)?0:Integer.valueOf(num);
			Integer wornNumInt = StringUtils.isBlank(wornNum)?0:Integer.valueOf(wornNum);
			Integer totalGoodsNum = numInt+wornNumInt;	// 导入进来的好品+坏品数量

			String msg ="";
			SaleOrderItemModel resultItemModel = new SaleOrderItemModel();
			resultItemModel.setOrderId(saleOrderModel.getId());
			resultItemModel.setGoodsNo(merchandise.getGoodsNo());
			if(StringUtils.isNotBlank(salePrice)){
				resultItemModel.setPrice(new BigDecimal(salePrice));
				msg = "，销售价格："+ salePrice ;
			}
			List<SaleOrderItemModel> tempItemList = saleOrderItemDao.list(resultItemModel);
			if(tempItemList == null || tempItemList.size() < 1){
				setErrorMsg(j, "商品货号:"+goodsNo+msg+"在销售订单："+saleOrderModel.getCode()+"中不存在", resultList);
				failure += 1;
				continue;
			}
			if(tempItemList != null && tempItemList.size() > 1 && StringUtils.isBlank(salePrice)){
				setErrorMsg(j, "商品货号：" + goodsNo+" 在销售订单："+saleOrderModel.getCode()+" 存在多条，单价不能为空" , resultList);
				failure += 1;
				continue;
			}else if(tempItemList != null &&  tempItemList.size() == 1){
				resultItemModel = tempItemList.get(0);
			}
			Integer saleNum=0;//销售数量
			if(resultItemModel!=null){
				saleNum = resultItemModel.getNum();
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("saleOrderId", saleOrderModel.getId());
			paramMap.put("goodsNo", merchandise.getGoodsNo());
			paramMap.put("merchantId", user.getMerchantId());
			//已出库数量
			Map<String, Object> saleOutMap = new HashMap<>();
			saleOutMap.put("saleItemId",resultItemModel.getId());
			Integer outDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(saleOutMap);
			if(outDepotNum==null){
				outDepotNum = 0;
			}
			if(checkGoodsNum.containsKey(saleCode+"_"+resultItemModel.getId())) {
				outDepotNum = outDepotNum + checkGoodsNum.get(saleCode+"_"+resultItemModel.getId());//当前导入文件 该商品累计的数量 + 已出库的数量

				Integer totalNumSum = checkGoodsNum.get(saleCode+"_"+resultItemModel.getId()) + totalGoodsNum;
				checkGoodsNum.put(saleCode+"_"+resultItemModel.getId(), totalNumSum);//累计导入数量
			}else {
				checkGoodsNum.put(saleCode+"_"+resultItemModel.getId(), totalGoodsNum);
			}

			Integer totalNum = saleNum-outDepotNum;	// 减去后的数量
			if(totalGoodsNum>totalNum){
				setErrorMsg(j, "销售订单号:"+saleCode+"货号:"+merchandise.getGoodsNo()+"剩余出库量为"+saleNum, resultList);
				failure += 1;
				continue ;
			}


			//出库仓库，校验仓库是否批次强校验
            Map<String, Object> params1 = new HashMap<String, Object>();
            params1.put("depotId", saleOrderModel.getOutDepotId());
            DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			String transferBatchNo = map.get("批次号");
			String productionDate = map.get("生产日期");
			String overDueDate = map.get("失效日期");
			if (depot.getBatchValidation()==null || DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())){
				if(checkIsNullOrNot(j, transferBatchNo, "批次号不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				if(checkIsNullOrNot(j, productionDate, "生产日期不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				productionDate = productionDate.trim() ;
				if(!isValidDate(productionDate)) {
					setErrorMsg(j, "生产日期格式有误，正确格式为:yyyy-MM-dd", resultList);
					failure += 1;
					continue;
				}
				if(checkIsNullOrNot(j, overDueDate, "失效日期不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				overDueDate = overDueDate.trim() ;
				if(!isValidDate(overDueDate)) {
					setErrorMsg(j, "失效日期格式有误，正确格式为:yyyy-MM-dd", resultList);
					failure += 1;
					continue;
				}
			}

			SaleOutDepotItemModel itemSaveModel= new SaleOutDepotItemModel();
			itemSaveModel.setGoodsId(merchandise.getMerchandiseId());
			itemSaveModel.setGoodsCode(merchandise.getGoodsCode());
			itemSaveModel.setGoodsName(merchandise.getName());
			itemSaveModel.setGoodsNo(merchandise.getGoodsNo());
			itemSaveModel.setTransferNum(numInt);
			itemSaveModel.setWornNum(wornNumInt);
			itemSaveModel.setBarcode(merchandise.getBarcode());
			itemSaveModel.setSaleNum(resultItemModel.getNum());
			itemSaveModel.setCommbarcode(merchandise.getCommbarcode());
			itemSaveModel.setPrice(resultItemModel.getPrice());
			itemSaveModel.setTransferBatchNo(transferBatchNo);  // 调拨批次
			itemSaveModel.setProductionDate(TimeUtils.parse(productionDate,"yyyy-MM-dd"));  // 生产日期
			itemSaveModel.setOverdueDate(TimeUtils.parse(overDueDate,"yyyy-MM-dd"));   // 失效日期
			itemSaveModel.setSaleItemId(resultItemModel.getId());
			List<SaleOutDepotItemModel> itemList = saleOutCodeOrderItemMap.get(saleCode+deliverDate+saleDeclareCode);
			if(itemList == null) {
				itemList = new ArrayList<SaleOutDepotItemModel>() ;
			}
			itemList.add(itemSaveModel) ;
			saleOutCodeOrderItemMap.put(saleCode+deliverDate+saleDeclareCode, itemList) ;

		}
		for (String saleCode : saleCodeOrderMap.keySet()){
			SaleOutDepotModel saleOutDepotModel = saleCodeOrderMap.get(saleCode);
			List<SaleOutDepotItemModel> list = saleOutCodeOrderItemMap.get(saleCode);
			if(list ==null || list.size() <1){
				continue;
			}
			if(saleOutDepotModel.getSaleDeclareOrderId() == null){
				continue;
			}
			SaleDeclareOrderItemModel item = new SaleDeclareOrderItemModel();
			item.setDeclareOrderId(saleOutDepotModel.getSaleDeclareOrderId());
			item.setSaleOrderId(saleOutDepotModel.getSaleOrderId());
			List<SaleDeclareOrderItemModel> declareItemList = saleDeclareOrderItemDao.list(item);


			Map<Long,List<SaleOutDepotItemModel>> itemMap = list.stream().collect(Collectors.groupingBy(SaleOutDepotItemModel::getSaleItemId));
			for (SaleDeclareOrderItemModel tempDecalreItem : declareItemList) {
				List<SaleOutDepotItemModel> outItemList = itemMap.get(tempDecalreItem.getSaleItemId());
				if(outItemList == null){
					setErrorMsg(null, "销售预申报单:"+saleOutDepotModel.getSaleDeclareOrderCode()+"销售订单："+saleOutDepotModel.getSaleOrderCode()+"导入商品数量与预申报单不一致", resultList);
					failure += 1;
					continue;
				}
				Integer transferNum = outItemList.stream().filter(o->o.getTransferNum() != null).mapToInt(SaleOutDepotItemModel::getTransferNum).sum();
				Integer wornNum = outItemList.stream().filter(o->o.getWornNum() != null).mapToInt(SaleOutDepotItemModel::getWornNum).sum();

				Integer stayNum = tempDecalreItem.getNum() - transferNum - wornNum;
				if( stayNum.intValue()!= 0){
					setErrorMsg(null, "销售预申报单:"+saleOutDepotModel.getSaleDeclareOrderCode()+"销售订单："+saleOutDepotModel.getSaleOrderCode()+
							"商品货号："+tempDecalreItem.getGoodsNo()+"销售单价："+tempDecalreItem.getSalePrice()+"导入数量与预申报不一致", resultList);
					failure += 1;
					continue;
				}
			}
		}


		//遍历外部单号，保存数据
		if(failure == 0) {
			for (String saleCode : saleCodeOrderMap.keySet()) {
				SaleOutDepotModel saleOutDepotModel = saleCodeOrderMap.get(saleCode);
				saleOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));
				Long saleOutDepotId =saleOutDepotDao.save(saleOutDepotModel);
				if(saleOutDepotId != null) {
					List<SaleOutDepotItemModel> list = saleOutCodeOrderItemMap.get(saleCode);
					//合并相同调入商品货号+批次+效期
	                Map<String, SaleOutDepotItemModel> map = new HashMap<>();
	                for (SaleOutDepotItemModel itemModel : list){
	                    String key = itemModel.getSaleItemId() + "_" + itemModel.getTransferBatchNo() + "_" +
	                            itemModel.getProductionDate() + "_" + itemModel.getOverdueDate();

	                    SaleOutDepotItemModel existItemModel = map.get(key);
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
	                	SaleOutDepotItemModel orderItemModel = map.get(key);
	                	orderItemModel.setSaleOutDepotId(saleOutDepotId);
						saleOutDepotItemDao.save(orderItemModel) ;
					}
					success += 1;
				}
			}
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

    /**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content ,
			String msg ,List<ImportErrorMessage> resultList ) {

		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true ;

		}else {
			return false ;
		}

	}
	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(Integer index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		if(index != null){
			message.setRows(index + 1);
		}
		message.setMessage(msg);
		resultList.add(message);
	}

	/**
	 * 判断是否yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	private boolean isDate(String date) {
		Pattern p = Pattern.compile("^([1-2]{1}\\d{3})\\-(([0]{1}[1-9]{1})|([1]{1}[0-2]{1}))\\-(([0]{1}[1-9]{1})|([1-2]{1}\\d{1})|([3]{1}[0-1]{1}))\\s(([0-1]{1}\\d{1})|([2]{1}[0-3]))\\:([0-5]{1}\\d{1})\\:([0-5]{1}\\d{1})$");
		return p.matcher(date).matches();
	}

	/**
	 * 校验费用格式
	 * @param free
	 * @param index
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean valiteFree(String free , int index , String msg ,List<ImportErrorMessage> resultList) {
		//String reg = "^[0-9]+(.[0-9]{1,2})?$" ;
		String reg = "^[0-9]+(.[0-9]{1,6})?$";
		Pattern pattern =Pattern.compile(reg);
		Matcher matcher = pattern.matcher(free);

		if(matcher.matches()) {
			return false ;
		}else {
			setErrorMsg(index, msg, resultList);
			return true ;
		}
	}
	@Override
	public int delImportOrder(List<Long> list) throws Exception {
		int count = saleOutDepotDao.getImportOrderCountByIds(list) ;
		if(list.size() != count) {
			throw new RuntimeException("删除失败，只能删除手工导入的待审核订单") ;
		}

		int total = 0 ;

		for (Long id : list) {
			SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(id) ;
			if(saleOutDepotModel != null) {
				if(!"017".equals(saleOutDepotModel.getStatus())) {
					throw new RuntimeException("删除失败，订单号：" + saleOutDepotModel.getCode() + "状态不为：待出库") ;
				}
				SaleOutDepotModel tempModel = new SaleOutDepotModel() ;
				tempModel.setId(saleOutDepotModel.getId());
				tempModel.setStatus("006");	// 已删除
				tempModel.setModifyDate(TimeUtils.getNow());
				total += saleOutDepotDao.modify(tempModel);
			}
		}

		return total;
	}

	private void checkBeforeDel(Long saleOutOrderId) throws Exception {
		SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(saleOutOrderId);
		if(DERP_ORDER.SALEOUTDEPOT_STATUS_026.equals(saleOutDepotModel.getStatus()) || DERP_ORDER.SALEOUTDEPOT_STATUS_031.equals(saleOutDepotModel.getStatus())){
			throw new RuntimeException("已上架的销售出库清单不能删除！");
		}
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
		if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
			throw new RuntimeException("销售出库单：" + saleOutDepotModel.getCode() + " 对应的销售订单红冲状态不为空，无法删除");
		}
		//判断所选择的单据的发货时间所属月份的公司事业部月结状态是否未月结/未关账
		if(saleOutDepotModel.getDeliverDate() != null){//"待出库"单据不一定有发货时间，发货时间未空，跳过此校验
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOutDepotModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleOutDepotModel.getOutDepotId());
			closeAccountsMongo.setBuId(saleOutDepotModel.getBuId());
			String maxMonth = "";

			// 查询关账日期
			maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			if (StringUtils.isNotBlank(maxMonth)) {
				// 获取最大关账月份下一个月1日时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
				String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
				// 关账下个月日期大于 入库日期
				if (saleOutDepotModel.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
					throw new RuntimeException("发货时间所属月份的公司事业部已月结/已关账，已月结/已关账的销售出库清单不能删除");
				}
			}
		}
	}

	public void delSaleOutOrder(Long saleOutOrderId, User user, String token) throws Exception {
		SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(saleOutOrderId) ;
		if(saleOutDepotModel == null) {
			throw new RuntimeException("销售出库单不存在");
		}
		SaleOrderModel querySaleOrder = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
		if(org.apache.commons.lang.StringUtils.isNotBlank(querySaleOrder.getWriteOffStatus())){
			throw new RuntimeException("销售单红冲状态不为空，无法删除");
		}
		//出库单校验
		checkBeforeDel(saleOutOrderId);
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleOutDepotModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params); 	// 获取仓库信息
		//----------------回滚单据 start -------------
		//出库中、已出库的单据进行回滚，出库中回滚，主要是往唯一表插入数据，防止重复推送
		if(DERP_ORDER.SALEOUTDEPOT_STATUS_018.equals(saleOutDepotModel.getStatus()) || DERP_ORDER.SALEOUTDEPOT_STATUS_027.equals(saleOutDepotModel.getStatus())) {
			String url = ApolloUtil.inventoryWebhost + "/webapi/inventory/inventoryDetails/inventoryRollBack.asyn?token="+token;
//			String url = "http://127.0.0.1:9040/webapi/inventory/inventoryDetails/inventoryRollBack.asyn?token="+token;

			LOGGER.info("请求:调用库存回滚接口url："+ url);
			LOGGER.info("调用库存回滚，参数:"+ saleOutDepotModel.getCode());

			String result = HttpClientUtil.doPost(url, saleOutDepotModel.getCode(), "utf-8");
			LOGGER.info("调用库存回滚接口，响应:"+result);

			// 3、若失败，提示错误原因
			if (StringUtils.isBlank(result)) {
				throw new RuntimeException("调用库存回滚接口返回异常！");
			}
			JSONObject resultJson = JSONObject.fromObject(result);
			String status = resultJson.getString("code");
			if (!MessageEnum.SUCCESS_10000.getCode().equals(status)) {
				if (resultJson.get("message") != null) {
					throw new RuntimeException(resultJson.getString("message"));
				}
			}
			//中转仓无需冻结
			if(!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
				//----------------冻结库存 start -------------
				JSONObject inventoryFreezeJSONObject=new JSONObject();
				inventoryFreezeJSONObject.put("orderNo",saleOutDepotModel.getCode());
				inventoryFreezeJSONObject.put("businessNo",saleOutDepotModel.getSaleOrderCode());
				LOGGER.info("推送mq重新冻结库存："+inventoryFreezeJSONObject.toString());
				rocketMQProducer.send(inventoryFreezeJSONObject.toString(), MQInventoryEnum.INVENTORY_FREEZE_ROLL_BACK.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ROLL_BACK.getTags());
				//----------------冻结库存 end -------------
			}
		}
		//----------------回滚单据 end -------------

		//----------------关闭日志监控 start-------------
		JSONObject logJSONObject=new JSONObject();
		logJSONObject.put("orderNo", saleOutDepotModel.getCode());// 加减明细ids
		logJSONObject.put("describe", "日志监控关闭订单加减明细失败数据和冻结解冻失败数据");
		LOGGER.info("推送日志mq关闭日志"+logJSONObject.toString());
		rocketMQProducer.send(logJSONObject.toString(), MQLogEnum.LOG_CONSUME_COLSE.getTopic(),MQLogEnum.LOG_CONSUME_COLSE.getTags());
		//----------------关闭日志监控 end -------------

		//删除勾稽关系表 start
		SaleAnalysisModel analysisModel = new SaleAnalysisModel();
		analysisModel.setSaleOutDepotId(saleOutOrderId);
		List<SaleAnalysisModel> saleAnalysisModelList = saleAnalysisDao.list(analysisModel);
		if(saleAnalysisModelList != null && saleAnalysisModelList.size() > 0){
			List<Long> analysisIds = saleAnalysisModelList.stream().map(SaleAnalysisModel::getId).collect(Collectors.toList());
			saleAnalysisDao.delete(analysisIds);
		}
		//删除勾稽关系表 end

		//修改销售出库单据状态
		SaleOutDepotModel tempModel = new SaleOutDepotModel() ;
		tempModel.setId(saleOutDepotModel.getId());
		tempModel.setStatus(DERP.DEL_CODE_006);	// 已删除
		tempModel.setModifyDate(TimeUtils.getNow());
		saleOutDepotDao.modify(tempModel);

		//删除唯一关系表
		if(saleOutDepotModel.getSaleDeclareOrderId() != null){
			OrderExternalCodeModel externalModel = new OrderExternalCodeModel();
			externalModel.setExternalCode(saleOutDepotModel.getSaleDeclareOrderCode());
			List<OrderExternalCodeModel> externalList = orderExternalCodeDao.list(externalModel);
			if(externalList != null && externalList.size() > 0){
				List<Long> ids = externalList.stream().map(OrderExternalCodeModel::getId).collect(Collectors.toList());
				orderExternalCodeDao.delete(ids);
			}

		}

		//修改预申报单状态
		if(saleOutDepotModel.getSaleDeclareOrderId() != null){
			SaleDeclareOrderModel saleDeclareOrderModel = new SaleDeclareOrderModel();
			saleDeclareOrderModel.setId(saleOutDepotModel.getSaleDeclareOrderId());
			saleDeclareOrderModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_001);
			saleDeclareOrderDao.modify(saleDeclareOrderModel);
		}
		//修改销售单状态
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
		Map<String,Object> queryMap = new HashMap<>();
		queryMap.put("code",saleOrderModel.getCode());
		//遍历销售订单商品未出库量
		List<Map<String, Object>> goodsNotOutNumList = saleOutDepotDao.getGoodsNotOutNumList(queryMap);
		Integer noOutNum = 0;
		for (Map<String, Object> goodsMap : goodsNotOutNumList) {
			BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
			if (diffnumbg.intValue() > 0) {
				noOutNum = noOutNum + diffnumbg.intValue();
			}
		}
//		SaleOutDepotDTO querySaleOut = new SaleOutDepotDTO();
//		querySaleOut.setSaleDeclareOrderCode(saleOutDepotModel.getSaleDeclareOrderCode());
//		List <SaleOutDepotDTO> querySaleOutList = saleOutDepotDao.queryDTODetailsList(querySaleOut);
//		Integer outNum = 0;
//		if(querySaleOutList == null && querySaleOutList.size() > 0){
//			Integer outTranferNum = querySaleOutList.stream().filter( o->o.getTransferNum() != null).mapToInt(SaleOutDepotDTO::getTransferNum).sum();
//			Integer outWornNum = querySaleOutList.stream().filter( o->o.getWornNum() != null).mapToInt(SaleOutDepotDTO::getWornNum).sum();
//			outNum = outTranferNum + outWornNum;
//		}

		String saleStatus = DERP_ORDER.SALEORDER_STATE_019;
		//未出库量 = 销售量，销售单没有出库单，状态改为已审核
		if(noOutNum.intValue() == saleOrderModel.getTotalNum().intValue()){
			saleStatus = DERP_ORDER.SALEORDER_STATE_003;//已审核
		}
		SaleOrderModel tempSaleOrderModel = new SaleOrderModel();
		tempSaleOrderModel.setId(saleOrderModel.getId());
		tempSaleOrderModel.setState(saleStatus);
		saleOrderDao.modify(tempSaleOrderModel);

	}

	/**
	 * 判断是否yyyy-MM-dd
	 * @param date
	 * @return
	 */
	private boolean isValidDate(String date) {
		String path="\\d{4}-\\d{2}-\\d{2}";//定义匹配规则
		Pattern p=Pattern.compile(path);//实例化Pattern
		return p.matcher(date).matches();

	}

}
