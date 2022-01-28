package com.topideal.service.impl;

import com.topideal.common.constant.*;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.BuInventoryItemDao;
import com.topideal.dao.order.*;
import com.topideal.dao.reporting.BuFinanceInventorySummaryDao;
import com.topideal.dao.reporting.InventoryFallingPriceReservesDao;
import com.topideal.dao.reporting.WeightedPriceDao;
import com.topideal.dao.system.*;
import com.topideal.entity.dto.InventoryFallingPriceTemDTO;
import com.topideal.entity.vo.order.SaleOrderModel;
import com.topideal.entity.vo.order.SaleOutDepotItemModel;
import com.topideal.entity.vo.order.SaleOutDepotModel;
import com.topideal.entity.vo.order.TransferInDepotModel;
import com.topideal.entity.vo.reporting.InventoryFallingPriceReservesModel;
import com.topideal.entity.vo.reporting.WeightedPriceModel;
import com.topideal.entity.vo.system.*;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.service.InventoryFallingPriceReservesService;
import com.topideal.service.SettlementPriceService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryFallingPriceReservesServiceImpl implements InventoryFallingPriceReservesService {

	// 商品dao
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;
	// 仓库dao
	@Autowired
	private DepotInfoDao depotInfoDao;
	//公司仓库事业部dao
	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao;
	// 存货跌价准备dao
	@Autowired
	private InventoryFallingPriceReservesDao inventoryFallingPriceReservesDao;
	@Autowired
	private SettlementPriceService settlementPriceService ;
	@Autowired
	private CommbarcodeDao commbarcodeDao;
	@Autowired
	private WeightedPriceDao weightedPriceDao ;
	@Autowired
	private BusinessUnitDao businessUnitDao;
	@Autowired
	private BuInventoryItemDao buInventoryItemDao;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	@Autowired
	private SaleOrderDao saleOrderDao;// 销售订单
	@Autowired
	private TransferOrderDao transferOrderDao;// 调拨订单
	@Autowired
	private TransferInDepotDao transferInDepotDao;// 调拨入库
	@Autowired
	private BuFinanceInventorySummaryDao buFinanceInventorySummaryDao;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao ;
	
	
	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(InventoryFallingPriceReservesServiceImpl.class);

	/**
	 * 生成存货跌价准备报表
	 * 
	 * @throws SQLException
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201500090, model = DERP_LOG_POINT.POINT_13201500090_Label)
	public void saveSummaryReport(String json, String keys, String topics, String tags,String month,MerchantInfoModel merchant) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		String depotIds = (String) jsonMap.get("depotIds");// 仓库Id 多个用,号隔开
		String buIds = (String) jsonMap.get("buIds");// 事业部Id 多个用,号隔开

		/**报表日期：默认为当前日期，若月份小于当前日期月份则为月份的下月1号*/
		String reportDate = TimeUtils.formatDay() ;
		String currentMonth = TimeUtils.formatMonth(new Date());//当前日期月份
        if(month.compareTo(currentMonth)<0){
			reportDate = TimeUtils.getNextMonthFirstDay(month);
		}
		/**3.检查月份是否小于2021.11  是则结束不允许刷新*/
		if(month.compareTo("2021-11")<0){
			logger.info(merchant.getName()+",2021-11前的数据不允许刷新,结束执行");
			return;
		}

		Map<String,Object> queryMap = new HashMap<>();
		queryMap.put("merchantId",merchant.getId());
		queryMap.put("depotIds",depotIds);
		queryMap.put("buIds",buIds);

        /**4.查询商家仓库、事业部统计存货跌价为是、公司事业部状态为启用的仓库、事业部数据（若指定仓库、事业部则查指定条件数据）*/
		List<Map<String,Object>> depotBuList = merchantDepotBuRelDao.getDepotAndBuList(queryMap);
        if(depotBuList==null||depotBuList.size()<=0){
			logger.info("商家名称："+merchant.getName()+",仓库|事业部数量为0,结束执行");
			return;
		}

        //存储所有存货跌价商品数据 key=goodsId,value=商品id、货号、商品名称、标准条码,箱转件数量、托转件数量、标准品牌Id、标准品牌名称、二级分类id、二级分类名称
		Map<String,Map<String,Object>> merchandiseAllMap = new HashMap<>();
		/**循环生成公司仓库、事业部的存货跌价报表*/
        for(Map<String,Object> depotBuMap:depotBuList){

			//查询仓库信息
			DepotInfoModel depotInfo = depotInfoDao.searchById((Long)depotBuMap.get("depot_id"));
			//查询事业部
			BusinessUnitModel buModel = businessUnitDao.searchById((Long)depotBuMap.get("bu_id"));

			//查询本月是否已关账
			Map<String, Object> statusMap = new HashMap<String, Object>();
			statusMap.put("merchantId", merchant.getId());
			statusMap.put("month", month);
			statusMap.put("buId", buModel.getId());
			statusMap.put("status",DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);
			String status=buFinanceInventorySummaryDao.getSummaryStatus(statusMap);
			//状态 029-未关账 030-已关账
			if(!StringUtils.isEmpty(status)&&status.equals(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030)){
				logger.info("商家:"+merchant.getName()+",事业部:"+buModel.getName()+",月份："+month+"已关账,结束执行");
				continue;
			}

			//公共查询条件
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("merchantId",depotBuMap.get("merchant_id"));
			paramMap.put("depotId",depotBuMap.get("depot_id"));
			paramMap.put("buId",depotBuMap.get("bu_id"));
			paramMap.put("month",month);
			paramMap.put("reportMonth",month);

			inventoryFallingPriceReservesDao.delReportByMap(paramMap) ;
			//获取事业部库存明细
			List<InventoryFallingPriceTemDTO> buItemList = getBuInventoryItemList(paramMap);
			logger.info("事业部库存数量："+buItemList.size());
			//获取采购在途
			List<InventoryFallingPriceTemDTO> purchaseList = null;
			if(depotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_D)){//采购在途全部计算到中转仓
				purchaseList = getPurchaseNotshelfList(paramMap);
			}
			//获取销售在途 销售在途量=出库量-上架量
			List<InventoryFallingPriceTemDTO> saleNoshelfList = getSaleNoshelfList(paramMap);
			//获取调拨在途
			List<InventoryFallingPriceTemDTO> transferNoshelfList = getTransferNoshelfList(paramMap);
			//合并所有实体
			List<InventoryFallingPriceTemDTO> temListAll = new ArrayList<>();
			if(buItemList!=null&&buItemList.size()>0) temListAll.addAll(buItemList);
			if(purchaseList!=null&&purchaseList.size()>0) temListAll.addAll(purchaseList);
			if(saleNoshelfList!=null&&saleNoshelfList.size()>0) temListAll.addAll(saleNoshelfList);
			if(transferNoshelfList!=null&&transferNoshelfList.size()>0) temListAll.addAll(transferNoshelfList);


			//获取所有存货基础实体的商品数据
			String errorMsg = getMerchandiseAll(temListAll,merchandiseAllMap);
			String headmsg = merchant.getName()+"_"+depotInfo.getName()+"_"+buModel.getName();//提示消息
			if(StringUtils.isNotBlank(errorMsg)){
				throw new RuntimeException(headmsg+"商品ID："+errorMsg +"标准条码管理未维护") ;
			}

			//箱托转换
			errorMsg = "";
			errorMsg = changeUnit(temListAll,merchandiseAllMap);
			if(StringUtils.isNotBlank(errorMsg)){
				throw new RuntimeException(headmsg+" 商品ID："+errorMsg +"箱拖转换未维护") ;
			}

			//保存
			saveEntity(merchant,depotInfo,buModel,reportDate,month, temListAll,merchandiseAllMap);
		}
	}

    /**获取事业部库存明细
	 * */
    private List<InventoryFallingPriceTemDTO> getBuInventoryItemList(Map<String,Object> paramMap)throws Exception{
		List<InventoryFallingPriceTemDTO> tempList =new ArrayList<>();//存储存货跌价基础实体
    	//获取事业部库存明细
		List<Map<String,Object>> itemList = buInventoryItemDao.getItemList(paramMap);
		if(itemList!=null&&itemList.size()>0){
			for(Map<String,Object> itemMap : itemList){
				InventoryFallingPriceTemDTO tempDTO = new InventoryFallingPriceTemDTO();
				tempDTO.setInverntoryStatus(DERP_REPORT.INVERNTORYSTATUS_1);//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
				tempDTO.setDepotId((Long)paramMap.get("depotId"));
				tempDTO.setBuId((Long)paramMap.get("buId"));
				tempDTO.setGoodsId((Long)itemMap.get("goods_id"));
				tempDTO.setBatchNo((String)itemMap.get("batch_no"));
				tempDTO.setProductionDate((Date)itemMap.get("production_date"));
				tempDTO.setOverdueDate((Date)itemMap.get("overdue_date"));
				tempDTO.setFirstShelfDate((Date)itemMap.get("first_shelf_date"));
				tempDTO.setInverntoryType((String)itemMap.get("type"));//库存类型  0 正常品  1 残次品
				tempDTO.setSurplusNum(Long.valueOf((Integer)itemMap.get("num")));
				tempDTO.setEffectiveInterval((String)itemMap.get("effective_interval"));//效期区间
				tempDTO.setTallyingUnit((String)itemMap.get("unit"));

				tempList.add(tempDTO);
			}
		}
		return tempList;
	}
	/**获取采购在途明细
	 * 采购在途量=采购订单量-已入库已勾稽量，拿到在途量后再分摊到订单商品效期区间上
	 * */
	private List<InventoryFallingPriceTemDTO> getPurchaseNotshelfList(Map<String,Object> paramMap) throws Exception{
		List<InventoryFallingPriceTemDTO> tempList =new ArrayList<>();//存储存货跌价基础实体

		List<Map<String,Object>> purchaseNotshelfList = purchaseOrderItemDao.getBuFinanceAddPurchaseNotshelfDetailsWeighte(paramMap);
        if(purchaseNotshelfList==null||purchaseNotshelfList.size()<=0){
			logger.info("仓库|事业部采购在途数量为0,跳过");
			return tempList;
		}
		/**采购在途量分摊到订单区间上
		 * 分摊效期区间顺序：2/3以上、1/2＜X≤2/3、1/3＜X≤1/2、1/5＜X≤1/3、1/10＜X≤1/5、0＜X≤1/10、残次品、过期品
		 */
         for(Map<String,Object> notshelfMap : purchaseNotshelfList){//start----------01
			String code = (String)notshelfMap.get("code");//采购单号
			Long goodsId = (Long)notshelfMap.get("goods_id");//商品id
			String tallyingUnit = (String)notshelfMap.get("tallying_unit");//理货单位
			String effectiveInterval = (String)notshelfMap.get("effective_interval");//剩余效期区间
			String currency = (String)notshelfMap.get("currency");//币种
			BigDecimal price = (BigDecimal)notshelfMap.get("price");//采购单价
			BigDecimal num = (BigDecimal)notshelfMap.get("num");//在途数量

			//基础实体
			InventoryFallingPriceTemDTO tempDTO = new InventoryFallingPriceTemDTO();
			tempDTO.setInverntoryStatus(DERP_REPORT.INVERNTORYSTATUS_2);//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
			tempDTO.setDepotId((Long)paramMap.get("depotId"));
			tempDTO.setBuId((Long)paramMap.get("buId"));
			tempDTO.setGoodsId(goodsId);
			tempDTO.setSurplusNum(Long.valueOf(num.intValue()));
			tempDTO.setEffectiveInterval(effectiveInterval);//效期区间
			tempDTO.setTallyingUnit(tallyingUnit);//理货单位 00 托盘 01箱  02 件
			tempDTO.setCurrency(currency);
			tempDTO.setPrice(price);
			tempDTO.setNoshelfCode(code);
			if(StringUtils.isNotBlank(effectiveInterval)
					&&effectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_8)){
				tempDTO.setInverntoryType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//库存类型  0 正常品  1 残次品
			}else{
				tempDTO.setInverntoryType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
			}
			tempList.add(tempDTO);
		 }

		return tempList;
	}
	/**获取销售在途明细
	 * 销售在途量=出库量-上架量，拿到在途量再分摊到出库批次上
	 * */
	private List<InventoryFallingPriceTemDTO> getSaleNoshelfList(Map<String,Object> paramMap) throws Exception{
		List<InventoryFallingPriceTemDTO> tempList =new ArrayList<>();//存储存货跌价基础实体


		List<Map<String,Object>> saleNoshelfList = saleOutDepotItemDao.getBuFinanceAddSaleNoshelfDetailsDX(paramMap);
		if(saleNoshelfList==null||saleNoshelfList.size()<=0){
			logger.info("仓库|事业部销售在途数量为0,跳过");
			return tempList;
		}
		/**销售在途量分摊到出库单批次上
		 * 分摊效期区间顺序：2/3以上、1/2＜X≤2/3、1/3＜X≤1/2、1/5＜X≤1/3、1/10＜X≤1/5、0＜X≤1/10、残次品、过期品
		 */
		for(Map<String,Object> notshelfMap : saleNoshelfList){//start----------01
			Long outDepotId = (Long)notshelfMap.get("id");//销售出库单id
			Long goodsId = (Long)notshelfMap.get("goods_id");//商品id
			Long saleItemId = (Long)notshelfMap.get("sale_item_id");//商品id
			String tallyingUnit = (String)notshelfMap.get("tallying_unit");//理货单位
			BigDecimal num = (BigDecimal)notshelfMap.get("num");//在途数量
			int numInt = num.intValue();

			//查询出库单
			SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(outDepotId);
			//查询销售订单
			SaleOrderModel saleOrderModel= saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());

			//获取销售出库单、商品,把在途量分摊到出库单商品商品
			SaleOutDepotItemModel orderItem = new SaleOutDepotItemModel();
			orderItem.setSaleOutDepotId(outDepotId);
			orderItem.setGoodsId(goodsId);
			orderItem.setSaleItemId(saleItemId);
			List<SaleOutDepotItemModel> itemList = saleOutDepotItemDao.list(orderItem);
			List<SaleOutDepotItemModel> saleLtemList = new ArrayList<>();
			List<SaleOutDepotItemModel> nullitemList = new ArrayList<>();//效期为空的排在最后面
			for(SaleOutDepotItemModel item : itemList){
				if(item.getOverdueDate()==null){
					nullitemList.add(item);
				}else{
					saleLtemList.add(item);
				}
			}
			//按失效日期倒序排序
			saleLtemList = saleLtemList.stream().sorted(Comparator.comparing(SaleOutDepotItemModel::getOverdueDate).reversed()) .collect(Collectors.toList());
			if(nullitemList.size()>0) saleLtemList.addAll(nullitemList);//排在最后
			for(SaleOutDepotItemModel item : saleLtemList){//start----------02
				if(item.getTransferNum()==null) continue;
				if(item.getTransferNum().intValue()==0||numInt==0) continue;
				int benNum=0;//本次分配量
				if(Math.abs(numInt)<=item.getTransferNum().intValue()){
					benNum=Math.abs(numInt);
				}else if(Math.abs(numInt)>item.getTransferNum().intValue()){
					benNum=Math.abs(item.getTransferNum().intValue());
				}
				if(numInt<0) benNum = benNum*-1;//红冲单数量为负
				numInt = numInt-benNum;//剩余未分摊量
				item.setTransferNum(item.getTransferNum()-benNum);

				//基础实体
				InventoryFallingPriceTemDTO tempDTO = new InventoryFallingPriceTemDTO();
				tempDTO.setInverntoryStatus(DERP_REPORT.INVERNTORYSTATUS_3);//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
				tempDTO.setDepotId((Long)paramMap.get("depotId"));
				tempDTO.setBuId((Long)paramMap.get("buId"));
				tempDTO.setGoodsId(item.getGoodsId());
				tempDTO.setBatchNo(item.getTransferBatchNo());
				tempDTO.setProductionDate(item.getProductionDate());
				tempDTO.setOverdueDate(item.getOverdueDate());
				tempDTO.setSurplusNum(Long.valueOf(benNum));
				tempDTO.setTallyingUnit(tallyingUnit);//理货单位 00 托盘 01箱  02 件
				tempDTO.setInverntoryType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型  0 正常品  1 残次品
				tempDTO.setNoshelfCode(saleOrderModel.getCode());
				tempList.add(tempDTO);
			}//end----------02

			//分摊完结束本在途量分摊
			if(numInt==0) continue;

			//未分摊完再分摊到坏品上
			for(SaleOutDepotItemModel item : saleLtemList){//start----------03
				if(item.getWornNum()==null) continue;
				if(item.getWornNum().intValue()==0||numInt==0) continue;
				int benNum=0;//本次分配量
				if(Math.abs(numInt)<=item.getWornNum().intValue()){
					benNum=Math.abs(numInt);
				}else if(Math.abs(numInt)>item.getWornNum().intValue()){
					benNum=Math.abs(item.getWornNum().intValue());
				}
				if(numInt<0) benNum = benNum*-1;//红冲单数量为负
				numInt = numInt-benNum;//剩余未分摊量
				item.setWornNum(item.getWornNum()-benNum);

				//基础实体
				InventoryFallingPriceTemDTO tempDTO = new InventoryFallingPriceTemDTO();
				tempDTO.setInverntoryStatus(DERP_REPORT.INVERNTORYSTATUS_3);//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
				tempDTO.setDepotId((Long)paramMap.get("depotId"));
				tempDTO.setBuId((Long)paramMap.get("buId"));
				tempDTO.setGoodsId(item.getGoodsId());
				tempDTO.setBatchNo(item.getTransferBatchNo());
				tempDTO.setProductionDate(item.getProductionDate());
				tempDTO.setOverdueDate(item.getOverdueDate());
				tempDTO.setSurplusNum(Long.valueOf(benNum));
				tempDTO.setTallyingUnit(tallyingUnit);//理货单位 00 托盘 01箱  02 件
				tempDTO.setInverntoryType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//库存类型  0 正常品  1 残次品
				tempDTO.setNoshelfCode(saleOrderModel.getCode());
				tempList.add(tempDTO);
			}//end----------03

		}//end----------01

		return tempList;
	}
	/**获取调拨在途明细
	 *  1.没有调拨入库单算在途
	 *  2.调拨入库时间大于当前月份算在途
	 * */
	private List<InventoryFallingPriceTemDTO> getTransferNoshelfList(Map<String,Object> paramMap) throws Exception{
		List<InventoryFallingPriceTemDTO> tempList =new ArrayList<>();//存储存货跌价基础实体

        //获取<=本月出库的出库量
		List<Map<String, Object>> transferOutList = transferOrderDao.getBuBusinessAddTransferNoshelfDetails(paramMap);
		if(transferOutList==null||transferOutList.size()<=0){
			logger.info("仓库|事业部出库数量为0,跳过");
			return tempList;
		}

		/**循环出库量查询入库单判断是否在途*/
		for (Map<String, Object> map : transferOutList) {//start------001
			Long orderId = (Long) map.get("order_id");//调拨订单id
			String orderCode = (String) map.get("order_code");//调拨单号
			Long inGoodsId = (Long) map.get("goods_id");//调入商品id
			String tallyingUnit = (String) map.get("tallying_unit");//理货单位
			String transferBatchNo = (String) map.get("transfer_batch_no");//批次号
			Date productionDate = (Date) map.get("production_date");//生产日期
			Date overdueDate = (Date) map.get("overdue_date");//失效日期
			Long transferNum = (Long) map.get("transfer_num");//正常数量
			Long wornNum = (Long) map.get("worn_num");//坏品数量

			/**根据调拨单id查询调拨入库单*/
			TransferInDepotModel transferInDepotQuery=new TransferInDepotModel();
			transferInDepotQuery.setTransferOrderId(orderId);
			List<TransferInDepotModel> transferInDepotList = transferInDepotDao.list(transferInDepotQuery);
			//过滤掉删除状态的入库单
			transferInDepotList = transferInDepotList.stream().filter(a -> !a.getStatus().equals(DERP.DEL_CODE_006)).collect(Collectors.toList());

			TransferInDepotModel inDepot = null;
			if(transferInDepotList!=null&&transferInDepotList.size()>0) inDepot = transferInDepotList.get(0);
			String month = (String)paramMap.get("month");//报表月份
			String transferMonth = "";//入库单月份
			if(inDepot!=null&&inDepot.getTransferDate()!=null) transferMonth = TimeUtils.formatMonth(inDepot.getTransferDate());

			/**1. 没有调拨入库单算在途
			 * 2. 调拨入库时间大于当前月份算在途
			 * */
			if(inDepot==null||inDepot.getTransferDate()==null||transferMonth.compareTo(month)>0){//start------002
				//基础实体 -好品量
				if(transferNum.intValue()>0){
					InventoryFallingPriceTemDTO tempDTO = new InventoryFallingPriceTemDTO();
					tempDTO.setInverntoryStatus(DERP_REPORT.INVERNTORYSTATUS_4);//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
					tempDTO.setDepotId((Long)paramMap.get("depotId"));
					tempDTO.setBuId((Long)paramMap.get("buId"));
					tempDTO.setGoodsId(inGoodsId);
					tempDTO.setBatchNo(transferBatchNo);
					tempDTO.setProductionDate(productionDate);
					tempDTO.setOverdueDate(overdueDate);
					tempDTO.setSurplusNum(Long.valueOf(transferNum));
					tempDTO.setTallyingUnit(tallyingUnit);//理货单位 00 托盘 01箱  02 件
					tempDTO.setInverntoryType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型  0 正常品  1 残次品
					tempDTO.setNoshelfCode(orderCode);
					tempList.add(tempDTO);
				}
				//基础实体 -坏品量
				if(wornNum.intValue()>0){
					InventoryFallingPriceTemDTO tempDTO = new InventoryFallingPriceTemDTO();
					tempDTO.setInverntoryStatus(DERP_REPORT.INVERNTORYSTATUS_4);//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
					tempDTO.setDepotId((Long)paramMap.get("depotId"));
					tempDTO.setBuId((Long)paramMap.get("buId"));
					tempDTO.setGoodsId(inGoodsId);
					tempDTO.setBatchNo(transferBatchNo);
					tempDTO.setProductionDate(productionDate);
					tempDTO.setOverdueDate(overdueDate);
					tempDTO.setSurplusNum(Long.valueOf(wornNum));
					tempDTO.setTallyingUnit(tallyingUnit);//理货单位 00 托盘 01箱  02 件
					tempDTO.setInverntoryType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//库存类型  0 正常品  1 残次品
					tempDTO.setNoshelfCode(orderCode);
					tempList.add(tempDTO);
				}
			}//end------002

		}//end------001
		return tempList;
	}
	/**获取存货跌价商品的商品基础数据
	 * merchandiseAllMap key=goodsId,value=商品id、货号、商品名称、标准条码,箱转件数量、托转件数量、标准品牌Id、标准品牌名称、二级分类id、二级分类名称
	 * */
	private String getMerchandiseAll(List<InventoryFallingPriceTemDTO> tempList,Map<String,Map<String,Object>> merchandiseAllMap) throws Exception{
		  String errorMsg = "";
		  if(tempList==null||tempList.size()<=0) return "";
		  for(InventoryFallingPriceTemDTO tempDto : tempList){
			  Map<String,Object> goodsMap = merchandiseAllMap.get(tempDto.getGoodsId().toString());
			  //已存在则跳过
			  if(goodsMap!=null) continue;

			  //获取商品信息
			  MerchandiseInfoModel tmpMerchandiseInfo = merchandiseInfoDao.searchById(tempDto.getGoodsId());
			  //获取标准条码信息
			  CommbarcodeModel commbarcodeTemp = new CommbarcodeModel();
			  commbarcodeTemp.setCommbarcode(tmpMerchandiseInfo.getCommbarcode());
			  CommbarcodeModel commbarcode = commbarcodeDao.searchByModel(commbarcodeTemp);

			  if(commbarcode==null) {
			  	errorMsg += " "+tempDto.getGoodsId();
			  	continue;
			  }

			  //商品id、货号、商品名称、标准条码,箱转件数量、托转件数量、标准品牌Id、标准品牌名称、二级分类id、二级分类名称
			  goodsMap = new HashMap<>();
			  goodsMap.put("goods_id",tmpMerchandiseInfo.getId());
			  goodsMap.put("goods_no",tmpMerchandiseInfo.getGoodsNo());
			  goodsMap.put("goods_name",tmpMerchandiseInfo.getName());
			  goodsMap.put("commbarcode",tmpMerchandiseInfo.getCommbarcode());
			  goodsMap.put("barcode",tmpMerchandiseInfo.getBarcode());
			  goodsMap.put("box_to_unit",tmpMerchandiseInfo.getBoxToUnit());
			  goodsMap.put("torr_to_unit",tmpMerchandiseInfo.getTorrToUnit());
			  goodsMap.put("comm_brand_parent_id",commbarcode.getCommBrandParentId());
			  goodsMap.put("comm_brand_parent_name",commbarcode.getCommBrandParentName());
			  goodsMap.put("comm_type_id",commbarcode.getCommTypeId());
			  goodsMap.put("comm_type_name",commbarcode.getCommTypeName());
			  merchandiseAllMap.put(tmpMerchandiseInfo.getId().toString(),goodsMap);
		  }

		  return errorMsg;
	}
	/**
	 * 保存存货跌价准备实体
	 */
	private void saveEntity(MerchantInfoModel merchant,DepotInfoModel depot,BusinessUnitModel buModel,String reportDate, String month,
							List<InventoryFallingPriceTemDTO> tempList,Map<String,Map<String,Object>> merchandiseAllMap) throws Exception {

		if(tempList==null||tempList.size()<=0) return ;
		for(InventoryFallingPriceTemDTO tempDto : tempList) {//start---------001

			Map goodsMap = merchandiseAllMap.get(tempDto.getGoodsId().toString());//获取商品
			InventoryFallingPriceReservesModel model = new InventoryFallingPriceReservesModel();
			model.setMerchantId(merchant.getId());
			model.setMerchantName(merchant.getName());
			model.setDepotId(depot.getId());
			model.setDepotName(depot.getName());
			model.setBuId(buModel.getId());
			model.setBuName(buModel.getName());
			model.setGoodsId(tempDto.getGoodsId());
			model.setGoodsNo((String)goodsMap.get("goods_no"));
			model.setGoodsName((String)goodsMap.get("goods_name"));
			model.setCommbarcode((String)goodsMap.get("commbarcode"));
			model.setBrandParentId((Long)goodsMap.get("comm_brand_parent_id"));//标准品牌
			model.setBrandParent((String)goodsMap.get("comm_brand_parent_name"));
			model.setTypeId((Long)goodsMap.get("comm_type_id"));//二级分类
			model.setTypeName((String)goodsMap.get("comm_type_name"));
			model.setBarcode((String)goodsMap.get("barcode"));
			model.setBatchNo(tempDto.getBatchNo());
			model.setInverntoryStatus(tempDto.getInverntoryStatus());
			model.setInverntoryType(tempDto.getInverntoryType());
			model.setProductionDate(tempDto.getProductionDate());
			model.setOverdueDate(tempDto.getOverdueDate());
			model.setFirstShelfDate(tempDto.getFirstShelfDate());
			model.setReportDate(TimeUtils.parseDay(reportDate));//报表日期
			model.setReportMonth(month);//报表月份
			model.setSurplusNum(tempDto.getSurplusNum());//总数量
			model.setNoshelfCode(tempDto.getNoshelfCode());
			/**计算在仓天数=进仓日期距当前日期的天数
			 * 在仓天数区间划分为：1: 0~30天;2: 30天~60天;3: 60天~90天; 4: 90天~120天;5: 120天以上'
			 * */
			if(model.getFirstShelfDate() != null) {
				int inWarehouseDays = TimeUtils.daysBetween(model.getFirstShelfDate(), TimeUtils.parseDay(reportDate));
				String inWarehouseInterval = "";
				if (inWarehouseDays < 30) {
					inWarehouseInterval = "1";
				} else if (inWarehouseDays < 60) {
					inWarehouseInterval = "2";
				} else if (inWarehouseDays < 90) {
					inWarehouseInterval = "3";
				} else if (inWarehouseDays < 120) {
					inWarehouseInterval = "4";
				} else if (inWarehouseDays >= 120) {
					inWarehouseInterval = "5";
				}
				model.setInWarehouseDays(inWarehouseDays);//在仓天数
				model.setInWarehouseInterval(inWarehouseInterval);//在仓天数区间
			}
			Long surplusDays =0L;//剩余效期天数
			BigDecimal surplusDaysBD = null;
			Long totalDays = 0L;//总效期天数
			BigDecimal totalDaysBD =null;
			BigDecimal surplusProportion = new BigDecimal(0.00);//剩余效期占比
			if(model.getProductionDate()!=null && model.getOverdueDate() != null) {
				model.setOverdueMonth(TimeUtils.formatMonth(model.getOverdueDate()));

				Date startDate = TimeUtils.parseDay(reportDate);
				surplusDays = Long.valueOf(TimeUtils.daysBetween(startDate,model.getOverdueDate()));
				totalDays = Long.valueOf(TimeUtils.daysBetween(model.getProductionDate(),model.getOverdueDate()));
				model.setSurplusDays(surplusDays);
				model.setTotalDays(totalDays);
				//剩余效期占比
				surplusDaysBD = new BigDecimal(surplusDays);
				totalDaysBD = new BigDecimal(totalDays);
				if(totalDays.intValue()>0) {
					surplusProportion = surplusDaysBD.divide(totalDaysBD, 4, BigDecimal.ROUND_HALF_UP);
					model.setSurplusProportion(surplusProportion);
				}
			}

			//剩余效期区间1:0<X≤1/10 ; 2: 1/10<X<≤1/5 ; 3: 1/5<X≤1/3 ; 4: 1/3<X≤1/2 ; 5: 1/2<X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品
			String effectiveInterval = null;
			//剩余效期占比(财务逻辑)1:1/10 ; 2: 1/5 ; 3: 1/3; 4: 1/2 ; 5: 1/2及以上 ; 7:过期品(为负) ; 8: 残次品
			String financialSurplusProportion = null;
			//跌价准备比例
			BigDecimal depreciationReserveProportion = null;
			/**（1）当剩余效期为负值时归为过期品；
			 * 剩余失效(天)等于0时或者剩余效期区间为过期品，归入过期品的区间
			 * 若既为坏品又为过期品则标识为过期品,跌价准备为100%；
			 */
			//订单表体的剩余效期区间（采购在途才有）
			String orderEffectiveInterval = tempDto.getEffectiveInterval()==null?"":tempDto.getEffectiveInterval();
			if((model.getProductionDate()!=null&&model.getOverdueDate()!=null&&surplusDays <= 0)
					||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7)) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_7;
				depreciationReserveProportion = new BigDecimal(1);
			}
			/**(2)当库存类型为坏品时，无论剩余效期为多少（除过期品外），均归为残次品,跌价准备为100%*/
			else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(model.getInverntoryType())) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_8;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_8;
				depreciationReserveProportion = new BigDecimal(1);
			}
			/**(3)当剩余失效占比>=0.00且<= 0.10，剩余失效(天)大于0时，或剩余效期占比在0~1/10 ，归入0~1/10的区间(0.00~0.10)*/
			else if((surplusProportion.doubleValue() >= 0.00 && surplusProportion.doubleValue() <= 0.10 && surplusDays > 0)
			        ||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1)) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_1;
			}
			/**(4)1/10~1/5区间(0.10~0.20)*/
			else if((surplusProportion.doubleValue() > new BigDecimal((double) 1 / 10).doubleValue()
					&& surplusProportion.doubleValue() <= new BigDecimal((double) 1 / 5).doubleValue())
			        ||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2)) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_2;
			}
			/**(5)1/5~1/3区间(0.20~0.33)*/
			else if ((surplusProportion.doubleValue() > new BigDecimal((double) 1 / 5).doubleValue()
					  &&surplusProportion.doubleValue() <= new BigDecimal((double) 1 / 3).doubleValue())
			         ||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3)) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_3;
			}
			/**(6)1/3~1/2区间(0.33~0.50)*/
			else if((surplusProportion.doubleValue() > new BigDecimal((double) 1 / 3).doubleValue()
					&&surplusProportion.doubleValue() <= new BigDecimal((double) 1 / 2).doubleValue())
			        ||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4)) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_4;
			}
			/**(7)1/2~2/3区间(0.50~0.60)*/
			else if((surplusProportion.doubleValue() > new BigDecimal((double) 1 / 2).doubleValue()
					&&surplusProportion.doubleValue() <= new BigDecimal((double) 2 / 3).doubleValue())
			        ||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_5)) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_5;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_5;
			}
			/**(8)2/3以上区间0.6~*/
			else if (surplusProportion.doubleValue() > new BigDecimal((double) 2 / 3).doubleValue()
					||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_6)) {
				effectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_6;
				financialSurplusProportion = DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_5;
			}
			model.setEffectiveInterval(effectiveInterval);
			model.setFinancialSurplusProportion(financialSurplusProportion);

			/**若跌价准备比例为空，按区间设值*/
			if (depreciationReserveProportion == null) {
				/**（1）当效期区间为1≤X＜10 以下(0.00~0.10)、或剩余效期（天）小于等于40天（不管什么效期区间）时，跌价准备为100%；*/
				if((model.getOverdueDate()!=null && surplusDays <= 40)
				   ||DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1.equals(model.getEffectiveInterval())) {
					depreciationReserveProportion = new BigDecimal(1);
				}
				/**（2）当效期区间为1/10≤X＜1/5时(0.10~0.20)（且剩余效期（天）大于40天），跌价准备为95%；
				 * 		若当前月份大于等于‘2021-02’ 跌价准备为70%；
				 */
				else if ((DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2.equals(model.getEffectiveInterval()) && surplusDays > 40)
				         ||(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2.equals(model.getEffectiveInterval())&&tempDto.getInverntoryStatus().equals(DERP_REPORT.INVERNTORYSTATUS_2))) {
					depreciationReserveProportion = new BigDecimal(0.95);
					if (month.compareTo("2021-02") >= 0) {
						depreciationReserveProportion = new BigDecimal(0.70);
					}
				}
				/**（3）当效期区间为1/5＜X≤1/3时(0.20~0.33)（且剩余效期（天）大于40天），跌价准备为70%；
				 * 		若当前月份大于等于‘2021-02’ 跌价准备为30%；
				 */
				else if ((DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3.equals(model.getEffectiveInterval()) && surplusDays > 40)
						||(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3.equals(model.getEffectiveInterval())&&tempDto.getInverntoryStatus().equals(DERP_REPORT.INVERNTORYSTATUS_2))) {
					depreciationReserveProportion = new BigDecimal(0.70);
					if (month.compareTo("2021-02") >= 0) {
						depreciationReserveProportion = new BigDecimal(0.30);
					}
				}
				/**（4）当效期区间为1/3≤X＜1/2时(0.33~0.50)（且剩余效期（天）大于40天），跌价准备为30%；
				 * 	若当前月份大于等于‘2021-02’ 跌价准备为10%；
				 */
				else if ((DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4.equals(model.getEffectiveInterval()) && surplusDays > 40)
						||(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4.equals(model.getEffectiveInterval())&&tempDto.getInverntoryStatus().equals(DERP_REPORT.INVERNTORYSTATUS_2))) {
					depreciationReserveProportion = new BigDecimal(0.30);
					if (month.compareTo("2021-02") >= 0) {
						depreciationReserveProportion = new BigDecimal(0.10);
					}
				}
				/**（5）当效期区间为2/3以上(0.67~)、1/2≤X<2/3、时，跌价准备为0%；*/
				else {
					depreciationReserveProportion = new BigDecimal(0.00);
				}

			}
			model.setDepreciationReserveProportion(depreciationReserveProportion);

			/**1.采购在途，单价=则取采购订单的币种转换为公司维护的本位币币种的月平均汇率*单价，作为存货跌价的单价
			 * 2.其他的根据公司配置：标准成本单价、期末加权取
			 * */
			BigDecimal price = null;
			if(model.getInverntoryStatus().equals(DERP_REPORT.INVERNTORYSTATUS_2)) {
				Double rate = null;//月平均汇率
				if(StringUtils.isNotBlank(tempDto.getCurrency())&&StringUtils.isNotBlank(merchant.getAccountCurrency())
				   &&tempDto.getCurrency().equals(merchant.getAccountCurrency())){
					rate = 1.00 ;
				}else if(StringUtils.isNotBlank(tempDto.getCurrency())&&StringUtils.isNotBlank(merchant.getAccountCurrency())){
					String effectiveDate = TimeUtils.getLastDayOfMonth(month);
					Map<String, Object> queryRateMap = new HashMap<>() ;
					queryRateMap.put("origCurrencyCode", tempDto.getCurrency()) ;//原币
					queryRateMap.put("tgtCurrencyCode", merchant.getAccountCurrency()) ;//目标币种
					queryRateMap.put("effectiveDate", effectiveDate);
					queryRateMap.put("status", "1") ;
					ExchangeRateMongo rateMongo = exchangeRateMongoDao.findLastRateByParams(queryRateMap);
					if(rateMongo!= null)  rate = rateMongo.getAvgRate();
				}
                if(rate!=null) {
					price = new BigDecimal(rate).multiply(tempDto.getPrice()).setScale(5, BigDecimal.ROUND_HALF_UP);
				}
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(merchant.getAccountPrice())) {
				WeightedPriceModel queryModel = new WeightedPriceModel();
				queryModel.setMerchantId(merchant.getId());
				queryModel.setEffectiveMonth(model.getReportMonth());
				queryModel.setCommbarcode(model.getCommbarcode());
				queryModel.setBuId(model.getBuId());
				queryModel = weightedPriceDao.searchByModel(queryModel);
				if(queryModel!=null) price = queryModel.getPrice();
			} else {
				price = settlementPriceService.getSettlementPrice(merchant, model.getGoodsId(), depot.getId(), month, buModel.getId());
			}
			model.setSettlementPrice(price);

			//计提总额=单价*数量*跌价准备比例
			if (price != null) {
				BigDecimal totalProvision = price.multiply(new BigDecimal(model.getSurplusNum())).multiply(depreciationReserveProportion).setScale(4, BigDecimal.ROUND_HALF_UP);
				model.setTotalProvision(totalProvision);
			}
			//总金额=单价*数量
			if (price != null) {
				BigDecimal totalAmount = price.multiply(new BigDecimal(model.getSurplusNum())).setScale(4, BigDecimal.ROUND_HALF_UP);
				model.setTotalAmount(totalAmount);
			}

			//2个月后剩余效期占比
			Calendar cal = Calendar.getInstance();
			cal.setTime(TimeUtils.parseDay(reportDate));
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 2);
			Date twoMonthsDate = cal.getTime();

			int twoMonthsSurplusDays = 0;//两个月后剩余失效天数
			if (model.getProductionDate()!=null&&model.getOverdueDate() != null) {
				twoMonthsSurplusDays = TimeUtils.daysBetween(twoMonthsDate, model.getOverdueDate());
			}

			BigDecimal twoMonthsSurplusProportion = new BigDecimal(0.00);//两个月后剩余效期占比
			if (totalDaysBD!=null && totalDaysBD.doubleValue() >0.00) {
				twoMonthsSurplusProportion = new BigDecimal(twoMonthsSurplusDays).divide(totalDaysBD, 4, BigDecimal.ROUND_HALF_UP);
				model.setTwoMonthsSurplusProportion(twoMonthsSurplusProportion);
			}

			//2个月后效期区间
			String twoMonthsEffectiveInterval = null;
			/**（1）当剩余效期为负值时归为过期品；
			 * 当剩余失效占比=0.00，且剩余失效(天)等于0时，归入过期品的区间
			 * 若既为坏品又为过期品则标识为过期品,跌价准备为100%；
			 */
			if ((model.getProductionDate()!=null&&model.getOverdueDate()!=null&&twoMonthsSurplusDays <=0)
					||orderEffectiveInterval.equals(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7)) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7;
			}
			/**(2)当库存类型为坏品时，无论剩余效期为多少（除过期品外），均归为残次品,跌价准备为100%*/
			else if (DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(model.getInverntoryType())) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_8;
			}
			/**(3)当剩余失效占比=0.00，剩余失效(天)大于0时，或剩余效期占比在0~1/10 ，归入0~1/10的区间(0.00~0.10)*/
			else if (twoMonthsSurplusProportion.doubleValue() >= 0.00 && twoMonthsSurplusProportion.doubleValue() <= 0.10&& twoMonthsSurplusDays > 0) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1;
			}
			/**(4)1/10~1/5区间(0.10~0.20)*/
			else if (twoMonthsSurplusProportion.doubleValue() > new BigDecimal((double) 1 / 10).doubleValue() &&
					twoMonthsSurplusProportion.doubleValue() <= new BigDecimal((double) 1 / 5).doubleValue()) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2;
			}
			/**(5)1/5~1/3区间(0.20~0.33)*/
			else if (twoMonthsSurplusProportion.doubleValue() > new BigDecimal((double) 1 / 5).doubleValue() &&
					twoMonthsSurplusProportion.doubleValue() <= new BigDecimal((double) 1 / 3).doubleValue()) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3;
			}
			/**(6)1/3~1/2区间(0.33~0.50)*/
			else if (twoMonthsSurplusProportion.doubleValue() > new BigDecimal((double) 1 / 3).doubleValue() &&
					twoMonthsSurplusProportion.doubleValue() <= new BigDecimal((double) 1 / 2).doubleValue()) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4;
			}
			/**(7)1/2~2/3区间(0.50~0.67)*/
			else if (twoMonthsSurplusProportion.doubleValue() > new BigDecimal((double) 1 / 2).doubleValue() &&
					twoMonthsSurplusProportion.doubleValue() <= new BigDecimal((double) 2 / 3).doubleValue()) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_5;
			}
			/**(8)2/3以上区间(0.67~)*/
			else if (twoMonthsSurplusProportion.doubleValue() > new BigDecimal((double) 2 / 3).doubleValue()) {
				twoMonthsEffectiveInterval = DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_6;
			}
			model.setTwoMonthsEffectiveInterval(twoMonthsEffectiveInterval);
			model.setCreateDate(TimeUtils.getNow());//创建时间

			inventoryFallingPriceReservesDao.save(model) ;//保存
		}//end---------001

	}
	/**
	 * 箱托转换
	 */
	private String changeUnit(List<InventoryFallingPriceTemDTO> temList,Map<String,Map<String,Object>> merchandiseAllMap) throws RuntimeException{
		String errorMsg = "";
		if(temList==null||temList.size()<=0) return errorMsg;
		for(InventoryFallingPriceTemDTO tempDto : temList){

			Map goodsMap = merchandiseAllMap.get(tempDto.getGoodsId().toString());
			Integer boxToUnit = (Integer)goodsMap.get("box_to_unit");//箱转件数量
			Integer torrToUnit = (Integer)goodsMap.get("torr_to_unit");//托转件数量

			Long numLong=0L;
			//转换单位为件后返回
			if(StringUtils.isEmpty(tempDto.getTallyingUnit()) ||tempDto.getTallyingUnit().equals(DERP.INVENTORY_UNIT_2)
					||tempDto.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)){
				numLong=tempDto.getSurplusNum().longValue();
			}else if(tempDto.getTallyingUnit().equals(DERP.INVENTORY_UNIT_1)||tempDto.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)){
				if(boxToUnit==null||boxToUnit.intValue()<=0){
					errorMsg +=" "+tempDto.getGoodsId();
					continue;
				}
				numLong=tempDto.getSurplusNum().longValue()*boxToUnit.intValue();
			}else if(tempDto.getTallyingUnit().equals(DERP.INVENTORY_UNIT_0)||tempDto.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)){
				if(torrToUnit==null||torrToUnit.intValue()<=0){
					errorMsg +=" "+tempDto.getGoodsId();
					continue;
				}
				numLong=tempDto.getSurplusNum().longValue()*torrToUnit.intValue();
			}
			tempDto.setSurplusNum(numLong);
		}
		return errorMsg;
	}


}
