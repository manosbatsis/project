package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.BillOutinDepotDao;
import com.topideal.dao.sale.BillOutinDepotItemDao;
import com.topideal.dao.sale.CrawlerBillDao;
import com.topideal.dao.sale.CrawlerOutTempDao;
import com.topideal.dao.sale.MerchandiseContrastDao;
import com.topideal.dao.sale.PojzTempDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.dao.sale.SaleShelfIdepotItemDao;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;
import com.topideal.entity.vo.sale.BillOutinDepotModel;
import com.topideal.entity.vo.sale.CrawlerBillModel;
import com.topideal.entity.vo.sale.CrawlerOutTempModel;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.CrawlerBillMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.VipCreateSaleOutDepotService;

import net.sf.json.JSONObject;

/**
 * 唯品 生成销售出库单
 */
@Service
public class VipCreateSaleOutDepotServiceImpl implements VipCreateSaleOutDepotService {
	private static final Logger logger = LoggerFactory.getLogger(VipCreateSaleOutDepotServiceImpl.class);
	// 爬虫账单
	@Autowired
	private CrawlerBillDao crawlerBillDao;
	// 爬虫生成销售出库单临时表
	@Autowired
	private CrawlerOutTempDao crawlerOutTempDao;
	// 商品对照表
	@Autowired
	private MerchandiseContrastDao merchandiseContrastDao;
	// 上架入库单
	@Autowired
	private SaleShelfIdepotItemDao shelfIdepotItemDao;
	//po历史结转临时表
	@Autowired
	private PojzTempDao pojzTempDao;
	//账单出入库单表
	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao;
	//账单出入库单头
	@Autowired
	private BillOutinDepotDao billOutinDepotDao;
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;
	@Autowired
	private TransferOrderDao transferOrderDao;
	
	// 商品
	@Autowired
	MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 商家
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private ReptileConfigMongoDao reptileConfigMongoDao;
	// MQ 业务
	@Autowired
	private RMQProducer mqProducer;
	// 日志MQ
	@Autowired
	private RMQLogProducer rocketMQProducer;

	/**==================================出库start========================================*/
	/**
	 * 4.0唯品账单生成出库单
	 * */
	// @SystemServiceLog(point="13201141100",model="唯品的账单数据生成销售出库清单")
	public List<InvetAddOrSubtractRootJson> saveVIPSaleOutDepot(Long merchantId,String billCode,List<Long> idList) throws Exception {
		// 查询商家
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", merchantId);// 商家id
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
		
		/**公共查询条件*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId", merchantId);
		paramMap.put("billCode", billCode);
		paramMap.put("idList", idList);
		
		/**3.查询本商家+账单号未使用过的账单明细按sku去重*/
		List<String> skuList = crawlerBillDao.searchSkuList(paramMap);
		/**4.查询爬虫对照表获取sku对应的经分销货号存储到map,sku=list<MerchandiseContrastModel>*/
		Map<String,List<MerchandiseContrastDTO>> skuGoodsNoMap = new HashMap<String, List<MerchandiseContrastDTO>>();
		skuGoodsNoMap = getSkuGoodsNo(merchant, billCode, skuList);
		if(skuGoodsNoMap.isEmpty()) {
			logger.info("唯品-账单号:"+billCode+"获取商品对照表货号为空,结束本账单号运行");
			return null;
		} 
		/**对应经分销货号转换成list*/
		List<String> goodsNoList = getSkuGoodsNoList(skuGoodsNoMap);
			
		// 获取库存jdbc链接用于查库存余量
		Connection conn = getConnection();
		if (conn == null) {
			String msg = "唯品-账单号:"+billCode+"获取库存链接失败,结束本账单号运行";
			logger.info(msg);
			sendCrawlerErrorLog(merchant, billCode, "", "", 0, msg);
			throw new RuntimeException(msg);
		}
		/**获取唯品爬虫配置存储到map key=userCode*/
		Map<String, Object> reptileAllMap = new HashMap<String, Object>();
		Map<String, Object> reptileMap = new HashMap<String, Object>();
		reptileMap.put("platformType", DERP.CRAWLER_TYPE_2);
		reptileMap.put("merchantId",merchant.getMerchantId());
		List<ReptileConfigMongo> reptileList = reptileConfigMongoDao.findAll(reptileMap);
		if(reptileList!=null&&reptileList.size()>0) {
			for(ReptileConfigMongo entity:reptileList) {
				reptileAllMap.put(entity.getLoginName(), entity);
			}
		}
		/**5.查询本商家+账单号正数未使用过的账单明细*/
		paramMap.put("processingType","ck");//出库 取正数
		List<Map<String, Object>> billListAll = crawlerBillDao.getBillList(paramMap);
		if(billListAll==null||billListAll.size()<=0) {
			logger.info("唯品-账单号:"+billCode+"正数未使用过的账单明细数量为0,结束本账单号运行");
			return null;
		}
		Map<String, Object> oneBillMap = billListAll.get(0);
		String userCode = (String)oneBillMap.get("user_code");
		ReptileConfigMongo reptileMongo = (ReptileConfigMongo) reptileAllMap.get(userCode);
		if(reptileMongo==null) {
			String msg="唯品-商家:"+merchant.getName()+",账单号:"+billCode+"未找到爬虫配置结束本账单号运行";
			logger.info(msg);
			sendCrawlerErrorLog(merchant,billCode,"","",0,msg);
			return null;
		}
		// 查询仓库
		Map<String, Object> depotMap = new HashMap<String, Object>();
		depotMap.put("depotId", reptileMongo.getOutDepotId());// 唯品仓库自编码
		DepotInfoMongo vipDepot = depotInfoMongoDao.findOne(depotMap);
		if(vipDepot==null) {
			String msg="唯品-商家:"+merchant.getName()+",账单号:"+billCode+"未找到仓库结束本账单号运行";
			logger.info(msg);
			sendCrawlerErrorLog(merchant,billCode,"","",0,msg);
			return null;
		}		
		/**6.同步这些货号的库存余量到临时分配表，若临时表已存在则跳过*/
		saveGoodsInventory(conn, merchant, vipDepot, goodsNoList);
		
		
		/**7.循环sku对应经分销货号获取本商家+po+货号的可核销量存储到临时表*/
		saveVerifiTemp(vipDepot, billListAll, skuGoodsNoMap);
		
		/**8.循环分配账单*/
		allotNumVIP(billListAll, skuGoodsNoMap, merchant, vipDepot);
		
		/**9.查询本商家+账单号已分配好的临时分配明细按处理类型归类+币种分组统计生成出库单、更新爬虫账单为已使用状态、生成库存扣减报文*/
		String crType = "out";//out-出库 in-入库 
		List<InvetAddOrSubtractRootJson> rootJsonList = saveBillOutinDepot(crType,billCode, merchant, vipDepot,reptileMongo,skuGoodsNoMap);
		
		return rootJsonList;	
	}

