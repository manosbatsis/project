package com.topideal.storage.service.takesstock.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.google.common.base.Joiner;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.*;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.dao.TakesStockResultsDao;
import com.topideal.entity.dto.ComfirmRequetVo;
import com.topideal.entity.dto.ImportErrorMessage;
import com.topideal.entity.dto.TakesStockResultItemDTO;
import com.topideal.entity.dto.TakesStockResultsDTO;
import com.topideal.entity.vo.TakesStockResultItemModel;
import com.topideal.entity.vo.TakesStockResultsModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.storage.service.takesstock.TakesStockResultService;
import com.topideal.storage.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.UsesSunHttpServer;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TakesStockResultServiceImpl implements TakesStockResultService{

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TakesStockResultServiceImpl.class);

	@Autowired
	public TakesStockResultsDao takesStockResultsDao;
	@Autowired
	public TakesStockResultItemDao takesStockResultItemDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

	/**
	 * 列表（分页）
	 *
	 * @param dto
	 * @return
	 */
	@Override
	public TakesStockResultsDTO listTakesStockResultPage(TakesStockResultsDTO dto) throws SQLException {
		return takesStockResultsDao.searchDTOByPage(dto);
	}

	/**
	 * 根据id查询盘点结果
	 * @return
	 */
	@Override
	public TakesStockResultsDTO queryById(Long Id) throws SQLException {
		return takesStockResultsDao.getDetail(Id);
	}

	/**
	 * 确认盘点结果
	 * */
    @SuppressWarnings("unused")
	@Override
	public String updateConfirmTakesStock(Long userId,String name,String topidealCode,String ids,String confirmType) throws Exception{
    	StringBuffer failCode = new StringBuffer();//盘点结果确认失败单号

    	String[] takesIdArr = ids.split(",");
		for(int i=0;i<takesIdArr.length;i++){
			Long Id = Long.valueOf(takesIdArr[i]);
			//查询盘点结果
			TakesStockResultsModel model = takesStockResultsDao.searchById(Id);
			if(model==null){
				continue;
			}
			//检查状态
			if(!model.getStatus().equals(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_024)){
				failCode.append(model.getCode()+":发送确认盘点结果单状态不是盘点结果待确认\n");
				continue;
			}
			//单据来源为手工导入时，单据确认后生成对应的库存调整收发明细、库存做相应的数量调整
			if (DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_4.equals(model.getSourceType())) {
				if (confirmType.equals(DERP_STORAGE.STORAGE_CONFIRMTYPE_20)) {	// 确认
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(model.getMerchantId());
					closeAccountsMongo.setDepotId(model.getDepotId());
					String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxDate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 归属日期
						if (model.getSourceTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							failCode.append(model.getCode()+":归属日期必须大于关账日期/月结日期\n");
							continue;
						}
					}
					String message = this.confirmToAddInventory(model, topidealCode);
					if (StringUtils.isNotBlank(message)) {
						failCode.append(model.getCode()+":" + message + "\n");
						continue;
					}
					model.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022);// 022-入库中
				}else {	// 驳回
					model.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_021);// 021-已作废
				}
				model.setConfirmTime(TimeUtils.getNow());
				model.setConfirmUserId(userId);
				model.setConfirmUsername(name);
				takesStockResultsDao.modify(model);
			}else{	// 接口回传的时候
				ComfirmRequetVo request = new ComfirmRequetVo();
				request.setOrder_id(model.getTakesStockCode());
				request.setDeal_result(confirmType);
				request.setDate(TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

				JSONObject jsonTakes = JSONObject.fromObject(request);
				jsonTakes.put("method",EpassAPIMethodEnum.EPASS_E06_METHOD.getMethod());
				jsonTakes.put("topideal_code",topidealCode);
				//发送盘点指令消息
				SendResult result = rocketMQProducer.send(jsonTakes.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
				System.out.println("发送确认盘点结果消息返回："+result.toString());
				if(result== null){
					failCode.append(model.getCode()+":发送确认盘点结果消息服务异常\n");
					continue;
				}
				if(!result.getSendStatus().name().equals("SEND_OK")){//SEND_OK-成功
					failCode.append(model.getCode()+":发送确认盘点结果指令失败\n");
					continue;
				}

				//更新状态
				if(confirmType.equals(DERP_STORAGE.STORAGE_CONFIRMTYPE_10)){//驳回
					model.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_021);// 021-已作废
				}else {//确认
					model.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_025);//025-盘点结果已确认
				}
				model.setConfirmTime(TimeUtils.getNow());
				model.setConfirmUserId(userId);
				model.setConfirmUsername(name);

				takesStockResultsDao.modify(model);
			}		
		}
    	return failCode.toString();
    }

	@Override
	public Map importTakesStockResult(List<List<Map<String, String>>> data, User user) throws Exception {
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
    	//表头：盘点指令单号、盘点仓库、归属日期
		//表体：盘点指令单号、事业部、商品货号、商品名称、盘盈数量、盘亏数量、调整类型、批次号、是否坏品、生产日期、失效日期、海外仓理货单位、盘点原因

		//商家
		Long merchantId = user.getMerchantId();

		//判断表头的盘点指令单号是否能在商品信息中找到对应的信息
		Map<String, Integer> takesStockCodeMap = new HashMap<>();
		Map<String, TakesStockResultsDTO> headInfoMap = new HashMap<>();
		Map<String, DepotInfoMongo> headDepotMap = new HashMap<>();
		Set<String> existSet = new HashSet<>();

		//表头必填字段的校验:盘点指令单号、盘点仓库、归属日期
		for (int i = 0; i < data.get(0).size(); i++) {
			Map<String, String> map = data.get(0).get(i);
			try {
				String takesStockCode = map.get("盘点指令单号");
				if (StringUtils.isEmpty(takesStockCode)) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i+1);
					message.setMessage("批量导入盘点指令单号不能为空");
					resultList.add(message);
					failure += 1;
					continue;
				}

				if (takesStockCodeMap.containsKey(takesStockCode) || isExistTakesStockCode(takesStockCode, merchantId)) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i+1);
					message.setMessage("批量导入盘点指令单号已经存在");
					resultList.add(message);
					failure += 1;
					continue;
				}

				String depotCode = map.get("盘点仓库");
				if (StringUtils.isEmpty(depotCode)) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i+1);
					message.setMessage("批量导入盘点仓库不能为空");
					resultList.add(message);
					failure += 1;
					continue;
				}



				// 根据仓库自编码获取仓库信息
				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotCode", depotCode);
				DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);
				if (depot == null) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i + 1);
					message.setMessage("盘点仓库不存在");
					resultList.add(message);
					failure += 1;
					continue;
				}

				Map<String, Object> relDepotParam = new HashMap<>();
				relDepotParam.put("merchantId", merchantId);
				relDepotParam.put("depotId", depot.getDepotId());
				DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(relDepotParam);
				if (depotMerchantRel == null) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i + 1);
					message.setMessage("在该商家下盘点仓库不存在");
					resultList.add(message);
					failure += 1;
					continue;
				}

				String sourceTime = map.get("归属日期");
				if (StringUtils.isEmpty(sourceTime)) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i+1);
					message.setMessage("批量导入归属日期不能为空");
					resultList.add(message);
					failure += 1;
					continue;
				}

				if (valiteDate(sourceTime)) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i+1);
					message.setMessage("批量导入归属日期时间格式有误");
					resultList.add(message);
					failure += 1;
					continue;
				}
				
				if(TimeUtils.daysBetween(TimeUtils.parse(sourceTime, "yyyy-MM-dd"), new Date()) < 0) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(i+1);
					message.setMessage("批量导入归属日期不可超过当前时间");
					resultList.add(message);
					failure += 1;
					continue;
				}
				
				TakesStockResultsDTO takesStockResultsDTO = new TakesStockResultsDTO();
				takesStockResultsDTO.setTakesStockCode(takesStockCode);
				takesStockResultsDTO.setDepotId(depot.getDepotId());
				takesStockResultsDTO.setDepotName(depot.getName());
				takesStockResultsDTO.setCreateUserId(user.getId());	// 创建人
				takesStockResultsDTO.setCreateUsername(user.getName());
				if (sourceTime.length()==10) {
					sourceTime = sourceTime + " 00:00:00";
				}
				takesStockResultsDTO.setSourceTime(TimeUtils.parseFullTime(sourceTime));
				headInfoMap.put(takesStockCode, takesStockResultsDTO);
				headDepotMap.put(takesStockCode, depot);
				takesStockCodeMap.put(takesStockCode, i+1);
			} catch (Exception e) {
				e.printStackTrace();
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 1);
				message.setMessage(e.getMessage());
				resultList.add(message);
				failure += 1;
				continue;
			}
		}

		//表体必填项校验：盘点指令单号、商品货号、调整类型、是否坏品（输入值：是、否）
		//盘盈数量、盘亏数量至少有一个是有值的，且大于0
		//当盘点仓库批次强校验时必填项:批次号、生产日期、失效日期
		//当盘点仓库为海外仓时必填项:海外仓理货单位
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> goodsBatchNoMap = new HashMap<>();
		if (failure == 0) {
			for (int j = 0; j < data.get(1).size(); j++) {
				Map<String, String> goodsMap = data.get(1).get(j);
				try {
					String takesStockCode = goodsMap.get("盘点指令单号");
					if (StringUtils.isEmpty(takesStockCode)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("盘点指令单号不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					if (!headInfoMap.containsKey(takesStockCode)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("盘点指令单号在表头不存在");
						resultList.add(message);
						failure += 1;
						continue;
					}
					if (takesStockCodeMap.containsKey(takesStockCode)) {
						takesStockCodeMap.remove(takesStockCode);
					}
					DepotInfoMongo depotInfo = headDepotMap.get(takesStockCode);
					
					String buCode = goodsMap.get("事业部");
					if (StringUtils.isEmpty(buCode)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("批量导入事业部不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					Map<String, Object> buMap = new HashMap<>();
					buMap.put("merchantId", merchantId);
					buMap.put("buCode", buCode);
					buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
					MerchantBuRelMongo buRelMongoDaoOne = merchantBuRelMongoDao.findOne(buMap);
					if (buRelMongoDaoOne == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("批量导入事业部在公司下不存在");
						resultList.add(message);
						failure += 1;
						continue;
					}
					Map<String, Object> depotBuMap = new HashMap<>();
					depotBuMap.put("merchantId", merchantId);
					depotBuMap.put("depotId", depotInfo.getDepotId());
					depotBuMap.put("buId", buRelMongoDaoOne.getBuId());
					MerchantDepotBuRelMongo merchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(depotBuMap);
					if (merchantDepotBuRelMongo == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("批量导入事业部在公司仓库下不存在");
						resultList.add(message);
						failure += 1;
						continue;
					}

					boolean isUserRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), buRelMongoDaoOne.getBuId());
					if (!isUserRelateBu) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("该事业部没有关联用户");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String stockLocationType = goodsMap.get("库位类型");
					if (buRelMongoDaoOne.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0) &&
							StringUtils.isEmpty(stockLocationType)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("公司事业部启用库位管理，库位类型不能为空");
						resultList.add(message);
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
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("库位类型在公司事业部下不存在");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}

					TakesStockResultsDTO takesStockResultsDTO = headInfoMap.get(takesStockCode);
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(merchantId);
					closeAccountsMongo.setDepotId(depotInfo.getDepotId());
					closeAccountsMongo.setBuId(buRelMongoDaoOne.getBuId());
					String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxDate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 归属日期
						if (takesStockResultsDTO.getSourceTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("归属日期必须大于关账日期/月结日期");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}

					String goodsNo = goodsMap.get("商品货号");
					if (StringUtils.isEmpty(goodsNo)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("表体商品信息的商品货号不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}

					Map<String, Object> relDepotParam = new HashMap<>();
					relDepotParam.put("merchantId", merchantId);
					relDepotParam.put("depotId", depotInfo.getDepotId());
					DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(relDepotParam);
					Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
					merchandiseInfo_params.put("goodsNo", goodsNo);
					merchandiseInfo_params.put("merchantId", merchantId);
					merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
					MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
					if (merchandise == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("表体商品信息的商品不存在");
						resultList.add(message);
						failure += 1;
						continue;
					}

					//当仓库在该商家下的“选品限制”为“仅备案商品”时，可选的商品为该商家下仅为备案商品；
					if (depotMerchantRel != null && DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRel.getProductRestriction())) {
						if (!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(merchandise.getIsRecord())) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("表体商品信息的商品不是“备案商品”");
							resultList.add(message);
							failure += 1;
							continue;
						}
					} else if (depotMerchantRel != null && DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRel.getProductRestriction())) {
						if (!DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0.equals(merchandise.getOutDepotFlag())) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("表体商品信息的商品不是“仅外仓统一码”商品");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}

					String surplusNumStr = goodsMap.get("盘盈数量");
					String deficientNumStr = goodsMap.get("盘亏数量");
					Integer surplusNum = null;
					Integer deficientNum = null;
					boolean existed = (StringUtils.isEmpty(surplusNumStr) && StringUtils.isEmpty(deficientNumStr))
							|| (StringUtils.isNotBlank(surplusNumStr) && StringUtils.isNotBlank(deficientNumStr));
					if (existed) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("盘盈数量/盘亏数量不能同时存在或同时为空");
						resultList.add(message);
						failure += 1;
						continue;
					}

					String type = goodsMap.get("调整类型");
					if (StringUtils.isEmpty(type)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("表体商品信息的调整类型不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}

					if (!("盘盈".equals(type)||"盘亏".equals(type))) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("调整类型值为“盘盈”或“盘亏”");
						resultList.add(message);
						failure += 1;
						continue;
					}

					if ("盘盈".equals(type)) {
						if (StringUtils.isEmpty(surplusNumStr) && !StringUtils.isNumeric(surplusNumStr)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("调整类型为“盘盈”时，盘盈数量不能为空且为数值");
							resultList.add(message);
							failure += 1;
							continue;
						}
						surplusNum = Integer.valueOf(surplusNumStr);
						if (surplusNum < 0 || surplusNum == 0) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("调整类型为“盘盈”时，盘盈数量不能为空且大于0");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}
					if ("盘亏".equals(type)) {
						if (StringUtils.isEmpty(deficientNumStr) && !StringUtils.isNumeric(deficientNumStr)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("调整类型为“盘亏”时，盘亏数量不能为空且为数值");
							resultList.add(message);
							failure += 1;
							continue;
						}
						deficientNum = Integer.valueOf(deficientNumStr);
						if (deficientNum < 0 || deficientNum == 0) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("调整类型为“盘亏”时，盘亏数量不能为空且大于0");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}

					String isDamage = goodsMap.get("是否坏品");
					if (StringUtils.isEmpty(isDamage)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("是否坏品不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}

					if (!("是".equals(isDamage)||"否".equals(isDamage))) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("是否坏品值为“是”或“否”");
						resultList.add(message);
						failure += 1;
						continue;
					}


					String batchNo = goodsMap.get("批次号");
					String productionDate = goodsMap.get("生产日期");
					String overdueDate = goodsMap.get("失效日期");
					String unit = goodsMap.get("海外仓理货单位");
					if (StringUtils.isNotBlank(productionDate)) {
						if (valiteDate(productionDate)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("生产日期时间格式有误");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}

					if (StringUtils.isNotBlank(overdueDate)) {
						if (valiteDate(overdueDate)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("生产日期时间格式有误");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}
					//对盘点单，类型为盘盈或其他，仓库若为批次效期强校验，或入库强校验时，批次号、生产日期、失效日期必填；
					if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotInfo.getBatchValidation())||checkTakesResult(type,depotInfo.getBatchValidation())) {
						if (StringUtils.isEmpty(batchNo)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("盘点仓库为批次强校验或入库强校验，批次号不能为空");
							resultList.add(message);
							failure += 1;
							continue;
						}
						if (StringUtils.isEmpty(productionDate)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("盘点仓库为批次强校验或入库强校验，生产日期不能为空");
							resultList.add(message);
							failure += 1;
							continue;
						}
						if (StringUtils.isEmpty(overdueDate)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("盘点仓库为批次强校验或入库强校验，失效日期不能为空");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}

					if (depotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
						if (StringUtils.isEmpty(unit)) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("当盘点仓库为海外仓时, 海外仓理货单位不能为空");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}
					String goodsBatchNoKey = takesStockCode+merchantDepotBuRelMongo.getBuId() + goodsNo + batchNo;
					if (goodsBatchNoMap.containsKey(goodsBatchNoKey) && !goodsBatchNoMap.get(goodsBatchNoKey).equals(type)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("盘点结果单据盘点指令单号:"+takesStockCode+"事业部:"+buRelMongoDaoOne.getBuName()+",货号:"+goodsNo+",批次:"+batchNo+"存在盘盈、盘亏两种调整类型");
						resultList.add(message);
						failure += 1;
						continue;
					}
					goodsBatchNoMap.put(goodsBatchNoKey, type);
					//当仓库+归属时间+商品货号+调整类型+批次号+生产日期+失效日期+是否坏品+理货单位+事业部相同时，报错并提示合并相同项

					String key = takesStockResultsDTO.getDepotId() + takesStockResultsDTO.getSourceTime().toString() + goodsNo
							+ type + batchNo + productionDate + overdueDate + isDamage + unit+merchantDepotBuRelMongo.getBuId();
					if (existSet.contains(key)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("盘点仓库:"+depotInfo.getDepotCode()+"事业部:"+buRelMongoDaoOne.getBuName()+"，归属日期:"+takesStockResultsDTO.getSourceTime()
								+",商品货号:"+goodsNo+",调整类型:"+type+",批次:"+batchNo+",生产日期:"+productionDate+",失效日期:"+overdueDate
								+",是否坏品:"+isDamage+" 有多条数据,请合并后导入");
						resultList.add(message);
						failure += 1;
						continue;
					} else {
						existSet.add(key);
					}
					String reason = goodsMap.get("盘点原因");

					TakesStockResultItemDTO itemDTO = new TakesStockResultItemDTO();
					itemDTO.setGoodsId(merchandise.getMerchandiseId()); //商品ID
					itemDTO.setGoodsCode(merchandise.getGoodsCode()); //商品编码
					itemDTO.setGoodsNo(goodsNo); //商品货号
					itemDTO.setGoodsName(merchandise.getName()); //商品名称
					itemDTO.setBarcode(merchandise.getBarcode()); //商品条形码
					itemDTO.setCommbarcode(merchandise.getCommbarcode()); //商品标准条码
					itemDTO.setBatchNo(batchNo); //批次号
					itemDTO.setDeficientNum(deficientNum); //盘亏数量
					itemDTO.setSurplusNum(surplusNum); //盘盈数量
//					itemDTO.setReasonCode(reason); //盘点原因代码
//					itemDTO.setReasonDes(DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResultItem_reasonCodeList, reason)); //盘点原因
					itemDTO.setReasonDes(reason);
					if (depotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
						itemDTO.setTallyUnit(unit); //理货单位
					}

					itemDTO.setBuId(buRelMongoDaoOne.getBuId());// 事业部id
					itemDTO.setBuName(buRelMongoDaoOne.getBuName());// 事业部名称
					if (StringUtils.isNotBlank(overdueDate)) {
						itemDTO.setOverdueDate(sdf.parse(overdueDate)); //失效日期
					}
					if (StringUtils.isNotBlank(productionDate)) {
						itemDTO.setProductionDate(sdf.parse(productionDate)); //生产日期
					}

					//调整类型: 1-盘盈 2-盘亏
					if ("盘盈".equals(type)) {
						itemDTO.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1);
					} else if ("盘亏".equals(type)) {
						itemDTO.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_2);
					}
					if ("是".equals(isDamage)) {
						itemDTO.setIsDamage(DERP_STORAGE.TAKESSTOCKRESULT_ISDAMAGE_1);
					} else {
						itemDTO.setIsDamage(DERP_STORAGE.TAKESSTOCKRESULT_ISDAMAGE_0);
					}

					if (buStockLocationTypeConfigMongo != null) {
						itemDTO.setStockLocationTypeId(buStockLocationTypeConfigMongo.getBuStockLocationTypeId());
						itemDTO.setStockLocationTypeName(buStockLocationTypeConfigMongo.getName());
					}

					takesStockResultsDTO.getDetails().add(itemDTO);
				} catch (Exception e) {
					e.printStackTrace();
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(j + 1);
					message.setMessage(e.getMessage());
					resultList.add(message);
					failure += 1;
					continue;
				}
			}

			if (takesStockCodeMap.size() > 0) {
				for (Map.Entry<String, Integer> entry : takesStockCodeMap.entrySet()) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(entry.getValue());
					message.setMessage("表头批量导入盘点指令单号在表体商品信息中没有找到对应盘点指令单号");
					resultList.add(message);
					failure += 1;
				}
			}

		}

		if (failure == 0) {
			for (TakesStockResultsDTO takesStockResultsDTO: headInfoMap.values()) {
				String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PDJG);
				TakesStockResultsModel takesStockResultsModel = new TakesStockResultsModel();
				BeanUtils.copyProperties(takesStockResultsDTO, takesStockResultsModel);
				takesStockResultsModel.setCode(code); //盘点单号
				takesStockResultsModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_024); //单据状态：待确认
				takesStockResultsModel.setCreateDate(TimeUtils.getNow()); //创建时间
				takesStockResultsModel.setCreateUserId(user.getId());// 创建人
				takesStockResultsModel.setCreateUsername(user.getName());
				takesStockResultsModel.setSourceType(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_4); //来源：手工导入
				takesStockResultsModel.setServerType(DERP_STORAGE.TAKESSTOCKRESULT_SERVERTYPE_20); //服务类型：自主盘点
				takesStockResultsModel.setMerchantId(merchantId);
				takesStockResultsModel.setMerchantName(user.getMerchantName());
				Long id = takesStockResultsDao.save(takesStockResultsModel);

				List<TakesStockResultItemDTO> takesStockResultItemDTOS = takesStockResultsDTO.getDetails();
				for (TakesStockResultItemDTO itemDTO : takesStockResultItemDTOS) {
					TakesStockResultItemModel itemModel = new TakesStockResultItemModel();
					BeanUtils.copyProperties(itemDTO, itemModel);
					itemModel.setTakesStockResultId(id);
					itemModel.setCreateDate(TimeUtils.getNow());
					takesStockResultItemDao.save(itemModel);
				}
				success ++;
			}
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	/**
	 *
	 * @param type 类型为盘盈且仓库若为批次效期强校验，或入库强校验时，批次号、生产日期、失效日期必填；
	 * @param depotinfo
	 * @return
	 */
	private boolean checkTakesResult(String type,String depotinfo){
    	if("盘盈".equals(type)){
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depotinfo)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getItemByResultIds(List<Long> resultIds) {
		return takesStockResultsDao.getItemByResultIds(resultIds);
	}

	@Override
	public String checkSourceTime(List<Long> ids) throws SQLException {
    	List<String> codeList = new ArrayList<>();
    	List<TakesStockResultsModel> models = takesStockResultsDao.getListByIds(ids);
		for (TakesStockResultsModel model : models) {
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setDepotId(model.getDepotId());
			String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 归属日期
				if (model.getSourceTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					codeList.add(model.getCode());
				}
			}
		}
		return Joiner.on(",").join(codeList);
	}


	/**
	 * 把两个数组组成一个map
	 * @param keys 键数组
	 * @param values 值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put( keys[i].toString().trim(), values[i].toString().trim());
			}
			return map;
		}
		return null;
	}

	/**
	 * 判断盘点指令单号是否已存在
	 * @param code
	 * @return
	 */
	private boolean isExistTakesStockCode(String code, Long merchantId) throws Exception {
		TakesStockResultsModel model = new TakesStockResultsModel();
		model.setMerchantId(merchantId);
		model.setTakesStockCode(code);
		TakesStockResultsModel takesStockResultsModel = takesStockResultsDao.searchByModel(model);
		if (takesStockResultsModel == null) {
			return false;
		}
		return true;
	}

	/**
	 * 单据确认后生成对应的库存调整收发明细、库存做相应的数量调整
	 * @param model
	 */
	private String confirmToAddInventory(TakesStockResultsModel model, String topidealCode) throws Exception {
		List<InvetAddOrSubtractGoodsListJson> itemMQList = new ArrayList<>();
		TakesStockResultItemModel itemModel = new TakesStockResultItemModel();
		itemModel.setTakesStockResultId(model.getId());
		List<TakesStockResultItemModel> itemModels = takesStockResultItemDao.list(itemModel);
		//仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", model.getDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);

		for (TakesStockResultItemModel goodsItem : itemModels) {

			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) ||
					(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1.equals(goodsItem.getType()) &&
							DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation()))) {
				if (StringUtils.isBlank(goodsItem.getBatchNo())) {
					return "批次不能为空！";
				}

				if (goodsItem.getProductionDate() == null) {
					return "生产日期不能为空！";
				}

				if (goodsItem.getOverdueDate() == null) {
					return "失效日期不能为空！";
				}
			}

			// 向推送库存mq中保存商品
			InvetAddOrSubtractGoodsListJson itemMQModel = new InvetAddOrSubtractGoodsListJson();
			// 事业部id
			itemMQModel.setBuId(String.valueOf(goodsItem.getBuId()));
			// 事业部名字
			itemMQModel.setBuName(goodsItem.getBuName());
			itemMQModel.setStockLocationTypeId(String.valueOf(goodsItem.getStockLocationTypeId()));
			itemMQModel.setStockLocationTypeName(goodsItem.getStockLocationTypeName());

			// 商品条形码
			itemMQModel.setBarcode(goodsItem.getBarcode());
			//商品ID
			itemMQModel.setGoodsId(goodsItem.getGoodsId().toString());
			//商品名称
			itemMQModel.setGoodsName(goodsItem.getGoodsName());
			//商品货号
			itemMQModel.setGoodsNo(goodsItem.getGoodsNo());

			// 批次号
			itemMQModel.setBatchNo(goodsItem.getBatchNo());
			// 生产日期
			itemMQModel.setProductionDate(TimeUtils.formatDay(goodsItem.getProductionDate()));
			// 失效日期
			itemMQModel.setOverdueDate(TimeUtils.formatDay(goodsItem.getOverdueDate()));
			//盘盈/盘亏数量
			Integer num = goodsItem.getSurplusNum() == null ? goodsItem.getDeficientNum() : goodsItem.getSurplusNum();
			itemMQModel.setNum(num);
			// 商品分类 （0 好品 1坏品 ）
			itemMQModel.setType(goodsItem.getIsDamage());
			//理货单位
			// 库存 单位	字符串 0 托盘 1箱  2 件
			String unit = goodsItem.getTallyUnit();
			if (StringUtils.isNotBlank(unit)) {
				if (DERP.ORDER_TALLYINGUNIT_00.equals(unit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_0);
				}else if (DERP.ORDER_TALLYINGUNIT_01.equals(unit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_1);
				}else if (DERP.ORDER_TALLYINGUNIT_02.equals(unit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_2);
				}

			}

			//判断过期品
			if (goodsItem.getOverdueDate() != null) {
				//判断是否过期是否过期（0是 1否）
				String isExpire = TimeUtils.isNotIsExpireByDate(goodsItem.getOverdueDate());
				itemMQModel.setIsExpire(isExpire);
			}else {
				itemMQModel.setIsExpire(DERP.ISEXPIRE_1);
			}

			// 盘盈:调增  盘亏：调减
			// 0 调减 1调增
			if (DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1.equals(goodsItem.getType())) {
				itemMQModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
			} else {
				itemMQModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
			}
			itemMQList.add(itemMQModel);
		}
		// 推送库存的盘盈
		InvetAddOrSubtractRootJson invetAddOrSubtractPD = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractPD.setBusinessNo(model.getTakesStockCode());// 业务单号
		invetAddOrSubtractPD.setMerchantId(String.valueOf(model.getMerchantId()));// 商家ID
		invetAddOrSubtractPD.setMerchantName(model.getMerchantName());// 商家名称
		invetAddOrSubtractPD.setTopidealCode(topidealCode);// 商家编码
		invetAddOrSubtractPD.setDepotId(String.valueOf(model.getDepotId()));// 仓库ID
		invetAddOrSubtractPD.setDepotName(model.getDepotName());// 仓库名称
		invetAddOrSubtractPD.setDepotType(depot.getType());// 仓库类型
		invetAddOrSubtractPD.setDepotCode(depot.getDepotCode());// 仓库编码
		invetAddOrSubtractPD.setOrderNo(model.getCode());// 盘点结果单
		invetAddOrSubtractPD.setIsTopBooks(depot.getIsTopBooks()); //是否代销仓
		invetAddOrSubtractPD.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0013); //PDJGD("0013","盘点结果单"),
		invetAddOrSubtractPD.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015);// 单据类型 PDJGD("0015","盘点结果单"),
		invetAddOrSubtractPD.setSourceDate(TimeUtils.formatFullTime());// 单据时间

		invetAddOrSubtractPD.setDivergenceDate(TimeUtils.formatFullTime(model.getSourceTime())); // 出入时间
		// 获取当前年月
		invetAddOrSubtractPD.setOwnMonth(TimeUtils.formatMonth(model.getSourceTime()));
		invetAddOrSubtractPD.setGoodsList(itemMQList);

		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractPD.setBackTags(MQPushBackStorageEnum.STORAGE_RESULTS_CONFIRM_PUSH_BACK.getTags());//回调标签
		invetAddOrSubtractPD.setBackTopic(MQPushBackStorageEnum.STORAGE_RESULTS_CONFIRM_PUSH_BACK.getTopic());//回调主题
		customParam.put("code", model.getCode());// 盘点结果内部单号
		invetAddOrSubtractPD.setCustomParam(customParam);////自定义回调参数
		String PDJson = JSONObject.fromObject(invetAddOrSubtractPD).toString();
		LOGGER.info("仓储盘点结果单据确认 推送库存盘盈MQ 请求报文" + PDJson);
		// 推送盘点
		if (itemMQList.size()>0) {
			rocketMQProducer.send(PDJson, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		}
		return null;
	}

	/**
	 * 确认入库
	 * */
	@Override
	public Map<String, String> confirmInDepot(User user,TakesStockResultsDTO dto) throws Exception {
		Map<String, String> result = new HashMap<>();
		TakesStockResultsModel model = takesStockResultsDao.searchById(dto.getId());
		
		List<TakesStockResultItemDTO> dtos = dto.getDetails();
		List<Long> buIds = new ArrayList<>();
		for (TakesStockResultItemDTO itemDTO : dtos) {
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
			TakesStockResultItemModel itemModel = new TakesStockResultItemModel();
			itemModel.setId(itemDTO.getId());
			itemModel.setBuId(itemDTO.getBuId());
			itemModel.setBuName(buRelMongoDaoOne.getBuName());
			takesStockResultItemDao.modify(itemModel);// 修改事业部
			buIds.add(itemDTO.getBuId());
		}
		//判断盘点结果单的所有表体信息是否都存在事业部，若是则推送库存加减接口并修改单据状态
		Long count = takesStockResultItemDao.countNoExistBu(dto.getId());
		//都存在事业部，推送库存加减接口并修改单据状态
		if (count == 0) {
			// 状态为入库中才可推库存（待确认、已确认两种状态只保存事业部不推库存）
			if (model.getStatus().equals(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022)) {//  022-处理中
				for (Long buId : buIds) {
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(model.getMerchantId());
					closeAccountsMongo.setDepotId(model.getDepotId());
					closeAccountsMongo.setBuId(buId);
					String maxDate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxDate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 归属日期
						if (model.getSourceTime().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							result.put("code", "01");
							result.put("message", "归属日期必须大于关账日期/月结日期");
							return result;
						}
					}
				}
				//推送库存扣减
				String message = this.confirmToAddInventory(model, user.getTopidealCode());
				if (StringUtils.isNotBlank(message)) {
					result.put("code", "01");
					result.put("message", message);
					return result;
				}
				/*修改状态*/
				model.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022);
				model.setInConfirmTime(TimeUtils.getNow());
				model.setInConfirmUserId(user.getId());
				model.setInConfirmUsername(user.getName());
				takesStockResultsDao.modify(model);
			}
		}

		result.put("code", "00");
		result.put("message", "保存成功！");
		return result;
	}

	@Override
	public List<Map<String, Object>> listForExport(TakesStockResultsDTO dto) {
		List<Map<String, Object>> mapList = takesStockResultsDao.listForExport(dto);
		for (Map<String, Object> map : mapList) {
			String status = (String) map.get("status");
			map.put("status", DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_statusList, status));
			String unit = (String) map.get("server_type");
			map.put("server_type", DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_serverTypeList, unit));
			String transport = (String) map.get("model");
			map.put("model", DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_modelList, transport));
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> listForExportItem(TakesStockResultsDTO dto) {
		List<Map<String, Object>> items = takesStockResultsDao.listForExportItem(dto);
		for (Map<String, Object> map : items) {
			String type = (String) map.get("type");
			map.put("type", DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResultItem_typeList, type));
			String isDamage = (String) map.get("is_damage");
			map.put("is_damage", DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_isDamageList, isDamage));
			String tallyUnit = (String) map.get("tally_unit");
			map.put("tally_unit", DERP.getLabelByKey(DERP.unitList, tallyUnit));
		}
		return items;
	}

	private boolean valiteDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String reg = "\\d{4}-\\d{1,2}-\\d{1,2}" ;
		Pattern pattern =Pattern.compile(reg);
		Matcher matcher = pattern.matcher(dateStr);

		if(matcher.matches()) {
			dateStr += " 00:00:00" ;
		}

		try {
			sdf.parse(dateStr) ;
			return false ;
		} catch (ParseException e) {
			return true ;
		}
	}
}
