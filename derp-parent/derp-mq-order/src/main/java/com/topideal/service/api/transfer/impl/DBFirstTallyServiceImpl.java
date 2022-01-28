package com.topideal.service.api.transfer.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.purchase.TallyingItemBatchDao;
import com.topideal.dao.purchase.TallyingOrderDao;
import com.topideal.dao.purchase.TallyingOrderItemDao;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.entity.vo.transfer.TransferOrderModel;
import com.topideal.json.api.v5_1.EpassFirstTallyGoodsListJson;
import com.topideal.json.api.v5_1.EpassFirstTallyReceiveListJson;
import com.topideal.json.api.v5_1.EpassFirstTallyRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.transfer.DBFirstTallyService;
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


@Service
public class DBFirstTallyServiceImpl implements DBFirstTallyService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DBFirstTallyServiceImpl.class);

    @Autowired
    private TallyingOrderDao tallyingOrderDao;// 理货单

    @Autowired
    private TallyingOrderItemDao tallyingOrderItemDao;// 理货单商品

    @Autowired
    private TallyingItemBatchDao itemBatchDao;// 理货单商品批次详情
    
    @Autowired
    private TransferOrderDao transferOrderDao;//调拨单
    @Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb



    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_12203130000,model=DERP_LOG_POINT.POINT_12203130000_Label,keyword="entInboundId")
    public boolean saveTallyInfo(String json,String keys,String topics,String tags)throws Exception{
    	/**
    	 * 说明: 举例 比如一个预申报单300件货 只会把300件货完全理完后再推初理货接口  不会出现(第一次推100件 ,第二次又推200件的情况)
    	 * 也就是说:一个预申报单只会对应一个 理货单状态是确认的理货单  一个确认状态的理货单只会对应一个入库申报单
    	 */
        // 实例化JSON对象
        JSONObject jsonData=JSONObject.fromObject(json);
        Map classMap = new HashMap();
		classMap.put("goodsList",EpassFirstTallyGoodsListJson.class);
		classMap.put("receiveList",EpassFirstTallyReceiveListJson.class);
		
        EpassFirstTallyRootJson firstTallyRoot = (EpassFirstTallyRootJson) JSONObject.toBean(jsonData, EpassFirstTallyRootJson.class,classMap);
        String entInboundId = firstTallyRoot.getEntInboundId();
        String tallyingOrderCode = firstTallyRoot.getTallyingOrderCode();
        String tallyingDate = firstTallyRoot.getTallyingDate();
        
        
        /****************后面根据单号进行匹配*******************/
       
        // 根据 调拨单号查询调拨单
        TransferOrderModel transferOrderModel =new TransferOrderModel();
        transferOrderModel.setCode(entInboundId);
        transferOrderModel.setMerchantId(firstTallyRoot.getMerchantId());
        transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);   
        if(null==transferOrderModel) {
            TransferOrderModel lbxTransferOrder = new TransferOrderModel();
            lbxTransferOrder.setLbxNo(entInboundId);
            transferOrderModel = transferOrderDao.searchByModel(lbxTransferOrder);
            if (transferOrderModel == null) {
                LOGGER.error("调拨单不存在");
                throw new RuntimeException("调拨单不存在");
            }
         }
        //检查调拨单状态
  		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)){ //DTJ("013","待提交"),
  			LOGGER.error("调拨单状态为待提交,推送失败"+transferOrderModel.getCode());
  			throw new RuntimeException("调拨单状态为待提交,推送失败"+transferOrderModel.getCode());
  		}
  		
  		 // 根据理货单编码查询理货单信息
        TallyingOrderModel tModel = new TallyingOrderModel();
        tModel.setCode(tallyingOrderCode);// 理货单编码
        tModel = tallyingOrderDao.searchByModel(tModel);
        //理货单存在就不能在插入了
        if (null != tModel) {
        	LOGGER.error("理货单已存在"+tallyingOrderCode);
            throw new RuntimeException("理货单已存在"+tallyingOrderCode);
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
     		
     	//针对同关区调入菜鸟仓，不需要下推调拨指令给跨境宝，调入结果通过调拨入库接收，不需要通过理货确认及入库申报 (页面调拨订单新增出入库仓库是保税仓的时候必填菜鸟仓是保税仓)
        /*if (inDepotInfoMongo.getName().contains("菜鸟")) {
        	if (StringUtils.isBlank(transferOrderModel.getIsSameArea())) {
        		String outCustomsNo = outDepotInfoMongo.getCustomsNo();//出仓库海关编码
				String inCustomsNo =  inDepotInfoMongo.getCustomsNo();// 入仓库海关编码
				if (outCustomsNo==null||inCustomsNo==null) {
					LOGGER.info("isSameArea为空,调拨出仓库或者调拨入仓库海关编码是空,订单号entInboundId"+entInboundId);
					throw new RuntimeException("入库仓是菜鸟仓 isSameArea为空,调拨出库或者调拨入海关编码是空,订单号entInboundId"+entInboundId);		
				}
				if (String.valueOf(outCustomsNo).equals(String.valueOf(inCustomsNo))) {
					LOGGER.info("入库仓是菜鸟仓 isSameArea为空,调出仓库的海关编码和调入仓库海关编码相同(同关区 不走改流程),订单号entInboundId"+entInboundId);
					throw new RuntimeException("入库仓是菜鸟仓 isSameArea为空,调出仓库的海关编码和调入仓库海关编码相同(同关区 不走改流程),订单号entInboundId"+entInboundId);
				}
			}else {
				if (DERP.ISSAMEAREA_1.equals(transferOrderModel.getIsSameArea())) {
					LOGGER.error("同关区调入菜鸟,调入结果通过调拨入库接收，不需要通过理货确认,入库申报,进仓状态的流程" + entInboundId);
		 			throw new RuntimeException("同关区调入菜鸟,调入结果通过调拨入库接收，不需要通过理货确认,入库申报,进仓状态的流程" + entInboundId);
				
				}
			}
        	
        }*/

        TallyingOrderModel tallyingOrder = new TallyingOrderModel();
        tallyingOrder.setCode(tallyingOrderCode);// 理货单号
    	tallyingOrder.setOrderId(transferOrderModel.getId());//订单id
    	tallyingOrder.setDepotId(transferOrderModel.getInDepotId());// 仓库id	
    	tallyingOrder.setDepotName(transferOrderModel.getInDepotName());// 仓库名称
    	tallyingOrder.setContractNo(transferOrderModel.getContractNo());// 合同号
    	tallyingOrder.setMerchantId(transferOrderModel.getMerchantId());//商家id
    	tallyingOrder.setMerchantName(transferOrderModel.getMerchantName());// 商家名称
    	tallyingOrder.setType(DERP_ORDER.TALLYINGORDER_TYPE_2);//类型  1 采购  2调拨
        tallyingOrder.setOrderCode(entInboundId);// 订单单编号
        tallyingOrder.setBuId(transferOrderModel.getBuId());
        tallyingOrder.setBuName(transferOrderModel.getBuName());
        if (tallyingDate.length()==10) {
        	tallyingDate=tallyingDate+" 00:00:00";	
        	tallyingOrder.setTallyingDate(Timestamp.valueOf(tallyingDate));//理货时间
		}else {
			tallyingOrder.setTallyingDate(Timestamp.valueOf(tallyingDate));//理货时间
		}  
        tallyingOrder.setState(DERP_ORDER.TALLYINGORDER_STATE_009);//"009","待确认"

			
        // 向理货单中插入数据
        tallyingOrderDao.save(tallyingOrder);

        // 根据商品编码查询商品信息
        List<EpassFirstTallyGoodsListJson> goodsList = firstTallyRoot.getGoodsList();
        for (int i = 0; i < goodsList.size(); i++) {
        	EpassFirstTallyGoodsListJson tallyGoods = goodsList.get(i);
            // 理货单商品详情类
            TallyingOrderItemModel tallyingOrderItemModel = new TallyingOrderItemModel();
            Object purchaseNum = tallyGoods.getPurchaseNum();//采购数量
            tallyingOrderItemModel.setPurchaseNum(Integer.valueOf(purchaseNum.toString()));//采购数量
            tallyingOrderItemModel.setTallyingNum(tallyGoods.getTallyingNum());//理货数量
            tallyingOrderItemModel.setNormalNum(tallyGoods.getTotoalNormalNum());//正常数量
            tallyingOrderItemModel.setMultiNum(tallyGoods.getMultiNum());//多货数量
            tallyingOrderItemModel.setLackNum(tallyGoods.getLackNum());// 缺货数量          
            tallyingOrderItemModel.setGoodsId(tallyGoods.getGoodsId());// 商品id
            tallyingOrderItemModel.setGoodsNo(tallyGoods.getGoodsNo());// 商品货号
            tallyingOrderItemModel.setGoodsName(tallyGoods.getGoodsName());// 商品名称
            tallyingOrderItemModel.setBarcode(tallyGoods.getBarcode());// 货品条形码
            tallyingOrderItemModel.setGrossWeight(tallyGoods.getGrossWeight());// 货品毛重
            tallyingOrderItemModel.setNetWeight(tallyGoods.getNetWeight());// 货品净重
            tallyingOrderItemModel.setVolume(tallyGoods.getVolume());// 货品体积
            tallyingOrderItemModel.setLength(tallyGoods.getLength());// 货品
            tallyingOrderItemModel.setWidth(tallyGoods.getWidth());// 货品
			tallyingOrderItemModel.setHeight(tallyGoods.getHeight());// 货品
			
            tallyingOrderItemModel.setTallyingOrderId(tallyingOrder.getId());//理货单id
            
            // 理货单商品条形码  待定
            //向理货单商品详情插入数据
            tallyingOrderItemDao.save(tallyingOrderItemModel);
            // 根据商品编码查询商品信息
            List<EpassFirstTallyReceiveListJson> receiveList = tallyGoods.getReceiveList();
            for (int j = 0; j < receiveList.size(); j++) {
            	EpassFirstTallyReceiveListJson receive = receiveList.get(j);
                //理货单商品批次详情
                TallyingItemBatchModel itemBatchModel= new TallyingItemBatchModel();
                itemBatchModel.setItemId(tallyingOrderItemModel.getId());//理货单商品详情id
                itemBatchModel.setGoodsId(tallyGoods.getGoodsId());//理货单商品详情id(用的是上面的jsonObj)
                itemBatchModel.setBatchNo(receive.getBatchNo());//批次号
                itemBatchModel.setWornNum(receive.getWornNum());//坏货数量
                itemBatchModel.setExpireNum(receive.getExpireNum());//过期数量
                itemBatchModel.setNormalNum(receive.getNormalNum());//正常数量
                String producedDate = receive.getProductionDate();
                if (StringUtils.isNotBlank(producedDate)) {
                	if (producedDate.length()==10) {
                    	producedDate=producedDate+" 00:00:00";	
                    	itemBatchModel.setProductionDate(Timestamp.valueOf(producedDate));//生产日期
            		}else {
            			itemBatchModel.setProductionDate(Timestamp.valueOf(producedDate));//生产日期
    				}  
				}
                   
             
                String expiredDate = receive.getOverdueDate();
                if (StringUtils.isNotBlank(expiredDate)) {
                	 if (expiredDate.length()==10) {
                     	expiredDate=expiredDate+" 00:00:00";	
                     	itemBatchModel.setOverdueDate(Timestamp.valueOf(expiredDate));//失效日期
             		}else {
             			itemBatchModel.setOverdueDate(Timestamp.valueOf(expiredDate));//失效日期
     				}
				}
               
                
                // 插入理货单批次
                itemBatchDao.save(itemBatchModel);
            }
        }
        return true;
    }
}
