package com.topideal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.dao.ConsumeMonitorOrderDao;
import com.topideal.entity.vo.ConsumeMonitorOrderModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.MqConsumeFailService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * MQ消费失败列表-发送邮件
 */
@Service
public class MqConsumeFailServiceImpl implements MqConsumeFailService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MqConsumeFailServiceImpl.class);
	// MQ消费监控
	@Autowired
	private ConsumeMonitorOrderDao consumeMonitorOrderDao;
		
	@Override
	public boolean sendMail(String json, String topics, String tags) throws Exception {
		LOGGER.info("-----------------MQ消费失败列表-发送邮件开始----------------------");
		//业务参数=========================
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mailCode",SmurfsAPICodeEnum.EMAIL_M011.getCode());
        JSONObject paramJson=new JSONObject();
        //MQ消费失败列表
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		date = calendar.getTime();
        String StartDate = sdf.format(date)+" 00:00:00";
        String endDate = sdf.format(date)+" 23:59:59";
        List<ConsumeMonitorOrderModel> list = consumeMonitorOrderDao.getConsumeFailList(StartDate,endDate);
        if(list== null || list.size()<50){
        	return false;
        }
        Map<String,Integer> map = new HashMap<String,Integer>();
        for(ConsumeMonitorOrderModel cmodel:list){
        	Integer errorNum = 1;
        	if(map.containsKey(cmodel.getModel()+"_"+cmodel.getPoint())){
        		errorNum += map.get(cmodel.getModel()+"_"+cmodel.getPoint());
        	}
        	map.put(cmodel.getModel()+"_"+cmodel.getPoint(), errorNum);
        }
        for(Map.Entry<String,Integer> entry:map.entrySet()){
        	String keys = entry.getKey();
        	String[] keyArr = keys.split("_");
        	JSONObject jsonModel=new JSONObject();
            jsonModel.put("point",keyArr[1]);
            jsonModel.put("modelName",keyArr[0]);
            jsonModel.put("errorNum",entry.getValue());
            jsonArray.add(jsonModel);	
        }
        paramJson.put("list",jsonArray);
        jsonObject.put("paramJson",paramJson);
        System.out.println("------paramJson:"+paramJson);
        jsonObject.put("recipients",ApolloUtil.smurfsRecipients);
        //调用外部接口发送邮件
        String resultMsg=SmurfsUtils.send(jsonObject,SmurfsAPIEnum.SMURFS_EMAIL);
        System.out.println(resultMsg);
		LOGGER.info("-----------------MQ消费失败列表-发送邮件结束----------------------");
		return true;
	}

}
