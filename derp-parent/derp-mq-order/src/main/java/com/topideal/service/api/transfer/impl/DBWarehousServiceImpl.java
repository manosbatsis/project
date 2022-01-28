package com.topideal.service.api.transfer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.purchase.TallyingOrderDao;
import com.topideal.dao.transfer.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.api.v5_2.EpassStorageDeclareGoodsListJson;
import com.topideal.json.api.v5_2.EpassStorageDeclareReceiveListJson;
import com.topideal.json.api.v5_2.EpassStorageDeclareRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.api.transfer.DBWarehousService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 入库申报接口实现类
 * @author yucaifu

 */
@Service
public class DBWarehousServiceImpl implements DBWarehousService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DBWarehousServiceImpl.class);

	@Autowired
	private TallyingOrderDao tallyingOrderDao;// 理货单

	@Autowired
	private TransferOrderDao transferOrderDao;// 调拨订单

	@Autowired
	private TransferInDepotDao transferInDepotDao;//调拨入库
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao;// 调拨入库表体
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao; //唯一外部单号表
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;
	
	//保存入库申报信息
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203130100,model=DERP_LOG_POINT.POINT_12203130100_Label,keyword="entInboundId")
	public boolean saveWarehousInfo(String json,String keys,String topics,String tags) throws Exception {
	
		LOGGER.info("入库申报-调拨，消费端开始json"+json);
	
		// 将字符串转成json
		JSONObject jsonData =JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",EpassStorageDeclareGoodsListJson.class);
		classMap.put("receiveList",EpassStorageDeclareReceiveListJson.class);
		
		EpassStorageDeclareRootJson declareRoot = (EpassStorageDeclareRootJson) JSONObject.toBean(jsonData, EpassStorageDeclareRootJson.class,classMap);
		
		String entInboundId = declareRoot.getEntInboundId();
		String isRookie = declareRoot.getIsRookie();//获取是否菜鸟字段(1-是，0-否)
		Long merchantId = declareRoot.getMerchantId();
		String topidealCode = declareRoot.getTopidealCode();
		 // 根据 调拨单号查询调拨单
        TransferOrderModel transferOrderModel =new TransferOrderModel();
        transferOrderModel.setMerchantId(merchantId);
        if(isRookie.equals("1")){//是否菜鸟字段(1-是，0-否)
        	transferOrderModel.setLbxNo(entInboundId);//调拨单号
        }else{
        	transferOrderModel.setCode(entInboundId);//调拨单号
        }
        transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);    
        if(transferOrderModel ==null) {
        	LOGGER.error("没有查到对应的订单:订单号"+entInboundId);
			throw new RuntimeException("没有查到对应的订单:订单号"+entInboundId);
		}
      		
        //检查调拨单状态
  		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)){ //DTJ("013","待提交"),
  			LOGGER.error("调拨单状态为待提交,推送失败"+transferOrderModel.getCode());
  			throw new RuntimeException("调拨单状态为待提交,推送失败"+transferOrderModel.getCode());
  		}
      		
    	// 根据理货单号查询理货单信息
		TallyingOrderModel tModel= new TallyingOrderModel();
	    tModel.setOrderCode(entInboundId);// 调拨订单号
	    tModel.setState(DERP_ORDER.TALLYINGORDER_STATE_010); //YQR("010","已确认"),
		tModel = tallyingOrderDao.searchByModel(tModel);
		if(tModel==null){
			tModel= new TallyingOrderModel();
		}

		// 向电商订单唯一标识表插入数据
		OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
		orderExternalCodeModel.setExternalCode(transferOrderModel.getCode());
		orderExternalCodeModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_4));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
		try {
			Long id = orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			throw new RuntimeException("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
		}

    	// 根据 调拨单号和理货单号查询调拨入库单   
		TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
		transferInDepotModel.setTransferOrderId(transferOrderModel.getId());
		transferInDepotModel = transferInDepotDao.getByModel(transferInDepotModel);
		if (transferInDepotModel!=null) {
			LOGGER.error("调拨入库单已经存在,调拨订单编码"+entInboundId);
			throw new RuntimeException("调拨入库单已经存在,调拨订单编码"+entInboundId);			
		}

		 // 根据仓库id到mongoDB中查询 仓库信息
 		Map<String, Object> outDepotInfoMap = new HashMap<>();
 		outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());// 调出仓库id
 		DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);// 调出仓库信息
 		if (outDepotInfoMongo == null) {
 			LOGGER.error("未查到调出仓库信息,订单编号" + entInboundId);
 			throw new RuntimeException("未查到调出仓库信息,订单编号" + entInboundId);
 		}
        	
 		// 根据仓库id到mongoDB中查询 仓库信息
 		Map<String, Object> inDepotInfoMap = new HashMap<>();
 		inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());//// 调入仓库id
 		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);
 		if (inDepotInfoMongo == null) {
 			LOGGER.error("未查到调入仓库信息,订单编号" + entInboundId);
 			throw new RuntimeException("未查到调入仓库信息,订单编号" + entInboundId);
 		}

 		// 根据商家仓库查询商家仓库关联信息
		Map<String, Object> outRelDepotMap = new HashMap<>();
 		outRelDepotMap.put("merchantId", merchantId);
 		outRelDepotMap.put("depotId", outDepotInfoMongo.getDepotId());
		DepotMerchantRelMongo outRelDepot = depotMerchantRelMongoDao.findOne(outRelDepotMap);
		if (outRelDepot == null) {
			LOGGER.error("该商家未查到调出仓库信息,订单编号" + entInboundId);
			throw new RuntimeException("该商家未查到调出仓库信息,订单编号" + entInboundId);
		}

		//针对同关区调入菜鸟仓，不需要下推调拨指令给跨境宝，调入结果通过调拨入库接收，不需要通过理货确认及入库申报	(页面调拨订单新增出入库仓库是保税仓的时候必填菜鸟仓是保税仓)
        if (inDepotInfoMongo.getName().contains("菜鸟")) {
        	if (StringUtils.isBlank(transferOrderModel.getIsSameArea())) {
        		String outCustomsNo = outDepotInfoMongo.getCustomsNo();//出仓库海关编码
				String inCustomsNo =  inDepotInfoMongo.getCustomsNo();// 入仓库海关编码
				if (outCustomsNo==null||inCustomsNo==null) {
					LOGGER.info("isSameArea为空,调拨出仓库或者调拨入仓库海关编码是空,订单号entInboundId"+entInboundId);
					throw new RuntimeException("入库仓是菜鸟仓 isSameArea为空,调拨出库或者调拨入海关编码是空,订单号entInboundId"+entInboundId);		
				}
				if (String.valueOf(outCustomsNo).equals(String.valueOf(inCustomsNo))) {
					LOGGER.info("入库仓是菜鸟仓 isSameArea为空,调出仓库的海关编码和调入仓库海关编码相同(同关区 不走该流程),订单号entInboundId"+entInboundId);
					throw new RuntimeException("入库仓是菜鸟仓 isSameArea为空,调出仓库的海关编码和调入仓库海关编码相同(同关区 不走该流程),订单号entInboundId"+entInboundId);
				}
			}else {
				if (DERP.ISSAMEAREA_1.equals(transferOrderModel.getIsSameArea())) {
					LOGGER.error("同关区调入菜鸟,调入结果通过调拨入库接收，不需要通过理货确认,入库申报,进仓状态的流程" + entInboundId);
		 			throw new RuntimeException("同关区调入菜鸟,调入结果通过调拨入库接收，不需要通过理货确认,入库申报,进仓状态的流程" + entInboundId);
				
				}
			}
        }

        for (EpassStorageDeclareGoodsListJson goodsJson : declareRoot.getGoodsList()) {// 商品
            for (EpassStorageDeclareReceiveListJson receive : goodsJson.getReceiveList()) {// 批次
                //批次效期强校验：1-是 0-否
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())
						|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())) {
					String batchNo = receive.getBatchNo();
					String productionDate = receive.getProductionDate();
					String overdueDate = receive.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						LOGGER.error("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验,"+"批次和效期不能为空,订单号:" + entInboundId);
						throw new RuntimeException("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验,"+"批次和效期不能为空,订单号:" + entInboundId);
					}
				}
                //检查理货单位
                String orderTallyingUnit = transferOrderModel.getTallyingUnit();//理货单位
                if(inDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&StringUtils.isBlank(goodsJson.getTallyingUnit())){
                    LOGGER.error(goodsJson.getGoodsNo()+"的理货单位为空");
                    throw new RuntimeException(goodsJson.getGoodsNo()+"的理货单位为空");
                }
                if(inDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&!goodsJson.getTallyingUnit().equals(orderTallyingUnit)){
                    LOGGER.error(goodsJson.getGoodsNo()+"的理货单位"+goodsJson.getTallyingUnit()+"与调拨单的理货单位"+orderTallyingUnit+"不一致");
                    throw new RuntimeException(goodsJson.getGoodsNo()+"的理货单位"+goodsJson.getTallyingUnit()+"与调拨单的理货单位"+orderTallyingUnit+"不一致");
                }
			}
		}

		//查询调拨商品
		TransferOrderItemModel transferOrderItemModel = new TransferOrderItemModel();
		transferOrderItemModel.setTransferOrderId(transferOrderModel.getId());
		List<TransferOrderItemModel> itemList = transferOrderItemDao.list(transferOrderItemModel);
        Map<Long, TransferOrderItemModel> goodMap = new HashMap<>();//key=原商品id value=调拨单商品实体
        Map<Long, TransferOrderItemModel> kwGoodsMap = new HashMap<>();//key=库位商品id value=调拨单商品实体  以出定入用
		for(TransferOrderItemModel model : itemList) {
			goodMap.put(model.getInGoodsId(), model);
            //库位商品id为key
            kwGoodsMap.put(model.getInGoodsId(), model);
		}
        
		// 新增调拨入库单
		TransferInDepotModel tInModel =new TransferInDepotModel();
		tInModel.setTransferOrderId(transferOrderModel.getId());//调拨订单ID
		tInModel.setTransferOrderCode(transferOrderModel.getCode());//调拨订单编号
		tInModel.setMerchantId(transferOrderModel.getMerchantId());//商家ID
		tInModel.setMerchantName(transferOrderModel.getMerchantName());//商家名称
		tInModel.setContractNo(transferOrderModel.getContractNo());//合同号
		tInModel.setInDepotId(transferOrderModel.getInDepotId());//调入仓库id
		tInModel.setInDepotName(transferOrderModel.getInDepotName());//调入仓库名称
		tInModel.setOutDepotId(transferOrderModel.getOutDepotId());//调出仓库id
		tInModel.setOutDepotName(transferOrderModel.getOutDepotName());//调出仓库名称
