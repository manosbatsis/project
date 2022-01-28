package com.topideal.service.timer.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.EmailConfigMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.EmailConfigMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.GrabDerpPurchaseOrderService;

import net.sf.json.JSONObject;

/**
 * 抓取经分销收到发票没有付款的采购订单
 * 
 * @author 杨创 2018/10/19
 */
@Service
public class GrabDerpPurchaseOrderServiceImpl implements GrabDerpPurchaseOrderService {
	@Autowired	
	private PurchaseOrderDao purchaseOrderDao;// 采购订单
	@Autowired	
	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家
	@Autowired	
	private EmailConfigMongoDao emailConfigMongoDao;//
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(GrabDerpPurchaseOrderServiceImpl.class);	

	
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201114800,model=DERP_LOG_POINT.POINT_13201114800_Label)
	public boolean saveGrabDerpPurchaseOrder(String json, String keys, String topics, String tags) throws Exception {
		LOGGER.info("抓取经分销收到发票没有付款的采购订单json:"+json);
		// 查询启用状态下邮件配置表
		Map<String, Object>params = new HashMap<>();
		params.put("status", DERP_SYS.EMAILCONFIG_STATUS_1);
		List<EmailConfigMongo> findAllList = emailConfigMongoDao.findAll(params);
		if (findAllList==null||findAllList.size()==0) {
			LOGGER.info("mongdb邮件发送配置表 ,没有启用邮件配置信息");
			return true;						
		}
		// 获取系统当前时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String nowStr = sdf.format(date);// 系统当前时间字符串 格式年月日				
		Date now = sdf.parse(nowStr);// 系统当前时间 
		for (EmailConfigMongo emailConfigMongo : findAllList) {
			Long merchantId = emailConfigMongo.getMerchantId();// 商家id
			Long supplierId = emailConfigMongo.getCustomerId();// 供应商id 说明邮件配置表存的都是供应商
			List<PurchaseOrderModel> purchaseOrderList = purchaseOrderDao.getGrabDerpPurchaseOrder(merchantId,supplierId);
			Integer accountPeriodDay = emailConfigMongo.getAccountPeriodDay();// 账期时间
			Long sum=Long.valueOf(accountPeriodDay);// 账期时间  之前是默认写死19天
			String accountUnitType = emailConfigMongo.getAccountUnitType();//账期单位 1 自然日, 2工作日
			String reminderUnitType = emailConfigMongo.getReminderUnitType();//提醒单位 1 自然日, 2工作日
			String advanceReminderDay = emailConfigMongo.getAdvanceReminderDay();//提前提醒天数  多个用英文逗号隔开
			List<String> advanceReminderDayList = Arrays.asList(advanceReminderDay.split(","));
			// 查询商家
			Map<String, Object>merchantParams = new HashMap<>();
			merchantParams.put("merchantId", merchantId);
			MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantParams);
			
			for (PurchaseOrderModel purchaseOrderModel : purchaseOrderList) {
				Timestamp drawInvoiceDate = purchaseOrderModel.getDrawInvoiceDate();// 开发票时间
				String drawInvoiceDateStr = sdf.format(drawInvoiceDate);// 开发票时间 年月日
				Timestamp endPaytime=null;// 最终付款时间 日期格式
				String endPaytimeStr=null;// 最终付款时间 字符串 年月日
				if (DERP_SYS.EMAILCONFIG_UNITTYPE_1.equals(accountUnitType)) {// 账期单位自然日
					Long endPaytimeLong=drawInvoiceDate.getTime()+(24*3600*1000)*sum;// 最后付款日期时间戳
					endPaytime= new Timestamp(endPaytimeLong);	//最终付款时间 日期格式
					endPaytimeStr = sdf.format(endPaytime);// 最终付款时间 字符串
				}else {//账期单位工作日
					endPaytimeStr = TimeUtils.getAfterDay(drawInvoiceDate, accountPeriodDay);// 最终付款时间 
					endPaytime=Timestamp.valueOf(endPaytimeStr+" 00:00:00");//最终付款时间 日期格式
				}
				// 发送邮件map 
				Map<String, String> emailMap=new HashMap<>();
				// 提醒天数遍历
				for (String advanceReminderDayStr : advanceReminderDayList) {
					String emailDayStr=null;// 发送邮件日期
					int beforeDay = Integer.valueOf(advanceReminderDayStr);					
					if (DERP_SYS.EMAILCONFIG_UNITTYPE_1.equals(reminderUnitType)) {// 提前天数自然日
						Long emailDayLong=endPaytime.getTime()-(24*3600*1000)*beforeDay;
						emailDayStr = sdf.format(emailDayLong);
					}else{//提前天数工作日
						emailDayStr = TimeUtils.getBeforeDay(endPaytime, beforeDay);// 发送邮件日期
					}
					emailMap.put(emailDayStr,advanceReminderDayStr);
				}
				// 如果发送日期中 包含 当前时间 则发送邮件
				if (emailMap.containsKey(nowStr)) {
					Long timeSub = Long.valueOf(emailMap.get(nowStr));//距离最后付款时间还剩余天数					
					
					JSONObject sendMailJson = new JSONObject();
					sendMailJson.put("mailCode", SmurfsAPICodeEnum.EMAIL_M018.getCode());// 邮件编码
					if (StringUtils.isNotBlank(merchantInfoMongo.getFinancePayEmail())) {
						sendMailJson.put("recipients","chuang.yang@topideal.com.cn;"+merchantInfoMongo.getFinancePayEmail());// 收件人
					}else {
						sendMailJson.put("recipients","chuang.yang@topideal.com.cn");// 收件人
					}
					
					JSONObject paramJson = new JSONObject();	
					paramJson.put("merchantName", merchantInfoMongo.getName());// 商家名称
					paramJson.put("orderCode", purchaseOrderModel.getCode());// 采购单号
					if (StringUtils.isNotBlank(purchaseOrderModel.getPoNo())) {
						paramJson.put("poNo", purchaseOrderModel.getPoNo());// po号
					}else {
						paramJson.put("poNo", " ");// po号
					}		
					paramJson.put("endPayTime",endPaytimeStr);// 最终付款时间
					if (DERP_SYS.EMAILCONFIG_UNITTYPE_1.equals(reminderUnitType)) {//自然日
						paramJson.put("reminderUnitType","自然日");
					}else {
						paramJson.put("reminderUnitType","工作日");
					}
					paramJson.put("remainTme", timeSub);	// 剩余付款时间
					sendMailJson.put("paramJson", paramJson);// 邮件模板参数  json
					// 修改采购订单
					PurchaseOrderModel pModel= new PurchaseOrderModel();
					if (StringUtils.isNotBlank(purchaseOrderModel.getMailStatus())) {
						String mailStatus = purchaseOrderModel.getMailStatus();
						Integer mailStatusInt = Integer.valueOf(mailStatus);
						mailStatusInt=mailStatusInt+1;
						pModel.setMailStatus(String.valueOf(mailStatusInt));
					}else {
						pModel.setMailStatus(DERP_SYS.EMAILCONFIG_STATUS_1);
					}
					pModel.setId(purchaseOrderModel.getId());
					purchaseOrderDao.modify(pModel);
					String send = SmurfsUtils.send(sendMailJson,SmurfsAPIEnum.SMURFS_EMAIL);
					
				}
			}
		}
						
		return true;
	}
	
			
	
}
