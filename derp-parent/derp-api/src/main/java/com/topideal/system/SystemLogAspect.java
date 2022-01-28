package com.topideal.system;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.log.APILog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.mongo.dao.ApiSecretConfigMongoDao;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.ApiSecretConfigMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;


/**
 * 切点类
 * @author tiangai
 * @since 2014-08-05 Pm 20:35
 * @version 1.0
 */
@Aspect
@Component
public  class SystemLogAspect {

    @Autowired
    private RMQLogProducer rocketMQProducer;
	// 库存日志
	@Autowired
	private JSONMongoDao jsonMongoDao;
    @Autowired
    private ApiSecretConfigMongoDao apiSecretConfigMongoDao;// api密钥配置
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;

    /**
     * ApolloUtil的注入有时比切面注入晚导致取不到属性问题在此注入一下，确保在切面的前面注入
     * */
    @Autowired
    private ApolloUtil apolloUtil;


    //本地异常日志记录对象
    private  static  final Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect. class);


    //Controller层切点
    @Pointcut("@annotation(com.topideal.common.system.annotation.SystemControllerLog)")
    public  void controllerAspect() {

    }
    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut="controllerAspect()",returning="rvt")
    public  void doAfterReturning(JoinPoint joinPoint, Object rvt) {
        LOGGER.info("================后置通知开始  正常流程===============");
        //API日志实体
        APILog apiLog=new APILog();
        //请求报文
        JSONObject requestMessage=null;
        //请求头报文
        String headMessage=null;
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取session
        HttpSession session = request.getSession();
        try {
            //--------------日志基本信息-------------------------------
            //请求地址
            String url="http://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?"+request.getQueryString();
            //请求方法
            String requestMethod=joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()";
            //参数名集合
            String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                    if(paramNames[i].equals("headerMap")) {
                        Map<String,Object> headerMap=(HashMap)joinPoint.getArgs()[i];
                        JSONObject headerJson = new JSONObject();
                        headerJson.put("appkey",headerMap.get("appkey"));
                        headerJson.put("timestamp",headerMap.get("timestamp"));
                        headerJson.put("sign",headerMap.get("sign"));
                        headerJson.put("v",headerMap.get("v"));
                        headMessage=headerJson.toString();
                    }else if(paramNames[i].equals("json")){
                        requestMessage=JSONObject.fromObject(joinPoint.getArgs()[i]);
                    }
                }
            }
            //获取关键字
            if(StringUtils.isNotBlank(getControllerMethodKeyword(joinPoint))){
                //设置主键字值
                apiLog.setKeyword(requestMessage.getString(getControllerMethodKeyword(joinPoint)));
                //设置主键字段名
                apiLog.setKeywordName(getControllerMethodKeyword(joinPoint));
            }

            // 根据appkey 查询 配置  表获取商家信息
            String appKey = (String) requestMessage.get("app_key");// appKey
            String merchantName = "";
            if(StringUtils.isNotBlank(appKey)){
                Map<String,Object> queryMap = new HashMap<>();
                queryMap.put("appKey",appKey);
                ApiSecretConfigMongo configMongo = apiSecretConfigMongoDao.findOne(queryMap);
                queryMap.clear();
                if(configMongo!=null){
                    queryMap.put("merchantId",configMongo.getMerchantId());
                    MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(queryMap);
                    if(merchantInfoMongo!=null) apiLog.setMerchantName(merchantInfoMongo.getName());
                }
            }

            //实体类注入值
            apiLog.setUrl(url);
            //接口名称
            apiLog.setModel(getControllerMethodModel(joinPoint));
            //埋点
            apiLog.setPoint(Long.valueOf(getControllerMethodPoint(joinPoint)));
            //请求方法
            apiLog.setRequestMethod(requestMethod);
            //设置接收时间
            apiLog.setReceiveData(System.currentTimeMillis());
            //----------------响应报文
            if(rvt instanceof Map){
                JSONObject responseJson=(JSONObject)rvt;
                String derpCode= responseJson.getString("derp-code");
                apiLog.setDerpCode(derpCode);
                //主数据异常处理
                if("001".equals(derpCode)){
                    if("SUCCESS".equalsIgnoreCase(responseJson.getString("flag"))){
                        apiLog.setState(1);
                    }else{
                        apiLog.setState(0);
                        apiLog.setExpMsg(responseJson.getString("errInfo"));
                    }
                }else if("002".equals(derpCode)){
                    if("1".equalsIgnoreCase(responseJson.getString("status"))){
                        apiLog.setState(1);
                    }else{
                        apiLog.setState(0);
                        apiLog.setExpMsg(responseJson.getString("notes"));
                    }
                }else if("004".equals(derpCode)){
                	if("1001".equalsIgnoreCase(responseJson.getString("status"))){
                        apiLog.setState(1);
                    }else{
                        apiLog.setState(0);
                        apiLog.setExpMsg(responseJson.getString("msg"));
                    }
				}
                responseJson.remove("derp-code");
            }
            //--------------- 获取用户信息
            JSONObject jsonObject=JSONObject.fromObject(apiLog);
            //设置请求报文
            jsonObject.put("requestMessage",requestMessage);
            //响应报文
            jsonObject.put("responseMessage",rvt);
            jsonObject.put("endDate", System.currentTimeMillis());
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("moduleCode", LogModuleType.MODULE_API.getType());
            SendResult sendResult = rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_API.getTopic(),MQLogEnum.LOG_API.getTags());
            jsonObject.put("modulCode", "1005");
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+sendResult+"==");
            if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            	 LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
			}
            //推送到日志MQ平台
            /*String resultMsg=SmurfsUtils.sendLog(jsonObject,SmurfsAPIEnum.SMURFS_DERPLOG_REPORT);
            if (resultMsg==null) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
			}else{
				JSONObject resultObject=JSONObject.fromObject(resultMsg);
				if (resultObject.get("state")==null||!"1".equals(String.valueOf(resultObject.get("state")))) {
					jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());	
				}				
			}
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+resultMsg+"==");
            System.out.println(resultMsg);*/
            LOGGER.info("================后置通知结束   正常流程===============");
        }  catch (Exception e) {
            //记录本地异常日志
            LOGGER.error("================后置通知  保存日志异常！！！===============");
            LOGGER.error("异常信息:{}", e.getMessage());
        }
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LOGGER.info("================后置异常通知  抛出异常===============");
        //API日志实体
        APILog apiLog=new APILog();
        //设置状态为失败
        apiLog.setState(0);
        //失败原因
        apiLog.setExpMsg(e.getMessage());
        //请求报文
        JSONObject requestMessage=null;
        //请求头报文
        String headMessage=null;
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取session
        HttpSession session = request.getSession();
        try {
            //--------------日志基本信息-------------------------------
            //请求地址
            String url="http://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?"+request.getQueryString();
            //请求方法
            String requestMethod=joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()";
            //参数名集合
            String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                    if(paramNames[i].equals("headerMap")) {
                        Map<String,Object> headerMap=(HashMap)joinPoint.getArgs()[i];
                        JSONObject headerJson = new JSONObject();
                        headerJson.put("appkey",headerMap.get("appkey"));
                        headerJson.put("timestamp",headerMap.get("timestamp"));
                        headerJson.put("sign",headerMap.get("sign"));
                        headerJson.put("v",headerMap.get("v"));
                        headMessage=headerJson.toString();
                    }else if(paramNames[i].equals("json")){
                        requestMessage=JSONObject.fromObject(joinPoint.getArgs()[i]);
                    }
                }
            }
            //获取关键字
            if(StringUtils.isNotBlank(getControllerMethodKeyword(joinPoint))){
                //设置主键字值
                apiLog.setKeyword(requestMessage.getString(getControllerMethodKeyword(joinPoint)));
                //设置主键字段名
                apiLog.setKeywordName(getControllerMethodKeyword(joinPoint));
            }

            // 根据appkey 查询 配置  表获取商家信息
            String appKey = (String) requestMessage.get("app_key");// appKey
            String merchantName = "";
            if(StringUtils.isNotBlank(appKey)){
                Map<String,Object> queryMap = new HashMap<>();
                queryMap.put("appKey",appKey);
                ApiSecretConfigMongo configMongo = apiSecretConfigMongoDao.findOne(queryMap);
                if(configMongo!=null){
                    queryMap.clear();
                    queryMap.put("merchantId",configMongo.getMerchantId());
                    MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(queryMap);
                    if(merchantInfoMongo!=null) apiLog.setMerchantName(merchantInfoMongo.getName());
                }
            }

            //实体类注入值
            apiLog.setUrl(url);
            //接口名称
            apiLog.setModel(getControllerMethodModel(joinPoint));
            //埋点
            apiLog.setPoint(Long.valueOf(getControllerMethodPoint(joinPoint)));
            //请求方法
            apiLog.setRequestMethod(requestMethod);
            //设置接收时间
            apiLog.setReceiveData(System.currentTimeMillis());
            //API日志报文
            JSONObject jsonObject=JSONObject.fromObject(apiLog);
            //设置请求报文
            jsonObject.put("requestMessage",requestMessage);
            //响应报文
            jsonObject.put("responseMessage",e.getMessage());
            jsonObject.put("endDate", System.currentTimeMillis());
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("moduleCode", LogModuleType.MODULE_API.getType());
            SendResult sendResult = rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_API.getTopic(),MQLogEnum.LOG_API.getTags());
            jsonObject.put("modulCode", "1005");
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+sendResult+"==");
            //推送到日志MQ平台
            /*String resultMsg=SmurfsUtils.sendLog(jsonObject,SmurfsAPIEnum.SMURFS_DERPLOG_REPORT);
            if (resultMsg==null) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
			}else{
				JSONObject resultObject=JSONObject.fromObject(resultMsg);
				if (resultObject.get("state")==null||!"1".equals(String.valueOf(resultObject.get("state")))) {
					jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());	
				}				
			}
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+resultMsg+"==");
            System.out.println(resultMsg);*/
            if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            	 LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
			}
            
            LOGGER.info("================后置异常通知  抛出异常===============");
        }  catch (Exception ex) {
            //记录本地异常日志
            LOGGER.error("================后置异常通知   保存日志异常！！！===============");
            LOGGER.error("异常信息:{}", ex.getMessage());
        }
         /*==========记录本地异常日志==========*/
            LOGGER.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage());
    }



    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public  static String getControllerMethodKeyword(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog. class).keyword();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public  static String getControllerMethodModel(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog. class).model();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public  static String getControllerMethodPoint(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog. class).point();
                    break;
                }
            }
        }
        return description;
    }

}