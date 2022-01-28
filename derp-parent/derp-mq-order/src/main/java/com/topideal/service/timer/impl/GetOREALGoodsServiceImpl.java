package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.platformdata.OrealPurchaseOrderDao;
import com.topideal.dao.platformdata.OrealPurchaseOrderItemDao;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderItemModel;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.GetOREALPurchaseOrdersService;

import net.sf.json.JSONObject;

/**
 * 获取欧莱雅供应商商品
 * @author 杨创
 *
 */
@Service
public class GetOREALGoodsServiceImpl implements GetOREALPurchaseOrdersService {

	private static final Logger LOGGER= LoggerFactory.getLogger(GetOREALGoodsServiceImpl.class);


	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private OrealPurchaseOrderDao orealPurchaseOrderDao;
	@Autowired
	private OrealPurchaseOrderItemDao orealPurchaseOrderItemDao;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201140000,model=DERP_LOG_POINT.POINT_13201140000_Label,keyword="vordercode")
	public void saveOREALPurchaseOrders(String json,String keys,String topics,String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		
		LOGGER.info("----------获取欧莱雅采购订单--------开始"+json);
		Map<String, Object>merchantParamsMap=new HashMap<String, Object>();
		merchantParamsMap.put("topidealCode", "0000138");		
		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParamsMap);
		if (merchantInfo==null) {
			throw new RuntimeException("没有查询到宝信商家");
		}
		Map<String, Object>merchantBuParamsMap=new HashMap<String, Object>();
		merchantBuParamsMap.put("merchantId", merchantInfo.getMerchantId());		
		merchantBuParamsMap.put("buCode", "E070200");
		MerchantBuRelMongo merchantBuRel= merchantBuRelMongoDao.findOne(merchantBuParamsMap);
		if (merchantBuRel==null) {
			throw new RuntimeException("没有查询到事业部");
		}
		String vordercode = (String) jsonData.get("vordercode");		
		List<Object> objectList = (List<Object>) jsonData.get("item");

		if (objectList==null || objectList.size()==0) {
			throw new RuntimeException("没有对应的商品");
		}
		JSONObject jSONObject = (JSONObject) objectList.get(0);
		// 订单id不能为空
		if (vordercode==null||StringUtils.isBlank(vordercode)||"null".equals(vordercode)) {
			throw new RuntimeException("vordercode is null");
		}
		// 商品名称不能为空
		if (jSONObject.get("invname")==null||StringUtils.isBlank(jSONObject.getString("invname"))||"null".equals(jSONObject.getString("invname"))) {
			throw new RuntimeException("invname is null");
		}

		OrealPurchaseOrderModel orderModel=new OrealPurchaseOrderModel();
		orderModel.setVordercode(vordercode);
		orderModel = orealPurchaseOrderDao.searchByModel(orderModel);
		if (orderModel!=null) {
			throw new RuntimeException("订单已经存在");
		}
		OrealPurchaseOrderModel model=new OrealPurchaseOrderModel();
		model.setMerchantId(merchantInfo.getMerchantId());
		model.setMerchantName(merchantInfo.getName());
		model.setBuId(merchantBuRel.getBuId());
		model.setBuName(merchantBuRel.getBuName());
		model.setVordercode(jSONObject.getString("vordercode"));
		model.setSource(DERP_ORDER.OREAL_PURCHASE_ORDER_SOURCE_1);
		
		if (jSONObject.get("custname")!=null&&StringUtils.isNotBlank(jSONObject.getString("custname"))&&!"null".equals(jSONObject.getString("custname"))) {
			model.setCustname(jSONObject.getString("custname"));
		}
		if (jSONObject.get("vdef1")!=null&&StringUtils.isNotBlank(jSONObject.getString("vdef1"))&&!"null".equals(jSONObject.getString("vdef1"))) {
			model.setVdef1(jSONObject.getString("vdef1"));
		}
		if (jSONObject.get("dorderdate")!=null&&StringUtils.isNotBlank(jSONObject.getString("dorderdate"))&&!"null".equals(jSONObject.getString("dorderdate"))) {
			String dorderdate = jSONObject.getString("dorderdate");
			model.setDorderdate(TimeUtils.parse(dorderdate, "yyyy-MM-dd"));
		}
		if (jSONObject.get("vdef13")!=null&&StringUtils.isNotBlank(jSONObject.getString("vdef13"))&&!"null".equals(jSONObject.getString("vdef13"))) {
			model.setVdef13(jSONObject.getString("vdef13"));
		}
		if (jSONObject.get("adress")!=null&&StringUtils.isNotBlank(jSONObject.getString("adress"))&&!"null".equals(jSONObject.getString("adress"))) {
			model.setAdress(jSONObject.getString("adress"));
		}
		
		if (jSONObject.get("docname")!=null&&StringUtils.isNotBlank(jSONObject.getString("docname"))&&!"null".equals(jSONObject.getString("docname"))) {
			model.setDocname(jSONObject.getString("docname"));
		}
		if (jSONObject.get("vdef7")!=null&&StringUtils.isNotBlank(jSONObject.getString("vdef7"))&&!"null".equals(jSONObject.getString("vdef7"))) {
			model.setVdef7(jSONObject.getString("vdef7"));
		}
		Long orealPurchaseOrderId = orealPurchaseOrderDao.save(model);			


		for (Object object : objectList) {
			OrealPurchaseOrderItemModel itemModel=new OrealPurchaseOrderItemModel();
			itemModel.setOrealPurchaseOrderId(orealPurchaseOrderId);
			itemModel.setInvname(jSONObject.getString("invname"));
			if (jSONObject.get("invbasdoc")!=null&&StringUtils.isNotBlank(jSONObject.getString("invbasdoc"))&&!"null".equals(jSONObject.getString("invbasdoc"))) {
				itemModel.setInvbasdoc(jSONObject.getString("invbasdoc"));
			}
			if (jSONObject.get("cinvmecode")!=null&&StringUtils.isNotBlank(jSONObject.getString("cinvmecode"))&&!"null".equals(jSONObject.getString("cinvmecode"))) {
				itemModel.setCinvmecode(jSONObject.getString("cinvmecode"));
			}
			if (jSONObject.get("vdef5")!=null&&StringUtils.isNotBlank(jSONObject.getString("vdef5"))&&!"null".equals(jSONObject.getString("vdef5"))) {
				itemModel.setVdef5(Integer.valueOf(jSONObject.getString("vdef5")));
			}
			if (jSONObject.get("nordernum")!=null&&StringUtils.isNotBlank(jSONObject.getString("nordernum"))&&!"null".equals(jSONObject.getString("nordernum"))) {
				itemModel.setNordernum(Integer.valueOf(jSONObject.getString("nordernum")));
			}
			if (jSONObject.get("refsaleprice")!=null&&StringUtils.isNotBlank(jSONObject.getString("refsaleprice"))&&!"null".equals(jSONObject.getString("refsaleprice"))) {
				itemModel.setRefsaleprice(new BigDecimal(jSONObject.getString("refsaleprice")));
			}
			if (jSONObject.get("vmemo")!=null&&StringUtils.isNotBlank(jSONObject.getString("vmemo"))&&!"null".equals(jSONObject.getString("vmemo"))) {
				itemModel.setVmemo(jSONObject.getString("vmemo"));
			}
			//表体			
			orealPurchaseOrderItemDao.save(itemModel);
		}
		
		
		
		
	}


}