//		tInModel.setTransferDate(new Timestamp((new Date().getTime())));//调入时间
		tInModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_011);//DRC("011","待入仓"), //状态011 待入仓 ,012已入仓
//		tInModel.setCode(CodeGeneratorUtil.getNo("DBRK"));//调拨入单号
		tInModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));//调拨入单号
		tInModel.setSource(DERP_ORDER.TRANSFERINDEPOT_SOURCE_2);//单据来源 1-调拨入自动生成 2-接口回推生成
		tInModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
		tInModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户id
		tInModel.setTallyingOrderCode(tModel.getCode());//理货单号
		tInModel.setTallyingOrderId(tModel.getId());//理货单id'
		tInModel.setLbxNo(transferOrderModel.getLbxNo());//lbx单号
		tInModel.setBuId(transferOrderModel.getBuId());
		tInModel.setBuName(transferOrderModel.getBuName());
		tInModel.setCreateName("接口回传");
		tInModel.setInExternalCode(declareRoot.getTallyingOrderCode());
		//新增 调拨入库单
		transferInDepotDao.save(tInModel);

		for(EpassStorageDeclareGoodsListJson goods : declareRoot.getGoodsList()) {
            List<EpassStorageDeclareReceiveListJson> receiveList = goods.getReceiveList();
            for(EpassStorageDeclareReceiveListJson receive : receiveList) {
                //查询商品
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("merchandiseId", goods.getGoodsId());
                MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
                // 新增调拨入库单表体
                TransferInDepotItemModel triItemModel= new TransferInDepotItemModel();
                triItemModel.setTransferDepotId(tInModel.getId());//调拨入库ID
                triItemModel.setInGoodsId(goods.getGoodsId());//调入商品id
                triItemModel.setInGoodsNo(goods.getGoodsNo());//调入商品货号
                triItemModel.setInGoodsName(goods.getGoodsName());//调入商品名称
                triItemModel.setInGoodsCode(goods.getGoodsCode());//调入商品编码
                triItemModel.setBarcode(goods.getBarcode());
                triItemModel.setInCommbarcode(inMerchandise.getCommbarcode());
                triItemModel.setTransferBatchNo(receive.getBatchNo());//调拨批次
                triItemModel.setTallyingUnit(goods.getTallyingUnit());//理货单位
                triItemModel.setTransferNum(Integer.valueOf(receive.getNormalNum()));//正常数量
                triItemModel.setExpireNum(receive.getExpireNum());// 过期品
                triItemModel.setWornNum(receive.getWornNum());// 坏品
                String productionDate = receive.getProductionDate();
                if (StringUtils.isNotBlank(productionDate)) {
                    if (productionDate.length()==10) {
                        triItemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
                    }else {
                        triItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
                    }
                }
                String overdueDate = receive.getOverdueDate();
                if (StringUtils.isNotBlank(overdueDate)) {
                    if (overdueDate.length()==10) {
                        triItemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//生产日期
                    }else {
                        triItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//生产日期
                    }
                }
                // 新增调拨入库单表体
                transferInDepotItemDao.save(triItemModel);
            }
		}

		boolean flag = false;
		//以入定出且入仓仓库不为香港仓（查询调拨出库单是否存在，若存在则不保存调拨出库单）
		if (DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1.equals(outRelDepot.getIsInDependOut())
		    &&!inDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
			OrderExternalCodeModel transferOutDepotExistModel = new OrderExternalCodeModel();
			transferOutDepotExistModel.setExternalCode(transferOrderModel.getCode());
			// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
			transferOutDepotExistModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5));
			try {
				orderExternalCodeDao.save(transferOutDepotExistModel);
				//查询调拨出库单是否存在
				TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
				transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());
				transferOutDepotModel = transferOutDepotDao.searchByModel(transferOutDepotModel);
				if (transferOutDepotModel == null){
					flag = true;
				}
			} catch (Exception e) {
				LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			}
		}

		if (flag) {
		    for (EpassStorageDeclareGoodsListJson goodsJson : declareRoot.getGoodsList()) {// 商品
		        for (EpassStorageDeclareReceiveListJson receive : goodsJson.getReceiveList()) {// 批次
                    // 批次效期强校验：1-是 0-否
                    if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
		                String batchNo = receive.getBatchNo();
						String productionDate = receive.getProductionDate();
						String overdueDate = receive.getOverdueDate();
						if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
							LOGGER.error("出库仓"+outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
							throw new RuntimeException("出库仓"+outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
						}
					}
				}
		        //检查报文商品在单据中是否存在
                TransferOrderItemModel orderItem = goodMap.get(goodsJson.getGoodsId());
                if(orderItem == null) {
                    LOGGER.error("调拨单未找到商品,订单编号" + transferOrderModel.getCode()+",货号"+goodsJson.getGoodsId());
                    throw new RuntimeException("调拨单未找到商品,订单编号" + transferOrderModel.getCode()+",货号"+goodsJson.getGoodsId());
                }
			}

			//生成调拨出库单
			TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
			transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());//调拨订单ID
			transferOutDepotModel.setTransferOrderCode(transferOrderModel.getCode());//调拨订单编号
			transferOutDepotModel.setMerchantId(transferOrderModel.getMerchantId());
			transferOutDepotModel.setMerchantName(transferOrderModel.getMerchantName());//商家名称
			transferOutDepotModel.setContractNo(transferOrderModel.getContractNo());//合同号
			transferOutDepotModel.setInDepotId(transferOrderModel.getInDepotId());//调入仓库id
			transferOutDepotModel.setInDepotName(transferOrderModel.getInDepotName());//调入仓库名称
			transferOutDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());//调出仓库id
			transferOutDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());//调出仓库名称
			transferOutDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
			transferOutDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户名称
			transferOutDepotModel.setLbxNo(transferOrderModel.getLbxNo());//LBX号
			transferOutDepotModel.setBuId(transferOrderModel.getBuId());
			transferOutDepotModel.setBuName(transferOrderModel.getBuName());
			transferOutDepotModel.setCreateName("接口回传");
			transferOutDepotModel.setOutExternalCode(declareRoot.getTallyingOrderCode());
			transferOutDepotModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_015);//状态  '状态 015.待出仓   ,016.已出仓 027出库中',
			transferOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK));//调拨出单号(对应调拨出库接口的调拨单号)
			transferOutDepotModel.setDataSource(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_2);
			//新增
			Long outId = transferOutDepotDao.save(transferOutDepotModel);

            //复制入库单生成出库单表体和减库存报文
            TransferInDepotModel inDepotModel = new TransferInDepotModel();
            inDepotModel.setTransferOrderId(transferOrderModel.getId());
            inDepotModel = transferInDepotDao.searchByModel(inDepotModel);
            TransferInDepotItemModel inDepotItem = new TransferInDepotItemModel();
            inDepotItem.setTransferDepotId(inDepotModel.getId());
            List<TransferInDepotItemModel> inItemList = transferInDepotItemDao.list(inDepotItem);
            for(TransferInDepotItemModel inItem : inItemList) {
                //调拨单商品
                TransferOrderItemModel orderItem = kwGoodsMap.get(inItem.getInGoodsId());
                //查询调出库位商品
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("merchandiseId", orderItem.getOutGoodsId());
                MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
                // 新增调拨出库单表体
                TransferOutDepotItemModel outDepotItemModel = new TransferOutDepotItemModel();
                outDepotItemModel.setTransferDepotId(outId);//调拨出库ID
                outDepotItemModel.setOutGoodsId(orderItem.getOutGoodsId());//库位商品
                outDepotItemModel.setOutGoodsNo(orderItem.getOutGoodsNo());
                outDepotItemModel.setOutGoodsName(orderItem.getOutGoodsName());
                outDepotItemModel.setOutGoodsCode(orderItem.getOutGoodsCode());
                outDepotItemModel.setOutCommbarcode(outMerchandise.getCommbarcode());
                outDepotItemModel.setBarcode(outMerchandise.getBarcode());
                outDepotItemModel.setTransferNum(inItem.getTransferNum());//正常数量
                outDepotItemModel.setExpireNum(inItem.getExpireNum());// 过期品
                outDepotItemModel.setWornNum(inItem.getWornNum());// 坏品
                outDepotItemModel.setTransferBatchNo(inItem.getTransferBatchNo());//调拨批次
                outDepotItemModel.setTallyingUnit(inItem.getTallyingUnit());//理货单位
                outDepotItemModel.setProductionDate(inItem.getProductionDate());//生产日期
                outDepotItemModel.setOverdueDate(inItem.getOverdueDate());//失效日期
                // 新增调拨出库单表体
                transferOutDepotItemDao.save(outDepotItemModel);
            }
		}

        LOGGER.info("入库申报-调拨，消费端结束json"+json);
		return true;
	}

}
