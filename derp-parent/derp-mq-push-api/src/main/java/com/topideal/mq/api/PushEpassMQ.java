package com.topideal.mq.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.http.HttpOPUtil;
import com.topideal.mongo.dao.ApiSecretConfigMongoDao;
import com.topideal.mongo.entity.ApiSecretConfigMongo;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.tools.EpassSignUtils;

import net.sf.json.JSONObject;

/**
 * 推送报文给跨境宝
 * 
 */
@Component
public class PushEpassMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger logger= LoggerFactory.getLogger(PushEpassMQ.class);
    @Autowired
    private ApiSecretConfigMongoDao ApiSecretConfigMongoDao;

    public PushEpassMQ(){
        super.setTags(MQPushApiEnum.PUSH_EPASS.getTags());
        super.setTopics(MQPushApiEnum.PUSH_EPASS.getTopic());
    }


    @Override
    @SystemServiceLog(point="10000",model="经分销-->跨境宝")
    public String execute(String json,String keys,String topics,String tags)throws Exception{
    	logger.info("===================异步推送跨境宝接口==================");
        //返回接口报文
        String returnJson=null;
        JSONObject jsonObject= JSONObject.fromObject(json);
      //用卓志编码去匹配商家
        String topidealCode =  jsonObject.getString("topideal_code");
        Map<String,Object> params = new HashMap<>();
        params.put("topidealCode", topidealCode);  
        ApiSecretConfigMongo mongo = ApiSecretConfigMongoDao.findOne(params);
        if(mongo==null){
            throw new  NullPointerException("卓志编码："+topidealCode+",找不到对应的appKey信息");
        }
        jsonObject.put("v", ApolloUtil.epassV);
        jsonObject.put("app_key",mongo.getAppKey());
        jsonObject.put("timestamp", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            String sign = EpassSignUtils.signForOuter(jsonObject.toString(), mongo.getAppSecret());
            jsonObject.put("sign", sign);
            logger.info("请求: "+ jsonObject.toString());
            String jsonStr = jsonObject.toString();
            // 防止中文乱码转码和特殊字符
            String encode = URLEncoder.encode(jsonStr, "utf-8");
            logger.info("请求地址: "+ ApolloUtil.epassApi);
            logger.info("请求转码: "+ encode);
            returnJson = HttpOPUtil.doPost(ApolloUtil.epassApi,encode, "utf-8");
            logger.info("返回结果:"+returnJson);
            //返回结果解析
            JSONObject responseJson=JSONObject.fromObject(returnJson);
            if(responseJson==null) throw new RuntimeException(returnJson);             
            Object status = responseJson.get("status");
            if (status==null||StringUtils.isBlank(status.toString())) {
            	 throw new RuntimeException(returnJson); 
			}
            if (!"1".equals(status.toString())) {
            	throw new RuntimeException(returnJson);
			}

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnJson;
    }



}
