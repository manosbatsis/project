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
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnOdepotDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSTTransfersOutStoragePushBackService;

import net.sf.json.JSONObject;
/**
 * 调拨出库库存加减成功回推
 * 2019/02/27
 * 杨创
 */
@Service
public class XSTTransfersOutStoragePushBackServiceImpl implements XSTTransfersOutStoragePushBackService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTTransfersOutStoragePushBackServiceImpl.class);
	
	@Autowired 
	private SaleReturnOrderDao saleReturnOrderDao;//销售退货订单
	@Autowired 
	private SaleReturnOdepotDao saleReturnOdepotDao;//销售退货出库		
	@Autowired 
	private SaleReturnIdepotDao saleReturnIdepotDao;//销售退货入库	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201153501,model=DERP_LOG_POINT.POINT_13201153501_Label,keyword="code")
	public boolean updateXSTTransfersOutStoragePushBackInfo(String json,String keys,String topics,String tags) throws Exception {
		// 说明无论是否是菜鸟仓 code 都是销售退货订单的code
		// 将字符串转成json
		Thread.sleep(50);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
				
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
		saleReturnOrderModel.setCode(code);
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);		
		if (saleReturnOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的订单,订单号:" + code);
		}
			
		// 根据销售退货订单id 销售销售退货出库单
		SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();		
		saleReturnOdepotModel.setOrderId(saleReturnOrderModel.getId());// 销售退货的订单号 订单号		
		saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);		
		if(saleReturnOdepotModel == null){
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售退货出库表不存在,销售退货订单号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售退货出库表不存在,销售退货订单号code:" + code);
		}
		// 修改销售退货出库订单
		SaleReturnOdepotModel sReturnOdepotModel = new SaleReturnOdepotModel();
		sReturnOdepotModel.setId(saleReturnOdepotModel.getId());
		sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_016);//"011","待入仓",012","已入仓""016","已出仓"
		saleReturnOdepotDao.modify(sReturnOdepotModel);
		
		// 根据销售退货订单id查询销售退货入库订单
		SaleReturnIdepotModel sReturnIdepotModel=new SaleReturnIdepotModel();
		sReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());
		sReturnIdepotModel = saleReturnIdepotDao.searchByModel(sReturnIdepotModel);
		//修改销售退货订单状态
    	SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();
		//判断销售退货入库单不为空 销售退货订单状态修改为已完结
		if(sReturnIdepotModel!=null){
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);
		}else{
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_016);	// "016","已出仓"
		}
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderDao.modify(saleReturnOrderModel1);
		return true;
	}
}