	/**
	 * 5.同步这些货号的库存余量到临时分配表，若临时表已存在则跳过
	 */
	public void saveGoodsInventory(Connection conn, MerchantInfoMongo merchant, DepotInfoMongo vipDepot,
			List<String> goodsNoList) throws Exception {
		// 遍历货号
		for(String goodsNo : goodsNoList) {
			CrawlerOutTempModel outTemp = new CrawlerOutTempModel();
			outTemp.setSourceType("2");// 数据来源 1-云集 2-唯品
			outTemp.setDataType("1");//数据类型 1-库存量 2-可核销量 3-出入库分配明细
			outTemp.setMerchantId(merchant.getMerchantId());
			outTemp.setDepotId(vipDepot.getDepotId());
			outTemp.setGoodsNo(goodsNo);
			try {
				CrawlerOutTempModel tempModel = crawlerOutTempDao.searchByModel(outTemp);
				if(tempModel!=null) continue;
				// 查询库存余量
				Integer surplusNum = getGoodsSurplusNum(conn, merchant.getMerchantId(), vipDepot.getDepotId(), goodsNo);
				if (surplusNum.intValue() > 0) {
					// 保存货号库存余量到临时表
					outTemp.setSurplusNum(surplusNum);
					crawlerOutTempDao.save(outTemp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				String msg = "爬虫出库-保存库存余量失败，货号" + goodsNo;
				logger.info(msg);
				sendCrawlerErrorLog(merchant,"","","",0,msg);
				throw new RuntimeException("爬虫出库-保存库存余量失败，货号" + goodsNo);
			}
		}
	
	}
	/**7.循环sku对应经分销货号获取本商家+po+货号的可核销量存储到临时表
	 * @throws SQLException */
	public void saveVerifiTemp(DepotInfoMongo vipDepot,List<Map<String, Object>> billListAll,Map<String,List<MerchandiseContrastDTO>> skuGoodsNoMap) throws SQLException {
		for(Map<String, Object> billMap:billListAll) {
			Long merchantId = (Long)billMap.get("merchant_id");
			String poNo = (String)billMap.get("po_no");
			String sku = (String)billMap.get("sku");
			List<String> goodsNoList = getSkuGoodsNoList(sku, skuGoodsNoMap);
			if(goodsNoList==null||goodsNoList.size()<=0) continue;
			for(String goodsNo:goodsNoList) {
				//获取商家+po+货号的可核销量，临时表已存在则跳过
				CrawlerOutTempModel temp = new CrawlerOutTempModel();
				temp.setSourceType("2");//数据来源 1-云集 2-唯品
				temp.setDataType("2");//数据类型 1-库存量 2-可核销量 3-出入库分配明细
				temp.setMerchantId(merchantId);
				temp.setDepotId(vipDepot.getDepotId());
				temp.setPoNo(poNo);
				temp.setGoodsNo(goodsNo);
				CrawlerOutTempModel modeltemp = crawlerOutTempDao.searchByModel(temp);
				if(modeltemp!=null) continue;
				//获取可核销量
				List<String> goodsNoOneList = new ArrayList<String>();
				goodsNoOneList.add(goodsNo);
				Integer verifiNum = getVerifiNum(merchantId, vipDepot, poNo,goodsNoOneList);
				temp.setVerifiNum(verifiNum);
				crawlerOutTempDao.save(temp);
			}
		}
	}
	/**
	 * 可核销量=上架入库量+临时结转量+账单入库量+调拨入量-账单出库量-调拨出量-销售退货量
	 */
	public Integer getVerifiNum(Long merchantId, DepotInfoMongo vipDepot, String poNo,List<String> goodsNoList){
				
        /**公共查询条件*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("depotId",vipDepot.getDepotId());
		paramMap.put("poNo",poNo);
		paramMap.put("goodsNoList",goodsNoList);

		//1.上架入库量			
		Integer shelfInNum = shelfIdepotItemDao.getshelfInNum(paramMap);
		if (shelfInNum == null) shelfInNum = 0;
		
		//2.临时结转量
		Integer jztemNum = pojzTempDao.getPojzNum(paramMap);
		if (jztemNum == null) jztemNum = 0;
		
		//3.账单入库量
		paramMap.put("operateType",DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//库存调整类型  0 调减 1调增
		Integer billInNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
		if(billInNum ==null) billInNum = 0;
		
		//4.账单出库量
		paramMap.put("operateType",DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//库存调整类型  0 调减 1调增
		Integer billOutNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
		if(billOutNum ==null) billOutNum = 0;
		//唯品暂存区
		Map<String,Object> depotParam = new HashMap<>();
		depotParam.put("depotCode", "WPTH001");
		DepotInfoMongo wzcDepot = depotInfoMongoDao.findOne(depotParam);
		
		//5.调拨入量=调拨单调出仓库为“唯品暂存区”，且调入仓库为“唯品备查库” 已入库的入库数量
		paramMap.put("outDepotId", wzcDepot.getDepotId());
		paramMap.put("inDepotId", vipDepot.getDepotId());
		Integer transferInNum = transferOrderDao.getTransferInNumByMap(paramMap);
		if(transferInNum==null) transferInNum=0;
		
		//6.调拨出量=调拨单调出仓库为“唯品备查库”，调入仓库为“唯品暂存区” 已出库的数量
		paramMap.put("outDepotId", vipDepot.getDepotId());
		paramMap.put("inDepotId", wzcDepot.getDepotId());
		Integer transferOutNum = transferOrderDao.getTransferOutNumByMap(paramMap);
		if(transferOutNum==null) transferOutNum=0;

		//7.销售退货数量
		Integer saleRetrunNum = saleReturnOrderDao.getReturnCount(paramMap); // 退货量(好品+坏品)
		if (saleRetrunNum == null) saleRetrunNum = 0;
		
		//可核销量=上架入库量+临时结转量+账单入库量+调拨入量-账单出库量-调拨出量-销售退货量
		Integer verifiNum = shelfInNum+jztemNum+billInNum+transferInNum-billOutNum-transferOutNum-saleRetrunNum;
		return verifiNum;
	}
	/**
	 * 8、循环分配账单
	 */
	public void allotNumVIP(List<Map<String, Object>> billList, Map<String, List<MerchandiseContrastDTO>> skuGoodsNoMap,
			MerchantInfoMongo merchant, DepotInfoMongo depot) throws Exception {
		/**8.循环分配账单 
		 * 	8.1循环账单，sku对应的货号,无对应货号则跳过
		 *  8.2检查可核销量、库存量是否足够，不够则提示日志，跳过
		 *  8.3分配
		 */
        for(Map<String,Object> billMap :billList) {
        	String billCode = (String)billMap.get("bill_code");
        	String poNo = (String)billMap.get("po_no");
        	String sku = (String)billMap.get("sku");
        	Long totalSalesQtyLong = (Long)billMap.get("total_sales_qty");
        	Integer totalSalesQty = totalSalesQtyLong.intValue();
        	
        	/**获取sku对应经分销的商品对照*/
        	List<MerchandiseContrastDTO> contrastList = skuGoodsNoMap.get(sku);

        	/**8.1循环的账单，循环sku对应的货号,无对应货号则跳过*/
        	if(contrastList==null ||contrastList.size()<=0) continue;
        	
        	/**8.2循环检查每个货号的可核销量、库存量是否足够，不够则提示日志，跳过，非组合品一个sku只对应经销的一个货号，组合品一个sku对应经销多个货号*/
        	boolean flag = true;//可核销量、库存量检查是否通过  true-通过 false-不通过
        	for(MerchandiseContrastDTO contrastModel:contrastList){
				Map<String, Object> queryMap = new HashMap<String, Object>();
				queryMap.put("sourceType", "2");//数据来源 1-云集 2-唯品
				queryMap.put("merchantId", merchant.getMerchantId());
				queryMap.put("depotId", depot.getDepotId());
				queryMap.put("poNo", poNo);
				queryMap.put("goodsNo", contrastModel.getGoodsNo());
				Integer outSalesQtyNum = totalSalesQty*contrastModel.getNum();//账单销量*映射数量=要出库的数量
				Integer verifiSumNum = crawlerOutTempDao.getVerifiSumNum(queryMap);
				if(verifiSumNum==null||outSalesQtyNum.intValue()>verifiSumNum.intValue()) {
					String msg="唯品-账单号:"+billCode+",po号:"+poNo+",sku:"+sku+"货号:"+contrastModel.getGoodsNo() +"可核销量不足,账单量:"+totalSalesQty+",可核销量:"+verifiSumNum;
					logger.info(msg);
					sendCrawlerErrorLog(merchant, billCode, poNo, sku, totalSalesQty, msg);
					flag = false;
					break;
				}
				Integer surplusNum = crawlerOutTempDao.getSurplusNum(queryMap);
				if(surplusNum==null||outSalesQtyNum.intValue()>surplusNum.intValue()) {
					String msg="唯品-账单号:"+billCode+",po号:"+poNo+",sku:"+sku+"货号:"+contrastModel.getGoodsNo() +"库存量不足,账单量:"+totalSalesQty+",库存量:"+surplusNum;
					logger.info(msg);
					sendCrawlerErrorLog(merchant, billCode, poNo, sku, totalSalesQty, msg);
					flag = false;
					break;
				}
			}
        	if(flag == false) continue;//可核销量、库存量检查不通过跳过结束本条账单明细出库

        	/**8.3分配*/
        	allotBill(billMap, contrastList, merchant, depot);
        }
	}
	/**分配
	 * 8.3账单循环分配到sku对应的货号上
	 * 	  8.3.1保存分配明细到临时表类型为出入库分配明细,减掉库存余量,减掉可核销数量更新到临时表。
	 * */
	public void allotBill(Map<String, Object> billMap, List<MerchandiseContrastDTO> contrastList ,
			MerchantInfoMongo merchant, DepotInfoMongo depot) throws Exception {
		Long customerId = (Long)billMap.get("customer_id");
		Long billId = (Long)billMap.get("id");
		String billCode = (String)billMap.get("bill_code");
    	String poNo = (String)billMap.get("po_no");
    	String sku = (String)billMap.get("sku");
    	String processingType = (String)billMap.get("processing_type");
    	String currencyCode = (String)billMap.get("currency_code");
    	String saleOrderCode = (String)billMap.get("sale_order_code");//平台销售单号
    	Long totalSalesQtyLong = (Long)billMap.get("total_sales_qty");
    	Integer totalSalesQty = totalSalesQtyLong.intValue();
    	BigDecimal billPrice = (BigDecimal)billMap.get("bill_price");//单价
    	BigDecimal billAmount = (BigDecimal)billMap.get("bill_amount");//金额

		/**处理类型归类：
		 * 销售出库：Sales(销售)、Allocation(Reverse)(调拨-反向)、空处理类型、库存买断#正数--取正数
		 * 国检出库：Inspection inventory output(国检出库)---------------------------------取正数
		 * 盘亏出库：Inventory shortage(库存盘亏)------------------------------------------取正数
		 * 报废：Scrap(报废)--------------------------------------------------------------取正数
		 */
		String processingTypeErp = "";
		if(processingType.equals("Sales(销售)")||processingType.equals("Allocation(Reverse)(调拨-反向)")
				||processingType.equals("库存买断")||processingType.isEmpty()){
			processingTypeErp = DERP_ORDER.PROCESSINGTYPE_XSC;//销售出库
		}else if(processingType.equals("Inspection inventory output(国检出库)")){
			processingTypeErp = DERP_ORDER.PROCESSINGTYPE_GJC;//国检出库
		}else if(processingType.equals("Inventory shortage(库存盘亏)")){
			processingTypeErp = DERP_ORDER.PROCESSINGTYPE_PKC;//盘亏出库
		}else if(processingType.equals("Scrap(报废)")){
			processingTypeErp = DERP_ORDER.PROCESSINGTYPE_BFC;//报废
		}
		if(StringUtils.isEmpty(processingTypeErp)) {
			//发日志
			String msg="唯品-账单号:"+billCode+",po号:"+poNo+",sku:"+sku+"账单id:"+billId+"处理类型不正确未匹配到归类:"+processingType;
			logger.info(msg);
			sendCrawlerErrorLog(merchant, billCode, poNo, sku, totalSalesQty, msg);
			return ;
		}

		BigDecimal amountTotal = new BigDecimal(0.00);//对应多个货号时用来累计前N个商品已使用的金额
		/**8.3账单循环分配到sku对应的货号上*/
		for(int i=0;i<contrastList.size();i++) {
			MerchandiseContrastDTO contrastDTO = contrastList.get(i);
			/**获取当前货号可核销量、库存余量*/
			Map<String, Object> queryMap = new HashMap<String, Object>();
        	queryMap.put("sourceType", "2");//数据来源 1-云集 2-唯品
        	queryMap.put("merchantId", merchant.getMerchantId());
        	queryMap.put("depotId", depot.getDepotId());
        	queryMap.put("poNo", poNo);
        	queryMap.put("goodsNo", contrastDTO.getGoodsNo());//单个货号
			Integer verifiSumNum = crawlerOutTempDao.getVerifiSumNum(queryMap);
			Integer surplusNum = crawlerOutTempDao.getSurplusNum(queryMap);
			//出库量数量=账单销量*映射表配置的数量
			Integer outsalesQty = totalSalesQty*contrastDTO.getNum();

			/**8.3.1保存分配明细到临时表类型为出入库分配明细,减掉库存余量,减掉可核销数量更新到临时表*/
			// 减掉库存余量、可核销量更新到临时表
			Map<String, Object> subMap = new HashMap<String, Object>();
			subMap.put("sourceType", "2");// 数据来源 1-云集 2-唯品
			subMap.put("merchantId", merchant.getMerchantId());// 商家id
			subMap.put("depotId", depot.getDepotId());// 仓库id
			subMap.put("poNo", poNo);//货号
			subMap.put("goodsNo", contrastDTO.getGoodsNo());//货号
			subMap.put("lowerNum", outsalesQty);// 扣减数量
			crawlerOutTempDao.updateLowerNum(subMap);
			crawlerOutTempDao.updateLowerVerifiNum(subMap);
			
			/**单价\金额计算
			 *1.若对应经销货号只有一个:
			 *  金额=账单金额
			 *  单价=若账单有单价则直接取，若无单价则用金额/数量
			 *2.若组合品时对应经销多个货号：
			 *  金额=（本商品销售单价*数量/对应的多个货号的销售单价*数量总和)*账单结算金额
			 *  单品单价=金额/(映射表数量*账单销量)
			 *最后一个商品的金额/单价:
			 *  金额=账单结算金额-前面已使用的金额
			 *  单品单价=金额/(映射表数量*账单销量)
			 * */
			BigDecimal price = new BigDecimal(0.00);
			BigDecimal amount = new BigDecimal(0.00);
			//1.若对应经销货号只有一个:
			if(contrastList.size()==1){
				amount = billAmount;
				price = billPrice;//单价默认取账单的单价
				if(price==null||price.doubleValue()<=0){
					price = billAmount.divide(new BigDecimal(totalSalesQty),2, BigDecimal.ROUND_HALF_UP);//四合五入保留2位小数;
				}
			}else{
				BigDecimal goodsAmountTotal = new BigDecimal(0.00);
				for (MerchandiseContrastDTO contrastTemp : contrastList) {
					goodsAmountTotal = goodsAmountTotal.add(contrastTemp.getPrice().multiply(new BigDecimal(contrastTemp.getNum())));
				}
				//金额=（本商品销售单价*数量/对应的多个货号的销售单价*数量总和)*账单结算金额
				amount = contrastDTO.getPrice().multiply(new BigDecimal(contrastDTO.getNum())).divide(goodsAmountTotal, 2, BigDecimal.ROUND_HALF_UP).multiply(billAmount);
				amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位
				//最后一个商品
				if(i+1==contrastList.size()){
					//金额=账单结算金额-前面已使用的金额
					amount = billAmount.subtract(amountTotal);
				}
				//单品单价=金额/(映射表数量*账单销量)
				price = amount.divide(new BigDecimal(outsalesQty), 2, BigDecimal.ROUND_HALF_UP);//四合五入保留2位小数;
				amountTotal = amountTotal.add(amount);//累计已摊分金额
			}

			//保存分配明细
			CrawlerOutTempModel tempModel = new CrawlerOutTempModel();
			tempModel.setSourceType("2");// 数据来源 1-云集 2-唯品
			tempModel.setDataType("3");// 数据类型 1-库存量 2-可核销量 3-出入库分配明细
			tempModel.setMerchantId(merchant.getMerchantId());
			tempModel.setCustomerId(customerId);// 经销客户id
			tempModel.setDepotId(depot.getDepotId());
			tempModel.setBillId(billId);// 经销自增长账单id
			tempModel.setBillCode(billCode);// 账单号
			tempModel.setPoNo(poNo);
			tempModel.setGoodsNo(contrastDTO.getGoodsNo());// 经分销货号
			tempModel.setSkuNo(sku);
			tempModel.setFpNum(outsalesQty);// 分配数量
			tempModel.setBillType(processingTypeErp);  //处理类型归类
			tempModel.setCurrencyCode(currencyCode);//币种
			tempModel.setSaleCode(saleOrderCode);//平台销售单号
			tempModel.setPrice(price);
			tempModel.setAmount(amount);
			tempModel.setBuId(contrastDTO.getBuId());//事业部id
			tempModel.setBuName(contrastDTO.getBuName());
			crawlerOutTempDao.save(tempModel);// 保存分配明细
        }
	}
	/**====================================出库end==========================================*/
	
	/**====================================公共方法start====================================*/
	/**
	 * 唯品4.0-查询未使用过的账单按商家+账单号分组去重
	 */
	public List<Map<String, Object>> searchMerchantIdBillCodeList(Map<String, Object> map) {
		return crawlerBillDao.searchMerchantIdBillCodeList(map);
	}
	/** 清空临时表 */
	public void clearTable(Map<String, Object> map) {
		crawlerOutTempDao.clearTable(map);
	}
	/**
	 * 4、循环账单明细获取对应经分销的货号 key=sku value=HashSet{goodsNo} ,没有对应货号的sku不会存在这里
	 * @throws Exception
	 */
	public Map<String, List<MerchandiseContrastDTO>> getSkuGoodsNo(MerchantInfoMongo merchant, String billCode,
			List<String> skuList) throws Exception {

		Map<String, List<MerchandiseContrastDTO>> skuGoodsNoMap = new HashMap<String, List<MerchandiseContrastDTO>>();
		for (String sku: skuList) {// ---循环账单start
			// 查询商品对照表获取对应经分销货号
			MerchandiseContrastModel contrastModel = new MerchandiseContrastModel();
			contrastModel.setCrawlerGoodsNo(sku);
			contrastModel.setMerchantId(merchant.getMerchantId());
			contrastModel.setStatus("0");// 0-启用 1-禁用
			contrastModel.setType(DERP_ORDER.MERCHANDISECONTRAST_TYPE_2);
			List<MerchandiseContrastDTO> contrastList = merchandiseContrastDao.listContrastAndItem(contrastModel);
			if (contrastList == null || contrastList.size() <= 0) {
				String msg="唯品-商家:"+merchant.getName()+",账单号:"+billCode+",sku:" + sku + "在商品对照表未找到对应货号";
				logger.info(msg);
				sendCrawlerErrorLog(merchant,billCode,"",sku,0,msg);
				continue;
			}
			// 存储sku对应的货号
			List<MerchandiseContrastDTO> goodsNoList = new ArrayList<MerchandiseContrastDTO>();
			for(MerchandiseContrastDTO contras : contrastList) {// ---循环对照表货号start
				if(contras.getBuId() != null) {
					goodsNoList.add(contras);
				}
			} // ---循环对照表货号end
			
			if(goodsNoList.size()>0) {
			    skuGoodsNoMap.put(sku, goodsNoList); // 爬虫SKU可能会对应我们系统多个货号
			}
		} // ---循环账单end
		return skuGoodsNoMap;
	}
	/**9.出入、入库公用方法：查询本商家+账单号已分配好的临时分配明细按处理类型归类+币种+事业部ID分组生成出库单
	 * @throws Exception */
	public List<InvetAddOrSubtractRootJson> saveBillOutinDepot(String crType,String billCode,MerchantInfoMongo merchant,
			DepotInfoMongo depot,ReptileConfigMongo reptileMongo,Map<String,List<MerchandiseContrastDTO>> skuGoodsNoMap) throws Exception {
		
		List<InvetAddOrSubtractRootJson> rootJsonList = new ArrayList<InvetAddOrSubtractRootJson>();
		
		/**9.查询本商家+账单号已分配好的临时分配明细按处理类型归类+币种+事业部分组*/
		Map<String, Object> queryMap = new HashMap<String, Object>();
    	queryMap.put("sourceType", "2");//数据来源 1-云集 2-唯品
    	queryMap.put("dataType", "3");//数据类型 1-库存量 2-可核销量 3-出入库分配明细
    	queryMap.put("merchantId", merchant.getMerchantId());
    	queryMap.put("billCode", billCode);
		List<Map<String, Object>> billTypeCurrencyList= crawlerOutTempDao.getListByBillTypeCurrency(queryMap);
		/**9.1循环本商家、账单号处理类型归类+币种分组生成出库单,相同商家、账单号、处理类型归类、币种生成一个出库单*/
		for(Map<String, Object> billTypeCurrencyMap: billTypeCurrencyList) {//---循环处理类型归类+币种+事业部ID
			String billType = (String)billTypeCurrencyMap.get("bill_type");
			String currencyCode = (String)billTypeCurrencyMap.get("currency_code");
			Long buId = (Long)billTypeCurrencyMap.get("bu_id");
			
			CrawlerOutTempModel tempMode = new CrawlerOutTempModel();
			tempMode.setSourceType("2");
			tempMode.setDataType("3");
			tempMode.setMerchantId(merchant.getMerchantId());
			tempMode.setBillCode(billCode);
			tempMode.setBillType(billType);
			tempMode.setCurrencyCode(currencyCode);
			tempMode.setBuId(buId);
			//相同sku的分配明细汇总后出库
			List<Map<String,Object>> tempMapList = crawlerOutTempDao.getListSumGroupBySku(tempMode);

			//本商家、账单号、处理归类、币种的分配明细 查出明细用于更新爬虫账单为已使用
			List<CrawlerOutTempModel> tempList = crawlerOutTempDao.list(tempMode);

			// 查询一条账单明细获取发货日期
			CrawlerBillModel billModel = crawlerBillDao.searchById(tempList.get(0).getBillId());
			//从临时分配明细里获取第一条的事业部名称
			String buName = tempList.get(0).getBuName();

			String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZDCK);
			//生成出库单表头
			BillOutinDepotModel outInDepot = new BillOutinDepotModel();
			outInDepot.setCode(code);
			outInDepot.setDepotId(depot.getDepotId());
			outInDepot.setDepotName(depot.getName());
			outInDepot.setCustomerId(billModel.getCustomerId());
			outInDepot.setCustomerName(billModel.getCustomerName());
			outInDepot.setBillCode(billCode);
			outInDepot.setProcessingType(billType);
			outInDepot.setDeliverDate(billModel.getBillDate());
			if(crType.equals("out")) {
			    outInDepot.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//调减
			}else if(crType.equals("in")) {
				outInDepot.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//调减增
			}
			outInDepot.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_01);//处理中
			outInDepot.setBillSource(DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_2);//账单来源 唯品
			outInDepot.setMerchantId(merchant.getMerchantId());
			outInDepot.setMerchantName(merchant.getName());
			outInDepot.setCurrency(billModel.getCurrencyCode());
			outInDepot.setBuId(buId);
			outInDepot.setBuName(buName);
			outInDepot.setCreater("系统自动生成");
			outInDepot.setCreateDate(TimeUtils.getNow());
			outInDepot.setModifyDate(TimeUtils.getNow());
			Long outInDepotId = billOutinDepotDao.save(outInDepot);
			
			//生成表体
			Integer totalNum = 0;
			BigDecimal totalAmount = new BigDecimal(0.00);
			for(Map<String,Object> tempMap: tempMapList) {//---临时分配明细生成表体
				String poNo = (String)tempMap.get("po_no");
				String goodsNo = (String)tempMap.get("goods_no");//经销货号
				String skuNo = (String)tempMap.get("sku_no");//爬虫sku
				BigDecimal amount = (BigDecimal)tempMap.get("amount");//相同sku汇总后的金额
				BigDecimal fpNum = (BigDecimal)tempMap.get("fp_num");//相同sku汇总后的数量
				BigDecimal price = amount.divide(fpNum,8, BigDecimal.ROUND_HALF_UP);//单价 四合五入保留8位小数;

				Map<String, Object> merchandiseMap = new HashMap<String, Object>();
				merchandiseMap.put("merchantId", merchant.getMerchantId());
				merchandiseMap.put("goodsNo", goodsNo);
				merchandiseMap.put("status", "1");//1使用中,0已禁用
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);

				List<MerchandiseContrastDTO> goodsList = skuGoodsNoMap.get(skuNo);
				int contrastNum = 1;//转换数量
				for(MerchandiseContrastDTO contrastDTO:goodsList){
					if(contrastDTO.getGoodsNo().equals(goodsNo)){
						contrastNum = contrastDTO.getNum();//转换数量
						break;
					}
				}

				//生成账单出入库表体
				BillOutinDepotItemModel itemModel = new BillOutinDepotItemModel();
				itemModel.setOutinDepotId(outInDepotId);
				itemModel.setGoodsId(merchandise.getMerchandiseId());
				itemModel.setGoodsNo(merchandise.getGoodsNo());
				itemModel.setGoodsName(merchandise.getName());
				itemModel.setCommbarcode(merchandise.getCommbarcode());
				itemModel.setPlatformSku(skuNo);
				itemModel.setContrastNum(contrastNum);//记录出库时爬虫商品对照表转换量
				itemModel.setPoNo(poNo);
				itemModel.setNum(fpNum.intValue());
				itemModel.setPrice(price);
				itemModel.setAmount(amount);
				//云集、唯品爬虫账单默认为税率表中的0；
				itemModel.setTaxRate(0.0);
				//云集、唯品爬虫账单税额默认为0
				itemModel.setTax(BigDecimal.ZERO);
				//云集、唯品爬虫账单默认： 结算金额（含税）=结算金额（不含税）
				itemModel.setTaxAmount(amount);
				//结算单价（含税）=结算金额（含税）/数量，保留8位小数
				itemModel.setTaxPrice(price);
				itemModel.setCreateDate(TimeUtils.getNow());
				itemModel.setModifyDate(TimeUtils.getNow());
				billOutinDepotItemDao.save(itemModel);
				
				totalNum = totalNum + fpNum.intValue();
				totalAmount = totalAmount.add(amount);

				//发送成功爬虫日志
				CrawlerBillMongo logModel = new CrawlerBillMongo();
				logModel.setSaleOutDepotCode(outInDepot.getCode());//出入库单号
				logModel.setGoodsNo(merchandise.getGoodsNo());
				logModel.setGoodsName(merchandise.getName());
				logModel.setBarcode(merchandise.getBarcode());
				logModel.setDepotId(outInDepot.getDepotId());
				logModel.setDepotName(outInDepot.getDepotName());
				logModel.setBillCode(billCode);// 账单号
				logModel.setPoNo(poNo);
				logModel.setGoodsNo(skuNo);
				logModel.setPlatformType("2");// 平台类型 1、云集 2、唯品
				logModel.setStatus(1);// 状态 1-成功，0-失败
				logModel.setNum(fpNum.intValue());// 数量
				logModel.setMerchantId(merchant.getMerchantId());
				logModel.setMerchantName(merchant.getName());
				logModel.setCreateDate(TimeUtils.getNow().getTime());
				sendCrawlerSuccessLog(logModel);
				
			}//---临时分配明细生成表体end

			//更新爬虫账单状态为已使用
			for(CrawlerOutTempModel temp : tempList){
				CrawlerBillModel crawlerBill = new CrawlerBillModel();
				crawlerBill.setId(temp.getBillId());
				crawlerBill.setIsUsed(DERP_ORDER.CRAWLERBILL_ISUSED_1);//已使用
				crawlerBill.setOutCode(outInDepot.getCode());//记录出到哪个出库单
				crawlerBillDao.modify(crawlerBill);
			}

			//更新金额、总数量到表头
			outInDepot = billOutinDepotDao.searchById(outInDepotId);
			outInDepot.setTotalNum(totalNum);
			outInDepot.setTotalAmount(totalAmount);
			billOutinDepotDao.modify(outInDepot);
			
			//根据单号汇总生成库存扣减报文
			InvetAddOrSubtractRootJson rootJson = getSubtractRootJson(crType, outInDepot, merchant, depot);
			rootJsonList.add(rootJson);
		}//---循环币种处理类型end
		
		return rootJsonList;
	}
	/**crType: in-入库 out=出库
	 * */
	public InvetAddOrSubtractRootJson getSubtractRootJson(String crType,BillOutinDepotModel outInDepot,
			MerchantInfoMongo merchant,DepotInfoMongo depot) {
		
		String deliverDateStr = TimeUtils.format(outInDepot.getDeliverDate(), "yyyy-MM-dd hh:mm:ss");
		String ownMonth = TimeUtils.format(outInDepot.getDeliverDate(), "yyyy-MM");// 归属月份
		
		//汇总表体相同商品的数量
		InvetAddOrSubtractRootJson rootJson = new InvetAddOrSubtractRootJson();
		rootJson.setMerchantId(String.valueOf(outInDepot.getMerchantId()));
		rootJson.setMerchantName(outInDepot.getMerchantName());
		rootJson.setTopidealCode(merchant.getTopidealCode());
		rootJson.setDepotId(String.valueOf(depot.getDepotId()));
		rootJson.setDepotName(depot.getName());
		rootJson.setDepotCode(depot.getCode());
		rootJson.setDepotType(depot.getType());
		rootJson.setOrderNo(outInDepot.getCode());
		rootJson.setBusinessNo(outInDepot.getCode());
		rootJson.setIsTopBooks(depot.getIsTopBooks());
		rootJson.setSourceDate(TimeUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
		rootJson.setOwnMonth(ownMonth);
		rootJson.setDivergenceDate(deliverDateStr);
		rootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0016);//账单出入库
		if(outInDepot.getProcessingType().equals(DERP_ORDER.PROCESSINGTYPE_XSC)){
			rootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0028);//账单销售出库
		}else if(outInDepot.getProcessingType().equals(DERP_ORDER.PROCESSINGTYPE_GJC)){
			rootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0022);//国检抽样
		}else if(outInDepot.getProcessingType().equals(DERP_ORDER.PROCESSINGTYPE_BFC)){
			rootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0025);//唯品报废
		}else if(outInDepot.getProcessingType().equals(DERP_ORDER.PROCESSINGTYPE_PKC)
				||outInDepot.getProcessingType().equals(DERP_ORDER.PROCESSINGTYPE_PYR)){
			rootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0024);//唯品盘点
		}else if(outInDepot.getProcessingType().equals(DERP_ORDER.PROCESSINGTYPE_KTR)){
			rootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0027);//客退入
		}
		rootJson.setBuId(String.valueOf(outInDepot.getBuId()));
		rootJson.setBuName(outInDepot.getBuName());
		rootJson.setBackTopic(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTopic());
		rootJson.setBackTags(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTags());
		Map<String, Object> customParam = new HashMap<String, Object>();
		rootJson.setCustomParam(customParam);
		
		List<InvetAddOrSubtractGoodsListJson> goodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		List<Map<String, Object>> goodsMapList = billOutinDepotItemDao.getCodeGoodsNum(outInDepot.getCode());
		for(Map<String, Object> goodsMap:goodsMapList) {
			Long goodsId = (Long)goodsMap.get("goods_id");
			String goodsNo = (String)goodsMap.get("goods_no");
			String goodsName = (String)goodsMap.get("goods_name");
			BigDecimal numdec = (BigDecimal)goodsMap.get("num");
			Integer num = numdec.intValue();
			
			InvetAddOrSubtractGoodsListJson goodsJson = new InvetAddOrSubtractGoodsListJson();
			goodsJson.setGoodsId(String.valueOf(goodsId));
			goodsJson.setGoodsNo(goodsNo);
			goodsJson.setGoodsName(goodsName);
			goodsJson.setType(DERP_INVENTORY.INVENTORY_TYPE_0);//(0 好品 1坏品 )
			goodsJson.setNum(num.intValue());
			if(crType.equals("out")) {
			   goodsJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//0 调减
			}else if(crType.equals("in")) {
				 goodsJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//1调增
			}
			goodsJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期 0 是 1 否
			goodsList.add(goodsJson);
		}
		rootJson.setGoodsList(goodsList);
		return rootJson;
	}
	/**
	 * 获取库存jdbc链接
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			String dburl = ApolloUtilDB.crInventoryUrl;
			String dbUserName = ApolloUtilDB.crInventoryUserName;
			String dbPassword = ApolloUtilDB.crInventoryPassword;
			conn = DriverManager.getConnection(dburl, dbUserName, dbPassword);
			if (conn == null) {// 重试一次
				conn = DriverManager.getConnection(dburl, dbUserName, dbPassword);
			}
		} catch (Exception e) {
			logger.error("创建库存链接失败" + e.getMessage());
		}
		return conn;
	}
	/**
	 * 根据商家、仓库、货号获取库存余量
	 */
	public Integer getGoodsSurplusNum(Connection conn, Long merchantId, Long depotId, String goodsNo) {
		Statement pst = null;
		ResultSet rs = null;
		Integer surplusNum = 0;
		try {
			String queryStr = "select SUM(surplus_num) surplus_num from i_inventory_batch " + "where merchant_id="
					+ merchantId + " AND depot_id=" + depotId + " AND goods_no='" + goodsNo
					+ "' AND TYPE='0' AND is_expire='1'";
			pst = conn.createStatement();
			rs = pst.executeQuery(queryStr);
			System.out.println("查询库存余量SQL:" + queryStr);
			while (rs.next()) {
				BigDecimal num = rs.getBigDecimal("surplus_num");
				if (num != null)
					surplusNum = num.intValue();
			}
		} catch (Exception e) {
			logger.error("查询库存余量失败" + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
			}
		}
		return surplusNum;
	}
	/**
	 * 根据sku拼装货号list返回
	 */
	public List<String> getSkuGoodsNoList(String sku, Map<String, List<MerchandiseContrastDTO>> skuGoodsNoMap) {
		List<String> goodsNoList = null;
		List<MerchandiseContrastDTO> contrastModelList = skuGoodsNoMap.get(sku);
		if (contrastModelList != null && contrastModelList.size()>0) {
			goodsNoList = new ArrayList<String>();
			for (MerchandiseContrastDTO model : contrastModelList) {
				goodsNoList.add(model.getGoodsNo());
			}
		}
		return goodsNoList;
	}
	/**
	 * sku对应经分销货号拼装成list返回
	 */
	public List<String> getSkuGoodsNoList(Map<String, List<MerchandiseContrastDTO>> skuGoodsNoMap) {
		List<String> goodsNoList = new ArrayList<String>();
		for(List<MerchandiseContrastDTO> contrastModelList : skuGoodsNoMap.values()) {
			for(MerchandiseContrastDTO contrastModel: contrastModelList) {
				goodsNoList.add(contrastModel.getGoodsNo());
			}
		}
		return goodsNoList;
	}
	/**
	 * 发送爬虫失败日志 
	 */
	public void sendCrawlerErrorLog(MerchantInfoMongo merchant,String billCode,String poNo,
			String sku,Integer num,String msg) throws Exception {
		CrawlerBillMongo logModel = new CrawlerBillMongo();
		logModel.setBillCode(billCode);// 账单号
		logModel.setPoNo(poNo);
		logModel.setGoodsNo(sku);
		logModel.setPlatformType("2");// 平台类型 1、云集 2、唯品
		logModel.setStatus(0);// 状态 1-成功，0-失败
		logModel.setNum(num);// 数量
		if(merchant!=null){
			logModel.setMerchantId(merchant.getMerchantId());
			logModel.setMerchantName(merchant.getName());
		}
		logModel.setErrorMsg(msg);
		logModel.setCreateDate(TimeUtils.getNow().getTime());
		JSONObject jsonObject = JSONObject.fromObject(logModel);
		logger.info("发送爬虫日志报文：" + jsonObject.toString());
		rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CRAWLER_BILL.getTopic(),
				MQLogEnum.LOG_CRAWLER_BILL.getTags());
	}
	/**
	 * 发送爬虫失败日志 
	 */
	public void sendCrawlerSuccessLog(CrawlerBillMongo logModel) throws Exception {
		
		JSONObject jsonObject = JSONObject.fromObject(logModel);
		logger.info("发送爬虫日志报文：" + jsonObject.toString());
		rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CRAWLER_BILL.getTopic(),
				MQLogEnum.LOG_CRAWLER_BILL.getTags());
	}
	/**
	 * 推送库存扣减报文、仓库唯品红冲报文。
	 * @throws Exception
	 */
	public void pushMQ(List<InvetAddOrSubtractRootJson> rootJsonList) {
		// 库存扣减
		if(rootJsonList==null||rootJsonList.size()<=0) {
			logger.info("爬虫推送扣减库存报文数量为0,结束");
			return ;
		}
		// 推送库存MQ-扣减库存
		for (InvetAddOrSubtractRootJson rootJson : rootJsonList) {
			JSONObject jsonObject = null;
			try {
				jsonObject = JSONObject.fromObject(rootJson);
				SendResult sendResult = mqProducer.send(jsonObject.toString(),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				logger.info("爬虫推送扣减库存报文pcmq：" + jsonObject.toString());
				logger.info("推送结果pcmq：" + sendResult);
				// 避免数据过多会导致锁库
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("库存扣减推送异常，报文pcmqfail：" + jsonObject.toString());
			}
		}
		
	}
	/**====================================公共方法====================================*/
	/**====================================入库start====================================*/
	/**
	 * 生成唯品爬虫入库
	 * */
	public List<InvetAddOrSubtractRootJson> saveVIPSaleInDepot(Long merchantId,String billCode,List<Long> idList) throws Exception {
		// 查询商家
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", merchantId);// 商家id
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
		
		/**公共查询条件*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId", merchantId);
		paramMap.put("billCode", billCode);
		paramMap.put("idList", idList);
		
		/**3.查询本商家+账单号未使用过的账单明细按sku去重*/
		List<String> skuList = crawlerBillDao.searchSkuList(paramMap);
		/**4.查询爬虫对照表获取sku对应的经分销货号存储到map,key=sku value=MerchandiseContrastModel*/
		Map<String,List<MerchandiseContrastDTO>> skuGoodsNoMap = new HashMap<String, List<MerchandiseContrastDTO>>();
		skuGoodsNoMap = getSkuGoodsNo(merchant, billCode, skuList);
		if(skuGoodsNoMap.isEmpty()) {
			logger.info("唯品-账单号:"+billCode+"获取商品对照表货号为空,结束本账单号运行");
			return null;
		}

		/**获取唯品爬虫配置存储到map key=userCode*/
		Map<String, Object> reptileAllMap = new HashMap<String, Object>();
		Map<String, Object> reptileMap = new HashMap<String, Object>();
		reptileMap.put("platformType", DERP.CRAWLER_TYPE_2);
		reptileMap.put("merchantId",merchant.getMerchantId());
		List<ReptileConfigMongo> reptileList = reptileConfigMongoDao.findAll(reptileMap);
		if(reptileList!=null&&reptileList.size()>0) {
			for(ReptileConfigMongo entity:reptileList) {
				reptileAllMap.put(entity.getLoginName(), entity);
			}
		}
		
		/**5.查询本商家+账单号负数未使用过的账单明细*/
		paramMap.put("processingType","rk");//入库 取负数
		List<Map<String, Object>> billListAll = crawlerBillDao.getBillList(paramMap);
		
		// 查询仓库
		Map<String, Object> oneBillMap = billListAll.get(0);
		String userCode = (String)oneBillMap.get("user_code");
		ReptileConfigMongo reptileMongo = (ReptileConfigMongo) reptileAllMap.get(userCode);
		if(reptileMongo==null) {
			String msg="唯品-商家:"+merchant.getName()+",账单号:"+billCode+"未找到爬虫配置结束本账单号运行";
			logger.info(msg);
			sendCrawlerErrorLog(merchant,billCode,"","",0,msg);
			return null;
		}
		Map<String, Object> depotMap = new HashMap<String, Object>();
		depotMap.put("depotId", reptileMongo.getInDepotId());// 
		DepotInfoMongo vipDepot = depotInfoMongoDao.findOne(depotMap);
		if(vipDepot==null) {
			String msg="唯品-商家:"+merchant.getName()+",账单号:"+billCode+"未找到仓库,结束本账单号运行";
			logger.info(msg);
			sendCrawlerErrorLog(merchant,billCode,"","",0,msg);
			return null;
		}
		/**6.循环分配账单*/
		allotNumVIPIn(billListAll, skuGoodsNoMap, merchant, vipDepot);
		
		/**7.查询本商家+账单号已分配好的临时分配明细按处理类型归类+币种分组统计生成出库单、更新爬虫账单为已使用状态、生成库存扣减报文*/
		String crType = "in";//out-出库 in-入库
		List<InvetAddOrSubtractRootJson> rootJsonList = saveBillOutinDepot(crType,billCode, merchant, vipDepot,reptileMongo,skuGoodsNoMap);
	
		/**8.发送库存扣减报文*/
		return rootJsonList;	
	}
	/**
	 * 6-in、循环分配账单
	 */
	public void allotNumVIPIn(List<Map<String, Object>> billList, Map<String,List<MerchandiseContrastDTO>> skuGoodsNoMap,
			MerchantInfoMongo merchant, DepotInfoMongo depot) throws Exception {
		/**6.循环分配账单明细
		 *   6.1获取sku对应经分销货号，无则跳过。
		 *   6.2分配数量到货号上保存分配明细。
		 */
        for(Map<String,Object> billMap :billList) {
        	Long customerId = (Long)billMap.get("customer_id");
        	Long billId = (Long)billMap.get("id");
        	String billCode = (String)billMap.get("bill_code");
        	String poNo = (String)billMap.get("po_no");
        	String sku = (String)billMap.get("sku");
        	String processingType = (String)billMap.get("processing_type");
        	String currencyCode = (String)billMap.get("currency_code");
        	String saleOrderCode = (String)billMap.get("sale_order_code");//平台销售单号
        	Long totalSalesQtyLong = (Long)billMap.get("total_sales_qty");
        	Integer totalSalesQty = totalSalesQtyLong.intValue();
        	BigDecimal billPrice = (BigDecimal)billMap.get("bill_price");//单价
        	BigDecimal billAmount = (BigDecimal)billMap.get("bill_amount");//金额

			/**处理类型归类：
			 *客退入库：Customer Return(客退)、Allocation(调拨)、库存买断、空处理类型#负数-------------取负数
			 *盘盈入库：Inventory overage(库存盘盈)--------------------------------------------------取负数
			 */
			String processingTypeErp = "";
			if(processingType.equals("Customer Return(客退)")||processingType.equals("Allocation(调拨)")
					||processingType.equals("库存买断")||processingType.isEmpty()){
				processingTypeErp = DERP_ORDER.PROCESSINGTYPE_KTR;//客退入库
			}else if(processingType.equals("Inventory overage(库存盘盈)")){
				processingTypeErp = DERP_ORDER.PROCESSINGTYPE_PYR;//盘盈入库
			}
        	/**6.1获取sku对应经分销货号，无则跳过。*/
			List<MerchandiseContrastDTO> contrastList = skuGoodsNoMap.get(sku);
			if(contrastList==null ||contrastList.size()<=0) continue;

			/**6.2循环对应经销的货号保存临时分配明细（非组合品时只会对应经销一个货号）*/
			BigDecimal amountTotal = new BigDecimal(0.00);//对应多个货号时用来累计前N个商品已使用的金额
			for(int i = 0;i<contrastList.size();i++){
				MerchandiseContrastDTO contrastDTO = contrastList.get(i);
				//入库量数量=账单销量*映射表配置的数量
				Integer insalesQty = totalSalesQty*contrastDTO.getNum();
				/**单价\金额计算
				 *1.若对应经销货号只有一个:
				 *  金额=账单金额
				 *  单价=若账单有单价则直接取，若无单价则用金额/数量
				 *2.若组合品时对应经销多个货号：
				 *  金额=（本商品销售单价*数量/对应的多个货号的销售单价*数量总和)*账单结算金额
				 *  单品单价=金额/(映射表数量*账单销量)
				 *最后一个商品的金额/单价:
				 *  金额=账单结算金额-前面已使用的金额
				 *  单品单价=金额/(映射表数量*账单销量)
				 * */
				BigDecimal price = new BigDecimal(0.00);
				BigDecimal amount = new BigDecimal(0.00);
				//1.若对应经销货号只有一个:
				if(contrastList.size()==1){
					amount = billAmount;
					price = billPrice;//单价默认取账单的单价
					if(price==null||price.doubleValue()<=0){
						price = billAmount.divide(new BigDecimal(totalSalesQty),2, BigDecimal.ROUND_HALF_UP);//四合五入保留2位小数;
					}
				}else{
					BigDecimal goodsAmountTotal = new BigDecimal(0.00);
					for (MerchandiseContrastDTO contrastTemp : contrastList) {
						goodsAmountTotal = goodsAmountTotal.add(contrastTemp.getPrice().multiply(new BigDecimal(contrastTemp.getNum())));
					}
					//金额=（本商品销售单价*数量/对应的多个货号的销售单价*数量总和)*账单结算金额
					amount = contrastDTO.getPrice().multiply(new BigDecimal(contrastDTO.getNum())).divide(goodsAmountTotal, 2, BigDecimal.ROUND_HALF_UP).multiply(billAmount);
					amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位
					//最后一个商品
					if(i+1==contrastList.size()){
						//金额=账单结算金额-前面已使用的金额
						amount = billAmount.subtract(amountTotal);
					}
					//单品单价=金额/(映射表数量*账单销量)
					price = amount.divide(new BigDecimal(insalesQty), 2, BigDecimal.ROUND_HALF_UP);//四合五入保留2位小数;
					amountTotal = amountTotal.add(amount);//累计已摊分金额
				}

				//保存分配明细
				CrawlerOutTempModel tempModel = new CrawlerOutTempModel();
				tempModel.setSourceType("2");// 数据来源 1-云集 2-唯品
				tempModel.setDataType("3");// 数据类型 1-库存量 2-可核销量 3-出入库分配明细
				tempModel.setMerchantId(merchant.getMerchantId());
				tempModel.setCustomerId(customerId);// 经销客户id
				tempModel.setDepotId(depot.getDepotId());
				tempModel.setBillId(billId);// 经销自增长账单id
				tempModel.setBillCode(billCode);// 账单号
				tempModel.setPoNo(poNo);
				tempModel.setGoodsNo(contrastDTO.getGoodsNo());// 经分销货号
				tempModel.setSkuNo(sku);
				tempModel.setFpNum(insalesQty);// 分配数量
				tempModel.setBillType(processingTypeErp);  //处理类型归类
				tempModel.setCurrencyCode(currencyCode);//币种
				tempModel.setSaleCode(saleOrderCode);//平台销售单号
				tempModel.setPrice(price);
				tempModel.setAmount(amount);
				tempModel.setBuId(contrastDTO.getBuId());//事业部ID
				tempModel.setBuName(contrastDTO.getBuName());
				crawlerOutTempDao.save(tempModel);// 保存分配明细
			}

        }
	}
	/**====================================入库end====================================*/
}
