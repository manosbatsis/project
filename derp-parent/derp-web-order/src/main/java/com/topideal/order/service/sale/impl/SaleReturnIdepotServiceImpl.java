package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdSaleConfigDao;
import com.topideal.dao.common.SdSaleConfigItemDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnIdepotItemDTO;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.SaleReturnIdepotService;
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
import java.util.stream.Collectors;

/**
 * 销售退货出库单service实现类
 */
@Service
public class SaleReturnIdepotServiceImpl implements SaleReturnIdepotService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory
			.getLogger(SaleReturnIdepotServiceImpl.class);
	// 销售退货出库单表头
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;
	// 销售退货出库单表体
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;
//	//调拨指令
//	@Autowired
//	TransferInstructionDao transferInstructionDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	// 销售退货表头
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnItemDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private SdSaleConfigDao sdSaleConfigDao;
	@Autowired
	private SdSaleConfigItemDao sdSaleConfigItemDao;
	@Autowired
	private SaleSdOrderDao saleSdOrderDao;
	@Autowired
	private SaleSdOrderItemDao saleSdOrderItemDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleReturnIdepotDTO listSaleReturnIdepotByPage(SaleReturnIdepotDTO dto, User user)
			throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return saleReturnIdepotDao.searchDTOByPage(dto);
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public SaleReturnIdepotDTO searchDetail(Long id) throws SQLException {
		SaleReturnIdepotDTO dto = new SaleReturnIdepotDTO();
		dto.setId(id);
		return saleReturnIdepotDao.searchDTOById(id);
	}

	/**
	 * 根据表头ID获取表体(包括商品信息)
	 * @param id
	 * @return
	 */
	@Override
	public List<SaleReturnIdepotItemDTO> listItemByOrderId(Long id) throws SQLException {
		SaleReturnIdepotItemDTO dto = new SaleReturnIdepotItemDTO();
		dto.setSreturnIdepotId(id);
		return saleReturnIdepotItemDao.listDTO(dto);
	}
	/**
	 * 根据条件获取销售退货入库
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SaleReturnIdepotDTO> listSaleReturnIdepot(SaleReturnIdepotDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<SaleReturnIdepotDTO>();
			}
			dto.setBuList(buIds);
		}
		return saleReturnIdepotDao.listSaleReturnIdepotDTO(dto);
	}

	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> importSaleReturnIdepot(User user, List<List<Map<String, String>>> data) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		Map<String , Integer> checkGoodsNum = new HashMap<String, Integer>();//记录销售退编号+商品货号维度
		Map<String , SaleReturnIdepotModel> returnIdepotMap = new HashMap<String, SaleReturnIdepotModel>();
		Map<String , List<SaleReturnIdepotItemModel>> returnIdepotItemMap = new HashMap<String, List<SaleReturnIdepotItemModel>>();
		int success = 0;
		int failure = 0;

		DepotInfoMongo depot = null;
		List<Map<String, String>> objects = data.get(0);
		for (int j = 0; j < objects.size(); j++) {
			boolean isFlag = true;
			Map<String, String> msg = new HashMap<String, String>();
			Map<String, String> map = objects.get(j);
			String saleReturnCode = map.get("销售退订单编号");
			if(checkIsNullOrNot(j, saleReturnCode, "销售退订单编号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			SaleReturnOrderModel returnOrderModel = new SaleReturnOrderModel();
			returnOrderModel.setCode(saleReturnCode);
			returnOrderModel =	saleReturnOrderDao.searchByModel(returnOrderModel);
			if(returnOrderModel == null) {
				setErrorMsg(j, "销售退订单编号不存在", resultList);
				failure += 1;
				continue ;
			}
			if(DERP_ORDER.SALERETURNORDER_STATUS_006.equals(returnOrderModel.getStatus())) {
				setErrorMsg(j, "销售退订单编号不存在", resultList);
				failure += 1;
				continue ;
			}
			if(DERP_ORDER.SALERETURNORDER_STATUS_001.equals(returnOrderModel.getStatus()) ) {
				setErrorMsg(j, "销售退订单:"+returnOrderModel.getCode()+"为“待审核”，无法导入入库单", resultList);
				failure += 1;
				continue ;
			}
			if(!returnOrderModel.getReturnType().equals(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3)) {
				setErrorMsg(j, "只允许导入销售退类型为购销退货的入库单", resultList);
				failure += 1;
				continue ;
			}
			if("1".equals(returnOrderModel.getIsGenerateDeclare()) ) {
				setErrorMsg(j, "销售退订单:"+returnOrderModel.getCode()+"已生成销售退预申报单，无法导入入库单", resultList);
				failure += 1;
				continue ;
			}
			Map<String, Object> depotInfoParams = new HashMap<String, Object>();
			depotInfoParams.put("code", returnOrderModel.getInDepotCode());
			depot = depotInfoMongoDao.findOne(depotInfoParams);
			if (depot == null) {
				setErrorMsg(j, "仓库不存在", resultList);
				failure += 1;
				continue;
			}

			depotInfoParams.clear();
			depotInfoParams.put("depotId", depot.getDepotId());
			depotInfoParams.put("merchantId", user.getMerchantId());
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotInfoParams);
			if (depotMerchantRel == null) {
				setErrorMsg(j, "公司仓库关联不存在", resultList);
				failure += 1;
				continue;
			}
			String poNo = map.get("PO号");
			if(checkIsNullOrNot(j, poNo, "PO号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			poNo = poNo.trim();

			String returnInDate = map.get("入库日期");

			String inGoodsNo = map.get("退入商品货号");
			if(checkIsNullOrNot(j, inGoodsNo, "退入商品货号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			MerchandiseInfoMogo merchandise =null;
			if(StringUtils.isNotBlank(inGoodsNo)){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("goodsNo", inGoodsNo) ;
				params.put("merchantId", user.getMerchantId());
				merchandise = merchandiseInfoMogoDao.findOne(params);
				if(merchandise == null) {
					setErrorMsg(j, "商品货号："+inGoodsNo+"商品信息不存在", resultList);
					failure += 1;
					continue ;
				}
			}
			SaleReturnOrderItemModel itemModel = new SaleReturnOrderItemModel();
			itemModel.setOrderId(returnOrderModel.getId());
			itemModel.setInGoodsNo(inGoodsNo);
			itemModel.setPoNo(poNo);
			itemModel = saleReturnItemDao.searchByModel(itemModel);
			if(itemModel == null) {
				setErrorMsg(j, "商品货号："+inGoodsNo+"，PO号："+poNo+" 在销售退订单编号:"+saleReturnCode+" 中不存在", resultList);
				failure += 1;
				continue ;
			}
			SaleReturnIdepotModel inModel = new SaleReturnIdepotModel();
			inModel.setOrderCode(saleReturnCode);
			inModel = saleReturnIdepotDao.searchByModel(inModel);
			if(inModel != null) {
				setErrorMsg(j, "销售退订单编号:"+saleReturnCode+" 已存在销售退入库单", resultList);
				failure += 1;
				continue ;
			}

			String returnNum = map.get("退入好品数量");
			String wornNum = map.get("退入坏品数量");
			if(StringUtils.isBlank(returnNum) && StringUtils.isBlank(wornNum)){
				setErrorMsg(j, "退入好品数量或退入坏品数量必须有一个不为空", resultList);
				failure += 1;
				continue ;
			}else if("0".equals(returnNum) && "0".equals(wornNum)){
				setErrorMsg(j, "退入好品数量或退出坏品数量必须有一个不为0", resultList);
				failure += 1;
				continue ;
			}

			Integer goodNum = 0;
			Integer badNum = 0;
			if(itemModel != null) {
				goodNum = itemModel.getReturnNum() == null ?0:itemModel.getReturnNum();
				badNum = itemModel.getBadGoodsNum() == null ?0:itemModel.getBadGoodsNum();

			}
			Integer returnDepotNum = goodNum + badNum;// 计划退入商品数量
			Integer numInt = StringUtils.isBlank(returnNum)?0:Integer.valueOf(returnNum);
			Integer wornNumInt = StringUtils.isBlank(wornNum)?0:Integer.valueOf(wornNum);
			Integer totalGoodsNum = numInt+wornNumInt;	// 导入进来的好品+坏品数量

			Integer inDepotNum = 0;//已导入的退入商品量
			if(checkGoodsNum.containsKey(saleReturnCode+inGoodsNo+poNo)) {
				inDepotNum = checkGoodsNum.get(saleReturnCode+inGoodsNo+poNo);
			}else {
				checkGoodsNum.put(saleReturnCode+inGoodsNo+poNo, totalGoodsNum);
			}

			Integer totalNum = returnDepotNum - inDepotNum;	// 减去后的数量
			if(totalGoodsNum > totalNum){
				setErrorMsg(j, "销售退订单编号:"+saleReturnCode+"退入商品货号:"+inGoodsNo+"，PO号："+poNo+" 剩余入库量为:"+totalNum, resultList);
				failure += 1;
				continue ;
			}

			String returnBatchNo = map.get("批次号");
			String returnProductionDate = map.get("生产日期");
			String returnOverdueDate = map.get("失效日期");
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())) {
				if(checkIsNullOrNot(j, returnBatchNo, "批次效期强校验的仓库，批次号不能为空", resultList)) {
					failure += 1;
					continue;
				}
				returnBatchNo = returnBatchNo.trim() ;

				if(checkIsNullOrNot(j, returnProductionDate, "批次效期强校验的仓库，生产日期不能为空", resultList)) {
					failure += 1;
					continue;
				}
				returnProductionDate = returnProductionDate.trim() ;
				if(!TimeUtils.isDate(returnProductionDate) && !isDate(returnProductionDate)) {
					setErrorMsg(j, "生产日期格式有误", resultList);
					failure += 1;
					continue;
				}

				if(checkIsNullOrNot(j, returnOverdueDate, "批次效期强校验的仓库，失效日期不能为空", resultList)) {
					failure += 1;
					continue;
				}
				returnOverdueDate = returnOverdueDate.trim() ;
				if(!TimeUtils.isDate(returnProductionDate) && !isDate(returnOverdueDate)) {
					setErrorMsg(j, "失效日期格式有误", resultList);
					failure += 1;
					continue;
				}
			}
			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(depot.getDepotId());
			closeAccountsMongo.setBuId(returnOrderModel.getBuId());
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
			String returnInDateTime = returnInDate;
			if(returnInDate.length() <= 10) {
				returnInDateTime =returnInDateTime + " 00:00:00";
			}
			// 关账日期和发货日期比较
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 出库日期
				if (Timestamp.valueOf(returnInDateTime).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j,"入库日期必须大于关账日期/月结日期", resultList);
					failure += 1;
					continue;
				}
			}

			SaleReturnIdepotModel sReturnIdepotModel = new SaleReturnIdepotModel();
			sReturnIdepotModel.setMerchantId(user.getMerchantId());//商家ID
			sReturnIdepotModel.setMerchantName(user.getMerchantName());//商家名称
			sReturnIdepotModel.setContractNo(returnOrderModel.getContractNo());//合同号
			sReturnIdepotModel.setInDepotId(returnOrderModel.getInDepotId());//退入仓库id
			sReturnIdepotModel.setInDepotName(returnOrderModel.getInDepotName());//退入仓库名称
			sReturnIdepotModel.setOutDepotId(returnOrderModel.getOutDepotId());//退出仓库id
			sReturnIdepotModel.setOutDepotName(returnOrderModel.getOutDepotName());//退出仓库名称
			sReturnIdepotModel.setReturnInDate(TimeUtils.parseDay(returnInDate));//退货入库时间
			sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_027);//"011","待入仓",012","已入仓""027","出库中"
			sReturnIdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTR));//销售退货出库单号
			sReturnIdepotModel.setOrderCode(returnOrderModel.getCode());//销售退货编号
			sReturnIdepotModel.setMerchantReturnNo(returnOrderModel.getMerchantReturnNo());//企业退运单号
			sReturnIdepotModel.setInspectNo(returnOrderModel.getInspectNo());//申报地国检编码
			sReturnIdepotModel.setCustomsNo(returnOrderModel.getCustomsNo());//申报地海关编码
			sReturnIdepotModel.setCustomerId(returnOrderModel.getCustomerId());//客户id
			sReturnIdepotModel.setCustomerName(returnOrderModel.getCustomerName());//客户名称
			sReturnIdepotModel.setRemark(returnOrderModel.getRemark());//备注
			sReturnIdepotModel.setLbxNo(returnOrderModel.getLbxNo());
			sReturnIdepotModel.setInExternalCode(returnOrderModel.getCode());// 销售退出外部单号
			sReturnIdepotModel.setBuId(returnOrderModel.getBuId());// 事业部
			sReturnIdepotModel.setBuName(returnOrderModel.getBuName());
			sReturnIdepotModel.setDataSource("1");//手工导入
			sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_011);//待入仓
			sReturnIdepotModel.setOrderId(returnOrderModel.getId());

			SaleReturnIdepotModel existModel = returnIdepotMap.get(saleReturnCode);
			if(existModel != null) {
				if(existModel.getReturnInDate().getTime() != sReturnIdepotModel.getReturnInDate().getTime()) {
					setErrorMsg(j,"相同销售退订单号"+saleReturnCode+"的入库日期要一致", resultList);
					failure += 1;
					continue;
				}
			}

			SaleReturnIdepotItemModel saleReturnIdepotItemModel = new SaleReturnIdepotItemModel();
			saleReturnIdepotItemModel.setInGoodsId(merchandise.getMerchandiseId());
			saleReturnIdepotItemModel.setInGoodsName(merchandise.getName());
			saleReturnIdepotItemModel.setInGoodsCode(merchandise.getGoodsCode());
			saleReturnIdepotItemModel.setInGoodsNo(inGoodsNo);
			saleReturnIdepotItemModel.setReturnNum(numInt);	// 正常品数量
			saleReturnIdepotItemModel.setReturnBatchNo(returnBatchNo);//批次号
			saleReturnIdepotItemModel.setWornNum(wornNumInt);	// 坏品数量
			saleReturnIdepotItemModel.setCreateDate(TimeUtils.getNow());
			saleReturnIdepotItemModel.setCreater(user.getId());
			saleReturnIdepotItemModel.setBarcode(merchandise.getBarcode());
			saleReturnIdepotItemModel.setExpireNum(0);	//过期数量
			saleReturnIdepotItemModel.setCommbarcode(merchandise.getCommbarcode());
			saleReturnIdepotItemModel.setPoNo(poNo);
			if(StringUtils.isNotBlank(returnProductionDate)) {
				saleReturnIdepotItemModel.setProductionDate(TimeUtils.strToSqlDate(returnProductionDate));	//生产日期
			}
			if(StringUtils.isNotBlank(returnOverdueDate)) {
				saleReturnIdepotItemModel.setOverdueDate(TimeUtils.strToSqlDate(returnOverdueDate));//失效日期
			}

			List<SaleReturnIdepotItemModel> itemList = returnIdepotItemMap.get(saleReturnCode);
			if(itemList == null) {
				itemList = new ArrayList<SaleReturnIdepotItemModel>() ;
			}
			itemList.add(saleReturnIdepotItemModel) ;
			returnIdepotItemMap.put(saleReturnCode, itemList) ;

			SaleReturnIdepotModel updatereturnIdepot= returnIdepotMap.get(saleReturnCode);
			Integer returnInNum =  0 ;
			if(updatereturnIdepot != null) {
				returnInNum = updatereturnIdepot.getReturnInNum();
			}
			returnInNum = returnInNum + totalGoodsNum;
			sReturnIdepotModel.setReturnInNum(returnInNum);
			returnIdepotMap.put(saleReturnCode, sReturnIdepotModel);

		}

		if(failure == 0) {
			for (String saleReturnCode : returnIdepotMap.keySet()) {
				SaleReturnIdepotModel model = returnIdepotMap.get(saleReturnCode);
				Long id =saleReturnIdepotDao.save(model);
				if(id != null) {
					 //合并相同调入商品货号+批次+效期
	                Map<String, SaleReturnIdepotItemModel> map = new HashMap<>();
	                List<SaleReturnIdepotItemModel> saleReturnIdepotItemModels = returnIdepotItemMap.get(saleReturnCode);

	                for (SaleReturnIdepotItemModel itemModel : saleReturnIdepotItemModels) {
	                    String key = itemModel.getPoNo() + "_" + itemModel.getInGoodsNo() + "_" + itemModel.getReturnBatchNo() + "_" +
	                            itemModel.getProductionDate() + "_" + itemModel.getOverdueDate();

	                    SaleReturnIdepotItemModel existItemModel = map.get(key);
	                    if (existItemModel != null) {
	                        Integer goodsNum = itemModel.getReturnNum() == null ? 0 : itemModel.getReturnNum();
	                        Integer wornNum = itemModel.getWornNum() == null ? 0 : itemModel.getWornNum();
	                        Integer existGoodsNum = existItemModel.getReturnNum() == null ? 0 : existItemModel.getReturnNum();
	                        Integer existWornNum = existItemModel.getWornNum() == null ? 0 : existItemModel.getWornNum();
	                        itemModel.setReturnNum(goodsNum + existGoodsNum);
	                        itemModel.setWornNum(wornNum + existWornNum);
	                    }
	                    map.put(key, itemModel);
	                }

	                for (String key : map.keySet()) {
	                	SaleReturnIdepotItemModel itemModel = map.get(key);
	                	itemModel.setSreturnIdepotId(id);
	                	saleReturnIdepotItemDao.save(itemModel);
					}
					success += 1;
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	@Override
	public Map<String, Object> auditSaleReturnIdepot(List list, User user) throws Exception {
		int num = 0;
		//失败原因
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		Map<String, Integer> auditedNumMap = new HashMap<>();
		for(int j=0;j< list.size();j++){
			boolean flag = true;
			SaleReturnIdepotModel saleReturnIdepotModel = saleReturnIdepotDao.searchById(Long.parseLong(list.get(j).toString()));
			// 1.单据状态必须为手工导入且待入仓
			if(!DERP_ORDER.SALERETURNIDEPOT_STATUS_011.equals(saleReturnIdepotModel.getStatus()) || !"1".equals(saleReturnIdepotModel.getDataSource())){
				setErrorMsg(j, "审核失败，必须是手工导入且状态为待入仓的单据\n",resultList);
				flag = false;
				continue;
			}
			SaleReturnOrderModel returnOrderModel = new SaleReturnOrderModel();
			returnOrderModel.setCode(saleReturnIdepotModel.getOrderCode());
			returnOrderModel =	saleReturnOrderDao.searchByModel(returnOrderModel);
			if(returnOrderModel == null) {
				setErrorMsg(j,"审核失败，销售退订单:"+saleReturnIdepotModel.getOrderCode()+"不存在\n",resultList);
				flag = false;
				continue;
			}
			if(!returnOrderModel.getReturnType().equals(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3)) {
				setErrorMsg(j,"审核失败，只允许销售退类型为购销退货的单据进行审核\n",resultList);
				flag = false;
				continue;
			}

			//mongo查询参数集合
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", saleReturnIdepotModel.getInDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params);
			if (depot == null) {
				setErrorMsg(j,"审核失败，仓库ID为:"+saleReturnIdepotModel.getInDepotId()+",未查到该入仓仓库信息\n",resultList);
				flag = false;
				continue;
			}
			// 仓库公司关联表
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", user.getMerchantId());
			depotMerchantRelParam.put("depotId", saleReturnIdepotModel.getInDepotId());
			DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if(depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)){
				setErrorMsg(j,"审核失败，仓库ID为:"+saleReturnIdepotModel.getInDepotId()+",未查到该公司下入仓仓库信息\n",resultList);
				flag = false;
				continue;
			}

			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleReturnIdepotModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleReturnIdepotModel.getInDepotId());
			closeAccountsMongo.setBuId(saleReturnIdepotModel.getBuId());
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
				if (saleReturnIdepotModel.getReturnInDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j,"审核失败，入库日期必须大于关账日期/月结日期库\n",resultList);
					flag = false;
					continue;
				}
			}

			String inventoryUnit = "";//理货单位
			if (DERP.ORDER_TALLYINGUNIT_00.equals(returnOrderModel.getTallyingUnit())) {
				inventoryUnit = DERP.INVENTORY_UNIT_0;
			} else if (DERP.ORDER_TALLYINGUNIT_01.equals(returnOrderModel.getTallyingUnit())) {
				inventoryUnit = DERP.INVENTORY_UNIT_1;
			} else if (DERP.ORDER_TALLYINGUNIT_02.equals(returnOrderModel.getTallyingUnit())) {
				inventoryUnit = DERP.INVENTORY_UNIT_2;
			}

			// 销售出库商品数量是否小于等于销售订单商品可核销量
			// 获取销售出库的商品信息
			SaleReturnIdepotItemModel itemOutModel = new SaleReturnIdepotItemModel();
			itemOutModel.setSreturnIdepotId(saleReturnIdepotModel.getId());
			List<SaleReturnIdepotItemModel> itemList = saleReturnIdepotItemDao.list(itemOutModel);

			for(SaleReturnIdepotItemModel item:itemList) {
				SaleReturnOrderItemModel itemModel = new SaleReturnOrderItemModel();
				itemModel.setOrderId(returnOrderModel.getId());
				itemModel.setInGoodsNo(item.getInGoodsNo());
				itemModel.setPoNo(item.getPoNo());
				itemModel = saleReturnItemDao.searchByModel(itemModel);
				if(itemModel == null) {
					setErrorMsg(j,"商品货号："+item.getInGoodsNo()+"在销售退订单编号:"+returnOrderModel.getCode()+" 中不存在",resultList);
					flag = false;
					continue;
				}
				Integer goodNum = itemModel.getReturnNum() == null ? 0:itemModel.getReturnNum();
				Integer badNum = itemModel.getBadGoodsNum() == null ? 0:itemModel.getBadGoodsNum();
				Integer totalNum = goodNum + badNum;//计划退出数量

				String key = saleReturnIdepotModel.getCode() + item.getInGoodsNo() + item.getPoNo();
				Integer auditedNum = item.getReturnNum()+item.getWornNum();	// 好品+坏品
				if (auditedNumMap.containsKey(key)) {
					auditedNum += auditedNumMap.get(key);
				}
				if (totalNum < auditedNum) {
					setErrorMsg(j,"审核失败，销售退入库单号：" + saleReturnIdepotModel.getCode() + ",商品货号:" + item.getInGoodsNo() +  "可核销量不足\n",resultList);
					flag = false;
					break;
				}
				auditedNumMap.put(key, auditedNum);
			}
			if (flag) {
				//修改出库单状态
				saleReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_028);
				saleReturnIdepotModel.setModifyDate(TimeUtils.getNow());
				saleReturnIdepotDao.modify(saleReturnIdepotModel);

				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();

				//扣减销售出库库存量
				invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				invetAddOrSubtractRootJson.setMerchantId(saleReturnIdepotModel.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(saleReturnIdepotModel.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode());
				invetAddOrSubtractRootJson.setDepotId(saleReturnIdepotModel.getInDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(saleReturnIdepotModel.getInDepotName());
				invetAddOrSubtractRootJson.setDepotCode(depot.getCode());
				invetAddOrSubtractRootJson.setDepotType(depot.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(depot.getIsTopBooks());
				invetAddOrSubtractRootJson.setOrderNo(saleReturnIdepotModel.getCode());	// 销售退出库单号
				invetAddOrSubtractRootJson.setBusinessNo(saleReturnIdepotModel.getOrderCode());	// 销售退订单号
				invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_005);
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_005);
				invetAddOrSubtractRootJson.setBuId(String.valueOf(saleReturnIdepotModel.getBuId()));	// 事业部
				invetAddOrSubtractRootJson.setBuName(saleReturnIdepotModel.getBuName());
				invetAddOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());
				invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(saleReturnIdepotModel.getReturnInDate(), "yyyy-MM-dd HH:mm:ss"));	// 出库日期
				// 获取当前年月
				invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(saleReturnIdepotModel.getReturnInDate()));
				for(SaleReturnIdepotItemModel item:itemList) {
					// 好品
					if (item.getReturnNum() !=null && item.getReturnNum() > 0) {
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getInGoodsId()));
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getInGoodsNo());
						invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getInGoodsName());


						invetAddOrSubtractGoodsListJson.setType("0");//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getReturnNum());
						invetAddOrSubtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getReturnBatchNo());
						if(item.getProductionDate() != null) {
							invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.format(item.getProductionDate(), "yyyy-MM-dd"));
						}
						if(item.getOverdueDate() != null) {
							invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.format(item.getOverdueDate(), "yyyy-MM-dd"));
						}
						invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）---默认不过期
						if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
							invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(returnOrderModel.getStockLocationTypeId() == null ? "" : returnOrderModel.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(returnOrderModel.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}

					// 坏品
					if (item.getWornNum() != null && item.getWornNum() > 0) {
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getInGoodsId()));
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getInGoodsNo());
						invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getInGoodsName());


						invetAddOrSubtractGoodsListJson.setType("1");//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getWornNum());	//坏品数量
						invetAddOrSubtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getReturnBatchNo());
						if(item.getProductionDate() != null) {
							invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.format(item.getProductionDate(), "yyyy-MM-dd"));
						}
						if(item.getOverdueDate() != null) {
							invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.format(item.getOverdueDate(), "yyyy-MM-dd"));
						}
						invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）---默认不过期
						if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
							// 托盘
							if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
							} else if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
							} else if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
							}
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(returnOrderModel.getStockLocationTypeId() == null ? "" : returnOrderModel.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(returnOrderModel.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}
				}
				//库存回推数据
				Map<String, Object> customParam2=new HashMap<String, Object>();
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALER_ETURN_IDEPOT_IN_PUSH_BACK.getTags());//回调标签
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALER_ETURN_IDEPOT_IN_PUSH_BACK.getTopic());//回调主题
				customParam2.put("code", saleReturnIdepotModel.getOrderCode());// 销售退货单号
				invetAddOrSubtractRootJson.setCustomParam(customParam2);//自定义回调参数
				try {
					invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
					// 减库存
					rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

				} catch (Exception e) {
					LOGGER.error("----------------------销售退入库单[" + saleReturnIdepotModel.getCode() + "]扣减库存失败----------------------");
				}

			}
			num++;
		}

        Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",num);
		map.put("failure",list.size()-num);
		map.put("failureMsg", resultList);
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
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
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

	@Override
	public void delSaleReturnIdepot(String ids) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long id : idList) {
			SaleReturnIdepotModel model = saleReturnIdepotDao.searchById(id);
			if(!("1".equals(model.getDataSource()) && DERP_ORDER.SALERETURNIDEPOT_STATUS_011.equals(model.getStatus()))) {
				throw new RuntimeException("只能删除手工导入且单据状态为“待入仓”的单据");
			}
			model.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_006);
			saleReturnIdepotDao.modify(model);
		}
	}
	/**
	 * 生成销售SD单
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	public void generateSaleSdOrder(String ids , User user) throws Exception {
		List<String> returnCodeList = new ArrayList<String>();
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long id : idList) {
			SaleReturnIdepotModel saleReturnIdepotModel =  saleReturnIdepotDao.searchById(id);
			if(saleReturnIdepotModel == null) {
				throw new RuntimeException("销售退入库单不存在");
			}

			SaleReturnOrderModel saleReturnOrderModel = saleReturnOrderDao.searchById(saleReturnIdepotModel.getOrderId());
			if(!DERP_ORDER.SALERETURNORDER_RETURNMODE_1.equals(saleReturnOrderModel.getReturnMode())) {
				throw new RuntimeException("销售退订单退货方式不为“退货退款”");
			}
			Map<String, List<SaleSdOrderItemModel>> resultMap = new HashMap<String, List<SaleSdOrderItemModel>>();
			Map<String, BigDecimal> amountMap = new HashMap<String, BigDecimal>();
			Map<String, Integer> numMap = new HashMap<String, Integer>();

			SaleReturnIdepotItemModel saleReturnIdepotItemModel = new SaleReturnIdepotItemModel();
			saleReturnIdepotItemModel.setSreturnIdepotId(saleReturnIdepotModel.getId());
			List<SaleReturnIdepotItemModel> queryIdepotItemList = saleReturnIdepotItemDao.list(saleReturnIdepotItemModel);
			Map<String ,List<SaleReturnIdepotItemModel>> idepotItemMap = queryIdepotItemList.stream().collect(Collectors.groupingBy(SaleReturnIdepotItemModel::getInGoodsNo));

			//查询销售退单是否存在销售SD单，若存在，先删除，再重新生成
			SaleSdOrderModel querySaleSdOrder = new SaleSdOrderModel();
			querySaleSdOrder.setOrderCode(saleReturnIdepotModel.getCode());
			List<SaleSdOrderModel> querySaleSdOrderList = saleSdOrderDao.list(querySaleSdOrder);
			if(querySaleSdOrderList != null && querySaleSdOrderList.size() > 0){
				for(SaleSdOrderModel delSdModel:querySaleSdOrderList){
					delSdModel.setState(DERP.DEL_CODE_006);
					saleSdOrderDao.modify(delSdModel);
				}
			}
			// 1、以“公司+事业部+客户”查询销售SD配置表 ,退货入库时间 在生效日期范围内，若无则结束；
			SdSaleConfigDTO config = new SdSaleConfigDTO();
			config.setMerchantId(saleReturnIdepotModel.getMerchantId());
			config.setBuId(saleReturnIdepotModel.getBuId());
			config.setCustomerId(saleReturnIdepotModel.getCustomerId());
			config.setOrderDate(saleReturnIdepotModel.getReturnInDate());
			config.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);
			List<SdSaleConfigDTO> configList = sdSaleConfigDao.listDTO(config);
			if (configList == null || configList.size() <= 0) {
				return;
			}
			// 审核时间为最新的一条配置信息
			configList = configList.stream().sorted(Comparator.comparing(SdSaleConfigDTO::getAuditDate).reversed()).collect(Collectors.toList());
			config = configList.get(0);

			SdSaleConfigItemModel configItemModel = new SdSaleConfigItemModel();
			configItemModel.setSaleConfigId(config.getId());
			List<SdSaleConfigItemModel> configItemList = sdSaleConfigItemDao.list(configItemModel);
			for(SdSaleConfigItemModel configItem : configItemList){
				for(String goodsNo : idepotItemMap.keySet()){
					List<SaleReturnIdepotItemModel> idepotItemList = idepotItemMap.get(goodsNo);
					SaleReturnIdepotItemModel idepotItem = idepotItemList.get(0);
					/**
					 * 2、若销售SD配置记录中存在单比例配置，则销售退单中所有商品均按照对应单比例配置的“SD类型+SD比例”进行生成的销售SD单，
					 * 各商品销售SD类型金额 = 销售退货数量*销售退单价*SD比例
					 *
					 * 3、若销售SD配置记录中存在多比例配置，根据销售退单中的商品标准条码查询是否存在对应多比例配置的“SD类型+SD比例”，若有则生成销售SD单，
					 * 商品销售SD类型金额 = 销售退货数量*销售退单价*SD比例；
					 */
					Integer returnNum = idepotItemList.stream().mapToInt(SaleReturnIdepotItemModel::getReturnNum).sum();
					Integer wornNum = idepotItemList.stream().mapToInt(SaleReturnIdepotItemModel::getWornNum).sum();
					Integer num = returnNum + wornNum;
					if(num.intValue() < 1){
	                    continue;
	                }
					if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(configItem.getSdType())  && !(StringUtils.isNotBlank(configItem.getCommbarcode())
							&& idepotItem.getCommbarcode().equals(configItem.getCommbarcode()))){
						continue;
					}
					SaleReturnOrderItemModel saleReturnOrderItem = new SaleReturnOrderItemModel();
					saleReturnOrderItem.setOrderId(saleReturnIdepotModel.getOrderId());
					saleReturnOrderItem.setInGoodsId(idepotItem.getInGoodsId());
					saleReturnOrderItem.setPoNo(idepotItem.getPoNo());
					saleReturnOrderItem = saleReturnOrderItemDao.searchByModel(saleReturnOrderItem);

					SaleSdOrderItemModel sdItemModel = new SaleSdOrderItemModel();
					sdItemModel.setGoodsId(idepotItem.getInGoodsId());
					sdItemModel.setGoodsNo(idepotItem.getInGoodsNo());
					sdItemModel.setGoodsName(idepotItem.getInGoodsName());
					sdItemModel.setBarcode(idepotItem.getBarcode());
					sdItemModel.setCommbarcode(idepotItem.getCommbarcode());
					sdItemModel.setNum(num);
					sdItemModel.setPrice(saleReturnOrderItem.getPrice());
					sdItemModel.setAmount(saleReturnOrderItem.getPrice().multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP));
					sdItemModel.setSdRatio(configItem.getProportion().doubleValue());

					// SD金额 = 销售退货数量*销售退单价*SD比例
					BigDecimal sdAmount = saleReturnOrderItem.getPrice().multiply(new BigDecimal(num))
							.multiply(configItem.getProportion()).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigDecimal sdPrice = sdAmount.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);
					sdItemModel.setSdPrice(sdPrice);
					sdItemModel.setSdAmount(sdAmount);

					String key = configItem.getSdTypeId()+ "_" + configItem.getSdTypeName() + "_" + configItem.getSdSimpleName();
					if (resultMap.containsKey(key)) {
						List<SaleSdOrderItemModel> itemList = resultMap.get(key);
						itemList.add(sdItemModel);
						resultMap.put(key, itemList);

						BigDecimal totalAmount = amountMap.get(key);
						totalAmount = totalAmount.add(sdAmount);
						amountMap.put(key, totalAmount);

						Integer totalNum = numMap.get(key);
						totalNum = totalNum + num ;
						numMap.put(key, totalNum);

					} else {
						List<SaleSdOrderItemModel> itemList = new ArrayList<SaleSdOrderItemModel>();
						itemList.add(sdItemModel);
						resultMap.put(key, itemList);
						amountMap.put(key, sdAmount);
						numMap.put(key, num);
					}
				}
			}

			for (String key : resultMap.keySet()) {
				String sdTypeId = key.split("_")[0];
				String sdType = key.split("_")[1];
				String sdTypeName = key.split("_")[2];

				SaleSdOrderModel sdOrderModel = new SaleSdOrderModel();
				sdOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSSD));
				sdOrderModel.setBuId(saleReturnIdepotModel.getBuId());
				sdOrderModel.setBuName(saleReturnIdepotModel.getBuName());
				sdOrderModel.setMerchantId(saleReturnIdepotModel.getMerchantId());
				sdOrderModel.setMerchantName(saleReturnIdepotModel.getMerchantName());
				sdOrderModel.setCustomerId(saleReturnIdepotModel.getCustomerId());
				sdOrderModel.setCustomerName(saleReturnIdepotModel.getCustomerName());
				sdOrderModel.setCurrency(saleReturnOrderModel.getCurrency());
				sdOrderModel.setOrderId(saleReturnIdepotModel.getId());
				sdOrderModel.setOrderCode(saleReturnIdepotModel.getCode());
				sdOrderModel.setOwnDate(saleReturnIdepotModel.getReturnInDate());
				sdOrderModel.setBusinessId(saleReturnOrderModel.getId());
				sdOrderModel.setBusinessCode(saleReturnOrderModel.getCode());
				sdOrderModel.setTotalSdAmount(amountMap.get(key));
				sdOrderModel.setTotalSdNum(numMap.get(key));
				sdOrderModel.setSdTypeId(Long.valueOf(sdTypeId));
				sdOrderModel.setSdType(sdType);
				sdOrderModel.setSdTypeName(sdTypeName);
				sdOrderModel.setPoNo(saleReturnOrderModel.getPoNo());
				sdOrderModel.setCreater(user.getId());
				sdOrderModel.setCreateName(user.getName());
				sdOrderModel.setCreateDate(TimeUtils.getNow());
				sdOrderModel.setState("001");
				sdOrderModel.setOrderSource(DERP_ORDER.SALE_SD_ORDER_ORDERSOURCE_1);
				sdOrderModel.setOrderType(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);//数据类型 1-上架单 2-销售退入库单

				Long num = saleSdOrderDao.save(sdOrderModel);

				List<SaleSdOrderItemModel> itemList = resultMap.get(key);
				for (SaleSdOrderItemModel itemModel : itemList) {
					itemModel.setSaleSdOrderId(num);
					itemModel.setCreateDate(TimeUtils.getNow());
					saleSdOrderItemDao.save(itemModel);
				}
			}

			//记录销售退订单
			if(!returnCodeList.contains(saleReturnIdepotModel.getOrderCode())) {
				returnCodeList.add(saleReturnIdepotModel.getOrderCode());
			}
		}
		if(returnCodeList.size() > 0) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("orderCodes", StringUtils.join(returnCodeList, ","));
			map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);
			rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
		}

	}
}
