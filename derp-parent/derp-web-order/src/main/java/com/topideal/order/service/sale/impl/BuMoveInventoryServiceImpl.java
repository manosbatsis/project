package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.BuMoveInventoryDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryExportDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryItemDTO;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j06.InventoryDetailJson;
import com.topideal.json.inventory.j06.InventoryGoodsDetailJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.BuMoveInventoryService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 事业部移库单service实现类
 */
@Service
public class BuMoveInventoryServiceImpl implements BuMoveInventoryService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(BuMoveInventoryServiceImpl.class);
	// 事业部移库单表头
	@Autowired
	private BuMoveInventoryDao buMoveInventoryDao;
	// 事业部移库单表体
	@Autowired
	private BuMoveInventoryItemDao buMoveInventoryItemDao;
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 仓库
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private SaleOrderDao saleOrderDao;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	// 商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 运单
	@Autowired
	private WayBillDao wayBillDao;
	// 运单
	@Autowired
	private WayBillItemDao wayBillItemDao;
	// 电商订单商品
	@Autowired
	private OrderItemDao orderItemDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	// 商家信息
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	// 汇率信息
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	//财务经销存关账mongdb
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private  OperateLogDao operateLogDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 */
	@Override
	public BuMoveInventoryDTO listBuMoveInventoryByPage(BuMoveInventoryDTO dto,User user)
			throws SQLException {
		List<Long> outBuList = new ArrayList<>();
		List<Long> inBuList = new ArrayList<>();
		int isFlag=0;// 2：移出移入事业部都为空，列表分页没按查询条件时
		if(dto.getOutBuId() == null) {//移出事业部
			isFlag=1;
			outBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(outBuList!=null && outBuList.size()>0) {
				dto.setOutBuList(outBuList);
			}
		}
		if(dto.getInBuId() == null) {//移入事业部
			isFlag=isFlag+1;
			inBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(inBuList!=null && inBuList.size()>0) {
				dto.setInBuList(inBuList);
			}
		}
		if(isFlag==2){
			if(outBuList.isEmpty() && inBuList.isEmpty()){
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
		}

		return buMoveInventoryDao.queryDTOListByPage(dto);
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public BuMoveInventoryDTO searchDetail(Long id) throws SQLException {
		BuMoveInventoryDTO dto = new BuMoveInventoryDTO();
		dto.setId(id);
		return buMoveInventoryDao.searchDTOById(id);
	}

	@Override
	public List<BuMoveInventoryItemDTO> listItemByOrderId(Long id) throws SQLException {
		BuMoveInventoryItemDTO buMoveInventoryItemDTO = new BuMoveInventoryItemDTO();
		buMoveInventoryItemDTO.setMoveId(id);//事业部移库单ID
		return buMoveInventoryItemDao.lisBuMoveInventoryItemDTO(buMoveInventoryItemDTO);
	}

	@Override
	public List<BuMoveInventoryDTO> listBuMoveInventoryDTO(BuMoveInventoryDTO dto,User user) throws SQLException {
		List<Long> outBuList = new ArrayList<>();
		List<Long> inBuList = new ArrayList<>();
		int isFlag=0;// 2：移出移入事业部都为空，列表分页没按查询条件时
		if(dto.getOutBuId() == null) {//移出事业部
			isFlag=1;
			outBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(outBuList!=null && outBuList.size()>0) {
				dto.setOutBuList(outBuList);
			}
		}
		if(dto.getInBuId() == null) {//移入事业部
			isFlag=isFlag+1;
			inBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(inBuList!=null && inBuList.size()>0) {
				dto.setInBuList(inBuList);
			}
		}
		if(isFlag==2){
			if(outBuList.isEmpty() && inBuList.isEmpty()){
				return new ArrayList<BuMoveInventoryDTO>();
			}
		}
		return buMoveInventoryDao.queryDTOList(dto);
	}

	@Override
	public List<BuMoveInventoryExportDTO> getDetailsByExport(BuMoveInventoryDTO dto,User user) throws SQLException {
		List<Long> outBuList = new ArrayList<>();
		List<Long> inBuList = new ArrayList<>();
		int isFlag=0;// 2：移出移入事业部都为空，列表分页没按查询条件时
		if(dto.getOutBuId() == null) {//移出事业部
			isFlag=1;
			outBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(outBuList!=null && outBuList.size()>0) {
				dto.setOutBuList(outBuList);
			}
		}
		if(dto.getInBuId() == null) {//移入事业部
			isFlag=isFlag+1;
			inBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(inBuList!=null && inBuList.size()>0) {
				dto.setInBuList(inBuList);
			}
		}
		if(isFlag==2){
			if(outBuList.isEmpty() && inBuList.isEmpty()){
				return new ArrayList<BuMoveInventoryExportDTO>();
			}
		}
		List<BuMoveInventoryExportDTO> list = buMoveInventoryDao.getDetailsByExport(dto);
		return list;
	}

	@Override
	public boolean delBuMoveInventory(List ids) throws Exception {
		int flag = 0;
		for (Object id : ids) {
			BuMoveInventoryModel buMoveInventoryModel = buMoveInventoryDao.searchById(Long.parseLong(id.toString()));
			if (!DERP_ORDER.BUMOVEINVENTORY_STATUS_017.equals(buMoveInventoryModel.getStatus()) ||
				!DERP_ORDER.BUMOVEINVENTORY_ORDERSOURCE_1.equals(buMoveInventoryModel.getOrderSource())) {//017-待移库;单据来源  1：手工导入
				flag = 1;
				break;
			}
		}
		if (flag == 1) {
			throw new RuntimeException("只能删除手工导入的待审核订单");
		}

		if (ids != null && ids.size() > 0) {
			// 先删业务库
			int result = buMoveInventoryDao.delete(ids);// 删表头
			int itemResult = buMoveInventoryItemDao.delByIdBatch(ids);// 删表体

		}
		return true;
	}

	/**
	 * 导入事业部移库单
	 * @param data
	 * @param userId
	 * @param name
	 * @param merchantId
	 * @param merchantName
	 * @return
	 */
	@Override
	public Map saveImportBuMoveInventory(Map<Integer, List<List<Object>>> data, Long userId, String name, Long merchantId, String merchantName,String userTopidealCode) {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		// 存储事业部移库单表头
		Map<String,BuMoveInventoryModel> buMoveInventoryMap = new HashMap<String,BuMoveInventoryModel>();
		//存储事业部移库单表体
		Map<String , List<BuMoveInventoryItemModel>> buMoveInventoryItemMap = new HashMap<String , List<BuMoveInventoryItemModel>>();
		// 检查某自编码+该仓库公司关联的信息
		Map<String , DepotMerchantRelMongo> checkDepotMerchantRelMap = new HashMap<>();
		// 存储自编码+销售订单
		Map<String,SaleOrderModel> saleOrderModelMap = new HashMap<String,SaleOrderModel>();
		// 存储自编码+电商订单
		Map<String,OrderModel> orderModelMap = new HashMap<String,OrderModel>();
		// 判断表头自编码是否重复
		List<String> zdyCheckList = new ArrayList<>();
		Map<Long , MerchantBuRelMongo> merchantBuRelMap = new HashMap<>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		for (int i = 0; i < 1; i++) {// 表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(0).get(0).toArray(), objects.get(j).toArray());
				try {
					//自编单号
					String zdyCode = map.get("自编单号");
					if (checkIsNullOrNot(j, zdyCode, "自编单号不能为空", resultList)) {
						failure += 1;
						continue;
					}
					zdyCode=zdyCode.trim();

					if (zdyCheckList.contains(zdyCode)) {
						setErrorMsg(j, "自编单号:"+zdyCode+"不能重复", resultList);
						failure += 1;
						continue;
					}
					zdyCheckList.add(zdyCode);

					String outBuCode = map.get("移出事业部");
					if (checkIsNullOrNot(j, outBuCode, "移出事业部不能为空", resultList)) {
						failure += 1;
						continue;
					}
					outBuCode = outBuCode.trim();
					Map<String, Object> queryMap = new HashMap<String, Object>() ;
					queryMap.put("buCode", outBuCode) ;
					queryMap.put("merchantId", merchantId) ;
					MerchantBuRelMongo outMerchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap);
					if(outMerchantBuRelMongo == null) {
						setErrorMsg(j, "移出事业部:"+outBuCode+"公司事业部关联不存在或已禁用", resultList);
						failure += 1;
						continue;
					}
					if(!merchantBuRelMap.containsKey(outMerchantBuRelMongo.getBuId())){
						merchantBuRelMap.put(outMerchantBuRelMongo.getBuId(), outMerchantBuRelMongo);
					}

					String inBuCode = map.get("移入事业部");
					if (checkIsNullOrNot(j, inBuCode, "移入事业部不能为空", resultList)) {
						failure += 1;
						continue;
					}
					inBuCode = inBuCode.trim();
					queryMap.clear();
					queryMap.put("buCode", inBuCode) ;
					queryMap.put("merchantId", merchantId) ;
					MerchantBuRelMongo inMerchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap);
					if(inMerchantBuRelMongo == null) {
						setErrorMsg(j, "移入事业部:"+inBuCode+"公司事业部关联不存在或已禁用", resultList);
						failure += 1;
						continue;
					}
					if(!merchantBuRelMap.containsKey(inMerchantBuRelMongo.getBuId())){
						merchantBuRelMap.put(inMerchantBuRelMongo.getBuId(), inMerchantBuRelMongo);
					}

					String depotCode = map.get("仓库");
					if (checkIsNullOrNot(j, depotCode, "仓库不能为空", resultList)) {
						failure += 1;
						continue;
					}
					depotCode = depotCode.trim();
					Map<String, Object> depotInfoParams = new HashMap<String, Object>();
					depotInfoParams.put("depotCode", depotCode);
					DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfoParams);
					if (depot == null) {
						setErrorMsg(j, "仓库不存在", resultList);
						failure += 1;
						continue;
					}
					depotInfoParams.clear();
					depotInfoParams.put("depotId", depot.getDepotId());
					depotInfoParams.put("merchantId", merchantId);
					DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotInfoParams);

					if (depotMerchantRel == null) {
						setErrorMsg(j, "商家仓库关联不存在", resultList);
						failure += 1;
						continue;
					}

					// 校验公司-仓库-移出事业部的关联表
					Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
					merchantDepotBuRelParam.put("merchantId", merchantId);
					merchantDepotBuRelParam.put("depotId", depot.getDepotId());	// 仓库
					merchantDepotBuRelParam.put("buId", outMerchantBuRelMongo.getBuId());//移出事业部
					MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
					if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
						setErrorMsg(j, "移出事业部编码为："+outMerchantBuRelMongo.getBuCode()+",仓库："+depot.getDepotCode()+",未查到该公司仓库事业部关联信息", resultList);
						failure += 1;
						continue;
					}
					// 校验公司-仓库-移入事业部的关联表
					merchantDepotBuRelParam.put("buId", inMerchantBuRelMongo.getBuId());//移入事业部
					MerchantDepotBuRelMongo inMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
					if(inMerchantDepotBuRelMongo == null || "".equals(inMerchantDepotBuRelMongo)){
						setErrorMsg(j, "移入事业部编码为："+inMerchantBuRelMongo.getBuCode()+",仓库："+depot.getDepotCode()+",未查到该公司仓库事业部关联信息", resultList);
						failure += 1;
						continue;
					}
					// 校验移出事业部与当前账号所绑定的事业部是否匹配
					boolean outRelateBu = userBuRelMongoDao.isUserRelateBu(userId, outMerchantBuRelMongo.getBuId());
					if(!outRelateBu){
						setErrorMsg(j, "移出事业部编码为："+outMerchantBuRelMongo.getBuCode()+",用户id："+userId+",无权限操作该事业部", resultList);
						failure += 1;
						continue;
					}
					// 校验移入事业部与当前账号所绑定的事业部是否匹配
					boolean inRelateBu = userBuRelMongoDao.isUserRelateBu(userId, inMerchantBuRelMongo.getBuId());
					if(!outRelateBu){
						setErrorMsg(j, "移入事业部编码为："+inMerchantBuRelMongo.getBuCode()+",用户id："+userId+",无权限操作该事业部", resultList);
						failure += 1;
						continue;
					}



					String moveDateStr = map.get("移库日期");
					if (checkIsNullOrNot(j, moveDateStr, "移库日期不能为空", resultList)) {
						failure += 1;
						continue;
					}
					moveDateStr = moveDateStr.trim();
					if(!isDate(moveDateStr)) {
						setErrorMsg(j, "移库日期格式不正确，正确为：yyyy-MM-dd", resultList);
						failure += 1;
						continue;
					}

					if(TimeUtils.daysBetween(TimeUtils.parse(moveDateStr, "yyyy-MM-dd"), new Date()) < 0) {
						setErrorMsg(j, "移库日期不可超过当前时间", resultList);
						failure += 1;
						continue;
					}

					String orderType = map.get("来源单据类别");
					if (checkIsNullOrNot(j, orderType, "来源单据类别别不能为空", resultList)) {
						failure += 1;
						continue;
					}
					orderType = orderType.trim();
					if(!DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD.equals(orderType) &&
							!DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_XSDD.equals(orderType)&&
							!DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_KCYKD.equals(orderType)){
						setErrorMsg(j, "单据类别只能为电商订单或者销售订单或者 库存移库单", resultList);
						failure += 1;
						continue;
					}

					// 电商订单 或者消失订单  业务单号不能为空
					// 必填字段的校验
					String businessNo=null;
					if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD.equals(orderType) ||
							DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_XSDD.equals(orderType)){
						businessNo = map.get("来源业务单号");
						if (checkIsNullOrNot(j, businessNo, "来源业务单号不能为空", resultList)) {
							failure += 1;
							continue;
						}
						businessNo = businessNo.trim();
						if (buMoveInventoryMap.containsKey(businessNo)) {
							setErrorMsg(j, "来源业务单号："+businessNo+"出现重复", resultList);
							failure += 1;
							continue;
						}
					}


					String tallyingUnit = map.get("海外理货单位");
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
						if (checkIsNullOrNot(j, tallyingUnit, "海外理货单位不能为空", resultList)) {
							failure += 1;
							continue;
						}

						if(!DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit) &&
								!DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)&&
								!DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)){
							setErrorMsg(j, "海外理货单位 只能是 托 或者 箱 或者件", resultList);
							failure += 1;
							continue;
						}
					}else {
						tallyingUnit=null;
					}


					String key=zdyCode;
					if(!checkDepotMerchantRelMap.containsKey(key)){
						checkDepotMerchantRelMap.put(key, depotMerchantRel);
					}
					// 单据类别(电商订单、销售订单),根据来源业务单号是否存在
					OrderModel orderModel = new OrderModel();
					SaleOrderModel saleOrderModel = new SaleOrderModel();
					if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD.equals(orderType)){	// 电商订单
						orderModel.setMerchantId(merchantId);
						orderModel.setCode(businessNo);
						orderModel = orderDao.searchByModel(orderModel);
						if(orderModel==null){
							setErrorMsg(j, "来源业务单号:"+businessNo+"不存在", resultList);
							failure += 1;
							continue;
						}
						if(!orderModelMap.containsKey(zdyCode)){
							orderModelMap.put(zdyCode,orderModel);
						}
						// 仓库必须存在系统单据里的出库仓库；
						if(!depot.getDepotId().equals(orderModel.getDepotId())){
							setErrorMsg(j, "来源业务单号:"+businessNo+"下没有该仓库："+depotCode, resultList);
							failure += 1;
							continue;
						}
					}else if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_XSDD.equals(orderType)){ // 销售订单
						saleOrderModel.setMerchantId(merchantId);
						saleOrderModel.setCode(businessNo);
						saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
						if(saleOrderModel==null){
							setErrorMsg(j, "来源业务单号:"+businessNo+"不存在", resultList);
							failure += 1;
							continue;
						}
						if(!saleOrderModelMap.containsKey(zdyCode)){
							saleOrderModelMap.put(zdyCode,saleOrderModel);
						}
						// 仓库必须存在系统单据里的出库仓库；
						if(!depot.getDepotId().equals(saleOrderModel.getOutDepotId())){
							setErrorMsg(j, "来源业务单号:"+businessNo+"下没有该仓库："+depotCode, resultList);
							failure += 1;
							continue;
						}
					}


					// 获取最大的关账日/月结日期
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(merchantId);
					closeAccountsMongo.setDepotId(depot.getDepotId());
					closeAccountsMongo.setBuId(outMerchantBuRelMongo.getBuId());//移出事业部
					String outMaxdate = "";
					if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
						outMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
					}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
						outMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
					}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
						outMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
					}
					String outMaxCloseAccountsMonth="";
					if (org.apache.commons.lang.StringUtils.isNotBlank(outMaxdate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(outMaxdate+"-01 00:00:00"));
						outMaxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(outMaxCloseAccountsMonth)) {
						// 校验移库日期归属月份必须大于关账月份/月结月份
						if (Timestamp.valueOf(moveDateStr + " 00:00:00").getTime()<Timestamp.valueOf(outMaxCloseAccountsMonth).getTime()) {
							setErrorMsg(j,"自编单号:"+zdyCode+"移出事业部："+outMerchantBuRelMongo.getBuName()+",移库日期必须大于关账日期/月结日期", resultList);
							failure += 1;
							continue;
						}
					}
					closeAccountsMongo.setMerchantId(merchantId);
					closeAccountsMongo.setDepotId(depot.getDepotId());
					closeAccountsMongo.setBuId(inMerchantBuRelMongo.getBuId());//移入事业部
					String inMaxdate = "";
					if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
						inMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
					}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
						inMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
					}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
						inMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
					}
					String inMaxCloseAccountsMonth="";
					if (org.apache.commons.lang.StringUtils.isNotBlank(inMaxdate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(inMaxdate+"-01 00:00:00"));
						inMaxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(inMaxCloseAccountsMonth)) {
						// 校验移库日期归属月份必须大于关账月份/月结月份
						if (Timestamp.valueOf(moveDateStr  + " 00:00:00").getTime()<Timestamp.valueOf(inMaxCloseAccountsMonth).getTime()) {
							setErrorMsg(j, "自编单号:"+zdyCode+"移入事业部："+inMerchantBuRelMongo.getBuName()+",移库日期必须大于关账日期/月结日期", resultList);
							failure += 1;
							continue;
						}
					}

					// 判断商品，给移库单单价、币种、金额赋值
					Map<String,Object> merchantInfoMap = new HashMap();
					merchantInfoMap.put("merchantId",merchantId);
					MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantInfoMap);
					String accountCurrency = merchantInfoMongo.getAccountCurrency();// 成本核算币种

					String agreementCurrency = map.get("协议币种");
					agreementCurrency = agreementCurrency.trim();
					String currency1=null;
					if(agreementCurrency == null || "".equals(agreementCurrency)){
						setErrorMsg(j, "自编单号:"+zdyCode+"协议币种不能为空", resultList);
						failure += 1;
						continue;
					}else{
						for (int k = 0; k < DERP.currencyCodeList.size(); k++) {
							DerpBasic derpBasic = DERP.currencyCodeList.get(k);
							if(derpBasic.getKey().equals(agreementCurrency)){
								currency1 = (String) derpBasic.getKey();
							}
						}
						if(StringUtils.isBlank(currency1)){
							setErrorMsg(j, "自编单号:"+zdyCode+"协议币种输入有误", resultList);
							failure += 1;
							continue;
						}
					}

					// 保存移库单
					BuMoveInventoryModel buMoveInventoryModel = new BuMoveInventoryModel();
					String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_SYBYK);
					buMoveInventoryModel.setCode(code);// 事业部移库单号
					if (DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD.equals(orderType) ||DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_XSDD.equals(orderType)) {
						buMoveInventoryModel.setBusinessNo(businessNo);
					}else {
						buMoveInventoryModel.setBusinessNo(code);
					}
					buMoveInventoryModel.setTallyingUnit(tallyingUnit);
					buMoveInventoryModel.setOrderSource(DERP_ORDER.BUMOVEINVENTORY_ORDERSOURCE_1);//单据来源  1：手工导入
					buMoveInventoryModel.setStatus(DERP_ORDER.BUMOVEINVENTORY_STATUS_017);//移库状态 017-待移库
					buMoveInventoryModel.setDepotId(depot.getDepotId());
					buMoveInventoryModel.setDepotName(depot.getName());
					buMoveInventoryModel.setInBuId(inMerchantBuRelMongo.getBuId());
					buMoveInventoryModel.setInBuName(inMerchantBuRelMongo.getBuName());
					buMoveInventoryModel.setOutBuId(outMerchantBuRelMongo.getBuId());
					buMoveInventoryModel.setOutBuName(outMerchantBuRelMongo.getBuName());
					buMoveInventoryModel.setMoveDate(TimeUtils.parseFullTime(moveDateStr + " 00:00:00"));
					buMoveInventoryModel.setMerchantId(merchantId);
					buMoveInventoryModel.setMerchantName(merchantName);
					buMoveInventoryModel.setCreater(userId);
					buMoveInventoryModel.setCreateName(name);
					buMoveInventoryModel.setCreateDate(TimeUtils.getNow());
					buMoveInventoryModel.setOrderType(orderType);
					buMoveInventoryModel.setAgreementCurrency(agreementCurrency);
					buMoveInventoryModel.setCurrency(accountCurrency);// 移库币种
					buMoveInventoryMap.put(zdyCode, buMoveInventoryModel);

				} catch (Exception e) {
					e.printStackTrace();
					failure += 1;
					continue;
				}
			}
		}

		Set<String> checkGoodsNoSet = new HashSet<>();// 检查来源业务单号+商品货号是否唯一
		if(failure==0){
			for (int i = 1; i < 2; i++) {//表体
				List<List<Object>> objects = data.get(i);
				for (int j = 1; j < objects.size(); j++) {
					try {
						Map<String,String> msg = new HashMap<String,String>();
						Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
						// 必填字段的校验
						String zdyCode = map.get("自编编码");
						if (checkIsNullOrNot(j, zdyCode, "自编编码不能为空", resultList)) {
							failure += 1;
							continue;
						}
						if (!buMoveInventoryMap.containsKey(zdyCode)) {
							setErrorMsg(j, "自编编码"+zdyCode+"在表头不存在", resultList);
							failure += 1;
							continue;
						}
						String goodsNo = map.get("商品货号");
						if (checkIsNullOrNot(j, goodsNo, "商品货号不能为空", resultList)) {
							failure += 1;
							continue;
						}
						goodsNo = goodsNo.trim();

						// 查询商品
						Map<String, Object> merchandiseMap = new HashMap<String, Object>();
						merchandiseMap.put("goodsNo", goodsNo);
						merchandiseMap.put("merchantId", merchantId);
						merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
						String key=zdyCode;
						if(checkDepotMerchantRelMap.containsKey(key)) {
							DepotMerchantRelMongo depotMerchantRel = checkDepotMerchantRelMap.get(key);
							if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRel.getProductRestriction())) {
								merchandiseMap.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);// 是否备案 1-是
							}

							if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRel.getProductRestriction())) {
								merchandiseMap.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
							}
						}
						MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);
						if (merchandise == null) {
							setErrorMsg(j, "商品不存在,货号：" + goodsNo, resultList);
							failure += 1;
							continue;
						}
						// 根据单据类型，判断该商品是否在销售单或电商订单的表体里
						BuMoveInventoryModel buMoveInventoryModel = buMoveInventoryMap.get(zdyCode);
						if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD.equals(buMoveInventoryModel.getOrderType())){
							OrderModel orderModel = orderModelMap.get(zdyCode);
							OrderItemModel orderItemModel = new OrderItemModel();
							orderItemModel.setOrderId(orderModel.getId());
							orderItemModel.setGoodsId(merchandise.getMerchandiseId());
							List<OrderItemModel> orderItemList = orderItemDao.list(orderItemModel);
							if(orderItemList==null || orderItemList.size()==0){
								setErrorMsg(j,  "自编编码"+zdyCode+"的商品货号：" + goodsNo+"在电商订单："+orderModel.getExternalCode()+"下不存在", resultList);
								failure += 1;
								continue;
							}
						}else if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_XSDD.equals(buMoveInventoryModel.getOrderType())){
							SaleOrderModel saleOrderModel = saleOrderModelMap.get(zdyCode);
							SaleOrderItemModel itemModel = new SaleOrderItemModel();
							itemModel.setOrderId(saleOrderModel.getId());
							itemModel.setGoodsId(merchandise.getMerchandiseId());
							List<SaleOrderItemModel> itemList = saleOrderItemDao.list(itemModel);
							if(itemList==null || itemList.size()==0){
								setErrorMsg(j,  "自编编码"+zdyCode+"商的品货号：" + goodsNo+"在销售订单："+saleOrderModel.getCode()+"下不存在", resultList);
								failure += 1;
								continue;
							}
						}
						String saleNumStr = map.get("移库数量");
						if (StringUtils.isEmpty(saleNumStr) || !StrUtils.stringReg(saleNumStr, "[0-9]*")) {
							setErrorMsg(j, "移库数量不正确", resultList);
							failure += 1;
							continue;
						}
						saleNumStr = saleNumStr.trim();

						int saleNum = Integer.parseInt(saleNumStr);
						if (saleNum <= 0) {
							setErrorMsg(j, "移库数量	必须大于0，且不能小于0", resultList);
							failure += 1;
							continue;
						}
						// 检查来源业务单号+商品货号是否唯一
						String isKey=zdyCode+goodsNo;
						if(!checkGoodsNoSet.contains(isKey)){
							checkGoodsNoSet.add(isKey);
						}else{
							setErrorMsg(j, "自编单号:"+zdyCode+"的商品货号:"+goodsNo+"有多条数据,请合并后导入", resultList);
							failure += 1;
							continue;
						}
						String agreementPrice = map.get("协议单价");
						if(StringUtils.isBlank(agreementPrice)){
							setErrorMsg(j, "自编单号:"+zdyCode+"商品货号:"+goodsNo+"协议单价不能为空", resultList);
							failure += 1;
							continue;
						}
						if(StringUtils.isNotBlank(agreementPrice)&&!isNumber(agreementPrice)){
							setErrorMsg(j, "自编单号:"+zdyCode+"商品货号:"+goodsNo+"协议单价应为数字", resultList);
							failure += 1;
							continue;
						}
						String type = map.get("库存类型");
						if(StringUtils.isBlank(type)){
							setErrorMsg(j, "自编单号:"+zdyCode+"商品货号:"+goodsNo+"库存类型不能为空", resultList);
							failure += 1;
							continue;
						}
						if (!DERP_ORDER.BUMOVEINVENTORYITEM_TYPE_0.equals(type)&&!DERP_ORDER.BUMOVEINVENTORYITEM_TYPE_1.equals(type)) {
							setErrorMsg(j, "自编单号:"+zdyCode+"商品货号:"+goodsNo+"库存类型不正确", resultList);
							failure += 1;
							continue;
						}

						String inStockLocationName = map.get("移入库位类型");
						//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
						MerchantBuRelMongo inMerchantBuRelMongo =  merchantBuRelMap.get(buMoveInventoryModel.getInBuId());
						if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(inMerchantBuRelMongo.getStockLocationManage()) && StringUtils.isBlank(inStockLocationName)){
							setErrorMsg(j, "自编单号:"+zdyCode+"当前公司主体下，移入事业部编码：" + inMerchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空", resultList);
							failure += 1;
							continue;
						}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(inMerchantBuRelMongo.getStockLocationManage()) && StringUtils.isNotBlank(inStockLocationName)){
							setErrorMsg(j, "自编单号:"+zdyCode+"当前公司主体下，移入事业部编码：" + inMerchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写", resultList);
							failure += 1;
							continue;
						}
						BuStockLocationTypeConfigMongo inStockLocationMongo = null;
						if(StringUtils.isNotBlank(inStockLocationName)){
							Map<String,Object> stockLocationMap = new HashMap<>();
							stockLocationMap.put("merchantId", merchantId);
							stockLocationMap.put("buId", inMerchantBuRelMongo.getBuId());
							stockLocationMap.put("name", inStockLocationName);
							stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
							inStockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);

							if(inStockLocationMongo == null){
								setErrorMsg(j, "自编单号:"+zdyCode+"当前公司主体下，移入事业部编码：" + inMerchantBuRelMongo.getBuCode()+"库位类型："+inStockLocationName+"，不存在", resultList);
								failure += 1;
								continue;
							}
						}
						String outStockLocationName = map.get("移出库位类型");
						//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
						MerchantBuRelMongo outMerchantBuRelMongo =  merchantBuRelMap.get(buMoveInventoryModel.getOutBuId());
						if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(outMerchantBuRelMongo.getStockLocationManage()) && StringUtils.isBlank(outStockLocationName)){
							setErrorMsg(j, "自编单号:"+zdyCode+"当前公司主体下，移出事业部编码：" + outMerchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空", resultList);
							failure += 1;
							continue;
						}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(outMerchantBuRelMongo.getStockLocationManage()) && StringUtils.isNotBlank(outStockLocationName)){
							setErrorMsg(j, "自编单号:"+zdyCode+"当前公司主体下，移出事业部编码：" + outMerchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写", resultList);
							failure += 1;
							continue;
						}
						BuStockLocationTypeConfigMongo outStockLocationMongo = null;
						if(org.apache.commons.lang3.StringUtils.isNotBlank(outStockLocationName)){
							Map<String,Object> stockLocationMap = new HashMap<>();
							stockLocationMap.put("merchantId", merchantId);
							stockLocationMap.put("buId", outMerchantBuRelMongo.getBuId());
							stockLocationMap.put("name", outStockLocationName);
							stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
							outStockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);

							if(outStockLocationMongo == null){
								setErrorMsg(j, "自编单号:"+zdyCode+"当前公司主体下，移出事业部编码：" + inMerchantBuRelMongo.getBuCode()+"库位类型："+outStockLocationName+"，不存在", resultList);
								failure += 1;
								continue;
							}
						}

						// 存储事业部移库商品
						BuMoveInventoryItemModel itemSaveModel = new BuMoveInventoryItemModel();
						itemSaveModel.setType(type);
						itemSaveModel.setGoodsNo(merchandise.getGoodsNo());
						itemSaveModel.setGoodsCode(merchandise.getGoodsCode());
						itemSaveModel.setGoodsId(merchandise.getMerchandiseId());
						itemSaveModel.setGoodsName(merchandise.getName());
						itemSaveModel.setBarcode(merchandise.getBarcode());
						itemSaveModel.setCommbarcode(merchandise.getCommbarcode());// 标准条码
						itemSaveModel.setNum(saleNum);
						itemSaveModel.setCreateDate(TimeUtils.getNow());
						itemSaveModel.setAgreementPrice(new BigDecimal(agreementPrice));//协议单价
						if(inStockLocationMongo != null){
							itemSaveModel.setInStockLocationTypeId(inStockLocationMongo.getBuStockLocationTypeId());
							itemSaveModel.setInStockLocationTypeName(inStockLocationMongo.getName());
						}
						if(outStockLocationMongo != null){
							itemSaveModel.setOutStockLocationTypeId(outStockLocationMongo.getBuStockLocationTypeId());
							itemSaveModel.setOutStockLocationTypeName(outStockLocationMongo.getName());
						}

						// 判断商品，给移库单单价、币种、金额赋值
						String accountCurrency = buMoveInventoryModel.getCurrency();// 移库币种
						if(buMoveInventoryModel.getAgreementCurrency().equals(accountCurrency)){
							itemSaveModel.setPrice(new BigDecimal(agreementPrice));//移库单价
							itemSaveModel.setAmount(new BigDecimal(agreementPrice).multiply(new BigDecimal(saleNum)));//移库金额
							itemSaveModel.setRate(1.0);//汇率
						}else{
							// 查汇率表
							Map<String,Object> exchangeRateMap = new HashMap<>();
							exchangeRateMap.put("origCurrencyCode",buMoveInventoryModel.getAgreementCurrency());
							exchangeRateMap.put("tgtCurrencyCode",accountCurrency);
							exchangeRateMap.put("effectiveDate",TimeUtils.format(buMoveInventoryModel.getMoveDate(), "yyyy-MM-dd"));//根据移库日期
							List<ExchangeRateMongo> exchangeRateList = exchangeRateMongoDao.findAll(exchangeRateMap);
							if(exchangeRateList!=null && exchangeRateList.size()==1){
								ExchangeRateMongo exchangeRateMongo = exchangeRateList.get(0);
								if(null!=exchangeRateMongo.getRate()){
									Double rate = exchangeRateMongo.getRate();
									BigDecimal rateBd = new BigDecimal(rate);//汇率
									BigDecimal price = new BigDecimal(agreementPrice).multiply(rateBd);
									itemSaveModel.setPrice(price);//移库单价
									BigDecimal amountBd = price.multiply(new BigDecimal(saleNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
									itemSaveModel.setAmount(amountBd.stripTrailingZeros());//移库金额
									itemSaveModel.setRate(rate);//汇率
								}
							}
						}

						List<BuMoveInventoryItemModel> itemList = buMoveInventoryItemMap.get(zdyCode);
						if(itemList==null){
							itemList = new ArrayList<BuMoveInventoryItemModel>();
						}
						itemList.add(itemSaveModel);
						buMoveInventoryItemMap.put(zdyCode,itemList);
					} catch(Exception e){
						e.printStackTrace();
						failure+=1;
						continue;
					}
				}
			}
		}
		try {
			//遍历来源业务单号，保存数据
			if(failure == 0) {
				for (String zdyCode : buMoveInventoryMap.keySet()) {
					BuMoveInventoryModel buMoveInventoryModel = buMoveInventoryMap.get(zdyCode);
					Long moveId =buMoveInventoryDao.save(buMoveInventoryModel);//保存表头
					if(moveId != null) {
						List<BuMoveInventoryItemModel> list = buMoveInventoryItemMap.get(zdyCode);
						for (BuMoveInventoryItemModel buMoveInventoryItemModel : list) {
							buMoveInventoryItemModel.setMoveId(moveId);
							buMoveInventoryItemDao.save(buMoveInventoryItemModel);//保存表体
						}
						success += 1;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	/**
	 * 审核移库单
	 * @param list
	 * @param user
	 * @throws Exception
	 */
	@Override
	public void auditBuMoveInventory(List<Long> list , User user) throws Exception {

		List<InventoryDetailJson> sendMQJsonList=new ArrayList<>();
		for (Long id : list) {
			// 1、仅对单据来源为”手工导入“且移库状态为”待移库“的可操作审核；
			BuMoveInventoryModel buMoveInventoryModel = buMoveInventoryDao.searchById(id);
			if(buMoveInventoryModel==null){
				throw new RuntimeException("审核失败，移库单不存在");
			}
			if(!DERP_ORDER.BUMOVEINVENTORY_ORDERSOURCE_1.equals(buMoveInventoryModel.getOrderSource()) ||
				!DERP_ORDER.BUMOVEINVENTORY_STATUS_017.equals(buMoveInventoryModel.getStatus())){
				throw new RuntimeException("仅对单据来源为”手工导入“且移库状态为”待移库“的可操作审核");
			}
			//获取仓库信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", buMoveInventoryModel.getDepotId());// 移库单的仓库
			DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(params);

			String unit=null;
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
				String tallyingUnit = buMoveInventoryModel.getTallyingUnit();
				if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
					unit=DERP.INVENTORY_UNIT_0;
				}else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
					unit=DERP.INVENTORY_UNIT_1;
				}else if (DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
					unit=DERP.INVENTORY_UNIT_2;
				}
			}


			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(buMoveInventoryModel.getMerchantId());
			closeAccountsMongo.setDepotId(buMoveInventoryModel.getDepotId());
			closeAccountsMongo.setBuId(buMoveInventoryModel.getOutBuId());//移出事业部
			String outMaxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				outMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				outMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				outMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String outMaxCloseAccountsMonth="";
			if (org.apache.commons.lang.StringUtils.isNotBlank(outMaxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(outMaxdate+"-01 00:00:00"));
				outMaxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (StringUtils.isNotBlank(outMaxCloseAccountsMonth)) {
				// 校验移库日期归属月份必须大于关账月份/月结月份
				if (buMoveInventoryModel.getMoveDate().getTime()<Timestamp.valueOf(outMaxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("移出事业部："+buMoveInventoryModel.getOutBuName()+",移库日期必须大于关账日期/月结日期");
				}
			}
			closeAccountsMongo.setMerchantId(buMoveInventoryModel.getMerchantId());
			closeAccountsMongo.setDepotId(buMoveInventoryModel.getDepotId());
			closeAccountsMongo.setBuId(buMoveInventoryModel.getInBuId());//移入事业部
			String inMaxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				inMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				inMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				inMaxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String inMaxCloseAccountsMonth="";
			if (org.apache.commons.lang.StringUtils.isNotBlank(inMaxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(inMaxdate+"-01 00:00:00"));
				inMaxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (StringUtils.isNotBlank(inMaxCloseAccountsMonth)) {
				// 校验移库日期归属月份必须大于关账月份/月结月份
				if (buMoveInventoryModel.getMoveDate().getTime()<Timestamp.valueOf(inMaxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("移入事业部："+buMoveInventoryModel.getInBuName()+",移库日期必须大于关账日期/月结日期");
				}
			}


			BuMoveInventoryItemModel buItemModel = new BuMoveInventoryItemModel();
			buItemModel.setMoveId(buMoveInventoryModel.getId());
			List<BuMoveInventoryItemModel> buMoveInventoryItemList = buMoveInventoryItemDao.list(buItemModel);// 移库单下的商品
			// 移入商品收发明细
			List<InventoryGoodsDetailJson> inInventoryGoodsDetailList = new ArrayList<>();

			// 封装表头
			InventoryDetailJson inInventoryDetailJson=getInventoryDetailJson(buMoveInventoryModel,depotInfoMongo,user);

			//再次校验导入的单据号是存在系统单据信息
			OrderModel orderModel = new OrderModel();
			SaleOrderModel saleOrderModel = new SaleOrderModel();
			if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD.equals(buMoveInventoryModel.getOrderType())){
				orderModel.setMerchantId(user.getMerchantId());
				orderModel.setCode(buMoveInventoryModel.getBusinessNo());
				orderModel = orderDao.searchByModel(orderModel);
				if(orderModel==null){
					throw new RuntimeException("来源业务单号:"+buMoveInventoryModel.getBusinessNo()+"不存在系统单据信息");
				}

				inInventoryDetailJson.setBusinessNo(orderModel.getCode());// 业务单据号(存销售单号/电商订单号)
				inInventoryDetailJson.setShopCode(orderModel.getShopCode());
				inInventoryDetailJson.setStorePlatformName(orderModel.getStorePlatformName());

				//查询运单表
				WayBillModel wayBillModel = new WayBillModel();
				wayBillModel.setOrderId(orderModel.getId());
				wayBillModel = wayBillDao.searchByModel(wayBillModel);
				if (wayBillModel==null){
					throw new RuntimeException("审核失败，业务单号:"+orderModel.getCode()+"未找到运单表数据");
				};
				inInventoryGoodsDetailList=getInInventoryGoodsDetailList(buMoveInventoryModel, buMoveInventoryItemList, unit,wayBillModel);

			}else if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_XSDD.equals(buMoveInventoryModel.getOrderType())) { // 销售订单
				saleOrderModel.setMerchantId(user.getMerchantId());
				saleOrderModel.setCode(buMoveInventoryModel.getBusinessNo());
				saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
				if(saleOrderModel==null){
					throw new RuntimeException("来源业务单号:"+buMoveInventoryModel.getBusinessNo()+"不存在系统单据信息");
				}
				inInventoryDetailJson.setBusinessNo(saleOrderModel.getCode());// 业务单据号（存销售单号/电商订单号）
				inInventoryGoodsDetailList=getInInventoryGoodsDetailList(buMoveInventoryModel,buMoveInventoryItemList,unit,null);

			}else if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_KCYKD.equals(buMoveInventoryModel.getOrderType())) { // 销售订单


				inInventoryDetailJson.setBusinessNo(buMoveInventoryModel.getCode());// 业务单据号（存销售单号/电商订单号）
				inInventoryGoodsDetailList=getInInventoryGoodsDetailList(buMoveInventoryModel,buMoveInventoryItemList,unit,null);
			}
			// 放移入、移出商品
			inInventoryDetailJson.setGoodsList(inInventoryGoodsDetailList);
			sendMQJsonList.add(inInventoryDetailJson);

			BuMoveInventoryModel updateModel = new BuMoveInventoryModel();
			updateModel.setId(id);
			updateModel.setStatus(DERP_ORDER.BUMOVEINVENTORY_STATUS_027);// 027-移库中
			updateModel.setAuditName(user.getName());
			updateModel.setAuditor(user.getId());
			updateModel.setAuditDate(TimeUtils.getNow());
			updateModel.setModifyDate(TimeUtils.getNow());
			buMoveInventoryDao.modify(updateModel);


			//审核日志

			OperateLogModel saveLogModel = new OperateLogModel() ;
			saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_18);
			saveLogModel.setOperateDate(TimeUtils.getNow());
			saveLogModel.setOperater(user.getName());
			saveLogModel.setOperateId(user.getId());
			saveLogModel.setOperateRemark("审核");
			saveLogModel.setRelCode(buMoveInventoryModel.getCode());// 没有单号  存id
			saveLogModel.setCreateDate(TimeUtils.getNow());
			operateLogDao.save(saveLogModel) ;

		}
		// 批量重推
		for (InventoryDetailJson inventoryDetailJson : sendMQJsonList) {
			// 移库单审核生成商品收发明细
			rocketMQProducer.send(JSONObject.fromObject(inventoryDetailJson).toString(), MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTopic(),MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTags());
		}



	}
	/**
	 * 封装表头数据
	 * @param buMoveInventoryModel
	 * @return
	 */
	private InventoryDetailJson getInventoryDetailJson(BuMoveInventoryModel buMoveInventoryModel,DepotInfoMongo depotInfoMongo,User user) {
		// 移出商品收发明细
		InventoryDetailJson inInventoryDetailJson = new InventoryDetailJson();
		inInventoryDetailJson.setMerchantId(String.valueOf(buMoveInventoryModel.getMerchantId()));
		inInventoryDetailJson.setMerchantName(buMoveInventoryModel.getMerchantName());
		inInventoryDetailJson.setTopidealCode(user.getTopidealCode());// 卓志编码
		inInventoryDetailJson.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));
		inInventoryDetailJson.setDepotCode(depotInfoMongo.getDepotCode());
		inInventoryDetailJson.setDepotName(depotInfoMongo.getName());
		inInventoryDetailJson.setDepotType(depotInfoMongo.getType());
		inInventoryDetailJson.setOrderNo(buMoveInventoryModel.getCode());// 来源单据号(存移库单号)
		inInventoryDetailJson.setOwnMonth(TimeUtils.formatMonth(buMoveInventoryModel.getMoveDate()));// 归属月份
		inInventoryDetailJson.setDivergenceDate(TimeUtils.formatFullTime(buMoveInventoryModel.getMoveDate()));
		inInventoryDetailJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0019);
		inInventoryDetailJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0031);//移库单
		inInventoryDetailJson.setSourceDate(TimeUtils.formatFullTime());

		//回调设置
		Map<String, Object> customParam=new HashMap<String, Object>();
		inInventoryDetailJson.setBackTags(MQPushBackOrderEnum.MOVEORDER_INVENTORY_DETAIL_PUSH_BACK.getTags());//回调标签
		inInventoryDetailJson.setBackTopic(MQPushBackOrderEnum.MOVEORDER_INVENTORY_DETAIL_PUSH_BACK.getTopic());//回调主题
		customParam.put("code", buMoveInventoryModel.getCode());// 移库单单号
		inInventoryDetailJson.setCustomParam(customParam);////自定义回调参数
		return inInventoryDetailJson;
	}

	/**
	 * 封装推送库存表体
	 * @param buMoveInventoryItemList1
	 * @param unit
	 * @return
	 * @throws SQLException
	 */
	private List<InventoryGoodsDetailJson> getInInventoryGoodsDetailList(BuMoveInventoryModel buMoveInventoryModel,
			List<BuMoveInventoryItemModel> buMoveInventoryItemList, String unit,WayBillModel wayBillModel) throws SQLException {
		List<InventoryGoodsDetailJson> inInventoryGoodsDetailList=new ArrayList<>();
		// 遍历移库单下的商品，生成商品收发明细
		for (int i = 0; i < buMoveInventoryItemList.size(); i++) {
			BuMoveInventoryItemModel buMoveInventoryItemModel = buMoveInventoryItemList.get(i);
			// 移入商品实体
			InventoryGoodsDetailJson inInventoryGoodsDetailJson = new InventoryGoodsDetailJson();
			// 移出商品实体
			InventoryGoodsDetailJson outInventoryGoodsDetailJson = new InventoryGoodsDetailJson();
			if(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD.equals(buMoveInventoryModel.getOrderType())){
				// 查询运单表体
				WayBillItemModel wayItemModel = new WayBillItemModel();
				wayItemModel.setBillId(wayBillModel.getId());
				wayItemModel.setGoodsId(buMoveInventoryItemModel.getGoodsId());
				List<WayBillItemModel> wayBillItemList = wayBillItemDao.list(wayItemModel);
				if (wayBillItemList==null||wayBillItemList.size()==0) {
					throw new RuntimeException("审核失败，移库单号:"+buMoveInventoryModel.getCode()+"商品:"+buMoveInventoryItemList.get(i).getGoodsNo()+"运单表体为空");
				}

				// 移库单下商品去运单表查批次
				WayBillItemModel wayBillItemModel = wayBillItemList.get(0);
				inInventoryGoodsDetailJson.setBatchNo(wayBillItemModel.getBatchNo());//批次号
				outInventoryGoodsDetailJson.setBatchNo(wayBillItemModel.getBatchNo());//批次号
				if (wayBillItemModel.getProductionDate()!=null) {
					inInventoryGoodsDetailJson.setProductionDate(TimeUtils.formatFullTime(wayBillItemModel.getProductionDate()));
					outInventoryGoodsDetailJson.setProductionDate(TimeUtils.formatFullTime(wayBillItemModel.getProductionDate()));

				}
				if (wayBillItemModel.getOverdueDate()!=null) {
					inInventoryGoodsDetailJson.setOverdueDate(TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate()));
					outInventoryGoodsDetailJson.setOverdueDate(TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate()));
				}
				String expDate = TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate());
				if (org.apache.commons.lang.StringUtils.isNotBlank(expDate)) {
					Timestamp exTtime = TimeUtils.parseFullTime(expDate);
					String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
					inInventoryGoodsDetailJson.setIsExpire(isExpire);//是否过期（0是 1否	)
					outInventoryGoodsDetailJson.setIsExpire(isExpire);//是否过期（0是 1否	)
				}else {
					inInventoryGoodsDetailJson.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
				}

			}
			inInventoryGoodsDetailJson.setStockLocationTypeId(buMoveInventoryItemModel.getInStockLocationTypeId() == null ? "": buMoveInventoryItemModel.getInStockLocationTypeId().toString());
			inInventoryGoodsDetailJson.setStockLocationTypeName(buMoveInventoryItemModel.getInStockLocationTypeName());


			inInventoryGoodsDetailJson.setType(buMoveInventoryItemModel.getType());
			inInventoryGoodsDetailJson.setUnit(unit);
			outInventoryGoodsDetailJson.setType(buMoveInventoryItemModel.getType());
			outInventoryGoodsDetailJson.setUnit(unit);

			inInventoryGoodsDetailJson.setGoodsId(String.valueOf(buMoveInventoryItemModel.getGoodsId()));
			inInventoryGoodsDetailJson.setGoodsNo(buMoveInventoryItemModel.getGoodsNo());
			inInventoryGoodsDetailJson.setGoodsName(buMoveInventoryItemModel.getGoodsName());
			Map<String, Object> merchandiseParam = new HashMap<>();
			merchandiseParam.put("merchandiseId", buMoveInventoryItemModel.getGoodsId());
			MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseParam);
			inInventoryGoodsDetailJson.setBarcode(merchandiseInfo.getBarcode());
			inInventoryGoodsDetailJson.setCommbarcode(merchandiseInfo.getCommbarcode());

			outInventoryGoodsDetailJson.setGoodsId(String.valueOf(buMoveInventoryItemModel.getGoodsId()));
			outInventoryGoodsDetailJson.setGoodsNo(buMoveInventoryItemModel.getGoodsNo());
			outInventoryGoodsDetailJson.setGoodsName(buMoveInventoryItemModel.getGoodsName());
			outInventoryGoodsDetailJson.setBarcode(merchandiseInfo.getBarcode());
			outInventoryGoodsDetailJson.setCommbarcode(merchandiseInfo.getCommbarcode());

			inInventoryGoodsDetailJson.setBuId(String.valueOf(buMoveInventoryModel.getInBuId())); //移入事业部ID
			inInventoryGoodsDetailJson.setBuName(buMoveInventoryModel.getInBuName());//移入事业部
			inInventoryGoodsDetailJson.setNum(buMoveInventoryItemModel.getNum());
			inInventoryGoodsDetailJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//库存加减操作类型 1-调增
			inInventoryGoodsDetailList.add(inInventoryGoodsDetailJson);

			outInventoryGoodsDetailJson.setBuId(String.valueOf(buMoveInventoryModel.getOutBuId())); //移出事业部ID
			outInventoryGoodsDetailJson.setBuName(buMoveInventoryModel.getOutBuName());//移出事业部
			outInventoryGoodsDetailJson.setNum(buMoveInventoryItemModel.getNum());
			outInventoryGoodsDetailJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//库存加减操作类型  0-调减
			outInventoryGoodsDetailJson.setStockLocationTypeId(buMoveInventoryItemModel.getOutStockLocationTypeId() == null ? "": buMoveInventoryItemModel.getOutStockLocationTypeId().toString());
			outInventoryGoodsDetailJson.setStockLocationTypeName(buMoveInventoryItemModel.getOutStockLocationTypeName());
			inInventoryGoodsDetailList.add(outInventoryGoodsDetailJson);
		}
		return inInventoryGoodsDetailList;
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
	/**
	 * 判断是否yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public boolean isDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
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
