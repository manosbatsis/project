package com.topideal.service.pushback.sale.impl;

import java.util.HashMap;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnOdepotDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.service.pushback.sale.XSTTransfersInStoragePushBackService;

import net.sf.json.JSONObject;
/**
 *销售调拨出库库存加减成功回推
 * 2019/02/27
 * 杨创
 */
@Service
public class XSTTransfersInStoragePushBackServiceImpl implements XSTTransfersInStoragePushBackService{
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTTransfersInStoragePushBackServiceImpl.class);
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;//销售退货订单
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货入库单
	@Autowired 
	private SaleReturnOdepotDao saleReturnOdepotDao;//销售退货出库		
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;// 仓库商家关联表 mongo
	@Autowired 
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	// 调拨入库
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201153700,model=DERP_LOG_POINT.POINT_13201153700_Label,keyword="code")
	public boolean updateXSTTransfersInStoragePushBack(String json,String keys,String topics,String tags) throws Exception {
		// 不论是否是菜鸟仓传的code都是销售退货订单的code
		// 将字符串转成json 
		Thread.sleep(50);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		// 销售退货订单
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();		
		saleReturnOrderModel.setCode(code);
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		
		if (saleReturnOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + code);
		}		
		// 根据销售退货入库单号 和 销售退货编号 查询销售退货入库表
		SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();		
		saleReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());		
		saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);						
		if (saleReturnIdepotModel==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在code:"+code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在code:"+code);
		}
		
		// 修改销售退货入库
    	SaleReturnIdepotModel sReturnIdepotModel = new SaleReturnIdepotModel();   	
    	sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_012);//'状态   DRC("011","待入仓"),YRC("012","已入仓"),  
    	sReturnIdepotModel.setId(saleReturnIdepotModel.getId());
    	saleReturnIdepotDao.modify(sReturnIdepotModel);
    	
    	Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleReturnOrderModel.getInDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
    	
    	DepotMerchantRelMongo depotMerchantRelInfo = null;
		if(null!=saleReturnOrderModel.getOutDepotId()){
			// 查询该商家销售退出仓仓库的相关信息
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", saleReturnOrderModel.getMerchantId());
			depotMerchantRelParam.put("depotId", saleReturnOrderModel.getOutDepotId());
			depotMerchantRelInfo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (depotMerchantRelInfo == null || "".equals(depotMerchantRelInfo)) {
				LOGGER.error("未查到该商家下退出仓库信息,订单编号" + code);
				throw new RuntimeException("未查到该商家下退出仓库信息,订单编号" + code);
			}
		}
     	/**
     	 * 销售退货类型为代销、购销均适用逻辑：
		 *	1、如果入库仓库为香港仓时，不走以入定出；
		 *	2、出库仓库在对应商家仓库管理下的“以入定出”标识为“是”的,则正常已入定出生成退货出库单，扣减退出仓的出库量；
		 *	3、有出库仓要考虑走以入定出，无出库仓就不用以入定出
     	 */
     	if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2.equals(saleReturnOrderModel.getReturnType()) ||
     		DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(saleReturnOrderModel.getReturnType())){
     		if(!DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType()) &&
     		depotMerchantRelInfo!=null && DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1.equals(depotMerchantRelInfo.getIsInDependOut())){

    	//根据销售退货订单id  查询销售退货出库单
    	SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();		
		saleReturnOdepotModel.setOrderId(saleReturnOrderModel.getId());// 销售退货的订单号 订单号		
		saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
    	    	   	
    	//修改销售退货订单状态
    	SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();
    	//判断是否已经出仓 	
		if(saleReturnOdepotModel!=null){
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);
		}else{
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_012);
		}
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderDao.modify(saleReturnOrderModel1);	
		}
	}
		return true;
	}
}
