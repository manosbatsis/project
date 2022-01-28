package com.topideal.system;


import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.entity.ReportMonitorMongo;
import com.topideal.tools.CollectionEnum;
import net.sf.json.JSONObject;
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

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 切点类
 * @author zhanghx
 * @version 1.0
 */
@Aspect
@Component
public  class SystemLogAspect {

    //本地异常日志记录对象
    private  static  final Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect. class);

    @Autowired
    private RMQLogProducer rocketMQProducer;
	// 库存日志
	@Autowired
	private JSONMongoDao jsonMongoDao;
    
    /**
     * ApolloUtil的注入有时比切面注入晚导致取不到属性问题在此注入一下，确保在切面的前面注入
     * */
    @Autowired
    private ApolloUtil apolloUtil;

    //Service层切点
    @Pointcut("@annotation(com.topideal.common.system.annotation.SystemServiceLog)")
    public  void serviceAspect() {
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut="serviceAspect()",returning="rvt")
    public  void doAfterReturning(JoinPoint joinPoint,Object rvt) {
        LOGGER.info("==报表MQ 后置通知开始==");
        //数据集
        MQLog mqLog=new MQLog();
        //请求报文
        JSONObject requestMessage=null;
        try {
            String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                    if(paramNames[i].equals("json")){
                        requestMessage=JSONObject.fromObject(joinPoint.getArgs()[i]);
                    }else if(paramNames[i].equals("keys")){
                        mqLog.setStartDate(Long.valueOf((String) joinPoint.getArgs()[i]));
                    }
                }
            }
            //消费成功
            mqLog.setKeyword("R"+ TimeUtils.getNow().getTime());
            mqLog.setDesc(getControllerMethodModel(joinPoint));
            mqLog.setModel(getControllerMethodModel(joinPoint));
            mqLog.setPoint(Long.valueOf(getControllerMethodPoint(joinPoint)));
            //结束时间
            mqLog.setEndDate(System.currentTimeMillis());
            //消费状态
            mqLog.setState(1);
            //mongodb 日志记录报文
            JSONObject jsonObject=JSONObject.fromObject(mqLog);
            //设置请求报文
            jsonObject.put("requestMessage",requestMessage);
            //执行业务，返回的结果集
            jsonObject.put("returnMessage",rvt);
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("moduleCode", LogModuleType.MODULE_REPORT.getType());
            jsonObject.put("modulCode", "1002");
            //推送到日志MQ平台
            SendResult sendResult = rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+sendResult+"==");
            if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            	 LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
			}
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
            LOGGER.info("==MQ 后置通知结束==");
        }  catch (Exception e) {
        	e.printStackTrace();
            //记录本地异常日志
            LOGGER.error("==前置通知异常==");
            LOGGER.error("异常信息:{}", e.getMessage());
        }
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LOGGER.info("==报表MQ 后置通知 异常 开始==");
        //数据集
        MQLog mqLog=new MQLog();
        //请求报文
        JSONObject requestMessage=null;
        try {
            //参数名集合
            String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            //请求方法
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                    if(paramNames[i].equals("json")){
                        requestMessage=JSONObject.fromObject(joinPoint.getArgs()[i]);
                    }else if(paramNames[i].equals("keys")){
                        mqLog.setStartDate(Long.valueOf((String) joinPoint.getArgs()[i]));
                    }
                }
            }
            //消费成功
            mqLog.setKeyword("R"+ TimeUtils.getNow().getTime());
            mqLog.setDesc(getControllerMethodModel(joinPoint));
            mqLog.setModel(getControllerMethodModel(joinPoint));
            mqLog.setPoint(Long.valueOf(getControllerMethodPoint(joinPoint)));
            //结束时间
            mqLog.setEndDate(System.currentTimeMillis());
            //消费状态
            mqLog.setState(0);
            mqLog.setExpMsg("异常信息："+e.getMessage());
            JSONObject jsonObject=JSONObject.fromObject(mqLog);
            //请求报文
            jsonObject.put("requestMessage",requestMessage);
            //设置响应报文
            jsonObject.put("returnMessage",e.getMessage());
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("moduleCode", LogModuleType.MODULE_REPORT.getType());
            jsonObject.put("modulCode", "1002");
            //推送到日志MQ平台
            SendResult sendResult = rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+sendResult+"==");
            if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            	 LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
			}
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
            LOGGER.info("==MQ 后置通知 异常==");
        } catch (Exception ex) {
        	ex.printStackTrace();
            //记录本地异常日志
            LOGGER.error("==异常通知异常== 结束 ");
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
    public  static String getControllerMethodModel(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String model = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    model = method.getAnnotation(SystemServiceLog. class).model();
                    break;
                }
            }
        }
        return model;
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
        String point = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    point = method.getAnnotation(SystemServiceLog. class).point();
                    break;
                }
            }
        }
        return point;
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
        String keyword = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    keyword = method.getAnnotation(SystemServiceLog. class).keyword();
                    break;
                }
            }
        }
        return keyword;
    }


}