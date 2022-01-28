package com.topideal.service.api.sale.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.OrderDao;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.json.api.v3_2.ESaleEnterBorderStatusJson;
import com.topideal.service.api.sale.DSEnterBorderStatusService;

import net.sf.json.JSONObject;
/**
 * 进境状态回推接口
 */
@Service
public class DSEnterBorderStatusServiceImpl implements DSEnterBorderStatusService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DSEnterBorderStatusServiceImpl.class);
	@Autowired
	private OrderDao orderDao;// 电商订单
	
	// 保存 进境状态回推信息
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203120400,model=DERP_LOG_POINT.POINT_12203120400_Label,keyword="externalCode")
	public boolean saveEnterBorderStatusInfo(String json,String keys,String topics,String tags) throws Exception {

		JSONObject jsonData=JSONObject.fromObject(json);	
		
		// JSON对象转实体
		ESaleEnterBorderStatusJson rootJson = (ESaleEnterBorderStatusJson) JSONObject.toBean(jsonData, ESaleEnterBorderStatusJson.class);
		// 电商订单
		OrderModel oModel =new OrderModel();		
		String externalCode = rootJson.getExternalCode();
		// 首先 根据外部单号查询电商订单 看看电商订单是否存在
		OrderModel orderModel =new OrderModel();
		orderModel.setExternalCode(externalCode);//  外部单号
		orderModel.setMerchantId(rootJson.getMerchantId());
		orderModel = orderDao.searchByModel(orderModel);		
		if (null==orderModel) {
			LOGGER.error(DERP.MQ_FAILTYPE_01 + "电商订单不存在,外部单号"+externalCode);
			throw new RuntimeException(DERP.MQ_FAILTYPE_01 + "电商订单不存在,外部单号"+externalCode);
		}	
		String type =  rootJson.getType();//1:国检;2:海关		
		/*国检状态 11：已报国检12：国检放行13：国检审核驳回14：国检抽检15：国检抽检未过,海关状态 21：已报海关22：海关单证放行23：报海关失败24：海关查验/转人工/挂起等 25：海关单证审核不通过26：海关已接受申报，待货物运抵后处理41：海关货物查扣42：海关货物放行*/
		String status = rootJson.getStatus();
		// 根据 回推过来的类型判断是推的是海关还是国检
		if ("1".equals(type)) {
			oModel.setInspectStatus(status);// 国检
		}else if ("2".equals(type)) {
			oModel.setCustomsStatus(status);// 海关
		}
		// 待放行
		if ("1".equals(orderModel.getStatus())) {
			//oModel.setStatus("2");// 1:待申报 2.待放行,3:待发货,4:已发货,5:已取消'
			String declareDate = rootJson.getDeclareDate();
			if (declareDate.length()==10) {
				oModel.setDeclareDate(Timestamp.valueOf(declareDate+" 00:00:00"));//申报时间
			}else{
				oModel.setDeclareDate(Timestamp.valueOf(declareDate));//申报时间
			}
			
		}
		//已报国检12  已报海关22 电商订单改为待发货(如果推过来的是已报国检 只需判断 查询的电商订单是否是已报海关)
		if ("12".equals(status)&&"22".equals(orderModel.getCustomsStatus())) {
			oModel.setReleaseDate(new Timestamp(new Date().getTime()));// 放行时间
			// 如果订单已经发货 或者已经取消在或者处理中推进境状态不会改变订单发货状态
			if ("4".equals(orderModel.getStatus())||"5".equals(orderModel.getStatus())||DERP_ORDER.ORDER_STATUS_027.equals(orderModel.getStatus())) {
				//oModel.setStatus(orderModel.getStatus());
			}else {
				//oModel.setStatus("3");// 1:待申报 2.待放行,3:待发货,4:已发货,5:已取消'
			}
																		
		}
		//已报国检12  已报海关22 电商订单改为待发货(如果推过来的是已报海关 只需判断 查询的电商订单是否是已报国检)
		if ("22".equals(status)&&"12".equals(orderModel.getInspectStatus())) {
			oModel.setReleaseDate(new Timestamp(new Date().getTime()));// 放行时间
			// 如果订单已经发货 或者已经取消在或者处理中推进境状态不会改变订单发货状态
			if ("4".equals(orderModel.getStatus())||"5".equals(orderModel.getStatus())||DERP_ORDER.ORDER_STATUS_027.equals(orderModel.getStatus())) {
				//oModel.setStatus(orderModel.getStatus());
			}else {
				//oModel.setStatus("3");// 1:待申报 2.待放行,3:待发货,4:已发货,5:已取消'
			}			
		}
		// 修改订单状态
		oModel.setId(orderModel.getId());// 电商订单id
		int modify = orderDao.modify(oModel);
		return true;
	}

}
