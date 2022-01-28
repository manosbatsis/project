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
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.dao.transfer.TransferInDepotDao;
import com.topideal.dao.transfer.TransferInDepotItemDao;
import com.topideal.dao.transfer.TransferOutDepotDao;
import com.topideal.dao.transfer.TransferOutDepotItemDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderItemDTO;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.vo.sale.*;
import com.topideal.entity.vo.transfer.TransferInDepotItemModel;
import com.topideal.entity.vo.transfer.TransferInDepotModel;
import com.topideal.entity.vo.transfer.TransferOutDepotItemModel;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;
import com.topideal.json.inventory.j06.InventoryDetailJson;
import com.topideal.json.inventory.j06.InventoryGoodsDetailJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.InventoryLocationAdjustmentOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 库位调整单service实现类
 */
@Service
public class InventoryLocationAdjustmentOrderServiceImpl implements InventoryLocationAdjustmentOrderService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(InventoryLocationAdjustmentOrderServiceImpl.class);
	// 库位调整单表头
	@Autowired
	private InventoryLocationAdjustmentOrderDao inventoryLocationAdjustmentOrderDao;
	// 库位调整单表体
	@Autowired
	private InventoryLocationAdjustmentOrderItemDao inventoryLocationAdjustmentOrderItemDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	// 商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 电商订单表头
	@Autowired
	private OrderDao orderDao;
	// 电商订单表体
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private RMQProducer rocketMQProducer;
	// 财务经销存关账mongdb
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;

	// 调拨入库表头
	@Autowired
	private TransferInDepotDao transInDepDao;
	// 调拨入库表体
	@Autowired
	private TransferInDepotItemDao transInDepItemDao;
	// 调拨出库表头
	@Autowired
	private TransferOutDepotDao transOutDepDao;
	// 调拨出库表体
	@Autowired
	private TransferOutDepotItemDao transOutDepItemDao;
	// 销售出库表头
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库表体
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;

	@Override
	public InventoryLocationAdjustmentOrderDTO listInventoryLocationAdjust(InventoryLocationAdjustmentOrderDTO dto,User user)
			throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		InventoryLocationAdjustmentOrderDTO inventoryLocationAdjustmentDTO = inventoryLocationAdjustmentOrderDao
				.queryDTOListByPage(dto);
		int total = inventoryLocationAdjustmentOrderDao.getTotal(dto);
		inventoryLocationAdjustmentDTO.setTotal(total);
		return inventoryLocationAdjustmentDTO;
	}

	/**
	 * 导入库位调整单
	 */
	@Override
	public Map saveInventoryLocationAdjust(Map<Integer, List<List<Object>>> data, User user) throws SQLException {
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<Map<String,String>> msgList = new ArrayList<Map<String,String>>();
		//ps:序号是表头与表体关联的标识    序号与表头是1对1，表头与表体是1对多
		Map<String, InventoryLocationAdjustmentOrderModel> modelMap = new HashMap<String,InventoryLocationAdjustmentOrderModel>();
		Map<String,List<InventoryLocationAdjustmentOrderItemModel>> itemMap= new HashMap<String,List<InventoryLocationAdjustmentOrderItemModel>>();
		// 检查以“仓库+事业部+单据类型+归属月份”为一个维度是否重复
		Set<String> checkDepotBuTypeOwnMonthSet = new HashSet<>();
		// 序号+仓库ID，检查导入的仓库是否与该公司主体下的对应单据的仓库一致
		Map<String,Long> depotMap= new HashMap<String,Long>();
		// 电商订单号调增商品货号，调增商品数量
		Map<String,Integer> orderNumMap= new HashMap<String,Integer>();
		// 出仓仓库信息
		DepotInfoMongo depotInfoMongo =null;

		Map<String,String > typeMap = new HashMap<String, String>();
		for (int i = 0; i < 1; i++) {//表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
			try{
				Map<String,String> msg = new HashMap<String,String>();
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
				String index = map.get("序号");
				if(index == null || "".equals(index)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "序号不能为空");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				String depotCode = map.get("仓库");
				if(depotCode == null || "".equals(depotCode)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "仓库不能为空");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				//获取出仓仓库信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("depotCode", depotCode);
				depotInfoMongo = depotInfoMongoDao.findOne(params);
				if(null == depotInfoMongo){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "仓库不存在");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				if(!depotMap.containsKey(index)){
					depotMap.put(index,depotInfoMongo.getDepotId());
				}

				// 仓库公司关联表
				Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
				depotMerchantRelParam.put("merchantId", user.getMerchantId());
				depotMerchantRelParam.put("depotId", depotInfoMongo.getDepotId());
				DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
				if(depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "仓库ID为："+depotInfoMongo.getDepotId()+",未查到该公司下仓库信息");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				String type = map.get("单据类型");
				if(StringUtils.isBlank(type)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "单据类型不能为空");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				type = type.trim();
				String typeName = DERP_ORDER.getLabelByKey(DERP_ORDER.inventoryLocationAdjustmentOrder_typeList, type);
				if(StringUtils.isBlank(typeName)) {
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "单据类型输入有误");
					msgList.add(msg);
					failure+=1;
					continue;
				}

				if(!typeMap.containsKey(index)) {
					typeMap.put(index,type);
				}

				String  buCode= map.get("事业部");
				if(buCode == null || "".equals(buCode)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "事业部不能为空");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				// 获取该事业部信息
				Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
				merchantBuRelParam.put("merchantId", user.getMerchantId());
				merchantBuRelParam.put("buCode", buCode);
				merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
				MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
				if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "事业部编码为："+buCode+",未查到该公司下事业部信息");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				// 校验公司-仓库-事业部的关联表
				Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
				merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
				merchantDepotBuRelParam.put("depotId", depotInfoMongo.getDepotId());	// 出仓仓库
				merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
				MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
				if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "事业部编码为："+merchantBuRelMongo.getBuCode()+",仓库："+depotCode+",未查到该公司仓库事业部关联信息");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				// 校验事业部与当前账号所绑定的事业部是否匹配
				boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
				if(!userRelateBu){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "事业部编码为："+merchantBuRelMongo.getBuCode()+",用户id："+user.getId()+",无权限操作该事业部");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				String ownDate= map.get("归属日期");
				if(ownDate == null || "".equals(ownDate)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "归属日期不能为空");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				if(!TimeUtils.isYmdDate(ownDate)) {
					msg.put("row", String.valueOf(j+1));
					msg.put("msg",  "归属日期格式有误,正确格式为:yyyy-MM-dd");
					msgList.add(msg);
					failure+=1;
					continue ;
				}
				ownDate = ownDate+" 00:00:00";

				String remark = map.get("调整原因");// 备注
				if(remark == null || "".equals(remark)){
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "调整原因不能为空");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				if (modelMap.containsKey(index)) {
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "表头序号："+index+"出现重复");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				//导入以“仓库+单据类型+事业部+归属月份”为一个维度
				String isKey=depotCode+buCode+ownDate+type;
				if(!checkDepotBuTypeOwnMonthSet.contains(isKey)){
					checkDepotBuTypeOwnMonthSet.add(isKey);
				}else{
					msg.put("row", String.valueOf(j+1));
					msg.put("msg", "序号:"+index+"，仓库:"+depotCode+"单据类型:"+type+"事业部:"+buCode+"归属日期:"+ownDate+"有多条数据,合并后导入");
					msgList.add(msg);
					failure+=1;
					continue;
				}

				//注入数据
				InventoryLocationAdjustmentOrderModel adjustmentOrderModel = new InventoryLocationAdjustmentOrderModel();
				adjustmentOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KWTZD));
				adjustmentOrderModel.setState(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_001);// 待审核
				adjustmentOrderModel.setMerchantId(user.getMerchantId());
				adjustmentOrderModel.setMerchantName(user.getMerchantName());
				adjustmentOrderModel.setDepotId(depotInfoMongo.getDepotId());
				adjustmentOrderModel.setDepotName(depotInfoMongo.getName());
				adjustmentOrderModel.setBuId(merchantBuRelMongo.getBuId());
				adjustmentOrderModel.setBuName(merchantBuRelMongo.getBuName());
				adjustmentOrderModel.setOwnDate(TimeUtils.parseFullTime(ownDate));//归属日期
				adjustmentOrderModel.setRemark(remark);
				adjustmentOrderModel.setCreater(user.getId());
				adjustmentOrderModel.setCreateName(user.getName());
				adjustmentOrderModel.setType(type);

				modelMap.put(index, adjustmentOrderModel);
			}catch (Exception e) {
				e.printStackTrace();
				failure+=1;
				continue;
			}
		}
	}
	//记录同一库位调整单电商订单的客户id	是否存在不同的如果存在不同的(电商订单)
	Map<String, List<Long>> customerIdMap=new HashMap<>();
	Map<String, String> customerNameMap=new HashMap<>();
	// 电商订单号调增商品货号，调增商品数量
	Map<String,Integer> adjustNumMap = new HashMap<String,Integer>();
	Map<String, Object> orderMap=new HashMap<String, Object>();
	Map<String, Object> orderGoodsMap=new HashMap<String, Object>();
	if(failure == 0){
		for (int i = 1; i < 2; i++) {//表体
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				try{
					Map<String,String> msg = new HashMap<String,String>();
					Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
					String index = map.get("序号");
					if(index == null || "".equals(index)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "序号不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					if (!modelMap.containsKey(index)) {
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "序号:"+index+"在表头不存在");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String orderCode = map.get("平台订单号");// 外部单号
					if(orderCode == null || "".equals(orderCode)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "平台订单号不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					orderCode=orderCode.trim();

					String increaseGoodsNo = map.get("调增商品货号");
					if(increaseGoodsNo == null || "".equals(increaseGoodsNo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "调增商品货号不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					//获取商品信息
					Map<String, Object> params1 = new HashMap<String,Object>();
					params1.put("goodsNo", increaseGoodsNo);
					params1.put("merchantId", user.getMerchantId());
					MerchandiseInfoMogo increaseGoods = merchandiseInfoMogoDao.findOne(params1);
					if(increaseGoods == null){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "序号:"+index+"，调增商品货号:"+increaseGoodsNo+"，该商品不存在");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String reduceGoodsNo = map.get("调减商品货号");
					if(reduceGoodsNo == null || "".equals(reduceGoodsNo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "调减商品货号不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					//获取商品信息
					params1.clear();
					params1.put("goodsNo", reduceGoodsNo);
					params1.put("merchantId", user.getMerchantId());
					MerchandiseInfoMogo reduceGoods = merchandiseInfoMogoDao.findOne(params1);
					if(reduceGoods == null){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "序号:"+index+"，调减商品货号:"+reduceGoodsNo+"，该商品不存在");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String numStr = map.get("调整数量");
					if(numStr == null || "".equals(numStr)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "调整数量不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					numStr=numStr.trim();

					String inventoryType = map.get("库存类型");
					if(inventoryType == null || "".equals(inventoryType)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "库存类型不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}

					//注入数据
					InventoryLocationAdjustmentOrderItemModel itemModel = new InventoryLocationAdjustmentOrderItemModel();
					String key = "";
					String tallyingUnit = "";//理货单位
					String orderKey = orderCode+"_"+user.getMerchantId();
					if(typeMap.get(index).equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DSDD)) {
						//导入的电商订单编号(外部单号)+调增商品货号 是否存在该公司主体下的电商订单里，且调整的数量不能大于电商订单中的销售量
						OrderModel orderModel = (OrderModel) orderMap.get(orderKey);
						if(orderModel == null) {
							orderModel = new OrderModel();
							orderModel.setExternalCode(orderCode);
							orderModel.setMerchantId(user.getMerchantId());
							orderModel = orderDao.searchByModel(orderModel);

							orderMap.put(orderKey, orderModel);
						}

						if(orderModel==null){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "平台订单编号"+orderCode+"不存在该公司主体下的电商订单");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						// 判断生成库位调整单的订单客户id是否一样
						Long customerId = orderModel.getCustomerId();
						if (customerId==null) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "平台订单编号"+orderCode+"客户id为空");
							msgList.add(msg);
							failure+=1;
							continue;
						}

						if (customerIdMap.containsKey(index)) {
							List<Long> customerIdList = customerIdMap.get(index);
							if (!customerIdList.contains(customerId))customerIdList.add(customerId);
							customerIdMap.put(index, customerIdList);
							if (customerIdList.size()>1) {
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "平台订单编号"+orderCode+"和其他单号的客户不同,客户名称:"+orderModel.getCustomerName());
								msgList.add(msg);
								failure+=1;
								continue;
							}
						}else {
							List<Long> customerIdList = new ArrayList<>();
							customerIdList.add(customerId);
							customerIdMap.put(index, customerIdList);
							customerNameMap.put(index, orderModel.getCustomerName());
						}

						if(!orderModel.getStatus().equals(DERP_ORDER.ORDER_STATUS_4)) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "电商订单编号"+orderCode+"，不为已发货");
							msgList.add(msg);
							failure+=1;
							continue;
						}


						List<Long> list = customerIdMap.get("index");

						//检查导入文件表头信息的仓库与订单对应的仓库是否一致，若存在不一致的订单，系统报错“订单出库仓库与调整仓库不一致”
						if(depotMap.containsKey(index)){
							if(!orderModel.getDepotId().equals(depotMap.get(index))){
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "表头信息的仓库"+depotMap.get(index)+"与订单"+orderCode+"对应的仓库不一致");
								msgList.add(msg);
								failure+=1;
								continue;
							}
						}
						//电商订单的库存类型只有好品
						if(inventoryType.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_1)) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "序号"+index+"，电商订单号："+orderCode+" 库存类型不能为坏品");
							msgList.add(msg);
							failure+=1;
							continue;
						}

						List<OrderItemModel> itemList = (List<OrderItemModel>) orderGoodsMap.get(orderKey+"_"+increaseGoodsNo);
						if(itemList ==null || itemList.size() < 0) {
							OrderItemModel orderItemModel = new OrderItemModel();
							orderItemModel.setOrderId(orderModel.getId());
							orderItemModel.setGoodsNo(increaseGoodsNo);
							itemList = orderItemDao.list(orderItemModel);

							orderGoodsMap.put(orderKey+"_"+increaseGoodsNo, itemList);
						}

						if(itemList==null || itemList.size()==0){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "调增商品:"+increaseGoodsNo+"在电商订单:"+orderCode+"下不存在");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						Integer orderNum = 0;
						for (OrderItemModel item:itemList){
							orderNum=orderNum+item.getNum();
						}
						//调整的数量不能大于电商订单中的销售量
						if(Integer.valueOf(numStr)>orderNum){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "序号："+index+",商品货号："+increaseGoodsNo+"调整的数量不能大于电商订单中的销售量");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						key = orderModel.getExternalCode()+":"+increaseGoodsNo+":"+typeMap.get(index);
						if(!orderNumMap.containsKey(key)){
							orderNumMap.put(key,orderNum);//订单中数量
						}
						if(!adjustNumMap.containsKey(key)){
							adjustNumMap.put(key,Integer.valueOf(numStr));
						}else{
							Integer num = adjustNumMap.get(key)+Integer.valueOf(numStr);//调整数量
							adjustNumMap.put(key,num);
						}

						itemModel.setOrderCode(orderModel.getCode());//系统订单号
						itemModel.setPlatformCode(orderModel.getStorePlatformName());//平台编码
						itemModel.setPlatformName(DERP.getLabelByKey(DERP.storePlatformList,orderModel.getStorePlatformName()));//平台名称
						itemModel.setShopCode(orderModel.getShopCode());//店铺编码
						itemModel.setShopName(orderModel.getShopName());//店铺名称

						tallyingUnit = orderModel.getTallyingUnit();

					}else if(typeMap.get(index).equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DBRK)) {
						//导入的调拨入库单+调减商品货号+库存类型 是否存在该公司主体下的调拨入库单，且调整的数量不能大于调拨入库中的调拨数量（区分好品、坏品）
						TransferInDepotModel transInDepModel = (TransferInDepotModel) orderMap.get(orderKey);
						if(transInDepModel == null) {
							transInDepModel = new TransferInDepotModel();
							transInDepModel.setCode(orderCode);
							transInDepModel.setMerchantId(user.getMerchantId());
							transInDepModel = transInDepDao.searchByModel(transInDepModel);

							orderMap.put(orderKey, transInDepModel);
						}
						if(transInDepModel == null) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "平台订单编号"+orderCode+"不存在该公司主体下调拨入库单");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						//调拨入库单必须是已入仓
						if(!transInDepModel.getStatus().equals(DERP_ORDER.TRANSFERINDEPOT_STATUS_012)) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "调拨入库单号"+orderCode+"，不为已入仓");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						List<TransferInDepotItemModel> itemList = (List<TransferInDepotItemModel>) orderGoodsMap.get(orderKey+"_"+reduceGoodsNo);
						if(itemList==null || itemList.size()==0){
							TransferInDepotItemModel transInDepItemModel = new TransferInDepotItemModel();
							transInDepItemModel.setTransferDepotId(transInDepModel.getId());
							transInDepItemModel.setInGoodsNo(reduceGoodsNo);
							itemList = transInDepItemDao.list(transInDepItemModel);

							orderGoodsMap.put(orderKey+"_"+reduceGoodsNo, itemList);
						}

						if(itemList==null || itemList.size()==0){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "调减商品:"+reduceGoodsNo+"在调拨入库单:"+orderCode+"下不存在");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						Integer orderNum = 0;
						if(inventoryType.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_0)) {
							for (TransferInDepotItemModel item:itemList){
								orderNum=orderNum+item.getTransferNum();
							}
							//调整的数量不能大于调拨入库中的调拨量（区分好品、坏品）
							if(Integer.valueOf(numStr)>orderNum){
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "序号："+index+",商品货号："+reduceGoodsNo+"调整的数量不能大于调拨入库中的好品量");
								msgList.add(msg);
								failure+=1;
								continue;
							}

						}else if(inventoryType.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_1)) {
							for (TransferInDepotItemModel item:itemList){
								orderNum=orderNum+item.getWornNum();
							}
							//调整的数量不能大于调拨入库中的调拨量（区分好品、坏品）
							if(Integer.valueOf(numStr)>orderNum){
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "序号："+index+",商品货号："+reduceGoodsNo+"调整的数量不能大于调拨入库中的坏品量");
								msgList.add(msg);
								failure+=1;
								continue;
							}

						}
						key =transInDepModel.getCode()+":"+reduceGoodsNo+":"+inventoryType+":"+typeMap.get(index);
						if(!orderNumMap.containsKey(key)){
							orderNumMap.put(key,orderNum);//订单中数量
						}
						if(!adjustNumMap.containsKey(key)){
							adjustNumMap.put(key,Integer.valueOf(numStr));
						}else{
							Integer num = adjustNumMap.get(key)+Integer.valueOf(numStr);//调整数量
							adjustNumMap.put(key,num);
						}
						tallyingUnit = itemList.get(0).getTallyingUnit();
					}else if(typeMap.get(index).equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DBCK)) {
						//导入的调拨出库单+调增商品货号+库存类型 是否存在该公司主体下的调拨出库单，且调整的数量不能大于调拨出库中的调拨数量（区分好品、坏品）
						TransferOutDepotModel transOutDepModel = (TransferOutDepotModel) orderMap.get(orderKey);
						if(transOutDepModel == null) {
							transOutDepModel = new TransferOutDepotModel();
							transOutDepModel.setCode(orderCode);
							transOutDepModel.setMerchantId(user.getMerchantId());
							transOutDepModel = transOutDepDao.searchByModel(transOutDepModel);

							orderMap.put(orderKey, transOutDepModel);
						}
						if(transOutDepModel == null) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "平台订单编号"+orderCode+"不存在该公司主体下调拨出库单");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						//调拨出库单必须是已出仓
						if(!transOutDepModel.getStatus().equals(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_016)) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "调拨出库单号"+orderCode+"，不为已出仓");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						List<TransferOutDepotItemModel> itemList = (List<TransferOutDepotItemModel>) orderGoodsMap.get(orderKey+"_"+increaseGoodsNo);
						if(itemList==null || itemList.size()==0){
							TransferOutDepotItemModel transOutDepItemModel = new TransferOutDepotItemModel();
							transOutDepItemModel.setTransferDepotId(transOutDepItemModel.getId());
							transOutDepItemModel.setOutGoodsNo(increaseGoodsNo);
							itemList = transOutDepItemDao.list(transOutDepItemModel);

							orderGoodsMap.put(orderKey+"_"+increaseGoodsNo, itemList);
						}
						if(itemList==null || itemList.size()==0){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "调减商品:"+increaseGoodsNo+"在调拨出库单:"+orderCode+"下不存在");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						Integer orderNum = 0;
						if(inventoryType.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_0)) {
							for (TransferOutDepotItemModel item:itemList){
								orderNum=orderNum+item.getTransferNum();
							}
							//调整的数量不能大于调拨入库中的调拨量（区分好品、坏品）
							if(Integer.valueOf(numStr)>orderNum){
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "序号："+index+",商品货号："+increaseGoodsNo+"调整的数量不能大于调拨出库中的好品量");
								msgList.add(msg);
								failure+=1;
								continue;
							}

						}else if(inventoryType.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_1)) {
							for (TransferOutDepotItemModel item:itemList){
								orderNum=orderNum+item.getWornNum();
							}
							//调整的数量不能大于调拨入库中的调拨量（区分好品、坏品）
							if(Integer.valueOf(numStr)>orderNum){
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "序号："+index+",商品货号："+increaseGoodsNo+"调整的数量不能大于调拨出库中的坏品量");
								msgList.add(msg);
								failure+=1;
								continue;
							}

						}
						key =transOutDepModel.getCode()+":"+increaseGoodsNo+":"+inventoryType+":"+typeMap.get(index);
						if(!orderNumMap.containsKey(key)){
							orderNumMap.put(key,orderNum);//订单中数量
						}
						if(!adjustNumMap.containsKey(key)){
							adjustNumMap.put(key,Integer.valueOf(numStr));
						}else{
							Integer num = adjustNumMap.get(key)+Integer.valueOf(numStr);//调整数量
							adjustNumMap.put(key,num);
						}

						tallyingUnit = itemList.get(0).getTallyingUnit();

					}else if(typeMap.get(index).equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_XSCK)) {
						//导入的销售出库单+调增商品货号+库存类型 是否存在该公司主体下的销售出库单，且调整的数量不能大于销售出库中的调拨数量（区分好品、坏品）
						SaleOutDepotModel saleOutDepModel = (SaleOutDepotModel) orderMap.get(orderKey);
						if(saleOutDepModel == null){
							saleOutDepModel = new SaleOutDepotModel();
							saleOutDepModel.setCode(orderCode);
							saleOutDepModel.setMerchantId(user.getMerchantId());
							saleOutDepModel = saleOutDepotDao.searchByModel(saleOutDepModel);

							orderMap.put(orderKey, saleOutDepModel);
						}
						if(saleOutDepModel == null) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "平台订单编号"+orderCode+"不存在该公司主体下销售出库单");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						//销售出库单必须是已出库/部分上架/已上架
						if(!(saleOutDepModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_018) ||
								saleOutDepModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_031) ||
								saleOutDepModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_026))) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "销售出库单号"+orderCode+"，不为已出库/部分上架/已上架");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						// 判断生成库位调整单的订单客户id是否一样
						Long customerId = saleOutDepModel.getCustomerId();
						if (customerId==null) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "平台订单编号"+orderCode+"客户id为空");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						if (customerIdMap.containsKey(index)) {
							List<Long> customerIdList = customerIdMap.get(index);
							if (!customerIdList.contains(customerId))customerIdList.add(customerId);
							customerIdMap.put(index, customerIdList);
							if (customerIdList.size()>1) {
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "平台订单编号"+orderCode+"和其他单号的客户不同,客户名称:"+saleOutDepModel.getCustomerName());
								msgList.add(msg);
								failure+=1;
								continue;
							}
						}else {
							List<Long> customerIdList = new ArrayList<>();
							customerIdList.add(customerId);
							customerIdMap.put(index, customerIdList);
							customerNameMap.put(index, saleOutDepModel.getCustomerName());
						}

						List<SaleOutDepotItemModel> itemList = (List<SaleOutDepotItemModel>) orderGoodsMap.get(orderKey+"_"+increaseGoodsNo);
						if(itemList==null || itemList.size()==0){
							SaleOutDepotItemModel saleOutDepItemModel = new SaleOutDepotItemModel();
							saleOutDepItemModel.setSaleOutDepotId(saleOutDepModel.getId());
							saleOutDepItemModel.setGoodsNo(increaseGoodsNo);
							itemList = saleOutDepotItemDao.list(saleOutDepItemModel);

							orderGoodsMap.put(orderKey+"_"+increaseGoodsNo, itemList);
						}
						if(itemList==null || itemList.size()==0){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "调增商品:"+increaseGoodsNo+"在销售出库单:"+orderCode+"下不存在");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						Integer orderNum = 0;
						if(inventoryType.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_0)) {
							for (SaleOutDepotItemModel item:itemList){
								orderNum=orderNum+item.getTransferNum();
							}
							//调整的数量不能大于销售出库中的调拨量（区分好品、坏品）
							if(Integer.valueOf(numStr)>orderNum){
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "序号："+index+",商品货号："+increaseGoodsNo+"调整的数量不能大于销售出库中的好品量");
								msgList.add(msg);
								failure+=1;
								continue;
							}

						}else if(inventoryType.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_1)) {
							for (SaleOutDepotItemModel item:itemList){
								orderNum=orderNum+item.getWornNum();
							}
							//调整的数量不能大于销售处库中的调拨量（区分好品、坏品）
							if(Integer.valueOf(numStr)>orderNum){
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "序号："+index+",商品货号："+increaseGoodsNo+"调整的数量不能大于销售出库中的坏品量");
								msgList.add(msg);
								failure+=1;
								continue;
							}

						}
						key =saleOutDepModel.getCode()+":"+increaseGoodsNo+":"+inventoryType+":"+typeMap.get(index);
						if(!orderNumMap.containsKey(key)){
							orderNumMap.put(key,orderNum);//订单中数量
						}
						if(!adjustNumMap.containsKey(key)){
							adjustNumMap.put(key,Integer.valueOf(numStr));
						}else{
							Integer num = adjustNumMap.get(key)+Integer.valueOf(numStr);//调整数量
							adjustNumMap.put(key,num);
						}

						tallyingUnit = itemList.get(0).getTallyingUnit();
					}

					if(!typeMap.get(index).equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DSDD)) {
						itemModel.setOrderCode(orderCode);
					}
					itemModel.setExternalCode(orderCode);
					itemModel.setIncreaseGoodsCode(increaseGoodsNo);
					itemModel.setIncreaseGoodsId(increaseGoods.getMerchandiseId());
					itemModel.setIncreaseGoodsNo(increaseGoods.getGoodsNo());
					itemModel.setIncreaseGoodsName(increaseGoods.getName());
					itemModel.setReduceGoodsId(reduceGoods.getMerchandiseId());
					itemModel.setReduceGoodsCode(reduceGoods.getGoodsCode());
					itemModel.setReduceGoodsNo(reduceGoods.getGoodsNo());
					itemModel.setReduceGoodsName(reduceGoods.getName());
					itemModel.setAdjustNum(Integer.valueOf(numStr));
					itemModel.setInventoryType(inventoryType);
					itemModel.setTallyingUnit(tallyingUnit);

					//记录表体信息
					List<InventoryLocationAdjustmentOrderItemModel> itemModelList = new ArrayList<InventoryLocationAdjustmentOrderItemModel>();
					if(itemMap.containsKey(index)){
						itemModelList = itemMap.get(index);
					}
					itemModelList.add(itemModel);
					itemMap.put(index, itemModelList);
				} catch(Exception e){
					e.printStackTrace();
					failure+=1;
					continue;
				}
			}
		}
	}

	for(Map.Entry<String, Integer> entry : adjustNumMap.entrySet()){
		Map<String,String> msg = new HashMap<String,String>();
		String key = entry.getKey();
		String[] split = key.split(":");
		Integer adjustNum = entry.getValue();
		if(orderNumMap.containsKey(key)){
			Integer orderNum = orderNumMap.get(key);
			if(split[2].equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DSDD)) {
				//调整的数量不能大于电商订单中的销售量
				if(Integer.valueOf(adjustNum)>orderNum){
					msg.put("row", "");
					msg.put("msg", "平台订单号："+split[0]+",商品货号："+split[1]+"调整的数量不能大于电商订单中的销售量");
					msgList.add(msg);
					failure+=1;
					continue;
				}
			}else {
				if(split[2].equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_0)) {
					//调整的数量不能大于电商订单中的销售量
					if(Integer.valueOf(adjustNum)>orderNum){
						msg.put("row", "");
						msg.put("msg", "平台订单号："+split[0]+",商品货号："+split[1]+"调整的数量不能大于"+DERP_ORDER.getLabelByKey(DERP_ORDER.inventoryLocationAdjustmentOrder_typeList, split[3])+"单中的好品量");
						msgList.add(msg);
						failure+=1;
						continue;
					}
				}
				if(split[2].equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_1)) {
					//调整的数量不能大于订单中的销售量
					if(Integer.valueOf(adjustNum)>orderNum){
						msg.put("row", "");
						msg.put("msg", "平台订单号："+split[0]+",商品货号："+split[1]+"调整的数量不能大于"+DERP_ORDER.getLabelByKey(DERP_ORDER.inventoryLocationAdjustmentOrder_typeList, split[3])+"单中的坏品量");
						msgList.add(msg);
						failure+=1;
						continue;
					}
				}
			}
		}
	}
	//保存数据
	if(failure==0) {
		for(Map.Entry<String, InventoryLocationAdjustmentOrderModel> entry : modelMap.entrySet()){
			Map<String,String> msg = new HashMap<String,String>();
			String index = entry.getKey();
			InventoryLocationAdjustmentOrderModel inventoryLocationAdjustmentOrderModel = entry.getValue();
			List<Long> customerIdList = customerIdMap.get(index);
			String customerName = null;
			Long customerId=null;
			if (customerIdList!=null&&customerIdList.size()==1) {
				customerId = customerIdList.get(0);
				customerName = customerNameMap.get(index);
			}
			// 校验表体
			List<InventoryLocationAdjustmentOrderItemModel> itemList = itemMap.get(index);
			if(itemList == null || itemList.size() == 0){
				msg.put("row", "");
				msg.put("msg", "序号："+index+"的商品信息为空");
				msgList.add(msg);
				failure+=1;
				continue;
			}
			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(inventoryLocationAdjustmentOrderModel.getMerchantId());
			closeAccountsMongo.setDepotId(inventoryLocationAdjustmentOrderModel.getDepotId());
			closeAccountsMongo.setBuId(inventoryLocationAdjustmentOrderModel.getBuId());
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth = "";
			if (org.apache.commons.lang.StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 校验归属月份必须大于关账月份/月结月份
				if (inventoryLocationAdjustmentOrderModel.getOwnDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					msg.put("row", "");
					msg.put("msg", "序号："+index+"归属日期必须大于关账日期/月结日期");
					msgList.add(msg);
					failure+=1;
					continue;
				}
			}
			if(failure==0){
				inventoryLocationAdjustmentOrderModel.setCustomerId(customerId);
				inventoryLocationAdjustmentOrderModel.setCustomerName(customerName);
				inventoryLocationAdjustmentOrderModel.setCreateDate(TimeUtils.getNow());
				inventoryLocationAdjustmentOrderModel.setCreater(user.getId());
				inventoryLocationAdjustmentOrderModel.setCreateName(user.getName());
				inventoryLocationAdjustmentOrderDao.save(inventoryLocationAdjustmentOrderModel);
				for(InventoryLocationAdjustmentOrderItemModel item:itemList){
					item.setCreateDate(TimeUtils.getNow());
					item.setInventoryLocationId(inventoryLocationAdjustmentOrderModel.getId());
					inventoryLocationAdjustmentOrderItemDao.save(item);
					success++;
				}
			}
		}
	}
		Map map =new HashMap();
		map.put("success",success);
		map.put("pass",pass);
		map.put("failure",failure);
		map.put("msgList",msgList);
		return map;
	}

	/**
	 * 错误时，设置错误内容
	 *
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
	 * 判断输入字段是否为空
	 *
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

	@Override
	public Integer getOrderCount(InventoryLocationAdjustmentOrderDTO dto) throws SQLException {
		return inventoryLocationAdjustmentOrderDao.getOrderCount(dto);
	}

	@Override
	public List<InventoryLocationAdjustExportDTO> getExportMainList(InventoryLocationAdjustmentOrderDTO dto,User user) {
		if (dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buIds.isEmpty()) {
				return new ArrayList<InventoryLocationAdjustExportDTO>();
			}
			dto.setBuList(buIds);
		}
		return inventoryLocationAdjustmentOrderDao.listDto(dto);
	}

	@Override
	public boolean delInventoryLocationAdjust(List ids) throws Exception {
		boolean flag = false;
		// 判断是否有已审核的订单
		for (Object id : ids) {
			// 获取销售订单信息
			InventoryLocationAdjustmentOrderModel adjustmentOrderModel = inventoryLocationAdjustmentOrderDao
					.searchById(Long.parseLong(id.toString()));
			// 单据状态是已审核
			if (adjustmentOrderModel.getState() != null && !adjustmentOrderModel.getState()
					.equals(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_001)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			throw new RuntimeException("只能删除待审核库位调整单");
		}
		if (ids != null && ids.size() > 0) {
			// 先删业务库
			int result = inventoryLocationAdjustmentOrderDao.delete(ids);// 删表头
			int itemResult = inventoryLocationAdjustmentOrderItemDao.delByIdBatch(ids);// 删表体
		}
		return true;
	}

	@Override
	public InventoryLocationAdjustmentOrderDTO searchDetail(Long id) throws SQLException {
		PreSaleOrderDTO dto = new PreSaleOrderDTO();
		dto.setId(id);
		return inventoryLocationAdjustmentOrderDao.searchDTOById(id);
	}

	/**
	 * 根据表头ID获取表体(包括商品信息)
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<InventoryLocationAdjustmentOrderItemDTO> listItemByOrderId(Long id) throws SQLException {
		InventoryLocationAdjustmentOrderItemDTO itemDTO = new InventoryLocationAdjustmentOrderItemDTO();
		itemDTO.setInventoryLocationId(id);
		return inventoryLocationAdjustmentOrderItemDao.listInventoryLocationAdjustDTO(itemDTO);
	}

	@Override
	public void auditInventoryLocationAdjust(List<Long> list, User user) throws Exception {
		for (Long id : list) {
			InventoryLocationAdjustmentOrderModel adjustmentOrderModel = inventoryLocationAdjustmentOrderDao
					.searchById(id);
			if (adjustmentOrderModel == null) {
				throw new RuntimeException("审核失败，库位调整单不存在");
			}
			if (!DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_001.equals(adjustmentOrderModel.getState())) {
				throw new RuntimeException("仅对状态为”待审核“的可操作审核");
			}
			// 获取仓库信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", adjustmentOrderModel.getDepotId());
			DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(params);

			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(adjustmentOrderModel.getMerchantId());
			closeAccountsMongo.setDepotId(adjustmentOrderModel.getDepotId());
			closeAccountsMongo.setBuId(adjustmentOrderModel.getBuId());
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth = "";
			if (org.apache.commons.lang.StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 校验归属月份必须大于关账月份/月结月份
				if (adjustmentOrderModel.getOwnDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("归属日期必须大于关账日期/月结日期");
				}
			}

			InventoryLocationAdjustmentOrderModel updateModel = new InventoryLocationAdjustmentOrderModel();
			updateModel.setId(adjustmentOrderModel.getId());
			updateModel.setState(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_027);// 027:审核中
			updateModel.setAuditor(user.getId());
			updateModel.setAuditName(user.getName());
			updateModel.setAuditDate(TimeUtils.getNow());
			updateModel.setModifyDate(TimeUtils.getNow());
			inventoryLocationAdjustmentOrderDao.modify(updateModel);

			InventoryLocationAdjustmentOrderItemModel itemModel = new InventoryLocationAdjustmentOrderItemModel();
			itemModel.setInventoryLocationId(adjustmentOrderModel.getId());
			List<InventoryLocationAdjustmentOrderItemModel> itemList = inventoryLocationAdjustmentOrderItemDao
					.list(itemModel);

			// 商品收发明细 调增
			List<InventoryGoodsDetailJson> inInventoryGoodsDetailList = new ArrayList<>();
			// 合并数量后的库位调整单商品
			List<InventoryLocationAdjustmentOrderItemModel> mergeNumItemList = new ArrayList<>();
			// 以调增商品+调减商品+好/坏品为一个维度合并,value:总调整数量
			Map<String, InventoryLocationAdjustmentOrderItemModel> mergeGoodsMap = new HashMap<>();
			// 遍历库位调整单下的商品，合并调整数量
			for (int i = 0; i < itemList.size(); i++) {
				InventoryLocationAdjustmentOrderItemModel adjustItemModel = itemList.get(i);
				String key = adjustItemModel.getIncreaseGoodsId() + "," + adjustItemModel.getReduceGoodsId() + ","
						+ adjustItemModel.getInventoryType();

				if (mergeGoodsMap.containsKey(key)) {
					InventoryLocationAdjustmentOrderItemModel itemModel2 = mergeGoodsMap.get(key);
					Integer adjustNum = itemModel2.getAdjustNum();
					itemModel2.setAdjustNum(adjustNum + adjustItemModel.getAdjustNum());
					mergeGoodsMap.put(key, itemModel2);
				} else {
					mergeGoodsMap.put(key, adjustItemModel);
				}
			}
			for (String key : mergeGoodsMap.keySet()) {
				mergeNumItemList.add(mergeGoodsMap.get(key));
			}

			// 遍历合并后的库位调整单下的商品，生成商品收发明细
			for (int i = 0; i < mergeNumItemList.size(); i++) {
				InventoryLocationAdjustmentOrderItemModel adjustItemModel = mergeNumItemList.get(i);
				InventoryGoodsDetailJson inInventoryGoodsDetailJson = new InventoryGoodsDetailJson();
				InventoryGoodsDetailJson outInventoryGoodsDetailJson = new InventoryGoodsDetailJson();

				inInventoryGoodsDetailJson.setGoodsId(String.valueOf(adjustItemModel.getIncreaseGoodsId()));
				inInventoryGoodsDetailJson.setGoodsName(adjustItemModel.getIncreaseGoodsName());
				inInventoryGoodsDetailJson.setGoodsNo(adjustItemModel.getIncreaseGoodsNo());
				Map<String, Object> merchandiseParam = new HashMap<>();
				merchandiseParam.put("merchandiseId", adjustItemModel.getIncreaseGoodsId());
				MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseParam);
				inInventoryGoodsDetailJson.setBarcode(merchandiseInfo.getBarcode());


				outInventoryGoodsDetailJson.setGoodsId(String.valueOf(adjustItemModel.getReduceGoodsId()));
				outInventoryGoodsDetailJson.setGoodsName(adjustItemModel.getReduceGoodsName());
				outInventoryGoodsDetailJson.setGoodsNo(adjustItemModel.getReduceGoodsNo());
				merchandiseParam.clear();
				merchandiseParam.put("merchandiseId", adjustItemModel.getReduceGoodsId());
				merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseParam);
				outInventoryGoodsDetailJson.setBarcode(merchandiseInfo.getBarcode());


				// 海外仓 单位必填
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
					// 单位
					if (StringUtils.isBlank(adjustItemModel.getTallyingUnit())) {
						LOGGER.error("海外仓单位必填");
						throw new RuntimeException("海外仓单位必填");
					}
					if(adjustItemModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
						inInventoryGoodsDetailJson.setUnit(DERP.INVENTORY_UNIT_0);
						outInventoryGoodsDetailJson.setUnit(DERP.INVENTORY_UNIT_0);
					}else if(adjustItemModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
						inInventoryGoodsDetailJson.setUnit(DERP.INVENTORY_UNIT_1);
						outInventoryGoodsDetailJson.setUnit(DERP.INVENTORY_UNIT_1);
					}else if(adjustItemModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
						inInventoryGoodsDetailJson.setUnit(DERP.INVENTORY_UNIT_2);
						outInventoryGoodsDetailJson.setUnit(DERP.INVENTORY_UNIT_2);
					}
				}
				inInventoryGoodsDetailJson.setType(adjustItemModel.getInventoryType());// 库存类型
				inInventoryGoodsDetailJson.setBuId(String.valueOf(adjustmentOrderModel.getBuId())); // 事业部
				inInventoryGoodsDetailJson.setBuName(adjustmentOrderModel.getBuName());
				inInventoryGoodsDetailJson.setNum(adjustItemModel.getAdjustNum());
				inInventoryGoodsDetailJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 库存加减操作类型 1-调增
				inInventoryGoodsDetailList.add(inInventoryGoodsDetailJson);

				outInventoryGoodsDetailJson.setType(adjustItemModel.getInventoryType());// 库存类型
				outInventoryGoodsDetailJson.setBuId(String.valueOf(adjustmentOrderModel.getBuId())); // 事业部
				outInventoryGoodsDetailJson.setBuName(adjustmentOrderModel.getBuName());
				outInventoryGoodsDetailJson.setNum(adjustItemModel.getAdjustNum());
				outInventoryGoodsDetailJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 库存加减操作类型 0-调减
				inInventoryGoodsDetailList.add(outInventoryGoodsDetailJson);

			}

			// 调增实体
			InventoryDetailJson inInventoryDetailJson = new InventoryDetailJson();
			inInventoryDetailJson.setMerchantId(String.valueOf(adjustmentOrderModel.getMerchantId()));
			inInventoryDetailJson.setMerchantName(adjustmentOrderModel.getMerchantName());
			inInventoryDetailJson.setTopidealCode(user.getTopidealCode());// 卓志编码
			inInventoryDetailJson.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));
			inInventoryDetailJson.setDepotCode(depotInfoMongo.getDepotCode());
			inInventoryDetailJson.setDepotName(depotInfoMongo.getName());
			inInventoryDetailJson.setDepotType(depotInfoMongo.getType());
			inInventoryDetailJson.setOrderNo(adjustmentOrderModel.getCode());// 库位调整单号
			inInventoryDetailJson.setOwnMonth(TimeUtils.format(adjustmentOrderModel.getOwnDate(), "yyyy-MM"));// 归属月份
			inInventoryDetailJson
					.setDivergenceDate(TimeUtils.format(adjustmentOrderModel.getOwnDate(), "yyyy-MM-dd HH:mm:ss"));// 变更日期
			inInventoryDetailJson.setSourceDate(TimeUtils.formatFullTime());// 单据日期
			inInventoryDetailJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0020);// 0020-库位调整单
			inInventoryDetailJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0032);
			inInventoryDetailJson.setBusinessNo(adjustmentOrderModel.getCode());// 库位调整单号

			inInventoryDetailJson.setGoodsList(inInventoryGoodsDetailList);

			// 回调设置
			Map<String, Object> customParam = new HashMap<String, Object>();
			inInventoryDetailJson
					.setBackTags(MQPushBackOrderEnum.INVENTORY_LOCATION_ADJUSTMENT_DETAIL_PUSH_BACK.getTags());// 回调标签
			inInventoryDetailJson
					.setBackTopic(MQPushBackOrderEnum.INVENTORY_LOCATION_ADJUSTMENT_DETAIL_PUSH_BACK.getTopic());// 回调主题
			customParam.put("code", adjustmentOrderModel.getCode());// 移库单单号
			inInventoryDetailJson.setCustomParam(customParam);//// 自定义回调参数
			// 库位调整单审核生成商品收发明细--调增
			rocketMQProducer.send(JSONObject.fromObject(inInventoryDetailJson).toString(),
					MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTopic(),
					MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTags());

		}
	}

	/**
	 * 把两个数组组成一个map
	 *
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

}
