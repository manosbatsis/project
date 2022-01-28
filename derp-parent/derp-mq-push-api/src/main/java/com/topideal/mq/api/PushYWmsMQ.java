package com.topideal.mq.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.enums.PushYwmsTypeEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.XmlConverUtil;
import com.topideal.http.HttpClientUtil;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.tools.QimenSignUtils;

/**
 * 推送报文给众邦云仓
 * 
 */
@Component
public class PushYWmsMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger logger= LoggerFactory.getLogger(PushYWmsMQ.class);

    public PushYWmsMQ(){
        super.setTags(MQPushApiEnum.PUSH_YWMS.getTags());
        super.setTopics(MQPushApiEnum.PUSH_YWMS.getTopic());
    }


    @Override
    @SystemServiceLog(point="10000",model="经分销-->众邦云仓")
    public String execute(String json,String keys,String topics,String tags)throws Exception{
    	logger.info("===================异步推送众邦云仓接口==================");
        //返回接口报文
        String returnXML=null;
        
        com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(json) ;
        
        if(jsonObject.get("type") == null ||
        		jsonObject.get("jsonStr") == null) {
        	throw new RuntimeException("参数有误，请检查参数") ;
        }
        
        String type = jsonObject.getString("type");
        String jsonStr = jsonObject.getString("jsonStr") ;
        logger.info("请求: "+ jsonStr);
        
        //json 转 xml
        String xml = XmlConverUtil.JsonToXml(jsonStr) ;
        
        String method = "" ;
        if(PushYwmsTypeEnum.CGRK.getType().equals(type)) {
        	method = PushYwmsTypeEnum.CGRK.getMethod() ;
        }else if(PushYwmsTypeEnum.JYCK.getType().equals(type)) {
        	method = PushYwmsTypeEnum.JYCK.getMethod() ;
        }else {
        	throw new RuntimeException("非推送众邦云仓类型");
        }
        
        //组装URL
        String ywmsApi = ApolloUtil.ywmsApi + "?" ;
        
        StringBuffer paramSB = new StringBuffer() ;
        paramSB = paramSB.append("app_key=").append(ApolloUtil.ywmsAppkey).append("&") ;
        paramSB = paramSB.append("format=").append("xml").append("&") ;
        paramSB = paramSB.append("method=").append(method).append("&") ;
        paramSB = paramSB.append("v=").append(ApolloUtil.ywmsV).append("&") ;
        paramSB = paramSB.append("sign_method=").append("md5").append("&") ;
        paramSB = paramSB.append("customerId=").append(ApolloUtil.ywmsCustomerId).append("&") ;
        paramSB = paramSB.append("timestamp=") ;
        
        String url = ywmsApi + paramSB.toString() ;
        
        //获取签名
        Map<String, String> map = QimenSignUtils.getParamsFromUrl(url);
        String sign = QimenSignUtils.sign(map, xml, ApolloUtil.ywmsSecret) ;
        
        paramSB = paramSB.append("&").append("sign=").append(sign) ;
        
        String paramUrl = paramSB.toString() ;
        
        url = ywmsApi + paramUrl ;
        
        logger.info("请求地址: "+ url);
        logger.info("请求转码: "+ url);

        returnXML = HttpClientUtil.doPostXml(url, xml, "UTF-8");
        logger.info("返回结果:"+returnXML);
        
        if(!returnXML.contains("success")) {
        	throw new RuntimeException(returnXML);
        }
        
        logger.info("===================异步推送众邦云仓接口结束==================");
        
        return returnXML;
    }


}
