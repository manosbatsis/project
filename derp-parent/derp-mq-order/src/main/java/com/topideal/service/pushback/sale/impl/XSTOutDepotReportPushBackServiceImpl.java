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
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleReturnDeclareOrderDao;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnOdepotDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderModel;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSTOutDepotReportPushBackService;

import net.sf.json.JSONObject;
/**
 * 销售退出库打托减库存成功回推
 * @author wenyan
 *
 */
@Service
public class XSTOutDepotReportPushBackServiceImpl implements XSTOutDepotReportPushBackService{
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTOutDepotReportPushBackServiceImpl.class);
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;// 销售退
	@Autowired
	private SaleReturnOdepotDao saleReturnOdepotDao;// 销售退出库
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货入库单
	@Autowired 
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao;//销售退预申报
	
	// 销售退入库
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201156600,model=DERP_LOG_POINT.POINT_13201156600_Label,keyword="code")
	public boolean updateXSTOutDepotReportPushBack(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串转成json 
		Thread.sleep(50);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");	// 销售退货出库单号
		
		// 销售退出库
		SaleReturnOdepotModel sReturnOdepotModel = new SaleReturnOdepotModel();
		sReturnOdepotModel.setCode(code);
		sReturnOdepotModel = saleReturnOdepotDao.searchByModel(sReturnOdepotModel);
		if (sReturnOdepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货出库单,退货出库单号:code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货出库单,退货出库单号:code" + code);
		}
		// 销售退货订单
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();		
		saleReturnOrderModel.setCode(sReturnOdepotModel.getOrderCode());
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		
		if (saleReturnOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + sReturnOdepotModel.getOrderCode());
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + sReturnOdepotModel.getOrderCode());
		}
		// 修改销售退货出库单状态
		SaleReturnOdepotModel model = new  SaleReturnOdepotModel();
		model.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_016); // 016-已出仓
		model.setId(sReturnOdepotModel.getId());
		saleReturnOdepotDao.modify(model);
		
		// 修改销售退货订单状态
		SaleReturnOrderModel saleReturnOrderModel1 = new  SaleReturnOrderModel();
		
		//根据销售退货订单id  查询销售退货出库单
    	SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();		
		saleReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());// 销售退货的订单号 订单号		
		saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);    	
    	//判断是否已经入仓 	
		if(saleReturnIdepotModel != null && DERP_ORDER.SALERETURNIDEPOT_STATUS_012.equals(saleReturnIdepotModel.getStatus())){
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);//007-已完结
			
			//若存在预申报单，是否全部都已完结，修改预申报单状态
			if(DERP_ORDER.SALEDECLARE_ISGENERATE_1.equals(saleReturnOrderModel.getIsGenerateDeclare()) ) {				
		    	SaleReturnDeclareOrderDTO declareDTO = new SaleReturnDeclareOrderDTO();
		    	declareDTO.setSaleReturnOrderIds(saleReturnOrderModel.getId().toString());
		    	declareDTO = saleReturnDeclareOrderDao.searchDetail(declareDTO);
		    	if(declareDTO != null) {
		    		boolean flag = true;
		    		//根据销售退预申报id  查询所有销售退货出库单		    		
		    		for(String returnId : declareDTO.getSaleReturnOrderIds().split(",")) {		    			
		    			//遍历预申报关联的销售退对应的销售退入库单
		    			SaleReturnIdepotModel querySaleReturnIdepotModel = new SaleReturnIdepotModel();		
		    			querySaleReturnIdepotModel.setOrderId(Long.valueOf(returnId));// 销售退货的订单号 订单号	
		    			querySaleReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_012);
		    			querySaleReturnIdepotModel = saleReturnIdepotDao.searchByModel(querySaleReturnIdepotModel);
		    			if(querySaleReturnIdepotModel == null) {
		    				flag = false;
		    				break;
		    			}
		    			//遍历预申报关联的销售退对应的销售退出库单
		    			SaleReturnOdepotModel querySaleReturnOdepotModel = new SaleReturnOdepotModel();		
		    			querySaleReturnOdepotModel.setOrderId(Long.valueOf(returnId));// 销售退货的订单号 订单号	
		    			querySaleReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_016);
		    			querySaleReturnOdepotModel = saleReturnOdepotDao.searchByModel(querySaleReturnOdepotModel);
		    			if(querySaleReturnOdepotModel == null) {
		    				flag = false;
		    				break;
		    			}
		    		}
		    		if(flag) {
		    			SaleReturnDeclareOrderModel declareModel = new SaleReturnDeclareOrderModel();
			    		declareModel.setId(declareDTO.getId());
			    		declareModel.setStatus(DERP_ORDER.SALERETURNDECLARE_STATUS_005);//已完结
			    		declareModel.setModifyDate(TimeUtils.getNow());
			    		saleReturnDeclareOrderDao.modify(declareModel);
		    		}
		    	}
			}
		}else{
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_016);//016-已出仓
		}
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderDao.modify(saleReturnOrderModel1);
		
		return true;
	}
}
