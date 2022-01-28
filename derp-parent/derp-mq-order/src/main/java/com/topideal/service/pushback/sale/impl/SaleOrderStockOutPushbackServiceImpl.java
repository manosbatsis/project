package com.topideal.service.pushback.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.service.pushback.sale.SaleOrderStockOutPushbackService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中转仓出库
 * @author zhanghx
 */
@Service
public class SaleOrderStockOutPushbackServiceImpl implements SaleOrderStockOutPushbackService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleOrderStockOutPushbackServiceImpl.class);

	@Autowired
	private SaleOrderDao saleOrderDao;
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	@Autowired
	private PreSaleOrderCorrelationDao preSaleOrderCorrelationDao;
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;	// 销售出库表体
	@Autowired
	private ShelfDao shelfDao;
	@Autowired
	private SaleShelfDao saleShelfDao;
	//财务经销存关账mongdb
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201153200, model = DERP_LOG_POINT.POINT_13201153200_Label,keyword="code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		// JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		// 获取销售出库单编码
		String code = rootJson.getCode();
		// 获取销售订单id
		Integer saleId = (Integer) rootJson.getCustomParam().get("saleId");
		// 获取销售订单id
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(Long.valueOf(saleId));
		if (saleOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售订单不存在,订单编号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售订单不存在,订单编号code:" + code);
		}

		// 根据销售出库单号查询销售出库清单
		SaleOutDepotModel outDepotModel = new SaleOutDepotModel();
		outDepotModel.setCode(code);
		SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchByModel(outDepotModel);
		if (saleOutDepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
		}

		/**
		 * 销售出库单：
		 *	出库数量 != 上架数量，部分上架
		 *	出库数量 == 上架数量，已上架
		 *  上架数量 == 0，已出库
		 */
		// 根据销售出库单id查询销售出库订单商品
		SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
		saleOutDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleOutDepotItemModel> outItemList = saleOutDepotItemDao.list(saleOutDepotItemModel);
		Integer totalOutNum = 0;//总出库数量
		for(SaleOutDepotItemModel item : outItemList){
			Integer transferNum = item.getTransferNum()==null?0:item.getTransferNum();// 好品数量
			Integer wornNum = item.getWornNum()==null?0:item.getWornNum();	// 坏品数量
			totalOutNum = totalOutNum + transferNum + wornNum;
		}
		// 根据销售出库单id查询销售上架商品
		SaleShelfModel shelfModel = new SaleShelfModel();
		shelfModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleShelfModel> saleShelfList = saleShelfDao.list(shelfModel);
		Integer totalShelfNum = 0;// 上架数量
		for(SaleShelfModel saleshelfModel : saleShelfList) {
			Integer transferNum = saleshelfModel.getShelfNum() == null ? 0: saleshelfModel.getShelfNum();// 好品数量
			Integer wornNum = saleshelfModel.getDamagedNum() == null ? 0:saleshelfModel.getDamagedNum();	// 坏品数量
			Integer lackNum = saleshelfModel.getLackNum() == null ? 0 :saleshelfModel.getLackNum();	// 缺货数量
			totalShelfNum = totalShelfNum + transferNum + wornNum + lackNum;
		}
		String saleOutStatus = "";
		if(totalShelfNum.equals(totalOutNum)) {
			saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_026;//已上架
		}else if(totalShelfNum.equals(0)){
			saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_018;//已出库
		}else {
			saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_031;//部分上架
		}

		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setId(saleOutDepotModel.getId());
		sOutDepotModel.setStatus(saleOutStatus);
		saleOutDepotDao.modify(sOutDepotModel);

		/**
		 * 有出库单，出库数量！=销售数量，部分出库；
		 * 有出库单，没有上架单，出库数量=销售数量，已出库；
		 * 有出库单，出库数量=销售数量，上架数量！=出库数量，部分上架；
		 * 有出库单，出库数量=销售数量，上架数量=出库数量，已上架
		 */
		String saleStatus = "";
		Map<String,Object> queryMap = new HashMap<>();
		queryMap.put("code",saleOrderModel.getCode());
		List<Map<String, Object>> goodsNotOutNumList = saleOutDepotDao.getGoodsNotOutNumList(queryMap);
		boolean allOut = true;
		for (Map<String, Object> goodsMap : goodsNotOutNumList) {
			BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
			if (diffnumbg.intValue() > 0) {
				allOut = false;// 存在未出库的量
				break;
			}
		}

		Integer noShelfNum = 0;
		List<Map<String, Object>> goodsNotShelNumList = saleOutDepotDao.getGoodsNotShelNumList(queryMap);
		// 遍历出库单商品未上架量，若全部商品未上架量都为0则销售单状态为已上架，若存在未上量大于0的商品则销售单状态为部分上架
		for (Map<String, Object> goodsMap : goodsNotShelNumList) {
			BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
			if (diffnumbg.intValue() > 0) {
				// 存在未上架的量
				noShelfNum = noShelfNum + diffnumbg.intValue();
			}
		}
		if(allOut){
			//未上架数量 == 销售数量，代表没有上架单 ，只进行了出库操作
			if(noShelfNum.equals(saleOrderModel.getTotalNum())) {
				saleStatus = DERP_ORDER.SALEORDER_STATE_018;//已出库
			}else if(noShelfNum.equals(0)){
				saleStatus = DERP_ORDER.SALEORDER_STATE_026;//未上架数量 = 0,已上架
			}else {
				saleStatus = DERP_ORDER.SALEORDER_STATE_031;//部分上架
			}
		}else {
			//存在未出库的量，部分出库
			saleStatus = DERP_ORDER.SALEORDER_STATE_019;//部分出库
		}

		// 修改销售订单
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setId(Long.valueOf(saleId));
		sModel.setState(saleStatus);
		sModel.setModifyDate(TimeUtils.getNow());	// 设置修改时间
		saleOrderDao.modify(sModel);

		// 如果是预售转销
		if ("1".equals(saleOrderModel.getOrderType())) {
			// 按商品分组查询销售出库单商品
			List<Map<String, Object>> itemMapList = saleOutDepotItemDao.getItemGroupByOutId(saleOutDepotModel.getId());
			List<PreSaleOrderCorrelationModel> preSaleList = preSaleOrderCorrelationDao.getPreSaleOrderCorList(saleOutDepotModel,saleOrderModel,itemMapList);
			for (PreSaleOrderCorrelationModel preSale : preSaleList) {
				preSaleOrderCorrelationDao.save(preSale);
			}
		}

		/**
		 * 如果是采购链路生成的销售单，自动执行上架操作
		 * @author gy
		 */
		if(StringUtils.isNotBlank(saleOrderModel.getLinkUniueCode())) {

			String dateStr = TimeUtils.format(new Date(), "yyyy-MM-dd") ;

			SaleOrderItemModel queryItemModel = new SaleOrderItemModel() ;
			queryItemModel.setOrderId(saleOrderModel.getId());

			List<SaleOrderItemModel> itemList = saleOrderItemDao.list(queryItemModel) ;

			JSONObject jsonItemList = new JSONObject() ;
			net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray() ;

			for (SaleOrderItemModel saleOrderItemDTO : itemList) {
				JSONObject saleItemJson = JSONObject.fromObject(saleOrderItemDTO);

				saleItemJson.put("poNo", saleOrderModel.getPoNo()) ;
				saleItemJson.put("orderType", DERP_ORDER.SALESHELF_ORDERTYPE_1) ;
				saleItemJson.put("totalShelfNum", saleOrderItemDTO.getNum()) ;
				saleItemJson.put("totalDamagedNum", 0) ;
				saleItemJson.put("totalLackNum", 0) ;
				saleItemJson.put("stayShelfNum", 0) ;
				saleItemJson.put("damagedNum", 0) ;
				saleItemJson.put("lackNum", 0) ;
				saleItemJson.put("shelfNum", saleOrderItemDTO.getNum()) ;
				saleItemJson.put("shelfDate", dateStr) ;
				saleItemJson.put("saleItemId",saleOrderItemDTO.getId());

				jsonArray.add(saleItemJson) ;
			}

			jsonItemList.put("itemList", jsonArray) ;
			jsonItemList.put("code", saleOrderModel.getCode()) ;
			jsonItemList.put("saleOutCode", saleOutDepotModel.getCode()) ;
			jsonItemList.put("saleOutId", saleOutDepotModel.getId());

			saveSaleShelf(jsonItemList.toString()) ;
		}
		//根据销售订单id，更新对应商品差异分析结余量
		for(SaleOutDepotItemModel item : outItemList){
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleItemId",item.getSaleItemId());
			Integer outDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//已出库数量

			SaleAnalysisModel saleAnalysisUpdateModel = new SaleAnalysisModel();
			saleAnalysisUpdateModel.setOrderId(Long.valueOf(saleId));
			saleAnalysisUpdateModel.setSaleItemId(item.getSaleItemId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisUpdateModel);
			for (SaleAnalysisModel saleAnalysis : saleAnalysisList) {
				saleAnalysis.setSurplus(item.getSaleNum() - outDepotNum);
				saleAnalysisDao.modify(saleAnalysis);
			}
		}
	}

	/**
	 * 销售上架（新）
	 * @param json
	 * @param userId
	 * @param name
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	public boolean saveSaleShelf(String json) throws Exception {
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		// 解析表头数据
		String saleOutCode = jsonObj.getString("saleOutCode");   // 销售出库单编号
		String saleOutId = jsonObj.getString("saleOutId");   // 销售出库单id
		String code1 = jsonObj.getString("code");   // 销售订单编号
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setCode(code1);
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		// 往上架单中加数据
		String code = SmurfsUtils.getID(DERP.UNIQUE_ID_SJD);
		ShelfModel sshelfModel = new ShelfModel();
		sshelfModel.setCode(code);
		sshelfModel.setSaleOrderCode(code1);
		sshelfModel.setState(DERP_ORDER.SHELF_STATUS_1);
		sshelfModel.setMerchantId(saleOrderModel.getMerchantId());
		sshelfModel.setMerchantName(saleOrderModel.getMerchantName());
		sshelfModel.setCustomerId(saleOrderModel.getCustomerId());
		sshelfModel.setCustomerName(saleOrderModel.getCustomerName());
		sshelfModel.setBuId(saleOrderModel.getBuId());
		sshelfModel.setBuName(saleOrderModel.getBuName());
		sshelfModel.setCurrency(saleOrderModel.getCurrency());
		sshelfModel.setOperator(saleOrderModel.getCreateName());
		sshelfModel.setOperatorId(saleOrderModel.getCreater());
		sshelfModel.setCreateDate(new Timestamp(System.currentTimeMillis()));
		sshelfModel.setModifyDate(new Timestamp(System.currentTimeMillis()));
		sshelfModel.setOutDepotId(saleOrderModel.getOutDepotId());
		sshelfModel.setOutDepotName(saleOrderModel.getOutDepotName());
		sshelfModel.setSaleOrderId(saleOrderModel.getId());
		sshelfModel.setSaleOutDepotCode(saleOutCode);
		sshelfModel.setSaleOutDepotId(Long.valueOf(saleOutId));

		shelfDao.save(sshelfModel);

		//获取已上架的数据统计已上架总量
		SaleShelfModel checkModel = new SaleShelfModel();
		checkModel.setOrderId(saleOrderModel.getId());
		List<SaleShelfModel> shelfList = saleShelfDao.list(checkModel);
		Map<Long, Integer> goodsShelfMap = new HashMap<Long, Integer>();
		for(SaleShelfModel ssModel:shelfList){
			Integer areadyShelfNum = ssModel.getShelfNum()+ssModel.getDamagedNum()+ssModel.getLackNum();
			if(goodsShelfMap.containsKey(ssModel.getSaleItemId())){
				areadyShelfNum = goodsShelfMap.get(ssModel.getSaleItemId())+areadyShelfNum;
			}
			goodsShelfMap.put(ssModel.getSaleItemId(), areadyShelfNum);
		}

		//解析表体数据
		Integer stotalShelfNum = 0;
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		for(int i=0;i<itemArr.size();i++){
			JSONObject job = itemArr.getJSONObject(i);
			Long orderId = Long.valueOf(job.getString("orderId"));	// 销售订单ID
			String orderType = (String) job.get("orderType");
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = (String) job.get("goodsNo");
			String goodsName = (String) job.get("goodsName");
			String barcode = (String) job.get("barcode");
			String tallyingUnit = (String) job.get("tallyingUnit");
			Integer num = Integer.valueOf(job.getString("num"));
			Integer totalShelfNum = Integer.valueOf(job.getString("totalShelfNum"));
			Integer totalDamagedNum = Integer.valueOf(job.getString("totalDamagedNum"));
			Integer totalLackNum = Integer.valueOf(job.getString("totalLackNum"));
			Integer stayShelfNum = Integer.valueOf(job.getString("stayShelfNum"));
			Integer shelfNum = Integer.valueOf(job.getString("shelfNum"));
			Integer damagedNum = Integer.valueOf(job.getString("damagedNum"));
			Integer lackNum = Integer.valueOf(job.getString("lackNum"));
			String poNo = (String) job.get("poNo");
			String remark = (String) job.get("remark");
			String commbarcode = (String) job.get("commbarcode");
			String shelfDate = (String) job.get("shelfDate");
			Long saleItemId = Long.valueOf((Integer)job.get("saleItemId"));

			if(shelfNum <= 0 && damagedNum <= 0&&lackNum<=0){//本次的上架数量和残损数量都小于等于0，不记录
				continue;
			}

			// 上架数量+残损数量+少货数量不能超过已经上架数量
			if(goodsShelfMap.containsKey(goodsId)){
				Integer areadyNum = goodsShelfMap.get(goodsId);
				Integer stayNum = num-areadyNum;// 待上架量
				Integer bcNum = shelfNum+damagedNum+lackNum;// 本次上架量
				if(bcNum>stayNum){
					throw new RuntimeException("上架数量+残损数量+少货数量不能大于待上架数量");
				}
			}

			// 上架日期不能大于当前日期
			Date nowDate = TimeUtils.parseDay(TimeUtils.formatDay());
			Date shelfD = TimeUtils.parseDay(shelfDate);
			if (shelfD.getTime()>nowDate.getTime()) {
				throw new RuntimeException("上架日期不能大于当前日期");
			}
			//上架日期要大于等于签收日期
			if(shelfD.getTime()<saleOrderModel.getReceiveDate().getTime()) {
				throw new RuntimeException("上架日期要大于等于签收日期");
			}
			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOrderModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleOrderModel.getOutDepotId());
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
			if (org.apache.commons.lang.StringUtils.isNotBlank(maxCloseAccountsMonth) && org.apache.commons.lang.StringUtils.isNotBlank(shelfDate)) {
				// 上架日期必须大于关账日期
				if (Timestamp.valueOf(shelfDate + " 00:00:00").getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("上架日期必须大于关账日期/月结日期");
				}
			}

			//组装销售上架信息表里的数据
			SaleShelfModel ssModel = new SaleShelfModel();
			ssModel.setBuId(saleOrderModel.getBuId());// 事业部
			ssModel.setBuName(saleOrderModel.getBuName());
			ssModel.setBarcode(barcode);
			ssModel.setDamagedNum(damagedNum);
			ssModel.setGoodsId(goodsId);
			ssModel.setGoodsName(goodsName);
			ssModel.setGoodsNo(goodsNo);
			ssModel.setCommbarcode(commbarcode);
			ssModel.setNum(num);
			ssModel.setOrderId(orderId);
			ssModel.setOrderType(orderType);
			ssModel.setShelfNum(shelfNum);
			ssModel.setTallyingUnit(tallyingUnit);
			ssModel.setTotalDamagedNum(totalDamagedNum);
			ssModel.setTotalShelfNum(totalShelfNum);
			ssModel.setStayShelfNum(stayShelfNum);
			ssModel.setLackNum(lackNum);// 缺货数量
			ssModel.setTotalLackNum(totalLackNum);// 已计缺货数量
			ssModel.setPoNo(poNo);
			ssModel.setRemark(remark);
			ssModel.setShelfDate(TimeUtils.parse(shelfDate, "yyyy-MM-dd"));
			ssModel.setCreateDate(TimeUtils.getNow());	// 操作时间
			ssModel.setOperatorId(saleOrderModel.getCreater()); // 操作人ID
			ssModel.setOperator(saleOrderModel.getCreateName());	// 操作人名字
			ssModel.setModifyDate(TimeUtils.getNow());
			ssModel.setShelfId(sshelfModel.getId());
			ssModel.setSaleOutDepotId(Long.valueOf(saleOutId));
			ssModel.setSaleItemId(saleItemId);
			saleShelfDao.save(ssModel);

			stotalShelfNum += shelfNum + damagedNum + lackNum;
			sshelfModel.setPoNo(poNo);
			sshelfModel.setShelfDate(TimeUtils.parse(shelfDate, "yyyy-MM-dd"));
		}
		sshelfModel.setTotalShelfNum(stotalShelfNum);
		shelfDao.modify(sshelfModel);

		/**更新销售出库单状态 start------------
		 * 采购链路生成的销售单，全量上架，直接更新出库单为已上架即可**/
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setCode(saleOutCode);
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);

		saleOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_026);
		saleOutDepotModel.setModifyDate(TimeUtils.getNow());
		saleOutDepotDao.modify(saleOutDepotModel);
		/**更新销售出库单状态 end------------**/

		/**更新销售单状态----------------------------------------------------
		 * 1.出库单的每个商品都已全部上架==已上架，否则部分上架*/
		Map<String,Object> queryMap = new HashMap<>();
		queryMap.put("code",saleOrderModel.getCode());
		List<Map<String,Object>> goodsNotShelNumList = saleOutDepotDao.getGoodsNotShelNumList(queryMap);
		//遍历出库单商品未上架量，若全部商品未上架量都为0则销售单状态为已上架，若存在未上量大于0的商品则销售单状态为部分上架
		SaleOrderModel saleOrderUpate = new SaleOrderModel();
		saleOrderUpate.setId(saleOrderModel.getId());
		saleOrderUpate.setState(DERP_ORDER.SALEORDER_STATE_026);//已上架
		for(Map<String,Object> goodsMap : goodsNotShelNumList){
			BigDecimal diffnumbg = (BigDecimal)goodsMap.get("diffnum");
			if(diffnumbg.intValue()>0){
				saleOrderUpate.setState(DERP_ORDER.SALEORDER_STATE_031);//部分上架
			}
		}
		saleOrderDao.modify(saleOrderUpate);

		/**2.销售单的每个商品都出库完&&已上架==已完结，否则不用更新*/
		List<Map<String,Object>> goodsNotOutNumList = saleOutDepotDao.getGoodsNotOutNumList(queryMap);
		boolean allOut = true;
		for(Map<String,Object> goodsMap : goodsNotOutNumList){
			BigDecimal diffnumbg = (BigDecimal)goodsMap.get("diffnum");
			if(diffnumbg.intValue()>0){
				allOut = false;//存在未出库的量
			}
		}
		if(allOut==true&&saleOrderUpate.getState().equals(DERP_ORDER.SALEORDER_STATE_026)){
			//更新差异分析表状态
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
			for(SaleAnalysisModel saleAnalysis :saleAnalysisList){
				saleAnalysis.setIsEnd("1");
				saleAnalysis.setEndDate(TimeUtils.getNow());
				saleAnalysisDao.modify(saleAnalysis);
			}
		}

		return true;
	}

}
