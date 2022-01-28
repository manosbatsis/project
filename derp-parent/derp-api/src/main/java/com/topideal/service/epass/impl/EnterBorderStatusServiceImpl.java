package com.topideal.service.epass.impl;

import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchantInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.service.epass.EnterBorderStatusService;

import net.sf.json.JSONObject;
/**
 * 进境状态回推接口
 * @author 杨创
 *2018/6/5
 */
@Service
public class EnterBorderStatusServiceImpl implements EnterBorderStatusService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterBorderStatusServiceImpl.class);
	@Autowired
	private ApiSecretConfigDao apiSecretConfigDao;// api密钥配置
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	
	// 保存 进境状态回推信息
	@Override
	@SystemServiceLog(point="1205",model="进境状态回推接口")
	public JSONObject enterBorderStatusInfo(String json,Long merchantId) throws Exception {
		
		LOGGER.info("进境状态回推信息Service 请求开始json:"+json);
		// 实例化JSON对象
		JSONObject jsonData=JSONObject.fromObject(json);

		// 根据电商企业查询商家
		MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
		merchantInfoModel.setId(merchantId);
		merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);

		// 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();
        jsonMQData.put("externalCode", jsonData.getString("order_id"));//订单编号(外部单号)
        jsonMQData.put("declareDate", jsonData.getString("apply_time"));//申请时间--申报时间
		/*国检状态 11：已报国检12：国检放行13：国检审核驳回14：国检抽检15：国检抽检未过,海关状态 21：已报海关22：海关单证放行23：报海关失败24：海关查验/转人工/挂起等 25：海关单证审核不通过26：海关已接受申报，待货物运抵后处理41：海关货物查扣42：海关货物放行*/
        jsonMQData.put("status", jsonData.getString("status"));//放行状态
        jsonMQData.put("type", jsonData.getString("type"));//1:国检;2:海关。
		jsonMQData.put("merchantName", merchantInfoModel.getName());
        LOGGER.info(" 进境状态回推信息Service 请求结束json:"+jsonMQData);
		return jsonMQData;
	}

}
