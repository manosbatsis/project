package com.topideal.service.pushback.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.pushback.sale.SaleOutDepotPushbackService;
import net.sf.json.JSONObject;
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
import java.util.stream.Collectors;

/**
 * 销售出库
 */
@Service
public class SaleOutDepotPushbackServiceImpl implements SaleOutDepotPushbackService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XSLoadingDeliveriesPushBackServiceImpl.class);
	@Autowired
	private SaleOrderDao saleOrderDao;	// 销售订单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;	// 销售出库
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;	// 销售出库表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao; // 销售订单表体
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;// 销售出库分析表
	@Autowired
	private PreSaleOrderCorrelationDao preSaleOrderCorrelationDao;
	@Autowired
	private SaleShelfDao saleShelfDao;
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao ;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private ShelfDao shelfDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private SaleDeclareOrderItemDao saleDeclareOrderItemDao ;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201155800, model = DERP_LOG_POINT.POINT_13201155800_Label,keyword="code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		// JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		// 获取销售出库单编码
		String code = rootJson.getCode();

		// 根据销售出库单号查询销售出库清单
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setCode(code);
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		if (saleOutDepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
		}
		if (null == saleOutDepotModel.getBuId()) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单单号code" + code+"事业部信息值为空");
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单单号code" + code+"事业部信息值为空");
		}

		SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
		if (saleOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售订单不存在,订单编号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售订单不存在,订单编号code:" + code);
		}

		// 根据销售出库单id查询销售出库订单商品
		SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
		saleOutDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleOutDepotItemModel> itemList = saleOutDepotItemDao.list(saleOutDepotItemModel);
		Integer totalOutNum = 0;//总出库数量
		for(SaleOutDepotItemModel item : itemList){
			Integer transferNum = item.getTransferNum()==null?0:item.getTransferNum();// 好品数量
			Integer wornNum = item.getWornNum()==null?0:item.getWornNum();	// 坏品数量
			Integer totalNum = transferNum+wornNum;	// 总量
			totalOutNum = totalOutNum + totalNum;

			//生成差异分析表数据
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			saleAnalysisModel.setSaleItemId(item.getSaleItemId());
			saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
			//存在差异分析，先删除，再新增
			if(saleAnalysisList != null && saleAnalysisList.size() > 0) {
				List<Long> ids = saleAnalysisList.stream().map(SaleAnalysisModel::getId).collect(Collectors.toList());
				saleAnalysisDao.delete(ids);
			}
			saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setCreateDate(TimeUtils.getNow());
			saleAnalysisModel.setCustomerId(saleOutDepotModel.getCustomerId());
			saleAnalysisModel.setCustomerName(saleOutDepotModel.getCustomerName());
			saleAnalysisModel.setGoodsId(item.getGoodsId());
			saleAnalysisModel.setGoodsName(item.getGoodsName());
			saleAnalysisModel.setGoodsNo(item.getGoodsNo());
			saleAnalysisModel.setIsEnd("0");	// 是否完结入库(1-是,0-否)
			saleAnalysisModel.setOrderCode(saleOutDepotModel.getSaleOrderCode());
			saleAnalysisModel.setOrderId(saleOutDepotModel.getSaleOrderId());
			saleAnalysisModel.setOutDepotNum(totalNum);	// 好品+坏品
			saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
			saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
			saleAnalysisModel.setMerchantId(saleOutDepotModel.getMerchantId());
			saleAnalysisModel.setMerchantName(saleOutDepotModel.getMerchantName());
			saleAnalysisModel.setBuId(saleOutDepotModel.getBuId()); // 事业部Id
			saleAnalysisModel.setBuName(saleOutDepotModel.getBuName());// 事业部名称
			saleAnalysisModel.setSaleType(saleOutDepotModel.getSaleType());	//  销售类型: 1购销 2代销 3采购执行
			//获取销售订单某商品信息
			SaleOrderItemModel saleOrderItem = saleOrderItemDao.searchById(item.getSaleItemId());
			saleAnalysisModel.setSaleNum(saleOrderItem.getNum());
			// 差异分析结余量
			saleAnalysisModel.setSurplus(saleOrderItem.getNum()-totalNum);	// 减去好品+坏品总量
			saleAnalysisModel.setSaleItemId(saleOrderItem.getId());
			saleAnalysisDao.save(saleAnalysisModel);
		}


		//出库仓库为中转仓且销售类型为采销执行，自动生成上架单
		Map<String, Object> depotParam = new HashMap<>();
		depotParam.put("depotId", saleOrderModel.getOutDepotId());
		DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(depotParam);
		if(DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderModel.getBusinessModel()) && DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepotInfo.getType())) {
			saveSaleShelf(saleOutDepotModel.getId());
		}

		// 修改销售出库单状态
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

		/**
		 * 销售出库单：
		 *	出库数量 != 上架数量，部分上架
		 *	出库数量 == 上架数量，已上架
		 *  上架数量 == 0，已出库
		 */
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
		 * 销售订单：
		 * 	出库数量！=销售数量，部分出库；
		 * 	没有上架单，出库数量=销售数量，已出库；
		 * 	出库数量=销售数量，上架数量！=出库数量，部分上架；
		 * 	出库数量=销售数量，上架数量=出库数量，已上架
		 */
		String saleStatus = "";
		Map<String,Object> queryMap = new HashMap<>();
		queryMap.put("code",saleOrderModel.getCode());
		// 遍历销售订单商品未出库量
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
		String isEnd ="0";
		if(allOut){
			//未上架数量 == 销售数量，代表没有上架单 ，只进行了出库操作
			if(noShelfNum.equals(saleOrderModel.getTotalNum())) {
				saleStatus = DERP_ORDER.SALEORDER_STATE_018;//已出库
			}else if(noShelfNum.equals(0)){
				saleStatus = DERP_ORDER.SALEORDER_STATE_026;//未上架数量 = 0,已上架
				isEnd = "1";
			}else {
				saleStatus = DERP_ORDER.SALEORDER_STATE_031;//部分上架
			}
		}else {
			//存在未出库的量，部分出库
			saleStatus = DERP_ORDER.SALEORDER_STATE_019;//部分出库
		}

		// 修改销售订单
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setId(saleOrderModel.getId());
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
		//根据销售订单id，更新对应商品差异分析结余量
		for(SaleOutDepotItemModel item : itemList){
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleItemId",item.getSaleItemId());
			Integer outDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//已出库数量

			SaleAnalysisModel saleAnalysisUpdateModel = new SaleAnalysisModel();
			saleAnalysisUpdateModel.setOrderId(saleOrderModel.getId());
			saleAnalysisUpdateModel.setSaleItemId(item.getSaleItemId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisUpdateModel);
			for (SaleAnalysisModel saleAnalysis : saleAnalysisList) {
				saleAnalysis.setSurplus(item.getSaleNum() - outDepotNum);
				saleAnalysis.setIsEnd(isEnd);
				saleAnalysisDao.modify(saleAnalysis);
			}
		}

		//销售预申报出库
		if(saleOutDepotModel.getSaleDeclareOrderId() != null) {
			//获取预申报关联的销售单
			SaleDeclareOrderItemModel tempDeclareItemModel = new SaleDeclareOrderItemModel();
			tempDeclareItemModel.setDeclareOrderId(saleOutDepotModel.getSaleDeclareOrderId());
			List<SaleDeclareOrderItemModel> tempDecalreItemList = saleDeclareOrderItemDao.list(tempDeclareItemModel);
			Integer declareNum = tempDecalreItemList.stream().filter( d->d.getNum() != null).mapToInt(SaleDeclareOrderItemModel::getNum).sum();//预申报总数
			boolean declareAllOut = true;
			//判断预申报单是否生成出库单
			SaleOutDepotDTO tempSaleOut = new SaleOutDepotDTO();
			tempSaleOut.setSaleDeclareOrderCode(saleOutDepotModel.getSaleDeclareOrderCode());
			List <SaleOutDepotDTO> tempSaleOutList = saleOutDepotDao.queryDTODetailsList(tempSaleOut);
			if(tempSaleOutList == null || tempSaleOutList.size() < 1){//预申报不存在出库单，未完全出库
				declareAllOut = false;
			}
			Integer outTranferNum = tempSaleOutList.stream().filter( o->o.getTransferNum() != null).mapToInt(SaleOutDepotDTO::getTransferNum).sum();
			Integer outWornNum = tempSaleOutList.stream().filter( o->o.getWornNum() != null).mapToInt(SaleOutDepotDTO::getWornNum).sum();
			Integer outNum = outTranferNum + outWornNum;
			if(outNum.intValue() != declareNum.intValue()){//预申报量！=出库量，未完全出库
				declareAllOut = false;
			}
			if(declareAllOut){
				String declareStatus = DERP_ORDER.SALEDECLARE_STATUS_004;
				List<Long> saleOutIds = tempSaleOutList.stream().map(SaleOutDepotDTO::getId).distinct().collect(Collectors.toList());
				SaleShelfDTO saleShelfDTO = new SaleShelfDTO();
				saleShelfDTO.setSaleOutOrderIds(saleOutIds);
				List<SaleShelfDTO> decalreSaleShelfList = saleShelfDao.listDTO(saleShelfDTO);
				Integer shelfTranferNum = decalreSaleShelfList.stream().filter( s->s.getShelfNum() != null).mapToInt(SaleShelfDTO::getShelfNum).sum();
				Integer shelfDamagedNum = decalreSaleShelfList.stream().filter( s->s.getDamagedNum() != null).mapToInt(SaleShelfDTO::getDamagedNum).sum();
				Integer shelfLackNum = decalreSaleShelfList.stream().filter( s->s.getLackNum() != null).mapToInt(SaleShelfDTO::getLackNum).sum();
				Integer declareShelfNum = shelfTranferNum + shelfDamagedNum + shelfLackNum;

				if(declareShelfNum.intValue() == declareNum.intValue()){//上架量=预申报量，已上架
					declareStatus = DERP_ORDER.SALEDECLARE_STATUS_007;
				}else if(declareShelfNum.intValue() != declareNum.intValue() && declareShelfNum.intValue() > 0 ){//上架量 !=预申报量，但上架量>0 部分上架
					declareStatus = DERP_ORDER.SALEDECLARE_STATUS_005;
				}
				SaleDeclareOrderModel declareModel = new SaleDeclareOrderModel();
				declareModel.setId(saleOutDepotModel.getSaleDeclareOrderId());
				declareModel.setStatus(declareStatus);
				declareModel.setDeliverDate(tempSaleOutList.get(0).getDeliverDate());
				saleDeclareOrderDao.modify(declareModel);

			}

		}
	}


	/**
	 * 上架
	 * @param saleOutDepotId
	 * @throws Exception
	 */
	private void saveSaleShelf(Long saleOutDepotId) throws Exception {
		SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(saleOutDepotId);
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
		ShelfModel queryShelfModel = new ShelfModel();
		queryShelfModel.setSaleOutDepotId(saleOutDepotId);
		List<ShelfModel> queryShelfList = shelfDao.list(queryShelfModel);
		if(queryShelfList != null && queryShelfList.size() > 0){
			LOGGER.info("上架单已存在，先删除再生成");
			List<Long> ids = queryShelfList.stream().map(ShelfModel::getId).collect(Collectors.toList());
			shelfDao.delete(ids);
			saleShelfDao.delItemsByShelfIds(ids);

		}
		//根据销售出库单id查询销售出库订单商品和对应出库数量
		List<Map<String, Object>> saleOutItemList =saleOutDepotItemDao.getItemGroupByOutId(saleOutDepotModel.getId());
		// 上架日期不能大于当前日期
		Date nowDate = TimeUtils.parseDay(TimeUtils.formatDay());
		if (saleOutDepotModel.getDeliverDate().compareTo(nowDate) == 1) {
			throw new RuntimeException("上架日期不能大于当前日期");
		}
		// 获取最大的关账日/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(saleOutDepotModel.getMerchantId());
		closeAccountsMongo.setDepotId(saleOutDepotModel.getOutDepotId());
		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);

		String maxCloseAccountsMonth = "";
		if (org.apache.commons.lang.StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (org.apache.commons.lang.StringUtils.isNotBlank(maxCloseAccountsMonth) && saleOutDepotModel.getDeliverDate() != null) {
			// 上架日期必须大于关账日期
			if (saleOutDepotModel.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("上架日期必须大于关账日期/月结日期");
			}
		}
		//保存上架单表头信息
		ShelfModel shelfModel = new ShelfModel();
		shelfModel.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_SJD));
		shelfModel.setSaleOrderCode(saleOrderModel.getCode());
		shelfModel.setState(DERP_ORDER.SHELF_STATUS_1);
		shelfModel.setMerchantId(saleOrderModel.getMerchantId());
		shelfModel.setMerchantName(saleOrderModel.getMerchantName());
		shelfModel.setCustomerId(saleOrderModel.getCustomerId());
		shelfModel.setCustomerName(saleOrderModel.getCustomerName());
		shelfModel.setBuId(saleOrderModel.getBuId());
		shelfModel.setBuName(saleOrderModel.getBuName());
		shelfModel.setCurrency(saleOrderModel.getCurrency());
		shelfModel.setOperatorId(saleOutDepotModel.getCreater());
		shelfModel.setCreateDate(TimeUtils.getNow());
		shelfModel.setModifyDate(TimeUtils.getNow());
		shelfModel.setOutDepotId(saleOrderModel.getOutDepotId());
		shelfModel.setOutDepotName(saleOrderModel.getOutDepotName());
		shelfModel.setSaleOrderId(saleOrderModel.getId());
		shelfModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
		shelfModel.setSaleOutDepotId(saleOutDepotModel.getId());
		shelfModel.setPoNo(saleOrderModel.getPoNo());
		shelfModel.setShelfDate(saleOutDepotModel.getDeliverDate());
		shelfModel.setTotalShelfNum(0);
		shelfModel.setOperator("系统创建");
		Long shelfId = shelfDao.save(shelfModel);
		Integer saveShelfNum = 0;
		for(Map<String, Object> outItemModel : saleOutItemList) {
			Long saleItemId = (Long) outItemModel.get("sale_item_id");
			Long goodsId = (Long) outItemModel.get("goods_id");
			BigDecimal outNumB = (BigDecimal) outItemModel.get("num");//商品出库量
			Integer outNum = outNumB.intValue();
			if(outNum == null || outNum.intValue() < 1) {
				continue;
			}
			Map<String, Object> merchandiseParamMap = new HashMap<String, Object>();
			merchandiseParamMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseParamMap);

			SaleShelfModel ssModel = new SaleShelfModel();
			ssModel.setBuId(saleOutDepotModel.getBuId());// 事业部
			ssModel.setBuName(saleOutDepotModel.getBuName());
			ssModel.setBarcode(merchandise.getBarcode());
			ssModel.setGoodsId(merchandise.getMerchandiseId());
			ssModel.setGoodsName(merchandise.getName());
			ssModel.setGoodsNo(merchandise.getGoodsNo());
			ssModel.setCommbarcode(merchandise.getCommbarcode());
			ssModel.setNum(outNum);
			ssModel.setOrderId(saleOutDepotModel.getSaleOrderId());
			ssModel.setOrderType("2");
			ssModel.setShelfNum(outNum);//上架数量 =出库量
			ssModel.setDamagedNum(0);
			ssModel.setLackNum(0);// 缺货数量
			ssModel.setStayShelfNum(0);
			ssModel.setTotalDamagedNum(0);// 已计坏品数量
			ssModel.setTotalShelfNum(outNum);
			ssModel.setTotalLackNum(0);// 已计缺货数量
			ssModel.setTallyingUnit(saleOrderModel.getTallyingUnit());
			ssModel.setPoNo(saleOutDepotModel.getPoNo());
			ssModel.setRemark("");
			ssModel.setShelfDate(saleOutDepotModel.getDeliverDate());
			ssModel.setCreateDate(TimeUtils.getNow());	// 操作时间
			ssModel.setOperator("系统创建");
			ssModel.setOperatorId(saleOutDepotModel.getCreater()); // 操作人ID
			ssModel.setModifyDate(TimeUtils.getNow());
			ssModel.setSaleOutDepotId(saleOutDepotModel.getId());
			ssModel.setShelfId(shelfId);
			ssModel.setSaleItemId(saleItemId);

			saleShelfDao.save(ssModel);

			saveShelfNum += ssModel.getShelfNum();
		}
		ShelfModel updateShelfModel = new ShelfModel();
		updateShelfModel.setId(shelfId);
		updateShelfModel.setTotalShelfNum(saveShelfNum);
		shelfDao.modify(updateShelfModel);

	}
}
